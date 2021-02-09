package uy.com.sofis.pfea.exceptions;

import javax.ejb.ApplicationException;

/**
 * Excepción de tipo técnico, por ejemplo, conexión a la base de datos, etc.
 * @note Las excepciones de este tipo corresponden a excepciones 
 * tecnicas (errores en la base de datos por ejemplo).
 * @author Sofis Solutions
 */
@ApplicationException(rollback = true)
public class TechnicalException extends GeneralException{
    
}
