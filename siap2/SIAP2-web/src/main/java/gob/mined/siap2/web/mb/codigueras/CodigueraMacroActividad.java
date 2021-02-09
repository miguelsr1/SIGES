/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.codigueras;

import gob.mined.siap2.entities.data.impl.MacroActividad;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "codigueraMacroActividad")
public class CodigueraMacroActividad extends CodigueraGenerico<MacroActividad>implements Serializable {

    @PostConstruct
    public void init() {
        super.init();
    }
    
    public void cancelar() {
        editando= new MacroActividad();
         RequestContext.getCurrentInstance().execute("$('#editModal').modal('hide');");
    }

    
}
