/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.business.datatype.DataCertificadoDisponibilidadPresupuestaria;
import gob.mined.siap2.business.ejbs.UsuarioBean;
import gob.mined.siap2.business.interceptors.log.LoggedInterceptor;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.Notificacion;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.PagoInsumo;
import gob.mined.siap2.entities.data.impl.PolizaDeConcentracion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.enums.TipoNotificacion;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.entities.tipos.FiltroClasFunc;
import gob.mined.siap2.entities.tipos.FiltroCronogramaRecursos;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.AdminContatoDAO;
import gob.mined.siap2.persistence.dao.imp.InsumoDAO;
import gob.mined.siap2.persistence.dao.imp.PolizaDAO;
import gob.mined.siap2.persistence.dao.imp.UsuarioDAO;
import gob.mined.siap2.ws.to.CronogramaRecurso;
import gob.mined.siap2.ws.to.ResumenClasificadorFuncional;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

/**
 *
 * @author Sofis Solutions
 */
@Stateless(name = "InsumoBean")
@LocalBean
@Interceptors({LoggedInterceptor.class})
public class InsumoBean {

    @Inject
    @JPADAO
    private InsumoDAO insumoDAO;
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private UsuarioDAO usuarioDAO;
    @Inject
    private UsuarioBean usuarioBean;
    @Inject
    @JPADAO
    private PolizaDAO polizaDAO;
    @Inject
    @JPADAO
    private AdminContatoDAO adminContratoDAO;

    private static final Logger logger = Logger.getLogger(InsumoBean.class.getName());

