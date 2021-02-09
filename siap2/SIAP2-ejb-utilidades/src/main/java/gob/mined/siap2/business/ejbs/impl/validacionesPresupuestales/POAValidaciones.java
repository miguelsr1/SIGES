/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl.validacionesPresupuestales;

import gob.mined.siap2.business.ejbs.DatosUsuario;
import gob.mined.siap2.business.ejbs.UsuarioBean;
import gob.mined.siap2.business.utils.FlujoCajaMensualUtils;
import gob.mined.siap2.business.utils.ProyectoUtils;
import gob.mined.siap2.business.utils.UnidadTecnicaUtils;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumoFlujoCajaMensual;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTramoTramo;
import gob.mined.siap2.entities.data.impl.ProyectoCategoriaConvenio;
import gob.mined.siap2.entities.data.impl.ProyectoComponente;
import gob.mined.siap2.entities.data.impl.ProyectoEstPorcentajeFuente;
import gob.mined.siap2.entities.data.impl.ProyectoEstructura;
import gob.mined.siap2.entities.data.impl.ProyectoFuente;
import gob.mined.siap2.entities.data.impl.ProyectoMacroActividad;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoPOA;
import gob.mined.siap2.entities.enums.TipoAporteProyecto;
import gob.mined.siap2.entities.enums.TipoEstructura;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.persistence.dao.imp.POADAO;
import gob.mined.siap2.persistence.dao.imp.ProyectoDAO;
import gob.mined.siap2.persistence.dao.imp.UsuarioDAO;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase incluye los métodos de validación de los POA desde el punto de
 * vista de reglas de negocio.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "POAValidaciones")
@LocalBean
public class POAValidaciones {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private ProyectoDAO proyectoDAO;
    @Inject
    @JPADAO
    private POADAO poadao;
    @Inject
    @JPADAO
    private UsuarioDAO usuarioDAO;
    @Inject
    private DatosUsuario datosUsuario;
    @Inject
    private UsuarioBean usuarioBean;

    /**
     * Chequea si un usuario tiene permiso de edición sobre un POA
     *
     * @param poa
     * @return
     */
    public boolean tienePermisoEdicionPOA(POAProyecto poa) {
        if (poa.getEstado() == EstadoPOA.EDITANDO_UT) {
            boolean encontro = false;
            //el usuario tiene que pertenecer a la ut

            //las ut que tiene el usuario
            List<UnidadTecnica> utDelUsuario = usuarioBean.getUTDelUsuario();
            if (UnidadTecnicaUtils.tieneAccesoAUT(utDelUsuario, poa.getUnidadTecnica())) {
                encontro = true;
            }
            //sino busca si la ut del usuario esta como colaboradora
            if (!encontro) {
                encontro = !proyectoDAO.esColaborador(utDelUsuario, poa.getId()).isEmpty();
            }

            return encontro;
        } else if (poa.getEstado() == EstadoPOA.VALIDANDO_POA) {
            String codigoUsuario = datosUsuario.getCodigoUsuario();
            //es presupuesto el que lo envia y el suauario tiene que tener la operacion de manear poa de accion central
            List l = usuarioDAO.getUsuariosConOperacion(ConstantesEstandares.Operaciones.OPERACION_PARA_VALIDAR_POA_PROYECTO, codigoUsuario);
            if (l.isEmpty()) {
                return false;
            }
            return true;
        } else if (poa.getEstado() == EstadoPOA.CONSOLIDANDO_POA) {
            String codigoUsuario = datosUsuario.getCodigoUsuario();
            List l = usuarioDAO.getUsuariosConOperacion(ConstantesEstandares.Operaciones.OPERACION_PARA_CONSOLIDAR_POA_PROYECTO, codigoUsuario);
            if (l.isEmpty()) {
                return false;
            }
            return true;
        }

        return false;
    }

    /**
     * Este método valida la estructura financiera del POA para edición
     *
     * @param idProyecto
     * @param poa
     */
    public void validarEstrucuraFinancieraPOAEdicionUT(Integer idProyecto, POAProyecto poa) {
        validarPOA(idProyecto, poa/*, false*/);
    }

