/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoPermuta; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class MotivoPermutaValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param mpe SgMotivoPermuta
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMotivoPermuta mpe) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mpe==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(mpe.getMpeCodigo())){
                ge.addError("mpeCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mpe.getMpeCodigo().length() > 4){
                ge.addError("mpeCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(mpe.getMpeNombre())){
                ge.addError("mpeNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mpe.getMpeNombre().length() > 100){
                ge.addError("mpeNombre", Errores.ERROR_LARGO_NOMBRE_100);
            }
            if (!StringUtils.isBlank(mpe.getMpeDescripcion())){
                 if(mpe.getMpeDescripcion().length()> 500){
                     ge.addError("mpeDescripcion", Errores.ERROR_LARGO_DESCRIPCION_500);
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
     * @param c1 SgMotivoPermuta
     * @param c2 SgMotivoPermuta
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMotivoPermuta c1, SgMotivoPermuta c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMpeCodigo(), c2.getMpeCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMpeNombre(), c2.getMpeNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMpeHabilitado(), c2.getMpeHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
