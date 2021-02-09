/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.com.sofis.pfea.anotaciones;

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
