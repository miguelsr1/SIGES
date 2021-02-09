/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author usuario
 */
public class FiltroSelloFirma implements Serializable {

    private Long sedPk;
    private Boolean sfiHabilitado;
    private String sfiNombreCompletoPersona;
    private Long sfiTipoRepresentantePk;
    private String sfiTipoRepresentanteCodigo;
    private List<Long> sfiSedes;
    private Boolean archivoVacio;
    private Long sfiPersonaPk;
    private String sfiDui;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroSelloFirma() {
        this.first = 0L;
    }

    public Long getSedPk() {
        return sedPk;
    }

    public void setSedPk(Long sedPk) {
        this.sedPk = sedPk;
    }

    public Boolean getSfiHabilitado() {
        return sfiHabilitado;
    }

    public void setSfiHabilitado(Boolean sfiHabilitado) {
        this.sfiHabilitado = sfiHabilitado;
    }

    public String getSfiNombreCompletoPersona() {
        return sfiNombreCompletoPersona;
    }

    public void setSfiNombreCompletoPersona(String sfiNombreCompletoPersona) {
        this.sfiNombreCompletoPersona = sfiNombreCompletoPersona;
    }

    public Long getSfiTipoRepresentantePk() {
        return sfiTipoRepresentantePk;
    }

    public void setSfiTipoRepresentantePk(Long sfiTipoRepresentantePk) {
        this.sfiTipoRepresentantePk = sfiTipoRepresentantePk;
    }

    public String getSfiTipoRepresentanteCodigo() {
        return sfiTipoRepresentanteCodigo;
    }

    public void setSfiTipoRepresentanteCodigo(String sfiTipoRepresentanteCodigo) {
        this.sfiTipoRepresentanteCodigo = sfiTipoRepresentanteCodigo;
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

    public List<Long> getSfiSedes() {
        return sfiSedes;
    }

    public void setSfiSedes(List<Long> sfiSedes) {
        this.sfiSedes = sfiSedes;
    }

    public Boolean getArchivoVacio() {
        return archivoVacio;
    }

    public void setArchivoVacio(Boolean archivoVacio) {
        this.archivoVacio = archivoVacio;
    }

    public Long getSfiPersonaPk() {
        return sfiPersonaPk;
    }

    public void setSfiPersonaPk(Long sfiPersonaPk) {
        this.sfiPersonaPk = sfiPersonaPk;
    }

    public String getSfiDui() {
        return sfiDui;
    }

    public void setSfiDui(String sfiDui) {
        this.sfiDui = sfiDui;
    }

}
