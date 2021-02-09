/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.business.datatype.DataInsumoPlantilla;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.CodificacionInsumo1Segmento;
import gob.mined.siap2.entities.data.impl.CodificacionInsumo2Familia;
import gob.mined.siap2.entities.data.impl.CodificacionInsumo3Clase;
import gob.mined.siap2.entities.data.impl.CodificacionInsumo4Articulo;
import gob.mined.siap2.entities.data.impl.Insumo;
import gob.mined.siap2.entities.data.impl.NodoPlantillaDeInsumos;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.PlantillaDeInsumosDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa métodos vinculados a plantilla de insumos.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "PlantillaDeInsumosBean")
@LocalBean
public class PlantillaDeInsumosBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private PlantillaDeInsumosDAO plantillaDeInsumosDAO;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método devuelve una lista de insumos en una plantilla de insumos.
     *
     * @return
     */
    public List<NodoPlantillaDeInsumos> loadRootInsumos() {
        try {
            List<NodoPlantillaDeInsumos> l = plantillaDeInsumosDAO.getInsumosHijos();
            for (NodoPlantillaDeInsumos n : l) {
                n.getHijos().isEmpty();
                n.getInsumos().isEmpty();
            }
            return l;
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
     * Este método devuelve un nodo de la plantilla de insumos
     *
     * @param id
     * @return
     */
    public NodoPlantillaDeInsumos loadInsumo(Integer id) {
        try {
            NodoPlantillaDeInsumos nodo = plantillaDeInsumosDAO.find(id);
            for (NodoPlantillaDeInsumos hijo : nodo.getHijos()) {
                hijo.getHijos().isEmpty();
                hijo.getInsumos().isEmpty();
            }
            nodo.getInsumos().isEmpty();
            return nodo;
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
     * Este método elimina un nodo de la plantilla
     *
     * @param id
     */
    public void eliminarNodoPlantilla(Integer id) {
        try {
            NodoPlantillaDeInsumos nodo = plantillaDeInsumosDAO.find(id);
            if (!nodo.getHijos().isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_ELIMINE_LOS_GRUPOS_HIJOS_ANTES_DE_ELIMINAR_GRUPO);
                throw b;
            }
            if (!nodo.getInsumos().isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DESASOCIE_LOS_INSUMOS_ANTES_DE_ELIMINAR_GRUPO);
                throw b;
            }
            generalDAO.delete(nodo);

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
     * Este método elimina un insumo de la plantilla según su id.
     *
     * @param idInsumo
     * @return
     */
    public Insumo eliminarInsumoPlantilla(Integer idInsumo) {
        try {
            Insumo insumo = (Insumo) generalDAO.find(Insumo.class, idInsumo);
            if (insumo.getPlnatillaDeInsumos() != null) {
                insumo.getPlnatillaDeInsumos().getInsumos().remove(insumo);
                insumo.setPlnatillaDeInsumos(null);
            }

            return insumo;

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
     * Este método agrega un conjunto de insumos a la plantilla en un
     * determinado nodo.
     *
     * @param lista
     * @param idNodoPlantillaDeInsumos
     * @param noMoverInsumos
     * @param filtroONUCodigo
     * @param filtroONURubro
     * @param filtroONUnombre
     * @param filtroONUSoloSinPlantillas
     */
    public void agregarInsumos(List<DataInsumoPlantilla> lista, Integer idNodoPlantillaDeInsumos, Boolean noMoverInsumos, String filtroONUCodigo, Integer filtroONURubro, String filtroONUnombre, Boolean filtroONUSoloSinPlantillas) {
        try {
            NodoPlantillaDeInsumos padre = (NodoPlantillaDeInsumos) generalDAO.find(NodoPlantillaDeInsumos.class, idNodoPlantillaDeInsumos);
            for (DataInsumoPlantilla iter : lista) {
                List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();
                if (iter.getType().equals(DataInsumoPlantilla.TIPO_SEGMENTO)) {
                    CodificacionInsumo1Segmento segmento = (CodificacionInsumo1Segmento) iter.getObjeto();
                    CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "articulo.padre.padre.padre.id", segmento.getId());
                    criterios.add(criterio);
                } else if (iter.getType().equals(DataInsumoPlantilla.TIPO_FAMILIA)) {
                    CodificacionInsumo2Familia familia = (CodificacionInsumo2Familia) iter.getObjeto();
                    CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "articulo.padre.padre.id", familia.getId());
                    criterios.add(criterio);
                } else if (iter.getType().equals(DataInsumoPlantilla.TIPO_CLASE)) {
                    CodificacionInsumo3Clase clase = (CodificacionInsumo3Clase) iter.getObjeto();
                    CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "articulo.padre.id", clase.getId());
                    criterios.add(criterio);
                } else if (iter.getType().equals(DataInsumoPlantilla.TIPO_ARTICULO)) {
                    CodificacionInsumo4Articulo articulo = (CodificacionInsumo4Articulo) iter.getObjeto();
                    CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "articulo.id", articulo.getId());
                    criterios.add(criterio);
                } else if (iter.getType().equals(DataInsumoPlantilla.TIPO_INSUMO)) {
                    Insumo insumo = (Insumo) iter.getObjeto();
                    CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "id", insumo.getId());
                    criterios.add(criterio);
                }
                //se saltea los dummy
                if (!criterios.isEmpty()) {
                    if (noMoverInsumos) {
                        MatchCriteriaTO criterio2 = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "plnatillaDeInsumos", 1);
                        criterios.add(criterio2);
                    }

                    if (!TextUtils.isEmpty(filtroONUCodigo)) {
                        CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "codigo", filtroONUCodigo);
                        criterios.add(criterio);
                    }

                    if (filtroONURubro != null) {
                        CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "objetoEspecificoGasto.clasificador", filtroONURubro);
                        criterios.add(criterio);
                    }
                    if (!TextUtils.isEmpty(filtroONUnombre)) {
                        CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombre", filtroONUnombre);
                        criterios.add(criterio);
                    }
                    if (filtroONUSoloSinPlantillas != null && filtroONUSoloSinPlantillas) {
                        CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NULL, "plnatillaDeInsumos", 1);
                        criterios.add(criterio);
                    }

                    CriteriaTO condicion = null;
                    if (criterios.size() == 1) {
                        condicion = criterios.get(0);
                    } else {
                        condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
                    }

                    List<Insumo> insumos = generalDAO.findEntityByCriteria(Insumo.class, condicion, null, null, null, null);
                    for (Insumo i : insumos) {
                        if (i.getPlnatillaDeInsumos() != null) {
                            i.getPlnatillaDeInsumos().getInsumos().remove(i);
                        }
                        i.setPlnatillaDeInsumos(padre);
                        padre.getInsumos().add(i);
                        generalDAO.update(i);
                    }
                }
            }

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
     * Este método mueve un insumo de un grupo (NodoPlantillaDeInsumos) a otro
     * @param idInsumo
     * @param idGrupo 
     */
    public void moverInsumoAGrupo(Integer idInsumo, Integer idGrupoDestino) {
        try{
            Insumo insumo = (Insumo)generalDAO.find(Insumo.class, idInsumo);
            insumo.getPlnatillaDeInsumos().getInsumos().remove(insumo);
            NodoPlantillaDeInsumos plnatillaDeInsumos = (NodoPlantillaDeInsumos)generalDAO.find(NodoPlantillaDeInsumos.class, idGrupoDestino);
            insumo.setPlnatillaDeInsumos(plnatillaDeInsumos);
            plnatillaDeInsumos.getInsumos().add(insumo);
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

}
