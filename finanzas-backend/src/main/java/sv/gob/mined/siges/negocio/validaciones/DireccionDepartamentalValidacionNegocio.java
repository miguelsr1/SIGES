/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDireccionDepartamental;

/**
 * Validación de los datos de las pagadurías de las DDE
 *
 * @author Sofis Solutions
 */
public class DireccionDepartamentalValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ded SgDireccionDepartamental
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDireccionDepartamental ded) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ded == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (StringUtils.isBlank(ded.getDedNombre())) {
                ge.addError("dedNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ded.getDedNombre().length() > 255) {
                ge.addError("dedNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }

            if (StringUtils.isBlank(ded.getDedNit())) {
                ge.addError("dedNit", Errores.ERROR_NIT_VACIO);
            } else if (ded.getDedNombre().length() > 255) {
                ge.addError("dedNit", Errores.ERROR_NIT_50);
            }

            if (!StringUtils.isBlank(ded.getDedTelefono()) && ded.getDedTelefono().length() > 20) {
                ge.addError("dedTelefono", Errores.ERROR_TELEFONO_20);
            }

            if (!StringUtils.isBlank(ded.getDedFax()) && ded.getDedFax().length() > 20) {
                ge.addError("dedFax", Errores.ERROR_FAX_20);
            }

            if (!StringUtils.isBlank(ded.getDedIpAutorizada()) && ded.getDedIpAutorizada().length() > 50) {
                ge.addError("dedIpAutorizada", Errores.ERROR_IP_50);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
