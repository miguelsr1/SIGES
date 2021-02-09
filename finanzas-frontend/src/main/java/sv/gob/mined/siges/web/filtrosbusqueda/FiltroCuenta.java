package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.siap2.SsCuenta;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroCuenta implements Serializable {

    private Long cuId;
    private String cuCodigo;
    private String cuNombre;
    private String cuDescripcion;
    private Boolean cuHabilitado;
    private Integer cuOrden;
    private SsCuenta cuCuentaPadre;
    private Date cuUltMod;
    private String cuUltUsuario;
    private Integer cuVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroCuenta() {

    }
    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">

    public Long getCuId() {
        return cuId;
    }

    public void setCuId(Long cuId) {
        this.cuId = cuId;
    }

    public String getCuCodigo() {
        return cuCodigo;
    }

    public void setCuCodigo(String cuCodigo) {
        this.cuCodigo = cuCodigo;
    }

    public String getCuNombre() {
        return cuNombre;
    }

    public void setCuNombre(String cuNombre) {
        this.cuNombre = cuNombre;
    }

    public String getCuDescripcion() {
        return cuDescripcion;
    }

    public void setCuDescripcion(String cuDescripcion) {
        this.cuDescripcion = cuDescripcion;
    }

    public Boolean getCuHabilitado() {
        return cuHabilitado;
    }

    public void setCuHabilitado(Boolean cuHabilitado) {
        this.cuHabilitado = cuHabilitado;
    }

    public Integer getCuOrden() {
        return cuOrden;
    }

    public void setCuOrden(Integer cuOrden) {
        this.cuOrden = cuOrden;
    }

    public SsCuenta getCuCuentaPadre() {
        return cuCuentaPadre;
    }

    public void setCuCuentaPadre(SsCuenta cuCuentaPadre) {
        this.cuCuentaPadre = cuCuentaPadre;
    }

    public Date getCuUltMod() {
        return cuUltMod;
    }

    public void setCuUltMod(Date cuUltMod) {
        this.cuUltMod = cuUltMod;
    }

    public String getCuUltUsuario() {
        return cuUltUsuario;
    }

    public void setCuUltUsuario(String cuUltUsuario) {
        this.cuUltUsuario = cuUltUsuario;
    }

    public Integer getCuVersion() {
        return cuVersion;
    }

    public void setCuVersion(Integer cuVersion) {
        this.cuVersion = cuVersion;
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
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.cuId);
        hash = 23 * hash + Objects.hashCode(this.cuCodigo);
        hash = 23 * hash + Objects.hashCode(this.cuNombre);
        hash = 23 * hash + Objects.hashCode(this.cuDescripcion);
        hash = 23 * hash + Objects.hashCode(this.cuHabilitado);
        hash = 23 * hash + Objects.hashCode(this.cuOrden);
        hash = 23 * hash + Objects.hashCode(this.cuCuentaPadre);
        hash = 23 * hash + Objects.hashCode(this.cuUltMod);
        hash = 23 * hash + Objects.hashCode(this.cuUltUsuario);
        hash = 23 * hash + Objects.hashCode(this.cuVersion);
        hash = 23 * hash + Objects.hashCode(this.first);
        hash = 23 * hash + Objects.hashCode(this.maxResults);
        hash = 23 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 23 * hash + Arrays.hashCode(this.ascending);
        hash = 23 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroCuenta other = (FiltroCuenta) obj;
        if (!Objects.equals(this.cuCodigo, other.cuCodigo)) {
            return false;
        }
        if (!Objects.equals(this.cuNombre, other.cuNombre)) {
            return false;
        }
        if (!Objects.equals(this.cuDescripcion, other.cuDescripcion)) {
            return false;
        }
        if (!Objects.equals(this.cuUltUsuario, other.cuUltUsuario)) {
            return false;
        }
        if (!Objects.equals(this.cuId, other.cuId)) {
            return false;
        }
        if (!Objects.equals(this.cuHabilitado, other.cuHabilitado)) {
            return false;
        }
        if (!Objects.equals(this.cuOrden, other.cuOrden)) {
            return false;
        }
        if (!Objects.equals(this.cuCuentaPadre, other.cuCuentaPadre)) {
            return false;
        }
        if (!Objects.equals(this.cuUltMod, other.cuUltMod)) {
            return false;
        }
        if (!Objects.equals(this.cuVersion, other.cuVersion)) {
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
