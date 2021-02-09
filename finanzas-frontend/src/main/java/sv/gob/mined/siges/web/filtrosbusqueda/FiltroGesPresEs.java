package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.enumerados.TipoTransferencia;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroGesPresEs implements Serializable {

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

    public FiltroGesPresEs() {

    }
    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">

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

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
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

    public Long getCpeId() {
        return cpeId;
    }

    public void setCpeId(Long cpeId) {
        this.cpeId = cpeId;
    }
    
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.gesId);
        hash = 79 * hash + Objects.hashCode(this.gesCod);
        hash = 79 * hash + Objects.hashCode(this.gesNombre);
        hash = 79 * hash + Objects.hashCode(this.gesDescripcion);
        hash = 79 * hash + Objects.hashCode(this.gesProyecto);
        hash = 79 * hash + Objects.hashCode(this.gesSubactividad);
        hash = 79 * hash + Objects.hashCode(this.gesCategoria);
        hash = 79 * hash + Objects.hashCode(this.gesConvenio);
        hash = 79 * hash + Objects.hashCode(this.gesTipoTransferencia);
        hash = 79 * hash + Objects.hashCode(this.gesCategoriaComponente);
        hash = 79 * hash + Objects.hashCode(this.gesHabilitado);
        hash = 79 * hash + Objects.hashCode(this.gesVersion);
        hash = 79 * hash + Objects.hashCode(this.first);
        hash = 79 * hash + Objects.hashCode(this.maxResults);
        hash = 79 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 79 * hash + Arrays.hashCode(this.ascending);
        hash = 79 * hash + Arrays.deepHashCode(this.incluirCampos);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FiltroGesPresEs other = (FiltroGesPresEs) obj;
        if (!Objects.equals(this.gesCod, other.gesCod)) {
            return false;
        }
        if (!Objects.equals(this.gesNombre, other.gesNombre)) {
            return false;
        }
        if (!Objects.equals(this.gesDescripcion, other.gesDescripcion)) {
            return false;
        }
        if (!Objects.equals(this.gesProyecto, other.gesProyecto)) {
            return false;
        }
        if (!Objects.equals(this.gesSubactividad, other.gesSubactividad)) {
            return false;
        }
        if (!Objects.equals(this.gesCategoria, other.gesCategoria)) {
            return false;
        }
        if (!Objects.equals(this.gesConvenio, other.gesConvenio)) {
            return false;
        }
        if (!Objects.equals(this.gesId, other.gesId)) {
            return false;
        }
        if (this.gesTipoTransferencia != other.gesTipoTransferencia) {
            return false;
        }
        if (!Objects.equals(this.gesCategoriaComponente, other.gesCategoriaComponente)) {
            return false;
        }
        if (!Objects.equals(this.gesHabilitado, other.gesHabilitado)) {
            return false;
        }
        if (!Objects.equals(this.gesVersion, other.gesVersion)) {
            return false;
        }
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.maxResults, other.maxResults)) {
            return false;
        }
        if (!Arrays.deepEquals(this.orderBy, other.orderBy)) {
            return false;
        }
        if (!Arrays.equals(this.ascending, other.ascending)) {
            return false;
        }
        if (!Arrays.deepEquals(this.incluirCampos, other.incluirCampos)) {
            return false;
        }
        return true;
    }
    // </editor-fold>
}
