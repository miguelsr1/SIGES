package gob.mined.siap2.web.mb.impl;

import com.mined.siap2.to.AreasInversionArchivoTO;
import gob.mined.siap2.business.ejbs.impl.AreasInversionBean;
import gob.mined.siap2.business.ejbs.impl.GeneracionInsumoAreasInversionDesdeArchivoBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AreasInversion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.filtros.FiltroAreasInversion;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.ArchivoUtils;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ViewScoped
@Named(value = "adicionInsumosAreasInversionDesdeArchivoMB")
public class AdicionInsumosAreasInversionDesdeArchivoMB implements Serializable{
 
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    private JSFUtils jSFUtils;
    
    @Inject
    private AreasInversionBean areasInversionBean;

    @Inject
    private GeneracionInsumoAreasInversionDesdeArchivoBean generacionInsumoAreasInversionDesdeArchivoBean;
    
    private AreasInversion areaSelected = null;
    private AreasInversion subAreaSelected = null;
    
    private UploadedFile uploadedFile;
    
    private AreasInversionArchivoTO crearDTO;
    
    @PostConstruct
    public void init() {
        crearDTO = new AreasInversionArchivoTO();
    }
 
    public List<AreasInversion> completeAreasInversion(String query){
        try {
            FiltroAreasInversion filtro = new FiltroAreasInversion();
            filtro.setNombre(query);
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            filtro.setOrderBy(new String[]{"nombre"});
            filtro.setMaxResults(10L);
            filtro.setIncluirCampos(new String[]{"nombre","codigo","version"});
            return areasInversionBean.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    public List<AreasInversion> completeSubAreasInversion(String query){
        try {
            FiltroAreasInversion filtro = new FiltroAreasInversion();
            filtro.setNombre(query);
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setAscending(new boolean[]{true});
            filtro.setOrderBy(new String[]{"nombre"});
            filtro.setMaxResults(10L);
            filtro.setIncluirCampos(new String[]{"nombre","codigo","version"});
            if(areaSelected != null) {
                filtro.setPadreId(areaSelected.getId());
            }
            return areasInversionBean.obtenerPorFiltro(filtro);
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }
    
    /**
     * Metodo utilizado para guardar y/o actualizar un registro de
     * TopePresupuestal
     *
     * @return
     */
    public String guardarActualizar() {

        try {
            crearDTO.setArea(areaSelected);
            crearDTO.setSubArea(subAreaSelected);
            generacionInsumoAreasInversionDesdeArchivoBean.crearDesdeArchivo(crearDTO);
            
            //return "crearEditarSubAreasInversion.xhtml?faces-redirect=true&id="+ areaSelected.getId() +"&sub" + subAreaSelected.getId();
            return "crearEditarSubAreasInversion.xhtml?faces-redirect=true&id=" + areaSelected.getId() + "&sub=" + subAreaSelected.getId();
        } catch (BusinessException be) {
            jSFUtils.mostrarErrorByPropertieCode(be.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
        return null;
    }

    public void fileUploadListener(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            if (uploadedFile != null) {
                crearDTO.setFile(ArchivoUtils.getDataFile(uploadedFile));
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }
 
    
    /**
     * Dirige al sitio de Areas de inversión
     * @return
     */
    public String cerrar() {
        return "consultaAreasInversion.xhtml?faces-redirect=true";
    }

    public AreasInversion getAreaSelected() {
        return areaSelected;
    }

    public void setAreaSelected(AreasInversion areaSelected) {
        this.areaSelected = areaSelected;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public AreasInversion getSubAreaSelected() {
        return subAreaSelected;
    }

    public void setSubAreaSelected(AreasInversion subAreaSelected) {
        this.subAreaSelected = subAreaSelected;
    }

    public AreasInversionArchivoTO getCrearDTO() {
        return crearDTO;
    }

    public void setCrearDTO(AreasInversionArchivoTO crearDTO) {
        this.crearDTO = crearDTO;
    }
    
}
