package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AreasInversion;
import gob.mined.siap2.entities.data.impl.RelacionAreasInversionInsumo;
import gob.mined.siap2.persistence.dao.exceptions.DAOConstraintViolationException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;


@JPADAO
public class RelacionAreasInversionInsumoDAO extends EclipselinkJpaDAOImp<AreasInversion, Integer> implements  Serializable{
    
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    public static final String ERR_CONSTRAINT_VIOLADA = "duplicate key value violates unique constraint";
    public static final String ERR_CONSTRAINT = "error.DuplicateKey";
    
    /**
     * Este método devuelve un registro de relacion de AreasIversion e Insumo, filtrado por ID de relacion
     * @param id
     * @return
     * @throws DAOGeneralException 
     */
    public RelacionAreasInversionInsumo getRelAreasInversionInsumoById(String id) throws DAOGeneralException {

        return (RelacionAreasInversionInsumo) entityManager
                .createQuery("select g from RelacionAreasInversionInsumo g where g.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }
    
    
    /**
     * Este método devuelve un registro de relacion de AreasIversion e Insumo, filtrado por los Ids de relacion
     * @param idAreaRelacion
     * @param idInsumo
     * @return
     * @throws DAOGeneralException 
     */
    public RelacionAreasInversionInsumo getRelAreasInversionInsumoByIds(String idAreaRelacion, String idInsumo) throws DAOGeneralException {
        return (RelacionAreasInversionInsumo) entityManager
                .createQuery("select g from RelacionAreasInversionInsumo g where g.areaInversion.id = :idAreaRelacion and g.insumo.id = :idInsumo")
                .setParameter("idAreaRelacion", idAreaRelacion)
                .setParameter("idInsumo", idInsumo)
                .getSingleResult();
    }
    
    
    /**
     * Este método devuelve un lista de registros de relacion de AreasIversion e Insumo filtrado por ID de AreasRelacion
     * @param id
     * @return
     * @throws DAOGeneralException 
     */
    public List<RelacionAreasInversionInsumo> getRelAreasInversionInsumoByAreasInversionId(Integer id) throws DAOGeneralException {

        return (List<RelacionAreasInversionInsumo>) entityManager
                .createQuery("select g from RelacionAreasInversionInsumo g where g.areaInversion.id = :id")
                .setParameter("id", id)
                .getResultList();
    }
    
    
    /**
     * Este método devuelve un lista de registros de relacion de AreasIversion e Insumo filtrado por ID de Insumo
     * @param id
     * @return
     * @throws DAOGeneralException 
     */
    public List<RelacionAreasInversionInsumo> getRelAreasInversionInsumoByInsumoId(String id) throws DAOGeneralException {
        
        return (List<RelacionAreasInversionInsumo>) entityManager
                .createQuery("select g from RelacionAreasInversionInsumo g where g.insumo.id = :id")
                .setParameter("id", id)
                .getResultList();
    }
    
    public RelacionAreasInversionInsumo update(RelacionAreasInversionInsumo entity) throws DAOGeneralException, PersistenceException {
        try {
            RelacionAreasInversionInsumo toReturn = entityManager.merge(entity);
            entityManager.flush();
            entityManager.refresh(toReturn);
            return toReturn;
            
        }catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            if(e.getMessage().contains(ERR_CONSTRAINT_VIOLADA)  ){
                DAOConstraintViolationException eDao = new DAOConstraintViolationException(e);
                throw eDao;
            }else{
                DAOGeneralException eDao = new DAOGeneralException(e);
                throw eDao;
            }
        }
    }
}
