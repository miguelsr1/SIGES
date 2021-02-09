/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.business.utils.CalcularSugerenciasMonto;
import gob.mined.siap2.business.utils.POAUtils;
import gob.mined.siap2.business.utils.ProyectoUtils;
import gob.mined.siap2.business.utils.TipoSeguimientoUtils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.Categoria;
import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import gob.mined.siap2.entities.data.impl.ComboValorSeguimiento;
import gob.mined.siap2.entities.data.impl.FlujoCajaAnio;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.Indicador;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POActividadProyecto;
import gob.mined.siap2.entities.data.impl.POFlujoCajaMenusal;
import gob.mined.siap2.entities.data.impl.POIndicadorLinea;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.ProgramaInstitucional;
import gob.mined.siap2.entities.data.impl.ProgramaPresupuestario;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoAporte;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTramoTramo;
import gob.mined.siap2.entities.data.impl.ProyectoComponente;
import gob.mined.siap2.entities.data.impl.ProyectoEstPorcentajeFuente;
import gob.mined.siap2.entities.data.impl.ProyectoEstProducto;
import gob.mined.siap2.entities.data.impl.ProyectoEstructura;
import gob.mined.siap2.entities.data.impl.ProyectoMacroActividad;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.EstadoActividadPOA;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.entities.enums.TipoAporteProyecto;
import gob.mined.siap2.entities.enums.TipoEstructura;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.entities.enums.TipoSeguimiento;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.persistence.dao.reference.EntityReference;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.datatypes.DataPOTablaLinea;
import gob.mined.siap2.web.delegates.POAProyectoDelegate;
import gob.mined.siap2.web.mb.UtilsMB;
import static gob.mined.siap2.web.mb.impl.po.POConActividadesEInsumosAbstract.logger;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DualListModel;
import org.primefaces.model.TreeNode;



/**
 * Este backing bean implementa los eventos y lógica de presentación  el manejo básico de un PO con lineas
 * 
 * @author Sofis Solutions
 */
public abstract class POProyectoConLineasAbstract extends POConActividadesEInsumosAbstract implements Serializable {

    protected Proyecto objeto;
    protected boolean verTodosLosMontos = false;

    @Inject
    POAProyectoDelegate pOAProyectoDelegate;

    /**
     * TIPO DE POA ES DE PROYECTO
     *
     * @return
     */
    @Override
    public String getTipoPO() {
        return POConActividadesEInsumosAbstract.TIPO_PO_PROYECTO;
    }

    /**
     * retorna los años de duración del PO supone que es POG entonces retorna
     * todos los años del proyecto si fuera POA tendría que retornar solo el año
     * actual
     *
     * @return
     */
    @Override
    public List<Integer> getListAniosPO() {
        List<Integer> l = new LinkedList();
        if (objeto != null) {
            int anio = DatesUtils.getYearOfDate(objeto.getInicio());
            int anioFin = DatesUtils.getYearOfDate(objeto.getFin());
            while (anio <= anioFin) {
                l.add(anio);
                anio++;
            }
        }
        return l;
    }

    /**
     * Este método retorna las lineas disponibles
     * @return 
     */
    public List<POLinea> getLineas() {
        return null;
    }

