/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates;

import gob.mined.siap2.business.ejbs.ErrorBean;
import gob.mined.siap2.entities.data.Error;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Esta clase incluye los métodos para gestión de mensajes de error entre la
 * capa lógica y de presentación web.
 *
 * @author Sofis Solutions
 */
@Named
public class ErrorDelegate implements Serializable {

    @Inject
    private ErrorBean errBean;

    /**
     * este método permite guardar un objeto de tipo Error.
     * @param err
     * @return
     * @throws GeneralException 
     */
    public Error guardar(Error err) throws GeneralException {
        return errBean.guardar(err);
    }

    /**
     * Este método permite obtener un error según su id.
     * @param id
     * @return
     * @throws GeneralException
     * @throws DAOGeneralException 
     */
    public Error obtenerErrorPorId(Integer id) throws GeneralException, DAOGeneralException {
        return errBean.obtenerErrorPorId(id);
    }

    /**
     * Este método permite obtener un error según su código.
     * @param codigo
     * @return
     * @throws GeneralException 
     */
    public Error obtenerErrorPorCodigo(String codigo) throws GeneralException {
        return errBean.obtenerErrorPorCodigo(codigo);
    }

    /**
     * Este método devuelve la lista de todos los errores en el sistema.
     * @return
     * @throws GeneralException 
     */
    public List<Error> obtenerTodos() throws GeneralException {
        return errBean.obtenerTodos();
    }

    public List<Error> obtenerPorCriteria(CriteriaTO cto, String[] orderBy, boolean[] ascending, Long startPosition, Long cantidad) throws GeneralException {
        return errBean.obtenerPorCriteria(cto, orderBy, ascending, startPosition, cantidad);
    }
}