   /**
    * Operación que retorna insumos de los POA de trabajo por varios filtros
    * @param noUACI
    * @param codigoSIIP
    * @param idOEG
    * @param idInsumo
    * @param idAnioFiscal
    * @param idPOA
    * @param idProyecto
    * @param idProcesoAdq
    * @param idProgramaPresupuestario
    * @param idSubprogramaPresupuestario
    * @param firstResult
    * @param maxResults
    * @param orderBy
    * @param ascending
    * @return 
    */
    public List<POInsumos> getEstadosInsumos(Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq,Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario, Integer firstResult, Integer maxResults, String[] orderBy, boolean[] ascending) {
        logger.log(Level.INFO, "getEstadosInsumo");
        try {
            List<POInsumos> res = insumoDAO.getEstadosInsumos(noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario, firstResult, maxResults, orderBy, ascending);
            res.isEmpty();
            return res;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Operación que cuenta los insumos de de los POA de trabajo
     * @param noUACI
     * @param codigoSIIP
     * @param idOEG
     * @param idInsumo
     * @param idAnioFiscal
     * @param idPOA
     * @param idProyecto
     * @param idProcesoAdq
     * @param idProgramaPresupuestario
     * @param idSubprogramaPresupuestario
     * @return 
     */
    public long countEstadosInsumos(Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq,Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario) {
        logger.log(Level.INFO, "countEstadosInsumos");
        try {

            return insumoDAO.countEstadosInsumos(noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Operación que cuenta los insumos de los POA de trabajo
     * @param nombreCampo
     * @param noUACI
     * @param codigoSIIP
     * @param idOEG
     * @param idInsumo
     * @param idAnioFiscal
     * @param idPOA
     * @param idProyecto
     * @param idProcesoAdq
     * @param idProgramaPresupuestario
     * @param idSubprogramaPresupuestario
     * @return 
     */
    public BigDecimal sumarEstadosInsumos(String nombreCampo, Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq, Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario) {
        logger.log(Level.INFO, "sumarEstadosInsumos");
        try {

            return insumoDAO.sumarEstadosInsumos(nombreCampo, noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Operación que suma todo lo pagado por los insumos de los POA de trabajo
     * @param noUACI
     * @param codigoSIIP
     * @param idOEG
     * @param idInsumo
     * @param idAnioFiscal
     * @param idPOA
     * @param idProyecto
     * @param idProcesoAdq
     * @param idProgramaPresupuestario
     * @param idSubprogramaPresupuestario
     * @return 
     */
    public BigDecimal sumarPagadoInsumos(Boolean noUACI, String codigoSIIP, Integer idOEG, Integer idInsumo, Integer idAnioFiscal, Integer idPOA, Integer idProyecto, Integer idProcesoAdq, Integer idProgramaPresupuestario, Integer idSubprogramaPresupuestario) {
        logger.log(Level.INFO, "sumarEstadosInsumos");
        try {

            return insumoDAO.sumarPagadoInsumos(noUACI, codigoSIIP, idOEG, idInsumo, idAnioFiscal, idPOA, idProyecto, idProcesoAdq, idProgramaPresupuestario, idSubprogramaPresupuestario);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método devuelve un resumen por clasificador funcional del gasto de
     * los insumos según filtro.
     * @param filtro
     * @param tipo
     * @return 
     */
    public Collection<ResumenClasificadorFuncional> obtenerResumen(FiltroClasFunc filtro, String tipo) {
        logger.log(Level.INFO, "obtenerResumen");
        try {
            return insumoDAO.obtenerResumen(filtro, tipo);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Operación que retornas los certificados de disponibilidad presupuestaria
     * utilizados por el paginador
     *
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<DataCertificadoDisponibilidadPresupuestaria> getCertificadoDisponibilidadPresupuestariaInsumos(Integer firstResult, Integer maxResults) {
        logger.log(Level.INFO, "getCertificadoDisponibilidadPresupuestariaInsumos");
        try {
            List<DataCertificadoDisponibilidadPresupuestaria> res = new LinkedList();

            List<Object[]> lst = insumoDAO.getCertificadoDisponibilidadPresupuestariaInsumos(firstResult, maxResults);
            for (Object o[] : lst) {
                Integer idUnidadTecnica = (Integer) o[0];
                Integer idProcesoAdquisicion = (Integer) o[1];;
                String nombreUnidadTecnica = (String) o[2];;
                String nombreProcesoAdquisicion = (String) o[3];;
                Integer anioProceso = (Integer) o[4];

                DataCertificadoDisponibilidadPresupuestaria data = new DataCertificadoDisponibilidadPresupuestaria();
                data.setIdUnidadTecnica(idUnidadTecnica);
                data.setIdProcesoAdquisicion(idProcesoAdquisicion);
                data.setNombreProcesoAdquisicion(nombreProcesoAdquisicion);
                data.setNombreUnidadTecnica(nombreUnidadTecnica);
                data.setAnioProceso(anioProceso);

                res.add(data);
            }

            return res;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Operación que retornas los certificados de disponibilidad presupuestaria
     * utilizados por el paginador
     *
     * @return
     */
    public long countCertificadoDisponibilidadPresupuestariaInsumos() {
        logger.log(Level.INFO, "countCertificadoDisponibilidadPresupuestariaInsumos");
        try {

            return insumoDAO.countCertificadoDisponibilidadPresupuestariaInsumos();

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Operación que devuelve los montos de las fuentes de una UT dentro de un
     * proceso de adquisición
     *
     * @param idUT
     * @param idProcesoAdq
     * @return
     */
    public List<POMontoFuenteInsumo> getMontofuentesProceso(Integer idUT, Integer idProcesoAdq) {
        logger.log(Level.INFO, "getMontofuentesProceso");
        try {
            List<POMontoFuenteInsumo> res = new LinkedList();
            List<POInsumos> l = insumoDAO.getInsumosUTEnProceso(idUT, idProcesoAdq);
            for (POInsumos i : l) {
                for (POMontoFuenteInsumo iter : i.getMontosFuentes()) {
                    res.add(iter);
                }
            }
            return res;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método guarda la disponibilidad presupuestaria de un un POInsumo
     *
     * @param insumo
     * @return
     */
    public POInsumos guardarDisponibilidadPresupuestriaInsumo(POInsumos insumo) {
        try {
            if (insumo.getPasoValidacionCertificadoDeDispPresupuestaria() != null
                    && insumo.getPasoValidacionCertificadoDeDispPresupuestaria()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EL_CERTIFICADO_DE_DISPONIBILIDAD_PRESUPUESTARIA_YA_FUE_APROBADO);
                throw b;
            }

            if (!insumo.getMontosFuentes().isEmpty()) {
                for (POMontoFuenteInsumo montoFuente : insumo.getMontosFuentes()) {
                    if (montoFuente.getCertificado() != null) {
                        if (montoFuente.getCertificado().compareTo(montoFuente.getMonto()) > 0) {
                            String[] params = {montoFuente.getFuente().getFuenteRecursos().getNombre()};
                            BusinessException b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_EN_LA_FUENTE_0_SE_CERTIFICA_POR_MONTO_MAYOR_AL_DE_LA_FUENTE, params);
                            throw b;
                        }
                    }
                }
            }

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
     * Este método envía un certificado de disponibilidad presupuestaria
     *
     * @param insumo
     * @return
     */
    public POInsumos enviarCertificadoDisponibiildadPresupuestaria(POInsumos insumo) {
        try {
            List<SsUsuario> anotificar = usuarioDAO.getUsuariosConOperacion(ConstantesEstandares.Operaciones.VALIDAR_CERTIFICADO_DISPONIBILIDAD_PRESUPESTARIA, null);
            for (SsUsuario usu : anotificar) {
                Notificacion notificacion = new Notificacion();
                notificacion.setTipo(TipoNotificacion.CERTIFICADO_DISPONIBILIDAD_PRESUPUESTARIA_PARA_VALIDAR);
                notificacion.setUsuario(usu);
                notificacion.setFecha(new Date());
                notificacion.setObjetoId(insumo.getId());
                notificacion.setContenido(insumo.getId() + " " + insumo.getInsumo().getNombre());

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

    private void caluclarAprovacionCertificadoDisponibilidadPresupuestaria(POInsumos insumo) {
        BigDecimal totalCert = BigDecimal.ZERO;
        for (POMontoFuenteInsumo fuente : insumo.getMontosFuentes()) {
            if (fuente.getCertificadoDisponibilidadPresupuestariaAprobada() != null && fuente.getCertificadoDisponibilidadPresupuestariaAprobada()) {
                if (fuente.getCertificado() != null) {
                    if (fuente.getCertificado().compareTo(fuente.getMonto()) > 0) {
                        String[] params = {fuente.getInsumo().getId().toString(), fuente.getFuente().getFuenteRecursos().getNombre()};
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_EN_INSUMO_0_LA_FUENTE_1_SE_CERTIFICA_POR_MONTO_MAYOR_AL_DE_LA_FUENTE, params);
                        throw b;
                    }
                    totalCert = totalCert.add(fuente.getCertificado());
                }
            }
        }
        insumo.setMontoTotalCertificado(totalCert);
        insumo.setPasoValidacionCertificadoDeDispPresupuestaria(Boolean.TRUE);

    }

    /**
     * Este método carga un insumo para tratar el certificado de disponibilidad
     * presupuestaria
     *
     * @param idInsumo
     * @return
     */
    public POInsumos loadInsumoDisponibilidadPresupuestaria(Integer idInsumo) {
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
     * Este método envía una notificación para validar las fuentes de los insumo
     *
     * @param idUT
     * @param idProcesoAdq
     * @param fuentes
     */
    public void enviarAvalidarFuentes(Integer idUT, Integer idProcesoAdq, List<POMontoFuenteInsumo> fuentes) {
        try {
            ProcesoAdquisicion procesoAdquisicion = (ProcesoAdquisicion) generalDAO.find(ProcesoAdquisicion.class, idProcesoAdq);
            UnidadTecnica unidadTecnica = (UnidadTecnica) generalDAO.find(UnidadTecnica.class, idUT);

            //se setea el motno certificado
            for (POMontoFuenteInsumo data : fuentes) {
                if (data.getCertificado() != null && data.getCertificado().compareTo(BigDecimal.ZERO) > 0
                        && (data.getCertificadoDisponibilidadPresupuestariaAprobada() == null || data.getCertificadoDisponibilidadPresupuestariaAprobada().equals(Boolean.FALSE))) {

                    POMontoFuenteInsumo fuente = (POMontoFuenteInsumo) generalDAO.find(POMontoFuenteInsumo.class, data.getId());

                    if (data.getCertificado().compareTo(fuente.getMonto()) > 0) {
                        String[] params = {fuente.getInsumo().getId().toString(), fuente.getFuente().getFuenteRecursos().getNombre()};
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_EN_INSUMO_0_LA_FUENTE_1_SE_CERTIFICA_POR_MONTO_MAYOR_AL_DE_LA_FUENTE, params);
                        throw b;
                    }

                    fuente.getInsumo().setEnviadoParaCertificar(Boolean.TRUE);
                    fuente.setCertificado(data.getCertificado());
                    fuente.setMontoDescertificado(data.getMontoDescertificado());
                }

            }

            //se notifica a los usuarios
            List<SsUsuario> anotificar = usuarioDAO.getUsuariosConOperacion(ConstantesEstandares.Operaciones.VALIDAR_CERTIFICADO_PRES_POR_PROCESO, null);
            for (SsUsuario usu : anotificar) {
                Notificacion notificacion = new Notificacion();
                notificacion.setTipo(TipoNotificacion.CERTIFICADO_DISPONIBILIDAD_PRESUPUESTARIA_PARA_VALIDAR_POR_PROCESO);
                notificacion.setUsuario(usu);
                notificacion.setFecha(new Date());
                notificacion.setObjetoId(unidadTecnica.getId());
                notificacion.setObjeto2Id(procesoAdquisicion.getId());
                notificacion.setContenido(procesoAdquisicion.getNombre() + " " + unidadTecnica.getNombre());

                notificacion = (Notificacion) generalDAO.update(notificacion);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve la categoría presupuestaria de un POA Insumo, conformada por
     * Proyecto (en caso de ser proyecto)-programa presupuestario-subprograma
     * presupuestario
     *
     * @param idPoInsumo
     */
    public String getCategoriaPresupuestariaDePoInsumo(Integer idPoInsumo) {
        POInsumos poInsumo = (POInsumos) generalDAO.find(POInsumos.class, idPoInsumo);
        String categoriaPresupuestaria = "";
        if (poInsumo.getPoa() != null) {
            if (poInsumo.getPoa().getTipo().equals(TipoPOA.POA_PROYECTO)) {
                POAProyecto poaProyecto = (POAProyecto) poInsumo.getPoa();
                categoriaPresupuestaria += poaProyecto.getProyecto().getId() + "";
                if (poaProyecto.getProyecto().getProgramaPresupuestario().getProgramaPresupuestario() != null) {
                    categoriaPresupuestaria += "-" + poaProyecto.getProyecto().getProgramaPresupuestario().getProgramaPresupuestario().getId();
                }
                if (poaProyecto.getProyecto().getProgramaPresupuestario() != null) {
                    categoriaPresupuestaria += "-" + poaProyecto.getProyecto().getProgramaPresupuestario().getId();
                }
            } else {
                POAConActividades poaActividades = (POAConActividades) poInsumo.getPoa();
                categoriaPresupuestaria = poaActividades.getConMontoPorAnio().getId() + "";
            }
        }
        return categoriaPresupuestaria;
    }

    /**
     * Devuelve un array con el código y nombre (en ese orden) del programa (si
     * es proyecto) o de AC o ANP de un POA Insumo
     *
     * @param idPoInsumo
     */
    public String[] getCodigoProgramaACoANPDePoInsumo(Integer idPoInsumo) {
        POInsumos poInsumo = (POInsumos) generalDAO.find(POInsumos.class, idPoInsumo);
        String[] codigoNombre = new String[2];
        String codigo = null;
        String nombre = "";
        if (poInsumo.getPoa() != null) {
            if (poInsumo.getPoa().getTipo().equals(TipoPOA.POA_PROYECTO)) {
                POAProyecto poaProyecto = (POAProyecto) poInsumo.getPoa();
                if (poaProyecto.getProyecto().getProgramaPresupuestario() != null && poaProyecto.getProyecto().getProgramaPresupuestario().getProgramaPresupuestario() != null) {
                    if (poaProyecto.getProyecto().getProgramaPresupuestario() != null && poaProyecto.getProyecto().getProgramaPresupuestario().getProgramaPresupuestario().getCodigo() != null) {
                        codigo = poaProyecto.getProyecto().getProgramaPresupuestario().getProgramaPresupuestario().getCodigo().toString();
                    }
                    nombre = poaProyecto.getProyecto().getProgramaPresupuestario().getProgramaPresupuestario().getNombre();
                }
            } else {
                POAConActividades poaActividades = (POAConActividades) poInsumo.getPoa();
                if (poaActividades.getConMontoPorAnio().getCodigo() != null) {
                    codigo = poaActividades.getConMontoPorAnio().getCodigo().toString();
                }
                nombre = poaActividades.getConMontoPorAnio().getNombre();
            }
        }
        codigoNombre[0] = codigo;
        codigoNombre[1] = nombre;
        return codigoNombre;
    }

    /**
     * Devuelve un array con el código y nombre (en ese orden) del subprograma
     * (si es proyecto) de un POA Insumo
     *
     * @param idPoInsumo
     */
    public String[] getCodigoSubprogramaDePoInsumo(Integer idPoInsumo) {
        POInsumos poInsumo = (POInsumos) generalDAO.find(POInsumos.class, idPoInsumo);
        String[] codigoNombre = new String[2];
        String codigo = null;
        String nombre = "";
        if (poInsumo.getPoa() != null) {
            if (poInsumo.getPoa().getTipo().equals(TipoPOA.POA_PROYECTO)) {
                POAProyecto poaProyecto = (POAProyecto) poInsumo.getPoa();
                if (poaProyecto.getProyecto().getProgramaPresupuestario() != null) {
                    if (poaProyecto.getProyecto().getProgramaPresupuestario().getCodigo() != null) {
                        codigo = poaProyecto.getProyecto().getProgramaPresupuestario().getCodigo().toString();
                    }
                    nombre = poaProyecto.getProyecto().getProgramaPresupuestario().getNombre();
                }
            }
        }
        codigoNombre[0] = codigo;
        codigoNombre[1] = nombre;
        return codigoNombre;
    }

    /**
     * Devuelve un array con el código y nombre (en ese orden) del proyecto (si
     * no es AC o ANP) de un POA Insumo
     *
     * @param idPoInsumo
     */
    public String[] getCodigoProyectoDePoInsumo(Integer idPoInsumo) {
        POInsumos poInsumo = (POInsumos) generalDAO.find(POInsumos.class, idPoInsumo);
        String[] codigoNombre = new String[2];
        String codigo = null;
        String nombre = "";
        if (poInsumo.getPoa() != null) {
            if (poInsumo.getPoa().getTipo().equals(TipoPOA.POA_PROYECTO)) {
                POAProyecto poaProyecto = (POAProyecto) poInsumo.getPoa();
                if (poaProyecto.getProyecto().getCodigo() != null) {
                    codigo = poaProyecto.getProyecto().getCodigo().toString();
                }
                nombre = poaProyecto.getProyecto().getNombre();
            }
        }
        codigoNombre[0] = codigo;
        codigoNombre[1] = nombre;
        return codigoNombre;
    }

    /**
     * Devuelve la lista de insumos No UACI según los filtros aplicados y de
     * acuerdo a las UT que pertenezca
     *
     * @param idAnioFiscal
     * @param idProyecto
     * @param idAC
     * @param idANP
     * @param idUT
     * @param codigo
     * @param codigoInterno
     * @param firstResult
     * @param maxResults
     * @param orderBy
     * @param ascending
     * @return
     */
    public List<POInsumos> getInsumosNoUACI(Integer idAnioFiscal, Integer idProyecto, Integer idAC, Integer idANP, Integer idUT, String codigo, String codigoInterno, Integer firstResult, Integer maxResults, String[] orderBy, boolean[] ascending) {
        logger.log(Level.INFO, "getInsumosNoUACI");
        try {
            Set<Integer> listaIdUTUsuarioActual = null;
            /*Si el usuario actual tiene la operación "VER_TODOS_INSUMOS_NO_UACI" podrá ver todos los insumos No UACI,
            de lo contrario solo ve los que pertenecen a las mismas UT que él*/
            if (!usuarioBean.usuarioActualTieneOperacion(ConstantesEstandares.Operaciones.VER_TODOS_INSUMOS_NO_UACI)) {
                List<UnidadTecnica> listaUTDelUsuActual = usuarioBean.getUTDelUsuario();
                listaIdUTUsuarioActual = new HashSet<Integer>();
                for (UnidadTecnica ut : listaUTDelUsuActual) {
                    listaIdUTUsuarioActual.add(ut.getId());
                }
            }
            List<POInsumos> res = insumoDAO.getInsumosNoUACI(listaIdUTUsuarioActual, idAnioFiscal, idProyecto, idAC, idANP, idUT, codigo, codigoInterno, firstResult, maxResults, orderBy, ascending);
            res.isEmpty();
            return res;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Devuelve la cantidad de insumos No UACI según los filtros aplicados
     *
     * @param idAnioFiscal
     * @param idProyecto
     * @param idAC
     * @param idANP
     * @param idUT
     * @param codigo
     * @param codigoInterno
     * @return
     */
    public long countInsumosNoUACI(Integer idAnioFiscal, Integer idProyecto, Integer idAC, Integer idANP, Integer idUT, String codigo, String codigoInterno) {
        logger.log(Level.INFO, "countEstadosInsumos");
        try {
            Set<Integer> listaIdUTUsuarioActual = null;
            /*Si el usuario actual tiene la operación "VER_TODOS_INSUMOS_NO_UACI" podrá ver todos los insumos No UACI,
            de lo contrario solo ve los que pertenecen a las mismas UT que él*/
            if (!usuarioBean.usuarioActualTieneOperacion(ConstantesEstandares.Operaciones.VER_TODOS_INSUMOS_NO_UACI)) {
                List<UnidadTecnica> listaUTDelUsuActual = usuarioBean.getUTDelUsuario();
                listaIdUTUsuarioActual = new HashSet<Integer>();
                for (UnidadTecnica ut : listaUTDelUsuActual) {
                    listaIdUTUsuarioActual.add(ut.getId());
                }
            }
            return insumoDAO.countInsumosNoUACI(listaIdUTUsuarioActual, idAnioFiscal, idProyecto, idAC, idANP, idUT, codigo, codigoInterno);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Verifica si un insumo está asociado a un Proyecto
     *
     * @param poInsumo
     * @return
     */
    public Boolean poInsumoEsDeProyecto(POInsumos poInsumo) {
        Boolean esDeProy = false;
        if (poInsumo.getPoa() != null && poInsumo.getPoa().getTipo().equals(TipoPOA.POA_PROYECTO)) {
            esDeProy = true;
        }
        return esDeProy;
    }

    /**
     * Verifica si un insumo está asociado a una Acción Central
     *
     * @param poInsumo
     * @return
     */
    public Boolean poInsumoEsDeAC(POInsumos poInsumo) {
        Boolean esDeAC = false;
        if (poInsumo.getPoa() != null && poInsumo.getPoa().getTipo().equals(TipoPOA.POA_CON_ACTIVIDADES)) {
            POAConActividades poaConAct = (POAConActividades) poInsumo.getPoa();
            if (poaConAct.getConMontoPorAnio().getTipo().equals(TipoMontoPorAnio.ACCION_CENTRAL)) {
                esDeAC = true;
            }
        }
        return esDeAC;
    }

    /**
     * Verifica si un insumo está asociado a una Asignación No Programable
     *
     * @param poInsumo
     * @return
     */
    public Boolean poInsumoEsDeANP(POInsumos poInsumo) {
        Boolean esDeANP = false;
        if (poInsumo.getPoa() != null && poInsumo.getPoa().getTipo().equals(TipoPOA.POA_CON_ACTIVIDADES)) {
            POAConActividades poaConAct = (POAConActividades) poInsumo.getPoa();
            if (poaConAct.getConMontoPorAnio().getTipo().equals(TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE)) {
                esDeANP = true;
            }
        }
        return esDeANP;
    }

    /**
     * Devuelve la lista de Pólizas de Concentración de un PoInsumo
     *
     * @param idPoInsumo
     * @return
     */
    public List<PolizaDeConcentracion> getPolizasDePoInsumo(Integer idPoInsumo) {
        List<PolizaDeConcentracion> listaPolizasDelPoInsumo = insumoDAO.getPolizasDePoInsumo(idPoInsumo);
        listaPolizasDelPoInsumo.isEmpty();
        return listaPolizasDelPoInsumo;
    }

    /**
     * Calcula y retorna el monto del insumo asociado a Pólizas de concentración
     * aprobadas
     *
     * @return
     */
    public BigDecimal calcularMontoEnPolizasAprobadas(Integer idPoInsumo) {
        BigDecimal montoPoliza = polizaDAO.obtenerMontoEnPolizasAprobadasPorInsumo(idPoInsumo);
        return montoPoliza;
    }

    /**
     * Calcula la diferencia entre el monto estimado y el certificado de un insumo
     * @param insumo
     * @return 
     */
    public BigDecimal calcularDiferenciaEntreEstimadoYCertificado(POInsumos insumo) {
        BigDecimal diferencia = BigDecimal.ZERO;
        BigDecimal montoEstimado = insumo.getMontoTotal();
        BigDecimal montoCertificado = insumo.getMontoTotalCertificado();
        if(montoEstimado != null){
            diferencia = montoEstimado;
            if(montoCertificado != null){
                diferencia = montoEstimado.subtract(montoCertificado);
            }
        }        
        return diferencia;
    }

    /**
     * Calcula la suma de los montos del insumo en las actas de recepción aprobadas
     * @param idPoInsumo
     * @return 
     */
    public BigDecimal calcularMontoActasRecepcionAprobadas(Integer idPoInsumo) {
        try {
            BigDecimal total = BigDecimal.ZERO;
            List<PagoInsumo> listaPagosInsumo = adminContratoDAO.getPagosEnActasAprobadasPorInsumo(idPoInsumo);
            for (PagoInsumo pago : listaPagosInsumo) {
                if (pago.getImporte() != null) {
                    total = total.add(pago.getImporte());
                }
            }
            return total;
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
    * Valor de PEP + valor de reprogramaciones
    * @param insumo
    * @return 
    */
    public BigDecimal calcularDisponibleModificado(POInsumos insumo) {
        BigDecimal montoDispModif = BigDecimal.ZERO;
        if(insumo.getMontoPepOriginal()!=null){
            montoDispModif = insumo.getMontoPepOriginal();
            if(insumo.getMontoReprogramaciones()!=null){
                montoDispModif = insumo.getMontoPepOriginal().add(insumo.getMontoReprogramaciones());
            }
        }
        return montoDispModif;
    }

    /**
     * Suma de montos descertificados de las fuentes del insumo
     * @param insumo
     * @return 
     */
    public BigDecimal calcularMontoDescertificado(POInsumos insumo) {
        BigDecimal montoDescertificado = BigDecimal.ZERO;
        for(POMontoFuenteInsumo montoFuente : insumo.getMontosFuentes()){
            if(montoFuente.getMontoDescertificado()!=null){
                montoDescertificado = montoDescertificado.add(montoFuente.getMontoDescertificado());
            }
        }
        return montoDescertificado;
    }

    /**
     * (Monto disponible modificado - monto certificado) + monto descertificado
     * @param insumo
     * @return 
     */
    public BigDecimal calcularMontoDisponible(POInsumos insumo) {
        BigDecimal montoDisponibleModificado = calcularDisponibleModificado(insumo);
        BigDecimal montoCertificado = insumo.getMontoTotalCertificado()!=null?insumo.getMontoTotalCertificado():BigDecimal.ZERO;
        //BigDecimal montoDescertificado = calcularMontoDescertificado(insumo);
        
        BigDecimal montoDisponible = montoDisponibleModificado.subtract(montoCertificado);
        //montoDisponible = montoDisponible.add(montoDescertificado);
        return montoDisponible;
    }

    /**
     * Este método devuelve el cronograma de recursos según filtros
     * @param filtro
     * @param tipoReporte
     * @return 
     */
    public Collection<CronogramaRecurso> obtenerCronogramaRecrusos(FiltroCronogramaRecursos filtro, String tipoReporte) {
        logger.log(Level.INFO, "obtenerCronogramaRecrusos");
        try {
            return insumoDAO.obtenerCronogramaRecrusos(filtro, tipoReporte);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

}
