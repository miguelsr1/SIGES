/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;


public class FiltroAula implements Serializable {
    
    private Long sedeId;
    private Long departamentoId;
    private Long municipioId;
    private String codigo;
    private Long inmueblePk;
    private Long edificioPk;
    private Long unidadAdministrativa;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    private String securityOperation;
    
    public FiltroAula() {
        this.securityOperation = ConstantesOperaciones.BUSCAR_AULA;
    }

    public Long getSedeId() {
        return sedeId;
    }

    public void setSedeId(Long sedeId) {
        this.sedeId = sedeId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }  

    public Long getInmueblePk() {
        return inmueblePk;
    }

    public void setInmueblePk(Long inmueblePk) {
        this.inmueblePk = inmueblePk;
    }

    public Long getEdificioPk() {
        return edificioPk;
    }

    public void setEdificioPk(Long edificioPk) {
        this.edificioPk = edificioPk;
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

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public Long getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(Long municipioId) {
        this.municipioId = municipioId;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public Long getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(Long unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.sedeId);
        hash = 97 * hash + Objects.hashCode(this.departamentoId);
        hash = 97 * hash + Objects.hashCode(this.municipioId);
        hash = 97 * hash + Objects.hashCode(this.codigo);
        hash = 97 * hash + Objects.hashCode(this.inmueblePk);
        hash = 97 * hash + Objects.hashCode(this.edificioPk);
        hash = 97 * hash + Objects.hashCode(this.unidadAdministrativa);
        hash = 97 * hash + Objects.hashCode(this.first);
        hash = 97 * hash + Objects.hashCode(this.maxResults);
        hash = 97 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 97 * hash + Arrays.hashCode(this.ascending);
        hash = 97 * hash + Arrays.deepHashCode(this.incluirCampos);
        hash = 97 * hash + Objects.hashCode(this.securityOperation);
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
        final FiltroAula other = (FiltroAula) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.securityOperation, other.securityOperation)) {
            return false;
        }
        if (!Objects.equals(this.sedeId, other.sedeId)) {
            return false;
        }
        if (!Objects.equals(this.departamentoId, other.departamentoId)) {
            return false;
        }
        if (!Objects.equals(this.municipioId, other.municipioId)) {
            return false;
        }
        if (!Objects.equals(this.inmueblePk, other.inmueblePk)) {
            return false;
        }
        if (!Objects.equals(this.edificioPk, other.edificioPk)) {
            return false;
        }
        if (!Objects.equals(this.unidadAdministrativa, other.unidadAdministrativa)) {
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
    
    
}
