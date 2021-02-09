/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoNombramiento; 

public class TipoNombramientoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tno SgTipoNombramiento
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoNombramiento tno) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tno==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(tno.getTnoCodigo())){
                ge.addError("tnoCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tno.getTnoCodigo().length() > 4){
                ge.addError("tnoCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(tno.getTnoNombre())){
                ge.addError("tnoNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tno.getTnoNombre().length() > 255){
                ge.addError("tnoNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if(BooleanUtils.isFalse(tno.getTnoAplicaAcuerdo()) && BooleanUtils.isFalse(tno.getTnoAplicaContrato()) && BooleanUtils.isFalse(tno.getTnoAplicaOtros())){
                ge.addError("tnoAcuerdo", Errores.ERROR_APLICA_VACIO);
            }
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
