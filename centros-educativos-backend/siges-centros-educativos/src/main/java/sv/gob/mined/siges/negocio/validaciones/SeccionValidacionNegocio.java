/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumTipoPeriodoSeccion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;

public class SeccionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgSeccion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSeccion entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(entity.getSecCodigo())) {
                ge.addError("secCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (entity.getSecCodigo().length() > 60) {
                ge.addError("secCodigo", Errores.ERROR_LARGO_CODIGO_60);
            }
            if (StringUtils.isBlank(entity.getSecNombre())) {
                ge.addError("secNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getSecNombre().length() > 255) {
                ge.addError("secNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if (entity.getSecServicioEducativo() == null){
                ge.addError("secServicioEducativo", Errores.ERROR_SERVICIO_EDUCATIVO_VACIO);
            }

            if (entity.getSecPlanEstudio() == null) {
                ge.addError("secPlanEstudio", Errores.ERROR_PLAN_ESTUDIO_VACIO);
            }
            if (entity.getSecJornadaLaboral() == null) {
                ge.addError("secJornadaLaboral", Errores.ERROR_JORNADA_VACIO);
            }
            if (entity.getSecAnioLectivo() == null) {
                ge.addError("secAnioLectivo", Errores.ERROR_ANIO_LECTIVO_VACIO);
            }

            if (entity.getSecEstado() == null) {
                ge.addError("secEstado", Errores.ERROR_ESTADO_VACIO);
            }
            if(!EnumTipoPeriodoSeccion.ANUAL.equals(entity.getSecTipoPeriodo()) && entity.getSecNumeroPeriodo()==null){
                ge.addError("secNumeroPeriodo", Errores.ERROR_NUMERO_PERIODO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
