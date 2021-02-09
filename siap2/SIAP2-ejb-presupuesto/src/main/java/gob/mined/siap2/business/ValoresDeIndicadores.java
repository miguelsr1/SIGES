/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import gob.mined.siap2.business.datatypes.DataActividad;
import gob.mined.siap2.business.datatypes.DataMetaIndicador;
import gob.mined.siap2.business.datatypes.DataTipoIndicador;
import gob.mined.siap2.business.datatypes.DataVerIndicadorTipo;
import gob.mined.siap2.business.datatypes.DataVerValoresIndicadores;
import gob.mined.siap2.business.datatypes.DataVerValoresValor;
import gob.mined.siap2.business.datatypes.DataVerValoresValorUT;
import gob.mined.siap2.business.datatypes.LineaMetaIndicadorTO;
import gob.mined.siap2.business.utils.ReportesUtils;
import gob.mined.siap2.business.utils.TipoSeguimientoUtils;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.Categoria;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.LineaEstrategica;
import gob.mined.siap2.entities.data.impl.MetaIndicador;
import gob.mined.siap2.entities.data.impl.MetaIndicadorProducto;
import gob.mined.siap2.entities.data.impl.MetaIndicadorProyecto;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividad;
import gob.mined.siap2.entities.data.impl.POActividadAsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POActividadProyecto;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.PeriodoSeguimientoIndicadores;
import gob.mined.siap2.entities.data.impl.PlanificacionEstrategica;
import gob.mined.siap2.entities.data.impl.Programa;
import gob.mined.siap2.entities.data.impl.ProgramaIndicador;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoComponente;
import gob.mined.siap2.entities.data.impl.ProyectoEstProducto;
import gob.mined.siap2.entities.data.impl.ProyectoEstructura;
import gob.mined.siap2.entities.data.impl.ProyectoMacroActividad;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.data.impl.ValoresDeIndicador;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.entities.enums.TipoEstructura;
import gob.mined.siap2.entities.enums.TipoMetaIndicador;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.enums.TipoPOAPAC;
import gob.mined.siap2.entities.enums.TipoProyecto;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.POADAO;
import gob.mined.siap2.persistence.dao.imp.ProyectoDAO;
import gob.mined.siap2.persistence.dao.imp.ValorDeIndicadorDAO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Esta clase implementa los métodos asociados a valores de indicadores.
 *
 * @author Sofis Solutions
 */
@Stateless(name = "ValoresDeIndicadores")
@LocalBean
public class ValoresDeIndicadores {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    @JPADAO
    private ValorDeIndicadorDAO valorDeIndicadorDAO;
    @Inject
    @JPADAO
    private POADAO poadao;
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    @JPADAO
    private ProyectoDAO proyectoDAO;

