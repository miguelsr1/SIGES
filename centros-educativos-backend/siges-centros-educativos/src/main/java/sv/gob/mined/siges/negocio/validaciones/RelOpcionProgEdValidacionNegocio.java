/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelOpcionProgEd;

public class RelOpcionProgEdValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgRelOpcionProgEd
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelOpcionProgEd entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {           
            if (entity.getRoeOpcion() == null){
                ge.addError("roeOpcion", Errores.ERROR_OPCION_VACIO);
            }   
            if (entity.getRoeProgramaEducativo() == null){
                ge.addError("roeProgramaEducativo", Errores.ERROR_PROGRAMA_EDUCATIVO_VACIO);
            }   
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
