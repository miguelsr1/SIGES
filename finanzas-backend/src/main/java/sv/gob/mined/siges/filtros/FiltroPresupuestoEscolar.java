/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumPresupuestoEscolarEstado;
import sv.gob.mined.siges.persistencia.entidades.SgSede;

/**
 * Filtro del presupuesto escolar
 *
 * @author jgiron
 */
public class FiltroPresupuestoEscolar implements Serializable {

    private Long pesPk;
    private Long idSede;
    private Integer anioFiscal;
    private EnumPresupuestoEscolarEstado pesEstado;
    private Boolean habilitado;
    private SgSede pesSedeFk;
    private Boolean pesEdicion;
    private List<Long> sedesIds;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String securityOperation;
    private Boolean seNecesitaDistinct;
    private String[] incluirCampos;

    public FiltroPresupuestoEscolar() {
        this.seNecesitaDistinct = Boolean.FALSE;
        this.securityOperation = ConstantesOperaciones.BUSCAR_PRESUPUESTOS_ESCOLARES;
    }

    public Long getPesPk() {
        return pesPk;
    }

    public void setPesPk(Long pesPk) {
        this.pesPk = pesPk;

    }

    public Long getIdSede() {
        return idSede;
    }

    public void setIdSede(Long idSede) {
        this.idSede = idSede;
    }

    public Integer getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(Integer anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public EnumPresupuestoEscolarEstado getPesEstado() {
        return pesEstado;
    }

    public void setPesEstado(EnumPresupuestoEscolarEstado pesEstado) {
        this.pesEstado = pesEstado;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
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

    public SgSede getPesSedeFk() {
        return pesSedeFk;
    }

    public void setPesSedeFk(SgSede pesSedeFk) {
        this.pesSedeFk = pesSedeFk;
    }

    public Boolean getPesEdicion() {
        return pesEdicion;
    }

    public void setPesEdicion(Boolean pesEdicion) {
        this.pesEdicion = pesEdicion;
    }

    public List<Long> getSedesIds() {
        return sedesIds;
    }

    public void setSedesIds(List<Long> sedesIds) {
        this.sedesIds = sedesIds;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }
    
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.pesPk ^ (this.pesPk >>> 32));
        hash = 67 * hash + (int) (this.idSede ^ (this.idSede >>> 32));
        hash = 67 * hash + Objects.hashCode(this.anioFiscal);
        hash = 67 * hash + Objects.hashCode(this.pesEstado);
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
        final FiltroPresupuestoEscolar other = (FiltroPresupuestoEscolar) obj;
        if (this.pesPk != other.pesPk) {
            return false;
        }
        return true;
    }

}
