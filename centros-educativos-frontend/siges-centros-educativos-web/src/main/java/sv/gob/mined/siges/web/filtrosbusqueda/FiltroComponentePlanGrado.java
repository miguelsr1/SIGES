/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.TipoComponentePlanEstudio;

public class FiltroComponentePlanGrado implements Serializable {

    private Long cpgPlanEstudioPk;
    private Long cpgGradoPk;
    private Long cpgComponentePlanEstudioPk;
    private Long cpgOpcionPk;
    private Long cpgProgramaEducativoPk;
    private Long cpgSeccionPk;
    private Boolean cpgObjetoPromocion;
    private Long cpgSeccionExclusiva;
    private Long cpgAgregandoSeccionExclusiva;
    private Boolean cpgCalificacionIngresadaCE;
    private TipoComponentePlanEstudio cpgTipoComponentePlanEstudio;
    private Boolean cpgEsTipoPaes;
    private Long cpgPk;
    private String cpeNombre;

    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroComponentePlanGrado() {
    }

    public Long getCpgPlanEstudioPk() {
        return cpgPlanEstudioPk;
    }

    public void setCpgPlanEstudioPk(Long cpgPlanEstudioPk) {
        this.cpgPlanEstudioPk = cpgPlanEstudioPk;
    }

    public Long getCpgGradoPk() {
        return cpgGradoPk;
    }

    public void setCpgGradoPk(Long cpgGradoPk) {
        this.cpgGradoPk = cpgGradoPk;
    }

    public Long getCpgComponentePlanEstudioPk() {
        return cpgComponentePlanEstudioPk;
    }

    public void setCpgComponentePlanEstudioPk(Long cpgComponentePlanEstudioPk) {
        this.cpgComponentePlanEstudioPk = cpgComponentePlanEstudioPk;
    }

    public Long getCpgOpcionPk() {
        return cpgOpcionPk;
    }

    public void setCpgOpcionPk(Long cpgOpcionPk) {
        this.cpgOpcionPk = cpgOpcionPk;
    }

    public Long getCpgProgramaEducativoPk() {
        return cpgProgramaEducativoPk;
    }

    public void setCpgProgramaEducativoPk(Long cpgProgramaEducativoPk) {
        this.cpgProgramaEducativoPk = cpgProgramaEducativoPk;
    }

    public Long getCpgSeccionPk() {
        return cpgSeccionPk;
    }

    public void setCpgSeccionPk(Long cpgSeccionPk) {
        this.cpgSeccionPk = cpgSeccionPk;
    }

    public Boolean getCpgObjetoPromocion() {
        return cpgObjetoPromocion;
    }

    public void setCpgObjetoPromocion(Boolean cpgObjetoPromocion) {
        this.cpgObjetoPromocion = cpgObjetoPromocion;
    }

    public Boolean getCpgCalificacionIngresadaCE() {
        return cpgCalificacionIngresadaCE;
    }

    public void setCpgCalificacionIngresadaCE(Boolean cpgCalificacionIngresadaCE) {
        this.cpgCalificacionIngresadaCE = cpgCalificacionIngresadaCE;
    }

    public TipoComponentePlanEstudio getCpgTipoComponentePlanEstudio() {
        return cpgTipoComponentePlanEstudio;
    }

    public void setCpgTipoComponentePlanEstudio(TipoComponentePlanEstudio cpgTipoComponentePlanEstudio) {
        this.cpgTipoComponentePlanEstudio = cpgTipoComponentePlanEstudio;
    }
    
    
    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
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

    public Long getCpgSeccionExclusiva() {
        return cpgSeccionExclusiva;
    }

    public void setCpgSeccionExclusiva(Long cpgSeccionExclusiva) {
        this.cpgSeccionExclusiva = cpgSeccionExclusiva;
    }

    public Long getCpgAgregandoSeccionExclusiva() {
        return cpgAgregandoSeccionExclusiva;
    }

