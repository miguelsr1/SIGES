/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgHabitosAlimentacion;

/**
 *
 * @author Sofis Solutions
 */
public class HabitosAlimentacionValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param hal SgHabitosAlimentacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgHabitosAlimentacion hal) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (hal==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if(hal.getHalAnioLectivoFk()==null){
                 ge.addError("achNombre", Errores.ERROR_ANIO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