    /**
     * Este método añade una linea nueva
     * 
     * @deprecated 
     * @param tempLinea 
     */
    public void addTmpLinea(POLinea tempLinea) {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    /**
     * Este método elimina una linea 
     * 
     * @deprecated 
     * @param tempLinea 
     */
    public void eliminarTmpLinea(POLinea tempLinea) {
        throw new UnsupportedOperationException("metodo no sobre-escrito.");
    }

    ////////////////////////////////////////////////////////////////////////////
    ///// EMPIEZA LOGICA
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Este método retorna los tipos de valores de seguimiento.
     * 
     * @return 
     */
    public List<String> getNombresDeTipoSeguimiento() {
        if (producto != null) {
            return TipoSeguimientoUtils.getListName(producto.getProducto().getTipo().getTipoSeguimiento());
        }
        return Collections.EMPTY_LIST;
    }
    
    /**
     * Este método suma el total usado por los valores de seguimiento
     */
    protected List<ComboValorSeguimiento> valoresSeguimiento;

    /**
     * Este método retorna el total usado por el seguimiento.
     * 
     * @param index
     * @return 
     */
    public BigDecimal sumarTotalValorSeguimiento(Integer index) {
        BigDecimal total = BigDecimal.ZERO;
        if (valoresSeguimiento != null) {
            for (ComboValorSeguimiento valorSeg : valoresSeguimiento) {
                if (index < valorSeg.getValores().size() && valorSeg.getValores().get(index).getValor() != null) {
                    total = total.add(valorSeg.getValores().get(index).getValor());
                }
            }
        }
        return total;
    }

    protected Integer anioValor = 0;

    
    /**
     * Este método retorna el total habilitado para el valor de seguimiento
     * @return 
     */
    public boolean habilitadoValorSeguimiento() {
        return (producto != null);
    }

    /**
     * abanza al siguiente año de seuigmiento
     */
    public void avanzarAnioSeguimiento() {
        anioValor = anioValor + 1;
    }

    /**
     * Este método marca si es el último año de seguimiento
     * @return 
     */
    // esto se usa en emodal para agregar indicador, pero se tendria que eliminar
    public boolean isUltimoAnioSeguimiento() {
        if (valoresSeguimiento != null && anioValor != null) {
            int anoSiguiente = anioValor + 1;
            return (valoresSeguimiento.size() <= anoSiguiente);
        } else {
            return false;
        }
    }

    //se usa para anadir una línea de pog con componete, macroactividad, producto y montos    
    // <editor-fold defaultstate="collapsed" desc="AÑADIR LÍNEA">
    protected POLinea tempLinea;
    protected String idProducto;
    protected Integer modalAbierto;
    protected DualListModel<UnidadTecnica> utColaboradoras;

    /**
     * Este método es utilizado para inicializar una nueva linea
     */
    public void initLinea() {
        valoresSeguimiento = null;
        anioValor = 0;
        modalAbierto = 1;

        producto = tempLinea.getProducto();
        idProducto = producto.getId().toString();

        valoresSeguimiento = tempLinea.getValoresProducto();

        List<UnidadTecnica> todas = emd.getEntities(UnidadTecnica.class.getName(), "nombre");
        for (UnidadTecnica u : tempLinea.getColaboradoras()) {
            todas.remove(u);
        }
        utColaboradoras = new DualListModel<UnidadTecnica>(todas, tempLinea.getColaboradoras());
    }

    ProyectoEstProducto producto = null;


    /**
     * Este método guarda la linea en edición
     */
    public void saveLinea() {
        try {

            tempLinea.setColaboradoras(utColaboradoras.getTarget());
            tempLinea.setValoresProducto(valoresSeguimiento);

            guardar();
            RequestContext.getCurrentInstance().execute("$('#anadirLinea').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método es utilizado para eliminar una linea
     */
    public void eliminarLinea() {
        try {
            //fixeme esto se tiene que ir
//            if (!tempLinea.getActividades().isEmpty()) {
//                BusinessException b = new BusinessException();
//                b.addError(ConstantesErrores.ERR_EXISTEN_ACTIVIDADES_ASOCIADAS_A_LA_LINEA);
//                throw b;
//            }
//            if (!tempLinea.getIndicadores().isEmpty()) {
//                BusinessException b = new BusinessException();
//                b.addError(ConstantesErrores.ERR_EXISTEN_INDICADORES_ASOCIADOS_A_LA_LINEA);
//                throw b;
//            }
//
//            //objeto.getPog().getLineas().remove(tempLinea);
//            eliminarTmpLinea(tempLinea);
//            //se vuelve a guardar el proyecto
//            guardar();
//
//            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // </editor-fold>
    private POIndicadorLinea tempLineaIndicador;
    private Indicador indicadorEnUso;

    // <editor-fold defaultstate="collapsed" desc="AÑADIR INDICADOR ESTRATEGICO">
    private Integer pasoModalIndicador = 1;
    private String idProgramaIndicador;
    protected boolean indicadorEstrategico = true;

    /**
     * Este método inicializa un indicador para la linea
     */
    public void initLineaPogParaIndicador() {
        initLinea();
        pasoModalIndicador = 1;
    }

    /**
     * Este método inicializa el indicador
     */
    public void initLinieaIndicador() {
        indicadorEstrategico = true;
        anioValor = 0;
        if (tempLineaIndicador == null) {
            //tempLineaIndicador = new POIndicadorLinea();
            idProgramaIndicador = null;
            valoresSeguimiento = null;
            indicadorEnUso = null;
        } else {
            valoresSeguimiento = tempLineaIndicador.getComboValoresSeguimiento();
            indicadorEnUso = tempLineaIndicador.getIndicador();
            //idIndicador = tempLineaIndicador.getIndicador().getId().toString();
        }

        pasoModalIndicador = 2;
    }

    // tipo indicador ya no tendria mas sentido
    private String tipoIndicador = null;
    private static final String INDICADOR_PI = "1";
    private static final String INDICADOR_PP = "2";
    private static final String INDICADOR_TODOS = "3";

    public Map<String, String> getIndicadoresEnPrograma() {
        Map<String, String> map = new LinkedHashMap();
//        for (ProyectoIndicador pi : objeto.getIndicadores()) {
//            if (!map.containsKey(pi.getIndicador().getNombre())) {
//                map.put(pi.getIndicador().getNombre(), pi.getId().toString());
//            }
//        }
        return map;
    }

    /**
     * Este método guarda el indicador estratégico
     */
    public void guardarIndicadorEstrategico() {
        if (tempLineaIndicador == null) {
            tempLineaIndicador = new POIndicadorLinea();
            tempLineaIndicador.setIndicador(indicadorEnUso);
            tempLineaIndicador.setComboValoresSeguimiento(valoresSeguimiento);
            tempLineaIndicador.setLineaPOProyecto(tempLinea);
            tempLinea.getIndicadores().add(tempLineaIndicador);
        }
    }

    public void irAPaso1() {
        pasoModalIndicador = 1;
    }

    public void eliminarIndicadorLinea(Integer index) {
        tempLinea.getIndicadores().remove(index.intValue());

    }

    // </editor-fold>
    /**
     * Este método guarda indicador
     */
    public void guardarIndicador() {
        try {
            if (indicadorEstrategico) {
                guardarIndicadorEstrategico();
            } else {
                guardarNuevoIndicador();
            }

            //se vuelve a guardar el proyecto
            guardar();

            pasoModalIndicador = 1;
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    public void chequearIndicadorEnLinea(POIndicadorLinea liToComparar) {
        for (POIndicadorLinea li : tempLinea.getIndicadores()) {
            if (li != liToComparar
                && li.getIndicador().getId().equals(liToComparar.getIndicador().getId())) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_YA_EXISTE_INDICADOR_EN_CON_SEGUIMIENTO_ASOCIADO);
                throw b;
            }
        }
    }

    public BigDecimal caclularSugerencia(POMontoFuenteInsumo monto, BigDecimal montoTotal) {
        return CalcularSugerenciasMonto.caclularSugerenciaPOG(this.objeto, monto, montoTotal);
    }

    // <editor-fold defaultstate="collapsed" desc="AÑADIR NUEVO INDICADOR">
    private String idIndicador;
    private String idTipoIndicador;
    private TipoSeguimiento tipoSeguimientoIndicador;

    public void initNuevoIndicador() {
        indicadorEstrategico = false;
        idProgramaIndicador = null;
        idTipoIndicador = null;
        valoresSeguimiento = null;
        indicadorEnUso = null;
        idIndicador = null;

    }

    public void setIndicador() {
        if (!TextUtils.isEmpty(idIndicador)) {
            Indicador i = (Indicador) emd.getEntityById(Indicador.class.getName(), Integer.valueOf(idIndicador));  
            indicadorEnUso = i;
        }
    }

    public void actualizarTioSeguimiento() {
        if (tipoSeguimientoIndicador != null) {
            valoresSeguimiento = TipoSeguimientoUtils.generateValoresDelProducto(tipoSeguimientoIndicador, getListAniosPO());
        }
    }

    public void guardarNuevoIndicador() {
        tempLineaIndicador = new POIndicadorLinea();
        tempLineaIndicador.setIndicador(indicadorEnUso);
        tempLineaIndicador.setComboValoresSeguimiento(valoresSeguimiento);
        tempLineaIndicador.setLineaPOProyecto(tempLinea);
        tempLinea.getIndicadores().add(tempLineaIndicador);
    }

    public TipoSeguimiento[] getTipoSeguimiento() {
        return TipoSeguimiento.values();
    }

    public Map getIndicadores() {
        Map map = new LinkedHashMap();

        CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estado", EstadoComun.VIGENTE);

        if (!TextUtils.isEmpty(idTipoIndicador)) {
            MatchCriteriaTO mismoTipo = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "tipo.id", Integer.parseInt(idTipoIndicador));
            criterio = CriteriaTOUtils.createANDTO(mismoTipo, criterio);
        }

        String[] orderBy = {"nombre"};
        boolean[] ascending = {true};
        String className = Indicador.class.getName();

        String[] propiedades = {"id", "nombre"};
        List<EntityReference<Integer>> ll = emd.getEntitiesReferenceByCriteria(className, criterio, null, null, propiedades, orderBy, ascending);
        for (EntityReference l : ll) {
            map.put((String) l.getPropertyMap().get("nombre"), String.valueOf(l.getPropertyMap().get("id")));
        }
        return map;
    }

    public Map getTiposIndicadores() {
        Map categorias = new LinkedHashMap();
        List<Categoria> ll = emd.getEntities(Categoria.class.getName(), "nombre");
        for (Categoria p : ll) {
            categorias.put(p.getNombre(), String.valueOf(p.getId()));
        }
        return categorias;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="get Componentes y macroActividades">
    public Map<String, Integer> getComponentesProyecto() {
        Map<String, Integer> map = new LinkedHashMap();
        if (objeto != null) {
            for (ProyectoComponente componente : objeto.getProyectoComponentes()) {
                if (componente.getComponentePadre() == null) {
                    addComponentesRecursivo(componente, map, 0, false);
                }
            }
        }
        return map;
    }

    protected void addComponentesRecursivo(ProyectoComponente componente, Map<String, Integer> map, int nivel, boolean tienePadreUT) {

        if (tienePadreUT || (componente.getUnidadTecnica() != null && componente.getUnidadTecnica().getId().equals(Integer.valueOf(idUnidadTecnica)))) {
            tienePadreUT = true;
            String padding = "";
            for (int i = 0; i < nivel; i++) {
                padding = padding + "&nbsp;";
            }
            map.put(padding + componente.getNombre(), componente.getId());
        }

        for (ProyectoComponente hijo : componente.getComponenteHijos()) {
            addComponentesRecursivo(hijo, map, nivel + 1, tienePadreUT);
        }

    }

    public Map<String, Integer> getMacroActividadesProyecto() {
        Map<String, Integer> map = new LinkedHashMap();
        if (objeto != null) {
            for (ProyectoMacroActividad macroActividad : objeto.getProyectoMacroactividad()) {
                if (macroActividad.getUnidadTecnica() != null && macroActividad.getUnidadTecnica().getId().equals(Integer.valueOf(idUnidadTecnica))) {
                    map.put(macroActividad.getMacroActividad().getNombre(), macroActividad.getId());
                }
            }
        }
        return map;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="AÑADIR ACTIVIDAD">
    
    /**
     * Este método inicializa una nueva actividad
     */
    @Override
    public void initActividad() {
        //initLinea();
        if (tempActividad == null) {
            tempActividad = new POActividadProyecto();
            tempActividad.setEstado(EstadoActividadPOA.NO_FINALIZADA);
            tempActividad.setInsumos(new LinkedList());
        }
        super.initActividad();
    }

    /**
     * Este método retorna la actividad en edición
     * @return 
     */
    @Override
    public POActividadProyecto getTempActividad() {
        return (POActividadProyecto) tempActividad;
    }

    /**
     * Este método guarda la actividad en edición
     * 
     * @param actividad 
     */
    @Override
    public void guardarActividad(POActividadBase actividad) {
        if (!tempLinea.getActividades().contains(actividad)) {
            //actividad.setLineaPOProyecto(tempLinea);
            tempLinea.getActividades().add((POActividadProyecto) actividad);
        }
    }

    /**
     * Este método elimina una actividad
     * 
     * @param actividad 
     */
    @Override
    public void eliminarActividad(POActividadBase actividad) {
        tempLinea.getActividades().remove(actividad);
    }

    /**
     * Este método debería devolver los usuarios disponibles para esta actividad
     * @return 
     */
    @Override
    public Map<String, String> getUsuariosForActividad() {
        return null;
    }

    
   

    // </editor-fold>
    
    
    // <editor-fold defaultstate="collapsed" desc="AÑADIR INSMUMO">
    /**
     * Este método es el encargado de inicializar un insumo 
     */
    @Override
    public void initInsumo() {
        super.initInsumo();

        //se sobrecargan las fuentes
        //solo toma las fuentes cargadas en el componente
        for (ProyectoEstPorcentajeFuente f : tempLinea.getProducto().getProyectoEstructura().getMontosFuentes()) {
            //if (!ProyectoUtils.aporteVacio(f)){
            if (!ProyectoUtils.aporteEnEstructuraVacio(f, false)) {
                POInsumos tempInsumo = getInsumoEnUso();
                if (ProyectoUtils.getInsumoMontoFuente(f.getFuente(), tempInsumo.getMontosFuentes()) == null) {
                    POMontoFuenteInsumo monto = new POMontoFuenteInsumo();
                    monto.setFuente(f.getFuente());
                    monto.setInsumo(tempInsumo);
                    tempInsumo.getMontosFuentes().add(monto);
                }
            }
        }
        
        cantidadMontosFuentesDePoInsumo = 0;
        if(tempInsumo.getMontosFuentes() != null){
            cantidadMontosFuentesDePoInsumo = tempInsumo.getMontosFuentes().size();
        }
       
    }

    /**
     * Este método retorna el monto del aporte en un insumo
     * 
     * @param insumo
     * @param aporte
     * @return 
     */
    public POMontoFuenteInsumo getAporteEnInsumo(POInsumos insumo, ProyectoAporte aporte) {
        for (POMontoFuenteInsumo montoEnInsumo : insumo.getMontosFuentes()) {
            if (montoEnInsumo.getFuente().equals(aporte)) {
                return montoEnInsumo;
            }
        }
        return null;
    }

    
    /**
     * Este método guarda un insumo
     * 
     * @param insumo 
     */
    @Override
    public void guardarInsumo(POInsumos insumo) {
        //se preserva el monto total introducido a mano
        if (insumo.getMontosFuentes() != null && !insumo.getMontosFuentes().isEmpty()) {
            BigDecimal montoTotal = BigDecimal.ZERO;
            for (POMontoFuenteInsumo mf : insumo.getMontosFuentes()) {
                montoTotal = montoTotal.add(mf.getMonto());
            }
            if (montoTotal.compareTo(insumo.getMontoTotal()) != 0) {
                //la distribucion por fuente no coincide cone l monto total del insumo
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_FUENTES_NO_MONTO_INSUMO);
                throw b;
            }
        }
        if (insumo.getActividad() == null) {
            insumo.setActividad(getActividadEnUso());
            getActividadEnUso().getInsumos().add(insumo);
        }
    }

    /**
     * Este método es utilizado para eliminar un insumo
     * @param insumo 
     */
    @Override
    public void eliminarInsumo(POInsumos insumo) {
        insumo.getActividad().getInsumos().remove(insumo);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="VALIDAR MONTO INSMUMO">
    /**
     * @deprecated 
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException 
     */
    @Override
    public void validateMontoInsumoPorFuente(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//fixme: comentado por cambio en producto

//        //POInsumos tempInsumo= getInsumoEnUso();
//        BigDecimal montoIngresado = new BigDecimal(value.toString());
//        Integer idFuente = (Integer) component.getAttributes().get("idfuente");
//
//        ProyectoComponente componente = null;
//        ProyectoMacroActividad macroActividad = (ProyectoMacroActividad) tempLinea.getEstructura();
//        if (tempLinea.getEstructura().getTipo() == TipoEstructura.COMPONENTE) {
//            componente = (ProyectoComponente) tempLinea.getEstructura();
//        } else {
//            macroActividad = (ProyectoMacroActividad) tempLinea.getEstructura();
//        }
//        //LineaPOGProyecto linea = tempActividad.getLineaPOProyecto();
//
//        //valida los montos del comopnente
//        if (componente != null) {
//            //vuelve a buscar el componente por problemas de lazy           
//            ProyectoEstPorcentajeFuente montoFuente = ProyectoUtils.getComponenteMontoFuente(idFuente, componente.getMontosFuentes());
//
//            BigDecimal montoUsado = BigDecimal.ZERO;
//            for (POLinea linea : getLineas()) {
//                //si la linea es del mismo componente
//                if (linea.getEstructura() != null && linea.getEstructura().getId().equals(componente.getId())) {
//                    for (POActividadBase a : linea.getActividades()) {
//                        for (POInsumos i : a.getInsumos()) {
//                            //si soy yo el monto lo sumo despues
//                            if (tempInsumo != i) {
//                                for (POMontoFuenteInsumo mi : i.getMontosFuentes()) {
//                                    if (mi.getFuente().getId().equals(idFuente)) {
//                                        montoUsado = montoUsado.add(mi.getMonto());
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            //suma el monto del insumo ingresado
//            montoUsado = montoUsado.add(montoIngresado);
//            //compara que no sobrepase
//            if (montoUsado.compareTo(montoFuente.getMonto()) > 0) {
//                FacesMessage msg = new FacesMessage("Error al validar montos por componentes", "Error: la suma de los montos es mayor que el monto del componente para dicha Fuente");
//                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//                throw new ValidatorException(msg);
//            }
//
//        }
//
//        //valida los montos de la macroactividad
//        if (macroActividad != null) {
//            //vuelve a buscar el componente por problemas de lazy           
//            ProyectoEstPorcentajeFuente montoFuente = ProyectoUtils.getComponenteMontoMacroActividad(idFuente, macroActividad.getMontosFuentes());
//
//            BigDecimal montoUsado = BigDecimal.ZERO;
//            for (POLinea linea : getLineas()) {
//                //si la linea es del mismo componente
//                if (linea.getEstructura() != null && linea.getEstructura().getId().equals(macroActividad.getId())) {
//                    for (POActividadBase a : linea.getActividades()) {
//                        for (POInsumos i : a.getInsumos()) {
//                            //si soy yo el monto lo sumo despues
//                            if (tempInsumo != i) {
//                                for (POMontoFuenteInsumo mi : i.getMontosFuentes()) {
//                                    if (mi.getFuente().getId().equals(idFuente)) {
//                                        montoUsado = montoUsado.add(mi.getMonto());
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            //suma el monto del insumo ingresado
//            montoUsado = montoUsado.add(montoIngresado);
//            //compara que no sobrepase
//            if (montoUsado.compareTo(montoFuente.getMonto()) > 0) {
//                FacesMessage msg = new FacesMessage("Error al validar montos por MacroActividad", "Error: la suma de los montos es mayor que el monto de la MacroActividad para dicha Fuente");
//                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//                throw new ValidatorException(msg);
//            }
//
//        }
    }

    /**
     * 
     * @deprecated 
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException 
     */
    @Override
    public void validateMontoInsumoTotal(FacesContext context, UIComponent component, Object value) throws ValidatorException {
//        FacesMessage msg = new FacesMessage("Error al validar montos", "Error: No existen fuentes con monto");
//        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
//        throw new ValidatorException(msg);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SELECCION DE TRAMOS EN CATEGORIA">
    /**
     * Este método que se llama cada vez que el usuario cambia el tramo seleccionado
     * en el combo
     *
     * @param idTramo
     */
    public void cambiarTramo(String idTramo) {
        if (TextUtils.isEmpty(idTramo)) {
            tempInsumo.setTramo(null);
        } else {
            ProyectoAporteTramoTramo tramo = pOAProyectoDelegate.getProyectoAporteTramoTramo(Integer.valueOf(idTramo));
            tempInsumo.setTramo(tramo);
        }
    }

    /**
     * Este método que retorna los tramos disponibles
     *
     * @return
     */
    public Map getTramosDisponibles() {
        Map<String, String> res = new LinkedHashMap();

        CategoriaConvenio cat = ProyectoUtils.getCategoriaConvenio(tempLinea.getProducto().getProyectoEstructura(), false);
        if (cat == null || cat.getTipo() != TipoAporteProyecto.POR_TRAMOS) {
            return res;
        }

        List<ProyectoAporteTramoTramo> tramos = pOAProyectoDelegate.getTramos(objeto.getId(), cat);
        for (ProyectoAporteTramoTramo tramo : tramos) {
            String monto = UtilsMB.nomberToString(tramo.getMontoHasta());
            res.put(monto, tramo.getId().toString());
        }

        return res;
    }

    /**
     * Este método que retorna si la categoría del insumo seleccionada es por tramos
     *
     * @return
     */
    public boolean isnumoRequiereTramos() {
        if (tempInsumo == null) {
            return false;
        }
        CategoriaConvenio cat = ProyectoUtils.getCategoriaConvenio(tempLinea.getProducto().getProyectoEstructura(), false);
        if (cat == null || cat.getTipo() != TipoAporteProyecto.POR_TRAMOS) {
            return false;
        }

        return true;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="GET MONTOS USADOS">
    /**
     * Este método retorna el monto usado por componente
     * 
     * @param idComponente
     * @param idFuente
     * @return 
     */
    public BigDecimal getMontoUsadoComponente(Integer idComponente, Integer idFuente) {
        BigDecimal usado = BigDecimal.ZERO;
        for (POLinea l : getLineas()) {
            if (l.getProducto().getProyectoEstructura().getId().equals(idComponente)) {
                for (POActividadBase a : l.getActividades()) {
                    for (POInsumos i : a.getInsumos()) {
                        for (POMontoFuenteInsumo m : i.getMontosFuentes()) {
                            if (m.getFuente().getId().equals(idFuente)) {
                                usado = usado.add(m.getMonto());
                            }
                        }
                    }
                }
            }
        }
        return usado;
    }

    /**
     * Este método retorna el monto usado en una macroactividad
     * 
     * @param idMacroactividad
     * @param idFuente
     * @return 
     */
    public BigDecimal getMontoUsadoMacroAcividad(Integer idMacroactividad, Integer idFuente) {
        BigDecimal usado = BigDecimal.ZERO;
        for (POLinea l : getLineas()) {
            if (l.getProducto().getProyectoEstructura().getId().equals(idMacroactividad)) {
                for (POActividadBase a : l.getActividades()) {
                    for (POInsumos i : a.getInsumos()) {
                        for (POMontoFuenteInsumo m : i.getMontosFuentes()) {
                            if (m.getFuente().getId().equals(idFuente)) {
                                usado = usado.add(m.getMonto());
                            }
                        }
                    }
                }
            }
        }
        return usado;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="GET MONTOS COMPONENTES">
    private boolean equalsIdUt(UnidadTecnica ut) {
        if (verTodosLosMontos) {
            return true;
        }
        if (ut == null) {
            return false;
        }
        if (TextUtils.isEmpty(idUnidadTecnica)) {
            return false;
        }
        return Integer.valueOf(idUnidadTecnica).equals(ut.getId());
    }

    /**
     * Este método retorna la estructura de árbol de los montos de los componentes
     * @return 
     */
    public TreeNode getRootNodeComponente() {
        List<ProyectoComponente> componentes = new LinkedList();

        for (POLinea linea : getLineas()) {
            ProyectoEstructura est = linea.getProducto().getProyectoEstructura();
            if (est.getTipo() == TipoEstructura.COMPONENTE && !componentes.contains(est)) {
                componentes.add((ProyectoComponente) est);
            }
        }

        TreeNode root = new DefaultTreeNode();
        root.setExpanded(true);
        for (ProyectoComponente componente : componentes) {
            if (componente.getComponentePadre() == null) {
                root.getChildren().add(conectToTreeNodeComponente(componente, root));
            } else {
                TreeNode nodoPAdre = new DefaultTreeNode(componente.getComponentePadre(), root);
                nodoPAdre.setExpanded(true);
                nodoPAdre.getChildren().add(conectToTreeNodeComponente(componente, nodoPAdre));
            }
        }
        return root;
    }

    /**
     * Este es un método auxiliar que  se usa para armar la estructura de árbol
     * 
     * @param componente
     * @param root
     * @return 
     */
    public TreeNode conectToTreeNodeComponente(ProyectoComponente componente, TreeNode root) {
        TreeNode actualNode = new DefaultTreeNode(componente, root);
        actualNode.setExpanded(true);
        for (ProyectoComponente hijo : componente.getComponenteHijos()) {
            conectToTreeNodeComponente(hijo, actualNode);
        }
        return actualNode;
    }

    //retorna las macroactividades para los montos
    public List<ProyectoMacroActividad> getMacroActividades() {
        List<ProyectoMacroActividad> l = new LinkedList();
        //cambiado por cambio de estructura en  el pog, chequear si el get lineas en poa da las lineas que corresponden al producto
        //por lo que no va a servir para poa esta funcion, ya que muchos posas comparten los mismos componentes (hay que chequear en base los saldos)
        for (POLinea linea : getLineas()) {
            if (linea.getProducto().getProyectoEstructura().getTipo() == TipoEstructura.MACROACTIVIDAD) {
                l.add((ProyectoMacroActividad) linea.getProducto().getProyectoEstructura());
            }
        }

        return l;
    }

    /**
     * Este método retorna el monto del componente para una fuente
     * 
     * @param c
     * @param f
     * @return 
     */
    public BigDecimal getMontoComponente(ProyectoComponente c, ProyectoAporte f) {
        if (c == null || f == null) {
            return BigDecimal.ZERO;
        }
        ProyectoEstPorcentajeFuente m = ProyectoUtils.getMontoAporteFromEstructura(c, f);
        if (m == null) {
            return BigDecimal.ZERO;
        }
        return (m.getMonto());
    }

    /**
     * Este método retorna el monto de una macroactividad para una fuente
     * @param ma
     * @param f
     * @return 
     */
    public BigDecimal getMontoMacroactividad(ProyectoMacroActividad ma, ProyectoAporte f) {
        if (ma == null || f == null) {
            return BigDecimal.ZERO;
        }
        ProyectoEstPorcentajeFuente m = ProyectoUtils.getMontoAporteFromEstructura(ma, f);
        if (m == null) {
            return BigDecimal.ZERO;
        }
        return (m.getMonto());
    }

    // </editor-fold>
    
    
    
    
    
    
    /**
     * Este método crea una tabla genérica a partir de cualquier POA
     * 
     * @deprecated 
     * @param poa
     * @return 
     */
    public List<DataPOTablaLinea>  transformarPOAEnTabla(GeneralPOA poa) {
        List<DataPOTablaLinea> res = new LinkedList<>();
        if (poa == null){
            return res;
        }
        
        
        if (poa.getTipo() == TipoPOA.POA_PROYECTO) {
            Integer countLinea = 0;
            POAProyecto poaProyecto = (POAProyecto) poa;
            for (POLinea linea : poaProyecto.getLineas()) {
                DataPOTablaLinea dataLinea = convertLinea(linea, countLinea);

                dataLinea.setPoa(poa);
                dataLinea.setPoLinea(linea);
                res.add(dataLinea);
                for (POActividadBase actividad : linea.getActividades()) {
                    DataPOTablaLinea dataActividad = convertActividad(actividad);

                    dataActividad.setPoa(poa);
                    dataActividad.setPoLinea(linea);
                    dataActividad.setPoActividad(actividad);
                    res.add(dataActividad);

                    for (POInsumos insumo : actividad.getInsumos()) {
                        DataPOTablaLinea dataInsumo = convertInsumo(insumo);
                        
                        if (insumo.getMontoTotal() != null) {
                            dataActividad.setMontoTotal(dataActividad.getMontoTotal().add(insumo.getMontoTotal()));
                            dataLinea.setMontoTotal(dataLinea.getMontoTotal().add(insumo.getMontoTotal()));
                        }

                        dataInsumo.setPoa(poa);
                        dataInsumo.setPoLinea(linea);
                        dataInsumo.setPoActividad(actividad);
                        dataInsumo.setPoIsumo(insumo);
                        res.add(dataInsumo);
                    }
                }

                countLinea++;
            }

        } else if (poa.getTipo() == TipoPOA.POA_CON_ACTIVIDADES) {
            POAConActividades poaConActividades = (POAConActividades) poa;
            for (POActividadBase actividad : poaConActividades.getActividades()) {
                DataPOTablaLinea dataActividad = convertActividad(actividad);

                dataActividad.setPoa(poa);
                dataActividad.setPoActividad(actividad);
                res.add(dataActividad);

                for (POInsumos insumo : actividad.getInsumos()) {
                    DataPOTablaLinea dataInsumo = convertInsumo(insumo);
                    
                    if (insumo.getMontoTotal() != null) {
                        dataActividad.setMontoTotal(dataActividad.getMontoTotal().add(insumo.getMontoTotal()));
                    }

                    dataInsumo.setPoa(poa);
                    dataInsumo.setPoActividad(actividad);
                    dataInsumo.setPoIsumo(insumo);
                    res.add(dataInsumo);
                }
            }

        }
        return res;
    }

    
    private DataPOTablaLinea convertLinea(POLinea linea, Integer countLinea) {
        DataPOTablaLinea dataLinea = new DataPOTablaLinea();
        dataLinea.setTipo(DataPOTablaLinea.TIPO_LINEA);
        dataLinea.setNumeroLinea(countLinea);
        dataLinea.setProducto(linea.getProducto().getProducto().getNombre());
        dataLinea.setComponenteMacroActividad(ProyectoUtils.getNombreEstructura(linea.getProducto().getProyectoEstructura()));
        dataLinea.setMontoTotal(BigDecimal.ZERO);
        return dataLinea;
    }

    private DataPOTablaLinea convertActividad(POActividadBase actividad) {
        DataPOTablaLinea dataActividad = new DataPOTablaLinea();
        dataActividad.setTipo(DataPOTablaLinea.TIPO_ACTIVIDAD);
        dataActividad.setActividad(POAUtils.getNombreActividad(actividad));
        dataActividad.setMontoTotal(BigDecimal.ZERO);
        return dataActividad;
    }

    private DataPOTablaLinea convertInsumo(POInsumos insumo) {
        DataPOTablaLinea dataInsumo = new DataPOTablaLinea();
        dataInsumo.setTipo(DataPOTablaLinea.TIPO_INSUMO);
        if (insumo.getInsumo() != null) {
            dataInsumo.setInsumo(insumo.getId() + " " + insumo.getInsumo().getNombre());
        }
        dataInsumo.setMontoTotal(insumo.getMontoTotal());
        return dataInsumo;
    }

    // <editor-fold defaultstate="collapsed" desc="getter caluclados">
    public TreeNode getRootNodeProgramaInstitucional() {
        TreeNode root = new DefaultTreeNode();
        root.setExpanded(false);
        if (objeto != null && objeto.getProgramaInstitucional() != null) {
            conectToTreeNodePI(objeto.getProgramaInstitucional(), root, false);
        }
        return root;
    }

    public TreeNode conectToTreeNodePI(ProgramaInstitucional p, TreeNode root, boolean expanded) {
        TreeNode actualNode = new DefaultTreeNode(p, root);
        actualNode.setExpanded(expanded);
        for (ProgramaInstitucional hijo : p.getProgramasInstitucionales()) {
            conectToTreeNodePI(hijo, actualNode, true);
        }
        return actualNode;
    }

    public TreeNode getRootNodeProgramaPresupuestario() {
        TreeNode root = new DefaultTreeNode();
        root.setExpanded(true);
        if (objeto != null && objeto.getProgramaPresupuestario() != null) {
            conectToTreeNodePP(objeto.getProgramaPresupuestario(), root, false);
        }
        return root;
    }

    public TreeNode conectToTreeNodePP(ProgramaPresupuestario p, TreeNode root, boolean expanded) {
        TreeNode actualNode = new DefaultTreeNode(p, root);
        actualNode.setExpanded(expanded);
        for (ProgramaPresupuestario hijo : p.getProgramasPresupuestarios()) {
            conectToTreeNodePP(hijo, actualNode, true);
        }
        return actualNode;
    }

    public Map<String, String> getProductos() {
        //solo lista los productos del componente o la macroacctividad
        Map<String, String> map = new LinkedHashMap();
        if (objeto != null && !TextUtils.isEmpty(idUnidadTecnica)) {
            Integer idUT = Integer.valueOf(idUnidadTecnica);
            for (ProyectoMacroActividad iter : objeto.getProyectoMacroactividad()) {
                for (ProyectoEstProducto iterProd : iter.getProductos()) {
                    if (idUT.equals(iterProd.getUnidadTecnica().getId())) {
                        map.put(iterProd.getProducto().getNombre(), iterProd.getId().toString());
                    }
                }
            }
            for (ProyectoComponente iter : objeto.getProyectoComponentes()) {
                for (ProyectoEstProducto iterProd : iter.getProductos()) {
                    if (idUT.equals(iterProd.getUnidadTecnica().getId())) {
                        map.put(iterProd.getProducto().getNombre(), iterProd.getId().toString());
                    }
                }
            }
        }
        return map;
    }

    public Map<String, String> getProyectos() {
        Map<String, String> map = new LinkedHashMap();
        String[] orderBy = {"nombre"};
        String[] propiedadesNombre = {"nombre"};
        boolean[] ascending = {true};

        
        List<EntityReference<Integer>> pl = emd.getEntitiesReferenceByCriteria(Proyecto.class.getName(), null, null, null, propiedadesNombre, orderBy, ascending);

        for (EntityReference p : pl) {
            map.put((String) p.getPropertyMap().get("nombre"), String.valueOf(p.getId()));
        }

        return map;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Proyecto getObjeto() {
        return objeto;
    }

    public void setObjeto(Proyecto objeto) {
        this.objeto = objeto;
    }

    public Integer getAnioValor() {
        return anioValor;
    }

    public void setAnioValor(Integer anioValor) {
        this.anioValor = anioValor;
    }

    public List<ComboValorSeguimiento> getValoresSeguimiento() {
        return valoresSeguimiento;
    }

    public void setValoresSeguimiento(List<ComboValorSeguimiento> valoresSeguimiento) {
        this.valoresSeguimiento = valoresSeguimiento;
    }

    public POLinea getTempLinea() {
        return tempLinea;
    }

    public Integer getModalAbierto() {
        return modalAbierto;
    }

    public void setModalAbierto(Integer modalAbierto) {
        this.modalAbierto = modalAbierto;
    }

    public DualListModel<UnidadTecnica> getUtColaboradoras() {
        return utColaboradoras;
    }

    public void setUtColaboradoras(DualListModel<UnidadTecnica> utColaboradoras) {
        this.utColaboradoras = utColaboradoras;
    }

    public void setTempLinea(POLinea tempLinea) {
        this.tempLinea = tempLinea;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public POIndicadorLinea getTempLineaIndicador() {
        return tempLineaIndicador;
    }

    public void setTempLineaIndicador(POIndicadorLinea tempLineaIndicador) {
        this.tempLineaIndicador = tempLineaIndicador;
    }

    public Indicador getIndicadorEnUso() {
        return indicadorEnUso;
    }

    public void setIndicadorEnUso(Indicador indicadorEnUso) {
        this.indicadorEnUso = indicadorEnUso;
    }

    public Integer getPasoModalIndicador() {
        return pasoModalIndicador;
    }

    public void setPasoModalIndicador(Integer pasoModalIndicador) {
        this.pasoModalIndicador = pasoModalIndicador;
    }

    public String getIdProgramaIndicador() {
        return idProgramaIndicador;
    }

    public void setIdProgramaIndicador(String idProgramaIndicador) {
        this.idProgramaIndicador = idProgramaIndicador;
    }

    public boolean isIndicadorEstrategico() {
        return indicadorEstrategico;
    }

    public void setIndicadorEstrategico(boolean indicadorEstrategico) {
        this.indicadorEstrategico = indicadorEstrategico;
    }

    public String getTipoIndicador() {
        return tipoIndicador;
    }

    public void setTipoIndicador(String tipoIndicador) {
        this.tipoIndicador = tipoIndicador;
    }

    public String getIdIndicador() {
        return idIndicador;
    }

    public void setIdIndicador(String idIndicador) {
        this.idIndicador = idIndicador;
    }

    public String getIdTipoIndicador() {
        return idTipoIndicador;
    }

    public void setIdTipoIndicador(String idTipoIndicador) {
        this.idTipoIndicador = idTipoIndicador;
    }

    public TipoSeguimiento getTipoSeguimientoIndicador() {
        return tipoSeguimientoIndicador;
    }

    public void setTipoSeguimientoIndicador(TipoSeguimiento tipoSeguimientoIndicador) {
        this.tipoSeguimientoIndicador = tipoSeguimientoIndicador;
    }

    public List<UnidadTecnica> getUsuarioUnidadTecnicas() {
        return usuarioUnidadTecnicas;
    }

    public void setUsuarioUnidadTecnicas(List<UnidadTecnica> usuarioUnidadTecnicas) {
        this.usuarioUnidadTecnicas = usuarioUnidadTecnicas;
    }

    // </editor-fold>
}