    /**
     * Este método valida la estructura financiera de un POA para enviar.
     *
     * @param idProyecto
     * @param poa
     */
    public void validarEstrucuraFinancieraPOAParaEnviar(Integer idProyecto, POAProyecto poa) {
        validarPOA(idProyecto, poa/*, false*/);
    }

    /**
     * Este método valida la estructura financiera de un proyecto para
     * validación
     *
     * @param idProyecto
     * @param poa
     */
    public void validarEstrucuraFinancieraPOAParaValidacion(Integer idProyecto, POAProyecto poa) {
        validarPOA(idProyecto, poa/*, true*/);
    }

    /**
     * valida un POA cuando se duplican las lineas
     *
     * @param idProyecto
     * @param poa
     */
    public void validarEstructuraFinancieraPOAParaDuplicar(Integer idProyecto, POAProyecto poa) {
        validarPOA(idProyecto, poa/*, true*/);
    }

    /**
     * Operación auxiliar que se encarga de la validación de un POA
     *
     * @param poa POA a validar
     * @param exacto si es true si validaran que los montos coincidan
     * exactamente con lo asignado, si es false se valida que no sobrepase
     */
    private void validarPOA(Integer idProyecto, POAProyecto poa/*, boolean vaALineaBase*/) {
        Proyecto proyecto = (Proyecto) generalDAO.find(Proyecto.class, idProyecto);
        //CONTROL 0 valida los datos basicos de los insumos
        for (POLinea linea : poa.getLineas()) {
            for (POActividadBase actividad : linea.getActividades()) {
                for (POInsumos insumo : actividad.getInsumos()) {
                    validarDatosBasicosInsumo(insumo, linea);
                }
            }
        }

        //CONTROL 1
        //los techos se distribuyen desde programa, subprograma, proyecto, fuente de financiamiento
        //con validar que la suma por fuente de financiamiento de los insumos de todo el poa no supere ya estaria...
        //porque la otra validaciones se hacen al cargar los techos
        for (ProyectoFuente fuente : proyecto.getFuentesProyecto()) {
            BigDecimal techo = ProyectoUtils.getTechoFuenteParaAnio(fuente, poa.getAnioFiscal());
            BigDecimal usado = poadao.suparUsadoPorPOAsEnTrabajo(proyecto.getId(), poa.getAnioFiscal().getId(), fuente.getFuenteRecursos().getId(), poa.getId());
            if (usado == null) {
                usado = BigDecimal.ZERO;
            }
            //ahora suma lo usado por el poa actual
            for (POLinea linea : poa.getLineas()) {
                for (POActividadBase actividad : linea.getActividades()) {
                    for (POInsumos insumo : actividad.getInsumos()) {
                        for (POMontoFuenteInsumo montoAporte : insumo.getMontosFuentes()) {
                            if (montoAporte.getFuente().getFuenteRecursos().equals(fuente.getFuenteRecursos())) {
                                usado = usado.add(montoAporte.getMonto());
                            }
                        }
                    }
                }
            }
            if (usado.compareTo(techo) > 0) {
                BusinessException b = new BusinessException();
                String[] params = {
                    String.valueOf(usado),
                    fuente.getFuenteRecursos().getNombre(),
                    String.valueOf(techo)
                };
                b.addError(ConstantesErrores.ERR_LA_SUMA_DE_INSUMOS_0_EN_LA_FUENTE_1_SOBREPASA_EL_MONTO_ASIGNADO_2, params);
                throw b;
            }
        }

        //CONTROL 2
        //En los casos que el poa va para línea base (de validacion y consolidacion)
        //también se verifica que el poa a guardar no sobrepase el monto asignado en el proyecto
        for (ProyectoComponente componente : proyecto.getProyectoComponentes()) {
            verificarMontosParaPOAParaEstructura(proyecto, componente, poa);
        }
        for (ProyectoMacroActividad macroactividad : proyecto.getProyectoMacroactividad()) {
            verificarMontosParaPOAParaEstructura(proyecto, macroactividad, poa);
        }

        //CONTROL 3
        //valida que el insumo no sobrepase lo que tiene asignado el tramo   
        Map<ProyectoAporteTramoTramo, BigDecimal> mapTramos = new LinkedHashMap();
        //ahora suma lo usado por el poa actual agrupando por tramos
        for (POLinea linea : poa.getLineas()) {
            for (POActividadBase actividad : linea.getActividades()) {
                for (POInsumos insumo : actividad.getInsumos()) {
                    if (insumo.getTramo() != null) {
                        BigDecimal usadoEnTramo = mapTramos.get(insumo.getTramo());
                        if (usadoEnTramo == null) {
                            usadoEnTramo = BigDecimal.ZERO;
                        }
                        usadoEnTramo = usadoEnTramo.add(insumo.getMontoTotal());
                        mapTramos.put(insumo.getTramo(), usadoEnTramo);

                    }
                }
            }
        }
        //valida que la suma de los tramos no se pasen con lo que tiene establecido el tramo
        for (Map.Entry<ProyectoAporteTramoTramo, BigDecimal> entry : mapTramos.entrySet()) {
            BigDecimal totalUsadoEnTramo = poadao.suparUsadoEnTramoExluirPOA(entry.getKey().getId(), poa.getId());
            if (totalUsadoEnTramo == null) {
                totalUsadoEnTramo = BigDecimal.ZERO;
            }
            totalUsadoEnTramo = totalUsadoEnTramo.add(entry.getValue());

            if (entry.getKey().getMontoHasta().compareTo(totalUsadoEnTramo) < 0) {
                BusinessException b = new BusinessException();
                String[] params = {
                    entry.getKey().getCategoria().getCategoriaConvenio().getNombre(),
                    String.valueOf(entry.getKey().getMontoHasta()),
                    String.valueOf(totalUsadoEnTramo)
                };
                b.addError(ConstantesErrores.ERR_LA_CATEGORIA_0_EL_TRAMO_DE_HASTA_1_TIENE_USADO_2, params);
                throw b;
            }

        }

    }

