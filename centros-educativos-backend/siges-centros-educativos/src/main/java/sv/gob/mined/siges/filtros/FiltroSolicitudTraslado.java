/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */

package sv.gob.mined.siges.filtros;
import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudTraslado;

public class FiltroSolicitudTraslado implements Serializable {
    
    private Long sotPk;
    
    private Long perNie;
    private String perPrimerNombre;
    private String perSegundoNombre;
    private String perTerceroNombre;
    private String perPrimerApellido;
    private String perSegundoApellido;
    private String perTercerApellido;
    private String perNombreCompleto;
    private Long perPersonaPk;
    
    private Long departamento;
    private Long municipio;
    private Long centroEducativo;
    private Long centroEducativoDestino;
    private Boolean buscarMismaSede;
    
    private EnumEstadoSolicitudTraslado sotEstado;
    private Long sotMotivoTraslado;
    
    private EnumEstadoSolicitudTraslado[] solicitudEnProceso;
    private List<EnumEstadoSolicitudTraslado> sotEstados;
    
    private Integer anioLectivo;
    private String sexo;
    
    private Boolean verDestino;
    private Boolean verOrigen;
    
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    

    public FiltroSolicitudTraslado() {
        this.verOrigen = Boolean.TRUE;
        this.verDestino = Boolean.TRUE;
    }

    public Long getSotPk() {
        return sotPk;
    }

    public void setSotPk(Long sotPk) {
        this.sotPk = sotPk;
    }

    public Long getPerNie() {
        return perNie;
    }

    public void setPerNie(Long perNie) {
        this.perNie = perNie;
    }

    public String getPerPrimerNombre() {
        return perPrimerNombre;
    }

    public void setPerPrimerNombre(String perPrimerNombre) {
        this.perPrimerNombre = perPrimerNombre;
    }

    public String getPerSegundoNombre() {
        return perSegundoNombre;
    }

    public void setPerSegundoNombre(String perSegundoNombre) {
        this.perSegundoNombre = perSegundoNombre;
    }

    public String getPerTerceroNombre() {
        return perTerceroNombre;
    }

    public void setPerTerceroNombre(String perTerceroNombre) {
        this.perTerceroNombre = perTerceroNombre;
    }

    public String getPerPrimerApellido() {
        return perPrimerApellido;
    }

    public void setPerPrimerApellido(String perPrimerApellido) {
        this.perPrimerApellido = perPrimerApellido;
    }

    public String getPerSegundoApellido() {
        return perSegundoApellido;
    }

    public void setPerSegundoApellido(String perSegundoApellido) {
        this.perSegundoApellido = perSegundoApellido;
    }

    public String getPerTercerApellido() {
        return perTercerApellido;
    }

    public void setPerTercerApellido(String perTercerApellido) {
        this.perTercerApellido = perTercerApellido;
    }

    public Long getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Long departamento) {
        this.departamento = departamento;
    }

    public Long getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Long municipio) {
        this.municipio = municipio;
    }

    public Long getCentroEducativo() {
        return centroEducativo;
    }

    public void setCentroEducativo(Long centroEducativo) {
        this.centroEducativo = centroEducativo;
    }

    public EnumEstadoSolicitudTraslado getSotEstado() {
        return sotEstado;
    }

    public void setSotEstado(EnumEstadoSolicitudTraslado sotEstado) {
        this.sotEstado = sotEstado;
    }

    public Long getSotMotivoTraslado() {
        return sotMotivoTraslado;
    }

    public void setSotMotivoTraslado(Long sotMotivoTraslado) {
        this.sotMotivoTraslado = sotMotivoTraslado;
    }

    public EnumEstadoSolicitudTraslado[] getSolicitudEnProceso() {
        return solicitudEnProceso;
    }

    public void setSolicitudEnProceso(EnumEstadoSolicitudTraslado[] solicitudEnProceso) {
        this.solicitudEnProceso = solicitudEnProceso;
    }

    public Integer getAnioLectivo() {
        return anioLectivo;
    }

    public void setAnioLectivo(Integer anioLectivo) {
        this.anioLectivo = anioLectivo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Long getCentroEducativoDestino() {
        return centroEducativoDestino;
    }

    public void setCentroEducativoDestino(Long centroEducativoDestino) {
        this.centroEducativoDestino = centroEducativoDestino;
    }

    public Boolean getBuscarMismaSede() {
        return buscarMismaSede;
    }

    public void setBuscarMismaSede(Boolean buscarMismaSede) {
        this.buscarMismaSede = buscarMismaSede;
    }

    public List<EnumEstadoSolicitudTraslado> getSotEstados() {
        return sotEstados;
    }

    public void setSotEstados(List<EnumEstadoSolicitudTraslado> sotEstados) {
        this.sotEstados = sotEstados;
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

    public String getPerNombreCompleto() {
        return perNombreCompleto;
    }

    public void setPerNombreCompleto(String perNombreCompleto) {
        this.perNombreCompleto = perNombreCompleto;
    }

    public Boolean getVerDestino() {
        return verDestino;
    }

    public void setVerDestino(Boolean verDestino) {
        this.verDestino = verDestino;
    }

    public Boolean getVerOrigen() {
        return verOrigen;
    }

    public void setVerOrigen(Boolean verOrigen) {
        this.verOrigen = verOrigen;
    }

    public Long getPerPersonaPk() {
        return perPersonaPk;
    }

    public void setPerPersonaPk(Long perPersonaPk) {
        this.perPersonaPk = perPersonaPk;
    }

    @Override
    public String toString() {
        return "FiltroSolicitudTraslado{" + "sotPk=" + sotPk + ", perNie=" + perNie + ", departamento=" + departamento + ", municipio=" + municipio + ", centroEducativo=" + centroEducativo + ", centroEducativoDestino=" + centroEducativoDestino + ", buscarMismaSede=" + buscarMismaSede + ", sotEstado=" + sotEstado + ", sotMotivoTraslado=" + sotMotivoTraslado + ", solicitudEnProceso=" + solicitudEnProceso + ", sotEstados=" + sotEstados + ", anioLectivo=" + anioLectivo + ", sexo=" + sexo + ", verDestino=" + verDestino + ", verOrigen=" + verOrigen + ", first=" + first + ", maxResults=" + maxResults + ", orderBy=" + orderBy + ", ascending=" + ascending + ", incluirCampos=" + incluirCampos + '}';
    }

      
}

