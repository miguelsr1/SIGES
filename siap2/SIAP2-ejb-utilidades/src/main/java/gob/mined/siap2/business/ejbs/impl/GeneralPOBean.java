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
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.ContratoOC;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.Notificacion;
import gob.mined.siap2.entities.data.impl.OrigenDistribuccionMontoInsumo;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAMetaAnual;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumoFlujoCajaMensual;
import gob.mined.siap2.entities.data.impl.TDR;
import gob.mined.siap2.entities.enums.EstadoContrato;
import gob.mined.siap2.entities.enums.TipoNotificacion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroPOA;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.AdminContatoDAO;
import gob.mined.siap2.persistence.dao.imp.MetodoAdquisicionDAO;
import gob.mined.siap2.persistence.dao.imp.POADAO;
import gob.mined.siap2.persistence.dao.imp.UsuarioDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Stateless(name = "GeneralPOBean")
@LocalBean
public class GeneralPOBean {

    @Inject
    @JPADAO
    private MetodoAdquisicionDAO metodoAdquisicionDAO;
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private UsuarioDAO usuarioDAO;
    @Inject
    @JPADAO
    private POADAO poaDAO;
    @Inject
    @JPADAO
    private AdminContatoDAO contratoDAO;

    @Inject
    private GeneralBean generalBean;

