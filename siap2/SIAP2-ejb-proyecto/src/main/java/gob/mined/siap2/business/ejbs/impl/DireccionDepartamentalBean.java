package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.DireccionDepartamental;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.DireccionDepartamentalDAO;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

@LocalBean
@Stateless(name = "DireccionDepartamentalBean")
public class DireccionDepartamentalBean {
    
    private static final Logger logger = Logger.getLogger(DireccionDepartamentalBean.class.getName());

    
    @Inject
    @JPADAO
    private DireccionDepartamentalDAO direccionDepartamentalDAO;
    
    
    /**
     * Este método busca un registro de direccionDepartamental por ID departamento
     * @param idDepartamento
     * @return 
     */
    public DireccionDepartamental getDireccionDepartamentalByIdDepartamento(Integer idDepartamento) {
        try {
            return direccionDepartamentalDAO.getDireccionDepartamentalByIdDepartamento(idDepartamento);
        } catch (DAOGeneralException ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método busca un registro de direccionDepartamental por ID departamento
     * @param idDepartamento
     * @return 
     */
    public DireccionDepartamental getDireccionDepartamentalIdDepartamento(Integer idDepartamento) {
        try {
            List<DireccionDepartamental> direcciones =  direccionDepartamentalDAO.getDireccionDepartamentalIdDepartamento(idDepartamento);
            if(direcciones != null && !direcciones.isEmpty() && direcciones.size() > 0) {
                return direcciones.get(0);
            }
        } catch (DAOGeneralException ex) {
            return null;
        }
        return null;
    }
    
    /**
     * Este método busca todos los registros de direccionDepartamental
     * 
     * @return 
     */
    public List<DireccionDepartamental> getDireccionesDepartamentales() {
        try {
            return direccionDepartamentalDAO.getDireccionesDepartamentales();
        } catch (DAOGeneralException ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
}
