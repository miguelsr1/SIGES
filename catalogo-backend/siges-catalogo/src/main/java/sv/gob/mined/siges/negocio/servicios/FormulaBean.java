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
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroFormula;
import sv.gob.mined.siges.negocio.validaciones.FormulaValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.FormulaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgFormula;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class FormulaBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgFormula
     * @throws GeneralException
     */
    public SgFormula obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgFormula> codDAO = new CodigueraDAO<>(em, SgFormula.class);
                SgFormula fom=codDAO.obtenerPorId(id);
                if(fom.getFomSubFormula()!=null){
                    fom.getFomSubFormula().size();
                }
                return fom;
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
                CodigueraDAO<SgFormula> codDAO = new CodigueraDAO<>(em, SgFormula.class);
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
     * @param fom SgFormula
     * @return SgFormula
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgFormula guardar(SgFormula fom) throws GeneralException {
        try {
            if (fom != null) {
                if (FormulaValidacionNegocio.validar(fom)) {
                    CodigueraDAO<SgFormula> codDAO = new CodigueraDAO<>(em, SgFormula.class);
                    boolean guardar = true;
               
                     return codDAO.guardar(fom);
                  
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return fom;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroFormula filtro) throws GeneralException {
        try {
            FormulaDAO codDAO = new FormulaDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgFormula
     * @throws GeneralException
     */
    public List<SgFormula> obtenerPorFiltro(FiltroFormula filtro) throws GeneralException {
        try {
            FormulaDAO codDAO = new FormulaDAO(em);
            List<SgFormula> listForm=codDAO.obtenerPorFiltro(filtro);
            if(BooleanUtils.isTrue(filtro.getIncluirSubformulas())){
                List<SgFormula> formulas=new ArrayList(listForm);
                for(SgFormula form:formulas){
                    Boolean descartarFormula=inicializarSubformulas(form,filtro.getDescartarSubFormulasPk());
                    if(descartarFormula){
                        listForm.remove(form);
                    }
                    //SE BUSCA EVITAR UN LOOP EN LAS SUBFORMULAS, DescartarSubFormulasPk tiene la Pk de la fórmula a la que se le quiere agregar subfórmulas
                    //si una de las fórmulas tiene como subfórmula esa pk la fórmula se descarta
                }
            }
            
            return listForm;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }    
 
    
     private Boolean inicializarSubformulas(SgFormula form, Long descartarSub) {
        if (descartarSub != null) {
            if (form.getFomPk().equals(descartarSub)) {
                return Boolean.TRUE;
            }

        }
        Boolean descartar=Boolean.FALSE;
        if (form.getFomTienSubformula()) {
            if(form.getFomSubFormula()!=null){
                form.getFomSubFormula().size();
                for (SgFormula subform : form.getFomSubFormula()) {
                   if(BooleanUtils.isTrue(inicializarSubformulas(subform, descartarSub))){
                        descartar= Boolean.TRUE;
                    }
                }
            }
        } 
        return descartar;
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
                CodigueraDAO<SgFormula> codDAO = new CodigueraDAO<>(em, SgFormula.class);
                codDAO.eliminarPorId(id);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
