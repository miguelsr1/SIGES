/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudPlaza;
import java.time.LocalDate;
import sv.gob.mined.siges.enumerados.EnumSituacionPlaza;
import sv.gob.mined.siges.enumerados.EnumTipoPlaza;

public class FiltroSolicitudPlaza implements Serializable {
    
    private Long departamento;
    private Long municipio;
    private EnumTipoPlaza tipo;
    private Long nivel;
    private Long jornada;
    private Long modalidadAtencion;
    private Long modalidadEducativa;
    private Long opcion;
    private Long especialidad;    
    private EnumEstadoSolicitudPlaza estado;
    private Long sede;
    private Long idPuesto;
    private Integer partida;
    private Integer subpartida;
    private Integer codigo;
    private EnumSituacionPlaza situacion;
    private Integer anioPartida;
    private LocalDate postulacionInicio;
    private LocalDate postulacionFin;
    private Boolean verSolopPostulacionesDeUsuario;
    private String aplicacionCodigoUsuario;
    private Boolean esPosiblePublicar;
    
    private String[] incluirCampos;
    private String securityOperation;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private Boolean seNecesitaDistinct;
    
    public FiltroSolicitudPlaza() {
    }

    //<editor-fold defaultstate="collapsed" desc="GET & SET">
    

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

    public EnumTipoPlaza getTipo() {
        return tipo;
    }

    public void setTipo(EnumTipoPlaza tipo) {
        this.tipo = tipo;
    }

    public Long getNivel() {
        return nivel;
    }

    public void setNivel(Long nivel) {
        this.nivel = nivel;
    }

    public Long getJornada() {
        return jornada;
    }

    public void setJornada(Long jornada) {
        this.jornada = jornada;
    }

    public Long getModalidadAtencion() {
        return modalidadAtencion;
    }

    public void setModalidadAtencion(Long modalidadAtencion) {
        this.modalidadAtencion = modalidadAtencion;
    }

    public Long getModalidadEducativa() {
        return modalidadEducativa;
    }

    public void setModalidadEducativa(Long modalidadEducativa) {
        this.modalidadEducativa = modalidadEducativa;
    }

    public Long getOpcion() {
        return opcion;
    }

    public void setOpcion(Long opcion) {
        this.opcion = opcion;
    }

    public Long getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Long especialidad) {
        this.especialidad = especialidad;
    }

    public EnumEstadoSolicitudPlaza getEstado() {
        return estado;
    }

    public void setEstado(EnumEstadoSolicitudPlaza estado) {
        this.estado = estado;
    }

    public Long getSede() {
        return sede;
    }

    public void setSede(Long sede) {
        this.sede = sede;
    }

    public Long getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Long idPuesto) {
        this.idPuesto = idPuesto;
    }

    public Integer getPartida() {
        return partida;
    }

    public void setPartida(Integer partida) {
        this.partida = partida;
    }

    public Integer getSubpartida() {
        return subpartida;
    }

    public void setSubpartida(Integer subpartida) {
        this.subpartida = subpartida;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public EnumSituacionPlaza getSituacion() {
        return situacion;
    }

    public void setSituacion(EnumSituacionPlaza situacion) {
        this.situacion = situacion;
    }

    public Integer getAnioPartida() {
        return anioPartida;
    }

    public void setAnioPartida(Integer anioPartida) {
        this.anioPartida = anioPartida;
    }

    public LocalDate getPostulacionInicio() {
        return postulacionInicio;
    }

    public void setPostulacionInicio(LocalDate postulacionInicio) {
        this.postulacionInicio = postulacionInicio;
    }

    public LocalDate getPostulacionFin() {
        return postulacionFin;
    }

    public void setPostulacionFin(LocalDate postulacionFin) {
        this.postulacionFin = postulacionFin;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
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
    public Boolean getVerSolopPostulacionesDeUsuario() {
        return verSolopPostulacionesDeUsuario;
    }

    public void setVerSolopPostulacionesDeUsuario(Boolean verSolopPostulacionesDeUsuario) {
        this.verSolopPostulacionesDeUsuario = verSolopPostulacionesDeUsuario;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }
    
    //</editor-fold>

    public String getAplicacionCodigoUsuario() {
        return aplicacionCodigoUsuario;
    }

    public void setAplicacionCodigoUsuario(String aplicacionCodigoUsuario) {
        this.aplicacionCodigoUsuario = aplicacionCodigoUsuario;
    }

    public Boolean getEsPosiblePublicar() {
        return esPosiblePublicar;
    }

    public void setEsPosiblePublicar(Boolean esPosiblePublicar) {
        this.esPosiblePublicar = esPosiblePublicar;
    }

    
}
