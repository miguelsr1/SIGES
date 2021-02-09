/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDesembolso;

/**
 * Validación de las reglas de negocio de desembolsos
 * @author Sofis Solutions
 */
public class DesembolsoValidacionNegocio{

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ded SgDesembolso
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDesembolso ded)throws BusinessException{
        boolean respuesta=true;
        BusinessException ge=new BusinessException();
        if(ded==null){
            ge.addError(Errores.ERROR_DATO_VACIO);
        }else{
            if(ded.getDsbEstado()==null){
                ge.addError("dsbEstado",Errores.ERROR_ESTADO_VACIO);
            }
        }
        if(ge.getErrores().size()>0){
            throw ge;
        }
        return respuesta;
    }

  

}