    /**
     * Método que se encarga de realizar la validación al incluir un insumo en
     * un poa. Son los mismos controles de arribas solo que enfocados a un
     * insumo
     *
     * @param idProyecto
     * @param poa
     * @param insumo
     */
    public void validarInsumo(Integer idProyecto, Integer idPOA, Integer idLinea, POInsumos insumo) {
        Proyecto proyecto = (Proyecto) generalDAO.find(Proyecto.class, idProyecto);
        POAProyecto poa = (POAProyecto) generalDAO.find(POAProyecto.class, idPOA);
        POLinea linea = (POLinea) generalDAO.find(POLinea.class, idLinea);

        //VALIDA QUE LOS DATOS TENGAN VALORES
        //CONTROL 0 se valida que la suma de las fuentes no superen el monto del insumo
        validarDatosBasicosInsumo(insumo, linea);

        //CONTROL 2
        //valida que la distribucion de insumos no sobrepase la distribucion por fuente del componente
        for (ProyectoEstPorcentajeFuente montoEnestructura : linea.getProducto().getProyectoEstructura().getMontosFuentes()) {
            BigDecimal usado = poadao.totalizarAportesEnPOATrabajo(idProyecto,/* poa.getAnioFiscal().getId() ,*/ linea.getProducto().getProyectoEstructura().getId(), montoEnestructura.getFuente().getId(), insumo.getId());
            if (usado == null) {
                usado = BigDecimal.ZERO;
            }

            //suma el insumo actual
            for (POMontoFuenteInsumo montInsumo : insumo.getMontosFuentes()) {
                if (montInsumo.getFuente().equals(montoEnestructura.getFuente())) {
                    usado = usado.add(montInsumo.getMonto());
                }
            }

            //verifica si sobrepasa asi tira exepción
            if (usado.compareTo(montoEnestructura.getMonto()) > 0) {
                BusinessException b = new BusinessException();
                String[] params = {ProyectoUtils.getNombreEstructura(linea.getProducto().getProyectoEstructura()),
                    montoEnestructura.getFuente().getFuenteRecursos().getNombre() + " " + montoEnestructura.getFuente().getCategoriaConvenio().getCodigo(),
                    String.valueOf(usado),
                    String.valueOf(montoEnestructura.getMonto())
                };

                b.addError(ConstantesErrores.ERR_LA_DISTRIBUCION_DE_MONTOS_EN_LA_LINEA_0_SOBREPASA_EL_MONTO_ASIGNADO_PARA_LA_FUENTE_1_USADO_2_TOAL_3, params);
                throw b;
            }
        }

        //CONTROL 1
        //los techos se distribuyen desde programa, subprograma, proyecto, fuente de financiamiento
        //con validar que la suma por fuente de financiamiento de los insumos de todo el POA no supere ya estaria...
        //porque la otra validaciones se hacen al cargar los techos
        for (ProyectoFuente fuente : proyecto.getFuentesProyecto()) {
            BigDecimal usado = BigDecimal.ZERO;

            boolean contieneFuente = false;
            for (POMontoFuenteInsumo montoAporte : insumo.getMontosFuentes()) {
                if (montoAporte.getFuente().getFuenteRecursos().equals(fuente.getFuenteRecursos())) {
                    usado = usado.add(montoAporte.getMonto());
                    contieneFuente = true;
                }
            }

            //el error se da solo si el insumo es financiado por esa fuente
            if (contieneFuente) {
                BigDecimal techo = ProyectoUtils.getTechoFuenteParaAnio(fuente, poa.getAnioFiscal());

                BigDecimal aSumar = poadao.suparUsadoPorPOAsYExchulirInsumoEnTrabajo(idProyecto, poa.getAnioFiscal().getId(), fuente.getFuenteRecursos().getId(), insumo.getId());
                if (aSumar != null) {
                    usado = usado.add(aSumar);
                }

                if (usado.compareTo(techo) > 0) {
                    BusinessException b = new BusinessException();
                    String[] params = {
                        String.valueOf(usado),
                        fuente.getFuenteRecursos().getNombre(),
                        String.valueOf(techo)
                    };
                    b.addError(ConstantesErrores.ERR_LA_SUMA_DE_INSUMOS_0_EN_LA_FUENTE_1_SOBREPASA_EL_MONTO_ASIGNADO_2, params);
                    throw b;
                }

            }
        }

        //CONTROL 3
        //valida que el insumo no sobrepase lo que tiene asignado el tramo   
        CategoriaConvenio categoriaConvenio = ProyectoUtils.getCategoriaConvenio(linea.getProducto().getProyectoEstructura(), false);
        if (categoriaConvenio != null && categoriaConvenio.getTipo() == TipoAporteProyecto.POR_TRAMOS) {
            BigDecimal totalUsadoEnTramo = poadao.suparUsadoEnTramoExluirInsumo(insumo.getTramo().getId(), insumo.getId());
            if (totalUsadoEnTramo == null) {
                totalUsadoEnTramo = BigDecimal.ZERO;
            }
            totalUsadoEnTramo = totalUsadoEnTramo.add(insumo.getMontoTotal());

            if (insumo.getTramo().getMontoHasta().compareTo(totalUsadoEnTramo) < 0) {
                BusinessException b = new BusinessException();
                String[] params = {
                    insumo.getTramo().getCategoria().getCategoriaConvenio().getNombre(),
                    String.valueOf(insumo.getTramo().getMontoHasta()),
                    String.valueOf(totalUsadoEnTramo)
                };
                b.addError(ConstantesErrores.ERR_LA_CATEGORIA_0_EL_TRAMO_DE_HASTA_1_TIENE_USADO_2, params);
                throw b;
            }
        }

    }

