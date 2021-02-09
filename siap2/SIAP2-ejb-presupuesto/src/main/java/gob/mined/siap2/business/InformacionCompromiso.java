/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business;

import com.mined.siap2.to.CompromisoArchivoTO;
import gob.mined.siap2.business.ejbs.impl.ArchivoBean;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.CompromisoSafi;
import gob.mined.siap2.entities.data.impl.InfComprometido;
import gob.mined.siap2.entities.data.impl.InfCompromiso;
import gob.mined.siap2.entities.data.impl.InfDevengado;
import gob.mined.siap2.entities.data.impl.InfPagado;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Esta clase implementa los métodos correspondientes al seguimiento financiero.
 * @author sofis
 */
@Stateless(name = "InformacionCompromiso")
@LocalBean
public class InformacionCompromiso {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    @Inject
    private ArchivoBean archivoBean;

    /**
     * Guarda el infCompromiso.
     * @param infCompromiso
     * @return 
     */
    public InfCompromiso saveArchivo(InfCompromiso infCompromiso) {

        try {
            
            
            //Inicializo las colecciones
            if (infCompromiso.getComprometidos() == null){
                infCompromiso.setComprometidos(new ArrayList<InfComprometido>());
            }
            if (infCompromiso.getDevengados() == null){
                infCompromiso.setDevengados(new ArrayList<InfDevengado>());
            }
            if (infCompromiso.getPagados() == null){
                infCompromiso.setPagados(new ArrayList<InfPagado>());
            }
            //fin inicializo las colecciones
            
            
            if (infCompromiso.getTempUploadedFile() != null) {
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                org.apache.commons.io.IOUtils.copy(infCompromiso.getTempUploadedFile().getInputStream(), baos);
                byte[] bytes = baos.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

                Workbook workbook = WorkbookFactory.create(bais);
                Sheet sheet = null;
                int indComienzo = 8;//primer fila con datos de tabla (registros)
                
                //---- Comienzo con 1era hoja-------------------------------------------------------
                sheet = workbook.getSheetAt(0);//Primer hoja - Comprometido.
                
                //Cargo valores del cabezal
                infCompromiso.setComprometidoFechaGenera(new Date());
                if (sheet.getRow(3)!=null){
                    infCompromiso.setComprometidoEjercicio(getValor(sheet.getRow(3).getCell(1)));
                }
                if (sheet.getRow(4)!=null){
                    infCompromiso.setComprometidoInstitucion(getValor(sheet.getRow(4).getCell(1)));
                }
                if (sheet.getRow(5)!=null){
                    infCompromiso.setComprometidoUnidad(getValor(sheet.getRow(5).getCell(1)));
                }


                //Cargo valores de registros
                for(int i = indComienzo;i<=sheet.getLastRowNum();i++){
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        if (row.getCell(0) == null || row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK){
                            break;//Líneas en blanco. Finalizo carga
                        }
                        InfComprometido infComprometido = new InfComprometido();
                        infComprometido.setCompromiso(infCompromiso);

                        infComprometido.setAnio(getValor(row.getCell(0)));
                        infComprometido.setNroComprobante(getValor(row.getCell(1)));
                        infComprometido.setItem(getValor(row.getCell(2)));
                        infComprometido.setFechaElaborac(getValorFecha(row.getCell(3)));
                        infComprometido.setNroDResp(getValor(row.getCell(4)));
                        infComprometido.setNit(getValor(row.getCell(5)));
                        infComprometido.setProveedor(getValor(row.getCell(6)));
                        infComprometido.setLt(getValor(row.getCell(7)));
                        infComprometido.setFf(getValor(row.getCell(8)));
                        infComprometido.setProyecto(getValor(row.getCell(9)));
                        infComprometido.setFr(getValor(row.getCell(10)));
                        infComprometido.setAo(getValor(row.getCell(11)));
                        infComprometido.setEspec(getValor(row.getCell(12)));
                        infComprometido.setMes(getValor(row.getCell(13)));
                        infComprometido.setComprometido(getValorBigDecimal(row.getCell(14)));
                        infComprometido.setDescomp(getValorBigDecimal(row.getCell(15)));
                        infComprometido.setCongelado(getValorBigDecimal(row.getCell(16)));
                        infComprometido.setDisponible(getValorBigDecimal(row.getCell(17)));
                        infComprometido.setConcepto(getValor(row.getCell(18)));
                        infComprometido.setClaveJoin(infComprometido.getNroDResp()+"I"+infComprometido.getItem());

                        infCompromiso.getComprometidos().add(infComprometido);
                    }
                }
                
                //---- Comienzo con 2da hoja-------------------------------------------------------
                sheet = workbook.getSheetAt(1);//Segunda hoja - Devengado.
                
                //Cargo valores del cabezal
                if (sheet.getRow(1)!=null){
                    infCompromiso.setDevengadoPeriodo(getValor(sheet.getRow(1).getCell(1)));
                }
                infCompromiso.setDevengadoFechaGenera(new Date());
                if (sheet.getRow(3)!=null){
                    infCompromiso.setDevengadoEjercicio(getValor(sheet.getRow(3).getCell(1)));
                }
                if (sheet.getRow(4)!=null){
                    infCompromiso.setDevengadoInstitucion(getValor(sheet.getRow(4).getCell(1)));
                }
                if (sheet.getRow(5)!=null){
                    infCompromiso.setDevengadoUnidad(getValor(sheet.getRow(5).getCell(1)));
                }
                
                //Cargo valores de registro                
                for(int i = indComienzo;i<=sheet.getLastRowNum();i++){
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        if (row.getCell(0) == null || row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK){
                            break;//Líneas en blanco. Finalizo carga
                        }
                        InfDevengado infDevengado = new InfDevengado();
                        infDevengado.setCompromiso(infCompromiso);
                        
                        infDevengado.setEjercicio(getValor(row.getCell(0)));
                        infDevengado.setNit(getValor(row.getCell(1)));
                        infDevengado.setNombre(getValor(row.getCell(2)));
                        infDevengado.setNroDocuOrig(getValor(row.getCell(3)));
                        infDevengado.setItemOblig(getValor(row.getCell(4)));
                        infDevengado.setProyecto(getValor(row.getCell(5)));
                        infDevengado.setFr(getValor(row.getCell(6)));
                        infDevengado.setAo(getValor(row.getCell(7)));
                        infDevengado.setUpLt(getValor(row.getCell(8)));
                        infDevengado.setFf(getValor(row.getCell(9)));
                        infDevengado.setCompromisoTxt(getValor(row.getCell(10)));
                        infDevengado.setItem(getValor(row.getCell(11)));
                        infDevengado.setMesDevengado(getValor(row.getCell(12)));
                        infDevengado.setDevengado(getValorBigDecimal(row.getCell(13)));
                        infDevengado.setConcepto(getValor(row.getCell(14)));
                        infDevengado.setClaveJoin(infDevengado.getNroDocuOrig()+"I"+infDevengado.getItem());

                        infCompromiso.getDevengados().add(infDevengado);
                    }
                }
                
                //---- Comienzo con 3ra hoja-------------------------------------------------------
                
                sheet = workbook.getSheetAt(2);//Tercera hoja - Pagado.
                
                
                //Cargo valores del cabezal
                if (sheet.getRow(1)!=null){
                    infCompromiso.setPagadoPeriodo(getValor(sheet.getRow(1).getCell(1)));
                }
                infCompromiso.setPagadoFechaGenera(new Date());
                if (sheet.getRow(3)!=null){
                    infCompromiso.setPagadoEjercicio(getValor(sheet.getRow(3).getCell(1)));
                }
                if (sheet.getRow(4)!=null){
                    infCompromiso.setPagadoInstitucion(getValor(sheet.getRow(4).getCell(1)));
                }
                if (sheet.getRow(5)!=null){
                    infCompromiso.setPagadoUnidad(getValor(sheet.getRow(5).getCell(1)));
                }
                
                
                //Cargo valores de registro
                for(int i = indComienzo;i<=sheet.getLastRowNum();i++){
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        if (row.getCell(0) == null || row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK){
                            break;//Líneas en blanco. Finalizo carga
                        }
                        InfPagado infPagado = new InfPagado();
                        infPagado.setCompromiso(infCompromiso);
                        
                        infPagado.setEjercicio(getValor(row.getCell(0)));
                        infPagado.setNit(getValor(row.getCell(1)));
                        infPagado.setNombre(getValor(row.getCell(2)));
                        infPagado.setNroDocuOrig(getValor(row.getCell(3)));
                        infPagado.setItemPago(getValor(row.getCell(4)));
                        infPagado.setProyecto(getValor(row.getCell(5)));
                        infPagado.setUpLt(getValor(row.getCell(6)));
                        infPagado.setAo(getValor(row.getCell(7)));
                        infPagado.setFr(getValor(row.getCell(8)));
                        infPagado.setFf(getValor(row.getCell(9)));
                        infPagado.setMesPagado(getValor(row.getCell(10)));
                        infPagado.setPagado(getValorBigDecimal(row.getCell(11)));
                        infPagado.setConcepto(getValor(row.getCell(12)));
                        infPagado.setOtra1(getValor(row.getCell(13)));
                        infPagado.setOtra2(getValor(row.getCell(14)));
                        infPagado.setClaveJoin(infPagado.getNroDocuOrig()+"I"+infPagado.getOtra1());
                        
                        infCompromiso.getPagados().add(infPagado);
                    }
                }
                
