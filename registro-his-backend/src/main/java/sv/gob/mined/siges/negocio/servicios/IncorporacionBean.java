/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.dto.SgRegistroIncorporacion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroIncorporacion;
import sv.gob.mined.siges.negocio.validaciones.IncorporacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.IncorporacionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgIncorporacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgNacionalidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgPais;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSexo;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class IncorporacionBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgIncorporacion
     * @throws GeneralException
     */
    public SgIncorporacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgIncorporacion> codDAO = new CodigueraDAO<>(em, SgIncorporacion.class);
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
                CodigueraDAO<SgIncorporacion> codDAO = new CodigueraDAO<>(em, SgIncorporacion.class);
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
     * @param inc SgIncorporacion
     * @return SgIncorporacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgIncorporacion guardar(SgIncorporacion inc) throws GeneralException {
        try {
            if (inc != null) {
                if (IncorporacionValidacionNegocio.validar(inc)) {
                    CodigueraDAO<SgIncorporacion> codDAO = new CodigueraDAO<>(em, SgIncorporacion.class);
                    return codDAO.guardar(inc, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return inc;
    }

    public void generarRegistroIncorporacion(SgRegistroIncorporacion registro) throws GeneralException {
        try {

            SgIncorporacion inc = new SgIncorporacion();

            inc.setIncDui(registro.getDui());
            inc.setIncPrimerNombre(registro.getPrimerNombre());
            inc.setIncSegundoNombre(registro.getSegundoNombre());
            inc.setIncTercerNombre(registro.getTercerNombre());
            inc.setIncPrimerApellido(registro.getPrimerApellido());
            inc.setIncSegundoApellido(registro.getSegundoApellido());
            inc.setIncTercerApellido(registro.getTercerApellido());

            if (registro.getSexoPk() != null && registro.getSexoPk() > 0L) {
                inc.setIncSexo(em.getReference(SgSexo.class, registro.getSexoPk()));
            }
            if (registro.getEstadoFamiliarPk() != null && registro.getEstadoFamiliarPk() > 0L) {
                inc.setIncEstadoCivil(em.getReference(SgEstadoCivil.class, registro.getEstadoFamiliarPk()));
            }
            if (registro.getNacionalidadPk() != null && registro.getNacionalidadPk() > 0L) {
                inc.setIncNacionalidad(em.getReference(SgNacionalidad.class, registro.getNacionalidadPk()));
            }

            if (!StringUtils.isBlank(registro.getNumCarneResidente())) {
                inc.setIncCarneResidente(registro.getNumCarneResidente());
            }

            if (!StringUtils.isBlank(registro.getNumPasaporte())) {
                inc.setIncPasaporte(registro.getNumPasaporte());
                if (registro.getPaisPasaportePk() != null && registro.getPaisPasaportePk() > 0L) {
                    inc.setIncPasaportePaisEmisor(em.getReference(SgPais.class, registro.getPaisPasaportePk()));
                } 
            }
            
            inc.setIncNombreSede(registro.getNombreSede());
            inc.setIncCiudad(registro.getCiudad());
            inc.setIncUltimoGradoEstudio(registro.getUltimoGradoEstudio());
            inc.setIncAnioEstudio(registro.getAnioEstudio());
            inc.setIncNumeroTramite(registro.getNumTramite());
            inc.setIncNumeroResolucion(registro.getNumResolucion());
            inc.setIncFechaAprobacion(registro.getFechaAprobacion());
            inc.setIncFechaNacimiento(registro.getFechaNacimiento());

            this.guardar(inc);
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
    public Long obtenerTotalPorFiltro(FiltroIncorporacion filtro) throws GeneralException {
        try {
            IncorporacionDAO incDAO = new IncorporacionDAO(em);
            return incDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgIncorporacion>
     * @throws GeneralException
     */
    public List<SgIncorporacion> obtenerPorFiltro(FiltroIncorporacion filtro) throws GeneralException {
        try {
            IncorporacionDAO incDAO = new IncorporacionDAO(em);
            return incDAO.obtenerPorFiltro(filtro);
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
                CodigueraDAO<SgIncorporacion> codDAO = new CodigueraDAO<>(em, SgIncorporacion.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