    @Inject
    private ArchivoBean archivoBean;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método se encarga de cargar un TDR
     *
     * @param idInsumo
     * @return
     */
    public TDR getTDRInsumo(Integer idInsumo) {
        try {
            POInsumos i = (POInsumos) generalDAO.find(POInsumos.class, idInsumo);
            TDR t = i.getTdr();
            return t;
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
     * Este método se encarga de guardar un TDR en un insumo
     *
     * @param idInsumo
     * @param tdr
     */
    public void saveTDRInsumo(Integer idInsumo, TDR tdr) {
        try {
            POInsumos insumo = (POInsumos) generalDAO.find(POInsumos.class, idInsumo);
            if (tdr.getTempUploadedFile() != null) {
                if (tdr.getArchivo() == null) {
                    tdr.setArchivo(archivoBean.crearArchivo());
                }
                archivoBean.asociarArchivo(tdr.getArchivo(), tdr.getTempUploadedFile(), false);
            }

            insumo.setTdr(tdr);

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
     * para los poa de acción central y asignación no programable, agrega una
     * distribución por montos para la fuente GOES cada vez que se guarda el POA
     * hay que actualizar esto
     *
     * @param poa
     */
    public void actualizarMontosGOES(POAConActividades poa) {
        for (POActividadBase a : poa.getActividades()) {
            for (POInsumos i : a.getInsumos()) {
                actualizarMontosGOES(i);
            }
        }
    }

    /**
     * Este método actualiza la fuente GOES en un insumo
     *
     * @param i
     */
    public void actualizarMontosGOES(POInsumos i) {
        if (i.getMontosFuentes() == null) {
            i.setMontosFuentes(new LinkedList());
        }
        POMontoFuenteInsumo montoFuente = null;
        if (i.getMontosFuentes().isEmpty()) {
            OrigenDistribuccionMontoInsumo distribuccionMontoInsumo = new OrigenDistribuccionMontoInsumo();
            distribuccionMontoInsumo.setFuenteRecursos(generalBean.getGOES());

            montoFuente = new POMontoFuenteInsumo();
            montoFuente.setFuente(distribuccionMontoInsumo);

            montoFuente.setInsumo(i);
            i.getMontosFuentes().add(montoFuente);

        } else {
            montoFuente = i.getMontosFuentes().get(0);
        }

        //Si aún no se le asociaron los montos fuentes de la fuente para cada mes
        if (montoFuente.getMontosFuentesInsumosFCM() == null || montoFuente.getMontosFuentesInsumosFCM().isEmpty()) {
            montoFuente.setMontosFuentesInsumosFCM(new LinkedList<POMontoFuenteInsumoFlujoCajaMensual>());
            for (FlujoCajaAnio fca : i.getFlujosDeCajaAnio()) {
                for (POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()) {
                    if (fcm.getMontosFuentesInsumosFCM() == null) {
                        fcm.setMontosFuentesInsumosFCM(new LinkedList<POMontoFuenteInsumoFlujoCajaMensual>());
                    }
                    POMontoFuenteInsumoFlujoCajaMensual montoFuenteInsumoFCM = new POMontoFuenteInsumoFlujoCajaMensual();
                    montoFuenteInsumoFCM.setFlujoCajaMensual(fcm);
                    montoFuenteInsumoFCM.setMontoFuenteInsumo(montoFuente);
                    fcm.getMontosFuentesInsumosFCM().add(montoFuenteInsumoFCM);
                    montoFuente.getMontosFuentesInsumosFCM().add(montoFuenteInsumoFCM);
                }
            }
        }
        for (FlujoCajaAnio fca : i.getFlujosDeCajaAnio()) {
            for (POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()) {
                fcm.getMontosFuentesInsumosFCM().get(0).setMonto(fcm.getMonto());
            }
        }

        //se actualiza el monto igual al de la fuente
        montoFuente.setMonto(i.getMontoTotal());

    }

    /**
     * Este método retorna un POA de proyecto por el id de un POInsumo
     *
     * @param idInsumo
     * @return
     */
    public POAProyecto obtenerPOAProyectoPorIdPoInsumo(Integer idInsumo) {
        try {
            return poaDAO.obtenerPOAProyectoPorIdPoInsumo(idInsumo);
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
     * Este método retorna un un poaConActividades a partir del id de un
     * POInsumo
     *
     * @param idInsumo
     * @return
     */
    public POAConActividades obtenerPOAConActividadesPorIdPoInsumo(Integer idInsumo) {
        try {
            return poaDAO.obtenerPOAConActividadesPorIdPoInsumo(idInsumo);
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
     * Este método retorna la línea a partir de un poInsumo
     *
     * @param idPoInsumo
     * @return
     */
    public POLinea obtenerPOLineaPorIdPoInsumo(Integer idPoInsumo) {
        try {
            return poaDAO.obtenerPOLineaPorIdPoInsumo(idPoInsumo);
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
     * Este método solicita la descertificación de un monto de un POInsumo
     *
     * @param idMonto
     * @return
     */
    public POInsumos solicitarDescertificacionMontoPOInsumo(Integer idMonto) {
        try {
            POMontoFuenteInsumo montoFuente = (POMontoFuenteInsumo) generalDAO.find(POMontoFuenteInsumo.class, idMonto);
            POInsumos insumo = montoFuente.getInsumo();
            List<SsUsuario> anotificar = usuarioDAO.getUsuariosConOperacion(ConstantesEstandares.Operaciones.VALIDAR_CERTIFICADO_DISPONIBILIDAD_PRESUPESTARIA, null);
            for (SsUsuario usu : anotificar) {
                Notificacion notificacion = new Notificacion();
                notificacion.setTipo(TipoNotificacion.DESCERTIFICACION_MONTO_FUENTE_INSUMO_PARA_VALIDAR);
                notificacion.setUsuario(usu);
                notificacion.setFecha(new Date());
                notificacion.setObjetoId(idMonto);
                notificacion.setContenido(insumo.getId() + " " + insumo.getInsumo().getNombre() + " - Fuente: " + montoFuente.getFuente().getFuenteRecursos().getNombre());

                notificacion = (Notificacion) generalDAO.update(notificacion);
            }

            insumo.setEnviadoParaCertificar(true);

            return (POInsumos) generalDAO.update(insumo);
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
     * Este método carga un monto de fuente
     *
     * @param idMonto
     * @return
     */
    public POMontoFuenteInsumo loadPOMontoFuenteInsumo(Integer idMonto) {
        try {
            POMontoFuenteInsumo montoFuente = (POMontoFuenteInsumo) generalDAO.find(POMontoFuenteInsumo.class, idMonto);
            return montoFuente;
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
     * Se descertifica un monto (de determinada fuente) de un insumo que antes
     * había sido certificado, verificando que el isnumo no esté asociado a un
     * proceso de adquisición.
     *
     * @param idPOMontoFuenteInsumo
     * @return
     */
    public POMontoFuenteInsumo aprobarDescertificacionMontoFuenteInsumo(Integer idPOMontoFuenteInsumo) {
        try {
            POMontoFuenteInsumo montoFuente = (POMontoFuenteInsumo) generalDAO.find(POMontoFuenteInsumo.class, idPOMontoFuenteInsumo);

            POInsumos insumo = montoFuente.getInsumo();
            if (insumo.getProcesoInsumo() != null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NO_SE_PUEDE_DESCERTIFICAR_MONTO_INSUMO_PERTENECIENTE_PRO_ADQ);
                throw b;
            }

            BigDecimal nuevoTotalInsumo = insumo.getMontoTotalCertificado().subtract(montoFuente.getCertificado());

            montoFuente.setCertificadoDisponibilidadPresupuestariaAprobada(false);
            montoFuente.setCertificadodeDisponibilidadPresupuestaria(null);
            montoFuente.setMontoDescertificado(montoFuente.getCertificado());
            montoFuente.setCertificado(BigDecimal.ZERO);

            insumo.setMontoTotalCertificado(nuevoTotalInsumo);
            insumo = (POInsumos) generalDAO.update(insumo);

            return montoFuente;
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
     * Este método devuelve un poInsumo a partir de un id
     *
     * @param idInsumo
     * @return
     */
    public POInsumos getPOInsumoByID(Integer idInsumo) {
        try {
            POInsumos insumo = (POInsumos) generalDAO.find(POInsumos.class, idInsumo);
            insumo.getMontosFuentes().isEmpty();
            return insumo;
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
     * Este método retorna el menor año de ejecución
     *
     * @return
     */
    public AnioFiscal getMenorAnioEjecucion() {
        try {
            String[] orderBy = {"anio"};
            boolean[] ascending = {true};
            List<AnioFiscal> anios = generalDAO.findByOneProperty(AnioFiscal.class, "habilitadoEjecucion", Boolean.TRUE, orderBy, ascending);
            if (anios.isEmpty()) {
                return null;
            }
            return anios.get(0);
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
     * Este método permite obtener un monto fuente por su id.
     *
     * @param idFuente
     * @return
     */
    public POMontoFuenteInsumo getMontoFuenteByID(Integer idFuente) {
        try {
            POMontoFuenteInsumo montoFuente = (POMontoFuenteInsumo) generalDAO.find(POMontoFuenteInsumo.class, idFuente);
            montoFuente.getMontosFuentesInsumosFCM().isEmpty();
            return montoFuente;
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
     * Verifica si alguna de las fuentes del insumo fue enviada para
     * certificación o ya fue certificada
     *
     * @return
     */
    public Boolean insumoTieneFuenteCertificadaOParaCertificar(POInsumos poInsumo) {
        try {
            Boolean insumoTieneFuenteEnCertificado = false;
            if (poInsumo != null && poInsumo.getId() != null) {
                Iterator<POMontoFuenteInsumo> itMontosFuentes = poInsumo.getMontosFuentes().iterator();
                while (itMontosFuentes.hasNext() && !insumoTieneFuenteEnCertificado) {
                    POMontoFuenteInsumo montoFuente = itMontosFuentes.next();
                    if (montoFuente.getCertificadoDisponibilidadPresupuestaria() != null) {
                        insumoTieneFuenteEnCertificado = true;
                    }
                }
            }

            return insumoTieneFuenteEnCertificado;
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
     * Devuelve el id de un contrato al que pertenece un PoInsumo
     *
     * @param poInsumo
     * @return
     */
    public ContratoOC getContratoAsociadoAInsumo(POInsumos poInsumo) {

        try {
            ContratoOC contrato = null;
            if (poInsumo != null) {
                if (poInsumo.getProcesoInsumo() != null) {
                    if (poInsumo.getProcesoInsumo().getRelItemInsumos() != null) {
                        if (!poInsumo.getProcesoInsumo().getRelItemInsumos().isEmpty() && poInsumo.getProcesoInsumo().getRelItemInsumos().get(0) != null) {
                            if (poInsumo.getProcesoInsumo().getRelItemInsumos().get(0).getItem() != null) {
                                if (poInsumo.getProcesoInsumo().getRelItemInsumos().get(0).getItem() != null) {
                                    if (poInsumo.getProcesoInsumo().getRelItemInsumos().get(0).getItem().getContrato() != null) {
                                        if (poInsumo.getProcesoInsumo().getRelItemInsumos().get(0).getItem().getContrato().getEstado() != EstadoContrato.EN_CREACION_DENTRO_PROCESO) {
                                            contrato = poInsumo.getProcesoInsumo().getRelItemInsumos().get(0).getItem().getContrato();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return contrato;
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
}
