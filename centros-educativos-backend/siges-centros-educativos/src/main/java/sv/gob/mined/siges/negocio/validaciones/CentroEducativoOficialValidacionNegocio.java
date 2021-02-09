/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCentroEducativoOficial;

public class CentroEducativoOficialValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgCentroEducativoOficial
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCentroEducativoOficial entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (BooleanUtils.isFalse(entity.getSedOrigenExterno())) {
                if (entity.getCedTipoOrganismoAdministrativo() == null) {
                    ge.addError("cedTipoOrganismoAdministrativo", Errores.ERROR_TIPO_ORGANISMO_ADMINISTRATIVO_VACIO);
                }
            }
            try {
                SedesValidacionNegocio.validar(entity);
            } catch (BusinessException be) {
                ge.getErrores().addAll(be.getErrores());
            }

            // Validaciones de SgCentroEducativoOficial
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
