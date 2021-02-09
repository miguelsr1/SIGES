/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoAnulacionTitulo;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class MotivoAnulacionTituloValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mat SgMotivoAnulacionTitulo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMotivoAnulacionTitulo mat) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mat==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(mat.getMatCodigo())) {
                ge.addError("matCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mat.getMatCodigo().length() > 45){
                ge.addError("matCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(mat.getMatNombre())) {
                ge.addError("matNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mat.getMatNombre().length() > 255){
                ge.addError("matNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgMotivoAnulacionTitulo
     * @param c2 SgMotivoAnulacionTitulo
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMotivoAnulacionTitulo c1, SgMotivoAnulacionTitulo c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMatCodigo(), c2.getMatCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMatNombre(), c2.getMatNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMatHabilitado(), c2.getMatHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
