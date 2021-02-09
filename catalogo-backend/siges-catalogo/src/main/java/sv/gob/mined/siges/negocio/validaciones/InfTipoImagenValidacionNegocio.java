/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgInfTipoImagen;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class InfTipoImagenValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tii SgInfTipoImagen
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgInfTipoImagen tii) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tii==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tii.getTiiCodigo())) {
                ge.addError("tiiCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tii.getTiiCodigo().length() > 45){
                ge.addError("tiiCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tii.getTiiNombre())) {
                ge.addError("tiiNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tii.getTiiNombre().length() > 255){
                ge.addError("tiiNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgInfTipoImagen
     * @param c2 SgInfTipoImagen
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgInfTipoImagen c1, SgInfTipoImagen c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTiiCodigo(), c2.getTiiCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTiiNombre(), c2.getTiiNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTiiHabilitado(), c2.getTiiHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
