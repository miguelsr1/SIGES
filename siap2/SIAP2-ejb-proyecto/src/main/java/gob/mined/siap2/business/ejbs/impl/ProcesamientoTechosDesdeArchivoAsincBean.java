/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.ejbs.impl;

import com.mined.siap2.to.TopePresupuestalArchivoTo;
import gob.mined.siap2.business.datatype.DataValoresMontos;
import gob.mined.siap2.business.ejbs.IdiomaBean;
import gob.mined.siap2.business.ejbs.TextoBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.entities.data.impl.Cuentas;
import gob.mined.siap2.entities.data.impl.FuenteRecursos;
import gob.mined.siap2.entities.data.impl.Notificacion;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.data.impl.RelacionPresAnioFinanciamiento;
import gob.mined.siap2.entities.data.impl.TopePresupuestal;
import gob.mined.siap2.entities.data.impl.TopePresupuestalGroup;
import gob.mined.siap2.entities.enums.EnumEstadoOrganismoAdministrativo;
import gob.mined.siap2.entities.enums.EstadoPresupuestoEscolar;
import gob.mined.siap2.entities.enums.EstadoTopePresupuestal;
import gob.mined.siap2.entities.enums.TipoMonto;
import gob.mined.siap2.entities.enums.TipoNotificacion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.filtros.FiltroRelPresAnioFinanciamiento;
import gob.mined.siap2.filtros.FiltroTopePresupuestal;
import gob.mined.siap2.persistence.dao.exceptions.DAOGeneralException;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import gob.mined.siap2.persistence.dao.imp.PresupuestoEscolarDAO;
import gob.mined.siges.entities.data.impl.SgOrganismoAdministracionEscolar;
import gob.mined.siges.entities.data.impl.SgPresupuestoEscolar;
import gob.mined.siges.entities.data.impl.SgSede;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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

@Stateless(name = "procesamientoTechosDesdeArchivoAsincInterface")
@Remote(ProcesamientoTechosDesdeArchivoAsincInterface.class)
@Asynchronous
@LocalBean
public class ProcesamientoTechosDesdeArchivoAsincBean implements ProcesamientoTechosDesdeArchivoAsincInterface{
    private static final Logger LOGGER = Logger.getLogger(ProcesamientoTechosDesdeArchivoAsincBean.class.getName());

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @Inject
    private TopePresupuestalBean topeBean;

    @Inject
    private SedeBean sedeBean;

    @Inject
    private CuentasBean cuentasBean;
    
    @Inject
    private IdiomaBean idiBean;
    
    @Inject
    private TextoBean textoBean;
    
    @Inject
    private PresupuestoFuentesFinanciamientoBean relPresAnioFinanciamientoBean;
    
    @Inject
    private FuentesRecursosBean fuentesRecursosBean;
    
    @Inject
    @JPADAO
    private PresupuestoEscolarDAO presupuestoEscolarDAO;
    
    
    
