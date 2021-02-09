/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.validations;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.UnidadTecnicaDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.utils.generalutils.StringsUtils;
import java.util.Objects;
import javax.inject.Inject;

/**
 *
 * @author Sofis Solutions
 */
public class UnidadTecnicaValidacion {    
    
    @Inject
    @JPADAO
    private UnidadTecnicaDAO utDAO;

    public boolean validar(UnidadTecnica ut) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ut == null) {
            ge.addError(ConstantesErrores.ERROR_DATO_VACIO);
        } else {
            if (StringsUtils.isEmpty(ut.getCodigo())) {
                ge.addError(ConstantesErrores.ERR_GEST_UNIDAD_TECNICA_CODIGO_VACIO);
            }
            if (StringsUtils.isEmpty(ut.getNombre())) {
                ge.addError(ConstantesErrores.ERR_GEST_UNIDAD_TECNICA_NOMBRE_VACIO);
            }
            if (ut.getPadre() == null) {
                ge.addError(ConstantesErrores.ERR_GEST_UNIDAD_TECNICA_PADRE_VACIO);
            }
            if (ut.getUniUsuario() == null) {
                ge.addError(ConstantesErrores.ERR_GEST_UNIDAD_RESPONSABLE_VACIO);
            }
            if (ut.getDireccion() == null) {
                ge.addError(ConstantesErrores.ERR_GEST_UNIDAD_DIRECCION_VACIA);
            } 
            Integer idUt = ut.getId()!=null?ut.getId():0;
            if(utDAO.existeUTByCodigo(idUt, ut.getCodigo())){
                ge.addError(ConstantesErrores.ERR_CODIGO_UT_YA_EXISTE);
            }
            if(ut.getCodigo()!=null && ut.getCodigo().length()>20){
                ge.addError(ConstantesErrores.ERR_CODIGO_UT_DEMASIADO_LARGO);
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Este método devuelve true si los dos elementos son iguales como para
     * grabar
     *
     * @param c1
     * @param c2
     * @return
     */
    public static boolean compararParaGrabar(UnidadTecnica c1, UnidadTecnica c2) {
        boolean respuesta = true;
        if (c1 != null) {
            if (c2 != null) {
                respuesta = StringsUtils.sonStringIguales(c1.getCodigo(), c2.getCodigo());
                respuesta = respuesta && StringsUtils.sonStringIguales(c1.getNombre(), c2.getNombre());
                respuesta = respuesta && Objects.equals(c1.getPadre(),c2.getPadre());
                respuesta = respuesta && Objects.equals(c1.getDireccion(),c2.getDireccion());
                respuesta = respuesta && Objects.equals(c1.getUniUsuario(),c2.getUniUsuario());
            }
        } else {
            respuesta = c2 == null;
        }

        return respuesta;
    }

}
