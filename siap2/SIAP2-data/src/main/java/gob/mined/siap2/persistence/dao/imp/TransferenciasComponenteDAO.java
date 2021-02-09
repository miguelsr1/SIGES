package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;

@JPADAO
public class TransferenciasComponenteDAO extends EclipselinkJpaDAOImp<TransferenciasComponente, Integer> implements  Serializable{
 
    /**
     * Este método obtiene un listado de todos los registros de la tabla TransferenciasComponente
     * @return
     * @throws DAOGeneralException 
     */
    public List<TransferenciasComponente> getTransferenciasComponente() throws DAOGeneralException{
        return (List<TransferenciasComponente>) entityManager.createQuery("Select t from TransferenciasComponente t").getResultList();
    }
    
    /**
     * Este método obtiene un listado de todos los registros de la tabla TransferenciasComponente que se relacionen con un registro de anioFiscal
     * @param idAnio
     * @return
     * @throws DAOGeneralException 
     */
    public List<TransferenciasComponente> getTransferenciasComponenteByAnioFiscal(Integer idAnio) throws DAOGeneralException{
        return (List<TransferenciasComponente>) entityManager.createQuery("Select t from TransferenciasComponente t where t.anioFiscal.id = :idAnio")
                .setParameter("idAnio", idAnio)
                .getResultList();
    }
    
    /**
     * Este método obtiene un listado de todos los registros de la tabla TransferenciasComponente que se relacionen con un registro de CategoriaPresupuestoEscolar
     * @param idCate
     * @return
     * @throws DAOGeneralException 
     */
    public List<TransferenciasComponente> getTransferenciasComponenteByCategoriaPresupuesto(Integer idCate) throws DAOGeneralException{
        return (List<TransferenciasComponente>) entityManager.createQuery("Select t from TransferenciasComponente t where t.componente.id = :idCate")
                .setParameter("idCate", idCate)
                .getResultList();
    }
    
    /**
     * Este método obtiene un listado de todos los registros de la tabla TransferenciasComponente que se relacionen con un registro de ComponentePresupuestoEscolar
     * @param idComp
     * @return
     * @throws DAOGeneralException 
     */
    public List<TransferenciasComponente> getTransferenciasComponenteByComponentePresupuesto(Integer idComp) throws DAOGeneralException{
        return (List<TransferenciasComponente>) entityManager.createQuery("Select t from TransferenciasComponente t where t.subcomponente.id = :idComp")
                .setParameter("idComp", idComp)
                .getResultList();
    }
    
    /**
     * Este método obtiene un listado de todos los registros de la tabla TransferenciasComponente que se relacionen con un registro de Componente
     * @param idCat
     * @return
     * @throws DAOGeneralException 
     */
    public List<TransferenciasComponente> getTransferenciasComponenteByCategoriaComponente(Integer idCat) throws DAOGeneralException{
        return (List<TransferenciasComponente>) entityManager.createQuery("Select t from TransferenciasComponente t where t.componente.id = :idCat")
                .setParameter("idCat", idCat)
                .getResultList();
    }

    
}
