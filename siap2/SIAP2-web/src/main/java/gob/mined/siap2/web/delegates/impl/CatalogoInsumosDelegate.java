/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.CatalogoInsumosBean;
import gob.mined.siap2.entities.data.impl.Insumo;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa un delegate del Catalogo de Insumos
 * @author Sofis Solutions
 */
@Named
public class CatalogoInsumosDelegate implements Serializable {

    @Inject
    private CatalogoInsumosBean bean;

    /**
     * Este método permite crear o actualizar un insumo del catálogo de insumos.
     * @param insumo
     * @param idArticulo 
     */
    public void crearOActualizarCatalogoInsumos(Insumo insumo, Integer idArticulo) {
        bean.crearOActualizarCatalogoInsumos(insumo, idArticulo);
    }

    /**
     * Este método permite eliminar un insumo
     * @param id 
     */
    public void eleiminarInsumo(Integer id) {
        bean.eleiminarInsumo(id);
    }
}
