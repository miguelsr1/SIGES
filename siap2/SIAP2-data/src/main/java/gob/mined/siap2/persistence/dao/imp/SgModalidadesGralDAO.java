package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siges.entities.data.impl.SgModalidad;
import gob.mined.siges.entities.data.impl.SgModalidadAtencion;
import gob.mined.siges.entities.data.impl.SgRelModEdModAten;
import gob.mined.siges.entities.data.impl.SgSubModalidad;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import java.util.List;

@JPADAO
public class SgModalidadesGralDAO extends EclipselinkJpaDAOImp<SgRelModEdModAten, Integer>{
    
    /**
     * Este metodo busca un registro de relacion de modalidades por ID
     * @param id
     * @return 
     */
    public SgRelModEdModAten findRelacionById(Integer id) {
        return entityManager.find(SgRelModEdModAten.class, id);
    }
    
    /**
     * Este metodo busca todos los registros de relacion de modalidades, filtrados por Modalidad (educativa)
     * @param id
     * @return 
     */
    public List<SgRelModEdModAten> findRelacionByModalidad(Integer id) {
        return (List<SgRelModEdModAten>) entityManager
                .createQuery("select r from SgRelModEdModAten r where r.modalidadEducativaFk.id = :id")
                .setParameter("id", id)
                .getResultList();
    }
    
        
    /**
     * Este metodo busca todos los registros de relacion de modalidades, filtrados por subModalidad
     * @param id
     * @return 
     */
    public List<SgRelModEdModAten> findRelacionBySubModalidad(Integer id) {
        return (List<SgRelModEdModAten>) entityManager
                .createQuery("select r from SgRelModEdModAten r where r.subModalidadAtencionFk.id = :id")
                .setParameter(":id", id)
                .getResultList();
    }
    
    /**
     * Este metodo busca todos los registros de relacion de modalidades, filtrados por Modalidad de atencion
     * @param id
     * @return 
     */
    public List<SgRelModEdModAten> findRelacionByModalidadAtencion(Integer id) {
        return (List<SgRelModEdModAten>) entityManager
                .createQuery("select r from SgRelModEdModAten r where r.subModalidadAtencionFk.id = :id")
                .setParameter(":id", id)
                .getResultList();
    }
    
    /**
     * Este metodo busca todos los registros de relacion de modalidades que esten habilitados
     * @return 
     */
    public List<SgRelModEdModAten> findRelacionHabilitados() {
        return (List<SgRelModEdModAten>) entityManager
                .createQuery("select r from SgRelModEdModAten r where r.habilitado = true")
                .getResultList();
    }
    
    /**
     * Este método devuelve todos los registros de SgNiveles
     * @return
     * @throws DAOGeneralException 
     */
    public List<SgRelModEdModAten> getRelacionModalidades() throws DAOGeneralException{
        return (List<SgRelModEdModAten>) entityManager.createQuery("select r from SgRelModEdModAten r").getResultList();
    }
    
       
    
    
   /****** MODALIDADES ******/
    
    /**
     * Este metodo busca un registro de modalidades filtrado por ID
     * @param id
     * @return 
     */
    public List<SgModalidad> findModalidadesById(Integer id) {
        return (List<SgModalidad>) entityManager
                .createQuery("select m from SgModalidad m where m.id = :id")
                .setParameter(":id", id)
                .getResultList();
    }
    
    /**
     * Este metodo busca todos los registros de Modalidades
     * @return 
     */
    public List<SgModalidad> findModalidades() {
        return (List<SgModalidad>) entityManager.createQuery("select r from SgModalidad r ").getResultList();
    }
    
    /**
     * Este metodo busca todos los registros de Por ciclo
     * @param idCiclo
     * @return 
     */
    public List<SgModalidad> findModalidadesByCiclo(Integer idCiclo) {
        return (List<SgModalidad>) entityManager.createQuery("select r from SgModalidad r where r.ciclo.id = :id ").setParameter(":id", idCiclo).
                getResultList();
    }
    
    /**
     * Este metodo busca todos los registros de modalidades que esten habilitados
     * @return 
     */
    public List<SgModalidad> findModalidadesHabilitados() {
        return (List<SgModalidad>) entityManager
                .createQuery("select r from SgModalidad r where r.habilitado = true")
                .getResultList();
    }
   
    
    
    
    
    /****** SUB MODALIDADES ******/
    
    /**
     * Este metodo busca un registro de subModalidades filtrado por ID
     * @param id
     * @return 
     */
    public List<SgSubModalidad> findSubModalidadById(Integer id) {
        return (List<SgSubModalidad>) entityManager
                .createQuery("select s from SgSubModalidad s where s.id = :id")
                .setParameter(":id", id)
                .getResultList();
    }
    
    /**
     * Este metodo busca todos los registros de SubModalidades
     * @return 
     */
    public List<SgSubModalidad> findSubModalidades() {
        return (List<SgSubModalidad>) entityManager.createQuery("select a from SgSubModalidad a ").getResultList();
    }
    /**
     * Este metodo busca todos los registros de SubModalidades de una misma modalidadAtencion
     * @param idModAtencion
     * @return 
     */
    public List<SgSubModalidad> findSubModalidadesByModalidadAtencion(Integer idModAtencion) {
        return (List<SgSubModalidad>) entityManager.createQuery("select a from SgSubModalidad a where a.modalidadFk.id = :id")
                .setParameter(":id", idModAtencion)
                .getResultList();
    }
    
    /**
     * Este metodo busca todos los registros de modalidades que esten habilitados
     * @return 
     */
    public List<SgSubModalidad> findSubModalidadesHabilitados() {
        return (List<SgSubModalidad>) entityManager
                .createQuery("select r from SgSubModalidad r where r.habilitado = true")
                .getResultList();
    }
    
    
    
    
    
    /****** MODALIDAD ATENCION ******/
    
    /**
     * Este metodo busca un registro de ModalidadAtencion filtrado por ID
     * @param id
     * @return 
     */
    public SgModalidadAtencion findModalidadAtencionById(Integer id) {
        return (SgModalidadAtencion) entityManager
                .createQuery("select s from SgModalidadAtencion s where s.id = :id")
                .setParameter(":id", id);
    }
    
    /**
     * Este metodo busca todos los registros de Modalidades de Atencion
     * @return 
     */
    public List<SgModalidadAtencion> findModalidadAtencion() {
        return (List<SgModalidadAtencion>) entityManager.createQuery("select a from SgModalidadAtencion a ").getResultList();
    }
   
    /**
     * Este metodo busca todos los registros de modalidades de atención que esten habilitados
     * @return 
     */
    public List<SgModalidadAtencion> findModalidadesAtencionHabilitados() {
        return (List<SgModalidadAtencion>) entityManager
                .createQuery("select r from SgModalidadAtencion r where r.habilitado = true")
                .getResultList();
    }
    
    /**
     * Este metodo busca todos los registros de modalidades de atención con las mismas relaciones
     * @return 
     */
    public List<SgModalidadAtencion> findModalidadesAtencionHabilitados(Integer idMod, Integer idCi, Integer idNiv, Integer idOc) {
        return (List<SgModalidadAtencion>) entityManager
                .createQuery("select r from SgModalidadAtencion r where r. = true")
                .getResultList();
    }

}