    public void setCpgAgregandoSeccionExclusiva(Long cpgAgregandoSeccionExclusiva) {
        this.cpgAgregandoSeccionExclusiva = cpgAgregandoSeccionExclusiva;
    }

    public Boolean getCpgEsTipoPaes() {
        return cpgEsTipoPaes;
    }

    public void setCpgEsTipoPaes(Boolean cpgEsTipoPaes) {
        this.cpgEsTipoPaes = cpgEsTipoPaes;
    }

    public Long getCpgPk() {
        return cpgPk;
    }

    public void setCpgPk(Long cpgPk) {
        this.cpgPk = cpgPk;
    }

    public String getCpeNombre() {
        return cpeNombre;
    }

    public void setCpeNombre(String cpeNombre) {
        this.cpeNombre = cpeNombre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.cpgPlanEstudioPk);
        hash = 73 * hash + Objects.hashCode(this.cpgGradoPk);
        hash = 73 * hash + Objects.hashCode(this.cpgComponentePlanEstudioPk);
        hash = 73 * hash + Objects.hashCode(this.cpgOpcionPk);
        hash = 73 * hash + Objects.hashCode(this.cpgProgramaEducativoPk);
        hash = 73 * hash + Objects.hashCode(this.cpgSeccionPk);
        hash = 73 * hash + Objects.hashCode(this.cpgObjetoPromocion);
        hash = 73 * hash + Objects.hashCode(this.cpgSeccionExclusiva);
        hash = 73 * hash + Objects.hashCode(this.cpgAgregandoSeccionExclusiva);
        hash = 73 * hash + Objects.hashCode(this.cpgCalificacionIngresadaCE);
        hash = 73 * hash + Objects.hashCode(this.cpgTipoComponentePlanEstudio);
        hash = 73 * hash + Objects.hashCode(this.cpgEsTipoPaes);
        hash = 73 * hash + Arrays.deepHashCode(this.incluirCampos);
        hash = 73 * hash + Objects.hashCode(this.first);
        hash = 73 * hash + Objects.hashCode(this.maxResults);
        hash = 73 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 73 * hash + Arrays.hashCode(this.ascending);
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
        final FiltroComponentePlanGrado other = (FiltroComponentePlanGrado) obj;
        if (!Objects.equals(this.cpgPlanEstudioPk, other.cpgPlanEstudioPk)) {
            return false;
        }
        if (!Objects.equals(this.cpgGradoPk, other.cpgGradoPk)) {
            return false;
        }
        if (!Objects.equals(this.cpgComponentePlanEstudioPk, other.cpgComponentePlanEstudioPk)) {
            return false;
        }
        if (!Objects.equals(this.cpgOpcionPk, other.cpgOpcionPk)) {
            return false;
        }
        if (!Objects.equals(this.cpgProgramaEducativoPk, other.cpgProgramaEducativoPk)) {
            return false;
        }
        if (!Objects.equals(this.cpgSeccionPk, other.cpgSeccionPk)) {
            return false;
        }
        if (!Objects.equals(this.cpgObjetoPromocion, other.cpgObjetoPromocion)) {
            return false;
        }
        if (!Objects.equals(this.cpgSeccionExclusiva, other.cpgSeccionExclusiva)) {
            return false;
        }
        if (!Objects.equals(this.cpgAgregandoSeccionExclusiva, other.cpgAgregandoSeccionExclusiva)) {
            return false;
        }
        if (!Objects.equals(this.cpgCalificacionIngresadaCE, other.cpgCalificacionIngresadaCE)) {
            return false;
        }
        if (this.cpgTipoComponentePlanEstudio != other.cpgTipoComponentePlanEstudio) {
            return false;
        }
        if (!Objects.equals(this.cpgEsTipoPaes, other.cpgEsTipoPaes)) {
            return false;
        }
        if (!Arrays.deepEquals(this.incluirCampos, other.incluirCampos)) {
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
        return true;
    }

    
}
