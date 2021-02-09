/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgArchivo;

/**
 * Validación de las reglas de negocio de los archivos
 *
 * @author jgiron
 */
public class ArchivoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgArchivo entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(entity.getAchNombre())) {
                ge.addError("achNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getAchNombre().length() > 255) {
                ge.addError("achNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }

            if (entity.getAchPk() == null) {
                if (StringUtils.isBlank(entity.getAchTmpPath())) {
                    ge.addError(Errores.ERROR_ARCHIVO_VACIO);
                } else if (entity.getAchTmpPath().length() > 500) {
                    ge.addError(Errores.ERROR_LARGO_PATH_500);
                }
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
