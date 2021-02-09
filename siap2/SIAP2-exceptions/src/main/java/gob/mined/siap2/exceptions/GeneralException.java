/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.exceptions;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.ApplicationException;

/**
 * Excepción de tipo general.
 *
 * @note Excepción de tipo general. Desde la lógica de negocio se lanzan
 * excepciones de este tipo.
 * @author Sofis Solutions
 */
@ApplicationException(rollback = true)
public class GeneralException extends RuntimeException {

    /**
     * Código OK
     */
    private String codigo = "OK";

   
    private Exception ex;
    /**
     * Lista de códigos de error.
     */
    List<String> errores = new ArrayList();

    /**
     * Constructor por defecto.
     */
    public GeneralException() {
        super("error en generalException");
    }

    /**
     * Constructor a partir de un texto.
     *
     * @param msg
     */
    public GeneralException(String msg) {
        super(msg);
    }

     // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    
    /**
     * Código de error
     * @return 
     */
    public String getCodigo() {
        return codigo;
    }
 
    
    /**
     * Ver {@see #getCodigo}
     * @return 
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * 
     * Excepción que encapsula
      * @return  la excepción que encapsula
     */
    public Exception getEx() {
        return ex;
    }

     /**
     * Ver {@see #ex}
     * Excepción que encapsula
     */
    public void setEx(Exception ex) {
        this.ex = ex;
    }

    /**
     * Lista de errores
     * @return 
     */
    public List<String> getErrores() {
        return errores;
    }

    /**
     * Ver {@see #getErrores}
     * @param errores 
     */
    public void setErrores(List<String> errores) {
        this.errores = errores;
    }

    /**
     * Agrega un código de error a la lista de errores de la excepción.
     * @param codigoError 
     */
    public void addError(String codigoError) {
        this.codigo = "ERR";
        this.errores.add(codigoError);
    }
     // </editor-fold>

    //Codifica dentro el error parametros
    //
    /**
     * Agrega un código de error, con una secuencia de valores. Internamente lo
     * codifica así: Mensaje#param1~param2~param3#
     *
     * @param codigoError
     * @param values
     */
    public void addError(String codigoError, String[] values) {
        this.codigo = "ERR";
        String params = "#";
        for (int i = 0; i < values.length; i++) {
            if (params.length() > 1) {
                params = params + "~";
            }
            params = params + values[i];
        }
        params = params + "#";
        if (params.length() > 1) {
            this.errores.add(codigoError + params);
        } else {
            this.errores.add(codigoError);
        }
    }

    //
    /**
     * Retorna el texto anterior a # ejemplo: Mensaje#parametros -> Mensaje
     *
     * @param error
     * @return
     */
    public static String getTextError(String error) {
        if (error.indexOf("#") != -1) {
            return error.substring(0, error.indexOf("#"));
        } else {
            return error;
        }
    }

    //
    /**
     * Retorna los parámetros embebidos en mensaje de error
     *
     * @param error
     * @return
     */
    public static String[] getParams(String error) {
        if (error.indexOf("#") != -1) {//Tiene parametros 
            String parametros = error.substring(error.indexOf("#"));//Me quedo con la parte final del mensaje que comienza con #
            if (parametros.length() > 2) {//Quiere decir que hay por lo menos #X#
                parametros = parametros.substring(1, parametros.length() - 1);//Quito el 1er y ultimo caracter
                String[] respuesta = parametros.split("~");
                return respuesta;
            }
        }
        return null;
    }

}
