

/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.business.datatype.DataCertificadoDisponibilidadPresupuestaria;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.impl.InsumoDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.PermisosMB;
import gob.mined.siap2.web.mb.UtilsMB;
import gob.mined.siap2.web.utils.dataProvider.CertificadoDisponibilidadPresupuestariaDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "certificadoDisponibilidadPresupuestariaConsulta")
public class CertificadoDisponibilidadPresupuestariaConsulta implements Serializable {

    @Inject
    PermisosMB permisosMB;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    InsumoDelegate insumoDelegate;
    @Inject
    UtilsMB utilsMB;

    @Inject
    ReporteDelegate reporteDelegate;

    private LazyDataModel<DataCertificadoDisponibilidadPresupuestaria> lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @PostConstruct
    public void init() {
        filterTable();
    }

    
    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter(){
    }

    
    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {

            IDataProvider dataProviderG = new CertificadoDisponibilidadPresupuestariaDataProvider(insumoDelegate);
            LazyDataModel var = new GeneralLazyDataModel(dataProviderG);
            lazyModel = var;

       

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



    // <editor-fold defaultstate="collapsed" desc="getter-setter">   
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    
    

    public LazyDataModel<DataCertificadoDisponibilidadPresupuestaria> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<DataCertificadoDisponibilidadPresupuestaria> lazyModel) {
        this.lazyModel = lazyModel;
    }
    
    
    

    // </editor-fold>
}
