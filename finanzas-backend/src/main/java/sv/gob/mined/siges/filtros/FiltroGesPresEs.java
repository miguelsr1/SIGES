/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import javax.ejb.SessionBean;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.enumerados.TipoTransferencia;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsCategoriaPresupuestoEscolar;

/**
 * Filtro de SubComponente SIAP2
 * @author Sofis Solutions
 */
public class FiltroGesPresEs implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());
    private FilterConfig filterConfig = null;

    private Long gesId;
    
    private Long cpeId;

    private String gesCod;

    private String gesNombre;

    private String gesDescripcion;

    private String gesProyecto;

    private String gesSubactividad;

    private String gesCategoria;

    private String gesConvenio;

    private TipoTransferencia gesTipoTransferencia;

    private SsCategoriaPresupuestoEscolar gesCategoriaComponente;

    private Boolean gesHabilitado;

    private Integer gesVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    @Inject
    private SessionBean sessionBean;

    public FiltroGesPresEs() {

        super();
        this.first = 0L;
    }

    public Long getCpeId() {
        return cpeId;
    }

    public void setCpeId(Long cpeId) {
        this.cpeId = cpeId;
    }
     
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Long getGesId() {
        return gesId;
    }

    public void setGesId(Long gesId) {
        this.gesId = gesId;
    }

    public String getGesCod() {
        return gesCod;
    }

    public void setGesCod(String gesCod) {
        this.gesCod = gesCod;
    }

    public String getGesNombre() {
        return gesNombre;
    }

    public void setGesNombre(String gesNombre) {
        this.gesNombre = gesNombre;
    }

    public String getGesDescripcion() {
        return gesDescripcion;
    }

    public void setGesDescripcion(String gesDescripcion) {
        this.gesDescripcion = gesDescripcion;
    }

    public String getGesProyecto() {
        return gesProyecto;
    }

    public void setGesProyecto(String gesProyecto) {
        this.gesProyecto = gesProyecto;
    }

    public String getGesSubactividad() {
        return gesSubactividad;
    }

    public void setGesSubactividad(String gesSubactividad) {
        this.gesSubactividad = gesSubactividad;
    }

    public String getGesCategoria() {
        return gesCategoria;
    }

    public void setGesCategoria(String gesCategoria) {
        this.gesCategoria = gesCategoria;
    }

    public String getGesConvenio() {
        return gesConvenio;
    }

    public void setGesConvenio(String gesConvenio) {
        this.gesConvenio = gesConvenio;
    }

    public TipoTransferencia getGesTipoTransferencia() {
        return gesTipoTransferencia;
    }

    public void setGesTipoTransferencia(TipoTransferencia gesTipoTransferencia) {
        this.gesTipoTransferencia = gesTipoTransferencia;
    }

    public SsCategoriaPresupuestoEscolar getGesCategoriaComponente() {
        return gesCategoriaComponente;
    }

    public void setGesCategoriaComponente(SsCategoriaPresupuestoEscolar gesCategoriaComponente) {
        this.gesCategoriaComponente = gesCategoriaComponente;
    }

    public Boolean getGesHabilitado() {
        return gesHabilitado;
    }

    public void setGesHabilitado(Boolean gesHabilitado) {
        this.gesHabilitado = gesHabilitado;
    }

    public Integer getGesVersion() {
        return gesVersion;
    }

    public void setGesVersion(Integer gesVersion) {
        this.gesVersion = gesVersion;
    }

}
