/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgCargo;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.enumerados.EnumCaracterizacionPlaza;
import sv.gob.mined.siges.web.enumerados.EnumEstadoPlaza;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudPlaza;
import sv.gob.mined.siges.web.enumerados.EnumTipoPlaza;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "splPk", scope = SgSolicitudPlaza.class)
public class SgSolicitudPlaza implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private Long splPk;
    
    private SgSede splSedeSolicitante;
    
    private EnumTipoPlaza splTipoPlaza;
    
    private SgNivel splNivel;
    
    private SgModalidad splModalidadEducativa;
    
    private SgModalidadAtencion splModalidadAtencion;
    
    private SgOpcion splOpcion;
    
    private SgComponentePlanEstudio splComponentePlanEstudio;
    
    private SgEspecialidad splEspecialidad;
    
    private SgFuenteFinanciamiento splFuenteFinanciamiento;
    
    private LocalDate splFechaDesde;
    
    private LocalDate splFechaHasta;
    
    private SgJornadaLaboral splJornada;
    
    private String splDescripcion;
    
    private EnumEstadoSolicitudPlaza splEstado;
    
    private SgCargo splTipoNombramiento;
    
    private LocalDateTime splUltModFecha;
    
    private String splUltModUsuario;
    
    private Integer splVersion;
    
    private String splCodigo;
    
    private Integer splIdPlaza;
    
    private Integer splAnioPartida;
    
    private String splNombre;
    
    private EnumCaracterizacionPlaza splCaracterizacion;
    
    private EnumEstadoPlaza splEstadoPlaza;
    
    private LocalDate splPostulacionInicio;
    
    private LocalDate splPostulacionFin;
    
    private Boolean splPlazaExistente;
    
    private SgPlaza splPlazaFk;
    
    private Boolean splPuedeSerPublicado;


    public SgSolicitudPlaza() {
        splPlazaExistente=Boolean.FALSE;
    }
    
    public Boolean getAbiertaPostulacion(){
        if(splPostulacionInicio!=null && splPostulacionFin!=null){
            return !(LocalDate.now().isBefore(splPostulacionInicio) || LocalDate.now().isAfter(splPostulacionFin));
        }
        if(splPostulacionInicio!=null && splPostulacionFin==null){
            return LocalDate.now().isAfter(splPostulacionInicio);
        }
        if(splPostulacionInicio==null && splPostulacionFin!=null){
            return LocalDate.now().isBefore(splPostulacionFin);
        }
        return Boolean.FALSE;
    }

    public SgSolicitudPlaza(Long splPk) {
        this.splPk = splPk;
    }

    public Long getSplPk() {
        return splPk;
    }

    public void setSplPk(Long splPk) {
        this.splPk = splPk;
    }

    public SgSede getSplSedeSolicitante() {
        return splSedeSolicitante;
    }

    public void setSplSedeSolicitante(SgSede splSedeSolicitante) {
        this.splSedeSolicitante = splSedeSolicitante;
    }

    public EnumTipoPlaza getSplTipoPlaza() {
        return splTipoPlaza;
    }

    public void setSplTipoPlaza(EnumTipoPlaza splTipoPlaza) {
        this.splTipoPlaza = splTipoPlaza;
    }

    public SgNivel getSplNivel() {
        return splNivel;
    }

    public void setSplNivel(SgNivel splNivel) {
        this.splNivel = splNivel;
    }

    public SgModalidad getSplModalidadEducativa() {
        return splModalidadEducativa;
    }

    public void setSplModalidadEducativa(SgModalidad splModalidadEducativa) {
        this.splModalidadEducativa = splModalidadEducativa;
    }

    public SgModalidadAtencion getSplModalidadAtencion() {
        return splModalidadAtencion;
    }

    public void setSplModalidadAtencion(SgModalidadAtencion splModalidadAtencion) {
        this.splModalidadAtencion = splModalidadAtencion;
    }

    public SgOpcion getSplOpcion() {
        return splOpcion;
    }

    public void setSplOpcion(SgOpcion splOpcion) {
        this.splOpcion = splOpcion;
    }

    public SgComponentePlanEstudio getSplComponentePlanEstudio() {
        return splComponentePlanEstudio;
    }

    public void setSplComponentePlanEstudio(SgComponentePlanEstudio splComponentePlanEstudio) {
        this.splComponentePlanEstudio = splComponentePlanEstudio;
    }

    public SgEspecialidad getSplEspecialidad() {
        return splEspecialidad;
    }

    public void setSplEspecialidad(SgEspecialidad splEspecialidad) {
        this.splEspecialidad = splEspecialidad;
    }

    public SgFuenteFinanciamiento getSplFuenteFinanciamiento() {
        return splFuenteFinanciamiento;
    }

    public void setSplFuenteFinanciamiento(SgFuenteFinanciamiento splFuenteFinanciamiento) {
        this.splFuenteFinanciamiento = splFuenteFinanciamiento;
    }

    public LocalDate getSplFechaDesde() {
        return splFechaDesde;
    }

    public void setSplFechaDesde(LocalDate splFechaDesde) {
        this.splFechaDesde = splFechaDesde;
    }

    public LocalDate getSplFechaHasta() {
        return splFechaHasta;
    }

    public void setSplFechaHasta(LocalDate splFechaHasta) {
        this.splFechaHasta = splFechaHasta;
    }

    public SgJornadaLaboral getSplJornada() {
        return splJornada;
    }

    public void setSplJornada(SgJornadaLaboral splJornada) {
        this.splJornada = splJornada;
    }

    public String getSplDescripcion() {
        return splDescripcion;
    }

    public void setSplDescripcion(String splDescripcion) {
        this.splDescripcion = splDescripcion;
    }

    public EnumEstadoSolicitudPlaza getSplEstado() {
        return splEstado;
    }

    public void setSplEstado(EnumEstadoSolicitudPlaza splEstado) {
        this.splEstado = splEstado;
    }

    public SgCargo getSplTipoNombramiento() {
        return splTipoNombramiento;
    }

    public void setSplTipoNombramiento(SgCargo splTipoNombramiento) {
        this.splTipoNombramiento = splTipoNombramiento;
    }

    public LocalDateTime getSplUltModFecha() {
        return splUltModFecha;
    }

    public void setSplUltModFecha(LocalDateTime splUltModFecha) {
        this.splUltModFecha = splUltModFecha;
    }

    public String getSplUltModUsuario() {
        return splUltModUsuario;
    }

    public void setSplUltModUsuario(String splUltModUsuario) {
        this.splUltModUsuario = splUltModUsuario;
    }

    public Integer getSplVersion() {
        return splVersion;
    }

    public void setSplVersion(Integer splVersion) {
        this.splVersion = splVersion;
    }

    public String getSplCodigo() {
        return splCodigo;
    }

    public void setSplCodigo(String splCodigo) {
        this.splCodigo = splCodigo;
    }

    public Integer getSplIdPlaza() {
        return splIdPlaza;
    }

    public void setSplIdPlaza(Integer splIdPlaza) {
        this.splIdPlaza = splIdPlaza;
    }

    public Integer getSplAnioPartida() {
        return splAnioPartida;
    }

    public void setSplAnioPartida(Integer splAnioPartida) {
        this.splAnioPartida = splAnioPartida;
    }

    public String getSplNombre() {
        return splNombre;
    }

    public void setSplNombre(String splNombre) {
        this.splNombre = splNombre;
    }

    public EnumCaracterizacionPlaza getSplCaracterizacion() {
        return splCaracterizacion;
    }

    public void setSplCaracterizacion(EnumCaracterizacionPlaza splCaracterizacion) {
        this.splCaracterizacion = splCaracterizacion;
    }

    public EnumEstadoPlaza getSplEstadoPlaza() {
        return splEstadoPlaza;
    }

    public void setSplEstadoPlaza(EnumEstadoPlaza splEstadoPlaza) {
        this.splEstadoPlaza = splEstadoPlaza;
    }

    public LocalDate getSplPostulacionInicio() {
        return splPostulacionInicio;
    }

    public void setSplPostulacionInicio(LocalDate splPostulacionInicio) {
        this.splPostulacionInicio = splPostulacionInicio;
    }

    public LocalDate getSplPostulacionFin() {
        return splPostulacionFin;
    }

    public void setSplPostulacionFin(LocalDate splPostulacionFin) {
        this.splPostulacionFin = splPostulacionFin;
    }

    public Boolean getSplPlazaExistente() {
        return splPlazaExistente;
    }

    public void setSplPlazaExistente(Boolean splPlazaExistente) {
        this.splPlazaExistente = splPlazaExistente;
    }

    public SgPlaza getSplPlazaFk() {
        return splPlazaFk;
    }

    public void setSplPlazaFk(SgPlaza splPlazaFk) {
        this.splPlazaFk = splPlazaFk;
    }

    public Boolean getSplPuedeSerPublicado() {
        return splPuedeSerPublicado;
    }

    public void setSplPuedeSerPublicado(Boolean splPuedeSerPublicado) {
        this.splPuedeSerPublicado = splPuedeSerPublicado;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (splPk != null ? splPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgSolicitudPlaza)) {
            return false;
        }
        SgSolicitudPlaza other = (SgSolicitudPlaza) object;
        if ((this.splPk == null && other.splPk != null) || (this.splPk != null && !this.splPk.equals(other.splPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSolicitudPlaza[ splPk=" + splPk + " ]";
    }
    
}
