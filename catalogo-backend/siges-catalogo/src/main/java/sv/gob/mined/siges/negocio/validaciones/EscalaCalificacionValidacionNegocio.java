/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEscalaCalificacion; 
import sv.gob.mined.siges.utils.BooleanUtils;
import sv.gob.mined.siges.utils.ObjectUtils;

public class EscalaCalificacionValidacionNegocio  {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param eca SgEscalaCalificacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEscalaCalificacion eca) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (eca==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(eca.getEcaCodigo())) {
                ge.addError("ecaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (eca.getEcaCodigo().length() > 45){
                ge.addError("ecaCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(eca.getEcaNombre())) {
                ge.addError("ecaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (eca.getEcaNombre().length() > 255){
                ge.addError("ecaNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if (eca.getEcaTipoEscala()== null) {
                ge.addError("ecaTipoEscala", Errores.ERROR_TIPO_ESCALA_VACIO);
            }
//            if (eca.getEcaValorMinimo()== null) {
//                ge.addError("ecaValorMinimo", Errores.ERROR_VALOR_MINIMO_VACIO);
//            }
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
     * @param c1 SgEscalaCalificacion
     * @param c2 SgEscalaCalificacion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgEscalaCalificacion c1, SgEscalaCalificacion c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEcaCodigo(), c2.getEcaCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEcaNombre(), c2.getEcaNombre());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getEcaTipoEscala(), c2.getEcaTipoEscala());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getEcaValorMinimo(), c2.getEcaValorMinimo());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEcaHabilitado(), c2.getEcaHabilitado());     
                respuesta = respuesta && StringUtils.equals(c1.getEcaMaximo()!=null? c1.getEcaMaximo().toString(): null,c2.getEcaMaximo()!=null? c2.getEcaMaximo().toString(): null);
                respuesta = respuesta && StringUtils.equals(c1.getEcaMinimo()!=null? c1.getEcaMinimo().toString(): null,c2.getEcaMinimo()!=null? c2.getEcaMinimo().toString(): null);
                respuesta = respuesta && StringUtils.equals(c1.getEcaPrecision()!=null? c1.getEcaPrecision().toString(): null,c2.getEcaPrecision()!=null? c2.getEcaPrecision().toString(): null);
                respuesta = respuesta && StringUtils.equals(c1.getEcaMinimoAprobacion()!=null? c1.getEcaMinimoAprobacion().toString(): null,c2.getEcaMinimoAprobacion()!=null? c2.getEcaMinimoAprobacion().toString(): null);
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
