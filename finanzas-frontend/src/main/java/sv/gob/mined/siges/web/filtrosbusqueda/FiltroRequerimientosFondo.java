/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudesTransferencia;

public class FiltroRequerimientosFondo implements Serializable {

    private Long strPk;
    private Long transferenciaGDep;
    private Long unidadId;
    private Long lineaId;
    private Long fuenteFinId;
    private Long fuenteRecId;
    private Long componenteId;
    private Long subComponenteId;
    private Integer anioFiscal;
    private EnumEstadoSolicitudesTransferencia estado;
    
    String[] orderBy;
    boolean[] ascending;
    private Long first;
    private Long maxResults;
    private String[] incluirCampos;

    public FiltroRequerimientosFondo() {
    }

    public Long getStrPk() {
        return strPk;
    }

    public void setStrPk(Long desPk) {
        this.strPk = desPk;
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

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Long getTransferenciaGDep() {
        return transferenciaGDep;
    }

    public void setTransferenciaGDep(Long transferenciaGDep) {
        this.transferenciaGDep = transferenciaGDep;
    }
    
    public Long getUnidadId() {
        return unidadId;
    }

    public void setUnidadId(Long unidadId) {
        this.unidadId = unidadId;
    }

    public Long getLineaId() {
        return lineaId;
    }

    public void setLineaId(Long lineaId) {
        this.lineaId = lineaId;
    }

    public Long getFuenteFinId() {
        return fuenteFinId;
    }

    public void setFuenteFinId(Long fuenteFinId) {
        this.fuenteFinId = fuenteFinId;
    }

    public Long getFuenteRecId() {
        return fuenteRecId;
    }

    public void setFuenteRecId(Long fuenteRecId) {
        this.fuenteRecId = fuenteRecId;
    }

    public Long getComponenteId() {
        return componenteId;
    }

    public void setComponenteId(Long componenteId) {
        this.componenteId = componenteId;
    }

    public Long getSubComponenteId() {
        return subComponenteId;
    }

    public void setSubComponenteId(Long subComponenteId) {
        this.subComponenteId = subComponenteId;
    }

    public Integer getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(Integer anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public EnumEstadoSolicitudesTransferencia getEstado() {
        return estado;
    }

    public void setEstado(EnumEstadoSolicitudesTransferencia estado) {
        this.estado = estado;
    }
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.strPk);
        hash = 79 * hash + Objects.hashCode(this.ascending);
        hash = 79 * hash + Objects.hashCode(this.orderBy);
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
        final FiltroRequerimientosFondo other = (FiltroRequerimientosFondo) obj;
        if (!Objects.equals(this.strPk, other.strPk)) {
            return false;
        }
        return true;
    }

}
