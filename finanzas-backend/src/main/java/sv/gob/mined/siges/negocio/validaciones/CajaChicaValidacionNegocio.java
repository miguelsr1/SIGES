/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCajaChica;

/**
 * Validación de las reglas de negocio delas cuentas bancarias de los centros
 * educativos
 *
 * @author Sofis Solutions
 */
public class CajaChicaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param bcc SgCajaChica
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCajaChica bcc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (bcc == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(bcc.getBccNumeroCuenta())) {
                ge.addError("bccNumeroCuenta", Errores.ERROR_NUMERO_CUENTA_VACIO);
            }
            if (StringUtils.isBlank(bcc.getBccTitular())) {
                ge.addError("bccTitular", Errores.ERROR_TITULAR_VACIO);
            } else if (bcc.getBccTitular().length() > 250) {
                ge.addError("bccTitular", Errores.ERROR_TITULAR_250);
            }
            
            if (bcc.getBccComentario()!=null && bcc.getBccComentario().length() > 4000) {
                ge.addError("bccComentario", Errores.ERROR_CUENTA_COMENTARIO_4000);
            }
            
            if (bcc.getBccSedeFk() == null) {
                ge.addError("bccSede", Errores.ERROR_SEDE_VACIO);
            }
            
            if (bcc.getBccAnioFk() == null) {
                ge.addError("bccAnioFk", Errores.ERROR_ANIO_VACIO);
            }
            if(bcc.getBccOtroIngreso().equals(Boolean.FALSE)){
                if (bcc.getBccSubcomponenteFk() == null) {
                    ge.addError("bccSubcomponenteFk", Errores.ERROR_SUBCOMPONENTE_VACIA);
                }
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
