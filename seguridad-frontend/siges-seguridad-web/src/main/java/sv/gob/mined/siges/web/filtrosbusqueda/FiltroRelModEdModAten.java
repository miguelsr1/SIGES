/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

public class FiltroRelModEdModAten {

    private Long reaModalidadEducativa;
    private Long reaModalidadAtencion;
    private Long reaSubModalidad;
    private Long reaNivel;

    private Boolean inicializarGrados = Boolean.FALSE;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroRelModEdModAten() {
    }

    public Long getReaModalidadEducativa() {
        return reaModalidadEducativa;
    }

    public void setReaModalidadEducativa(Long reaModalidadEducativa) {
        this.reaModalidadEducativa = reaModalidadEducativa;
    }

    public Boolean getInicializarGrados() {
        return inicializarGrados;
    }

    public void setInicializarGrados(Boolean inicializarGrados) {
        this.inicializarGrados = inicializarGrados;
    }

    public Long getReaModalidadAtencion() {
        return reaModalidadAtencion;
    }

    public void setReaModalidadAtencion(Long reaModalidadAtencion) {
        this.reaModalidadAtencion = reaModalidadAtencion;
    }

    public Long getReaSubModalidad() {
        return reaSubModalidad;
    }

    public void setReaSubModalidad(Long reaSubModalidad) {
        this.reaSubModalidad = reaSubModalidad;
    }

    public Long getReaNivel() {
        return reaNivel;
    }

    public void setReaNivel(Long reaNivel) {
        this.reaNivel = reaNivel;
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
}
