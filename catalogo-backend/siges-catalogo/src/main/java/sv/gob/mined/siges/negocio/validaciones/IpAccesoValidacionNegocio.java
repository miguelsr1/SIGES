/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgIpsAcceso;
import sv.gob.mined.siges.utils.InetAddressUtils;

public class IpAccesoValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgIpsAcceso
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
        public static boolean validar(SgIpsAcceso etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (StringUtils.isBlank(etn.getIpaCodigo())){
                ge.addError("ipaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getIpaCodigo().length() > 4){
                ge.addError("ipaCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(etn.getIpaNombre())){
                ge.addError("impNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getIpaNombre().length() > 255){
                ge.addError("impNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if (StringUtils.isBlank(etn.getIpaIpAcceso())){
                ge.addError("ipaIpAcceso", Errores.ERROR_IP_VACIO);
            } else if (etn.getIpaIpAcceso().length() > 15){
                ge.addError("ipaIpAcceso", Errores.ERROR_IP_VACIO_LARGO_15);
            } else if (!InetAddressUtils.isIPv4Address(etn.getIpaIpAcceso())) {
                ge.addError("ipaIpAcceso", Errores.ERROR_IP_INVALIDA);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}
