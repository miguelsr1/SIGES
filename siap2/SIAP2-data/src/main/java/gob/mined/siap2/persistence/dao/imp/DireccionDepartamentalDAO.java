package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.DireccionDepartamental;
import gob.mined.siap2.entities.data.impl.TransferenciaDireccionDepartamental;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;



@JPADAO
public class DireccionDepartamentalDAO extends EclipselinkJpaDAOImp<TransferenciaDireccionDepartamental, Integer> implements  Serializable{
   
    
    /**
     * Este método busca un registro de direccionDepartamental por ID departamento
     * @param idDepartamento
     * @return
     * @throws DAOGeneralException 
     */
    public DireccionDepartamental getDireccionDepartamentalByIdDepartamento(Integer idDepartamento) throws DAOGeneralException {
        return (DireccionDepartamental) entityManager
                .createQuery("select g from DireccionDepartamental g where g.departamento.pk = :idDepartamento")
                .setParameter("idDepartamento", idDepartamento)
                .getSingleResult();
    }
    
    public List<DireccionDepartamental> getDireccionDepartamentalIdDepartamento(Integer idDepartamento) throws DAOGeneralException {
        return (List<DireccionDepartamental> )entityManager
                .createQuery("select g from DireccionDepartamental g where g.departamento.pk = :idDepartamento")
                .setParameter("idDepartamento", idDepartamento)
                .getResultList();
    }
    
    /**
     * Este método busca todos los registros de direccionDepartamental
     * 
     * @return
     * @throws DAOGeneralException 
     */
    public List<DireccionDepartamental> getDireccionesDepartamentales() throws DAOGeneralException {
        return (List<DireccionDepartamental>) entityManager.createQuery("select g from DireccionDepartamental g").getResultList();
    }

}
