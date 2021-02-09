/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.reports.generator;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.pentaho.reporting.engine.classic.core.DataFactory;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.TableDataFactory;
import org.pentaho.reporting.engine.classic.core.util.TypedTableModel;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import sv.gob.mined.siges.utils.SofisFileUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.dto.finanzas.SgReqFondoCed;
import sv.gob.mined.siges.web.dto.finanzas.SgRequerimientoFondo;
import sv.gob.mined.siges.web.dto.siap2.FuenteFinanciamiento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReqFondoCed;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRequerimientosFondo;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.ReqFondoCedRestClient;
import sv.gob.mined.siges.web.restclient.RequerimientoFondoRestClient;
import sv.gob.mined.siges.web.utilidades.Generales;
import sv.gob.mined.siges.web.utilidades.NumberToLetterConverter;

/**
 *
 * @author Sofis Solutions
 */
@Named
@RequestScoped
public class RequerimientoTransfFondosGenerator {

    
    @Inject
    private RequerimientoFondoRestClient restClient;
    
    
    @Inject
    private ReqFondoCedRestClient restReqFondoCed;

    @Inject
    @ConfigProperty(name = "plantillas.utilizarExternas")
    private Boolean utilizarPlantillasExternas;

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    private PlantillaRestClient plantillaClient;
    
    private String unidad = new String();
    private String linea = new String();
    private BigDecimal montoTotal = BigDecimal.ZERO;

