/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.siap2.SsTransferencia;
import sv.gob.mined.siges.web.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.web.enumerados.EnumTransferenciaEstado;


public class FiltroTransferenciaACed implements Serializable {
    
  
    private Long tacPk;
    private Long tacSedePk;
    private Long tacTransferenciaFk;
    private Long tacTransferenciaDireccionDepFk;
    
    private SsTransferencia ssTransferencia;
    private EnumTransferenciaEstado tacEstado;
    private SgDepartamento departamento;
    private Long componente;
    private Long subComponente;
    private LocalDate tacFechaDesde;
    private LocalDate tacFechaHasta;
    private EnumEstadoOrganismoAdministrativo estadoOae;
    private Integer anioFiscal;
        
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroTransferenciaACed() {
    }

    public Long getTacPk() {
        return tacPk;
    }

    public void setTacPk(Long tacPk) {
        this.tacPk = tacPk;
    }

    public EnumTransferenciaEstado getTacEstado() {
        return tacEstado;
    }

    public void setTacEstado(EnumTransferenciaEstado tacEstado) {
        this.tacEstado = tacEstado;
    }

    public Long getTacSedePk() {
        return tacSedePk;
    }

    public void setTacSedePk(Long tacSedePk) {
        this.tacSedePk = tacSedePk;
    }

    public LocalDate getTacFechaDesde() {
        return tacFechaDesde;
    }

    public void setTacFechaDesde(LocalDate tacFechaDesde) {
        this.tacFechaDesde = tacFechaDesde;
    }

    public LocalDate getTacFechaHasta() {
        return tacFechaHasta;
    }

    public void setTacFechaHasta(LocalDate tacFechaHasta) {
        this.tacFechaHasta = tacFechaHasta;
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

    public SgDepartamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(SgDepartamento departamento) {
        this.departamento = departamento;
    }

    public Long getComponente() {
        return componente;
    }

    public void setComponente(Long componente) {
        this.componente = componente;
    }

    public Long getSubComponente() {
        return subComponente;
    }

    public void setSubComponente(Long subComponente) {
        this.subComponente = subComponente;
    }

    public EnumEstadoOrganismoAdministrativo getEstadoOae() {
        return estadoOae;
    }

    public void setEstadoOae(EnumEstadoOrganismoAdministrativo estadoOae) {
        this.estadoOae = estadoOae;
    }

    public Long getTacTransferenciaDireccionDepFk() {
        return tacTransferenciaDireccionDepFk;
    }

    public void setTacTransferenciaDireccionDepFk(Long tacTransferenciaDireccionDepFk) {
        this.tacTransferenciaDireccionDepFk = tacTransferenciaDireccionDepFk;
    }

    public Integer getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(Integer anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public Long getTacTransferenciaFk() {
        return tacTransferenciaFk;
    }

    public void setTacTransferenciaFk(Long tacTransferenciaFk) {
        this.tacTransferenciaFk = tacTransferenciaFk;
    }

    public SsTransferencia getSsTransferencia() {
        return ssTransferencia;
    }

    public void setSsTransferencia(SsTransferencia ssTransferencia) {
        this.ssTransferencia = ssTransferencia;
    }
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.tacPk ^ (this.tacPk >>> 32));
        hash = 43 * hash + Objects.hashCode(this.tacEstado);
        hash = 43 * hash + Objects.hashCode(this.tacSedePk);
        hash = 43 * hash + Objects.hashCode(this.departamento);
        hash = 43 * hash + Objects.hashCode(this.tacFechaDesde);
        hash = 43 * hash + Objects.hashCode(this.tacFechaHasta);
        hash = 43 * hash + Objects.hashCode(this.first);
        hash = 43 * hash + Objects.hashCode(this.maxResults);
        hash = 43 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 43 * hash + Arrays.hashCode(this.ascending);
        hash = 43 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroTransferenciaACed other = (FiltroTransferenciaACed) obj;
        if (this.tacPk != other.tacPk) {
            return false;
        }
        return true;
    }

}
