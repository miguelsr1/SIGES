/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelGradoPlanEstudio;

/**
 *
 * @author Sofis Solutions
 */
public class RelGradoPlanEstudioValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rgp SgRelGradoPlanEstudio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelGradoPlanEstudio rgp) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rgp==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (rgp.getRgpGradoFk()== null){
                ge.addError("rgpGradoFk", Errores.ERROR_GRADO_VACIO);
            }
            if (rgp.getRgpPlanEstudioFk()== null){
                ge.addError("rgpPlanEstudioFk", Errores.ERROR_PLAN_ESTUDIO_VACIO);
            }         
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
