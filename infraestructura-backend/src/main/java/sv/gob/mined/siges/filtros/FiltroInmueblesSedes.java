/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumInmueblePertenece;

public class FiltroInmueblesSedes implements Serializable {

    private Long sedeId;
    private Long departamentoId;
    private Long municipioId;
    private Boolean terrenoPrincipal;
    private Boolean propietario;
    private String codigo;
    private Boolean activoFijo;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getActivoFijo() {
        return activoFijo;
    }

    public void setActivoFijo(Boolean activoFijo) {
        this.activoFijo = activoFijo;
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

    public Long getInmueblePk() {
        return inmueblePk;
    }

    public void setInmueblePk(Long inmueblePk) {
        this.inmueblePk = inmueblePk;
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

}
