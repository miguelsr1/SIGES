/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package com.mined.siap2.audit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.CategoriaPresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.entities.data.impl.POActividadProyecto;
import java.util.logging.Logger;
import javax.persistence.Version;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.history.AsOfClause;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ClientSession;

/**
 *
 * @author sofis
 */
public class AuditReader {
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    public static <T> List<T> read(EntityManager em, Class<T> entityClass, int id) {
        List<T> respuesta = new LinkedList<>();
        String tableName = getTableName(em, entityClass);
        String idColumn = getPKColumnName(entityClass);
        String versionColumn = getVersionColumnName(entityClass);
        String stQuery = "select start_date, end_date from "+Constantes.SCHEMA_SIAP2 +"."+ tableName + "_hist where " + idColumn + " = " + id + " order by " + versionColumn;
        Query q = em.createNativeQuery(stQuery);
        List<Object[]> fechas = q.getResultList();
        for (Object[] o : fechas) {
            Date startDate = (Date) o[0];
            Date endDate = (Date) o[1];
            Date fechaRetorno = null;
            if (startDate != null){
                fechaRetorno = startDate;
            }else{
                fechaRetorno = fechaMenos(endDate,1);//Resto un milisegundo a endDate
            }
            //Tengo la fecha para buscar el objeto.
            //-- Busco el objeto en el historial a la fecha "fechaRetorno" --
            JpaEntityManager jpaEntityManager = em.unwrap(JpaEntityManager.class);
            ClientSession clientSession = jpaEntityManager.getServerSession().acquireClientSession();
            AsOfClause asOfClause = new AsOfClause(fechaRetorno);
            Session historicalSession = clientSession.acquireHistoricalSession(asOfClause); 
            ReadAllQuery historicalQuery = new ReadAllQuery(entityClass);
           // historicalQuery.setShouldBindAllParameters(true);
           // historicalQuery.setShouldOuterJoinSubclasses(true);
            
            
            ExpressionBuilder emp = new ExpressionBuilder();
            Expression exp = emp.get("id").equal(id);
            historicalQuery.setSelectionCriteria(exp);
            
            List<T> lista = (List) historicalSession.executeQuery(historicalQuery);
            if (!lista.isEmpty()){
                respuesta.add(lista.get(0));//Tiene que haber un solo elemento de la lista.
                
                
                /**
                T  obj = lista.get(0);
                if(obj instanceof CategoriaPresupuestoEscolar) {
                    logger.info("Es instancia de CategoriaPresupuestoEscolar");
                    ((CategoriaPresupuestoEscolar) obj).getSubComponentes().size();
                    logger.info("((CategoriaPresupuestoEscolar) obj).getSubComponentes().size(): " + ((CategoriaPresupuestoEscolar) obj).getSubComponentes().size());
                }
                if(obj instanceof ComponentePresupuestoEscolar) {
                    logger.info("Es instancia de ComponentePresupuestoEscolar");
                    ((ComponentePresupuestoEscolar) obj).getRelacionGesPresEsAnioFiscals().size();
                    ((ComponentePresupuestoEscolar) obj).getSubareasInversion().size();
                    ((ComponentePresupuestoEscolar) obj).getTiposOrganismoCurricular().size();
                    
                    logger.info("((ComponentePresupuestoEscolar) obj).getRelacionGesPresEsAnioFiscals().size(): " + ((ComponentePresupuestoEscolar) obj).getRelacionGesPresEsAnioFiscals().size());
                    logger.info("((ComponentePresupuestoEscolar) obj).getSubareasInversion().size(): " + ((ComponentePresupuestoEscolar) obj).getSubareasInversion().size());
                    logger.info("((ComponentePresupuestoEscolar) obj).getTiposOrganismoCurricular().size(): " + ((ComponentePresupuestoEscolar) obj).getTiposOrganismoCurricular().size());
                }**/
                
            }
            //-- Fin Busco ---------------------------------------------------
        }

        return respuesta;

    }
    
