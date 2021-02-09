/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoReimpresion;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class MotivoReimpresionValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mri SgMotivoReimpresion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMotivoReimpresion mri) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mri==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(mri.getMriCodigo())) {
                ge.addError("mriCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mri.getMriCodigo().length() > 45){
                ge.addError("mriCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(mri.getMriNombre())) {
                ge.addError("mriNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mri.getMriNombre().length() > 255){
                ge.addError("mriNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgMotivoReimpresion
     * @param c2 SgMotivoReimpresion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMotivoReimpresion c1, SgMotivoReimpresion c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMriCodigo(), c2.getMriCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMriNombre(), c2.getMriNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMriHabilitado(), c2.getMriHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
