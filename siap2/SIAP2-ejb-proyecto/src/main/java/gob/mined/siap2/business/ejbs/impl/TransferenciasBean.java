package gob.mined.siap2.business.ejbs.impl;

import com.mined.siap2.to.TranferenciaArchivoTo;
import com.mined.siap2.to.TranferenciasGroup;
import gob.mined.siap2.business.datatype.DataTransferencia;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.Beneficiarios;
import gob.mined.siap2.entities.data.impl.DireccionDepartamental;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.data.impl.RelacionPresAnioFinanciamiento;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.Transferencia;
import gob.mined.siap2.entities.data.impl.TransferenciaDireccionDepartamental;
import gob.mined.siap2.entities.data.impl.TransferenciasACed;
import gob.mined.siap2.entities.data.impl.TransferenciasComponente;
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.entities.enums.EstadoTransferenciaACed;
import gob.mined.siap2.entities.enums.EstadoTransferenciaComponente;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroTopePresupuestal;
import gob.mined.siap2.filtros.FiltroTransferenciaComponente;
import gob.mined.siap2.filtros.FiltroTransferencias;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.sofisform.to.CriteriaTO;
import gob.mined.siap2.sofisform.to.MatchCriteriaTO;
import gob.mined.siap2.utils.CriteriaTOUtils;
import gob.mined.siges.entities.data.impl.SgSede;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jboss.ejb3.annotation.TransactionTimeout;


@LocalBean
@Stateless(name = "TransferenciasBean")
public class TransferenciasBean {
    
    private static final Logger LOGGER = Logger.getLogger(TransferenciasBean.class.getName());
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

     @Inject
    private SedeBean sedeBean;
    @Inject
    private CuentasBean cuentasBean;
    
    @Inject
    private TopePresupuestalBean topePresupuestalBean;
    
    @Inject
    private DireccionDepartamentalBean direccionDepartamentalBean;
    
    @Inject
    private TransferenciasComponenteBean transferenciasComponenteBean;
    @Inject
    private TransferenciaDireccionDepartamentalBean transferenciaDireccionDepartamentalBean;
    
    @Inject
    private TransferenciasACedBean transferenciasACedBean;
    
