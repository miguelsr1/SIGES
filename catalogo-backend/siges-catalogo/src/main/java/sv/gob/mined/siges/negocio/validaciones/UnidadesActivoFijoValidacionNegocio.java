/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfUnidadesActivoFijo;

public class UnidadesActivoFijoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgAfUnidadesActivoFijo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfUnidadesActivoFijo etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(etn.getUafCodigo())){
                ge.addError("uafCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getUafCodigo().length() > 5){
                ge.addError("uafCodigo", Errores.ERROR_LARGO_CODIGO_5);
            }

            if (StringUtils.isBlank(etn.getUafNombre())){
                ge.addError("uafNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getUafNombre().length() > 255){
                ge.addError("uafNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if(etn.getUafDepartamento()== null) {
                ge.addError("uafDepartamento", Errores.ERROR_DEPARTAMENTO_VACIO);
            }
            
            if (StringUtils.isBlank(etn.getUafResponsableAF())){
                ge.addError("uafResponsableAF", Errores.ERROR_RESPONSABLE_AF_VACIO);
            } else if (etn.getUafNombre().length() > 100){
                ge.addError("uafResponsableAF", Errores.ERROR_LARGO_RESPONSABLE_AF_100);
            }
            
            if (StringUtils.isBlank(etn.getUafCargoResponsableAF())){
                ge.addError("uafCargoResponsableAF", Errores.ERROR_CARGO_RESPONSABLE_AF_VACIO);
            } else if (etn.getUafNombre().length() > 100){
                ge.addError("uafCargoResponsableAF", Errores.ERROR_LARGO_CARGO_RESPONSABLE_AF_100);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}