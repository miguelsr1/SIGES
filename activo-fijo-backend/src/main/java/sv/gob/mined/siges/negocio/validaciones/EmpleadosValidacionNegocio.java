/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfEmpleados;
import sv.gob.mined.siges.utils.ValidationUtils;

public class EmpleadosValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param dis SgAfEmpleados
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfEmpleados emp) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (emp==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            
            
            if(emp.getEmpPersonaFk() == null) {
                ge.addError("empPersonaFk", Errores.ERROR_PERSONA_VACIO);
            }else {
                if(emp.getEmpPersonaFk().getPerDui() == null || StringUtils.isBlank(emp.getEmpPersonaFk().getPerDui())) {
                    ge.addError("perDui", Errores.ERROR_DUI_VACIO);
                } else {
                    if (!ValidationUtils.validarDUI(emp.getEmpPersonaFk().getPerDui())) {
                        ge.addError("perDui", Errores.ERROR_DUI_INCORRECTO);
                    }
                }
            }
            
            if (emp.getEmpUnidadAdministrativaFk() == null){
                ge.addError("empUnidadAdministrativaFk", Errores.ERROR_UNIDAD_ADMINISTRATIVA_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }   
}
