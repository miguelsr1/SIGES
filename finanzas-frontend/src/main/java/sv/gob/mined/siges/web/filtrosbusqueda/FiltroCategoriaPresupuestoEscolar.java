package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroCategoriaPresupuestoEscolar implements Serializable {

    private Long cpeId;

    private String cpeCodigo;

    private String cpeNombre;

    private Boolean cpeHabilitado;

    private Date cpeUltMod;

    private String cpeUltUsuario;

    private Integer cpeVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroCategoriaPresupuestoEscolar() {

    }
    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">

    public Long getCpeId() {
        return cpeId;
    }

    public void setCpeId(Long cpeId) {
        this.cpeId = cpeId;
    }

    public String getCpeCodigo() {
        return cpeCodigo;
    }

    public void setCpeCodigo(String cpeCodigo) {
        this.cpeCodigo = cpeCodigo;
    }

    public String getCpeNombre() {
        return cpeNombre;
    }

    public void setCpeNombre(String cpeNombre) {
        this.cpeNombre = cpeNombre;
    }

    public Boolean getCpeHabilitado() {
        return cpeHabilitado;
    }

    public void setCpeHabilitado(Boolean cpeHabilitado) {
        this.cpeHabilitado = cpeHabilitado;
    }

    public Date getCpeUltMod() {
        return cpeUltMod;
    }

    public void setCpeUltMod(Date cpeUltMod) {
        this.cpeUltMod = cpeUltMod;
    }

    public String getCpeUltUsuario() {
        return cpeUltUsuario;
    }

    public void setCpeUltUsuario(String cpeUltUsuario) {
        this.cpeUltUsuario = cpeUltUsuario;
    }

    public Integer getCpeVersion() {
        return cpeVersion;
    }

    public void setCpeVersion(Integer cpeVersion) {
        this.cpeVersion = cpeVersion;
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

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.cpeId);
        hash = 71 * hash + Objects.hashCode(this.cpeCodigo);
        hash = 71 * hash + Objects.hashCode(this.cpeNombre);
        hash = 71 * hash + Objects.hashCode(this.cpeHabilitado);
        hash = 71 * hash + Objects.hashCode(this.cpeUltMod);
        hash = 71 * hash + Objects.hashCode(this.cpeUltUsuario);
        hash = 71 * hash + Objects.hashCode(this.cpeVersion);
        hash = 71 * hash + Objects.hashCode(this.first);
        hash = 71 * hash + Objects.hashCode(this.maxResults);
        hash = 71 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 71 * hash + Arrays.hashCode(this.ascending);
        hash = 71 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroCategoriaPresupuestoEscolar other = (FiltroCategoriaPresupuestoEscolar) obj;
        if (!Objects.equals(this.cpeCodigo, other.cpeCodigo)) {
            return false;
        }
        if (!Objects.equals(this.cpeNombre, other.cpeNombre)) {
            return false;
        }
        if (!Objects.equals(this.cpeUltUsuario, other.cpeUltUsuario)) {
            return false;
        }
        if (!Objects.equals(this.cpeId, other.cpeId)) {
            return false;
        }
        if (!Objects.equals(this.cpeHabilitado, other.cpeHabilitado)) {
            return false;
        }
        if (!Objects.equals(this.cpeUltMod, other.cpeUltMod)) {
            return false;
        }
        if (!Objects.equals(this.cpeVersion, other.cpeVersion)) {
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
