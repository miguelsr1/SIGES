/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.business.ejbs.UsuarioBean;
import gob.mined.siap2.business.ejbs.impl.validacionesPresupuestales.POAValidaciones;
import gob.mined.siap2.business.utils.PACUtils;
import gob.mined.siap2.business.utils.POAConverter;
import gob.mined.siap2.business.utils.POProyectoUtils;
import gob.mined.siap2.business.utils.ProyectoUtils;
import gob.mined.siap2.business.utils.UnidadTecnicaUtils;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.NotificacionPOA;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POActividadProyecto;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumoFlujoCajaMensual;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTramoTramo;
import gob.mined.siap2.entities.data.impl.ProyectoCategoriaConvenio;
import gob.mined.siap2.entities.data.impl.ProyectoComponente;
import gob.mined.siap2.entities.data.impl.ProyectoMacroActividad;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.entities.enums.EstadoPOA;
import gob.mined.siap2.entities.enums.TipoNotificacion;
import gob.mined.siap2.entities.enums.TipoPOAPAC;
import gob.mined.siap2.entities.enums.TipoProyecto;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroPOA;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.POADAO;
import gob.mined.siap2.persistence.dao.imp.ProcesoAdquisicionDAO;
import gob.mined.siap2.persistence.dao.imp.ProyectoDAO;
import gob.mined.siap2.persistence.dao.imp.UsuarioDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 * Esta clase implementa los métodos de los servicios de POA
 *
 * @author Sofis Solutions
 */
@Stateless(name = "POAProyecto")
@LocalBean
public class POAProyectoBean {

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
    @JPADAO
    private ProcesoAdquisicionDAO procesoAdqDAO;

    @Inject
    private UsuarioBean usuarioBean;
    @Inject
    private POAValidaciones pOAValidaciones;
    @Inject
    private GeneralBean generalBean;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Retorna los años fiscales para un poa el parámetro
     * soloDisponiblesPAraProgramacion sirve para filtrar solos los años en los
     * que es posible programar el proyecto
     *
     * @param IdProyecto
     * @return
     */
    public List<AnioFiscal> getAniosPOA(Integer IdProyecto, boolean soloDisponiblesPAraProgramacion) {
        try {
            List<AnioFiscal> res = new LinkedList<>();

            Proyecto p = (Proyecto) generalDAO.find(Proyecto.class, IdProyecto);
            int anio = DatesUtils.getYearOfDate(p.getInicio());
            int anioFin = DatesUtils.getYearOfDate(p.getFin());
            while (anio <= anioFin) {
                List<AnioFiscal> aniosFiscales = generalDAO.findByOneProperty(AnioFiscal.class, "anio", anio);
                if (!aniosFiscales.isEmpty()) {
                    AnioFiscal anioFiscal = aniosFiscales.get(0);
                    if (!soloDisponiblesPAraProgramacion || anioFiscal.getHabilitadoPlanificacion()) {
                        res.add(aniosFiscales.get(0));
                    } 
                }
                anio++;
            }
            return res;
        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
    }

    /**
     * Este método devuelve los años fiscales para programación POA de un
     * proyecto
     *
     * @param IdProyecto
     * @return
     */
    public List<AnioFiscal> getAniosDisponiblesProgramacionPOA(Integer IdProyecto) {
        return getAniosPOA(IdProyecto, true);
    }

    /**
     * Este método devuelve todos los años para los POA de un proyecto
     *
     * @param IdProyecto
     * @return
     */
    public List<AnioFiscal> getTodosAniosPOA(Integer IdProyecto) {
        return getAniosPOA(IdProyecto, false);
    }

    /**
     * Retorna las UT que el usuario tiene disponible para ese poa
     *
     * @param poa
     * @return
     */
    public List<UnidadTecnica> getUTDisponiblesParaUsuario(Integer idProyecto, Integer idAnioFiscal) {
        List<UnidadTecnica> res = new LinkedList<>();

        List<UnidadTecnica> utDelUsuario = usuarioBean.getUTDelUsuario();

        List<UnidadTecnica> utConPoas = poadao.getUTConPOA(idProyecto, idAnioFiscal);
        for (UnidadTecnica utEnPOA : utConPoas) {
            if (UnidadTecnicaUtils.tieneAccesoAUT(utDelUsuario, utEnPOA)) {
                res.add(utEnPOA);
            }
        }

        return res;
    }

    /**
     * Operación que retorna el poa de la línea de trabajo inicializado
     *
     * @param idProyecto proyecto del que se quiere el poa
     * @param idAnioFiscal identificador del año fiscal
     * @param idUnidadTecnica identificador de la unidad técnica
     * @return poa de proyecto
     */
    public POAProyecto getPOATrabajo(Integer idProyecto, Integer idAnioFiscal, Integer idUnidadTecnica) {
        try {
            Proyecto p = (Proyecto) generalDAO.find(Proyecto.class, idProyecto);

            //busca el poa de la línea de trabajo
            List<POAProyecto> poas = proyectoDAO.getPOATrabajo(idProyecto, idAnioFiscal, idUnidadTecnica);

            if (poas.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_POA_NO_SE_ENCUENTRA_GENERADO_PARA_ANIO_SELECCIONADO);
                throw b;
            }

            POAProyecto poa = poas.get(0);
            POProyectoUtils.initPOAProyecto(poa);

            if (!pOAValidaciones.tienePermisoEdicionPOA(poa)) {
                if (poa.getEstado() == EstadoPOA.EDITANDO_UT) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_NO_SE_ENCUENTRA_EN_ESTADO_ENVIADO_SOLO_PUEDE_SER_MODIFICADO_POR_UT);
                    throw b;
                }
            }