    /**
     * Operación auxiliar que se usa para validar que se usaron todos los montos
     * de una estructura en el POG
     *
     * @param estructura
     * @param pog
     */
    private void verificarMontosParaPOAParaEstructura(Proyecto proyecto, ProyectoEstructura estructura, POAProyecto poa) {
        //hace la validacion solo si la estructura tiene productos
        if (!estructura.getProductos().isEmpty()) {
            //va a verificar para cada fuente que se cumpla la validacion
            for (ProyectoEstPorcentajeFuente montoFuenteEnEstructura : estructura.getMontosFuentes()) {
                BigDecimal usado = poadao.suparUsadoPorPOAsEnBase(proyecto.getId(), estructura.getId(), montoFuenteEnEstructura.getFuente().getId(), poa.getId());
                if (usado == null) {
                    usado = BigDecimal.ZERO;
                }
                // suma el poa a guardar
                for (POLinea iterLinea : poa.getLineas()) {
                    //se queda solo con las lineas de ese producto
                    if (iterLinea.getProducto().getProyectoEstructura().equals(estructura)) {
                        for (POActividadBase actividad : iterLinea.getActividades()) {
                            for (POInsumos insumo : actividad.getInsumos()) {
                                for (POMontoFuenteInsumo mi : insumo.getMontosFuentes()) {
                                    //se queda solo con los muntos de esa fuente
                                    if (mi.getFuente().equals(montoFuenteEnEstructura.getFuente())) {
                                        usado = usado.add(mi.getMonto());
                                    }
                                }
                            }
                        }
                    }
                }
                //si el monto sobrepasa lo asignado con lo usado 
                if (usado.compareTo(montoFuenteEnEstructura.getMonto()) > 0) {
                    String nombreEstructura = ProyectoUtils.getNombreEstructura(estructura);

                    BusinessException b = new BusinessException();
                    String[] params = {montoFuenteEnEstructura.getFuente().getFuenteRecursos().getNombre() + " " + montoFuenteEnEstructura.getFuente().getCategoriaConvenio().getNombre(), nombreEstructura};
                    b.addError(ConstantesErrores.ERR_LA_SUMA_DE_MONTOS_DISTINTO_PARA_FUENTES_0_EN_ESTRUCTURA_1, params);
                    throw b;
                }
            }
        }
    }

