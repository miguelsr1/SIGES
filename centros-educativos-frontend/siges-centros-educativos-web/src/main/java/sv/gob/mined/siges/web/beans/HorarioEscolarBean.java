/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgCeldaDiaHora;
import sv.gob.mined.siges.web.dto.SgComponentePlanEstudio;
import sv.gob.mined.siges.web.dto.SgComponentePlanGrado;
import sv.gob.mined.siges.web.dto.SgHorarioEscolar;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumCeldaDiaHora;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCeldaDiaHora;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroHorarioEscolar;
import sv.gob.mined.siges.web.lazymodels.LazyHorarioEscolarDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CeldaDiaHoraRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanGradoRestClient;
import sv.gob.mined.siges.web.restclient.HorarioEscolarRestClient;
import sv.gob.mined.siges.web.restclient.SeccionRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgComponenteDocente;
import sv.gob.mined.siges.web.dto.SgDiaLectivoHorarioEscolar;
import sv.gob.mined.siges.web.dto.SgPersonalSedeEducativaNoJsonIdentity;
import sv.gob.mined.siges.web.dto.catalogo.SgConfiguracion;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiaLectivoHorarioEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonalSedeEducativa;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DiaLectivoHorarioEscolarRestClient;
import sv.gob.mined.siges.web.restclient.PersonalSedeEducativaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class HorarioEscolarBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(HorarioEscolarBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long HorarioEscolarId;

    @Inject
    @Param(name = "seccionId")
    private Long seccionId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    private HorarioEscolarRestClient restClient;

    @Inject
    private SeccionRestClient seccionRestClient;

    @Inject
    private ComponentePlanGradoRestClient componentePlanGradoRestClient;

    @Inject
    private CeldaDiaHoraRestClient celdaDiaHoraRestClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private PersonalSedeEducativaRestClient restPersonalSede;

    @Inject
    private ActividadesEspecialesComponenteBean actividadEspecialComponenteBean;
    
    @Inject
    private AsignacionDocenteComponenteBean asignacionDocenteComponente;
    
   @Inject
    private DiaLectivoHorarioEscolarRestClient diaLectivoHorarioEscolarRestClient;
   
   @Inject
    private CatalogosRestClient restCatalogo;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgHorarioEscolar entidadEnEdicion = new SgHorarioEscolar();
    private List<RevHistorico> historialHorarioEscolar = new ArrayList();
    private List<SgHorarioEscolar> listHorarioEscolar = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyHorarioEscolarDataModel horarioEscolarLazyModel;

    private SofisComboG<SgComponentePlanEstudio>[][] matrizComboComponentePlanEstudio;
    private SgSede sedeSeleccionada;
    private List<SgComponentePlanGrado> listCompPlanGrado;
    private List<SgComponentePlanEstudio> listCompPlanEstudio;
    private Integer cantidadFilas;
    private List<EnumCeldaDiaHora> listaDias;
    private List<Integer> listaCantidad;
    private Boolean habilitarBoton = Boolean.TRUE;
    private Boolean soloLectura = Boolean.FALSE;
    private SgSeccion seccion;
    private SofisComboG<SgPersonalSedeEducativaNoJsonIdentity> comboDocentes;
    private List<SgPersonalSedeEducativaNoJsonIdentity> listaDocentes;
    private SofisComboG<SgPersonalSedeEducativaNoJsonIdentity>[] arrayComboPersonal;
    private List<SgCeldaDiaHora> listCeldaDiaHora;
    private Integer selectedTab;
    private List<SgDiaLectivoHorarioEscolar> diaLectivoHorarioEscolarList;
    private List<Boolean> checkDiaLectivo = new ArrayList();
    private Boolean[] celdaDiaHoraHabilitado;
    private Boolean habilitadoDiaLectivo=Boolean.FALSE;

    public HorarioEscolarBean() {
    }

    @PostConstruct
    public void init() {
        try {
            habilitadoFuncionalidadDiaLectivo();
            if (HorarioEscolarId != null && HorarioEscolarId > 0) {
                this.actualizar(restClient.obtenerPorId(HorarioEscolarId));
                soloLectura = editable != null ? !editable : soloLectura;
            } else if (seccionId != null && seccionId > 0) {
                agregarHorarioEscolar(seccionRestClient.obtenerPorId(seccionId));
            }
            validarAcceso();
            
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }
    
    public void habilitadoFuncionalidadDiaLectivo(){
        try{
            habilitadoDiaLectivo=Boolean.FALSE;
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setCodigo(Constantes.HABILITAR_CONTROL_DIA_LECTIVO);
            List<SgConfiguracion> confList = restCatalogo.buscarConfiguracion(fc);
            if(!confList.isEmpty()){
                SgConfiguracion conf=confList.get(0);
                if(conf.getConValor().equals("1")){
                    habilitadoDiaLectivo=Boolean.TRUE;
                }
            }
            
        }catch(Exception exp){
            
        }
    }

    public void cargarDocentes() {
        try {
            FiltroPersonalSedeEducativa fps = new FiltroPersonalSedeEducativa();
            fps.setPerCentroEducativo(entidadEnEdicion.getHesSeccion().getSecServicioEducativo().getSduSede().getSedPk());
            fps.setDocenteOActividadDocente(Boolean.TRUE);
            fps.setPersonalActivo(Boolean.TRUE);
            fps.setOrderBy(new String[]{"psePersona.perPrimerApellido", "psePersona.perSegundoApellido", "psePersona.perTercerApellido",
                "psePersona.perPrimerNombre", "psePersona.perSegundoNombre", "psePersona.perTercerNombre"});
            fps.setAscending(new boolean[]{true, true, true, true, true, true});
            fps.setIncluirCampos(new String[]{"psePk", "psePersona.perPk", "psePersona.perVersion",
                "psePersona.perPrimerNombre", "psePersona.perSegundoNombre", "psePersona.perTercerNombre",
                "psePersona.perPrimerApellido", "psePersona.perSegundoApellido", "psePersona.perTercerApellido", "psePersona.perDui", "pseVersion"});

            listaDocentes = new ArrayList<>(restPersonalSede.buscarLite(fps));

            comboDocentes = new SofisComboG(listaDocentes, "pseDuiNombreCompleto");
            comboDocentes.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            cargarListaComponentes();

            if (BooleanUtils.isTrue(entidadEnEdicion.getHesUnicoDocente())) {
                comboDocentes.setSelectedT(entidadEnEdicion.getHesDocente());
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public String getTituloPagina() {
        if (this.HorarioEscolarId == null) {
            return Etiquetas.getValue("nuevoHorarioEscolar");
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verHorarioEscolar");
        } else {
            return Etiquetas.getValue("edicionHorarioEscolar");
        }
    }

    public void validarAcceso() {
        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_HORARIOS_ESCOLARES)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (entidadEnEdicion.getHesPk() == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_HORARIOS_ESCOLARES)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_HORARIOS_ESCOLARES)) {
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public void buscar() {
        try {

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarTabla() {
        listaDias = new ArrayList(Arrays.asList(EnumCeldaDiaHora.values()));
        listaCantidad = new ArrayList();
        for (int i = 0; i < cantidadFilas; i++) {
            listaCantidad.add(i);
        }
    }

    public void actualizar(SgHorarioEscolar var) {
        entidadEnEdicion = var;
        agregarHorarioEscolar(var.getHesSeccion());
        JSFUtils.limpiarMensajesError();
    }
    
    public void seleccionarSeccion(SgSeccion seccion){
        listCompPlanEstudio = new ArrayList<>();
        entidadEnEdicion=new SgHorarioEscolar();
        if (seccion == null) {                
            return;
        }
        agregarHorarioEscolar(seccion);
        
    }
    
    public void seleccionCeldaDiaHora(Integer Hora,Integer Dia){
        SgComponentePlanEstudio cpe=matrizComboComponentePlanEstudio[Hora][Dia].getSelectedT();
        if(cpe!=null){
            SgComponentePlanGrado comp=new SgComponentePlanGrado();
            componentePlanGrado:
            for (SgComponentePlanGrado cpg : listCompPlanGrado) {
                if (cpg.getCpgGrado().equals(entidadEnEdicion.getHesSeccion().getSecServicioEducativo().getSduGrado())
                        && cpg.getCpgPlanEstudio().equals(entidadEnEdicion.getHesSeccion().getSecPlanEstudio())
                        && cpg.getCpgComponentePlanEstudio().equals(cpe)) {
                    comp=cpg;
                    break componentePlanGrado;
                }
            }
            
            Boolean encontro=Boolean.FALSE;
            celdaDiaHora:
            for(SgCeldaDiaHora cdh:listCeldaDiaHora){
                if(cdh.getCdhDia().equals(listaDias.get(Dia)) && cdh.getHesHora().equals(Hora)){
                    if(cdh.getCdhComponentePlanGrado()!=null){
                        if(!cdh.getCdhComponentePlanGrado().getCpgPk().equals(comp.getCpgPk())){
                            cdh.setCdhComponentePlanGrado(comp);                        
                        }
                    }else{
                        cdh.setCdhComponentePlanGrado(comp);    
                    }
                    encontro=Boolean.TRUE;
                    break celdaDiaHora;
                }
            }
            if(! encontro){
                SgCeldaDiaHora celdaDiaHora=new SgCeldaDiaHora();
                celdaDiaHora.setCdhDia(listaDias.get(Dia));
                celdaDiaHora.setHesHora(Hora);
                celdaDiaHora.setCdhComponentePlanGrado(comp);
                listCeldaDiaHora.add(celdaDiaHora);
            }
        }else{
            for(SgCeldaDiaHora cdh:listCeldaDiaHora){
                if(cdh.getCdhDia().equals(listaDias.get(Dia)) && cdh.getHesHora().equals(Hora)){
                    cdh.setCdhComponentePlanGrado(null);
                }
            }
        }
       
        
    }

    public void agregarHorarioEscolar(SgSeccion seccion) {
        try {
            listCeldaDiaHora=new ArrayList<>();
            listCompPlanEstudio = new ArrayList<>();
            if (seccion == null) {
                return;
            }

            if (entidadEnEdicion.getHesPk() == null) {
                FiltroHorarioEscolar fhe = new FiltroHorarioEscolar();
                fhe.setHesSeccion(seccion.getSecPk());
                List<SgHorarioEscolar> listHorario = restClient.buscar(fhe);
                entidadEnEdicion = !listHorario.isEmpty() ? listHorario.get(0) : new SgHorarioEscolar();               
             }

            entidadEnEdicion.setHesSeccion(seccion);
            cantidadFilas = entidadEnEdicion.getHesPk() != null ? entidadEnEdicion.getHesCantidad() : 5;
            cargarTabla();

            FiltroComponentePlanGrado filtro = new FiltroComponentePlanGrado();
            if (seccion.getSecPlanEstudio() == null) {
                JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_SECCION_SIN_PLAN_ESTUDIO), "");
                return;
            }

            filtro.setCpgGradoPk(seccion.getSecServicioEducativo().getSduGrado().getGraPk());
            filtro.setCpgPlanEstudioPk(seccion.getSecPlanEstudio().getPesPk());
            filtro.setCpgAgregandoSeccionExclusiva(entidadEnEdicion.getHesSeccion().getSecPk());
            listCompPlanGrado = componentePlanGradoRestClient.buscar(filtro);
            listCompPlanEstudio.addAll(listCompPlanGrado.stream().map(cpg -> cpg.getCpgComponentePlanEstudio()).collect(Collectors.toList()));
            listCompPlanEstudio.sort(Comparator.comparing(SgComponentePlanEstudio::getCpeNombre));
            matrizCompPlanEstCargar();

            if (entidadEnEdicion.getHesPk() != null) {
                cantidadFilas = entidadEnEdicion.getHesCantidad();
                FiltroCeldaDiaHora fcdh = new FiltroCeldaDiaHora();
                fcdh.setCdhHorarioEscolar(entidadEnEdicion.getHesPk());
                fcdh.setIncluirCampos(new String[]{"cdhDia", "hesHora", "cdhVersion","cdhComponentePlanGrado.cpgPk","cdhComponentePlanGrado.cpgVersion","cdhComponentePlanGrado.cpgComponentePlanEstudio.cpePk","cdhComponentePlanGrado.cpgComponentePlanEstudio.cpeVersion","cdhComponentePlanGrado.cpgComponentePlanEstudio.cpeNombre","cdhComponentePlanGrado.cpgComponentePlanEstudio.cpeTipo"});
                listCeldaDiaHora= new ArrayList<>(celdaDiaHoraRestClient.buscar(fcdh));
                for (SgCeldaDiaHora celda : listCeldaDiaHora) {
                    matrizComboComponentePlanEstudio[celda.getHesHora()][celda.getCdhDia().ordinal()].setSelectedT(celda.getCdhComponentePlanGrado().getCpgComponentePlanEstudio());
                }
            } else {
                entidadEnEdicion.setHesCantidad(cantidadFilas);
            }

            cargarDocentes();
            actividadEspecialComponenteBean.seleccionarSeccion(entidadEnEdicion.getHesSeccion());
            if(entidadEnEdicion.getHesPk()!=null){
                asignacionDocenteComponente.setHorarioEscolarEnEdicion(entidadEnEdicion);
                asignacionDocenteComponente.setCompPlanEstudioList(listCompPlanEstudio);
                asignacionDocenteComponente.cargarDatos();
            }
            cargarDiaLectivo();
            selectedTab=0;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarDiaLectivo(){
        try{
            diaLectivoHorarioEscolarList=new ArrayList();
            checkDiaLectivo=new ArrayList();
            celdaDiaHoraHabilitado=new Boolean[7];
            Arrays.fill(celdaDiaHoraHabilitado, Boolean.TRUE);

            for(int i=0;i<7;i++){
                checkDiaLectivo.add(Boolean.TRUE);
            }
            
            if(habilitadoDiaLectivo){               

                if(entidadEnEdicion.getHesPk()!=null){
                    FiltroDiaLectivoHorarioEscolar fdl=new FiltroDiaLectivoHorarioEscolar();
                    fdl.setHorarioEscolarFk(entidadEnEdicion.getHesPk());
                    diaLectivoHorarioEscolarList=new ArrayList(diaLectivoHorarioEscolarRestClient.buscar(fdl));
                    if(!diaLectivoHorarioEscolarList.isEmpty()){
                        List<EnumCeldaDiaHora> dias= new ArrayList(Arrays.asList(EnumCeldaDiaHora.values()));
                         for(EnumCeldaDiaHora dia:dias){
                             SgDiaLectivoHorarioEscolar diaHora=diaLectivoHorarioEscolarList.stream().filter(c->c.getDlhDia().equals(dia)).collect(Collectors.toList()).get(0);
                             checkDiaLectivo.set(dias.indexOf(dia),diaHora.getDlhEsLectivo());
                             celdaDiaHoraHabilitado[EnumCeldaDiaHora.numero(dia)]=diaHora.getDlhEsLectivo();
                         }
                    }
                }

                if(diaLectivoHorarioEscolarList.isEmpty()){
                     List<EnumCeldaDiaHora> dias= new ArrayList(Arrays.asList(EnumCeldaDiaHora.values()));
                     for(EnumCeldaDiaHora dia:dias){
                         SgDiaLectivoHorarioEscolar diaLectivo=new SgDiaLectivoHorarioEscolar();
                         diaLectivo.setDlhDia(dia);
                         diaLectivoHorarioEscolarList.add(diaLectivo);
                     }
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void seleccionarDiaLectivo(Integer colIndex){
       
        celdaDiaHoraHabilitado[colIndex]=checkDiaLectivo.get(colIndex);
        if(checkDiaLectivo.get(colIndex).equals(Boolean.FALSE)){
            for(Integer i=0;i<cantidadFilas;i++){
                matrizComboComponentePlanEstudio[i][colIndex].setSelected(-1);
            }
            
        }
        EnumCeldaDiaHora celdaDiaHora=listaDias.get(colIndex);
        SgDiaLectivoHorarioEscolar dia=diaLectivoHorarioEscolarList.stream().filter(c->c.getDlhDia().equals(celdaDiaHora)).collect(Collectors.toList()).get(0);
        dia.setDlhEsLectivo(checkDiaLectivo.get(colIndex));
    }
    


    public void matrizCompPlanEstCargar() {
        if (!listCompPlanEstudio.isEmpty()) {
            matrizComboComponentePlanEstudio = new SofisComboG[cantidadFilas][7];
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < this.cantidadFilas; j++) {
                    matrizComboComponentePlanEstudio[j][i] = new SofisComboG(listCompPlanEstudio, "cpeNombre");
                    matrizComboComponentePlanEstudio[j][i].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                }

            }
        }
    }

    public void cargarListaComponentes() {
        if (!listCompPlanEstudio.isEmpty()) {
            arrayComboPersonal = new SofisComboG[listCompPlanEstudio.size()];
            for (int i = 0; i < listCompPlanEstudio.size(); i++) {
                arrayComboPersonal[i] = new SofisComboG(listaDocentes, "pseDuiNombreCompleto");
                arrayComboPersonal[i].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        }

        if (BooleanUtils.isFalse(entidadEnEdicion.getHesUnicoDocente()) && entidadEnEdicion.getHesDocentes() != null) {
            for (SgComponenteDocente doc : entidadEnEdicion.getHesDocentes()) {
                forListaComp:
                for (int cont = 0; cont < listCompPlanEstudio.size(); cont++) {
                    if (doc.getCdoComponente().equals(listCompPlanEstudio.get(cont))) {
                        if (doc.getCdoDocente() != null) {
                            arrayComboPersonal[cont].setSelected(doc.getCdoDocente().hashCode());
                        }
                    }
                }
            }
        }
    }

    public void cambiarNumeroFilas() {
        try {
            cargarTabla();
            matrizCompPlanEstCargar();
            if (entidadEnEdicion.getHesPk() != null) {
                FiltroCeldaDiaHora fcdh = new FiltroCeldaDiaHora();
                fcdh.setCdhHorarioEscolar(entidadEnEdicion.getHesPk());
                List<SgCeldaDiaHora> listCeldas = celdaDiaHoraRestClient.buscar(fcdh);
                for (SgCeldaDiaHora celda : listCeldas) {
                    if (celda.getHesHora() < cantidadFilas) {
                        matrizComboComponentePlanEstudio[celda.getHesHora()][celda.getCdhDia().ordinal()].setSelectedT(celda.getCdhComponentePlanGrado().getCpgComponentePlanEstudio());
                    }
                }
            }
            entidadEnEdicion.setHesCantidad(cantidadFilas);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void guardar() {
        try {
            if (this.matrizComboComponentePlanEstudio.length != this.cantidadFilas) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_FILAS_HORARIO), "");
            } else {
                listCeldaDiaHora.removeIf(c->c.getHesHora() >= cantidadFilas || c.getCdhComponentePlanGrado()==null);
                entidadEnEdicion.setHesCeldasDiaHora(listCeldaDiaHora);
                
                SgPersonalSedeEducativaNoJsonIdentity personalAntesDeGuardar=entidadEnEdicion.getHesDocente();//En backend la sección es lazy por lo que luego de guardar vuelve null.
                if(!entidadEnEdicion.getHesUnicoDocente() && entidadEnEdicion.getHesDocente()!=null){
                    entidadEnEdicion.setHesUnicoDocente(Boolean.TRUE);
                }

                Boolean elementoNuevo=entidadEnEdicion.getHesPk()==null;
                SgSeccion secBeforeSave = entidadEnEdicion.getHesSeccion(); //En backend la sección es lazy por lo que luego de guardar vuelve null.
               
                       
                if(!diaLectivoHorarioEscolarList.isEmpty()){
          
                    entidadEnEdicion.setHesDiasLectivos(diaLectivoHorarioEscolarList);
            
                }
                
                entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                entidadEnEdicion.setHesSeccion(secBeforeSave);
                entidadEnEdicion.setHesDocente(personalAntesDeGuardar);
                FiltroCeldaDiaHora fcdh = new FiltroCeldaDiaHora();
                fcdh.setIncluirCampos(new String[]{"cdhDia", "hesHora", "cdhVersion","cdhComponentePlanGrado.cpgPk","cdhComponentePlanGrado.cpgVersion","cdhComponentePlanGrado.cpgComponentePlanEstudio.cpePk","cdhComponentePlanGrado.cpgComponentePlanEstudio.cpeVersion","cdhComponentePlanGrado.cpgComponentePlanEstudio.cpeNombre","cdhComponentePlanGrado.cpgComponentePlanEstudio.cpeTipo"});
                fcdh.setCdhHorarioEscolar(entidadEnEdicion.getHesPk());
                listCeldaDiaHora= new ArrayList<>(celdaDiaHoraRestClient.buscar(fcdh));
                cargarListaComponentes();
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                if(elementoNuevo){
                    asignacionDocenteComponente.setHorarioEscolarEnEdicion(entidadEnEdicion);
                    asignacionDocenteComponente.setCompPlanEstudioList(listCompPlanEstudio);
                    asignacionDocenteComponente.cargarDatos();
                }
                cargarDiaLectivo();

            }
        }catch (IllegalArgumentException ie){
            LOGGER.log(Level.SEVERE, "error "+ie.getLocalizedMessage());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getHesPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialHorarioEscolar = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgHorarioEscolar getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgHorarioEscolar entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public HorarioEscolarRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(HorarioEscolarRestClient restClient) {
        this.restClient = restClient;
    }

    public SeccionRestClient getSeccionRestClient() {
        return seccionRestClient;
    }

    public void setSeccionRestClient(SeccionRestClient seccionRestClient) {
        this.seccionRestClient = seccionRestClient;
    }

    public ComponentePlanGradoRestClient getComponentePlanGradoRestClient() {
        return componentePlanGradoRestClient;
    }

    public void setComponentePlanGradoRestClient(ComponentePlanGradoRestClient componentePlanGradoRestClient) {
        this.componentePlanGradoRestClient = componentePlanGradoRestClient;
    }

    public CeldaDiaHoraRestClient getCeldaDiaHoraRestClient() {
        return celdaDiaHoraRestClient;
    }

    public void setCeldaDiaHoraRestClient(CeldaDiaHoraRestClient celdaDiaHoraRestClient) {
        this.celdaDiaHoraRestClient = celdaDiaHoraRestClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public List<RevHistorico> getHistorialHorarioEscolar() {
        return historialHorarioEscolar;
    }

    public void setHistorialHorarioEscolar(List<RevHistorico> historialHorarioEscolar) {
        this.historialHorarioEscolar = historialHorarioEscolar;
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

    public LazyHorarioEscolarDataModel getHorarioEscolarLazyModel() {
        return horarioEscolarLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyHorarioEscolarDataModel horarioEscolarLazyModel) {
        this.horarioEscolarLazyModel = horarioEscolarLazyModel;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public List<SgComponentePlanGrado> getListCompPlanGrado() {
        return listCompPlanGrado;
    }

    public void setListCompPlanGrado(List<SgComponentePlanGrado> listCompPlanGrado) {
        this.listCompPlanGrado = listCompPlanGrado;
    }

    public Integer getCantidadFilas() {
        return cantidadFilas;
    }

    public void setCantidadFilas(Integer cantidadFilas) {
        this.cantidadFilas = cantidadFilas;
    }

    public SofisComboG<SgComponentePlanEstudio>[][] getMatrizComboComponentePlanEstudio() {
        return matrizComboComponentePlanEstudio;
    }

    public void setMatrizComboComponentePlanEstudio(SofisComboG<SgComponentePlanEstudio>[][] matrizComboComponentePlanEstudio) {
        this.matrizComboComponentePlanEstudio = matrizComboComponentePlanEstudio;
    }

    public List<SgComponentePlanEstudio> getListCompPlanEstudio() {
        return listCompPlanEstudio;
    }

    public void setListCompPlanEstudio(List<SgComponentePlanEstudio> listCompPlanEstudio) {
        this.listCompPlanEstudio = listCompPlanEstudio;
    }

    public List<EnumCeldaDiaHora> getListaDias() {
        return listaDias;
    }

    public void setListaDias(List<EnumCeldaDiaHora> listaDias) {
        this.listaDias = listaDias;
    }

    public List<Integer> getListaCantidad() {
        return listaCantidad;
    }

    public void setListaCantidad(List<Integer> listaCantidad) {
        this.listaCantidad = listaCantidad;
    }

    public List<SgHorarioEscolar> getListHorarioEscolar() {
        return listHorarioEscolar;
    }

    public void setListHorarioEscolar(List<SgHorarioEscolar> listHorarioEscolar) {
        this.listHorarioEscolar = listHorarioEscolar;
    }

    public Boolean getHabilitarBoton() {
        return habilitarBoton;
    }

    public void setHabilitarBoton(Boolean habilitarBoton) {
        this.habilitarBoton = habilitarBoton;
    }

    public Long getHorarioEscolarId() {
        return HorarioEscolarId;
    }

    public void setHorarioEscolarId(Long HorarioEscolarId) {
        this.HorarioEscolarId = HorarioEscolarId;
    }

    public Long getSeccionId() {
        return seccionId;
    }

    public void setSeccionId(Long seccionId) {
        this.seccionId = seccionId;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgSeccion getSeccion() {
        return seccion;
    }

    public void setSeccion(SgSeccion seccion) {
        this.seccion = seccion;
    }

    public SofisComboG<SgPersonalSedeEducativaNoJsonIdentity> getComboDocentes() {
        return comboDocentes;
    }

    public void setComboDocentes(SofisComboG<SgPersonalSedeEducativaNoJsonIdentity> comboDocentes) {
        this.comboDocentes = comboDocentes;
    }

    public List<SgPersonalSedeEducativaNoJsonIdentity> getListaDocentes() {
        return listaDocentes;
    }

    public void setListaDocentes(List<SgPersonalSedeEducativaNoJsonIdentity> listaDocentes) {
        this.listaDocentes = listaDocentes;
    }

    public SofisComboG<SgPersonalSedeEducativaNoJsonIdentity>[] getArrayComboPersonal() {
        return arrayComboPersonal;
    }

    public void setArrayComboPersonal(SofisComboG<SgPersonalSedeEducativaNoJsonIdentity>[] arrayComboPersonal) {
        this.arrayComboPersonal = arrayComboPersonal;
    }

    public List<SgCeldaDiaHora> getListCeldaDiaHora() {
        return listCeldaDiaHora;
    }

    public void setListCeldaDiaHora(List<SgCeldaDiaHora> listCeldaDiaHora) {
        this.listCeldaDiaHora = listCeldaDiaHora;
    }

    public Integer getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(Integer selectedTab) {
        this.selectedTab = selectedTab;
    }

    public List<SgDiaLectivoHorarioEscolar> getDiaLectivoHorarioEscolarList() {
        return diaLectivoHorarioEscolarList;
    }

    public void setDiaLectivoHorarioEscolarList(List<SgDiaLectivoHorarioEscolar> diaLectivoHorarioEscolarList) {
        this.diaLectivoHorarioEscolarList = diaLectivoHorarioEscolarList;
    }

    public List<Boolean> getCheckDiaLectivo() {
        return checkDiaLectivo;
    }

    public void setCheckDiaLectivo(List<Boolean> checkDiaLectivo) {
        this.checkDiaLectivo = checkDiaLectivo;
    }

    public Boolean[] getCeldaDiaHoraHabilitado() {
        return celdaDiaHoraHabilitado;
    }

    public void setCeldaDiaHoraHabilitado(Boolean[] celdaDiaHoraHabilitado) {
        this.celdaDiaHoraHabilitado = celdaDiaHoraHabilitado;
    }

    public Boolean getHabilitadoDiaLectivo() {
        return habilitadoDiaLectivo;
    }

    public void setHabilitadoDiaLectivo(Boolean habilitadoDiaLectivo) {
        this.habilitadoDiaLectivo = habilitadoDiaLectivo;
    }

  

   
}
