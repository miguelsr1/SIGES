/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCargo; 

public class CargoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param car SgCargo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCargo car) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (car==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(car.getCarCodigo())){
                ge.addError("carCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (car.getCarCodigo().length() > 45){
                ge.addError("carCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            
            if (StringUtils.isBlank(car.getCarNombre())){
                ge.addError("carNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (car.getCarNombre().length() > 255){
                ge.addError("carNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if(BooleanUtils.isFalse(car.getCarAplicaAcuerdo()) && BooleanUtils.isFalse(car.getCarAplicaContrato()) && BooleanUtils.isFalse(car.getCarAplicaOtros())){
                ge.addError("carAcuerdo", Errores.ERROR_APLICA_VACIO);
            }
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
