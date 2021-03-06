/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Origen de la última modificación realizada a una entidad.
 * @author Sofis Solutions
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AtributoUltimaOrigen {
    
}
