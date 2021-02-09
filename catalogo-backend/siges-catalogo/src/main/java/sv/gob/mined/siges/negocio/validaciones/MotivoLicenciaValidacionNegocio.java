/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoLicencia; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class MotivoLicenciaValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param mli SgMotivoLicencia
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMotivoLicencia mli) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mli==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(mli.getMliCodigo())){
                ge.addError("mliCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mli.getMliCodigo().length() > 4){
                ge.addError("mliCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(mli.getMliNombre())){
                ge.addError("mliNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mli.getMliNombre().length() > 100){
                ge.addError("mliNombre", Errores.ERROR_LARGO_NOMBRE_100);
            }
            
            if (!StringUtils.isBlank(mli.getMliDescripcion())){
                 if(mli.getMliDescripcion().length()> 500){
                     ge.addError("smoDescripcion", Errores.ERROR_LARGO_DESCRIPCION_500);
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
     * @param c1 SgMotivoLicencia
     * @param c2 SgMotivoLicencia
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMotivoLicencia c1, SgMotivoLicencia c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMliCodigo(), c2.getMliCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMliNombre(), c2.getMliNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMliHabilitado(), c2.getMliHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
