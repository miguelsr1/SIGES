/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.utils.FlujoCajaMensualUtils;
import gob.mined.siap2.business.utils.POAConActividadesUtils;
import gob.mined.siap2.business.utils.POAConverter;
import gob.mined.siap2.business.utils.POAUtils;
import gob.mined.siap2.business.utils.POProyectoUtils;
import gob.mined.siap2.business.validaciones.ValidacionReprogramacion;
import gob.mined.siap2.business.validaciones.ValidacionReprogramacionDetalle;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.PAC;
import gob.mined.siap2.entities.data.impl.PACGrupo;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividad;
import gob.mined.siap2.entities.data.impl.POActividadAsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POActividadProyecto;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumoFlujoCajaMensual;
import gob.mined.siap2.entities.data.impl.Reprogramacion;
import gob.mined.siap2.entities.data.impl.ReprogramacionDetalle;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.entities.enums.EstadoReprogramacion;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.entities.enums.TipoReprogramacion;
import gob.mined.siap2.entities.tipos.FiltroReprogramacion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.ProyectoDAO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.BooleanUtils;

/**
 * Esta clase implementa los métodos para reprogramación .
 *
 * @author Sofis Solutions
 */
@Stateless(name = "ReprogramacionBean")
@LocalBean
public class ReprogramacionBean {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    @JPADAO
    private ProyectoDAO proyectoDAO;
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Inject
    private PACBean pacBean;

