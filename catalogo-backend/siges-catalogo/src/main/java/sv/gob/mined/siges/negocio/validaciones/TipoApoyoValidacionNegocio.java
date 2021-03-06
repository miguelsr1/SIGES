/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoApoyo;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class TipoApoyoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tap SgTipoApoyo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoApoyo tap) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tap==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tap.getTapCodigo())) {
                ge.addError("tapCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tap.getTapCodigo().length() > 45){
                ge.addError("tapCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tap.getTapNombre())) {
                ge.addError("tapNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tap.getTapNombre().length() > 255){
                ge.addError("tapNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTipoApoyo
     * @param c2 SgTipoApoyo
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoApoyo c1, SgTipoApoyo c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTapCodigo(), c2.getTapCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTapNombre(), c2.getTapNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTapHabilitado(), c2.getTapHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
