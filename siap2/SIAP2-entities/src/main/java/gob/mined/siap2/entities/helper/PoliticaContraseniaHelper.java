/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.helper;

import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.PoliticaContrasenia;

/**
 *
 * @author sofis
 */
public class PoliticaContraseniaHelper {
    
    public static PoliticaContrasenia obtenerPoliticaContraseniaPorDefecto() {
        PoliticaContrasenia pol = new PoliticaContrasenia();
        pol.setCambiaPasswordDespuesOlvido(ConstantesEstandares.DEFAULT_PWD_CAMBIO_PASSWORD_DESPUES_OLVIDO);
        pol.setCambiaPasswordPrimeraVez(ConstantesEstandares.DEFAULT_PWD_CAMBIO_PASSWORD_PRIMERA_VEZ);
        pol.setCantidadIntentos(ConstantesEstandares.DEFAULT_PWD_CANTIDAD_INTENTOS_FALLIDOS);
        pol.setCantidadMinimaCaracteresEspeciales(ConstantesEstandares.DEFAULT_PWD_CANTIDAD_MIN_CARACTERES_ESPECIALES);
        pol.setCantidadMinimaCaracteresMayuscula(ConstantesEstandares.DEFAULT_PWD_CANTIDAD_MIN_MAYUSCULAS);
        pol.setCantidadMinimaCaracteresMinusculas(ConstantesEstandares.DEFAULT_PWD_CANTIDAD_MIN_MINUSCULAS);
        pol.setCantidadMinimaDigitos(ConstantesEstandares.DEFAULT_PWD_CANTIDAD_MIN_DIGITOS);
        pol.setDiasCaducidad(ConstantesEstandares.DEFAULT_PWD_DIAS_CADUCIDAD);
        pol.setId(1);
        pol.setLargoMinimo(ConstantesEstandares.DEFAULT_PWD_LARGO_MININO);
        pol.setPermiteUsuCodEnPassword(ConstantesEstandares.DEFAULT_PWD_PERMITE_USU_COD_EN_PASSWORD);
        return pol;

    }
    
}
