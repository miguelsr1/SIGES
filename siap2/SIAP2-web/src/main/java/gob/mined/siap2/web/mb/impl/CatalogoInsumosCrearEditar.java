/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.entities.data.impl.ObjetoEspecificoGasto;
import gob.mined.siap2.entities.data.impl.Programa;
import gob.mined.siges.entities.data.impl.SgAfTipoBienes;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.filtros.FiltroPrograma;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.BienesDepreciablesDelegate;
import gob.mined.siap2.web.delegates.impl.CatalogoInsumosDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.ArchivoUtils;
import gob.mined.siap2.web.utils.OEGUtils;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "catalogoInsumosCE")
public class CatalogoInsumosCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    CatalogoInsumosDelegate catalogoInsumosDelegate;

    @Inject
    BienesDepreciablesDelegate bienesDepreciablesDelegate;
    
    private boolean update = false;
    private Insumo objeto;
    
    private String idSegemnto;
    private String idFamilia;
    private String idClase;
    private String idArticulo;

    private List<String> oegs ;

    private UploadedFile file;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            update = true;
            objeto = (Insumo) emd.getEntityById(Insumo.class.getName(), Integer.valueOf(id));
            
           if(objeto.getTipoBien() != null) {
                String codigoNombreCategoria = objeto.getTipoBien().getTbiCodigo() != null ? objeto.getTipoBien().getTbiCodigo().trim() : "";
                if(StringUtils.isNotBlank(objeto.getTipoBien().getTbiNombre())) {
                    codigoNombreCategoria += "-" + objeto.getTipoBien().getTbiNombre();
                }
                if(this.objeto.getTipoBien().getTbiCategoriaBienes() != null && this.objeto.getTipoBien().getTbiCategoriaBienes().getCabNombre() != null 
                        && StringUtils.isNotBlank(this.objeto.getTipoBien().getTbiCategoriaBienes().getCabNombre())) {
                    codigoNombreCategoria += "-" + this.objeto.getTipoBien().getTbiCategoriaBienes().getCabNombre().trim();
                }
                objeto.getTipoBien().setTbiCodigoNombreCategoria(codigoNombreCategoria);
            }
            
            
            idArticulo = objeto.getArticulo().getId().toString();
            idClase =  objeto.getArticulo().getPadre().getId().toString();
            idFamilia =  objeto.getArticulo().getPadre().getPadre().getId().toString();
            idSegemnto =  objeto.getArticulo().getPadre().getPadre().getPadre().getId().toString();

        } else {
            objeto = new Insumo();          
        }        
        
       oegs=OEGUtils.oegToList(objeto.getObjetoEspecificoGasto());
    }
    
    /**
     * Devuelve en un texto la lista de objetos específicos de gasto
     * @return 
     */
    private String getOEGString(){
        String oeg ="";
        for (String iter : oegs){
            oeg= oeg + iter;
        }
        return oeg;
    }

    /**
     * Devuelve un objeto específico de gasto
     * @return 
     */
    public ObjetoEspecificoGasto getObjetoEspecificoGasto(){
        String oeg= getOEGString();
        if (TextUtils.isEmpty(oeg)){
            return null;
        }
        Integer codigo = Integer.valueOf(oeg);
        return (ObjetoEspecificoGasto) emd.getEntityById(ObjetoEspecificoGasto.class.getName(), codigo);
        
    }
    
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar()  {
        try {
            ObjetoEspecificoGasto oeg = getObjetoEspecificoGasto();
            if (oeg== null){
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_SELECCIONE_UN_OEG_EXISTENTE);
                throw b;
            }
            objeto.setObjetoEspecificoGasto(oeg);
            objeto.setTempUploadedFile(ArchivoUtils.getDataFile(file));
            catalogoInsumosDelegate.crearOActualizarCatalogoInsumos(objeto, Integer.valueOf(idArticulo));        
            return "consultaCatalogoDeInsumos.xhtml?faces-redirect=true";   
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

    public List<SgAfTipoBienes> completeTiposBienes(String query){
        try {
            if(query != null && !query.isEmpty()) {
                return bienesDepreciablesDelegate.obtenerTiposBienPorQuery(query);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    /**
     * Redirige el sitio a la consulta de insumos
     * @return 
     */
    public String cerrar() {
        return "consultaCatalogoDeInsumos.xhtml?faces-redirect=true";   
    }

    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public static Logger getLogger() {
        return logger;
    }

    public String getIdSegemnto() {
        return idSegemnto;
    }

    public void setIdSegemnto(String idSegemnto) {
        this.idSegemnto = idSegemnto;
    }

    public List<String> getOegs() {
        return oegs;
    }

    public void setOegs(List<String> oegs) {
        this.oegs = oegs;
    }

    public String getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(String idFamilia) {
        this.idFamilia = idFamilia;
    }

    public String getIdClase() {
        return idClase;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void setIdClase(String idClase) {
        this.idClase = idClase;
    }

    public String getIdArticulo() {
        return idArticulo;
    }

    public CatalogoInsumosDelegate getCatalogoInsumosDelegate() {
        return catalogoInsumosDelegate;
    }

    public void setCatalogoInsumosDelegate(CatalogoInsumosDelegate catalogoInsumosDelegate) {
        this.catalogoInsumosDelegate = catalogoInsumosDelegate;
    }

    public void setIdArticulo(String idArticulo) {
        this.idArticulo = idArticulo;
    }

 
    public Insumo getObjeto() {
        return objeto;
    }

    public void setObjeto(Insumo objeto) {
        this.objeto = objeto;
    }



    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }


    // </editor-fold>
    
  

}
