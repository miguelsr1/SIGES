/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumSeccionEstado;

public class FiltroSeccion implements Serializable {

    private Long secPk;
    private String secCodigo;
    private Long secGradoFk;
    private Long secServicioEducativoFk;
    private Long secSedeFk;
    private Long secAnioLectivoFk;
    private String codigoSede;
    private String nombreSede;
    private Long departamento;
    private Long municipio;
    private Long nivel;
    private EnumSeccionEstado secEstado;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroSeccion() {
    }

    public Long getSecPk() {
        return secPk;
    }

    public void setSecPk(Long secPk) {
        this.secPk = secPk;
    }

    public String getSecCodigo() {
        return secCodigo;
    }

    public void setSecCodigo(String secCodigo) {
        this.secCodigo = secCodigo;
    }

    public Long getSecGradoFk() {
        return secGradoFk;
    }

    public void setSecGradoFk(Long secGradoFk) {
        this.secGradoFk = secGradoFk;
    }

    public Long getSecSedeFk() {
        return secSedeFk;
    }

    public void setSecSedeFk(Long secSedeFk) {
        this.secSedeFk = secSedeFk;
    }

    public Long getSecAnioLectivoFk() {
        return secAnioLectivoFk;
    }

    public void setSecAnioLectivoFk(Long secAnioLectivoFk) {
        this.secAnioLectivoFk = secAnioLectivoFk;
    }

    public EnumSeccionEstado getSecEstado() {
        return secEstado;
    }

    public void setSecEstado(EnumSeccionEstado secEstado) {
        this.secEstado = secEstado;
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

    public Long getSecServicioEducativoFk() {
        return secServicioEducativoFk;
    }

    public void setSecServicioEducativoFk(Long secServicioEducativoFk) {
        this.secServicioEducativoFk = secServicioEducativoFk;
    }

    public String getCodigoSede() {
        return codigoSede;
    }

    public void setCodigoSede(String codigoSede) {
        this.codigoSede = codigoSede;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }

    public Long getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Long departamento) {
        this.departamento = departamento;
    }

    public Long getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Long municipio) {
        this.municipio = municipio;
    }

    public Long getNivel() {
        return nivel;
    }

    public void setNivel(Long nivel) {
        this.nivel = nivel;
    }

}
