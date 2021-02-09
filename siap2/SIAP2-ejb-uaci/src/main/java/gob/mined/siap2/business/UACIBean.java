/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicion;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicionRango;
import gob.mined.siap2.entities.data.impl.MetodoAdquisicionTarea;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.imp.MetodoAdquisicionDAO;
import gob.mined.siap2.utils.generalutils.StringsUtils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa métodos generales de UACI.
 * @author Sofis Solutions
 */
@Stateless(name = "UACIBean")
@LocalBean
public class UACIBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
     
    @Inject
    @JPADAO
    private MetodoAdquisicionDAO madao;
     

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método permite guardar o crear un método de adquisición
     * @param metodo
     * @param tareasABorrar
     * @param rangosABorrar
     * @throws GeneralException 
     */
    public void guardarOEditarMetodoAdquisicion(MetodoAdquisicion metodo, List<MetodoAdquisicionTarea> tareasABorrar, List<MetodoAdquisicionRango> rangosABorrar) throws GeneralException {
        try {            
            BusinessException b = new BusinessException();
            if (StringsUtils.isEmpty(metodo.getNombre())) {
                b.addError(ConstantesErrores.ERR_NOMBRE_ADQUISICION_VACIO);
            }
            if (metodo.getTareas().size() < 1) {
                b.addError(ConstantesErrores.ERR_NOMBRE_ADQUISICION_CANT_TAREAS);
            }
            if (metodo.getNormativa() == null) {
                b.addError(ConstantesErrores.ERR_ADQUISICION_NORMATIVA_VACIA);
            }
            if (rangosABorrar != null && !rangosABorrar.isEmpty() && !metodo.getRangos().isEmpty()) {
                for (MetodoAdquisicionRango rango : rangosABorrar) {
                    MetodoAdquisicionRango delrango = (MetodoAdquisicionRango) generalDAO.find(MetodoAdquisicionRango.class, rango.getId());
                    generalDAO.delete(delrango);
                }
            } else if (rangosABorrar != null && !rangosABorrar.isEmpty() && metodo.getRangos().isEmpty()) {
                b.addError(ConstantesErrores.ERR_ADQUISICION_RANGOS_DELETE);
            }

            if (tareasABorrar != null && !tareasABorrar.isEmpty() && !metodo.getTareas().isEmpty()) {
                for (MetodoAdquisicionTarea tarea : tareasABorrar) {
                    MetodoAdquisicionTarea deltarea = (MetodoAdquisicionTarea) generalDAO.find(MetodoAdquisicionTarea.class, tarea.getId());
                    generalDAO.delete(deltarea);
                }
            } else if (tareasABorrar != null && !tareasABorrar.isEmpty() && metodo.getTareas().isEmpty()) {
                b.addError(ConstantesErrores.ERR_ADQUISICION_TAREAS_DELETE);
            }
            if (!b.getErrores().isEmpty()) {
                throw b;
            }
            
            generalDAO.update(metodo);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método elimina un método de adquisición
     * @param metId
     */
    public void eliminarMetodoAdquicision(Integer metId) throws GeneralException {
        try {
            MetodoAdquisicion m = (MetodoAdquisicion) generalDAO.find(MetodoAdquisicion.class, metId);
            generalDAO.delete(m);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve un método de adquisición según su id
     * @param id
     * @return
     * @throws GeneralException 
     */
    public MetodoAdquisicion getMetodoAdquisicion(Integer id) throws GeneralException {
        try {
            MetodoAdquisicion m = (MetodoAdquisicion) generalDAO.find(MetodoAdquisicion.class, id);
            m.getRangos().isEmpty();
            m.getTareas().isEmpty();
            return m;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve los PAC asociados a un método
     * @param metId
     * @return
     * @throws GeneralException 
     */
    public List<String> obtenerPacsAsociadosPorMetId(Integer metId) throws GeneralException {
        try {
            return madao.obtenerPacsNombresAsociadosPorMetId(metId);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método permite obtener los PAC asociados no finalizados según el método.
     * @param metId
     * @return
     * @throws GeneralException 
     */
    public List<String> obtenerPacsAsociadosNoFinalizadoPorMetId(Integer metId) throws GeneralException {
        try {
            return madao.obtenerPacsNombresNoFinalizadoAsociadosPorMetId(metId);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método determina si existen PAC no cerrados para el año dado según el método indicado.
     * @param anio
     * @param metId
     * @return
     * @throws GeneralException 
     */
    public Boolean existePacsAnioRangoAsociadosPorMetIdNoCerrado(Integer anio, Integer metId) throws GeneralException {
        try {

            return !madao.obtenerPacsAnioRangoAsociadosPorMetIdNoCerrado(anio, metId).isEmpty();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
}