    /**
     * Este método devuelve una reprogramación según su id.
     *
     * @param idReprogramacion
     * @return
     */
    public Reprogramacion getReprogramacion(Integer idReprogramacion) {
        try {
            Reprogramacion reprogramacion = (Reprogramacion) generalDAO.find(Reprogramacion.class, idReprogramacion);
            for (ReprogramacionDetalle detalle : reprogramacion.getRep_detalle_lista()) {
                if (detalle.getPoa() != null) {
                    if (detalle.getPoa().getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
                        POAConActividadesUtils.initPOA((POAConActividades) detalle.getPoa());
                    } else {
                        POProyectoUtils.initPOAProyecto((POAProyecto) detalle.getPoa());
                    }

                }
                detalle.getInsumoNuevoMontosFuentes().isEmpty();
            }

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
     * Almacenamiento de una reprogramación de acción central
     *
     * @param reprog
     * @return reprog actualizada
     */
    public Reprogramacion guardarReprogramacion(Reprogramacion reprog) {
        try {
            for (ReprogramacionDetalle iter : reprog.getRep_detalle_lista()) {
                distribuirMontoCertificadoMensualPorFuente(iter);
                ValidacionReprogramacionDetalle.validar(iter, reprog.getEstado());
            }

            return (Reprogramacion) generalDAO.update(reprog);
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
     * Calcula y setea en cada monto fuente por mes el monto certificado en
     * proporción a lo certificado para la fuente
     *
     * @param cert
     */
    private void distribuirMontoCertificadoMensualPorFuente(ReprogramacionDetalle repDetalle) {
        for (POMontoFuenteInsumo montoFuente : repDetalle.getInsumoNuevoMontosFuentes()) {
            if (montoFuente.getMontosFuentesInsumosFCM() != null) {
                List<POMontoFuenteInsumoFlujoCajaMensual> listaMontoFuenteFCMConMonto = new LinkedList<>();
                for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : montoFuente.getMontosFuentesInsumosFCM()) {
                    if (montoFuenteFCM.getMonto() != null && montoFuenteFCM.getMonto().compareTo(BigDecimal.ZERO) != 0) {
                        listaMontoFuenteFCMConMonto.add(montoFuenteFCM);
                    }
                }
                if (!listaMontoFuenteFCMConMonto.isEmpty()) {
                    this.ordenarListaMontoFuenteFCMPorMes(listaMontoFuenteFCMConMonto);
                    //Me quedo con el último de la lista para hacer el prorrateo
                    POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCMUltimo = listaMontoFuenteFCMConMonto.get(listaMontoFuenteFCMConMonto.size() - 1);
                    BigDecimal sumaMontosCertExceptoUltimo = BigDecimal.ZERO;
                    for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCM : listaMontoFuenteFCMConMonto) {
                        //Si el monto certificado de la fuente es igual al monto original, para cada mes de la fuente, el monto certificado también será igual al monto original
                        if (montoFuente.getMonto().equals(montoFuente.getCertificado())) {
                            montoFuenteFCM.setMontoCertificado(montoFuenteFCM.getMonto());
                        } else {
                            //Si no es el último, hago el calculo normal                            
                            //Para comprobar que sea distinto comparo el mes               
                            if (montoFuenteFCMUltimo.getFlujoCajaMensual().getMes() != montoFuenteFCM.getFlujoCajaMensual().getMes()) {
                                //Monto Fuente Mes Cert = (Monto fuente mes / monto fuente) * monto fuente cert
                                BigDecimal montoFuenteFMCCertificado = montoFuenteFCM.getMonto().divide(montoFuente.getMonto(), 2, RoundingMode.DOWN);
                                montoFuenteFMCCertificado = montoFuenteFMCCertificado.multiply(montoFuente.getCertificado());
                                montoFuenteFMCCertificado = montoFuenteFMCCertificado.setScale(2, RoundingMode.CEILING);
                                montoFuenteFCM.setMontoCertificado(montoFuenteFMCCertificado);
                                sumaMontosCertExceptoUltimo = sumaMontosCertExceptoUltimo.add(montoFuenteFMCCertificado);
                            }
                        }
                    }
                    sumaMontosCertExceptoUltimo = sumaMontosCertExceptoUltimo.setScale(2, RoundingMode.CEILING);
                    //Si el monto a certificar no era excactamente igual al monto original
                    if (!montoFuente.getMonto().equals(montoFuente.getCertificado())) {
                        //Prorateo en el último monto fuente FCM con lo que resta para completar el monto certificado de la fuente
                        BigDecimal montoDiferenciaCertFCM = montoFuente.getCertificado().subtract(sumaMontosCertExceptoUltimo);
                        montoFuenteFCMUltimo.setMontoCertificado(montoDiferenciaCertFCM);
                    }
                }

            }
        }
    }

    /**
     * Ordena una lista de POMontoFuenteInsumoFlujoCajaMensual por mes
     *
     * @param listaMontoFuenteFCMConMonto
     */
    private void ordenarListaMontoFuenteFCMPorMes(List<POMontoFuenteInsumoFlujoCajaMensual> listaMontoFuenteFCMConMonto) {
        Collections.sort(listaMontoFuenteFCMConMonto, new Comparator<POMontoFuenteInsumoFlujoCajaMensual>() {
            @Override
            public int compare(POMontoFuenteInsumoFlujoCajaMensual o1, POMontoFuenteInsumoFlujoCajaMensual o2) {
                return o1.getFlujoCajaMensual().getMes().compareTo(o2.getFlujoCajaMensual().getMes());
            }
        });
    }

    /**
     * Este método permite enviar una reprogramación para su procesamiento.
     *
     * @param reprog
     * @return
     */
    public Reprogramacion enviarReprogramacion(Reprogramacion reprog) {
        try {
            //if (afectaPAC(reprog)) {
            //reprog.setEstado(EstadoReprogramacion.PENDIENTE_PAC);
            if (reprog != null) {
                reprog.setEstado(EstadoReprogramacion.APROBADO);
                reprog = (Reprogramacion) generalDAO.update(reprog);
            }
            return reprog;
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
     * Este método rechaza una reprogramación desde el PAC.
     *
     * @param reprog
     * @return
     */
    public Reprogramacion rechazarEnPac(Reprogramacion reprog) {
        try {
            reprog.setEstado(EstadoReprogramacion.FORMULACION);
            return (Reprogramacion) generalDAO.update(reprog);
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
     * Este método aprueba una reprogramación en PAC.
     *
     * @param reprog
     * @return
     * @throws BusinessException
     */
    public Reprogramacion aprobarEnPac(Reprogramacion reprog) throws BusinessException {
        try {
            ValidacionReprogramacion.validar(reprog);
            reprog.setEstado(EstadoReprogramacion.APROBADO);
            return (Reprogramacion) generalDAO.update(reprog);
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
     * Este método determina si una reprogramación afecta al PAC.
     *
     * @param reprog
     * @return
     */
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

    /**
     * Este método obtiene los grupos de un POA.
     *
     * @param id
     * @return
     */
    public Collection<PACGrupo> obtenerGruposPorPOAId(Integer id) {
        Collection<PACGrupo> l = proyectoDAO.obtenerGruposPACPorPOAId(id);
        l.isEmpty();
        return l;
    }

    //Reprogramaciones
    public Collection<Reprogramacion> obtenerReprogramacionesPorFiltro(FiltroReprogramacion filtro) {
        return proyectoDAO.obtenerReprogramacionesPorFiltro(filtro);
    }

    public PAC obtenerPACPorPOAId(Integer id) {
        return proyectoDAO.obtenerPACPorPOAId(id);
    }

    /**
     * Método encargado de aplicar una reprogramación. impactar los datos en el
     * poa y en el pac
     *
     * @param idReprogramacion
     */
    public Reprogramacion aplicarReprogramacion(Integer idReprogramacion) {
        try {
            Reprogramacion reprogramacion = (Reprogramacion) generalDAO.find(Reprogramacion.class, idReprogramacion);

            Set<GeneralPOA> poasModificados = new LinkedHashSet();

            for (ReprogramacionDetalle detalle : reprogramacion.getRep_detalle_lista()) {
                if (detalle.getPoa() == null) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_EXISTE_DETALLE_EN_REPROGRAMACION_NO_ASOCIADO_A_NINGUN_POA);
                    throw b;
                }

                GeneralPOA poa = detalle.getPoa();

                POActividadBase actividad = null;
                if (!detalle.getNuevaActividad()) {
                    actividad = detalle.getPoaActividad();
                } else {

                    //si la actividad dodne va a añadir el insumo no existe, entonces crea una actividad nueva
                    if (reprogramacion.getTipoReprogramacion() == TipoReprogramacion.ACCION_CENTRAL) {
                        POActividadAsignacionNoProgramable actividadnp = new POActividadAsignacionNoProgramable();
                        actividadnp.setActividadNP(detalle.getActividadNuevaAsigNP());

                        actividad = actividadnp;
                        ((POAConActividades) poa).getActividades().add(actividad);
                    } else if (reprogramacion.getTipoReprogramacion() == TipoReprogramacion.ASIGNACION_NO_PROGRAMABLE) {
                        POActividad actividadAC = new POActividad();
                        actividadAC.setNombre(detalle.getActividadNuevaAccionCentral());

                        actividad = actividadAC;
                        ((POAConActividades) poa).getActividades().add(actividad);
                    } else if (reprogramacion.getTipoReprogramacion() == TipoReprogramacion.PROYECTO) {
                        POActividadProyecto actividadp = new POActividadProyecto();
                        actividadp.setActividadCodiguera(detalle.getActividadNuevaProyecto());

                        actividad = actividadp;
                        detalle.getPoaLinea().getActividades().add(actividad);
                    }

                    actividad.setEstado(EstadoActividadPOA.NO_FINALIZADA);
                    actividad.setInsumos(new LinkedList());

                }

                //se busca el insumo con el cual se va a trabajar
                POInsumos insumoSelecionado = null;
                if (detalle.getNuevoInsumo()) {
                    insumoSelecionado = new POInsumos();
                    insumoSelecionado.setMontosFuentes(new LinkedList());
                    insumoSelecionado.setFlujosDeCajaAnio(new LinkedHashSet());
                    insumoSelecionado.setNoUACI(false);
                    insumoSelecionado.setEnviadoParaCertificar(false);
                    insumoSelecionado.setPasoValidacionCertificadoDeDispPresupuestaria(false);
                    insumoSelecionado.setPoa(detalle.getPoa());
                    insumoSelecionado.setUnidadTecnica(detalle.getPoa().getUnidadTecnica());
                    insumoSelecionado.setMontosFuentes(new LinkedList());

                    //se añade el insumo a la actividad
                    actividad.getInsumos().add(insumoSelecionado);
                    insumoSelecionado.setActividad(actividad);
                } else {
                    insumoSelecionado = detalle.getPoaInsumo();
                    pacBean.calcularDesasociarInsumo(insumoSelecionado);
                }
                //se actualizan los valores de las fuentes en el insumo existente
                for (POMontoFuenteInsumo iter : detalle.getInsumoNuevoMontosFuentes()) {
                    POMontoFuenteInsumo fuente = POAUtils.getFuenteEnInsumo(insumoSelecionado, iter.getFuente());
                    if (fuente == null) {
                        fuente = new POMontoFuenteInsumo();
                        fuente.setInsumo(insumoSelecionado);
                        insumoSelecionado.getMontosFuentes().add(fuente);
                    }
                    fuente.setCertificadoDisponibilidadPresupuestariaAprobada(iter.getCertificadoDisponibilidadPresupuestariaAprobada());
                    fuente.setCertificado(iter.getCertificado());
                    fuente.setMonto(iter.getMonto());
                    fuente.setFuente(iter.getFuente());
                }

                //se actualiza el total certificado del insumo
                BigDecimal totalCertificado = BigDecimal.ZERO;
                for (POMontoFuenteInsumo iter : detalle.getInsumoNuevoMontosFuentes()) {
                    if (iter.getCertificadoDisponibilidadPresupuestariaAprobada() != null && iter.getCertificadoDisponibilidadPresupuestariaAprobada()) {
                        totalCertificado = totalCertificado.add(iter.getCertificado());
                    }
                }
                insumoSelecionado.setMontoTotalCertificado(totalCertificado);

                //se actualiza la informacion del insumo
                insumoSelecionado.setFechaContratacion(detalle.getInsumoNuevoFechaContratacion());
                insumoSelecionado.setObservacion(detalle.getInsumoNuevoObservaciones());
                if (detalle.getInsumoNuevo() != null) {
                    insumoSelecionado.setInsumo(detalle.getInsumoNuevo());
                }
                insumoSelecionado.setNoUACI(detalle.getInsumoNuevoNoUaci());
                insumoSelecionado.setCantidad(detalle.getInsumoNuevoCantidad());
                insumoSelecionado.setMontoUnit(detalle.getInsumoNuevoUnitario());
                //se añade el poa  la lista d emodificados
                BigDecimal montoReprogramacion = BigDecimal.ZERO;
                if(insumoSelecionado.getMontoTotal()!=null && detalle.getInsumoNuevoTotal()!=null){                 
                    montoReprogramacion = detalle.getInsumoNuevoTotal().subtract(insumoSelecionado.getMontoTotal());
                    if(insumoSelecionado.getMontoReprogramaciones()!=null){
                       montoReprogramacion = montoReprogramacion.add(insumoSelecionado.getMontoReprogramaciones());
                    }
                }                
                insumoSelecionado.setMontoTotal(detalle.getInsumoNuevoTotal()); 
                insumoSelecionado.setMontoReprogramaciones(montoReprogramacion);
                
                insumoSelecionado.setUnidadTecnica(detalle.getPoa().getUnidadTecnica());
                if (reprogramacion.getTipoReprogramacion() == TipoReprogramacion.PROYECTO) {
                    insumoSelecionado.setTramo(detalle.getInsumoNuevoTramo());
                }

//                //se actualizan las fuentes del insumo
//                //se seta el monto del insumo en el monto de la fuente
                //se actualiza la programación de pagos para el insumo    
                Set<FlujoCajaAnio> flujos = null;
                if (detalle.getNuevoInsumo() != null && detalle.getNuevoInsumo()) {
                    flujos = new LinkedHashSet();
                    FlujoCajaAnio fca = FlujoCajaMensualUtils.generarFCM(detalle.getPoa().getAnioFiscal().getAnio());
                    flujos.add(fca);
                    insumoSelecionado.setFlujosDeCajaAnio(flujos);
                } else {
                    flujos = insumoSelecionado.getFlujosDeCajaAnio();
                }
                for (FlujoCajaAnio fca : flujos) {
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
                }

                if (detalle.getInsumoNuevoNoUaci() != null && detalle.getInsumoNuevoNoUaci()) {
                    for (POMontoFuenteInsumo montoFuente : insumoSelecionado.getMontosFuentes()) {
                        if (montoFuente.getMontosFuentesInsumosFCM() == null || montoFuente.getMontosFuentesInsumosFCM().isEmpty()) {
                            cargarFCMParaFuentesDelInsumo(montoFuente, insumoSelecionado.getFlujosDeCajaAnio(), detalle.getInsumoNuevoMontosFuentes());
                        } else {
                            for (POMontoFuenteInsumoFlujoCajaMensual mfFCM : montoFuente.getMontosFuentesInsumosFCM()) {
                                actualizarFCMParaFuentesDelInsumo(mfFCM, detalle.getInsumoNuevoMontosFuentes());
                            }
                        }
                    }
                }
                
                //Se setea la diferencia entre el monto original y el monto de la reprogramacion
                

                //se añade el insumo al grupo de pac
                if (!detalle.getInsumoNuevoNoUaci()) {
                    pacBean.calcularAsociarInsumo(insumoSelecionado, detalle.getGrupoPAC());
                }

                

            }

            //se genera una nueva línea base para los poas modificados
            for (GeneralPOA poa : poasModificados) {
                POAConverter converter = new POAConverter();
                if (poa.getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
                    POAConActividades base = converter.convertPOAConActividades((POAConActividades) poa);
                    generalDAO.create(base);
                } else {
                    POAProyecto base = converter.convertPOAPOAProyecto((POAProyecto) poa);
                    generalDAO.create(base);

                }
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

    private void cargarFCMParaFuentesDelInsumo(POMontoFuenteInsumo montoFuente, Set<FlujoCajaAnio> fcaInsumo, List<POMontoFuenteInsumo> montosFuentesReprog) {
        for (POMontoFuenteInsumo montoFuenteReprog : montosFuentesReprog) {
            if (montoFuente.getFuente().getId().equals(montoFuenteReprog.getFuente().getId())) {
                for (FlujoCajaAnio fca : fcaInsumo) {
                    for (POFlujoCajaMenusal fcm : fca.getFlujoCajaMenusal()) {
                        if (fcm.getMontosFuentesInsumosFCM() == null) {
                            fcm.setMontosFuentesInsumosFCM(new LinkedList<POMontoFuenteInsumoFlujoCajaMensual>());
                        }
                        if (montoFuente.getMontosFuentesInsumosFCM() == null) {
                            montoFuente.setMontosFuentesInsumosFCM(new LinkedList<POMontoFuenteInsumoFlujoCajaMensual>());
                        }
                        POMontoFuenteInsumoFlujoCajaMensual montoFuenteInsumoFCM = new POMontoFuenteInsumoFlujoCajaMensual();
                        montoFuenteInsumoFCM.setFlujoCajaMensual(fcm);
                        montoFuenteInsumoFCM.setMontoFuenteInsumo(montoFuente);
                        fcm.getMontosFuentesInsumosFCM().add(montoFuenteInsumoFCM);
                        montoFuente.getMontosFuentesInsumosFCM().add(montoFuenteInsumoFCM);
                        for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCMReprog : montoFuenteReprog.getMontosFuentesInsumosFCM()) {
                            if (fcm.getMes().equals(montoFuenteFCMReprog.getFlujoCajaMensual().getMes())) {
                                montoFuenteInsumoFCM.setMonto(montoFuenteFCMReprog.getMonto());
                                montoFuenteInsumoFCM.setMontoCertificado(montoFuenteFCMReprog.getMontoCertificado());
                            }
                        }
                    }
                }
            }
        }
    }

    private void actualizarFCMParaFuentesDelInsumo(POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCMInsumo, List<POMontoFuenteInsumo> insumoNuevoMontosFuentes) {
        for (POMontoFuenteInsumo montoFuenteReprog : insumoNuevoMontosFuentes) {
            for (POMontoFuenteInsumoFlujoCajaMensual montoFuenteFCMReprog : montoFuenteReprog.getMontosFuentesInsumosFCM()) {
                if (montoFuenteFCMInsumo.getMontoFuenteInsumo().getFuente().getFuenteRecursos().getId().equals(montoFuenteFCMReprog.getMontoFuenteInsumo().getFuente().getFuenteRecursos().getId())) {
                    if (montoFuenteFCMInsumo.getFlujoCajaMensual().getMes().equals(montoFuenteFCMReprog.getFlujoCajaMensual().getMes())) {
                        montoFuenteFCMInsumo.setMonto(montoFuenteFCMReprog.getMonto());
                        montoFuenteFCMInsumo.setMontoCertificado(montoFuenteFCMReprog.getMontoCertificado());
                    }
                }
            }
        }
    }

}
