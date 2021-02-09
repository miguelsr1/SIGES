/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSedeEducame; 


public class SedeEducameValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgSedeEducame
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSedeEducame entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {      
            
            try {
                SedesValidacionNegocio.validar(entity);
            } catch (BusinessException be){
                ge.getErrores().addAll(be.getErrores());
            }
            
            if (entity.getSedSedeAdscritaDe() == null && BooleanUtils.isNotTrue(entity.getSedEsAdscriptaAOtraSede()) ){
                ge.addError("sedSedeAdscritaDe", Errores.ERROR_SEDE_ADSCRITA_VACIO);
            }
            
            // Validaciones de SgSedeEducame
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }    
}
