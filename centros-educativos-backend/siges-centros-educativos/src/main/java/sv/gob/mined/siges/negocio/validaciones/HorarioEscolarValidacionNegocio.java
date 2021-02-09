/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgHorarioEscolar;

/**
 *
 * @author Sofis Solutions
 */
public class HorarioEscolarValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param hes SgHorarioEscolar
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgHorarioEscolar hes) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (hes==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
           
            if(hes.getHesPk()!=null && hes.getHesUnicoDocente()){
                if(hes.getHesDocente()==null){
                    ge.addError("hesDocente", Errores.ERROR_DOCENTE_VACIO);
                }
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
     * @param c1 SgHorarioEscolar
     * @param c2 SgHorarioEscolar
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgHorarioEscolar c1, SgHorarioEscolar c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
             /*   respuesta = StringUtils.equals(c1.getHesCodigo(), c2.getHesCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getHesNombre(), c2.getHesNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getHesHabilitado(), c2.getHesHabilitado());*/
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
