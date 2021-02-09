/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import com.mined.siap2.audit.AuditReader;
import gob.mined.siap2.business.ejbs.DatosUsuario;
import gob.mined.siap2.business.interceptors.log.LoggedInterceptor;
import gob.mined.siap2.business.validations.POAValidacion;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.NotificacionPOA;
import gob.mined.siap2.entities.data.impl.POA;
import gob.mined.siap2.entities.data.impl.POAActividadMeta;
import gob.mined.siap2.entities.data.impl.POALite;
import gob.mined.siap2.entities.data.impl.POAMetaAnual;
import gob.mined.siap2.entities.enums.EstadosPOA;
import gob.mined.siap2.entities.enums.TipoNotificacion;
import gob.mined.siap2.entities.tipos.FiltroRiesgo;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroPOA;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.POADAOUT;
import gob.mined.siap2.persistence.dao.imp.UsuarioDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.ws.to.RiesgoTOPOA;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import org.apache.commons.lang3.StringUtils;

/**
 * Esta clase incluye los métodos de servicio correspondientes a POAs.
 * @author Sofis Solutions
 */
@Stateless(name = "POABean")
@LocalBean
@Interceptors({LoggedInterceptor.class})
public class POABean {
    
    @Inject
    private DatosUsuario du;
    @Inject
    @JPADAO
    private POADAOUT poaDAO;
    @Inject
    private POAValidacion poaValidacion;

    @Inject
    @JPADAO
    private UsuarioDAO usuarioDAO;
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    private static final Logger logger = Logger.getLogger(POABean.class.getName());

