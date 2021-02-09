/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;
import sv.gob.mined.siges.web.enumerados.EnumTipoPeriodoSeccion;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "pcaPk", scope = SgPeriodoCalificacion.class)
public class SgPeriodoCalificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long pcaPk;

    private String pcaNombre;

    private Integer pcaNumero;

    private LocalDateTime pcaUltModFecha;

    private String pcaUltModUsuario;

    private Integer pcaVersion;
    
    private Boolean pcaPermiteCalificarSinNie;

    private SgModalidad pcaModalidad;

    private SgModalidadAtencion pcaModalidadAtencion;
    
    private SgSubModalidadAtencion pcaSubModalidadAtencion;

    private SgCalendario pcaCalendario;

    private List<SgRangoFecha> pcaRangoFecha;
    
    private Boolean pcaEsPrueba;
    
    //Las secciones pueden ser anuales o pertencer a un periodo especifico del año
    //Cuando no es anual se especifica a que periodo del año pertenece con pcaNumeroPeriodo
    private EnumTipoPeriodoSeccion pcaTipoPeriodo;
   
    private Integer pcaNumeroPeriodo;

    public SgPeriodoCalificacion() {
        this.pcaRangoFecha = new ArrayList();
        this.pcaPermiteCalificarSinNie = Boolean.TRUE;
        pcaEsPrueba=Boolean.FALSE;
        pcaTipoPeriodo=EnumTipoPeriodoSeccion.ANUAL;

    }
    
    @JsonIgnore
    public String getNombreTipoPeriodo() {
        if (pcaTipoPeriodo != null && !EnumTipoPeriodoSeccion.ANUAL.equals(pcaTipoPeriodo) && pcaNumeroPeriodo!=null) {
            switch(pcaNumeroPeriodo){
                case 1: return pcaNombre+" - Primer período";
                case 2: return pcaNombre+" - Segundo período";
            }
            return pcaNombre ;
        } else {
            return pcaNombre;
        }
    }

    public Long getPcaPk() {
        return pcaPk;
    }

    public void setPcaPk(Long pcaPk) {
        this.pcaPk = pcaPk;
    }

    public String getPcaNombre() {
        return pcaNombre;
    }

    public void setPcaNombre(String pcaNombre) {
        this.pcaNombre = pcaNombre;
    }

    public Integer getPcaNumero() {
        return pcaNumero;
    }

    public void setPcaNumero(Integer pcaNumero) {
        this.pcaNumero = pcaNumero;
    }

    public SgModalidad getPcaModalidad() {
        return pcaModalidad;
    }

    public void setPcaModalidad(SgModalidad pcaModalidad) {
        this.pcaModalidad = pcaModalidad;
    }

    public SgModalidadAtencion getPcaModalidadAtencion() {
        return pcaModalidadAtencion;
    }

    public void setPcaModalidadAtencion(SgModalidadAtencion pcaModalidadAtencion) {
        this.pcaModalidadAtencion = pcaModalidadAtencion;
    }

    public SgCalendario getPcaCalendario() {
        return pcaCalendario;
    }

    public void setPcaCalendario(SgCalendario pcaCalendario) {
        this.pcaCalendario = pcaCalendario;
    }

    public LocalDateTime getPcaUltModFecha() {
        return pcaUltModFecha;
    }

    public void setPcaUltModFecha(LocalDateTime pcaUltModFecha) {
        this.pcaUltModFecha = pcaUltModFecha;
    }

    public String getPcaUltModUsuario() {
        return pcaUltModUsuario;
    }

    public void setPcaUltModUsuario(String pcaUltModUsuario) {
        this.pcaUltModUsuario = pcaUltModUsuario;
    }

    public Integer getPcaVersion() {
        return pcaVersion;
    }

    public void setPcaVersion(Integer pcaVersion) {
        this.pcaVersion = pcaVersion;
    }

    public List<SgRangoFecha> getPcaRangoFecha() {
        return pcaRangoFecha;
    }

    public void setPcaRangoFecha(List<SgRangoFecha> pcaRangoFecha) {
        this.pcaRangoFecha = pcaRangoFecha;
    }

    public SgSubModalidadAtencion getPcaSubModalidadAtencion() {
        return pcaSubModalidadAtencion;
    }

    public void setPcaSubModalidadAtencion(SgSubModalidadAtencion pcaSubModalidadAtencion) {
        this.pcaSubModalidadAtencion = pcaSubModalidadAtencion;
    }

    public Boolean getPcaPermiteCalificarSinNie() {
        return pcaPermiteCalificarSinNie;
    }

    public void setPcaPermiteCalificarSinNie(Boolean pcaPermiteCalificarSinNie) {
        this.pcaPermiteCalificarSinNie = pcaPermiteCalificarSinNie;
    }

    public Boolean getPcaEsPrueba() {
        return pcaEsPrueba;
    }

    public void setPcaEsPrueba(Boolean pcaEsPrueba) {
        this.pcaEsPrueba = pcaEsPrueba;
    }

    public EnumTipoPeriodoSeccion getPcaTipoPeriodo() {
        return pcaTipoPeriodo;
    }

    public void setPcaTipoPeriodo(EnumTipoPeriodoSeccion pcaTipoPeriodo) {
        this.pcaTipoPeriodo = pcaTipoPeriodo;
    }

    public Integer getPcaNumeroPeriodo() {
        return pcaNumeroPeriodo;
    }

    public void setPcaNumeroPeriodo(Integer pcaNumeroPeriodo) {
        this.pcaNumeroPeriodo = pcaNumeroPeriodo;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pcaPk != null ? pcaPk.hashCode() : 0);
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
        final SgPeriodoCalificacion other = (SgPeriodoCalificacion) obj;
        if (!Objects.equals(this.pcaPk, other.pcaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgPeriodoCalificacion[ pcaPk=" + pcaPk + " ]";
    }

}
