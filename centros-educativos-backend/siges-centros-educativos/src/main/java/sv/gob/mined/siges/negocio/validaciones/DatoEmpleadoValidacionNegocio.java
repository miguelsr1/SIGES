/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDatoEmpleado;

public class DatoEmpleadoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgDatoEmpleado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDatoEmpleado entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            switch (entity.getDemTipoDatoGuardar()) {
                case ESCALAFON:
                    if (entity.getDemEstado() == null) {
                        ge.addError("demEstado", Errores.ERROR_ESTADO_VACIO);
                    }

                    if (entity.getDemNivelFk() == null) {
                        ge.addError("demNivel", Errores.ERROR_NIVEL_VACIO);
                    }

                    if (entity.getDemCategoriaFk() == null) {
                        ge.addError("demCategoria", Errores.ERROR_CATEGORIA_VACIO);
                    }

                    if (entity.getDemFechaRegistro() == null) {
                        ge.addError("demFechaRegistro", Errores.ERROR_FECHA_REGISTRO_VACIO);
                    }
                    break;
                case DATOS_GENERALES:
                    if (BooleanUtils.isTrue(entity.getDemEmpleadoMineducyt())) {
                        if (StringUtils.isBlank(entity.getDemCodigoEmpleado())) {
                            ge.addError("demCodigoEmpleado", Errores.ERROR_CODIGO_VACIO);
                        } else if (entity.getDemCodigoEmpleado().length() > 45) {
                            ge.addError("demCodigoEmpleado", Errores.ERROR_LARGO_CODIGO_45);
                        }
                        if (entity.getDemAfp() == null) {
                            ge.addError("demAfp", Errores.ERROR_AFP_VACIA);
                        }
                        if (entity.getDemFechaIngresoGob() != null) {
                            if (entity.getDemFechaIngresoGob().isAfter(LocalDate.now())) {
                                ge.addError("demFechaIngresoGob", Errores.ERROR_FECHA_INGRESO_GOB_MAYOR);
                            }
                        }

                        if (entity.getDemFechaIngresoMined() != null) {
                            if (entity.getDemFechaIngresoMined().isAfter(LocalDate.now())) {
                                ge.addError("demFechaIngresoMined", Errores.ERROR_FECHA_INGRESO_MINED_MAYOR);
                            }
                        }
                    }
                    break;
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
