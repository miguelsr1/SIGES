/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleDocumento;

/**
 *
 * @author Sofis Solutions
 */
public class RelInmuebleDocumentoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rid SgRelInmuebleDocumento
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelInmuebleDocumento rid) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rid == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (rid.getRidDocumento() == null) {
                ge.addError(Errores.ERROR_ARCHIVO_VACIO);
            }
            if (StringUtils.isEmpty(rid.getRidDescripcion())) {
                ge.addError(Errores.ERROR_DESCRIPCION_VACIO);
            } else if (rid.getRidDescripcion().length() > 4000) {
                ge.addError(Errores.ERROR_LARGO_DESCRIPCION_4000);
            }
            if (StringUtils.isEmpty(rid.getRidNombre())) {
                ge.addError(Errores.ERROR_NOMBRE_VACIO);
            } else if (rid.getRidNombre().length() > 40) {
                ge.addError(Errores.ERROR_LARGO_NOMBRE_40);
            }
            if (rid.getRidTipoDocumento() == null) {
                ge.addError(Errores.ERROR_TIPO_DOCUMENTO_INFRA_VACIO);
            }
            if (rid.getRidInmuebleSede() == null) {
                ge.addError(Errores.ERROR_INMUEBLE_VACIO);
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
