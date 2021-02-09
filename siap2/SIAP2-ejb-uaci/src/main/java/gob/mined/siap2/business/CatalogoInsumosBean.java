/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.ejbs.impl.ArchivoBean;
import gob.mined.siap2.business.ejbs.impl.GeneralBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.CodificacionInsumo4Articulo;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroInsumo;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.InsumoDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 * Esta clase implementa los métodos de servicio del catálogo de insumos
 * @author Sofis Solutions
 */
@Stateless(name = "CatalogoInsumosBean")
@LocalBean
public class CatalogoInsumosBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Inject
    @JPADAO
    private InsumoDAO insumoDAO;

    @Inject
    private ArchivoBean archivoBean;
    @Inject
    private GeneralBean generalBean;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método genera el código de un artículo a partir del código ONU
     * @param articulo
     * @return 
     */
    private String generarCodigo(CodificacionInsumo4Articulo articulo) {
        try {
            List<String> maxCodigoArticulo = insumoDAO.getMaxCodeStart(articulo.getCodigo());

            String endCode = "0";
            if (!maxCodigoArticulo.isEmpty()) {
                String res = maxCodigoArticulo.get(0);
                if (!TextUtils.isEmpty(res)){
                    endCode = res.substring(res.length() - 4);
                }
            } 
            Integer digito = Integer.valueOf(endCode) + 1;
           
            return articulo.getCodigo() +  NumberUtils.ponerCerosALaIzquierda(digito, 4);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_AL_AUTOGENERAR_CODIGO_INSUMO);
            throw b;
        }
    }

    /**
     * Este método actualiza un insumo a partir de un artículo
     * 
     * @param insumo
     * @param idArticulo 
     */
    public void crearOActualizarCatalogoInsumos(Insumo insumo, Integer idArticulo) {
        try {
            generalBean.chequearIgualNombre(Insumo.class, insumo.getId(), insumo.getNombre());

            CodificacionInsumo4Articulo art = (CodificacionInsumo4Articulo) generalDAO.findById(CodificacionInsumo4Articulo.class, idArticulo);
            insumo.setArticulo(art);

            //si no tiene codigo se genera como el codigo del articulo mas 4 digitos correlativos
            if (TextUtils.isEmpty(insumo.getCodigo())) {
                insumo.setCodigo(generarCodigo(art));
            }

            generalBean.chequearIgualCodigo(Insumo.class, insumo.getId(), insumo.getCodigo());

            if (insumo.getTempUploadedFile() != null) {
                if (insumo.getArchivo() == null) {
                    insumo.setArchivo(archivoBean.crearArchivo());
                }
                archivoBean.asociarArchivo(insumo.getArchivo(), insumo.getTempUploadedFile(), true);
            }
            if(insumo.getTipoBien() == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_TIPO_BIEN_VACIO);
                throw b;
            }
            
            generalDAO.update(insumo);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    /**
     * Este método elimina el insumo con id
     * 
     * @param id id del elemento a eliminar
     */
    public void eleiminarInsumo(Integer id) {
        try {
            Insumo i = (Insumo) generalDAO.find(Insumo.class, id);
            generalDAO.delete(i);
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA_SIN_PARAMETROS);
            throw b;
        }
    }
    
    public Insumo getInsumoByCodigo(String codigo) {
        try {
            return insumoDAO.getInsumoByCodigo(codigo);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            TechnicalException ge = new TechnicalException();
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    private List<CriteriaTO> generarCriteria(FiltroInsumo filtro) {
        List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
        if(filtro.getId() != null) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "id", filtro.getId());
            criterios.add(criterio); 
        }
        if (StringUtils.isNotBlank(filtro.getCodigo())) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.EQUALS, "codigo", filtro.getCodigo().trim());
            criterios.add(criterio);
        }
        if (StringUtils.isNotBlank(filtro.getNombre()) ) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "nombre", filtro.getNombre().trim());
            criterios.add(criterio);
        }
        if (StringUtils.isNotBlank(filtro.getConocidoPor()) ) {
            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO( MatchCriteriaTO.types.CONTAINS, "conocidoPor", filtro.getConocidoPor().trim());
            criterios.add(criterio);
        }
        return criterios;
        
    }
    
    public List<Insumo> obtenerPorFiltro(FiltroInsumo filtro) {
        try {
            List<CriteriaTO> criterios = generarCriteria(filtro);
           
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(Insumo.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

}
