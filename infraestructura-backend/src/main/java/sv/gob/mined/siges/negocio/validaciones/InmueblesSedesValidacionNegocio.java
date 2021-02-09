/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedes;

public class InmueblesSedesValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgInmueblesSedes
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgInmueblesSedes entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if(BooleanUtils.isTrue(entity.getTisPerteneceSede())){
                if (entity.getTisSedeFk() == null) {
                    ge.addError("tisSedeFk", Errores.ERROR_SEDE_VACIO);
                }
            }
            if(BooleanUtils.isFalse(entity.getTisPerteneceSede())){
                if(entity.getTisAfUnidadAdministrativa()==null){
                    ge.addError("tisSedeFk", Errores.ERROR_UNIDAD_ADMINISTRATIVO_VACIO);
                }                
            }
            if (entity.getTisPropietario() == null) {
                ge.addError("tisPropietario", Errores.ERROR_PROPIETARIO_VACIO);
            }else{
                if(BooleanUtils.isTrue(entity.getTisPropietario()) && entity.getTisFechaAdquisicion()==null){
                    ge.addError("tisSedeFk", Errores.ERROR_FECHA_ADQUISICION_VACIO);
                }
            }
            if (entity.getTisActivoFijo() == null) {
                ge.addError("tisPropietario", Errores.ERROR_ACTIVO_FIJO_VACIO);
            }
            if (entity.getTisDireccion() != null) {
                DireccionValidacionNegocio.validar(entity.getTisDireccion());
            } else {
                ge.addError("tisDireccion", Errores.ERROR_DIRECCION_VACIA);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
