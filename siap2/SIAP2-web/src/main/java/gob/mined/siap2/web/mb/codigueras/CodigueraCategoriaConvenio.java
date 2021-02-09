/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.codigueras;

import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "codigueraCategoriaConvenio")
public class CodigueraCategoriaConvenio extends CodigueraGenerico<CategoriaConvenio>implements Serializable {

    @PostConstruct
    public void init() {
        super.init();
    }
    
    @Override
    public String[] getPropiedades(){
       String[] propiedades = {"id","codigo","nombre","tipo"};
       return  propiedades;
    }
    
    @Override
    public String[] getOrderBy(){
        String[] orderBy = {"codigo"};
        return orderBy;
    }
    
    @Override
    public boolean[] getAsc(){
        boolean[] asc = {true};
        return asc;
    }

}
