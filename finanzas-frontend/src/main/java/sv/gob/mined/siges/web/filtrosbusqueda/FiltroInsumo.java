package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroInsumo implements Serializable {

    private Long insPk;

    private String insCodigo;

    private String insDescr;

    private String insNombre;

    private Long relInsumoArea;

    private Boolean ins_Ce;

    private LocalDateTime insUltmodFecha;

    private String insUltmodUsuario;

    private Integer insVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private Long relInsumoSubArea;

    public FiltroInsumo() {

    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getRelInsumoSubArea() {
        return relInsumoSubArea;
    }

    public void setRelInsumoSubArea(Long relInsumoSubArea) {
        this.relInsumoSubArea = relInsumoSubArea;
    }

    public Long getRelInsumoArea() {
        return relInsumoArea;
    }

    public void setRelInsumoArea(Long relInsumoArea) {
        this.relInsumoArea = relInsumoArea;
    }

    public Long getInsPk() {
        return insPk;
    }

    public void setInsPk(Long insPk) {
        this.insPk = insPk;
    }

    public String getInsCodigo() {
        return insCodigo;
    }

    public void setInsCodigo(String insCodigo) {
        this.insCodigo = insCodigo;
    }

    public String getInsDescr() {
        return insDescr;
    }

    public void setInsDescr(String insDescr) {
        this.insDescr = insDescr;
    }

    public String getInsNombre() {
        return insNombre;
    }

    public void setInsNombre(String insNombre) {
        this.insNombre = insNombre;
    }

    public Boolean getIns_Ce() {
        return ins_Ce;
    }

    public void setIns_Ce(Boolean ins_Ce) {
        this.ins_Ce = ins_Ce;
    }

    public LocalDateTime getInsUltmodFecha() {
        return insUltmodFecha;
    }

    public void setInsUltmodFecha(LocalDateTime insUltmodFecha) {
        this.insUltmodFecha = insUltmodFecha;
    }

    public String getInsUltmodUsuario() {
        return insUltmodUsuario;
    }

    public void setInsUltmodUsuario(String insUltmodUsuario) {
        this.insUltmodUsuario = insUltmodUsuario;
    }

    public Integer getInsVersion() {
        return insVersion;
    }

    public void setInsVersion(Integer insVersion) {
        this.insVersion = insVersion;
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
        hash = 97 * hash + Objects.hashCode(this.insPk);
        hash = 97 * hash + Objects.hashCode(this.insCodigo);
        hash = 97 * hash + Objects.hashCode(this.insDescr);
        hash = 97 * hash + Objects.hashCode(this.insNombre);
        hash = 97 * hash + Objects.hashCode(this.ins_Ce);
        hash = 97 * hash + Objects.hashCode(this.insUltmodFecha);
        hash = 97 * hash + Objects.hashCode(this.insUltmodUsuario);
        hash = 97 * hash + Objects.hashCode(this.insVersion);
        hash = 97 * hash + Objects.hashCode(this.first);
        hash = 97 * hash + Objects.hashCode(this.maxResults);
        hash = 97 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 97 * hash + Arrays.hashCode(this.ascending);
        hash = 97 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroInsumo other = (FiltroInsumo) obj;
        if (!Objects.equals(this.insCodigo, other.insCodigo)) {
            return false;
        }
        if (!Objects.equals(this.insDescr, other.insDescr)) {
            return false;
        }
        if (!Objects.equals(this.insNombre, other.insNombre)) {
            return false;
        }
        if (!Objects.equals(this.insUltmodUsuario, other.insUltmodUsuario)) {
            return false;
        }
        if (!Objects.equals(this.insPk, other.insPk)) {
            return false;
        }
        if (!Objects.equals(this.ins_Ce, other.ins_Ce)) {
            return false;
        }
        if (!Objects.equals(this.insUltmodFecha, other.insUltmodFecha)) {
            return false;
        }
        if (!Objects.equals(this.insVersion, other.insVersion)) {
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
