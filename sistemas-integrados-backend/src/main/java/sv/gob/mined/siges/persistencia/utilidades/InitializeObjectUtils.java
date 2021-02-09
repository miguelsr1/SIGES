/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.apache.commons.beanutils.PropertyUtilsBean;
import sv.gob.mined.siges.utils.ReflectionUtils;

/**
 *
 * @author Sofis Solutions
 */
public class InitializeObjectUtils {

    private static final Logger LOGGER = Logger.getLogger(LoadLazyCollectionsViewInterceptor.class.getName());
    private static PropertyUtilsBean beanUtil = new PropertyUtilsBean();

    /*
    * Inicializa colecciones anotadas con @AtributoInicializarColeccion recursivamente.
    * Inicializa colecciones de relación *ToOne anotada con @AtributoInicializarColeccion.
     */
    public static void initializeLazyCollectionsRecursive(Object o) {
        try {
            if (o != null) {
                for (Field field : ReflectionUtils.getAllFields(o.getClass())) {
                    field.setAccessible(true);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /*
    * Inicializa colecciones anotadas con @AtributoInicializarColeccion.
     */
    public static void initializeLazyCollections(Object o) {
        try {
            if (o != null) {
                for (Field field : ReflectionUtils.getAllFields(o.getClass())) {
                    field.setAccessible(true);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Inicializa todas las relaciones *ToOne recursivamente.
     * Inicializa colecciones anotadas con @AtributoInicializarColeccion recursivamente.
     * Los objetos no inicializados quedan en null.
     */
    public static void initializeHistoricRevisionRecursive(Object o, Set<String> inicializados) {
        try {
            if (o != null) {
                String campoId = ReflectionUtils.obtenerNombreCampoAnotado(o.getClass(), Id.class);
                Object id = beanUtil.getProperty(o, campoId);
                String fieldIdentifier;
                for (Field field : ReflectionUtils.getAllFields(o.getClass())) {
                    field.setAccessible(true);
                    fieldIdentifier = o.getClass() + "-" + id + "-" + field.getName();
                    if (field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToOne.class)) {
                        // Inicializamos objeto *ToOne
                        Object value = beanUtil.getProperty(o, field.getName());
                        if (!inicializados.contains(fieldIdentifier) && value != null) {
                            inicializados.add(fieldIdentifier);
                            InitializeObjectUtils.initializeHistoricRevisionRecursive(value, inicializados);
                        }
                    }  else if (field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToMany.class)) {
                        // Collection no inicializada. Reemplazamos proxy por null
                        beanUtil.setProperty(o, field.getName(), null);
                    }
                }
            }   
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    
}
