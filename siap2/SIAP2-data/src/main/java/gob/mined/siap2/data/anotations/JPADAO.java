/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.data.anotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Qualifier;
 
/**
 *
 * @author Sofis Solutions
 */
@Qualifier
@ApplicationScoped
@Target({ TYPE, METHOD, PARAMETER, FIELD })
@Retention(RUNTIME)
//@Documented
public @interface JPADAO {
 
}
