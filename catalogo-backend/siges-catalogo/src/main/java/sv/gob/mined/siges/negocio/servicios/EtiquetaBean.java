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
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroEtiqueta;
import sv.gob.mined.siges.negocio.validaciones.EtiquetaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EtiquetaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEtiqueta;

@Stateless
public class EtiquetaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgEtiqueta
     * @throws GeneralException
     */
    public SgEtiqueta obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgEtiqueta> codDAO = new CodigueraDAO<>(em, SgEtiqueta.class);
                return codDAO.obtenerPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve las etiquetas en el formato necesario para un Resource Bundle
     *
     * @return Response Object[][]
     * @throws GeneralException
     */
    public Object[][] obtenerParaResourceBundle() throws GeneralException {
        try {
            EtiquetaDAO etiDAO = new EtiquetaDAO(em);
            return etiDAO.obtenerParaResourceBundle();
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
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
                CodigueraDAO<SgEtiqueta> codDAO = new CodigueraDAO<>(em, SgEtiqueta.class);
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
     * @param eti SgEtiqueta
     * @return SgEtiqueta
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgEtiqueta guardar(SgEtiqueta eti) throws GeneralException {
        try {
            if (eti != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (EtiquetaValidacionNegocio.validar(eti)) {
                    CodigueraDAO<SgEtiqueta> codDAO = new CodigueraDAO<>(em, SgEtiqueta.class
                    );
                    boolean guardar = true;
                    if (eti.getEtiPk() != null) {
                        //Si el dato ya fue guardado, se determina que haya cambiado alguno de los valores. En caso contrario no se guarda
                        Object valorAnt = ch.obtenerEnVersion(eti.getClass(), eti.getEtiPk(), eti.getEtiVersion());
                        SgEtiqueta valorAnterior = (SgEtiqueta) valorAnt;
                        guardar = !EtiquetaValidacionNegocio.compararParaGrabar(valorAnterior, eti);
                    }
                    if (guardar) {
                        return codDAO.guardar(eti);
                    }
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return eti;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroEtiqueta filtro) throws GeneralException {
        try {
            EtiquetaDAO codDAO = new EtiquetaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroEtiqueta
     * @return Lista de SgEtiqueta 
     * @throws GeneralException
     */
    public List<SgEtiqueta> obtenerPorFiltro(FiltroEtiqueta filtro) throws GeneralException {
        try {
            EtiquetaDAO codDAO = new EtiquetaDAO(em);
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
                CodigueraDAO<SgEtiqueta> codDAO = new CodigueraDAO<>(em, SgEtiqueta.class
                );
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }
}
