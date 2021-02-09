/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSelloFirma;

/**
 *
 * @author Sofis Solutions
 */
public class SelloFirmaValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param sfi SgSelloFirma
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSelloFirma sfi) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (sfi==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if(sfi.getSfiSede()==null){
                ge.addError("sfiSede", Errores.ERROR_SEDE_VACIO);
            }
            if(sfi.getSfiPersona()==null){
                ge.addError("sfiPersona", Errores.ERROR_PERSONAL_VACIO);
            }
             if(sfi.getSfiTipoRepresentante()==null){
                 ge.addError("sfiTipoRepresentante", Errores.ERROR_TIPO_REPRESENTANTE_VACIO);
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
     * @param c1 SgSelloFirma
     * @param c2 SgSelloFirma
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgSelloFirma c1, SgSelloFirma c2) {
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
