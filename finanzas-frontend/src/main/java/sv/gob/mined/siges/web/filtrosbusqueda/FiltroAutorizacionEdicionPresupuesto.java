package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroAutorizacionEdicionPresupuesto implements Serializable {

    private Long autPk;
    private Long autPresupuestoFk;
    private Long autUsuarioValidadoFk;
    private LocalDateTime autUltMod;
    private String autUltUsuario;
    private Integer autVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroAutorizacionEdicionPresupuesto() {

    }
    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">

    public Long getAutPk() {
        return autPk;
    }

    public void setAutPk(Long autPk) {
        this.autPk = autPk;
    }

    public Long getAutPresupuestoFk() {
        return autPresupuestoFk;
    }

    public void setAutPresupuestoFk(Long autPresupuestoFk) {
        this.autPresupuestoFk = autPresupuestoFk;
    }

    public Long getAutUsuarioValidadoFk() {
        return autUsuarioValidadoFk;
    }

    public void setAutUsuarioValidadoFk(Long autUsuarioValidadoFk) {
        this.autUsuarioValidadoFk = autUsuarioValidadoFk;
    }

    public LocalDateTime getAutUltMod() {
        return autUltMod;
    }

    public void setAutUltMod(LocalDateTime autUltMod) {
        this.autUltMod = autUltMod;
    }

    public String getAutUltUsuario() {
        return autUltUsuario;
    }

    public void setAutUltUsuario(String autUltUsuario) {
        this.autUltUsuario = autUltUsuario;
    }

    public Integer getAutVersion() {
        return autVersion;
    }

    public void setAutVersion(Integer autVersion) {
        this.autVersion = autVersion;
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
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.autPk);
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
        final FiltroAutorizacionEdicionPresupuesto other = (FiltroAutorizacionEdicionPresupuesto) obj;
        if (!Objects.equals(this.autPk, other.autPk)) {
            return false;
        }
        return true;
    }

    // </editor-fold>
}
