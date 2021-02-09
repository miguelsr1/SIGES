/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.persistencia.annotations.AtributoInicializarHistorial;
import sv.gob.mined.siges.utils.ReflectionUtils;

@Stateless
public class ConsultaHistoricoBean<T> {
    
    @PersistenceContext
    private EntityManager em;
    
    /**
     * Deuvelve el historial del objeto T con la id indicada
     *
     * @param clase Class
     * @param id Long
     * @return List de T
     * @throws GeneralException
     */
    public <T> List<T> obtenerHistorialPorId(Class clase, Long id) throws GeneralException {
        AuditReader reader = AuditReaderFactory.get(em);
        Field versionField = ReflectionUtils.obtenerCampoAnotado(clase, Version.class);
        List<T> respuesta = reader.createQuery().forRevisionsOfEntity(clase, true, true)
                .add(AuditEntity.id().eq(id))                
                .addOrder(AuditEntity.property(versionField.getName()).desc())
                .getResultList();

        try {
            //Inicializa los objetos marcados con AtributoInicializarHistorial de la entidad.
            //Solamente un nivel. No es recursivo.
            PropertyUtilsBean beanUtil = new PropertyUtilsBean();
            for (Object o : respuesta) {
                for (Field field : o.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(AtributoInicializarHistorial.class)) {
                        Object value = field.get(o);
                        if (Collection.class.isAssignableFrom(field.getType())) {
                            //Es una lista
                            Collection.class.cast(value).size();
                        } else {
                            //Es un campo
                            String campoId = ReflectionUtils.obtenerNombreCampoAnotado(field.getType(), Id.class);
                            beanUtil.getProperty(value, campoId);
                        }
                    } else if (field.isAnnotationPresent(OneToMany.class)
                            || field.isAnnotationPresent(ManyToOne.class)
                            || field.isAnnotationPresent(OneToOne.class)
                            || field.isAnnotationPresent(ManyToMany.class)) {
                        field.set(o, null); // Reemplazamos proxy por null
                    }
                }
            }

        } catch (Exception ex) {
            throw new TechnicalException(ex);
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
