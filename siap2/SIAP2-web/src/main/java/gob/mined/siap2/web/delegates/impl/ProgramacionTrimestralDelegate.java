/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.delegates.impl;

import gob.mined.siap2.business.ejbs.impl.ProgramacionTrimestralBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.ProgramacionTrimestral;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Sofis Solutions
 */
@Named
public class ProgramacionTrimestralDelegate implements Serializable {

    @Inject
    private ProgramacionTrimestralBean ptBean;

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    
    /**
     * Este método permite guardar una unidad técnica (crear o actualizar)
     * @param cnf
     * @return
     * @throws GeneralException 
     */
    public ProgramacionTrimestral guardar(ProgramacionTrimestral cnf) throws GeneralException {
        return ptBean.guardar(cnf);
    }
    
    /**
     * Este método permite obtener una ProgramacionTrimestral por Id.
     * @param id
     * @return 
     */
    public ProgramacionTrimestral obtenerPorId(Integer id) {
        return ptBean.obtenerPorId(id);
    }
    
    /**
     * Este método permite obtener una ProgramacionTrimestral por Id.
     * @param id
     * @return 
     */
    public ProgramacionTrimestral obtenerPorAnioFiscal(Integer id) {
        return ptBean.obtenerPorAnioFiscal(id);
    }
    
    /**
     * Este método permite obtener todas las POAs
     * @return
     * @throws GeneralException 
     */
    public List<ProgramacionTrimestral> obtenerTodos() throws GeneralException {
        return ptBean.obtenerTodos();
    }

    /**
     * Este método permite unidades técnicas según un criterio o filtro.
     * @param cto
     * @param orderBy
     * @param ascending
     * @param startPosition
     * @param cantidad
     * @return
     * @throws GeneralException 
     */
    public List<ProgramacionTrimestral> obtenerPorCriteria(CriteriaTO cto, String[] orderBy, boolean[] ascending, Long startPosition, Long cantidad) throws GeneralException {
        return ptBean.obtenerPorCriteria(cto, orderBy, ascending, startPosition, cantidad);
    }      
    
    /**
     * Método que se encarga de eliminar una cuenta
     *
     * @param id Identificador de registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            ptBean.eliminar(id);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
}