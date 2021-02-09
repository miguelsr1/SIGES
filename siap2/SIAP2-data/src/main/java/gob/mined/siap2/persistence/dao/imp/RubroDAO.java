package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.Rubro;
import gob.mined.siap2.entities.data.impl.SubRubro;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;

@JPADAO
public class RubroDAO extends EclipselinkJpaDAOImp<Rubro, Integer> implements  Serializable{
    
    
    
    /**
     * Este método devuelve un registro de Rubro filtrado por código
     * @param codigo
     * @return
     * @throws DAOGeneralException 
     */
    public Rubro getRubroByCodigo(String codigo) throws DAOGeneralException {
        return (Rubro) entityManager
                .createQuery("select g from Rubro g where g.codigo = :codigo")
                .setParameter("codigo", codigo)
                .getSingleResult();
    }

    /**
     * Este método devuelve todos los registros de Rubros
     * @return
     * @throws DAOGeneralException 
     */
    public List<Rubro> getRubros() throws DAOGeneralException{
        return (List<Rubro>) entityManager.createQuery("select g from Rubro g").getResultList();
    }

    /**
     * Este método devuelve una lista de registros de Rubro, que se encuentren habilitados
     * @return
     * @throws DAOGeneralException 
     */
    public List<Rubro> getRubrosHabilitados() throws DAOGeneralException {
        return (List<Rubro>) entityManager
                .createQuery("select g from Rubro g where g.habilitado = TRUE").getResultList();
    }


    
    
    
    /**
     * Este método devuelve un registro de SubRubro filtrado por código
     * @param codigo
     * @return
     * @throws DAOGeneralException 
     */
    public SubRubro getSubRubroByCodigo(String codigo) throws DAOGeneralException {
        return (SubRubro) entityManager
                .createQuery("select g from SubRubro g where g.codigo = :codigo")
                .setParameter("codigo", codigo)
                .getSingleResult();
    }

    /**
     * Este método devuelve todos los registros de SubRubros
     * @return
     * @throws DAOGeneralException 
     */
    public List<SubRubro> getSubRubros() throws DAOGeneralException{
        return (List<SubRubro>) entityManager.createQuery("select g from SubRubro g").getResultList();
    }

    /**
     * Este método devuelve una lista de registros de SubRubro, que se encuentren habilitados
     * @return
     * @throws DAOGeneralException 
     */
    public List<SubRubro> getSubRubrosHabilitados() throws DAOGeneralException {
        return (List<SubRubro>) entityManager
                .createQuery("select g from SubRubro g where g.habilitado = TRUE").getResultList();
    }

}
