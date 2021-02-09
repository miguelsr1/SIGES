/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumTipoArchivoCarga;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCargaArchivo;

/**
 *
 * @author Sofis Solutions
 */
public class CargaArchivoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cga SgCargaArchivo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCargaArchivo cga) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cga==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(cga.getCgaCodigo())) {
                ge.addError("cgaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (cga.getCgaCodigo().length() > 45){
                ge.addError("cgaCodigo",Errores.ERROR_LARGO_CODIGO_45);
            }
            
            if(cga.getCgaTipoArchivo()==null){
                ge.addError("cgaTipoArchivo",Errores.ERROR_TIPO_ARCHIVO_VACIO);
            }else{
                if(!cga.getCgaTipoArchivo().equals(EnumTipoArchivoCarga.DOCUMENTOS)){
                    if(cga.getCgaAncho()==null){
                        ge.addError("cgaAncho",Errores.ERROR_ANCHO_VACIO);
                    }

                    if(cga.getCgaAlto()==null){
                        ge.addError("cgaAlto",Errores.ERROR_ALTO_VACIO);
                    }
                }
            }
            
            if (StringUtils.isBlank(cga.getCgaNombre())) {
                ge.addError("cgaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (cga.getCgaNombre().length() > 255){
                ge.addError("cgaNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if(StringUtils.isBlank(cga.getCgaFormatosPermitidos())){
                ge.addError("cgaFormatosPermitidos",Errores.ERROR_FORMATO_VACIO);
            }
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
