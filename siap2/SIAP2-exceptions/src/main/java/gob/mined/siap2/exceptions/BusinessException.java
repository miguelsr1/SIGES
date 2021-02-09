/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.exceptions;

import javax.ejb.ApplicationException;

/**
 * Esta clase corresponde a las excepciones de la lógica de negocio de la aplicación.
 * @note Este tipo de excepciones corresponden 
 * a errores en validaciones o restricciones 
 * al modelo de datos.
 * @author Sofis Solutions
 */
@ApplicationException(rollback = true)
public class BusinessException extends GeneralException {
    
    /**
     * Constructor por defecto
     */
    public BusinessException() {
        
    }
    
    /**
     * Constructor a partir de un mensaje.
     * @param msg 
     */
    public BusinessException(String msg) {
        super(msg);
    }
}
