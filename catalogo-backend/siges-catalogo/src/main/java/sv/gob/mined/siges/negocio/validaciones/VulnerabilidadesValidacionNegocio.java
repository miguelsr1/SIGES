/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgVulnerabilidades;
import sv.gob.mined.siges.utils.BooleanUtils;


public class VulnerabilidadesValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgVulnerabilidades
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
        public static boolean validar(SgVulnerabilidades etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (StringUtils.isBlank(etn.getVulCodigo())){
                ge.addError("vulCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getVulCodigo().length() > 4){
                ge.addError("vulCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(etn.getVulNombre())){
                ge.addError("vulNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getVulNombre().length() > 255){
                ge.addError("vulNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgVulnerabilidades
     * @param c2 SgVulnerabilidades
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgVulnerabilidades c1, SgVulnerabilidades c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getVulCodigo(), c2.getVulCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getVulNombre(), c2.getVulNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getVulHabilitado(), c2.getVulHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }  
}
