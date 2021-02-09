/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.validaciones;

import gob.mined.siap2.entities.data.impl.Reprogramacion;
import gob.mined.siap2.entities.data.impl.ReprogramacionDetalle;
import gob.mined.siap2.exceptions.BusinessException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class ValidacionReprogramacion implements Serializable {

    public static boolean validar(Reprogramacion  rep) throws BusinessException {
        List<String> errores = new ArrayList();
        boolean valida = true;
        if (rep != null) {

             for(ReprogramacionDetalle det: rep.getRep_detalle_lista()) {
                 ValidacionReprogramacionDetalle.validar(det, rep.getEstado());
                 if (!BooleanUtils.isTrue(det.getInsumoNuevoNoUaci())) {
                     if (det.getGrupoPAC()==null) {
                         errores.add("ERROR_INSUMO_SIN_GRUPO");
                     }
                 }
             }
             
        }
        if (errores.size() > 0) {
            BusinessException be = new BusinessException();
            be.setErrores(errores);
            throw be;
        }
        return valida;
    }

}
