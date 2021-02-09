/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgOrganizacionCurricular; 

public class OrganizacionCurricularValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgOrganizacionCurricular
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgOrganizacionCurricular entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(entity.getOcuCodigo())){
                ge.addError("ocuCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (entity.getOcuCodigo().length() > 4){
                ge.addError("ocuCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(entity.getOcuNombre())){
                ge.addError("ocuNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getOcuNombre().length() > 255){
                ge.addError("ocuNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}