/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgInstitucionPagaSalario; 

public class InstitucionPagaSalarioValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param ips SgInstitucionPagaSalario
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgInstitucionPagaSalario ips) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ips==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(ips.getIpsCodigo())){
                ge.addError("ipsCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ips.getIpsCodigo().length() > 4){
                ge.addError("ipsCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(ips.getIpsNombre())){
                ge.addError("ipsNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ips.getIpsNombre().length() > 255){
                ge.addError("ipsNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if (ips.getIpsTipoInstitucion() == null){
                ge.addError("ipsTipoInstitucion", Errores.ERROR_TIPO_INSTITUCION_VACIO);
            }
            
            if(BooleanUtils.isFalse(ips.getIpsAplicaAcuerdo()) && BooleanUtils.isFalse(ips.getIpsAplicaContrato()) && BooleanUtils.isFalse(ips.getIpsAplicaOtros())){
                ge.addError("ipsAcuerdo", Errores.ERROR_APLICA_VACIO);
            }
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
     
    
}
