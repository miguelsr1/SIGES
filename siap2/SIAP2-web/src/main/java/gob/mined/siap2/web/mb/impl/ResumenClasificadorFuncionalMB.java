/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.ejbs.impl.InsumoBean;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.tipos.FiltroClasFunc;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.InsumoDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.ws.to.ResumenClasificadorFuncional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * de resumen del clasificador funcional del gasto.
 * 
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "resumenClasificadorFuncionalMB")
public class ResumenClasificadorFuncionalMB implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    UsuarioInfo usuarioInfo;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    private InsumoDelegate insumoDelegate;

    private Collection<ResumenClasificadorFuncional> listaClasificadores = new ArrayList();
    private String tipoReporte = "UT";
    private BigDecimal totalImporteEstimado;
    private BigDecimal totalPep;
    private BigDecimal totalImporteAdjudicado;
    private BigDecimal totalImporteComprometido;
    private BigDecimal totalSaldo;
    private FiltroClasFunc filtro;
    private BigDecimal totalImporteModificado;
    private static final Logger logger = Logger.getLogger(InsumoBean.class.getName());

    @PostConstruct
    public void init() {
        initFilter();
        filterTable();

    }
    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        filtro = new FiltroClasFunc();
    }
    
    /**
     * Actualiza la tabla de clasificadores funcionales
     */
    public void actualizar() {
        filterTable();
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {

            listaClasificadores = insumoDelegate.obtenerResumen(filtro, tipoReporte);

            totalImporteAdjudicado = BigDecimal.ZERO;
            totalImporteComprometido = BigDecimal.ZERO;
            totalImporteEstimado = BigDecimal.ZERO;
            totalPep = BigDecimal.ZERO;
            totalSaldo = BigDecimal.ZERO;
            totalImporteModificado = BigDecimal.ZERO;
            for (ResumenClasificadorFuncional c : listaClasificadores) {
                if (c.getImporteAdjudicado() != null) {
                    totalImporteAdjudicado = totalImporteAdjudicado.add(c.getImporteAdjudicado());
                }
                if (c.getImporteComprometido() != null) {
                    totalImporteComprometido = totalImporteComprometido.add(c.getImporteComprometido());
                }
                if (c.getImporteEstimado() != null) {
                    totalImporteEstimado = totalImporteEstimado.add(c.getImporteEstimado());
                }
                if (c.getImporteModificado()!= null) {
                    totalImporteModificado = totalImporteModificado.add(c.getImporteModificado());
                }
                if (c.getPep() != null) {
                    totalPep = totalPep.add(c.getPep());
                }
                if (c.getSaldo() != null) {
                    totalSaldo = totalSaldo.add(c.getSaldo());
                }
            }

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    
    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
    }

    public Collection<ResumenClasificadorFuncional> getListaClasificadores() {
        return listaClasificadores;
    }

    public void setListaClasificadores(Collection<ResumenClasificadorFuncional> listaClasificadores) {
        this.listaClasificadores = listaClasificadores;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public BigDecimal getTotalImporteEstimado() {
        return totalImporteEstimado;
    }

    public void setTotalImporteEstimado(BigDecimal totalImporteEstimado) {
        this.totalImporteEstimado = totalImporteEstimado;
    }

    public BigDecimal getTotalPep() {
        return totalPep;
    }

    public void setTotalPep(BigDecimal totalPep) {
        this.totalPep = totalPep;
    }

    public BigDecimal getTotalImporteAdjudicado() {
        return totalImporteAdjudicado;
    }

    public void setTotalImporteAdjudicado(BigDecimal totalImporteAdjudicado) {
        this.totalImporteAdjudicado = totalImporteAdjudicado;
    }

    public BigDecimal getTotalImporteComprometido() {
        return totalImporteComprometido;
    }

    public void setTotalImporteComprometido(BigDecimal totalImporteComprometido) {
        this.totalImporteComprometido = totalImporteComprometido;
    }

    public BigDecimal getTotalSaldo() {
        return totalSaldo;
    }

    public void setTotalSaldo(BigDecimal totalSaldo) {
        this.totalSaldo = totalSaldo;
    }

    public FiltroClasFunc getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroClasFunc filtro) {
        this.filtro = filtro;
    }

    public BigDecimal getTotalImporteModificado() {
        return totalImporteModificado;
    }

    public void setTotalImporteModificado(BigDecimal totalImporteModificado) {
        this.totalImporteModificado = totalImporteModificado;
    }
        
}
