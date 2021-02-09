/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDiplomaEstudiante;

/**
 *
 * @author Sofis Solutions
 */
public class DiplomaEstudianteValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param die SgDiplomaEstudiante
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDiplomaEstudiante die) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (die==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (die.getDieDiplomaFk()==null) {
                ge.addError("dieCodigo", Errores.ERROR_DIPLOMA_VACIO);
            } 
            if (die.getDieEstudianteFk()==null) {
                ge.addError("dieCodigo", Errores.ERROR_ESTUDIANTE_VACIO);
            } 
    
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
