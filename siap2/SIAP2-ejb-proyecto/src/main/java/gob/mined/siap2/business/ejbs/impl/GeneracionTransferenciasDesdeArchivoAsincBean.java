/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.ejbs.impl;

import com.mined.siap2.to.TranferenciaArchivoTo;
import gob.mined.siap2.business.datatype.DataTransferencia;
import gob.mined.siap2.business.ejbs.IdiomaBean;
import gob.mined.siap2.business.ejbs.TextoBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.DireccionDepartamental;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.Notificacion;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.data.impl.RelacionPresAnioFinanciamiento;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.Transferencia;
import gob.mined.siap2.entities.data.impl.TransferenciaDireccionDepartamental;
import gob.mined.siap2.entities.data.impl.TransferenciasACed;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.entities.enums.EstadoTransferenciaComponente;
import gob.mined.siap2.entities.enums.TipoNotificacion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.filtros.FiltroRelPresAnioFinanciamiento;
import gob.mined.siap2.filtros.FiltroTopePresupuestal;
import gob.mined.siap2.filtros.FiltroTransferenciaComponente;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siges.entities.data.impl.SgSede;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jboss.ejb3.annotation.TransactionTimeout;

@Stateless(name = "generacionTransferenciasDesdeArchivoAsincBean")
@Remote(GeneracionTransferenciasDesdeArchivoAsincInterface.class)
@Asynchronous
@LocalBean
public class GeneracionTransferenciasDesdeArchivoAsincBean implements GeneracionTransferenciasDesdeArchivoAsincInterface{

