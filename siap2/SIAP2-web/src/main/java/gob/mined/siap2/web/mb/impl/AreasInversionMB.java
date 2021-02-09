package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.CatalogoInsumosBean;
import gob.mined.siap2.business.ejbs.impl.AreasInversionBean;
import gob.mined.siap2.business.ejbs.impl.RelacionAreasInversionInsumoBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AreasInversion;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.entities.data.impl.RelacionAreasInversionInsumo;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroInsumo;
import gob.mined.siap2.persistence.dao.exceptions.DAOConstraintViolationException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.dataProvider.EntityReferenceDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

@ViewScoped
@Named(value = "areasInversionMB")
public class AreasInversionMB implements Serializable{
 
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    
    
    private AreasInversion filtroAreasInversion;
    private AreasInversion areasInversion;
    private AreasInversion subAreasInversion;
    private RelacionAreasInversionInsumo areasInversionInsumo;
    
    private List<AreasInversion> listaAreasInversion; //Lista de subareas
    private List<AreasInversion> listaAreasInversionSubArea; //Lista utlizada en filtros de busqueda
    private List<AreasInversion> listaHistoricoAreasInversion;
    private List<RelacionAreasInversionInsumo> listaRelacion;
    
    protected LazyDataModel lazyModel;
    
    private String filtroHabilitado;
    private String codigoInsumo;
    private String nombreInsumo;
    private String conocidoPor;        
    @Inject
    private EntityManagementDelegate emd;
    
    @Inject
    private JSFUtils jSFUtils;
    
    @Inject
    private AreasInversionBean areasInversionBean;
    
    @Inject 
    private RelacionAreasInversionInsumoBean areasInversionInsumoBean;

    @Inject
    private CatalogoInsumosBean catalogoInsumoBean;
  
    
    @PostConstruct
    public void init() {
        String id = buscarIdentificador("id");
        String sub = buscarIdentificador("sub");
         
        cleanObject();
        if (!TextUtils.isEmpty(id)) {
            asignarEdicion(Integer.parseInt(id));
            findListaSubAreasInversionByIdPadre(Integer.parseInt(id));
        }
        if (!TextUtils.isEmpty(sub)) {
            asignarEdicionSubAreaInversion(Integer.parseInt(sub));
            findRelacionesPorSubArea(Integer.parseInt(sub));
        }
        
    }
    