    /**
     * Método encargado de devolver los poa para la carga de sus indicadores
     *
     * @param unidadesTecnicas
     * @param idAnioFiscal
     * @return
     */
    public List<DataTipoIndicador> getValoresDeIndicadores(List<Integer> unidadesTecnicas, Integer idAnioFiscal) {
        try {
            Map<Categoria, DataTipoIndicador> map = new LinkedHashMap();

            AnioFiscal anioFiscal = (AnioFiscal) generalDAO.find(AnioFiscal.class, idAnioFiscal);
            if (anioFiscal == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_ANIO_FISCAL);
                throw b;
            }

            List<Integer> proyectosAnalizados = new LinkedList<>();
            List<Proyecto> proyectos = proyectoDAO.getProyectosConPOAParaValoresIndicadores(idAnioFiscal);
            for (Proyecto proyecto : proyectos) {
                if (!proyectosAnalizados.contains(proyecto.getId())) {
                    proyectosAnalizados.add(proyecto.getId());

                    for (ProyectoComponente estructura : proyecto.getProyectoComponentes()) {
                        generarListaParaProductos(estructura.getProductos(), unidadesTecnicas, proyecto, anioFiscal, map);
                    }
                    for (ProyectoMacroActividad estructura : proyecto.getProyectoMacroactividad()) {
                        generarListaParaProductos(estructura.getProductos(), unidadesTecnicas, proyecto, anioFiscal, map);
                    }

                    for (ProgramaIndicador programaIndicador : proyecto.getIndicadoresAsociados()) {
                        if (unidadesTecnicas.contains(programaIndicador.getUtResponsable().getId())) {
                            List<MetaIndicadorProyecto> metaConsulta = valorDeIndicadorDAO.getMetaIndicadorProyecto(programaIndicador.getIndicador().getId(), anioFiscal.getId(), proyecto.getId());
                            if (!metaConsulta.isEmpty()) {
                                addElementToRetorno(map, metaConsulta.get(0), proyecto, programaIndicador.getUtResponsable());
                            } else {
                                MetaIndicadorProyecto meta = new MetaIndicadorProyecto();
                                meta.setTipo(TipoMetaIndicador.META_INDICADOR);
                                meta.setAnioFiscal(anioFiscal);
                                meta.setIndicador(programaIndicador.getIndicador());
                                meta.setProyecto(proyecto);
                                meta.setTipoSeguimiento(programaIndicador.getIndicador().getTipo().getTipoSeguimiento());
                                TipoSeguimientoUtils.cargarValoresMeta(meta);

                                addElementToRetorno(map, meta, proyecto, programaIndicador.getUtResponsable());
                            }
                        }
                    }

                }
            }

            List<DataTipoIndicador> l = new LinkedList<>();
            for (Map.Entry<Categoria, DataTipoIndicador> entry : map.entrySet()) {
                l.add(entry.getValue());
            }

            //se ordena la lista por tipo de indicadores
            Collections.sort(l, new Comparator<DataTipoIndicador>() {
                @Override
                public int compare(DataTipoIndicador o1, DataTipoIndicador o2) {
                    return o1.getTipoIndicador().getNombre().compareTo(o2.getTipoIndicador().getNombre());
                }
            });
            Collections.sort(l, new Comparator<DataTipoIndicador>() {
                @Override
                public int compare(DataTipoIndicador o1, DataTipoIndicador o2) {
                    if (o1.getTipoIndicador().getOrden() != null && o2.getTipoIndicador().getOrden() != null) {
                        return o1.getTipoIndicador().getOrden().compareTo(o2.getTipoIndicador().getOrden());
                    }
                    return 0;
                }
            });

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

    private void addElementToRetorno(Map<Categoria, DataTipoIndicador> map, MetaIndicador element, Proyecto proyecto, UnidadTecnica ut) {
        Indicador indicador = null;
        if (element.getTipo() == TipoMetaIndicador.META_PRODUCTO) {
            indicador = ((MetaIndicadorProducto) element).getProyectoEstProducto().getProducto();
        } else if (element.getTipo() == TipoMetaIndicador.META_INDICADOR) {
            indicador = ((MetaIndicadorProyecto) element).getIndicador();
        }

        DataTipoIndicador dataTipoIndicador = null;
        if (indicador != null && indicador.getTipo() != null) {
            dataTipoIndicador = map.get(indicador.getTipo());
        }

        if (dataTipoIndicador == null) {
            dataTipoIndicador = new DataTipoIndicador();
            dataTipoIndicador.setMetas(new LinkedList<DataMetaIndicador>());
            if (indicador != null) {
                dataTipoIndicador.setTipoIndicador(indicador.getTipo());
            }

            if (indicador != null && indicador.getTipo() != null) {
                map.put(indicador.getTipo(), dataTipoIndicador);
            }
        }

        DataMetaIndicador data = new DataMetaIndicador();
        data.setProyecto(proyecto);
        data.setUt(ut);
        data.setMetaIndicador(element);
        data.setIndicador(indicador);

        dataTipoIndicador.getMetas().add(data);
    }

    private void generarListaParaProductos(List<ProyectoEstProducto> productos, List<Integer> unidadesTecnicas, Proyecto proyecto, AnioFiscal anioFiscal, Map<Categoria, DataTipoIndicador> map) {

        for (ProyectoEstProducto producto : productos) {
            //si el producto le pertenece a la ut del usuario
            if (unidadesTecnicas.contains(producto.getUnidadTecnica().getId())) {
                List<MetaIndicadorProducto> metaConsulta = valorDeIndicadorDAO.getMetaIndicadorProducto(producto.getId(), anioFiscal.getId());
                if (!metaConsulta.isEmpty()) {

                    addElementToRetorno(map, metaConsulta.get(0), proyecto, producto.getUnidadTecnica());
                } else {
                    MetaIndicadorProducto meta = new MetaIndicadorProducto();
                    meta.setTipo(TipoMetaIndicador.META_PRODUCTO);
                    meta.setAnioFiscal(anioFiscal);
                    meta.setProyectoEstProducto(producto);
                    meta.setTipoSeguimiento(producto.getProducto().getTipo().getTipoSeguimiento());
                    TipoSeguimientoUtils.cargarValoresMeta(meta);

                    addElementToRetorno(map, meta, proyecto, producto.getUnidadTecnica());

                }
            }
        }

    }

    /**
     * Método que retorna cuáles son las posiciones de los valores de los
     * indicadores habilitadas para realizar la carga
     *
     * @param tipoSeguimiento
     * @param tipoProyecto
     * @param idAnioFiscal
     * @return
     */
    public List<Boolean> getPosicionesHabilitadasParaCarga(TipoSeguimiento tipoSeguimiento, TipoProyecto tipoProyecto, Integer idAnioFiscal) {
        List<Boolean> res = new LinkedList<>();
        boolean habilitadoMensual = false;
        boolean habilitadoTrimestral = false;
        boolean habilitadoCuatrimestral = false;
        boolean habilitadoSemestral = false;
        boolean habilitadoAnual = false;

        int size = 0;
        if (tipoSeguimiento == TipoSeguimiento.MENSUAL) {
            habilitadoMensual = true;
            size = 12;
        } else if (tipoSeguimiento == TipoSeguimiento.TRIMESTRAL) {
            habilitadoTrimestral = true;
            size = 4;
        } else if (tipoSeguimiento == TipoSeguimiento.CUATRIMESTRAL) {
            habilitadoCuatrimestral = true;
            size = 3;
        } else if (tipoSeguimiento == TipoSeguimiento.SEMESTRAL) {
            habilitadoSemestral = true;
            size = 2;
        } else if (tipoSeguimiento == TipoSeguimiento.ANUAL) {
            habilitadoAnual = true;
            size = 1;
        }

        boolean aplicaProyectosDeInversion = false;
        boolean aplicaProyectosAdministrarivos = false;
        if (tipoProyecto == TipoProyecto.ADMINISTRATIVO) {
            aplicaProyectosAdministrarivos = true;
        } else {
            aplicaProyectosDeInversion = true;
        }

        Date fechaAcual = new Date();

        for (int iter = 0; iter < size; iter++) {
            int posicionHabilitadaMensual = iter;
            int posicionHabilitadaTrimestral = iter;
            int posicionHabilitadaCuatrimestral = iter;
            int posicionHabilitadaSemestral = iter;

            List<PeriodoSeguimientoIndicadores> habilitados = valorDeIndicadorDAO.getPeriodosHabilitados(fechaAcual,
                    idAnioFiscal,
                    habilitadoMensual,
                    posicionHabilitadaMensual,
                    habilitadoTrimestral,
                    posicionHabilitadaTrimestral,
                    habilitadoCuatrimestral,
                    posicionHabilitadaCuatrimestral,
                    habilitadoSemestral,
                    posicionHabilitadaSemestral,
                    habilitadoAnual,
                    aplicaProyectosDeInversion,
                    aplicaProyectosAdministrarivos
            );
            res.add(!habilitados.isEmpty());

        }
        return res;

    }

    /**
     * Método encargado de guardar un indicador
     *
     * @param valorEditando
     * @return
     */
    public MetaIndicador guardar(MetaIndicador valorEditando) {
        try {
            return (MetaIndicador) generalDAO.update(valorEditando);

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
     * Método encargado de cambiarle el estado a una actividad
     *
     * @param idActividad
     * @param estado
     * @return
     */
    public POActividadBase cambiarEstadoActividad(Integer idActividad, EstadoActividadPOA estado) {
        try {
            POActividadBase actividad = (POActividadBase) generalDAO.find(POActividadBase.class, idActividad);
            actividad.setFechaCambioEstado(new Date());
            actividad.setEstado(estado);
            return (POActividadBase) generalDAO.update(actividad);
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
     * guarda las metas para un indicador
     *
     * @param techos
     */
    public void guardarTechos(List<DataTipoIndicador> techos) {
        try {
            for (DataTipoIndicador techo : techos) {
                for (DataMetaIndicador meta : techo.getMetas()) {
                    generalDAO.update(meta.getMetaIndicador());
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
     * Método encargado para la visualización de los valores de los indicadores
     *
     * @param unidadesTecnicas
     * @param idAnioFiscal
     * @return
     */
    public List<DataVerIndicadorTipo> getVisualizacionDeValoresDeIndicadores(Integer idAnioFiscal, Integer idProgramaInstitucional, Integer idProgramaPresupuestario) {
        try {
            Map<Categoria, DataVerIndicadorTipo> map = new LinkedHashMap();

            AnioFiscal anioFiscal = (AnioFiscal) generalDAO.find(AnioFiscal.class, idAnioFiscal);
            if (anioFiscal == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_ANIO_FISCAL);
                throw b;
            }
            boolean filtraPorPrograma = (idProgramaInstitucional != null || idProgramaPresupuestario != null);

            List<Integer> proyectosAnalizados = new LinkedList<>();
            //trae todos los proyectos para los que existe un poa en ese año fiscal
            List<Proyecto> proyectos = proyectoDAO.getProyectosConPOAParaValoresIndicadores(idAnioFiscal);
            for (Proyecto proyecto : proyectos) {
                if (!proyectosAnalizados.contains(proyecto.getId())) {
                    proyectosAnalizados.add(proyecto.getId());
                    //valida que no se este filtrando por programa
                    if (!filtraPorPrograma) {
                        for (ProyectoComponente estructura : proyecto.getProyectoComponentes()) {
                            generarListaParaProductosEnVisualizarIndicador(estructura.getProductos(), proyecto, anioFiscal, map);
                        }
                        for (ProyectoMacroActividad estructura : proyecto.getProyectoMacroactividad()) {
                            generarListaParaProductosEnVisualizarIndicador(estructura.getProductos(), proyecto, anioFiscal, map);
                        }
                    }
                    for (ProgramaIndicador programaIndicador : proyecto.getIndicadoresAsociados()) {
                        //se filtran los programas qu ecorresopndan
                        if ((!filtraPorPrograma)
                                || (idProgramaInstitucional != null && idProgramaInstitucional.equals(programaIndicador.getPrograma().getId()))
                                || (idProgramaPresupuestario != null && idProgramaPresupuestario.equals(programaIndicador.getPrograma().getId()))) {

                            List<MetaIndicadorProyecto> metaConsulta = valorDeIndicadorDAO.getMetaIndicadorProyecto(programaIndicador.getIndicador().getId(), anioFiscal.getId(), proyecto.getId());
                            if (!metaConsulta.isEmpty()) {
                                for (MetaIndicadorProyecto metaIndicadorProyecto : metaConsulta) {
                                    addElementToVerValoresIndicadores(map, metaIndicadorProyecto, proyecto, programaIndicador.getUtResponsable(), anioFiscal);
                                }
                            } else {
                                //se crea uno vacio para que aparezca vacio y se añade
                                MetaIndicadorProyecto meta = new MetaIndicadorProyecto();
                                meta.setTipo(TipoMetaIndicador.META_INDICADOR);
                                meta.setAnioFiscal(anioFiscal);
                                meta.setIndicador(programaIndicador.getIndicador());
                                meta.setProyecto(proyecto);
                                meta.setTipoSeguimiento(programaIndicador.getIndicador().getTipo().getTipoSeguimiento());
                                TipoSeguimientoUtils.cargarValoresMeta(meta);

                                addElementToVerValoresIndicadores(map, meta, proyecto, programaIndicador.getUtResponsable(), anioFiscal);
                            }
                        }
                    }

                }
            }

            List<DataVerIndicadorTipo> l = new LinkedList<>();
            for (Map.Entry<Categoria, DataVerIndicadorTipo> entry : map.entrySet()) {
                DataVerIndicadorTipo tipoIndicador = entry.getValue();
                l.add(tipoIndicador);

                //se calculan los totales, esto se tendria que hacer a medida que se agregan
                for (DataVerValoresIndicadores valorIndicador : tipoIndicador.getIndicadores()) {
                    valorIndicador.setTotalValor(BigDecimal.ZERO);
                    valorIndicador.setTotalMeta(BigDecimal.ZERO);
                    for (DataVerValoresValor valor : valorIndicador.getValores()) {
                        if (valor.getMeta() != null) {
                            valorIndicador.setTotalMeta(valorIndicador.getTotalMeta().add(valor.getMeta()));
                        }
                        if (valor.getValor() != null) {
                            valorIndicador.setTotalValor(valorIndicador.getTotalValor().add(valor.getValor()));
                        }
                    }

                    //ahora se suman totales a nivel de ut
                    for (DataVerValoresValorUT valorUT : valorIndicador.getDesgloce()) {
                        valorUT.setTotalValor(BigDecimal.ZERO);
                        valorUT.setTotalMeta(BigDecimal.ZERO);
                        for (DataVerValoresValor valor : valorUT.getValores()) {
                            if (valor.getMeta() != null) {
                                valorUT.setTotalMeta(valorUT.getTotalMeta().add(valor.getMeta()));
                            }
                            if (valor.getValor() != null) {
                                valorUT.setTotalValor(valorUT.getTotalValor().add(valor.getValor()));
                            }
                        }
                    }
                }
            }

            //se ordena la lista por tipo de indicadores
            Collections.sort(l, new Comparator<DataVerIndicadorTipo>() {
                @Override
                public int compare(DataVerIndicadorTipo o1, DataVerIndicadorTipo o2) {
                    return o1.getTipoIndicador().getNombre().compareTo(o2.getTipoIndicador().getNombre());
                }
            });
            Collections.sort(l, new Comparator<DataVerIndicadorTipo>() {
                @Override
                public int compare(DataVerIndicadorTipo o1, DataVerIndicadorTipo o2) {
                    if (o1.getTipoIndicador().getOrden() != null && o2.getTipoIndicador().getOrden() != null) {
                        return o1.getTipoIndicador().getOrden().compareTo(o2.getTipoIndicador().getOrden());
                    }
                    return 0;
                }
            });

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
     * Este método genera la lista con todos los productos asociados a un
     * indicador.
     *
     * @param productos
     * @param proyecto
     * @param anioFiscal
     * @param map
     */
    private void generarListaParaProductosEnVisualizarIndicador(List<ProyectoEstProducto> productos, Proyecto proyecto, AnioFiscal anioFiscal, Map<Categoria, DataVerIndicadorTipo> map) {

        for (ProyectoEstProducto producto : productos) {
            //si el producto le pertenece a la ut del usuario

            List<MetaIndicadorProducto> metaConsulta = valorDeIndicadorDAO.getMetaIndicadorProducto(producto.getId(), anioFiscal.getId());
            if (!metaConsulta.isEmpty()) {
                addElementToVerValoresIndicadores(map, metaConsulta.get(0), proyecto, producto.getUnidadTecnica(), anioFiscal);
            } else {
                MetaIndicadorProducto meta = new MetaIndicadorProducto();
                meta.setTipo(TipoMetaIndicador.META_PRODUCTO);
                meta.setAnioFiscal(anioFiscal);
                meta.setProyectoEstProducto(producto);
                meta.setTipoSeguimiento(producto.getProducto().getTipo().getTipoSeguimiento());
                TipoSeguimientoUtils.cargarValoresMeta(meta);

                addElementToVerValoresIndicadores(map, meta, proyecto, producto.getUnidadTecnica(), anioFiscal);

            }

        }

    }

    /**
     * Este método agrega los valores a visualizar para un indicador en
     * particular.
     *
     * @param map
     * @param element
     * @param proyecto
     * @param ut
     * @param anioFiscal
     */
    private void addElementToVerValoresIndicadores(Map<Categoria, DataVerIndicadorTipo> map, MetaIndicador element, Proyecto proyecto, UnidadTecnica ut, AnioFiscal anioFiscal) {
        Indicador indicador = null;
        if (element.getTipo() == TipoMetaIndicador.META_PRODUCTO) {
            indicador = ((MetaIndicadorProducto) element).getProyectoEstProducto().getProducto();
        } else if (element.getTipo() == TipoMetaIndicador.META_INDICADOR) {
            indicador = ((MetaIndicadorProyecto) element).getIndicador();
        }

        DataVerIndicadorTipo tipoIndicador = map.get(indicador.getTipo());
        if (tipoIndicador == null) {
            tipoIndicador = new DataVerIndicadorTipo();
            tipoIndicador.setIndicadores(new LinkedList());
            tipoIndicador.setTipoIndicador(indicador.getTipo());

            map.put(indicador.getTipo(), tipoIndicador);
        }

        DataVerValoresIndicadores dataValoresIndicador = buscarIndicador(tipoIndicador, anioFiscal, indicador);
        if (dataValoresIndicador == null) {
            dataValoresIndicador = new DataVerValoresIndicadores();
            dataValoresIndicador.setValores(new LinkedList());
            dataValoresIndicador.setDesgloce(new LinkedList<DataVerValoresValorUT>());
            dataValoresIndicador.setAnioFiscal(anioFiscal);
            dataValoresIndicador.setIndicador(indicador);

            tipoIndicador.getIndicadores().add(dataValoresIndicador);
        }

        DataVerValoresValorUT dataValoresUT = new DataVerValoresValorUT();
        dataValoresUT.setAnioFiscal(anioFiscal);
        dataValoresUT.setProyecto(proyecto);
        dataValoresUT.setUt(ut);
        dataValoresUT.setValores(new LinkedList<DataVerValoresValor>());

        dataValoresIndicador.getDesgloce().add(dataValoresUT);

        for (int i = 0; i < element.getValores().size(); i++) {
            ValoresDeIndicador valor = element.getValores().get(i);

            //si aun no se ha creado el valor(meta o valor) dentro del indicador, sino lo crea
            DataVerValoresValor dataValorIndicador = null;
            if (i < dataValoresIndicador.getValores().size()) {
                dataValorIndicador = dataValoresIndicador.getValores().get(i);
            } else {
                dataValorIndicador = new DataVerValoresValor();
                dataValorIndicador.setMeta(BigDecimal.ZERO);
                dataValorIndicador.setValor(BigDecimal.ZERO);
                dataValoresIndicador.getValores().add(dataValorIndicador);
            }

            DataVerValoresValor dataValorUt = new DataVerValoresValor();
            dataValorUt.setMeta(valor.getMeta());
            dataValorUt.setValor(valor.getValor());
            dataValorUt.setAlcanceYLimitante(valor.getAlcanceYLimitante());
            dataValorUt.setMedioVerificacion(valor.getMedioVerificacion());
            dataValoresUT.getValores().add(dataValorUt);

            if (valor.getMeta() != null) {
                dataValorIndicador.setMeta(dataValorIndicador.getMeta().add(valor.getMeta()));
            }
            if (valor.getValor() != null) {
                dataValorIndicador.setValor(dataValorIndicador.getValor().add(valor.getValor()));
            }
        }
    }

    /**
     * Este método obtiene todos los indicadores de un tipo para un año dado.
     *
     * @param tipoIndicador
     * @param anioFiscal
     * @param indicador
     * @return
     */
    public DataVerValoresIndicadores buscarIndicador(DataVerIndicadorTipo tipoIndicador, AnioFiscal anioFiscal, Indicador indicador) {
        for (DataVerValoresIndicadores data : tipoIndicador.getIndicadores()) {
            if (data.getIndicador().equals(indicador) && data.getAnioFiscal().equals(anioFiscal)) {
                return data;
            }
        }
        return null;
    }

    /**
     * Método que se encarga de dar las actividades para la carga
     *
     * @param unidadesTecnicas
     * @param idAnioFiscal
     * @return
     */
    public List<DataActividad> getActividadesParaCarga(List<Integer> unidadesTecnicas, Integer idAnioFiscal) {
        try {

            List<DataActividad> res = new LinkedList<>();
            //se añaden los POAs de proyecto a la línea
            List<POAProyecto> poasProyecto = valorDeIndicadorDAO.getPOASTrabajoProyectoParaFinaliarActividades(unidadesTecnicas, idAnioFiscal);
            for (POAProyecto poa : poasProyecto) {
                for (POLinea linea : poa.getLineas()) {
                    for (POActividadBase actividad : linea.getActividades()) {
                        POActividadProyecto actividadProyecto = (POActividadProyecto) actividad;

                        DataActividad data = new DataActividad();
                        data.setAnioFiscal(poa.getAnioFiscal());
                        data.setUt(poa.getUnidadTecnica());
                        data.setNombrePOA(poa.getProyecto().getNombre());
                        data.setActividad(actividadProyecto);
                        if (actividadProyecto.getActividadCodiguera() != null) {
                                data.setNombreActividad(actividadProyecto.getActividadCodiguera().getNombre());
                        }
                        data.setTipoPOA(TipoPOAPAC.POA_PROYECTO);

                        res.add(data);
                    }
                }
            }

            //se añaden los poa de accion central o asignaicono programable
            List<POAConActividades> poasConActividades = valorDeIndicadorDAO.getPOASTrabajoPAraFinalizarActividades(unidadesTecnicas, idAnioFiscal);
            for (POAConActividades poa : poasConActividades) {
                for (POActividadBase actividad : poa.getActividades()) {
                    DataActividad data = new DataActividad();
                    data.setAnioFiscal(poa.getAnioFiscal());
                    data.setUt(poa.getUnidadTecnica());
                    data.setActividad(actividad);
                    if (poa.getConMontoPorAnio().getTipo() == TipoMontoPorAnio.ACCION_CENTRAL) {
                        POActividad pOActividad = (POActividad) actividad;
                        data.setNombreActividad(pOActividad.getNombre());
                        data.setTipoPOA(TipoPOAPAC.POA_ACCION_CENTRAL);
                    }
                    if (poa.getConMontoPorAnio().getTipo() == TipoMontoPorAnio.ASIGN_NO_PROGRAMABLE) {
                        POActividadAsignacionNoProgramable actividadAsignacionNP = (POActividadAsignacionNoProgramable) actividad;
                        if (actividadAsignacionNP.getActividadNP() != null) {
                            data.setNombreActividad(actividadAsignacionNP.getActividadNP().getNombre());
                        }
                        data.setTipoPOA(TipoPOAPAC.POA_ASIGNACION_NO_PROGRAMABLE);
                    }
                    data.setNombrePOA(poa.getConMontoPorAnio().getNombre());

                    res.add(data);
                }
            }

            return res;
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
     * Este método permite obtener los datos a incluir en el reporte de
     * seguimiento de proyectos administrativos.
     *
     * @param utId
     * @param anioFiscalId
     * @param seguimiento
     * @return
     */
    public List<HashMap> obtenerInfoReporteSeguimientoProyectoAdministrativo(Integer utId, Integer anioFiscalId, String seguimiento) {
        if (seguimiento == null || !(seguimiento.equals("M") || seguimiento.equals("T"))) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_ANIO_SEGUIMIENTO);
            throw b;
        }
        if (seguimiento.equals("M")) {
            return obtenerInfoReporteSeguimientoProyectoAdministrativoMensual(utId, anioFiscalId);
        } else {
            return obtenerInfoReporteSeguimientoProyectoAdministrativoTrimestral(utId, anioFiscalId);
        }
    }

    /**
     * Este método permite obtener los datos para el informe de seguimiento de
     * proyectos administrativos mensuales
     *
     * @param utId
     * @param anioFiscalId
     * @return
     */
    private List<HashMap> obtenerInfoReporteSeguimientoProyectoAdministrativoMensual(Integer utId, Integer anioFiscalId) {
        List<HashMap> respuesta = new ArrayList<>();
        try {
            AnioFiscal anioFiscal = (AnioFiscal) generalDAO.find(AnioFiscal.class, anioFiscalId);
            if (anioFiscal == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_ANIO_FISCAL);
                throw b;
            }
            UnidadTecnica unidadTecnica = (UnidadTecnica) generalDAO.find(UnidadTecnica.class, utId);
            if (unidadTecnica == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UNIDAD_TECNICA);
                throw b;
            }

            HashMap fila1 = new HashMap();
            fila1.put("col1", "Seguimiento Mensual de Proyectos Administrativos");
            fila1.put("styleClass", "tblFilaTitulo1");
            completarFilaSeguimiento(fila1, 12);
            respuesta.add(fila1);
            HashMap filaBlanco = new HashMap();

            filaBlanco.put("styleClass", "tblFilaBlanco");
            completarFilaSeguimiento(filaBlanco, 12);
            respuesta.add(filaBlanco);

            HashMap fila2 = new HashMap();
            fila2.put("col1", "Unidad Técnica: " + unidadTecnica.getNombre());
            fila2.put("styleClass", "tblFilaTitulo1");
            completarFilaSeguimiento(fila2, 12);
            respuesta.add(fila2);
            HashMap fila3 = new HashMap();
            fila3.put("col1", "Año: " + anioFiscal.getAnio());
            fila3.put("styleClass", "tblFilaTitulo1");
            completarFilaSeguimiento(fila3, 12);
            respuesta.add(fila3);

            filaBlanco.put("styleClass", "tblFilaBlanco");
            completarFilaSeguimiento(filaBlanco, 12);
            respuesta.add(filaBlanco);

            HashMap fila4 = new HashMap();
            fila4.put("col8", "Planificado");
            fila4.put("col21", "Ejecutado");
            fila4.put("styleClass", "tblFilaTitulo2");
            completarFilaSeguimiento(fila4, 12);
            respuesta.add(fila4);

            HashMap fila5 = new HashMap();
            fila5.put("col1", "Indicador");
            fila5.put("col2", "Proyecto");
            fila5.put("col3", "M1");
            fila5.put("col4", "M2");
            fila5.put("col5", "M3");
            fila5.put("col6", "M4");
            fila5.put("col7", "M5");
            fila5.put("col8", "M6");
            fila5.put("col9", "M7");
            fila5.put("col10", "M8");
            fila5.put("col11", "M9");
            fila5.put("col12", "M10");
            fila5.put("col13", "M11");
            fila5.put("col14", "M12");
            fila5.put("col15", "Total");

            fila5.put("col16", "M1");
            fila5.put("col17", "M2");
            fila5.put("col18", "M3");
            fila5.put("col19", "M4");
            fila5.put("col20", "M5");
            fila5.put("col21", "M6");
            fila5.put("col22", "M7");
            fila5.put("col23", "M8");
            fila5.put("col24", "M9");
            fila5.put("col25", "M10");
            fila5.put("col26", "M11");
            fila5.put("col27", "M12");
            fila5.put("col28", "Total");

            fila5.put("colAlcance", "Alcance Limitante");
            fila5.put("colMedio", "Medio de Verificación");
            fila5.put("styleClass", "tblFilaTitulo3");
            completarFilaSeguimiento(fila4, 12);
            respuesta.add(fila5);

            List<MetaIndicadorProducto> metas = valorDeIndicadorDAO.getMetaIndicadoresProductoEnAniosPorUT(anioFiscalId, utId);
            for (MetaIndicadorProducto meta : metas) {
                Indicador indicador = meta.getProyectoEstProducto().getProducto();
                Proyecto proyecto = meta.getProyectoEstProducto().getProyectoEstructura().getProyecto();
                //Armo trimestres
                //Si no hay 12 valores es un error de datos y no lo considero. Es una situación que no debería pasar porque este indicador debería ser mensual.
                if (meta.getValores() != null && meta.getValores().size() == 12) {
                    String alcance = "";
                    String medioVerificacion = "";
                    HashMap fila = new HashMap();
                    fila.put("col1", indicador.getNombre());
                    fila.put("col2", proyecto.getNombre());
                    int indMeta = 3;
                    int indValor = 16;
                    BigDecimal totalMes = (BigDecimal.ZERO);
                    BigDecimal totalEjec = (BigDecimal.ZERO);
                    for (int i = 0; i < 12; i++) {
                        BigDecimal mes = (BigDecimal.ZERO);
                        BigDecimal ejec = (BigDecimal.ZERO);
                        if (meta.getValores().get(i).getMeta() != null) {
                            mes = meta.getValores().get(i).getMeta();
                            totalMes = totalMes.add(mes);
                        }
                        if (meta.getValores().get(i).getValor() != null) {
                            ejec = meta.getValores().get(i).getValor();
                            totalEjec = totalEjec.add(ejec);
                        }
                        fila.put("col" + indMeta, ReportesUtils.getNumber(mes));
                        fila.put("col" + indValor, ReportesUtils.getNumber(ejec));

                        if (meta.getValores().get(i).getAlcanceYLimitante() != null && meta.getValores().get(i).getAlcanceYLimitante().trim().length() > 0) {
                            alcance = meta.getValores().get(i).getAlcanceYLimitante();
                            medioVerificacion = meta.getValores().get(i).getMedioVerificacion();
                        }
                        indMeta++;
                        indValor++;
                    }
                    fila.put("col" + indMeta, ReportesUtils.getNumber(totalMes));//Total de las metas
                    fila.put("col" + indValor, ReportesUtils.getNumber(totalEjec));//Total de los valores
                    fila.put("colAlcance", alcance);
                    fila.put("colMedio", medioVerificacion);
                    fila.put("styleClass", "tblFilaContenido");
                    completarFilaSeguimiento(fila, 14);
                    respuesta.add(fila);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
        return respuesta;

    }

    /**
     * Este método permite obtener los datos para el reporte de seguimiento de
     * proyectos administrativos trimestrales.
     *
     * @param utId
     * @param anioFiscalId
     * @return
     */
    private List<HashMap> obtenerInfoReporteSeguimientoProyectoAdministrativoTrimestral(Integer utId, Integer anioFiscalId) {
        List<HashMap> respuesta = new ArrayList<>();
        try {
            AnioFiscal anioFiscal = (AnioFiscal) generalDAO.find(AnioFiscal.class, anioFiscalId);
            if (anioFiscal == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_ANIO_FISCAL);
                throw b;
            }
            UnidadTecnica unidadTecnica = (UnidadTecnica) generalDAO.find(UnidadTecnica.class, utId);
            if (unidadTecnica == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UNIDAD_TECNICA);
                throw b;
            }

            HashMap fila1 = new HashMap();
            fila1.put("col1", "Seguimiento Trimestral de Proyectos Administrativos");
            fila1.put("styleClass", "tblFilaTitulo1");
            completarFilaSeguimiento(fila1, 12);
            respuesta.add(fila1);
            HashMap filaBlanco = new HashMap();

            filaBlanco.put("styleClass", "tblFilaBlanco");
            completarFilaSeguimiento(filaBlanco, 12);
            respuesta.add(filaBlanco);

            HashMap fila2 = new HashMap();
            fila2.put("col1", "Unidad Técnica: " + unidadTecnica.getNombre());
            fila2.put("styleClass", "tblFilaTitulo1");
            completarFilaSeguimiento(fila2, 12);
            respuesta.add(fila2);
            HashMap fila3 = new HashMap();
            fila3.put("col1", "Año: " + anioFiscal.getAnio());
            fila3.put("styleClass", "tblFilaTitulo1");
            completarFilaSeguimiento(fila3, 12);
            respuesta.add(fila3);

            filaBlanco.put("styleClass", "tblFilaBlanco");
            completarFilaSeguimiento(filaBlanco, 12);
            respuesta.add(filaBlanco);

            HashMap fila4 = new HashMap();
            fila4.put("col5", "Planificado");
            fila4.put("col10", "Ejecutado");
            fila4.put("styleClass", "tblFilaTitulo2");
            completarFilaSeguimiento(fila4, 12);
            respuesta.add(fila4);

            HashMap fila5 = new HashMap();
            fila5.put("col1", "Indicador");
            fila5.put("col2", "Proyecto");
            fila5.put("col3", "Trim. 1");
            fila5.put("col4", "Trim. 2");
            fila5.put("col5", "Trim. 3");
            fila5.put("col6", "Trim. 4");
            fila5.put("col7", "Total");
            fila5.put("col8", "Trim. 1");
            fila5.put("col9", "Trim. 2");
            fila5.put("col10", "Trim. 3");
            fila5.put("col11", "Trim. 4");
            fila5.put("col12", "Total");
            fila5.put("colAlcance", "Alcance Limitante");
            fila5.put("colMedio", "Medio de Verificación");
            fila5.put("styleClass", "tblFilaTitulo3");
            completarFilaSeguimiento(fila4, 12);
            respuesta.add(fila5);

            List<MetaIndicadorProducto> metas = valorDeIndicadorDAO.getMetaIndicadoresProductoEnAniosPorUT(anioFiscalId, utId);
            for (MetaIndicadorProducto meta : metas) {
                Indicador indicador = meta.getProyectoEstProducto().getProducto();
                Proyecto proyecto = meta.getProyectoEstProducto().getProyectoEstructura().getProyecto();
                //Armo trimestres
                //Si no hay 12 valores es un error de datos y no lo considero. Es una situación que no debería pasar porque este indicador debería ser mensual.
                if (meta.getValores() != null && meta.getValores().size() == 12) {
                    BigDecimal trim1 = (BigDecimal.ZERO);
                    BigDecimal trim1Ejec = (BigDecimal.ZERO);
                    String alcance = "";
                    String medioVerificacion = "";
                    for (int i = 0; i <= 2; i++) {
                        ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                        if (valorDeIndicador.getMeta() != null) {
                            trim1 = trim1.add(valorDeIndicador.getMeta());
                        }
                        if (valorDeIndicador.getValor() != null) {
                            trim1Ejec = trim1Ejec.add(valorDeIndicador.getValor());
                        }
                        if (valorDeIndicador.getAlcanceYLimitante() != null && valorDeIndicador.getAlcanceYLimitante().trim().length() > 0) {
                            alcance = valorDeIndicador.getAlcanceYLimitante();
                            medioVerificacion = valorDeIndicador.getMedioVerificacion();
                        }
                    }
                    BigDecimal trim2 = (BigDecimal.ZERO);
                    BigDecimal trim2Ejec = (BigDecimal.ZERO);
                    for (int i = 3; i <= 5; i++) {
                        ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                        if (valorDeIndicador.getMeta() != null) {
                            trim2 = trim2.add(valorDeIndicador.getMeta());
                        }
                        if (valorDeIndicador.getValor() != null) {
                            trim2Ejec = trim2Ejec.add(valorDeIndicador.getValor());
                        }
                        if (valorDeIndicador.getAlcanceYLimitante() != null && valorDeIndicador.getAlcanceYLimitante().trim().length() > 0) {
                            alcance = valorDeIndicador.getAlcanceYLimitante();
                            medioVerificacion = valorDeIndicador.getMedioVerificacion();
                        }
                    }
                    BigDecimal trim3 = (BigDecimal.ZERO);
                    BigDecimal trim3Ejec = (BigDecimal.ZERO);
                    for (int i = 6; i <= 8; i++) {
                        ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                        if (valorDeIndicador.getMeta() != null) {
                            trim3 = trim3.add(valorDeIndicador.getMeta());
                        }
                        if (valorDeIndicador.getValor() != null) {
                            trim3Ejec = trim3Ejec.add(valorDeIndicador.getValor());
                        }
                        if (valorDeIndicador.getAlcanceYLimitante() != null && valorDeIndicador.getAlcanceYLimitante().trim().length() > 0) {
                            alcance = valorDeIndicador.getAlcanceYLimitante();
                            medioVerificacion = valorDeIndicador.getMedioVerificacion();
                        }
                    }
                    BigDecimal trim4 = (BigDecimal.ZERO);
                    BigDecimal trim4Ejec = (BigDecimal.ZERO);
                    for (int i = 9; i <= 11; i++) {
                        ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                        if (valorDeIndicador.getMeta() != null) {
                            trim4 = trim4.add(valorDeIndicador.getMeta());
                        }
                        if (valorDeIndicador.getValor() != null) {
                            trim4Ejec = trim4Ejec.add(valorDeIndicador.getValor());
                        }
                        if (valorDeIndicador.getAlcanceYLimitante() != null && valorDeIndicador.getAlcanceYLimitante().trim().length() > 0) {
                            alcance = valorDeIndicador.getAlcanceYLimitante();
                            medioVerificacion = valorDeIndicador.getMedioVerificacion();
                        }
                    }
                    BigDecimal total = trim1.add(trim2.add(trim3.add(trim4)));
                    BigDecimal totalEjec = trim1Ejec.add(trim2Ejec.add(trim3Ejec.add(trim4Ejec)));

                    HashMap fila = new HashMap();
                    fila.put("col1", indicador.getNombre());
                    fila.put("col2", proyecto.getNombre());
                    fila.put("col3", ReportesUtils.getNumber(trim1));
                    fila.put("col4", ReportesUtils.getNumber(trim2));
                    fila.put("col5", ReportesUtils.getNumber(trim3));
                    fila.put("col6", ReportesUtils.getNumber(trim4));
                    fila.put("col7", ReportesUtils.getNumber(total));
                    fila.put("col8", ReportesUtils.getNumber(trim1Ejec));
                    fila.put("col9", ReportesUtils.getNumber(trim2Ejec));
                    fila.put("col10", ReportesUtils.getNumber(trim3Ejec));
                    fila.put("col11", ReportesUtils.getNumber(trim4Ejec));
                    fila.put("col12", ReportesUtils.getNumber(totalEjec));
                    fila.put("colAlcance", alcance);
                    fila.put("colMedio", medioVerificacion);
                    fila.put("styleClass", "tblFilaContenido");
                    completarFilaSeguimiento(fila, 14);
                    respuesta.add(fila);

                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
        return respuesta;
    }

    /**
     * Este método permite obtener la información para generar el reporte POI de
     * Proyectos Administrativos por Programa Presupuestario
     *
     * @param planificacionId
     * @param anioFiscalId
     * @return
     */
    public List<HashMap> obtenerInfoReportePOIProyectosAdministrativosProgPresupuestario(Integer planificacionId, Integer anioFiscalId) {
        List<HashMap> respuesta = new ArrayList<>();
        try {
            AnioFiscal anioFiscal = (AnioFiscal) generalDAO.find(AnioFiscal.class, anioFiscalId);
            if (anioFiscal == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_ANIO_FISCAL);
                throw b;
            }
            PlanificacionEstrategica planificacion = (PlanificacionEstrategica) generalDAO.find(PlanificacionEstrategica.class, planificacionId);
            if (planificacion == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UNA_PLANIFICACION);
                throw b;
            }
            HashMap fila1 = new HashMap();
            fila1.put("col1", "POI Proyectos Administrativos");
            fila1.put("styleClass", "tblFilaTitulo1");
            completarFilaLinea(fila1, 10);
            respuesta.add(fila1);
            HashMap filaBlanco = new HashMap();
            filaBlanco.put("styleClass", "tblFilaBlanco");
            completarFilaLinea(filaBlanco, 10);
            respuesta.add(filaBlanco);

            HashMap fila2 = new HashMap();
            fila2.put("col7", "Programación");
            fila2.put("styleClass", "tblFilaTitulo2");
            completarFilaLinea(fila2, 10);
            respuesta.add(fila2);

            HashMap fila3 = new HashMap();
            fila3.put("col1", "Programa Presupuestario");
            fila3.put("col2", "Proyecto");
            fila3.put("col3", "UT");
            fila3.put("col4", "Indicador");
            fila3.put("col5", "Trim. 1");
            fila3.put("col6", "Trim. 2");
            fila3.put("col7", "Trim. 3");
            fila3.put("col8", "Trim. 4");
            fila3.put("col9", "Total");
            fila3.put("col10", "Monto");
            fila3.put("styleClass", "tblFilaTitulo3");
            completarFilaLinea(fila3, 10);
            respuesta.add(fila3);

            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "programaPresupuestario.planificacionEstrategica.id", planificacionId);
            String[] propiedades = {"id"};
            List<Proyecto> listaProyectos = generalDAO.findEntityByCriteria(Proyecto.class, criterio, null, null, null, null, propiedades);

            for (Proyecto proyId : listaProyectos) {
                Proyecto proyecto = generalDAO.getEntityManager().find(Proyecto.class, proyId.getId());
                List<MetaIndicadorProducto> metas = valorDeIndicadorDAO.getMetaIndicadoresProductoEnAnios(anioFiscalId, proyecto.getId());
                for (MetaIndicadorProducto meta : metas) {
                    String unidadTecnica = "";
                    if (meta.getProyectoEstProducto() != null && meta.getProyectoEstProducto().getUnidadTecnica() != null) {
                        unidadTecnica = meta.getProyectoEstProducto().getUnidadTecnica().getNombre();
                    }
                    Indicador indicador = meta.getProyectoEstProducto().getProducto();

                    //Armo trimestres
                    //Si no hay 12 valores es un error de datos y no lo considero. Es una situación que no debería pasar porque este indicador debería ser mensual.
                    if (meta.getValores() != null && meta.getValores().size() == 12) {
                        BigDecimal trim1 = (BigDecimal.ZERO);
                        for (int i = 0; i <= 2; i++) {
                            ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                            if (valorDeIndicador.getMeta() != null) {
                                trim1 = trim1.add(valorDeIndicador.getMeta());
                            }
                        }
                        BigDecimal trim2 = (BigDecimal.ZERO);
                        for (int i = 3; i <= 5; i++) {
                            ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                            if (valorDeIndicador.getMeta() != null) {
                                trim2 = trim2.add(valorDeIndicador.getMeta());
                            }
                        }
                        BigDecimal trim3 = (BigDecimal.ZERO);
                        for (int i = 6; i <= 8; i++) {
                            ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                            if (valorDeIndicador.getMeta() != null) {
                                trim3 = trim3.add(valorDeIndicador.getMeta());
                            }
                        }
                        BigDecimal trim4 = (BigDecimal.ZERO);
                        for (int i = 9; i <= 11; i++) {
                            ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                            if (valorDeIndicador.getMeta() != null) {
                                trim4 = trim4.add(valorDeIndicador.getMeta());
                            }
                        }
                        BigDecimal total = trim1.add(trim2.add(trim3.add(trim4)));

                        BigDecimal montoTotal = poadao.sumarInsumosEnProducto(anioFiscalId, meta.getProyectoEstProducto().getId());

                        HashMap fila = new HashMap();
                        fila.put("col1", proyecto.getProgramaPresupuestario().getNombre());
                        fila.put("col2", proyecto.getNombre());
                        fila.put("col3", unidadTecnica);
                        fila.put("col4", indicador.getNombre());
                        fila.put("col5", ReportesUtils.getNumber(trim1));
                        fila.put("col6", ReportesUtils.getNumber(trim2));
                        fila.put("col7", ReportesUtils.getNumber(trim3));
                        fila.put("col8", ReportesUtils.getNumber(trim4));
                        fila.put("col9", ReportesUtils.getNumber(total));
                        fila.put("col10", ReportesUtils.getNumber(montoTotal));
                        fila.put("styleClass", "tblFilaContenido");
                        completarFilaLinea(fila, 10);
                        respuesta.add(fila);

                    }
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
        return respuesta;
    }

    /**
     * Este método permite obtener los los datos para el reporte POI de
     * proyectos administrativos por programa institucional
     *
     * @param planificacionId
     * @param anioFiscalId
     * @return
     */
    public List<HashMap> obtenerInfoReportePOIProyectosAdministrativosProgInstitucional(Integer planificacionId, Integer anioFiscalId) {
        List<HashMap> respuesta = new ArrayList<>();
        try {
            AnioFiscal anioFiscal = (AnioFiscal) generalDAO.find(AnioFiscal.class, anioFiscalId);
            if (anioFiscal == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_ANIO_FISCAL);
                throw b;
            }

            PlanificacionEstrategica planificacion = (PlanificacionEstrategica) generalDAO.find(PlanificacionEstrategica.class, planificacionId);
            if (planificacion == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UNA_PLANIFICACION);
                throw b;
            }

            HashMap fila1 = new HashMap();
            fila1.put("col1", "POI Proyectos Administrativos");
            fila1.put("styleClass", "tblFilaTitulo1");
            completarFilaLinea(fila1, 9);
            respuesta.add(fila1);
            HashMap filaBlanco = new HashMap();
            filaBlanco.put("styleClass", "tblFilaBlanco");
            completarFilaLinea(filaBlanco, 9);
            respuesta.add(filaBlanco);

            HashMap fila2 = new HashMap();
            fila2.put("col7", "Programación");
            fila2.put("styleClass", "tblFilaTitulo2");
            completarFilaLinea(fila2, 9);
            respuesta.add(fila2);

            HashMap fila3 = new HashMap();
            fila3.put("col1", "Programa Institucional");
            fila3.put("col2", "Proyecto");
            fila3.put("col3", "UT");
            fila3.put("col4", "Indicador");
            fila3.put("col5", "Trim. 1");
            fila3.put("col6", "Trim. 2");
            fila3.put("col7", "Trim. 3");
            fila3.put("col8", "Trim. 4");
            fila3.put("col9", "Total");
            fila3.put("styleClass", "tblFilaTitulo3");
            completarFilaLinea(fila3, 9);
            respuesta.add(fila3);

            MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "programaInstitucional.planificacionEstrategica.id", planificacionId);
            String[] propiedades = {"id"};
            List<Proyecto> listaProyectos = generalDAO.findEntityByCriteria(Proyecto.class, criterio, null, null, null, null, propiedades);

            for (Proyecto proyId : listaProyectos) {
                Proyecto proyecto = generalDAO.getEntityManager().find(Proyecto.class, proyId.getId());
                List<MetaIndicadorProducto> metas = valorDeIndicadorDAO.getMetaIndicadoresProductoEnAnios(anioFiscalId, proyecto.getId());
                for (MetaIndicadorProducto meta : metas) {
                    String unidadTecnica = "";
                    if (meta.getProyectoEstProducto() != null && meta.getProyectoEstProducto().getUnidadTecnica() != null) {
                        unidadTecnica = meta.getProyectoEstProducto().getUnidadTecnica().getNombre();
                    }
                    Indicador indicador = meta.getProyectoEstProducto().getProducto();

                    //Armo trimestres
                    //Si no hay 12 valores es un error de datos y no lo considero. Es una situación que no debería pasar porque este indicador debería ser mensual.
                    if (meta.getValores() != null && meta.getValores().size() == 12) {
                        BigDecimal trim1 = (BigDecimal.ZERO);
                        for (int i = 0; i <= 2; i++) {
                            ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                            if (valorDeIndicador.getMeta() != null) {
                                trim1 = trim1.add(valorDeIndicador.getMeta());
                            }
                        }
                        BigDecimal trim2 = (BigDecimal.ZERO);
                        for (int i = 3; i <= 5; i++) {
                            ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                            if (valorDeIndicador.getMeta() != null) {
                                trim2 = trim2.add(valorDeIndicador.getMeta());
                            }
                        }
                        BigDecimal trim3 = (BigDecimal.ZERO);
                        for (int i = 6; i <= 8; i++) {
                            ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                            if (valorDeIndicador.getMeta() != null) {
                                trim3 = trim3.add(valorDeIndicador.getMeta());
                            }
                        }
                        BigDecimal trim4 = (BigDecimal.ZERO);
                        for (int i = 9; i <= 11; i++) {
                            ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                            if (valorDeIndicador.getMeta() != null) {
                                trim4 = trim4.add(valorDeIndicador.getMeta());
                            }
                        }
                        BigDecimal total = trim1.add(trim2.add(trim3.add(trim4)));

                        HashMap fila = new HashMap();
                        fila.put("col1", proyecto.getProgramaPresupuestario().getNombre());
                        fila.put("col2", proyecto.getNombre());
                        fila.put("col3", unidadTecnica);
                        fila.put("col4", indicador.getNombre());
                        fila.put("col5", ReportesUtils.getNumber(trim1));
                        fila.put("col6", ReportesUtils.getNumber(trim2));
                        fila.put("col7", ReportesUtils.getNumber(trim3));
                        fila.put("col8", ReportesUtils.getNumber(trim4));
                        fila.put("col9", ReportesUtils.getNumber(total));
                        fila.put("styleClass", "tblFilaContenido");
                        completarFilaLinea(fila, 9);
                        respuesta.add(fila);

                    }

                }

            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
        return respuesta;
    }

    /**
     * Este método permite obtener los datos para el reporte POA de Proyectos
     * administrativos
     *
     * @param proyectoId
     * @param anioFiscalId
     * @return
     */
    public List<HashMap> obtenerInfoReportePoaProyectoAdministrativo(Integer proyectoId, Integer anioFiscalId) {

        List<HashMap> respuesta = new ArrayList<HashMap>();
        try {
            AnioFiscal anioFiscal = (AnioFiscal) generalDAO.find(AnioFiscal.class, anioFiscalId);
            if (anioFiscal == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_ANIO_FISCAL);
                throw b;
            }
            Proyecto proyecto = (Proyecto) generalDAO.find(Proyecto.class, proyectoId);
            if (proyectoId == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_PROYECTO);
                throw b;
            }

            HashMap fila1 = new HashMap();
            fila1.put("col1", "Reporte POA");
            fila1.put("styleClass", "tblFilaTitulo1");
            completarFilaLinea(fila1, 11);
            respuesta.add(fila1);
            HashMap fila2 = new HashMap();
            fila2.put("col1", "Proyecto: " + proyecto.getNombre());
            fila2.put("styleClass", "tblFilaTitulo1");
            completarFilaLinea(fila2, 11);
            respuesta.add(fila2);
            HashMap fila3 = new HashMap();
            fila3.put("col1", "Año: " + anioFiscal.getAnio());
            fila3.put("styleClass", "tblFilaTitulo1");
            completarFilaLinea(fila3, 11);
            respuesta.add(fila3);

            HashMap filaBlanco = new HashMap();
            filaBlanco.put("styleClass", "tblFilaBlanco");
            completarFilaLinea(filaBlanco, 11);
            respuesta.add(filaBlanco);

            HashMap fila4 = new HashMap();
            fila4.put("col6", "Metas");
            fila4.put("styleClass", "tblFilaTitulo2");
            completarFilaLinea(fila4, 11);
            respuesta.add(fila4);

            HashMap fila5 = new HashMap();
            fila5.put("col1", "Línea Estratégica");
            fila5.put("col2", "Programa Institucional");
            fila5.put("col3", "Indicador");
            fila5.put("col4", "Trim. 1");
            fila5.put("col5", "Trim. 2");
            fila5.put("col6", "Trim. 3");
            fila5.put("col7", "Trim. 4");
            fila5.put("col8", "Total");
            fila5.put("col9", "Macroactividad");
            fila5.put("col10", "Responsable");
            fila5.put("col11", "Unidad Técnica");
            fila5.put("styleClass", "tblFilaTitulo3");
            respuesta.add(fila5);

            List<MetaIndicadorProducto> metas = valorDeIndicadorDAO.getMetaIndicadoresProductoEnAnios(anioFiscalId, proyectoId);
            for (MetaIndicadorProducto meta : metas) {
                ProyectoEstructura estructura = meta.getProyectoEstProducto().getProyectoEstructura();
                if (estructura.getTipo() == TipoEstructura.MACROACTIVIDAD) {//Caso contrario, no se considera.
                    ProyectoMacroActividad proyectoMacroActividad = (ProyectoMacroActividad) estructura;
                    Indicador indicador = meta.getProyectoEstProducto().getProducto();
                    for (LineaEstrategica lineaEstrategica : proyecto.getProgramaInstitucional().getLineasEstrategicas()) {

                        String responsable = "";
                        if (proyectoMacroActividad.getResponsable() != null) {
                            responsable = proyectoMacroActividad.getResponsable().getNombreCompleto();
                        }
                        String unidadTecnica = "";
                        if (meta.getProyectoEstProducto() != null && meta.getProyectoEstProducto().getUnidadTecnica() != null) {
                            unidadTecnica = meta.getProyectoEstProducto().getUnidadTecnica().getNombre();
                        }

                        //Armo trimestres
                        //Si no hay 12 valores es un error de datos y no lo considero. Es una situación que no debería pasar porque este indicador debería ser mensual.
                        if (meta.getValores() != null && meta.getValores().size() == 12) {
                            BigDecimal trim1 = (BigDecimal.ZERO);
                            for (int i = 0; i <= 2; i++) {
                                ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                                if (valorDeIndicador.getMeta() != null) {
                                    trim1 = trim1.add(valorDeIndicador.getMeta());
                                }
                            }
                            BigDecimal trim2 = (BigDecimal.ZERO);
                            for (int i = 3; i <= 5; i++) {
                                ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                                if (valorDeIndicador.getMeta() != null) {
                                    trim2 = trim2.add(valorDeIndicador.getMeta());
                                }
                            }
                            BigDecimal trim3 = (BigDecimal.ZERO);
                            for (int i = 6; i <= 8; i++) {
                                ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                                if (valorDeIndicador.getMeta() != null) {
                                    trim3 = trim3.add(valorDeIndicador.getMeta());
                                }
                            }
                            BigDecimal trim4 = (BigDecimal.ZERO);
                            for (int i = 9; i <= 11; i++) {
                                ValoresDeIndicador valorDeIndicador = meta.getValores().get(i);
                                if (valorDeIndicador.getMeta() != null) {
                                    trim4 = trim4.add(valorDeIndicador.getMeta());
                                }
                            }
                            BigDecimal total = trim1.add(trim2.add(trim3.add(trim4)));

                            HashMap fila = new HashMap();
                            fila.put("col1", lineaEstrategica.getNombre());
                            fila.put("col2", proyecto.getProgramaInstitucional().getNombre());
                            fila.put("col3", indicador.getNombre());
                            fila.put("col4", ReportesUtils.getNumber(trim1));
                            fila.put("col5", ReportesUtils.getNumber(trim2));
                            fila.put("col6", ReportesUtils.getNumber(trim3));
                            fila.put("col7", ReportesUtils.getNumber(trim4));
                            fila.put("col8", ReportesUtils.getNumber(total));
                            fila.put("col9", proyectoMacroActividad.getMacroActividad().getNombre());
                            fila.put("col10", responsable);
                            fila.put("col11", unidadTecnica);
                            fila.put("styleClass", "tblFilaContenido");
                            completarFilaLinea(fila, 11);
                            respuesta.add(fila);
                        }
                    }

                }

            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
        return respuesta;
    }

    /**
     * Este método permite obtener los datos para la elaboración del tablero de
     * indicadores.
     *
     * @param lineaEstrategicaId
     * @param anioFiscalId
     * @return
     */
    public List<HashMap> obtenerLineasMetasIndicadores(Integer lineaEstrategicaId, Integer anioFiscalId) {
        List<HashMap> respuesta = new ArrayList<HashMap>();
        try {
            AnioFiscal anioFiscal = (AnioFiscal) generalDAO.find(AnioFiscal.class, anioFiscalId);
            if (anioFiscal == null) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_UN_ANIO_FISCAL);
                throw b;
            }

            LineaEstrategica lineaEstrategica = generalDAO.getEntityManager().find(LineaEstrategica.class, lineaEstrategicaId);
            if (lineaEstrategica == null) {
                return respuesta;
            }
            HashMap fila1 = new HashMap();
            fila1.put("col1", "Línea estratégica: " + lineaEstrategica.getNombre());
            //fila1.put("col2", lineaEstrategica.getNombre());
            fila1.put("styleClass", "tblFilaTitulo1");
            completarFilaLinea(fila1, 6);
            respuesta.add(fila1);
            HashMap fila2 = new HashMap();
            fila2.put("col1", "Año:" + anioFiscal.getAnio().toString());
            //fila2.put("col2", anioFiscalId.toString());
            fila2.put("styleClass", "tblFilaTitulo1");
            completarFilaLinea(fila2, 6);
            respuesta.add(fila2);
            HashMap fila3 = new HashMap();
            fila3.put("styleClass", "tblFilaBlanco");
            completarFilaLinea(fila3, 6);//Fila completamente en blanco.
            respuesta.add(fila3);
            HashMap fila4 = new HashMap();
            fila4.put("col1", "Programa");
            fila4.put("col2", "Proyecto");
            fila4.put("col3", "Indicador");
            fila4.put("col4", "Valor meta");
            fila4.put("col5", "Valor actual");
            fila4.put("col6", "Estado");
            fila4.put("styleClass", "tblFilaTitulo2");
            completarFilaLinea(fila4, 6);//Fila completamente en blanco.
            respuesta.add(fila4);

            HashMap<String, LineaMetaIndicadorTO> map = new LinkedHashMap();

            List<MetaIndicador> metas = valorDeIndicadorDAO.getIndicadoresEnAnios(anioFiscalId);
            for (MetaIndicador meta : metas) {
                if (meta.getAnioFiscal().equals(anioFiscal)) {
                    Proyecto proyecto = null;
                    Indicador indicador = null;

                    if (meta.getTipo() == TipoMetaIndicador.META_PRODUCTO) {
                        MetaIndicadorProducto metaProducto = (MetaIndicadorProducto) meta;
                        //Hay que sacarlo de donde dijo Gustavo
                        //Si el indicador está en la relación programaIndicador, entonces lo sumo.
                        proyecto = metaProducto.getProyectoEstProducto().getProyectoEstructura().getProyecto();
                        indicador = metaProducto.getProyectoEstProducto().getProducto();

                    } else {
                        MetaIndicadorProyecto metaIndicadorProyecto = (MetaIndicadorProyecto) meta;
                        proyecto = metaIndicadorProyecto.getProyecto();
                        indicador = metaIndicadorProyecto.getIndicador();
                    }

                    //FILTRAR POR LÍNEA
                    if (estaLinea(proyecto.getProgramaInstitucional().getLineasEstrategicas(), lineaEstrategicaId)
                            || estaLinea(proyecto.getProgramaPresupuestario().getLineasEstrategicas(), lineaEstrategicaId)) {

                        List<Programa> progrmas = valorDeIndicadorDAO.getProgramasConIndicador(indicador.getId());
                        for (Programa programa : progrmas) {
                            String key = "PRG" + programa.getId() + "PRY" + proyecto.getId() + "IND" + indicador.getId();
                            LineaMetaIndicadorTO item = map.get(key);
                            if (item == null) {
                                item = new LineaMetaIndicadorTO();
                                item.setIndicador(indicador.getNombre());
                                item.setFinToleranciaVerde(indicador.getFinToleranciaVerde());
                                item.setFinToleranciaAmarillo(indicador.getFinToleranciaAmarillo());
                                item.setPrograma(programa.getNombre());
                                item.setProyecto(proyecto.getNombre());
                                item.setMeta(BigDecimal.ZERO);
                                item.setActual(BigDecimal.ZERO);

                                map.put(key, item);
                            }

                            //sumar el valor actual y meta
                            BigDecimal auxMeta = BigDecimal.ZERO;
                            BigDecimal auxActual = BigDecimal.ZERO;
                            List<Boolean> posiciones = getPosicionesHabilitadasParaCarga(meta.getTipoSeguimiento(), proyecto.getTipoProyecto(), anioFiscalId);
                            for (int i = posiciones.size() - 1; i >= 0; i--) {//Recorro de mayor a menor
                                if (posiciones.get(i) != null && posiciones.get(i)) {
                                    ValoresDeIndicador valorIndicador = meta.getValores().get(i);
                                    if (valorIndicador.getMeta() != null) {
                                        auxMeta = valorIndicador.getMeta();
                                    }
                                    if (valorIndicador.getValor() != null) {
                                        auxActual = valorIndicador.getValor();
                                    }
                                    break;
                                }
                            }

                            item.setMeta(item.getMeta().add(auxMeta));
                            item.setActual(item.getActual().add(auxActual));
                            map.put(key, item);

                        }
                    }

                }

            }

            //A partir de map armo la tabla.
            Map<String, LineaMetaIndicadorTO> treeMap = new TreeMap<>(map);

            for (LineaMetaIndicadorTO to : treeMap.values()) {

                //--- Color de estado -----
                //Variacion = (100 – Valoractual*100/ValorMeta )
                String col6RowStyleClass = "estadoBlanco";
                String estado = "";
                if (to.getFinToleranciaVerde() != null && to.getFinToleranciaAmarillo() != null) {
                    if (!to.getMeta().equals(BigDecimal.ZERO) /*&& !to.getMeta().equals(BigDecimal.ZERO)*/) {
                        BigDecimal variacion = BigDecimal.valueOf(100).subtract((to.getActual().multiply(BigDecimal.valueOf(100))).divide(to.getMeta()));
                        if (variacion.compareTo(BigDecimal.ZERO) < 0) {//Menor a cero
                            estado = "Verde";
                            col6RowStyleClass = "estadoVerde";
                        } else if (variacion.compareTo(to.getFinToleranciaVerde()) < 0) {//Menor
                            estado = "Verde";
                            col6RowStyleClass = "estadoVerde";
                        } else if (variacion.compareTo(to.getFinToleranciaVerde()) == 0) {//Igual
                            estado = "Amarillo";
                            col6RowStyleClass = "estadoAmarillo";
                        } else if (variacion.compareTo(to.getFinToleranciaVerde()) == 1
                                && (variacion.compareTo(to.getFinToleranciaAmarillo()) == -1
                                || variacion.compareTo(to.getFinToleranciaAmarillo()) == -0)) {//Mayor a verde y menor o igual a amarillo
                            estado = "Amarillo";
                            col6RowStyleClass = "estadoAmarillo";
                        } else {
                            estado = "Rojo";
                            col6RowStyleClass = "Rojo";
                        }
                    }
                }
                //--- Fin color de estado ---

                HashMap fila = new HashMap();
                fila.put("col1", to.getPrograma());
                fila.put("col2", to.getProyecto());
                fila.put("col3", to.getIndicador());
                fila.put("col4", ReportesUtils.getNumber(to.getMeta()));
                fila.put("col5", ReportesUtils.getNumber(to.getActual()));
                fila.put("col6", estado);
                fila.put("col6RowStyleClass", col6RowStyleClass);//Agregado para colores de columna 6
                completarFilaLinea(fila, 6);
                respuesta.add(fila);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERROR_GENERAL);
            throw b;
        }
        return respuesta;
    }

    private boolean estaLinea(Set<LineaEstrategica> lineas, Integer lineaId) {
        for (LineaEstrategica linea : lineas) {
            if (linea.getId().equals(lineaId)) {
                return true;
            }
        }
        return false;
    }

    private void completarFilaLinea(HashMap fila, int cantidadColumnas) {
        if (fila == null) {
            return;
        }
        for (int i = 1; i <= cantidadColumnas; i++) {
            if (fila.get("col" + i) == null) {
                fila.put("col" + i, "");
            }
        }
    }

    private void completarFilaSeguimiento(HashMap fila, int cantidadColumnas) {
        if (fila == null) {
            return;
        }
        for (int i = 1; i <= cantidadColumnas; i++) {
            if (fila.get("col" + i) == null) {
                fila.put("col" + i, "");
            }
        }
        if (fila.get("colAlcance") == null) {
            fila.put("colAlcance", "");
        }
        if (fila.get("colMedio") == null) {
            fila.put("colMedio", "");
        }
    }
}
