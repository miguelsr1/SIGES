/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAsistenciaPersonal;
import sv.gob.mined.siges.persistencia.entidades.SgControlAsistenciaPersonalCabezal;

public class ControlAsistenciaPersonalCabezalValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgControlAsistenciaPersonalCabezal entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getCpcSede() == null) {
                ge.addError("cpcSede", Errores.ERROR_SEDE_VACIO);
            }
            if (entity.getCpcFecha() == null) {
                ge.addError("cpcFecha", Errores.ERROR_FECHA_VACIO);
            }
            if (entity.getCpcAsistenciaPersonal() != null) {
                for (SgAsistenciaPersonal asistencia : entity.getCpcAsistenciaPersonal()) {
                    if (asistencia.getApeInasistencia().equals(Boolean.TRUE) && asistencia.getApeMotivoInasistencia() == null) {
                        ge.addError(Errores.ERROR_INASISTENCIA_MOTIVO_VACIO);
                        break;
                    }
                }
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
