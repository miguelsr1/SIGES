/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgFormula;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class FormulaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param fom SgFormula
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgFormula fom) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (fom == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(fom.getFomCodigo())) {
                ge.addError("fomCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (fom.getFomCodigo().length() > 45) {
                ge.addError("fomCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(fom.getFomNombre())) {
                ge.addError("fomNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (fom.getFomNombre().length() > 255) {
                ge.addError("fomNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if (fom.getFomTipoFormula() == null) {
                ge.addError("fomTipoFormula", Errores.ERROR_TIPO_FORMULA_VACIO);
            }

            if (fom.getFomTextoLargo() != null && fom.getFomTextoLargo().length() > 4000) {
                ge.addError("fomTextoLargo", Errores.ERROR_LARGO_TEXTO_4000);
            }
            if (fom.getFomDescripcion() != null && fom.getFomDescripcion().length() > 255) {
                ge.addError("fomDescripcion", Errores.ERROR_LARGO_DESCRIPCION_255);
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Compara dos elementos del tipo. Indica si los elementos contienen la
     * misma información.
     *
     * @param c1 SgFormula
     * @param c2 SgFormula
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgFormula c1, SgFormula c2) {
        boolean respuesta = true;
        if (c1 != null) {
            if (c2 != null) {
                respuesta = StringUtils.equals(c1.getFomCodigo(), c2.getFomCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getFomNombre(), c2.getFomNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getFomHabilitado(), c2.getFomHabilitado());
                respuesta = respuesta && StringUtils.equals(c1.getFomTextoLargo(), c2.getFomTextoLargo());
                respuesta = respuesta && StringUtils.equals(c1.getFomDescripcion(), c2.getFomDescripcion());
                respuesta = respuesta && c1.getFomTipoFormula().equals(c2.getFomTipoFormula());
            }
        } else {
            respuesta = c2 == null;
        }
        return respuesta;
    }
}
