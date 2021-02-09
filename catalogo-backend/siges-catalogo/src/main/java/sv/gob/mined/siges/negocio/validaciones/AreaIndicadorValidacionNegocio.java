/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAreaIndicador;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class AreaIndicadorValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param arein SgAreaIndicador
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAreaIndicador arein) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (arein==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(arein.getAriCodigo())) {
                ge.addError("areinCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (arein.getAriCodigo().length() > 45){
                ge.addError("areinCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(arein.getAriNombre())) {
                ge.addError("areinNombre",Errores.ERROR_NOMBRE_VACIO);
            } else if (arein.getAriNombre().length() > 255){
                ge.addError("areinNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgAreaIndicador
     * @param c2 SgAreaIndicador
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAreaIndicador c1, SgAreaIndicador c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getAriCodigo(), c2.getAriCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getAriNombre(), c2.getAriNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getAriHabilitado(), c2.getAriHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
