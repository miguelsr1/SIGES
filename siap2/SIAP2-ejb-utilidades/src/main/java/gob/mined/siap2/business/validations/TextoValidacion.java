/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.validations;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.Texto;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.utils.generalutils.BooleanUtils;
import gob.mined.siap2.utils.generalutils.StringsUtils;

/**
 *
 * @author Sofis Solutions
 */
public class TextoValidacion {

    public static boolean validar(Texto tho) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tho == null) {
            ge.addError(ConstantesErrores.ERROR_DATO_VACIO);
        } else {
            String aux = StringsUtils.normalizarString(tho.getTexCodigo());
            tho.setTexCodigo(aux);
            if (StringsUtils.isEmpty(tho.getTexCodigo())) {
                ge.addError(ConstantesErrores.ERROR_CODIGO_VACIO);
            }

            aux = StringsUtils.normalizarString(tho.getTexValor());
            tho.setTexValor(aux);
            if (StringsUtils.isEmpty(tho.getTexValor())) {
                ge.addError(ConstantesErrores.ERROR_DESCRIPCION_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    public static boolean compararParaGrabar(Texto c1, Texto c2) {
        boolean respuesta = true;
        if (c1 != null) {
            if (c2 != null) {
                respuesta = StringsUtils.sonStringIguales(c1.getTexCodigo(), c2.getTexCodigo());
                respuesta = respuesta && StringsUtils.sonStringIguales(c1.getTexValor(), c2.getTexValor());
                respuesta = respuesta && BooleanUtils.sonBooleanIguales(c1.getTexHabilitado(), c2.getTexHabilitado());

            }
        } else {
            respuesta = c2 == null;
        }
        return respuesta;
    }
}
