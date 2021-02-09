/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.enumerados.EnumSeccionEstado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAsistencia;
import sv.gob.mined.siges.persistencia.entidades.SgControlAsistenciaCabezal;

public class ControlAsistenciaCabezalValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgControlAsistenciaCabezal entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getCacSeccion() == null) {
                ge.addError("cacSeccion", Errores.ERROR_SECCION_VACIO);
            }
            if (entity.getCacFecha() == null) {
                ge.addError("cacFecha", Errores.ERROR_FECHA_VACIO);
            }
            if (entity.getCacAsistencia() != null) {
                for (SgAsistencia asistencia : entity.getCacAsistencia()) {
                    if (asistencia.getAsiInasistencia().equals(Boolean.TRUE) && asistencia.getAsiMotivoInasistencia() == null) {
                        ge.addError(Errores.ERROR_INASISTENCIA_MOTIVO_VACIO);
                        break;
                    }
                }
            }
            if(!EnumSeccionEstado.ABIERTA.equals(entity.getCacSeccion().getSecEstado())){
                ge.addError("cacSeccion.secEstado", Errores.ERROR_SECCION_NO_ABIERTA);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
