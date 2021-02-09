/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCeldaDiaHora;

/**
 *
 * @author Sofis Solutions
 */
public class CeldaDiaHoraValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cdh SgCeldaDiaHora
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCeldaDiaHora cdh) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cdh==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
          /*  if (StringUtils.isBlank(cdh.getCdhCodigo())) {
                ge.addError("cdhCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (cdh.getCdhCodigo().length() > 45){
                ge.addError("cdhCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(cdh.getCdhNombre())) {
                ge.addError("cdhNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (cdh.getCdhNombre().length() > 255){
                ge.addError("cdhNombre",Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgCeldaDiaHora
     * @param c2 SgCeldaDiaHora
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgCeldaDiaHora c1, SgCeldaDiaHora c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
         /*       respuesta = StringUtils.equals(c1.getCdhCodigo(), c2.getCdhCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getCdhNombre(), c2.getCdhNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCdhHabilitado(), c2.getCdhHabilitado());*/
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
