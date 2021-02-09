/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.NodoPlantillaDeInsumos;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.List;

/**
 * Esta clase incluye los métodos de acceso a datos vinculados a la plantilla de insumos.
 * @author Sofis Solutions
 */
@JPADAO
public class PlantillaDeInsumosDAO extends EclipselinkJpaDAOImp<Proyecto, Integer> {
    
      /**
       * Este método devuelve un elemento específico de la plantilla (grupo).
       * @param primaryKey
       * @return 
       */ 
    public NodoPlantillaDeInsumos find (Integer primaryKey){
        return  entityManager.find(NodoPlantillaDeInsumos.class, primaryKey);
    }
    
    /**
     * Este método devuelve los insumos hijos.
     * @return 
     */
    public List<NodoPlantillaDeInsumos> getInsumosHijos(){
        return entityManager.createQuery("SELECT p"
                + " FROM NodoPlantillaDeInsumos p"
                + " WHERE p.padre IS NULL "
                + " ORDER BY p.nombre ")
                .getResultList();
    }

    
        
    public NodoPlantillaDeInsumos getNodoInsumo(Integer idInsumo){
        List<NodoPlantillaDeInsumos> l = entityManager.createQuery("SELECT p"
                + " FROM NodoPlantillaDeInsumos p, p.insumos i"
                + " WHERE i.id =:idInsumo ")
                .setParameter("idInsumo", idInsumo)
                .getResultList();
        if (l.isEmpty()){
            return null;
        }
        return l.get(0);
    }
    
}
