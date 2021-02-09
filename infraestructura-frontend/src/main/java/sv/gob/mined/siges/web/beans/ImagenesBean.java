/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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

    public InputStream getFileLowQuality(Long pk) throws Exception {
        try {
            Image img = ImageIO.read(new File(basePath + SofisFileUtils.getPathFromPk(pk))).getScaledInstance(250, 250, BufferedImage.SCALE_SMOOTH);
            BufferedImage bi = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_RGB);  
            Graphics bg = bi.getGraphics();
            bg.drawImage(img, 0, 0, null);  
            bg.dispose(); 

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos );
            baos.flush();

            byte[] imageInByte = baos.toByteArray();
            baos.close();

            InputStream is = new ByteArrayInputStream(imageInByte);           

            return is;
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
