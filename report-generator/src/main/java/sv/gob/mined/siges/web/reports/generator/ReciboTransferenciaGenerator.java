/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgCentroEducativo;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgPersonaOrganismoAdministracion;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.SgTransferenciaACed;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancarias;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPersonaOrganismoAdministrativo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTransferenciaACed;
import sv.gob.mined.siges.web.restclient.CuentasBancariasRestClient;
import sv.gob.mined.siges.web.restclient.PersonaOrganismoAdministracionRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.restclient.TransferenciaACedRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;
import sv.gob.mined.siges.web.utilidades.NumberToLetterConverter;
//import sv.gob.mined.siges.web.utilidades.NumberToLetterConverter;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class ReciboTransferenciaGenerator {
    
    @Inject
    private PlantillaRestClient plantillaClient;
    
    @Inject
    private TransferenciaACedRestClient cedRestClient;
    
    @Inject 
    private SedeRestClient sedeRestClient;
    
    @Inject
    private PersonaOrganismoAdministracionRestClient personaOrganismoAdministracionRestClient;
    
    @Inject
    private CuentasBancariasRestClient cuentaBancRestClient;
    
    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;
    
    @Inject
    private SessionBean sessionBean;
      
    public MasterReport generarReporte(Long transferencia) throws Exception {
        try {
            BusinessException be = new BusinessException();
            SgTransferenciaACed transferenciaCed = new SgTransferenciaACed();
            
            if (transferencia == null) {
                be.addError("Debes ingresar la sede.");
                throw be;
            }
             // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();
            

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_RECIBO_TRANSFERENCIA);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_RECIBO_TRANSFERENCIA + " inexistente.");
                    throw be;
                }
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("reciboTransferencia.prpt");
            }
                    
            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
            
            
            FiltroTransferenciaACed filtro = new FiltroTransferenciaACed();
            filtro.setFirst(new Long(0));
            filtro.setIncluirCampos(new String[]{"tacPk",
                "tacCedFk.sedPk",
                "tacCedFk.sedCodigo",
                "tacCedFk.sedNombre",
                "tacCedFk.sedTipo",
                "tacCedFk.sedVersion",
                "tacCedFk.sedDireccion.dirDireccion",
                "tacCedFk.sedDireccion.dirDepartamento.depNombre",
                "tacCedFk.sedDireccion.dirMunicipio.munNombre",
                "tacCedFk.sedDireccion.dirCanton.canNombre",
                "tacCedFk.sedDireccion.dirCaserio.casNombre",
                "tacTransferenciaFk.tcId",
                "tacTransferenciaFk.componente.cpeId",
                "tacTransferenciaFk.componente.cpeNombre",
                "tacTransferenciaFk.subComponente.gesId",
                "tacTransferenciaFk.subComponente.gesNombre",
                "tacTransferenciaFk.subComponente.gesVersion",
                "tacTransferenciaFk.anioFiscal.aniPk",
                "tacTransferenciaFk.anioFiscal.aniAnio",
                "tacTransferenciaFk.anioFiscal.aniVersion",
                "tacTransferenciaFk.unidadPresupuestaria.cuNombre",
                "tacTransferenciaFk.unidadPresupuestaria.cuCodigo",
                "tacTransferenciaFk.lineaPresupuestaria.cuNombre",
                "tacTransferenciaFk.lineaPresupuestaria.cuCodigo",
                "tacTransferenciaFk.tcFuenteRecursosFk.nombre",
                "tacTransferenciaFk.tcFuenteRecursosFk.version",
                "tacTransferenciaFk.tcFuenteRecursosFk.fuenteFinanciamiento.nombre",
                "tacTransferenciaFk.tcFuenteRecursosFk.fuenteFinanciamiento.version",
                "tacTransferenciaFk.tcPorcentaje",
                "tacTransferenciaFk.tcVersion",
                "tacTransferenciaDireccionDepFk.tddDireccionDepFk.dedDepartamentoFk.depCodigo",
                "tacTransferenciaDireccionDepFk.tddDireccionDepFk.dedDepartamentoFk.depNombre",
                "tacTransferenciaDireccionDepFk.tddDireccionDepFk.dedDepartamentoFk.depVersion",
                "tacTransferenciaDireccionDepFk.tddDireccionDepFk.dedNombre",
                "recibo.recPk",
                "recibo.recVersion",
                "tacMontoAutorizado",
                "tacMontoSolicitado",
                "tacEstado",
                "tacVersion"});
            filtro.setTacPk(transferencia);
            List<sv.gob.mined.siges.web.dto.SgTransferenciaACed> list = cedRestClient.buscar(filtro);
            
            if(!list.isEmpty()){
                transferenciaCed = list.get(0);
            }
            else{
                be.addError("No se encontro la transferencia");
                throw be;
            }
            
            SgCentroEducativo ced = sedeRestClient.obtenerCentroEducativoPorId(transferenciaCed.getTacCedFk().getSedPk());
            //ORDEN 1-PRESIDENTE
            String nombrePresidente = StringUtils.EMPTY;
            String duiPresidente = StringUtils.EMPTY;

            //ORDEN 2-TESORERO
            String nombreTesorero = StringUtils.EMPTY;
            String duiTesorero = StringUtils.EMPTY;

            
            if(ced!=null){
            
                List<String> codigos = new ArrayList();
                codigos.add(ced.getCedTipoOrganismoAdministrativo().getToaCodigo());

                FiltroPersonaOrganismoAdministrativo fPOA = new FiltroPersonaOrganismoAdministrativo();
                fPOA.setSedeId(transferenciaCed.getTacCedFk().getSedPk());
                fPOA.setCodigosTOA(codigos);
                fPOA.setPoaHabilitado(Boolean.TRUE);
                fPOA.setAscending(new boolean[]{true});
                fPOA.setOrderBy(new String[]{"poaCargo.coaOrden"});
                fPOA.setIncluirCampos(new String[]{"poaPersona.perPrimerNombre","poaPersona.perSegundoNombre","poaPersona.perTercerNombre","poaPersona.perPrimerApellido","poaPersona.perDui",
                                    "poaPersona.perSegundoApellido","poaPersona.perTercerApellido","poaPersona.perOcupacion.ocuNombre","poaPersona.perDireccion.dirDireccion",
                                    "poaPersona.perProfesion.proNombre", "poaCargo.coaCodigo","poaCargo.coaNombre","poaPrerregistro","poaNombre","poaDui","poaOcupacion","poaDomicilio",
                                    "poaDuiExpedidoEn","poaFechaExpedicionDUI"});


                List<SgPersonaOrganismoAdministracion> listaOrganismoEscolar =  personaOrganismoAdministracionRestClient.buscar(fPOA);      
                
                String codigoPresidente = StringUtils.EMPTY;
                String codigoTesorero = StringUtils.EMPTY;
                
                if(ced.getCedTipoOrganismoAdministrativo().getToaCodigo().equals(Constantes.CODIGO_CDE)){
                    codigoPresidente = Constantes.CODIGO_PRESIDENTE_CDE;
                    codigoTesorero = Constantes.CODIGO_TESORERO_CDE;
                }
                else if(ced.getCedTipoOrganismoAdministrativo().getToaCodigo().equals(Constantes.CODIGO_CECE)){
                    codigoPresidente = Constantes.CODIGO_PRESIDENTE_CECE;
                    codigoTesorero = Constantes.CODIGO_TESORERO_CECE;
                }
                else if(ced.getCedTipoOrganismoAdministrativo().getToaCodigo().equals(Constantes.CODIGO_CIE)){
                    codigoPresidente = Constantes.CODIGO_PRESIDENTE_CIE;
                    codigoTesorero = Constantes.CODIGO_TESORERO_CIE;
                }
              
               Integer count = 0;
               Boolean preregistrado = Boolean.FALSE;
               for(SgPersonaOrganismoAdministracion poa : listaOrganismoEscolar) {
                       if(poa.getPoaCargo() != null && poa.getPoaCargo().getCoaCodigo() != null) {
                          preregistrado = poa.getPoaPrerregistro() != null ? poa.getPoaPrerregistro() : false;
                          if(poa.getPoaCargo().getCoaCodigo().trim().equals(codigoPresidente)) {
                               if(preregistrado) {
                                   nombrePresidente = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "" ;
                                   duiPresidente = poa.getPoaDui() != null ? poa.getPoaDui().trim() : "";
                               } else {
                                   nombrePresidente = poa.getPoaNombre() != null && StringUtils.isNotBlank(poa.getPoaNombre()) ? poa.getPoaNombre().trim().toUpperCase() : "" ;
                                   duiPresidente = poa.getPoaDui() != null ? poa.getPoaDui().trim() : "";
                               }

                               count ++;
                          } else if(poa.getPoaCargo().getCoaCodigo().trim().equals(codigoTesorero)) {
                              if(preregistrado) {
                                   nombreTesorero = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "" ;
                                   duiTesorero = poa.getPoaDui() != null ? poa.getPoaDui().trim() : "";
                               } else {
                                   nombreTesorero = poa.getPoaPersona() != null && poa.getPoaPersona().getPerNombreCompleto() != null ? poa.getPoaPersona().getPerNombreCompleto().trim().toUpperCase() : "" ;
                                   duiTesorero = poa.getPoaDui() != null ? poa.getPoaDui().trim() : "";
                               }

                               count ++;
                          }
                       }
                       if(count > 2) break;

               }
                
            }
            
            SgCuentasBancarias cuenta = new SgCuentasBancarias();
            String numeroCuenta = StringUtils.EMPTY;
            String banco = StringUtils.EMPTY;
            
            FiltroCuentasBancarias filtroCuenta = new FiltroCuentasBancarias();
            filtroCuenta.setIncluirCampos(new String[]{"cbcNumeroCuenta","cbcBancoFk.bncNombre"});
            filtroCuenta.setCbcSedeFk(transferenciaCed.getTacCedFk().getSedPk());
            filtroCuenta.setSinComponente(Boolean.TRUE);
            //Listado de cuentas sin componente
            List<SgCuentasBancarias> listCuentasSC = cuentaBancRestClient.buscar(filtroCuenta);
            
            filtroCuenta.setSinComponente(Boolean.FALSE);
            filtroCuenta.setComponentePk(transferenciaCed.getTacTransferenciaFk().getComponente().getCpeId());
            //Listado de cuentas con componente
            List<SgCuentasBancarias> listCuentasCC = cuentaBancRestClient.buscar(filtroCuenta);
            
            if(listCuentasCC!=null && !listCuentasCC.isEmpty()){
                cuenta = listCuentasCC.get(0);
                if(cuenta!=null){
                    numeroCuenta=cuenta.getCbcNumeroCuenta();
                    banco = cuenta.getCbcBancoFk().getBncNombre();
                }
            }
            else if (listCuentasSC!=null && !listCuentasSC.isEmpty()){
                cuenta = listCuentasSC.get(0);
                if(cuenta!=null){
                    numeroCuenta=cuenta.getCbcNumeroCuenta();
                    banco = cuenta.getCbcBancoFk().getBncNombre();
                }  
            }
            
            
            report.getParameterValues().put("nRecibo",transferenciaCed.getRecibo()!=null ? transferenciaCed.getRecibo().getRecPk().toString() : "");
            report.getParameterValues().put("nTransferencia", transferenciaCed.getTacPk().toString());
            report.getParameterValues().put("nombreUnidad", transferenciaCed.getTacTransferenciaDireccionDepFk().getTddDireccionDepFk().getDedNombre());
            report.getParameterValues().put("cantidadLetras", NumberToLetterConverter.convertNumberToLetter(new BigDecimal(transferenciaCed.getTacMontoAutorizado()), Boolean.TRUE).trim().toUpperCase());
            report.getParameterValues().put("cantidadNumero", transferenciaCed.getTacMontoAutorizado().toString());
            report.getParameterValues().put("componente", StringUtils.isNotBlank(transferenciaCed.getTacTransferenciaFk().getComponente().getCpeNombre()) ? transferenciaCed.getTacTransferenciaFk().getComponente().getCpeNombre().trim() : "");
            report.getParameterValues().put("subcomponente",StringUtils.isNotBlank(transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesNombre()) ?  transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesNombre().trim() : "");
            report.getParameterValues().put("fuenteRecursos", transferenciaCed.getTacTransferenciaFk().getTcFuenteRecursosFk().getNombre());
            report.getParameterValues().put("unidad", transferenciaCed.getTacTransferenciaFk().getUnidadPresupuestaria().getCuCodigo());
            report.getParameterValues().put("linea", transferenciaCed.getTacTransferenciaFk().getLineaPresupuestaria().getCuCodigo().concat(" ").concat(transferenciaCed.getTacTransferenciaFk().getLineaPresupuestaria().getCuNombre()));
            report.getParameterValues().put("nombreConsejo", ced.getCedTipoOrganismoAdministrativo().getToaNombre().concat(" (").concat(ced.getCedTipoOrganismoAdministrativo().getToaCodigo()).concat(")"));
            report.getParameterValues().put("organismo", transferenciaCed.getTacCedFk().getSedNombre());
            report.getParameterValues().put("codigo", transferenciaCed.getTacCedFk().getSedCodigo());
            report.getParameterValues().put("codigo_sede", transferenciaCed.getTacCedFk().getSedCodigo());
            
            report.getParameterValues().put("municipio", transferenciaCed.getTacCedFk().getSedDireccion().getDirMunicipio().getMunNombre());
            report.getParameterValues().put("departamento", transferenciaCed.getTacCedFk().getSedDireccion().getDirDepartamento().getDepNombre());
            report.getParameterValues().put("canton", (transferenciaCed.getTacCedFk().getSedDireccion().getDirCanton()!=null) ? transferenciaCed.getTacCedFk().getSedDireccion().getDirCanton().getCanNombre() : "" );
            report.getParameterValues().put("caserio", (transferenciaCed.getTacCedFk().getSedDireccion().getDirCaserio()!=null) ? transferenciaCed.getTacCedFk().getSedDireccion().getDirCaserio().getCasNombre() : "");
            
            report.getParameterValues().put("fechaEmisionRecibo", Generales.getDateTimeFormat(LocalDateTime.now(), "dd/MM/yyyy HH:mm"));
            report.getParameterValues().put("banco", banco);
            report.getParameterValues().put("numeroCuenta", numeroCuenta);
            
            report.getParameterValues().put("nombrePresidente", nombrePresidente);
            report.getParameterValues().put("duiPresidente", duiPresidente);
            report.getParameterValues().put("nombreTesorero", nombreTesorero);
            report.getParameterValues().put("duiTesorero", duiTesorero);
            
            if(StringUtils.isNotBlank(transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesConvenio()) || StringUtils.isNotBlank(transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesProyecto()) ||  
               StringUtils.isNotBlank(transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesCategoria()) || StringUtils.isNotBlank(transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesSubactividad()) ||
               transferenciaCed.getTacTransferenciaFk().getSubComponente().getCategoriaGastoConvenio()!=null){
                report.getParameterValues().put("datos_convenio", Constantes.SI);
                report.getParameterValues().put("convenio", StringUtils.isNotBlank(transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesConvenio()) ? transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesConvenio().trim() : "");
                report.getParameterValues().put("proyecto", StringUtils.isNotBlank(transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesProyecto()) ? transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesProyecto().trim() : "");
                report.getParameterValues().put("componenteConvenio", StringUtils.isNotBlank(transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesCategoria()) ?  transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesCategoria().trim() : "");
                report.getParameterValues().put("subcomponenteConvenio", StringUtils.isNotBlank(transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesSubactividad()) ?  transferenciaCed.getTacTransferenciaFk().getSubComponente().getGesSubactividad().trim() : "");
                report.getParameterValues().put("categoriaGasto", transferenciaCed.getTacTransferenciaFk().getSubComponente().getCategoriaGastoConvenio()!=null ?  transferenciaCed.getTacTransferenciaFk().getSubComponente().getCategoriaGastoConvenio().toString().trim() : "");
                
            }
            

            report.setDataFactory(this.getDataFactory(transferenciaCed));
            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }
    
    private DataFactory getDataFactory(SgTransferenciaACed transferenciaACed) throws Exception {
        
        
        TypedTableModel model = new TypedTableModel();
        model.addColumn("fuentederecursos", TypedTableModel.class);
        
        TypedTableModel fuentederecursos = new TypedTableModel();
        fuentederecursos.addColumn("codigo", String.class);
        fuentederecursos.addColumn("organismo", BigDecimal.class);
        fuentederecursos.addColumn("montoTotal", BigDecimal.class);
      
        fuentederecursos.addRow(transferenciaACed.getTacCedFk().getSedCodigo(),transferenciaACed.getTacCedFk().getSedNombre(),transferenciaACed.getTacMontoAutorizado());
                
        
        model.addRow(fuentederecursos);
        TableDataFactory dataFactory = new TableDataFactory();
        dataFactory.addTable("param-query", model);
        return dataFactory;
    
    }
    
}
