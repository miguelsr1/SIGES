/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgConfirmacionSolicitudTraslado;

public class ConfirmacionSolicitudTrasladoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgSolicitudTraslado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgConfirmacionSolicitudTraslado entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getSotSolicitudTrasladoPk() == null) {
                ge.addError("sotFechaSolicitud", Errores.ERROR_SOLICITUD_TRASLADO_VACIA);
            }
            if (!StringUtils.isBlank(entity.getSotResolucion()) && entity.getSotResolucion().length() > 500) {
                ge.addError("sotResolucion", Errores.ERROR_LARGO_RESOLUCION_500);
            }
            if (entity.getSotSeccion() == null || entity.getSotSeccion().getSecPk() == null) {
                ge.addError("sotSeccion", Errores.ERROR_SECCION_VACIO);
            }
            if (entity.getSotFechaTraslado() == null) {
                ge.addError("sotFechaTraslado", Errores.ERROR_FECHA_TRASLADO_VACIA);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
