/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAlcanceExtraccion;
import sv.gob.mined.siges.persistencia.entidades.SgExtraccion;

/**
 *
 * @author Sofis Solutions
 */
public class ExtraccionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ese SgEstadisticaEstudiante
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgExtraccion ese) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ese == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (ese.getExtNombre() == null) {
                ge.addError("extNombre", Errores.ERROR_NOMBRE_EXTRACCION_VACIO);
            }

            if (ese.getExtDataset() == null) {
                ge.addError("extDataset", Errores.ERROR_DATASET_VACIO);
            }

            if (ese.getExtAnio() == null) {
                ge.addError("extAnio", Errores.ERROR_ANIO_VACIO);
            }

            if (ese.getExtDescripcion() != null && ese.getExtDescripcion().length() > 2000) {
                ge.addError("extDescripcion", Errores.ERROR_LARGO_DESCRIPCION_2000);
            }

            // se valida si es para estudiantes
            if (ese.getExtDataset() != null && ese.getExtDataset().getDatNombre().equalsIgnoreCase("estudiantes")) {

                if (ese.getExtAlcance() == null || ese.getExtAlcance().isEmpty()) {
                    ge.addError(Errores.ERROR_ALCANCE_EXTRACCION_VACIO);
                } else {

                    for (SgAlcanceExtraccion alc : ese.getExtAlcance()) {
                        if (alc.getAlcFechaMatriculas() == null) {
                            ge.addError(Errores.ERROR_ALCANCE_EXTRACCION_SIN_FECHA_MATRICULA);
                            break;
                        }

                    }

                }

            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
