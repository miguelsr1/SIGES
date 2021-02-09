/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgInfMinisterioOtorgante;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class InfMinisterioOtorganteValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mio SgInfMinisterioOtorgante
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgInfMinisterioOtorgante mio) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mio==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(mio.getMioCodigo())) {
                ge.addError("mioCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mio.getMioCodigo().length() > 45){
                ge.addError("mioCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(mio.getMioNombre())) {
                ge.addError("mioNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mio.getMioNombre().length() > 255){
                ge.addError("mioNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgInfMinisterioOtorgante
     * @param c2 SgInfMinisterioOtorgante
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgInfMinisterioOtorgante c1, SgInfMinisterioOtorgante c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMioCodigo(), c2.getMioCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMioNombre(), c2.getMioNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMioHabilitado(), c2.getMioHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
