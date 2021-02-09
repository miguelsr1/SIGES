/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates;

import gob.mined.siap2.business.ejbs.impl.ArchivoBean;
import gob.mined.siap2.entities.data.impl.Archivo;
import java.io.File;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate de ArchivoBean.
 *
 * @author Sofis Solutions
 */
@Named
public class ArchivoDelegate implements Serializable {

    @Inject
    private ArchivoBean bean;

    /**
     * Este método devuelve un objeto de tipo File a partir de un archivo.
     *
     * @param a
     * @return
     */
    public File getFile(Archivo a) {
        return bean.getFile(a);
    }

    /**
     * Este método devuelve la ruta absoluta a un archivo.
     *
     * @param fileName
     * @return
     */
    public String getAbsolutePath(String fileName) {
        return bean.getAbsolutePath(fileName);
    }

    /**
     * Este método devuelve un archivo a partir del nombre y su token
     *
     * @param name
     * @param token
     * @return
     */
    public Archivo getArchivo(String name, String token) {
        return bean.getArchivo(name, token);
    }

}
