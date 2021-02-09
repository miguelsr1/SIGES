/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelSedeRiesgoAfectacion;

/**
 *
 * @author Sofis Solutions
 */
public class RelSedeRiesgoAfectacionValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rar SgRelSedeRiesgoAfectacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelSedeRiesgoAfectacion rar) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rar==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
           
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
     * @param c1 SgRelSedeRiesgoAfectacion
     * @param c2 SgRelSedeRiesgoAfectacion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgRelSedeRiesgoAfectacion c1, SgRelSedeRiesgoAfectacion c2) {
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
