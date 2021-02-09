/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.ejbs.impl;

import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.Programa;
import gob.mined.siap2.entities.data.impl.ProgramaIndicador;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestarioTechoAnio;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTechoPresupuestarioAnio;
import gob.mined.siap2.entities.data.impl.ProyectoFuente;
import gob.mined.siap2.entities.data.impl.ProyectoTechoPresupuestarioAnio;
import gob.mined.siap2.entities.data.impl.TechoProgramaPresupueastarioAnio;
import gob.mined.siap2.entities.data.impl.TechoProgramaPresupueastarioFuente;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroPrograma;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.ProgramaDAO;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos de servicio vinculados a programas
 *
 * @author Sofis Solutions
 */
@Stateless(name = "ProgramaBean")
@LocalBean
public class ProgramaBean {

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    ProgramaDAO programaDAO;

    @Inject
    private GeneralBean generalBean;

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Este método crea o actualiza un programa institucional de una
     * planificación.
     *
     * @param p
     * @param idPlanificacion
     */
    public void crearActualizarProgramaInstitucional(ProgramaInstitucional p, Integer idPlanificacion) {
        try {
            //lo chequea solo si no es un subprograma
            if (p.getProgramaInstitucional() == null) {
                generalBean.chequearIgualNombre(ProgramaInstitucional.class, p.getId(), p.getNombre());
                PlanificacionEstrategica pla = (PlanificacionEstrategica) generalDAO.find(PlanificacionEstrategica.class, idPlanificacion);
                p.setPlanificacionEstrategica(pla);
                for (ProgramaInstitucional hijo : p.getProgramasInstitucionales()) {
                    //verifica que las lineas sean las mismas de los hijos
                    hijo.setPlanificacionEstrategica(p.getPlanificacionEstrategica());
                    for (LineaEstrategica lineaHijo : hijo.getLineasEstrategicas()) {
                        if (!p.getLineasEstrategicas().contains(lineaHijo)) {
                            BusinessException b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_UN_SUBPROGRAMA_PERTENECE_A_LINEA_ESTRATEGICA_QUE_NO_PERTENECE_EL_PADRE);
                            throw b;
                        }
                    }

                    //verifica que los subprogramas no tengan el mismo nombre que el programa
                    if (hijo.getNombre().equals(p.getNombre())) {
                        BusinessException b = new BusinessException();
                        String[] params = {hijo.getNombre()};
                        b.addError(ConstantesErrores.ERR_SUBPROGRAMAS_TIENE_MISMO_NOMBRE_QUE_PROGRAMA_0, params);
                        throw b;

                    }

                    //verifica que no tengan el mismo nombre
                    for (ProgramaInstitucional hijo2 : p.getProgramasInstitucionales()) {
                        System.out.println("hijo nom " + hijo.getNombre() + " hijo2 nom " + hijo2.getNombre());
                        System.out.println("hijo " + hijo + " hijo2 " + hijo2);
                        if (!hijo.equals(hijo2) && hijo.getNombre().equals(hijo2.getNombre())) {
                            BusinessException b = new BusinessException();
                            String[] params = {hijo.getNombre()};
                            b.addError(ConstantesErrores.ERR_UN_EXISTEN_SUBPROGRAMAS_CON_EL_MISMO_NOMBRE_0, params);
                            throw b;

                        }
                    }
                }
            } else {
                ProgramaInstitucional padre = p.getProgramaInstitucional();
                p.setPlanificacionEstrategica(padre.getPlanificacionEstrategica());
            }
            generalDAO.update(p);
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
     * Este método elimina un programa de una planificación estratégica.
     *
     * @param idPrograma
     */
    public void eleiminarProgramaInstitucional(Integer idPrograma) {
        try {
            ProgramaInstitucional toRemove = (ProgramaInstitucional) generalDAO.find(ProgramaInstitucional.class, idPrograma);

            if (toRemove.getProgramasInstitucionales().size() > 0) {
                BusinessException b = new BusinessException();
                String[] params = {"subprogramas", "al programa institucional"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }
            if (toRemove.getSupuestos().size() > 0) {
                BusinessException b = new BusinessException();
                String[] params = {"supuestos", "al programa institucional"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }

            if (!programaDAO.getProgramasPresupuestariosConInstitucional(toRemove.getId()).isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_EXISTE_PROGRAMA_PRESUPUESTARIO_ASOCIADO);
                throw b;
            }

            if (!generalDAO.findByOneProperty(ProgramaIndicador.class, "programa.id", idPrograma).isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {"indicadores", "al programa institucional"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;

            }

            generalDAO.delete(toRemove);

        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA);
            throw b;
        }
    }

    /**
     * Este método crea o actualiza un programa presupuestario en una
     * planificación estratégica.
     *
     * @param p
     * @param idPlanificacion
     */
    public void crearActualizarProgramaPresupuestario(ProgramaPresupuestario p, Integer idPlanificacion) {
        try {
            //lo chequea solo si no es un subprograma
            if (p.getProgramaPresupuestario() == null) {
                generalBean.chequearIgualNombre(ProgramaPresupuestario.class, p.getId(), p.getNombre());
                PlanificacionEstrategica pla = (PlanificacionEstrategica) generalDAO.find(PlanificacionEstrategica.class, idPlanificacion);
                p.setPlanificacionEstrategica(pla);
                //copia la planificacionpara los hijos y chequea que las lienas estrategicas petenezcan
                for (ProgramaPresupuestario hijo : p.getProgramasPresupuestarios()) {
                    hijo.setPlanificacionEstrategica(p.getPlanificacionEstrategica());
                    //verifica las lineas
                    for (LineaEstrategica lineaHijo : hijo.getLineasEstrategicas()) {
                        if (!p.getLineasEstrategicas().contains(lineaHijo)) {
                            BusinessException b = new BusinessException();
                            b.addError(ConstantesErrores.ERR_UN_SUBPROGRAMA_PERTENECE_A_LINEA_ESTRATEGICA_QUE_NO_PERTENECE_EL_PADRE);
                            throw b;
                        }
                    }

                    //verifica que los subprogramas no tengan el mismo nombre que el programa
                    if (hijo.getNombre().equals(p.getNombre())) {
                        BusinessException b = new BusinessException();
                        String[] params = {hijo.getNombre()};
                        b.addError(ConstantesErrores.ERR_SUBPROGRAMAS_TIENE_MISMO_NOMBRE_QUE_PROGRAMA_0, params);
                        throw b;
                    }

                    //verifica que no tenga dos hijos iguales
                    for (ProgramaPresupuestario hijo2 : p.getProgramasPresupuestarios()) {
                        if (!hijo.equals(hijo2) && hijo.getNombre().equals(hijo2.getNombre())) {
                            BusinessException b = new BusinessException();
                            String[] params = {hijo.getNombre()};
                            b.addError(ConstantesErrores.ERR_UN_EXISTEN_SUBPROGRAMAS_CON_EL_MISMO_NOMBRE_0, params);
                            throw b;

                        }
                    }
                    //se validan los techos para los hijos
                    validarTechosPresupuestarioPrograma(hijo);
                }
                //chequea que solo se relacione con programas institucionales de la misma planificacion
                for (ProgramaInstitucional institucional : p.getProgramasInstitucionales()) {
                    if (!p.getPlanificacionEstrategica().equals(institucional.getPlanificacionEstrategica())) {
                        BusinessException b = new BusinessException();
                        b.addError(ConstantesErrores.ERR_PROGRAMA_INSTITUCIONAL_TIENE_ASOCIADA_UNA_PLANIFICACION_ESTRATEGICA_DISTINTA_AL_PRESUPUESTARIO);
                        throw b;
                    }
                }
                //se validan los techos para el
                validarTechosPresupuestarioPrograma(p);
            } else {
                //es un hijo
                //copia la planificacion del padre
                ProgramaPresupuestario padre = p.getProgramaPresupuestario();
                p.setPlanificacionEstrategica(padre.getPlanificacionEstrategica());
                validarTechosPresupuestarioPrograma(p);
            }
            generalDAO.update(p);

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
     * Este método determina si un programa presupuestario se puede eliminar
     *
     * @param id
     * @return
     */
    public boolean eliminableProgramaPresupuestario(Integer id) {
        try {
            if (id == null) {
                return true;
            }
            List l = generalDAO.findByOneProperty(ProgramaPresupuestarioTechoAnio.class, "programaPresupuestario.id", id);
            if (!l.isEmpty()) {
                return false;
            }

            l = generalDAO.findByOneProperty(Proyecto.class, "programaPresupuestario.id", id);
            if (!l.isEmpty()) {
                return false;
            }
            return true;
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
     * Este método valida los techos de un programa presupuestario
     *
     * @param p
     */
    private void validarTechosPresupuestarioPrograma(ProgramaPresupuestario p) {
        Map<Integer, BigDecimal> usadoPorAnio = new LinkedHashMap();
        Iterator<TechoProgramaPresupueastarioFuente> iter = p.getTechos().iterator();
        while (iter.hasNext()) {
            TechoProgramaPresupueastarioFuente techo = iter.next();

            //VERIFICA QUE NO SE SOBREPASE DEL TECHO PARA EL AÑO
            for (TechoProgramaPresupueastarioAnio techoAnio : techo.getTechos()) {
                BigDecimal usado = usadoPorAnio.get(techoAnio.getAnioFiscal().getAnio());
                if (usado == null) {
                    usado = BigDecimal.ZERO;
                }
                usado = usado.add(techoAnio.getMonto());
                usadoPorAnio.put(techoAnio.getAnioFiscal().getAnio(), usado);
            }
        }

        for (Map.Entry<Integer, BigDecimal> entry : usadoPorAnio.entrySet()) {
            BigDecimal techo = BigDecimal.ZERO;
            for (ProgramaPresupuestarioTechoAnio iterTecho : p.getTechosPresupuestarios()) {
                if (iterTecho.getAnioFiscal().getAnio().equals(entry.getKey())) {
                    techo = techo.add(iterTecho.getTecho());
                }
            }
            if (entry.getValue().compareTo(techo) > 0) {
                BusinessException b = new BusinessException();
                String[] values = {p.getNombre(), String.valueOf(entry.getKey()), String.valueOf(entry.getValue()), String.valueOf(techo)};
                b.addError(ConstantesErrores.ERR_TECHOS_PROGRAMA_0_ANIO_1_MONTO_2_MAYORQUE_3, values);
                throw b;
            }
        }

    }

    /**
     * Este método elimina un programa presupuestario
     *
     * @param idPrograma
     */
    public void eleiminarProgramaPresupuestario(Integer idPrograma) {
        try {
            ProgramaPresupuestario toRemove = (ProgramaPresupuestario) generalDAO.find(ProgramaPresupuestario.class, idPrograma);
            if (toRemove.getProgramasInstitucionales().size() > 0) {
                BusinessException b = new BusinessException();
                String[] params = {"programas institucionales", "al programa presupuestario"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }
            if (toRemove.getSupuestos().size() > 0) {
                BusinessException b = new BusinessException();
                String[] params = {"supuestos", "al programa presupuestario"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }
            if (toRemove.getProgramasPresupuestarios().size() > 0) {
                BusinessException b = new BusinessException();
                String[] params = {"subprogramas", "al programa presupuestario"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }
            if (toRemove.getIndicadoresAsociados().size() > 0) {
                BusinessException b = new BusinessException();
                String[] params = {"indicadores", "al programa presupuestario"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;
            }

            if (!generalDAO.findByOneProperty(ProgramaIndicador.class, "programa.id", idPrograma).isEmpty()) {
                BusinessException b = new BusinessException();
                String[] params = {"indicadores", "al programa presupuestario"};
                b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA, params);
                throw b;

            }

            generalDAO.delete(toRemove);

        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA);
            throw b;
        }
    }

    /**
     * Este método devuelve los programas presupuestarios para la funcionalidad
     * de cargar techos
     *
     * @param idPlanificacionEstrategica
     * @return
     */
    public List<ProgramaPresupuestario> getProgramasPresupuestariosParaCargaDeTechos(Integer idPlanificacionEstrategica) {
        try {
            List<ProgramaPresupuestario> l = programaDAO.getProgramasPresupuestariosParaCargarTechos(idPlanificacionEstrategica);
            for (ProgramaPresupuestario p : l) {
                p.getTechosPresupuestarios().isEmpty();
                for (ProgramaPresupuestario s : p.getProgramasPresupuestarios()) {
                    s.getTechosPresupuestarios().isEmpty();
                    s.setProyectos(programaDAO.getProyectosEnPrograma(s.getId()));
                    for (Proyecto proy : s.getProyectos()) {
                        for (ProyectoFuente aporte : proy.getFuentesProyecto()) {
                            aporte.getTechos().isEmpty();
                        }
                    }
                }
            }
            return l;
        } catch (BusinessException be) {
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_EXISTE_ENTIDAD_ASOCIADA);
            throw b;
        }
    }

    /**
     * Este método permite validar un programa presupuestario
     *
     * @param root
     * @param noExactos
     */
    public static void validaNodoPrograma(ProgramaPresupuestario root, Set<String> noExactos) {
        for (ProgramaPresupuestarioTechoAnio techoAnio : root.getTechosPresupuestarios()) {
            //la esttructura admite hijos proyectos y subprogrmas por eso se usa la misma variable
            BigDecimal sumaHijos = BigDecimal.ZERO;
            //valida los subprogramas          
            for (ProgramaPresupuestario subprograma : root.getProgramasPresupuestarios()) {
                for (ProgramaPresupuestarioTechoAnio subprogramaTechoAnio : subprograma.getTechosPresupuestarios()) {
                    if (subprogramaTechoAnio.getAnioFiscal().equals(techoAnio.getAnioFiscal())) {
                        sumaHijos = sumaHijos.add(subprogramaTechoAnio.getTecho());
                    }
                }
            }
            if (sumaHijos.compareTo(techoAnio.getTecho()) > 0) {
                BusinessException b = new BusinessException();
                String[] values = {root.getNombre(), String.valueOf(techoAnio.getAnioFiscal().getAnio())};
                b.addError(ConstantesErrores.ERR_SUMA_DE_TECHOS_DE_SUBPROGRAMA_MAYOR_QUE_LA_DEL_PROGRAMA_0_PARA_ANIO_1, values);
                throw b;
            }

            for (Proyecto proyecto : root.getProyectos()) {
                for (ProyectoTechoPresupuestarioAnio techoProyecto : proyecto.getTechos()) {
                    if (techoProyecto.getAnioFiscal().equals(techoAnio.getAnioFiscal())) {
                        sumaHijos = sumaHijos.add(techoProyecto.getTecho());
                    }
                }
            }
            if (sumaHijos.compareTo(techoAnio.getTecho()) > 0) {
                BusinessException b = new BusinessException();
                String[] values = {root.getNombre(), String.valueOf(techoAnio.getAnioFiscal().getAnio())};
                b.addError(ConstantesErrores.ERR_SUMA_DE_TECHOS_DE_PROYECTOS_MAYOR_QUE_LA_DEL_PROGRAMA_0_PARA_ANIO_1, values);
                throw b;
            }

            if (sumaHijos.compareTo(techoAnio.getTecho()) != 0) {
                noExactos.add(root.getNombre());
            }

        }
    }

    /**
     * Operación intermedia que se utiliza para validar la los topes de un
     * proyecto desde los programas presupuestario
     *
     * @param root
     */
    public static void validarProyecto(Proyecto root, Set<String> noExactos) {
        for (ProyectoTechoPresupuestarioAnio techoAnio : root.getTechos()) {
            BigDecimal sumaHijos = BigDecimal.ZERO;
            for (ProyectoFuente aporte : root.getFuentesProyecto()) {
                for (ProyectoAporteTechoPresupuestarioAnio techoAporte : aporte.getTechos()) {
                    if (techoAporte.getAnioFiscal().equals(techoAnio.getAnioFiscal())) {
                        sumaHijos = sumaHijos.add(techoAporte.getTecho());
                    }
                }
            }
            if (sumaHijos.compareTo(techoAnio.getTecho()) > 0) {
                BusinessException b = new BusinessException();
                String[] values = {root.getNombre(), String.valueOf(techoAnio.getAnioFiscal().getAnio())};
                b.addError(ConstantesErrores.ERR_SUMA_DE_TECHOS_DE_FUENTES_DE_PROYECTO_MAYOR_QUE_PROYECTO_0_PARA_ANIO_1, values);
                throw b;
            }
            if (sumaHijos.compareTo(techoAnio.getTecho()) != 0) {
                noExactos.add(root.getNombre());
            }
        }
    }

    public static void validacionRecursiva(ProgramaPresupuestario p, Set<String> noExactos) {
        validaNodoPrograma(p, noExactos);
        for (Proyecto proy : p.getProyectos()) {
            validarProyecto(proy, noExactos);
        }

        for (ProgramaPresupuestario subprograma : p.getProgramasPresupuestarios()) {
            validacionRecursiva(subprograma, noExactos);
        }

    }

    /**
     * Este método guarda los techos de un conjunto de programas
     * presupuestarios.
     *
     * @param programas
     * @return
     */
    public Set<String> guardarTechos(List<ProgramaPresupuestario> programas) {
        try {
            Set<String> noExactos = new LinkedHashSet();
            for (ProgramaPresupuestario p : programas) {
                validacionRecursiva(p, noExactos);

                //se asume solo el nivel de programa subprograma
                for (ProgramaPresupuestario sp : p.getProgramasPresupuestarios()) {
                    for (Proyecto proy : sp.getProyectos()) {
                        generalDAO.update(proy);
                    }
                }
                for (Proyecto proy : p.getProyectos()) {
                    generalDAO.update(proy);
                }

                generalDAO.update(p);
            }

            return noExactos;
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
    public List<Programa> getProgramaByNombre(String query) {
        return programaDAO.getProgramaByNombre(query);
    }
    
    public List<Programa> obtenerPorFiltro(FiltroPrograma filtro) {
        List<Programa> resultado = new LinkedList();
        List<ProgramaInstitucional> lista = programaDAO.getProgramasInstitucioanalesActivosByQuery(filtro.getNombre());
        for(ProgramaInstitucional prog : lista) {
            resultado.add(prog);
        }
        return resultado;
    }
    
    
    public List<ProgramaInstitucional> obtenerProgramasPorFiltro(FiltroPrograma filtro) {
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

            if(filtro.getEstado() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "estado", filtro.getEstado());
                criterios.add(criterio);
            }
            
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return programaDAO.findEntityByCriteria(ProgramaInstitucional.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
}
