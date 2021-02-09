/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelGradoPrecedencia;

/**
 *
 * @author Sofis Solutions
 */
public class RelGradoPrecedenciaValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rgp SgRelGradoPlanEstudio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelGradoPrecedencia rgp) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rgp==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
              
            if (rgp.getRgpGradoDestinoFk() == null || rgp.getRgpGradoOrigenFk() == null){
                ge.addError("rgpGradoFk", Errores.ERROR_GRADO_VACIO);
            } else if (rgp.getRgpGradoDestinoFk().equals(rgp.getRgpGradoOrigenFk())){
                ge.addError("rgpGradoFk", Errores.ERROR_MISMO_GRADO);
            }
  
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
