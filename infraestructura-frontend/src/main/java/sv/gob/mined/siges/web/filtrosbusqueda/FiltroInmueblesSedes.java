/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.enumerados.EnumInmueblePertenece;


public class FiltroInmueblesSedes implements Serializable {
    
    private Long sedeId;
    private Long departamentoId;
    private Long municipioId;
    private Boolean terrenoPrincipal;
    private Boolean propietario;
    private Boolean activoFijo;
    private String codigo;
    private Long estadoInmueble;
    private Long unidadAdministrativa;
    private Long inmueblePk;
    private EnumInmueblePertenece perteneceA;
    private Long sedeDeUnidadRespYOtrasSedes;
    private Long unidadAdministrativaDeUnidadRespYOtrasUnidades;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;
    private String securityOperation;

    public FiltroInmueblesSedes() {
        this.seNecesitaDistinct = Boolean.FALSE;
        this.securityOperation = ConstantesOperaciones.BUSCAR_INMUEBLE_O_TERRENO;
    }

    public Boolean getActivoFijo() {
        return activoFijo;
    }

    public void setActivoFijo(Boolean activoFijo) {
        this.activoFijo = activoFijo;
    }
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
        
    public Long getSedeId() {
        return sedeId;
    }

    public void setSedeId(Long sedeId) {
        this.sedeId = sedeId;
    }

    public Boolean getTerrenoPrincipal() {
        return terrenoPrincipal;
    }

    public void setTerrenoPrincipal(Boolean terrenoPrincipal) {
        this.terrenoPrincipal = terrenoPrincipal;
    }

    public Boolean getPropietario() {
        return propietario;
    }

    public void setPropietario(Boolean propietario) {
        this.propietario = propietario;
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

    public Long getInmueblePk() {
        return inmueblePk;
    }

    public void setInmueblePk(Long inmueblePk) {
        this.inmueblePk = inmueblePk;
    }

    public Long getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(Long municipioId) {
        this.municipioId = municipioId;
    }

    public Long getEstadoInmueble() {
        return estadoInmueble;
    }

    public void setEstadoInmueble(Long estadoInmueble) {
        this.estadoInmueble = estadoInmueble;
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

    public Long getUnidadAdministrativa() {
        return unidadAdministrativa;
    }

    public void setUnidadAdministrativa(Long unidadAdministrativa) {
        this.unidadAdministrativa = unidadAdministrativa;
    }

    public EnumInmueblePertenece getPerteneceA() {
        return perteneceA;
    }

    public void setPerteneceA(EnumInmueblePertenece perteneceA) {
        this.perteneceA = perteneceA;
    }

    public Long getSedeDeUnidadRespYOtrasSedes() {
        return sedeDeUnidadRespYOtrasSedes;
    }

    public void setSedeDeUnidadRespYOtrasSedes(Long sedeDeUnidadRespYOtrasSedes) {
        this.sedeDeUnidadRespYOtrasSedes = sedeDeUnidadRespYOtrasSedes;
    }

    public Long getUnidadAdministrativaDeUnidadRespYOtrasUnidades() {
        return unidadAdministrativaDeUnidadRespYOtrasUnidades;
    }

    public void setUnidadAdministrativaDeUnidadRespYOtrasUnidades(Long unidadAdministrativaDeUnidadRespYOtrasUnidades) {
        this.unidadAdministrativaDeUnidadRespYOtrasUnidades = unidadAdministrativaDeUnidadRespYOtrasUnidades;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.sedeId);
        hash = 73 * hash + Objects.hashCode(this.departamentoId);
        hash = 73 * hash + Objects.hashCode(this.municipioId);
        hash = 73 * hash + Objects.hashCode(this.terrenoPrincipal);
        hash = 73 * hash + Objects.hashCode(this.propietario);
        hash = 73 * hash + Objects.hashCode(this.activoFijo);
        hash = 73 * hash + Objects.hashCode(this.codigo);
        hash = 73 * hash + Objects.hashCode(this.estadoInmueble);
        hash = 73 * hash + Objects.hashCode(this.unidadAdministrativa);
        hash = 73 * hash + Objects.hashCode(this.first);
        hash = 73 * hash + Objects.hashCode(this.maxResults);
        hash = 73 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 73 * hash + Arrays.hashCode(this.ascending);
        hash = 73 * hash + Arrays.deepHashCode(this.incluirCampos);
        hash = 73 * hash + Objects.hashCode(this.seNecesitaDistinct);
        hash = 73 * hash + Objects.hashCode(this.securityOperation);
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
        final FiltroInmueblesSedes other = (FiltroInmueblesSedes) obj;
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
        if (!Objects.equals(this.terrenoPrincipal, other.terrenoPrincipal)) {
            return false;
        }
        if (!Objects.equals(this.propietario, other.propietario)) {
            return false;
        }
        if (!Objects.equals(this.activoFijo, other.activoFijo)) {
            return false;
        }
        if (!Objects.equals(this.estadoInmueble, other.estadoInmueble)) {
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
        if (!Objects.equals(this.seNecesitaDistinct, other.seNecesitaDistinct)) {
            return false;
        }
        return true;
    }
    
}
