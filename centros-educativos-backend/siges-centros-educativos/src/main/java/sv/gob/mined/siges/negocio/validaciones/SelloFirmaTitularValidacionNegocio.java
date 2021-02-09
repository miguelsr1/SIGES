/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSelloFirmaTitular;

/**
 *
 * @author Sofis Solutions
 */
public class SelloFirmaTitularValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param sft SgSelloFirmaTitular
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSelloFirmaTitular sft) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (sft==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
        
             if(StringUtils.isBlank(sft.getSftPrimerNombre())){
                 ge.addError("sftSelloPrimerNombre", Errores.ERROR_PRIMER_NOMBRE_VACIO);
             }
             if(StringUtils.isBlank(sft.getSftPrimerApellido())){
                 ge.addError("sftPrimerApellido", Errores.ERROR_PRIMER_APELLIDO_VACIO);
             }
             if(sft.getSftFechaDesde()==null){
                 ge.addError("sftFechaDesde", Errores.ERROR_FECHA_DESDE_VACIO);
             }
             if(sft.getSftFechaHasta()==null){
                 ge.addError("sftFechaHasta", Errores.ERROR_FECHA_HASTA_VACIO);
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
     * @param c1 SgSelloFirmaTitular
     * @param c2 SgSelloFirmaTitular
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgSelloFirmaTitular c1, SgSelloFirmaTitular c2) {
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
