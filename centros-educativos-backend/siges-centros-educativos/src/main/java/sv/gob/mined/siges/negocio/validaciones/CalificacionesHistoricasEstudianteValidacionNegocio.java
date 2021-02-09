/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionesHistoricasEstudiante;

/**
 *
 * @author Sofis Solutions
 */
public class CalificacionesHistoricasEstudianteValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param che SgCalificacionesHistoricasEstudiante
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCalificacionesHistoricasEstudiante che) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (che == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (!StringUtils.isBlank(che.getCheSedeExternaCodigo()) && StringUtils.isBlank(che.getCheSedeExternaNombre())) {
                ge.addError("capNombreCapacitacion", Errores.ERROR_SEDE_NOMBRE_VACIO);
            } else {
                if (StringUtils.isBlank(che.getCheSedeExternaCodigo()) && !StringUtils.isBlank(che.getCheSedeExternaNombre())) {
                    ge.addError("capNombreCapacitacion", Errores.ERROR_CODIGO_SEDE_VACIO);
                } else {
                    if (StringUtils.isBlank(che.getCheSedeExternaCodigo())
                            && StringUtils.isBlank(che.getCheSedeExternaNombre())
                            && che.getCheSedeFk() == null) {
                        ge.addError("capNombreCapacitacion", Errores.ERROR_SEDE_VACIO);
                    }
                }
            }

            if (che.getCheAnioMatricula() == null) {
                ge.addError("capNombreCapacitacion", Errores.ERROR_ANIO_VACIO);
            }
            if (StringUtils.isBlank(che.getCheComponente())) {
                ge.addError("capNombreCapacitacion", Errores.ERROR_COMPONENTE_VACIO);
            }
            if (StringUtils.isBlank(che.getChePlanEstudioExternoDescripcion())) {
                ge.addError("capNombreCapacitacion", Errores.ERROR_PLAN_VACIO);
            }
            if (StringUtils.isBlank(che.getCheServicioEducativoExternoEtiquetaImpresion())) {
                ge.addError("capNombreCapacitacion", Errores.ERROR_SERVICIO_VACIO);
            }
            if(StringUtils.isBlank(che.getCheNF())){
                ge.addError("capNombreCapacitacion", Errores.ERROR_NF_VACIO);
            }
         

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
