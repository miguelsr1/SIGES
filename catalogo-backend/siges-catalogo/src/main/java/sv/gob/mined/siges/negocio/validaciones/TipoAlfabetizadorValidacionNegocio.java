/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoAlfabetizador;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class TipoAlfabetizadorValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param tal SgTipoAlfabetizador
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoAlfabetizador tal) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tal==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tal.getTalCodigo())) {
                ge.addError("talCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tal.getTalCodigo().length() > 45){
                ge.addError("talCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tal.getTalNombre())) {
                ge.addError("talNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tal.getTalNombre().length() > 255){
                ge.addError("talNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTipoAlfabetizador
     * @param c2 SgTipoAlfabetizador
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoAlfabetizador c1, SgTipoAlfabetizador c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTalCodigo(), c2.getTalCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTalNombre(), c2.getTalNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTalHabilitado(), c2.getTalHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
