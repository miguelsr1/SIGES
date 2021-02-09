/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.exceptions;

import javax.ejb.ApplicationException;

/**
 * Excepción de tipo técnico, por ejemplo, conexión a la base de datos, etc.
 * @note Las excepciones de este tipo corresponden a excepciones 
 * técnicas (errores en la base de datos por ejemplo).
 * @author Sofis Solutions
 */
@ApplicationException(rollback = true)
public class TechnicalException extends GeneralException{
    
}
