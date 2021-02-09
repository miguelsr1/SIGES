/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Notificacion;
import gob.mined.siap2.entities.data.impl.NotificacionPOA;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.TipoNotificacion;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.filtrosUI.FiltroNotificacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "notificacionesConsulta")
public class NotificacionesConsulta implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    UsuarioInfo usuarioInfo;
    @Inject
    EntityManagementDelegate emd;

    private LazyDataModel lazyModel;
    private LazyDataModel lazyModelCompleto;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private FiltroNotificacion filtroNotificacion = new FiltroNotificacion();
    private Integer cantidadElementos=0;

    @PostConstruct
    public void init() {
        filterTable();
        actualizarCantElementos();
    }

    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter() {
        filtroNotificacion = new FiltroNotificacion();
    }
    
    /**
     * Actualiza el número de modificaciones
     */
    public void actualizarCantElementos() {
        filterTableCompleto();
        cantidadElementos=lazyModelCompleto.getRowCount();
    }

    /**
     * Este método corresponde al evento de consulta y obtiene todas las notificaciones
     */
    public void filterTableCompleto() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "usuario.usuCod", usuarioInfo.getUserCod());
            criterios.add(criterio);

            
            CriteriaTO condicion = null;
            if (!criterios.isEmpty()) {
                if (criterios.size() == 1) {
                    condicion = criterios.get(0);
                } else {
                    condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
                }
            } else {
                // condición dummy para que el count by criteria funcione
                condicion = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "id", 1);
            }

            String[] propiedades = {"id", "tipo", "contenido", "fecha"};

            String className = Notificacion.class.getName();
            String[] orderBy = {"fecha"};
            boolean[] asc = {false};

            IDataProvider dataProvider = new EntityReferenceDataProvider(propiedades, className, condicion, orderBy, asc);
            lazyModelCompleto = new GeneralLazyDataModel(dataProvider);

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "usuario.usuCod", usuarioInfo.getUserCod());
            criterios.add(criterio);

            if (!TextUtils.isEmpty(filtroNotificacion.getTipo())) {
                MatchCriteriaTO criterioTipo = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "tipo", filtroNotificacion.getTipo());
                criterios.add(criterioTipo);
            }
            if (!TextUtils.isEmpty(filtroNotificacion.getDescripcion())) {
                MatchCriteriaTO criterioNotificacion = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "contenido", filtroNotificacion.getDescripcion());
                criterios.add(criterioNotificacion);
            }
            if (filtroNotificacion.getDesde() != null) {
                MatchCriteriaTO criterioDesde = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "fecha", filtroNotificacion.getDesde());
                criterios.add(criterioDesde);
            }
            if (filtroNotificacion.getHasta() != null) {
                MatchCriteriaTO criterioDesde = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "fecha", filtroNotificacion.getHasta());
                criterios.add(criterioDesde);
            }
            CriteriaTO condicion = null;
            if (!criterios.isEmpty()) {
                if (criterios.size() == 1) {
                    condicion = criterios.get(0);
                } else {
                    condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
                }
            } else {
                // condición dummy para que el count by criteria funcione
                condicion = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "id", 1);
            }

            String[] propiedades = {"id", "tipo", "contenido", "fecha"};

            String className = Notificacion.class.getName();
            String[] orderBy = {"fecha"};
            boolean[] asc = {false};

            IDataProvider dataProvider = new EntityReferenceDataProvider(propiedades, className, condicion, orderBy, asc);
            lazyModel = new GeneralLazyDataModel(dataProvider);

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    
    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
    }

    /**
     * Completa el texto del mensaje mostrado en la notificación
     * @param texto
     * @param idN
     * @return 
     */
    public String completarTextoMensaje(String texto, Integer idN) {
        Map<String, String> m = new HashMap();
        Notificacion n = (Notificacion) emd.getEntityById(Notificacion.class.getName(), idN);
        if (n instanceof NotificacionPOA) {
            NotificacionPOA np = (NotificacionPOA) n;
            UnidadTecnica ut = (UnidadTecnica) emd.getEntityById(UnidadTecnica.class.getName(), np.getPoaUT());
            m.put("unidadTecnica", ut.getNombre());
        }

        if (!m.isEmpty()) {
            texto = TextUtils.replaceTokens(texto, m);
        }
        return texto;
    }

    /**
     * Redirige a la página que corresponda según la notificación seleccionada
     * @param id
     * @return 
     */
    public String openNotificacion(Integer id) {
        Notificacion n = (Notificacion) emd.getEntityById(Notificacion.class.getName(), id);
        if (n.getTipo() == TipoNotificacion.POA_ACCION_CENTRAL_FUE_VALIDADO
                || n.getTipo() == TipoNotificacion.POA_ACCION_CENTRAL_FUE_RECHAZADO
                || n.getTipo() == TipoNotificacion.INGRESO_TDR_CERT_DISP_INSUMO_ACC_CENTR) {
            NotificacionPOA npoa = (NotificacionPOA) n;
            return "crearEditarPOAAccionCentral.xhtml?faces-redirect=true&id=" + npoa.getObjetoId()
                    + "&idAnioFiscal=" + npoa.getPoaAnio()
                    + "&idUnidadTecnica=" + npoa.getPoaUT();

        } else if (n.getTipo() == TipoNotificacion.NUEVO_POA_ACCION_CENTRAL_PARA_VALIDAR) {
            NotificacionPOA npoa = (NotificacionPOA) n;
            return "validarPOAAccionCentral.xhtml?faces-redirect=true&id=" + npoa.getObjetoId()
                    + "&idAnioFiscal=" + npoa.getPoaAnio()
                    + "&idUnidadTecnica=" + npoa.getPoaUT();

        } else if (n.getTipo() == TipoNotificacion.POA_ASIGNACION_NO_PROGRAMABLE_FUE_VALIDADO
                || n.getTipo() == TipoNotificacion.POA_ASIGNACION_NO_PROGRAMABLE_FUE_RECHAZADO
                || n.getTipo() == TipoNotificacion.INGRESO_TDR_CERT_DISP_INSUMO_ASIG_NP) {
            NotificacionPOA npoa = (NotificacionPOA) n;
            return "crearEditarPOAAsignacionesNoProgramables.xhtml?faces-redirect=true&id=" + npoa.getObjetoId()
                    + "&idAnioFiscal=" + npoa.getPoaAnio()
                    + "&idUnidadTecnica=" + npoa.getPoaUT();

        } else if (n.getTipo() == TipoNotificacion.NUEVO_POA_ASIGNACION_NO_PROGRAMABLE_PARA_VALIDAR) {
            NotificacionPOA npoa = (NotificacionPOA) n;
            return "validarPOAAsignacionNoProgramable.xhtml?faces-redirect=true&id=" + npoa.getObjetoId()
                    + "&idAnioFiscal=" + npoa.getPoaAnio()
                    + "&idUnidadTecnica=" + npoa.getPoaUT();

        } else if (n.getTipo() == TipoNotificacion.NUEVO_POA_PROYECTO_PARA_VALIDAR) {
            NotificacionPOA npoa = (NotificacionPOA) n;
            return "validarPOAProyecto.xhtml?faces-redirect=true&id=" + npoa.getObjetoId()
                    + "&idAnioFiscal=" + npoa.getPoaAnio()
                    + "&idUnidadTecnica=" + npoa.getPoaUT();

        } else if (n.getTipo() == TipoNotificacion.POA_PROYECTO_FUE_RECHAZADO
                || n.getTipo() == TipoNotificacion.POA_PROYECTO_FUE_VALIDADO
                || n.getTipo() == TipoNotificacion.INGRESO_TDR_CERT_DISP_INSUMO_PROY
                || n.getTipo() == TipoNotificacion.PROCESO_ADQUISICION_EN_PAUSA
                || n.getTipo() == TipoNotificacion.ITEM_DESIERTO
                || n.getTipo() == TipoNotificacion.ITEM_SIN_EFECTO) {
            NotificacionPOA npoa = (NotificacionPOA) n;
            return "crearEditarPOAProyecto.xhtml?faces-redirect=true&id=" + npoa.getObjetoId()
                    + "&idAnioFiscal=" + npoa.getPoaAnio()
                    + "&idUnidadTecnica=" + npoa.getPoaUT();

        } else if (n.getTipo() == TipoNotificacion.INGRESO_ORDEN_INICIO_CONTRATO) {

            return "crearEditarContratoOC.xhtml?faces-redirect=true&id=" + n.getObjetoId();

        } else if (n.getTipo() == TipoNotificacion.INSUMOS_PROCESO_ADQ_PENDIENTE_CARGA_TDR_ET) {
            NotificacionPOA npoa = (NotificacionPOA) n;
            return "crearEditarPOAAccionCentral.xhtml?faces-redirect=true&id=" + npoa.getObjetoId()
                    + "&idAnioFiscal=" + npoa.getPoaAnio()
                    + "&idUnidadTecnica=" + npoa.getPoaUT();

        } else if (n.getTipo() == TipoNotificacion.INSUMOS_PROCESO_ADQ_PENDIENTE_CARGA_TDR_ET_POA_PROYECTO) {
            NotificacionPOA npoa = (NotificacionPOA) n;
            return "crearEditarPOAProyecto.xhtml?faces-redirect=true&id=" + npoa.getObjetoId()
                    + "&idAnioFiscal=" + npoa.getPoaAnio()
                    + "&idUnidadTecnica=" + npoa.getPoaUT();

        } else if (n.getTipo() == TipoNotificacion.CERTIFICADO_DISPONIBILIDAD_PRESUPUESTARIA_PARA_VALIDAR) {

            return "validarCertificadoDisponibilidadPresupuestaria.xhtml?faces-redirect=true&id=" + n.getObjetoId();

        } else if (n.getTipo() == TipoNotificacion.VALIDAR_IMPUESTOS_CONTRATO) {

            return "validarImpuestosContrato.xhtml?faces-redirect=true&id=" + n.getObjetoId();
        } else if (n.getTipo() == TipoNotificacion.PROCESO_ADQ_ASIGNADO_A_ORDEN_INICIO) {
            return "crearEditarProcesoAdquisicion.xhtml?faces-redirect=true&id=" + n.getObjetoId();
            
        } else if (n.getTipo() == TipoNotificacion.VALIDACION_COMPROMISO_PRESUPUESTARIO) {
            return "validarCompromisoPresupuestario.xhtml?faces-redirect=true&id=" + n.getObjetoId();
        
        } else if(n.getTipo() == TipoNotificacion.DESCERTIFICACION_MONTO_FUENTE_INSUMO_PARA_VALIDAR){ 
            return "descertificarMontoFuenteInsumo.xhtml?faces-redirect=true&id=" + n.getObjetoId();
            
        } else if(n.getTipo() == TipoNotificacion.CERTIFICADO_DISPONIBILIDAD_PRESUPUESTARIA_PARA_VALIDAR_POR_PROCESO){ 
            return "validarCertificadoDisponibilidadPresupuestaria.xhtml?faces-redirect=true&id=" + n.getObjetoId() ;
            
       	} else if(n.getTipo() == TipoNotificacion.CERTIFICADO_DISP_PRESUPUESTARIA_COMETNARIO_PARA_SOLICITANTE){ 
            return "reenviarCertificadoDisponibilidadPresupuestaria.xhtml?faces-redirect=true&id=" + n.getObjetoId() ;
            
        }else if(n.getTipo() == TipoNotificacion.CERTIFICADO_DISP_PRESUPUESTARIA_COMETNARIO_PARA_VALIDADOR){ 
            return "validarCertificadoDisponibilidadPresupuestaria.xhtml?faces-redirect=true&id=" + n.getObjetoId() ;
        } else if(n.getTipo() == TipoNotificacion.NUEVO_POA_PARA_EVALUAR || n.getTipo() == TipoNotificacion.POA_FUE_APROBADO){ 
            return "validarPlanOperativoAnual.xhtml?faces-redirect=true&id=" + n.getObjetoId() ;
       	} else if(n.getTipo() == TipoNotificacion.POA_FUE_RECHAZADO){ 
            return "crearEditarPlanOperativoAnual.xhtml?faces-redirect=true&id=" + n.getObjetoId() ;
            
       	}
        return null;
    }

    private String idToEliminar;

    /**
     * Elimina una notificación
     */
    public void eliminar() {
        try {
            emd.delete(Notificacion.class, Integer.valueOf(idToEliminar));
            RequestContext.getCurrentInstance().execute("$('#confirmModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">  

    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public String getIdToEliminar() {
        return idToEliminar;
    }

    public void setIdToEliminar(String idToEliminar) {
        this.idToEliminar = idToEliminar;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public FiltroNotificacion getFiltroNotificacion() {
        return filtroNotificacion;
    }

    public void setFiltroNotificacion(FiltroNotificacion filtroNotificacion) {
        this.filtroNotificacion = filtroNotificacion;
    }
    
    public Integer getCantidadElementos() {
        return cantidadElementos;
    }

    public void setCantidadElementos(Integer cantidadElementos) {
        this.cantidadElementos = cantidadElementos;
    }


     // </editor-fold>
}
