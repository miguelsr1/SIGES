/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.Date;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Sofis Solutions
 */
public class Utils {
    // Returns the contents of the file in a byte array.

    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            // Get the size of the file
            long length = file.length();

            // You cannot create an array using a long type.
            // It needs to be an int type.
            // Before converting to an int type, check
            // to ensure that file is not larger than Integer.MAX_VALUE.
            if (length > Integer.MAX_VALUE) {
                // File is too large
                throw new IOException("File is too large!");
            }

            // Create the byte array to hold the data
            byte[] bytes = new byte[(int) length];

            // Read in the bytes
            int offset = 0;
            int numRead = 0;

            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            // Ensure all the bytes have been read in
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file "
                        + file.getName());
            }
            return bytes;
        } catch (IOException e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static String md5(String txt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(txt.getBytes("ISO-8859-1"));
            byte[] raw = md.digest();
            BASE64Encoder env = new BASE64Encoder();
            return env.encode(raw);

            // return new String(raw);
        } catch (Exception ex) {

        }
        return null;
    }

    public static String md5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            byte[] raw = md.digest();
            BASE64Encoder env = new BASE64Encoder();
            return env.encode(raw);

            // return new String(raw);
        } catch (Exception ex) {

        }
        return null;
    }

    /**
     * Método auxiliar que transforma un Integer en un Long
     *
     * @param value
     * @return
     */
    public static Long toLong(Integer value) {
        if (value == null) {
            return null;
        }
        Long hastaL = null;
        try {
            hastaL = new Long(value);
        } catch (Exception w) {
        }

        return hastaL;
    }

    /**
     * Método auxiliar que transforma un String que representa una clase en una
     * tipo Class
     *
     * @param className
     * @return
     * @throws DAOGeneralException
     */
    public static Class toClass(String className) {
        Class class_ = null;
        try {
            class_ = Class.forName(className);
        } catch (Exception ex) {

        }
        return class_;
    }

    /**
     * Retorna la fecha del día
     *
     * @return
     */
    public static Date getToday() {
        return new Date();
    }

    /**
     * Compara dos codigos, sin distinguir las mayusculas y los espacios al
     * principio y al final
     *
     * @param codigo1
     * @param codigo2
     * @return
     */
    public static boolean codigosIguales(String codigo1, String codigo2) {
        if (codigo1 == null) {
            return (codigo2 == null);
        }
        String c1 = codigo1.trim().toLowerCase();
        String c2 = codigo2.trim().toLowerCase();
        return c1.equals(c2);
    }

    public static void downloadPDF(FacesContext facesContext, byte[] pdfData) throws IOException {

        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

        // Initialize response.
        response.reset(); // Some JSF component library or some Filter might have set some headers in the buffer beforehand. We want to get rid of them, else it may collide.
        response.setContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ServletContext#getMimeType() for auto-detection based on filename.
        response.setHeader("Content-disposition", "attachment; filename=\"name.pdf\""); // The Save As popup magic is done here. You can give it any filename you want, this only won't work in MSIE, it will use current request URL as filename instead.

        // Write file to response.
        OutputStream output = response.getOutputStream();
        output.write(pdfData);
        output.close();

        // Inform JSF to not take the response in hands.
        facesContext.responseComplete();
    }

    public static BigDecimal sumar(BigDecimal a, BigDecimal b) {
        BigDecimal respuesta = BigDecimal.ZERO;
        respuesta = respuesta.add(a != null ? a : BigDecimal.ZERO);
        respuesta = respuesta.add(b != null ? b : BigDecimal.ZERO);
        return respuesta;
    }
}
