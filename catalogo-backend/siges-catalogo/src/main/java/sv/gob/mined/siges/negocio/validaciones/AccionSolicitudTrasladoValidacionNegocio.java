/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAccionSolicitudTraslado; 

public class AccionSolicitudTrasladoValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param acc SgAccionSolicitudTraslado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAccionSolicitudTraslado acc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (acc==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(acc.getAccNombreAccion())) {
                ge.addError("accNombreAccion", Errores.ERROR_NOMBRE_ACCION_VACIO);
            } else if (acc.getAccNombreAccion().length() > 45){
                ge.addError("accNombreAccion", Errores.ERROR_LARGO_NOMBRE_ACCION_45);
            }
            if (acc.getAccNum() == null) {
                ge.addError("accNum", Errores.ERROR_NUM_VACIO);
            }
            if (acc.getAccEstadoOrigen() == null) {
                ge.addError("accEstadoOrigen", Errores.ERROR_ESTADO_ORIGEN_VACIO);
            }
            if (acc.getAccEstadoDestino() == null) {
                ge.addError("accEstadoDestino", Errores.ERROR_ESTADO_DESTINO_VACIO);
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
     * @param c1 SgAccionSolicitudTraslado
     * @param c2 SgAccionSolicitudTraslado
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAccionSolicitudTraslado c1, SgAccionSolicitudTraslado c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = c1.getAccNum().equals(c2.getAccNum());
                respuesta = respuesta && StringUtils.equals(c1.getAccEstadoOrigen().getText(), c2.getAccEstadoOrigen().getText());
                respuesta = respuesta && StringUtils.equals(c1.getAccEstadoDestino().getText(), c2.getAccEstadoDestino().getText());
                respuesta = respuesta && StringUtils.equals(c1.getAccNombreAccion(), c2.getAccNombreAccion());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }  
}
