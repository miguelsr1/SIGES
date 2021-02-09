package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.TipoCredito;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;

@JPADAO
public class TipoCreditoDAO extends EclipselinkJpaDAOImp<TipoCredito, Integer> implements  Serializable{
    
    
    /**
     * Este método devuelve un registro de TipoCrediro filtrado por Id
     * @param id idetificador del registro
     * @return
     * @throws DAOGeneralException 
     */
    public TipoCredito getTipoCreditoById(String id) throws DAOGeneralException {
        return (TipoCredito) entityManager
                .createQuery("select g from TipoCredito g where g.id = :id")
                .setParameter(":id", id)
                .getSingleResult();
    }
    
    
    /**
     * Este método devuelve todos los registros de Tipo de credito
     * @return
     * @throws DAOGeneralException 
     */
    public List<TipoCredito> getTipoCredito() throws DAOGeneralException{
        return (List<TipoCredito>) entityManager.createQuery("select g from TipoCredito g").getResultList();
    }
    
    /**
     * Este método devuelve una lista de registros TipoCredito que se encuentren habilitados
     * @return 
     */
    public List<TipoCredito> getTipoCreditoHabilitados() {
        return entityManager.createQuery("SELECT g FROM TipoCredito g WHERE g.habilitado = TRUE ").getResultList();
    }
    
}
