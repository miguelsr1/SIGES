/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfLoteBienes;

public class LoteBienesValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgAfLoteBienes
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfLoteBienes entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getLbiCodigoInventarioPadre() == null) {
               ge.addError("lbiCodigoInventarioPadre", Errores.ERROR_CODIGO_INVENTARIO_VACIO);
            }
            
            if(entity.getLbiSede() == null && entity.getLbiUnidadesAdministrativas() == null) {
                 ge.addError("lbiUnidadesAdministrativa", Errores.ERROR_UNIDAD_ADMINISTRATIVA_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}