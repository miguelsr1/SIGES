/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.ejbs.DatosUsuario;
import gob.mined.siap2.business.ejbs.UsuarioBean;
import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.business.ejbs.impl.GeneralPOBean;
import gob.mined.siap2.business.utils.FlujoCajaMensualUtils;
import gob.mined.siap2.business.utils.PACUtils;
import gob.mined.siap2.business.utils.POAConActividadesUtils;
import gob.mined.siap2.business.utils.POAConverter;
import gob.mined.siap2.business.utils.POAUtils;
import gob.mined.siap2.business.utils.UnidadTecnicaUtils;
import gob.mined.siap2.business.validaciones.ValidacionReprogramacion;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.ConMontoPorAnio;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.NotificacionPOA;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POActividad;
import gob.mined.siap2.entities.data.impl.POActividadAsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.Reprogramacion;
import gob.mined.siap2.entities.data.impl.ReprogramacionDetalle;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.entities.enums.EstadoPOA;
import gob.mined.siap2.entities.enums.EstadoReprogramacion;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.enums.TipoNotificacion;
import gob.mined.siap2.entities.enums.TipoPOAPAC;
import gob.mined.siap2.entities.enums.TipoReprogramacion;
import gob.mined.siap2.entities.tipos.FiltroMA;
import gob.mined.siap2.entities.tipos.FiltroReprogramacion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.PoaConActividadesDAO;
import gob.mined.siap2.persistence.dao.imp.ProcesoAdquisicionDAO;
import gob.mined.siap2.persistence.dao.imp.UsuarioDAO;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.BooleanUtils;

/**
 * Esta clase implementa los métodos de POA con actividades. Este POA aplica a
 * Accion Central y Asignacion No Programable
 *
 * @author Sofis Solutions
 */
@Stateless(name = "POAConActividadesBean")
@LocalBean
public class POAConActividadesBean {

    @Inject
    @JPADAO
    private UsuarioDAO usuarioDAO;
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private ProcesoAdquisicionDAO procesoAdqDAO;

    @Inject
    @JPADAO
    private PoaConActividadesDAO poaConActividadesDAO;
    @Inject
    private DatosUsuario datosUsuario;
    @Inject
    private UsuarioBean usuarioBean;
    @Inject
    private GeneralPOBean generalPOBean;
    @Inject
    private PACBean pacBean;
    @Inject
    private GeneralBean generalBean;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Método auxiliar que retorna el tipo de operación necesario a partir del
     * tipo de monto
     *
     * @param tipoMontoPorAnio
     * @return
     */
    private String getOperacion(TipoMontoPorAnio tipoMontoPorAnio) {
        switch (tipoMontoPorAnio) {
            case ACCION_CENTRAL:
                return ConstantesEstandares.Operaciones.OPERACION_PARA_VALIDAR_POA_ACCION_CENTRAL;
            case ASIGN_NO_PROGRAMABLE:
                return ConstantesEstandares.Operaciones.OPERACION_PARA_VALIDAR_POA_ASIGNACION_NO_PROGRAMABLE;
        }
        return "OPE_INEXISTENTE";
    }

    /**
     * operación que retorna si el usuario tiene permiso de edición sobre un poa
     *
     * @param poa
     * @param tipoMontoPorAnio
     * @return
     */
    public boolean tienePermisoEdicionPOAAccionCentral(POAConActividades poa) {
        // trae las ut del usuario
        if (poa.getEstado() == EstadoPOA.EDITANDO_UT) {
            boolean encontro = false;

            //las ut que tiene el usuario
            List<UnidadTecnica> utDelUsuario = usuarioBean.getUTDelUsuario();
            //chequea si tiene acceso
            if (UnidadTecnicaUtils.tieneAccesoAUT(utDelUsuario, poa.getUnidadTecnica())) {
                encontro = true;
            }

            return encontro;
        } else {

            //es presupuesto el que lo envia y el suauario tiene que tener la operacion de manear poa de accion central
            String codigoUsuario = datosUsuario.getCodigoUsuario();
            List l = usuarioDAO.getUsuariosConOperacion(getOperacion(poa.getConMontoPorAnio().getTipo()), codigoUsuario);
            if (l.isEmpty()) {
                return false;
            }
            return true;
        }
    }

