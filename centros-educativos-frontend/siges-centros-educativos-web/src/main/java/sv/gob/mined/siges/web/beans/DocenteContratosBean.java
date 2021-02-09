/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgDatoContratacion;
import sv.gob.mined.siges.web.dto.SgDatoEmpleado;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativa;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgCargo;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoDatoContratacion;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgInstitucionPagaSalario;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.dto.catalogo.SgLeySalario;
import sv.gob.mined.siges.web.dto.catalogo.SgModoPago;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoAlfabetizador;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoContrato;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoInstitucionPaga;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoNombramiento;
import sv.gob.mined.siges.web.enumerados.EnumModeloContrato;
import sv.gob.mined.siges.web.enumerados.TipoPersonalSedeEducativa;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDatoContratacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DatoContratacionRestClient;
import sv.gob.mined.siges.web.restclient.DatoEmpleadoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class DocenteContratosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DocenteContratosBean.class.getName());

    @Inject
    private DatoEmpleadoRestClient restClient;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private CatalogosRestClient restCatalogo;

    @Inject
    private DatoContratacionRestClient restDatoContratacion;

    @Inject
    private SessionBean sessionBean;

    private SgDatoEmpleado entidadEnEdicion = new SgDatoEmpleado();
    private SgPersonalSedeEducativa personalSede;
    private SgDatoContratacion entidadEnEdicionContratos = new SgDatoContratacion();
    private FiltroCodiguera filtro = new FiltroCodiguera();
    private List<RevHistorico> historialNoticia = new ArrayList();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<SgCargo> comboCargo;
    private List<SgCargo> listCargo;
    private SofisComboG<SgTipoNombramiento> comboTipoNombramiento;
    private SofisComboG<SgJornadaLaboral> comboJornadaLaboral;
    private SofisComboG<SgTipoContrato> comboTipoContrato;
    private SofisComboG<SgLeySalario> comboLeySalario; //
    private SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamiento;
    private SofisComboG<SgInstitucionPagaSalario> comboInstitucionPagaSalario;
    private SofisComboG<SgTipoInstitucionPaga> comboTipoInstitucionPaga;
    private SofisComboG<SgModoPago> comboModoPago;
    private SofisComboG<SgTipoAlfabetizador> comboTipoAlfabetizador;
    private SgSede sedeSeleccionado;
    private Boolean soloLectura = Boolean.TRUE;
    private List<SgJornadaLaboral> jornadaLaboralList;
    private List<SgJornadaLaboral> jornadaLaboralDatosList;
    private SofisComboG<TipoPersonalSedeEducativa> comboTiposPersonalSedeEducativa;

    private List<SgDatoContratacion> listaAcuerdos = new ArrayList();
    private List<SgDatoContratacion> listaContratos = new ArrayList();
    private List<SgDatoContratacion> listaContratosOtros = new ArrayList();
    
    private SofisComboG<SgEstadoDatoContratacion> comboEstado;
    private List<EnumModeloContrato> modelosContrato;

    public DocenteContratosBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void personalSedeEducativa(SgPersonalSedeEducativa var) {
        try {
            personalSede = var;
            entidadEnEdicion = var.getPseDatoEmpleado();
            actualizarListas();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizarListas() {
        try {
            if (entidadEnEdicion != null && entidadEnEdicion.getDemPk() != null) {

                FiltroDatoContratacion filtro = new FiltroDatoContratacion();
                filtro.setDcoDatoEmpleado(entidadEnEdicion.getDemPk());
                filtro.setIncluirJornadasLaborales(Boolean.TRUE);
                entidadEnEdicion.setDemDatoContratacion(restDatoContratacion.buscar(filtro));

                listaAcuerdos = entidadEnEdicion.getDemDatoContratacion()
                        .stream()
                        .filter(c -> c.getDcoModeloContrato().equals(EnumModeloContrato.ACUERDO))
                        .collect(Collectors.toList());

                listaContratos = entidadEnEdicion.getDemDatoContratacion()
                        .stream()
                        .filter(c -> c.getDcoModeloContrato().equals(EnumModeloContrato.CONTRATO))
                        .collect(Collectors.toList());

                listaContratosOtros = entidadEnEdicion.getDemDatoContratacion()
                        .stream()
                        .filter(c -> c.getDcoModeloContrato().equals(EnumModeloContrato.OTRO))
                        .collect(Collectors.toList());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void validarAcceso() {
    }
    
    public Boolean verEditar(SgDatoContratacion dco) {
        if (dco.getDcoTipo()!= null) {
            switch (dco.getDcoTipo()) {
                case ADM:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_ADMINISTRATIVO)) {
                        return true;
                    }
                    break;
                case ATPI:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_ATPI)) {
                        return true;
                    }
                    break;
                case DOC:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_DOCENTE)) {
                        return true;
                    }
                    break;
                case DOF:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_DOCENTE_OTRAS_FUENTES)) {
                        return true;
                    }
                    break;
                case ALF:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_ALFABETIZADOR)) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }

    public Boolean verEliminar(SgDatoContratacion dco) {
        if (dco.getDcoTipo()!= null) {
            switch (dco.getDcoTipo()) {
                case ADM:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_ADMINISTRATIVO)) {
                        return true;
                    }
                    break;
                case ATPI:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_ATPI)) {
                        return true;
                    }
                    break;
                case DOC:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_DOCENTE)) {
                        return true;
                    }
                    break;
                case DOF:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_DOCENTE_OTRAS_FUENTES)) {
                        return true;
                    }
                    break;
                case ALF:
                    if (sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_ALFABETIZADOR)) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }
    
    public Boolean verEditarTipoPersonal(){
        return entidadEnEdicionContratos.getDcoPk() == null 
                || (sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_MODIFICAR_TIPO_PERSONAL_DATO_CONTRATACION) &&
                    comboTiposPersonalSedeEducativa.getAllTs().contains(entidadEnEdicionContratos.getDcoTipo()));
    }

    public void cargarCombos() {
        try {

            FiltroCodiguera filtroC = new FiltroCodiguera();
            filtroC.setHabilitado(Boolean.TRUE);

            filtroC.setOrderBy(new String[]{"carNombre"});
            filtroC.setIncluirCampos(new String[]{"carNombre", "carVersion","carAplicaAcuerdo","carAplicaContrato","carAplicaOtros"});
            filtroC.setAscending(new boolean[]{true});
            filtroC.setOrderBy(new String[]{"carNombre"});
            List<SgCargo> listaCargo = restCatalogo.buscarCargo(filtroC);
            comboCargo = new SofisComboG(listaCargo, "carNombre");
            comboCargo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            listCargo=new ArrayList(listaCargo);

            filtroC.setOrderBy(new String[]{"tnoNombre"});
            filtroC.setIncluirCampos(new String[]{"tnoNombre", "tnoVersion"});
            List<SgTipoNombramiento> listaTipoNombramiento = restCatalogo.buscarTipoNombramiento(filtroC);
            comboTipoNombramiento = new SofisComboG(listaTipoNombramiento, "tnoNombre");
            comboTipoNombramiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroC.setOrderBy(new String[]{"jlaNombre"});
            filtroC.setIncluirCampos(new String[]{"jlaNombre", "jlaVersion"});
            List<SgJornadaLaboral> listaJornadaLaboral = restCatalogo.buscarJornadasLaborales(filtroC);
            jornadaLaboralDatosList = listaJornadaLaboral;
            comboJornadaLaboral = new SofisComboG(listaJornadaLaboral, "jlaNombre");
            comboJornadaLaboral.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroC.setOrderBy(new String[]{"tcoNombre"});
            filtroC.setIncluirCampos(new String[]{"tcoNombre", "tcoVersion", "tcoEsInterinato", "tcoRequiereFechaHasta"});
            List<SgTipoContrato> listaTipoContrato = restCatalogo.buscarTipoContrato(filtroC);
            comboTipoContrato = new SofisComboG(listaTipoContrato, "tcoNombre");
            comboTipoContrato.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroC.setOrderBy(new String[]{"ffiNombre"});
            filtroC.setIncluirCampos(new String[]{"ffiNombre", "ffiVersion"});
            List<SgFuenteFinanciamiento> listaFuenteFinanciamiento = restCatalogo.buscarFuenteFinanciamiento(filtroC);
            comboFuenteFinanciamiento = new SofisComboG(listaFuenteFinanciamiento, "ffiNombre");
            comboFuenteFinanciamiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroC.setOrderBy(new String[]{"ipsNombre"});
            filtroC.setIncluirCampos(new String[]{"ipsCodigo", "ipsNombre", "ipsVersion"});
            List<SgInstitucionPagaSalario> listaInstitucionPagaSalario = restCatalogo.buscarInstitucionPagaSalario(filtroC);
            comboInstitucionPagaSalario = new SofisComboG(listaInstitucionPagaSalario, "ipsNombre");
            comboInstitucionPagaSalario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroC.setOrderBy(new String[]{"tipNombre"});
            filtroC.setIncluirCampos(new String[]{"tipNombre", "tipVersion"});
            List<SgTipoInstitucionPaga> listaTipoInstitucionPaga = restCatalogo.buscarTipoInstitucionPaga(filtroC);
            comboTipoInstitucionPaga = new SofisComboG(listaTipoInstitucionPaga, "tipNombre");
            comboTipoInstitucionPaga.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroC.setOrderBy(new String[]{"mpaNombre"});
            filtroC.setIncluirCampos(new String[]{"mpaNombre", "mpaVersion"});
            List<SgModoPago> listaModoPago = restCatalogo.buscarModoPago(filtroC);
            comboModoPago = new SofisComboG(listaModoPago, "mpaNombre");
            comboModoPago.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            filtroC.setOrderBy(new String[]{"edcNombre"});
            filtroC.setIncluirCampos(new String[]{"edcNombre", "edcVersion"});
            List<SgEstadoDatoContratacion> listaEstados = restCatalogo.buscarEstadoDatoContratacion(filtroC);
            comboEstado = new SofisComboG(listaEstados, "edcNombre");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            filtroC.setOrderBy(new String[]{"talNombre"});
            filtroC.setIncluirCampos(new String[]{"talNombre", "talVersion"});
            List<SgTipoAlfabetizador> listaAlfabetizador = restCatalogo.buscarTipoAlfabetizador(filtroC);
            comboTipoAlfabetizador = new SofisComboG(listaAlfabetizador, "talNombre");
            comboTipoAlfabetizador.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            
            modelosContrato = Arrays.asList(EnumModeloContrato.values());
            
            List<TipoPersonalSedeEducativa> listaTipoPersonas = new ArrayList();

            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_ADMINISTRATIVO)) {
                listaTipoPersonas.add(TipoPersonalSedeEducativa.ADM);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_ATPI)) {
                listaTipoPersonas.add(TipoPersonalSedeEducativa.ATPI);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_DOCENTE)) {
                listaTipoPersonas.add(TipoPersonalSedeEducativa.DOC);
            }
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_DOCENTE_OTRAS_FUENTES)) {
                listaTipoPersonas.add(TipoPersonalSedeEducativa.DOF);
            }
            
            if (sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_ALFABETIZADOR)) {
                listaTipoPersonas.add(TipoPersonalSedeEducativa.ALF);
            }
            
            comboTiposPersonalSedeEducativa = new SofisComboG(listaTipoPersonas, "text");
            comboTiposPersonalSedeEducativa.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));


        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    private void limpiarCombos() {

    }

    public void limpiarCombosContratacion() {
        //comboLeySalario.setSelected(-1);
        comboTiposPersonalSedeEducativa.setSelected(-1);
        limpiarCombosDependientesDeModeloContrato();
    }
    
    public void limpiarCombosDependientesDeModeloContrato(){
        comboCargo.setSelected(-1);
        comboTipoNombramiento.setSelected(-1);
        comboTipoContrato.setSelected(-1);
        comboFuenteFinanciamiento.setSelected(-1);
        comboInstitucionPagaSalario.setSelected(-1);
        comboTipoInstitucionPaga.setSelected(-1);
        comboJornadaLaboral.setSelected(-1);
        sedeSeleccionado = null;
        comboModoPago.setSelected(-1);
        comboEstado.setSelected(-1);
        comboTipoAlfabetizador.setSelected(-1);
    }

    public String limpiar() {
        filtro = new FiltroCodiguera();
        return null;
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getDemPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public String historial(Long id) {
        try {
            historialNoticia = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return restSede.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public List<SgCargo> buscarCargosCorrespondientes(EnumModeloContrato en){
        if(en!=null){
            switch(en){
                case ACUERDO: return listCargo.stream().filter(c->BooleanUtils.isNotFalse(c.getCarAplicaAcuerdo())).collect(Collectors.toList());
                case CONTRATO: return listCargo.stream().filter(c->BooleanUtils.isNotFalse(c.getCarAplicaContrato())).collect(Collectors.toList());
                case OTRO: return listCargo.stream().filter(c->BooleanUtils.isNotFalse(c.getCarAplicaOtros())).collect(Collectors.toList());
            }
        }
        return new ArrayList();
    }

    public void agregarAcuerdo() {
        jornadaLaboralList = new ArrayList();
        entidadEnEdicionContratos = new SgDatoContratacion();
        entidadEnEdicionContratos.setDcoModeloContrato(EnumModeloContrato.ACUERDO);
        limpiarCombosContratacion();

        comboCargo = new SofisComboG(buscarCargosCorrespondientes(EnumModeloContrato.ACUERDO), "carNombre");
        comboCargo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void agregarContrato() {
        jornadaLaboralList = new ArrayList();
        entidadEnEdicionContratos = new SgDatoContratacion();
        entidadEnEdicionContratos.setDcoModeloContrato(EnumModeloContrato.CONTRATO);
        limpiarCombosContratacion();
        
        comboCargo = new SofisComboG(buscarCargosCorrespondientes(EnumModeloContrato.CONTRATO), "carNombre");
        comboCargo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void agregarOtro() {
        jornadaLaboralList = new ArrayList();
        entidadEnEdicionContratos = new SgDatoContratacion();
        entidadEnEdicionContratos.setDcoModeloContrato(EnumModeloContrato.OTRO);
        limpiarCombosContratacion();
        comboCargo = new SofisComboG(buscarCargosCorrespondientes(EnumModeloContrato.OTRO), "carNombre");
        comboCargo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }
    
    public Boolean validarDatoContratacion(){
        try {
            return restDatoContratacion.validar(entidadEnEdicionContratos);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return Boolean.FALSE;
    }

    public void editarContrato(SgDatoContratacion var) {
        try {
            limpiarCombosContratacion();
            entidadEnEdicionContratos = restDatoContratacion.obtenerPorId(var.getDcoPk());
            comboCargo.setSelectedT(entidadEnEdicionContratos.getDcoCargo());
            comboTipoNombramiento.setSelectedT(entidadEnEdicionContratos.getDcoTipoNombramiento());
            comboTipoContrato.setSelectedT(entidadEnEdicionContratos.getDcoTipoContrato());
            comboFuenteFinanciamiento.setSelectedT(entidadEnEdicionContratos.getDcoFuenteFinanciamiento());
            comboInstitucionPagaSalario.setSelectedT(entidadEnEdicionContratos.getDcoInstitucionPagaSalario());
            comboTipoInstitucionPaga.setSelectedT(entidadEnEdicionContratos.getDcoTipoInstitucionPaga());
            jornadaLaboralList = entidadEnEdicionContratos.getDcoJornadasLaborales();
            sedeSeleccionado = entidadEnEdicionContratos.getDcoCentroEducativo();
            comboModoPago.setSelectedT(entidadEnEdicionContratos.getDcoModoPago());
            comboEstado.setSelectedT(entidadEnEdicionContratos.getDcoEstado());
            //comboLeySalario.setSelectedT(entidadEnEdicionContratos.getDcoLeySalario());
            comboTipoAlfabetizador.setSelectedT(entidadEnEdicionContratos.getDcoTipoAlfabetizador());
            comboTiposPersonalSedeEducativa.setSelectedT(entidadEnEdicionContratos.getDcoTipo());
            
            comboCargo = new SofisComboG(buscarCargosCorrespondientes(entidadEnEdicionContratos.getDcoModeloContrato()), "carNombre");
            comboCargo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));            
            comboCargo.setSelectedT(entidadEnEdicionContratos.getDcoCargo());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }
    
    public void seleccionarContrato(SgDatoContratacion var){
        entidadEnEdicionContratos = var;
    }

    public void eliminarContrato() {
        try {
            restDatoContratacion.eliminar(entidadEnEdicionContratos.getDcoPk());
            this.entidadEnEdicion.getDemDatoContratacion().remove(entidadEnEdicionContratos);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            actualizarListas();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void setTipoPersonal(){
        entidadEnEdicionContratos.setDcoTipo(this.comboTiposPersonalSedeEducativa.getSelectedT());
    }
    
    public void setearCombosEnEntidadContrato(){
        
        if (jornadaLaboralList != null) {
                entidadEnEdicionContratos.setDcoJornadasLaborales(jornadaLaboralList);
            }
            entidadEnEdicionContratos.setDcoCargo(comboCargo.getSelectedT());
            entidadEnEdicionContratos.setDcoFuenteFinanciamiento(comboFuenteFinanciamiento.getSelectedT());
            entidadEnEdicionContratos.setDcoInstitucionPagaSalario(comboInstitucionPagaSalario.getSelectedT());
            entidadEnEdicionContratos.setDcoTipoInstitucionPaga(comboTipoInstitucionPaga.getSelectedT());
            entidadEnEdicionContratos.setDcoTipoContrato(comboTipoContrato.getSelectedT());
            entidadEnEdicionContratos.setDcoJornadasLaborales(jornadaLaboralList);
            entidadEnEdicionContratos.setDcoCentroEducativo(sedeSeleccionado);
            entidadEnEdicionContratos.setDcoCargo(comboCargo.getSelectedT());
            entidadEnEdicionContratos.setDcoTipoNombramiento(comboTipoNombramiento.getSelectedT());
            entidadEnEdicionContratos.setDcoModoPago(comboModoPago.getSelectedT());
            entidadEnEdicionContratos.setDcoEstado(comboEstado!=null?comboEstado.getSelectedT():null);
            entidadEnEdicionContratos.setDcoTipoAlfabetizador(comboTipoAlfabetizador.getSelectedT());    
    }

    public void agregarContratoDatosEmpleados() {
        try {
            setearCombosEnEntidadContrato();    
            
            entidadEnEdicionContratos.setDcoDatoEmpleado(new SgDatoEmpleado(entidadEnEdicion.getDemPk(), entidadEnEdicion.getDemVersion()));
            entidadEnEdicionContratos.getDcoDatoEmpleado().setDemPersonalSede(new SgPersonalSedeEducativa(personalSede.getPsePk(), personalSede.getPseVersion()));
            
            SgDatoContratacion dc = restDatoContratacion.guardar(entidadEnEdicionContratos);
            entidadEnEdicionContratos.setDcoPk(dc.getDcoPk());
            entidadEnEdicionContratos.setDcoVersion(dc.getDcoVersion());

            if (entidadEnEdicion.getDemDatoContratacion() != null && entidadEnEdicion.getDemDatoContratacion().contains(entidadEnEdicionContratos)) {

                entidadEnEdicion.getDemDatoContratacion().set(entidadEnEdicion.getDemDatoContratacion().indexOf(entidadEnEdicionContratos), entidadEnEdicionContratos);
            } else {
                if (entidadEnEdicion.getDemDatoContratacion() == null) {
                    entidadEnEdicion.setDemDatoContratacion(new ArrayList());
                }
                entidadEnEdicion.getDemDatoContratacion().add(entidadEnEdicionContratos);
            }

            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            PrimeFaces.current().executeScript("PF('contratosDialog').hide()");
            actualizarListas();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgContrato", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError("popupmsgContrato", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public Boolean getSoloLecturaDatosContratacion() {
        if (entidadEnEdicionContratos != null && entidadEnEdicionContratos.getDcoModeloContrato() != null) {
            switch (entidadEnEdicionContratos.getDcoModeloContrato()) {
                case ACUERDO:
                    return this.soloLectura || (entidadEnEdicionContratos.getDcoPk() != null && !sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_EDITAR_ACUERDO));
                case CONTRATO:
                    return this.soloLectura || (entidadEnEdicionContratos.getDcoPk() != null && !sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_EDITAR_CONTRATO));
                case OTRO:
                    return this.soloLectura || (entidadEnEdicionContratos.getDcoPk() != null && !sessionBean.getOperaciones().contains(ConstantesOperaciones.PUEDE_EDITAR_CONTRATO_OTRO));
            }
        }
        return this.soloLectura;
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SofisComboG<SgEstadoDatoContratacion> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<SgEstadoDatoContratacion> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public List<RevHistorico> getHistorialNoticia() {
        return historialNoticia;
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public DatoEmpleadoRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(DatoEmpleadoRestClient restClient) {
        this.restClient = restClient;
    }

    public SgDatoEmpleado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgDatoEmpleado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public CatalogosRestClient getRestCatalogo() {
        return restCatalogo;
    }

    public void setRestCatalogo(CatalogosRestClient restCatalogo) {
        this.restCatalogo = restCatalogo;
    }

    public SgDatoContratacion getEntidadEnEdicionContratos() {
        return entidadEnEdicionContratos;
    }

    public void setEntidadEnEdicionContratos(SgDatoContratacion entidadEnEdicionContratos) {
        this.entidadEnEdicionContratos = entidadEnEdicionContratos;
    }

    public SgSede getSedeSeleccionado() {
        return sedeSeleccionado;
    }

    public void setSedeSeleccionado(SgSede sedeSeleccionado) {
        this.sedeSeleccionado = sedeSeleccionado;
    }

    public SedeRestClient getRestSede() {
        return restSede;
    }

    public void setRestSede(SedeRestClient restSede) {
        this.restSede = restSede;
    }

    public SofisComboG<SgCargo> getComboCargo() {
        return comboCargo;
    }

    public void setComboCargo(SofisComboG<SgCargo> comboCargo) {
        this.comboCargo = comboCargo;
    }

    public SofisComboG<SgJornadaLaboral> getComboJornadaLaboral() {
        return comboJornadaLaboral;
    }

    public void setComboJornadaLaboral(SofisComboG<SgJornadaLaboral> comboJornadaLaboral) {
        this.comboJornadaLaboral = comboJornadaLaboral;
    }

    public SofisComboG<SgTipoContrato> getComboTipoContrato() {
        return comboTipoContrato;
    }

    public void setComboTipoContrato(SofisComboG<SgTipoContrato> comboTipoContrato) {
        this.comboTipoContrato = comboTipoContrato;
    }

    public SofisComboG<SgLeySalario> getComboLeySalario() {
        return comboLeySalario;
    }

    public void setComboLeySalario(SofisComboG<SgLeySalario> comboLeySalario) {
        this.comboLeySalario = comboLeySalario;
    }

    public SofisComboG<SgFuenteFinanciamiento> getComboFuenteFinanciamiento() {
        return comboFuenteFinanciamiento;
    }

    public void setComboFuenteFinanciamiento(SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamiento) {
        this.comboFuenteFinanciamiento = comboFuenteFinanciamiento;
    }

    public SofisComboG<SgInstitucionPagaSalario> getComboInstitucionPagaSalario() {
        return comboInstitucionPagaSalario;
    }

    public void setComboInstitucionPagaSalario(SofisComboG<SgInstitucionPagaSalario> comboInstitucionPagaSalario) {
        this.comboInstitucionPagaSalario = comboInstitucionPagaSalario;
    }

    public SofisComboG<SgTipoInstitucionPaga> getComboTipoInstitucionPaga() {
        return comboTipoInstitucionPaga;
    }

    public void setComboTipoInstitucionPaga(SofisComboG<SgTipoInstitucionPaga> comboTipoInstitucionPaga) {
        this.comboTipoInstitucionPaga = comboTipoInstitucionPaga;
    }

    public SofisComboG<SgModoPago> getComboModoPago() {
        return comboModoPago;
    }

    public void setComboModoPago(SofisComboG<SgModoPago> comboModoPago) {
        this.comboModoPago = comboModoPago;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public List<SgJornadaLaboral> getJornadaLaboralList() {
        return jornadaLaboralList;
    }

    public void setJornadaLaboralList(List<SgJornadaLaboral> jornadaLaboralList) {
        this.jornadaLaboralList = jornadaLaboralList;
    }

    public List<SgJornadaLaboral> getJornadaLaboralDatosList() {
        return jornadaLaboralDatosList;
    }

    public void setJornadaLaboralDatosList(List<SgJornadaLaboral> jornadaLaboralDatosList) {
        this.jornadaLaboralDatosList = jornadaLaboralDatosList;
    }

    public SofisComboG<SgTipoNombramiento> getComboTipoNombramiento() {
        return comboTipoNombramiento;
    }

    public void setComboTipoNombramiento(SofisComboG<SgTipoNombramiento> comboTipoNombramiento) {
        this.comboTipoNombramiento = comboTipoNombramiento;
    }

    public List<SgDatoContratacion> getListaAcuerdos() {
        return listaAcuerdos;
    }

    public void setListaAcuerdos(List<SgDatoContratacion> listaAcuerdos) {
        this.listaAcuerdos = listaAcuerdos;
    }

    public List<SgDatoContratacion> getListaContratos() {
        return listaContratos;
    }

    public void setListaContratos(List<SgDatoContratacion> listaContratos) {
        this.listaContratos = listaContratos;
    }

    public SgPersonalSedeEducativa getPersonalSede() {
        return personalSede;
    }

    public void setPersonalSede(SgPersonalSedeEducativa personalSede) {
        this.personalSede = personalSede;
    }

    public List<SgDatoContratacion> getListaContratosOtros() {
        return listaContratosOtros;
    }

    public void setListaContratosOtros(List<SgDatoContratacion> listaContratosOtros) {
        this.listaContratosOtros = listaContratosOtros;
    }

    public SofisComboG<TipoPersonalSedeEducativa> getComboTiposPersonalSedeEducativa() {
        return comboTiposPersonalSedeEducativa;
    }

    public void setComboTiposPersonalSedeEducativa(SofisComboG<TipoPersonalSedeEducativa> comboTiposPersonalSedeEducativa) {
        this.comboTiposPersonalSedeEducativa = comboTiposPersonalSedeEducativa;
    }
    
    public SofisComboG<SgTipoAlfabetizador> getComboTipoAlfabetizador() {
        return comboTipoAlfabetizador;
    }

    public void setComboTipoAlfabetizador(SofisComboG<SgTipoAlfabetizador> comboTipoAlfabetizador) {
        this.comboTipoAlfabetizador = comboTipoAlfabetizador;
    }

    public List<EnumModeloContrato> getModelosContrato() {
        return modelosContrato;
    }

    public void setModelosContrato(List<EnumModeloContrato> modelosContrato) {
        this.modelosContrato = modelosContrato;
    }

    public List<SgCargo> getListCargo() {
        return listCargo;
    }

    public void setListCargo(List<SgCargo> listCargo) {
        this.listCargo = listCargo;
    }

    

    
}
