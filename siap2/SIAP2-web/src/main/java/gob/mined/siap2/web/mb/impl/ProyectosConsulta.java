/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.enums.EstadoProyecto;
import gob.mined.siap2.entities.enums.TipoProyecto;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.sofisform.to.OR_TO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.ProyectoDelegate;
import gob.mined.siap2.web.delegates.impl.PlanificacionEstrategicaDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.mb.UtilsMB;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que realiza la consulta de Proyectos
 * 
 *
 * @author sofis
 */
@ViewScoped
@Named(value = "proyectoConsulta")
public class ProyectosConsulta implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;
    @Inject
    VersionesDelegate versionDelegate;
    @Inject
    UtilsMB utilsMB;
    @Inject
    TextMB textMB;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    PlanificacionEstrategicaDelegate planificacionDelegate;
    @Inject
    ProyectoDelegate progDelegate;
    @Inject
    PermisosMB permisosMB;

    private LazyDataModel lazyModel;
    private String codigoSIP;
    private String nombre;
    private TipoProyecto tipoProyecto;

    private String idPlanificacion;
    private String idLinea;
    private String idProgramaPresupuestario;
    private String idSubProgramaPresupuestario;
    private String idProgramaInstitucional;
    private String unidadTecnicaId;
    private String tmpIdFuenteFinanciamiento;
    private String tmpIdFuenteRecurso;
    private String restriccionXTipoProyecto;
    private String programaAsociado;
    private String estadoProyecto = "0";
    private List<TipoProyecto> tipoProyectosDisponibles = new ArrayList();

    private static final String FILTRO_SIN_PROGRAMA_ASOCIADO = "FILTRO_SIN_PROGRAMA_ASOCIADO";
    private static final String FILTRO_CON_PROGRAMAS_ASOCIADO = "FILTRO_CON_PROGRAMAS_ASOCIADO";

    /**
     * Creates a new instance of ProgramaInstitucionalMB
     */
    public ProyectosConsulta() {
    }

    @PostConstruct
    public void init() {
        //busca la restrincion en flash
        restriccionXTipoProyecto = (String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getFlash()
                .get("restriccionXTipoProyecto");
        //si no la encuentra la saca de la url
        if (TextUtils.isEmpty(restriccionXTipoProyecto)) {
            restriccionXTipoProyecto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("restriccionXTipoProyecto");
        }
        //Carga los tipo de proyectos disponibles para los filtros
        if (!TextUtils.isEmpty(restriccionXTipoProyecto)) {
            switch (restriccionXTipoProyecto) {
                case "1":
                    tipoProyectosDisponibles.add(TipoProyecto.ADMINISTRATIVO);
                    break;
                case "2":
                    tipoProyectosDisponibles.add(TipoProyecto.INVERSION);
                    tipoProyectosDisponibles.add(TipoProyecto.NO_INVERSION);
                    break;
            }
        } else {
            tipoProyectosDisponibles.add(TipoProyecto.ADMINISTRATIVO);
            tipoProyectosDisponibles.add(TipoProyecto.INVERSION);
            tipoProyectosDisponibles.add(TipoProyecto.NO_INVERSION);
        }

        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        codigoSIP = null;
        nombre = null;
        tipoProyecto = null;
        idPlanificacion = null;
        idLinea = null;
        idProgramaPresupuestario = null;
        idProgramaInstitucional = null;
        unidadTecnicaId = null;
        tmpIdFuenteFinanciamiento = null;
        tmpIdFuenteRecurso = null;
        programaAsociado = null;
        idSubProgramaPresupuestario = null;
    }

    
    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            //--- Condición de permisos sobre proyectos -------------------------------------------------------------------
            if (restriccionXTipoProyecto != null && restriccionXTipoProyecto.equals("1")) {//Proyecto administrativo
                CriteriaTO administrativo = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tipoProyecto", TipoProyecto.ADMINISTRATIVO);
                criterios.add(administrativo);
            } else if (restriccionXTipoProyecto != null && restriccionXTipoProyecto.equals("2")) {//Proyecto inversion, no-inversion
                CriteriaTO administrativo = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "tipoProyecto", TipoProyecto.ADMINISTRATIVO);
                criterios.add(administrativo);
            }

            if (!TextUtils.isEmpty(codigoSIP)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "codigoSIIP", codigoSIP);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(nombre)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombre", nombre);
                criterios.add(criterio);
            }

            if (tipoProyecto != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tipoProyecto", tipoProyecto);
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(idProgramaPresupuestario)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "programaPresupuestario.programaPresupuestario.id", Integer.valueOf(idProgramaPresupuestario));
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(idSubProgramaPresupuestario)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "programaPresupuestario.id", Integer.valueOf(idSubProgramaPresupuestario));
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(idProgramaInstitucional)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "programaInstitucional.id", Integer.valueOf(idProgramaInstitucional));
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(unidadTecnicaId)) {
                MatchCriteriaTO criterioComponente = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "proyectoComponentes.unidadTecnica.id", Integer.valueOf(unidadTecnicaId));
                MatchCriteriaTO criterioMacroActividades = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "proyectoMacroactividad.unidadTecnica.id", Integer.valueOf(unidadTecnicaId));
                OR_TO or = CriteriaTOUtils.createORTO(criterioComponente, criterioMacroActividades);
                criterios.add(or);
            }

            if (!TextUtils.isEmpty(tmpIdFuenteFinanciamiento)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuentesProyecto.fuenteFinanciamiento.id", Integer.valueOf(tmpIdFuenteFinanciamiento));
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(tmpIdFuenteRecurso)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuentesProyecto.fuenteRecursos.id", Integer.valueOf(tmpIdFuenteRecurso));
                criterios.add(criterio);
            }
            if (estadoProyecto != null && !"0".equals(estadoProyecto)) {
                EstadoProyecto estado = EstadoProyecto.EN_FORMULACION;
                switch (estadoProyecto) {
                    case "1":
                        estado = EstadoProyecto.EN_FORMULACION;
                        break;
                    case "2":
                        estado = EstadoProyecto.BLOQUEADO;
                        break;
                    default:
                        estado = EstadoProyecto.EN_FORMULACION;
                        break;
                }
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", estado);
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(programaAsociado)) {
                if (FILTRO_CON_PROGRAMAS_ASOCIADO.equals(programaAsociado)) {
                    MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "programaPresupuestario", 1);
                    MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "programaInstitucional", 1);
                    criterios.add(CriteriaTOUtils.createORTO(criterio1, criterio2));
                } else if (FILTRO_SIN_PROGRAMA_ASOCIADO.equals(programaAsociado)) {
                    MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "programaPresupuestario", 1);
                    MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "programaInstitucional", 1); 
                    criterios.add(criterio1);
                    criterios.add(criterio2);

                }
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

            String[] propiedades = {"id", "nombre", "codigoSIIP", "tipoProyecto", "programaPresupuestario.nombre", "programaInstitucional.nombre", "programaPresupuestario.programaPresupuestario.nombre",
                "programaPresupuestario.nombre", "estado"};

            String className = Proyecto.class.getName();
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
     * Este método corresponde al evento de eliminación del objeto seleccionado.
     *
     * @param idToEliminar
     */
    public void eliminar(Integer idToEliminar) {
        try {
            progDelegate.eleiminarProyecto(Integer.valueOf(idToEliminar));
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método es el encargado de generar los POAS para un proyecto
     * 
     * @param idProyecto 
     */
    public void generarPOAS(Integer idProyecto) {
        try {
            progDelegate.generarPOAsPAraProyevo(idProyecto);
            String texto = textMB.obtenerTexto("labels.POAGeneradoCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Devuelve la etiqueta utilizada para mostrar en un proyecto
     * @return 
     */
    public String getLabelNombreProyecto() {
        if (restriccionXTipoProyecto == null) {
            return "labels.consultaProyectos";
        }
        if (restriccionXTipoProyecto.equals("1")) {
            return "labels.consultaProyectosAdministrativo";
        } else {
            return "labels.consultaProyectosInversion-NoInversion";
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    
    public List<TipoProyecto> getTipoProyectosDisponibles() {
        return tipoProyectosDisponibles;
    }

    public void setTipoProyectosDisponibles(List<TipoProyecto> tipoProyectosDisponibles) {
        this.tipoProyectosDisponibles = tipoProyectosDisponibles;
    }

    public String getRestriccionXTipoProyecto() {
        return restriccionXTipoProyecto;
    }

    public void setRestriccionXTipoProyecto(String restriccionXTipoProyecto) {
        this.restriccionXTipoProyecto = restriccionXTipoProyecto;
    }

    public String getFILTRO_SIN_PROGRAMA_ASOCIADO() {
        return FILTRO_SIN_PROGRAMA_ASOCIADO;
    }

    public String getFILTRO_CON_PROGRAMAS_ASOCIADO() {
        return FILTRO_CON_PROGRAMAS_ASOCIADO;
    }

    public String getProgramaAsociado() {
        return programaAsociado;
    }

    public void setProgramaAsociado(String programaAsociado) {
        this.programaAsociado = programaAsociado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoProyecto getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(TipoProyecto tipoProyecto) {
        this.tipoProyecto = tipoProyecto;
    }

    public String getIdPlanificacion() {
        return idPlanificacion;
    }

    public String getUnidadTecnicaId() {
        return unidadTecnicaId;
    }

    public String getTmpIdFuenteFinanciamiento() {
        return tmpIdFuenteFinanciamiento;
    }

    public void setTmpIdFuenteFinanciamiento(String tmpIdFuenteFinanciamiento) {
        this.tmpIdFuenteFinanciamiento = tmpIdFuenteFinanciamiento;
    }

    public String getTmpIdFuenteRecurso() {
        return tmpIdFuenteRecurso;
    }

    public void setTmpIdFuenteRecurso(String tmpIdFuenteRecurso) {
        this.tmpIdFuenteRecurso = tmpIdFuenteRecurso;
    }

    public void setUnidadTecnicaId(String unidadTecnicaId) {
        this.unidadTecnicaId = unidadTecnicaId;
    }

    public String getCodigoSIP() {
        return codigoSIP;
    }

    public void setCodigoSIP(String codigoSIP) {
        this.codigoSIP = codigoSIP;
    }

    public void setIdPlanificacion(String idPlanificacion) {
        this.idPlanificacion = idPlanificacion;
    }

    public String getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(String idLinea) {
        this.idLinea = idLinea;
    }

    public String getIdProgramaPresupuestario() {
        return idProgramaPresupuestario;
    }

    public void setIdProgramaPresupuestario(String idProgramaPresupuestario) {
        this.idProgramaPresupuestario = idProgramaPresupuestario;
    }

    public String getIdProgramaInstitucional() {
        return idProgramaInstitucional;
    }

    public void setIdProgramaInstitucional(String idProgramaInstitucional) {
        this.idProgramaInstitucional = idProgramaInstitucional;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public String getIdSubProgramaPresupuestario() {
        return idSubProgramaPresupuestario;
    }

    public void setIdSubProgramaPresupuestario(String idSubProgramaPresupuestario) {
        this.idSubProgramaPresupuestario = idSubProgramaPresupuestario;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public String getEstadoProyecto() {
        return estadoProyecto;
    }

    public void setEstadoProyecto(String estadoProyecto) {
        this.estadoProyecto = estadoProyecto;
    }

    // </editor-fold>
}
