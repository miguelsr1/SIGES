/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import com.mined.siap2.to.CompromisoArchivoTO;
import gob.mined.siap2.business.InformacionCompromiso;
import gob.mined.siap2.entities.data.impl.CompromisoSafi;
import gob.mined.siap2.entities.data.impl.InfCompromiso;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author sofis
 */
@Named
public class InformacionCompromisoDelegate implements Serializable {

    @Inject
    private InformacionCompromiso bean;


    public InfCompromiso saveArchivo(InfCompromiso infCompromiso) {
        return bean.saveArchivo(infCompromiso);
    }
    
    public InfCompromiso getCompromiso(Integer idCompromiso){
        return bean.getCompromiso(idCompromiso);
    }
    
    public List<CompromisoArchivoTO> obtenerArchivos(){
        return bean.obtenerArchivos();
    }
    
    public List<CompromisoSafi> obtenerCompromisosSafi(String nroCompromisoSafi){
        return bean.obtenerCompromisosSafi(nroCompromisoSafi);
    }


}
