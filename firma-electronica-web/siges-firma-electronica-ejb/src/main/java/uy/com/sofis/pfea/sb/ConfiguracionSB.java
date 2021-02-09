/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uy.com.sofis.pfea.sb;

import com.sofis.persistence.dao.GeneralDAO;
import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import uy.com.sofis.pfea.entities.Configuracion;
import uy.com.sofis.pfea.anotaciones.JPADAO;
import uy.com.sofis.pfea.constantes.ConstantesErrores;
import uy.com.sofis.pfea.exceptions.BusinessException;
import uy.com.sofis.pfea.exceptions.GeneralException;
import uy.com.sofis.pfea.exceptions.TechnicalException;

/**
 *
 * @author Sofis Solutions
 */
@Named
@Stateless(name = "ConfiguracionSB")
@LocalBean
public class ConfiguracionSB {

    private static final Logger logger = Logger.getLogger(ConfiguracionSB.class.getName());

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    public Configuracion obtenerCnfPorCodigo(String codigo) {
        try {
            Configuracion result = null;
            CriteriaTO to = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cnfCodigo", codigo);
            List<Configuracion> lista = generalDAO.findEntityByCriteria(Configuracion.class, to, null, null, null, null);
            if (lista != null && !lista.isEmpty()) {
                result = lista.get(0);
            }
            return result;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            te.setEx(ex);
            throw te;
        }
    }

    /**
     * Este método permite obtener una configuración con un determinado código
     * en el idioma indicado
     *
     * @param codigoConfiguracion es el código de la configuración.
     * @return
     * @throws GeneralException
     */
    @TransactionAttribute(REQUIRES_NEW)
    public String obtenerConfiguracionPorCodigos(String codigoConfiguracion) throws GeneralException {
        try {
            String result;
            CriteriaTO to = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "cnfCodigo", codigoConfiguracion);
            List<Configuracion> lista = generalDAO.findEntityByCriteria(Configuracion.class, to, null, null, null, null);
            if (lista == null || lista.isEmpty()) {
                Configuracion config = new Configuracion();
                config.setCnfCodigo(codigoConfiguracion);
                config.setCnfDescripcion("??" + codigoConfiguracion + "_??");
                config.setCnfValor("");
                config = guardar(config);
                result = config.getCnfValor();
            } else {
                result = lista.get(0).getCnfValor();
            }

            return result;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            throw ex;
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            te.setEx(ex);
            throw te;
        }
    }

    /**
     * Este método guarda un elemento de tipo Configuracion. Se aplica para la
     * creación de la entidad y para la modificación de una entidad existente.
     *
     * @param config
     * @return
     * @throws GeneralException Devuelve los códigos de error de la validación
     */
    public Configuracion guardar(Configuracion config) throws GeneralException {
        try {
            //Validar el elemento a guardar. Si no valida, se lanza una excepcion

            if (validar(config)) {
                if (config.getCnfId() == null) {
                    if (config.getCnfCodigo().length() > 255) {
                        config.setCnfCodigo(config.getCnfCodigo().substring(0, 255));
                        config.setCnfValor(config.getCnfValor().substring(0, 255));
                    }
                    config = (Configuracion) generalDAO.create(config);
                } else {
                    config = (Configuracion) generalDAO.update(config);
                }
            }
            return config;
        } catch (BusinessException be) {
            //Si es de tipo negocio envía la misma excepción
            throw be;
        } catch (DAOGeneralException ex) {
            //Las demás excepciones se consideran técnicas
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    private boolean validar(Configuracion config) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (config == null) {
            ge.addError(ConstantesErrores.ERROR_DATO_VACIO);
        } else {
            String aux = normalizarString(config.getCnfCodigo());
            config.setCnfCodigo(aux);
            if (StringUtils.isBlank(config.getCnfCodigo())) {
                ge.addError(ConstantesErrores.ERROR_CODIGO_VACIO);
            }
            aux = normalizarString(config.getCnfValor());
            config.setCnfValor(aux);
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Normaliza una cadena de caracteres de la siguiente manera: Elimina los
     * espacios en blanco al inicio, al final y, si ocurren más de un espacio en
     * blanco consecutivo, deja uno solo.
     *
     * @param s
     * @return
     */
    private String normalizarString(String s) {
        if (s != null) {
            return s.replaceAll("[ ]+", " ").trim();
        }
        return null;
    }
    
        /**
     * Este método permite obtener las configuraciones.
     *
     * @return
     */
    public List<Configuracion> obtenerTodasLasConfiguraciones() {
        List<Configuracion> respuesta = null;
        try {
            respuesta = generalDAO.findAll(Configuracion.class);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            te.setEx(ex);
            throw te;
        }
        return respuesta;
    }


}
