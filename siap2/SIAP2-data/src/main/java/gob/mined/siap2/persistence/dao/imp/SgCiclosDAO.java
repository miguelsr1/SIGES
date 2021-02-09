package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siges.entities.data.impl.SgCiclo;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.List;

@JPADAO
public class SgCiclosDAO extends EclipselinkJpaDAOImp<SgCiclo, Integer>{
    
    
    
    /**
     * Este método devuelve todos los registros de SgCiclo
     * @return
     * @throws DAOGeneralException 
     */
    public List<SgCiclo> findSgCiclos() throws DAOGeneralException{
        return (List<SgCiclo>) entityManager.createQuery("select g from SgCiclo g").getResultList();
    }

    
    /**
     * Este método devuelve todos los registros de SgCiclo que se encuentren Habilitados
     * @return
     * @throws DAOGeneralException 
     */
    public List<SgCiclo> findSgCiclosHabilitados() throws DAOGeneralException{
        return (List<SgCiclo>) entityManager.createQuery("select g from SgCiclo g where g.habilitado = true").getResultList();
    }
    
    public List<SgCiclo> findSgCiclosByNivel(Integer idNivel) throws DAOGeneralException{
        return (List<SgCiclo>) entityManager.createQuery("select g from SgCiclo g where g.nivel.id = :nivel")
                .setParameter("nivel", idNivel).getResultList();
    }
}