    /**
     * Operación auxiliar que se encarga de la validación de la estructura
     * financiera de los POA a consolidar
     *
     * @param poa POA a validar
     * @param exacto si es true si validaran que los montos coincidan
     * exactamente con lo asignado, si es false se valida que no sobrepase
     */
    public void validarEstrucuraFinancieraPOAParaConsolidacion(Integer idProyecto, AnioFiscal anioFiscal, List<POAProyecto> poas) {
        Proyecto proyecto = (Proyecto) generalDAO.find(Proyecto.class, idProyecto);

        //CONTROL 1
        //los techos se distribuyen desde programa, subprograma, proyecto, fuente de financiamiento
        //con validar que la suma por fuente de financiamiento de los insumos de todo el POA no supere ya estaria...
        //porque la otra validaciones se hacen al cargar los techos
        for (ProyectoFuente fuente : proyecto.getFuentesProyecto()) {
            BigDecimal techo = ProyectoUtils.getTechoFuenteParaAnio(fuente, anioFiscal);
            BigDecimal usado = BigDecimal.ZERO;

            for (POAProyecto poa : poas) {
                //ahora suma lo usado por el poa actual
                for (POLinea linea : poa.getLineas()) {
                    for (POActividadBase actividad : linea.getActividades()) {
                        for (POInsumos insumo : actividad.getInsumos()) {
                            for (POMontoFuenteInsumo montoAporte : insumo.getMontosFuentes()) {
                                if (montoAporte.getFuente().getFuenteRecursos().equals(fuente.getFuenteRecursos())) {
                                    usado = usado.add(montoAporte.getMonto());
                                }
                            }
                        }
                    }
                }
            }
            if (usado.compareTo(techo) > 0) {
                BusinessException b = new BusinessException();
                String[] params = {
                    String.valueOf(usado),
                    fuente.getFuenteRecursos().getNombre(),
                    String.valueOf(techo)
                };
                b.addError(ConstantesErrores.ERR_LA_SUMA_DE_INSUMOS_0_EN_LA_FUENTE_1_SOBREPASA_EL_MONTO_ASIGNADO_2, params);
                throw b;
            }
        }

        //CONTROL 2
        //En los casos que el poa va para línea base (de validacion y consolidacion)
        //también se verifica que el poa a guardar no sobrepase el monto asignado en el proyecto
        for (ProyectoComponente componente : proyecto.getProyectoComponentes()) {
            verificarMontosParaPOAParaEstructura(proyecto, componente, poas);
        }
        for (ProyectoMacroActividad macroactividad : proyecto.getProyectoMacroactividad()) {
            verificarMontosParaPOAParaEstructura(proyecto, macroactividad, poas);
        }

    }

