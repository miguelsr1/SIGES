/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgGrado;

public class GradoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgGrado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgGrado entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(entity.getGraCodigo())) {
                ge.addError("graCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (entity.getGraCodigo().length() > 4) {
                ge.addError(Errores.ERROR_LARGO_CODIGO_4);
            }
            if (StringUtils.isBlank(entity.getGraNombre())) {
                ge.addError("graNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getGraNombre().length() > 255) {
                ge.addError(Errores.ERROR_LARGO_NOMBRE_255);
            }
            if (entity.getGraRelacionModalidad() == null) {
                ge.addError("graRelacionModalidad", Errores.ERROR_RELACION_MODALIDAD_VACIA);
            }
            if (entity.getGraEdadMaxima() != null) {
                if (entity.getGraEdadMaxima() > 100) {
                    ge.addError("graRelacionModalidad", Errores.ERROR_EDAD_MAXIMA_MAYOR_100);
                }
                if (entity.getGraEdadMinima() != null) {
                    if (entity.getGraEdadMaxima() < entity.getGraEdadMinima()) {
                        ge.addError("graRelacionModalidad", Errores.ERROR_EDAD_MINIMA_MAYOR_EDAD_MAXIMA);
                    }
                }
            }    
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
