/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.po;

import gob.mined.siap2.business.utils.POGUtils;
import gob.mined.siap2.business.utils.ProyectoUtils;
import gob.mined.siap2.business.utils.UnidadTecnicaUtils;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.AnioFiscal;
import gob.mined.siap2.entities.data.impl.GeneralPOA;
import gob.mined.siap2.entities.data.impl.POActividadBase;
import gob.mined.siap2.entities.data.impl.POGActividadProyecto;
import gob.mined.siap2.entities.data.impl.POGInsumo;
import gob.mined.siap2.entities.data.impl.POGInsumoAnio;
import gob.mined.siap2.entities.data.impl.POGMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.data.impl.POLinea;
import gob.mined.siap2.entities.data.impl.POMontoFuenteInsumo;
import gob.mined.siap2.entities.data.impl.ProyectoAporte;
import gob.mined.siap2.entities.data.impl.ProyectoAporteTramoTramo;
import gob.mined.siap2.entities.data.impl.ProyectoCategoriaConvenio;
import gob.mined.siap2.entities.data.impl.ProyectoFuente;
import gob.mined.siap2.entities.enums.TipoAporteProyecto;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.datatypes.DataNodeDistribuccionCategorias;
import gob.mined.siap2.web.delegates.ProyectoDelegate;
import gob.mined.siap2.web.mb.TextMB;
import static gob.mined.siap2.web.mb.impl.po.POConActividadesEInsumosAbstract.logger;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la
 * página que maneja los POG de proyecto.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "pogProyectoCE")
public class POGProyectoCE extends POProyectoConLineasAbstract implements Serializable {

    @Inject
    ProyectoDelegate proyectoDelegate;

    @Inject
    TextMB textMB;

    AnioFiscal anioEjecucion;

    private String idProyecto;

    private List<ProyectoFuente> fuenteProyectoSaldoMayorQueCero;

