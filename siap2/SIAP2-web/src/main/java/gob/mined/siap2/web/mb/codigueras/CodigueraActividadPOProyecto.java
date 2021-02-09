/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.codigueras;

import gob.mined.siap2.entities.data.impl.ActividadPOProyecto;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página de codigueras de ActividadesPOProyecto
 * 
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "codigueraActividadPOProyecto")
public class CodigueraActividadPOProyecto extends CodigueraGenerico<ActividadPOProyecto>implements Serializable {

    @PostConstruct
    public void init() {
        super.init();
    }
    
    


}
