/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgChequera;

/**
 * Validación de las reglas de negocio de chequeras
 *
 * @author Sofis Solutions
 */
public class ChequeraValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param che SgChequera
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgChequera che) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (che == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (che.getCheCuentaBancariaFk() == null) {
                ge.addError("cheCuentaBancariaFk", Errores.ERROR_CHEQUERA_SEDE_CUENTA_VACIA);
            }
            if (che.getCheSerie() == null) {
                ge.addError("cheSerie", Errores.ERROR_CHEQUERA_SERIE_VACIA);
            }
            if (che.getCheNumeroInicial() == null) {
                ge.addError("cheNumeroInicial", Errores.ERROR_CHEQUERA_INICIAL_VACIO);
            }
            if (che.getCheNumeroFinal() == null) {
                ge.addError("cheNumeroFinal", Errores.ERROR_CHEQUERA_FINAL_VACIO);
            }
            if (che.getCheFechaChequera() == null) {
                ge.addError("cheFechaChequera", Errores.ERROR_CHEQUERA_FECHA_VACIA);
            }
            if (che.getCheNumeroInicial() != null && che.getCheNumeroFinal() != null) {
                if (che.getCheNumeroInicial() > che.getCheNumeroFinal()) {
                    ge.addError("cheRangos", Errores.ERROR_CHEQUERA_INICIAL_FINAL_INVALIDO);
                }
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
