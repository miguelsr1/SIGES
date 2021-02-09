/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgPlaza;

public class PlazaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgPlaza
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPlaza entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(entity.getPlaNombre())) {
                ge.addError("plaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getPlaNombre().length() > 255) {
                ge.addError("plaNombre", Errores.ERROR_LARGO_NOMBRE_100);
            }

            if (entity.getPlaIdPuesto() == null) {
                ge.addError("plaIdPuesto", Errores.ERROR_ID_PUESTO_VACIO);
            }
            if (entity.getPlaPartida() == null) {
                ge.addError("plaPartida", Errores.ERROR_PARTIDA_VACIO);
            }
            if (entity.getPlaSubpartida() == null) {
                ge.addError("plaSubpartida", Errores.ERROR_SUBPARTIDA_VACIO);
            }
            if (entity.getPlaEstado() == null) {
                ge.addError("plaEstado", Errores.ERROR_ESTADO_VACIO);
            }
            if (entity.getPlaSituacion() == null) {
                ge.addError("plaSituacion", Errores.ERROR_SITUACION_VACIO);
            }
            if (entity.getPlaAnioPartida() == null) {
                ge.addError("plaAnioPartida", Errores.ERROR_ANIO_VACIO);
            }
            if (entity.getPlaSedeFk() == null) {
                ge.addError("plaAnioPartida", Errores.ERROR_SEDE_VACIO);
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
