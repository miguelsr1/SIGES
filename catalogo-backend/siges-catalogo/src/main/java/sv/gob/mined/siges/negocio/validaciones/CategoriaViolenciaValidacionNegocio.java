/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCategoriaViolencia;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class CategoriaViolenciaValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cav SgCategoriaViolencia
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCategoriaViolencia cav) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cav==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(cav.getCavCodigo())) {
                ge.addError("cavCodigo", (Errores.ERROR_CODIGO_VACIO));
            } else if (cav.getCavCodigo().length() > 45){
                ge.addError("cavCodigo", (Errores.ERROR_LARGO_CODIGO_45));
            }
            if (StringUtils.isBlank(cav.getCavNombre())) {
                ge.addError("cavNombre", (Errores.ERROR_NOMBRE_VACIO));
            } else if (cav.getCavNombre().length() > 255){
                ge.addError("cavNombre", (Errores.ERROR_LARGO_NOMBRE_255));
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Compara dos elementos del tipo.
     * Indica si los elementos contienen la misma información.
     *
     * @param c1 SgCategoriaViolencia
     * @param c2 SgCategoriaViolencia
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgCategoriaViolencia c1, SgCategoriaViolencia c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getCavCodigo(), c2.getCavCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getCavNombre(), c2.getCavNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCavHabilitado(), c2.getCavHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
