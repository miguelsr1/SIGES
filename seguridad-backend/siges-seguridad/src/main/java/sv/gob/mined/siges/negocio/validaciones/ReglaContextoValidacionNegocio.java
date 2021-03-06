/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgReglaContexto;

public class ReglaContextoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgUsuario
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgReglaContexto entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (StringUtils.isBlank(entity.getRecNombre())) {
                ge.addError("recNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getRecNombre().length() > 255) {
                ge.addError("recNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }

            if (StringUtils.isBlank(entity.getRecRegla())) {
                ge.addError("recRegla", Errores.ERROR_REGLA_VACIO);
            } else if (entity.getRecRegla().toUpperCase().contains("DROP")
                    || entity.getRecRegla().toUpperCase().contains("DELETE")
                    || entity.getRecRegla().toUpperCase().contains("TRUNCATE")
                    || entity.getRecRegla().toUpperCase().contains("INSERT")){
                ge.addError("recRegla", Errores.ERROR_REGLA_INCORRECTA);
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
