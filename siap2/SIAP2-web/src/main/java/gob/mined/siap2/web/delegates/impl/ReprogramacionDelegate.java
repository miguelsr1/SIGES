/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ReprogramacionBean;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.Reprogramacion;
import gob.mined.siap2.entities.tipos.FiltroReprogramacion;
import gob.mined.siap2.exceptions.BusinessException;
import java.io.Serializable;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author rodrigo
 */
@Named
public class ReprogramacionDelegate implements Serializable {

    @Inject
    private ReprogramacionBean bean;

    public Reprogramacion getReprogramacion(Integer idReprogramacion) {
        return bean.getReprogramacion(idReprogramacion);
    }

    public Reprogramacion guardarReprogramacion(Reprogramacion reprog) {
        return bean.guardarReprogramacion(reprog);
    }

    public Reprogramacion enviarReprogramacion(Reprogramacion reprog) {
        return bean.enviarReprogramacion(reprog);
    }

    public Reprogramacion rechazarEnPac(Reprogramacion reprog) {
        return bean.rechazarEnPac(reprog);
    }

    public Reprogramacion aprobarEnPac(Reprogramacion reprog) throws BusinessException {
        return bean.aprobarEnPac(reprog);
    }

    public Collection<PACGrupo> obtenerGruposPorPOAId(Integer id) {
        return bean.obtenerGruposPorPOAId(id);
    }

    public Collection<Reprogramacion> obtenerReprogramacionesPorFiltro(FiltroReprogramacion filtro) {
        return bean.obtenerReprogramacionesPorFiltro(filtro);
    }

    public Reprogramacion aplicarReprogramacion(Integer idReprogramacion) {
        return bean.aplicarReprogramacion(idReprogramacion);
    }

    public PAC obtenerPACPorPOAId(Integer id) {
        return bean.obtenerPACPorPOAId(id);
    }

}
