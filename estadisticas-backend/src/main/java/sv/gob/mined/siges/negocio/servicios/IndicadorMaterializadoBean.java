/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.EstGenerica;
import sv.gob.mined.siges.enumerados.EnumDesagregacion;
import sv.gob.mined.siges.enumerados.EnumTipoNumericoValorEstadistica;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroEstadisticas;
import sv.gob.mined.siges.filtros.FiltroIndicadorMaterializado;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.EstadisticasIndicadorMaterializadoDAO;
import sv.gob.mined.siges.persistencia.daos.IndicadorMaterializadoDAO;
import sv.gob.mined.siges.persistencia.entidades.SgEstIndicador;
import sv.gob.mined.siges.persistencia.entidades.SgIndicadorMaterializado;
import sv.gob.mined.siges.persistencia.entidades.SgTuplaIndicadorMaterializado;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEstNombreExtraccion;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class IndicadorMaterializadoBean {

    @PersistenceContext(name = Constantes.MAIN_DATASOURCE)
    private EntityManager em;

    @Inject
    private EstadisticasEstudiantesBean estadisticasEstudiantesBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgIndicador
     * @throws GeneralException
     */
    public SgIndicadorMaterializado obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgIndicadorMaterializado> codDAO = new CodigueraDAO<>(em, SgIndicadorMaterializado.class);
                return codDAO.obtenerPorId(id);
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
    public Boolean objetoExistePorIdIndicadorYAnio(Long idIndicador, Integer anio) throws GeneralException {
        if (idIndicador != null && anio != null) {
            try {
                FiltroIndicadorMaterializado fil = new FiltroIndicadorMaterializado();
                fil.setIndicadorPk(idIndicador);
                fil.setAnio(anio);
                return this.obtenerTotalPorFiltro(fil) > 0L;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    public void crearMaterializacionIndicadorAnio(FiltroEstadisticas filtro) throws GeneralException {
        try {

            if (filtro.getIndicadorPk() != null) {
                SgEstIndicador ind = em.find(SgEstIndicador.class, filtro.getIndicadorPk());

                filtro.setDesagregacion(null);
                crearMaterializacionIndicadorAnioDesagregacion(ind, filtro);

                for (EnumDesagregacion des : ind.getIndDesagregaciones()) {
                    filtro.setDesagregacion(des);
                    crearMaterializacionIndicadorAnioDesagregacion(ind, filtro);
                }

            }

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    private void crearMaterializacionIndicadorAnioDesagregacion(SgEstIndicador ind, FiltroEstadisticas filtro) throws GeneralException {

        try {

            if (filtro.getIndicadorPk() != null) {

                List<EstGenerica> res = estadisticasEstudiantesBean.obtenerEstadisticas(filtro);

                SgIndicadorMaterializado indmat = new SgIndicadorMaterializado();
                indmat.setIndAnio(filtro.getAnio());
                indmat.setIndIndicador(ind);
                indmat.setIndDesagregacion(filtro.getDesagregacion());
                if (filtro.getNombrePk() != null){
                    indmat.setIndNombreExtraccion(em.getReference(SgEstNombreExtraccion.class, filtro.getNombrePk()));
                }

                EstGenerica est = res.get(0);
                if (est.getCantidad() instanceof Double) {
                    indmat.setIndTipoNumerico(EnumTipoNumericoValorEstadistica.DECIMAL);
                } else if (est.getCantidad() instanceof Number) { //Integer o Long
                    indmat.setIndTipoNumerico(EnumTipoNumericoValorEstadistica.ENTERO);
                }

                em.persist(indmat);

                for (EstGenerica eg : res) {

                    SgTuplaIndicadorMaterializado tim = new SgTuplaIndicadorMaterializado();
                    tim.setTupDesagregacion(eg.getDesagregacion());
                    tim.setTupIdentificador(eg.getDato().toString());
                    tim.setTupIndicadorMaterializado(indmat);

                    if (eg.getCantidad() instanceof Number) {
                        tim.setTupValor(((Number) eg.getCantidad()).doubleValue());
                    }
                    
                    em.persist(tim);

                }

            }

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
    public Long obtenerTotalPorFiltro(FiltroIndicadorMaterializado filtro) throws GeneralException {
        try {
            IndicadorMaterializadoDAO codDAO = new IndicadorMaterializadoDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgIndicador>
     * @throws GeneralException
     */
    public List<SgIndicadorMaterializado> obtenerPorFiltro(FiltroIndicadorMaterializado filtro) throws GeneralException {
        try {
            IndicadorMaterializadoDAO codDAO = new IndicadorMaterializadoDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    
    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroEstadisticas
     * @return Lista de <EstGenerica>
     * @throws GeneralException
     */
    public List<EstGenerica> obtenerEstadisticaDeIndicadorMaterializado(FiltroEstadisticas filtro) throws GeneralException {
        try {
            EstadisticasIndicadorMaterializadoDAO codDAO = new EstadisticasIndicadorMaterializadoDAO(em);

            if (filtro.getAnio() == null || filtro.getIndicadorPk() == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_DATO_VACIO);
                throw be;
            }

            FiltroIndicadorMaterializado filEx = new FiltroIndicadorMaterializado();
            filEx.setAnio(filtro.getAnio());
            filEx.setIndicadorPk(filtro.getIndicadorPk());
            filEx.setDesagregacion(filtro.getDesagregacion());
            if (filtro.getDesagregacion() == null) {
                filEx.setSinDesagregacion(Boolean.TRUE);
            }
            filEx.setIncluirCampos(new String[]{"indPk", "indTipoNumerico"});

            List<SgIndicadorMaterializado> indicadores = this.obtenerPorFiltro(filEx);

            if (indicadores.isEmpty()) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_NO_EXISTE_INDICADOR_MATERIALIZADO);
                throw be;
            } else {

                Long pkIndicadorMaterializado = indicadores.get(0).getIndPk();
                return codDAO.obtenerEstadisticaDeIndicadorMaterializado(pkIndicadorMaterializado, filtro.getDesagregacion() != null, indicadores.get(0).getIndTipoNumerico());

            }

        } catch (BusinessException be) {
            throw be;
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
    public void eliminar(Long pkIndicador, Integer anio) throws GeneralException {
        if (pkIndicador != null) {
            try {
                
                FiltroIndicadorMaterializado fil = new FiltroIndicadorMaterializado();
                fil.setIndicadorPk(pkIndicador);
                fil.setAnio(anio);
                List<SgIndicadorMaterializado> indicadores = this.obtenerPorFiltro(fil);
                
                List<Long> pks = indicadores.stream().map(i -> i.getIndPk()).collect(Collectors.toList());
                
                
                em.createQuery("DELETE FROM SgTuplaIndicadorMaterializado where  tupIndicadorMaterializado.indPk IN (:pks)")
                        .setParameter("pks", pks)
                        .executeUpdate();
                
                for (SgIndicadorMaterializado ind : indicadores){
                    em.remove(ind);
                }
                
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
