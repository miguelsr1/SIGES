/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEdificio;

/**
 *
 * @author Sofis Solutions
 */
public class EdificioValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param edi SgEdificio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEdificio edi) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (edi==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(edi.getEdiCodigo())) {
                ge.addError("ediCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (edi.getEdiCodigo().length() > 10){
                ge.addError("ediCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(edi.getEdiNombre())) {
                ge.addError("ediNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (edi.getEdiNombre().length() > 255){
                ge.addError("ediNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if(edi.getEdiTipoConstruccion()==null){
                ge.addError("ediTipoConstruccion", Errores.ERROR_TIPO_CONSTRUCCION_VACIO);
            }
            if(edi.getEdiInmuebleSedeFk()==null){
                ge.addError("ediInmuebleSedeFk", Errores.ERROR_INMUEBLE_VACIO);
            }

            if(edi.getEdiCantidadNiveles()!=null && edi.getEdiCantidadNiveles()>50){
                ge.addError("ediInmuebleSedeFk", Errores.ERROR_CANTIDAD_NIVELES_50);
            }

            if(edi.getEdiArea()!=null && edi.getEdiInmuebleSedeFk() != null){
                if(edi.getEdiInmuebleSedeFk().getTisAreaTotal()!=null){
                    if(edi.getEdiArea().compareTo(edi.getEdiInmuebleSedeFk().getTisAreaTotal())>0){
                        ge.addError("ediArea", Errores.ERROR_AREA_MAYOR_AREA_INMUEBLE);
                    }
                }
                
            }
            
            if (edi.getEdiInmuebleSedeFk() != null && (edi.getEdiInmuebleSedeFk().getTisSedeFk() != null || edi.getEdiInmuebleSedeFk().getTisAfUnidadAdministrativa() != null) && (BooleanUtils.isTrue(edi.getEdiInmuebleSedeFk().getTisPropietario()))) {
                if(edi.getEdiInversion()==null){
                    ge.addError("ediArea", Errores.ERROR_INVERSION_VACIO);
                }
            } 
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

   
}
