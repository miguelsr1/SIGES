package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siges.entities.data.impl.SgSede;
import gob.mined.siap2.entities.enums.EnumEstadoOrganismoAdministrativo;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroSede;
import gob.mined.siap2.persistence.dao.imp.SedesDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.entities.data.impl.Beneficiarios;
import gob.mined.siap2.entities.data.impl.TopePresupuestalMatriculasDetalleGroup;
import gob.mined.siap2.exceptions.BusinessException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang.BooleanUtils;

@Stateless
@LocalBean
public class SedeBean {

    private static final Logger logger = Logger.getLogger(SedeBean.class.getName());

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Inject
    @JPADAO
    private SedesDAO sedesDAO;

    /**
     * Este método agrupa los resultados de TopePresupuestal
     *
     * @param sede
     * @return
     */
    public SgSede getSedeByCodigo(String sede) {
        try {
            return sedesDAO.getSedeByCodigo(sede);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }


    public List<Object[]> getCantidadMatriculasPorSede(Date fechaMatricula, List<Integer> sedes, List<Beneficiarios> benef, Boolean incluirAdscritas) {
        try {
            BusinessException be = new BusinessException();
            if (benef == null || benef.isEmpty()){
                be.addError(ConstantesErrores.ERR_BENEFICIARIOS_VACIO);
            }
            if (fechaMatricula == null){
                be.addError(ConstantesErrores.ERR_FECHA_MATRICULA_VACIA);
            }
            if (sedes == null || sedes.isEmpty()){
                be.addError(ConstantesErrores.ERR_SEDES_VACIO);
            }
            if (!be.getErrores().isEmpty()){
                throw be;
            }
            return sedesDAO.getCantidadMatriculasPorSede(fechaMatricula, sedes, benef, incluirAdscritas);
        } catch (BusinessException be){
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    public List<TopePresupuestalMatriculasDetalleGroup> getDetalleCantidadMatriculasPorSede(Integer sedesPk, Date fechaMatricula, List<Beneficiarios> benef) {
        try {
            BusinessException be = new BusinessException();
            if (sedesPk == null){
                be.addError(ConstantesErrores.ERR_SEDES_VACIO);
            }
            if (!be.getErrores().isEmpty()){
                throw be;
            }
            return sedesDAO.getDetalleCantidadMatriculasPorSede(sedesPk,fechaMatricula,benef);
        } catch (BusinessException be){
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    public List<Object[]> getCantidadMatriculasPorSede(List<Integer> sedes, Boolean incluirAdscritas) {
        try {
            BusinessException be = new BusinessException();

            if (sedes == null || sedes.isEmpty()){
                be.addError(ConstantesErrores.ERR_SEDES_VACIO);
            }
            if (!be.getErrores().isEmpty()){
                throw be;
            }
            return sedesDAO.getCantidadMatriculasPorSede(sedes, incluirAdscritas);
        } catch (BusinessException be){
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    public List<SgSede> getSedesByFiltro(FiltroSede filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (filtro.getCodigo() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "codigo", filtro.getCodigo());
                criterios.add(criterio);
            }

            if (filtro.getId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "id", filtro.getId());
                criterios.add(criterio);
            }

            if (filtro.getClasificacionesId() != null && !filtro.getClasificacionesId().isEmpty()) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "sedClasificacion.claPk", filtro.getClasificacionesId());
                criterios.add(criterio);
            }

            if (filtro.getTipos() != null && !filtro.getTipos().isEmpty()) {
                 CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "tipo", filtro.getTipos());
                criterios.add(criterio);
            }

            if (BooleanUtils.isTrue(filtro.getOaeYMiembrosVigente())) {

                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "organismosAdministracionEscolar.oaeMiembrosVigentes", true);
                criterios.add(criterio);

                criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "organismosAdministracionEscolar.oeaEstado", EnumEstadoOrganismoAdministrativo.APROBADO);
                criterios.add(criterio);

                criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATER, "organismosAdministracionEscolar.oaeFechaVencimiento", new Date());
                criterios.add(criterio);

            }
            
            if(filtro.getTiposOrganismos() != null && !filtro.getTiposOrganismos().isEmpty() && filtro.getTiposOrganismos().size() > 0 ) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "organismosAdministracionEscolar.oaeTipoOrganismoAdministrativo.toaCodigo", filtro.getTiposOrganismos());
                criterios.add(criterio);
            }

            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(SgSede.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

//    /**
//     * Devuelve el objeto del tipo que tiene el id indicado
//     *
//     * @param id Long
//     * @return SgSedes
//     * @throws GeneralException
//     */
//    public SgSede obtenerPorId(Long id) throws GeneralException {
//        if (id != null) {
//            try {
//                CodigueraDAO<SgSede> codDAO = new CodigueraDAO<>(em, SgSede.class);
//                return codDAO.obtenerPorId(id);
//            } catch (Exception ex) {
//                throw new TechnicalException(ex);
//            }
//        }
//        return null;
//    }
//
//   
//
//    /**
//     * Devuelve la cantidad total de elementos que satisfacen el criterio
//     *
//     * @param filtro FiltroCodiguera
//     * @return Long
//     * @throws GeneralException
//     */
//    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//    public Long obtenerTotalPorFiltro(FiltroSedes filtro) throws GeneralException {
//        try {
//            SedesDAO dao = new SedesDAO(em);
//            return dao.cantidadTotal(filtro, filtro.getSecurityOperation());
//        } catch (Exception ex) {
//            throw new TechnicalException(ex);
//        }
//    }
//
//    /**
//     * Realiza la consulta de los elementos que satisfacen el criterio
//     *
//     * @param filtro FiltroCodiguera
//     * @return Lista de SgSedes
//     * @throws GeneralException
//     */
//    public List<SgSede> obtenerPorFiltro(FiltroSedes filtro) throws GeneralException {
//        try {
//            SedesDAO dao = new SedesDAO(em);
//            List<SgSede> sedes = dao.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
//            return sedes;
//        } catch (Exception ex) {
//            throw new TechnicalException(ex);
//        }
//    }
}
