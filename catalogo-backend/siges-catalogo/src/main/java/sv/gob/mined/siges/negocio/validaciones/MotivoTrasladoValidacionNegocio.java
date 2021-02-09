/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoTraslado; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class MotivoTrasladoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param mot SgMotivoTraslado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMotivoTraslado mot) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mot==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(mot.getMotCodigo())){
                ge.addError("motCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mot.getMotCodigo().length() > 4){
                ge.addError("motCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(mot.getMotNombre())){
                ge.addError("motNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mot.getMotNombre().length() > 255){
                ge.addError("motNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgMotivoTraslado
     * @param c2 SgMotivoTraslado
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMotivoTraslado c1, SgMotivoTraslado c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMotCodigo(), c2.getMotCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMotNombre(), c2.getMotNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMotHabilitado(), c2.getMotHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
