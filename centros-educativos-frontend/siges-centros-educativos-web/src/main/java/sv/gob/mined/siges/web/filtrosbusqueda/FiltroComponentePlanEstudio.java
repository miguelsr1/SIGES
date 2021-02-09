/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.TipoComponentePlanEstudio;

public class FiltroComponentePlanEstudio implements Serializable {

    private Long cpePk;
    private String cpeCodigo;
    private String cpeNombre;
    private String cpeNombrePublicable;
    private String cpeDescripcion;
    private String cpeContenidoTematico;
    private TipoComponentePlanEstudio cpeTipo;
    private Boolean cpeHabilitado;
    private Long exculsivoDeSeccion;
    private TipoComponentePlanEstudio cpeOmitirTipo;

    private Long cpeGrado;
    private Long cpePlanEstudio;

    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroComponentePlanEstudio() {
    }

    public Long getCpePk() {
        return cpePk;
    }

    public void setCpePk(Long cpePk) {
        this.cpePk = cpePk;
    }

    public String getCpeCodigo() {
        return cpeCodigo;
    }

    public void setCpeCodigo(String cpeCodigo) {
        this.cpeCodigo = cpeCodigo;
    }

    public String getCpeNombre() {
        return cpeNombre;
    }

    public void setCpeNombre(String cpeNombre) {
        this.cpeNombre = cpeNombre;
    }

    public String getCpeNombrePublicable() {
        return cpeNombrePublicable;
    }

    public void setCpeNombrePublicable(String cpeNombrePublicable) {
        this.cpeNombrePublicable = cpeNombrePublicable;
    }

    public String getCpeDescripcion() {
        return cpeDescripcion;
    }

    public void setCpeDescripcion(String cpeDescripcion) {
        this.cpeDescripcion = cpeDescripcion;
    }

    public String getCpeContenidoTematico() {
        return cpeContenidoTematico;
    }

    public void setCpeContenidoTematico(String cpeContenidoTematico) {
        this.cpeContenidoTematico = cpeContenidoTematico;
    }

    public TipoComponentePlanEstudio getCpeTipo() {
        return cpeTipo;
    }

    public void setCpeTipo(TipoComponentePlanEstudio cpeTipo) {
        this.cpeTipo = cpeTipo;
    }

    public Boolean getCpeHabilitado() {
        return cpeHabilitado;
    }

    public void setCpeHabilitado(Boolean cpeHabilitado) {
        this.cpeHabilitado = cpeHabilitado;
    }

    public Long getCpeGrado() {
        return cpeGrado;
    }

    public void setCpeGrado(Long cpeGrado) {
        this.cpeGrado = cpeGrado;
    }

    public Long getCpePlanEstudio() {
        return cpePlanEstudio;
    }

    public void setCpePlanEstudio(Long cpePlanEstudio) {
        this.cpePlanEstudio = cpePlanEstudio;
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

    public Long getExculsivoDeSeccion() {
        return exculsivoDeSeccion;
    }

    public void setExculsivoDeSeccion(Long exculsivoDeSeccion) {
        this.exculsivoDeSeccion = exculsivoDeSeccion;
    }

    public TipoComponentePlanEstudio getCpeOmitirTipo() {
        return cpeOmitirTipo;
    }

    public void setCpeOmitirTipo(TipoComponentePlanEstudio cpeOmitirTipo) {
        this.cpeOmitirTipo = cpeOmitirTipo;
    }

}
