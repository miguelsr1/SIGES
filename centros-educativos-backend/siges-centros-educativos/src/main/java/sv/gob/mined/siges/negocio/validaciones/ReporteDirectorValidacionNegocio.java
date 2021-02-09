/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgReporteDirector;

/**
 *
 * @author Sofis Solutions
 */
public class ReporteDirectorValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rdi SgReporteDirector
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgReporteDirector rdi) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rdi==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if (rdi.getRdiSede() == null){
                ge.addError("rdiSede", Errores.ERROR_SEDE_VACIO);
            }
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Compara dos elementos del tipo.
     * Indica si los elementos contienen la misma información.
     *
     * @param c1 SgReporteDirector
     * @param c2 SgReporteDirector
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgReporteDirector c1, SgReporteDirector c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
             
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
