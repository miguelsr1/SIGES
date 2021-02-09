/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgBeneficio;

/**
 *
 * @author Sofis Solutions
 */
public class BeneficioValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param bnf SgBeneficio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgBeneficio bnf) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (bnf==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(bnf.getBnfNombre())) {
                ge.addError("bnfNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (bnf.getBnfNombre().length() > 100){
                ge.addError("bnfNombre", Errores.ERROR_LARGO_NOMBRE_100);
            }
            
            if (!StringUtils.isBlank(bnf.getBnfDescripcion()) && bnf.getBnfDescripcion().length() > 255){
                ge.addError("bnfDescripcion", Errores.ERROR_LARGO_DESCRIPCION_255);
            }
            
            if (!StringUtils.isBlank(bnf.getBnfObjetivos()) && bnf.getBnfObjetivos().length() > 500){
                ge.addError("bnfObjetivos", Errores.ERROR_LARGO_OBJETIVOS_500);
            }
            
            if (bnf.getBnfTipoBeneficio()==null){
                ge.addError("bnfTipoBeneficio", Errores.ERROR_TIPO_BENEFICIO_VACIO);
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
     * @param c1 SgBeneficio
     * @param c2 SgBeneficio
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgBeneficio c1, SgBeneficio c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getBnfNombre(), c2.getBnfNombre());
                respuesta = respuesta && StringUtils.equals(c1.getBnfDescripcion(), c2.getBnfDescripcion());
                respuesta = respuesta && StringUtils.equals(c1.getBnfObjetivos(), c2.getBnfObjetivos());
                respuesta = respuesta && c1.getBnfTipoBeneficio().equals(c2.getBnfTipoBeneficio());
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