     public static <T> List<T> readAllByVersion(EntityManager em, Class<T> entityClass, int id, int version) {
        List<T> respuesta = new LinkedList<>();
        String tableName = getTableName(em, entityClass);
        String idColumn = getPKColumnName(entityClass);
        String versionColumn = getVersionColumnName(entityClass);
        String stQuery = "select hist.start_date, hist.end_date from "+Constantes.SCHEMA_SIAP2 +"."+ tableName + "_hist hist where hist." + idColumn + " = " + id + " and hist." + versionColumn + "="+ version + " order by hist." + versionColumn;
        Query q = em.createNativeQuery(stQuery);
        List<Object[]> fechas = q.getResultList();
        for (Object[] o : fechas) {
            Date startDate = (Date) o[0];
            Date endDate = (Date) o[1];
            Date fechaRetorno = null;
            if (startDate != null){
                fechaRetorno = startDate;
            }else{
                fechaRetorno = fechaMenos(endDate,1);//Resto un milisegundo a endDate
            }
            //Tengo la fecha para buscar el objeto.
            //-- Busco el objeto en el historial a la fecha "fechaRetorno" --
            JpaEntityManager jpaEntityManager = em.unwrap(JpaEntityManager.class);
            ClientSession clientSession = jpaEntityManager.getServerSession().acquireClientSession();
            AsOfClause asOfClause = new AsOfClause(fechaRetorno);
            Session historicalSession = clientSession.acquireHistoricalSession(asOfClause); 
            ReadAllQuery historicalQuery = new ReadAllQuery(entityClass); 
            ExpressionBuilder emp = new ExpressionBuilder();
            Expression exp = emp.get("id").equal(id);
            historicalQuery.setSelectionCriteria(exp);
            List<T> lista = (List) historicalSession.executeQuery(historicalQuery);
            if (!lista.isEmpty()){
                respuesta.add(lista.get(0));//Tiene que haber un solo elemento de la lista.
            }
            //-- Fin Busco ---------------------------------------------------
        }

        return respuesta;

    }
    
    public static <T> List<T> read(EntityManager em, Class<T> entityClass, Date fecha, Expression exp) {
        List<T> respuesta;
        JpaEntityManager jpaEntityManager = em.unwrap(JpaEntityManager.class);
        ClientSession clientSession = jpaEntityManager.getServerSession().acquireClientSession();
        AsOfClause asOfClause = new AsOfClause(fecha);
        Session historicalSession = clientSession.acquireHistoricalSession(asOfClause);
        ReadAllQuery historicalQuery = new ReadAllQuery(entityClass);
        if (exp != null){
            historicalQuery.setSelectionCriteria(exp);
        }
        respuesta = (List<T>) historicalSession.executeQuery(historicalQuery);
        return respuesta;
    }

    /**
     * Retorna el campo con la anotación @Id
     * @param entityClass
     * @return 
     */
    public static String findIdField(Class entityClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            String name = field.getName();
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Id.class)) {
                    return name;
                }
            }
        }
        return null;
    }
    
    public static String getPKColumnName(Class entityClass) {
        if (entityClass == null) {
            return null;
        }
        String name = null;
        for (Field f : entityClass.getDeclaredFields()) {
            Id id = null;
            Column column = null;
            Annotation[] as = f.getAnnotations();
            for (Annotation a : as) {
                if (a.annotationType() == Id.class) {
                    id = (Id) a;
                } else if (a.annotationType() == Column.class) {
                    column = (Column) a;
                }
            }
            if (id != null && column != null) {
                name = column.name();
                break;
            }
        }
        if (name == null && entityClass.getSuperclass() != Object.class) {
            name = getPKColumnName(entityClass.getSuperclass());
        }
        return name;
    }
    
    public static String getVersionColumnName(Class entityClass) {
        if (entityClass == null) {
            return null;
        }
        boolean encontreField = false;
        String columnName = null;
        for (Field f : entityClass.getDeclaredFields()) {
            Annotation[] as = f.getAnnotations();
            for (Annotation a : as) {
                if(a instanceof Version){
                    encontreField = true;
                }
                if(a instanceof Column){
                    Column col = (Column) a;
                    columnName = col.name();
                }
            }
            if (encontreField){
                break;
            }
        }
        return columnName;
    }

    
    
    /**
     * Retorna el nombre de la tabla correspondiente con la entidad pasada por parámetro.
     * 
     *
     * @param <T>
     * @param em
     * @param entityClass
     * @return
     */
    public static <T> String getTableName(EntityManager em, Class<T> entityClass) {
        /*
         * Controla si la entidad está presente en los metadatos.
         * Lanza IllegalArgumentException en caso que no lo esté.
         */
        Metamodel meta = em.getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);

        Table t = entityClass.getAnnotation(Table.class);

        String tableName = (t == null)
                ? entityType.getName().toUpperCase()
                : t.name();
        return tableName;
    }
    
       
    private static Date fechaMenos(Date fch, int miliseconds) {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.MILLISECOND, -miliseconds);
        return new Date(cal.getTimeInMillis());
    }
            
            
    
}