    @PostConstruct
    @Override
    public void init() {
        verTodosLosMontos = false;
        idProyecto = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(idProyecto)) {
            update = true;
        }
        super.init();
        initProyecto();
    }

    /**
     * Este método es inicializar el POA
     */
    public void reloadPO() {
        initProyecto();
    }

    /**
     * Este método se utiliza para realizar la inicialización
     */
    public void initProyecto() {
        if (!TextUtils.isEmpty(idProyecto)) {
            objeto = proyectoDelegate.getProyectoForPOG(Integer.valueOf(idProyecto));
            anioEjecucion = generalPODelegate.getMenorAnioEjecucion();
        }
    }

    /**
     * Este método retorna el POa de trabajo
     *
     * @return
     */
    @Override
    public GeneralPOA getPOAEnTrabajo() {
        return null;
    }

    /**
     * Este método retorna el tipo de POG
     *
     * @return
     */
    @Override
    public String getTipoPO() {
        return POConActividadesEInsumosAbstract.TIPO_POG_PROYECTO;
    }

    /**
     * Devuelve una lista de ProyectoAporte filtrando aquellos que el monto es
     * mayor a cero.
     *
     * @return
     */
    public List<ProyectoAporte> getProyectoAporteMayorACero() {
        List<ProyectoAporte> respuesta = new ArrayList<>();
        if (objeto != null && objeto.getAportesProyecto() != null) {
            for (ProyectoAporte proyectoAporte : objeto.getAportesProyecto()) {
                if (proyectoAporte.getMonto() != null && proyectoAporte.getMonto().compareTo(BigDecimal.ZERO) > 0) {
                    respuesta.add(proyectoAporte);
                }
            }
        }
        return respuesta;
    }

    /**
     * Devuelve una clase para CSS utilizada la tabla. Esto permite variar el
     * tamaño de letra según la cantidad de columnas.
     *
     * @return
     */
    public String getFuenteStyleClass() {
        int cantColumnas = getProyectoAporteMayorACero().size() + 1;//Se suma 1 por el campo "Nombre"
        if (cantColumnas <= 8) {
            return "columnasLetraNormal";
        }
        if (cantColumnas <= 13) {
            return "columnasLetraChica";
        }
        if (cantColumnas < 13) {
            return "columnasLetraMuyChica";
        }
        return "columnasLetraNormal";
    }

    // este metodo no se tendria que usar mas en pog
    public boolean estaElPOACargado() {
        return (objeto != null && objeto.getPog() != null);
    }

    /**
     * el guardar general solo guarda la línea (indicadores, colaboradores..
     * etc) los guardar de la línea para abajo se sobreescriben
     */
    @Override
    public void guardar() {
        proyectoDelegate.actualizarLineaPOG(objeto.getId(), tempLinea);
        initProyecto();
    }

    /**
     * Este método guarda la linea en edición
     */
    @Override
    public void saveLinea() {
        try {
            tempLinea.setColaboradoras(utColaboradoras.getTarget());
            tempLinea.setValoresProducto(valoresSeguimiento);

            proyectoDelegate.actualizarLineaPOG(objeto.getId(), tempLinea);
            initProyecto();
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
     * Este método es el encargado de iniciar una actividad
     */
    @Override
    public void initActividad() {
        //initLinea();
        if (tempActividad == null) {
            tempActividad = new POGActividadProyecto();
            tempActividad.setInsumos(new LinkedList());
        }
        super.initActividad();
    }

    /**
     * Este método es el encargado de retornar la actividad en edición.
     *
     * @return
     */
    @Override
    public POGActividadProyecto getTempActividad() {
        return (POGActividadProyecto) tempActividad;
    }

    /**
     * Este método es el encargado de guardar la actividad en edición
     */
    @Override
    public void guardarActividad() {
        try {
            tempActividad.setUtResponsable(unidadTecnicaSelecionada);
            if (TextUtils.isEmpty(idUsuarioActividad)) {
                tempActividad.setResponsable(null);
            } else {
                SsUsuario responsable = (SsUsuario) emd.getEntityById(SsUsuario.class.getName(), Integer.valueOf(idUsuarioActividad));
                tempActividad.setResponsable(responsable);
            }
            proyectoDelegate.crearActualizarActividadPOG(objeto.getId(), tempLinea.getId(), (POGActividadProyecto) tempActividad);
            initProyecto();
            RequestContext.getCurrentInstance().execute("$('#anadirActividad').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método es el encargado de guardar la distribución de insumos
     */
    public void guardarDistribuccionInsumos() {
        try {
            proyectoDelegate.guardarDistribucionAniosInsumosPOG(objeto.getId(), tempLinea.getId(), (POGActividadProyecto) tempActividad);
            initProyecto();
            RequestContext.getCurrentInstance().execute("$('#anadirPOGcompletarInsumosEnActividad').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método es el encargado de inicializar un insumo de POG de proyecto
     */
    @Override
    public void initInsumo() {
        if (tempInsumo == null) {
            tempInsumo = new POGInsumo();
            idInsumo = null;
            //tempInsumo.setTipoMontoEstructura(TipoMontoEstructura.IMPORTE);
            tempInsumo.setMontosFuentes(new LinkedList());
            tempInsumo.setFlujosDeCajaAnio(new LinkedHashSet());
            tempInsumo.setNoUACI(false);
            tempInsumo.setEnviadoParaCertificar(false);
            tempInsumo.setPasoValidacionCertificadoDeDispPresupuestaria(false);
            ((POGInsumo) tempInsumo).setDistribucionAnios(new LinkedList());
            tempInsumo.setTramo(getTramoCorrespondeInsumo());
        }
        super.initInsumo();

        cantidadMontosFuentesDePoInsumo = 0;
        if (tempInsumo.getMontosFuentes() != null) {
            cantidadMontosFuentesDePoInsumo = tempInsumo.getMontosFuentes().size();
        }
    }

    /**
     * Este método es el encargado de realizar el cierre del POG
     */
    public void cerrarPOG() {
        try {
            proyectoDelegate.consolidarPOG(objeto.getId());
            initProyecto();
            RequestContext.getCurrentInstance().execute("$('#cerrarPOGModal').modal('hide');");

            String texto = textMB.obtenerTexto("labels.POGCerradoCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método es el encargado de ordenar la distribución de insumos en la
     * actividad en edición
     */
    public void ordenarDistribucionInsumosEnActividad() {
        List<Integer> anios = getListAniosTempActividad();
        for (int pos = 0; pos < anios.size(); pos++) {
            Integer anio = anios.get(pos);
            for (Iterator<POInsumos> it = tempActividad.getInsumos().iterator(); it.hasNext();) {
                POGInsumo pogInsumo = (POGInsumo) it.next();

                //reordena la lista segun los años actuales del pog
                int iter = 0;
                boolean encontro = false;
                POGInsumoAnio insumoAnio = null;
                while (iter < pogInsumo.getDistribucionAnios().size()) {
                    if (pogInsumo.getDistribucionAnios().get(iter).getAnio().equals(anio)) {
                        encontro = true;
                        break;
                    }
                    iter++;
                }
                //si lo encontro en otra posicion lo cambia de lugar
                if (encontro && iter != pos) {
                    insumoAnio = pogInsumo.getDistribucionAnios().get(iter);
                    pogInsumo.getDistribucionAnios().remove(iter);
                    pogInsumo.getDistribucionAnios().add(pos, insumoAnio);
                } else if (encontro) {
                    insumoAnio = pogInsumo.getDistribucionAnios().get(iter);
                }

                if (!encontro) {
                    insumoAnio = new POGInsumoAnio();
                    insumoAnio.setInsumo(pogInsumo);
                    insumoAnio.setAnio(anio);
                    insumoAnio.setMontosFuentes(new LinkedList<POGMontoFuenteInsumo>());
                    pogInsumo.getDistribucionAnios().add(pos, insumoAnio);
                }

                //se ordenan todas las fuentes segun las fuentes actuales del pog
                for (int posFuente = 0; posFuente < objeto.getAportesProyecto().size(); posFuente++) {
                    ProyectoAporte fuente = objeto.getAportesProyecto().get(posFuente);
                    int iterFuente = 0;
                    boolean encontroFuente = false;
                    while (iterFuente < insumoAnio.getMontosFuentes().size()) {
                        if (insumoAnio.getMontosFuentes().get(iterFuente).getFuente().equals(fuente)) {
                            encontroFuente = true;
                            break;
                        }
                        iterFuente++;
                    }

                    //si lo encontro en otra posicion lo cambia de lugar
                    if (encontroFuente && iterFuente != posFuente) {
                        POGMontoFuenteInsumo mf = insumoAnio.getMontosFuentes().get(iterFuente);
                        insumoAnio.getMontosFuentes().remove(iterFuente);
                        insumoAnio.getMontosFuentes().add(posFuente, mf);
                    }

                    if (!encontroFuente) {
                        POGMontoFuenteInsumo mf = new POGMontoFuenteInsumo();
                        mf.setInsumo(insumoAnio);
                        mf.setFuente(fuente);
                        insumoAnio.getMontosFuentes().add(posFuente, mf);
                    }

                }
            }
        }
    }

    /**
     * Este método retorna si un aporte es vacío
     *
     * @param aporte
     * @return
     */
    public boolean esVacioAporteProyecto(ProyectoAporte aporte) {
        return ProyectoUtils.aporteVacio(aporte);
    }

    /**
     * Este método retorna los aportes no vacíos del proyecto
     *
     * @return
     */
    public List<ProyectoAporte> getAportesProyectosNoVacios() {
        List<ProyectoAporte> res = new LinkedList<>();
        for (ProyectoAporte aporte : objeto.getAportesProyecto()) {
            if (!ProyectoUtils.aporteVacio(aporte)) {
                res.add(aporte);
            }
        }
        return res;
    }

    /**
     * Este método suma los montos de las fuentes para un año
     *
     * @param montoFuente
     * @return
     */
    public BigDecimal sumarMontosFuentesParaAnioPOG(POGInsumoAnio montoFuente) {
        BigDecimal total = BigDecimal.ZERO;
        if (montoFuente != null) {
            for (POGMontoFuenteInsumo mf : montoFuente.getMontosFuentes()) {
                if (mf.getMonto() != null) {
                    total = total.add(mf.getMonto());
                }
            }
        }
        return total;
    }

    /**
     * Este método suma los montos de las fuentes para un año
     *
     *
     * @param iterPOInsumo
     * @return
     */
    public BigDecimal sumarMontosFuentesParaTodosAnioPOG(POGInsumo iterPOInsumo) {
        BigDecimal total = BigDecimal.ZERO;
        if (iterPOInsumo != null) {
            for (POGInsumoAnio insumoAnio : iterPOInsumo.getDistribucionAnios()) {
                for (POGMontoFuenteInsumo mf : insumoAnio.getMontosFuentes()) {
                    if (mf.getMonto() != null) {
                        total = total.add(mf.getMonto());
                    }
                }
            }
        }
        return total;
    }

    /**
     * Este método retorna los totales de las fuentes para un año
     *
     * @param fuente
     * @param anio
     * @return
     */
    public BigDecimal sumarMontosTotalesFuentesAnioPOG(ProyectoAporte fuente, Integer anio) {
        BigDecimal total = BigDecimal.ZERO;
        if (tempActividad != null) {
            for (Iterator<POInsumos> it = tempActividad.getInsumos().iterator(); it.hasNext();) {
                POGInsumo insumo = (POGInsumo) it.next();
                for (POGInsumoAnio insumoAnio : insumo.getDistribucionAnios()) {
                    if (anio.equals(0) || anio.equals(insumoAnio.getAnio())) {
                        for (POGMontoFuenteInsumo mf : insumoAnio.getMontosFuentes()) {
                            if (fuente == null || mf.getFuente().equals(fuente)) {
                                if (mf.getMonto() != null) {
                                    total = total.add(mf.getMonto());
                                }
                            }
                        }
                    }
                }

            }
        }
        return total;
    }

    /**
     * Este método es el encargado de guardar un insumo.
     */
    @Override
    public void guardarInsumo() {
        try {
            proyectoDelegate.crearActualizarInsumoPOG(objeto.getId(), tempLinea.getId(), tempActividad.getId(), tempInsumo);
            initProyecto();
            RequestContext.getCurrentInstance().execute("$('#anadirInsumo').modal('hide');");
            RequestContext.getCurrentInstance().execute("$('#veranadirInsumo').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método es se utiliza para retornar al página anterior
     *
     * @return
     */
    public String cerrar() {
        return "consultaPOGParaProyectos.xhtml?faces-redirect=true";
    }

    /**
     * Este método retorna la cantidad total de un insumo, sumando la
     * distribución entre los años
     *
     * @param pogInsumo
     * @return
     */
    public Integer sumarCantidadesInsumos(POGInsumo pogInsumo) {
        Integer total = 0;
        for (POGInsumoAnio anio : pogInsumo.getDistribucionAnios()) {
            if (anio.getCantidadInsumo() != null) {
                total = total + anio.getCantidadInsumo();
            }
        }
        return total;
    }

    /**
     * Este método retorna los años disponibles para una actividad
     *
     * @return
     */
    public List<Integer> getListAniosTempActividad() {
        List<Integer> l = new LinkedList();
        if (tempActividad != null) {
            POGActividadProyecto actividad = (POGActividadProyecto) tempActividad;
            if (actividad.getAnioFin() != null) {
                for (int anio = actividad.getAnioInicio(); anio <= actividad.getAnioFin(); anio++) {
                    l.add(anio);
                }
            }
        }
        return l;
    }

    /**
     * Este método retorna los años del POG en una estructura de mapa
     *
     * @return
     */
    public Map<String, Integer> getAniosMap() {
        Map<String, Integer> map = new LinkedHashMap<>();
        List<Integer> anios = getListAniosPO();
        for (Integer anio : anios) {
            map.put(String.valueOf(anio), (anio));
        }
        return map;
    }

    /**
     * Este método retorna la fecha de inicio del POG
     *
     * @return
     */
    @Override
    public Date getInicioPO() {
        if (objeto == null) {
            return null;
        }
        return objeto.getInicio();
    }

    /**
     * este método retorna la fecha de fin del POG
     *
     * @return
     */
    @Override
    public Date getFinPO() {
        if (objeto == null) {
            return null;
        }
        return objeto.getFin();
    }

    /**
     * Este método añade una linea nueva al POG
     *
     * @param tempLinea
     */
    @Override
    public void addTmpLinea(POLinea tempLinea) {
        if (!objeto.getPog().getLineas().contains(tempLinea)) {
            objeto.getPog().getLineas().add(tempLinea);
        }
    }

    /**
     * Este método elimina una linea del POG
     *
     * @param tempLinea
     */
    @Override
    public void eliminarTmpLinea(POLinea tempLinea) {
        objeto.getPog().getLineas().remove(tempLinea);
    }

    /**
     * Este método retorna las lineas del POG
     *
     * @return
     */
    @Override
    public List<POLinea> getLineas() {
        //filtra la lineas para las que solo tiene permiso de ver
        List<POLinea> lienas = new LinkedList();
        for (POLinea linea : objeto.getPog().getLineas()) {
            if (UnidadTecnicaUtils.tieneAccesoAUT(usuarioUnidadTecnicas, linea.getProducto().getUnidadTecnica())) {
                lienas.add(linea);
            }
        }
        return lienas;
    }

    /**
     * Este método retorna todas las lineas de del POG
     *
     * @return
     */
    public List<POLinea> getTodasLineas() {
        //filtra la lineas para las que solo tiene permiso de ver
        List<POLinea> lienas = new LinkedList();
        for (POLinea linea : objeto.getPog().getLineas()) {
            lienas.add(linea);
        }
        return lienas;
    }

    /**
     * Este método retorna el monto usado en el POG
     *
     * @return
     */
    public BigDecimal getMontoUsado() {
        BigDecimal usado = BigDecimal.ZERO;
        for (POLinea linea : objeto.getPog().getLineas()) {
            for (POActividadBase actividad : linea.getActividades()) {
                for (POInsumos insumo : actividad.getInsumos()) {
                    if (insumo.getMontoTotal() != null) {
                        usado = usado.add(insumo.getMontoTotal());
                    }
                }
            }
        }
        return usado;
    }

    /**
     * Este método retorna el saldo del POG
     *
     * @param total
     * @param usado
     * @return
     */
    public BigDecimal getSaldo(BigDecimal total, BigDecimal usado) {
        if (total == null) {
            total = BigDecimal.ZERO;
        }
        if (usado == null) {
            usado = BigDecimal.ZERO;
        }
        return total.subtract(usado);
    }

    /**
     * Este método retorna el monto usado por una fuente en el pog
     *
     * @param fuente
     * @return
     */
    public BigDecimal getMontoUsadoFuente(ProyectoFuente fuente) {
        BigDecimal usado = BigDecimal.ZERO;
        for (POLinea linea : objeto.getPog().getLineas()) {
            for (POActividadBase actividad : linea.getActividades()) {
                for (Iterator<POInsumos> it = actividad.getInsumos().iterator(); it.hasNext();) {
                    POGInsumo insumo = (POGInsumo) it.next();
                    for (POMontoFuenteInsumo montoAporte : insumo.getMontosFuentes()) {
                        if (montoAporte.getFuente().getFuenteRecursos().equals(fuente.getFuenteRecursos())) {
                            if (montoAporte.getMonto() != null) {
                                usado = usado.add(montoAporte.getMonto());
                            }
                        }
                    }
                }
            }
        }
        return usado;
    }

    /**
     * Este método retorna el monto usado por una categoría en el pog
     *
     * @param categoria
     * @return
     */
    public BigDecimal getMontoUsadoCategoria(ProyectoCategoriaConvenio categoria) {
        BigDecimal usado = BigDecimal.ZERO;
        for (POLinea linea : objeto.getPog().getLineas()) {
            for (POActividadBase actividad : linea.getActividades()) {
                for (Iterator<POInsumos> it = actividad.getInsumos().iterator(); it.hasNext();) {
                    POGInsumo insumo = (POGInsumo) it.next();
                    for (POMontoFuenteInsumo montoAporte : insumo.getMontosFuentes()) {
                        if (((ProyectoAporte) montoAporte.getFuente()).getCategoriaConvenio().equals(categoria.getCategoriaConvenio())) {
                            if (montoAporte.getMonto() != null) {
                                usado = usado.add(montoAporte.getMonto());
                            }
                        }
                    }
                }
            }
        }
        return usado;
    }

    /**
     * Este método arma la estructura de árbol para la distribución de montos de
     * una categoría. Solo se agregan las categorías que tienen al menos uno de
     * sus valores mayor a cero
     *
     * @return
     */
    public TreeNode getVerMontosDistribuccionCategorias() {
        TreeNode root = new DefaultTreeNode(new DataNodeDistribuccionCategorias(), null);
        root.setExpanded(true);

        for (ProyectoCategoriaConvenio cat : this.objeto.getDistribuccionCategorias()) {

            if (cat.getMonto() != null && cat.getMonto().compareTo(BigDecimal.ZERO) > 0
                    || getMontoUsadoCategoria(cat).compareTo(BigDecimal.ZERO) > 0
                    || getSaldo(cat.getMonto(), getMontoUsadoCategoria(cat)).compareTo(BigDecimal.ZERO) > 0) {

                DataNodeDistribuccionCategorias content = new DataNodeDistribuccionCategorias();
                content.setNombre(cat.getCategoriaConvenio().getNombre());
                content.setMonto(cat.getMonto());
                BigDecimal usado = getMontoUsadoCategoria(cat);
                content.setUsado(usado);
                content.setSaldo(getSaldo(cat.getMonto(), usado));

                TreeNode nodeCategoria = new DefaultTreeNode(content, root);
                nodeCategoria.setExpanded(true);

                if (cat.getCategoriaConvenio().getTipo() == TipoAporteProyecto.POR_TRAMOS) {
                    ProyectoUtils.ordenarAportesTramos(cat.getTramos(), false);
                    ProyectoAporteTramoTramo tramoAnterior = null;
                    for (ProyectoAporteTramoTramo tramo : cat.getTramos()) {
                        DataNodeDistribuccionCategorias contentTramo = new DataNodeDistribuccionCategorias();
                        contentTramo.setNombre(tramo.getMontoHasta().toString());
                        BigDecimal montoTotalTramo = tramo.getMontoHasta();
                        if (tramoAnterior != null) {
                            montoTotalTramo = montoTotalTramo.subtract(tramoAnterior.getMontoHasta());
                        }
                        contentTramo.setMonto(montoTotalTramo);
                        BigDecimal usadoTramo = POGUtils.getUsadoEnTramo(this.objeto.getPog(), tramo);
                        contentTramo.setUsado(usadoTramo);
                        contentTramo.setSaldo(getSaldo(montoTotalTramo, usadoTramo));

                        tramoAnterior = tramo;
                        TreeNode nodeTramo = new DefaultTreeNode(contentTramo, nodeCategoria);
                        nodeTramo.setExpanded(true);
                    }
                }
            }
        }

        return root;
    }

    /**
     * Este método retorna si esta habilitada la actividad según el año en
     * ejecución
     *
     * @param actividad
     * @return
     */
    public boolean habilitadoActividad(POActividadBase actividad) {
        if (actividad == null) {
            return true;
        }
        if (objeto == null || objeto.getPog() == null) {
            return true;
        }
        return POGUtils.habilitadoActividad(objeto.getPog(), actividad, anioEjecucion);
    }

    /**
     * Este método retorna si esta habilitado el insumo según el año en
     * ejecución
     *
     * @param insumo
     * @return
     */
    public boolean habilitadoInsumo(POInsumos insumo) {
        if (insumo == null) {
            return true;
        }
        if (objeto == null || objeto.getPog() == null) {
            return true;
        }
        return POGUtils.habilitadoInsumo(objeto.getPog(), (POGInsumo) insumo, anioEjecucion);
    }

    /**
     * Este método retorna si el año pasado por parámetro esta habilitado
     *
     * @param anio
     * @return
     */
    public boolean habilitadoAnio(Integer anio) {
        if (anio == null) {
            return false;
        }
        if (anioEjecucion == null) {
            return false;
        }
        return anio.compareTo(anioEjecucion.getAnio()) >= 0;
    }

    /**
     * Carga la lista de fuentes del proyecto que tienen al menos uno de sus
     * saldos mayor a cero
     *
     * @return
     */
    public void iniciarListaFuentesProyecto() {
        fuenteProyectoSaldoMayorQueCero = new LinkedList<>();
        for (ProyectoFuente proyectoFuente : objeto.getFuentesProyecto()) {
            if (proyectoFuente.getMonto() != null && proyectoFuente.getMonto().compareTo(BigDecimal.ZERO) > 0
                    || getMontoUsadoFuente(proyectoFuente).compareTo(BigDecimal.ONE) > 0
                    || getSaldo(proyectoFuente.getMonto(), getMontoUsadoFuente(proyectoFuente)).compareTo(BigDecimal.ONE) > 0) {

                fuenteProyectoSaldoMayorQueCero.add(proyectoFuente);

            }
        }
    }

//se sobre escriben los metodos que reducen las cosas de la unhidad tecnica
/*descomentar este fragmento si se quiere que parezcan todos los componentes y no solo los que le pertenecen al usuario
    y setar verTodosLosMontos = true
    @Override
    protected void addComponentesRecursivo(ProyectoComponente componente, Map<String, Integer> map, int nivel, boolean tienePadreUT) {

        tienePadreUT = true;
        String padding = "";
        for (int i = 0; i < nivel; i++) {
            padding = padding + "&nbsp;";
        }
        map.put(padding + componente.getNombre(), componente.getId());

        for (ProyectoComponente hijo : componente.getComponenteHijos()) {
            addComponentesRecursivo(hijo, map, nivel + 1, tienePadreUT);
        }

    }

    @Override
    public Map<String, Integer> getMacroActividadesProyecto() {
        Map<String, Integer> map = new LinkedHashMap();
        if (objeto != null) {
            for (ProyectoMacroActividad macroActividad : objeto.getProyectoMacroactividad()) {
                map.put(macroActividad.getMacroActividad().getNombre(), macroActividad.getId());
            }
        }
        return map;
    }
     */
// <editor-fold defaultstate="collapsed" desc="getter-setter">
    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public List<ProyectoFuente> getFuenteProyectoSaldoMayorQueCero() {
        return fuenteProyectoSaldoMayorQueCero;
    }

    public void setFuenteProyectoSaldoMayorQueCero(List<ProyectoFuente> fuenteProyectoSaldoMayorQueCero) {
        this.fuenteProyectoSaldoMayorQueCero = fuenteProyectoSaldoMayorQueCero;
    }

    // </editor-fold>
}
