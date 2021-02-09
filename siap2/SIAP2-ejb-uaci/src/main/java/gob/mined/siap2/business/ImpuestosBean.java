/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.ejbs.DatosUsuario;
import gob.mined.siap2.business.ejbs.UsuarioBean;
import gob.mined.siap2.business.ejbs.impl.ArchivoBean;
import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.business.ejbs.impl.ReporteBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ActaContrato;
import gob.mined.siap2.entities.data.impl.Impuesto;
import gob.mined.siap2.entities.data.impl.QuedanEmitido;
import gob.mined.siap2.entities.data.impl.ValoresImpuestoQuedan;
import gob.mined.siap2.entities.enums.TipoImpuesto;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.AdminContatoDAO;
import gob.mined.siap2.persistence.dao.imp.ImpuestoDAO;
import gob.mined.siap2.persistence.dao.imp.POADAO;
import gob.mined.siap2.persistence.dao.imp.ProcesoAdquisicionDAO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase incluye los métodos correspondientes a la gestión de impuestos.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "ImpuestosBean")
@LocalBean
public class ImpuestosBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private ProcesoAdquisicionDAO pAdqDao;
    @Inject
    @JPADAO
    private POADAO poadao;
    @Inject
    @JPADAO
    private AdminContatoDAO adminContatoDAO;
    @Inject
    @JPADAO
    private ImpuestoDAO impuestoDAO;
    @Inject
    private DatosUsuario usrBean;
    @Inject
    private ArchivoBean archivoBean;
    @Inject
    private ReporteBean reporteBean;
    @Inject
    private DatosUsuario datosUsuario;
    @Inject
    private UsuarioBean usuarioBean;
    @Inject
    private GeneralBean generalBean;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método devuelve las actas emitidas para un proyecto dado en un rango
     * de fechas y que tienen un impuesto asociado.
     *
     * @param idProyecto
     * @param fechaDesde
     * @param fechaHasta
     * @param idImpuesto
     * @return
     */
    public List<QuedanEmitido> getActasEnProyecto(Integer idProyecto, Date fechaDesde, Date fechaHasta, Integer idImpuesto) {
        try {
            List<QuedanEmitido> actas = impuestoDAO.getQuedanEnProyecto(idProyecto, fechaDesde, fechaHasta, idImpuesto);
            actas.isEmpty();
            return actas;
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
     * Devuelve el monto retenido en un acta por determinado impuesto
     *
     * @param idActa
     * @param idImpuesto
     * @return
     */
    public BigDecimal obtenerMontoRetenidoParaImpuesto(Integer idActa, Integer idImpuesto) {
        try {
            BigDecimal montoRetenido = BigDecimal.ZERO;
            ActaContrato acta = (ActaContrato) generalDAO.find(ActaContrato.class, idActa);
            if (idImpuesto != null && acta.getQuedan() != null) {
                for (ValoresImpuestoQuedan valorImpuesto : acta.getQuedan().getValoresImpuesto()) {
                    if (valorImpuesto.getImpuesto() != null) {
                        if (valorImpuesto.getImpuesto().getId().equals(idImpuesto)) {
                            if (valorImpuesto.getValorRetencionEnPago() != null) {
                                montoRetenido = montoRetenido.add(valorImpuesto.getValorRetencionEnPago());
                            }
                        }
                    }
                }
            }
            return montoRetenido;
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
     * Guarda el impuesto creado/editado luego de verificar que el nombre no
     * exista y que los valores de porcentajes sean correctos
     *
     * @param impuesto
     * @return
     */
    public Impuesto guardarImpuesto(Impuesto impuesto) {
        try {
            generalBean.chequearIgualNombre(impuesto.getClass(), impuesto.getId(), impuesto.getNombre());
            if (impuesto.getTipoImpuesto() != null && impuesto.getTipoImpuesto().equals(TipoImpuesto.PORCENTAJE)) {
                if (!esPorcentajeCorrecto(impuesto.getValor())) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERROR_VALOR_PORCENTAJE_INCORRECTO);
                    throw b;
                }
            }
            if (!esPorcentajeCorrecto(impuesto.getPorcentajeRetencionPersonaFisicaNacional()) || !esPorcentajeCorrecto(impuesto.getPorcentajeRetencionPersonaJuridicaNacional())
                    || !esPorcentajeCorrecto(impuesto.getPorcentajeRetencionPersonaFisicaExtranjera()) || !esPorcentajeCorrecto(impuesto.getPorcentajeRetencionPersonaJuridicaExtranjera())) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_VALOR_PORCENTAJE_INCORRECTO);
                throw b;
            }
            return (Impuesto) generalDAO.update(impuesto);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    private Boolean esPorcentajeCorrecto(BigDecimal valor) {
        return (valor != null && valor.compareTo(new BigDecimal(100)) <= 0 && valor.compareTo(BigDecimal.ZERO) >= 0);
    }

}
