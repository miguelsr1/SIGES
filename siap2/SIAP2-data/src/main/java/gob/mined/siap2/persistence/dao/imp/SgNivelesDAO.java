package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siges.entities.data.impl.SgNivel;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.List;

@JPADAO
public class SgNivelesDAO extends EclipselinkJpaDAOImp<SgNivel, Integer>{
    
  

    /**
     * Este método devuelve todos los registros de SgNivel
     * @return
     * @throws DAOGeneralException 
     */
    public List<SgNivel> findSgNiveles() throws DAOGeneralException{
        return (List<SgNivel>) entityManager.createQuery("select n from SgNivel n").getResultList();
    }
    
    /**
     * Este método devuelve todos los registros de SgNivel que pertenezcan a una misma organizacion curricular
     * @param idOc
     * @return
     * @throws DAOGeneralException 
     */
    public List<SgNivel> findSgNivelesByOc(Integer idOc) throws DAOGeneralException{
        return (List<SgNivel>) entityManager.createQuery("select n from SgNivel n where n.organizacionCurricular.id = :id")
                .setParameter(":id", idOc).getResultList();
    }
    
    /**
     * Este método devuelve todos los registros de SgNivel que se encuentren Habilitados
     * @return
     * @throws DAOGeneralException 
     */
    public List<SgNivel> findSgNivelesHabilitados() throws DAOGeneralException{
        return (List<SgNivel>) entityManager.createQuery("select n from SgNivel n where n.habilitado = true").getResultList();
    }
}
