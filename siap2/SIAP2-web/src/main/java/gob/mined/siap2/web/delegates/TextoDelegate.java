/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates;

import gob.mined.siap2.business.ejbs.TextoBean;
import gob.mined.siap2.entities.data.Texto;
import gob.mined.siap2.exceptions.GeneralException;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate de TExtos entre la capa de presentación y la lógica.
 * @author Sofis Solutions
 */
@Named
public class TextoDelegate implements Serializable {

    @Inject
    private TextoBean genBean;

    /**
     * Este método guarda un texto (lo crea o lo actualiza)
     * @param tdp
     * @return
     * @throws GeneralException 
     */
    public Texto guardar(Texto tdp) throws GeneralException {
        return genBean.guardar(tdp);
    }

    /**
     * Este método permite obtener un texto según su id.
     * @param id
     * @return
     * @throws GeneralException 
     */
    public Texto obtenerTextoPorId(Integer id) throws GeneralException {
        return genBean.obtenerTextoPorId(id);
    }

    /**
     * Este método permite eliminar un texto según su id.
     * @param id
     * @throws GeneralException 
     */
    public void eliminarTexto(Integer id) throws GeneralException {
        genBean.eliminarTexto(id);
    }

    /**
     * Este método permite crear un texto con un código en un determinado idioma.
     * @param codigo
     * @param idiomaId
     * @return
     * @throws GeneralException 
     */
    public String obtenerCrearTextoPorCodigo(String codigo, Integer idiomaId) throws GeneralException {
       
        return genBean.obtenerTextoPorCodigos(codigo, idiomaId);
    }

    /**
     * Este método permite obtener todos los textos en un determinado idioma.
     * @param idiomaId
     * @return 
     */
    public List<Texto> obtenerTodosLosTextos(Integer idiomaId) {
        return genBean.obtenerTodosLosTextos(idiomaId);
    }

    
    
     public String obtenerCrearTextoAyudaPorCodigos(String codigoTexto, Integer codigoIdioma) throws GeneralException {
        return  genBean.obtenerCrearTextoAyudaPorCodigos(codigoTexto, codigoIdioma);
     }
}
