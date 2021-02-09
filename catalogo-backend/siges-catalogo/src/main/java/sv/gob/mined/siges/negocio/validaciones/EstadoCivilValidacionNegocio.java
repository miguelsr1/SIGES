/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEstadoCivil; 
import sv.gob.mined.siges.utils.BooleanUtils;


public class EstadoCivilValidacionNegocio {
    
                /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param  eci SgEstadoCivil
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEstadoCivil eci) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (eci==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if (StringUtils.isBlank(eci.getEciCodigo())){
                ge.addError("eciCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (eci.getEciCodigo().length() > 4){
                ge.addError("eciCodigo", Errores.ERROR_LARGO_CODIGO_4);

            }
            if (StringUtils.isBlank(eci.getEciNombre())){
                ge.addError("eciNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (eci.getEciNombre().length() > 255){
                ge.addError("eciNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgEstadoCivil
     * @param c2 SgEstadoCivil
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgEstadoCivil c1, SgEstadoCivil c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEciCodigo(), c2.getEciCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEciNombre(), c2.getEciNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEciHabilitado(), c2.getEciHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
