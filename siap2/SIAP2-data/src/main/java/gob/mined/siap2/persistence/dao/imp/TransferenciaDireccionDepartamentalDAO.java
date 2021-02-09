package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.TransferenciaDireccionDepartamental;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;

@JPADAO
public class TransferenciaDireccionDepartamentalDAO extends EclipselinkJpaDAOImp<TransferenciaDireccionDepartamental, Integer> implements  Serializable{
   
    
    /**
     * Este método busca un registro de TransferenciaDireccionDepartamental filtrado por TransferenciasComponente y 
     * direccion departamental
     * 
     * @param idTC
     * @param idDirDepa
     * @return
     * @throws DAOGeneralException 
     */
    public TransferenciaDireccionDepartamental getTransferenciasDirDepaByTransfCompYDirDepa(Integer idTC, Integer idDirDepa) throws DAOGeneralException {
        return (TransferenciaDireccionDepartamental) entityManager
                .createQuery("select g from TransferenciaDireccionDepartamental g where g.transferenciasComponente.id = :idTC and g.direccionDep.pk = :idDirDepa")
                .setParameter("idTC", idTC)
                .setParameter("idDirDepa", idDirDepa)
                .getSingleResult();
    }
    
    /**
     * Este método busca una lista de registros de TransferenciaDireccionDepartamental filtrado por TransferenciasComponente
     * 
     * @param idTC
     * @return
     * @throws DAOGeneralException 
     */
    public List<TransferenciaDireccionDepartamental> getTransferenciasDirDepaByTransfComp(Integer idTC) throws DAOGeneralException {
        return (List<TransferenciaDireccionDepartamental>) entityManager
                .createQuery("select g from TransferenciaDireccionDepartamental g where g.transferenciasComponente.id = :idTC")
                .setParameter("idTC", idTC)
                .getResultList();
    }
    
}
