/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.CertificadoDisponibilidadPresupuestaria;
import gob.mined.siap2.entities.enums.EstadoCertificadoDispPresupuestaria;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.CertificadoPresupuestarioDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.dataProvider.CertificadoDispPresDataProvider;
import gob.mined.siap2.web.utils.dataProvider.GeneralLazyDataModel;
import gob.mined.siap2.web.utils.dataProvider.IDataProvider;
import java.io.Serializable;
import java.util.Date;
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
@Named(value = "certificadoPresupuestarioValidarConsulta")
public class CertificadoPresupuestarioValidarConsulta implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    EntityManagementDelegate emd;
    @Inject
    CertificadoPresupuestarioDelegate delegate;

    private LazyDataModel<CertificadoDisponibilidadPresupuestaria> lazyModel;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    private String numero;
    private Date fecha;
    private EstadoCertificadoDispPresupuestaria estado;
    private String idPOInsumo;
    private String idFuenteRecursos;
    private String idFuenteFinanciamiento;
    private String idProcesoAdq;
    private String idProgramaPres;
    private String idSubProgramaPres;
    private String idProyecto;
    private String idAccCentral;
    private String idAsigNp;

    @PostConstruct
    public void init() {
        initFilter();
        filterTable();
    }

    /**
     * Este método inicia los filtros de la consulta
     */
    private void initFilter() {
        numero = null;
        fecha = null;
        estado = EstadoCertificadoDispPresupuestaria.ENVIADO;
        idPOInsumo = null;
        idFuenteRecursos = null;
        idFuenteFinanciamiento = null;
        idProcesoAdq = null;
        idProgramaPres = null;
        idSubProgramaPres = null;
        idProyecto = null;
        idAccCentral = null;
        idAsigNp = null;

    }

    /**
     * Este método corresponde al evento de consulta y obtiene el resultado
     * según el filtro aplicado
     */
    public void filterTable() {
        try {
            String[] orderBy = {"id"};
            boolean[] asc = {false};

            IDataProvider dataProviderG = new CertificadoDispPresDataProvider(delegate,
                NumberUtils.getIntegerONull(numero),
                fecha,
                estado,
                NumberUtils.getIntegerONull(idPOInsumo),
                NumberUtils.getIntegerONull(idFuenteRecursos),
                NumberUtils.getIntegerONull(idFuenteFinanciamiento),
                NumberUtils.getIntegerONull(idProcesoAdq),
                NumberUtils.getIntegerONull(idProgramaPres),
                NumberUtils.getIntegerONull(idSubProgramaPres),
                NumberUtils.getIntegerONull(idProyecto),
                NumberUtils.getIntegerONull(idAccCentral),
                NumberUtils.getIntegerONull(idAsigNp),
                orderBy,
                asc);

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

    public LazyDataModel<CertificadoDisponibilidadPresupuestaria> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<CertificadoDisponibilidadPresupuestaria> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIdPOInsumo() {
        return idPOInsumo;
    }

    public void setIdPOInsumo(String idPOInsumo) {
        this.idPOInsumo = idPOInsumo;
    }

    public String getIdFuenteRecursos() {
        return idFuenteRecursos;
    }

    public void setIdFuenteRecursos(String idFuenteRecursos) {
        this.idFuenteRecursos = idFuenteRecursos;
    }

    public String getIdFuenteFinanciamiento() {
        return idFuenteFinanciamiento;
    }

    public void setIdFuenteFinanciamiento(String idFuenteFinanciamiento) {
        this.idFuenteFinanciamiento = idFuenteFinanciamiento;
    }

    public String getIdProcesoAdq() {
        return idProcesoAdq;
    }

    public void setIdProcesoAdq(String idProcesoAdq) {
        this.idProcesoAdq = idProcesoAdq;
    }

    public String getIdProgramaPres() {
        return idProgramaPres;
    }

    public void setIdProgramaPres(String idProgramaPres) {
        this.idProgramaPres = idProgramaPres;
    }

    public String getIdSubProgramaPres() {
        return idSubProgramaPres;
    }

    public void setIdSubProgramaPres(String idSubProgramaPres) {
        this.idSubProgramaPres = idSubProgramaPres;
    }

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getIdAccCentral() {
        return idAccCentral;
    }

    public void setIdAccCentral(String idAccCentral) {
        this.idAccCentral = idAccCentral;
    }

    public String getIdAsigNp() {
        return idAsigNp;
    }

    public void setIdAsigNp(String idAsigNp) {
        this.idAsigNp = idAsigNp;
    }

    

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }


    public EstadoCertificadoDispPresupuestaria getEstado() {
        return estado;
    }

    public void setEstado(EstadoCertificadoDispPresupuestaria estado) {
        this.estado = estado;
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    // </editor-fold>
}
