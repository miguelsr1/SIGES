/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import com.google.gson.Gson;
import gob.mined.siap2.business.datatype.DataProyectoGraficoTorta;
import gob.mined.siap2.business.datatype.ProyectoArbol;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.CategoriaConvenio;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.ProyectoAporte;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTechoPresupuestarioAnio;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTramoTramo;
import gob.mined.siap2.entities.data.impl.ProyectoCategoriaConvenio;
import gob.mined.siap2.entities.data.impl.ProyectoComponente;
import gob.mined.siap2.entities.data.impl.ProyectoEstPorcentajeFuente;
import gob.mined.siap2.entities.data.impl.ProyectoEstProducto;
import gob.mined.siap2.entities.data.impl.ProyectoEstructura;
import gob.mined.siap2.entities.data.impl.ProyectoFuente;
import gob.mined.siap2.entities.data.impl.ProyectoMacroActividad;
import gob.mined.siap2.entities.data.impl.ProyectoParipassuTramo;
import gob.mined.siap2.entities.data.impl.UnidadTecnica;
import gob.mined.siap2.entities.enums.TipoAporteProyecto;
import gob.mined.siap2.entities.enums.TipoEstructura;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.utils.generalutils.DatesUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author Sofis Solutions
 */
public class ProyectoUtils {

//    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
   private static final Logger logger = Logger.getLogger(ProyectoUtils.class.getName());
    /**
     * Método que se encarga de regenerar los aportes del proyecto en base a
     * las categorías y fuentes
     */
    public static void regenerarAportesProyecto(Proyecto proyecto) {
        List<ProyectoAporte> aportesUsados = new LinkedList<>();

        //se agreagn las fuentes de recursos a un set para que no aparezcan repetidas
        Set<FuenteRecursos> fuentesRecursos = new LinkedHashSet<>();
        for (ProyectoFuente pf : proyecto.getFuentesProyecto()) {
            fuentesRecursos.add(pf.getFuenteRecursos());
        }
        //va a iterar las categorias y para cada fuetne de recurso va a crear un aporte
        for (ProyectoCategoriaConvenio iterCaterogira : proyecto.getDistribuccionCategorias()) {
            for (FuenteRecursos iterF : fuentesRecursos) {
                ProyectoAporte tmpAporte = getAporte(proyecto, iterCaterogira.getCategoriaConvenio(), iterF);
                if (tmpAporte == null) {
                    tmpAporte = new ProyectoAporte();
                    tmpAporte.setProyecto(proyecto);
                    tmpAporte.setCategoriaConvenio(iterCaterogira.getCategoriaConvenio());
                    tmpAporte.setFuenteRecursos(iterF);
                    tmpAporte.setMonto(BigDecimal.ZERO);
                    proyecto.getAportesProyecto().add(tmpAporte);
                }
                aportesUsados.add(tmpAporte);
            }
        }

        //se eliminan los aportes no usados
        List<ProyectoAporte> toDeletes = new LinkedList<>();
        for (ProyectoAporte aporte : proyecto.getAportesProyecto()) {
            if (!aportesUsados.contains(aporte)) {
                toDeletes.add(aporte);
            }
        }
        for (ProyectoAporte aporteAeliminar : toDeletes) {
            eliminarAporteEnEstructura(proyecto, aporteAeliminar);
            //lo elimina del proyecto
            boolean remove = proyecto.getAportesProyecto().remove(aporteAeliminar);
        }

        //una vez regenerados todos los aportes también se regeneran los paripassus para cada aporte
        ProyectoUtils.regenerarTodosParipassuEnTramos(proyecto);

    }

    /**
     * Método que elimina un aporte del proyecto lo saca de todos los montos de
     * la estructura
     *
     * @param proyecto
     * @param toDelete
     */
    private static void eliminarAporteEnEstructura(Proyecto proyecto, ProyectoAporte toDelete) {
        //lo elimina de los componentes
        for (ProyectoComponente pc : proyecto.getProyectoComponentes()) {
            Iterator<ProyectoEstPorcentajeFuente> iter = pc.getMontosFuentes().iterator();
            while (iter.hasNext()) {
                ProyectoEstPorcentajeFuente mf = iter.next();
                if (mf.getFuente().equals(toDelete)) {
                    iter.remove();
                }
            }
        }
        //lo elimina de las macroactividades
        Iterator iterMA = proyecto.getProyectoMacroactividad().iterator();
        while (iterMA.hasNext()) {
            ProyectoMacroActividad pm = (ProyectoMacroActividad) iterMA.next();
            Iterator iterF = pm.getMontosFuentes().iterator();
            while (iterF.hasNext()) {
                ProyectoEstPorcentajeFuente pmaf = (ProyectoEstPorcentajeFuente) iterF.next();
                if (pmaf.getFuente().equals(toDelete)) {
                    iterF.remove();
                }
            }
        }

    }