    /**
     * Este método guarda un elemento de tipo POA. Se aplica para la
     * creación de la entidad y para la modificación de una entidad existente.
     *
     * @param cnf
     * @throws GeneralException Devuelve los códigos de error de la validación
     */
    public POA guardar(POA poa, Boolean indicador, String userCod, Boolean metasActividadesObligatorias, Boolean validarSeguimiento) throws GeneralException {
        logger.log(Level.SEVERE, "guardar");
        try {
            //Validar el elemento a guardar. Si no valida, se lanza una excepcion
            if (poaValidacion.validar(poa, indicador, metasActividadesObligatorias, validarSeguimiento)) {
                if(indicador != null && indicador) {
                    if(!usuarioDAO.getUsuarioTienePermisoEnUTPadreConOperacion(poa.getUnidadTecnica().getId(), ConstantesEstandares.Operaciones.REGISTRAR_POA_APOYO, userCod)) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_USUARIO_NO_TIENE_PERMISO_REGISTRAR_APOYO_EN_UNIDAD_TECNICA_POA);
                        throw b;
                    }
                    
                    POALite poaAnterior = obtenerHistoricoPorVersion(poa.getId(), poa.getVersion(),POALite.class);
                    Boolean metaModificadaPrimerTrimestre = false;
                    Boolean metaModificadaSegundoTrimestre = false;
                    Boolean metaModificadaTercerTrimestre = false;
                    Boolean metaModificadaCuartoTrimestre = false;
                    Boolean metaModificada = false;
                    if(poaAnterior != null) {
                        for(POAMetaAnual meta: poa.getMetasAnuales()) {
                            POAMetaAnual metaAnterior = obtenerHistoricoPorVersion(meta.getId(), meta.getVersion(),POAMetaAnual.class);
                            if(meta.getMedioVerificacion() != null && meta.getAlcanceLimitaciones() != null && metaAnterior.getMedioVerificacion() != null && metaAnterior.getAlcanceLimitaciones() != null) {
                                if(!meta.getMedioVerificacion().equals(metaAnterior.getMedioVerificacion()) || !meta.getAlcanceLimitaciones().equals(metaAnterior.getAlcanceLimitaciones())) {
                                    metaModificada = true;
                                }
                            }
                            if(meta.getProgramaPrimerTrimestreReal() != null && metaAnterior.getProgramaPrimerTrimestreReal() != null) {
                                if(!meta.getProgramaPrimerTrimestreReal().equals(metaAnterior.getProgramaPrimerTrimestreReal())) {
                                    metaModificadaPrimerTrimestre = true;
                                    meta.setUltFechaModSeguimientoPrimerTrimestre(new Date());
                                }
                            }
                            if(meta.getProgramaSegundoTrimestreReal() != null && metaAnterior.getProgramaSegundoTrimestreReal() != null) {
                                if(!meta.getProgramaSegundoTrimestreReal().equals(metaAnterior.getProgramaSegundoTrimestreReal())) {
                                    metaModificadaSegundoTrimestre = true;
                                    meta.setUltFechaModSeguimientoSegundoTrimestre(new Date());
                                }
                            }

                            if(meta.getProgramaTercerTrimestreReal() != null && metaAnterior.getProgramaTercerTrimestreReal() != null) {
                                if( !meta.getProgramaTercerTrimestreReal().equals(metaAnterior.getProgramaTercerTrimestreReal())) {
                                    metaModificadaTercerTrimestre = true;
                                    meta.setUltFechaModSeguimientoTercerTrimestre(new Date());
                                }
                            }

                            if(meta.getProgramaCuartoTrimestreReal() != null && metaAnterior.getProgramaCuartoTrimestreReal() != null) {
                                if( !meta.getProgramaCuartoTrimestreReal().equals(metaAnterior.getProgramaCuartoTrimestreReal())) {
                                    metaModificadaCuartoTrimestre = true;
                                    meta.setUltFechaModSeguimientoCuartoTrimestre(new Date());
                                }
                            }

                            if(meta.getActividades() != null) {              
                                for(POAActividadMeta act : meta.getActividades()) {
                                    act.setUltimoPeriodoModificado(null);
                                    if(act.getPorcentajeAvancePrimerTrimestre() != null && act.getPorcentajeAvancePrimerTrimestre() > 0) {
                                        act.setUltimoPeriodoModificado(1);
                                    }
                                    if(act.getPorcentajeAvanceSegundoTrimestre() != null && act.getPorcentajeAvanceSegundoTrimestre() > 0) {
                                        act.setUltimoPeriodoModificado(2);
                                    }
                                    if(act.getPorcentajeAvanceTercerTrimestre() != null && act.getPorcentajeAvanceTercerTrimestre() > 0) {
                                        act.setUltimoPeriodoModificado(3);
                                    }
                                    if(act.getPorcentajeAvanceCuartoTrimestre() != null && act.getPorcentajeAvanceCuartoTrimestre() > 0) {
                                        act.setUltimoPeriodoModificado(4);
                                    }
                                    
                                    POAActividadMeta actAnterior = obtenerHistoricoPorVersion(act.getId(), act.getVersion(),POAActividadMeta.class);
                                    
                                    if(act.getPorcentajeAvancePrimerTrimestre() != null && actAnterior.getPorcentajeAvancePrimerTrimestre() != null) {
                                        if(!act.getPorcentajeAvancePrimerTrimestre().equals(actAnterior.getPorcentajeAvancePrimerTrimestre())) {
                                            metaModificadaPrimerTrimestre = true;
                                            meta.setUltFechaModSeguimientoPrimerTrimestre(new Date());
                                        }
                                    }
                                    if(act.getPorcentajeAvanceSegundoTrimestre() != null && actAnterior.getPorcentajeAvanceSegundoTrimestre() != null) {
                                        if(!act.getPorcentajeAvanceSegundoTrimestre().equals(actAnterior.getPorcentajeAvanceSegundoTrimestre())) {
                                            metaModificadaSegundoTrimestre = true;
                                            meta.setUltFechaModSeguimientoSegundoTrimestre(new Date());
                                        }
                                    }

                                    if(act.getPorcentajeAvanceTercerTrimestre() != null && actAnterior.getPorcentajeAvanceTercerTrimestre() != null) {
                                        if(!act.getPorcentajeAvanceTercerTrimestre().equals(actAnterior.getPorcentajeAvanceTercerTrimestre())) {
                                            metaModificadaTercerTrimestre = true;
                                            meta.setUltFechaModSeguimientoTercerTrimestre(new Date());
                                        }
                                    }

                                    if(act.getPorcentajeAvanceCuartoTrimestre() != null && actAnterior.getPorcentajeAvanceCuartoTrimestre() != null) {
                                        if(!act.getPorcentajeAvanceCuartoTrimestre().equals(actAnterior.getPorcentajeAvanceCuartoTrimestre())) {
                                            metaModificadaCuartoTrimestre = true;
                                            meta.setUltFechaModSeguimientoCuartoTrimestre(new Date());
                                        }
                                    }
                                    
                                  /**  if(metaAnterior.getActividades() != null) {
                                        for(POAActividadMeta actAnterior : metaAnterior.getActividades()) {
                                            if(act.getId().equals(actAnterior.getId())) {
                                                if(act.getPorcentajeAvancePrimerTrimestre() != null && actAnterior.getPorcentajeAvancePrimerTrimestre() != null) {
                                                    if(!act.getPorcentajeAvancePrimerTrimestre().equals(actAnterior.getPorcentajeAvancePrimerTrimestre())) {
                                                        metaModificadaPrimerTrimestre = true;
                                                        meta.setUltFechaModSeguimientoPrimerTrimestre(new Date());
                                                    }
                                                }
                                                if(act.getPorcentajeAvanceSegundoTrimestre() != null && actAnterior.getPorcentajeAvanceSegundoTrimestre() != null) {
                                                    if(!act.getPorcentajeAvanceSegundoTrimestre().equals(actAnterior.getPorcentajeAvanceSegundoTrimestre())) {
                                                        metaModificadaSegundoTrimestre = true;
                                                        meta.setUltFechaModSeguimientoSegundoTrimestre(new Date());
                                                    }
                                                }

                                                if(act.getPorcentajeAvanceTercerTrimestre() != null && actAnterior.getPorcentajeAvanceTercerTrimestre() != null) {
                                                    if(!act.getPorcentajeAvanceTercerTrimestre().equals(actAnterior.getPorcentajeAvanceTercerTrimestre())) {
                                                        metaModificadaTercerTrimestre = true;
                                                        meta.setUltFechaModSeguimientoTercerTrimestre(new Date());
                                                    }
                                                }

                                                if(act.getPorcentajeAvanceCuartoTrimestre() != null && actAnterior.getPorcentajeAvanceCuartoTrimestre() != null) {
                                                    if(!act.getPorcentajeAvanceCuartoTrimestre().equals(actAnterior.getPorcentajeAvanceCuartoTrimestre())) {
                                                        metaModificadaCuartoTrimestre = true;
                                                        meta.setUltFechaModSeguimientoCuartoTrimestre(new Date());
                                                    }
                                                }

                                                break;
                                            }
                                        }
                                    }**/
                                }
                            }
                            
                            
                            
                               /** if(meta.getActividades() != null) {
                                    for(POAActividadMeta act : meta.getActividades()) {
                                        if(act.getPorcentajeAvancePrimerTrimestre() != null && act.getPorcentajeAvancePrimerTrimestre() > 0) {
                                            act.setUltimoPeriodoModificado(1);
                                        }
                                        if(act.getPorcentajeAvanceSegundoTrimestre() != null && act.getPorcentajeAvanceSegundoTrimestre() > 0) {
                                            act.setUltimoPeriodoModificado(2);
                                        }
                                        if(act.getPorcentajeAvanceTercerTrimestre() != null && act.getPorcentajeAvanceTercerTrimestre() > 0) {
                                            act.setUltimoPeriodoModificado(3);
                                        }
                                        if(act.getPorcentajeAvanceCuartoTrimestre() != null && act.getPorcentajeAvanceCuartoTrimestre() > 0) {
                                            act.setUltimoPeriodoModificado(4);
                                        }
                                        if(metaAnterior.getActividades() != null) {
                                            for(POAActividadMeta actAnterior : metaAnterior.getActividades()) {
                                                if(act.getId().equals(actAnterior.getId())) {
                                                    if(act.getPorcentajeAvancePrimerTrimestre() != null && actAnterior.getPorcentajeAvancePrimerTrimestre() != null) {
                                                        if(!act.getPorcentajeAvancePrimerTrimestre().equals(actAnterior.getPorcentajeAvancePrimerTrimestre())) {
                                                            metaModificadaPrimerTrimestre = true;
                                                            meta.setUltFechaModSeguimientoPrimerTrimestre(new Date());
                                                        }
                                                    }
                                                    if(act.getPorcentajeAvanceSegundoTrimestre() != null && actAnterior.getPorcentajeAvanceSegundoTrimestre() != null) {
                                                        if(!act.getPorcentajeAvanceSegundoTrimestre().equals(actAnterior.getPorcentajeAvanceSegundoTrimestre())) {
                                                            metaModificadaSegundoTrimestre = true;
                                                            meta.setUltFechaModSeguimientoSegundoTrimestre(new Date());
                                                        }
                                                    }

                                                    if(act.getPorcentajeAvanceTercerTrimestre() != null && actAnterior.getPorcentajeAvanceTercerTrimestre() != null) {
                                                        if(!act.getPorcentajeAvanceTercerTrimestre().equals(actAnterior.getPorcentajeAvanceTercerTrimestre())) {
                                                            metaModificadaTercerTrimestre = true;
                                                            meta.setUltFechaModSeguimientoTercerTrimestre(new Date());
                                                        }
                                                    }

                                                    if(act.getPorcentajeAvanceCuartoTrimestre() != null && actAnterior.getPorcentajeAvanceCuartoTrimestre() != null) {
                                                        if(!act.getPorcentajeAvanceCuartoTrimestre().equals(actAnterior.getPorcentajeAvanceCuartoTrimestre())) {
                                                            metaModificadaCuartoTrimestre = true;
                                                            meta.setUltFechaModSeguimientoCuartoTrimestre(new Date());
                                                        }
                                                    }

                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }**/
                                
                                
                                
                           /** for(POAMetaAnual metaAnterior: poaAnterior.getMetasAnuales()) {
                                if(metaAnterior.equals(meta)) {
                                    if(meta.getMedioVerificacion() != null && meta.getAlcanceLimitaciones() != null && metaAnterior.getMedioVerificacion() != null && metaAnterior.getAlcanceLimitaciones() != null) {
                                        if(!meta.getMedioVerificacion().equals(metaAnterior.getMedioVerificacion()) || !meta.getAlcanceLimitaciones().equals(metaAnterior.getAlcanceLimitaciones())) {
                                            metaModificada = true;
                                        }
                                    }
                                    if(meta.getProgramaPrimerTrimestreReal() != null && metaAnterior.getProgramaPrimerTrimestreReal() != null) {
                                        if(!meta.getProgramaPrimerTrimestreReal().equals(metaAnterior.getProgramaPrimerTrimestreReal())) {
                                            metaModificadaPrimerTrimestre = true;
                                            meta.setUltFechaModSeguimientoPrimerTrimestre(new Date());
                                        }
                                    }
                                    if(meta.getProgramaSegundoTrimestreReal() != null && metaAnterior.getProgramaSegundoTrimestreReal() != null) {
                                        if(!meta.getProgramaSegundoTrimestreReal().equals(metaAnterior.getProgramaSegundoTrimestreReal())) {
                                            metaModificadaSegundoTrimestre = true;
                                            meta.setUltFechaModSeguimientoSegundoTrimestre(new Date());
                                        }
                                    }
                                    
                                    if(meta.getProgramaTercerTrimestreReal() != null && metaAnterior.getProgramaTercerTrimestreReal() != null) {
                                        if( !meta.getProgramaTercerTrimestreReal().equals(metaAnterior.getProgramaTercerTrimestreReal())) {
                                            metaModificadaTercerTrimestre = true;
                                            meta.setUltFechaModSeguimientoTercerTrimestre(new Date());
                                        }
                                    }
                                    
                                    if(meta.getProgramaCuartoTrimestreReal() != null && metaAnterior.getProgramaCuartoTrimestreReal() != null) {
                                        if( !meta.getProgramaCuartoTrimestreReal().equals(metaAnterior.getProgramaCuartoTrimestreReal())) {
                                            metaModificadaCuartoTrimestre = true;
                                            meta.setUltFechaModSeguimientoCuartoTrimestre(new Date());
                                        }
                                    }
                                    
                                    if(meta.getActividades() != null) {
                                        for(POAActividadMeta act : meta.getActividades()) {
                                            if(act.getPorcentajeAvancePrimerTrimestre() != null && act.getPorcentajeAvancePrimerTrimestre() > 0) {
                                                act.setUltimoPeriodoModificado(1);
                                            }
                                            if(act.getPorcentajeAvanceSegundoTrimestre() != null && act.getPorcentajeAvanceSegundoTrimestre() > 0) {
                                                act.setUltimoPeriodoModificado(2);
                                            }
                                            if(act.getPorcentajeAvanceTercerTrimestre() != null && act.getPorcentajeAvanceTercerTrimestre() > 0) {
                                                act.setUltimoPeriodoModificado(3);
                                            }
                                            if(act.getPorcentajeAvanceCuartoTrimestre() != null && act.getPorcentajeAvanceCuartoTrimestre() > 0) {
                                                act.setUltimoPeriodoModificado(4);
                                            }
                                            if(metaAnterior.getActividades() != null) {
                                                for(POAActividadMeta actAnterior : metaAnterior.getActividades()) {
                                                    if(act.getId().equals(actAnterior.getId())) {
                                                        if(act.getPorcentajeAvancePrimerTrimestre() != null && actAnterior.getPorcentajeAvancePrimerTrimestre() != null) {
                                                            if(!act.getPorcentajeAvancePrimerTrimestre().equals(actAnterior.getPorcentajeAvancePrimerTrimestre())) {
                                                                metaModificadaPrimerTrimestre = true;
                                                                meta.setUltFechaModSeguimientoPrimerTrimestre(new Date());
                                                            }
                                                        }
                                                        if(act.getPorcentajeAvanceSegundoTrimestre() != null && actAnterior.getPorcentajeAvanceSegundoTrimestre() != null) {
                                                            if(!act.getPorcentajeAvanceSegundoTrimestre().equals(actAnterior.getPorcentajeAvanceSegundoTrimestre())) {
                                                                metaModificadaSegundoTrimestre = true;
                                                                meta.setUltFechaModSeguimientoSegundoTrimestre(new Date());
                                                            }
                                                        }
                                                        
                                                        if(act.getPorcentajeAvanceTercerTrimestre() != null && actAnterior.getPorcentajeAvanceTercerTrimestre() != null) {
                                                            if(!act.getPorcentajeAvanceTercerTrimestre().equals(actAnterior.getPorcentajeAvanceTercerTrimestre())) {
                                                                metaModificadaTercerTrimestre = true;
                                                                meta.setUltFechaModSeguimientoTercerTrimestre(new Date());
                                                            }
                                                        }
                                                        
                                                        if(act.getPorcentajeAvanceCuartoTrimestre() != null && actAnterior.getPorcentajeAvanceCuartoTrimestre() != null) {
                                                            if(!act.getPorcentajeAvanceCuartoTrimestre().equals(actAnterior.getPorcentajeAvanceCuartoTrimestre())) {
                                                                metaModificadaCuartoTrimestre = true;
                                                                meta.setUltFechaModSeguimientoCuartoTrimestre(new Date());
                                                            }
                                                        }
                                                        
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    break;
                                }
                            }**/

                            if(metaModificada || metaModificadaPrimerTrimestre || metaModificadaSegundoTrimestre || metaModificadaTercerTrimestre || metaModificadaCuartoTrimestre) {
                                meta.setUltFechaModSeguimiento(new Date());
                            }
                        }
                    }
                    
                    if(metaModificada || metaModificadaPrimerTrimestre || metaModificadaSegundoTrimestre || metaModificadaTercerTrimestre || metaModificadaCuartoTrimestre) {
                        poa.setUltFechaModSeguimiento(new Date());
                    }
                    
                }
                if (poa.getId() == null) {
                    poa = (POA) poaDAO.create(poa, du.getCodigoUsuario(), du.getOrigen());
                } else {
                    
                    poa = (POA) poaDAO.update(poa, du.getCodigoUsuario(), du.getOrigen());
                }
            }
            return poa;
        } catch (BusinessException be) {
            //Si es de tipo negocio envía la misma excepción
            throw be;
        } catch (Exception ex) {
            //Las demás excepciones se consideran técnicas
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * @param idPOA
     * @return
     */
    public POA enviarPOA(Integer idPOA) {
        try {
            POA poa = (POA) poaDAO.find(POA.class, idPOA);

            poaValidacion.validar(poa,Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
            
            poa.setEstado(EstadosPOA.ENVIADO);
            
            String contenidoNotificacion = "UT: " + poa.getUnidadTecnica().getNombre() + ", PI: " + poa.getProgramaInstitucional().getNombre() + ", Año: " + poa.getAnio().getAnio();
            
            String opecodigoANotificar = ConstantesEstandares.Operaciones.ENVIAR_PLAN_OPERATIVO_ANUAL;

            List<SsUsuario> anotificar = usuarioDAO.getUsuariosConOperacion(opecodigoANotificar, null);
            for (SsUsuario u : anotificar) {
                NotificacionPOA n = new NotificacionPOA();
                n.setTipo(TipoNotificacion.NUEVO_POA_PARA_EVALUAR);
                n.setUsuario(u);
                n.setObjetoId(poa.getId());
                n.setPoaAnio(poa.getAnio().getId());
                n.setPoaUT(poa.getUnidadTecnica().getId());
                n.setFecha(new Date());
                n.setContenido(contenidoNotificacion);

                generalDAO.update(n);
            }

            poa = (POA) poaDAO.update(poa);
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
    
    public POA cambiarEstadoPOA(POA poa) {
        try {
            poaValidacion.validar(poa, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            
            poa = (POA) poaDAO.update(poa);
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
     * @param idPOA
     * @return
     */
    public POA aprobarPOA(Integer idPOA) {
        try {
            POA poa = (POA) poaDAO.find(POA.class, idPOA);

            poaValidacion.validar(poa, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            
            poa.setEstado(EstadosPOA.APROBADO);
            
            String contenidoNotificacion = "UT: " + poa.getUnidadTecnica().getNombre() + ", PI: " + poa.getProgramaInstitucional().getNombre() + ", Año: " + poa.getAnio().getAnio(); 
            List<SsUsuario> anotificar = usuarioDAO.getUsuariosConUT(poa.getUnidadTecnica().getId(), null);
            for (SsUsuario u : anotificar) {
                NotificacionPOA n = new NotificacionPOA();
                n.setTipo(TipoNotificacion.POA_FUE_APROBADO);
                n.setUsuario(u);
                n.setObjetoId(poa.getId());
                n.setPoaAnio(poa.getAnio().getId());
                n.setPoaUT(poa.getUnidadTecnica().getId());
                n.setFecha(new Date());
                n.setContenido(contenidoNotificacion);

                generalDAO.update(n);
            }

            poa = (POA) poaDAO.update(poa);
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
     * Este método marca un POA como rechazado e indica cuál es el motivo de
     * ello
     *
     * @param idPoa
     * @param motivoRechazo
     */
    public void rechazarPOA(Integer idPoa, String motivoRechazo) {
        try {
            POA poa = (POA) poaDAO.find(POA.class, idPoa);

            poa.setEstado(EstadosPOA.EN_ELABORACION);
            
            List<SsUsuario> anotificar = usuarioDAO.getUsuariosConUT(poa.getUnidadTecnica().getId(), null);
            for (SsUsuario u : anotificar) {
                NotificacionPOA n = new NotificacionPOA();
                n.setTipo(TipoNotificacion.POA_FUE_RECHAZADO);
                n.setContenido(motivoRechazo);
                n.setUsuario(u);
                n.setObjetoId(poa.getId());
                n.setPoaAnio(poa.getAnio().getId());
                n.setPoaUT(poa.getUnidadTecnica().getId());
                n.setFecha(new Date());

                generalDAO.update(n);
            }
            poaDAO.update(poa);
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
     * Devuelve el elemento POA por el ID
     *
     * @param id
     * @return
     * @throws GeneralException
     */
    public POA obtenerPOAPorId(Integer id) throws GeneralException {
        logger.log(Level.INFO, "obtenerPOAPorId");
        try {
            return (POA) poaDAO.findById(POA.class, id);
        } catch (DAOGeneralException ex) {
           logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }
    /**
     * Devuelve todos los elementos de tipo POA
     *
     * @return
     * @throws GeneralException
     */
    public List<POA> obtenerTodos() throws GeneralException {
        logger.log(Level.INFO, "obtenerTodos");
        try {
            return poaDAO.findAll(POA.class);
        } catch (Exception ex) {
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    public List<POA> obtenerPorFiltro(FiltroPOA filtro) {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
           
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(POA.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    public List<POAActividadMeta> obtenerActividadesPorFiltro(FiltroPOA filtro) {
        try {
            List<CriteriaTO> criterios = generarCriteriaActividades(filtro);
           
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(POAActividadMeta.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    public List<POAMetaAnual> obtenerMetasPorFiltro(FiltroPOA filtro) {
        try {
            List<CriteriaTO> criterios = generarCriteriaMetas(filtro);
           
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(POAMetaAnual.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    public Long obtenerTotalPorFiltro(FiltroPOA filtro) throws DAOGeneralException {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);

            CriteriaTO criterio = null;
            if (criterios.size() > 1) {
                criterio = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[criterios.size()]));
            } else if (criterios.size() == 1) {
                criterio = criterios.get(0);
            }
            return generalDAO.countByCriteria(POA.class, criterio, filtro.getFirst(), filtro.getMaxResults());
        } catch (Exception ex) {
            throw new DAOGeneralException(ex);
        }
    }
    
    private List<CriteriaTO> generarCriteria(FiltroPOA filtro) {
        List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if(filtro.getId() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "id", filtro.getId());
                criterios.add(criterio); 
            }
            if (filtro.getAnioFiscalId() != null && filtro.getAnioFiscalId() > 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "anio.id", filtro.getAnioFiscalId());
                criterios.add(criterio);
            }
            if (filtro.getProgramaId() != null && filtro.getProgramaId() > 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "programaInstitucional.id", filtro.getProgramaId());
                criterios.add(criterio);
            }

            if (filtro.getUnidadTecnicaId() != null && filtro.getUnidadTecnicaId() > 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "unidadTecnica.id", filtro.getUnidadTecnicaId());
                criterios.add(criterio);
            }
            if (StringUtils.isNotBlank(filtro.getNombre()) ) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "nombreBusqueda", filtro.getNombre().trim());
                criterios.add(criterio);
            }
            if (filtro.getEstado() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", filtro.getEstado());
                criterios.add(criterio);
            }
            return criterios;
        
    }
    private List<CriteriaTO> generarCriteriaActividades(FiltroPOA filtro) {
        List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if(filtro.getId() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "meta.poa.id", filtro.getId());
                criterios.add(criterio); 
            }
            if (filtro.getAnioFiscalId() != null && filtro.getAnioFiscalId() > 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "meta.poa.anio.id", filtro.getAnioFiscalId());
                criterios.add(criterio);
            }
            if (filtro.getProgramaId() != null && filtro.getProgramaId() > 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "meta.poa.programaInstitucional.id", filtro.getProgramaId());
                criterios.add(criterio);
            }

            if (filtro.getUnidadTecnicaId() != null && filtro.getUnidadTecnicaId() > 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "meta.poa.unidadTecnica.id", filtro.getUnidadTecnicaId());
                criterios.add(criterio);
            }
            if (StringUtils.isNotBlank(filtro.getNombre()) ) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "meta.poa.nombreBusqueda", filtro.getNombre().trim());
                criterios.add(criterio);
            }
            if (filtro.getEstado() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "meta.poa.estado", filtro.getEstado());
                criterios.add(criterio);
            }
            if (filtro.getUltPeriodoHabilitado() != null) {
                if(filtro.getUltPeriodoHabilitado().compareTo(1)==0) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATER, "porcentajeAvancePrimerTrimestre", 0);
                    criterios.add(criterio);
                } else if(filtro.getUltPeriodoHabilitado().compareTo(2)==0) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATER, "porcentajeAvanceSegundoTrimestre", 0);
                    criterios.add(criterio);
                } else if(filtro.getUltPeriodoHabilitado().compareTo(3)==0) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATER, "porcentajeAvanceTercerTrimestre", 0);
                    criterios.add(criterio);
                } else if(filtro.getUltPeriodoHabilitado().compareTo(4)==0) {
                    MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATER, "porcentajeAvanceCuartoTrimestre", 0);
                    criterios.add(criterio);
                }
            }
            
            return criterios;
        
    }
    private List<CriteriaTO> generarCriteriaMetas(FiltroPOA filtro) {
        List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
            if(filtro.getId() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poa.id", filtro.getId());
                criterios.add(criterio); 
            }
            if (filtro.getAnioFiscalId() != null && filtro.getAnioFiscalId() > 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poa.anio.id", filtro.getAnioFiscalId());
                criterios.add(criterio);
            }
            if (filtro.getProgramaId() != null && filtro.getProgramaId() > 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "poa.programaInstitucional.id", filtro.getProgramaId());
                criterios.add(criterio);
            }

            if (filtro.getUnidadTecnicaId() != null && filtro.getUnidadTecnicaId() > 0) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "poa.unidadTecnica.id", filtro.getUnidadTecnicaId());
                criterios.add(criterio);
            }
            if (StringUtils.isNotBlank(filtro.getNombre()) ) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "poa.nombreBusqueda", filtro.getNombre().trim());
                criterios.add(criterio);
            }
            if (filtro.getEstado() != null) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "poa.estado", filtro.getEstado());
                criterios.add(criterio);
            }
            
            return criterios;
        
    }
    
    /**
     * Elimina el objeto POA
     *
     * @return
     * @throws GeneralException
     */
    public void eliminar(Integer id) throws GeneralException {
        logger.log(Level.INFO, "eliminar");
        try {
            POA poa = (POA) generalDAO.findById(POA.class, id);
            generalDAO.delete(poa);
        } catch (Exception ex) {
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }
    /**
     * Devuelve los elementos que satisfacen el criterio ingresado
     *
     * @param cto
     * @param orderBy
     * @param ascending
     * @param startPosition
     * @param cantidad
     * @return
     * @throws GeneralException
     */
    public List<POA> obtenerPorCriteria(CriteriaTO cto, String[] orderBy, boolean[] ascending, Long startPosition, Long cantidad) throws GeneralException {
        logger.log(Level.INFO, "obtenerPorCriteria");
        try {
            return poaDAO.findEntityByCriteria(POA.class, cto, orderBy, ascending, startPosition, cantidad);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);

        }
        return null;
    }
    
    public <T> T obtenerHistoricoPorVersion(int id, int version, Class<T> entityClass) {
        List<T> lista = AuditReader.readAllByVersion(generalDAO.getEntityManager(), entityClass, id, version);
        if(lista != null && !lista.isEmpty() && lista.size() > 0) {
            return lista.get(0);
        }
        return null;
    }
    
    public Collection<RiesgoTOPOA> obtenerRiesgosPorCriteria(FiltroRiesgo filtro) {
        if (filtro != null) {
            try {

                return poaDAO.obtenerRiesgosPorFiltro(filtro);
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
}