/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import sv.gob.mined.siges.web.dto.catalogo.SgCategoriaCurso;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoModulo;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.enumerados.EnumEstadoCurso;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cdsPk", scope = SgCursoDocente.class)
public class SgCursoDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long cdsPk;
    
    private SgModuloFormacionDocente cdsModulo;
    
    private String cdsNombre;
    
    private String cdsDescripcion;
    
    private String cdsFacilitador;
    
    private LocalDate cdsFechaInicio;
    
    private LocalDate cdsFechaFin;
    
    private SgCategoriaCurso cdsCategoria;
    
    private SgTipoModulo cdsTipoModulo;
    
    private SgNivel cdsNivel;
    
    private SgEspecialidad cdsEspecialidad;
    
    private Integer cdsCupo;
    
    private SgSede cdsSede;
    
    private Boolean cdsOtraSede;
    
    private String cdsSedeNombre;
    
    private SgDireccion cdsSedeDireccion;
    
    private Boolean cdsAdmiteInscripcionSolicitud;
    
    private EnumEstadoCurso cdsEstado;
    
    private LocalDateTime cdsUltModFecha;
    
    private String cdsUltModUsuario;
    
    private Integer cdsVersion;
    
    private List<SgCeldaDiaHoraCurso> cdsCeldasDiaHora;

    public SgCursoDocente() {
        this.cdsCeldasDiaHora = new ArrayList<>();
        this.cdsSedeDireccion = new SgDireccion();
        this.cdsOtraSede = Boolean.FALSE;
        this.cdsAdmiteInscripcionSolicitud = Boolean.FALSE;
    }

    public SgCursoDocente(Long cdsPk) {
        this.cdsPk = cdsPk;
    }

    public Long getCdsPk() {
        return cdsPk;
    }

    public void setCdsPk(Long cdsPk) {
        this.cdsPk = cdsPk;
    }

    public SgModuloFormacionDocente getCdsModulo() {
        return cdsModulo;
    }

    public void setCdsModulo(SgModuloFormacionDocente cdsModulo) {
        this.cdsModulo = cdsModulo;
    }

    public String getCdsNombre() {
        return cdsNombre;
    }

    public void setCdsNombre(String cdsNombre) {
        this.cdsNombre = cdsNombre;
    }

    public String getCdsDescripcion() {
        return cdsDescripcion;
    }

    public void setCdsDescripcion(String cdsDescripcion) {
        this.cdsDescripcion = cdsDescripcion;
    }

    public String getCdsFacilitador() {
        return cdsFacilitador;
    }

    public void setCdsFacilitador(String cdsFacilitador) {
        this.cdsFacilitador = cdsFacilitador;
    }

    public LocalDate getCdsFechaInicio() {
        return cdsFechaInicio;
    }

    public void setCdsFechaInicio(LocalDate cdsFechaInicio) {
        this.cdsFechaInicio = cdsFechaInicio;
    }

    public LocalDate getCdsFechaFin() {
        return cdsFechaFin;
    }

    public void setCdsFechaFin(LocalDate cdsFechaFin) {
        this.cdsFechaFin = cdsFechaFin;
    }

    public SgCategoriaCurso getCdsCategoria() {
        return cdsCategoria;
    }

    public void setCdsCategoria(SgCategoriaCurso cdsCategoria) {
        this.cdsCategoria = cdsCategoria;
    }

    public SgTipoModulo getCdsTipoModulo() {
        return cdsTipoModulo;
    }

    public void setCdsTipoModulo(SgTipoModulo cdsTipoModulo) {
        this.cdsTipoModulo = cdsTipoModulo;
    }

    public SgNivel getCdsNivel() {
        return cdsNivel;
    }

    public void setCdsNivel(SgNivel cdsNivel) {
        this.cdsNivel = cdsNivel;
    }

    public SgEspecialidad getCdsEspecialidad() {
        return cdsEspecialidad;
    }

    public void setCdsEspecialidad(SgEspecialidad cdsEspecialidad) {
        this.cdsEspecialidad = cdsEspecialidad;
    }

    public Integer getCdsCupo() {
        return cdsCupo;
    }

    public void setCdsCupo(Integer cdsCupo) {
        this.cdsCupo = cdsCupo;
    }

    public SgSede getCdsSede() {
        return cdsSede;
    }

    public void setCdsSede(SgSede cdsSede) {
        this.cdsSede = cdsSede;
    }

    public Boolean getCdsOtraSede() {
        return cdsOtraSede;
    }

    public void setCdsOtraSede(Boolean cdsOtraSede) {
        this.cdsOtraSede = cdsOtraSede;
    }

    public String getCdsSedeNombre() {
        return cdsSedeNombre;
    }

    public void setCdsSedeNombre(String cdsSedeNombre) {
        this.cdsSedeNombre = cdsSedeNombre;
    }

    public SgDireccion getCdsSedeDireccion() {
        return cdsSedeDireccion;
    }

    public void setCdsSedeDireccion(SgDireccion cdsSedeDireccion) {
        this.cdsSedeDireccion = cdsSedeDireccion;
    }

    public Boolean getCdsAdmiteInscripcionSolicitud() {
        return cdsAdmiteInscripcionSolicitud;
    }

    public void setCdsAdmiteInscripcionSolicitud(Boolean cdsAdmiteInscripcionSolicitud) {
        this.cdsAdmiteInscripcionSolicitud = cdsAdmiteInscripcionSolicitud;
    }

    public EnumEstadoCurso getCdsEstado() {
        return cdsEstado;
    }

    public void setCdsEstado(EnumEstadoCurso cdsEstado) {
        this.cdsEstado = cdsEstado;
    }

    public LocalDateTime getCdsUltModFecha() {
        return cdsUltModFecha;
    }

    public void setCdsUltModFecha(LocalDateTime cdsUltModFecha) {
        this.cdsUltModFecha = cdsUltModFecha;
    }

    public String getCdsUltModUsuario() {
        return cdsUltModUsuario;
    }

    public void setCdsUltModUsuario(String cdsUltModUsuario) {
        this.cdsUltModUsuario = cdsUltModUsuario;
    }

    public Integer getCdsVersion() {
        return cdsVersion;
    }

    public void setCdsVersion(Integer cdsVersion) {
        this.cdsVersion = cdsVersion;
    }

    public List<SgCeldaDiaHoraCurso> getCdsCeldasDiaHora() {
        return cdsCeldasDiaHora;
    }

    public void setCdsCeldasDiaHora(List<SgCeldaDiaHoraCurso> cdsCeldasDiaHora) {
        this.cdsCeldasDiaHora = cdsCeldasDiaHora;
    }

    
    
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cdsPk != null ? cdsPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCursoDocente)) {
            return false;
        }
        SgCursoDocente other = (SgCursoDocente) object;
        if ((this.cdsPk == null && other.cdsPk != null) || (this.cdsPk != null && !this.cdsPk.equals(other.cdsPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCursoDocente[ cdsPk=" + cdsPk + " ]";
    }
    
}
