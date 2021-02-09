/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.POA;
import gob.mined.siap2.entities.data.impl.Programa;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadosPOA;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroUnidadTecnica;
import gob.mined.siap2.filtros.FiltroPrograma;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.web.delegates.UnidadTecnicaDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.delegates.impl.POADelegate;
import gob.mined.siap2.web.delegates.impl.ProgramaDelegate;
import gob.mined.siap2.web.delegates.impl.ProgramacionTrimestralDelegate;
import gob.mined.siap2.web.delegates.impl.VersionesDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.utils.SofisComboG;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "poaConsultaSeguimiento")
public class POAConsultaSeguimiento implements Serializable {
    @Inject
    JSFUtils jSFUtils;
    
    @Inject
    POADelegate poaDelegate;

    @Inject
    UsuarioDelegate usuarioDelegate;

    @Inject
    UnidadTecnicaDelegate unidadTecnicaDelegate;
    
    @Inject
    ProgramaDelegate progamaDelegate;
 
    @Inject
    VersionesDelegate versionDelegate;
    
    @Inject
    ProgramacionTrimestralDelegate progTrimestralDelegate;
    
    @Inject
    private UsuarioInfo usuarioInfo;
    
    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private SofisComboG<EstadosPOA> comboEstadosPOA;
    private UnidadTecnica filtroUnidadTecnicaSelected;
    private Programa filtroProgramaInstitucionalSelected;
    private Integer anioFiscalId;
    private POA editando = new POA();
    private Date desde;
    private Date hasta;
    private UnidadTecnica unidadTecnicaSelected;
    private Programa programaInstitucionalSelected;
    private AnioFiscal anioSelected;
    
