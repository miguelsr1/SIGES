/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoContrato; 

public class TipoContratoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tco SgTipoContrato
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoContrato tco) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tco==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(tco.getTcoCodigo())){
                ge.addError("tcoCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tco.getTcoCodigo().length() > 45){
                ge.addError("tcoCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            
            if (StringUtils.isBlank(tco.getTcoNombre())){
                ge.addError("tcoNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tco.getTcoNombre().length() > 255){
                ge.addError("tcoNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if(BooleanUtils.isFalse(tco.getTcoAplicaAcuerdo()) && BooleanUtils.isFalse(tco.getTcoAplicaContrato()) && BooleanUtils.isFalse(tco.getTcoAplicaOtros())){
                ge.addError("tcoAcuerdo", Errores.ERROR_APLICA_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
        
}
