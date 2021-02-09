/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEspacioComun;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class EspacioComunValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param eco SgEspacioComun
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEspacioComun eco) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (eco==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(eco.getEcoCodigo())) {
                ge.addError("ecoCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (eco.getEcoCodigo().length() > 45){
                ge.addError("ecoCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(eco.getEcoNombre())) {
                ge.addError("ecoNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (eco.getEcoNombre().length() > 255){
                ge.addError("ecoNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgEspacioComun
     * @param c2 SgEspacioComun
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgEspacioComun c1, SgEspacioComun c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEcoCodigo(), c2.getEcoCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEcoNombre(), c2.getEcoNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEcoHabilitado(), c2.getEcoHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
