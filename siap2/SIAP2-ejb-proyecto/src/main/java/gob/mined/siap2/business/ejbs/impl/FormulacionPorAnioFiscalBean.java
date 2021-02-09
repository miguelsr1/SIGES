/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos correspondientes a los servicios de formulación de flujo por año fiscal.
 * @author Sofis Solutions
 */
@Stateless(name = "FormulacionPorAnioFiscalBean")
@LocalBean
public class FormulacionPorAnioFiscalBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método crea o actualiza la formulación de un año
     * @param formulacion 
     */
    public void crearActualizarFormulacion(AnioFiscal formulacion) {
        try {
            /*cheuqea no exista igual nombre */
             
            if (formulacion.getDesde().compareTo(formulacion.getHasta()) > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_FECHA_DESDE_MAYOR_QUE_HASTA);
                throw b;
            }

 
            //que no exista ya una formulacion para el año ingresado
            List<AnioFiscal> l2 = generalDAO.findByOneProperty(AnioFiscal.class, "anio", formulacion.getAnio());
            if (!l2.isEmpty() && !l2.get(0).equals(formulacion)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_YA_EXISTE_FORMULACION_PARA_ANIO_FISCAL);
                throw b;
            }

            generalDAO.update(formulacion);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método elimina una formación según su id.
     * @param id 
     */
    public void eliminarFormulacion(Integer id) {
        try {
            AnioFiscal i = (AnioFiscal) generalDAO.findById(AnioFiscal.class, id);
 
            generalDAO.delete(i);
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA_SIN_PARAMETROS);
            throw b;
        }
    }
}