            return poa;

        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;

        }
    }

    /**
     * Método que retorna a que tramo pertenece el próximo insumo a agregar.
     *
     * @param idProyecto
     * @param categoriaConvenio
     * @return
     */
    public List<ProyectoAporteTramoTramo> getTramos(Integer idProyecto, CategoriaConvenio categoriaConvenio) {
        ProyectoCategoriaConvenio categoria = proyectoDAO.getCategoria(idProyecto, categoriaConvenio.getId());
        if (categoria == null) {
            return Collections.EMPTY_LIST;
        }

        ProyectoUtils.ordenarAportesTramos(categoria.getTramos(), false);
        categoria.getTramos().isEmpty();
        return categoria.getTramos();

    }

    public ProyectoAporteTramoTramo getProyectoAporteTramoTramo(Integer idProyectoAporteTramoTramo) {
        ProyectoAporteTramoTramo res = (ProyectoAporteTramoTramo) generalDAO.find(ProyectoAporteTramoTramo.class, idProyectoAporteTramoTramo);
        res.getParipassus().isEmpty();
        return res;
    }

    /**
     * Método que se encarga de verificar si un poa es de la línea de trabajo y
     * si tiene permiso de edición
     *
     * @param poa
     */
    private void verificarSiPOAEditable(POAProyecto poa) {
        if (!pOAValidaciones.tienePermisoEdicionPOA(poa)) {
            if (poa.getEstado() == EstadoPOA.EDITANDO_UT) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_POA_NO_SE_ENCUENTRA_EN_ESTADO_ENVIADO_SOLO_PUEDE_SER_MODIFICADO_POR_UT);
                throw b;
            } else {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_POA_PARA_GENERAR_POA_YA_ENVIADO_SOLO_PUEDE_SER_EDITADO_PROY);
                throw b;
            }
        }
    }
    
    public POAProyecto guardarRiesgosPOAProyecto(POAProyecto poa) {
        try {       
            poa = (POAProyecto) generalDAO.update(poa);
            return poa;
        } catch (DAOObjetoNoEditableException e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
    }

    /**
     * Operación que se llama cada vez que una ut actualiza su poa
     *
     * @param idProyecto
     * @param poa
     * @return
     */
    public POAProyecto guardarPOATrabajo(Integer idProyecto, POAProyecto poa) {
        try {
            verificarSiPOAEditable(poa);
            //se bloquea para realizar las validaciones de montos
            generalDAO.lock(Proyecto.class, poa.getProyecto().getId());
            pOAValidaciones.validarEstrucuraFinancieraPOAEdicionUT(idProyecto, poa);

            poa = (POAProyecto) generalDAO.update(poa);

            return poa;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
    }
    
    /**
     * Método utilizado para cerrar los POA de un año en especifico.
     * @param idAnioFiscal 
     */
    public void actualizarCierrePresupuesto(Integer idAnioFiscal){
        try {
            List<GeneralPOA> lista = poadao.getPOANoCierreEnAnioFiscal(idAnioFiscal);
            if(lista != null && !lista.isEmpty()){
                for(GeneralPOA item : lista){
                    item.setCierreAnual(true);
                    generalDAO.update(item);
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
    }

    /**
     * Método que se encarga de guardar un insumo en un poa de proyecto
     *
     * @param idProyecto
     * @param idPOA
     * @param idLinea
     * @param insumo
     * @return
     */
    public POInsumos guardarInsumo(Integer idProyecto, Integer idPOA, Integer idLinea, Integer idActividad, POInsumos insumo) {
        try {
            POAProyecto poa = (POAProyecto) generalDAO.find(POAProyecto.class, idPOA);
            verificarSiPOAEditable(poa);

            if (insumo.getFechaContratacion() != null) {
                int anioInsumo = DatesUtils.getYearOfDate(insumo.getFechaContratacion());
                if (anioInsumo != poa.getAnioFiscal().getAnio().intValue()) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_ANIO_CONTRATACION_DE_INSUMO_DINSTINTO_POA);
                    throw b;
                }
            }
            
            if(insumo.getNoUACI() == null || !insumo.getNoUACI()){
               insumo.setFechaRealInicioEjecucion(null);
               insumo.setDuracion(null);
            }

            //se bloquea para realizar las validaciones de montos
            generalDAO.lock(Proyecto.class, poa.getProyecto().getId());

            pOAValidaciones.validarInsumo(idProyecto, poa.getId(), idLinea, insumo);

            POActividadBase actividad = (POActividadBase) generalDAO.find(POActividadBase.class, idActividad);
            if (!actividad.getInsumos().contains(insumo)) {
                actividad.getInsumos().add(insumo);
            } else {
                insumo = (POInsumos) generalDAO.update(insumo);
            }
            return insumo;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
    }

    /**
     * Método que se encarga de eliminar un insumo en un poa de proyecto
     *
     * @param idPOA
     * @param insumo
     */
    public void eliminarInsumo(Integer idPOA, Integer idActividad, POInsumos insumo) {
        try {

            POAProyecto poa = (POAProyecto) generalDAO.find(POAProyecto.class, idPOA);
            verificarSiPOAEditable(poa);

            POActividadBase actividad = (POActividadBase) generalDAO.find(POActividadBase.class, idActividad);

            for (POMontoFuenteInsumo montoFuente : insumo.getMontosFuentes()) {
                if (montoFuente.getMontosFuentesInsumosFCM() != null) {
                    for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : montoFuente.getMontosFuentesInsumosFCM()) {
                        montoFuenteFCM.setMontoFuenteInsumo(null);
                    }
                    montoFuente.getMontosFuentesInsumosFCM().clear();
                }
                montoFuente.setInsumo(null);
            }
            insumo.getMontosFuentes().clear();

            for (FlujoCajaAnio fca : insumo.getFlujosDeCajaAnio()) {
                for (POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()) {
                    for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : fcm.getMontosFuentesInsumosFCM()) {
                        montoFuenteFCM.setFlujoCajaMensual(null);
                    }
                    fcm.getMontosFuentesInsumosFCM().clear();
                }
            }
            insumo.getFlujosDeCajaAnio().clear();
            
            actividad.getInsumos().remove(insumo);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
    }

    /**
     * Método que se encarga de guardar la actividad de un poa de proyecto
     *
     * @param idProyecto
     * @param idPOA
     * @param idLinea
     * @param actividad
     * @return
     */
    public POActividadProyecto guardarActividad(Integer idProyecto, Integer idPOA, Integer idLinea, POActividadProyecto actividad) {
        try {
            POAProyecto poa = (POAProyecto) generalDAO.find(POAProyecto.class, idPOA);
            verificarSiPOAEditable(poa);

            POLinea linea = (POLinea) generalDAO.find(POLinea.class, idLinea);
            if (!linea.getActividades().contains(actividad)) {
                linea.getActividades().add((POActividadProyecto) actividad);
            } else {
                actividad = (POActividadProyecto) generalDAO.update(actividad);
            }

            return actividad;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
    }

    /**
     * Método que se encarga de eliminar la actividad de un poa de proyecto
     *
     * @param idProyecto
     * @param idPOA
     * @param idLinea
     * @param actividad
     */
    public void eliminarActividad(Integer idProyecto, Integer idPOA, Integer idLinea, POActividadProyecto actividad) {
        try {
            POAProyecto poa = (POAProyecto) generalDAO.find(POAProyecto.class, idPOA);
            verificarSiPOAEditable(poa);

            POActividadProyecto actividadEnBase = (POActividadProyecto) generalDAO.find(POActividadProyecto.class, actividad.getId());
            if (!actividadEnBase.getInsumos().isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTEN_INSUMOS_ASOCIADOS_A_LA_ACTIVIDAD);
                throw b;
            }

            POLinea linea = (POLinea) generalDAO.find(POLinea.class, idLinea);
            linea.getActividades().remove(actividadEnBase);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
    }

    /**
     * Operación que se llama cuando un usuario de una UT envía el poa para
     * validar
     *
     * @param idProyecto
     * @param idPOA
     * @return
     */
    public POAProyecto enviarPOA(Integer idProyecto, Integer idPOA) {
        try {
            POAProyecto poa = (POAProyecto) generalDAO.find(POAProyecto.class, idPOA);
            if (!pOAValidaciones.tienePermisoEdicionPOA(poa)) {
                if (poa.getEstado() == EstadoPOA.EDITANDO_UT) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_NO_SE_ENCUENTRA_EN_ESTADO_ENVIADO_SOLO_PUEDE_SER_MODIFICADO_POR_UT);
                    throw b;
                } else {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_PARA_GENERAR_POA_YA_ENVIADO_SOLO_PUEDE_SER_EDITADO_PROY);
                    throw b;
                }
            }

            //se conservan los montos utilizados
            pOAValidaciones.validarEstrucuraFinancieraPOAParaEnviar(idProyecto, poa);

            Proyecto p = (Proyecto) generalDAO.find(Proyecto.class, idProyecto);
            String contenidoNotificacion = "UT: " + poa.getUnidadTecnica().getNombre() + ", POA: " + p.getNombre();

            poa.setEstado(EstadoPOA.VALIDANDO_POA);

            String opecodigoANotificar = p.getTipoProyecto() == TipoProyecto.ADMINISTRATIVO ? ConstantesEstandares.Operaciones.VALIDAR_POA_PROYECTO_ADMINISTRATIVO : ConstantesEstandares.Operaciones.VALIDAR_POA_PROYECTO_INV_NO_INV;

            List<SsUsuario> anotificar = usuarioDAO.getUsuariosConOperacion(opecodigoANotificar, null);
            for (SsUsuario u : anotificar) {
                NotificacionPOA n = new NotificacionPOA();
                n.setTipo(TipoNotificacion.NUEVO_POA_PROYECTO_PARA_VALIDAR);
                n.setUsuario(u);
                n.setObjetoId(p.getId());
                n.setPoaAnio(poa.getAnioFiscal().getId());
                n.setPoaUT(poa.getUnidadTecnica().getId());
                n.setFecha(new Date());
                n.setContenido(contenidoNotificacion);

                generalDAO.update(n);
            }

            poa = (POAProyecto) generalDAO.update(poa);
            return poa;
        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
    }

    /**
     * Operación que se llama cuando usuario con rol validador, le da validar al
     * poa de una UT
     *
     * @param idPOA
     */
    public void validarPOA(Integer idPOA) {
        try {
            POAProyecto poa = (POAProyecto) generalDAO.find(POAProyecto.class, idPOA);

            if (poa.getEstado() != EstadoPOA.VALIDANDO_POA || !pOAValidaciones.tienePermisoEdicionPOA(poa)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_POA_PARA_GENERAR_LINEA_BASE_EL_POA_TIENE_QUE_ENVIARSE_Y_SER_PRESUPUESTO);
                throw b;
            }
            //conserva los montos introducidos por el usuario
            pOAValidaciones.validarEstrucuraFinancieraPOAParaValidacion(poa.getProyecto().getId(), poa);

            generarLineaBase(poa);

            String contenidoNotificacion = "UT: " + poa.getUnidadTecnica().getNombre() + ", POA: " + poa.getProyecto().getNombre();
            poa.setEstado(EstadoPOA.CONSOLIDANDO_POA);
            List<SsUsuario> anotificar = usuarioDAO.getUsuariosConUT(poa.getUnidadTecnica().getId(), null);
            for (SsUsuario u : anotificar) {
                NotificacionPOA n = new NotificacionPOA();
                n.setTipo(TipoNotificacion.POA_PROYECTO_FUE_VALIDADO);
                n.setUsuario(u);
                n.setObjetoId(poa.getProyecto().getId());
                n.setPoaAnio(poa.getAnioFiscal().getId());
                n.setPoaUT(poa.getUnidadTecnica().getId());
                n.setFecha(new Date());
                n.setContenido(contenidoNotificacion);

                generalDAO.update(n);
            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Operación auxiliar que retorna si es necesario generar un PAC
     *
     * @param listPoa
     * @return
     */
    private boolean esNecesarioGenerarPAC(List<POAProyecto> listPoa) {
        for (POAProyecto poa : listPoa) {
            for (POLinea l : poa.getLineas()) {
                for (POActividadBase a : l.getActividades()) {
                    for (POInsumos i : a.getInsumos()) {
                        if (i.getNoUACI() == null || !i.getNoUACI()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Operación que se llama cuando se consolida un poa de proyecto
     *
     * @param listPoa
     * @param idProyecto
     */
    public void consolidar(List<POAProyecto> listPoa, Integer idProyecto) {
        try {
            if (listPoa != null) {
                Proyecto p = proyectoDAO.find(idProyecto);
                boolean esNecesarioPAC = esNecesarioGenerarPAC(listPoa);
                List<GeneralPOA> paosAPAC = new LinkedList();

                AnioFiscal anioFiscal = listPoa.get(0).getAnioFiscal();
                pOAValidaciones.validarEstrucuraFinancieraPOAParaConsolidacion(idProyecto, anioFiscal, listPoa);

                for (POAProyecto poa : listPoa) {
                    if (poa.getEstado() != EstadoPOA.CONSOLIDANDO_POA) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_POA_PARA_CONSOLIDARSE_TIENE_QUE_ESTAR_EN_ESTADO_DE_CONSOLIDACION);
                        throw b;
                    }
                    
                    //Se setea el monto PEP original de cada insumo con el monto total del insumo, ya que este último puede ser modificado en una reprogramación
                    for(POLinea linea : poa.getLineas()){
                        for(POActividadBase actividad : linea.getActividades()){
                            for(POInsumos insumo : actividad.getInsumos()){
                                insumo.setMontoPepOriginal(insumo.getMontoTotal());
                            }
                        }
                    }

                    generarLineaBase(poa);

                    poa.setEstado(EstadoPOA.TERMINO_CONSOLIDACION);
                    poa = (POAProyecto) generalDAO.update(poa);

                    if (esNecesarioPAC) {
                        paosAPAC.add(poa);
                    }
                }

                if (esNecesarioPAC) {
                    PAC pac = null;
                    if (!paosAPAC.isEmpty()) {
                        pac = PACUtils.crearPAC(paosAPAC, anioFiscal.getAnio(), TipoPOAPAC.POA_PROYECTO);
                        pac = (PAC) generalDAO.update(pac);
                    }
                    p.setPac(pac);
                }
            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    private POAProyecto generarLineaBase(POAProyecto poa) throws Exception {
        POAConverter converter = new POAConverter();
        POAProyecto base = converter.convertPOAPOAProyecto(poa);
        return (POAProyecto) generalDAO.create(base);
    }

    /**
     * Este método marca un POA como rechazado e indica cuál es el motivo de
     * ello
     *
     * @param idPoa
     * @param motivoRechazo
     */
    public void rechazarPOA(Integer idPoa, String motivoRechazo) {
        try {
            POAProyecto poa = (POAProyecto) generalDAO.find(POAProyecto.class, idPoa);

            if (poa.getEstado() != EstadoPOA.VALIDANDO_POA || !pOAValidaciones.tienePermisoEdicionPOA(poa)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_POA_PARA_GENERAR_LINEA_BASE_EL_POA_TIENE_QUE_ENVIARSE_Y_SER_PRESUPUESTO);
                throw b;
            }

            poa.setEstado(EstadoPOA.EDITANDO_UT);

            List<SsUsuario> anotificar = usuarioDAO.getUsuariosConUT(poa.getUnidadTecnica().getId(), null);
            for (SsUsuario u : anotificar) {
                NotificacionPOA n = new NotificacionPOA();
                n.setTipo(TipoNotificacion.POA_PROYECTO_FUE_RECHAZADO);
                n.setContenido(motivoRechazo);
                n.setUsuario(u);
                n.setObjetoId(poa.getProyecto().getId());
                n.setPoaAnio(poa.getAnioFiscal().getId());
                n.setPoaUT(poa.getUnidadTecnica().getId());
                n.setFecha(new Date());

                generalDAO.update(n);
            }

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve los POA de trabajo por distintos criterios.
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @param estado
     * @return
     */
    public List<POAProyecto> getPOAsTrabajo(Integer idProyecto, Integer idAnioFiscal, EstadoPOA estado) {
        List<POAProyecto> poas = proyectoDAO.getPOAsTrabajo(idProyecto, idAnioFiscal, estado);
        List<POAProyecto> result = new ArrayList<>();
        if (poas != null) {
            for (POAProyecto poa : poas) {
                POProyectoUtils.initPOAProyecto(poa);
                result.add(poa);
            }
        }
        return result;
    }

    /**
     * Retorna todos los POAs consolidados
     *
     * @param filtrarUT
     * @param utAfiltrar
     * @param utUsuarios
     * @param idAnioFiscal
     * @return
     */
    public List<POAProyecto> getPOAsParaReprogramacion(boolean filtrarUT, List<UnidadTecnica> utAfiltrar, Integer idAnioFiscal) {
        List<POAProyecto> result = new ArrayList<>();
        List<POAProyecto> poas = proyectoDAO.getPOAsParaReprogramacion(filtrarUT, utAfiltrar, idAnioFiscal);
        if (poas != null) {
            for (POAProyecto poa : poas) {
                POProyectoUtils.initPOAProyecto(poa);
                result.add(poa);
            }
        }
        return result;
    }

    /**
     * Este método devuelve las unidades técnicas para las que aún no se ha
     * consolidado el POA de un proyecto.
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @return
     */
    public List<UnidadTecnica> getUTPendientesParaConsolidar(Integer idProyecto, Integer idAnioFiscal) {
        List<UnidadTecnica> l = proyectoDAO.getUTPendientesParaConsolidar(idProyecto, idAnioFiscal);
        l.isEmpty();
        return l;
    }

    /**
     * Trae los poa para duplicar
     *
     * @param idProyecto id del proyecto que se quiere duplicar
     * @param anioPOA el anio que se quiere hacer la duplicacion
     * @param idUT la ut que quiere hacer la duplicacion
     * @return
     */
    public List<POAProyecto> getPOASParaDucplicar(Integer idPoa) {
        try {
            POAProyecto poa = (POAProyecto) generalDAO.find(POAProyecto.class, idPoa);

            LinkedList<POAProyecto> l = new LinkedList();

            List<POAProyecto> poas = proyectoDAO.getPOATrabajoADuplicar(poa.getProyecto().getId(), poa.getUnidadTecnica().getId());
            for (POAProyecto iterPOA : poas) {
                if (!iterPOA.equals(poa)) {
                    POProyectoUtils.initPOAProyecto(iterPOA);
                    l.add(iterPOA);
                }
            }

            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método duplica una línea de POA
     *
     * @param idPoa
     * @param idInsumosAduplicar
     * @return
     */
    public POAProyecto duplicarLineaEnPOA(Integer idPoa, List<Integer> idInsumosAduplicar) {
        try {
            POAProyecto poa = (POAProyecto) generalDAO.find(POAProyecto.class, idPoa);
            verificarSiPOAEditable(poa);

            for (Integer idInsumoAduplicar : idInsumosAduplicar) {
                POInsumos insumoAduplicar = (POInsumos) generalDAO.find(POInsumos.class, idInsumoAduplicar);
                POActividadProyecto actividadADuplicar = (POActividadProyecto) insumoAduplicar.getActividad();
                POLinea lineaADuplicar = proyectoDAO.getLineaDeActividad(actividadADuplicar.getId());

                //se busca la línea en la que se va a a añadir la actividad
                POLinea nuevaLinea = POProyectoUtils.findLinea(poa.getLineas(), lineaADuplicar.getProducto());
                if (nuevaLinea == null) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_NO_EXISTE_EN_EL_POA_LA_LINEA_A_DUPLICAR);
                    throw b;
                }

                //se busca la actividad en la que se va a añadir el insumo
                List actividades = nuevaLinea.getActividades();
                POActividadProyecto nuevaActividad = POProyectoUtils.findActividad(actividades, actividadADuplicar.getActividadCodiguera());
                if (nuevaActividad == null) {
                    nuevaActividad = new POActividadProyecto();
                    nuevaActividad.setActividadCodiguera(actividadADuplicar.getActividadCodiguera());
                    nuevaActividad.setDuracion(actividadADuplicar.getDuracion());
                    nuevaActividad.setInsumos(new LinkedList());
                    nuevaActividad.setEstado(EstadoActividadPOA.NO_FINALIZADA);
                    nuevaActividad.setResponsable(actividadADuplicar.getResponsable());
                    nuevaActividad.setUbicacion(actividadADuplicar.getUbicacion());
                    nuevaActividad.setUtResponsable(actividadADuplicar.getUtResponsable());

                    nuevaLinea.getActividades().add(nuevaActividad);
                }

                //se crea el nuevo insumo a duplicar 
                POInsumos nuevoInsumo = new POInsumos();
                nuevoInsumo.setFlujosDeCajaAnio(new LinkedHashSet());
                nuevoInsumo.setEnviadoParaCertificar(false);
                nuevoInsumo.setPasoValidacionCertificadoDeDispPresupuestaria(false);
                nuevoInsumo.setPoa(poa);
                if (insumoAduplicar.getFechaContratacion() != null) {
                    Date f = DatesUtils.setYearToDate(insumoAduplicar.getFechaContratacion(), poa.getAnioFiscal().getAnio());
                    nuevoInsumo.setFechaContratacion(f);
                }
                nuevoInsumo.setObservacion(insumoAduplicar.getObservacion());
                nuevoInsumo.setInsumo(insumoAduplicar.getInsumo());
                nuevoInsumo.setNoUACI(insumoAduplicar.getNoUACI());
                nuevoInsumo.setCantidad(insumoAduplicar.getCantidad());
                nuevoInsumo.setMontoUnit(insumoAduplicar.getMontoUnit());
                nuevoInsumo.setMontoTotal(insumoAduplicar.getMontoTotal());
                nuevoInsumo.setUnidadTecnica(poa.getUnidadTecnica());
                nuevoInsumo.setTramo(insumoAduplicar.getTramo());

                nuevoInsumo.setMontosFuentes(new LinkedList());
                for (POMontoFuenteInsumo iter : insumoAduplicar.getMontosFuentes()) {
                    POMontoFuenteInsumo fuente = new POMontoFuenteInsumo();
                    fuente.setCertificadoDisponibilidadPresupuestariaAprobada(false);
                    fuente.setCertificado(BigDecimal.ZERO);
                    fuente.setMontoDescertificado(BigDecimal.ZERO);
                    fuente.setMonto(iter.getMonto());
                    fuente.setFuente(iter.getFuente());

                    fuente.setInsumo(nuevoInsumo);
                    nuevoInsumo.getMontosFuentes().add(fuente);
                }

                //se añade el insumo a la actividad
                nuevaActividad.getInsumos().add(nuevoInsumo);
                nuevoInsumo.setActividad(nuevaActividad);

            }

            //se bloquea para realizar las validaciones de montos
            generalDAO.lock(Proyecto.class, poa.getProyecto().getId());
            pOAValidaciones.validarEstructuraFinancieraPOAParaDuplicar(poa.getProyecto().getId(), poa);

            return poa;

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve los POA en línea base para un proyecto y unidad
     * técnica en un determinado año.
     *
     * @param idProyecto
     * @param idAnioFiscal
     * @param idUnidadTecnica
     * @return
     */
    public Map<String, Integer> getPOASEnLineaBase(Integer idProyecto, Integer idAnioFiscal, Integer idUnidadTecnica) {
        try {

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

            Map<String, Integer> m = new LinkedHashMap();
            List<POAProyecto> l = proyectoDAO.getPOASBase(idProyecto, idAnioFiscal, idUnidadTecnica);
            for (POAProyecto poa : l) {
                m.put(df.format(poa.getFechaFijacion()), poa.getId());
            }
            return m;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve el POA en línea base de un determinado POA
     *
     * @param idPOA
     * @return
     */
    public POAProyecto getPOAEnLineaBase(Integer idPOA) {
        try {
            POAProyecto poa = (POAProyecto) generalDAO.find(POAProyecto.class, idPOA);
            POProyectoUtils.initPOAProyecto(poa);
            return poa;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve los id de los componentes de un proyecto en los que
     * interviene una unidad técnica.
     *
     * @param objeto
     * @param idUT
     * @return
     */
    public List<Integer> getComponentesProyecto(Proyecto objeto, Integer idUT) {
        List<Integer> l = new LinkedList();
        if (objeto != null) {
            for (ProyectoComponente componente : objeto.getProyectoComponentes()) {
                if (componente.getComponentePadre() == null) {
                    addComponentesRecursivo(componente, l, false, idUT);
                }
            }
        }
        return l;
    }

    private void addComponentesRecursivo(ProyectoComponente componente, List<Integer> list, boolean tienePadreUT, Integer idUT) {
        if (tienePadreUT || (componente.getUnidadTecnica() != null && idUT.equals(componente.getUnidadTecnica().getId()))) {
            tienePadreUT = true;
            list.add(componente.getId());
        }
        for (ProyectoComponente hijo : componente.getComponenteHijos()) {
            addComponentesRecursivo(hijo, list, tienePadreUT, idUT);
        }

    }

    /**
     * Este método devuelve las macro-actividades de un proyecto en las que está
     * involucrada una unidad técnica.
     *
     * @param objeto
     * @param idUnidadTecnica
     * @return
     */
    public List<Integer> getMacroActividadesProyecto(Proyecto objeto, Integer idUnidadTecnica) {
        List<Integer> list = new LinkedList();
        if (objeto != null) {
            for (ProyectoMacroActividad macroActividad : objeto.getProyectoMacroactividad()) {
                if (macroActividad.getUnidadTecnica() != null && macroActividad.getUnidadTecnica().getId().equals(idUnidadTecnica)) {
                    list.add(macroActividad.getId());
                }
            }
        }
        return list;
    }

    /**
     * Retorna las unidades técnicas que compartieron lineas en su poa de
     * trabajo, con alguna de las UT pasadas por parámetro
     *
     * @param unidades
     * @param idProyecto
     * @return
     */
    public List<UnidadTecnica> getUTQueCompartieronConUT(Integer idProyecto, Integer idAnioFiscal, List<UnidadTecnica> unidades) {
        try {
            List<UnidadTecnica> l = proyectoDAO.getQueCompartieron(idProyecto, idAnioFiscal, unidades);
            l.isEmpty();
            return l;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve las actividades de un poa
     *
     * @param poaId
     * @return
     */
    public Collection<POActividadBase> obtenerLineasPorPOA(Integer poaId) {
        Collection<POActividadBase> respuesta = new ArrayList();
        try {
            POAProyecto poa = (POAProyecto) poadao.findById(GeneralPOA.class, poaId);
            if (poa != null) {
                for (POLinea linea : poa.getLineas()) {
                    for (POActividadBase a : linea.getActividades()) {
                        respuesta.add(a);
                    }
                }
            }
        } catch (DAOGeneralException ex) {
            Logger.getLogger(POAProyectoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

    /**
     * Verifica que para determinado POA, todas las actividades estén
     * completadas o deshabilitadas, en caso contrario, existen actividades
     * pendientes
     *
     * @param idPOA
     * @return
     */
    public Boolean existenActividadesPendientesEnPOA(Integer idPOA) {
        try {
            POAProyecto poaProy = (POAProyecto) generalDAO.find(POAProyecto.class, idPOA);
            Boolean hayActividadPendiente = false;
            Iterator<POLinea> itLineas = poaProy.getLineas().iterator();
            while (itLineas.hasNext() && !hayActividadPendiente) {
                POLinea linea = itLineas.next();
                Iterator<POActividadBase> itActividades = linea.getActividades().iterator();
                while (itActividades.hasNext() && !hayActividadPendiente) {
                    POActividadBase actividad = itActividades.next();
                    if ((actividad.getEstado() == null || actividad.getEstado().equals(EstadoActividadPOA.NO_FINALIZADA))) {
                        hayActividadPendiente = true;
                    }
                }
            }
            return hayActividadPendiente;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Verifica que para determinado POA, todos los insumos adquiridos,
     * deshabilitados, cancelados o declarados como desiertos, en caso
     * contrario, existen insumos pendientes
     *
     * @param idPOA
     * @return
     */
    public Boolean existenInsumosPendientesEnPOA(Integer idPOA) {
        try {
            POAProyecto poaProy = (POAProyecto) generalDAO.find(POAProyecto.class, idPOA);
            Boolean hayInsumoPendiente = false;
            Iterator<POLinea> itLineas = poaProy.getLineas().iterator();
            while (itLineas.hasNext() && !hayInsumoPendiente) {
                POLinea linea = itLineas.next();
                Iterator<POActividadBase> itActividades = linea.getActividades().iterator();
                while (itActividades.hasNext() && !hayInsumoPendiente) {
                    POActividadBase actividad = itActividades.next();
                    Iterator<POInsumos> itPoInsumos = actividad.getInsumos().iterator();
                    while (itPoInsumos.hasNext() && !hayInsumoPendiente) {
                        POInsumos poInsumo = itPoInsumos.next();
                        /**
                         * Formas de que el insumo este "pendiente" 1) No esté
                         * asociado a un proceso de adquisición 2) Este asociado
                         * a un proceso de adquisición pero una parte no esta
                         * asociada a un item 3) Esta asociado a items, pero
                         * dicho item no tiene un estado asociado
                         */

                        //verifica que el insumo este en algun proceso de adquisicion
                        if (poInsumo.getNoUACI() != null && !poInsumo.getNoUACI()) {
                            if (procesoAdqDAO.insumoEstaEnProceso(poInsumo.getId())) {
                                hayInsumoPendiente = true;
                            }
                        }
                    }
                }
            }
            return hayInsumoPendiente;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Cierra el POA para el año seleccionado
     *
     * @param idPOA
     * @return
     */
    public void confirmarCierreAnual(Integer idProyecto, Integer idAnioFiscal) {
        try {
            Iterator<POAProyecto> itListaPOAProy = proyectoDAO.getPOAsTrabajo(idProyecto, idAnioFiscal, null).iterator();
            while (itListaPOAProy.hasNext()) {
                POAProyecto poaProy = itListaPOAProy.next();
                BusinessException b = new BusinessException();
                if (poaProy.getEstado() != EstadoPOA.TERMINO_CONSOLIDACION) {
                    b.addError(ConstantesErrores.ERR_HAY_POAS_SIN_CONSOLIDAR);
                }

                if (existenActividadesPendientesEnPOA(poaProy.getId())) {
                    b.addError(ConstantesErrores.ERR_CIERRE_ANUAL_POA_POR_ACTIVIDADES_SIN_FINALIZAR);
                }
                if (existenInsumosPendientesEnPOA(poaProy.getId())) {
                    b.addError(ConstantesErrores.ERR_CIERRE_ANUAL_POA_POR_INSUMOS_PENDIENTES);
                }
                if (!generalBean.anioSeleccionadoEstaFinalizado(poaProy.getAnioFiscal().getId())) {
                    b.addError(ConstantesErrores.ERR_CIERRE_ANUAL_ANIO_FISCAL_NO_FINALIZADO);
                }
                if (!b.getErrores().isEmpty()) {
                    throw b;
                }
                poaProy.setCierreAnual(true);
            }
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Cierra el POA para el año seleccionado
     *
     * @param idPOA
     * @return
     */
    public void confirmarCierreAnual(Integer idProyectoPOA, Boolean confirmar) {
        try {
            //Iterator<POAProyecto> itListaPOAProy = proyectoDAO.getPOAsTrabajo(idProyecto, idAnioFiscal, null).iterator();
           // while (itListaPOAProy.hasNext()) {
                POAProyecto poaProy = (POAProyecto) generalDAO.find(POAProyecto.class, idProyectoPOA);
                if(confirmar != null && confirmar != null && !confirmar) {
                    BusinessException b = new BusinessException();
                    if (poaProy.getEstado() != EstadoPOA.TERMINO_CONSOLIDACION) {
                        b.addError(ConstantesErrores.ERR_HAY_POAS_SIN_CONSOLIDAR);
                    }

                    if (existenActividadesPendientesEnPOA(poaProy.getId())) {
                        b.addError(ConstantesErrores.ERR_CIERRE_ANUAL_POA_POR_ACTIVIDADES_SIN_FINALIZAR);
                    }
                    if (existenInsumosPendientesEnPOA(poaProy.getId())) {
                        b.addError(ConstantesErrores.ERR_CIERRE_ANUAL_POA_POR_INSUMOS_PENDIENTES);
                    }
                    //if (!generalBean.anioSeleccionadoEstaFinalizado(poaProy.getAnioFiscal().getId())) {
                    //    b.addError(ConstantesErrores.ERR_CIERRE_ANUAL_ANIO_FISCAL_NO_FINALIZADO);
                    //
                    if (!b.getErrores().isEmpty()) {
                        throw b;
                    }
                }
                
                if(confirmar != null && confirmar) {
                    List<Integer> idsInsumosEliminar = new ArrayList();
                    poaProy.setCierreAnual(true);
                    for(POLinea linea : poaProy.getLineas()) {
                        for(POActividadBase act : linea.getActividades()) {
                            act.setEstado(EstadoActividadPOA.FINALIZADA);
                            for(POInsumos ins : act.getInsumos()) {
                                ins.setHabilitado(Boolean.FALSE);
                                
                                if(poaProy.getProyecto().getPac() != null && poaProy.getProyecto().getPac().getInsumos() != null) {
                                    for(POInsumos in : poaProy.getProyecto().getPac().getInsumos()) {
                                        if(in.getId().equals(ins.getId())) {
                                            idsInsumosEliminar.add(in.getId());
                                        }
                                    }
                                }
                            }
                        }
                    } 
     
                    poaProy.setCierreAnual(true);
                    generalDAO.update(poaProy);
                }
                
            //}
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    private List<CriteriaTO> generarCriteria(FiltroPOA filtro) {
        List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

        if (StringUtils.isNotBlank(filtro.getNombre()) ) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "nombre", filtro.getNombre().trim());
            criterios.add(criterio);
        }


        return criterios;
        
    }

    public List<Proyecto> obtenerPOAProyectoPorFiltro(FiltroPOA filtro) {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
           
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(Proyecto.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
}
