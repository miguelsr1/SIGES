/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.persistencia.entidades.RevEntity;
import sv.gob.mined.siges.persistencia.utilidades.RevHistorico;
import sv.gob.mined.siges.utils.ReflectionUtils;

@Stateless
public class ConsultaHistoricoBean<T> {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Devuelve las revisiones del objeto T con la id indicada
     *
     * @param clase Class
     * @param id Long
     * @return List de RevHistorico
     * @throws GeneralException
     */
    public List<RevHistorico> obtenerHistorialRevisionesPorId(Class clase, Long id) throws GeneralException {
        AuditReader reader = AuditReaderFactory.get(em);
        List<Object[]> resultados = reader.createQuery().forRevisionsOfEntity(clase, false, true)
                .add(AuditEntity.id().eq(id))
                .addOrder(AuditEntity.revisionNumber().desc())
                .getResultList();
        List<RevHistorico> respuesta = new ArrayList<>();      
        for (Object[] r : resultados) {
            RevHistorico rh = new RevHistorico();
            rh.setObjPk(id);
            rh.setRevEntity((RevEntity) r[1]);
            rh.setRevType((RevisionType) r[2]);
            respuesta.add(rh);
        }
        return respuesta;
    }
    
    
    /**
     * Devuelve un objeto en una determinada versión
     *
     * @param clase Class
     * @param id Long
     * @param version Integer
     * @return T
     */
    public <T> T obtenerEnVersion(Class clase, Long id, Integer version) {
        try {
            AuditReader reader = AuditReaderFactory.get(em);
            Field versionField = ReflectionUtils.obtenerCampoAnotado(clase, Version.class);
            List<T> respuesta = reader.createQuery().forRevisionsOfEntity(clase, true, true)
                    .add(AuditEntity.id().eq(id))
                    .add(AuditEntity.property(versionField.getName()).eq(version))
                    .getResultList();
            if (respuesta != null && respuesta.size() > 0) {
                return respuesta.get(0);
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }
    
    
    /**
     * Devuelve un objeto en una determinada revision
     *
     * @param clase Class
     * @param id Long
     * @param revision Long
     * @return T
     */
    public <T> T obtenerEnRevision(Class clase, Long revision, Long id) {
        try {
            AuditReader reader = AuditReaderFactory.get(em);
            List<T> respuesta = reader.createQuery().forEntitiesAtRevision(clase, revision)
                    .add(AuditEntity.id().eq(id))
                    .getResultList();
            if (respuesta != null && respuesta.size() > 0) {
                return respuesta.get(0);
            }
        } catch (Exception ex) {         
            throw new TechnicalException(ex);
        }
        return null;
    }

    
 
    

}
