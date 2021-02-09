/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDetalleDesembolso;

/**
 * Validación de las reglas de negocio del detalle desembolso
 * @author Sofis Solutions
 */
public class DetalleDesembolsoValidacionNegocio{

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ded SgDetalleDesembolso
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDetalleDesembolso ded)throws BusinessException{
        boolean respuesta=true;
        BusinessException ge=new BusinessException();
        if(ded==null){
            ge.addError(Errores.ERROR_DATO_VACIO);
        }else{
            if(ded.getDedDesembolsoFk()==null){
                ge.addError("dedCodigo",Errores.ERROR_ESTADO_DESEMBOLSO_VACIO);
            }

        }
        if(ge.getErrores().size()>0){
            throw ge;
        }
        return respuesta;
    }

  

}
