/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.datatype.DataInsumoPlantilla;
import gob.mined.siap2.business.ejbs.impl.PlantillaDeInsumosBean;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.entities.data.impl.NodoPlantillaDeInsumos;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class PlantillaDeInsumosDelegate implements Serializable {

    @Inject
    private PlantillaDeInsumosBean bean;

    public List<NodoPlantillaDeInsumos> loadRootInsumos() {
        return bean.loadRootInsumos();
    }

    public NodoPlantillaDeInsumos loadInsumo(Integer id) {
        return bean.loadInsumo(id);
    }

    public void eliminarNodoPlantilla (Integer id){
        bean.eliminarNodoPlantilla(id);
    }
    public Insumo eliminarInsumoPlantilla(Integer idInsumo) {
       return bean.eliminarInsumoPlantilla(idInsumo);
    }
    
    public void  agregarInsumos(List<DataInsumoPlantilla> lista, Integer idNodoPlantillaDeInsumos, Boolean noMoverInsumos, String filtroONUCodigo, Integer filtroONURubro, String filtroONUnombre, Boolean filtroONUSoloSinPlantillas) {
        bean.agregarInsumos(lista, idNodoPlantillaDeInsumos, noMoverInsumos, filtroONUCodigo, filtroONURubro, filtroONUnombre, filtroONUSoloSinPlantillas);  
    }

    public void moverInsumoAGrupo(Integer idInsumo, Integer idGrupoDestino) {
        bean.moverInsumoAGrupo(idInsumo,idGrupoDestino);
    }
}