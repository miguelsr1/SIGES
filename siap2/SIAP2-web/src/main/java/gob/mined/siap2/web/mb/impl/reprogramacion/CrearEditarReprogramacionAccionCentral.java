/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.reprogramacion;

import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.enums.TipoReprogramacion;
import gob.mined.siap2.web.delegates.impl.POAConActividadesDelegate;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que realiza la reprogramación de acción central
 * 
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "crearEditarReprogramacionAccionCentral")
public class CrearEditarReprogramacionAccionCentral extends CrearEditarReprogramacionBase implements Serializable {

  
    @Inject
    POAConActividadesDelegate poaConActividadesDelegate;
    
    /**
     * Devuelve la lista de POAs con actividades
     * @param anio
     * @param filtrarUT
     * @param utAfiltrar
     * @return 
     */
    @Override
   protected List<GeneralPOA> obtenerpoas(AnioFiscal anio, boolean filtrarUT, List<UnidadTecnica> utAfiltrar) {
        List l= poaConActividadesDelegate.getPOAsParaReprogramacion( filtrarUT,  utAfiltrar, anio.getId(), TipoMontoPorAnio.ACCION_CENTRAL);
        return l;
    }
    
    
    @PostConstruct
    public void init() {
        super.init(TipoReprogramacion.ACCION_CENTRAL);
    }

    /**
     * Dirige el sitio a la página de consulta de reprogramaciones de Acción Central
     * @return 
     */
    @Override
    public String getVolver() {
        return "consultaReprogramacionAccionCentral.xhtml";
    }

    
    
}
