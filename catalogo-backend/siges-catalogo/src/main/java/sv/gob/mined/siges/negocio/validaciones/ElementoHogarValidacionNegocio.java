/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgElementoHogar;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class ElementoHogarValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param eho SgElementoHogar
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgElementoHogar eho) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (eho==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(eho.getEhoCodigo())) {
                ge.addError("ehoCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (eho.getEhoCodigo().length() > 45){
                ge.addError("ehoCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(eho.getEhoNombre())) {
                ge.addError("ehoNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (eho.getEhoNombre().length() > 255){
                ge.addError("ehoNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgElementoHogar
     * @param c2 SgElementoHogar
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgElementoHogar c1, SgElementoHogar c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEhoCodigo(), c2.getEhoCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEhoNombre(), c2.getEhoNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEhoHabilitado(), c2.getEhoHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