    /**
     * Este método busca la llave de los parametros enviados desde la vista y obtiene su valor
     * @param parametro
     * @return 
     */
    public String buscarIdentificador(String parametro){
        try {
            String valor = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(parametro);
            if(valor == null){
                valor = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get(parametro);
            }
            return valor;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    /**
     * Este método inicializa los objetos base del mantenimiento
     */
    public void cleanObject() {
        this.areasInversion = inicializarObjetos(null);
        this.subAreasInversion = inicializarObjetos(null);
        this.filtroAreasInversion = inicializarObjetos(null);
        this.areasInversionInsumo = inicializarObjetosRelacion(null);
        filterTable();
    }
    
    
    
    
    
    /**
     * Este método inicializa un objeto de Áreas de Inversión
     * @param local
     * @return 
     */
    public AreasInversion inicializarObjetos(AreasInversion local){
        if(local == null){
            local = new AreasInversion();
        }
        inicializarRelaciones(local);
        return local;
    }
    
    public RelacionAreasInversionInsumo inicializarObjetosRelacion(RelacionAreasInversionInsumo local){
        if(local == null){
            local = new RelacionAreasInversionInsumo();
        }
        inicializarRelacionesRelacion(local);
        return local;
    }
    
    /**
     * Este metodo inicializa las relaciones de Áreas de Inversión
     * @param local 
     */
    public void inicializarRelaciones(AreasInversion local){
        if(local.getAreaPadre() == null)
            local.setAreaPadre(new AreasInversion());
    }
    
    /**
     * Este metodo inicializa las relaciones de una relacion de Areas de inversion e insumo
     * @param local 
     */
    public void inicializarRelacionesRelacion(RelacionAreasInversionInsumo local){
        if(local.getAreaInversion() == null)
            local.setAreaInversion(new AreasInversion());
        if(local.getInsumo() == null)
            local.setInsumo(new Insumo());
    }
   
    
    
    
    
    /**
     * Metodo utilizado para buscar un registro de AreasInversion, filtrado por ID
     * @param id
     */
    public void findAreasInversionById(Integer id) {
        try {
            setAreasInversion(areasInversionBean.getAreaInversionById(id));
            inicializarRelaciones(getAreasInversion());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Metodo utilizado para buscar un registro de AreasInversion, filtrado por ID
     * @param id
     */
    public void findSubAreasInversionById(Integer id) {
        try {
            setSubAreasInversion(areasInversionBean.getAreaInversionById(id));
            inicializarRelaciones(getSubAreasInversion());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Metodo utilizado para buscar todos los registros de AreasInversion que pertenezcan al mismo padre
     * @param id
     */
    public void findListaSubAreasInversionByIdPadre(Integer id) {
        try {
            setListaAreasInversion(areasInversionBean.getAreasInversionByIdPadre(id));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    /**
     * Método utilizado para cargar los registros de relaciones de áreas e insumos, por ID de área
     * @param sub 
     */
    public void findRelacionesPorSubArea(Integer sub){
        try {
            setListaRelacion(areasInversionInsumoBean.getRelacionByAreaInversionId(sub));
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    
    
    
    
    /**
     * Este método busca un registro de áreas de inversión por su ID
     * y asigna el resultado a un contexto de edición
     * @param id 
     */
    public void asignarEdicion(Integer id) {
        findAreasInversionById(id);
    }
    
    /**
     * Este método busca un registro de áreas de inversión por su ID
     * y asigna el resultado a un contexto de edición
     * @param id 
     */
    public void asignarEdicionSubAreaInversion(Integer id) {
        findSubAreasInversionById(id);
    }
    
    /**
     * Metodo utilizado para crear y/o actualizar un registro de Áreas de Inversión
     * @return
     */
    public String guardarActualizar() {
        try {
            verificarRelaciones();
            areasInversionBean.crearActualizar(getAreasInversion());
            cleanObject();
            return "consultaAreasInversion.xhtml?faces-redirect=true";
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
     * Metodo utilizado para crear y/o actualizar un registro de Áreas de Inversión
     * @return
     */
    public String guardarActualizarSubAreaInversion() {
        try {
            getSubAreasInversion().setAreaPadre(getAreasInversion());
            areasInversionBean.crearActualizar(getSubAreasInversion());
            FacesContext.getCurrentInstance().getExternalContext().getFlash().put("id", getAreasInversion().getId().toString());
            
            jSFUtils.agregarInfo("Registro guardado correctamente");
            return "crearEditarAreasInversion.xhtml?faces-redirect=true";
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
     * Este método busca un registro de Insumo con el codigo ingresado, si lo encuentra, lo asigna 
     * a la relacion y llama el metodo de guardar Relacion
     */
    public void buscarIsumo(){
        try {
            FiltroInsumo filtro = new FiltroInsumo();
            filtro.setCodigo(codigoInsumo);
            filtro.setNombre(nombreInsumo);
            filtro.setConocidoPor(conocidoPor);
            filtro.setMaxResults(1L);
            filtro.setOrderBy(new String[]{"codigo"});
            filtro.setAscending(new boolean[]{true});
            List<Insumo> lista = catalogoInsumoBean.obtenerPorFiltro(filtro);
            if(lista != null && !lista.isEmpty() && lista.size() > 0) {
                Insumo ins = lista.get(0);
                if(ins != null){
                    getAreasInversionInsumo().setInsumo(ins);
                    guardarActualizarRelacion();
                }
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_CODIGO_DE_INSUMO_NO_EXISTE );
            
        }
    }
    
    /**
     * Metodo utilizado para crear y/o actualizar un registro de Relacion entre Áreas de Inversión e Insumos
     */
    public void guardarActualizarRelacion() {
        try {
            getAreasInversionInsumo().setAreaInversion(getSubAreasInversion());
            areasInversionInsumoBean.crearActualizar(getAreasInversionInsumo());
            findRelacionesPorSubArea(getSubAreasInversion().getId());
            
        } catch (DAOConstraintViolationException e) {
            logger.log(Level.SEVERE, null, e);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERR_CODIGO_INSUMO_REPETIDO);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");

        } catch (Exception ex) {
            jSFUtils.mostrarErrorByPropertieCode(ex.getMessage());
            logger.log(Level.SEVERE, null, ex);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
            
        } finally{
            this.areasInversionInsumo = inicializarObjetosRelacion(null);
            this.codigoInsumo = null;
        }
    }
    
    
    
    /**
     * Metodo utilizado para eliminar un registro de Áreas de inversión
     * @param id ID del registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            areasInversionBean.eliminar(id);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } finally {
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        }
    }
    
    /**
     * Metodo utilizado para eliminar un registro de Relacion entre Áreas de inversión e Insumo
     * @param id 
     */
    public void eliminarRelacion(Integer id){
       try {
            areasInversionInsumoBean.eliminar(id);
            this.codigoInsumo = null;
            findRelacionesPorSubArea(getSubAreasInversion().getId());
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } finally {
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        }
    }
    
    
    
    
    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            
            MatchCriteriaTO criterio0 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "areaPadre.id", 1);
            criterios.add(criterio0);
            
            if (!TextUtils.isEmpty(getFiltroAreasInversion().getCodigo())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "codigo", getFiltroAreasInversion().getCodigo());
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(getFiltroAreasInversion().getNombre())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "nombre", getFiltroAreasInversion().getNombre());
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(getFiltroAreasInversion().getDescripcion())) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "descripcion", getFiltroAreasInversion().getDescripcion());
                criterios.add(criterio);
            }            
            
            if (getFiltroHabilitado() != null && !getFiltroHabilitado().isEmpty()) {
                if (getFiltroHabilitado().equals("true")) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", true);
                    criterios.add(criterio);
                } else if(getFiltroHabilitado().equals("false")){
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", false);
                    criterios.add(criterio);
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

            String[] propiedades = {"id", "codigo", "nombre", "descripcion", "habilitado", "orden"};
            String className = AreasInversion.class.getName();
            String[] orderBy = {"id"};
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
     * Este método carga el listado historico del registro seleccionado
     * @param id 
     */
    public void cargarHistorico(Integer id) {
        this.listaHistoricoAreasInversion = emd.obtenerHistorico(AreasInversion.class, id);
    }
    
    /**
     * Este método verifica que el contenido de los objetos que son relaciones de un área de Inversión
     */
    public void verificarRelaciones(){
        if(getAreasInversion().getAreaPadre()!= null && (getAreasInversion().getAreaPadre().getId() == null || getAreasInversion().getAreaPadre().getId() == 0)){
            getAreasInversion().setAreaPadre(null);
        }
    }
    
    /**
     * Dirige al sitio de Areas de inversión
     * @return
     */
    public String cerrar() {
        return "consultaAreasInversion.xhtml?faces-redirect=true";
    }
    
    /**
     * Dirige al sitio de Areas de inversión
     * @return
     */
    public String cerrarSubArea() {
        return "crearEditarAreasInversion.xhtml?faces-redirect=true";
    }
    
    /**
     * Este método se utiliza para generar un archivo Workbook a partir de un listado de AreasInversion
     */
    public void exportarAreas(){
        try {           
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            
            List<AreasInversion> lista = areasInversionBean.getAreasInversion();
            HSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("id");
            row.createCell(1).setCellValue("codigo");
            row.createCell(2).setCellValue("nombre");
            row.createCell(3).setCellValue("descripcion");
            row.createCell(4).setCellValue("habilitado");
            row.createCell(5).setCellValue("orden");
            row.createCell(6).setCellValue("areaPadre");
            
            HSSFRow row2;
            for(int i = 0; i < lista.size(); i++){
                row2 = sheet.createRow(i+1);
                row2.createCell(0).setCellValue(verificarValor(lista.get(i).getId()));
                row2.createCell(1).setCellValue(verificarValor(lista.get(i).getCodigo()));
                row2.createCell(2).setCellValue(verificarValor(lista.get(i).getNombre()));
                row2.createCell(3).setCellValue(verificarValor(lista.get(i).getDescripcion()));
                row2.createCell(4).setCellValue(verificarValor(lista.get(i).getHabilitado()));
                row2.createCell(5).setCellValue(verificarValor(lista.get(i).getOrden()));
                row2.createCell(6).setCellValue(verificarValor(lista.get(i).getAreaPadre()));
            }

            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
    
    /**
     * Este método se utiliza para verificar el valor de los atributos de un objeto y devolver si respectivo valor en texto o nulo
     * 
     * @param objeto
     * @return 
     */
    public String verificarValor(Object objeto){
        if(objeto != null){
            if(objeto instanceof AreasInversion){
                return ((AreasInversion) objeto).getId().toString();
            }
            return objeto.toString();
        }
        return "";
    }
    
    
    
    
    
    
    
    
    
    
    public AreasInversion getAreasInversion() {
        return areasInversion;
    }

    public void setAreasInversion(AreasInversion areasInversion) {
        this.areasInversion = areasInversion;
    }

    public List<AreasInversion> getListaAreasInversion() {
        return listaAreasInversion;
    }

    public void setListaAreasInversion(List<AreasInversion> listaAreasInversion) {
        this.listaAreasInversion = listaAreasInversion;
    }

    public AreasInversion getFiltroAreasInversion() {
        return filtroAreasInversion;
    }

    public void setFiltroAreasInversion(AreasInversion filtroAreasInversion) {
        this.filtroAreasInversion = filtroAreasInversion;
    }

    public String getFiltroHabilitado() {
        return filtroHabilitado;
    }

    public void setFiltroHabilitado(String filtroHabilitado) {
        this.filtroHabilitado = filtroHabilitado;
    }

    public List<AreasInversion> getListaHistoricoAreasInversion() {
        return listaHistoricoAreasInversion;
    }

    public void setListaHistoricoAreasInversion(List<AreasInversion> listaHistoricoAreasInversion) {
        this.listaHistoricoAreasInversion = listaHistoricoAreasInversion;
    }

    public List<AreasInversion> getListaAreasInversionSubArea() {
        return listaAreasInversionSubArea;
    }

    public void setListaAreasInversionSubArea(List<AreasInversion> listaAreasInversionSubArea) {
        this.listaAreasInversionSubArea = listaAreasInversionSubArea;
    }

    public LazyDataModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public AreasInversion getSubAreasInversion() {
        return subAreasInversion;
    }

    public void setSubAreasInversion(AreasInversion subAreasInversion) {
        this.subAreasInversion = subAreasInversion;
    }

    public List<RelacionAreasInversionInsumo> getListaRelacion() {
        return listaRelacion;
    }

    public void setListaRelacion(List<RelacionAreasInversionInsumo> listaRelacion) {
        this.listaRelacion = listaRelacion;
    }

    public RelacionAreasInversionInsumo getAreasInversionInsumo() {
        return areasInversionInsumo;
    }

    public void setAreasInversionInsumo(RelacionAreasInversionInsumo areasInversionInsumo) {
        this.areasInversionInsumo = areasInversionInsumo;
    }

    public String getCodigoInsumo() {
        return codigoInsumo;
    }

    public void setCodigoInsumo(String codigoInsumo) {
        this.codigoInsumo = codigoInsumo;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public String getConocidoPor() {
        return conocidoPor;
    }

    public void setConocidoPor(String conocidoPor) {
        this.conocidoPor = conocidoPor;
    }
    
    
    
    
}
