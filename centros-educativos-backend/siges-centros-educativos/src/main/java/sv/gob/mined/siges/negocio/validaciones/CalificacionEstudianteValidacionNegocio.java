/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionEstudiante;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEscalaCalificacion;

/**
 *
 * @author Sofis Solutions
 */
public class CalificacionEstudianteValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cae SgCalificacionEstudiante
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCalificacionEstudiante cae, SgEscalaCalificacion eca) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cae == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (!StringUtils.isBlank(cae.getCaeCalificacionNumericaEstudiante())) {
                if (Double.valueOf(cae.getCaeCalificacionNumericaEstudiante()) < 0) {
                    ge.addError("caeCalificacionNumericaEstudiante", Errores.ERROR_CALIFICACION_NEGATIVO);
                }

            }
            if (cae.getCaeEstudiante() == null || cae.getCaeEstudiante().getEstPk() == null) {
                ge.addError("caeEstudiante", Errores.ERROR_ESTUDIANTE_VACIO);
            }
            if (eca != null) {
                if (eca.getEcaTipoEscala().equals(TipoEscalaCalificacion.NUMERICA)) {
                    if (!StringUtils.isBlank(cae.getCaeCalificacionNumericaEstudiante())) {
                        Double nota = Double.parseDouble(cae.getCaeCalificacionNumericaEstudiante());
                        if (nota > eca.getEcaMaximo()) {
                            ge.addError("caeCalificacionNumericaEstudiante", Errores.ERROR_CALIFICACION_NO_PERTENECE_ESCALA);
                        }
                    }
                }
            }
            if(EnumTiposPeriodosCalificaciones.APR.equals(cae.getCaeCalificacion().getCalTipoPeriodoCalificacion())){
                if(cae.getCaeResolucion()==null){
                    ge.addError("calificacion", Errores.ERROR_CALIFICACION_RESOLUCION_VACIO);
                }
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Compara dos elementos del tipo. Indica si los elementos contienen la
     * misma información.
     *
     * @param c1 SgCalificacionEstudiante
     * @param c2 SgCalificacionEstudiante
     * @return Boolean
     */
}
