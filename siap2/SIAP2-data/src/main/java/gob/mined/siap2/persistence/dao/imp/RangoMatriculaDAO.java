/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.RangoMatricula;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;


@JPADAO
public class RangoMatriculaDAO extends EclipselinkJpaDAOImp<RangoMatricula, Integer> implements  Serializable{

   
    
    /**
     * Este método devuelve todos los registros de RangoMatricula
     * @return
     * @throws DAOGeneralException 
     */
    public List<RangoMatricula> getRangoMatricula() throws DAOGeneralException{
        return (List<RangoMatricula>) entityManager.createQuery("select g from RangoMatricula g").getResultList();
    }
    
    /**
     * Este método devuelve todos los registros de RangoMatricula filtrados por ID Relacion componente anio fiscal
     * 
     * @param id
     * @return
     * @throws DAOGeneralException 
     */
    public List<RangoMatricula> getRangoMatriculaByRelacion(Integer id) throws DAOGeneralException{
        return (List<RangoMatricula>) entityManager.createQuery("select g from RangoMatricula g where g.relacionAnioPresupuesto.id = :id")
                .setParameter("id", id)
                .getResultList();
    }
}
