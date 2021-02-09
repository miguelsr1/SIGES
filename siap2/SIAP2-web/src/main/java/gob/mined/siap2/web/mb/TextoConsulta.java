/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Texto;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "textoConsulta")
public class TextoConsulta implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    ApplicationTextMB applicationTextMB;

    private LazyDataModel lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String textoValor;
    private String textoCodigo;

    @PostConstruct
    public void init() {
        filterTable();
    }

    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter() {
        textoValor = null;
        textoCodigo = null;
    }

    private String elementoOrdenacion = "texId";
    private String ascendente = "Descendente";

    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("tableForm:dataTable");
            if (dataTable != null) {
                dataTable.resetValue();
            }

            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if (!TextUtils.isEmpty(textoValor)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "texValor", textoValor);
                criterios.add(criterio);
            }

            if (!TextUtils.isEmpty(textoCodigo)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(
                    MatchCriteriaTO.types.CONTAINS, "texCodigo", textoCodigo);
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
                    MatchCriteriaTO.types.NOT_NULL, "texId", 1);
            }

            String[] propiedades = {"texId", "texCodigo", "texValor", "texHabilitado", "texIdiId.idiDescripcion"};

            String className = Texto.class.getName();
            String[] orderBy = {elementoOrdenacion};
            boolean[] asc = {"Ascendente".equals(ascendente)};

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

    private Texto editando = new Texto();

    public void cargarToEditar(Integer id) {
        editando = (Texto) emd.getEntityById(Texto.class.getName(), id);
    }

    /**
     * Guarda el objeto en edición
     */
    public void guardarEditando() {
        try {
            emd.saveOrUpdate(editando);
            filterTable();
            applicationTextMB.limpiarCacheTexto(editando);
            RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    public void eliminarTexto(Integer idToEliminar) {
        try {
            Texto texto = (Texto) emd.getEntityById(Texto.class.getName(), idToEliminar);
            applicationTextMB.limpiarCacheTexto(texto);
            
            emd.delete(Texto.class, Integer.valueOf(idToEliminar));
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
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

    public String getElementoOrdenacion() {
        return elementoOrdenacion;
    }

    public String getTextoValor() {
        return textoValor;
    }

    public void setTextoValor(String textoValor) {
        this.textoValor = textoValor;
    }

    public String getTextoCodigo() {
        return textoCodigo;
    }

    public void setTextoCodigo(String textoCodigo) {
        this.textoCodigo = textoCodigo;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public Texto getEditando() {
        return editando;
    }

    public void setEditando(Texto editando) {
        this.editando = editando;
    }

    public void setElementoOrdenacion(String elementoOrdenacion) {
        this.elementoOrdenacion = elementoOrdenacion;
    }

    public String getAscendente() {
        return ascendente;
    }

    public void setAscendente(String ascendente) {
        this.ascendente = ascendente;
    }

    // </editor-fold>
}
