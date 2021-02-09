/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.utils;

import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Date;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Sofis Solutions (www.sofis-solutions.com)
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
                throw new IOException("Could not completely read file " + file.getName());
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

            //return new String(raw);
        } catch (Exception ex) {
            //nada para hacer
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

        } catch (Exception ex) {
            //nada para hacer
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
        Long hastaL = null;
        try {
            hastaL = new Long(value);
        } catch (Exception w) {
        }

        return hastaL;
    }

    /**
     * Metodo auxiliar que transforma un String que representa una clase en una
     * tipo Class
     *
     * @param className
     * @return
     * @throws DAOGeneralException
     */
    public static Class toClass(String className) throws DAOGeneralException {
        Class class_;
        try {
            class_ = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new DAOGeneralException(ex);
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
}
