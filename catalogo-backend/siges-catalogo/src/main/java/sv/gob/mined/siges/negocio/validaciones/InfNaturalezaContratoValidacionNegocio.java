/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgInfNaturalezaContrato;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class InfNaturalezaContratoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param nac SgInfNaturalezaContrato
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgInfNaturalezaContrato nac) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (nac==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(nac.getNacCodigo())) {
                ge.addError("nacCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (nac.getNacCodigo().length() > 45){
                ge.addError("nacCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(nac.getNacNombre())) {
                ge.addError("nacNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (nac.getNacNombre().length() > 255){
                ge.addError("nacNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgInfNaturalezaContrato
     * @param c2 SgInfNaturalezaContrato
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgInfNaturalezaContrato c1, SgInfNaturalezaContrato c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getNacCodigo(), c2.getNacCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getNacNombre(), c2.getNacNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getNacHabilitado(), c2.getNacHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
