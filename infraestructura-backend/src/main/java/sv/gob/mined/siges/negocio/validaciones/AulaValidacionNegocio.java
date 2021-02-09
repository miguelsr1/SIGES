/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAula;

/**
 *
 * @author Sofis Solutions
 */
public class AulaValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param aul SgAula
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAula aul) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (aul==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(aul.getAulCodigo())) {
                ge.addError("aulCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (aul.getAulCodigo().length() > 10){
                ge.addError("aulCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(aul.getAulNombre())) {
                ge.addError("aulNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (aul.getAulNombre().length() > 255){
                ge.addError("aulNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if(aul.getAulEdificioFk()==null){
                ge.addError("aulNombre", Errores.ERROR_EDIFICIO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

 
}
