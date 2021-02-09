package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siges.entities.data.impl.SgClasificacion;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.List;

@JPADAO
public class SgCatalogosSigesDAO extends EclipselinkJpaDAOImp<SgClasificacion, Integer>{
    
        
    /**
     * Este método devuelve todos los registros de SgClasificacion que se encuentren Habilitados
     * @return
     * @throws DAOGeneralException 
     */
    public List<SgClasificacion> findClasificacionesHabilitadas() throws DAOGeneralException{
        return (List<SgClasificacion>) entityManager.createQuery("select g from SgClasificacion g where g.claHabilitado = true").getResultList();
    }
    
}
