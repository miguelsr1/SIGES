/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroChequera;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.ChequeraValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.ChequeraDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgChequera;

/**
 * Servicio que gestiona las Chequeras
 *
 * @author Sofis Solutions
 */
@Stateless
public class ChequeraBean {

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private SeguridadBean seguridadBean;
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroChequera
     * @return Lista de <SgChequera>
     * @throws GeneralException
     */
    public List<SgChequera> obtenerPorFiltro(FiltroChequera filtro) throws GeneralException {
        try {
            ChequeraDAO codDAO = new ChequeraDAO(em,seguridadBean);
            return codDAO.obtenerPorFiltro(filtro,filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgChequeras
     * @throws GeneralException
     */
    public SgChequera obtenerPorIdLazy(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgChequera> codDAO = new CodigueraDAO<>(em, SgChequera.class);
                return codDAO.obtenerPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgChequera
     * @return SgChequera
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgChequera guardar(SgChequera entity, Boolean dataSecurity) throws GeneralException {
        try {
            BusinessException ge = new BusinessException();
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                if (ChequeraValidacionNegocio.validar(entity)) {
                    CodigueraDAO<SgChequera> codDAO = new CodigueraDAO<>(em, SgChequera.class);

                    // Se verifica que el pk venga nulo , si es nulo es nuevo
                    if (entity.getChePk() == null) {

                        // se valida que la nueva chequera no exista para esa cuenta y serie.
                        FiltroChequera che = new FiltroChequera();
                        che.setCuentaBancaria(entity.getCheCuentaBancariaFk().getCbcPk());
                        che.setCheSerie(entity.getCheSerie());
                        che.setIncluirCampos(new String[]{
                            "chePk",
                            "cheCuentaBancariaFk.cbcPk",
                            "cheCuentaBancariaFk.cbcNumeroCuenta",
                            "cheCuentaBancariaFk.cbcVersion",
                            "cheSerie",
                            "cheNumeroInicial",
                            "cheNumeroFinal",
                            "cheVersion"});
                        List<SgChequera> listChequeras = obtenerPorFiltro(che);

                        //Se verifica que la lista de la busqueda venga vacia, si
                        //viene vacia se guarda
                        if (!listChequeras.isEmpty()) {
                            Integer existe = 0; //bandera
                            //se recorre a lo largo de la lista.
                            for (int i = 0; i < listChequeras.size(); i++) {
                                SgChequera cheq = listChequeras.get(i);
                                //Se valida que para esa chequera el rango sea válido.
                                if (entity.getCheNumeroInicial() >= cheq.getCheNumeroInicial() && entity.getCheNumeroInicial() <= cheq.getCheNumeroFinal()) {
                                    existe = existe + 1;
                                    //mensaje de error
                                    ge.addError("rangoInvalido", Errores.ERROR_CHEQUERA_RANGO_INVALIDO);
                                    break;
                                }
                            }
                            // si no encontro ninguna guarda.
                            if (existe == 0) {
                                if (BooleanUtils.isTrue(dataSecurity)) {
                                    return codDAO.guardar(entity, entity.getChePk() == null ? ConstantesOperaciones.CREAR_CHEQUERA : ConstantesOperaciones.ACTUALIZAR_CHEQUERA);
                                } else {
                                    return codDAO.guardar(entity, null);
                                }
                            }
                            // Viene vacia Guarda
                        } else {
                            if (BooleanUtils.isTrue(dataSecurity)) {
                                return codDAO.guardar(entity, entity.getChePk() == null ? ConstantesOperaciones.CREAR_CHEQUERA : ConstantesOperaciones.ACTUALIZAR_CHEQUERA);
                            } else {
                                return codDAO.guardar(entity, null);
                            }
                        }

                        // si pk no es nulo entonces ya existe
                    } else {
                        // se obtiene el valor ya guardado
                        SgChequera chequera = obtenerPorId(entity.getChePk(), true);
                        //  se comparan los datos criticos si son iguales solo guarda aquellos no evaluados
                        if (chequera.getCheCuentaBancariaFk().getCbcPk().equals(entity.getCheCuentaBancariaFk().getCbcPk())
                                && chequera.getCheSerie().equals(entity.getCheSerie())
                                && chequera.getCheNumeroInicial().equals(entity.getCheNumeroInicial())
                                && chequera.getCheNumeroFinal().equals(entity.getCheNumeroFinal())) {
                            if (BooleanUtils.isTrue(dataSecurity)) {
                                return codDAO.guardar(entity, entity.getChePk() == null ? ConstantesOperaciones.CREAR_CHEQUERA : ConstantesOperaciones.ACTUALIZAR_CHEQUERA);
                            } else {
                                return codDAO.guardar(entity, null);
                            }

                        } else {
                            ge.addError("valoresFIjos", Errores.ERROR_CHEQUERA_VALORES_FIJOS);
                        }
                    }
                }
            }
            if (ge.getErrores().size() > 0) {
                throw ge;
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
     * @param filtro FiltroChequera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroChequera filtro) throws GeneralException {
        try {
            ChequeraDAO codDAO = new ChequeraDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro,filtro.getSecurityOperation());
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
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgChequera> codDAO = new CodigueraDAO<>(em, SgChequera.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_CHERQUERA);
                } else {
                    return codDAO.objetoExistePorId(id, null);
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
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
                CodigueraDAO<SgChequera> codDAO = new CodigueraDAO<>(em, SgChequera.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_CHEQUERA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    public SgChequera obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgChequera> codDAO = new CodigueraDAO<>(em, SgChequera.class);
                if (BooleanUtils.isTrue(dataSecurity)) {
                    return codDAO.obtenerPorId(id, ConstantesOperaciones.BUSCAR_CHERQUERA);
                } else {
                    return codDAO.obtenerPorId(id, null);
                }
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

}
