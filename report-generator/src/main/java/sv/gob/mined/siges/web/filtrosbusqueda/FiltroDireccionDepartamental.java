/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.SgDepartamento;

public class FiltroDireccionDepartamental implements Serializable {

    private Long dedPk;
    
    private Long departamentoPk;

    private String dedNombre;

    private String dedNit;

    private String dedTelefono;

    private String dedFax;

    private String dedIpAutorizada;

    private Boolean dedHabilitado;

    private LocalDateTime dedUltModFecha;

    private String dedUltModUsuario;

    private Integer dedVersion;

    private SgDepartamento dedDepartamentoFk;

    private String decDirectorCargo;
    private String decDirectorNombre;
    private String decPagadorCargo;
    private String decPagadorNombre;
    private String decRefrendarioCargo;
    private String decRefrendarioNombre;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroDireccionDepartamental() {
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

    public Long getDedPk() {
        return dedPk;
    }

    public void setDedPk(Long dedPk) {
        this.dedPk = dedPk;
    }

    public String getDedNombre() {
        return dedNombre;
    }

    public void setDedNombre(String dedNombre) {
        this.dedNombre = dedNombre;
    }

    public String getDedNit() {
        return dedNit;
    }

    public void setDedNit(String dedNit) {
        this.dedNit = dedNit;
    }

    public String getDedTelefono() {
        return dedTelefono;
    }

    public void setDedTelefono(String dedTelefono) {
        this.dedTelefono = dedTelefono;
    }

    public String getDedFax() {
        return dedFax;
    }

    public void setDedFax(String dedFax) {
        this.dedFax = dedFax;
    }

    public String getDedIpAutorizada() {
        return dedIpAutorizada;
    }

    public void setDedIpAutorizada(String dedIpAutorizada) {
        this.dedIpAutorizada = dedIpAutorizada;
    }

    public Boolean getDedHabilitado() {
        return dedHabilitado;
    }

    public void setDedHabilitado(Boolean dedHabilitado) {
        this.dedHabilitado = dedHabilitado;
    }

    public LocalDateTime getDedUltModFecha() {
        return dedUltModFecha;
    }

    public void setDedUltModFecha(LocalDateTime dedUltModFecha) {
        this.dedUltModFecha = dedUltModFecha;
    }

    public String getDedUltModUsuario() {
        return dedUltModUsuario;
    }

    public void setDedUltModUsuario(String dedUltModUsuario) {
        this.dedUltModUsuario = dedUltModUsuario;
    }

    public Integer getDedVersion() {
        return dedVersion;
    }

    public void setDedVersion(Integer dedVersion) {
        this.dedVersion = dedVersion;
    }

    public SgDepartamento getDedDepartamentoFk() {
        return dedDepartamentoFk;
    }

    public void setDedDepartamentoFk(SgDepartamento dedDepartamentoFk) {
        this.dedDepartamentoFk = dedDepartamentoFk;
    }

    public String getDecDirectorCargo() {
        return decDirectorCargo;
    }

    public void setDecDirectorCargo(String decDirectorCargo) {
        this.decDirectorCargo = decDirectorCargo;
    }

    public String getDecDirectorNombre() {
        return decDirectorNombre;
    }

    public void setDecDirectorNombre(String decDirectorNombre) {
        this.decDirectorNombre = decDirectorNombre;
    }

    public String getDecPagadorCargo() {
        return decPagadorCargo;
    }

    public void setDecPagadorCargo(String decPagadorCargo) {
        this.decPagadorCargo = decPagadorCargo;
    }

    public String getDecPagadorNombre() {
        return decPagadorNombre;
    }

    public void setDecPagadorNombre(String decPagadorNombre) {
        this.decPagadorNombre = decPagadorNombre;
    }

    public String getDecRefrendarioCargo() {
        return decRefrendarioCargo;
    }

    public void setDecRefrendarioCargo(String decRefrendarioCargo) {
        this.decRefrendarioCargo = decRefrendarioCargo;
    }

    public String getDecRefrendarioNombre() {
        return decRefrendarioNombre;
    }

    public void setDecRefrendarioNombre(String decRefrendarioNombre) {
        this.decRefrendarioNombre = decRefrendarioNombre;
    }

    public Long getDepartamentoPk() {
        return departamentoPk;
    }

    public void setDepartamentoPk(Long departamentoPk) {
        this.departamentoPk = departamentoPk;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.dedPk);
        hash = 31 * hash + Objects.hashCode(this.dedNombre);
        hash = 31 * hash + Objects.hashCode(this.dedNit);
        hash = 31 * hash + Objects.hashCode(this.dedTelefono);
        hash = 31 * hash + Objects.hashCode(this.dedFax);
        hash = 31 * hash + Objects.hashCode(this.dedIpAutorizada);
        hash = 31 * hash + Objects.hashCode(this.dedHabilitado);
        hash = 31 * hash + Objects.hashCode(this.dedUltModFecha);
        hash = 31 * hash + Objects.hashCode(this.dedUltModUsuario);
        hash = 31 * hash + Objects.hashCode(this.dedVersion);
        hash = 31 * hash + Objects.hashCode(this.dedDepartamentoFk);
        hash = 31 * hash + Objects.hashCode(this.decDirectorCargo);
        hash = 31 * hash + Objects.hashCode(this.decDirectorNombre);
        hash = 31 * hash + Objects.hashCode(this.decPagadorCargo);
        hash = 31 * hash + Objects.hashCode(this.decPagadorNombre);
        hash = 31 * hash + Objects.hashCode(this.decRefrendarioCargo);
        hash = 31 * hash + Objects.hashCode(this.decRefrendarioNombre);
        hash = 31 * hash + Objects.hashCode(this.first);
        hash = 31 * hash + Objects.hashCode(this.maxResults);
        hash = 31 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 31 * hash + Arrays.hashCode(this.ascending);
        hash = 31 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroDireccionDepartamental other = (FiltroDireccionDepartamental) obj;
        if (!Objects.equals(this.dedPk, other.dedPk)) {
            return false;
        }
        return true;
    }

}
