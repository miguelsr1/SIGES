/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDiploma;

/**
 *
 * @author Sofis Solutions
 */
public class DiplomaValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param dil SgDiploma
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDiploma dil) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (dil==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (dil.getDilSedeFk()==null) {
                ge.addError("dilCodigo", Errores.ERROR_SEDE_VACIO);
            } 
            if (dil.getDilAnioLectivoFk()==null) {
                ge.addError("dilCodigo", Errores.ERROR_ANIO_VACIO);
            } 
            if (dil.getDilDiplomadoFk()==null) {
                ge.addError("dilCodigo", Errores.ERROR_DIPLOMADO_VACIO);
            } 
            if(dil.getDiplomaEstudiantes()==null || dil.getDiplomaEstudiantes().isEmpty()){
                ge.addError("dilCodigo", Errores.ERROR_ESTUDIANTES_VACIO);
            }
           
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
