/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsProveedor;

/**
 * Validación de las reglas de negocio del proveedor
 *
 * @author jgiron
 */
public class ProveedorValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param pro SsProveedor
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SsProveedor pro) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (pro == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(pro.getProNombre())) {
                ge.addError("proNombre", Errores.ERROR_PROVEEDOR_NOMBRE_VACIO);
            }
            if (StringUtils.isBlank(pro.getProNit())) {
                ge.addError("proNit", Errores.ERROR_PROVEEDOR_NIT_VACIO);
            }
            if (StringUtils.isBlank(pro.getProDireccionFactura())) {
                ge.addError("proDireccionFactura", Errores.ERROR_PROVEEDOR_DIRECCION_VACIO);
            }
            if (StringUtils.isBlank(pro.getProTelefonoRepresentante())) {
                ge.addError("proTelefonoRepresentante", Errores.ERROR_PROVEEDOR_TELEFONO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
