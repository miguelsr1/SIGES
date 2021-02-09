/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMejoras;

/**
 *
 * @author Sofis Solutions
 */
public class MejorasValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mej SgMejoras
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMejoras mej) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mej==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
          if (mej.getMejFecha()== null){
                ge.addError("raeAula", Errores.ERROR_FECHA_VACIO);
            }
            if (mej.getMejTipoMejora()== null){
                ge.addError("raeEspacio", Errores.ERROR_TIPO_MEJORA_VACIO);
            }
            if(StringUtils.isBlank(mej.getMejDescripcion())){
                ge.addError("raeEspacio", Errores.ERROR_DESCRIPCION_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
