/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoConstruccion;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class TipoConstruccionValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tco SgTipoConstruccion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoConstruccion tco) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tco==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tco.getTcoCodigo())) {
                ge.addError("tcoCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tco.getTcoCodigo().length() > 45){
                ge.addError("tcoCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tco.getTcoNombre())) {
                ge.addError("tcoNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tco.getTcoNombre().length() > 255){
                ge.addError("tcoNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTipoConstruccion
     * @param c2 SgTipoConstruccion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoConstruccion c1, SgTipoConstruccion c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTcoCodigo(), c2.getTcoCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTcoNombre(), c2.getTcoNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTcoHabilitado(), c2.getTcoHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
