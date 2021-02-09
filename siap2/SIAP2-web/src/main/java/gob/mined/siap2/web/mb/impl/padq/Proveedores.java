/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl.padq;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicion;
import gob.mined.siap2.entities.data.impl.ProcesoAdquisicionProveedor;
import gob.mined.siap2.entities.data.impl.Proveedor;
import gob.mined.siap2.entities.enums.PasosProcesoAdquisicion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.datatypes.DataProveedorFile;
import gob.mined.siap2.web.delegates.impl.ProcesoAdquisicionDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.utils.ArchivoUtils;
import gob.mined.siap2.web.utils.dataProvider.EntityDataProvider;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import gob.mined.siap2.web.utils.dataProvider.ProveedoresLazyDataModel;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.UploadedFile;

/**
 * Esta clase implementa el backing bean de la sección proveedores del proceso
 * de adquisición.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "proveedores")
public class Proveedores implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    private JSFUtils jSFUtils;

    @Inject
    private ProcesoAdquisicionDelegate pAdqDelegate;

    @Inject
    TextMB textMB;

    @Inject
    private ReporteDelegate reporteDelegate;

    private List<Proveedor> proveedores = new ArrayList<>();
    private List<Proveedor> proveedoresSeleccionados = new ArrayList<>();
    private List<DataProveedorFile> proveedoresDelProceso = new ArrayList<>();
    private List<DataProveedorFile> proveedoresABorrar = new ArrayList<>();
    private List<ProcesoAdquisicionProveedor> proveedoresProcesoAEliminar = new ArrayList<>();
    private ProcesoAdqMain mainBean;
    private Integer procesoAdquisicionId;
    private String id;
    private String nombre;
    private String nit;
    private DataProveedorFile dataProveedorFile;
    private Boolean presionoFiltrar;
    private Boolean habilitarBotonGenerarInvitaciones;    
    private UploadedFile uploadedFile;

    private LazyDataModel lazyModelProveedores;

    @PostConstruct
    public void init() {
        mainBean = (ProcesoAdqMain) JSFUtils.getBean("procesoAdqMain");
        procesoAdquisicionId = mainBean.getObjeto().getId();
        proveedores = pAdqDelegate.obtenerProveedoresNoAsignadosAlproceso(procesoAdquisicionId);
        obtenerProveedoresDeAdquisicion();
        presionoFiltrar = false;
        this.habilitarGenerarInvitaciones();
    }

    /**
     * Si la lista de proveedores del proceso (guardados en la BD), tiene proveedores invitados, habilitó el botón.
     * Esto lo hago solamente al iniciar la vista y al guardar, ya que es cuándo trabajo con datos de la base
     */
    private void habilitarGenerarInvitaciones() {
        if (proveedoresDelProceso.isEmpty()) {
            setHabilitarBotonGenerarInvitaciones((Boolean) false);
        } else {
            boolean hayUnoInvitado = false;
            Iterator<DataProveedorFile> itProveedores = proveedoresDelProceso.iterator();
            while (itProveedores.hasNext() && !hayUnoInvitado) {
                ProcesoAdquisicionProveedor proveedor = itProveedores.next().getAdquisicionProveedor();
                if (proveedor.getInvitado()) {
                    hayUnoInvitado = true;
                }
            }
            setHabilitarBotonGenerarInvitaciones((Boolean) hayUnoInvitado);
        }
    }

    /**
     * Solo en el init la variable está en false, para que la lista al inicio se muestre vacía
     */
    public void presionoBtnFiltrar() {
        presionoFiltrar = true;
        initProveedoresList();
    }

    /**
     * Este método inicializa la lista de proveedores
     */
    public void initProveedoresList() {
        try {
            List<CriteriaTO> criterios = new ArrayList< >();
            if (!TextUtils.isEmpty(nombre)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nombreComercial", nombre);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(nit)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.CONTAINS, "nitOferente", nit);
                criterios.add(criterio);
            }
            if (!TextUtils.isEmpty(id) && TextUtils.isInteger(id)) {
                MatchCriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "idOferente", Integer.valueOf(id));
                criterios.add(criterio);
            } else if (!TextUtils.isEmpty(id) && !TextUtils.isInteger(id)) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERROR_ID_OFERENTE_INVALIDO);
                throw b;
            }

            CriteriaTO condicion = null;
            if (!criterios.isEmpty()) {
                if (criterios.size() == 1) {
                    condicion = criterios.get(0);
                } else {
                    condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
                }
            } else {
                // condición dummy para que el count by criteria funcione
                condicion = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.NOT_NULL, "id", 1);
            }

            String className = Proveedor.class.getName();
            String[] orderBy = {"nombreComercial"};
            boolean[] asc = {true};

            IDataProvider dataProvider = new EntityDataProvider(className, condicion, orderBy, asc);
            lazyModelProveedores = new ProveedoresLazyDataModel(dataProvider);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método corresponde al evento de agregar un proveedor seleccionado.
     */
    public void agregarProveedor() {
        try {
            if (proveedoresSeleccionados == null || proveedoresSeleccionados.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_PROVEEDOR_A_AGREGAR);
                throw b;
            }
            Integer provAgregados = 0;
            Integer provNoAgregados = 0;
            for (Proveedor proveedor : proveedoresSeleccionados) {
                boolean agregarProv = true;
                Iterator<DataProveedorFile> it = proveedoresDelProceso.iterator();
                while (it.hasNext() && agregarProv) {
                    DataProveedorFile provYaAgregado = it.next();
                    if (provYaAgregado.getAdquisicionProveedor().getProveedor().getId().equals(proveedor.getId())) {
                        agregarProv = false;
                        provNoAgregados += 1;
                    }
                }
                if (agregarProv) {
                    provAgregados += 1;
                    ProcesoAdquisicionProveedor proveedorProceso = new ProcesoAdquisicionProveedor();
                    proveedorProceso.setProveedor(proveedor);
                    DataProveedorFile dataProveedorFile = new DataProveedorFile();
                    dataProveedorFile.setAdquisicionProveedor(proveedorProceso);
                    dataProveedorFile.setIndex(proveedoresDelProceso.size());
                    proveedoresDelProceso.add(dataProveedorFile);
                }
            }

            proveedores.removeAll(proveedoresSeleccionados);
            this.proveedoresABorrar = new ArrayList<>();
            this.proveedoresSeleccionados = new ArrayList<>();

            //Para mostrar el mensaje adecuado en base a los proveedores que pudieron agregarse y los que no
            BusinessException b = new BusinessException();
            if (provAgregados == 0 && provNoAgregados == 1) {
                //El proveedor no pudo agregarse
                b.addError(ConstantesErrores.ERR_PROVEEDOR_YA_ESTABA_AGREGADO);
                throw b;
            } else if (provAgregados == 0 && provNoAgregados > 1) {
                //Los proveedores no se agregan porque estan asociados
                b.addError(ConstantesErrores.ERR_PROVEEDORES_YA_ESTABAN_AGREGADOS);
                throw b;
            } else if (provAgregados > 0 && provNoAgregados > 0) {
                //Algunos proveedores no pudieron agregarse (1 o más) pporque ya estaban asociados
                b.addError(ConstantesErrores.ERR_ALGUNOS_PROVEEDORES_NO_SE_AGREGARON_PORQUE_YA_ESTABAN_AGREGADOS);
                throw b;
            }

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }

    }

    /**
     * Este método corresponde al evento asociado a eliminar un proveedor seleccionado para eliminar.
     */
    public void eliminarProveedor() {
        try {
            if (proveedoresABorrar == null || proveedoresABorrar.isEmpty()) {
                BusinessException b = new BusinessException();
                b.addError(ConstantesErrores.ERR_DEBE_SELECCIONAR_PROVEEDOR_A_BORRAR);
                throw b;
            }
            for (DataProveedorFile dProveedorFile : proveedoresABorrar) {
                //Si el estado del proceso es Recepción de Ofertas y el proveedor a eliminar está invitado, no se podrá eliminar
                if (dProveedorFile.getAdquisicionProveedor().getInvitado() != null
                        && dProveedorFile.getAdquisicionProveedor().getInvitado()
                        && mainBean.getObjeto().getEstado() == PasosProcesoAdquisicion.RECEPCION_OFERTAS) {
                    BusinessException b = new BusinessException();
                    b.addError(ConstantesErrores.ERR_NO_SE_PUEDE_ELIMINAR_PROVEEDOR_INVITADO);
                    throw b;
                }
                if (dProveedorFile.getAdquisicionProveedor().getId() != null) {
                    if (!proveedoresProcesoAEliminar.contains(dProveedorFile.getAdquisicionProveedor())) {
                        proveedoresProcesoAEliminar.add(dProveedorFile.getAdquisicionProveedor());
                    }

                }
                if (!proveedores.contains(dProveedorFile.getAdquisicionProveedor().getProveedor())) {
                    proveedores.add(dProveedorFile.getAdquisicionProveedor().getProveedor());
                }

            }

            proveedoresDelProceso.removeAll(proveedoresABorrar);
            this.proveedoresABorrar = new ArrayList<>();
            this.proveedoresSeleccionados = new ArrayList<>();
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }

    /**
     * Este método carga lo datos de un proveedor para su visualización.
     * @param d 
     */
    public void cargarDataProveedor(DataProveedorFile d) {
        this.dataProveedorFile = d;
        uploadedFile = null;
    }

    /**
     * Este método se utiliza para cargar un archivo
     */
    public void fileUpload() {
        try {
            if (uploadedFile != null) {
                this.dataProveedorFile.setUploadFile(uploadedFile);
                this.dataProveedorFile.setArchivoNombre(uploadedFile.getFileName());
                this.proveedoresABorrar = new ArrayList<>();
                this.dataProveedorFile.setArchivo(null);
                uploadedFile = null;                
            }

            RequestContext.getCurrentInstance().execute("$('#anadirDocumento').modal('hide');");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método se utiliza para cargar un archivo
     * @param event 
     */
    public void fileUploadListener(FileUploadEvent event) {
        try {

            uploadedFile = event.getFile();

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Este método guarda los datos de un proveedor (botón Guardar).
     */
    public void guardar() {
        try {

            List<ProcesoAdquisicionProveedor> adquisicionProvedores = new ArrayList<>();
            for (DataProveedorFile dataProveedor : proveedoresDelProceso) {
                ProcesoAdquisicionProveedor adquisicionProveedor = dataProveedor.getAdquisicionProveedor();
                if (dataProveedor.getUploadFile() != null) {

                    try {
                        adquisicionProveedor.setTempUploadedFile(ArchivoUtils.getDataFile(dataProveedor.getUploadFile()));
                    } catch (IOException ex) {
                        Logger.getLogger(Proveedores.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                adquisicionProveedor.setInvitado(dataProveedor.isInvitado());
                adquisicionProvedores.add(adquisicionProveedor);
            }
            ProcesoAdquisicion proceso = mainBean.getObjeto();

            pAdqDelegate.guardarProveedores(proceso, adquisicionProvedores, proveedoresProcesoAEliminar);
            mainBean.reloadProceso();
            this.habilitarGenerarInvitaciones();
            deshacer();
            String texto = textMB.obtenerTexto("labels.CambiosGuardadoCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().scrollTo("tabsPanel");
        }
    }

    /**
     * Este método deshace una asignación.
     */
    public void deshacer() {
        proveedores = new ArrayList<>();
        proveedoresSeleccionados = new ArrayList<>();
        proveedoresDelProceso = new ArrayList<>();
        proveedoresABorrar = new ArrayList<>();
        proveedoresProcesoAEliminar = new ArrayList<>();

        proveedores = pAdqDelegate.obtenerProveedoresNoAsignadosAlproceso(procesoAdquisicionId);
        obtenerProveedoresDeAdquisicion();
    }

    /**
     * Este método obtiene los proveedores de adquisición de un proceso.
     */
    public void obtenerProveedoresDeAdquisicion() {
        proveedoresDelProceso = new ArrayList<>();
        proveedoresABorrar = new ArrayList<>();
        List<ProcesoAdquisicionProveedor> proveedoresAdquisicion = pAdqDelegate.obtenerProveedoresAdquisicionDelProceso(procesoAdquisicionId);
        int index = 0;
        for (ProcesoAdquisicionProveedor proveedorAdquisicion : proveedoresAdquisicion) {
            DataProveedorFile dataProveedorFile = new DataProveedorFile();
            dataProveedorFile.setAdquisicionProveedor(proveedorAdquisicion);
            dataProveedorFile.setIndex(index);
            dataProveedorFile.setInvitado(proveedorAdquisicion.getInvitado());
            if (proveedorAdquisicion.getArchivo() != null) {
                dataProveedorFile.setArchivoNombre(proveedorAdquisicion.getArchivo().getNombreOriginal());
                dataProveedorFile.setArchivo(proveedorAdquisicion.getArchivo());
            }
            proveedoresDelProceso.add(dataProveedorFile);
            index++;
        }
    }

    /**
     * Este método genera el archivo de invitación de los proveedores.
     */
    public void invitarProveedores() {
        byte[] bytespdf = reporteDelegate.generarInvitacionesProveedores(procesoAdquisicionId, mainBean.getObjeto().getObservaciones());
        ArchivoUtils.downloadPdfFromBytes(bytespdf, "InvitacionesProvedores.pdf");

        mainBean.reloadProceso();
    }

    /**
     * Borra los filtros de la consulta de proveedores
     */
    public void limparFiltros() {
        nombre = null;
        id = null;
        nit = null;
    }
    
    /**
     * Antes de generar invitaciones se realizan las validaciones necesarias
     */
    public void prepararGeneracionInvitaciones(){
        try {
            pAdqDelegate.verificarGeneracionInvitaciones(mainBean.getObjeto().getId());            
            RequestContext.getCurrentInstance().execute("$('#generarReporteInvitaciones').modal('show');"); 
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    public List<Proveedor> getProveedoresSeleccionados() {
        return proveedoresSeleccionados;
    }

    public void setProveedoresSeleccionados(List<Proveedor> proveedoresSeleccionados) {
        this.proveedoresSeleccionados = proveedoresSeleccionados;
    }

    public List<DataProveedorFile> getProveedoresDelProceso() {
        return proveedoresDelProceso;
    }

    public void setProveedoresDelProceso(List<DataProveedorFile> proveedoresDelProceso) {
        this.proveedoresDelProceso = proveedoresDelProceso;
    }

    public List<DataProveedorFile> getProveedoresABorrar() {
        return proveedoresABorrar;
    }

    public void setProveedoresABorrar(List<DataProveedorFile> proveedoresABorrar) {
        this.proveedoresABorrar = proveedoresABorrar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public DataProveedorFile getDataProveedorFile() {
        return dataProveedorFile;
    }

    public void setDataProveedorFile(DataProveedorFile dataProveedorFile) {
        this.dataProveedorFile = dataProveedorFile;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public Boolean getPresionoFiltrar() {
        return presionoFiltrar;
    }

    public void setPresionoFiltrar(Boolean presionoFiltrar) {
        this.presionoFiltrar = presionoFiltrar;
    }

    public LazyDataModel getLazyModelProveedores() {
        return lazyModelProveedores;
    }

    public void setLazyModelProveedores(LazyDataModel lazyModelProveedores) {
        this.lazyModelProveedores = lazyModelProveedores;
    }

    public Boolean getHabilitarBotonGenerarInvitaciones() {
        return habilitarBotonGenerarInvitaciones;
    }

    public void setHabilitarBotonGenerarInvitaciones(Boolean habilitarBotonGenerarInvitaciones) {
        this.habilitarBotonGenerarInvitaciones = habilitarBotonGenerarInvitaciones;
    }
  // </editor-fold>
}
