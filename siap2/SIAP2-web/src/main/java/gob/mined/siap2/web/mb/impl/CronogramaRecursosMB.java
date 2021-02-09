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
import gob.mined.siap2.entities.tipos.FiltroCronogramaRecursos;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.InsumoDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.UsuarioInfo;
import gob.mined.siap2.ws.to.CronogramaRecurso;
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
@Named(value = "cronogramaRecursosMB")
public class CronogramaRecursosMB implements Serializable {

    @Inject
    private JSFUtils jSFUtils;
    @Inject
    private UsuarioInfo usuarioInfo;
    @Inject
    private EntityManagementDelegate emd;
    @Inject
    private InsumoDelegate insumoDelegate;

    private Collection<CronogramaRecurso> listaClasificadores = new ArrayList();
    private String tipoReporte = "UT";
    private BigDecimal totalMontoPlanificado;
    private BigDecimal totalMontoActas;
    private BigDecimal totalMontoQuedan;
    private BigDecimal totalMontoProyectado;    
    private FiltroCronogramaRecursos filtro;
    private static Logger logger = Logger.getLogger(InsumoBean.class.getName());

    @PostConstruct
    public void init() {
        initFilter();
        filterTable();

    }
    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
        setFiltro(new FiltroCronogramaRecursos());
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

            setListaClasificadores(getInsumoDelegate().obtenerCronogramaRecrusos(getFiltro(), getTipoReporte()));

            setTotalMontoPlanificado(BigDecimal.ZERO);
            setTotalMontoActas(BigDecimal.ZERO);
            setTotalMontoQuedan(BigDecimal.ZERO);
            setTotalMontoProyectado(BigDecimal.ZERO);
            for (CronogramaRecurso c : getListaClasificadores()) {
                if (c.getMontoPlanificado() != null) {
                    setTotalMontoPlanificado(getTotalMontoPlanificado().add(c.getMontoPlanificado()));
                }
                if (c.getMontoActas() != null) {
                    setTotalMontoActas(getTotalMontoActas().add(c.getMontoActas()));
                }
                if (c.getMontoQuedan() != null) {
                    setTotalMontoQuedan(getTotalMontoQuedan().add(c.getMontoQuedan()));
                }
                if (c.getMontoProyectado()!= null) {
                    setTotalMontoProyectado(getTotalMontoProyectado().add(c.getMontoProyectado()));
                }
            }

        } catch (GeneralException ex) {
            getLogger().log(Level.SEVERE, null, ex);
            getjSFUtils().mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, null, ex);
            getjSFUtils().mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    
    /**
     * Este método vuelve a iniciar los filtros y aplica la consulta
     */
    public void limpiar() {
        initFilter();
        filterTable();
    }

    public Collection<CronogramaRecurso> getListaClasificadores() {
        return listaClasificadores;
    }

    public void setListaClasificadores(Collection<CronogramaRecurso> listaClasificadores) {
        this.listaClasificadores = listaClasificadores;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }
    
    public FiltroCronogramaRecursos getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCronogramaRecursos filtro) {
        this.filtro = filtro;
    }

    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public UsuarioInfo getUsuarioInfo() {
        return usuarioInfo;
    }

    public void setUsuarioInfo(UsuarioInfo usuarioInfo) {
        this.usuarioInfo = usuarioInfo;
    }

    public InsumoDelegate getInsumoDelegate() {
        return insumoDelegate;
    }

    public void setInsumoDelegate(InsumoDelegate insumoDelegate) {
        this.insumoDelegate = insumoDelegate;
    }

    public BigDecimal getTotalMontoPlanificado() {
        return totalMontoPlanificado;
    }

    public void setTotalMontoPlanificado(BigDecimal totalMontoPlanificado) {
        this.totalMontoPlanificado = totalMontoPlanificado;
    }

    public BigDecimal getTotalMontoActas() {
        return totalMontoActas;
    }

    public void setTotalMontoActas(BigDecimal totalMontoActas) {
        this.totalMontoActas = totalMontoActas;
    }

    public BigDecimal getTotalMontoQuedan() {
        return totalMontoQuedan;
    }

    public void setTotalMontoQuedan(BigDecimal totalMontoQuedan) {
        this.totalMontoQuedan = totalMontoQuedan;
    }

    public BigDecimal getTotalMontoProyectado() {
        return totalMontoProyectado;
    }

    public void setTotalMontoProyectado(BigDecimal totalMontoProyectado) {
        this.totalMontoProyectado = totalMontoProyectado;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger aLogger) {
        logger = aLogger;
    }
    
    
        
}