     /**
     * Este método crea o actualiza un registro de Transferencia
     * 
     * @param transf 
     * @return  
     */
    public Transferencia crearActualizar(Transferencia transf) {
        try {
            if(transf.getId() == null){
                return (Transferencia) generalDAO.create(transf);
            }else{
                return (Transferencia) generalDAO.update(transf);
            }
            
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOGeneralException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
    
    public Transferencia crearActualizarTransferencias(Transferencia tr, List<TransferenciasComponente> listaTc) {
        try {
            tr = crearActualizar(tr);

            if(listaTc != null && !listaTc.isEmpty() && listaTc.size() > 0) {
                for(TransferenciasComponente trsc: listaTc) {
                    trsc.setTransferencia(tr);
                }
                transferenciasComponenteBean.crearActualizar(listaTc);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
        return tr;
    }
    
    public void autorizarTransferencias(List<Transferencia> listaTr, List<TransferenciasComponente> listaTc) {
        try {
            
            if(listaTr != null && !listaTr.isEmpty() && listaTr.size() > 0) {
                for(Transferencia trsc: listaTr) {
                    trsc.setEstado(EstadoTransferenciaComponente.AUTORIZADA);
                    crearActualizar(trsc);
                }
            }
            if(listaTc != null && !listaTc.isEmpty() && listaTc.size() > 0) {
                for(TransferenciasComponente trsc: listaTc) {
                    trsc.setEstado(EstadoTransferenciaComponente.AUTORIZADA);
                }
                transferenciasComponenteBean.crearActualizar(listaTc);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Método que se encarga de eliminar un registro de Transferencias
     * @param id Identificador de registro a eliminar
     */
    public void eliminar(Integer id) {
        try {
            Transferencia ai = (Transferencia) generalDAO.find(Transferencia.class, id);
            if (ai != null) {
                generalDAO.delete(ai);
            }
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, null, be);
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }

    
    /**
     * Metodo utilizado para filtrar registros de Transferencias
     * @param filtro
     * @return 
     */
    public List<Transferencia> getTransferenciaByFiltro(FiltroTransferencias filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (filtro.getIdAnioFiscal() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "anioFiscal.id", filtro.getIdAnioFiscal());
                criterios.add(criterio);
            }

            if (filtro.getIdSubcomponente() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "subcomponente.id", filtro.getIdSubcomponente());
                criterios.add(criterio);
            }

            if (filtro.getTipo() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "subcomponente.relacionGesPresEsAnioFiscals.tipo", filtro.getTipo());
                criterios.add(criterio);
            }

            if (filtro.getIdLineaPresupuestaria() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lineaPresupuestaria.id", filtro.getIdLineaPresupuestaria());
                criterios.add(criterio);
            }
            
            if (filtro.getIdSubcomponente() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "subcomponente.id", filtro.getIdSubcomponente());
                criterios.add(criterio);
            }


            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }

            return generalDAO.findEntityByCriteria(Transferencia.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
    

    /**
     * Este método devuelve un registro de Transferencia filtrado por ID
     * @param id
     * @return 
     */
    public Transferencia getTransferenciaById(Integer id) {
        return generalDAO.getEntityManager().find(Transferencia.class, id);
    }
    
    @TransactionTimeout(value = 15, unit = TimeUnit.MINUTES)
    public List<String> crearDesdeArchivo(TranferenciaArchivoTo archivo) {
        List<String> mensajesErrorMontos = new ArrayList();
        try {
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

            RelacionGesPresEsAnioFiscal relPresupuestoAnio = generalDAO.getEntityManager().find(RelacionGesPresEsAnioFiscal.class, archivo.getRelGesPres().getId());
            
            List<RelacionPresAnioFinanciamiento> listaRelFuentes = relPresupuestoAnio.getRelFinanciamiento();
    
            org.apache.poi.ss.usermodel.Workbook myWorkBook = WorkbookFactory.create(archivo.getFile().getInputStream());
            org.apache.poi.ss.usermodel.Sheet mySheet = myWorkBook.getSheetAt(0);

            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();
            List<Transferencia> transferencias = new ArrayList<>();
            List<FuenteRecursos> fuentesRecurso = new ArrayList<>();
            
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
                            tr = getTransferenciaById(transId);
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
                            List<FuenteRecursos> fuentes = generalDAO.findByOneProperty(FuenteRecursos.class, "codigo", fRecurnsoCodigo);
                            if(fuentes != null && !fuentes.isEmpty() && fuentes.size() > 0) {
                                fRecurso = fuentes.get(0);
                            }
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
                                sede = sedeBean.getSedeByCodigo(sedCodigo);
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
                List<TransferenciasComponente> lista = getTransferenciaComponenteByFiltro(filtro);
                if(lista != null && !lista.isEmpty() && lista.size() > 0) {
                    TransferenciasComponente trComp = lista.get(0);
                    trComp.setEstado(EstadoTransferenciaComponente.AUTORIZADA);
                    listaTrComp.add(trComp);
                    
                    BigDecimal montoTotal = BigDecimal.ZERO;
                    //Se clasifican los topes por TDD
                    HashMap<TransferenciaDireccionDepartamental, List<DataTransferencia>> topesPorDepartamento = new HashMap<TransferenciaDireccionDepartamental, List<DataTransferencia>>();
                    for(DataTransferencia tope : listado){
                        DireccionDepartamental ddp = direccionDepartamentalBean.getDireccionDepartamentalByIdDepartamento(tope.getSede().getDireccion().getDepartamento().getPk());
                        TransferenciaDireccionDepartamental tdd = transferenciaDireccionDepartamentalBean.getTransferenciasDirDepaByTransfCompYDirDepa(trComp.getId(), ddp.getPk());
                        if(tdd == null){
                            tdd = nuevoTDD(ddp, trComp);
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
                                aced = crearNuevaTransferenciaACed(tope, trComp);
                            } else {
                                montoAnterior = aced.getMontoAutorizado();
                                aced = actualizarTransferenciaACed(tope, trComp, aced);
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
                                    mensajesErrorMontos.add(tope.getSede().getCodigo() + ". Monto Aprobado del techo: " + montoTope + ". Monto de tranferencias acumuladas: " + montoAcumuladoMostrar +". Monto de tranferencia: " + aced.getMontoAutorizado());
                                }
                            } else {
                                 mensajesErrorMontos.add(tope.getSede().getCodigo() + ". No existe techo aprobado.");
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
            autorizarTransferencias(listaTrans, listaTrComp);
            
        } catch (BusinessException be) {
            mensajesErrorMontos = new ArrayList();
            throw be;
        } catch (DAOObjetoNoEditableException e) {
            mensajesErrorMontos = new ArrayList();
            BusinessException b = new BusinessException();
            b.addError(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO);
            throw b;
        } catch (Exception ex) {
            mensajesErrorMontos = new ArrayList();
            LOGGER.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE);
            ge.addError(ex.getMessage());
            throw ge;
        }
        return mensajesErrorMontos;
    }
    
    public List<TopePresupuestal> obtenerTopesPorTransferenciaComponente(List<TopePresupuestal> topes, TransferenciasComponente tc) {
        List<TopePresupuestal> listado = new ArrayList();
        if(topes != null) {
            for(TopePresupuestal tope : topes) {
                if(tope.getGesPresEs().getId().equals(tc.getSubcomponente().getId())
                        && tope.getFuenteFinanciamiento().getId().equals(tc.getFuenteRecursos().getFuenteFinanciamiento().getId())
                        && tope.getFuenteRecursos().getId().equals(tc.getFuenteRecursos().getId())) {
                    listado.add(tope);
                }
            }
        }
        return listado;
    }
    /**
     * Metodo utilizado para crear un nuevo registro de TransferenciaDireccionDepartamental
     * @param ddp
     * @return 
     */
    public TransferenciaDireccionDepartamental nuevoTDD(DireccionDepartamental ddp, TransferenciasComponente tr){
        TransferenciaDireccionDepartamental tdd = new TransferenciaDireccionDepartamental();
        tdd.setDireccionDep(ddp);
        tdd.setTransferenciasComponente(tr);
        tdd.setEstado(EstadoTransferenciaACed.TRANSFERIDO);
        return tdd;
    }
    
    /**
     * Método utilizado para crear un nuevo objeto de TransferenciaACed
     * 
     * @param tope
     * @return 
     */
    public TransferenciasACed crearNuevaTransferenciaACed(DataTransferencia tope, TransferenciasComponente tr) {
        TransferenciasACed aCed = new TransferenciasACed();
        aCed.setCed(tope.getSede());
        aCed.setEstado(EstadoTransferenciaACed.AUTORIZADO);
        aCed.setTransferencia(tr);
        //aCed.setMontoAutorizado(obtenerPorcentajeSede(tope, tr));
        aCed.setMontoAutorizado(tope.getMonto());
        return aCed;
    }
    /**
     * Método utilizado para crear un nuevo objeto de TransferenciaACed
     * 
     * @param tope
     * @return 
     */
    public TransferenciasACed actualizarTransferenciaACed(DataTransferencia tope,TransferenciasComponente tr, TransferenciasACed aCed) {
        aCed.setCed(tope.getSede());
        aCed.setEstado(EstadoTransferenciaACed.AUTORIZADO);
        aCed.setTransferencia(tr);
        //aCed.setMontoAutorizado(obtenerPorcentajeSede(tope, tr));
        aCed.setMontoAutorizado(tope.getMonto());
        return aCed;
    }
    /**
     * Método utilizado para obtener el porcentaje del monto aprobado de un topePresupuestal, según la transferenciaComponente
     * 
     * @param tope
     * @return 
     */
    public BigDecimal obtenerPorcentajeSede(DataTransferencia tope,TransferenciasComponente tr) throws BusinessException {
        BigDecimal monto = null;
        BusinessException be = new BusinessException();
        try {
            if(tope.getMonto() != null && tope.getMonto().compareTo(BigDecimal.ZERO) > 0){
                if(tr.getPorcentaje()!= null && tr.getPorcentaje().compareTo(BigDecimal.ZERO) > 0){
                    monto = tope.getMonto().multiply(tr.getPorcentaje().divide(new BigDecimal(100)));
                }else{
                    be.addError(ConstantesErrores.ERR_PORCENTAJE_INGRESADO_NO_VALIDO);
                }
            }else{
                be.addError(ConstantesErrores.ERR_MONTO_APROBADO_NO_VALIDO);
            }
            if(tope.getSede() == null){
                be.addError(ConstantesErrores.ERR_CENTRO_EDUCATIVO_NO_ENCONTRADO);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            be.addError(ConstantesErrores.ERROR_GENERAL);
        }
        if(be.getErrores() != null && be.getErrores().size() > 0) {
            throw  be;
        }
        return monto;
    }
    
    /**
     * Metodo utilizado para el filtrado de registros TransferenciaDireccionDepartamental
     * @param filtro
     * @return 
     */
    public List<TransferenciasComponente> getTransferenciaComponenteByFiltro(FiltroTransferenciaComponente filtro) {
        try {
            List<CriteriaTO> criterios = new ArrayList<CriteriaTO>();

            if (filtro.getIdAnioFiscal() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "anioFiscal.id", filtro.getIdAnioFiscal());
                criterios.add(criterio);
            }

            if (filtro.getIdComponente() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "componente.id", filtro.getIdComponente());
                criterios.add(criterio);
            }

            if (filtro.getIdSubcomponente() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "subcomponente.id", filtro.getIdSubcomponente());
                criterios.add(criterio);
            }

            if (filtro.getIdUnidadPresupuestaria() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "unidadPresupuestaria.id", filtro.getIdUnidadPresupuestaria());
                criterios.add(criterio);
            }

            if (filtro.getIdLineaPresupuestaria() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "lineaPresupuestaria.id", filtro.getIdLineaPresupuestaria());
                criterios.add(criterio);
            }

            if (filtro.getIdFuenteFinanciamiento() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuenteFinanciamiento.id", filtro.getIdFuenteFinanciamiento());
                criterios.add(criterio);
            }

            if (filtro.getIdFuenteRecursos() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "fuenteRecursos.id", filtro.getIdFuenteRecursos());
                criterios.add(criterio);
            }

            if (filtro.getIdDireccionDepartamental() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "direccionDepartamental.pk", filtro.getIdDireccionDepartamental());
                criterios.add(criterio);
            }
            
            if (filtro.getIdTransferencia() != null) {
                CriteriaTO criterio = CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "transferencia.id", filtro.getIdTransferencia());
                criterios.add(criterio);
            }
            
            
            CriteriaTO condicion = null;
            if (criterios.size() == 1) {
                condicion = criterios.get(0);
            } else {
                condicion = CriteriaTOUtils.createANDTO(criterios.toArray(new CriteriaTO[0]));
            }
            return generalDAO.findEntityByCriteria(TransferenciasComponente.class, condicion, filtro.getOrderBy(), filtro.getAscending(), filtro.getFirst(), filtro.getMaxResults(), filtro.getIncluirCampos());
        } catch (DAOGeneralException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }

    }
    
    /**
     * Este método agrupa los resultados de TranferenciasGroup
     * @param idAnio
     * @param idCategoria
     * @param idComponente
     * @return 
     * @throws gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException 
     */
    public List<TranferenciasGroup> getTransferenciasAgrupado(Integer idAnio, Integer idCategoria, Integer idComponente) throws DAOGeneralException{
        try {
            List<TranferenciasGroup> listado = new ArrayList<TranferenciasGroup>();
            StringBuilder builder = new StringBuilder();
            builder.append(
                "SELECT cp.cu_codigo,cp.cu_nombre,sc.cu_codigo,sc.cu_nombre, sum(tddep.tdd_monto_autorizado) "+
                "FROM " + Constantes.SCHEMA_FINANZAS +".sg_transferencia_direccion_departamental tddep "+
                "inner join " +  Constantes.SCHEMA_SIAP2 +".ss_transferencias_componente tc on (tc.tc_id = tddep.tdd_transferencia_fk) "+
                "inner join " + Constantes.SCHEMA_SIAP2 + ".ss_transferencias t on (tc.tc_transferencia = t.tra_id) "+        
                "inner join " + Constantes.SCHEMA_SIAP2 + ".ss_cuentas sc ON (sc.cu_id = tc.tc_linea_presupuestaria) "+
                "left join " + Constantes.SCHEMA_SIAP2 +".ss_cuentas cp ON (sc.cu_cuenta_padre = cp.cu_id) "+
                "left join " + Constantes.SCHEMA_SIAP2 + ".ss_ges_pres_es gpe ON (gpe.ges_id = t.tra_id_subcomponente)  "+
                "left join " + Constantes.SCHEMA_SIAP2 + ".ss_categoria_presupuesto_escolar cpe ON (cpe.cpe_id = gpe.ges_categoria_componente) "+
                "WHERE 1 = 1 ");
                if(idAnio != null && idAnio != 0)
                    builder.append("AND tc.tc_anio_fiscal = ?1 ");
                if(idComponente != null && idComponente != 0)
                    builder.append("AND tc.tc_subcomponente = ?2 ");
                if(idCategoria != null && idCategoria != 0)
                    builder.append("AND cpe.cpe_id = ?3 ");
                builder.append(" GROUP BY cp.cu_codigo,cp.cu_nombre,sc.cu_codigo, sc.cu_nombre ");
                
                Query q = generalDAO.getEntityManager().createNativeQuery(builder.toString());
                if(idAnio != null && idAnio != 0)
                    q.setParameter("1", idAnio);
                if(idComponente != null && idComponente != 0)
                    q.setParameter("2", idComponente);
                if(idCategoria != null && idCategoria != 0)
                    q.setParameter("3", idCategoria);
 
            TranferenciasGroup tp;
            for(Object obj : q.getResultList()){
                Object[] obar = (Object[]) obj;
                tp = new TranferenciasGroup();
                tp.setCodigoCuenta((String) obar[0]);
                tp.setNombreCuenta((String) obar[1]);
                tp.setCodigoSubCuenta((String) obar[2]);
                tp.setNombreSubCuenta((String) obar[3]);
                tp.setMontoAprobado((BigDecimal) obar[4]);
                listado.add(tp);
            }
            return listado;
        } catch (Exception e) {
            throw e;
        }
    }
}
