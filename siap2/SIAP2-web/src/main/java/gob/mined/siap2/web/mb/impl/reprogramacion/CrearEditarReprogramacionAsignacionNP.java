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
 * que realiza la reprogramación de asignación no programable
 * 
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "crearEditarReprogramacionAsignacionNP")
public class CrearEditarReprogramacionAsignacionNP extends CrearEditarReprogramacionBase implements Serializable {

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
        List l= poaConActividadesDelegate.getPOAsParaReprogramacion( filtrarUT,  utAfiltrar, anio.getId(), TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE);
        return l;
    }
    

    @PostConstruct
    public void init() {
        super.init(TipoReprogramacion.ASIGNACION_NO_PROGRAMABLE);
    }

    /**
     * Dirige el sitio a la página de consulta de reprogramaciones de ANP
     * @return 
     */
    @Override
    public String getVolver() {
        return "consultaReprogramacionAsignacionNoProgramable.xhtml";
    }

}
