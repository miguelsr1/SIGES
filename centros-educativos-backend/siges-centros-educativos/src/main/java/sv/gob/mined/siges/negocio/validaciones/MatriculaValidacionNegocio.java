/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.dto.SgPromRepMatriculas;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;

public class MatriculaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos al crear
     *
     * @param entity SgMatricula
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validarCrear(SgMatricula entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getMatEstudiante() == null) {
                ge.addError("matEstudiante", Errores.ERROR_ESTUDIANTE_VACIO);
            }
            if (entity.getMatSeccion() == null) {
                ge.addError("matSeccion", Errores.ERROR_SECCION_VACIO);
            }
            if (entity.getMatFechaIngreso() == null) {
                ge.addError("matFechaIngreso", Errores.ERROR_FECHA_INGRESO_VACIO);
            }
            if (entity.getMatEstudiante().getEstPersona().getPerResponsable() == null && !entity.getMatEstudiante().getEstPersona().getPerEsMayorDeEdad()) {
                ge.addError(Errores.ERROR_RESPONSABLE_VACIO);
            }
            if (!StringUtils.isBlank(entity.getMatObservacioneProvisional())) {
                if (entity.getMatObservacioneProvisional().length() > 255) {
                    ge.addError("matObservaciones", Errores.ERROR_LARGO_OBSERVACIONES_255);
                }
            }
            if (BooleanUtils.isTrue(entity.getMatProvisional())) {
                if (StringUtils.isBlank(entity.getMatObservacioneProvisional())) {
                    ge.addError("matObservaciones", Errores.ERROR_OBSERVACIONES_VACIO);
                }
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Valida que los datos del elemento sean correctos al crear promociones de
     * matriculas
     *
     * @param entity SgMatricula
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validarPromociones(SgPromRepMatriculas promMat) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();

        if ((promMat.getMatriculasPromovidas() == null || promMat.getMatriculasPromovidas().isEmpty())
                && (promMat.getMatriculasRepetidoras() == null || promMat.getMatriculasRepetidoras().isEmpty())) {
            ge.addError(Errores.ERROR_DATO_VACIO);
            throw ge;
        }

        if (promMat.getNuevaSeccion() == null) {
            ge.addError("matSeccion", Errores.ERROR_SECCION_VACIO);
            throw ge;
        }

        if (promMat.getFechaIngreso() == null) {
            ge.addError("matFechaIngreso", Errores.ERROR_FECHA_INGRESO_VACIO);
            throw ge;
        }

        for (SgMatricula entity : promMat.getMatriculasPromovidas()) {
            if (entity.getMatEstudiante() == null) {
                ge.addError("matEstudiante", Errores.ERROR_ESTUDIANTE_VACIO);
                throw ge;
            }
        }

        for (SgMatricula entity : promMat.getMatriculasRepetidoras()) {
            if (entity.getMatEstudiante() == null) {
                ge.addError("matEstudiante", Errores.ERROR_ESTUDIANTE_VACIO);
                throw ge;
            }
        }

        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Valida que los datos del elemento sean correctos al crear
     *
     * @param entity SgMatricula
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validarActualizar(SgMatricula entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if (entity.getMatPk() == null){
                ge.addError("matPk", Errores.ERROR_MATRICULA_VACIO);
            }
            
            if (entity.getMatEstudiante() == null) {
                ge.addError("matEstudiante", Errores.ERROR_ESTUDIANTE_VACIO);
            }
            if (entity.getMatSeccion() == null) {
                ge.addError("matSeccion", Errores.ERROR_SECCION_VACIO);
            }
            if (!StringUtils.isBlank(entity.getMatObservacioneProvisional())) {
                if (entity.getMatObservacioneProvisional().length() > 500) {
                    ge.addError("matObservaciones", Errores.ERROR_LARGO_OBSERVACIONES_500);
                }
            }
            if (BooleanUtils.isTrue(entity.getMatProvisional())) {
                if (StringUtils.isBlank(entity.getMatObservacioneProvisional())) {
                    ge.addError("matObservaciones", Errores.ERROR_OBSERVACIONES_VACIO);
                }
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    public static boolean validarRetiro(SgMatricula entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (entity.getMatMotivoRetiro() == null) {
                ge.addError("matMotivoRetiro", Errores.ERROR_MOTIVO_RETIRO_VACIO);
            }

            if (entity.getMatFechaHasta() == null) {
                ge.addError("matFechaHasta", Errores.ERROR_FECHA_HASTA_VACIO);
            }

            if ((entity.getMatMotivoRetiro() != null) && (entity.getMatFechaHasta() != null)) {
                if (entity.getMatFechaHasta().isBefore(entity.getMatFechaIngreso())) {
                    ge.addError("matFechaHasta", Errores.ERROR_FECHA_RETIRO_MENOR_INGRESO);
                }
            }

            if (StringUtils.isBlank(entity.getMatObservaciones())) {
                ge.addError("matObservaciones", Errores.ERROR_OBSERVACIONES_VACIO);
            } else if (entity.getMatObservaciones().length() > 500) {
                ge.addError("matObservaciones", Errores.ERROR_LARGO_OBSERVACIONES_500);
            }

            if (EnumMatriculaEstado.CERRADO.equals(entity.getMatEstado())) {
                ge.addError("matEstado", Errores.ERROR_MATRICULA_YA_CERRADA);
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    public static boolean validarAnularRetiro(SgMatricula entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (StringUtils.isBlank(entity.getMatObsAnuRetiro())) {
                ge.addError("matObsAnuRetiro", Errores.ERROR_OBSERVACIONES_VACIO);
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Valida que la fecha Hasta sea mayor o igual a la fecha desde
     *
     * @param desde
     * @param hasta
     * @return
     */
    public static Boolean validarHastaDesde(LocalDate desde, LocalDate hasta) {
        return (hasta.isAfter(desde) || hasta.isEqual(desde));
    }

}
