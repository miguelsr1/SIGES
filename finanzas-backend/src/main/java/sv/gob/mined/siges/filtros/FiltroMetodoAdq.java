/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Filtro de los métodos de adquisición
 *
 * @author jgiron
 */
public class FiltroMetodoAdq implements Serializable {

    private Long metId;

    private String metNombre;

    private Boolean metHabilitado;

    private LocalDateTime metUltMod;

    private String metUltUsuario;

    private Integer meVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    //Auxiliar
    private Boolean seNecesitaDistinct;

    public FiltroMetodoAdq() {
        this.seNecesitaDistinct = Boolean.FALSE;
    }

    public Long getMetId() {
        return metId;
    }

    public void setMetId(Long metId) {
        this.metId = metId;
    }

    public String getMetNombre() {
        return metNombre;
    }

    public void setMetNombre(String metNombre) {
        this.metNombre = metNombre;
    }

    public Boolean getMetHabilitado() {
        return metHabilitado;
    }

    public void setMetHabilitado(Boolean metHabilitado) {
        this.metHabilitado = metHabilitado;
    }

    public LocalDateTime getMetUltMod() {
        return metUltMod;
    }

    public void setMetUltMod(LocalDateTime metUltMod) {
        this.metUltMod = metUltMod;
    }

    public String getMetUltUsuario() {
        return metUltUsuario;
    }

    public void setMetUltUsuario(String metUltUsuario) {
        this.metUltUsuario = metUltUsuario;
    }

    public Integer getMeVersion() {
        return meVersion;
    }

    public void setMeVersion(Integer meVersion) {
        this.meVersion = meVersion;
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

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

}