    /**
     * Método que se encarga de validar los datos básicos de un insumo. Que los
     * datos de montos tengan sentido
     *
     * @param insumo
     * @param linea 
     */
    public static void validarDatosBasicosInsumo(POInsumos insumo, POLinea linea) {
        //VALIDA QUE LOS DATOS TENGAN VALORES
        if (insumo.getCantidad() == null || insumo.getCantidad().intValue() < 0) {
            BusinessException b = new BusinessException();
            String[] params = {insumo.getId() + " " + insumo.getInsumo().getNombre()};
            b.addError(ConstantesErrores.ERR_LA_CANTIDAD_DEL_INSUMO_0_DEBE_SER_UN_MONTO_MAYOR_IGUAL_A_CERO, params);
            throw b;
        }
        if (insumo.getMontoUnit() == null || insumo.getMontoUnit().compareTo(BigDecimal.ZERO) < 0) {
            BusinessException b = new BusinessException();
            String[] params = {insumo.getId() + " " + insumo.getInsumo().getNombre()};
            b.addError(ConstantesErrores.ERR_EL_MONTO_UNITARIO_DEL_INSUMO_0_DEBE_SER_UN_MONTO_MAYOR_IGUAL_A_CERO, params);
            throw b;
        }
        if (insumo.getMontoTotal() == null || insumo.getMontoTotal().compareTo(BigDecimal.ZERO) < 0) {
            BusinessException b = new BusinessException();
            String[] params = {insumo.getId() + " " + insumo.getInsumo().getNombre()};
            b.addError(ConstantesErrores.ERR_EL_MONTO_TOTAL_DEL_INSUMO_0_DEBE_SER_UN_MONTO_MAYOR_IGUAL_A_CERO, params);
            throw b;
        }

        //CONTROL 0 se valida que la suma de las fuentes no superen el monto del insumo
        //se preserva el monto total introducido a mano
        if (insumo.getMontosFuentes() != null && !insumo.getMontosFuentes().isEmpty()) {
            // if (insumo.getTipoMontoEstructura() == TipoMontoEstructura.IMPORTE) {
            BigDecimal montoTotal = BigDecimal.ZERO;
            for (POMontoFuenteInsumo mf : insumo.getMontosFuentes()) {
                montoTotal = montoTotal.add(mf.getMonto());
            }
            if (montoTotal.compareTo(insumo.getMontoTotal()) != 0) {
                //la distribucion por fuente no coincide cone l monto total del insumo
                BusinessException b = new BusinessException();
                String[] params = {insumo.getId() + " " + insumo.getInsumo().getNombre()};
                b.addError(ConstantesErrores.ERR_EN_EL_INSUMO_0_LAS_FUENTES_NO_SUMAN_SU_MONTO, params);
                throw b;
            }
        }

        //valida que si la categoria del insumo es por tramos tenga tramo seleccionado
        CategoriaConvenio categoriaConvenio = ProyectoUtils.getCategoriaConvenio(linea.getProducto().getProyectoEstructura(), false);
        if (categoriaConvenio != null && categoriaConvenio.getTipo() == TipoAporteProyecto.POR_TRAMOS) {
            if (insumo.getTramo() == null) {
                BusinessException b = new BusinessException();
                String[] params = {insumo.getId() + " " + insumo.getInsumo().getNombre(), categoriaConvenio.getNombre()};
                b.addError(ConstantesErrores.ERR_INSUMO_EN_LA_CATEGORIA_0_POR_TRAMOS_NO_TIENE_TRAMO_SELECIONADO, params);
                throw b;
            }
        }

        //valida que la suma de montos de los 12 meses del insumo sea igual al monto del insumo
        if (insumo.getNoUACI() != null && insumo.getNoUACI()) {
            BigDecimal montoTotalInsumo = insumo.getMontoTotal();
            if (montoTotalInsumo == null) {
                montoTotalInsumo = BigDecimal.ZERO;
            }
            BigDecimal montoTotal12Meses = BigDecimal.ZERO;
            for (FlujoCajaAnio fca : insumo.getFlujosDeCajaAnio()) {
                for (POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()) {
                    if (fcm.getMonto() != null) {
                        montoTotal12Meses = montoTotal12Meses.add(fcm.getMonto());
                    }
                }
            }
            if (montoTotalInsumo.compareTo(montoTotal12Meses) != 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_SUMA_MONTOS_INSUMO_EN_MESES_DIFERENTE_MONTO_INSUMO_TOTAL);
                throw b;
            }
        }

