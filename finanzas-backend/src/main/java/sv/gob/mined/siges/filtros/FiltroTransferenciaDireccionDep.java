/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import sv.gob.mined.siges.enumerados.EnumTransferenciaEstado;
import sv.gob.mined.siges.persistencia.entidades.SgDireccionDepartamental;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;

/**
 * Filtro de las transferencias a las deirecciones departamentales
 *
 * @author jgiron
 */
public class FiltroTransferenciaDireccionDep implements Serializable {
    
  
    private Long tddPk;
    private Long tddTransferenciaFk;
    private Long transferenciaId;
    private EnumTransferenciaEstado tddEstado;
    private SgDireccionDepartamental tddDireccionDep;
    private SgDepartamento departamento;
    private Long componente;
    private Long subComponente;
    private LocalDate tddFechaDesde;
    private LocalDate tddFechaHasta;
    private Double montoAutorizado;
    private Double montoSolicitado;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroTransferenciaDireccionDep() {
    }
    
    public Long getTddPk() {
        return tddPk;
    }

    public void setTddPk(Long tddPk) {
        this.tddPk = tddPk;
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

    public EnumTransferenciaEstado getTddEstado() {
        return tddEstado;
    }

    public void setTddEstado(EnumTransferenciaEstado tddEstado) {
        this.tddEstado = tddEstado;
    }

    public LocalDate getTddFechaDesde() {
        return tddFechaDesde;
    }

    public void setTddFechaDesde(LocalDate tddFechaDesde) {
        this.tddFechaDesde = tddFechaDesde;
    }

    public LocalDate getTddFechaHasta() {
        return tddFechaHasta;
    }

    public void setTddFechaHasta(LocalDate tddFechaHasta) {
        this.tddFechaHasta = tddFechaHasta;
    }

    public Double getMontoAutorizado() {
        return montoAutorizado;
    }

    public void setMontoAutorizado(Double montoAutorizado) {
        this.montoAutorizado = montoAutorizado;
    }

    public Double getMontoSolicitado() {
        return montoSolicitado;
    }

    public void setMontoSolicitado(Double montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }

    public SgDireccionDepartamental getTddDireccionDep() {
        return tddDireccionDep;
    }

    public void setTddDireccionDep(SgDireccionDepartamental tddDireccionDep) {
        this.tddDireccionDep = tddDireccionDep;
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

    public Long getTddTransferenciaFk() {
        return tddTransferenciaFk;
    }

    public void setTddTransferenciaFk(Long tddTransferenciaFk) {
        this.tddTransferenciaFk = tddTransferenciaFk;
    }

    public Long getTransferenciaId() {
        return transferenciaId;
    }

    public void setTransferenciaId(Long transferenciaId) {
        this.transferenciaId = transferenciaId;
    }

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.tddPk ^ (this.tddPk >>> 32));
        hash = 67 * hash + Objects.hashCode(this.tddEstado);
        hash = 67 * hash + Objects.hashCode(this.montoAutorizado);
        hash = 67 * hash + Objects.hashCode(this.montoSolicitado);
        hash = 67 * hash + Objects.hashCode(this.first);
        hash = 67 * hash + Objects.hashCode(this.maxResults);
        hash = 67 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 67 * hash + Arrays.hashCode(this.ascending);
        hash = 67 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroTransferenciaDireccionDep other = (FiltroTransferenciaDireccionDep) obj;
        if (this.tddPk != other.tddPk) {
            return false;
        }
        return true;
    }

}