                bais = new ByteArrayInputStream(bytes);
                infCompromiso.getTempUploadedFile().setInputStream(bais);
                if (infCompromiso.getArchivo() == null) {
                    infCompromiso.setArchivo(archivoBean.crearArchivo());
                }
                archivoBean.asociarArchivo(infCompromiso.getArchivo(), infCompromiso.getTempUploadedFile(), false);
            }
            
            return (InfCompromiso)generalDAO.update(infCompromiso);
        } catch (BusinessException be) {
            logger.log(Level.SEVERE, null, be);
            throw be;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERR_GNRAL_FORMATO_INCORRECTO);
            ge.addError(ex.getMessage());
            throw ge;
        }
    }
    
    /**
     * Este método se encarga de retornar un valor para formar el reporte
     * 
     * @param cell
     * @return 
     */
    private String getValor(Cell cell) {
        String respuesta = "";
        if (cell == null){
            return respuesta;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                respuesta = cell.getBooleanCellValue()+"";
                break;
            case Cell.CELL_TYPE_NUMERIC:
                double d = cell.getNumericCellValue();
                respuesta = d + "";
                //Si termina en .0, le saco el .0
                if (respuesta.length() >= 2 && respuesta.substring(respuesta.length()-2, respuesta.length()).equals(".0")){
                    respuesta = respuesta.substring(0,respuesta.length()-2);
                }
                break;
            case Cell.CELL_TYPE_STRING:
                respuesta = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BLANK: 
                break;
            default:
        }
        return respuesta;
    }
    
    /**
     * Solo retorna un valor de tipo fecha si la celda no es vacía y es numérica.
     * Este caso cubre el tipo de celda blank
     * @param cell
     * @return 
     */
    private Date getValorFecha(Cell cell) {
        Date respuesta = null;
        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            respuesta = DateUtil.getJavaDate(cell.getNumericCellValue());
        }
        return respuesta;
    }
    
    private BigDecimal getValorBigDecimal(Cell cell) {
        BigDecimal respuesta = null;
        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            String txt = cell.getNumericCellValue()+"";
            respuesta = new BigDecimal(txt);
        }
        return respuesta;
    }
        
    
    
    
    /**
     * Obtiene InfComprimiso a partir del id.
     * @param idCompromiso
     * @return 
     */
    public InfCompromiso getCompromiso(Integer idCompromiso){
        if (idCompromiso == null){
            return null;
        }
        InfCompromiso infCompromiso = generalDAO.getEntityManager().find(InfCompromiso.class, idCompromiso);
        if (infCompromiso == null){
            return null;
        }
        for(InfComprometido inf: infCompromiso.getComprometidos()){
            inf.getConcepto();//Solo para cargar los datos y evitar lazyException
        }
        for(InfDevengado inf: infCompromiso.getDevengados()){
            inf.getConcepto();//Solo para cargar los datos y evitar lazyException
        }
        for(InfPagado inf: infCompromiso.getPagados()){
            inf.getConcepto();//Solo para cargar los datos y evitar lazyException
        }
        return infCompromiso;
    }
    
    /**
     * Obtiene los nombres de las planillas cargadas y los id correspondiente de los InfComprimiso.
     * @return 
     */
    public List<CompromisoArchivoTO> obtenerArchivos() {
        List<CompromisoArchivoTO> respuesta = new ArrayList<>();
        try {
            List<Object[]> lst = generalDAO.getEntityManager().createQuery("select inf.id, inf.archivo.nombreOriginal from InfCompromiso inf order by inf.id").getResultList();
            for(Object[] arreglo:lst){
                CompromisoArchivoTO to = new CompromisoArchivoTO();
                Integer id = (Integer) arreglo[0];
                String nombreArch = (String)arreglo[1];
                to.setIdCompromiso(id+"");
                to.setNombreArchivo(nombreArch);
                respuesta.add(to);
            }
            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            TechnicalException ge = new TechnicalException();
            ge.setCodigo(ConstantesErrores.ERROR_GENERAL);
            ge.addError(ex.getMessage());
            throw ge;
        }
        return respuesta;
    }
    
    /**
     * Obtiene CompromosoSafi a partir del nroCompromiso.
     * @param nroCompromisoSafi
     * @return 
     */
    public List<CompromisoSafi> obtenerCompromisosSafi(String nroCompromisoSafi){
        return generalDAO.getEntityManager().createQuery("select c from CompromisoSafi c where c.nroCompromiso = :nroCompromiso")
                .setParameter("nroCompromiso", nroCompromisoSafi)
                .getResultList();
    }

}
