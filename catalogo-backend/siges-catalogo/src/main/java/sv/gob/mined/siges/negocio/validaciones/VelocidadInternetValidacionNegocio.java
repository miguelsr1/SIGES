/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgVelocidadInternet;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class VelocidadInternetValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param vin SgVelocidadInternet
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgVelocidadInternet vin) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (vin==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(vin.getVinCodigo())) {
                ge.addError("vinCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (vin.getVinCodigo().length() > 45){
                ge.addError("vinCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(vin.getVinNombre())) {
                ge.addError("vinNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (vin.getVinNombre().length() > 255){
                ge.addError("vinNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgVelocidadInternet
     * @param c2 SgVelocidadInternet
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgVelocidadInternet c1, SgVelocidadInternet c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getVinCodigo(), c2.getVinCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getVinNombre(), c2.getVinNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getVinHabilitado(), c2.getVinHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
