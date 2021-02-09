/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 */

package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siges.entities.data.impl.SgModalidad;
import gob.mined.siges.entities.data.impl.SgModalidadAtencion;
import gob.mined.siges.entities.data.impl.SgRelModEdModAten;
import gob.mined.siges.entities.data.impl.SgSubModalidad;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.SgModalidadesGralDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;


/**
 * Esta clase incluye los métodos para la gestión de Modalidades
 *
 * @author Sofis Solutions
 */
@Stateless(name = "sgModalidadesBean")
@LocalBean
public class SgModalidadesBean{

    private static final Logger logger = Logger.getLogger(SgModalidadesBean.class.getName());
    
    @Inject
    @JPADAO        
    private SgModalidadesGralDAO modalidadesDao;
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO; 
    
    
    /**
     * Este método devuelve una relacion de modalidades filtrada por ID
     * @param id
     * @return 
     */
    public SgRelModEdModAten getRelacionById(Integer id) {
        return generalDAO.getEntityManager().find(SgRelModEdModAten.class, id);
    }
    
    /**
     * Este método devuelve un listado de Relaciones de modalidades sin filtrar
     * @return 
     */
    public List<SgRelModEdModAten> getRelaciones() {
        try {
            List<SgRelModEdModAten> listaRelaciones = generalDAO.findAll(SgRelModEdModAten.class);
            listaRelaciones.isEmpty();
            return listaRelaciones;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este metodo devuelve una lista de Relaciones de modalidades filtradas por ID de modalidad
     * @param id
     * @return 
     */
    public List<SgRelModEdModAten> getRelacionByModalidad(Integer id) {
        try {
            List<SgRelModEdModAten> listaRelaciones = modalidadesDao.findRelacionByModalidad(id);
            listaRelaciones.isEmpty();
            return listaRelaciones;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este metodo devuelve un listado de Relaciones de modalidades filtradas por ID de SubModalidad
     * @param id
     * @return 
     */
    public List<SgRelModEdModAten> getRelacionBySubModalidad(Integer id) {
        try {
            List<SgRelModEdModAten> listaRelaciones = modalidadesDao.findRelacionBySubModalidad(id);
            listaRelaciones.isEmpty();
            return listaRelaciones;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este metodo devuelve un listado de Relaciones de modalidades filtradas por ID de Modalidad de atencion
     * @param id
     * @return 
     */
    public List<SgRelModEdModAten> getRelacionByModalidadAtencion(Integer id) {
        try {
            List<SgRelModEdModAten> listaRelaciones = modalidadesDao.findRelacionByModalidadAtencion(id);
            listaRelaciones.isEmpty();
            return listaRelaciones;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este metodo devuelve un listado de todos los registros Habilitados de Relaciones de modalidades
     * @return 
     */
    public List<SgRelModEdModAten> getRelacionHabilitados() {
        try {
            List<SgRelModEdModAten> listaRelaciones = modalidadesDao.findRelacionHabilitados();
            listaRelaciones.isEmpty();
            return listaRelaciones;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    
    
    
    /**
     * Este metodo devuelve un registro de Modalidad, filtrado por ID
     * @param id
     * @return 
     */
    public SgModalidad getModalidadById(Integer id) {
        try {
            return generalDAO.getEntityManager().find(SgModalidad.class, id);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este metodo devuelve un listado de Modalidades, filtrados por ID modalidad
     * @return 
     */
    public List<SgModalidad> getModalidades() {
        try {
            List<SgModalidad> listaMod = generalDAO.findAll(SgModalidad.class);
            listaMod.isEmpty();
            return listaMod;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este metodo devuelve un listado de Modalidades, filtrados por ID ciclo
     * @param idCiclo
     * @return 
     */
    public List<SgModalidad> getModalidadesByIdCiclo(Integer idCiclo) {
        try {
            List<SgModalidad> listaMod = modalidadesDao.findModalidadesByCiclo(idCiclo);
            return listaMod;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este metodo devuelve todos los registros de Modalidades que se encuentren Habilitados
     * @return 
     */
    public List<SgModalidad> getModalidadesHabilitados() {
        try {
            List<SgModalidad> listaMod = modalidadesDao.findModalidadesHabilitados();
            return listaMod;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    
    
    
    /**
     * Este metodo devuelve un registro de SubModalidad, filtrado por ID
     * @param id
     * @return 
     */
    public SgSubModalidad getSubModalidadById(Integer id) {
        try {
            return generalDAO.getEntityManager().find(SgSubModalidad.class, id);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este metodo devuelve un listado de SubModalidades, filtrados por ID subModalidad
     * @return 
     */
    public List<SgSubModalidad> getSubModalidades() {
        try {
            List<SgSubModalidad> listaMod = generalDAO.findAll(SgSubModalidad.class);
            return listaMod;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    /**
     * Este metodo devuelve un listado de SubModalidades, filtrados por ID modalidadAtencion
     * @param idModalidadAtencion
     * @return 
     */
    public List<SgSubModalidad> getSubModalidadesByModalidadAtencion(Integer idModalidadAtencion) {
        try {
            List<SgSubModalidad> listaMod = modalidadesDao.findSubModalidadesByModalidadAtencion(idModalidadAtencion);
            return listaMod;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este metodo devuelve todos los registros de SubModalidades que se encuentren Habilitados
     * @return 
     */
    public List<SgSubModalidad> getSubModalidadesHabilitados() {
        try {
            List<SgSubModalidad> listaMod = modalidadesDao.findSubModalidadesHabilitados();
            return listaMod;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    
    
    
    
    /**
     * Este metodo devuelve un registro de ModalidadAtencion, filtrado por ID
     * @param id
     * @return 
     */
    public SgModalidadAtencion getModalidadAtencionById(Integer id) {
        try {
            return generalDAO.getEntityManager().find(SgModalidadAtencion.class, id);
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este metodo devuelve un listado de ModalidadesAtencion, filtrados por ID ModalidadAtencion
     * @return 
     */
    public List<SgModalidadAtencion> getModalidadAtencions() {
        try {
            List<SgModalidadAtencion> listaMod = generalDAO.findAll(SgModalidadAtencion.class);
            return listaMod;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este metodo devuelve todos los registros de ModalidadesAtencion que se encuentren Habilitados
     * @return 
     */
    public List<SgModalidadAtencion> getModalidadesAtencionHabilitados() {
        try {
            List<SgModalidadAtencion> listaMod = modalidadesDao.findModalidadesAtencionHabilitados();
            return listaMod;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    
    
}
