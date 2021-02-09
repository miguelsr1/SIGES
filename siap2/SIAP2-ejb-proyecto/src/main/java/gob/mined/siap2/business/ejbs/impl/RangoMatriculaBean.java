/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.RangoMatricula;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.RangoMatriculaDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

@LocalBean
@Stateless(name = "RangoMatriculaBean")
public class RangoMatriculaBean {
 
    
    private static final Logger LOGGER = Logger.getLogger(RangoMatriculaBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private RangoMatriculaDAO rangoMatriculaDAO;
    
    
    
    /**
     * Este método crea o actualiza una lista de RangoMatricula
     * 
     * @param listaRango 
     * @param listaRangoEliminar 
     */
    public void crearActualizar(List<RangoMatricula> listaRango, List<RangoMatricula> listaRangoEliminar) {
        try {
            eliminar(listaRangoEliminar);
            if(listaRango != null && !listaRango.isEmpty()){
                for(RangoMatricula item : listaRango){
                    if(item.getId() == null){
                        generalDAO.create(item);
                    }else{
                        generalDAO.update(item);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    /**
     * Método que se encarga de eliminar un RangoMatricula
     * 
     * @param listaRango
     */
    public void eliminar(List<RangoMatricula> listaRango) {
        try {
            RangoMatricula rng;
            if(listaRango != null && !listaRango.isEmpty()){
                for(RangoMatricula item : listaRango){
                    rng = getRangoMatriculaById(item.getId());
                    generalDAO.delete(rng);
                }
            }
        } catch (DAOGeneralException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
 
    
    
    
    /**
     * Este método devuelve un RangoMatricula filtrado por Id
     * @param rangoId
     * @return 
     */
    public RangoMatricula getRangoMatriculaById(Integer rangoId) {
        return generalDAO.getEntityManager().find(RangoMatricula.class, rangoId);
    }

    
    /**
     * Este método devuelve todos los registros de RangoMatricula
     * @return 
     */
    public List<RangoMatricula> getRangoMatricula() {
        try {
            List<RangoMatricula> listaRegistros = rangoMatriculaDAO.getRangoMatricula();
            listaRegistros.isEmpty();
            return listaRegistros;
        } catch (DAOGeneralException ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método devuelve todos los registros de RangoMatricula filtrado por RelacionPresupuestoAnioFiscal
     * 
     * @param idRelacion
     * @return 
     */
    public List<RangoMatricula> getRangoMatriculaByRelacion(Integer idRelacion) {
        try {
            List<RangoMatricula> listaRegistros = rangoMatriculaDAO.getRangoMatriculaByRelacion(idRelacion);
            listaRegistros.isEmpty();
            return listaRegistros;
        } catch (DAOGeneralException ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
}
