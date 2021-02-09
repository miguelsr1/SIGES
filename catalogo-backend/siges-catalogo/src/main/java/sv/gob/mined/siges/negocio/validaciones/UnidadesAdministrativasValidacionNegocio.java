/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfUnidadesAdministrativas;

public class UnidadesAdministrativasValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgAfUnidadesAdministrativas
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfUnidadesAdministrativas uad) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (uad == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(uad.getUadCodigo())){
                ge.addError("uadCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (uad.getUadCodigo().length() > 7){
                ge.addError("uadCodigo", Errores.ERROR_LARGO_CODIGO_7);
            }

            if (StringUtils.isBlank(uad.getUadNombre())){
                ge.addError("uadNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (uad.getUadNombre().length() > 255){
                ge.addError("uadNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if(uad.getUadUnidadActivoFijoFk()== null) {
                ge.addError("uadUnidadActivoFijoFk", Errores.ERROR_UNIDAD_ACTIVO_FIJO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}