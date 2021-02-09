/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoTrabajo; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class TipoTrabajoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param ttr SgTipoTrabajo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoTrabajo ttr) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ttr==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(ttr.getTtrCodigo())){
                ge.addError("ttrCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ttr.getTtrCodigo().length() > 4){
                ge.addError("ttrCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(ttr.getTtrNombre())){
                ge.addError("ttrNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ttr.getTtrNombre().length() > 255){
                ge.addError("ttrNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTipoTrabajo
     * @param c2 SgTipoTrabajo
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoTrabajo c1, SgTipoTrabajo c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTtrCodigo(), c2.getTtrCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTtrNombre(), c2.getTtrNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTtrHabilitado(), c2.getTtrHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}

