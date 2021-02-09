/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.data.daos;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.Texto;
import gob.mined.siap2.entities.data.impl.TextoAyuda;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * Esta clase incluye métodos para manear los textos de la interfaz de usuario
 * en lo que respecta al acceso a la capa de datos.
 * @author Sofis Solutions
 */
@JPADAO
public class TextoDAO extends EclipselinkJpaDAOImp<Texto, Integer> implements Serializable {

    /**
     * Este método permite obtener un texto por código en un determinado idioma.
     * @param codigo del texto a recuperar
     * @param idiId id del idioma
     * @return
     * @throws DAOGeneralException 
     */
//    public String obtenerTextoPorCodigoIdioma(String codigo, Integer idiId) throws DAOGeneralException {
//        Query q = entityManager.createNativeQuery("Select s.tex_valor from ss_textos s where s.tex_codigo = :codigo and s.tex_idi_id = :idiId");
//        q.setParameter("codigo", codigo);
//        q.setParameter("idiId", idiId);
//        String res = null;
//        try {
//            res = (String) q.getSingleResult();
//        } catch (NoResultException ex) {
//            Logger.getLogger(ConstantesEstandares.LOGGER).warning(ex.getMessage());
//            res = null;
//        }
//        return res;
//    }
    
    /**
     * Este método permite obtener un texto según el código y el código del idioma.
     * @param codigoTexto
     * @param codigoIdioma
     * @return
     * @throws DAOGeneralException 
     */
    public Texto obtenerTextoPorCodigoIdioma(String codigoTexto, String codigoIdioma) throws DAOGeneralException {
        Query q = entityManager.createQuery("Select s from Texto s where s.texCodigo = :codigoTexto and s.texIdiId.idiCodigo = :codigoIdioma");
        q.setParameter("codigoTexto", codigoTexto);
        q.setParameter("codigoIdioma", codigoIdioma);
        List<Texto> res = q.getResultList();
        return res.isEmpty() ? null : res.get(0);
    }
    
    /**
     * Este método permite obtener un texto según el código y el código del idioma.
     * @param codigoTexto
     * @param codigoIdioma
     * @return
     * @throws DAOGeneralException 
     */
    public String obtenerTextoPorCodigos(String codigoTexto, String codigoIdioma) throws DAOGeneralException {
        Query q = entityManager.createQuery("Select s.texValor from Texto s where s.texCodigo = :codigoTexto and s.texIdiId.idiCodigo = :codigoIdioma");
        q.setParameter("codigoTexto", codigoTexto);
        q.setParameter("codigoIdioma", codigoIdioma);
        String res = null;
        try {
            res = (String) q.getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(ConstantesEstandares.LOGGER).warning(ex.getMessage());
            res = null;
        } catch (Exception ex){
            Logger.getLogger(ConstantesEstandares.LOGGER).warning(ex.getMessage());
        }
        return res;
    }
    
    /**
     * Este método permite obtener un texto según el código y el id del idioma.
     * @param codigoTexto
     * @param codigoIdioma
     * @return
     * @throws DAOGeneralException 
     */
    public String obtenerTextoPorCodigosId(String codigoTexto, Integer idIdioma) throws DAOGeneralException {        
        List<Texto> l = entityManager.createQuery("Select s from Texto s where s.texCodigo = :codigoTexto and s.texIdiId.idiId = :idIdioma")
        .setParameter("codigoTexto", codigoTexto)
        .setParameter("idIdioma", idIdioma)
        .getResultList();
               
        if (l.isEmpty()){
            return null;
        }else{
            return l.get(0).getTexValor();
        }
    }
    
    /**
     * Este método permite obtener un texto de ayuda según el código y el código del idioma.
     * @param codigoTexto
     * @param idIdioma
     * @return
     * @throws DAOGeneralException 
     */
    public TextoAyuda obtenerTextoAyudaPorCodigosId(String codigoTexto, Integer idIdioma) throws DAOGeneralException {        
        List<TextoAyuda> l = entityManager.createQuery("Select s from TextoAyuda s where s.codigo = :codigoTexto and s.idioma.idiId = :idIdioma")
        .setParameter("codigoTexto", codigoTexto)
        .setParameter("idIdioma", idIdioma)
        .getResultList();
               
        if (l.isEmpty()){
            return null;
        }else{
            return l.get(0);
        }
    }
}
