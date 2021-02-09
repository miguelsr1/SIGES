/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.BienesDepreciablesBean;
import gob.mined.siges.entities.data.impl.SgAfTipoBienes;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate del Catalogo de Insumos
 * @author Sofis Solutions
 */
@Named
public class BienesDepreciablesDelegate implements Serializable {
    
    @Inject
    private BienesDepreciablesBean bienesBean;

    /**
     * Este método permite obtener una lista de bienes mediante un string
     * @param insumo
     * @param idArticulo 
     */
    public List<SgAfTipoBienes> obtenerTiposBienPorQuery(String query) {
        return bienesBean.obtenerTiposBienPorQuery(query);
    }
}
