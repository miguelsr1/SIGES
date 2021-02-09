package gob.mined.siap2.web.utils;

import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import org.omnifaces.resourcehandler.DefaultResourceHandler;
import org.omnifaces.resourcehandler.RemappedResource;

/**
 *
 * @author fabricio Utilizado para forzar que el navegador refresque el css
 */
public class VersionResourceHandler extends DefaultResourceHandler {

    static String version = "1.0";

    static {
        try {
            InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/META-INF/MANIFEST.MF");
            Manifest manifest = new Manifest();
            manifest.read(is);
            Attributes atts = manifest.getMainAttributes();
            version = atts.getValue("Implementation-Build");
        } catch (Exception ex) {

        }
    }

    public VersionResourceHandler(ResourceHandler wrapped) {
        super(wrapped);
    }

    @Override
    public Resource decorateResource(Resource resource) {
        if (resource == null || !"css".equals(resource.getLibraryName())) {
            return resource;
        }
        return new RemappedResource(resource, resource.getRequestPath() + "&v=" + version);
    }

}
