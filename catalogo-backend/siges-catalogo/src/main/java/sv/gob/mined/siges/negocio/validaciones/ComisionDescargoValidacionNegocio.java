/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfComisionDescargo;

public class ComisionDescargoValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param  cla SgClasificacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfComisionDescargo cla) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if ( cla==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank( cla.getCdeNombreRepresentante())){
                ge.addError(Errores.ERROR_NOMBRE_VACIO);
            } else if ( cla.getCdeNombreRepresentante().length() > 255){
                ge.addError(Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if (StringUtils.isBlank( cla.getCdeCargoRepresentante())){
                ge.addError(Errores.ERROR_CARGO_VACIO);
            } else if ( cla.getCdeCargoRepresentante().length() > 255){
                ge.addError(Errores.ERROR_LARGO_CARGO_255);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}
