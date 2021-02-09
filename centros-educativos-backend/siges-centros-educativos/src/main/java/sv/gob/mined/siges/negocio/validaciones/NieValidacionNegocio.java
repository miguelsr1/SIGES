/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgNie;

/**
 *
 * @author Sofis Solutions
 */
public class NieValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param nie SgNie
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgNie nie) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (nie==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
          /*  if (StringUtils.isBlank(nie.getNieCodigo())) {
                ge.addError("nieCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (nie.getNieCodigo().length() > 45){
                ge.addError("nieCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(nie.getNieNombre())) {
                ge.addError("nieNombre",Errores.ERROR_NOMBRE_VACIO);
            } else if (nie.getNieNombre().length() > 255){
                ge.addError("nieNombre",Errores.ERROR_LARGO_NOMBRE_255);
            }*/
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
     * @param c1 SgNie
     * @param c2 SgNie
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgNie c1, SgNie c2) {
        boolean respuesta = true;
        if (c1!=null) {
        /*    if (c2!=null) {
                respuesta = StringUtils.equals(c1.getNieCodigo(), c2.getNieCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getNieNombre(), c2.getNieNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getNieHabilitado(), c2.getNieHabilitado());
            }*/
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
