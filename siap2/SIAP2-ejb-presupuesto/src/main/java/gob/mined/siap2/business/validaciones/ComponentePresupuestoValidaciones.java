/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.validaciones;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.ComponentePresupuestoEscolar;
import gob.mined.siap2.exceptions.BusinessException;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Sofis Solutions
 */
public class ComponentePresupuestoValidaciones implements Serializable {
    public static boolean validar(ComponentePresupuestoEscolar  p) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (p == null) {
            ge.addError(ConstantesErrores.ERROR_DATO_VACIO);
        } else {
            if(p.getCategoriaPresupuestoEscolar() == null) {
                ge.addError(ConstantesErrores.ERR_COMPONENTE_VACIO);
            }
            
            if(StringUtils.isBlank(p.getNombre())) {
               ge.addError(ConstantesErrores.ERR_NOMBRE_VACIO); 
            } else if(p.getNombre().trim().length() > 255) {
                ge.addError(ConstantesErrores.ERR_NOMBRE_LARGO_100); 
            }
            
            if(p.getTipo() == null) {
                ge.addError(ConstantesErrores.ERR_TIPO_CALCULO_VACIO);
            }
                   
            if (p.getUnidadTecnica()== null) {
                ge.addError(ConstantesErrores.ERR_UNIDAD_TECNICA_VACIA);
            }
            if (p.getUsuario() == null) {
                ge.addError(ConstantesErrores.ERR_USUARIO_VACIO);
            }
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
