/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ValoresDeIndicadoresPOA;
import gob.mined.siap2.business.datatypes.DataVerIndicadorPOA;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class ValoresDeIndicadoresPOADelegate implements Serializable {

    @Inject
    private ValoresDeIndicadoresPOA bean;

    
    public List<DataVerIndicadorPOA> getVisualizacionDeValoresDeIndicadoresPOA(Integer idAnioFiscal, Integer idProgramaInstitucional) {
        return bean.getVisualizacionDeValoresDeIndicadoresPOA(idAnioFiscal, idProgramaInstitucional);
    }
}
