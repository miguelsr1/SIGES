/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.business.utils.ReportesUtils;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.AnioIndicador;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.ProgramaIndicador;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.TechoPlanificacionAccionCentral;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.LineaEstrategicaDAO;
import gob.mined.siap2.sofisform.to.AND_TO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos de servicio vinculados a una planificación
 * estratégica.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "PlanificacionEstrategicaBean")
@LocalBean
public class PlanificacionEstrategicaBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Inject
    @JPADAO
    private LineaEstrategicaDAO lineaEstrategicaDAO;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método crea o actualiza una planificación estratégica.
     *
     * @param p
     * @param toAdd
     */
    public void crearActualizarPlanificacion(PlanificacionEstrategica p, Set<LineaEstrategica> toAdd) {
        try {
            if (p.getHasta().compareTo(p.getDesde()) < 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_FECHA_DESDE_MAYOR_QUE_HASTA);
                throw b;
            }

            /*cheuqea no exista igual nombre */
            List<CriteriaTO> criterios = new ArrayList<>();
            MatchCriteriaTO igualNombre = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "nombre", p.getNombre());
            criterios.add(igualNombre);
            if (p.getId() != null) {
                MatchCriteriaTO distintoId = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "id", p.getId());
                criterios.add(distintoId);
            }
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }
            String[] propiedades = {"id"};
            List existeIgualNombre = generalDAO.findEntityByCriteria(PlanificacionEstrategica.class, condicion, null, null, null, null, propiedades);
            if (existeIgualNombre.size() > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_NOMBRE_REPETIDO);
                throw b;
            }

            /*que no se superpongan periodos*/
            criterios = new ArrayList<>();
            MatchCriteriaTO desdeD = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "desde", p.getDesde());
            MatchCriteriaTO hastaD = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "hasta", p.getDesde());
            AND_TO desde = CriteriaTOUtils.createANDTO(desdeD, hastaD);
            MatchCriteriaTO desdeH = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "desde", p.getHasta());
            MatchCriteriaTO hastaH = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "hasta", p.getHasta());
            AND_TO hasta = CriteriaTOUtils.createANDTO(desdeH, hastaH);
            MatchCriteriaTO adentroD = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.GREATEREQUAL, "desde", p.getDesde());
            MatchCriteriaTO adentroH = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.LESSEQUAL, "hasta", p.getHasta());
            AND_TO adentro = CriteriaTOUtils.createANDTO(adentroD, adentroH);

            criterios.add(CriteriaTOUtils.createORTO(hasta, desde, adentro));

            if (p.getId() != null) {
                MatchCriteriaTO distintoId = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_EQUALS, "id", p.getId());
                criterios.add(distintoId);
            }
            condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }
            List existeIgualFechas = generalDAO.findEntityByCriteria(PlanificacionEstrategica.class, condicion, null, null, null, null, propiedades);
            if (existeIgualFechas.size() > 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_PLANIFICACION_SUPERPONE_PERIODO);
                throw b;
            }

            p.setLineasEstrategicas(toAdd);
            p = (PlanificacionEstrategica) generalDAO.update(p);

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método crea o actualiza techos de una planificación estratégica.
     *
     * @param p
     */
    public void crearActualizarTechos(PlanificacionEstrategica p) {
        try {
            Iterator<TechoPlanificacionAccionCentral> iterA = p.getTechosAccionCentral().iterator();
            while (iterA.hasNext()) {
                TechoPlanificacionAccionCentral techo = iterA.next();
                techo.setPlanificacion(p);
                if (techo.getMonto() == null || techo.getMonto().equals(BigDecimal.ZERO)) {
                    iterA.remove();
                }
            }
            p = (PlanificacionEstrategica) generalDAO.update(p);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método devuelve las líneas estratégicas de una planificación
     * estratégica.
     *
     * @param idPlanificacion
     * @return
     */
    public List<LineaEstrategica> getLineas(Integer idPlanificacion) {
        List<LineaEstrategica> l = lineaEstrategicaDAO.getLineas(idPlanificacion);
        l.isEmpty();
        return l;
    }

    /**
     * Este método determina si una línea en una planificación se puede eliminar
     *
     * @param idPlanificacion
     * @param idLinea
     */
    public void chequearEliminableLinea(Integer idPlanificacion, Integer idLinea) {
        try {
            BusinessException b = new BusinessException();

            MatchCriteriaTO tieneLinea = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lineasEstrategicas.id", idLinea);
            MatchCriteriaTO tienePlanificacion = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "planificacionEstrategica.id", idPlanificacion);
            CriteriaTO condicion = CriteriaTOUtils.createANDTO(tieneLinea, tienePlanificacion);

            Long countPI = generalDAO.countByCriteria(ProgramaInstitucional.class, condicion, null, null);
            if (countPI > 0) {
                b.addError(ConstantesErrores.ERROR_HAY_PI_ASOCIADO_A_PLANIFICACION_Y_LINEA);
            }
            Long countPP = generalDAO.countByCriteria(ProgramaInstitucional.class, condicion, null, null);
            if (countPP > 0) {
                b.addError(ConstantesErrores.ERROR_HAY_PP_ASOCIADO_A_PLANIFICACION_Y_LINEA);
            }
            Long countAC = generalDAO.countByCriteria(AccionCentral.class, condicion, null, null);
            if (countAC > 0) {
                b.addError(ConstantesErrores.ERROR_HAY_AC_ASOCIADO_A_PLANIFICACION_Y_LINEA);
            }
            Long countANP = generalDAO.countByCriteria(AsignacionNoProgramable.class, condicion, null, null);
            if (countANP > 0) {
                b.addError(ConstantesErrores.ERROR_HAY_ANP_ASOCIADO_A_PLANIFICACION_Y_LINEA);
            }
            if (!b.getErrores().isEmpty()) {
                throw b;
            }

        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }

    /**
     * Este método permite eliminar una planificación según su id.
     *
     * @param idPlanificaion
     */
    public void eliminarPlanificacion(Integer idPlanificaion) {
        try {
            PlanificacionEstrategica p = (PlanificacionEstrategica) generalDAO.findById(PlanificacionEstrategica.class, idPlanificaion);
            if (!p.getLineasEstrategicas().isEmpty()) {
                BusinessException b = new BusinessException();
                String[] values = {"una línea", "la planificación"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, values);
                throw b;
            }
            generalDAO.delete(p);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
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

    /**
     * Este método permite obtener las líneas y metas de una planificación.
     *
     * @param idPlanificacion
     * @return
     */
    public List<HashMap> obtenerPlaLineasMetasIndicadores(Integer idPlanificacion) {

        List<HashMap> retorno = new ArrayList<HashMap>();
        try {
            List<HashMap> respuesta = new ArrayList<HashMap>();
            SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy");

            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "programa.planificacionEstrategica.id", idPlanificacion);
            List<ProgramaIndicador> listaProgramaIndicador = generalDAO.findEntityByCriteria(ProgramaIndicador.class, criterio, null, null, null, null);
            PlanificacionEstrategica planificacion = null;
            if (listaProgramaIndicador != null && !listaProgramaIndicador.isEmpty()) {
                planificacion = listaProgramaIndicador.get(0).getPrograma().getPlanificacionEstrategica();
            }
            if (planificacion != null) {
                HashMap fila1 = new HashMap();
                fila1.put("col1", "Planificación estratégica -" + planificacion.getNombre() + "-");
                fila1.put("col2", "" + formatAnio.format(planificacion.getDesde()) + "-" + formatAnio.format(planificacion.getHasta()));
                fila1.put("styleClass", "tblFilaTitulo1");
                fila1.put("orden", "1");
                completarFila(fila1);
                respuesta.add(fila1);
                HashMap fila2 = new HashMap();
                fila2.put("styleClass", "tblFilaBlanco");
                fila2.put("orden", "2");
                completarFila(fila2);//Fila completamente en blanco.
                respuesta.add(fila2);
                HashMap fila3 = new HashMap();
                fila3.put("col1", "Línea estratégica");
                fila3.put("col2", "Programa");
                fila3.put("col3", "Indicador");
                fila3.put("col6", "Metas");
                fila3.put("styleClass", "tblFilaTitulo2");
                fila3.put("orden", "3");
                completarFila(fila3);
                respuesta.add(fila3);
                HashMap fila4 = new HashMap();
                fila4.put("col4", "Año1");
                fila4.put("col5", "Año2");
                fila4.put("col6", "Año3");
                fila4.put("col7", "Año4");
                fila4.put("col8", "Año5");
                fila4.put("styleClass", "tblFilaTitulo3");
                fila4.put("orden", "4");
                completarFila(fila4);
                respuesta.add(fila4);
            }

            for (ProgramaIndicador pi : listaProgramaIndicador) {
                HashMap fila = new HashMap();

                String metaAnio1 = obtenerMetaAnio(1, planificacion, pi.getAnioIndicadors());
                String metaAnio2 = obtenerMetaAnio(2, planificacion, pi.getAnioIndicadors());
                String metaAnio3 = obtenerMetaAnio(3, planificacion, pi.getAnioIndicadors());
                String metaAnio4 = obtenerMetaAnio(4, planificacion, pi.getAnioIndicadors());
                String metaAnio5 = obtenerMetaAnio(5, planificacion, pi.getAnioIndicadors());

                for (LineaEstrategica lineaEstrategica : pi.getPrograma().getLineasEstrategicas()) {
                    fila.put("col1", lineaEstrategica.getNombre());
                    fila.put("col2", pi.getPrograma().getNombre());
                    String txtIndicador = pi.getIndicador().getNombre();
                    if (pi.getIndicador().getUnidadDeMedida() != null && pi.getIndicador().getUnidadDeMedida().getNombre() != null) {
                        txtIndicador = txtIndicador + " (" + pi.getIndicador().getUnidadDeMedida().getNombre() + ")";
                    }
                    fila.put("col3", txtIndicador);
                    fila.put("col4", metaAnio1);
                    fila.put("col5", metaAnio2);
                    fila.put("col6", metaAnio3);
                    fila.put("col7", metaAnio4);
                    fila.put("col8", metaAnio5);
                    fila.put("styleClass", "tblFilaContenido");
                    fila.put("orden", lineaEstrategica.getNombre() + pi.getPrograma().getNombre() + pi.getIndicador().getNombre());
                    completarFila(fila);
                    respuesta.add(fila);
                }
            }

            Collections.sort(respuesta, new Comparator<HashMap>() {
                public int compare(HashMap d1, HashMap d2) {
                    return ((String) d1.get("orden")).compareTo((String) d2.get("orden"));
                }
            });

            //Saco repetidos
            String anterior = "XXXX";
            for (HashMap fila : respuesta) {
                if (!fila.get("orden").equals(anterior)) {
                    retorno.add(fila);
                }
                anterior = (String) fila.get("orden");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }

        return retorno;
    }

    /**
     * Este método permite obtener la meta de un indicador en una planificación
     * estratégica.
     *
     * @param ordenAnio
     * @param planificacion
     * @param listAnioIndicador
     * @return
     */
    private String obtenerMetaAnio(int ordenAnio, PlanificacionEstrategica planificacion, List<AnioIndicador> listAnioIndicador) {
        String respuesta = null;
        SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy");
        Integer anioInicial = new Integer(formatAnio.format(planificacion.getDesde()));
        Integer anioFinal = new Integer(formatAnio.format(planificacion.getHasta()));
        int indiceOrden = 1;
        for (int anio = anioInicial; anio <= anioFinal; anio++) {
            if (indiceOrden == ordenAnio) {
                //Busco anio en la lista de indicadores
                AnioIndicador ai = obtenerAnioIndicador(listAnioIndicador, anio);
                if (ai != null) {
                    respuesta = ReportesUtils.getNumber(ai.getMeta());
                    break;
                }
            }
            indiceOrden++;
        }
        return respuesta;
    }

    /**
     * Este método devuelve los datos de un indicador en un año, en una
     * planificación estratégica.
     *
     * @param listAnioIndicador
     * @param anio
     * @return
     */
    private AnioIndicador obtenerAnioIndicador(List<AnioIndicador> listAnioIndicador, int anio) {
        for (AnioIndicador anioIndicador : listAnioIndicador) {
            if (anioIndicador.getAnio().intValue() == anio) {
                return anioIndicador;
            }
        }
        return null;
    }

    private void completarFila(HashMap fila) {
        if (fila == null) {
            return;
        }
        for (int i = 1; i <= 8; i++) {
            if (fila.get("col" + i) == null) {
                fila.put("col" + i, "");
            }
        }
    }

}
