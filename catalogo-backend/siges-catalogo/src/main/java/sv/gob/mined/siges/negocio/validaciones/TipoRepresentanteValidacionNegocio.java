/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoRepresentante;
import sv.gob.mined.siges.utils.BooleanUtils;
import sv.gob.mined.siges.utils.ObjectUtils;

/**
 *
 * @author Sofis Solutions
 */
public class TipoRepresentanteValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tre SgTipoRepresentante
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoRepresentante tre) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tre==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tre.getTreCodigo())) {
                ge.addError("treCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tre.getTreCodigo().length() > 45){
                ge.addError("treCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tre.getTreNombre())) {
                ge.addError("treNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tre.getTreNombre().length() > 255){
                ge.addError("treNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if (tre.getTreCargoAsociado() == null){
                ge.addError("treCargoAsociado", Errores.ERROR_CARGO_VACIO);
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
     * @param c1 SgTipoRepresentante
     * @param c2 SgTipoRepresentante
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoRepresentante c1, SgTipoRepresentante c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTreCodigo(), c2.getTreCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTreNombre(), c2.getTreNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTreHabilitado(), c2.getTreHabilitado());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getTreCargoAsociado(), c2.getTreCargoAsociado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
