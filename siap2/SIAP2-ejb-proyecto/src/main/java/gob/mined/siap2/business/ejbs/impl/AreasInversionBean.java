package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.AreasInversion;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroAreasInversion;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.imp.AreasInversionDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.poi.ss.usermodel.Cell;


@LocalBean
@Stateless(name = "AreasInversionBean")
public class AreasInversionBean {
    
    
    private static final Logger logger = Logger.getLogger(AreasInversionBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    @JPADAO
    private AreasInversionDAO areasInversionDAO;
    
    
    
    
    /**
     * Este método crea o actualiza una área de inversión
     * @param areasInversion 
     */
    public void crearActualizar(AreasInversion areasInversion) {
        try {
            generalDAO.update(areasInversion);
        } catch (DAOGeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
    
    
    /**
     * Método que se encarga de eliminar una área de inversion
     * actividades
     * @param id Identificador de registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            AreasInversion ai = (AreasInversion) generalDAO.find(AreasInversion.class, id);
            if(ai != null){
                generalDAO.delete(ai);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
 
    
    /**
     * Este método devuelve una area de inversion filtrada por Id
     * @param gesId
     * @return 
     */
    public AreasInversion getAreaInversionById(Integer gesId) {
        return generalDAO.getEntityManager().find(AreasInversion.class, gesId);
    }

    
    /**
     * Este método devuelve todos los registros de áreas de inversión
     * @return 
     */
    public List<AreasInversion> getAreasInversion() {
        try {
            List<AreasInversion> listaRegistros = areasInversionDAO.getAreasInversion();
            listaRegistros.isEmpty();
            return listaRegistros;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    
    /**
     * Este método devuelve todos los registros de AreasInversion que sean padres.
     * @return 
     */
    public List<AreasInversion> getAreasInversionPadres() {
        try {
            List<AreasInversion> gestiones = areasInversionDAO.getAreasInversionPadres();
            gestiones.isEmpty();
            return gestiones;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    
    /**
     * Este método devuelve todos los registros de AreasInversion que tengan el mismo padre
     * @param idPadre
     * @return 
     */
    public List<AreasInversion> getAreasInversionByIdPadre(Integer idPadre) {
        try {
            List<AreasInversion> gestiones = areasInversionDAO.getAreasInversionByIdPadre(idPadre);
            gestiones.isEmpty();
            return gestiones;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método devuelve todos los registros de AreasInversion que se encuentren habilitadas
     * @return 
     */
    public List<AreasInversion> getAreasInversionHabilitadas() {
        try {
            List<AreasInversion> gestiones = areasInversionDAO.getAreasInversionPadresHabilitados();
            gestiones.isEmpty();
            return gestiones;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    public List<AreasInversion> obtenerPorFiltro(FiltroAreasInversion filtro) {
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

            if(filtro.getCodigoNombre() != null && !filtro.getCodigoNombre().isEmpty()) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "codigo", filtro.getCodigoNombre().toLowerCase().trim());
                MatchCriteriaTO criterio1 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombreBusqueda", filtro.getCodigoNombre().toLowerCase().trim());
                criterios.add(CriteriaTOUtils.createORTO(criterio, criterio1));
            }
            if(filtro.getHabilitado() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "habilitado", filtro.getHabilitado());
                criterios.add(criterio);
            }
            if(filtro.getPadreId() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "areaPadre.id", filtro.getPadreId());
                criterios.add(criterio);
            }
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(AreasInversion.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    public String getValor(Cell cell) {
        String respuesta = "";
        if (cell == null) {
            return respuesta;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                respuesta = cell.getBooleanCellValue() + "";
                break;
            case Cell.CELL_TYPE_NUMERIC:
                double d = cell.getNumericCellValue();
                respuesta = d + "";
                //Si termina en .0, le saco el .0
                if (respuesta.length() >= 2 && respuesta.substring(respuesta.length() - 2, respuesta.length()).equals(".0")) {
                    respuesta = respuesta.substring(0, respuesta.length() - 2);
                }
                break;
            case Cell.CELL_TYPE_STRING:
                respuesta = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                break;
            default:
        }
        return respuesta;
    }
}
