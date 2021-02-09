/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.datatypes;

import gob.mined.siap2.ws.to.DataFile;
import java.io.IOException;
import java.io.InputStream;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Sofis Solutions
 */
public class ArchivoUploaded implements UploadedFile{
    private DataFile file;

    @Override
    public String getFileName() {
        return file.getFileName();
    }

    @Override
    public InputStream getInputstream() throws IOException {
        return file.getInputStream();
    }

    @Override
    public long getSize() {
        return file.getSize();
    }

    @Override
    public byte[] getContents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getContentType() {
        return file.getContentType();
    }

    @Override
    public void write(String filePath) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DataFile getFile() {
        return file;
    }

    public void setFile(DataFile file) {
        this.file = file;
    }
    
}
