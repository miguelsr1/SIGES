/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.dto.SgMovimientos;

public class FiltroAreaInversion implements Serializable {

    private Long adiPk;
    private String adiCodigo;
    private String adiNombre;
    private SgMovimientos movPk;
    private String adiDescripcion;
    private Boolean adiHabilitado;
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

    public void setFirst(Long first) {
        this.first = first;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Long getFirst() {
        return first;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
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

    public Integer getAdiVersion() {
        return adiVersion;
    }

    public void setAdiVersion(Integer adiVersion) {
        this.adiVersion = adiVersion;
    }

}
