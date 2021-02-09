/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientos;

/**
 * Filtro de las àreas de inversión
 *
 * @author jgiron
 */
public class FiltroAreaInversion implements Serializable {

    private Long adiPk;
    private String adiCodigo;
    private String adiNombre;
    private String adiDescripcion;
    private Boolean adiHabilitado;
    private SgMovimientos movPk;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private Long adiPadrePk;
    private Integer adiVersion;

    public FiltroAreaInversion() {
    }

    public SgMovimientos getMovPk() {
        return movPk;
    }

    public void setMovPk(SgMovimientos movPk) {
        this.movPk = movPk;
    }

    public Long getAdiPk() {
        return adiPk;
    }

    public String getAdiCodigo() {
        return adiCodigo;
    }

    public String getAdiNombre() {
        return adiNombre;
    }

    public String getAdiDescripcion() {
        return adiDescripcion;
    }

    public Boolean getAdiHabilitado() {
        return adiHabilitado;
    }

    public Long getAdiPadrePk() {
        return adiPadrePk;
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

    public void setAdiPk(Long adiPk) {
        this.adiPk = adiPk;
    }

    public void setAdiCodigo(String adiCodigo) {
        this.adiCodigo = adiCodigo;
    }

    public void setAdiNombre(String adiNombre) {
        this.adiNombre = adiNombre;
    }

    public void setAdiDescripcion(String adiDescripcion) {
        this.adiDescripcion = adiDescripcion;
    }

    public void setAdiHabilitado(Boolean adiHabilitado) {
        this.adiHabilitado = adiHabilitado;
    }

    public void setAdiPadrePk(Long adiPadrePk) {
        this.adiPadrePk = adiPadrePk;
    }

    public Integer getAdiVersion() {
        return adiVersion;
    }

    public void setAdiVersion(Integer adiVersion) {
        this.adiVersion = adiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.adiPk);
        hash = 89 * hash + Objects.hashCode(this.adiCodigo);
        hash = 89 * hash + Objects.hashCode(this.adiNombre);
        hash = 89 * hash + Objects.hashCode(this.adiDescripcion);
        hash = 89 * hash + Objects.hashCode(this.adiHabilitado);
        hash = 89 * hash + Objects.hashCode(this.first);
        hash = 89 * hash + Objects.hashCode(this.maxResults);
        hash = 89 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 89 * hash + Arrays.hashCode(this.ascending);
        hash = 89 * hash + Arrays.deepHashCode(this.incluirCampos);
        hash = 89 * hash + Objects.hashCode(this.adiPadrePk);
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
        final FiltroAreaInversion other = (FiltroAreaInversion) obj;
        return true;
    }

}
