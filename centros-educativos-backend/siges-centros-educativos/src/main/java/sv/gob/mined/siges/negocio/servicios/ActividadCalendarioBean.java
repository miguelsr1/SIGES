/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroActividadCalendario;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.filtros.FiltroCodiguera;
import sv.gob.mined.siges.filtros.FiltroControlAsistenciaCabezal;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.ActividadCalendarioValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.ActividadCalendarioDAO;
import sv.gob.mined.siges.persistencia.entidades.SgActividadCalendario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgConfiguracion;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
@Traced
public class ActividadCalendarioBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private ControlAsistenciaCabezalBean controlAsistenciaCabezalBean;
    
    @Inject
    private ConfiguracionBean configuracionBean;
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgActividadCalendario
     * @throws GeneralException
     */
    public SgActividadCalendario obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgActividadCalendario> codDAO = new CodigueraDAO<>(em, SgActividadCalendario.class);
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgActividadCalendario> codDAO = new CodigueraDAO<>(em, SgActividadCalendario.class);
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
     * @param aca SgActividadCalendario
     * @return SgActividadCalendario
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgActividadCalendario guardar(SgActividadCalendario aca) throws GeneralException {
        try {
            if (aca != null) {
                if (ActividadCalendarioValidacionNegocio.validar(aca)) {
                    
                    FiltroCodiguera fc = new FiltroCodiguera();
                    fc.setCodigo(Constantes.HABILITAR_CONTROL_DIA_LECTIVO);
                    List<SgConfiguracion> confList = configuracionBean.obtenerPorFiltro(fc);
                    Boolean acitvadoDiaLectivo=Boolean.FALSE;
                    if(!confList.isEmpty()){
                        SgConfiguracion conf=confList.get(0);
                        if(conf.getConValor().equals("1")){
                            acitvadoDiaLectivo=Boolean.TRUE;
                        }
                    }
                    if(BooleanUtils.isFalse(aca.getAcaDiaLectivo()) && acitvadoDiaLectivo){
                        FiltroControlAsistenciaCabezal fca= new FiltroControlAsistenciaCabezal();
                        fca.setCacDesde(aca.getAcaFechaDesde());
                        fca.setCacHasta(aca.getAcaFechaHasta());
                        fca.setSecAnioLectivoFk(aca.getAcaCalendario().getCalAnioLectivo().getAlePk());
                        fca.setSecTipoCalendario(aca.getAcaCalendario().getCalTipoCalendario().getTcePk());
                        if(aca.getAcaAmbito().equals(EnumAmbito.SEDE)){                        
                            fca.setSecSedeFk(aca.getAcaSede().getSedPk());
                        }
                        if(aca.getAcaAmbito().equals(EnumAmbito.DEPARTAMENTAL)){
                            fca.setDepartamento(aca.getAcaDepartamento().getDepPk());
                        }   
                        fca.setMaxResults(1L);
                        Long i=controlAsistenciaCabezalBean.obtenerTotalPorFiltro(fca);
                        if(i.compareTo(0L)!=0){
                            BusinessException ge = new BusinessException();
                            ge.addError("acaNombre", Errores.ERROR_EXISTE_CONTROL_ASISTENCIA_PARA_FECHA);
                            throw ge;
                        }
                        
                    }
                    CodigueraDAO<SgActividadCalendario> codDAO = new CodigueraDAO<>(em, SgActividadCalendario.class);
                    return codDAO.guardar(aca, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return aca;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroActividadCalendario filtro) throws GeneralException {
        try {
            ActividadCalendarioDAO actDAO = new ActividadCalendarioDAO(em, seguridadBean);
            return actDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ACTIVIDAD_CALENDARIO);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgActividadCalendario
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<SgActividadCalendario> obtenerPorFiltro(FiltroActividadCalendario filtro) throws GeneralException {
        try {
            ActividadCalendarioDAO actDAO = new ActividadCalendarioDAO(em, seguridadBean);
            return actDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_ACTIVIDAD_CALENDARIO);
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
                CodigueraDAO<SgActividadCalendario> codDAO = new CodigueraDAO<>(em, SgActividadCalendario.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
