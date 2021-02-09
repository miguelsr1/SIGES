/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.persistencia.entidades.RevEntity;
import sv.gob.mined.siges.persistencia.utilidades.RevHistorico;

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
                .addProjection(AuditEntity.revisionNumber())
                .addProjection(AuditEntity.revisionType())
                .addProjection(AuditEntity.revisionProperty("revtstmp"))
                .addProjection(AuditEntity.revisionProperty("usuario"))
                .addProjection(AuditEntity.id())
                .addOrder(AuditEntity.revisionNumber().desc())
                .getResultList();
        List<RevHistorico> respuesta = new ArrayList<>();
        for (Object[] r : resultados) {
            RevHistorico rh = new RevHistorico();            
            RevEntity revEntity = new RevEntity();
            revEntity.setRev((Long) r[0]);
            revEntity.setRevtstmp((Long) r[2]);
            revEntity.setUsuario((String) r[3]);

            rh.setObjPk(id);
            rh.setRevEntity(revEntity);
            rh.setRevType((RevisionType)r[1]);
            respuesta.add(rh);
        }
        return respuesta;
    }
    
    public List<RevHistorico> obtenerHistorialRevisionesPorIdConEntidad(Class clase, Long id) throws GeneralException {
        AuditReader reader = AuditReaderFactory.get(em);
        List<Object[]> resultados = reader.createQuery().forRevisionsOfEntity(clase, false, true)
                .add(AuditEntity.id().eq(id))
                .addOrder(AuditEntity.revisionNumber().desc())
                .getResultList();
        List<RevHistorico> respuesta = new ArrayList<>();
        for (Object[] r : resultados) {
            RevHistorico rh = new RevHistorico();
            rh.setObjPk(id);
            rh.setObj(r[0]);
            rh.setRevEntity((RevEntity) r[1]);
            rh.setRevType((RevisionType) r[2]);
            respuesta.add(rh);
        }
        return respuesta;
    }

    /**
     * Devuelve un objeto en una determinada revision
     *
     * @param clase Class
     * @param id Long
     * @param revision Long
     * @return T
     */
    public <T> T obtenerEnRevision(Class clase, Long id, Long revision) {
        try {
            AuditReader reader = AuditReaderFactory.get(em);
            List<T> respuesta = reader.createQuery().forEntitiesAtRevision(clase, revision)
                    .add(AuditEntity.id().eq(id))
                    .getResultList();
            if (respuesta != null && respuesta.size() > 0) {
                T ret = respuesta.get(0);
                //InitializeObjectUtils.initializeHistoricRevisionRecursive(ret, new HashSet<>());
                return ret;
            }
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return null;
    }

}
