/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgLeySalario; 

public class LeySalarioValidacionNegocio {
    
    private static final Logger LOGGER = Logger.getLogger(LeySalarioValidacionNegocio.class.getName());
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param lsa SgLeySalario
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgLeySalario lsa) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (lsa==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(lsa.getLsaCodigo())){
                ge.addError("lsaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (lsa.getLsaCodigo().length() > 4){
                ge.addError("lsaCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(lsa.getLsaNombre())){
                ge.addError("lsaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (lsa.getLsaNombre().length() > 255){
                ge.addError("lsaNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
  
    
}
