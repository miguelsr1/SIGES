/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoCierreSede;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class MotivoCierreSedeValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mcs SgMotivoCierreSede
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMotivoCierreSede mcs) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mcs==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(mcs.getMcsCodigo())) {
                ge.addError("mcsCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mcs.getMcsCodigo().length() > 45){
                ge.addError("mcsCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(mcs.getMcsNombre())) {
                ge.addError("mcsNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mcs.getMcsNombre().length() > 255){
                ge.addError("mcsNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgMotivoCierreSede
     * @param c2 SgMotivoCierreSede
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMotivoCierreSede c1, SgMotivoCierreSede c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMcsCodigo(), c2.getMcsCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMcsNombre(), c2.getMcsNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMcsHabilitado(), c2.getMcsHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
