/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelOAEIdentificacionOAE;

/**
 *
 * @author Sofis Solutions
 */
public class RelOAEIdentificacionOAEValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rio SgRelOAEIdentificacionOAE
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelOAEIdentificacionOAE rio) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rio == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (rio.getRioIdentificacionOAEfk() == null) {
                ge.addError(Errores.ERROR_IDENTIFICACION_OAE_VACIO);
            } else {
                if (EnumEstadoOrganismoAdministrativo.APROBADO.equals(rio.getRioOrganismoAdministracionEscolarFk().getOaeEstado()) && BooleanUtils.isTrue(rio.getRioIdentificacionOAEfk().getIoaEsObligatorio()) && StringUtils.isBlank(rio.getRioValor())) {
                      ge.addError(Errores.ERROR_VALOR_VACIO_IDENTIFICACION_OBLIGATORIA);
                }
            }
            if (rio.getRioOrganismoAdministracionEscolarFk()== null) {
                ge.addError(Errores.ERROR_OAE_VACIO);
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
