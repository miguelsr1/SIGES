/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDesembolsoCed;

/**
 * Validación de las reglas de negocio de desembolso a centros educativos
 * @author Sofis Solutions
 */
public class DesembolsoCedValidacionNegocio{

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param dce SgDesembolsoCed
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDesembolsoCed dce)throws BusinessException{
        boolean respuesta=true;
        BusinessException ge=new BusinessException();
        if(dce==null){
            ge.addError(Errores.ERROR_DATO_VACIO);
        }
        else{
            if(dce.getDceDetDesembolsoFk()==null){
                ge.addError("dceDetDesembolsoFk",Errores.ERROR_CODIGO_VACIO);
            }
            if(dce.getDceEstado()==null){
                ge.addError("dceEstado",Errores.ERROR_NOMBRE_VACIO);
            }
            if(dce.getDceMonto()==null){
                ge.addError("dceMonto",Errores.ERROR_NOMBRE_VACIO);
            }
        }
        if(ge.getErrores().size()>0){
            throw ge;
        }
        return respuesta;
    }


}