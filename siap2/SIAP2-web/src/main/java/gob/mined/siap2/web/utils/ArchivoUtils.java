/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.utils;

import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.web.mb.impl.padq.ContratoOrdenCompra;
import gob.mined.siap2.ws.to.DataFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Sofis Solutions
 */
public class ArchivoUtils {

    public static void downloadFile(Archivo a, File file) throws Exception {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();

        ec.setResponseContentType(a.getContentType());
        if (a.getSize() != null) {
            ec.setResponseContentLength(a.getSize().intValue());
        }
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + a.getNombreOriginal() + "\"");
        OutputStream output = ec.getResponseOutputStream();
        Files.copy(file.toPath(), output);

        fc.responseComplete();
    }

    public static void downloadFile(DataFile a) throws Exception {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();

        ec.setResponseContentType(a.getContentType());
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + a.getFileName() + "\"");
        OutputStream output = ec.getResponseOutputStream();
        //si no se pude reiniciar el inputstream se transforma a otro asi nose pierde el archivo
        if (!a.getInputStream().markSupported()) {
            //esto no esta del todo bien, pero se quiere que se pueda descargar antes que se guarde
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            InputStream in = a.getInputStream();
            byte[] buffer = new byte[1024];
            int len = in.read(buffer);
            while (len != -1) {
                bout.write(buffer, 0, len);
                len = in.read(buffer);
            }
            in.close();
            InputStream isFromFirstData = new ByteArrayInputStream(bout.toByteArray());
            a.setInputStream(isFromFirstData);
        }

        IOUtils.copy(a.getInputStream(), output);
        a.getInputStream().reset();

        fc.responseComplete();
    }

    public static DataFile getDataFile(UploadedFile file) throws IOException {
        DataFile df = null;
        if (file != null) {
            df = new DataFile();
            df.setInputStream(file.getInputstream());
            df.setFileName(file.getFileName());
            df.setSize(file.getSize());
            df.setContentType(file.getContentType());
        }
        return df;
    }

    public static void downloadPdfFromBytes(byte[] bytespdf, String fileName) {
        FacesContext fc = FacesContext.getCurrentInstance();

        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();
        ec.setResponseContentType("application/pdf");
        ec.setResponseContentLength(bytespdf.length);
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        OutputStream output;
        try {
            output = ec.getResponseOutputStream();
            output.write(bytespdf);
        } catch (IOException ex) {
            Logger.getLogger(ContratoOrdenCompra.class.getName()).log(Level.SEVERE, null, ex);
        }
        fc.responseComplete();
    }

}
