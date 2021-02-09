/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.omnifaces.cdi.GraphicImageBean;
import sv.gob.mined.siges.utils.SofisFileUtils;

/**
 *
 * @author USUARIO
 */
@GraphicImageBean
public class ImagenesBean {

    private static final Logger LOGGER = Logger.getLogger(ImagenesBean.class.getName());

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpPath;
  

    public InputStream getFile(Long pk) throws Exception {
        try {
            return new BufferedInputStream(new FileInputStream(basePath + SofisFileUtils.getPathFromPk(pk)));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public InputStream getTmpFile(String path) throws Exception {
        try {
            return new BufferedInputStream(new FileInputStream(tmpPath + path));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }
}
