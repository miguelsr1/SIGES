/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.business.ejbs.impl.validacionesPresupuestales.POAValidaciones;
import gob.mined.siap2.business.utils.POAConverter;
import gob.mined.siap2.business.utils.POGUtils;
import gob.mined.siap2.business.utils.POProyectoUtils;
import gob.mined.siap2.business.utils.ProyectoUtils;
import gob.mined.siap2.business.utils.ProyectoValidacion;
import gob.mined.siap2.business.utils.TipoSeguimientoUtils;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.ComboValorSeguimiento;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POActividadProyecto;
import gob.mined.siap2.entities.data.impl.POGActividadProyecto;
import gob.mined.siap2.entities.data.impl.POGInsumo;
import gob.mined.siap2.entities.data.impl.POGInsumoAnio;
import gob.mined.siap2.entities.data.impl.POGMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.POGProyecto;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoAporte;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTramoTramo;
import gob.mined.siap2.entities.data.impl.ProyectoCategoriaConvenio;
import gob.mined.siap2.entities.data.impl.ProyectoComponente;
import gob.mined.siap2.entities.data.impl.ProyectoDocumentos;
import gob.mined.siap2.entities.data.impl.ProyectoEstPorcentajeFuente;
import gob.mined.siap2.entities.data.impl.ProyectoEstProducto;
import gob.mined.siap2.entities.data.impl.ProyectoEstructura;
import gob.mined.siap2.entities.data.impl.ProyectoMacroActividad;
import gob.mined.siap2.entities.data.impl.ProyectoParipassuTramo;
import gob.mined.siap2.entities.data.impl.ProyectoProrroga;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.data.impl.ValoresSeguimiento;
import gob.mined.siap2.entities.enums.EstadoPOA;
import gob.mined.siap2.entities.enums.EstadoPOGProyecto;
import gob.mined.siap2.entities.enums.EstadoProyecto;
import gob.mined.siap2.entities.enums.TipoEstructura;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.entities.tipos.FiltroRiesgo;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.POADAO;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.ws.to.RiesgoTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos de servicio correspondientes a proyectos.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "ProyectoBean")
@LocalBean
public class ProyectoBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Inject
    private GeneralBean generalBean;
    @Inject
    private GeneralPOBean generalPOBean;
    @Inject
    private ArchivoBean archivoBean;
    @Inject
    private POAValidaciones pOAValidaciones;

    @Inject
    @JPADAO
    private POADAO poadao;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * guarda o actualiza un proyecto
     *
     * @param proyecto objeto a guardar
     * @param paso el paso en que esta guardando (va aumentando de pasos hata
     * que lo guarda todo)
     * @return
     */
    public Proyecto crearOActualizarProyecto(Proyecto proyecto, Integer paso) {
        try {

            //se  crean los archivos para los documentos que no los tienen
            //se asocia los archivos con los documentos
            for (ProyectoDocumentos pd : proyecto.getProyectoDocumentos()) {
                if (pd.getTempUploadedFile() != null) {
                    if (pd.getArchivo() == null) {
                        pd.setArchivo(archivoBean.crearArchivo());
                    }
                }
            }

            //vacia la estructura en caso que se cambie
            if (Boolean.FALSE.equals(proyecto.getTipoEstructuraComponente())) {
                proyecto.setProyectoComponentes(new LinkedList());
            }
            if (Boolean.FALSE.equals(proyecto.getTipoEstructuraMacroactividad())) {
                proyecto.setProyectoMacroactividad(new LinkedList());
            }

            //se regeneran las cosas autogeneradas
            ProyectoUtils.recalcularTodasCategorias(proyecto);
            ProyectoUtils.regenerarAportesProyecto(proyecto);
            ProyectoUtils.regenerarMontosEstructura(proyecto);

            //valida datos generales
            if (proyecto.getPasoActual() >= 0) {
                generalBean.chequearIgualNombre(Proyecto.class, proyecto.getId(), proyecto.getNombre());
                ProyectoValidacion.validarDatosGenerales(proyecto);
            }

            //valida financiacion
            if (proyecto.getPasoActual() >= 1) {
                ProyectoValidacion.validarFinanciacion(proyecto);
            }

            //valida estructrura
            if (proyecto.getPasoActual() >= 2) {
                ProyectoValidacion.validarEstructura(proyecto);
            }

            //se guardan los archivos en disco
            for (ProyectoDocumentos pd : proyecto.getProyectoDocumentos()) {
                if (pd.getTempUploadedFile() != null) {
                    if (pd.getArchivo() == null) {
                        pd.setArchivo(archivoBean.crearArchivo());
                    }
                    archivoBean.asociarArchivo(pd.getArchivo(), pd.getTempUploadedFile(), false);
                }
            }

            if (proyecto.getPasoActual().intValue() <= 5) {
                if (proyecto.getPasoActual().equals(paso)) {
                    proyecto.setPasoActual(proyecto.getPasoActual() + 1);
                }
            }

            proyecto = (Proyecto) generalDAO.update(proyecto);
            return proyecto;
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
     * Este método encargado de guardar una prorroga a un proyecto
     *
     * @param idProyecto
     * @param prorroga
     * @return
     */
    public Proyecto guardarProrroga(Integer idProyecto, ProyectoProrroga prorroga) {
        try {
            Proyecto p = (Proyecto) generalDAO.find(Proyecto.class, idProyecto);
            if (prorroga.getProrroga() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_FECHA_DE_PRORROGA_VACIA);
                throw b;
            }

            if (prorroga.getProrroga().compareTo(p.getFin()) < 0) {
                //se esta achicando la duracion del proyecto. Se eliminan los POAs existentes
                int anioProrroga = DatesUtils.getYearOfDate(prorroga.getProrroga());
                int anioProyecto = DatesUtils.getYearOfDate(p.getFin());

                while (anioProrroga <= anioProyecto) {
                    List<AnioFiscal> consultaAniosFiscales = generalDAO.findByOneProperty(AnioFiscal.class, "anio", anioProrroga);
                    if (!consultaAniosFiscales.isEmpty()) {
                        AnioFiscal anioFiscal = consultaAniosFiscales.get(0);
                        List<POAProyecto> poas = poadao.getPOASTrabajoProyecto(p.getId(), anioFiscal.getId());
                        for (POAProyecto poa : poas) {
                            for (POLinea linea : poa.getLineas()) {
                                if (linea.getActividades().isEmpty()) {
                                    String[] params = {poa.getUnidadTecnica().getNombre(), poa.getAnioFiscal().getAnio().toString()};
                                    BusinessException b = new BusinessException();
                                    b.addError(ConstantesErrores.ERR_EXISTE_UN_POA_DE_LA_UT_0_PARA_EL_ANIO_1_CON_DATOS, params);
                                    throw b;
                                }
                            }
                            generalDAO.delete(poa);
                        }
                    }
                    anioProrroga++;
                }

            }

            p.setFin(prorroga.getProrroga());
            p.getProrrogas().add(prorroga);
            prorroga.setProyecto(p);
            p.getProrrogas().add(prorroga);

            return p;
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
     * Este método que se encarga de guardar un documento en un proyecto
     *
     * @param idProyecto
     * @param pd
     * @return
     */
    public ProyectoDocumentos guardarDocumento(Integer idProyecto, ProyectoDocumentos pd) {
        try {
            Proyecto p = (Proyecto) generalDAO.find(Proyecto.class, idProyecto);
            if (pd.getTempUploadedFile() != null) {
                if (pd.getArchivo() == null) {
                    pd.setArchivo(archivoBean.crearArchivo());
                }
                archivoBean.asociarArchivo(pd.getArchivo(), pd.getTempUploadedFile(), false);
            }

            if (!p.getProyectoDocumentos().contains(pd)) {
                pd.setProyecto(p);
                p.getProyectoDocumentos().add(pd);
            } else {
                pd = (ProyectoDocumentos) generalDAO.update(pd);
            }

            return pd;
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
     * Retorna un proyecto con todas sus relaciones inicializadas
     *
     * @param id
     * @return
     */
    public Proyecto getProyecto(Integer id) {
        try {

            Proyecto p = (Proyecto) generalDAO.find(Proyecto.class, id);

            p.getProrrogas().isEmpty();
            p.getProyectoDesembolso().isEmpty();
            p.getProyectoEnmienda().isEmpty();
            p.getProyectoCoEjecutoras().isEmpty();
            p.getIndicadoresAsociados().isEmpty();
            for (ProyectoCategoriaConvenio proyectoCategoriaConvenio : p.getDistribuccionCategorias()) {
                for (ProyectoAporteTramoTramo tramo : proyectoCategoriaConvenio.getTramos()) {
                    tramo.getParipassus().isEmpty();
                }
            }

            p.getProyectoDocumentos().isEmpty();
            for (ProyectoComponente pc : p.getProyectoComponentes()) {
                initComponentes(pc);
            }
            for (ProyectoMacroActividad pm : p.getProyectoMacroactividad()) {
                pm.getMontosFuentes().isEmpty();
            }
            return p;
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

    private void initComponentes(ProyectoComponente pc) {
        pc.getMontosFuentes().isEmpty();
        for (ProyectoComponente hija : pc.getComponenteHijos()) {
            initComponentes(hija);
        }
    }

    /**
     * elimina un proyecto
     *
     * @param idProyecto
     */
    public void eleiminarProyecto(Integer idProyecto) {
        try {
            Proyecto p = (Proyecto) generalDAO.find(Proyecto.class, idProyecto);
            if (p.getPog() != null) {
                if (!p.getPog().getLineas().isEmpty()) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EL_PROYECTO_TIENE_POG_ASOCIADO);
                    throw b;
                }
            }

            if (!p.getProrrogas().isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {"prorroga", "el proyecto"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }
            if (!p.getAportesProyecto().isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {"fuentes", "el proyecto"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }
            if (!p.getProyectoDesembolso().isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {"desembolso", "el proyecto"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }
            if (!p.getProyectoEnmienda().isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {"enmienda", "el proyecto"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }
            if (!p.getProyectoCoEjecutoras().isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {"co-ejecutora", "el proyecto"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }
            if (!p.getProyectoDocumentos().isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {"documentos", "el proyecto"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }
            if (!p.getProyectoComponentes().isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {"componentes", "el proyecto"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }
            if (!p.getProyectoMacroactividad().isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {"macroactividad", "el proyecto"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }

            generalDAO.delete(p);

        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA_SIN_PARAMETROS);
            throw b;
        }
    }

    /**
     * retorna un proyecto inicializado para el POG, con su POG inicializado
     *
     * @param id
     * @return
     */
    public Proyecto getProyectoForPOG(Integer id) {
        try {
            Proyecto p = this.getProyecto(id);

            for (ProyectoComponente iter : p.getProyectoComponentes()) {
                iter.getComponenteHijos().isEmpty();
                iter.getProductos().isEmpty();
            }
            for (ProyectoMacroActividad iter : p.getProyectoMacroactividad()) {
                iter.getProductos().isEmpty();
            }

            if (p.getPog() != null) {
                POProyectoUtils.initPOG(p.getPog());
            } else {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.PROYECTO_NO_TIENE_POG_DEBE_CERRARLO);
                throw b;
            }

            return p;
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
     * verifica si el POG de un proyecto es editable
     *
     * @param idProyecto
     */
    private void verificarSiPOGEditable(Integer idProyecto, POGActividadProyecto actividad) {
        Proyecto proyecto = (Proyecto) generalDAO.find(Proyecto.class, idProyecto);
        if (!proyecto.getTienePOG().equals(Boolean.TRUE)) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EL_PROYECTO_NO_LLEVA_POG);
            throw b;
        }
        if (proyecto.getEstado() != EstadoProyecto.BLOQUEADO) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_PARA_CREAR_POG_EL_PROYECTO_DEBE_ESTAR_BLOQUEADO);
            throw b;
        }

        if (actividad != null) {
            AnioFiscal anioEjecucion = generalPOBean.getMenorAnioEjecucion();
            if (!POGUtils.habilitadoActividad(proyecto.getPog(), actividad, anioEjecucion)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EL_POG_NO_SE_ENCUENTRA_EN_UN_ESTADO_EDITABLE_POR_UT);
                throw b;
            }
        }
    }

    private void verificarSiPOGEditable(Integer idProyecto, POGInsumo insumo) {
        Proyecto proyecto = (Proyecto) generalDAO.find(Proyecto.class, idProyecto);
        if (!proyecto.getTienePOG().equals(Boolean.TRUE)) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EL_PROYECTO_NO_LLEVA_POG);
            throw b;
        }
        if (proyecto.getEstado() != EstadoProyecto.BLOQUEADO) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_PARA_CREAR_POG_EL_PROYECTO_DEBE_ESTAR_BLOQUEADO);
            throw b;
        }
        AnioFiscal anioEjecucion = generalPOBean.getMenorAnioEjecucion();

        if (!POGUtils.habilitadoInsumo(proyecto.getPog(), insumo, anioEjecucion)) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EL_POG_NO_SE_ENCUENTRA_EN_UN_ESTADO_EDITABLE_POR_UT);
            throw b;
        }

    }

    /**
     * guarda una línea de POG
     *
     * @param idP
     * @param lineaAGuardar
     */
    public void actualizarLineaPOG(Integer idP, POLinea lineaAGuardar) {
        try {
            Proyecto proyecto = (Proyecto) generalDAO.find(Proyecto.class, idP);

            BigDecimal total = BigDecimal.ZERO;
            for (ComboValorSeguimiento comboValSeg : lineaAGuardar.getValoresProducto()) {
                for (ValoresSeguimiento valSeg : comboValSeg.getValores()) {
                    if (valSeg.getValor() != null) {
                        total = total.add(valSeg.getValor());
                    }
                }
            }

            if (total.compareTo(lineaAGuardar.getProducto().getCantidad()) > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_LA_CANTIDAD_DE_PRODUCTOS_EN_EL_POG_ES_MAYOR_QUE_LA_DEL_PROYECTO);
                throw b;
            }

            POGProyecto pog = proyecto.getPog();
            generalDAO.update(lineaAGuardar);
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
     * guarda una actividad
     *
     * @param idP POG al que pertenece la línea
     * @param idLinea línea a la que pertenece la actividad
     * @param actividadAGuardar actividad a guardar
     */
    public void crearActualizarActividadPOG(Integer idP, Integer idLinea, POGActividadProyecto actividadAGuardar) {
        try {
            verificarSiPOGEditable(idP, actividadAGuardar);
            Proyecto proyecto = (Proyecto) generalDAO.find(Proyecto.class, idP);

            if (actividadAGuardar != null && actividadAGuardar.getAnioInicio() != null && actividadAGuardar.getAnioFin().intValue() < actividadAGuardar.getAnioInicio().intValue()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_FECHA_DESDE_MAYOR_QUE_HASTA);
                throw b;
            }

            POLinea linea = (POLinea) generalDAO.find(POLinea.class, idLinea);

            if (linea.getActividades().contains(actividadAGuardar)) {
                generalDAO.update(actividadAGuardar);
            } else {
                linea.getActividades().add(actividadAGuardar);
            }
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
     * Este método guarda la distribución por años de los insumos del POG
     *
     * @param idP
     * @param idLinea
     * @param actividadAGuardar
     */
    public void guardarDistribucionAniosInsumosPOG(Integer idP, Integer idLinea, POGActividadProyecto actividadAGuardar) {
        try {
            verificarSiPOGEditable(idP, actividadAGuardar);
            Proyecto proyecto = (Proyecto) generalDAO.find(Proyecto.class, idP);
            //se guarda
            if (actividadAGuardar != null && actividadAGuardar.getInsumos() != null) {

                validarDiferenciaMontoFuenteInsumoEnDistribucion(actividadAGuardar.getInsumos(), false);

                for (Iterator<POInsumos> it = actividadAGuardar.getInsumos().iterator(); it.hasNext();) {
                    POGInsumo pogInsumo = (POGInsumo) it.next();

                    validarDiferenciaMontoYCantidadInsumoConFuentes(pogInsumo, false);

                    generalDAO.update(pogInsumo);
                }

            }
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
     * Este método crea o actualiza los insumos del POG
     *
     * @param idP
     * @param idLineaPO
     * @param idActividadPO
     * @param insumoAguardar
     */
    public void crearActualizarInsumoPOG(Integer idP, Integer idLineaPO, Integer idActividadPO, POInsumos insumoAguardar) {
        try {
            verificarSiPOGEditable(idP, (POGInsumo) insumoAguardar);
            Proyecto proyecto = (Proyecto) generalDAO.find(Proyecto.class, idP);

            //verifica que las fuentes no sobrepasen el monto total del insumo
            BigDecimal suma = BigDecimal.ZERO;
            if (insumoAguardar != null) {
                if (insumoAguardar.getMontosFuentes() != null) {
                    for (POMontoFuenteInsumo iter : insumoAguardar.getMontosFuentes()) {
                        if (iter.getMonto() != null) {
                            suma = suma.add(iter.getMonto());
                        }
                    }
                }
                if (insumoAguardar.getMontoTotal() != null && insumoAguardar.getMontoTotal().compareTo(suma) < 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_LA_SUMA_DE_MONTOS_DE_FUENTE_SOBREPASA_EL_MONTO_TOTAL_DEL_INSUMO);
                    throw b;
                }
            }

            /**
             * VERIFICA NO PASARSE LOS MONTOS POR APORTE
             */
            POGProyecto pog = proyecto.getPog();
            generalDAO.lock(POGProyecto.class, pog.getId());

            POLinea lineaAguardar = (POLinea) generalDAO.find(POLinea.class, idLineaPO);
            for (ProyectoEstPorcentajeFuente montoFuenteEnProducto : lineaAguardar.getProducto().getProyectoEstructura().getMontosFuentes()) {
                //se suma todo menos el inumo a guardar
                BigDecimal montoUsado = BigDecimal.ZERO;
                for (POLinea linea : pog.getLineas()) {
                    //si la línea tiene el mismo padre, osea viene del mismo elemento en la estructura valida lo usado por fuente
                    if (linea.getProducto().getProyectoEstructura().equals(lineaAguardar.getProducto().getProyectoEstructura())) {
                        for (POActividadBase actividad : linea.getActividades()) {
                            for (POInsumos insumo : actividad.getInsumos()) {
                                if (!insumo.equals(insumoAguardar)) {
                                    for (POMontoFuenteInsumo mi : insumo.getMontosFuentes()) {
                                        if (mi.getFuente().equals(montoFuenteEnProducto.getFuente())) {
                                            montoUsado = montoUsado.add(mi.getMonto());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //se sua el insumo a guardar
                if (insumoAguardar != null && insumoAguardar.getMontosFuentes() != null) {
                    for (POMontoFuenteInsumo mi : insumoAguardar.getMontosFuentes()) {
                        if (mi.getFuente().equals(montoFuenteEnProducto.getFuente())) {
                            montoUsado = montoUsado.add(mi.getMonto());
                        }
                    }
                }
                if (montoUsado.compareTo(montoFuenteEnProducto.getMonto()) > 0) {
                    BusinessException b = new BusinessException();
                    String[] params = {montoFuenteEnProducto.getFuente().getFuenteRecursos().getNombre() + " " + montoFuenteEnProducto.getFuente().getCategoriaConvenio().getNombre()};
                    b.addError(ConstantesErrores.ERR_LA_SUMA_DE_MONTOS_SOBREPASA_MONTOS_FUENTES_0, params);
                    throw b;
                }
            }

            /**
             * VERIFICA NO SE PASE DEL MONTO TOTAL DEL PROYECTO Y SI ES POR
             * TRAMOS QUE NO SE PASE DEL TRAMO
             */
            BigDecimal montoTotal = BigDecimal.ZERO;
            BigDecimal montoTotalTramo = BigDecimal.ZERO;
            for (POLinea linea : pog.getLineas()) {
                for (POActividadBase actividad : linea.getActividades()) {
                    for (POInsumos insumo : actividad.getInsumos()) {
                        if (!insumo.equals(insumoAguardar)) {
                            montoTotal = montoTotal.add(insumo.getMontoTotal());
                            //suma lo usado en el tramo
                            if (insumo.getTramo() != null && insumoAguardar != null && insumoAguardar.getTramo() != null && insumo.getTramo().equals(insumoAguardar.getTramo())) {
                                montoTotalTramo = montoTotalTramo.add(insumo.getMontoTotal());
                            }
                        }
                    }
                }
            }
            //verifica que no se pase del monto total 
            if (insumoAguardar != null) {
                montoTotal = montoTotal.add(insumoAguardar.getMontoTotal());
            }
            if (montoTotal.compareTo(proyecto.getMontoGlobal()) > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_SUMA_DE_MONTOS_DE_INSUMOS_SOBREPASA_MONTO_DEL_PROYECTO);
                throw b;
            }

            //verifica que no se pase del tramo
            if (insumoAguardar != null && insumoAguardar.getTramo() != null) {
                montoTotalTramo = montoTotalTramo.add(insumoAguardar.getMontoTotal());
                if (insumoAguardar.getTramo().getMontoHasta().compareTo(montoTotalTramo) < 0) {
                    BusinessException b = new BusinessException();
                    String[] params = {
                        insumoAguardar.getTramo().getCategoria().getCategoriaConvenio().getNombre(),
                        String.valueOf(insumoAguardar.getTramo().getMontoHasta()),
                        String.valueOf(montoTotalTramo)
                    };
                    b.addError(ConstantesErrores.ERR_LA_CATEGORIA_0_EL_TRAMO_DE_HASTA_1_TIENE_USADO_2, params);
                    throw b;
                }
            }

            /**
             * GUARDA EL INSUMO
             */
            if (insumoAguardar != null) {
                if (insumoAguardar.getActividad() != null) {
                    generalDAO.update(insumoAguardar);
                } else {
                    POActividadProyecto actividad = (POActividadProyecto) generalDAO.find(POActividadProyecto.class, idActividadPO);
                    insumoAguardar.setActividad(actividad);
                    actividad.getInsumos().add(insumoAguardar);
                }
            }
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
     * cierra el proyecto valida que cumplan todos los montos crea el POG para
     * el proyecto
     *
     * @param proyecto
     * @return
     */
    public void cerrarProyecto(Integer idProyecto) {
        try {
            //bloquea el proyecto porque actualiza los techosy genera poa si no existe
            Proyecto proyecto = (Proyecto) generalDAO.selectForUpdate(Proyecto.class, idProyecto);

            //valida que el proyecto sirva para cerrar
            ProyectoValidacion.validarCierreProyecto(proyecto);
            //valida que sea consecuente con los opas existentes
            pOAValidaciones.validarEnConstruccionParaCerrarse(proyecto);

            //cierra el proyecto
            proyecto.setEstado(EstadoProyecto.BLOQUEADO);

            //se setean los montos que estaban en construccion
            proyecto.setMontoGlobal(proyecto.getMontoGlobalEnConstruccion());
            for (ProyectoAporte aporte : proyecto.getAportesProyecto()) {
                aporte.setMontoParipassu(aporte.getMontoParipassuEnConstruccion());
                aporte.setFormulaParipassu(aporte.getFormulaParipassuEnConstruccion());
            }
            for (ProyectoCategoriaConvenio cat : proyecto.getDistribuccionCategorias()) {
                cat.setTipoParipassu(cat.getTipoParipassuEnConstruccion());
                for (ProyectoAporteTramoTramo tramo : cat.getTramos()) {
                    tramo.setMontoHasta(tramo.getMontoHastaEnConstruccion());
                    for (ProyectoParipassuTramo paripasu : tramo.getParipassus()) {
                        paripasu.setMontoParipassu(paripasu.getMontoParipassuEnConstruccion());
                        paripasu.setFormulaParipassu(paripasu.getFormulaParipassuEnConstruccion());
                    }
                }
            }
            for (ProyectoComponente est : proyecto.getProyectoComponentes()) {
                for (ProyectoEstPorcentajeFuente fuente : est.getMontosFuentes()) {
                    fuente.setMonto(fuente.getMontoEnConstruccion());
                }
            }
            for (ProyectoMacroActividad est : proyecto.getProyectoMacroactividad()) {
                for (ProyectoEstPorcentajeFuente fuente : est.getMontosFuentes()) {
                    fuente.setMonto(fuente.getMontoEnConstruccion());
                }
            }

            //se genera el pog
            if (proyecto.getTienePOG()) {
                List<Integer> aniosProyecto = ProyectoUtils.getListAnios(proyecto);

                POGProyecto pog;
                if (proyecto.getPog() != null) {
                    pog = proyecto.getPog();
                } else {
                    pog = new POGProyecto();
                    pog.setEstado(EstadoPOGProyecto.EDICION_UT);
                    pog.setLineas(new LinkedList());
                    proyecto.setPog(pog);

                }

                ProyectoUtils.ordenarProyectoEstructuraPorOrden((List<ProyectoEstructura>) (List<?>) proyecto.getProyectoMacroactividad());
                for (ProyectoMacroActividad iter : proyecto.getProyectoMacroactividad()) {
                    for (ProyectoEstProducto prod : iter.getProductos()) {
                        if (!POGUtils.existeLinea(pog, prod)) {
                            POLinea linea = new POLinea();
                            linea.setProducto(prod);
                            TipoSeguimiento tipoSeg = prod.getProducto().getTipo().getTipoSeguimiento();
                            //esto no se si tiene algun sentido
                            linea.setValoresProducto(TipoSeguimientoUtils.generateValoresDelProducto(tipoSeg, aniosProyecto));
                            pog.getLineas().add(linea);
                        }

                    }
                }
                ProyectoUtils.ordenarProyectoEstructuraPorOrden((List<ProyectoEstructura>) (List<?>) proyecto.getProyectoComponentes());
                for (ProyectoComponente iter : proyecto.getProyectoComponentes()) {
                    for (ProyectoEstProducto prod : iter.getProductos()) {
                        if (!POGUtils.existeLinea(pog, prod)) {
                            POLinea linea = new POLinea();
                            linea.setProducto(prod);
                            TipoSeguimiento tipoSeg = prod.getProducto().getTipo().getTipoSeguimiento();
                            //esto no se si tiene algun sentido
                            linea.setValoresProducto(TipoSeguimientoUtils.generateValoresDelProducto(tipoSeg, aniosProyecto));
                            pog.getLineas().add(linea);
                        }
                    }
                }
            }

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
     * genera los poa para todos los años fiscales marcados como planificación
     * para el id de proyecto pasado por parámetro
     *
     * @param idProyecto
     */
    public void generarPOAsPAraProyevoAnio1(Integer idProyecto) {
        try {
            //se bloquea el proyecto para evitar dos veces la generacion del mismo poa
            Proyecto proyecto = (Proyecto) generalDAO.selectForUpdate(Proyecto.class, idProyecto);
            //se ordenan las estructuras para que los poas queden en orden
            ProyectoUtils.ordenarProyectoEstructuraPorOrden((List<ProyectoEstructura>) (List<?>) proyecto.getProyectoMacroactividad());
            ProyectoUtils.ordenarProyectoEstructuraPorOrden((List<ProyectoEstructura>) (List<?>) proyecto.getProyectoComponentes());

            List<Integer> aniosProyecto = ProyectoUtils.getListAnios(proyecto);
            for (int anio : aniosProyecto) {

                AnioFiscal anioFiscal = generalBean.getAnioFiscalOErrorPorAnio(anio);

                //trae todos los opas disponibles para ese año
                List<POAProyecto> poas = poadao.getPOASTrabajoProyecto(proyecto.getId(), anioFiscal.getId());

                //va a ir creando las lineas de los poas si no estan creadas
                for (ProyectoMacroActividad iter : proyecto.getProyectoMacroactividad()) {
                    for (ProyectoEstProducto prod : iter.getProductos()) {
                        //busca la linea a duplicar
                        POLinea lineabasePOG = null;
                        if (proyecto.getPog() != null) {
                            lineabasePOG = POProyectoUtils.findLinea(proyecto.getPog().getLineas(), prod);
                        }
                        //busca el poa de esa ut
                        POAProyecto poa = getPOAoCreate(poas, prod.getUnidadTecnica(), anioFiscal, proyecto);
                        if (lineabasePOG == null) {
                            addLineaSiNoExiste(poa, prod, anioFiscal.getAnio());
                        } else {
                            addLineaSiNoExisteDesdePOG(poa, anioFiscal.getAnio(), lineabasePOG);
                        }
                    }
                }
                for (ProyectoComponente iter : proyecto.getProyectoComponentes()) {
                    for (ProyectoEstProducto prod : iter.getProductos()) {
                        POLinea lineabasePOG = null;
                        if (proyecto.getPog() != null) {
                            lineabasePOG = POProyectoUtils.findLinea(proyecto.getPog().getLineas(), prod);
                        }
                        //busca el poa de es aut
                        POAProyecto poa = getPOAoCreate(poas, prod.getUnidadTecnica(), anioFiscal, proyecto);
                        if (lineabasePOG == null) {
                            addLineaSiNoExiste(poa, prod, anioFiscal.getAnio());
                        } else {
                            addLineaSiNoExisteDesdePOG(poa, anioFiscal.getAnio(), lineabasePOG);
                        }
                    }
                }

            }
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
     * función auxiliar que busca el poa y si no existe lo crea
     *
     */
    private POAProyecto getPOAoCreate(List<POAProyecto> poas, UnidadTecnica ut, AnioFiscal anio, Proyecto proyecto) throws DAOGeneralException {
        for (POAProyecto poa : poas) {
            if (poa.getUnidadTecnica().equals(ut)) {
                return poa;
            }
        }
        POAProyecto poa = new POAProyecto();
        poa.setLineas(new LinkedList<POLinea>());
        poa.setAnioFiscal(anio);
        poa.setUnidadTecnica(ut);
        poa.setEstado(EstadoPOA.EDITANDO_UT);
        poa.setProyecto(proyecto);
        poa.setCierreAnual(Boolean.FALSE);

        poa = (POAProyecto) generalDAO.create(poa);
        poas.add(poa);
        return poa;
    }

    /**
     * función auxiliar que agrega una línea al poa en caso de no existir
     * retorna la línea añadida
     */
    private POLinea addLineaSiNoExiste(POAProyecto poa, ProyectoEstProducto producto, Integer anio) {
        for (POLinea linea : poa.getLineas()) {
            if (linea.getProducto().equals(producto)) {
                return linea;
            }
        }
        POLinea linea = new POLinea();
        linea.setProducto(producto);

        poa.getLineas().add(linea);
        return linea;
    }

    /**
     * función auxiliar que añade una línea desde el POG sin no existe
     */
    private POLinea addLineaSiNoExisteDesdePOG(POAProyecto poa, Integer anio, POLinea lineaPOG) {

        for (POLinea linea : poa.getLineas()) {
            if (linea.getProducto().equals(lineaPOG.getProducto())) {
                return linea;
            }
        }
        POLinea poaLinea = new POLinea();
        poaLinea.setProducto(lineaPOG.getProducto());

        //copia las actividades
        poaLinea.setActividades(new LinkedList<POActividadBase>());
        for (Iterator<POActividadBase> it = lineaPOG.getActividades().iterator(); it.hasNext();) {
            POGActividadProyecto pogActividad = (POGActividadProyecto) it.next();

            //filtra las actividades para ese año
            if ((pogActividad.getAnioInicio() == null || pogActividad.getAnioInicio().intValue() <= anio)
                    && (pogActividad.getAnioFin() == null || pogActividad.getAnioFin().intValue() >= anio)) {

                POActividadProyecto poaActividad = new POActividadProyecto();
                poaActividad.setActividadCodiguera(pogActividad.getActividadCodiguera());
                poaActividad.setDuracion(pogActividad.getDuracion());
                poaActividad.setResponsable(pogActividad.getResponsable());
                poaActividad.setUbicacion(pogActividad.getUbicacion());
                poaActividad.setUtResponsable(pogActividad.getUtResponsable());

                //copia los insumos
                poaActividad.setInsumos(new LinkedList<POInsumos>());
                for (Iterator<POInsumos> it2 = pogActividad.getInsumos().iterator(); it2.hasNext();) {
                    POGInsumo pogInsumo = (POGInsumo) it2.next();
                    POGInsumoAnio pogInsumoAnio = POGUtils.getAnioInsumo(pogInsumo, anio);
                    //se queda solo con los insumos que tienen cantidades para ese anio
                    if (pogInsumoAnio != null && pogInsumoAnio.getCantidadInsumo() != null) {
                        POInsumos poaInsumo = new POInsumos();
                        poaInsumo.setActividad(poaActividad);
                        poaInsumo.setInsumo(pogInsumo.getInsumo());
                        poaInsumo.setNoUACI(pogInsumo.getNoUACI());
                        poaInsumo.setPoa(poa);
                        poaInsumo.setUnidadTecnica(poa.getUnidadTecnica());
                        if (pogInsumo.getFechaContratacion() != null) {
                            Date d = DatesUtils.setYearToDate(pogInsumo.getFechaContratacion(), anio);
                            poaInsumo.setFechaContratacion(d);
                        }

                        poaInsumo.setCantidad(0);
                        poaInsumo.setMontoUnit(BigDecimal.ZERO);
                        poaInsumo.setMontoTotal(BigDecimal.ZERO);
                        if (pogInsumoAnio != null && pogInsumoAnio.getCantidadInsumo() != null) {
                            poaInsumo.setCantidad(pogInsumoAnio.getCantidadInsumo());
                        }
                        if (pogInsumo.getMontoUnit() != null) {
                            poaInsumo.setMontoUnit(pogInsumo.getMontoUnit());
                        }
                        poaInsumo.setMontoTotal(poaInsumo.getMontoUnit().multiply(BigDecimal.valueOf(poaInsumo.getCantidad())));

                        //copia la distribucion de las fuentes de los insumos
                        poaInsumo.setTramo(pogInsumo.getTramo());
                        poaInsumo.setMontosFuentes(new LinkedList<POMontoFuenteInsumo>());
                        for (POGMontoFuenteInsumo montoPOG : pogInsumoAnio.getMontosFuentes()) {
                            if (montoPOG.getMonto() != null && montoPOG.getMonto().compareTo(BigDecimal.ZERO) != 0) {
                                POMontoFuenteInsumo montoPOA = new POMontoFuenteInsumo();
                                montoPOA.setFuente(montoPOG.getFuente());
                                montoPOA.setInsumo(poaInsumo);
                                montoPOA.setMonto(montoPOG.getMonto());
                                poaInsumo.getMontosFuentes().add(montoPOA);

                            }
                        }
                        poaInsumo.setActividad(poaActividad);
                        poaActividad.getInsumos().add(poaInsumo);

                    }
                }
                poaLinea.getActividades().add(poaActividad);
            }
        }
        poa.getLineas().add(poaLinea);
        return poaLinea;
    }

    /**
     * Operación que cierra el pog del proyecto pasado por parámetro verifica
     * que todas las sumas den
     *
     * @param idProyecto
     */
    public void consolidarPOG(Integer idProyecto) {
        try {
            Proyecto proyecto = (Proyecto) generalDAO.find(Proyecto.class, idProyecto);
            POGProyecto pog = proyecto.getPog();
            /**
             * VERIFICA NO PASARSE LOS MONTOS
             */

            for (POLinea linea : pog.getLineas()) {
                for (POActividadBase actividad : linea.getActividades()) {
                    this.validarDiferenciaMontoFuenteInsumoEnDistribucion(actividad.getInsumos(), true);
                    for (Iterator<POInsumos> it = actividad.getInsumos().iterator(); it.hasNext();) {
                        POGInsumo pogInsumo = (POGInsumo) it.next();
                        this.validarDiferenciaMontoYCantidadInsumoConFuentes(pogInsumo, true);
                    }
                }
            }

            for (ProyectoComponente componente : proyecto.getProyectoComponentes()) {
                verificarMontosParaPOGParaEstructura(componente, pog);
            }
            for (ProyectoMacroActividad macroactividad : proyecto.getProyectoMacroactividad()) {
                verificarMontosParaPOGParaEstructura(macroactividad, pog);
            }

            //se crea una línea base para el pog cuando se cierra
            POAConverter converter = new POAConverter();
            POGProyecto base = converter.convertPOGProyecto(pog);
            base = (POGProyecto) generalDAO.persist(base);

            pog.setEstado(EstadoPOGProyecto.CERRADO);

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
     * Operación auxiliar que se usa para validar que se usaron todos los montos
     * de una estructura en el pog
     *
     * @param estructura
     * @param pog
     */
    private void verificarMontosParaPOGParaEstructura(ProyectoEstructura estructura, POGProyecto pog) {
        //hace la validacion solo si la estructura tinee productos
        if (!estructura.getProductos().isEmpty()) {
            //va a verificar para cada fuente que se cumpla la validacion
            for (ProyectoEstPorcentajeFuente montoFuenteEnEstructura : estructura.getMontosFuentes()) {
                //para cada fuente recorre todas las lineas del pog
                BigDecimal montoUsado = BigDecimal.ZERO;
                for (POLinea iterLinea : pog.getLineas()) {
                    //se queda solo con las lineas de ese producto
                    if (iterLinea.getProducto().getProyectoEstructura().equals(estructura)) {
                        for (POActividadBase actividad : iterLinea.getActividades()) {
                            for (POInsumos insumo : actividad.getInsumos()) {
                                for (POMontoFuenteInsumo mi : insumo.getMontosFuentes()) {
                                    //se queda solo con los muntos de esa fuente
                                    if (mi.getFuente().equals(montoFuenteEnEstructura.getFuente())) {
                                        montoUsado = montoUsado.add(mi.getMonto());
                                    }
                                }
                            }
                        }
                    }
                }

                //si el monto sobrepasa lo asignado con lo usado 
                if (montoUsado.compareTo(montoFuenteEnEstructura.getMonto()) != 0) {
                    String nombreEstructura = null;
                    if (estructura.getTipo() == TipoEstructura.COMPONENTE) {
                        nombreEstructura = ((ProyectoComponente) estructura).getNombre();
                    } else if (((ProyectoMacroActividad) estructura).getMacroActividad() != null) {
                        nombreEstructura = ((ProyectoMacroActividad) estructura).getMacroActividad().getNombre();
                    }
                    BusinessException b = new BusinessException();
                    String[] params = {montoFuenteEnEstructura.getFuente().getFuenteRecursos().getNombre() + " " + montoFuenteEnEstructura.getFuente().getCategoriaConvenio().getNombre(), nombreEstructura};
                    b.addError(ConstantesErrores.ERR_LA_SUMA_DE_MONTOS_DISTINTO_PARA_FUENTES_0_EN_ESTRUCTURA_1, params);
                    throw b;
                }
            }
        }
    }

    public Collection<RiesgoTO> obtenerRiesgosPorCriteria(FiltroRiesgo filtro) {
        if (filtro != null) {
            try {

                return poadao.obtenerRiesgosPorFiltro(filtro);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, null, ex);
                BusinessException b = new BusinessException();
                b.setEx(ex);
                b.addError(ConstantesErrores.ERROR_GENERAL);
                throw b;
            }
        }
        return new ArrayList();
    }

    /**
     * Valida que la suma de montos de las fuentes de un insumo en la
     * distribución, sea menor o igual a lo que se ingresó en el insumo
     *
     * @param pogInsumo
     * @param montoExcactamenteIgual
     */
    private void validarDiferenciaMontoYCantidadInsumoConFuentes(POGInsumo pogInsumo, boolean montoExcactamenteIgual) {

        BigDecimal montoTotalInsumoPorFuentesPOG = BigDecimal.ZERO;

        Integer cantidiad = 0;
        for (POGInsumoAnio iteranio : pogInsumo.getDistribucionAnios()) {
            if (iteranio.getCantidadInsumo() != null) {
                cantidiad = cantidiad + iteranio.getCantidadInsumo();
            }
            if (iteranio.getMontosFuentes() != null) {
                for (POGMontoFuenteInsumo montoFuentePOG : iteranio.getMontosFuentes()) {
                    if (montoFuentePOG.getMonto() != null) {
                        montoTotalInsumoPorFuentesPOG = montoTotalInsumoPorFuentesPOG.add(montoFuentePOG.getMonto());
                    }
                }
            }
        }

        String nombreInsumo = "";
        if (pogInsumo != null) {
            if (pogInsumo.getInsumo() != null) {
                nombreInsumo = pogInsumo.getInsumo().getNombre();
                if (pogInsumo.getId() != null) {
                    nombreInsumo += " (" + pogInsumo.getId() + ")";
                }
            }
        }

        String[] paramInsumo = {nombreInsumo};
        if (montoExcactamenteIgual) {
            if (pogInsumo.getCantidad() != null && pogInsumo.getCantidad().compareTo(cantidiad) != 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EN_INSUMO_0_LA_DISTRIBUCION_POR_ANIOS_DISTINTO_A_LA_CANTIDAD_ESTABLECIDA_EN_EL_INSUMO, paramInsumo);
                throw b;
            }

            if (pogInsumo.getMontoTotal().compareTo(montoTotalInsumoPorFuentesPOG) != 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_SUMA_MONTOS_FUENTES_POG_EN_INSUMO_0_DISTINTO_MONTO_TOTAL_INSUMO_POG, paramInsumo);
                throw b;
            }
        } else {
            if (pogInsumo.getCantidad() != null && pogInsumo.getCantidad().compareTo(cantidiad) < 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EN_INSUMO_0_LA_DISTRIBUCION_POR_ANIOS_SUPERA_A_LA_CANTIDAD_ESTABLECIDA_EN_EL_INSUMO, paramInsumo);
                throw b;
            }

            if (pogInsumo.getMontoTotal().compareTo(montoTotalInsumoPorFuentesPOG) < 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_SUMA_MONTOS_FUENTES_POG_EN_INSUMO_0_SUPERA_MONTO_TOTAL_INSUMO_POG, paramInsumo);
                throw b;
            }
        }
    }

    /**
     * Compara que la suma de los montos de las fuentes de los insumos en la
     * distribución por año sea menor o igual a lo ingresado en el insumo
     *
     * @param pogInsumo
     * @param montoExcactamenteIgual
     */
    private void validarDiferenciaMontoFuenteInsumoEnDistribucion(List<POInsumos> listaPoInsumos, boolean montoExcactamenteIgual) {

        List<POMontoFuenteInsumo> listaMontosFuentes = listaPoInsumos.get(0).getMontosFuentes();

        for (POMontoFuenteInsumo montoFuente : listaMontosFuentes) {
            BigDecimal totalFuenteEnInsumos = BigDecimal.ZERO;
            BigDecimal totalFuenteEnDistribucion = BigDecimal.ZERO;
            for (POInsumos insumo : listaPoInsumos) {
                POGInsumo pogInsumo = (POGInsumo) insumo;
                for (POMontoFuenteInsumo montoFuentePOG : pogInsumo.getMontosFuentes()) {
                    if (montoFuentePOG.getFuente().equals(montoFuente.getFuente()) && montoFuentePOG.getMonto() != null) {
                        totalFuenteEnInsumos = totalFuenteEnInsumos.add(montoFuentePOG.getMonto());
                    }
                }
                for (POGInsumoAnio iteranio : pogInsumo.getDistribucionAnios()) {
                    if (iteranio.getMontosFuentes() != null) {
                        for (POGMontoFuenteInsumo montoFuentePOG : iteranio.getMontosFuentes()) {
                            if (montoFuente.getFuente().equals(montoFuentePOG.getFuente()) && montoFuentePOG.getMonto() != null) {
                                totalFuenteEnDistribucion = totalFuenteEnDistribucion.add(montoFuentePOG.getMonto());
                            }
                        }
                    }
                }
            }

            String codigoFuente = montoFuente.getFuente().getFuenteRecursos().getCodigo();            
            String montoEnDistribucion = NumberUtils.nomberToString(totalFuenteEnDistribucion);
            String montoEnInsumos = NumberUtils.nomberToString(totalFuenteEnInsumos);
            String[] paramInsumo = {codigoFuente, montoEnDistribucion, montoEnInsumos};
            if (montoExcactamenteIgual) {
                if (totalFuenteEnInsumos.compareTo(totalFuenteEnDistribucion) != 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_LA_SUMA_DE_MONTOS_DE_LA_FUENTE_0_1_EN_LA_DISTRIBUCION_POR_ANIOS_ES_DISTINTO_AL_TOTAL_EN_LOS_INSUMOS_2, paramInsumo);
                    throw b;
                }
            } else {
                if (totalFuenteEnDistribucion.compareTo(totalFuenteEnInsumos) > 0) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_LA_SUMA_DE_MONTOS_DE_LA_FUENTE_0_1_EN_LA_DISTRIBUCION_POR_ANIOS_ES_MAYOR_AL_TOTAL_EN_LOS_INSUMOS_2, paramInsumo);
                    throw b;
                }

            }
        }

    }

}
