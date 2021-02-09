/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.business.ejbs.ConfiguracionBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesConfiguracion;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.entities.data.impl.Archivo;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.ws.to.DataFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos para el trabajo con archivos físicos.
 * @author Sofis Solutions
 */
@Stateless(name = "ArchivoBean")
@LocalBean
public class ArchivoBean {

    @Inject
    private ConfiguracionBean configuracionBean;
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    private static final String IMG_SMALL = "-SM";

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método guarda un archivo en el sistema
     * @return
     * @throws DAOGeneralException 
     */
    @TransactionAttribute(REQUIRES_NEW)
    public Archivo crearArchivo() throws DAOGeneralException {
        Archivo a = new Archivo();
        return (Archivo) generalDAO.create(a);
    }

    /**
     * Este método almacena un archivo en el sistema de archivos.
     * @param a archivo a guardar
     * @param uploadedFile archivo
     * @param isImage indica si es una imagen
     * @return
     * @throws IOException
     * @throws DAOGeneralException 
     */
    public Archivo asociarArchivo(Archivo a, DataFile uploadedFile, boolean isImage) throws IOException, DAOGeneralException {
        Configuracion c = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.RUTA_ARCHIVOS);
        if (c == null) {
            BusinessException be = new BusinessException();
            be.addError(ConstantesErrores.ERROR_PARA_GUARDAR_ARCHIVOS_DEBE_CONFIRAR_RUTA);
            throw be;
        }
        String path = c.getCnfValor();

        File file = new File(path + String.valueOf(a.getId()));
        InputStream input = uploadedFile.getInputStream();
        Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);

        a.setNombreOriginal(uploadedFile.getFileName());
        a.setContentType(uploadedFile.getContentType());
        a.setSize(uploadedFile.getSize());
        a.setToken(nextToken());

        return (Archivo) generalDAO.update(a);
    }

    /**
     * Este método devuelve un archivo a partir del Id
     * @param a archivo con el ID a buscar
     * @return 
     */
    public File getFile(Archivo a) {
        File file = new File(getAbsolutePath(a.getId().toString()));
        return file;
    }

    /**
     * Este método devuelve la ruta absoluta a un archivo.
     * @param fileName
     * @return 
     */
    public String getAbsolutePath(String fileName) {
        Configuracion c = configuracionBean.obtenerCnfPorCodigo(ConstantesConfiguracion.RUTA_ARCHIVOS);
        if (c == null) {
            BusinessException be = new BusinessException();
            be.addError(ConstantesErrores.ERROR_PARA_GUARDAR_ARCHIVOS_DEBE_CONFIRAR_RUTA);
            throw be;
        }
        String path = c.getCnfValor();
        return path + String.valueOf(fileName);
    }

    /**
     * Este método devuelve el archivo a partir del nombre y un token.
     * @param name nombre del archivo
     * @param token token del archivo
     * @return 
     */
    public Archivo getArchivo(String name, String token) {
        Archivo a =(Archivo) generalDAO.find(Archivo.class, Integer.valueOf(name));
        if (Objects.equals(a.getToken(), token)){
            return a;
        }
        return null;
    }


    private   SecureRandom random = new SecureRandom();

    private  synchronized String nextToken() {
      return new BigInteger(130, random).toString(32);
    }


}
