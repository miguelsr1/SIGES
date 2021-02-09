/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoServicioSanitario; 

public class TipoServicioSanitarioValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tss SgTipoServicioSanitario
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoServicioSanitario tss) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tss==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(tss.getTssCodigo())){
                ge.addError("tssCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tss.getTssCodigo().length() > 4){
                ge.addError("tssCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(tss.getTssNombre())){
                ge.addError("tssNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tss.getTssNombre().length() > 255){
                ge.addError("tssNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }

    
}
