/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoAccion;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class TipoAccionValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tac SgTipoAccion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoAccion tac) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tac==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tac.getTacCodigo())) {
                ge.addError("tacCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tac.getTacCodigo().length() > 45){
                ge.addError("tacCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tac.getTacNombre())) {
                ge.addError("tacNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tac.getTacNombre().length() > 255){
                ge.addError("tacNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTipoAccion
     * @param c2 SgTipoAccion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoAccion c1, SgTipoAccion c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTacCodigo(), c2.getTacCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTacNombre(), c2.getTacNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTacHabilitado(), c2.getTacHabilitado());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTacNecesitaDescripcion(), c2.getTacNecesitaDescripcion());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
