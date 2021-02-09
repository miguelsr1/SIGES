/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgGlosario;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class GlosarioValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param glo SgGlosario
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgGlosario glo) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (glo==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(glo.getGloCodigo())) {
                ge.addError("gloCodigo",Errores.ERROR_CODIGO_VACIO);
            } else if (glo.getGloCodigo().length() > 45){
                ge.addError("gloCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(glo.getGloNombre())) {
                ge.addError("gloNombre",Errores.ERROR_NOMBRE_VACIO);
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
     * @param c1 SgGlosario
     * @param c2 SgGlosario
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgGlosario c1, SgGlosario c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getGloCodigo(), c2.getGloCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getGloNombre(), c2.getGloNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getGloHabilitado(), c2.getGloHabilitado());
                respuesta = respuesta && StringUtils.equals(c1.getGloFuente(), c2.getGloFuente());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
