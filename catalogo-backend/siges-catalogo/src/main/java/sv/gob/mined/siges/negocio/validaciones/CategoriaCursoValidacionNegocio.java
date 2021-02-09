/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCategoriaCurso;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class CategoriaCursoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ccu SgCategoriaCurso
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCategoriaCurso ccu) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ccu==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(ccu.getCcuCodigo())) {
                ge.addError("ccuCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ccu.getCcuCodigo().length() > 45){
                ge.addError("ccuCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(ccu.getCcuNombre())) {
                ge.addError("ccuNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ccu.getCcuNombre().length() > 255){
                ge.addError("ccuNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgCategoriaCurso
     * @param c2 SgCategoriaCurso
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgCategoriaCurso c1, SgCategoriaCurso c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getCcuCodigo(), c2.getCcuCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getCcuNombre(), c2.getCcuNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCcuHabilitado(), c2.getCcuHabilitado());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
