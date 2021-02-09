/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgConfirmacionMatriculas;

/**
 *
 * @author Sofis Solutions
 */
public class ConfirmacionMatriculasValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cma SgConfirmacionMatriculas
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgConfirmacionMatriculas cma) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cma==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if (cma.getCmaSede() == null){
                ge.addError("cmaSede", Errores.ERROR_SEDE_VACIO);
            }
            
            if (cma.getCmaAnioLectivo() == null){
                ge.addError("cmaAnioLectivo", Errores.ERROR_ANIO_LECTIVO_VACIO);
            }
            
           
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
