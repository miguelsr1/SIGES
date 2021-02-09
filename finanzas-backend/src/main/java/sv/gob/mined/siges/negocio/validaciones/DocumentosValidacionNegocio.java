/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDocumentos;

/**
 * Validación de las reglas de negocio de los documentos
 *
 * @author Sofis Solutions
 */
public class DocumentosValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param doc SgDocumentos
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDocumentos doc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (doc == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (doc.getDocArchivoFk() == null) {
                ge.addError("docCodigo", Errores.ERROR_CODIGO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
