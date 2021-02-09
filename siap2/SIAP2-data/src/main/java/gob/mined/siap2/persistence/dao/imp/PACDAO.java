/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.persistence.dao.imp;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.enums.EstadoPAC;
import gob.mined.siap2.persistence.dao.imp.eclipselink.EclipselinkJpaDAOImp;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Query;

/**
 * Esta clase incluye los métodos de acceso a datos de PAC
 *
 * @author Sofis Solutions
 */
@JPADAO
public class PACDAO extends EclipselinkJpaDAOImp<AccionCentral, Integer> {

    /**
     * Este método devuelve la lista de PAC de un año dado.
     *
     * @param anio
     * @return
     */
    public List<PAC> getPACEnAnio(Integer anio) {
        return entityManager.createQuery("SELECT p FROM PAC p"
                + " WHERE p.anio =:anio ")
                .setParameter("anio", anio)
                .getResultList();
    }

    /**
     * Este método determina si en un determinado año hay algún PAC sin cerrar.
     *
     * @param anio
     * @return
     */
    public boolean hayPACSinCerrar(Integer anio) {
        List<PAC> l = entityManager.createQuery("SELECT 1 FROM PAC p"
                + " WHERE p.anio =:anio AND "
                + " p.estado != :estadoPAC")
                .setParameter("estadoPAC", EstadoPAC.CERRADO)
                .setParameter("anio", anio)
                .getResultList();
        return !l.isEmpty();
    }

    /**
     * Este método obtiene todos los insumos a incluir en el reporte PEP de un
     * PAC dado.
     *
     * @param idPAC
     * @return
     */
    public List<POInsumos> getInsumosParaReportePEP(Integer idPAC) {
        return entityManager.createQuery("SELECT i FROM PAC p, p.grupos g, g.insumos i"
                + " WHERE p.id =:idPAC ")
                .setParameter("idPAC", idPAC)
                .getResultList();
    }
    
    /**
     * método que retorna loa PAC, filtrando por id o nombre
     * 
     * @param id
     * @param nombrePac
     * @return 
     */
    public List<PAC> filtrarPACs(Integer id, String  nombrePac) {
        String query =  "SELECT p FROM PAC p";        
        List<String> where = new LinkedList<>();
        
        if (id != null){
            where.add(" p.id = :id");
        }
        if (!TextUtils.isEmpty(nombrePac)){
            where.add(" UPPER (p.nombre) like :nombrePac");
        }
        
        
        if (!where.isEmpty()){
            query = query + " WHERE " + TextUtils.join(" AND ", where);
        }
        
        Query q= entityManager.createQuery(query);
        
        if (id!= null){
            q.setParameter("id", id);
        }        
        if (!TextUtils.isEmpty(nombrePac)){
            q.setParameter("nombrePac", "%" + nombrePac.toUpperCase() + "%");
        }
        return q.getResultList();
    }

    

}
