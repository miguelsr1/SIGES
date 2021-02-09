/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.data.daos;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.io.Serializable;
import java.util.Collection;

/**
 * Clase DAO para la gestión de los años fiscales.
 * @author Sofis Solutions
 */
@JPADAO
public class AnioFiscalDAO extends EclipselinkJpaDAOImp<AnioFiscal, Integer> implements Serializable {

    
    /**
     * Este método devuelve los años fiscales habilitados para ejecución
     * @return el conjunto de años fiscales para ejecución
     */
   public Collection<AnioFiscal> obtenerAniosFiscalesEjecucion() {
        
       return entityManager.createQuery("SELECT a from AnioFiscal a "
           + " where a.habilitadoEjecucion=true "
           + " order by a.anio ")
               .getResultList();
       
       
   }
}
