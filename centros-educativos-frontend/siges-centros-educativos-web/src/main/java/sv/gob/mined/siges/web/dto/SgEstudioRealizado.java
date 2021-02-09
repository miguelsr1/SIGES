/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import sv.gob.mined.siges.web.dto.catalogo.SgEscolaridad;
import sv.gob.mined.siges.web.dto.catalogo.SgMaximoNivelEducativoAlcanzado;
import sv.gob.mined.siges.web.dto.catalogo.SgSistemaGestionContenido;
import sv.gob.mined.siges.web.enumerados.EnumNivelSGC;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "erePk", scope = SgEstudioRealizado.class)
public class SgEstudioRealizado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long erePk;

    private SgPersonalSedeEducativa erePersonalSede;

    private Boolean ereInternet;

    private SgSistemaGestionContenido ereSistemaGestionContenido;

    private EnumNivelSGC ereNivelManejoSgc;

    private String ereHabilidades;

    private String ereDestrezas;

    private SgEscolaridad ereEscolaridad;

    private LocalDateTime ereUltModFecha;

    private String ereUltModUsuario;

    private Integer ereVersion;

    private List<SgIdiomaRealizado> ereIdiomas;

    private List<SgEstudioSuperior> ereEstudiosSuperiores;

    private List<SgCapacitacion> ereCapacitaciones;
    
    private SgMaximoNivelEducativoAlcanzado ereMaximoNivelEducativoAlcanzado;    

    public SgEstudioRealizado() {
        this.ereIdiomas = new ArrayList<>();
        this.ereEstudiosSuperiores = new ArrayList<>();
        this.ereCapacitaciones = new ArrayList<>();
        ereInternet=Boolean.FALSE;
        ereNivelManejoSgc=EnumNivelSGC.BAJO;
    }

    public SgEstudioRealizado(Long erePk, Integer ereVersion) {
        this.erePk = erePk;
        this.ereVersion = ereVersion;
    }

    public Long getErePk() {
        return erePk;
    }

    public void setErePk(Long erePk) {
        this.erePk = erePk;
    }

    public Boolean getEreInternet() {
        return ereInternet;
    }

    public void setEreInternet(Boolean ereInternet) {
        this.ereInternet = ereInternet;
    }

    public SgSistemaGestionContenido getEreSistemaGestionContenido() {
        return ereSistemaGestionContenido;
    }

    public void setEreSistemaGestionContenido(SgSistemaGestionContenido ereSistemaGestionContenido) {
        this.ereSistemaGestionContenido = ereSistemaGestionContenido;
    }

    public EnumNivelSGC getEreNivelManejoSgc() {
        return ereNivelManejoSgc;
    }

    public void setEreNivelManejoSgc(EnumNivelSGC ereNivelManejoSgc) {
        this.ereNivelManejoSgc = ereNivelManejoSgc;
    }

    public String getEreHabilidades() {
        return ereHabilidades;
    }

    public void setEreHabilidades(String ereHabilidades) {
        this.ereHabilidades = ereHabilidades;
    }

    public String getEreDestrezas() {
        return ereDestrezas;
    }

    public void setEreDestrezas(String ereDestrezas) {
        this.ereDestrezas = ereDestrezas;
    }

    public SgEscolaridad getEreEscolaridad() {
        return ereEscolaridad;
    }

    public void setEreEscolaridad(SgEscolaridad ereEscolaridad) {
        this.ereEscolaridad = ereEscolaridad;
    }

    public LocalDateTime getEreUltModFecha() {
        return ereUltModFecha;
    }

    public void setEreUltModFecha(LocalDateTime ereUltModFecha) {
        this.ereUltModFecha = ereUltModFecha;
    }

    public String getEreUltModUsuario() {
        return ereUltModUsuario;
    }

    public void setEreUltModUsuario(String ereUltModUsuario) {
        this.ereUltModUsuario = ereUltModUsuario;
    }

    public Integer getEreVersion() {
        return ereVersion;
    }

    public void setEreVersion(Integer ereVersion) {
        this.ereVersion = ereVersion;
    }

    public List<SgIdiomaRealizado> getEreIdiomas() {
        return ereIdiomas;
    }

    public void setEreIdiomas(List<SgIdiomaRealizado> ereIdiomas) {
        this.ereIdiomas = ereIdiomas;
    }

    public List<SgCapacitacion> getEreCapacitaciones() {
        return ereCapacitaciones;
    }

    public void setEreCapacitaciones(List<SgCapacitacion> ereCapacitaciones) {
        this.ereCapacitaciones = ereCapacitaciones;
    }

    public SgPersonalSedeEducativa getErePersonalSede() {
        return erePersonalSede;
    }

    public void setErePersonalSede(SgPersonalSedeEducativa erePersonalSede) {
        this.erePersonalSede = erePersonalSede;
    }

    public SgMaximoNivelEducativoAlcanzado getEreMaximoNivelEducativoAlcanzado() {
        return ereMaximoNivelEducativoAlcanzado;
    }

    public void setEreMaximoNivelEducativoAlcanzado(SgMaximoNivelEducativoAlcanzado ereMaximoNivelEducativoAlcanzado) {
        this.ereMaximoNivelEducativoAlcanzado = ereMaximoNivelEducativoAlcanzado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (erePk != null ? erePk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgEstudioRealizado)) {
            return false;
        }
        SgEstudioRealizado other = (SgEstudioRealizado) object;
        if ((this.erePk == null && other.erePk != null) || (this.erePk != null && !this.erePk.equals(other.erePk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstudioRealizado[ erePk=" + erePk + " ]";
    }

    public List<SgEstudioSuperior> getEreEstudiosSuperiores() {
        return ereEstudiosSuperiores;
    }

    public void setEreEstudiosSuperiores(List<SgEstudioSuperior> ereEstudiosSuperiores) {
        this.ereEstudiosSuperiores = ereEstudiosSuperiores;
    }

}
