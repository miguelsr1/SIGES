/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCirculoAlfabetizacion;


public class CirculoAlfabetizacionValidacionNegocio  {
     
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgCentroEducativoOficial
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCirculoAlfabetizacion entity, Long sedePadreAlfabetizacionPk) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {      
            try {
                SedesValidacionNegocio.validar(entity);
            } catch (BusinessException be){
                ge.getErrores().addAll(be.getErrores());
            }
            
            // Validaciones de SgCirculoAlfabetizacion
            
            if (entity.getSedImplementadora() == null){
                 ge.addError("sedImplementadora", Errores.ERROR_IMPLEMENTADORA_VACIO);
            }
            
            if (!sedePadreAlfabetizacionPk.equals(entity.getSedPk()) && BooleanUtils.isNotTrue(entity.getSedEsAdscriptaAOtraSede()) && entity.getSedSedeAdscritaDe() == null){
                ge.addError("sedSedeAdscritaDe", Errores.ERROR_SEDE_ADSCRITA_VACIO);
            }
            
            if (entity.getSedDireccion().getDirDepartamento() != null &&
                    Constantes.DEPARTAMENTO_SIN_DATO_PK.equals(entity.getSedDireccion().getDirDepartamento().getDepPk())){
                ge.addError("dirDepartamento", Errores.ERROR_DEPARTAMENTO_VACIO);
            }
            
            if (entity.getSedDireccion().getDirMunicipio()!= null &&
                    Constantes.MUNICIPIO_SIN_DATO_PK.equals(entity.getSedDireccion().getDirMunicipio().getMunPk())){
                ge.addError("dirMunicipio", Errores.ERROR_MUNICIPIO_VACIO);
            }
                
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }    
}
