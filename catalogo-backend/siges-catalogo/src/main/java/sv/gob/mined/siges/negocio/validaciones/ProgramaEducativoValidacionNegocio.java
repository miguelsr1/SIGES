/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgProgramaEducativo; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class ProgramaEducativoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param ped SgProgramaEducativo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgProgramaEducativo ped) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ped==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(ped.getPedCodigo())){
                ge.addError("pedCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ped.getPedCodigo().length() > 10){
                ge.addError("pedCodigo", Errores.ERROR_LARGO_CODIGO_10);
            }
            
            if (StringUtils.isBlank(ped.getPedNombre())){
                ge.addError("pedNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ped.getPedNombre().length() > 255){
                ge.addError("pedNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
             if (!StringUtils.isBlank(ped.getPedDescripcion())){
                 if(ped.getPedDescripcion().length()> 500){
                     ge.addError("pedDescripcion", Errores.ERROR_LARGO_DESCRIPCION_500);
                 }                
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
     * @param c1 SgProgramaEducativo
     * @param c2 SgProgramaEducativo
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgProgramaEducativo c1, SgProgramaEducativo c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getPedCodigo(), c2.getPedCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getPedNombre(), c2.getPedNombre());
                respuesta = respuesta && StringUtils.equals(c1.getPedDescripcion(), c2.getPedDescripcion());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getPedHabilitado(), c2.getPedHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
