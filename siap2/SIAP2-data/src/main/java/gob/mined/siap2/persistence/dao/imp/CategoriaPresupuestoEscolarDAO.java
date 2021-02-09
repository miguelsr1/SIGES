package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.AreasInversion;
import gob.mined.siap2.entities.data.impl.CategoriaPresupuestoEscolar;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;


@JPADAO
public class CategoriaPresupuestoEscolarDAO extends EclipselinkJpaDAOImp<AreasInversion, Integer> implements  Serializable{
 
     /**
     * Este método devuelve un registro de CategoriaPresupuestoEscolar
     * @param codigo
     * @return
     * @throws DAOGeneralException 
     */
    public CategoriaPresupuestoEscolar getCatPresupuestoEscolarByCodigo(String codigo) throws DAOGeneralException {

        return (CategoriaPresupuestoEscolar) entityManager
                .createQuery("select g from CategoriaPresupuestoEscolar g where g.codigo = :codigo")
                .setParameter("codigo", codigo)
                .getSingleResult();
    }
    
    
    /**
     * Este método devuelve todos los registros de CategoriaPresupuestoEscolar
     * @return
     * @throws DAOGeneralException 
     */
    public List<CategoriaPresupuestoEscolar> getAllCatPresupuestoEscolar() throws DAOGeneralException {
        return (List<CategoriaPresupuestoEscolar>) entityManager
                .createQuery("select g from CategoriaPresupuestoEscolar g ")
                .getResultList();
    }
    
    
    /**
     * Este método devuelve todos los registros de CategoriaPresupuestoEscolar que se encuentren habilitados
     * @return
     * @throws DAOGeneralException 
     */
    public List<CategoriaPresupuestoEscolar> getAllCatPresupuestoEscolarHabilitados() throws DAOGeneralException {
        return (List<CategoriaPresupuestoEscolar>) entityManager
                .createQuery("select g from CategoriaPresupuestoEscolar g where g.habilitado = TRUE order by g.nombre")
                .getResultList();
    }
}
