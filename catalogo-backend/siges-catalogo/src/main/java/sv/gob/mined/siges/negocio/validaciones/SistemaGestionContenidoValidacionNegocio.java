/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSistemaGestionContenido; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class SistemaGestionContenidoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param sgc SgSistemaGestionContenido
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSistemaGestionContenido sgc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (sgc==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(sgc.getSgcCodigo())){
                ge.addError("sgcCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (sgc.getSgcCodigo().length() > 45){
                ge.addError("sgcCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            
            if (StringUtils.isBlank(sgc.getSgcNombre())){
                ge.addError("sgcNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (sgc.getSgcNombre().length() > 255){
                ge.addError("sgcNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgSistemaGestionContenido
     * @param c2 SgSistemaGestionContenido
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgSistemaGestionContenido c1, SgSistemaGestionContenido c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getSgcCodigo(), c2.getSgcCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getSgcNombre(), c2.getSgcNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getSgcHabilitado(), c2.getSgcHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}


