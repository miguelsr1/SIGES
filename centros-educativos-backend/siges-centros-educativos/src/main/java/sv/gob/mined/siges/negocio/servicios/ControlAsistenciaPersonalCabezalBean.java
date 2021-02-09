/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroControlAsistenciaPersonalCabezal;
import sv.gob.mined.siges.negocio.validaciones.ControlAsistenciaPersonalCabezalValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ControlAsistenciaPersonalCabezalDAO;
import sv.gob.mined.siges.persistencia.entidades.SgControlAsistenciaPersonalCabezal;

@Stateless
@Traced
public class ControlAsistenciaPersonalCabezalBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeguridadBean seguridadBean;
    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id
     * @return SgControlAsistenciaPersonalCabezal
     * @throws GeneralException
     */
    public SgControlAsistenciaPersonalCabezal obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgControlAsistenciaPersonalCabezal> codDAO = new CodigueraDAO<>(em, SgControlAsistenciaPersonalCabezal.class);
                return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id
     * @return Boolean
     * @throws GeneralException
     */
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgControlAsistenciaPersonalCabezal> codDAO = new CodigueraDAO<>(em, SgControlAsistenciaPersonalCabezal.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity
     * @return SgControlAsistenciaPersonalCabezal
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgControlAsistenciaPersonalCabezal guardar(SgControlAsistenciaPersonalCabezal entity) throws GeneralException {
        try {
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion

                if (ControlAsistenciaPersonalCabezalValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgControlAsistenciaPersonalCabezal> codDAO = new CodigueraDAO<>(em, SgControlAsistenciaPersonalCabezal.class);
                    entity.setCpcPersonalPresente((int) entity.getCpcAsistenciaPersonal().stream().filter(a -> !a.getApeInasistencia()).count());
                    entity.setCpcPersonalAusentesJustificados((int) entity.getCpcAsistenciaPersonal().stream().filter(a -> BooleanUtils.isTrue(a.getApeInasistencia()) && (a.getApeMotivoInasistencia() != null ? a.getApeMotivoInasistencia().getMipMotivoJustificado():Boolean.FALSE)).count());
                    entity.setCpcPersonalAusentesSinJustificar((int) entity.getCpcAsistenciaPersonal().stream().filter(a -> BooleanUtils.isTrue(a.getApeInasistencia()) && (a.getApeMotivoInasistencia() != null ? (!a.getApeMotivoInasistencia().getMipMotivoJustificado()):Boolean.FALSE)).count());
                    entity.setCpcPersonalEnLista(entity.getCpcAsistenciaPersonal().size());
                    
                    return codDAO.guardar(entity, entity.getCpcPk() == null ? ConstantesOperaciones.CREAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL : ConstantesOperaciones.ACTUALIZAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroControlAsistenciaPersonalCabezal filtro) throws GeneralException {
        try {
            ControlAsistenciaPersonalCabezalDAO codDAO = new ControlAsistenciaPersonalCabezalDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro
     * @return List
     * @throws GeneralException
     */
    public List<SgControlAsistenciaPersonalCabezal> obtenerPorFiltro(FiltroControlAsistenciaPersonalCabezal filtro) throws GeneralException {
        try {
            ControlAsistenciaPersonalCabezalDAO codDAO = new ControlAsistenciaPersonalCabezalDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgControlAsistenciaPersonalCabezal> codDAO = new CodigueraDAO<>(em, SgControlAsistenciaPersonalCabezal.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_CONTROL_ASISTENCIA_PERSONAL_CABEZAL);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