        //valida que la suma de montos de las fuentes por mes sea igual al monto del insumo para ese mes
        if (insumo.getNoUACI() != null && insumo.getNoUACI()) {
            for (FlujoCajaAnio fca : insumo.getFlujosDeCajaAnio()) {
                for (POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()) {
                    BigDecimal montoFuentesEnMes = BigDecimal.ZERO;
                    if (fcm.getMontosFuentesInsumosFCM() != null) {
                        for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteMes : fcm.getMontosFuentesInsumosFCM()) {
                            if (montoFuenteMes.getMonto() != null) {
                                montoFuentesEnMes = montoFuentesEnMes.add(montoFuenteMes.getMonto());
                            }
                        }
                    }
                    if (fcm.getMonto().compareTo(montoFuentesEnMes) != 0) {
                        BusinessException b = new BusinessException();
                        
                        String mes = FlujoCajaMensualUtils.getMapMesesDelAnio().get(fcm.getMes()+"");
                        String[] params = {mes};
                        b.addError(ConstantesErrores.ERR_SUMA_MONTOS_FUENTES_MES_1_DIFERENTE_MONTO_INSUMO_MES, params);
                        throw b;
                    }
                }
            }
        }

        //valida que la suma de cada fuente en los 12 meses no supere el monto asignado a la fuente para ese insumo
        if (insumo.getNoUACI() != null && insumo.getNoUACI()) {
            for (POMontoFuenteInsumo montoFuente : insumo.getMontosFuentes()) {
                BigDecimal montoFuenteInsumo = montoFuente.getMonto();
                BigDecimal totalMontoFuenteEn12Meses = BigDecimal.ZERO;
                for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : montoFuente.getMontosFuentesInsumosFCM()) {
                    if (montoFuenteFCM.getMonto() != null) {
                        if (montoFuenteFCM.getMonto().compareTo(BigDecimal.ZERO) < 0) {
                            BusinessException b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_MONTO_FUENTE_MES_INCORRECTO);
                            throw b;
                        }
                        totalMontoFuenteEn12Meses = totalMontoFuenteEn12Meses.add(montoFuenteFCM.getMonto());
                    }

                }
                if (totalMontoFuenteEn12Meses.compareTo(montoFuenteInsumo) > 0) {
                    BusinessException b = new BusinessException();
                    String[] params = {montoFuente.getFuente().getFuenteRecursos().getCodigo()};
                    b.addError(ConstantesErrores.ERR_SUMA_MONTOS_FUENTE_1_SUPERA_MONTO_ASIGNADO_A_FUENTE_EN_INSUMO, params);
                    throw b;
                }
            }
        }

    }

    /**
     * Operación auxiliar que se usa para validar que se usaron todos los montos
     * de una estructura en el pog
     *
     * @param estructura
     * @param pog
     */
    private void verificarMontosParaPOAParaEstructura(Proyecto proyecto, ProyectoEstructura estructura, List<POAProyecto> poas) {
        //hace la validacion solo si la estructura tiene productos
        if (!estructura.getProductos().isEmpty()) {
            //va a verificar para cada fuente que se cumpla la validacion
            for (ProyectoEstPorcentajeFuente montoFuenteEnEstructura : estructura.getMontosFuentes()) {
                BigDecimal usado = BigDecimal.ZERO;

                for (POAProyecto poa : poas) {
                    // suma el poa a guardar
                    for (POLinea iterLinea : poa.getLineas()) {
                        //se queda solo con las lineas de ese producto
                        if (iterLinea.getProducto().getProyectoEstructura().equals(estructura)) {
                            for (POActividadBase actividad : iterLinea.getActividades()) {
                                for (POInsumos insumo : actividad.getInsumos()) {
                                    for (POMontoFuenteInsumo mi : insumo.getMontosFuentes()) {
                                        //se queda solo con los muntos de esa fuente
                                        if (mi.getFuente().equals(montoFuenteEnEstructura.getFuente())) {
                                            usado = usado.add(mi.getMonto());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //si el monto sobrepasa lo asignado con lo usado 
                if (usado.compareTo(montoFuenteEnEstructura.getMonto()) > 0) {
                    String nombreEstructura = null;
                    if (estructura.getTipo() == TipoEstructura.COMPONENTE) {
                        nombreEstructura = ((ProyectoComponente) estructura).getNombre();
                    } else if (((ProyectoMacroActividad) estructura).getMacroActividad() != null) {
                        nombreEstructura = ((ProyectoMacroActividad) estructura).getMacroActividad().getNombre();
                    }
                    BusinessException b = new BusinessException();
                    String[] params = {montoFuenteEnEstructura.getFuente().getFuenteRecursos().getNombre() + " " + montoFuenteEnEstructura.getFuente().getCategoriaConvenio().getNombre(), nombreEstructura};
                    b.addError(ConstantesErrores.ERR_LA_SUMA_DE_MONTOS_DISTINTO_PARA_FUENTES_0_EN_ESTRUCTURA_1, params);
                    throw b;
                }
            }
        }
    }

    /**
     * Función que se usa para validar la construcción de un proyecto que esta
     * para cerrase
     *
     * @param proyecto
     */
    public void validarEnConstruccionParaCerrarse(Proyecto proyecto) {
        //CONTROL 2
        //En los casos que el poa va para línea base (de validacion y consolidacion)
        //también se verifica que el poa a guardar no sobrepase el monto asignado en el proyecto
        for (ProyectoComponente componente : proyecto.getProyectoComponentes()) {
            verificarMontosParaPOAParaEstructuraEnConstruccion(proyecto, componente);
        }
        for (ProyectoMacroActividad macroactividad : proyecto.getProyectoMacroactividad()) {
            verificarMontosParaPOAParaEstructuraEnConstruccion(proyecto, macroactividad);
        }

        //CONTROL 3
        //valida que el insumo no sobrepase lo que tiene asignado el tramo   
        for (ProyectoCategoriaConvenio cat : proyecto.getDistribuccionCategorias()) {
            if (cat.getCategoriaConvenio().getTipo() == TipoAporteProyecto.POR_TRAMOS) {
                for (ProyectoAporteTramoTramo tramo : cat.getTramos()) {
                    BigDecimal totalUsadoEnTramo = poadao.suparUsadoEnTramoExluirPOA(tramo.getId(), null);
                    if (totalUsadoEnTramo == null) {
                        totalUsadoEnTramo = BigDecimal.ZERO;
                    }

                    if (tramo.getMontoHastaEnConstruccion().compareTo(totalUsadoEnTramo) < 0) {
                        BusinessException b = new BusinessException();
                        String[] params = {
                            tramo.getCategoria().getCategoriaConvenio().getNombre(),
                            String.valueOf(tramo.getMontoHastaEnConstruccion()),
                            String.valueOf(totalUsadoEnTramo)
                        };
                        b.addError(ConstantesErrores.ERR_LA_CATEGORIA_0_EL_TRAMO_DE_HASTA_1_TIENE_USADO_2, params);
                        throw b;
                    }
                }
            }
        }

    }

    /**
     * Método auxiliar que se usa para validar que la distribución de los montos
     * en construcción de las fuentes se pueda aplicar
     *
     * @param proyecto
     * @param estructura
     */
    private void verificarMontosParaPOAParaEstructuraEnConstruccion(Proyecto proyecto, ProyectoEstructura estructura) {
        for (ProyectoEstPorcentajeFuente montoFuenteEnEstructura : estructura.getMontosFuentes()) {
            BigDecimal usado = poadao.suparUsadoPorPOAsEnBase(proyecto.getId(), estructura.getId(), montoFuenteEnEstructura.getFuente().getId(), null);
            //si el monto sobrepasa lo asignado con lo usado 
            if (montoFuenteEnEstructura != null && usado != null && montoFuenteEnEstructura.getMontoEnConstruccion() != null && usado.compareTo(montoFuenteEnEstructura.getMontoEnConstruccion()) > 0) {
                String nombreEstructura = ProyectoUtils.getNombreEstructura(estructura);

                BusinessException b = new BusinessException();
                String[] params = {montoFuenteEnEstructura.getFuente().getFuenteRecursos().getNombre() + " " + montoFuenteEnEstructura.getFuente().getCategoriaConvenio().getNombre(), nombreEstructura};
                b.addError(ConstantesErrores.ERR_LA_SUMA_DE_MONTOS_DISTINTO_PARA_FUENTES_0_EN_ESTRUCTURA_1, params);
                throw b;
            }
        }
    }

}