    private static final Logger LOGGER = Logger.getLogger(GeneracionTransferenciasDesdeArchivoAsincBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Inject
    private TextoBean textoBean;
     
    @Inject
    private SedeBean sedeBean;
    
    @Inject
    private TopePresupuestalBean topePresupuestalBean;
    
    @Inject
    private DireccionDepartamentalBean direccionDepartamentalBean;
    
    @Inject
    private TransferenciaDireccionDepartamentalBean transferenciaDireccionDepartamentalBean;
    
    @Inject
    private TransferenciasACedBean transferenciasACedBean;
    
    @Inject
    private TransferenciasBean transferenciasBean;
    
    @Inject
    private IdiomaBean idiBean;
    
    @Inject
    private PresupuestoFuentesFinanciamientoBean relPresAnioFinanciamientoBean;
    
    @Inject
    private FuentesRecursosBean fuentesRecursosBean;
    
    @Asynchronous
    @TransactionTimeout(unit=TimeUnit.MINUTES, value=90)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void crearDesdeArchivo(TranferenciaArchivoTo archivo) {
        List<String> mensajesErrorMontos = new ArrayList();
        String contenidoNotificacion = "Errores en la ejecución de proceso de generación de tranferencias."; 
        String presupuesto = "";
        //SsUsuario usuario = null;
        List<SsUsuario> usuarios = archivo.getUsuarios();
        Integer idiomaId = 1;
        Boolean existoso = false;
        List<Transferencia> transferencias = new ArrayList<>();
        List<FuenteRecursos> fuentesRecurso = new ArrayList<>();
        RelacionGesPresEsAnioFiscal relPresupuestoAnio = null;
        try {
            Thread.sleep(5000);//5 segundos de espera
            
            idiomaId = idiBean.obtenerIdiomaEspaniol().getIdiId();
            
            HashMap<TransferenciaDireccionDepartamental, List<TransferenciasACed>> sedesPorTDD;
            Map<String, List<DataTransferencia>> fuentesTransferencia = new HashMap();
            
            BusinessException be = new BusinessException();
            if (archivo.getAnioFiscal() == null) {
                be.addError(ConstantesErrores.ERR_DEBE_INGRESAR_AÑO_FISCAL);
            }
            
            if (archivo.getComponente() == null || archivo.getRelGesPres() == null) {
                be.addError(ConstantesErrores.ERR_COMPONENTE_PRESUPUESTO_ESCOLAR_VACIO);
            }            
            if (archivo.getFile() == null) {
                be.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_ARCHIVO);
            }

            if (!be.getErrores().isEmpty()) {
                throw be;
            }
            if(usuarios == null) {
                usuarios = new ArrayList();
            }
            relPresupuestoAnio = generalDAO.getEntityManager().find(RelacionGesPresEsAnioFiscal.class, archivo.getRelGesPres().getId());
            if(relPresupuestoAnio != null) {
                Boolean agregarusuarioComponente = Boolean.TRUE;
                if(relPresupuestoAnio.getComponentePresupuestoEscolar() != null && relPresupuestoAnio.getComponentePresupuestoEscolar().getUsuario() != null) {
                    SsUsuario usuarioSunComp= relPresupuestoAnio.getComponentePresupuestoEscolar() != null ? relPresupuestoAnio.getComponentePresupuestoEscolar().getUsuario() : null;
                    if(usuarioSunComp != null) {
                        for(SsUsuario usu : usuarios) {
                            if(usu.getUsuId().equals(usuarioSunComp.getUsuId())) {
                                agregarusuarioComponente = Boolean.FALSE;
                                break;
                            }
                        }
                        if(agregarusuarioComponente) {
                            usuarios.add(relPresupuestoAnio.getComponentePresupuestoEscolar().getUsuario());
                        }
                    }
                }
                presupuesto = relPresupuestoAnio.getDescripcion().trim();
            }
            
            
            

            FiltroRelPresAnioFinanciamiento fr = new FiltroRelPresAnioFinanciamiento();
            fr.setRelAnioPresupuesto(relPresupuestoAnio.getId());
            List<RelacionPresAnioFinanciamiento> listaRelFuentes = relPresAnioFinanciamientoBean.getComponentesPresupuestoEscolarByFiltro(fr);
            
            
            org.apache.poi.ss.usermodel.Workbook myWorkBook = WorkbookFactory.create(archivo.getFile().getInputStream());
            org.apache.poi.ss.usermodel.Sheet mySheet = myWorkBook.getSheetAt(0);

            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();
            
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (row.getRowNum() == 0) {
                    //Obtener cuentas
                    Iterator<Cell> cellIterator = row.cellIterator();

                    if (cellIterator.hasNext()) {
                        cellIterator.next(); // Skip primera columna
                    }

                    whileTranferencias:
                    while (cellIterator.hasNext()) {
                        BigDecimal dato = topePresupuestalBean.getValorBigDecimal(cellIterator.next());
                        if(dato == null) {
                            break whileTranferencias;
                        }
                        Integer transId = dato.toBigInteger().intValue();
                        Transferencia tr = null;

                        if (transId != null && transId > 0) {
                            tr = transferenciasBean.getTransferenciaById(transId);
                        } else {
                            break whileTranferencias;
                        }

                        if (tr == null ) {
                            //Solamente tranferencias
                            be.addError(ConstantesErrores.ERR_TRANSFERENCIA_INEXISTENTE, new String[]{""+transId});
                            throw be;
                        } else {
                            if(tr.getRelacionGesPresEsAnioFiscal() != null && tr.getRelacionGesPresEsAnioFiscal().getId().equals(relPresupuestoAnio.getId())) {
                                transferencias.add(tr);
                            } else {
                                //Solamente tranferencias
                                be.addError(ConstantesErrores.ERR_TRANSFERENCIA_INEXISTENTE_EN_COMPONENTE, new String[]{""+tr.getId() , relPresupuestoAnio.getDescripcion().trim()});
                                throw be;
                            }
                        }

                        
                    }

                } if (row.getRowNum() == 1) {
                    //Obtener fuentes de financiamiento
                    Iterator<Cell> cellIterator = row.cellIterator();

                    if (cellIterator.hasNext()) {
                        cellIterator.next(); // Skip primera columna
                    }

                    whileFuentesFinanciamiento:
                    while (cellIterator.hasNext()) {
                        String fRecurnsoCodigo = topePresupuestalBean.getValor(cellIterator.next());
                        FuenteRecursos fRecurso = null;

                        if (!StringUtils.isBlank(fRecurnsoCodigo)) {
                           // List<FuenteRecursos> fuentes = generalDAO.findByOneProperty(FuenteRecursos.class, "codigo", fRecurnsoCodigo.trim());
                           // if(fuentes != null && !fuentes.isEmpty() && fuentes.size() > 0) {
                                fRecurso = fuentesRecursosBean.getFuenteRecursosByCodigo(fRecurnsoCodigo.trim());
                           // }
                        } else {
                            break whileFuentesFinanciamiento;
                        }

                        if (fRecurso == null ) {
                            //Solamente fuentes de recurso
                            be.addError(ConstantesErrores.ERR_CODIGO_FUENTE_RECURSO_INEXISTENTE, new String[]{fRecurnsoCodigo});
                            throw be;
                        } else {
                            Boolean existe = Boolean.FALSE;
                            for(RelacionPresAnioFinanciamiento relFuente : listaRelFuentes) {
                                if(fRecurso.getId().equals(relFuente.getFuenteRecursos().getId())&& fRecurso.getFuenteFinanciamiento().getId().equals(relFuente.getFuenteRecursos().getFuenteFinanciamiento().getId())) {
                                    fuentesRecurso.add(fRecurso);
                                    existe = Boolean.TRUE;
                                    break;
                                }
                            }  
                            if(!existe) {
                                be.addError(ConstantesErrores.ERR_CODIGO_FUENTE_RECURSO_INEXISTENTE_EN_COMPONENTE, new String[]{fRecurnsoCodigo,relPresupuestoAnio.getDescripcion().trim()});
                                throw be;
                            }
                        }

                        

                    }

                } else if (row.getRowNum() > 1) {
                    
                    try {
                        //Cuentas                    
                        Iterator<Cell> cellIterator = row.cellIterator();
                        if(cellIterator != null) {
                            //Sede
                            String sedCodigo = topePresupuestalBean.getValor(cellIterator.next());
                            SgSede sede = null;
                            if (!StringUtils.isBlank(sedCodigo)) {
                                sede = sedeBean.getSedeByCodigo(sedCodigo.trim());
                            } else {
                                continue;
                            }
                            if (sede == null) {
                                be.addError(ConstantesErrores.ERR_CODIGO_SEDE_INEXISTENTE, new String[]{sedCodigo});
                                throw be;
                            }

                            whileCuentas:
                            while (cellIterator.hasNext()) {
                                if(cellIterator != null) {

                                        Cell cell = cellIterator.next();
                                        BigDecimal monto = topePresupuestalBean.getValorBigDecimal(cell);

                                        Integer tranferIndex = cell.getColumnIndex() - 1;

                                        if (tranferIndex >= transferencias.size()) {
                                            continue;
                                        }

                                        Transferencia tr =   transferencias.get(tranferIndex);
                                        if(tr.getRelacionGesPresEsAnioFiscal().getId().equals(relPresupuestoAnio.getId())) {
                                            for(RelacionPresAnioFinanciamiento relFuente : listaRelFuentes) {
                                                if(fuentesRecurso.get(tranferIndex).getId().equals(relFuente.getFuenteRecursos().getId()) && fuentesRecurso.get(tranferIndex).getFuenteFinanciamiento().getId().equals(relFuente.getFuenteRecursos().getFuenteFinanciamiento().getId())) {
                                                    
                                                    DataTransferencia data = new DataTransferencia();
                                                    data.setAnioFiscal(archivo.getAnioFiscal());
                                                    data.setFuenteFinanciamiento(fuentesRecurso.get(tranferIndex).getFuenteFinanciamiento());
                                                    data.setFuenteRecursos(fuentesRecurso.get(tranferIndex));
                                                    data.setGesPresEs(archivo.getSubComponente());
                                                    data.setMonto(monto);
                                                    data.setRelGesPres(relPresupuestoAnio);
                                                    data.setSede(sede);
                                                    data.setTranferencia(transferencias.get(tranferIndex));
                                             
                                                    //anio_id - subcomponente_id - frecurso_id - tranferencia_id
                                                    String codigo = archivo.getAnioFiscal().getId() + "-"+ archivo.getSubComponente().getId()+ "-" + fuentesRecurso.get(tranferIndex).getId() 
                                                            + "-" +transferencias.get(tranferIndex).getId();
                                                    
                                                    
                                                    if(fuentesTransferencia.containsKey(codigo)) {
                                                        fuentesTransferencia.get(codigo).add(data);
                                                    } else {
                                                        List<DataTransferencia> trList = new ArrayList();
                                                        trList.add(data);
                                                        fuentesTransferencia.put(codigo, trList);
                                                    }
                                                }
                                            }
                                            
                                        }
                                    }
                                }
                            }
                        } catch (BusinessException e) {
                            throw e;
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, "Error al procesar las sedes", e);
                        }
                }

            }
            