    private Integer trimestreHabilitado = 0;
    @PostConstruct
    public void init() {
        cargarCombos();
        filterTable();
        
    }

    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        filtroUnidadTecnicaSelected = null;
        filtroProgramaInstitucionalSelected = null;
        desde = null;
        hasta = null;
    }
 /**
     * Inicializa los combos para el objeto en edición
     * 
     * @param id 
     */
    public void cargarCombos() {
        List<EstadosPOA> estados = new ArrayList();
        estados.add(EstadosPOA.EN_ELABORACION);
        estados.add(EstadosPOA.ENVIADO);
        estados.add(EstadosPOA.CERRADO);
        comboEstadosPOA = new SofisComboG(estados, "label");
        comboEstadosPOA.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
    }
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {

            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (filtroProgramaInstitucionalSelected != null && filtroProgramaInstitucionalSelected.getId() > 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.EQUALS, "programaInstitucional.id", filtroProgramaInstitucionalSelected.getId());
                criterios.add(criterio);
            }

            if (filtroUnidadTecnicaSelected != null && filtroUnidadTecnicaSelected.getId() > 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.EQUALS, "unidadTecnica.id", filtroUnidadTecnicaSelected.getId());
                criterios.add(criterio);
            }
            
            
            if (desde != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.GREATEREQUAL, "ultFechaModSeguimiento", desde);
                criterios.add(criterio);
            }
            if (hasta != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(hasta); 
                
                Date fechaHasta = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),23,59,59);
                
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.LESSEQUAL, "ultFechaModSeguimiento", fechaHasta);
                criterios.add(criterio);
            }
            
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", EstadosPOA.APROBADO);
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

            String[] propiedades = {"id","nombre", "unidadTecnica.nombre", "programaInstitucional.nombre", "anio.anio", "estado"};

            String className = POA.class.getName();
            String[] orderBy = {"id"};
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
     * Guarda la unidad en edición
     * 
     * @param id 
     */
    public void editar(Integer id) {
        try {
            if (id != null) {
                POA poa = poaDelegate.obtenerPOAPorId(id);
                if (poa != null) {
                    editando = poa;
                    anioFiscalId = editando.getAnio() != null ? editando.getAnio().getId() : null;

                } else {
                    BusinessException bs = new BusinessException("No se pudo determinar la POA.");
                    bs.addError(ConstantesErrores.ERR_GEST_UNIDAD_TECNICA_DETERMINADA);
                    throw bs;
                }
            } else {
                BusinessException bs = new BusinessException("No se pudo determinar el identificador de la POA");
                bs.addError(ConstantesErrores.ERR_GEST_UNIDAD_TECNICA_ID_NO_DETERMINADO);
                throw bs;
            }

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    public void modificarEstado() {
        try {
            editando.setEstado(comboEstadosPOA.getSelectedT());
            poaDelegate.cambiarEstadoPOA(editando);
            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
            filterTable();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    public List<UnidadTecnica> completeUnidadesTecnicas(String query){
        try {
            //if(query != null && !query.isEmpty()) {
                FiltroUnidadTecnica filtro = new FiltroUnidadTecnica();
                filtro.setNombre(query);
                filtro.setCodUsuario(usuarioInfo.getUserCod());
                filtro.setAscending(new boolean[]{true});
                filtro.setUnidadOperativa(Boolean.TRUE);
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setMaxResults(10L);
                filtro.setIncluirCampos(new String[]{"id","nombre","version"});
                filtro.setOperacion(ConstantesEstandares.Operaciones.GESTION_POA_UNIDADES_APOYO);
                return usuarioDelegate.getUTDeUsuarioConOperacionByNombre(filtro);
           // }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    public List<Programa> completeProgramasInstitucionales(String query){
        try {
            if(query != null && !query.isEmpty()) {
                FiltroPrograma filtro = new FiltroPrograma();
                filtro.setNombre(query);
                filtro.setAscending(new boolean[]{true});
                filtro.setOrderBy(new String[]{"nombre"});
                filtro.setMaxResults(11L);
                filtro.setIncluirCampos(new String[]{"id","nombre","version"});
                return progamaDelegate.obtenerProgramasPorFiltro(filtro);
            }
            
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

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public UnidadTecnica getFiltroUnidadTecnicaSelected() {
        return filtroUnidadTecnicaSelected;
    }

    public void setFiltroUnidadTecnicaSelected(UnidadTecnica filtroUnidadTecnicaSelected) {
        this.filtroUnidadTecnicaSelected = filtroUnidadTecnicaSelected;
    }

    public Programa getFiltroProgramaInstitucionalSelected() {
        return filtroProgramaInstitucionalSelected;
    }

    public void setFiltroProgramaInstitucionalSelected(Programa filtroProgramaInstitucionalSelected) {
        this.filtroProgramaInstitucionalSelected = filtroProgramaInstitucionalSelected;
    }

    public Integer getAnioFiscalId() {
        return anioFiscalId;
    }

    public void setAnioFiscalId(Integer anioFiscalId) {
        this.anioFiscalId = anioFiscalId;
    }

    public POA getEditando() {
        return editando;
    }

    public void setEditando(POA editando) {
        this.editando = editando;
    }

    public UnidadTecnica getUnidadTecnicaSelected() {
        return unidadTecnicaSelected;
    }

    public void setUnidadTecnicaSelected(UnidadTecnica unidadTecnicaSelected) {
        this.unidadTecnicaSelected = unidadTecnicaSelected;
    }

    public Programa getProgramaInstitucionalSelected() {
        return programaInstitucionalSelected;
    }

    public void setProgramaInstitucionalSelected(Programa programaInstitucionalSelected) {
        this.programaInstitucionalSelected = programaInstitucionalSelected;
    }

    public AnioFiscal getAnioSelected() {
        return anioSelected;
    }

    public void setAnioSelected(AnioFiscal anioSelected) {
        this.anioSelected = anioSelected;
    }
    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }
    
    public SofisComboG<EstadosPOA> getComboEstadosPOA() {
        return comboEstadosPOA;
    }

    public void setComboEstadosPOA(SofisComboG<EstadosPOA> comboEstadosPOA) {
        this.comboEstadosPOA = comboEstadosPOA;
    }
    public Integer getTrimestreHabilitado() {
        return trimestreHabilitado;
    }

    public void setTrimestreHabilitado(Integer trimestreHabilitado) {
        this.trimestreHabilitado = trimestreHabilitado;
    }
    // </editor-fold>
    
}
