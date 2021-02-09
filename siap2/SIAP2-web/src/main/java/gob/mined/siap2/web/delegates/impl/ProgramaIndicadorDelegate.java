/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ejbs.impl.ProgramaIndicadorlBean;
import gob.mined.siap2.entities.data.impl.ProgramaIndicador;
import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class ProgramaIndicadorDelegate implements Serializable {
    @Inject
    private ProgramaIndicadorlBean bean;

    public void crearOActualizarProgramaIndicador(Integer idPrograma, Integer idIndicador, ProgramaIndicador objeto) {
        bean.crearOActualizarProgramaIndicador(idPrograma, idIndicador, objeto);
    }

    public void eleiminarProgramaIndicador(Integer id) {
        bean.eleiminarProgramaIndicador(id);
    }
    
}