    @Asynchronous
    @TransactionTimeout(unit=TimeUnit.MINUTES, value=90)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Override
    public void crearDesdeArchivo(TopePresupuestalArchivoTo archivo) {
        String contenidoNotificacion = "Errores en la ejecución de proceso de generación de techos por archivo.";
        String contenidoNotificacionAdvertencias = "Aunque las sedes siguientes no crearon o actualizaron sus techos presupuestales por las razones detalladas a continuación.";
        List<String> errores = new ArrayList();
        String advertencia = "";
        String presupuesto = "";
        //SsUsuario usuario = null;
        Integer idiomaId = 1;
        Boolean existoso = false;
        List<SsUsuario> usuarios = archivo.getUsuarios();
        RelacionGesPresEsAnioFiscal relPresupuestoAnio = null;
        try {
            Thread.sleep(5000);//5 segundos de espera
            
            idiomaId = idiBean.obtenerIdiomaEspaniol().getIdiId();
            
            Map<Cuentas, DataValoresMontos> topesSegunCuentas = new HashMap();
            Map<RelacionPresAnioFinanciamiento, DataValoresMontos> topesSegunFuentesRecursos = new HashMap();
            List <RelacionPresAnioFinanciamiento> listaSegunFuentesRecursos = new ArrayList();
            BusinessException be = new BusinessException();
            if (archivo.getAnioFiscal() == null) {
                be.addError(ConstantesErrores.ERR_DEBE_INGRESAR_AÑO_FISCAL);
            }
            
            if (archivo.getGesPresEs() == null || archivo.getRelGesPres() == null) {
                be.addError(ConstantesErrores.ERR_COMPONENTE_PRESUPUESTO_ESCOLAR_VACIO);
            }            
            if (archivo.getFile() == null) {
                be.addError(ConstantesErrores.ERR_DEBE_SELECIONAR_ARCHIVO);
            }
            if(archivo.getTipoMonto() == null){
                be.addError(ConstantesErrores.ERR_TIPO_MONTO_NO_INGRESADO);
            }
            
            if (!be.getErrores().isEmpty()) {
                throw be;
            }
            relPresupuestoAnio = generalDAO.getEntityManager().find(RelacionGesPresEsAnioFiscal.class, archivo.getRelGesPres().getId());

            if(relPresupuestoAnio != null) {
                relPresupuestoAnio.setMontoTotalAprobado(new BigDecimal(BigInteger.ZERO));
                relPresupuestoAnio.setCantidadMatriculasAprobadas(0);
                if (archivo.getTipoMonto().equals(TipoMonto.APROBADO)) {
                    topeBean.actualizarTopesPorRelGesPres(archivo.getRelGesPres().getId());
                } else {
                    topeBean.eliminarTopesPorRelGesPres(archivo.getRelGesPres().getId());
                    relPresupuestoAnio.setMontoTotalFormulado(new BigDecimal(BigInteger.ZERO));
                    relPresupuestoAnio.setCantidadMatriculas(0);
                }
                if(usuarios == null) {
                    usuarios = new ArrayList();
                }
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
            if(listaRelFuentes == null || listaRelFuentes.isEmpty()) {
                be.addError(ConstantesErrores.ERR_PRESUPUESTO_SIN_FUENTES_FINANCIAMIENTO, new String[]{relPresupuestoAnio.getDescripcion().trim()});
                throw be;
            }
            
            
            if(relPresupuestoAnio != null) {
                for(RelacionPresAnioFinanciamiento rel : listaRelFuentes) {
                    if(archivo.getTipoMonto().equals(TipoMonto.FORMULADO)) {
                        rel.setMontoTotalAprobado(BigDecimal.ZERO);
                        rel.setMontoTotalFormulado(BigDecimal.ZERO);
                    } else if (archivo.getTipoMonto().equals(TipoMonto.APROBADO)) {
                        rel.setMontoTotalAprobado(BigDecimal.ZERO);
                    }
                    listaSegunFuentesRecursos.add(rel);
                    topesSegunFuentesRecursos.put(rel, new DataValoresMontos(BigDecimal.ZERO, BigDecimal.ZERO));
                }
            }
            
            
            
            org.apache.poi.ss.usermodel.Workbook myWorkBook = WorkbookFactory.create(archivo.getFile().getInputStream());
            org.apache.poi.ss.usermodel.Sheet mySheet = myWorkBook.getSheetAt(0);

            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();
            List<Cuentas> cuentas = new ArrayList<>();
            List<FuenteRecursos> fuentesRecurso = new ArrayList<>();
            
            List<TopePresupuestal> nuevosTopes = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (row.getRowNum() == 0) {
                    //Obtener cuentas
                    Iterator<Cell> cellIterator = row.cellIterator();

                    if (cellIterator.hasNext()) {
                        cellIterator.next(); // Skip primera columna
                    }

                    whileCuentas:
                    while (cellIterator.hasNext()) {
                        String cuentaCodigo = topeBean.getValor(cellIterator.next());
                        Cuentas cuenta = null;
                        LOGGER.info("año:" + relPresupuestoAnio.getAnioFiscal().getId());
                        LOGGER.info("cuentaCodigo:" + cuentaCodigo.trim());
                        
                        if (!StringUtils.isBlank(cuentaCodigo)) {
                            cuenta = cuentasBean.getCuentaByCodigoYAnioFiscal(cuentaCodigo.trim(), relPresupuestoAnio.getAnioFiscal().getId());
                        } else {
                            break whileCuentas;
                        }
                        
                        if (cuenta == null || cuenta.getCuentaPadre() == null) {
                           LOGGER.info("cuenta:" + cuenta + ", cuenta.getCuentaPadre(): " + cuenta.getCuentaPadre());
                            //Solamente subcuentas
                            be.addError(ConstantesErrores.ERR_CODIGO_SUBCUENTA_INEXISTENTE, new String[]{cuentaCodigo.trim()});
                            throw be;
                        }
                        if(!relPresupuestoAnio.getSubCuenta().getCodigo().equals(cuenta.getCodigo())) {
                            be.addError(ConstantesErrores.ERR_CODIGO_SUBCUENTA_INEXISTENTE_EN_PRESUPUESTO, new String[]{cuentaCodigo,relPresupuestoAnio.getDescripcion().trim()});
                            throw be;
                        }
                        cuentas.add(cuenta);
                    }

                } if (row.getRowNum() == 1) {
                    //Obtener fuentes de financiamiento
                    Iterator<Cell> cellIterator = row.cellIterator();

                    if (cellIterator.hasNext()) {
                        cellIterator.next(); // Skip primera columna
                    }

                    whileFuentesFinanciamiento:
                    while (cellIterator.hasNext()) {
                        String fRecurnsoCodigo = topeBean.getValor(cellIterator.next());
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
                                if(fRecurso.getId().equals(relFuente.getFuenteRecursos().getId())
                                        && fRecurso.getFuenteFinanciamiento().getId().equals(relFuente.getFuenteRecursos().getFuenteFinanciamiento().getId())) {
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
                            String sedCodigo = topeBean.getValor(cellIterator.next());
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
                                        BigDecimal monto = topeBean.getValorBigDecimal(cell);

                                        Integer cuentaIndex = cell.getColumnIndex() - 1;

                                        if (cuentaIndex >= cuentas.size()) {
                                            continue;
                                        }
                                        Boolean agregar = false;
                                        if(sede.getOrganismosAdministracionEscolar() != null && !sede.getOrganismosAdministracionEscolar().isEmpty()) {
                                            for(SgOrganismoAdministracionEscolar org : sede.getOrganismosAdministracionEscolar()) {
                                            
                                                if(org.getOaeMiembrosVigentes() != null && org.getOaeMiembrosVigentes() && org.getOeaEstado() != null && org.getOeaEstado().equals(EnumEstadoOrganismoAdministrativo.APROBADO)) {
                                                    agregar = true; 
                                                    break;
                                                }
                                            }
                                        }
                                        /**
                                        SgPresupuestoEscolar pe = presupuestoEscolarDAO.getPresupuestoEscolarByAnioYSede(archivo.getAnioFiscal().getAnio(), sede.getId());
                                        
                                       
                                        if(pe != null && pe.getEstado() != null && pe.getEstado().equals(EstadoPresupuestoEscolar.FORMULADO)) {
                                            agregar = false; 
                                        }**/
                                       
                                       // if(agregar) {
                                            TopePresupuestal tope = new TopePresupuestal();
                                            tope.setAnioFiscal(archivo.getAnioFiscal());
                                            tope.setGesPresEs(archivo.getGesPresEs());
                                            tope.setSede(sede);
                                            tope.setSubCuenta(cuentas.get(cuentaIndex));
                                            tope.setFuenteRecursos(fuentesRecurso.get(cuentaIndex));
                                            tope.setFuenteFinanciamiento(fuentesRecurso.get(cuentaIndex).getFuenteFinanciamiento());

                                            if(archivo.getTipoMonto().equals(TipoMonto.APROBADO)){
                                                tope.setMontoAprobado(monto);
                                                tope.setEstado(EstadoTopePresupuestal.APROBACION);
                                            }else if(archivo.getTipoMonto().equals(TipoMonto.FORMULADO)){
                                                tope.setMonto(monto);
                                                tope.setEstado(EstadoTopePresupuestal.EN_PROCESO);
                                            }

                                            nuevosTopes.add(tope);
                                            DataValoresMontos val = null;
                                            if(topesSegunCuentas.containsKey(cuentas.get(cuentaIndex))) {
                                                val = topesSegunCuentas.get(cuentas.get(cuentaIndex));

                                                BigDecimal mt1 = tope.getMonto() != null ? tope.getMonto() : new BigDecimal(BigInteger.ZERO);
                                                BigDecimal mt2 = tope.getMontoAprobado() != null ? tope.getMontoAprobado() : new BigDecimal(BigInteger.ZERO);


                                                BigDecimal mont = val.getMonto() != null ? val.getMonto() : new BigDecimal(BigInteger.ZERO);
                                                BigDecimal montAprobado = val.getMontoAprobado() != null ? val.getMontoAprobado() : new BigDecimal(BigInteger.ZERO);

                                                val.setMonto(mt1.add(mont));
                                                val.setMontoAprobado(mt2.add(montAprobado));
                                                topesSegunCuentas.put(cuentas.get(cuentaIndex), val);
                                            } else {
                                                val = new DataValoresMontos(tope.getMonto(), tope.getMontoAprobado());
                                                topesSegunCuentas.put(cuentas.get(cuentaIndex), new DataValoresMontos(tope.getMonto(), tope.getMontoAprobado()));
                                            }

                                            RelacionPresAnioFinanciamiento f = topeBean.obtenerRelacionPresAnioFinanciamiento(listaSegunFuentesRecursos, relPresupuestoAnio.getId(), 
                                                    fuentesRecurso.get(cuentaIndex).getFuenteFinanciamiento().getId(), 
                                                    fuentesRecurso.get(cuentaIndex).getId());

                                            if(f != null) {
                                                if(topesSegunFuentesRecursos.containsKey(f)) {
                                                    DataValoresMontos d = topesSegunFuentesRecursos.get(f);

                                                    BigDecimal mt1 = tope.getMonto() != null ? tope.getMonto() : new BigDecimal(BigInteger.ZERO);
                                                    BigDecimal mt2 = tope.getMontoAprobado() != null ? tope.getMontoAprobado() : new BigDecimal(BigInteger.ZERO);


                                                    BigDecimal mont = d.getMonto() != null ? d.getMonto() : new BigDecimal(BigInteger.ZERO);
                                                    BigDecimal montAprobado = d.getMontoAprobado() != null ? d.getMontoAprobado() : new BigDecimal(BigInteger.ZERO);


                                                    d.setMonto(mt1.add(mont));
                                                    d.setMontoAprobado(mt2.add(montAprobado));

                                                    topesSegunFuentesRecursos.put(f, d);

                                                }
                                            }
                                        }
                                        
                                        
                                    //}
                                }
                            }
                        }
                    catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Error al leer la celda", e);
                    }
                }

            }

            for (TopePresupuestal t : nuevosTopes) {
                //Buscar tope existente y actualizar
                
                FiltroTopePresupuestal filtro = new FiltroTopePresupuestal();

                filtro.setAnioFiscalId(t.getAnioFiscal().getId());
                filtro.setComponentePresupuestoEscolarId(t.getGesPresEs().getId());
                filtro.setCuentaId(t.getSubCuenta().getId());
                filtro.setSedeId(t.getSede().getId());
                filtro.setCompPresAnioFiscalId(relPresupuestoAnio.getId());
                filtro.setFuenteFinId(t.getFuenteFinanciamiento().getId());
                filtro.setFuenteRecursoId(t.getFuenteRecursos().getId());
                
                Boolean guardar = Boolean.FALSE;
                
                SgPresupuestoEscolar pe = presupuestoEscolarDAO.getPresupuestoEscolarByAnioYSede(archivo.getAnioFiscal().getAnio(), t.getSede().getId());
                if(pe == null) {
                     guardar = Boolean.TRUE; //para guardar los datos cuando no existe presupuesto(FORMULADO Y APROBADO)
                }
                
                
                List<TopePresupuestal> existentes = topeBean.getTopesPresupuestalesByFiltro(filtro);
                
                if (existentes.isEmpty()) {//Si es nuevo, se agrega solo si no está en estado FORMULADO 
                    
                    if(pe != null) {
                        if(archivo.getTipoMonto().equals(TipoMonto.FORMULADO)){
                            if(pe != null && pe.getEstado() != null && 
                                (pe.getEstado().equals(EstadoPresupuestoEscolar.EN_PROCESO) 
                                || pe.getEstado().equals(EstadoPresupuestoEscolar.FORMULADO))) {//SE GUARDA SOLO EN CASO QUE EL ESTADO ESTE EN PROCESO O FORMULADO
                                guardar = Boolean.TRUE;
                            }
                        } else if(archivo.getTipoMonto().equals(TipoMonto.APROBADO)){
                            if(pe != null && pe.getEstado() != null && (pe.getEstado().equals(EstadoPresupuestoEscolar.EN_PROCESO)
                                || pe.getEstado().equals(EstadoPresupuestoEscolar.FORMULADO)
                                || pe.getEstado().equals(EstadoPresupuestoEscolar.EN_AJUSTE)
                                || pe.getEstado().equals(EstadoPresupuestoEscolar.APROBADO)
                               // || pe.getEstado().equals(EstadoPresupuestoEscolar.EDITADO)
                                || pe.getEstado().equals(EstadoPresupuestoEscolar.AJUSTADO))) {//SE GUARDAR EN CASO QUE EL ESTADO EN_PROCESO,FORMULADO, EN_AJUSTE, APROBADO,EDITADO Y AJUSTADO
                                guardar = Boolean.TRUE;
                            }
                         }
                    }
                } else {
                    TopePresupuestal existente = existentes.get(0);
                    if(pe != null) {
                        if(archivo.getTipoMonto().equals(TipoMonto.FORMULADO)){
                            if(pe != null && pe.getEstado() != null && 
                                (pe.getEstado().equals(EstadoPresupuestoEscolar.EN_PROCESO) 
                                || pe.getEstado().equals(EstadoPresupuestoEscolar.FORMULADO))) {//SE GUARDA SOLO EN CASO QUE EL ESTADO ESTE EN PROCESO O FORMULADO
                                guardar = Boolean.TRUE;
                                existente.setMonto(t.getMonto());
                                existente.setEstado(EstadoTopePresupuestal.EN_PROCESO);
                            }
                        } else if(archivo.getTipoMonto().equals(TipoMonto.APROBADO)){
                            if(pe != null && pe.getEstado() != null && (pe.getEstado().equals(EstadoPresupuestoEscolar.EN_PROCESO)
                                || pe.getEstado().equals(EstadoPresupuestoEscolar.FORMULADO)
                                || pe.getEstado().equals(EstadoPresupuestoEscolar.EN_AJUSTE)
                                //|| pe.getEstado().equals(EstadoPresupuestoEscolar.APROBADO)
                                //|| pe.getEstado().equals(EstadoPresupuestoEscolar.EDITADO)
                                || pe.getEstado().equals(EstadoPresupuestoEscolar.AJUSTADO))) { //SE GUARDAR EN CASO QUE EL ESTADO EN_PROCESO,FORMULADO, EN_AJUSTE, APROBADO,EDITADO Y AJUSTADO
                                existente.setMontoAprobado(t.getMontoAprobado());
                                existente.setEstado(EstadoTopePresupuestal.APROBACION);
                                guardar = Boolean.TRUE;
                            } else  if(pe != null && pe.getEstado() != null && pe.getEstado().equals(EstadoPresupuestoEscolar.APROBADO)) {//SE GUARDAR EN CASO EL ESTADO ESTE APROBADO Y EL MONTO HAYA CAMBIADO
                                if(existente.getMontoAprobado() == null) {
                                    guardar = Boolean.TRUE;
                                } else if(existente.getMontoAprobado().compareTo(t.getMontoAprobado()) != 0) {
                                        existente.setMontoAprobado(t.getMontoAprobado());
                                        existente.setEstado(EstadoTopePresupuestal.APROBACION);
                                        guardar = Boolean.TRUE;
                                    }
                                }
                        }
                    } else {
                        if(archivo.getTipoMonto().equals(TipoMonto.FORMULADO)){
                            existente.setMonto(t.getMonto());
                            existente.setEstado(EstadoTopePresupuestal.EN_PROCESO);
                        } else if(archivo.getTipoMonto().equals(TipoMonto.APROBADO)){
                            existente.setMontoAprobado(t.getMontoAprobado());
                            existente.setEstado(EstadoTopePresupuestal.APROBACION);
                        }
                    }
  
                    existente.setFuenteRecursos(t.getFuenteRecursos());
                    existente.setFuenteFinanciamiento(t.getFuenteFinanciamiento());
                    
                    
                    t = existente;
                }
                
                if(guardar) {
                    t.setRelGesPres(relPresupuestoAnio);
                    topeBean.crearActualizarTopeMovimiento(t, archivo.getTipoMonto());
                } else {
                    if(pe != null && pe.getEstado() != null) {
                        advertencia = "Sede " + t.getSede().getCodigo() + " para el año " + t.getAnioFiscal().getAnio() + " tiene presupuesto en estado " + pe.getEstado().getText()+".";
                        errores.add(advertencia);
                    }
                    
                }
            }

            //OBTENER AQUI LA SUMA DE LOS MONTOS POR SUBCUENTAS PARA ACTUALIZAR, ESTO DADO QUE HAY TECHOS QUE EXISTEN PERO NO SE TOMAN EN CUENTA PARA ACTUALIZAR EN ESTE PROCESO.
            List<TopePresupuestalGroup> topesPorFuentes = topeBean.getTopePresupuestalFuentesAgrupado(relPresupuestoAnio.getId());
            //for(listaSegunFuentesRecursos)
            BigDecimal totalMontoFormulado = BigDecimal.ZERO;
            BigDecimal totalMontoAprobado = BigDecimal.ZERO;
            for(RelacionPresAnioFinanciamiento rfin : listaSegunFuentesRecursos) {
                for(TopePresupuestalGroup g: topesPorFuentes) {
                    if(g.getIdFuente().equals(rfin.getFuenteFinanciamiento().getId()) && g.getIdFuenteRecursos().equals(rfin.getFuenteRecursos().getId())) {
                        rfin.setMontoTotalFormulado(g.getMontoFormulacion());
                        rfin.setMontoTotalAprobado(g.getMontoAprobado());
                        
                        totalMontoFormulado = totalMontoFormulado.add(g.getMontoFormulacion());
                        totalMontoAprobado = totalMontoAprobado.add(g.getMontoAprobado());
                    }
                }
            }
            relPresupuestoAnio.setRelFinanciamiento(listaSegunFuentesRecursos);
            relPresupuestoAnio.setMontoTotalFormulado(totalMontoFormulado);
            relPresupuestoAnio.setMontoTotalAprobado(totalMontoAprobado);
            existoso = true;
        }  catch (BusinessException be) {
            existoso = false;
            for(String error : be.getErrores()) {
                String mensaje = textoBean.obtenerMensajeByProperty(error, idiomaId);
                //String mensaje = error;
                contenidoNotificacion += "-" + mensaje;
            }
            LOGGER.log(Level.SEVERE, null, be);
        } catch (DAOObjetoNoEditableException e) {
            existoso = false;
            String mensaje = textoBean.obtenerMensajeByProperty(ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO, idiomaId);
            //String mensaje = ConstantesErrores.ERR_SOLO_EDITABLE_LINEA_TRABAJO;
            contenidoNotificacion += "-" + mensaje;
            
            LOGGER.log(Level.SEVERE, null, e);
            //throw b;
        } catch (Exception ex) {
            existoso = false;
            LOGGER.log(Level.SEVERE, null, ex);
            String mensaje = textoBean.obtenerMensajeByProperty(ConstantesErrores.ERR_GNRAL_SAVE_UPDATE, idiomaId);
            contenidoNotificacion += "-" + mensaje;

        } finally {
            try {
                relPresupuestoAnio.setProcesoEnCurso(Boolean.FALSE);
                generalDAO.updateNew(relPresupuestoAnio);
                
                if(existoso) {
                    if(archivo.getTipoMonto().equals(TipoMonto.FORMULADO)) {
                        contenidoNotificacion = "La generación de techos por archivo para FORMULACIÓN del presupuesto " + presupuesto + " se ejecutó correctamente.";
                    } else if(archivo.getTipoMonto().equals(TipoMonto.APROBADO)) {
                        contenidoNotificacion = "La generación de techos por archivo para APROBACIÓN del presupuesto " + presupuesto + " se ejecutó correctamente.";
                    }
                   if(errores != null && !errores.isEmpty() && errores.size() > 0) {
                       contenidoNotificacion += contenidoNotificacionAdvertencias;
                       for(String err : errores) {
                           contenidoNotificacion += "-" + err;
                       }
                   }
                }
                
                if(usuarios != null && !usuarios.isEmpty() && usuarios.size() > 0) {
                    for(SsUsuario usu : usuarios) {
                        Notificacion n = new Notificacion();
                        n.setTipo(TipoNotificacion.EJECUCION_PROCESO_TECHOS_ARCHIVO);
                        n.setUsuario(usu);
                        n.setFecha(new Date());
                        n.setContenido(contenidoNotificacion);
                        n.setObjetoId(relPresupuestoAnio.getComponentePresupuestoEscolar().getId());
                        generalDAO.updateNew(n);
                    }
                }
                
            } catch (DAOGeneralException ex) {
                LOGGER.log(Level.SEVERE, "Error al procesar archivo de tranferencias", ex);
            }
        }
        //return new AsyncResult<Void>(null);
    }
    
    
    
    
}
