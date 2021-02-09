/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelPeriodosHabilitacionPeriodo;

/**
 *
 * @author Sofis Solutions
 */
public class RelPeriodosHabilitacionPeriodoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rph SgRelPeriodosHabilitacionPeriodo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelPeriodosHabilitacionPeriodo rph) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rph==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if(rph.getRphTipoPeriodoCalificacion()==null) {
                ge.addError("rphCodigo", Errores.ERROR_TIPO_PERIODO_VACIO);
            } 
            if (rph.getRphRangoFechaFk()==null && rph.getRphTipoCalendarioEscolar()==null) {
                ge.addError("rphNombre", Errores.ERROR_RANGO_TIPO_CALENDARIO_VACIO);
            } 
            if(rph.getRphHabilitacionPeriodoCalificacionFk()==null){
               ge.addError("rphNombre", Errores.ERROR_HABILITACION_PERIODO_CALIFICACION_VACIO); 
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
