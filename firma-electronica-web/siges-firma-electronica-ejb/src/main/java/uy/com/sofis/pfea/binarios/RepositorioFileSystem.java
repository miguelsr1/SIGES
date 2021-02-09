package uy.com.sofis.pfea.binarios;

import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import uy.com.sofis.pfea.constantes.ConstantesErrores;
import uy.com.sofis.pfea.exceptions.TechnicalException;

/**
 * @author Sofis Solutions
 */
public class RepositorioFileSystem extends Repositorio {

    private static String BASE_PATH;

    private static final String PATH_IMANGENNODISPONIBLE = "/binarios/";
    private static final String IMAGENNODISPONIBLE = "imagen-no-disponible.jpg";
    private static final String PATH_DOCUMENTOS_BANDEJA = "/binarios/documentos_bandeja";

    public RepositorioFileSystem() {
        BASE_PATH = System.getProperty("jboss.server.base.dir");
    }

    @Override
    public byte[] obtenerBytesImagenNoDisponible() throws TechnicalException {
        try {
            Path path = FileSystems.getDefault().getPath(BASE_PATH + PATH_IMANGENNODISPONIBLE, IMAGENNODISPONIBLE);
            byte[] bytes = Files.readAllBytes(path);
            return bytes;
        } catch (java.nio.file.NoSuchFileException e) {
            return null;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            ge.setEx(ex);
            throw ge;
        }
    }

    @Override
    public List<byte[]> obtenerBytesDocumento(String nombreArchivo) throws TechnicalException {

        if (nombreArchivo == null) {
            return null;
        }

        List<byte[]> result = new ArrayList<>();
        File baseFolder = new File(BASE_PATH + PATH_DOCUMENTOS_BANDEJA);
        File[] files = baseFolder.listFiles();
        if(files != null) {
            for(File file : files)  {
                if(file.isFile() && file.getName().startsWith(nombreArchivo + "_")) {
                    try {
                        byte[] bytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                        result.add(bytes);
                    } catch (Exception ex) {
                        TechnicalException ge = new TechnicalException();
                        ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
                        ge.addError(ex.getMessage());
                        ge.setEx(ex);
                        throw ge;
                    }
                }
            }
        }

        return result;
    }

    @Override
    public void guardarDocumento(String nombreArchivo, List<byte[]> bytes) {
        int length = bytes.size();
        for (int i = 0; i < length; i++) {
            byte[] bytesData = bytes.get(i);
            try {
                Path path = FileSystems.getDefault().getPath(BASE_PATH + PATH_DOCUMENTOS_BANDEJA, nombreArchivo + "_" + i);
                Files.write(path, bytesData);
            } catch (Exception ex) {
                ex.printStackTrace();
                TechnicalException ge = new TechnicalException();
                ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
                ge.addError(ex.getMessage());
                ge.setEx(ex);
                throw ge;
            }
        }
    }

    @Override
    public void eliminarDocumento(String nombreArchivo) {
        File baseFolder = new File(BASE_PATH + PATH_DOCUMENTOS_BANDEJA);
        File[] files = baseFolder.listFiles();
        if(files != null) {
            for(File file : files)  {
                if(file.isFile() && file.getName().startsWith(nombreArchivo + "_")) {
                    try {
                        Files.delete(Paths.get(file.getAbsolutePath()));
                    } catch (Exception ex) {
                        TechnicalException ge = new TechnicalException();
                        ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
                        ge.addError(ex.getMessage());
                        ge.setEx(ex);
                        throw ge;
                    }
                }
            }
        }
    }
}
