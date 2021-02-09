/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumPresupuestoEscolarEstado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgPresupuestoEscolar;

/**
 * Validación de las reglas de negocio del presupuesto escolar
 *
 * @author jgiron
 */
public class PresupuestoEscolarValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param pres SgPresupuestoEscolar
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPresupuestoEscolar pres) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (pres == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (pres.getPesSedeFk() == null) {
                ge.addError("pesSedeFk", Errores.ERROR_SEDE_VACIA);
            }
            if (pres.getPesAnioFiscalFk() == null) {
                ge.addError("pesAnioFiscalFk", Errores.ERROR_ANIO_FISCAL_VACIO);
            }
            if (StringUtils.isBlank(pres.getPesCodigo())) {
                ge.addError("pesCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (pres.getPesCodigo().length() > 45) {
                ge.addError("pesCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(pres.getPesNombre())) {
                ge.addError("pesNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (pres.getPesNombre().length() > 255) {
                ge.addError("pesNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if (pres.getPesDescripcion() != null && pres.getPesDescripcion().length() > 500) {
                ge.addError("pesDescripcion", Errores.ERROR_LARGO_DESCRIPCION_500);
            }
            if (pres.getPesEstado().equals(EnumPresupuestoEscolarEstado.OBSERVADO) && pres.getPesObservacion() == null) {
                ge.addError(Errores.ERROR_PRESUPUESTO_OBSERVACION_VACIA);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