    /**
     * regenera la estructura, agrega montos solo para los aportes que tienen
     * monto
     *
     * @param proyecto
     */
    public static void regenerarMontosEstructura(Proyecto proyecto) {
        //se agregan las fuentes nuevas
        for (ProyectoAporte aporte : proyecto.getAportesProyecto()) {
            if (aporteVacio(aporte)) {
                eliminarAporteEnEstructura(proyecto, aporte);
            } else {
                //tiene que verificar que el monto del aporte exista para todos los componentes y macroactividades
                for (ProyectoMacroActividad macroActividad : proyecto.getProyectoMacroactividad()) {
                    if (getMontoAporteFromEstructura(macroActividad, aporte) == null) {
                        ProyectoEstPorcentajeFuente monto = new ProyectoEstPorcentajeFuente();
                        monto.setFuente(aporte);
                        monto.setProyectoEstructura(macroActividad);
                        monto.setMontoEnConstruccion(BigDecimal.ZERO);
                        macroActividad.getMontosFuentes().add(monto);
                    }
                }

                //tiene que verificar que el aporte exista para todos los componentes
                for (ProyectoComponente componente : proyecto.getProyectoComponentes()) {
                    if (getMontoAporteFromEstructura(componente, aporte) == null) {
                        ProyectoEstPorcentajeFuente monto = new ProyectoEstPorcentajeFuente();
                        monto.setFuente(aporte);
                        monto.setProyectoEstructura(componente);
                        monto.setMontoEnConstruccion(BigDecimal.ZERO);
                        componente.getMontosFuentes().add(monto);
                    }
                }
            }
        }

        //se eliminan las fuentes eliminadas en los montos de las estructuras
        for (ProyectoMacroActividad macroActividad : proyecto.getProyectoMacroactividad()) {
            removeAportesEliminadosEnEstrucura(macroActividad, proyecto);
        }
        for (ProyectoComponente componente : proyecto.getProyectoComponentes()) {
            removeAportesEliminadosEnEstrucura(componente, proyecto);
        }

    }

    private static void removeAportesEliminadosEnEstrucura(ProyectoEstructura pe, Proyecto proyecto) {
        Iterator<ProyectoEstPorcentajeFuente> iter = pe.getMontosFuentes().iterator();
        while (iter.hasNext()) {
            ProyectoEstPorcentajeFuente mf = iter.next();
            if (!proyecto.getAportesProyecto().contains(mf.getFuente())) {
                iter.remove();
            }
        }
    }

    /**
     * Método que se encarga de crear los portes para una estructura con montos
     * vacíos
     *
     * @param proyecto
     * @param estructura
     */
    public static void crearAportesParaEstrucutra(Proyecto proyecto, ProyectoEstructura estructura) {
        for (ProyectoAporte aporte : proyecto.getAportesProyecto()) {
            if (!aporteVacio(aporte)) {
                ProyectoEstPorcentajeFuente monto = new ProyectoEstPorcentajeFuente();
                monto.setFuente(aporte);
                monto.setProyectoEstructura(estructura);
                estructura.getMontosFuentes().add(monto);
            }
        }
    }

    /**
     * Método que retorna si un aporte es vacío o no si el aporte es vacío no se
     * usa en la distribución de montos de la estructura
     *
     * @param aporte
     * @return
     */
    public static boolean aporteVacio(ProyectoAporte aporte) {
        if ((aporte.getMonto() == null || aporte.getMonto().compareTo(BigDecimal.ZERO) == 0) /* && (aporte.getPorcentaje() == null || aporte.getPorcentaje().compareTo(BigDecimal.ZERO) == 0)*/) {
            return true;
        }
        return false;
    }