    public MasterReport generarReporte(Long reqId) throws Exception {
        try {
            SgRequerimientoFondo reqFondo = new SgRequerimientoFondo();
            BusinessException be = new BusinessException();
            if (reqId == null) {
                be.addError("Debes ingresar la id de solicitud.");
                throw be;
            }
            // Getting the report.
            ResourceManager manager = new ResourceManager();
            manager.registerDefaults();

            URL reportDefinitionURL = null;
            if (BooleanUtils.isTrue(utilizarPlantillasExternas)) {
                SgPlantilla plantilla = plantillaClient.obtenerPorCodigo(Constantes.PLANTILLA_REQUERIMIENTO_TRANSF_FONDOS);
                if (plantilla == null || plantilla.getPlaArchivo() == null) {
                    be.addError("Plantilla " + Constantes.PLANTILLA_REQUERIMIENTO_TRANSF_FONDOS + " inexistente.");
                    throw be;
                }
                reportDefinitionURL = Paths.get(this.basePath + SofisFileUtils.getPathFromPk(plantilla.getPlaArchivo().getAchPk())).toUri().toURL();
            } else {
                final ClassLoader classloader = this.getClass().getClassLoader();
                reportDefinitionURL = classloader.getResource("requerimientoTransfFondos.prpt");
            }

            Resource res = manager.createDirectly(reportDefinitionURL, MasterReport.class);
            MasterReport report = (MasterReport) res.getResource();
            
            //Busqueda de Requerimiento de Fondo
            FiltroRequerimientosFondo filtroSol = new FiltroRequerimientosFondo();
            filtroSol.setStrPk(reqId);
            filtroSol.setIncluirCampos(new String[]{"strPk",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traId",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traVersion",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.tipoTransferencia.nombre",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesNombre",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesConvenio",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesProyecto",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesCategoria",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesSubactividad",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.categoriaGastoConvenio",
                "strTransferenciaGDepFk.tgdTransferenciaFk.traSubcomponente.gesCategoriaComponente.cpeNombre",
                "strTransferenciaGDepFk.tgdDepartamentoFk.depNombre",
                "strCuentaBancDdFk.cbdNumeroCuenta",
                "strCuentaBancDdFk.cbdBancoFk.bncNombre",
                "strCuentaBancDdFk.cbdDirDepFk.dedNombre",
                "strCuentaBancDdFk.cbdDirDepFk.dedNit",
                "strCuentaBancDdFk.cbdDirDepFk.decDirectorCargo",
                "strCuentaBancDdFk.cbdDirDepFk.decDirectorNombre",
                "strCuentaBancDdFk.cbdDirDepFk.decPagadorCargo",
                "strCuentaBancDdFk.cbdDirDepFk.decPagadorNombre"});
            filtroSol.setOrderBy(new String[]{"strPk"});
            filtroSol.setAscending(new boolean[]{false});
            List<SgRequerimientoFondo> listSol = restClient.buscar(filtroSol);
            if (!listSol.isEmpty()) {
                reqFondo = listSol.get(0);
            }
            

            if (reqFondo.getStrPk()==null) {
                be.addError(Mensajes.obtenerMensaje(Mensajes.SOLICITUD_NO_ENCONTRADA));
                throw be;
            }
            
            report.getParameterValues().put("concepto", reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getTipoTransferencia().getNombre());
            report.getParameterValues().put("num_requerimiento", reqFondo.getStrPk().toString());
            report.getParameterValues().put("num_transferencia", reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraId().toString());
            report.getParameterValues().put("fecha_elaboracion", Generales.getDateTimeFormat(LocalDateTime.now(), "dd/MM/yyyy HH:mm"));
            report.getParameterValues().put("componente", reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesCategoriaComponente().getCpeNombre());
            report.getParameterValues().put("subcomponente", reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesNombre());
            report.getParameterValues().put("banco", reqFondo.getStrCuentaBancDdFk().getCbdBancoFk().getBncNombre());
            report.getParameterValues().put("num_cuenta", reqFondo.getStrCuentaBancDdFk().getCbdNumeroCuenta());
            report.getParameterValues().put("nombre_director", reqFondo.getStrCuentaBancDdFk().getCbdDirDepFk().getDecDirectorNombre());
            report.getParameterValues().put("nombre_pagador", reqFondo.getStrCuentaBancDdFk().getCbdDirDepFk().getDecPagadorNombre());
            report.getParameterValues().put("cargo_director", reqFondo.getStrCuentaBancDdFk().getCbdDirDepFk().getDecDirectorCargo());
            report.getParameterValues().put("cargo_pagador", reqFondo.getStrCuentaBancDdFk().getCbdDirDepFk().getDecPagadorCargo());
            report.getParameterValues().put("departamento", reqFondo.getStrTransferenciaGDepFk().getTgdDepartamentoFk().getDepNombre());
            report.getParameterValues().put("nit_departamental", reqFondo.getStrCuentaBancDdFk().getCbdDirDepFk().getDedNit());
            
            if(StringUtils.isNotBlank(reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesConvenio()) || StringUtils.isNotBlank(reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesProyecto()) ||  
               StringUtils.isNotBlank(reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesCategoria()) || StringUtils.isNotBlank(reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesSubactividad()) ||
               reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getCategoriaGastoConvenio()!=null){
                report.getParameterValues().put("datos_convenio", Constantes.SI);
                report.getParameterValues().put("convenio", StringUtils.isNotBlank(reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesConvenio()) ? reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesConvenio().trim() : "");
                report.getParameterValues().put("proyecto", StringUtils.isNotBlank(reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesProyecto()) ? reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesProyecto().trim() : "");
                report.getParameterValues().put("componente_convenio", StringUtils.isNotBlank(reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesCategoria()) ?  reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesCategoria().trim() : "");
                report.getParameterValues().put("subcomponente_convenio", StringUtils.isNotBlank(reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesSubactividad()) ?  reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getGesSubactividad().trim() : "");
                report.getParameterValues().put("cat_gasto_convenio", reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getCategoriaGastoConvenio()!=null ?  reqFondo.getStrTransferenciaGDepFk().getTgdTransferenciaFk().getTraSubcomponente().getCategoriaGastoConvenio().toString().trim() : "");
            }
            
            
            report.setDataFactory(getDataFactory(reqFondo));
            report.getParameterValues().put("cantidad", montoTotal);
            report.getParameterValues().put("cantidad_solicitada", NumberToLetterConverter.convertNumberToLetter(montoTotal, Boolean.TRUE).trim().toUpperCase());

            return report;
        } catch (BusinessException ge) {
            throw ge;
        }
    }
    
    
    private DataFactory getDataFactory(SgRequerimientoFondo reqFondo) throws Exception {
            
            
            TypedTableModel model = new TypedTableModel();
            model.addColumn("detalle", TypedTableModel.class);

            TypedTableModel detalle = new TypedTableModel();
            detalle.addColumn("unidad", String.class);
            detalle.addColumn("linea", String.class);
            detalle.addColumn("fuente_financiamiento", String.class);
            detalle.addColumn("monto_autorizado", BigDecimal.class);



            FiltroReqFondoCed filtroReqFondoCed = new FiltroReqFondoCed();
            filtroReqFondoCed.setFirst(new Long(0));
            filtroReqFondoCed.setOrderBy(new String[]{"rfcPk"});
            filtroReqFondoCed.setIncluirCampos(new String[]{
                "rfcPk", "rfcMonto",
                "rfcTransferenciaCedFk.tacTransferenciaFk.unidadPresupuestaria.cuCodigo",
                "rfcTransferenciaCedFk.tacTransferenciaFk.lineaPresupuestaria.cuNombre",
                "rfcTransferenciaCedFk.tacTransferenciaFk.tcFuenteRecursosFk.fuenteFinanciamiento.nombre",
                "rfcTransferenciaCedFk.tacTransferenciaFk.tcFuenteRecursosFk.fuenteFinanciamiento.codigo"});
            filtroReqFondoCed.setAscending(new boolean[]{false});
            filtroReqFondoCed.setRfcStrFk(reqFondo.getStrPk());
            List<SgReqFondoCed> resultado = restReqFondoCed.buscar(filtroReqFondoCed);

            if(!resultado.isEmpty()){

                List<FuenteFinanciamiento> fuentes = resultado.stream().filter(r-> r.getRfcTransferenciaCedFk()!=null).filter(r->r.getRfcTransferenciaCedFk().getTacTransferenciaFk()!=null)
                                                                         .filter(r->r.getRfcTransferenciaCedFk().getTacTransferenciaFk().getTcFuenteRecursosFk()!=null)
                                                                         .map(r->r.getRfcTransferenciaCedFk().getTacTransferenciaFk().getTcFuenteRecursosFk().getFuenteFinanciamiento())
                                                                         .distinct()
                                                                         .sorted(Comparator.comparing(FuenteFinanciamiento::getCodigo))
                                                                         .collect(Collectors.toList());

                if(!fuentes.isEmpty()){
                    unidad =  resultado.get(0).getRfcTransferenciaCedFk().getTacTransferenciaFk().getUnidadPresupuestaria().getCuCodigo();
                    linea = resultado.get(0).getRfcTransferenciaCedFk().getTacTransferenciaFk().getLineaPresupuestaria().getCuNombre();

                    for(FuenteFinanciamiento fue: fuentes){
                        String fuente=resultado.stream()
                                       .filter(req-> req.getRfcTransferenciaCedFk().getTacTransferenciaFk().getTcFuenteRecursosFk().getFuenteFinanciamiento().getCodigo().equals(fue.getCodigo()))
                                       .map(req-> req.getRfcTransferenciaCedFk().getTacTransferenciaFk().getTcFuenteRecursosFk().getFuenteFinanciamiento().getNombre()).findFirst().orElse("");

                        BigDecimal monto=resultado.stream()
                                       .filter(req-> req.getRfcTransferenciaCedFk().getTacTransferenciaFk().getTcFuenteRecursosFk().getFuenteFinanciamiento().getCodigo().equals(fue.getCodigo()))
                                       .map(SgReqFondoCed::getRfcMonto)
                                       .reduce(BigDecimal.ZERO, BigDecimal::add);

                        detalle.addRow(unidad,linea,fuente,monto);
                        montoTotal = montoTotal.add(monto);

                    }
                }

                

            }


            model.addRow(detalle);
            TableDataFactory dataFactory = new TableDataFactory();
            dataFactory.addTable("param-query", model);
            
            
            return dataFactory;
    
    }
    
}