            sedesPorTDD = new HashMap<TransferenciaDireccionDepartamental, List<TransferenciasACed>>();
            List<TransferenciasComponente> listaTrComp = new ArrayList();
            List<Transferencia> listaTrans = new ArrayList();
            
            for (Map.Entry<String, List<DataTransferencia>> entry : fuentesTransferencia.entrySet()) {
                String codigo = entry.getKey();
                List<DataTransferencia> listado = entry.getValue();
                String[] ids = codigo.split("-");
                Integer idAnio = Integer.valueOf(ids[0]);
                Integer idSubcomponente = Integer.valueOf(ids[1]);
                Integer idFuenteRecurso = Integer.valueOf(ids[2]);
                Integer idTransferencia = Integer.valueOf(ids[3]);
                for(Transferencia t : transferencias) {
                    if(t.getId().equals(idTransferencia)) {
                        listaTrans.add(t);
                        break;
                    }
                }
                
                
                FiltroTransferenciaComponente filtro = new FiltroTransferenciaComponente();
                filtro.setIdTransferencia(idTransferencia);
                filtro.setIdSubcomponente(idSubcomponente);
                filtro.setIdAnioFiscal(idAnio);
                filtro.setIdFuenteRecursos(idFuenteRecurso);
                List<TransferenciasComponente> lista = transferenciasBean.getTransferenciaComponenteByFiltro(filtro);
                if(lista != null && !lista.isEmpty() && lista.size() > 0) {
                    TransferenciasComponente trComp = lista.get(0);
                    trComp.setEstado(EstadoTransferenciaComponente.AUTORIZADA);
                    listaTrComp.add(trComp);
                    
                    BigDecimal montoTotal = BigDecimal.ZERO;
                    //Se clasifican los topes por TDD
                    HashMap<TransferenciaDireccionDepartamental, List<DataTransferencia>> topesPorDepartamento = new HashMap<TransferenciaDireccionDepartamental, List<DataTransferencia>>();
                    for(DataTransferencia tope : listado){
                        DireccionDepartamental ddp = direccionDepartamentalBean.getDireccionDepartamentalIdDepartamento(tope.getSede().getDireccion().getDepartamento().getPk());
                        if(ddp == null) {
                            be.addError(ConstantesErrores.ERR_NO_EXISTE_DIRECCION_DEPARTAMENTAl_SEDE,new String[]{tope.getSede().getCodigo()});
                            throw be;
                        }
                        TransferenciaDireccionDepartamental tdd = transferenciaDireccionDepartamentalBean.getTransferenciasDirDepaByTransfCompYDirDepa(trComp.getId(), ddp.getPk());
                        if(tdd == null){
                            tdd = transferenciasBean.nuevoTDD(ddp, trComp);
                        }
                        if(topesPorDepartamento.containsKey(tdd)){
                            topesPorDepartamento.get(tdd).add(tope);
                        }else{
                            topesPorDepartamento.put(tdd, new ArrayList<DataTransferencia>());
                            topesPorDepartamento.get(tdd).add(tope);
                        }
                    }
                    
                    for(Map.Entry<TransferenciaDireccionDepartamental, List<DataTransferencia>> item : topesPorDepartamento.entrySet()){
                        TransferenciasACed aced = null;
                        Integer sumaBeneficiarios = 0;
                        montoTotal = BigDecimal.ZERO;
                        for(DataTransferencia tope: item.getValue()){
                            //Integer componenteId,Integer subComponenteId, Integer unidadId, Integer lineaId, Integer fuenteRecursosId,Integer anioId, Integer idSede
                            BigDecimal montoAcumulado = transferenciasACedBean.getTotalMontoTransferenciaACedByComponenteSedeTDD(trComp.getComponente().getId(),
                                                                                                                                 trComp.getSubcomponente().getId(),
                                                                                                                                 trComp.getUnidadPresupuestaria().getId(),
                                                                                                                                 trComp.getLineaPresupuestaria().getId(),
                                                                                                                                 trComp.getFuenteRecursos().getId(),
                                                                                                                                 tope.getAnioFiscal().getId(),
                                                                                                                                 tope.getSede().getId());
                            BigDecimal montoAcumuladoMostrar = montoAcumulado;
                            BigDecimal montoAnterior = BigDecimal.ZERO;
                            aced = transferenciasACedBean.getTransferenciaACedByComponenteSedeTDD(trComp.getId(), tope.getSede().getId(), item.getKey().getPk());
                            Boolean existente = false;
                            if(aced == null){
                                aced = transferenciasBean.crearNuevaTransferenciaACed(tope, trComp);
                            } else {
                                montoAnterior = aced.getMontoAutorizado();
                                aced = transferenciasBean.actualizarTransferenciaACed(tope, trComp, aced);
                                existente = true;
                            }
                            
                            //Consultas topes existentes
                            FiltroTopePresupuestal filtroComponente = new FiltroTopePresupuestal();
                            filtroComponente.setAnioFiscalId(tope.getAnioFiscal().getId());
                            filtroComponente.setComponentePresupuestoEscolarId(tope.getGesPresEs().getId());
                           // filtroComponente.setCuentaId(tope.getSubCuenta().getId());
                            filtroComponente.setSedesId(Arrays.asList(tope.getSede().getId()));
                            filtroComponente.setFuenteRecursoId(tope.getFuenteRecursos().getId());
                            filtroComponente.setCompPresAnioFiscalId(tope.getRelGesPres().getId());
                            filtroComponente.setEstado(EstadoTopePresupuestal.APROBACION);
                            List<TopePresupuestal> existentes = topePresupuestalBean.getTopesPresupuestalesByFiltro(filtroComponente);
                            
                            BigDecimal montoTope = BigDecimal.ZERO;
                            Boolean topeExiste = false;
                            for (TopePresupuestal t : existentes) {
                                montoTope = montoTope.add(t.getMontoAprobado());
                                topeExiste = true;
                            }
                            if(topeExiste) {
                                if(existente) {
                                    montoAcumulado = montoAcumulado.subtract(montoAnterior).add(aced.getMontoAutorizado());
                                } else {
                                    montoAcumulado = montoAcumulado.add(aced.getMontoAutorizado());
                                }
                                
                                if(montoAcumulado.compareTo(montoTope) <= 0) {
                                    montoTotal = montoTotal.add(aced.getMontoAutorizado());

                                    if(sedesPorTDD.containsKey(item.getKey())){
                                        sedesPorTDD.get(item.getKey()).add(aced);
                                    }else{
                                        sedesPorTDD.put(item.getKey(), new ArrayList<TransferenciasACed>());
                                        sedesPorTDD.get(item.getKey()).add(aced);
                                    }
                                } else {
                                    mensajesErrorMontos.add(tope.getSede().getCodigo() + ": Monto Aprobado del techo: " + montoTope + ", Monto de tranferencias acumuladas: " + montoAcumuladoMostrar +", Monto de tranferencia: " + aced.getMontoAutorizado());
                                }
                            } else {
                                 mensajesErrorMontos.add(tope.getSede().getCodigo() + ": No existe techo aprobado.");
                            }
                            
                        }
                        item.getKey().setBeneficiarios(sumaBeneficiarios);
                        item.getKey().setMontoAutorizado(montoTotal);
                    }
                }
            }
            if(sedesPorTDD != null && !sedesPorTDD.isEmpty() && sedesPorTDD.size() > 0) {
                transferenciasACedBean.crearActualizarTransferenciasDireccionesDepartamental(sedesPorTDD);
            }
            transferenciasBean.autorizarTransferencias(listaTrans, listaTrComp);
            existoso = true;
        } catch (BusinessException be) {
            existoso = false;
            mensajesErrorMontos = new ArrayList();
            for(String error : be.getErrores()) {
                String mensaje = textoBean.obtenerMensajeByProperty(error, idiomaId);
                //String mensaje = error;
                contenidoNotificacion += mensaje;
            }
            LOGGER.log(Level.SEVERE, null, be);
        } catch (DAOObjetoNoEditableException e) {
            existoso = false;
            mensajesErrorMontos = new ArrayList();
            //BusinessException b = new BusinessException();
           // b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            
            String mensaje = textoBean.obtenerMensajeByProperty(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO, idiomaId);
            //String mensaje = ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO;
            contenidoNotificacion += mensaje;
            
            LOGGER.log(Level.SEVERE, null, e);
            //throw b;
        } catch (Exception ex) {
            existoso = false;
            mensajesErrorMontos = new ArrayList();
            LOGGER.log(Level.SEVERE, null, ex);
            String mensaje = textoBean.obtenerMensajeByProperty(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE, idiomaId);
            //String mensaje = ConstantesErrores.ERR_GNRAL_SAVE_UPDATE;
            contenidoNotificacion +=  mensaje;
            //TechnicalException ge = new TechnicalException();
            //ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            //ge.addError(ex.getMessage());
            //throw ge;
        } finally {
            try {
                relPresupuestoAnio.setProcesoEnCurso(Boolean.FALSE);
                guardarActualizar(relPresupuestoAnio);
                
                if(existoso) {
                    if(transferencias != null) {
                        String tranferencia = "";
                        contenidoNotificacion = "";
                        Integer count = 0;
                        for(Transferencia tr : transferencias) {
                            if(count ==0) {
                                tranferencia = String.valueOf(tr.getId());
                            } else {
                                tranferencia += "," + String.valueOf(tr.getId());
                            }
                            count = count + 1;
                        }
                        if(count == 1) {
                            contenidoNotificacion = "La generación de tranferencias con id "+ tranferencia+" del presupuesto " + presupuesto + " se ejecutó correctamente.";
                        } else {
                            contenidoNotificacion = "La generación de tranferencias con ids " + tranferencia + " del presupuesto " + presupuesto + " se ejecutaron correctamente.";
                        }
                    }
                    if(mensajesErrorMontos != null && !mensajesErrorMontos.isEmpty() && mensajesErrorMontos.size() > 0) {
                        contenidoNotificacion += "Aunque las siguientes sedes presentan diferencia entre el monto del techo aprobado y el total de las transferencias.";
                        for(String error : mensajesErrorMontos) {
                            contenidoNotificacion += "-" + error;
                        }
                    }
                }

                if(usuarios != null && !usuarios.isEmpty() && usuarios.size() > 0) {
                    for(SsUsuario usu : usuarios) {
                        Notificacion n = new Notificacion();
                        n.setTipo(TipoNotificacion.EJECUCION_PROCESO_TRANSFERENCIAS_ARCHIVO);
                        n.setUsuario(usu);
                        n.setFecha(new Date());
                        n.setObjetoId(relPresupuestoAnio.getComponentePresupuestoEscolar().getId());
                        n.setContenido(contenidoNotificacion);
                        generalDAO.updateNew(n);
                    }
                }
                
            } catch (DAOGeneralException ex) {
                LOGGER.log(Level.SEVERE, "Error al procesar archivo de tranferencias", ex);
            }
        }
       //return new AsyncResult<Void>(null);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void guardarActualizar(Object o) throws DAOGeneralException {
        generalDAO.update(o);
    }
}
