package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.AreasInversion;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;

@JPADAO
public class AreasInversionDAO extends EclipselinkJpaDAOImp<AreasInversion, Integer> implements  Serializable{
    
    
    
    /**
     * Este método devuelve un registro de AreasIversion filtrado por código
     * @param codigo
     * @return
     * @throws DAOGeneralException 
     */
    public AreasInversion getAreaInversionByCodigo(String codigo) throws DAOGeneralException {

        return (AreasInversion) entityManager
                .createQuery("select g from AreasInversion g where g.codigo = :codigo")
                .setParameter("codigo", codigo)
                .getSingleResult();
    }
    
    
    /**
     * Este método devuelve todos los registros de Areas de Inversion
     * @return
     * @throws DAOGeneralException 
     */
    public List<AreasInversion> getAreasInversion() throws DAOGeneralException{
        return (List<AreasInversion>) entityManager.createQuery("select g from AreasInversion g").getResultList();
    }
    
    
    /**
     * Este método devuelve una lista de registros de Areas de inversion que sean registros padre y que estén habilitados
     * @return 
     */
    public List<AreasInversion> getAreasInversionPadresHabilitados() {
        return entityManager.createQuery("SELECT g FROM AreasInversion g WHERE g.areaPadre is null and g.habilitado = TRUE ").getResultList();
    }
    /**
     * Este método devuelve una lista de registros de Areas de inversion que sean registros padre y que estén habilitados
     * @return 
     */
    public List<AreasInversion> getAreasInversionPorComponente(Integer id) {
        return entityManager.createQuery("SELECT g FROM AreasInversion g WHERE g.areaPadre is null and g.habilitado = TRUE ").getResultList();
    }
    
    /**
     * Este método devuelve una lista de registros de Areas de inversion habilitados
     * @return 
     */
    public List<AreasInversion> getAreasInversionPadres() {
        return entityManager.createQuery("SELECT g FROM AreasInversion g WHERE g.areaPadre IS NULL").getResultList();
    }
    
    
    /**
     * Este método devuelve una lista de registros de Areas de inversion, filtrados por ID AreaInversion padre
     * @param id
     * @return
     * @throws DAOGeneralException 
     */
    public List<AreasInversion> getAreasInversionByIdPadre(Integer id) throws DAOGeneralException {
        return (List<AreasInversion>) entityManager
                .createQuery("select g from AreasInversion g where g.areaPadre.id = :id")
                .setParameter("id", id)
                .getResultList();
    }
}