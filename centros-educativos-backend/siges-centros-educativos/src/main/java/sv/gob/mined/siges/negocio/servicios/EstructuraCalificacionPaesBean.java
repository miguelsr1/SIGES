/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumTipoPruebaPaes;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroComponentePlanEstudio;
import sv.gob.mined.siges.filtros.FiltroEstructuraCalificacionPaes;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.EstructuraCalificacionPaesValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EstructuraCalificacionPaesDAO;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanEstudio;
import sv.gob.mined.siges.persistencia.entidades.SgEstructuraCalificacionPaes;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class EstructuraCalificacionPaesBean {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private ComponentesPlanEstudioBean componentePlanEstudioBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEstructuraCalificacionPaes
     * @throws GeneralException
     */
    public SgEstructuraCalificacionPaes obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstructuraCalificacionPaes> codDAO = new CodigueraDAO<>(em, SgEstructuraCalificacionPaes.class);
                return codDAO.obtenerPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEstructuraCalificacionPaes> codDAO = new CodigueraDAO<>(em, SgEstructuraCalificacionPaes.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param ecp SgEstructuraCalificacionPaes
     * @return SgEstructuraCalificacionPaes
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEstructuraCalificacionPaes guardar(SgEstructuraCalificacionPaes ecp) throws GeneralException {
        try {
            if (ecp != null) {
                if (EstructuraCalificacionPaesValidacionNegocio.validar(ecp)) {
                    CodigueraDAO<SgEstructuraCalificacionPaes> codDAO = new CodigueraDAO<>(em, SgEstructuraCalificacionPaes.class);
                    
                    SgComponentePlanEstudio componente = null;
                    Integer codigoInt = null;
                    try {
                        codigoInt = Integer.parseInt(ecp.getEcpCodigoCpe());
                    } catch (NumberFormatException ex) {
                        //Luego se muestra el error al procesar estructura
                        BusinessException be = new BusinessException();
                        be.addError(Errores.ERROR_CODIGO_NO_NUMERICO);
                        throw be;
                        
                    }
                    
                    FiltroComponentePlanEstudio fcpe = new FiltroComponentePlanEstudio();
                    fcpe.setCpeCodigoExterno(codigoInt);
                    fcpe.setMaxResults(1L);
                    fcpe.setCpeEsPaes(Boolean.TRUE);
                    fcpe.setIncluirCampos(new String[]{"cpePk", "cpeTipo", "cpeVersion", "cpeTipoPaes"});
                    List<SgComponentePlanEstudio> componentePAESList = componentePlanEstudioBean.obtenerPorFiltro(fcpe);
                    if (!componentePAESList.isEmpty()) {
                        componente = componentePAESList.get(0);
                        
                        JsonWebToken jwt = Lookup.obtenerJWT();
                        Boolean puedeProcesarPaes = jwt.getGroups().contains(ConstantesOperaciones.HABILITADO_CALIFICACIONES_PAES);
                        Boolean puedeProcesarExternas = jwt.getGroups().contains(ConstantesOperaciones.HABILITADO_CALIFICACIONES_EXTERNAS);
                        
                        if (EnumTipoPruebaPaes.EXTERNA.equals(componente.getCpeTipoPaes())) {
                            
                            if (!puedeProcesarExternas) {
                                BusinessException be = new BusinessException();
                                be.addError(Errores.ERROR_PERMISOS_INSUFICIENTES_CALIFICACIONES_EXTERNAS);
                                throw be;
                                
                            }
                            
                        } else if (EnumTipoPruebaPaes.PAES.equals(componente.getCpeTipoPaes())) {
                            
                            if (!puedeProcesarPaes) {
                                BusinessException be = new BusinessException();
                                be.addError(Errores.ERROR_PERMISOS_INSUFICIENTES_CALIFICACIONES_PAES);
                                throw be;
                                
                            }
                            
                        } else {
                            
                            BusinessException be = new BusinessException();
                            be.addError(Errores.ERROR_TIPO_PAES_VACIO);
                            throw be;
                            
                        }
                        
                    } else {
                        BusinessException be = new BusinessException();
                        be.addError(Errores.ERROR_NO_EXISTE_COMPONENTE_PLAN_ESTUDIO_PAES_PARA_CODIGO_INGRESADO);
                        throw be;
                    }
                    
                    return codDAO.guardar(ecp, null);
                    
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        
        return ecp;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroEstructuraCalificacionPaes
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroEstructuraCalificacionPaes filtro) throws GeneralException {
        try {
            EstructuraCalificacionPaesDAO codDAO = new EstructuraCalificacionPaesDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroEstructuraCalificacionPaes
     * @return Lista de <SgEstructuraCalificacionPaes>
     * @throws GeneralException
     */
    public List<SgEstructuraCalificacionPaes> obtenerPorFiltro(FiltroEstructuraCalificacionPaes filtro) throws GeneralException {
        try {
            EstructuraCalificacionPaesDAO codDAO = new EstructuraCalificacionPaesDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                
                FiltroEstructuraCalificacionPaes filtro = new FiltroEstructuraCalificacionPaes();
                filtro.setIncluirCampos(new String[]{"ecpCodigoCpe"});
                filtro.setEstructuraPk(id);
                filtro.setMaxResults(1L);
                List<SgEstructuraCalificacionPaes> est = this.obtenerPorFiltro(filtro);
                
                if (!est.isEmpty()) {
                    SgEstructuraCalificacionPaes ecp = est.get(0);
                    SgComponentePlanEstudio componente = null;
                    Integer codigoInt = null;
                    try {
                        codigoInt = Integer.parseInt(ecp.getEcpCodigoCpe());
                        
                        FiltroComponentePlanEstudio fcpe = new FiltroComponentePlanEstudio();
                        fcpe.setCpeCodigoExterno(codigoInt);
                        fcpe.setMaxResults(1L);
                        fcpe.setCpeEsPaes(Boolean.TRUE);
                        fcpe.setIncluirCampos(new String[]{"cpePk", "cpeTipo", "cpeVersion", "cpeTipoPaes"});
                        List<SgComponentePlanEstudio> componentePAESList = componentePlanEstudioBean.obtenerPorFiltro(fcpe);
                        if (!componentePAESList.isEmpty()) {
                            componente = componentePAESList.get(0);
                            
                            JsonWebToken jwt = Lookup.obtenerJWT();
                            Boolean puedeProcesarPaes = jwt.getGroups().contains(ConstantesOperaciones.HABILITADO_CALIFICACIONES_PAES);
                            Boolean puedeProcesarExternas = jwt.getGroups().contains(ConstantesOperaciones.HABILITADO_CALIFICACIONES_EXTERNAS);
                            
                            if (EnumTipoPruebaPaes.EXTERNA.equals(componente.getCpeTipoPaes())) {
                                
                                if (!puedeProcesarExternas) {
                                    BusinessException be = new BusinessException();
                                    be.addError(Errores.ERROR_PERMISOS_INSUFICIENTES_CALIFICACIONES_EXTERNAS);
                                    throw be;
                                    
                                }
                                
                            } else if (EnumTipoPruebaPaes.PAES.equals(componente.getCpeTipoPaes())) {
                                
                                if (!puedeProcesarPaes) {
                                    BusinessException be = new BusinessException();
                                    be.addError(Errores.ERROR_PERMISOS_INSUFICIENTES_CALIFICACIONES_PAES);
                                    throw be;
                                    
                                }
                                
                            } else {
                                
                                BusinessException be = new BusinessException();
                                be.addError(Errores.ERROR_TIPO_PAES_VACIO);
                                throw be;
                                
                            }
                            
                        }
                        
                    } catch (NumberFormatException ex) {
                    }
                    
                }
                
                Query q = em.createNativeQuery("delete from centros_educativos.sg_estructura_calificacion_paes where ecp_pk=:ecppk");
                q.setParameter("ecppk", id);
                q.executeUpdate();
                em.flush();
                em.clear();
                
            } catch (BusinessException be) {
                throw be;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
    
    public List<SgEstructuraCalificacionPaes> obtenerHistorialPorId(Long id) throws GeneralException {
        try {
            
            Query q = em.createNativeQuery("select "
                    + "  ecp_nie,ecp_calificacion,ecp_codigo_cpe,ecp_ult_mod_fecha,ecp_ult_mod_usuario,ecp_version "
                    + "from "
                    + "  centros_educativos.sg_estructura_calificacion_paes_aud "
                    + "where "
                    + " ecp_pk=:ecp_pk");
            q.setParameter("ecp_pk", id);
            
            List<Object[]> objs = (List<Object[]>) q.getResultList();
            
            List<SgEstructuraCalificacionPaes> estructuraHist = new ArrayList();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSX");
            if (!objs.isEmpty()) {
                for (Object[] obj : objs) {
                    SgEstructuraCalificacionPaes est = new SgEstructuraCalificacionPaes();
                    est.setEcpNie(obj[0] != null ? obj[0].toString() : null);
                    est.setEcpCalificacion(obj[1] != null ? obj[1].toString() : null);
                    est.setEcpCodigoCpe(obj[2] != null ? obj[2].toString() : null);
                    String fecha = null;
                    if (obj[3] != null) {
                        fecha = obj[3].toString();
                        String inputModified = fecha.replace(" ", "T");
                        int lengthOfAbbreviatedOffset = 3;
                        if (inputModified.indexOf("+") == (inputModified.length() - lengthOfAbbreviatedOffset)) {
                            // If third character from end is a PLUS SIGN, append ':00'.
                            inputModified = inputModified + ":00";
                        }
                        if (inputModified.indexOf("-") == (inputModified.length() - lengthOfAbbreviatedOffset)) {
                            // If third character from end is a PLUS SIGN, append ':00'.
                            inputModified = inputModified + ":00";
                        }
                        fecha = inputModified;
                    }
                    est.setEcpUltModFecha(obj[3] != null ? LocalDateTime.parse(fecha) : null);
                    est.setEcpUltModUsuario(obj[4] != null ? obj[4].toString() : null);
                    est.setEcpVersion(((Integer) obj[5]));
                    estructuraHist.add(est);
                }
                
            }
            return estructuraHist;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
}
