package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.AnioLectivo;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;

@JPADAO
public class AnioLectivoDAO extends EclipselinkJpaDAOImp<AnioLectivo, Integer> implements  Serializable{
    
    /**
     * Este método busca un registro de AnioLectivo filtrado por un anio
     * @param anio
     * @return
     * @throws DAOGeneralException 
     */
    public AnioLectivo getAnioLectivoByAnio(Integer anio) throws DAOGeneralException {
        return (AnioLectivo) entityManager
                .createQuery("select g from AnioLectivo g where g.anio = :anio")
                .setParameter("anio", anio)
                .getSingleResult();
    }
}
