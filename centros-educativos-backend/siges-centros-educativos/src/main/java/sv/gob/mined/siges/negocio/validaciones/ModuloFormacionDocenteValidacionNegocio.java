/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgModuloFormacionDocente;

public class ModuloFormacionDocenteValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgModuloFormacionDocente
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgModuloFormacionDocente entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(entity.getMfdCodigo())){
                ge.addError("mfdCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (entity.getMfdCodigo().length() > 10){
                ge.addError("mfdCodigo", Errores.ERROR_LARGO_CODIGO_10);
            }
            if (StringUtils.isBlank(entity.getMfdNombre())){
                ge.addError("mfdNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getMfdNombre().length() > 255){
                ge.addError("mfdNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
