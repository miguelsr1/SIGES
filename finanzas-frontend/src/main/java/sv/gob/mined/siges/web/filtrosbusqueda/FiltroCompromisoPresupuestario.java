package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.SgRequerimientoFondo;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroCompromisoPresupuestario implements Serializable {

    private Long cprPk;
    private SgRequerimientoFondo cprRequerimientoFondoFk;
    private String cprNumeroPresupuestario;
    private LocalDateTime cprUltMod;
    private String cprUltUsuario;
    private Integer cprVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroCompromisoPresupuestario() {

    }
    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">

    public Long getCprPk() {
        return cprPk;
    }

    public void setCprPk(Long cprPk) {
        this.cprPk = cprPk;
    }

    public SgRequerimientoFondo getCprRequerimientoFondoFk() {
        return cprRequerimientoFondoFk;
    }

    public void setCprRequerimientoFondoFk(SgRequerimientoFondo cprRequerimientoFondoFk) {
        this.cprRequerimientoFondoFk = cprRequerimientoFondoFk;
    }

    public String getCprNumeroPresupuestario() {
        return cprNumeroPresupuestario;
    }

    public void setCprNumeroPresupuestario(String cprNumeroPresupuestario) {
        this.cprNumeroPresupuestario = cprNumeroPresupuestario;
    }

    public LocalDateTime getCprUltMod() {
        return cprUltMod;
    }

    public void setCprUltMod(LocalDateTime cprUltMod) {
        this.cprUltMod = cprUltMod;
    }

    public String getCprUltUsuario() {
        return cprUltUsuario;
    }

    public void setCprUltUsuario(String cprUltUsuario) {
        this.cprUltUsuario = cprUltUsuario;
    }

    public Integer getCprVersion() {
        return cprVersion;
    }

    public void setCprVersion(Integer cprVersion) {
        this.cprVersion = cprVersion;
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
    // </editor-fold>
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.cprPk);
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
        final FiltroCompromisoPresupuestario other = (FiltroCompromisoPresupuestario) obj;
        if (!Objects.equals(this.cprPk, other.cprPk)) {
            return false;
        }
        return true;
    }
}
