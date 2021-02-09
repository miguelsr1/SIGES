/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.business.ejbs.DatosUsuario;
import gob.mined.siap2.business.interceptors.log.LoggedInterceptor;
import gob.mined.siap2.business.utils.UnidadTecnicaUtils;
import gob.mined.siap2.business.validations.UnidadTecnicaValidacion;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.UnidadTecnicaDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EnumEstadoOrganismoAdministrativo;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroUnidadTecnica;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siges.entities.data.impl.SgSede;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Esta clase incluye los métodos de servicio correspondientes a unidades técnicas.
 * @author Sofis Solutions
 */
@Stateless(name = "UnidadTecnicaBean")
@LocalBean
@Interceptors({LoggedInterceptor.class})
public class UnidadTecnicaBean {
 
    @Inject
    private DatosUsuario du;
    @Inject
    @JPADAO
    private UnidadTecnicaDAO utDAO;
    @Inject
    private UnidadTecnicaValidacion utValidacion;

    private static final Logger logger = Logger.getLogger(UnidadTecnicaBean.class.getName());

    /**
     * Este método guarda un elemento de tipo UnidadTecnica. Se aplica para la
     * creación de la entidad y para la modificación de una entidad existente.
     *
     * @param cnf
     * @throws GeneralException Devuelve los códigos de error de la validación
     */
    public UnidadTecnica guardar(UnidadTecnica cnf) throws GeneralException {
        logger.log(Level.SEVERE, "guardar");
        try {
            //Validar el elemento a guardar. Si no valida, se lanza una excepcion
            if (utValidacion.validar(cnf)) {
                if (cnf.getId() == null) {
                    cnf = (UnidadTecnica) utDAO.create(cnf, du.getCodigoUsuario(), du.getOrigen());
                } else {
                    cnf = (UnidadTecnica) utDAO.update(cnf, du.getCodigoUsuario(), du.getOrigen());
                }
            }
            
            List<Integer> listUTtienenAcceso =new LinkedList<>();
            if (cnf.getPadre()!=null){
                listUTtienenAcceso = UnidadTecnicaUtils.getUTTienenAcceso(cnf.getPadre());
                listUTtienenAcceso.add(cnf.getPadre().getId());
            }           
            UnidadTecnicaUtils.setUTTienenAcceso(cnf, listUTtienenAcceso);
            
            return cnf;
        } catch (BusinessException be) {
            //Si es de tipo negocio envía la misma excepción
            throw be;
        } catch (Exception ex) {
            //Las demás excepciones se consideran técnicas
            logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Devuelve el elemento UnidadTecnica por el ID
     *
     * @param id
     * @return
     * @throws GeneralException
     */
    public UnidadTecnica obtenerUnidadTecnicaPorId(Integer id) throws GeneralException {
        logger.log(Level.INFO, "obtenerUnidadTecnicaPorId");
        try {
            return (UnidadTecnica) utDAO.findById(UnidadTecnica.class, id);
        } catch (DAOGeneralException ex) {
           logger.log(Level.SEVERE, ex.getMessage() , ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    /**
     * Devuelve el elemento de UnidadTecnica según el código Si no hay ningún
     * elemento con ese código devuelve null
     *
     * @param codigo
     * @return
     * @throws GeneralException
     */
    public UnidadTecnica obtenerUnidadTecnicaPorCodigo(String codigo) throws GeneralException {
        logger.log(Level.INFO, "obtenerUnidadTecnicaPorCodigo");
        try {
            List<UnidadTecnica> resultado = utDAO.findByOneProperty(UnidadTecnica.class, "cnfCodigo", codigo);
            if (resultado.size() == 1) {
                return resultado.get(0);
            } else if (resultado.isEmpty()) {
                return null;
            } else {
                BusinessException be = new BusinessException();
                be.addError(ConstantesErrores.ERROR_DEMASIADOS_RESULTADOS);
                throw be;
            }
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    /**
     * Devuelve todos los elementos de tipo UnidadTecnica
     *
     * @return
     * @throws GeneralException
     */
    public List<UnidadTecnica> obtenerTodos() throws GeneralException {
        logger.log(Level.INFO, "obtenerTodos");
        try {
            return utDAO.findAll(UnidadTecnica.class);
        } catch (Exception ex) {
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio ingresado
     *
     * @param cto
     * @param orderBy
     * @param ascending
     * @param startPosition
     * @param cantidad
     * @return
     * @throws GeneralException
     */
    public List<UnidadTecnica> obtenerPorCriteria(CriteriaTO cto, String[] orderBy, boolean[] ascending, Long startPosition, Long cantidad) throws GeneralException {
        logger.log(Level.INFO, "obtenerPorCriteria");
        try {
            return utDAO.findEntityByCriteria(UnidadTecnica.class, cto, orderBy, ascending, startPosition, cantidad);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);

        }
        return null;
    }

    /**
     * Dado el id de una unidad técnica retorna los posible padres para la misma
     * @param id
     * @return
     * @throws GeneralException 
     */
    public List<UnidadTecnica> getPadresByUnidadTecnicaId(Integer id) throws GeneralException {
        try {
            logger.log(Level.INFO, "obtenerPorCriteria");
            if(id!=null){
                return utDAO.getPadresByUnidadTecnicaId(id);
            }else{
                return obtenerTodos();
            }
            
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException te = new TechnicalException();
            te.addError(ex.getMessage());
            throw te;
        }
    }
    
    
    /**
     * Este método devuelve una lista de unidades técnicas que tienen a query como substring del nombre.
     * @param query
     * @return 
     */
    public List<UnidadTecnica> getUTBynombre(String query){     
         //List<UnidadTecnica> l= utDAO.getUTBynombre(query);
         //l.isEmpty();
        FiltroUnidadTecnica filtro = new FiltroUnidadTecnica();
        filtro.setNombre(query);
        
         return obtenerUnidadesPorFiltro(filtro);        
    }
    /**
     * Este método devuelve una lista de unidades técnicas que tienen a query como substring del nombre y que pertenecen al codiUsuario.
     * @param query
     * @return 
     */
    public List<UnidadTecnica> getUTBynombreAndUsuario(FiltroUnidadTecnica filtro){
        List<UnidadTecnica> resultado = new LinkedList<>();
        FiltroUnidadTecnica filtroTemp = new FiltroUnidadTecnica();
        filtroTemp.setNombre(filtro.getNombre());
        filtroTemp.setUnidadOperativa(Boolean.TRUE);
        filtroTemp.setCodUsuario(filtro.getCodUsuario());
        filtroTemp.setIncluirCampos(new String[]{"id","padre.id","version" });
         List<UnidadTecnica> lista = obtenerUnidadesPorFiltro(filtroTemp);
         List<Integer> listaIds = new LinkedList<>();
         for(UnidadTecnica ut : lista) {
            Integer id = ut.getId();
            Integer padre = ut.getPadre() != null ? ut.getPadre().getId() : null;
            if(!listaIds.contains(id)) {
                listaIds.add(id);
            }
            if(padre != null) {
                if(!listaIds.contains(padre)) {
                    listaIds.add(padre);
                }
            }
        }
        if(listaIds != null && !listaIds.isEmpty() && listaIds.size() > 0) {
            filtroTemp = new FiltroUnidadTecnica();
            filtroTemp.setIncluirCampos(filtro.getIncluirCampos());
            filtroTemp.setListaIds(listaIds);
            filtroTemp.setOrderBy(filtro.getOrderBy());
            filtroTemp.setAscending(filtro.getAscending());
            filtroTemp.setMaxResults(filtro.getMaxResults());
            resultado = obtenerUnidadesPorFiltro(filtroTemp);  
        }
        
        return resultado;       
    }
    
    public List<UnidadTecnica> obtenerUnidadesPorFiltro(FiltroUnidadTecnica filtro) {
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

            if (filtro.getNombre() != null && !filtro.getNombre().isEmpty()) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombreBusqueda", filtro.getNombre().trim().toLowerCase());
                criterios.add(criterio);
            }

            if (filtro.getUnidadOperativa()!= null) {
                 CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "unidadOperativa", filtro.getUnidadOperativa());
                criterios.add(criterio);
            }
            if(filtro.getCodUsuario() != null && !filtro.getCodUsuario().isEmpty()) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "uniUsuario.usuCod", filtro.getCodUsuario().trim());
                criterios.add(criterio);
            }
            
            if(filtro.getListaIds() != null && !filtro.getListaIds().isEmpty() && filtro.getListaIds().size() > 0 ) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "id", filtro.getListaIds());
                criterios.add(criterio);
            }

            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return utDAO.findEntityByCriteria(UnidadTecnica.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
}
