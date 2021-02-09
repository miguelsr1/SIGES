package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.List;

@JPADAO
public class GestionPresupuestoEscolarDAO extends EclipselinkJpaDAOImp<ComponentePresupuestoEscolar, Integer> implements  Serializable{
    
    
    
    /**
     * Este método devuelve un registro de PresupuestoEscolar filtrado por código
     * @param codigo código de la gestion
     * @return
     * @throws DAOGeneralException 
     */
    public ComponentePresupuestoEscolar getGesPresEsByCodigo(String codigo) throws DAOGeneralException {

        return (ComponentePresupuestoEscolar) entityManager
                .createQuery("select g from ComponentePresupuestoEscolar g where g.cod = :codigo")
                .setParameter("codigo", codigo)
                .getSingleResult();
    }
    
    
    /**
     * Este método devuelve todos los registros de "presupuesto de gestión escolar"
     * @return
     * @throws DAOGeneralException 
     */
    public List<ComponentePresupuestoEscolar> getGesPresEs() throws DAOGeneralException{
        return (List<ComponentePresupuestoEscolar>) entityManager.createQuery("select g from ComponentePresupuestoEscolar g").getResultList();
    }
    
    /**
     * Este método devuelve una lista de registros de "presupuesto de gestión escolar" habilitados
     * @return 
     */
    public List<ComponentePresupuestoEscolar> getGesPresEsHabilitados() {
        return entityManager.createQuery("SELECT g FROM ComponentePresupuestoEscolar g WHERE g.habilitado = TRUE ")
                .getResultList();
    }
    
    /**
     * Este método devuelve una lista de registros de "presupuesto de gestión escolar" habilitados
     * @param idCategoria
     * @return 
     */
    public List<ComponentePresupuestoEscolar> getGesPresEsByCategoria(Integer idCategoria) {
        return entityManager.createQuery("SELECT g FROM ComponentePresupuestoEscolar g WHERE g.categoriaPresupuestoEscolar.id = :id")
                .setParameter("id",idCategoria)
                .getResultList();
    }
    
    /**
     * Este método devuelve una lista de registros de componentes habilitados de una misma categoria
     * @param idCategoria
     * @return 
     */
    public List<ComponentePresupuestoEscolar> getComponentesHabilitadosByCategoria(Integer idCategoria) {
        return entityManager.createQuery("SELECT g FROM ComponentePresupuestoEscolar g WHERE g.categoriaPresupuestoEscolar.id = :id and g.habilitado = TRUE")
                .setParameter("id",idCategoria)
                .getResultList();
    }
    
}
