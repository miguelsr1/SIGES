package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroEncabezadoPlanCompra implements Serializable {

    private Long plaPk;

    private Long plaPresupuestoFk;

    private String plaComentario;

    private Long sedeFk;

    private Integer anioFiscalFk;

    private LocalDateTime plaUltModFecha;

    private String plaUltModUsuario;

    private Integer plaVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroEncabezadoPlanCompra() {

    }
    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">

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

    public Long getPlaPk() {
        return plaPk;
    }

    public void setPlaPk(Long plaPk) {
        this.plaPk = plaPk;
    }

    public Long getPlaPresupuestoFk() {
        return plaPresupuestoFk;
    }

    public void setPlaPresupuestoFk(Long plaPresupuestoFk) {
        this.plaPresupuestoFk = plaPresupuestoFk;
    }

    public String getPlaComentario() {
        return plaComentario;
    }

    public void setPlaComentario(String plaComentario) {
        this.plaComentario = plaComentario;
    }

    public LocalDateTime getPlaUltModFecha() {
        return plaUltModFecha;
    }

    public void setPlaUltModFecha(LocalDateTime plaUltModFecha) {
        this.plaUltModFecha = plaUltModFecha;
    }

    public String getPlaUltModUsuario() {
        return plaUltModUsuario;
    }

    public void setPlaUltModUsuario(String plaUltModUsuario) {
        this.plaUltModUsuario = plaUltModUsuario;
    }

    public Long getSedeFk() {
        return sedeFk;
    }

    public void setSedeFk(Long sedeFk) {
        this.sedeFk = sedeFk;
    }

    public Integer getAnioFiscalFk() {
        return anioFiscalFk;
    }

    public void setAnioFiscalFk(Integer anioFiscalFk) {
        this.anioFiscalFk = anioFiscalFk;
    }

    public Integer getPlaVersion() {
        return plaVersion;
    }

    public void setPlaVersion(Integer plaVersion) {
        this.plaVersion = plaVersion;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.plaPk);
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
        final FiltroEncabezadoPlanCompra other = (FiltroEncabezadoPlanCompra) obj;
        if (!Objects.equals(this.plaPk, other.plaPk)) {
            return false;
        }
        return true;
    }

    // </editor-fold>
}
