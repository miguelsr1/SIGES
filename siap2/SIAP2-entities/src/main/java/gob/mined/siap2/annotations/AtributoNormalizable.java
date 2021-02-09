/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siap2.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AtributoNormalizable {
    
}
