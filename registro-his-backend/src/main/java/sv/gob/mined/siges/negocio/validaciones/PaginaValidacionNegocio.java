/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRhPagina;

/**
 *
 * @author Sofis Solutions
 */
public class PaginaValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgRhPagina
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRhPagina entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else{
            if(entity.getPagAnio()== null){
                ge.addError(Errores.ERROR_ANIO_VACIO);
            }else if(entity.getPagAnio() <= 1979 || entity.getPagAnio() >= 2009){
                ge.addError(Errores.ERROR_ANIO_RANGO);
            }
            if(entity.getPagNivel()== null){
                ge.addError(Errores.ERROR_NIVEL_VACIO);
            }
            if(entity.getPagNombreLibro() == null || entity.getPagNombreLibro().equals("")){
                ge.addError(Errores.ERROR_NOMBRE_LIBRO_VACIO);
            }else if(entity.getPagNombreLibro().length() > 100){
                ge.addError(Errores.ERROR_NOMBRE_LIBRO_MAXIMO);
            }
           if(entity.getPagLibro()==null){
               ge.addError(Errores.ERROR_PAGINA_VACIO);
           }
           if(entity.getPagPagina()==null){
               ge.addError(Errores.ERROR_NUMERO_PAGINA_VACIO);
           }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
    
    public static boolean validarPaginaDeEtiqueta(SgRhPagina entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else{
            if(entity.getPagAnio()== null){
                ge.addError(Errores.ERROR_ANIO_VACIO);
            }else if(entity.getPagAnio() <= 1979 || entity.getPagAnio() >= 2009){
                ge.addError(Errores.ERROR_ANIO_RANGO);
            }
            if(entity.getPagNivel()== null){
                ge.addError(Errores.ERROR_NIVEL_VACIO);
            }         
           if(entity.getPagLibro()==null){
               ge.addError(Errores.ERROR_LIBRO_VACIO);
           }
           if(entity.getPagPagina()==null){
               ge.addError(Errores.ERROR_NUMERO_PAGINA_VACIO);
           }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
