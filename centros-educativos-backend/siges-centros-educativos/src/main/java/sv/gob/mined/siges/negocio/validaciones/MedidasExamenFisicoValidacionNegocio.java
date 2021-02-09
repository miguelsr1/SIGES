/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMedidasExamenFisico;

/**
 *
 * @author Sofis Solutions
 */
public class MedidasExamenFisicoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mef SgMedidasExamenFisico
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMedidasExamenFisico mef) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mef==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if(mef.getMefCbd()==null && mef.getMefEdad()==null && mef.getMefImc()==null &&
                    mef.getMefPeso()==null && mef.getMefTalla()==null){
                ge.addError(Errores.ERROR_COMPLETAR_AL_MENOS_UN_DATO);
            }
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
