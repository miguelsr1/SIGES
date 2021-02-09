/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.TechoPresupuestarioGOESBean;
import gob.mined.siap2.entities.data.impl.TechoPresupuestarioGOES;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class TechoPresupuestarioGOESDelegate implements Serializable {
    @Inject
    private TechoPresupuestarioGOESBean bean;

    public void crearOActualizarTechoPresupuestarioGOES(TechoPresupuestarioGOES techo) {
        bean.crearOActualizarTechoPresupuestarioGOES(techo);
    }

    public TechoPresupuestarioGOES getTechoPresupuestario(Integer id) {
        return bean.getTechoPresupuestario(id);
    }

    public void eleiminarTechoPresupuestarioGOES(Integer id) {
        bean.eleiminarTechoPresupuestarioGOES(id);
    }
   
}
