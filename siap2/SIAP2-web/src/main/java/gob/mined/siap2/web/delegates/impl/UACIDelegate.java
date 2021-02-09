/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.UACIBean;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicionRango;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicionTarea;
import gob.mined.siap2.exceptions.GeneralException;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class UACIDelegate implements Serializable {

    @Inject
    private UACIBean bean;

    public void guardarOEditarMetodoAdquisicion(MetodoAdquisicion m, List<MetodoAdquisicionTarea> tareasABorrar, List<MetodoAdquisicionRango> rangosABorrar) throws GeneralException {
        bean.guardarOEditarMetodoAdquisicion(m, tareasABorrar, rangosABorrar);
    }

    public MetodoAdquisicion getMetodoAdquisicion(Integer id) throws GeneralException {
        return bean.getMetodoAdquisicion(id);
    }

    public void eliminarMetodoAdquisicion(Integer Id) throws GeneralException {
        bean.eliminarMetodoAdquicision(Id);
    }

    public List<String> obtenerNombresPacsAsociados(Integer id) throws GeneralException {
        return bean.obtenerPacsAsociadosPorMetId(id);
    }

    public List<String> obtenerNombresPacsAsociadosNoFinalizados(Integer id) throws GeneralException {
        return bean.obtenerPacsAsociadosNoFinalizadoPorMetId(id);
    }

    public Boolean existePacsAnioRangoAsociadosPorMetIdNoCerrado(Integer anio, Integer metId) throws GeneralException {
        return bean.existePacsAnioRangoAsociadosPorMetIdNoCerrado(anio, metId);
    }
}
