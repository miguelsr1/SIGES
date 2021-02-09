/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.codigueras;

import gob.mined.siap2.entities.data.impl.IndicadorFormaMedicion;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "codigueraIndicadorFormaMedicion")
public class CodigueraIndicadorFormaMedicion extends CodigueraGenerico<IndicadorFormaMedicion>implements Serializable {

    @PostConstruct
    public void init() {
        super.init();
    }
    
    


}
