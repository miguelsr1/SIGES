/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionCE;

/**
 *
 * @author Sofis Solutions
 */
public class CalificacionValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cal SgCalificacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCalificacionCE cal) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cal==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (cal.getCalSeccion() == null || cal.getCalSeccion().getSecPk() == null){
                ge.addError("calSeccion", Errores.ERROR_SECCION_VACIO);
            }   
                     
            if (cal.getCalTipoPeriodoCalificacion() == null){
                ge.addError("calTipoPeriodoCalificacion", "ERROR_TIPO_PERIODO_CALIFICACION_VACIO");
            } else {
            
                if (!cal.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.GRA) && 
                        (cal.getCalComponentePlanEstudio() == null || cal.getCalComponentePlanEstudio().getCpePk() == null)){
                    ge.addError("calComponentePlanEstudio", Errores.ERROR_COMPONENTE_PLAN_ESTUDIO_VACIO);
                }
            }
            
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Compara dos elementos del tipo.
     * Indica si los elementos contienen la misma información.
     *
     * @param c1 SgCalificacion
     * @param c2 SgCalificacion
     * @return Boolean
     */
 
}
