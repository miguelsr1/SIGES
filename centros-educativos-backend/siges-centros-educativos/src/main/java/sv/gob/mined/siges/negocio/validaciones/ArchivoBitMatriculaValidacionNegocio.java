/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgArchivoBitMatricula;

public class ArchivoBitMatriculaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgArchivoBitMatricula entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if(entity.getAbmDiaIngreso()==null){
                ge.addError("abmDiaIngreso", "Debe completar el día de ingreso.");
            }else if(entity.getAbmDiaIngreso()==0){
                ge.addError("abmDiaIngreso", "El día de ingreso no puede ser 0.");
            }
            
            if(entity.getAbmMesIngreso()==null){
                ge.addError("abmMesIngreso", "Debe completar el mes de ingreso.");
            }else if(entity.getAbmMesIngreso()==0){
                ge.addError("abmMesIngreso", "El mes de ingreso no puede ser 0.");
            }
            
            if(entity.getAbmDiaEgreso()==null){
                ge.addError("abmDiaEgreso", "Debe completar el día de egreso.");
            }else if(entity.getAbmDiaEgreso()==0){
                ge.addError("abmDiaEgreso", "El día de egreso no puede ser 0.");
            }
            
            if(entity.getAbmMesEgreso()==null){
                ge.addError("abmMesEgreso", "Debe completar el mes de egreso.");
            }else if(entity.getAbmMesEgreso()==0){
                ge.addError("abmMesEgreso", "El mes de egreso no puede ser 0.");
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
