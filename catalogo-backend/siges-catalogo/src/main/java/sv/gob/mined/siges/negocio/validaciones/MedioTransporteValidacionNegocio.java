/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMedioTransporte; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class MedioTransporteValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param mtr SgMedioTransporte
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMedioTransporte mtr) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mtr==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(mtr.getMtrCodigo())){
                ge.addError("mtrCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mtr.getMtrCodigo().length() > 4){
                ge.addError("mtrCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(mtr.getMtrNombre())){
                ge.addError("mtrNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mtr.getMtrNombre().length() > 255){
                ge.addError("mtrNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
    /**
     * Compara dos elementos del tipo. 
     * Indica si los elementos contienen la misma información.
     * 
     * @param c1 SgMedioTransporte
     * @param c2 SgMedioTransporte
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMedioTransporte c1, SgMedioTransporte c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMtrCodigo(), c2.getMtrCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMtrNombre(), c2.getMtrNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMtrHabilitado(), c2.getMtrHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
