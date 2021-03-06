/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.padq;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.PasosProcesoAdquisicion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.ArchivoDelegate;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.delegates.impl.ProcesoAdquisicionDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.web.utils.SofisComboG;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
@Named(value = "procesoAdqConsultaTiempos")
public class ProcesoAdquisicionConsultaTiempos implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    UsuarioDelegate usuarioDelegate;
    @Inject
    ProcesoAdquisicionDelegate pAdqDelegate;
    @Inject
    ArchivoDelegate archivoDelegate;

    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String nombre;
    private SofisComboG<AnioFiscal> comboAnioFiscal;
    private SofisComboG<MetodoAdquisicion> comboMetodoAdq;
    private String numero;
    private SofisComboG<SsUsuario> comboUsuarios;
    private PasosProcesoAdquisicion estado = null;
    private Date menorFechaInsumo = null;
    private Date menorFechaGantt = null;
    private SofisComboG<UnidadTecnica> comboUnidadTecnica;

    private String nombrePAC;
    private String nroPAC;

    @PostConstruct
    public void init() {
        initAnioFisscales();
        initUsuarios();
        initMetodoAdq();
        initUnidadesTecnica();

        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        nombre = null;
        comboAnioFiscal.clearSelectedT();
        comboMetodoAdq.clearSelectedT();
        numero = null;
        comboUsuarios.clearSelectedT();
        estado = null;
        menorFechaInsumo = null;
        menorFechaGantt = null;
        comboUnidadTecnica.clearSelectedT();
        nombrePAC = null;
        nroPAC = null;
    }

    /**
     * Este método corresponde a la consulta según los filtros definidos.
     */
    public void filterTable() {
        try {

            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            BusinessException b = new BusinessException();
            if (comboAnioFiscal.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "anio.id", comboAnioFiscal.getSelectedT().getId());
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(nombre)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombre", nombre);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(numero) && TextUtils.isInteger(numero)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "secuenciaNumero", Integer.valueOf(numero));
                criterios.add(criterio);
            } else if (!TextUtils.isEmpty(numero) && !TextUtils.isInteger(numero)) {
                b.addError(ConstantesErrores.ERROR_NUMERO_INAVLIDO);
            }
            if (comboMetodoAdq.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "metodoAdquisicion.id", comboMetodoAdq.getSelectedT().getId());
                criterios.add(criterio);
            }
            if (comboUsuarios.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "responsable.usuId", comboUsuarios.getSelectedT().getUsuId());
                criterios.add(criterio);
            }
            if (estado != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", estado);
                criterios.add(criterio);
            }
            if (menorFechaInsumo != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "menorFechaInsumo", menorFechaInsumo);
                criterios.add(criterio);
            }
            if (menorFechaGantt != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "menorFechaGantt", menorFechaGantt);
                criterios.add(criterio);
            }
            if (comboUnidadTecnica.getSelectedT() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poInsumos.unidadTecnica.id", comboUnidadTecnica.getSelectedT().getId());
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(nroPAC) && TextUtils.isInteger(nroPAC)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poInsumos.pacGrupo.pac.id", Integer.valueOf(nroPAC));
                criterios.add(criterio);
            } else if (!TextUtils.isEmpty(nroPAC) && !TextUtils.isInteger(nroPAC)) {
                b.addError(ConstantesErrores.ERROR_NUMERO_PAC_INAVLIDO);
            }
            if (!TextUtils.isEmpty(nombrePAC)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "poInsumos.pacGrupo.pac.nombre", nombrePAC);
                criterios.add(criterio);
            }
            if (!b.getErrores().isEmpty()) {
                throw b;
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

            String[] propiedades = {"id", "secuenciaAnio", "secuenciaNumero", "nombre", "montoTotal",
                "menorFechaInsumo", "menorFechaGantt", "mayorFechaGantt",
                "metodoAdquisicion.nombre", "responsable.usuCod", "estado", "anio.anio", "estadoProceso", "diasEnPausa", "metodoAdquisicion.nombre"};

            String className = ProcesoAdquisicion.class.getName();
            String[] orderBy = {"nombre"};
            boolean[] asc = {true};

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
     * Inicialización del combo de años fiscales.
     */
    public void initAnioFisscales() {
        List<AnioFiscal> l = emd.getEntities(AnioFiscal.class.getName(), "anio");
        comboAnioFiscal = new SofisComboG<>(l, "anio");
        comboAnioFiscal.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
    }

    /**
     * Inicialización de métodos de adquisición.
     */
    public void initMetodoAdq() {
        List<MetodoAdquisicion> l = emd.getEntities(MetodoAdquisicion.class.getName(), "nombre");
        comboMetodoAdq = new SofisComboG<>(l, "nombre");
        comboMetodoAdq.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
    }

    /**
     * Inicialización de combo de usuarios.
     */
    public void initUsuarios() {
        List<SsUsuario> l = usuarioDelegate.getUsuariosConOperacion(ConstantesEstandares.Operaciones.OPERACION_CREAR_EDITAR_PROCESO_ADQUISICION);
        comboUsuarios = new SofisComboG<>(l, "usuCod");
        comboUsuarios.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
    }

    /**
     * Inicialización de combo de unidades técnicas.
     */
    public void initUnidadesTecnica() {
        List<UnidadTecnica> l = emd.getEntities(UnidadTecnica.class.getName(), "nombre");
        comboUnidadTecnica = new SofisComboG<>(l, "nombre");
        comboUnidadTecnica.addEmptyItem(ConstantesPresentacion.FILTRO_VACIO);
    }

    /**
     * Este método obtiene la fecha de inicio planificada de un proceso.
     * @param idProceso
     * @return 
     */
    public Date obtenerFechaInicioPlanificada(Integer idProceso) {
        try {
            return pAdqDelegate.obtenerFechaInicioPlanificada(idProceso);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;        
    }
      
    /**
     * Este método obtiene la fecha de fin planificada para el paso actual de un proceso.
     * @param idProceso
     * @return 
     */
    public Date obtenerFechaFinPlanificadaParaPasoActual(Integer idProceso) {
        try {
            return pAdqDelegate.obtenerFechaFinPlanificadaParaPasoActual(idProceso);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;        
    }
         
    /**
     * Este método obtiene la fecha real de inicio de un proceso.
     * @param idProceso
     * @return 
     */
    public Date obtenerFechaInicioReal(Integer idProceso) {
        try {
            return pAdqDelegate.obtenerFechaInicioReal(idProceso);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;        
    }
    
    /**
     * Este método permite obtener el tiempo planificado para el estado actual
     * @param idProceso
     * @return 
     */
    public Integer obtenerTiempoPlanificadoParaEstadoActual(Integer idProceso) {
        try {
            return pAdqDelegate.obtenerTiempoPlanificadoParaEstadoActual(idProceso);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;        
    }
    
    /**
     * Este método permite obtener el tiempo real para el estado actual.
     * @param idProceso
     * @return 
     */
    public Integer obtenerTiempoRealParaEstadoActual(Integer idProceso) {
        try {
            return pAdqDelegate.obtenerTiempoRealParaEstadoActual(idProceso);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;        
    }

    /**
     * Este método permite obtener el tiempo de atraso de un proceso.
     * @param idProceso
     * @return 
     */
    public Integer obtenerTiempoAtraso(Integer idProceso) {
        try {
            return pAdqDelegate.obtenerTiempoAtraso(idProceso);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;        
    }
    
    /**
     * Este método permite obtener el tiempo adelantado de un proceso.
     * @param idProceso
     * @return 
     */
    public Integer obtenerTiempoAdelanto(Integer idProceso) {
        try {
            return pAdqDelegate.obtenerTiempoAdelanto(idProceso);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;        
    }
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public PasosProcesoAdquisicion getEstado() {
        return estado;
    }

    public String getNombrePAC() {
        return nombrePAC;
    }

    public void setNombrePAC(String nombrePAC) {
        this.nombrePAC = nombrePAC;
    }

    public String getNroPAC() {
        return nroPAC;
    }

    public void setNroPAC(String nroPAC) {
        this.nroPAC = nroPAC;
    }

    public void setEstado(PasosProcesoAdquisicion estado) {
        this.estado = estado;
    }

    public SofisComboG<MetodoAdquisicion> getComboMetodoAdq() {
        return comboMetodoAdq;
    }

    public Date getMenorFechaInsumo() {
        return menorFechaInsumo;
    }

    public void setMenorFechaInsumo(Date menorFechaInsumo) {
        this.menorFechaInsumo = menorFechaInsumo;
    }

    public void setComboMetodoAdq(SofisComboG<MetodoAdquisicion> comboMetodoAdq) {
        this.comboMetodoAdq = comboMetodoAdq;
    }

    public SofisComboG<SsUsuario> getComboUsuarios() {
        return comboUsuarios;
    }

    public void setComboUsuarios(SofisComboG<SsUsuario> comboUsuarios) {
        this.comboUsuarios = comboUsuarios;
    }

    public Date getMenorFechaGantt() {
        return menorFechaGantt;
    }

    public void setMenorFechaGantt(Date menorFechaGantt) {
        this.menorFechaGantt = menorFechaGantt;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public String getNombre() {
        return nombre;
    }

    public VersionesDelegate getVersionDelegate() {
        return versionDelegate;
    }

    public SofisComboG<AnioFiscal> getComboAnioFiscal() {
        return comboAnioFiscal;
    }

    public void setComboAnioFiscal(SofisComboG<AnioFiscal> comboAnioFiscal) {
        this.comboAnioFiscal = comboAnioFiscal;
    }

    public SofisComboG<UnidadTecnica> getComboUnidadTecnica() {
        return comboUnidadTecnica;
    }

    public void setComboUnidadTecnica(SofisComboG<UnidadTecnica> comboUnidadTecnica) {
        this.comboUnidadTecnica = comboUnidadTecnica;
    }

    public void setVersionDelegate(VersionesDelegate versionDelegate) {
        this.versionDelegate = versionDelegate;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public static Logger getLogger() {
        return logger;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    // </editor-fold>
}
