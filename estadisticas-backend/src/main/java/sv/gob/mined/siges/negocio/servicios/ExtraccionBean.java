/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.SgDuplicarExtraccion;
import sv.gob.mined.siges.enumerados.EnumEstadoExtraccion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroExtraccion;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.ExtraccionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.ExtraccionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAlcanceExtraccion;
import sv.gob.mined.siges.persistencia.entidades.SgExtraccion;
import sv.gob.mined.siges.persistencia.entidades.SgGradoReportaEn;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class ExtraccionBean {

    @PersistenceContext(name = Constantes.MAIN_DATASOURCE)
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgExtraccion
     * @throws GeneralException
     */
    public SgExtraccion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgExtraccion> codDAO = new CodigueraDAO<>(em, SgExtraccion.class);
                SgExtraccion ext = codDAO.obtenerPorId(id);
                if (ext.getExtAlcance() != null) {
                    ext.getExtAlcance().size();
                }
                if (ext.getExtGradoReportaEn() != null) {
                    ext.getExtGradoReportaEn().size();
                }
                return ext;
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
                CodigueraDAO<SgExtraccion> codDAO = new CodigueraDAO<>(em, SgExtraccion.class);
                return codDAO.objetoExistePorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param ese SgExtraccion
     * @return SgExtraccion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgExtraccion guardar(SgExtraccion ese) throws GeneralException {
        try {
            if (ese != null) {
                if (ExtraccionValidacionNegocio.validar(ese)) {
                    CodigueraDAO<SgExtraccion> codDAO = new CodigueraDAO<>(em, SgExtraccion.class);
                    ese.setExtEstado(EnumEstadoExtraccion.PENDIENTE);
                    return codDAO.guardar(ese);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return ese;
    }

    /**
     * Duplica la extracción para otro año
     *
     * @param extPk Long
     * @param anio Integer
     * @return SgExtraccion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgExtraccion duplicarExtraccionParaOtroAnio(SgDuplicarExtraccion dup) {
        try {

            if (dup.getExtPk() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DATO_VACIO);
                throw be;
            }

            if (dup.getNuevoAnio() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_ANIO_VACIO);
                throw be;
            }

            CodigueraDAO<SgExtraccion> codDAO = new CodigueraDAO<>(em, SgExtraccion.class);

            SgExtraccion ext = em.find(SgExtraccion.class, dup.getExtPk());
            ext.getExtAlcance().size();
            ext.getExtGradoReportaEn().size();

            em.detach(ext);

            List<SgAlcanceExtraccion> alcance = new ArrayList<>();
            List<SgGradoReportaEn> reportaEn = new ArrayList<>();

            for (SgAlcanceExtraccion c : ext.getExtAlcance()) {
                c.setAlcPk(null);
                c.setAlcVersion(null);
                c.setAlcExtraccion(ext);
                c.setAlcFechaMatriculas(c.getAlcFechaMatriculas().withYear(dup.getNuevoAnio()));
                alcance.add(c);
            }

            for (SgGradoReportaEn c : ext.getExtGradoReportaEn()) {
                c.setRepPk(null);
                c.setRepVersion(null);
                c.setRepExtraccion(ext);
                reportaEn.add(c);
            }

            ext.setExtPk(null);
            ext.setExtVersion(null);
            ext.setExtAnio(dup.getNuevoAnio());
            ext.setExtEstado(EnumEstadoExtraccion.PENDIENTE);
            ext.setExtAlcance(alcance);
            ext.setExtGradoReportaEn(reportaEn);
            ext.setExtNombre(dup.getNuevoNombre());
            ext.setExtDescripcion(dup.getNuevaDescripcion());

            ExtraccionValidacionNegocio.validar(ext);
            
            return codDAO.guardar(ext);

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroExtraccion filtro) throws GeneralException {
        try {
            ExtraccionDAO codDAO = new ExtraccionDAO(em);
            return codDAO.cantidadTotal(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgExtraccion>
     * @throws GeneralException
     */
    public List<SgExtraccion> obtenerPorFiltro(FiltroExtraccion filtro) throws GeneralException {
        try {
            ExtraccionDAO codDAO = new ExtraccionDAO(em);
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
                SgExtraccion e = em.find(SgExtraccion.class, id);

                switch (e.getExtDataset().getDatCodigo()) {
                    case Constantes.DATASET_ESTUDIANTES_CODIGO:
                        em.createNativeQuery("DELETE FROM estadisticas.sg_ext_est_tipos_parentesco where tpa_ext_cabezal_fk = :cabezalPk")
                                .setParameter("cabezalPk", id)
                                .executeUpdate();
                        em.createNativeQuery("DELETE FROM estadisticas.sg_ext_est_discapacidades where dis_ext_cabezal_fk = :cabezalPk")
                                .setParameter("cabezalPk", id)
                                .executeUpdate();
                        em.createNativeQuery("DELETE FROM estadisticas.sg_ext_estudiantes where ext_cabezal_fk = :cabezalPk")
                                .setParameter("cabezalPk", id)
                                .executeUpdate();
                        break;
                    case Constantes.DATASET_DOCENTES_CODIGO:
                        em.createNativeQuery("DELETE FROM estadisticas.sg_ext_pers_tipo_sede where pts_ext_cabezal_fk = :cabezalPk")
                                .setParameter("cabezalPk", id)
                                .executeUpdate();
                        em.createNativeQuery("DELETE FROM estadisticas.sg_ext_pers_sec where sec_ext_cabezal_fk = :cabezalPk")
                                .setParameter("cabezalPk", id)
                                .executeUpdate();
                        em.createNativeQuery("DELETE FROM estadisticas.sg_ext_personal where ext_cabezal_fk = :cabezalPk")
                                .setParameter("cabezalPk", id)
                                .executeUpdate();
                        break;
                    case Constantes.DATASET_SEDES_CODIGO:
                        em.createNativeQuery("DELETE FROM estadisticas.sg_ext_sede_servicios_basicos where sba_ext_cabezal_fk = :cabezalPk")
                                .setParameter("cabezalPk", id)
                                .executeUpdate();
                        em.createNativeQuery("DELETE FROM estadisticas.sg_ext_sedes where ext_cabezal_fk = :cabezalPk")
                                .setParameter("cabezalPk", id)
                                .executeUpdate();
                        break;
                }

                em.remove(e);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Procesa extracciones pendientes
     *
     * @throws GeneralException
     */
    public void procesarExtraccionesPendientes() throws GeneralException {
        try {
            ExtraccionDAO codDAO = new ExtraccionDAO(em);
            codDAO.procesarExtraccionesPendientes();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }
}
