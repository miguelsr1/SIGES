/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.codigueras;

import gob.mined.siap2.entities.data.impl.Categoria;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
/**
 * 
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "codigueraCategoria")
public class CodigueraCategoria extends CodigueraGenerico<Categoria> implements Serializable {

    @PostConstruct
    public void init() {
        super.init();
    }
    
    
    @Override
    public String[] getPropiedades(){
       String[] propiedades = {"id","habilitado","orden","codigo","nombre", "tipoSeguimiento"};
       return  propiedades;
    }
    
     public void crearCategoria() throws Exception{
      editando  =new Categoria();
      editando.setEsProducto(Boolean.FALSE);
    }
    


}
