/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAcuerdo;

/**
 *
 * @author Sofis Solutions
 */
public class AcuerdoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param acu SgAcuerdo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAcuerdo acu) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (acu==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(acu.getAcuNombreAcuerdo())){
                ge.addError("capNombreCapacitacion", Errores.ERROR_NOMBRE_VACIO);
            } else{
                if(acu.getAcuNombreAcuerdo().length()>255){
                    ge.addError("capNombreCapacitacion", Errores.ERROR_NOMBRE_255);
                }
            }
            if(acu.getAcuEstado()==null){
                ge.addError("capNombreCapacitacion", Errores.ERROR_ESTADO_VACIO);
            }
            if(StringUtils.isBlank(acu.getAcuDescripcion())){
                ge.addError("capNombreCapacitacion", Errores.ERROR_DESCRIPCION_VACIO);
            }else{
                if(acu.getAcuDescripcion().length()>4000){
                    ge.addError("capNombreCapacitacion", Errores.ERROR_DESCRIPCION_4000);
                }
            }
            if(acu.getAcuFecha()==null){
                ge.addError("capNombreCapacitacion", Errores.ERROR_FECHA_VACIO);
            }
            if(acu.getAcuPropuestaPedagogica()==null){
                ge.addError("capNombreCapacitacion", Errores.ERROR_PROPUESTA_PEDAGOGICA_VACIO);
            }            
        
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

 
}