    public static boolean aporteEnEstructuraVacio(ProyectoEstPorcentajeFuente aporte, boolean enConstruccion) {
        if (!enConstruccion) {
            if ((aporte.getMonto() == null || aporte.getMonto().compareTo(BigDecimal.ZERO) == 0) /* && (aporte.getPorcentaje() == null || aporte.getPorcentaje().compareTo(BigDecimal.ZERO) == 0)*/) {
                return true;
            }
        } else {
            if ((aporte.getMontoEnConstruccion() == null || aporte.getMontoEnConstruccion().compareTo(BigDecimal.ZERO) == 0) /* && (aporte.getPorcentaje() == null || aporte.getPorcentaje().compareTo(BigDecimal.ZERO) == 0)*/) {
                return true;
            }
        }

        return false;
    }

    /**
     * Método que se encarga de generar los paripassu en los tramos de una
     * categoría
     *
     * @param proyecto
     * @param aporte
     */
    public static void regenerarPAripassuuEnTramos(Proyecto proyecto, ProyectoCategoriaConvenio categoria) {

        for (ProyectoAporteTramoTramo tramo : categoria.getTramos()) {
            List<ProyectoParipassuTramo> usados = new LinkedList<>();

            for (ProyectoFuente fuente : proyecto.getFuentesProyecto()) {
                ProyectoParipassuTramo paripassuTramo = getParipassuTramo(tramo, fuente.getFuenteRecursos());
                if (paripassuTramo == null) {
                    paripassuTramo = new ProyectoParipassuTramo();
                    paripassuTramo.setFuenteRecursos(fuente.getFuenteRecursos());
                    paripassuTramo.setTramo(tramo);
                    tramo.getParipassus().add(paripassuTramo);
                }
                usados.add(paripassuTramo);
            }

            //se elimina lo que no se uso            
            Iterator<ProyectoParipassuTramo> iter = tramo.getParipassus().iterator();
            while (iter.hasNext()) {
                ProyectoParipassuTramo objeto = iter.next();
                if (!usados.contains(objeto)) {
                    iter.remove();
                }
            }
        }

    }

    /**
     * Método que se encarga de regenerar todos los paripassu en los tramos
     *
     * @param proyecto
     */
    public static void regenerarTodosParipassuEnTramos(Proyecto proyecto) {
        for (ProyectoCategoriaConvenio categoria : proyecto.getDistribuccionCategorias()) {
            if (categoria.getCategoriaConvenio().getTipo() == TipoAporteProyecto.POR_TRAMOS) {
                regenerarPAripassuuEnTramos(proyecto, categoria);
            }
        }
    }

    /**
     * Método que inicializa los paripassu en tramos
     *
     * @param tramo
     * @param proyecto
     */
    public static void createParipassusTramo(Proyecto proyecto, ProyectoAporteTramoTramo tramo) {
        for (ProyectoFuente fuente : proyecto.getFuentesProyecto()) {
            ProyectoParipassuTramo paripassuTramo = new ProyectoParipassuTramo();
            paripassuTramo.setFuenteRecursos(fuente.getFuenteRecursos());
            paripassuTramo.setTramo(tramo);
            tramo.getParipassus().add(paripassuTramo);
        }
    }

    public static ProyectoParipassuTramo getParipassuTramo(ProyectoAporteTramoTramo tramo, FuenteRecursos fuenteRecursos) {
        for (ProyectoParipassuTramo tp : tramo.getParipassus()) {
            if (tp.getFuenteRecursos().equals(fuenteRecursos)) {
                return tp;
            }
        }
        return null;
    }

    /**
     * retorna el aporte, que busca por categoría y fuente de recursos
     *
     * @param cate
     * @param fuenteR
     * @return
     */
    public static ProyectoAporte getAporte(Proyecto proyecto, CategoriaConvenio cate, FuenteRecursos fuenteR) {
        for (ProyectoAporte aporte : proyecto.getAportesProyecto()) {
            if (aporte.getCategoriaConvenio().equals(cate)
                && aporte.getFuenteRecursos().equals(fuenteR)) {
                return aporte;
            }
        }
        return null;
    }

