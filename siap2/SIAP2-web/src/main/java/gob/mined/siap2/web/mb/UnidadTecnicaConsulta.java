/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroUsuario;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.UnidadTecnicaDelegate;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import gob.mined.siap2.web.genericos.constantes.ConstantesPresentacion;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.web.utils.SofisComboG;
import java.io.Serializable;
import java.util.ArrayList;
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
@Named(value = "unidadTecnicaConsulta")
public class UnidadTecnicaConsulta implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    UnidadTecnicaDelegate uniDelegate;

    @Inject
    UsuarioDelegate usuarioDelegate;

    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String filtroCodigo;
    private String filtroNombre;

    private UnidadTecnica editando = new UnidadTecnica();

    private SofisComboG<UnidadTecnica> comboPadres;

    @PostConstruct
    public void init() {

        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        filtroCodigo = null;
        filtroNombre = null;
    }

    /**
     * Inicializa los combos para el objeto en edición
     * 
     * @param id 
     */
    public void cargarCombos(Integer id) {
        comboPadres = new SofisComboG<>(uniDelegate.obtenerTodos(), "nombre");
        comboPadres.addEmptyItem(ConstantesPresentacion.DEFECTO_COMBO);
    }
    
    public List<SsUsuario> completeUsuarios(String query){
        try {
            FiltroUsuario filtro = new FiltroUsuario();
            filtro.setCodigo(query);
            filtro.setMaxResults(11L);
            return usuarioDelegate.getUsuariosByFiltro(filtro);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {

            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (!TextUtils.isEmpty(filtroCodigo)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.CONTAINS, "codigo", filtroCodigo);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(filtroNombre)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.CONTAINS, "nombre", filtroNombre);
                criterios.add(criterio);
            }

            CriteriaTO condicion = null;
            if (!criterios.isEmpty()) {
                if (criterios.size() == 1) {
                    condicion = criterios.get(0);
                } else {
                    condicion = CriteriaTOUtils.createANDTO(criterios
                            .toArray(new CriteriaTO[0]));
                }
            } else {
                // condición dummy para que el count by criteria funcione
                condicion = CriteriaTOUtils.createMatchCriteriaTO(
                        MatchCriteriaTO.types.NOT_NULL, "id", 1);
            }

            String[] propiedades = {"id", "nombre", "codigo", "padre.nombre", "direccion", "uniUsuario.usuCod"};

            String className = UnidadTecnica.class.getName();
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
     * Inicializa una unidad técnica hija
     * 
     * @param id 
     */
    public void agregarHijo(Integer id) {
        try {
            if (id != null) {
                cargarCombos(id);
                UnidadTecnica padre = uniDelegate.obtenerUnidadTecnicaPorId(id);
                if (padre != null) {
                    editando = new UnidadTecnica();
                    editando.setPadre(padre);
                    comboPadres.setSelectedT(padre);
                    editando.setDireccion(Boolean.FALSE);
                } else {
                    BusinessException bs = new BusinessException("No se pudo determinar el id del padre");
                    bs.addError(ConstantesErrores.ERR_GEST_UNIDAD_TECNICA_PADRE_NO_DETERMINADO);
                    throw bs;
                }
            } else {
                BusinessException bs = new BusinessException("No se pudo determinar el id del padre");
                bs.addError(ConstantesErrores.ERR_GEST_UNIDAD_TECNICA_ID_PADRE_NO_DETERMINADO);
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

    /**
     * Guarda la unidad en edición
     * 
     * @param id 
     */
    public void editar(Integer id) {
        try {
            if (id != null) {
                UnidadTecnica ut = uniDelegate.obtenerUnidadTecnicaPorId(id);
                cargarCombos(id);
                if (ut != null) {

                    editando = ut;
                    if (editando.getPadre() != null) {
                        comboPadres.setSelectedT(editando.getPadre());
                    }
                    if (editando.getDireccion() == null) {
                        editando.setDireccion(Boolean.FALSE);
                    }
                } else {
                    BusinessException bs = new BusinessException("No se pudo determinar la unidad tecnica.");
                    bs.addError(ConstantesErrores.ERR_GEST_UNIDAD_TECNICA_DETERMINADA);
                    throw bs;
                }
            } else {
                BusinessException bs = new BusinessException("No se pudo determinar el identificador de la unidad técnica.");
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


    
    /**
     * Guarda el objeto en edición
     */
    public void guardarEditando() {
        try {
            if(editando.getDireccion()==null){
                editando.setDireccion(Boolean.FALSE);
            }
            if(comboPadres!=null && comboPadres.getSelectedT()!=null){
                editando.setPadre(comboPadres.getSelectedT());
            }
            uniDelegate.guardar(editando);
            filterTable();
            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
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

    public String getFiltroCodigo() {
        return filtroCodigo;
    }

    public void setFiltroCodigo(String filtroCodigo) {
        this.filtroCodigo = filtroCodigo;
    }

    public UnidadTecnica getEditando() {
        return editando;
    }

    public void setEditando(UnidadTecnica editando) {
        this.editando = editando;
    }

    public String getFiltroNombre() {
        return filtroNombre;
    }

    public void setFiltroNombre(String filtroNombre) {
        this.filtroNombre = filtroNombre;
    }


    public SofisComboG<UnidadTecnica> getComboPadres() {
        return comboPadres;
    }

    public void setComboPadres(SofisComboG<UnidadTecnica> comboPadres) {
        this.comboPadres = comboPadres;
    }

    // </editor-fold>
}
