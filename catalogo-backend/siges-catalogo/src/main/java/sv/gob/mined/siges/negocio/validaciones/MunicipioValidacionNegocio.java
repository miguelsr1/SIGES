/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMunicipio; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class MunicipioValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param mun SgMunicipio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMunicipio mun) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mun==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(mun.getMunCodigo())){
                ge.addError("munCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mun.getMunCodigo().length() > 4){
                ge.addError("munCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(mun.getMunNombre())){
                ge.addError("munNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mun.getMunNombre().length() > 255){
                ge.addError("munNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if(mun.getMunDepartamento()== null){
                ge.addError("munDepartamento", Errores.ERROR_DEPARTAMENTO_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
    /**
     * Compara dos elementos del tipo. 
     * Indica si los elementos contienen la misma información.
     * 
     * @param c1 SgMunicipio
     * @param c2 SgMunicipio
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMunicipio c1, SgMunicipio c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMunCodigo(), c2.getMunCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMunNombre(), c2.getMunNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMunHabilitado(), c2.getMunHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