    public static BigDecimal getTechoFuenteParaAnio(ProyectoFuente proyectoFuente, AnioFiscal anioFiscal) {
        for (ProyectoAporteTechoPresupuestarioAnio techo : proyectoFuente.getTechos()) {
            if (techo.getAnioFiscal().equals(anioFiscal)) {
                return techo.getTecho() != null ? techo.getTecho() : BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    public static ProyectoEstPorcentajeFuente getMontoAporteFromEstructura(ProyectoEstructura estructura, ProyectoAporte fuente) {
        for (ProyectoEstPorcentajeFuente iter : estructura.getMontosFuentes()) {
            if (iter.getFuente() != null && iter.getFuente().equals(fuente)) {
                return iter;
            }
        }
        return null;
    }

    public static POMontoFuenteInsumo getInsumoMontoFuente(ProyectoAporte fuente, List<POMontoFuenteInsumo> componentes) {
        for (POMontoFuenteInsumo iter : componentes) {
            if (iter.getFuente().equals(fuente)) {
                return iter;
            }
        }
        return null;
    }

    public static ProyectoEstPorcentajeFuente getComponenteMontoFuente(Integer idfuente, List<ProyectoEstPorcentajeFuente> componentes) {
        for (ProyectoEstPorcentajeFuente iter : componentes) {
            if (idfuente.equals(iter.getFuente().getId())) {
                return iter;
            }
        }
        return null;
    }

    public static ProyectoEstPorcentajeFuente getComponenteMontoMacroActividad(Integer idfuente, List<ProyectoEstPorcentajeFuente> componentes) {
        for (ProyectoEstPorcentajeFuente iter : componentes) {
            if (idfuente.equals(iter.getFuente().getId())) {
                return iter;
            }
        }
        return null;
    }

    public static void ordenarAportesTramos(List<ProyectoAporteTramoTramo> aporte, boolean enConstruccion) {
        if (enConstruccion) {
            Collections.sort(aporte, new Comparator<ProyectoAporteTramoTramo>() {
                @Override
                public int compare(ProyectoAporteTramoTramo o1, ProyectoAporteTramoTramo o2) {
                    return o1.getMontoHastaEnConstruccion().compareTo(o2.getMontoHastaEnConstruccion());
                }
            });
        }else{
            Collections.sort(aporte, new Comparator<ProyectoAporteTramoTramo>() {
                @Override
                public int compare(ProyectoAporteTramoTramo o1, ProyectoAporteTramoTramo o2) {
                    return o1.getMontoHasta().compareTo(o2.getMontoHasta());
                }
            });            
        }
    }

    public static void reorganizarAporteTramo(ProyectoCategoriaConvenio categoria) {
        ordenarAportesTramos(categoria.getTramos(), true);

        ProyectoAporteTramoTramo tramoAnterior = null;
        for (ProyectoAporteTramoTramo tramo : categoria.getTramos()) {
            //verifica que dos tramos no financien el mismo porcentaje
            if (tramoAnterior != null && tramoAnterior.getMontoHastaEnConstruccion().compareTo(tramo.getMontoHastaEnConstruccion()) == 0) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_HAY_DOS_TRAMOS_QUE_FINANCIAN_HASTA_EL_MISMO_MONTO);
                throw b;
            }
            //sigue iterando
            tramoAnterior = tramo;
        }

        //todo: verificar que los tramos no se pasen de algo
    }

    public static List<Integer> getListAnios(Proyecto objeto) {
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
     * convierte a grafica de torta
     */
    public static String getJsonProyectoToGraficaTorta(Proyecto p) {
        if (p == null) {
            return null;
        }

        DataProyectoGraficoTorta data = new DataProyectoGraficoTorta();
        data.setName("Distribución de monto del proyecto");
        data.setChildren(new LinkedList<DataProyectoGraficoTorta>());

        for (ProyectoComponente componente : p.getProyectoComponentes()) {
            if (componente.getComponentePadre() == null) {
                addComponente(data, componente);
            }
        }
        for (ProyectoMacroActividad macroActividad : p.getProyectoMacroactividad()) {
            addMacroActividad(data, macroActividad);
        }

        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public static void addComponente(DataProyectoGraficoTorta padre, ProyectoComponente componente) {
        DataProyectoGraficoTorta actual = new DataProyectoGraficoTorta();
        actual.setName(componente.getNombre());
        actual.setSize(componente.getImporte());
        actual.setChildren(new LinkedList<DataProyectoGraficoTorta>());
        padre.getChildren().add(actual);

        for (ProyectoComponente hijo : componente.getComponenteHijos()) {
            addComponente(actual, hijo);
        }
    }

    public static void addMacroActividad(DataProyectoGraficoTorta padre, ProyectoMacroActividad macroactividad) {
        DataProyectoGraficoTorta actual = new DataProyectoGraficoTorta();
        if (macroactividad.getMacroActividad() != null) {
            actual.setName(macroactividad.getMacroActividad().getNombre());
        }
        actual.setSize(macroactividad.getImporte());
        actual.setChildren(new LinkedList<DataProyectoGraficoTorta>());
        padre.getChildren().add(actual);

    }

    
    public static List<FuenteRecursos> getFuentesRecursosProyecto(Proyecto objeto) {
        List<FuenteRecursos> l = new LinkedList();
        for (ProyectoAporte aporte : objeto.getAportesProyecto()) {
            if (!l.contains(aporte.getFuenteRecursos())) {
                l.add(aporte.getFuenteRecursos());
            }
        }
        return l;
    }

    /**
     * convierte a estructura de arbol *
     */
    public static String getJsonProyectoToArbol(ProyectoArbol p) {
        if (p == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(p);
    }

    private static String getTitleProyectoArbol(String nombre, String nombreUT, String monto, String cssClass) {
        return String.format("<div class='proyecto-node-text %4$s'>"
            + "<span class='proyecto-nodo-nombre'>%1$1.23s</span><br/>"
            + "<span class='proyecto-nodo-ut'>%2$1.23s</span><br/> "
            + "<span class='proyecto-nodo-monto'>%3$s</span>"
            + "</div>", TextUtils.ellipsize(nombre, 23), TextUtils.ellipsize(nombreUT, 23), monto, cssClass);
    }

    /**
     * convierte el arbol de proyecto
     *
     * @param p
     * @return
     */
    public static ProyectoArbol convertProyectoToArbol(Proyecto p) {
        if (p == null) {
            return null;
        }
        ProyectoArbol root = new ProyectoArbol();
        root.setChildren(new LinkedList());
        root.setName(getTitleProyectoArbol(p.getNombre(), "", String.valueOf(p.getMontoGlobalEnConstruccion()), "proyecto-nodo-base"));
        for (ProyectoComponente componente : p.getProyectoComponentes()) {
            if (componente.getComponentePadre() == null) {
                completeNodoComponente(componente, root);
            }
        }
        for (ProyectoMacroActividad macroactividad : p.getProyectoMacroactividad()) {
            completeNodoMacroactividad(macroactividad, root);
        }
        return root;
    }

    private static void completeNodoComponente(ProyectoComponente componente, ProyectoArbol padre) {
        ProyectoArbol node = new ProyectoArbol();
        node.setChildren(new LinkedList());
        node.setName(getTitleProyectoArbol(componente.getNombre(), getNombreUT(componente.getUnidadTecnica()), String.valueOf(componente.getImporte()), "proyecto-nodo-componente"));
        for (ProyectoComponente chijo : componente.getComponenteHijos()) {
            completeNodoComponente(chijo, node);
        }
        for (ProyectoEstProducto producto : componente.getProductos()) {
            completeNodoProducto(producto, node);
        }
        padre.getChildren().add(node);
    }

    private static void completeNodoMacroactividad(ProyectoMacroActividad macroactividad, ProyectoArbol padre) {
        ProyectoArbol node = new ProyectoArbol();
        node.setChildren(new LinkedList());
        String name = macroactividad.getMacroActividad() != null ? macroactividad.getMacroActividad().getNombre() : "";
        node.setName(getTitleProyectoArbol(name, getNombreUT(macroactividad.getUnidadTecnica()), String.valueOf(macroactividad.getImporte()), "proyecto-nodo-macroactividad"));
        for (ProyectoEstProducto producto : macroactividad.getProductos()) {
            completeNodoProducto(producto, node);
        }
        padre.getChildren().add(node);
    }

    private static void completeNodoProducto(ProyectoEstProducto producto, ProyectoArbol padre) {
        ProyectoArbol node = new ProyectoArbol();
        node.setChildren(new LinkedList());
        String name = producto.getProducto() != null ? producto.getProducto().getNombre() : "";
        node.setName(getTitleProyectoArbol(name, getNombreUT(producto.getUnidadTecnica()), "", "proyecto-nodo-producto"));
        padre.getChildren().add(node);
    }

    public static String getNombreUT(UnidadTecnica ut) {
        if (ut == null) {
            return "";
        }
        return ut.getNombre();
    }

    /* Get width of a given level */

 /* Function to get the maximum width of a tree*/
    public static int getMaxWidth(ProyectoArbol node) {
        //int width;
        int h = height(node);

        // Create an array that will store count of nodes at each level
        int count[] = new int[10];

        int level = 0;

        // Fill the count array using preorder traversal
        getMaxWidthRecur(node, count, level);

        // Return the maximum value from count array
        return getMax(count, h);
    }

    // A function that fills count array with count of nodes at every
    // level of given binary tree
    private static void getMaxWidthRecur(ProyectoArbol node, int count[], int level) {
        if (node != null) {
            count[level]++;
            for (ProyectoArbol hijo : node.getChildren()) {
                getMaxWidthRecur(hijo, count, level + 1);
            }
        }
    }

    /* UTILITY FUNCTIONS */
 /* Compute the "height" of a tree -- the number of
     nodes along the longest path from the root node
     down to the farthest leaf node.*/
    private static int height(ProyectoArbol node) {
        if (node == null) {
            return 0;
        } else {
            /* compute the height of each subtree */
            int max = 0;
            for (ProyectoArbol hijo : node.getChildren()) {
                int size = height(hijo);
                if (size > max) {
                    max = size;
                }
            }
            /* use the larger one */
            return (max + 1);
        }
    }

    // Return the maximum value from count array
    private static int getMax(int arr[], int n) {
        int max = arr[0];
        int i;
        for (i = 0; i < n; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    /**
     *
     */
    public static void recalcularTodasCategorias(Proyecto p) {
        for (ProyectoCategoriaConvenio proyectoCategoria : p.getDistribuccionCategorias()) {
            recalcularCategoria(p, proyectoCategoria);
        }
    }

    /**
     * Método que se encarga de setear el monto usado en una categoria
     *
     * @param p
     * @param proyectoCategoria
     */
    public static void recalcularCategoria(Proyecto p, ProyectoCategoriaConvenio proyectoCategoria) {
        BigDecimal total = BigDecimal.ZERO;
        for (ProyectoAporte aporte : p.getAportesProyecto()) {
            if (aporte.getCategoriaConvenio().equals(proyectoCategoria.getCategoriaConvenio()) && aporte.getMonto() != null) {
                total = total.add(aporte.getMonto());
            }
        }
        proyectoCategoria.setMonto(total);

    }

    public static ProyectoCategoriaConvenio getProyectoCategoria(Proyecto proyecto, CategoriaConvenio categoria) {
        for (ProyectoCategoriaConvenio pcat : proyecto.getDistribuccionCategorias()) {
            if (pcat.getCategoriaConvenio().equals(categoria)) {
                return pcat;
            }
        }
        return null;
    }

    public static CategoriaConvenio getCategoriaConvenio(ProyectoEstructura estructura, boolean enConstruccion) {
        for (ProyectoEstPorcentajeFuente montoFuente : estructura.getMontosFuentes()) {
            if (!aporteEnEstructuraVacio(montoFuente, enConstruccion)) {
                return montoFuente.getFuente().getCategoriaConvenio();
            }
        }
        return null;
    }

    public static String getNombreEstructura(ProyectoEstructura estructura) {
        if (estructura == null) {
            return null;
        }
        if (estructura.getTipo() == TipoEstructura.COMPONENTE) {
            return ((ProyectoComponente) estructura).getNombre();
        }
        if (estructura.getTipo() == TipoEstructura.MACROACTIVIDAD) {
            ProyectoMacroActividad macro = ((ProyectoMacroActividad) estructura);
            if (macro.getMacroActividad() != null) {
                return macro.getMacroActividad().getNombre();
            }
        }

        return null;
    }

    public static void ordenarProyectoEstructuraPorOrden(List<ProyectoEstructura> estructuras) {
        if (estructuras != null) {
            Collections.sort(estructuras, new Comparator<ProyectoEstructura>() {
                @Override
                public int compare(ProyectoEstructura o1, ProyectoEstructura o2) {
                    //los do snulos
                    if (o1.getNumero() == o2.getNumero()) {
                        return 0;
                    }
                    if (o1.getNumero() != null && o2.getNumero() == null) {
                        return -1;
                    }
                    if (o2.getNumero() != null && o1.getNumero() == null) {
                        return 1;
                    }
                    return o1.getNumero().compareTo(o2.getNumero());
                }
            });
        }
        //return componentes;
    }

}
