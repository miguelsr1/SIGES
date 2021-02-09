/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.mined.siap2.business.ejbs.impl;

import com.mined.siap2.to.TranferenciaArchivoTo;
import gob.mined.siap2.data.anotations.JPADAO;
import gob.mined.siap2.data.daos.GeneralDAO;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.RelacionGesPresEsAnioFiscal;
import gob.mined.siap2.entities.data.impl.RelacionPresAnioFinanciamiento;
import gob.mined.siap2.entities.data.impl.Transferencia;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.TechnicalException;
import gob.mined.siap2.filtros.FiltroRelPresAnioFinanciamiento;
import gob.mined.siap2.persistence.dao.exceptions.DAOObjetoNoEditableException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.poi.ss.usermodel.Cell;
import org.jboss.ejb3.annotation.TransactionTimeout;

@Stateless(name = "generacionTransferenciasDesdeArchivoBean")
@LocalBean
public class GeneracionTransferenciasDesdeArchivoBean {
    
    private static final Logger LOGGER = Logger.getLogger(GeneracionTransferenciasDesdeArchivoBean.class.getName());
    
    @Inject
    private GeneracionTransferenciasDesdeArchivoAsincBean generacionTransferenciasDesdeArchivoAsincBean;
    
    @Inject
    @JPADAO
    private GeneralDAO generalDAO;
    
    @Inject
    private PresupuestoFuentesFinanciamientoBean relPresAnioFinanciamientoBean;
    
    /**
     * Este método devuelve un registro de Transferencia filtrado por ID
     * @param id
     * @return 
     */
    public Transferencia getTransferenciaById(Integer id) {
        return generalDAO.getEntityManager().find(Transferencia.class, id);
    }
    
    @TransactionTimeout(unit=TimeUnit.MINUTES, value=90)
    public void crearDesdeArchivo(TranferenciaArchivoTo archivo) {
        try {
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

            FiltroRelPresAnioFinanciamiento fr = new FiltroRelPresAnioFinanciamiento();
            fr.setRelAnioPresupuesto(relPresupuestoAnio.getId());
            List<RelacionPresAnioFinanciamiento> listaRelFuentes = relPresAnioFinanciamientoBean.getComponentesPresupuestoEscolarByFiltro(fr);
            if(listaRelFuentes == null || listaRelFuentes.isEmpty()) {
                be.addError(ConstantesErrores.ERR_PRESUPUESTO_SIN_FUENTES_FINANCIAMIENTO, new String[]{relPresupuestoAnio.getDescripcion().trim()});
                throw be;
            }
            
            if(relPresupuestoAnio.getProcesoEnCurso() == null || !relPresupuestoAnio.getProcesoEnCurso()) {
                generacionTransferenciasDesdeArchivoAsincBean.crearDesdeArchivo(archivo);
            } else {
                be.addError(ConstantesErrores.ERR_YA_EXISTE_PROCESO_PRESUPUESTO_EJECUTANDOSE, new String[]{relPresupuestoAnio.getDescripcion().trim()});
                throw be;
            }

        } catch (BusinessException be) {
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
    
    public String getValor(Cell cell) {
        String respuesta = "";
        if (cell == null) {
            return respuesta;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                respuesta = cell.getBooleanCellValue() + "";
                break;
            case Cell.CELL_TYPE_NUMERIC:
                double d = cell.getNumericCellValue();
                respuesta = d + "";
                //Si termina en .0, le saco el .0
                if (respuesta.length() >= 2 && respuesta.substring(respuesta.length() - 2, respuesta.length()).equals(".0")) {
                    respuesta = respuesta.substring(0, respuesta.length() - 2);
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

    public BigDecimal getValorBigDecimal(Cell cell) {
        BigDecimal respuesta = null;
        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            String txt = cell.getNumericCellValue() + "";
            respuesta = new BigDecimal(txt);
        }
        return respuesta;
    }
}
