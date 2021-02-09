/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoVacuna; 
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */  
public class TipoVacunaValidacionNegocio  {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tvc SgTipoVacuna
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoVacuna tvc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tvc==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tvc.getTvcCodigo())) {
                ge.addError("tvcCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tvc.getTvcCodigo().length() > 10){
                ge.addError("tvcCodigo", Errores.ERROR_LARGO_CODIGO_10);
            }
            if (StringUtils.isBlank(tvc.getTvcNombre())) {
                ge.addError("tvcNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tvc.getTvcNombre().length() > 255){
                ge.addError("tvcNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
    /**
     * Compara dos elementos del tipo. 
     * Indica si los elementos contienen la misma información.
     * 
     * @param c1 SgTipoVacuna
     * @param c2 SgTipoVacuna
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoVacuna c1, SgTipoVacuna c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTvcCodigo(), c2.getTvcCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTvcNombre(), c2.getTvcNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTvcHabilitado(), c2.getTvcHabilitado());     
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