    /**
     * Operación encargada de obtener un poa de trabajo. En caso de existir
     * devuelve el que existe, en caso contrario lo crea
     *
     * @param idConMontoPorAnio
     * @param idAnioFiscal
     * @param idUnidadTecnica
     * @return
     */
    public POAConActividades getPOATrabajo(Integer idConMontoPorAnio, Integer idAnioFiscal, Integer idUnidadTecnica) {
        try {
            POAConActividades poa;
            List<POAConActividades> poas = poaConActividadesDAO.getPOATrabajoConMontoPorAnio(idConMontoPorAnio, idAnioFiscal, idUnidadTecnica);
            if (poas.isEmpty()) {
                poa = crearPOATrabajo(idConMontoPorAnio, idAnioFiscal, idUnidadTecnica);
            } else {
                poa = poas.get(0);
            }
            POAConActividadesUtils.initPOA(poa);

            if (!tienePermisoEdicionPOAAccionCentral(poa)) {
                if (poa.getEstado() == EstadoPOA.EDITANDO_UT) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_NO_SE_ENCUENTRA_EN_ESTADO_ENVIADO_SOLO_PUEDE_SER_MODIFICADO_POR_UT);
                    throw b;
                } else {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_YA_FUE_ENVIADO_SOLO_PUEDE_SER_EDITADO_POR_PRESUPUESTO);
                    throw b;
                }
            }

            return poa;

        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
    }

    /**
     * operación que se encarga de crear un poa de la la línea de trabajo
     * realizando la mutuo exclusión
     *
     * @param idConMontoPorAnio
     * @param idAnioFiscal
     * @param idUnidadTecnica
     * @return
     */
    private POAConActividades crearPOATrabajo(Integer idConMontoPorAnio, Integer idAnioFiscal, Integer idUnidadTecnica) {
        try {
            //se bloquea para evitar la generacion de dos veces el mismo poa
            ConMontoPorAnio conMontoPorAnio = (ConMontoPorAnio) generalDAO.selectForUpdate(ConMontoPorAnio.class, idConMontoPorAnio);

            POAConActividades poa;
            List<POAConActividades> poas = poaConActividadesDAO.getPOATrabajoConMontoPorAnio(idConMontoPorAnio, idAnioFiscal, idUnidadTecnica);
            if (!poas.isEmpty()) {
                poa = poas.get(0);
                POAConActividadesUtils.initPOA(poa);
                return poa;
            }

            UnidadTecnica ut = (UnidadTecnica) generalDAO.find(UnidadTecnica.class, idUnidadTecnica);
            AnioFiscal anioFiscal = (AnioFiscal) generalDAO.find(AnioFiscal.class, idAnioFiscal);

            BigDecimal montoUT = POAUtils.getMonto(conMontoPorAnio, ut, anioFiscal);
            if (montoUT == null || montoUT.compareTo(BigDecimal.ZERO) == 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_LA_UT_NO_TIENE_MONTO_ASIGNADO_PARA_EL_POA);
                throw b;
            }
            poa = new POAConActividades();
            poa.setEstado(EstadoPOA.EDITANDO_UT);
            poa.setActividades(new LinkedList());
            poa.setAnioFiscal(anioFiscal);
            poa.setUnidadTecnica(ut);
            poa.setConMontoPorAnio(conMontoPorAnio);
            poa.setCierreAnual(Boolean.FALSE);

            poa = (POAConActividades) generalDAO.create(poa);
            conMontoPorAnio.getPoas().add(poa);
            return poa;

        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
    }

    /**
     * Método que guarda una actividad en un poa.
     *
     * @param idPOA
     * @param actividad
     * @return
     */
    public POActividadBase guardarActividad(Integer idPOA, POActividadBase actividad) {
        try {
            POAConActividades poa = (POAConActividades) generalDAO.find(POAConActividades.class, idPOA);
            if (!tienePermisoEdicionPOAAccionCentral(poa)) {
                if (poa.getEstado() == EstadoPOA.EDITANDO_UT) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_NO_SE_ENCUENTRA_EN_ESTADO_ENVIADO_SOLO_PUEDE_SER_MODIFICADO_POR_UT);
                    throw b;
                } else {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_YA_FUE_ENVIADO_SOLO_PUEDE_SER_EDITADO_POR_PRESUPUESTO);
                    throw b;
                }
            }

            if (!poa.getActividades().contains(actividad)) {
                poa.getActividades().add(actividad);
            } else {
                actividad = (POActividadBase) generalDAO.update(actividad);
            }
            return actividad;
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
     * Método que guarda un insumo en una actividad del POA con actividades.
     *
     * @param idPOA
     * @param idActividad
     * @param insumo
     * @return
     */
    public POInsumos guardarInsumo(Integer idPOA, Integer idActividad, POInsumos insumo) {
        try {

            POAConActividades poa = (POAConActividades) generalDAO.find(POAConActividades.class, idPOA);
            if (!tienePermisoEdicionPOAAccionCentral(poa)) {
                if (poa.getEstado() == EstadoPOA.EDITANDO_UT) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_NO_SE_ENCUENTRA_EN_ESTADO_ENVIADO_SOLO_PUEDE_SER_MODIFICADO_POR_UT);
                    throw b;
                } else {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_YA_FUE_ENVIADO_SOLO_PUEDE_SER_EDITADO_POR_PRESUPUESTO);
                    throw b;
                }
            }

            //se bloquea el poa para no pasarse en los montos
            generalDAO.lock(POAConActividades.class, idPOA);

            //se setea el monto de la fuente GOES, ya que los insumos de estos poas solo trabjan con goes como fuente por defecto
            generalPOBean.actualizarMontosGOES(insumo);

            POAConActividadesUtils.validateInsumo(insumo);

            if (insumo.getNoUACI() == null || !insumo.getNoUACI()) {
                insumo.setFechaRealInicioEjecucion(null);
                insumo.setDuracion(null);
            }

            POActividadBase actividad = POAConActividadesUtils.getActividadEnPOA(poa, idActividad);
            //se guarda el insumo modificado
            if (!actividad.getInsumos().contains(insumo)) {
                actividad.getInsumos().add(insumo);
            } else {
                insumo = (POInsumos) generalDAO.update(insumo);
            }

            //se valida el poa
            POAConActividadesUtils.validatePOA(poa.getConMontoPorAnio(), poa);
            //se actualiza su distribuccion opr fuente para todos sus insumos

            generalDAO.update(poa);

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
     * Método que se encarga de eliminar un insumo de una actividad de POA con
     * actividades
     *
     * @param idPOA
     * @param idActividad
     * @param insumo
     */
    public void eliminarInsumo(Integer idPOA, Integer idActividad, POInsumos insumo) {
        try {
            POAConActividades poa = (POAConActividades) generalDAO.find(POAConActividades.class, idPOA);

            if (!tienePermisoEdicionPOAAccionCentral(poa)) {
                if (poa.getEstado() == EstadoPOA.EDITANDO_UT) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_NO_SE_ENCUENTRA_EN_ESTADO_ENVIADO_SOLO_PUEDE_SER_MODIFICADO_POR_UT);
                    throw b;
                } else {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_YA_FUE_ENVIADO_SOLO_PUEDE_SER_EDITADO_POR_PRESUPUESTO);
                    throw b;
                }
            }
            POActividadBase actividad = (POActividadBase) generalDAO.find(POActividadBase.class, idActividad);
            actividad.getInsumos().remove(insumo);
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
     * Método que elimina una actividad de un POA con actividades.
     *
     * @param idPOA
     * @param actividad
     */
    public void eliminarActividad(Integer idPOA, POActividadBase actividad) {
        try {
            POAConActividades poa = (POAConActividades) generalDAO.find(POAConActividades.class, idPOA);

            if (!tienePermisoEdicionPOAAccionCentral(poa)) {
                if (poa.getEstado() == EstadoPOA.EDITANDO_UT) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_NO_SE_ENCUENTRA_EN_ESTADO_ENVIADO_SOLO_PUEDE_SER_MODIFICADO_POR_UT);
                    throw b;
                } else {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_YA_FUE_ENVIADO_SOLO_PUEDE_SER_EDITADO_POR_PRESUPUESTO);
                    throw b;
                }
            }

            poa.getActividades().remove(actividad);
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
     * Método que envía un poa con actividades para validar.
     *
     * @param idConMontoPorAnio
     * @param idPOA
     * @param tipoMontoPorAnio
     * @return
     */
    public POAConActividades enviarPOAConActividades(Integer idConMontoPorAnio, Integer idPOA, TipoMontoPorAnio tipoMontoPorAnio) {
        try {
            //si edita verifica que sea editable
            POAConActividades poa = (POAConActividades) generalDAO.find(POAConActividades.class, idPOA);
            if (!tienePermisoEdicionPOAAccionCentral(poa)) {
                if (poa.getEstado() == EstadoPOA.EDITANDO_UT) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_NO_SE_ENCUENTRA_EN_ESTADO_ENVIADO_SOLO_PUEDE_SER_MODIFICADO_POR_UT);
                    throw b;
                } else {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_POA_YA_FUE_ENVIADO_SOLO_PUEDE_SER_EDITADO_POR_PRESUPUESTO);
                    throw b;
                }
            }

            ConMontoPorAnio conMontoPorAnio = (ConMontoPorAnio) generalDAO.find(ConMontoPorAnio.class, idConMontoPorAnio);

            POAConActividadesUtils.validatePOAtoSend(conMontoPorAnio, poa);
            poa.setEstado(EstadoPOA.VALIDANDO_POA);

            TipoNotificacion tipoN;
            if (tipoMontoPorAnio == TipoMontoPorAnio.ACCION_CENTRAL) {
                tipoN = TipoNotificacion.NUEVO_POA_ACCION_CENTRAL_PARA_VALIDAR;
            } else {
                tipoN = TipoNotificacion.NUEVO_POA_ASIGNACION_NO_PROGRAMABLE_PARA_VALIDAR;
            }
            String contenidoNotificacion = "UT: " + poa.getUnidadTecnica().getNombre() + ", POA: " + conMontoPorAnio.getNombre();

            List<SsUsuario> anotificar = usuarioDAO.getUsuariosConOperacion(getOperacion(tipoMontoPorAnio), null);
            for (SsUsuario u : anotificar) {
                NotificacionPOA n = new NotificacionPOA();
                n.setTipo(tipoN);
                n.setUsuario(u);
                n.setObjetoId(conMontoPorAnio.getId());
                n.setContenido(contenidoNotificacion);

                n.setPoaAnio(poa.getAnioFiscal().getId());
                n.setPoaUT(poa.getUnidadTecnica().getId());
                n.setFecha(new Date());
                generalDAO.update(n);
            }

            poa = (POAConActividades) generalDAO.update(poa);

            return poa;

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
     * Método que se encarga de generar una línea base a partir de un POA con
     * actividades.
     *
     * @param idPoa
     */
    public void generarLineaBase(Integer idPoa) {
        try {
            POAConActividades poa = (POAConActividades) generalDAO.find(POAConActividades.class, idPoa);
            generarLineaBase(poa);
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
     * Método que se encarga de generar una línea base a partir de un POA con
     * actividades.
     *
     * @param poa
     * @throws Exception
     */
    public void generarLineaBase(POAConActividades poa) throws Exception {
        ConMontoPorAnio objeto = poa.getConMontoPorAnio();

        if (!tienePermisoEdicionPOAAccionCentral(poa)) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_NO_TIENE_PERMISO_PARA_ACCCION_SOLICITADA);
            throw b;
        }

        POAConverter converter = new POAConverter();
        POAConActividades base = converter.convertPOAConActividades(poa);
        generalDAO.create(base);
        objeto.getPoas().add(base);

        TipoNotificacion tipoN;
        if (objeto.getTipo() == TipoMontoPorAnio.ACCION_CENTRAL) {
            tipoN = TipoNotificacion.POA_ACCION_CENTRAL_FUE_VALIDADO;
        } else {
            tipoN = TipoNotificacion.POA_ASIGNACION_NO_PROGRAMABLE_FUE_VALIDADO;
        }

        String contenidoNotificacion = "UT: " + poa.getUnidadTecnica().getNombre() + ", POA: " + poa.getConMontoPorAnio().getNombre();
        List<SsUsuario> anotificar = usuarioDAO.getUsuariosConUT(poa.getUnidadTecnica().getId(), null);
        for (SsUsuario u : anotificar) {
            NotificacionPOA n = new NotificacionPOA();
            n.setTipo(tipoN);
            n.setContenido(contenidoNotificacion);
            n.setUsuario(u);
            n.setObjetoId(objeto.getId());

            n.setPoaAnio(poa.getAnioFiscal().getId());
            n.setPoaUT(poa.getUnidadTecnica().getId());
            n.setFecha(new Date());

            generalDAO.update(n);
        }

    }

    /**
     * Método que verifica si es necesaria la creación de un PAC.
     *
     * @param listPoa
     * @return
     */
    public boolean esNecesarioGenerarPAC(List<POAConActividades> listPoa) {
        for (POAConActividades poa : listPoa) {
            for (POActividadBase a : poa.getActividades()) {
                for (POInsumos i : a.getInsumos()) {
                    if (i.getNoUACI() == null || !i.getNoUACI()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Método que se encarga de rechazar un POA
     *
     * @param idPoa
     * @param idAccionCentral
     * @param motivoRechazo
     * @param tipoMontoPorAnio
     */
    public void rechazarPOA(Integer idPoa, Integer idAccionCentral, String motivoRechazo, TipoMontoPorAnio tipoMontoPorAnio) {
        try {
            POAConActividades poa = (POAConActividades) generalDAO.find(POAConActividades.class, idPoa);
            ConMontoPorAnio objeto = (ConMontoPorAnio) generalDAO.find(ConMontoPorAnio.class, idAccionCentral);

            if (poa.getEstado() != EstadoPOA.VALIDANDO_POA || !tienePermisoEdicionPOAAccionCentral(poa)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_POA_PARA_GENERAR_LINEA_BASE_EL_POA_TIENE_QUE_ENVIARSE_Y_SER_PRESUPUESTO);
                throw b;
            }

            poa.setEstado(EstadoPOA.EDITANDO_UT);
            TipoNotificacion tipoN;
            if (tipoMontoPorAnio == TipoMontoPorAnio.ACCION_CENTRAL) {
                tipoN = TipoNotificacion.POA_ACCION_CENTRAL_FUE_RECHAZADO;
            } else {
                tipoN = TipoNotificacion.POA_ASIGNACION_NO_PROGRAMABLE_FUE_RECHAZADO;
            }

            List<SsUsuario> anotificar = usuarioDAO.getUsuariosConUT(poa.getUnidadTecnica().getId(), null);
            for (SsUsuario u : anotificar) {
                NotificacionPOA n = new NotificacionPOA();
                n.setTipo(tipoN);
                n.setContenido(motivoRechazo);
                n.setUsuario(u);
                n.setObjetoId(objeto.getId());

                n.setPoaAnio(poa.getAnioFiscal().getId());
                n.setPoaUT(poa.getUnidadTecnica().getId());
                n.setFecha(new Date());

                generalDAO.update(n);
            }

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
     * Método encargado de validar un POA con actividades y dejarlo en estado de
     * consolidación.
     *
     * @param idPoa
     */
    public void validarPOA(Integer idPoa) {
        try {
            POAConActividades poa = (POAConActividades) generalDAO.find(POAConActividades.class, idPoa);
            generarLineaBase(idPoa);
            poa.setEstado(EstadoPOA.CONSOLIDANDO_POA);
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
     * Método que retorna los poas en los que no se han generado lineas bases
     *
     * @param idConMontoPorAnio
     * @param idAnioFiscal
     * @param idUnidadTecnica
     * @return
     */
    public Map<String, Integer> getPOASEnLineaBase(Integer idConMontoPorAnio, Integer idAnioFiscal, Integer idUnidadTecnica) {
        try {

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

            Map<String, Integer> m = new LinkedHashMap();
            List<POAConActividades> l = poaConActividadesDAO.getPOASBase(idConMontoPorAnio, idAnioFiscal, idUnidadTecnica);
            for (POAConActividades poa : l) {
                m.put(df.format(poa.getFechaFijacion()), poa.getId());
            }
            return m;
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

    public POAConActividades getPOAEnLineaBase(Integer idPOA) {
        try {
            POAConActividades poa = (POAConActividades) generalDAO.find(POAConActividades.class, idPOA);
            POAConActividadesUtils.initPOA(poa);
            return poa;
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
     * Retorna los POA de trabajo para una Acción central y año fiscal.
     *
     * @param idAccionCentral
     * @param idAnioFiscal
     * @return List
     */
    public List<POAConActividades> getPOAsTrabajo(Integer idConMontoPorAnio, Integer idAnioFiscal, EstadoPOA estado, TipoMontoPorAnio tipo) {
        List<POAConActividades> poas = poaConActividadesDAO.getPOAsTrabajo(idConMontoPorAnio, idAnioFiscal, estado, tipo);
        List<POAConActividades> result = new ArrayList<>();
        if (poas != null) {
            for (POAConActividades poa : poas) {
                if (poa != null) {
                    POAConActividadesUtils.initPOA(poa);
                    poa.getUnidadTecnica().getNombre();
                    result.add(poa);
                }
            }
        }
        return result;
    }

    /**
     * Retorna todos los poas consolidados
     *
     * @param filtrarUT
     * @param utAfiltrar
     * @param idAnioFiscal
     * @param tipo
     * @return
     */
    public List<POAConActividades> getPOAsParaReprogramacion(boolean filtrarUT, List<UnidadTecnica> utAfiltrar, Integer idAnioFiscal, TipoMontoPorAnio tipo) {
        List<POAConActividades> poas = poaConActividadesDAO.getPOAsParaReprogramacion(filtrarUT, utAfiltrar, idAnioFiscal, tipo);
        List<POAConActividades> result = new ArrayList<>();
        if (poas != null) {
            for (POAConActividades poa : poas) {
                if (poa != null) {
                    POAConActividadesUtils.initPOA(poa);
                    poa.getUnidadTecnica().getNombre();
                    result.add(poa);
                }
            }
        }
        return result;

    }

    public List<POAConActividades> getPOAsTrabajo(Integer idConMontoPorAnio, Integer idAnioFiscal, EstadoPOA estado) {
        return getPOAsTrabajo(idConMontoPorAnio, idAnioFiscal, estado, null);
    }

    /**
     * Retorna una lista de las últimas línea base de cada POA de acción central
     * y año fiscal.
     *
     * @param idAccionCentral
     * @param idAnioFiscal
     * @return List
     */
    public List<POAConActividades> getPOALineaBase(Integer idConMontoPorAnio, Integer idAnioFiscal) {
        List<POAConActividades> poas = poaConActividadesDAO.getPOALineaBase(idConMontoPorAnio, idAnioFiscal);
        List<POAConActividades> result = new ArrayList<>();
        if (poas != null) {
            for (POAConActividades poa : poas) {
                if (poa != null) {
                    POAConActividadesUtils.initPOA(poa);
                    poa.getUnidadTecnica().getNombre();
                    result.add(poa);
                }
            }
        }
        return result;
    }

    /**
     * Retorna true si todos los POA para la acción central y el año fiscal
     * están validados para generar el consolidado.
     *
     * @param idAccionCentral
     * @param idAnioFiscal
     * @return boolean
     */
    public boolean isCompletoParaConsolidado(Integer idAccionCentral, Integer idAnioFiscal) {
        return poaConActividadesDAO.isCompletoParaConsolidado(idAccionCentral, idAnioFiscal);
    }

    public void consolidar(List<POAConActividades> listPoa, Integer idConMontoPorAnio) {
        try {
            if (listPoa != null) {
                boolean esNecesarioPAC = esNecesarioGenerarPAC(listPoa);
                List<GeneralPOA> paos = new LinkedList();

                for (POAConActividades cadaPoa : listPoa) {
                    POAConActividadesUtils.validatePOA(cadaPoa.getConMontoPorAnio(), cadaPoa);

                    cadaPoa.setEstado(EstadoPOA.TERMINO_CONSOLIDACION);
                    cadaPoa = (POAConActividades) generalDAO.update(cadaPoa);

                    for (POActividadBase actividad : cadaPoa.getActividades()) {
                        for (POInsumos insumo : actividad.getInsumos()) {
                            insumo.setMontoPepOriginal(insumo.getMontoTotal());
                        }
                    }

                    //se genera la línea base
                    generarLineaBase(cadaPoa.getId());

                    if (esNecesarioPAC) {
                        paos.add(cadaPoa);
                    }
                }
                if (esNecesarioPAC) {
                    PAC pac = null;
                    if (!paos.isEmpty()) {
                        Integer anio = ((POAConActividades) paos.get(0)).getAnioFiscal().getAnio();
                        pac = PACUtils.crearPAC(paos, anio, TipoPOAPAC.POA_ACCION_CENTRAL);
                        pac = (PAC) generalDAO.update(pac);
                    }
                    ConMontoPorAnio o = (ConMontoPorAnio) generalDAO.find(ConMontoPorAnio.class, idConMontoPorAnio);
                    o.setPac(pac);
                }
            }
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

    public Collection<ConMontoPorAnio> obtenerConMontoPorAnioPorFiltro(FiltroMA filtro) {
        Collection<ConMontoPorAnio> respuesta = new ArrayList();
        if (filtro != null) {
            respuesta = poaConActividadesDAO.obtenerConMontoPorAnioPorFiltro(filtro);
        }
        return respuesta;
    }

    /**
     * Almacenamiento de una reprogramación de acción central
     *
     * @param reprog
     * @return reprog actualizada
     */
    public Reprogramacion guardarReprogramacion(Reprogramacion reprog) {

        if (reprog != null) {
            //TODO Validacion
            return poaConActividadesDAO.guardarReprogramacion(reprog);
        }
        return null;
    }

    public ReprogramacionDetalle guardarReprogramacionDetalle(ReprogramacionDetalle reprog) {
        if (reprog != null) {
            return poaConActividadesDAO.guardarReprogramacionDetalle(reprog);
        }
        return null;
    }

    public Reprogramacion enviarReprogramacion(Reprogramacion reprog) {
        if (reprog != null) {
            if (afectaPAC(reprog)) {
                reprog.setEstado(EstadoReprogramacion.PENDIENTE_PAC);
            } else {
                //TODO Impactar PAC
                reprog.setEstado(EstadoReprogramacion.APROBADO);
            }
            reprog = poaConActividadesDAO.guardarReprogramacion(reprog);
        }
        return reprog;
    }

    public Reprogramacion rechazarEnPac(Reprogramacion reprog) {
        if (reprog != null) {
            reprog.setEstado(EstadoReprogramacion.FORMULACION);
            reprog = poaConActividadesDAO.guardarReprogramacion(reprog);
        }
        return reprog;
    }

    public Reprogramacion aprobarEnPac(Reprogramacion reprog) throws BusinessException {
        if (reprog != null) {
            ValidacionReprogramacion.validar(reprog);
            reprog.setEstado(EstadoReprogramacion.APROBADO);
            reprog = poaConActividadesDAO.guardarReprogramacion(reprog);
        }
        return reprog;
    }

    private boolean afectaPAC(Reprogramacion reprog) {
        boolean respuesta = false;
        if (reprog != null) {
            for (ReprogramacionDetalle rep : reprog.getRep_detalle_lista()) {
                if (!BooleanUtils.isTrue(rep.getInsumoNuevoNoUaci())) {
                    respuesta = true;
                    break;
                } else {
                    if (!rep.getPoaInsumo().getNoUACI()) {
                        respuesta = true;
                        break;
                    }
                }
            }
        }
        return respuesta;
    }

    public Collection<Reprogramacion> obtenerReprogramacionesPorFiltro(FiltroReprogramacion filtro) {
        return poaConActividadesDAO.obtenerReprogramacionesPorFiltro(filtro);
    }

    public Collection<PACGrupo> obtenerGruposPorPOAId(Integer id) {
        return poaConActividadesDAO.obtenerGruposPACPorPOAId(id);
    }

    /**
     * método encargado de aplicar una reprogramación. impactar los datos en el
     * poa y en el pac
     *
     * @param idReprogramacion
     */
    public Reprogramacion aplicarReprogramacionPOAConActividades(Integer idReprogramacion) {
        try {
            Reprogramacion reprogramacion = (Reprogramacion) generalDAO.find(Reprogramacion.class, idReprogramacion);
            Set<POAConActividades> poasModificados = new LinkedHashSet();

            for (ReprogramacionDetalle detalle : reprogramacion.getRep_detalle_lista()) {

                if (detalle.getPoa() == null) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EXISTE_DETALLE_EN_REPROGRAMACION_NO_ASOCIADO_A_NINGUN_POA);
                    throw b;
                }

                POAConActividades poa = (POAConActividades) detalle.getPoa();

                POActividadBase actividadSelecionada = detalle.getPoaActividad();
                //si la actividad dodne va a añadir el insumo no existe, entonces crea una actividad nueva
                if (actividadSelecionada == null || (detalle.getNuevaActividad() != null && detalle.getNuevaActividad().equals(Boolean.TRUE))) {
                    if (reprogramacion.getTipoReprogramacion() == TipoReprogramacion.ASIGNACION_NO_PROGRAMABLE) {
                        POActividadAsignacionNoProgramable nuevaActividad = new POActividadAsignacionNoProgramable();
                        nuevaActividad.setNombre(detalle.getActividadNuevaAccionCentral());

                        actividadSelecionada = nuevaActividad;

                    } else if (reprogramacion.getTipoReprogramacion() == TipoReprogramacion.ACCION_CENTRAL) {
                        POActividad nuevaActividad = new POActividad();
                        nuevaActividad.setNombre(detalle.getActividadNuevaAccionCentral());

                        actividadSelecionada = nuevaActividad;

                    }
                    actividadSelecionada.setEstado(EstadoActividadPOA.NO_FINALIZADA);
                    actividadSelecionada.setInsumos(new LinkedList());

                    //se añade la actividad al poa
                    poa.getActividades().add(actividadSelecionada);
                }

                //se busca el insumo con el cual se va a trabajar
                POInsumos insumoSelecionado = detalle.getPoaInsumo();
                if (insumoSelecionado == null) {
                    insumoSelecionado = new POInsumos();
                    insumoSelecionado.setMontosFuentes(new LinkedList());
                    insumoSelecionado.setFlujosDeCajaAnio(new LinkedHashSet());
                    insumoSelecionado.setNoUACI(false);
                    insumoSelecionado.setEnviadoParaCertificar(false);
                    insumoSelecionado.setPasoValidacionCertificadoDeDispPresupuestaria(false);
                    insumoSelecionado.setPoa(detalle.getPoa());
                    insumoSelecionado.setUnidadTecnica(detalle.getPoa().getUnidadTecnica());

                    //se setea el monto para goes
                    generalPOBean.actualizarMontosGOES(insumoSelecionado);

                    //se añade el insumo a la actividad
                    actividadSelecionada.getInsumos().add(insumoSelecionado);
                    insumoSelecionado.setActividad(actividadSelecionada);
                } else {
                    pacBean.calcularDesasociarInsumo(insumoSelecionado);
                }

                //se actualiza la informacion del insumo
                insumoSelecionado.setFechaContratacion(detalle.getInsumoNuevoFechaContratacion());
                insumoSelecionado.setObservacion(detalle.getInsumoNuevoObservaciones());
                insumoSelecionado.setInsumo(detalle.getInsumoNuevo());
                insumoSelecionado.setNoUACI(detalle.getInsumoNuevoNoUaci());
                insumoSelecionado.setCantidad(detalle.getInsumoNuevoCantidad());
                insumoSelecionado.setMontoUnit(detalle.getInsumoNuevoUnitario());
                insumoSelecionado.setMontoTotal(detalle.getInsumoNuevoTotal());
                insumoSelecionado.setUnidadTecnica(detalle.getPoa().getUnidadTecnica());

                //se seta el monto del insumo en el monto de la fuente
                insumoSelecionado.getMontosFuentes().get(0).setMonto(insumoSelecionado.getMontoTotal());

                //se actualiza la programacion de pagos para el insumo                
                Set<FlujoCajaAnio> flujos = new LinkedHashSet();
                FlujoCajaAnio fca = FlujoCajaMensualUtils.generarFCM(detalle.getPoa().getAnioFiscal().getAnio());
                flujos.add(fca);
                for (int mes = 0; mes < fca.getFlujoCajaMenusal().size(); mes++) {
                    POFlujoCajaMenusal fcm = fca.getFlujoCajaMenusal().get(mes);
                    switch (mes) {
                        case 0:
                            fcm.setMonto(detalle.getRdeMes1());
                            break;
                        case 1:
                            fcm.setMonto(detalle.getRdeMes2());
                            break;
                        case 2:
                            fcm.setMonto(detalle.getRdeMes3());
                            break;
                        case 3:
                            fcm.setMonto(detalle.getRdeMes4());
                            break;
                        case 4:
                            fcm.setMonto(detalle.getRdeMes5());
                            break;
                        case 5:
                            fcm.setMonto(detalle.getRdeMes6());
                            break;
                        case 6:
                            fcm.setMonto(detalle.getRdeMes7());
                            break;
                        case 7:
                            fcm.setMonto(detalle.getRdeMes8());
                            break;
                        case 8:
                            fcm.setMonto(detalle.getRdeMes9());
                            break;
                        case 9:
                            fcm.setMonto(detalle.getRdeMes10());
                            break;
                        case 10:
                            fcm.setMonto(detalle.getRdeMes11());
                            break;
                        case 11:
                            fcm.setMonto(detalle.getRdeMes12());
                            break;
                    }
                }
                insumoSelecionado.setFlujosDeCajaAnio(flujos);

                //se añade el insumo al grupo de pac
                if (detalle.getGrupoPAC() != null) {
                    pacBean.calcularAsociarInsumo(insumoSelecionado, detalle.getGrupoPAC());
                }

                //se añade el poa  la lista d emodificados
                poasModificados.add(poa);
            }

            //se genera una nueva línea base para los poas modificados
            for (POAConActividades poa : poasModificados) {
                generarLineaBase(poa);
            }

            reprogramacion.setEstado(EstadoReprogramacion.APLICADA);
            return reprogramacion;
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
     * Verifica que para determinado POA, todas las actividades estén
     * completadas o deshabilitadas, en caso contrario, existen actividades
     * pendientes
     *
     * @param idPOA
     * @return
     */
    public Boolean existenActividadesPendientesEnPOA(Integer idPOA) {
        try {
            POAConActividades poaConAct = (POAConActividades) generalDAO.find(POAConActividades.class, idPOA);
            Boolean hayActividadPendiente = false;
            Iterator<POActividadBase> itActividades = poaConAct.getActividades().iterator();
            while (itActividades.hasNext() && !hayActividadPendiente) {
                POActividadBase actividad = itActividades.next();
                if (actividad.getEstado() == null || actividad.getEstado().equals(EstadoActividadPOA.NO_FINALIZADA)) {
                    hayActividadPendiente = true;
                }
            }
            return hayActividadPendiente;
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
     * Verifica que para determinado POA, todos los insumos adquiridos,
     * deshabilitados, cancelados o declarados como desiertos, en caso
     * contrario, existen insumos pendientes
     *
     * @param idPOA
     * @return
     */
    public Boolean existenInsumosPendientesEnPOA(Integer idPOA) {
        try {
            POAConActividades poaConAct = (POAConActividades) generalDAO.find(POAConActividades.class, idPOA);
            Boolean hayInsumoPendiente = false;
            Iterator<POActividadBase> itActividades = poaConAct.getActividades().iterator();
            while (itActividades.hasNext() && !hayInsumoPendiente) {
                POActividadBase actividad = itActividades.next();
                Iterator<POInsumos> itPoInsumos = actividad.getInsumos().iterator();
                while (itPoInsumos.hasNext() && !hayInsumoPendiente) {
                    POInsumos poInsumo = itPoInsumos.next();
                    /**
                     * Formas de que el insumo este "pendiente" 1) No esté
                     * asociado a un proceso de adquisición 2) Este asociado a
                     * un proceso de adquisición pero una parte no esta asociada
                     * a un item 3) Esta asociado a items, pero dicho item no
                     * tiene un estado asociado
                     */

                    //verifica que el insumo este en algun proceso de adquisicion
                    if (poInsumo.getNoUACI() != null && !poInsumo.getNoUACI()) {
                        if (!procesoAdqDAO.insumoEstaEnProceso(poInsumo.getId())) {
                            hayInsumoPendiente = true;
                        }
                    }
                }
            }
            return hayInsumoPendiente;
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
     * Cierra el POA para el año seleccionado
     *
     * @param idPOA
     * @return
     */
    public void confirmarCierreAnual(Integer idConMontoPorAnio, Integer idAnioFiscal) {
        try {
            Iterator<POAConActividades> itListaPOAProy = poaConActividadesDAO.getPOAsTrabajo(idConMontoPorAnio, idAnioFiscal, null, null).iterator();
            while (itListaPOAProy.hasNext()) {
                POAConActividades poaConAct = itListaPOAProy.next();
                BusinessException b = new BusinessException();
                if (poaConAct.getEstado() != EstadoPOA.TERMINO_CONSOLIDACION) {
                    b.addError(ConstantesErrores.ERR_HAY_POAS_SIN_CONSOLIDAR);
                }
                if (existenActividadesPendientesEnPOA(poaConAct.getId())) {
                    b.addError(ConstantesErrores.ERR_CIERRE_ANUAL_POA_POR_ACTIVIDADES_SIN_FINALIZAR);
                }
                if (existenInsumosPendientesEnPOA(poaConAct.getId())) {
                    b.addError(ConstantesErrores.ERR_CIERRE_ANUAL_POA_POR_INSUMOS_PENDIENTES);
                }
                if (!generalBean.anioSeleccionadoEstaFinalizado(poaConAct.getAnioFiscal().getId())) {
                    b.addError(ConstantesErrores.ERR_CIERRE_ANUAL_ANIO_FISCAL_NO_FINALIZADO);
                }
                if (!b.getErrores().isEmpty()) {
                    throw b;
                }
                poaConAct.setCierreAnual(true);
            }
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
