/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoAporte;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTramoTramo;
import gob.mined.siap2.entities.data.impl.ProyectoCategoriaConvenio;
import gob.mined.siap2.entities.data.impl.ProyectoComponente;
import gob.mined.siap2.entities.data.impl.ProyectoEstPorcentajeFuente;
import gob.mined.siap2.entities.data.impl.ProyectoEstProducto;
import gob.mined.siap2.entities.data.impl.ProyectoEstructura;
import gob.mined.siap2.entities.data.impl.ProyectoFuente;
import gob.mined.siap2.entities.data.impl.ProyectoMacroActividad;
import gob.mined.siap2.entities.data.impl.ProyectoParipassuTramo;
import gob.mined.siap2.entities.enums.TipoAporteProyecto;
import gob.mined.siap2.entities.enums.TipoParipassu;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase implementa validaciones de proyecto.
 * @author Sofis Solutions
 */
public class ProyectoValidacion {
    
        private static final Logger logger = Logger.getLogger(ProyectoValidacion.class.getName());

    /**
     * Este método realiza las validaciones de la sección datos generales de un proyecto.
     * @param proyecto 
     */
    public static void validarDatosGenerales(Proyecto proyecto) {
        if (proyecto.getFin() != null && proyecto.getInicio() != null) {
            if (proyecto.getFin().compareTo(proyecto.getInicio()) < 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_FECHA_INICIO_MAYOR_QUE_FIN);
                throw b;
            }
        }
    }

    /**
     * Método que se encarga de realizar la validación para el paripassu de una
     * categoría verifica que todos los aportes dentro de esa categoría estén
     * correctos
     *
     * @param tmpProyCategoriaConvenio
     * @param tmpFuentesParipassu
     */
    public static void validarParipassu(ProyectoCategoriaConvenio categoria, List<ProyectoAporte> tmpFuentesParipassu) {
        BigDecimal totalPorcentaje = BigDecimal.ZERO;
        for (ProyectoAporte aporte : tmpFuentesParipassu) {
            if (categoria.getCategoriaConvenio().equals(aporte.getCategoriaConvenio())) {
                //en caso de que sea por porcentaje 
                if (categoria.getTipoParipassuEnConstruccion() == TipoParipassu.PORCENTAJE) {
                    if (aporte.getMontoParipassuEnConstruccion() == null) {
                        aporte.setMontoParipassuEnConstruccion(BigDecimal.ZERO);
                    }
                    totalPorcentaje = totalPorcentaje.add(aporte.getMontoParipassuEnConstruccion());
                } else {
                    aporte.setMontoParipassuEnConstruccion(null);
                }

                // en caso de que sea por formula
                if (categoria.getTipoParipassuEnConstruccion() == TipoParipassu.FORMULA) {
                    //fixme: validar consistencia de la formula
                } else {
                    aporte.setFormulaParipassuEnConstruccion(null);
                }
            }
        }

        if (categoria.getTipoParipassuEnConstruccion() == TipoParipassu.PORCENTAJE) {
            if (totalPorcentaje.compareTo(NumberUtils.CIEN) != 0) {
                BusinessException b = new BusinessException();
                String[] params = {categoria.getCategoriaConvenio().getCodigo()};
                b.addError(ConstantesErrores.ERR_EL_PORCENTAJE_DEL_PARIPASSU_EN_LA_CATEGORIA_0_DISTINTO_DE_CIEN, params);
                throw b;
            }
        }
    }

    /**
     * valida la distribución ("paripassu") de los tramos en una categoría
     * supone que la categoría es por tramos
     *
     * @param categoria
     */
    public static void validarDistribucionTramosEnCategoria(ProyectoCategoriaConvenio categoria) {
        if (categoria.getTipoParipassuEnConstruccion() == TipoParipassu.PORCENTAJE) {
            for (ProyectoAporteTramoTramo tramo : categoria.getTramos()) {
                BigDecimal total = BigDecimal.ZERO;
                for (ProyectoParipassuTramo paripassu : tramo.getParipassus()) {
                    if (paripassu.getMontoParipassuEnConstruccion() != null) {
                        total = total.add(paripassu.getMontoParipassuEnConstruccion());
                    }
                }
                if (total.compareTo(NumberUtils.CIEN) > 0) {
                    BusinessException b = new BusinessException();
                    String[] params = {categoria.getCategoriaConvenio().getCodigo()};
                    b.addError(ConstantesErrores.ERR_EN_LA_CATEGORIA_0_SU_DISTRIBUCION_POR_TRAMOS_SUPERA_100, params);
                    throw b;
                }
            }
        }
    }

    /**
     * Método que se encarga de realizar la validación para todo lo que se
     * encuentra dentro de la pestaña de financiación
     *
     * @param proyecto
     */
    public static void validarFinanciacion(Proyecto proyecto) {
        //valida que una fuente no este financiando dos veces
        List<Integer> idFuentesRecursos = new LinkedList();
        for (ProyectoFuente f : proyecto.getFuentesProyecto()) {
            if (idFuentesRecursos.contains(f.getFuenteRecursos().getId())) {
                BusinessException b = new BusinessException();
                String[] params = {f.getFuenteRecursos().getNombre()};
                b.addError(ConstantesErrores.ERR_LA_FUENTE_DE_RECURSOS_0_ESTA_FINANCIANDO_DOS_VECES_EN_EL_PROYECTO, params);
                throw b;
            }
            idFuentesRecursos.add(f.getFuenteRecursos().getId());
        }

        //valida que las fuentes no se pasaen del monto del proyecto 
        if (proyecto.getMontoGlobalEnConstruccion().compareTo(BigDecimal.ZERO) != 0) {
            //valida las fuentes
            BigDecimal totalFuentes = BigDecimal.ZERO;
            for (ProyectoFuente fuente : proyecto.getFuentesProyecto()) {
                if (fuente.getMonto() != null) {
                    totalFuentes = totalFuentes.add(fuente.getMonto());
                }
            }
            if (totalFuentes.compareTo(proyecto.getMontoGlobalEnConstruccion()) > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_LA_DISTRIBUCION_POR_FUENTES_SUPERA_MONTO_DEL_PROYECTO);
                throw b;
            }
            //valida las categorias
            BigDecimal totalCategorias = BigDecimal.ZERO;
            for (ProyectoCategoriaConvenio iter : proyecto.getDistribuccionCategorias()) {
                //reorganiza los tramos
                if (iter.getCategoriaConvenio().getTipo() == TipoAporteProyecto.POR_TRAMOS) {
                    ProyectoUtils.reorganizarAporteTramo(iter);

                }
                if (iter.getMonto() != null) {
                    totalCategorias = totalCategorias.add(iter.getMonto());
                }
            }
            if (totalCategorias.compareTo(proyecto.getMontoGlobalEnConstruccion()) > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_LA_DISTRIBUCION_POR_CATEGORIAS_SUPERA_MONTO_DEL_PROYECTO);
                throw b;
            }

      
            //valida los aportes no superen los montos topes de las fuentes
            for (ProyectoFuente fuente : proyecto.getFuentesProyecto()) {
                BigDecimal total = BigDecimal.ZERO;
                for (ProyectoAporte f : proyecto.getAportesProyecto()) {
                    if (f.getFuenteRecursos() != null && f.getFuenteRecursos().equals(fuente.getFuenteRecursos())) {
                        if (f.getMonto() != null) {
                            total = total.add(f.getMonto());
                        }
                    }
                }
                if (total.compareTo(fuente.getMonto()) > 0) {
                    BusinessException b = new BusinessException();
                    String[] params = {fuente.getFuenteRecursos().getNombre()};
                    b.addError(ConstantesErrores.ERR_LA_DISTRIBUCION_DE_APORTES_SUPERA_MONTO_PARA_FUENTE_RECURSOS_0, params);
                    throw b;
                }
            }

        }

        //valida que se cumplan los parippasu
        for (ProyectoCategoriaConvenio categoria : proyecto.getDistribuccionCategorias()) {
            if (categoria.getCategoriaConvenio().getTipo() == TipoAporteProyecto.POR_PORCENTAJE) {
                validarParipassu(categoria, proyecto.getAportesProyecto());
            } else {
                validarDistribucionTramosEnCategoria(categoria);
            }
        }
    }

    
    /**
     * Este método valida la estructura de un proyecto.
     * @param proyecto 
     */
    public static void validarEstructura(Proyecto proyecto) {
        //vacia la estructura en caso que se cambie
        if (Boolean.FALSE.equals(proyecto.getTipoEstructuraComponente())) {
            proyecto.setProyectoComponentes(new LinkedList());
        }
        if (Boolean.FALSE.equals(proyecto.getTipoEstructuraMacroactividad())) {
            proyecto.setProyectoMacroactividad(new LinkedList());
        }

        if ((proyecto.getTipoEstructuraComponente() == null || proyecto.getTipoEstructuraComponente().equals(Boolean.FALSE))
            && (proyecto.getTipoEstructuraMacroactividad() == null || proyecto.getTipoEstructuraMacroactividad().equals(Boolean.FALSE))) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_AL_MENOS_TIENE_QUE_SELECCIONAR_UN_TIPO_ESTRUCTURA);
            throw b;
        }

        BigDecimal montoTotal = BigDecimal.ZERO;
        //valida que los componentes tengan solo una unidad Tecnica
        for (ProyectoComponente componente : proyecto.getProyectoComponentes()) {
            if (componente.getUnidadTecnica() == null) {
                //en la rama no hay componente si ut
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTE_COMPONENTE_SIN_UNIDAD_T);
                throw b;
            }
            BigDecimal montosHijos = BigDecimal.ZERO;
            for (ProyectoComponente hijo : componente.getComponenteHijos()) {
                montosHijos = montosHijos.add(hijo.getImporte());
            }
            if (montosHijos.compareTo(componente.getImporte()) > 0) {
                BusinessException b = new BusinessException();
                String[] params = {componente.getNombre()};
                b.addError(ConstantesErrores.ERR_LA_SUMA_DE_SUBCOMPONENTES_SOBREPASA_EL_COMPONENTE_0, params);
                throw b;
            }

            validarAportesEnEstructura(componente);

            if (componente.getComponentePadre() == null) {
                montoTotal = montoTotal.add(componente.getImporte());
            }
        }
        //valida que las macroactividades tengan solo una unidad Tecnica
        for (ProyectoMacroActividad macroActividad : proyecto.getProyectoMacroactividad()) {
            if (macroActividad.getUnidadTecnica() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTE_MACROACTIVIDAD_SIN_UNIDAD_T);
                throw b;
            }
            validarAportesEnEstructura(macroActividad);
            montoTotal = montoTotal.add(macroActividad.getImporte());
        }

        if (montoTotal.compareTo(proyecto.getMontoGlobalEnConstruccion()) > 0) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SUMA_DE_COMPONENTES_MACROACTIVIDADES_SOBREPASA_MONTO_PROYECTO);
            throw b;
        }

        /**
         * valida que los montos de las fuentes y porcentaje concuerden
         *
         */
        for (ProyectoAporte fuente : proyecto.getAportesProyecto()) {
            //valida la estructura para las fuentes
            //componente
            BigDecimal montoUsadoAporte = BigDecimal.ZERO;
            for (ProyectoComponente componente : proyecto.getProyectoComponentes()) {
                //OBTIENE CUANTO TIENE ASIGNADO EL COMPONENTE PARA ESA FUENTE
                BigDecimal montoUsadoComponente = BigDecimal.ZERO;
                for (ProyectoEstPorcentajeFuente montoFuente : componente.getMontosFuentes()) {
                    if (fuente.equals(montoFuente.getFuente())) {
                        montoUsadoComponente = montoUsadoComponente.add(montoFuente.getMontoEnConstruccion());
                    }
                }
                //verifica que la suma de los subcomponentes no sobrepase el componente
                BigDecimal usadoPorSubComponentes = BigDecimal.ZERO;
                for (ProyectoComponente subComponente : componente.getComponenteHijos()) {
                    for (ProyectoEstPorcentajeFuente montoFuente : subComponente.getMontosFuentes()) {
                        if (fuente.equals(montoFuente.getFuente())) {
                            usadoPorSubComponentes = usadoPorSubComponentes.add(montoFuente.getMontoEnConstruccion());
                        }
                    }
                }
                if (montoUsadoComponente.compareTo(usadoPorSubComponentes) < 0) {
                    BusinessException b = new BusinessException();
                    String[] params = {componente.getNombre(), fuente.getFuenteRecursos().getNombre() + " " + fuente.getCategoriaConvenio().getNombre()};
                    b.addError(ConstantesErrores.ERR_LA_SUMA_DE_SUBCOMPONENTES_PARA_EL_COMPONENTE_0_SOBREPASA_EN_LA_FUENTE_1, params);
                    throw b;
                }

                if (componente.getComponentePadre() == null) {
                    montoUsadoAporte = montoUsadoAporte.add(montoUsadoComponente);
                }
            }

            //macoActividades
            //BigDecimal montoMacroActividad = BigDecimal.ZERO;
            for (ProyectoMacroActividad m : proyecto.getProyectoMacroactividad()) {
                for (ProyectoEstPorcentajeFuente montoFuente : m.getMontosFuentes()) {
                    if (montoFuente.getFuente() == fuente || (fuente.getId() != null && fuente.equals(montoFuente.getFuente()))) {
                        montoUsadoAporte = montoUsadoAporte.add(montoFuente.getMontoEnConstruccion());
                    }
                }
            }

            if (fuente.getMonto().compareTo(montoUsadoAporte) < 0) {
                BusinessException b = new BusinessException();
                String[] params = {fuente.getFuenteRecursos().getNombre() + " " + fuente.getCategoriaConvenio().getNombre()};
                b.addError(ConstantesErrores.ERR_COMPONETNES_Y_MACROACTIVIDADES_SOBREPASAN_MONTOS_PARA_LA_FUENTE_0, params);
                throw b;
            }
        }
    }

    /**
     * Este método valida los aportes por fuente de recurso en la estructura del proyecto.
     * @param estructura 
     */
    public static void validarAportesEnEstructura(ProyectoEstructura estructura) {
        //valida que sumen lo mismo que el monto de la estructura
        BigDecimal montoSuma = BigDecimal.ZERO;

        CategoriaConvenio cat = null;     
        for (ProyectoEstPorcentajeFuente fuente : estructura.getMontosFuentes()) {
            //se setea en 0 si tiene motno null
            if (!ProyectoUtils.aporteEnEstructuraVacio(fuente, true)) {
                montoSuma = montoSuma.add(fuente.getMontoEnConstruccion());
                if (cat == null) {
                    //se queda con la primer categoria
                    cat = fuente.getFuente().getCategoriaConvenio();
                } else //valida que no exista una cagegoria distinta
                //esta validación se hace solo si tiene algun producto el componente
                if (!estructura.getProductos().isEmpty()) {
                    if (!cat.equals(fuente.getFuente().getCategoriaConvenio())) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_EXISTEN_MOTNOS_QUE_PERTENECEN_A_DISTINTAS_CATEGORIAS_EN_LA_FINANCIACION_DE_LA_ESTRUCTURA);
                        throw b;
                    }
                }
            }
        }
             
        if (montoSuma.compareTo(estructura.getImporte()) != 0) {
            BusinessException b = new BusinessException();
            String[] params = {ProyectoUtils.getNombreEstructura(estructura)};
            b.addError(ConstantesErrores.ERR_DISTRIBUCCION_POR_FUENTES_SUMA_DISTINTO_MONTO_EN_ESTRUCTURA_0, params);
            throw b;

        }

    }

 
    /**
     * Este método implementa la validación para el cierre de la ficha de formulación del proyecto.
     * @param proyecto 
     */
    public static void validarCierreProyecto(Proyecto proyecto) {
        //valida que no existan nodos sin productos
        for (ProyectoMacroActividad macroactividad : proyecto.getProyectoMacroactividad()) {
            if (macroactividad.getProductos().isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {macroactividad.getMacroActividad().getNombre()};
                b.addError(ConstantesErrores.ERR_LA_MACROACTICIDAD_0_NO_TIENE_PRODUCTOS, params);
                throw b;

            }
        }
        for (ProyectoComponente componente : proyecto.getProyectoComponentes()) {
            if (componente.getProductos().isEmpty() && componente.getComponenteHijos().isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {componente.getNombre()};
                b.addError(ConstantesErrores.ERR_EL_COMPONENTE_0_NO_TIENE_PRODUCTOS, params);
                throw b;

            }
            if (!componente.getProductos().isEmpty() && !componente.getComponenteHijos().isEmpty()){
                BusinessException b = new BusinessException();
                String[] params = {componente.getNombre()};
                b.addError(ConstantesErrores.ERR_EL_COMPONENTE_0_TIENE_ASOCIADO_PRODUCTOS_Y_SUBCOMPONENTES_SOLO_SE_ADMITEN_UNO_DE_LOS_DOS, params);
                throw b;                
            }
        }

        //valida que tenga programa presupuestario asociado
        if (proyecto.getProgramaPresupuestario() == null) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EL_PARA_CERRAR_EL_PROYECTO_ES_NECESARIO_TENER_SUB_PRES_ASOCIADO);
            throw b;
        }

        //valida las fuentes USEN TODO EL TOTAL
        BigDecimal totalFuentes = BigDecimal.ZERO;
        for (ProyectoFuente fuente : proyecto.getFuentesProyecto()) {
            if (fuente.getMonto() != null) {
                totalFuentes = totalFuentes.add(fuente.getMonto());
            }
        }
        if (totalFuentes.compareTo(proyecto.getMontoGlobalEnConstruccion()) != 0) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_PARA_CERRAR_EL_PROYECTO_LA_DIST_POR_FUENTES_DEBEN_FINCANCIAR_EXACATAMETNE_EL_MONTO_GLOBAL);
            throw b;
        }
        //valida las categorias usen todo el total
        BigDecimal totalCategorias = BigDecimal.ZERO;
        for (ProyectoCategoriaConvenio iter : proyecto.getDistribuccionCategorias()) {
            if (iter.getMonto() != null) {
                totalCategorias = totalCategorias.add(iter.getMonto());
            } else {
                iter.setMonto(BigDecimal.ZERO);
            }
        }

        if (totalCategorias.compareTo(proyecto.getMontoGlobalEnConstruccion()) != 0) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_PARA_CERRAR_EL_PROYECTO_LA_DIST_POR_CATEGORIA_DEBE_FINANCIAR_EXACATAMETNE_EL_MONTO_GLOBAL);
            throw b;
        }

    
        //valida los aportes sean iguales a los montos topes de las categorias
        for (ProyectoCategoriaConvenio iter : proyecto.getDistribuccionCategorias()) {
            BigDecimal total = BigDecimal.ZERO;
            for (ProyectoAporte f : proyecto.getAportesProyecto()) {
                if (f.getCategoriaConvenio() != null && f.getCategoriaConvenio().equals(iter.getCategoriaConvenio())) {
                    if (f.getMonto() != null) {
                        total = total.add(f.getMonto());
                    }
                }
            }
            if (total.compareTo(iter.getMonto()) != 0) {
                BusinessException b = new BusinessException();
                String[] params = {iter.getCategoriaConvenio().getNombre()};
                b.addError(ConstantesErrores.ERR_LA_DISTRIBUCION_DE_APORTES_NO_FINANCIAN_EXACTAMENTE_LA_CATEGORIA_0, params);
                throw b;
            }
        }
        //valida los aportes sean iguales a los montos topes de las fuentes
        for (ProyectoFuente fuente : proyecto.getFuentesProyecto()) {
            BigDecimal total = BigDecimal.ZERO;
            for (ProyectoAporte f : proyecto.getAportesProyecto()) {
                if (f.getFuenteRecursos() != null && f.getFuenteRecursos().equals(fuente.getFuenteRecursos())) {
                    if (f.getMonto() != null) {
                        total = total.add(f.getMonto());
                    }
                }
            }
            if (total.compareTo(fuente.getMonto()) != 0) {
                BusinessException b = new BusinessException();
                String[] params = {fuente.getFuenteRecursos().getNombre()};
                b.addError(ConstantesErrores.ERR_LA_DISTRIBUCION_DE_APORTES_NO_FINANCIA_EXACTAMENTE_FUENTE_RECURSOS_0, params);
                throw b;
            }
        }

        //valida los totales de la estructura
        BigDecimal montoTotal = BigDecimal.ZERO;
        for (ProyectoComponente componente : proyecto.getProyectoComponentes()) {
            //valida los productos tengan componente
            for (ProyectoEstProducto producto : componente.getProductos()) {
                if (producto.getUnidadTecnica() == null) {
                    BusinessException b = new BusinessException();
                    String[] params = {componente.getNombre()};
                    b.addError(ConstantesErrores.ERR_PARA_CERRAR_PROYECTO_ASIGNE_UT_AL_COMPONENTE_0, params);
                    throw b;
                }
            }

            if (!componente.getComponenteHijos().isEmpty()) {
                BigDecimal montosHijos = BigDecimal.ZERO;
                for (ProyectoComponente hijo : componente.getComponenteHijos()) {
                    montosHijos = montosHijos.add(hijo.getImporte());
                }
                if (montosHijos.compareTo(componente.getImporte()) != 0) {
                    BusinessException b = new BusinessException();
                    String[] params = {componente.getNombre()};
                    b.addError(ConstantesErrores.ERR_PARA_CERAR_EL_PROYECTO_LA_SUMA_DE_SUBCOMPONENTES_DEBE_SER_IGAUL_AL_COMPONENTE_0, params);
                    throw b;
                }
            }
            if (componente.getComponentePadre() == null) {
                montoTotal = montoTotal.add(componente.getImporte());
            }

        }
        for (ProyectoMacroActividad macroActividad : proyecto.getProyectoMacroactividad()) {
            //valida los productos tengan componente
            for (ProyectoEstProducto producto : macroActividad.getProductos()) {
                if (producto.getUnidadTecnica() == null) {
                    BusinessException b = new BusinessException();
                    String[] params = {macroActividad.getMacroActividad().getNombre()};
                    b.addError(ConstantesErrores.ERR_PARA_CERRAR_PROYECTO_ASIGNE_UT_A_LA_MACROACTIVIDAD_0, params);
                    throw b;
                }
            }
            montoTotal = montoTotal.add(macroActividad.getImporte());
        }

        if (montoTotal.compareTo(proyecto.getMontoGlobalEnConstruccion
    ()) != 0) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_PARA_CERRAR_EL_PROYECTO_LA_SUMA_DE_COMPONENTES_Y_MACROACTIVIDADES_DEBE_SER_IGUAL_AL_MONTO_DEL_PROYECTO);
            throw b;
        }

        /**
         * valida que los montos de las fuentes y porcentaje concuerden
         *
         */
        for (ProyectoAporte fuente : proyecto.getAportesProyecto()) {
            //valida la estructura para las fuentes
            //componente
            BigDecimal montoUsadoAporte = BigDecimal.ZERO;
            for (ProyectoComponente componente : proyecto.getProyectoComponentes()) {
                //OBTIENE CUANTO TIENE ASIGNADO EL COMPONENTE PARA ESA FUENTE
                BigDecimal montoUsadoComponente = BigDecimal.ZERO;
                for (ProyectoEstPorcentajeFuente montoFuente : componente.getMontosFuentes()) {
                    if (fuente.equals(montoFuente.getFuente())) {
                        montoUsadoComponente = montoUsadoComponente.add(montoFuente.getMontoEnConstruccion());
                    }
                }
                //verifica que la suma de los subcomponentes no sobrepase el componente
                if (!componente.getComponenteHijos().isEmpty()) {
                    BigDecimal usadoPorSubComponentes = BigDecimal.ZERO;
                    for (ProyectoComponente subComponente : componente.getComponenteHijos()) {
                        for (ProyectoEstPorcentajeFuente montoFuente : subComponente.getMontosFuentes()) {
                            if (fuente.equals(montoFuente.getFuente())) {
                                usadoPorSubComponentes = usadoPorSubComponentes.add(montoFuente.getMontoEnConstruccion());
                            }
                        }
                    }
                    if (montoUsadoComponente.compareTo(usadoPorSubComponentes) != 0) {
                        BusinessException b = new BusinessException();
                        String[] params = {componente.getNombre(), fuente.getFuenteRecursos().getNombre() + " " + fuente.getCategoriaConvenio().getNombre()};
                        b.addError(ConstantesErrores.ERR_PARA_CERRAR_EL_PROYECTO_EN_EL_COMPONENTE_0_LA_SUMA_DE_SUBCOMOPNENTES_TIENE_QUE_SER_IGAL_EN_LA_FUENTE_1, params);
                        throw b;
                    }
                }

                if (componente.getComponentePadre() == null) {
                    montoUsadoAporte = montoUsadoAporte.add(montoUsadoComponente);
                }
            }

            //macoActividades
            for (ProyectoMacroActividad m : proyecto.getProyectoMacroactividad()) {
                for (ProyectoEstPorcentajeFuente montoFuente : m.getMontosFuentes()) {
                    if (montoFuente.getFuente() == fuente || (fuente.getId() != null && fuente.equals(montoFuente.getFuente()))) {
                        montoUsadoAporte = montoUsadoAporte.add(montoFuente.getMontoEnConstruccion());
                    }
                }
            }

            if (fuente.getMonto().compareTo(montoUsadoAporte) != 0) {
                BusinessException b = new BusinessException();
                String[] params = {fuente.getFuenteRecursos().getNombre() + " " + fuente.getCategoriaConvenio().getNombre()};
                b.addError(ConstantesErrores.ERR_PARA_CERRAR_EL_PROYECTO_LA_SUMA_DE_COMPONETNES_Y_MACROACTIVIDADES_TIENE_QUE_SER_IGUAL_PARA_LA_FUENTE_0, params);
                throw b;
            }
        }

    }
 

}
