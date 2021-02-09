/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ejbs.impl.LineaEstrategicaBean;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class LineaEstrategicaDelegate implements Serializable {
    @Inject
    private LineaEstrategicaBean bean;
    
    public void crearEditarLineaEstrategica(LineaEstrategica l){
        bean.crearEditarLineaEstrategica(l);
    }
    
    public void eliminarLinea(Integer idLinea){
        bean.eliminarLinea(idLinea);
    }
    
}
