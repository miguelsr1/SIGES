/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgProyectoInstitucional; 

public class ProyectoInstitucionalValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param pin SgProyectoInstitucional
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgProyectoInstitucional pin) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (pin==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(pin.getPinCodigo())){
                ge.addError("pinCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (pin.getPinCodigo().length() > 4){
                ge.addError("pinCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(pin.getPinNombre())){
                ge.addError("pinNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (pin.getPinNombre().length() > 100){
                ge.addError("pinNombre", Errores.ERROR_LARGO_NOMBRE_100);
            }
            
             if (!StringUtils.isBlank(pin.getPinDescripcion())){
                 if(pin.getPinDescripcion().length()> 500){
                     ge.addError("smoDescripcion", Errores.ERROR_LARGO_DESCRIPCION_255);
                 }
                
            }
            if(pin.getPinFechaInicio()==null){
                ge.addError("pinFechaInicio", Errores.ERROR_FECHA_INICIO_VACIO);
            }
            if(pin.getPinFechaFin()==null){
                ge.addError("pinFechaFin", Errores.ERROR_FECHA_FIN_VACIO);
            }
            if(pin.getPinFechaInicio()!=null && pin.getPinFechaFin()!=null){
                if(pin.getPinFechaInicio().isAfter(pin.getPinFechaFin())){
                    ge.addError("pinFechaInicio", Errores.ERROR_FECHA_INICIO_MAYOR_HASTA);
                }
            }
            
            if(pin.getPinAnio()==null){
                ge.addError("pinAnio", Errores.ERROR_ANIO_VACIO);
            }
            
            if (StringUtils.isNotBlank(pin.getPinOrigenTransferencia()) && pin.getPinOrigenTransferencia().length() > 100){
                ge.addError("pinOrigenTransferencia", Errores.ERROR_LARGO_ORIGEN_TRASNFERENCIA_100);
            }
            if (StringUtils.isNotBlank(pin.getPinConvenio()) && pin.getPinConvenio().length() > 100){
                ge.addError("pinConvenio", Errores.ERROR_LARGO_CONVENIO_100);
            }
            if (StringUtils.isNotBlank(pin.getPinCondicionesEntrega()) && pin.getPinCondicionesEntrega().length() > 100){
                ge.addError("pinCondicionesEntrega", Errores.ERROR_LARGO_CONDICIONES_ENTREGA_100);
            }
            if (pin.getPinMonto() != null && pin.getPinMonto().compareTo(BigDecimal.ZERO)<=0){
                ge.addError("pinMonto", Errores.ERROR_MONTO_MENOR_IGUAL_CERO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
       
}
