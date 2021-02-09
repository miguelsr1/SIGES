/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgHabitosPersonales;

/**
 *
 * @author Sofis Solutions
 */
public class HabitosPersonalesValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param hap SgHabitosPersonales
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgHabitosPersonales hap) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (hap==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if(hap.getHapAnioLectivoFk()==null){
                 ge.addError("achNombre", Errores.ERROR_ANIO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
