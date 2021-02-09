/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.web.delegates.ArchivoDelegate;
import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author Sofis Solutions
 */
@Named(value = "imgUtils")
@ApplicationScoped
public class ImgUtils implements Serializable {

    @Inject
    ArchivoDelegate archivoDelegate;
    
    private static final String BASE_SERVLET_NAME = "/webresources/images/";

    /**
     * Este método es utilizado para generar la URL que se ponen en el atributo src de las imágenes.
     * Recibe por parámetro el archivo que representa dicha imagen
     * 
     * @param a
     * @return 
     */
    public String getImgPath(Archivo a){
        try {
            if (a != null){
                return getImageSrc(ImgUtils.BASE_SERVLET_NAME + a.getId()+ "/" +a.getToken());
            } 
        }catch(Exception e){
        }  
        return null;
    }
        
    private String getImageSrc( String imagePath) throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();
        String url = context.getApplication().getViewHandler().getResourceURL(context, imagePath);
        return context.getExternalContext().encodeResourceURL(url);
    }

}
