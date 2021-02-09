/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgComponenteDocente;

/**
 *
 * @author Sofis Solutions
 */
public class ComponenteDocenteValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cdo SgComponenteDocente
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgComponenteDocente cdo) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cdo == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (cdo.getCdoHorarioEscolar() == null) {
                ge.addError("cdoHorarioEscolar", Errores.ERROR_HORARIO_ESCOLAR_VACIO);
            }
            if (cdo.getCdoComponente() == null) {
                ge.addError("cdoComponente", Errores.ERROR_COMPONENTE_PLAN_ESTUDIO_VACIO);
            }
            if (cdo.getCdoDocente() == null) {
                ge.addError("cdoDocente", Errores.ERROR_DOCENTE_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
