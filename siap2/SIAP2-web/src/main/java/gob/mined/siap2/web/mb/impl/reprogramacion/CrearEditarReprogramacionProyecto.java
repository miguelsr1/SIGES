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
import gob.mined.siap2.entities.enums.TipoReprogramacion;
import gob.mined.siap2.web.delegates.POAProyectoDelegate;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;


/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * que realiza la reprogramación de proyecto
 * 
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "crearEditarReprogramacionProyecto")
public class CrearEditarReprogramacionProyecto extends CrearEditarReprogramacionBase implements Serializable {

    
    @Inject
    private POAProyectoDelegate poaPyDelegate;
    
    @Override
    protected List<GeneralPOA> obtenerpoas(AnioFiscal anio, boolean filtrarUT, List<UnidadTecnica> utAfiltrar) {
        List l= poaPyDelegate.getPOAsParaReprogramacion(filtrarUT, utAfiltrar,  anio.getId());
        return l;
    }
    
    
    @PostConstruct
    public void init() {
        super.init(TipoReprogramacion.PROYECTO);
    }

    
    @Override
    public String getVolver() {
        return "consultaReprogramacionProyecto.xhtml";
    }
    
    
    
}
