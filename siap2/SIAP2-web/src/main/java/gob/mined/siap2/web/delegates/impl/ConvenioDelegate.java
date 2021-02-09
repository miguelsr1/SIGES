/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ejbs.impl.ConvenioBean;
import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import gob.mined.siap2.entities.data.impl.Convenio;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase implementa los métodos correspondientes a un delegate de Convenio.
 * @author Sofis Solutions
 */
@Named
public class ConvenioDelegate implements Serializable {

    @Inject
    private ConvenioBean bean;

    /**
     * Este método permite crear o actualizar un convenio.
     * @param c 
     */
    public void crearActualizarConvenio(Convenio c) {
        bean.crearActualizarConvenio(c);
    }

    /**
     * Este método permite obtener un convenio a partir de su id.
     * @param idConvenio
     * @return 
     */
    public Convenio getConvenio(Integer idConvenio) {
        return bean.getConvenio(idConvenio);
    }

    /**
     * Este método permite obtener todos los convenios que incluyen query en su nombre.
     * @param query
     * @return 
     */
    public List<Convenio> getConveniosHabilitadosPorCodigo(String query) {
        return bean.getConveniosHabilitadosPorCodigo(query);
    }

    /**
     * Este método permite obtener las categorías de un convenio según su id.
     * @param idConvenio
     * @return 
     */
    public List<CategoriaConvenio> getCategorias(Integer idConvenio) {
        return bean.getCategorias(idConvenio);
    }

}
