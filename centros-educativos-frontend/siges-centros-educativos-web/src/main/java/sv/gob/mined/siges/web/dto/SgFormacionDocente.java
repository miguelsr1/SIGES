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
import sv.gob.mined.siges.web.dto.catalogo.SgCategoriaFormacionDocente;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;
import sv.gob.mined.siges.web.dto.catalogo.SgNivelFormacionDocente;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.dto.catalogo.SgModuloFormacionDocente;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "fdoPk", scope = SgFormacionDocente.class)
public class SgFormacionDocente implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long fdoPk;

    private SgPersonalSedeEducativa fdoPersonalSede;

    private SgNivelFormacionDocente fdoNivel;

    private SgCategoriaFormacionDocente fdoCategoria;

    private SgEspecialidad fdoEspecialidad;

    private SgModuloFormacionDocente fdoModulo;

    private LocalDate fdoFechaDesde;

    private LocalDate fdoFechaHasta;

    private SgDepartamento fdoDepartamento;

    private SgSede fdoSede;

    private Boolean fdoAprobado;

    private String fdoCalificacionFinal;

    private LocalDateTime fdoUltModFecha;

    private String fdoUltModUsuario;

    private Integer fdoVersion;

    public SgFormacionDocente() {
        fdoAprobado = Boolean.TRUE;
    }

    public SgFormacionDocente(Long fdoPk) {
        this.fdoPk = fdoPk;
    }

    public Long getFdoPk() {
        return fdoPk;
    }

    public void setFdoPk(Long fdoPk) {
        this.fdoPk = fdoPk;
    }

    public SgNivelFormacionDocente getFdoNivel() {
        return fdoNivel;
    }

    public void setFdoNivel(SgNivelFormacionDocente fdoNivel) {
        this.fdoNivel = fdoNivel;
    }

    public SgCategoriaFormacionDocente getFdoCategoria() {
        return fdoCategoria;
    }

    public void setFdoCategoria(SgCategoriaFormacionDocente fdoCategoria) {
        this.fdoCategoria = fdoCategoria;
    }

    public SgEspecialidad getFdoEspecialidad() {
        return fdoEspecialidad;
    }

    public void setFdoEspecialidad(SgEspecialidad fdoEspecialidad) {
        this.fdoEspecialidad = fdoEspecialidad;
    }

    public SgModuloFormacionDocente getFdoModulo() {
        return fdoModulo;
    }

    public void setFdoModulo(SgModuloFormacionDocente fdoModulo) {
        this.fdoModulo = fdoModulo;
    }

    public LocalDate getFdoFechaDesde() {
        return fdoFechaDesde;
    }

    public void setFdoFechaDesde(LocalDate fdoFechaDesde) {
        this.fdoFechaDesde = fdoFechaDesde;
    }

    public LocalDate getFdoFechaHasta() {
        return fdoFechaHasta;
    }

    public void setFdoFechaHasta(LocalDate fdoFechaHasta) {
        this.fdoFechaHasta = fdoFechaHasta;
    }

    public SgDepartamento getFdoDepartamento() {
        return fdoDepartamento;
    }

    public void setFdoDepartamento(SgDepartamento fdoDepartamento) {
        this.fdoDepartamento = fdoDepartamento;
    }

    public SgSede getFdoSede() {
        return fdoSede;
    }

    public void setFdoSede(SgSede fdoSede) {
        this.fdoSede = fdoSede;
    }

    public Boolean getFdoAprobado() {
        return fdoAprobado;
    }

    public void setFdoAprobado(Boolean fdoAprobado) {
        this.fdoAprobado = fdoAprobado;
    }

    public String getFdoCalificacionFinal() {
        return fdoCalificacionFinal;
    }

    public void setFdoCalificacionFinal(String fdoCalificacionFinal) {
        this.fdoCalificacionFinal = fdoCalificacionFinal;
    }

    public LocalDateTime getFdoUltModFecha() {
        return fdoUltModFecha;
    }

    public void setFdoUltModFecha(LocalDateTime fdoUltModFecha) {
        this.fdoUltModFecha = fdoUltModFecha;
    }

    public String getFdoUltModUsuario() {
        return fdoUltModUsuario;
    }

    public void setFdoUltModUsuario(String fdoUltModUsuario) {
        this.fdoUltModUsuario = fdoUltModUsuario;
    }

    public Integer getFdoVersion() {
        return fdoVersion;
    }

    public void setFdoVersion(Integer fdoVersion) {
        this.fdoVersion = fdoVersion;
    }

    public SgPersonalSedeEducativa getFdoPersonalSede() {
        return fdoPersonalSede;
    }

    public void setFdoPersonalSede(SgPersonalSedeEducativa fdoPersonalSede) {
        this.fdoPersonalSede = fdoPersonalSede;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (fdoPk != null ? fdoPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgFormacionDocente)) {
            return false;
        }
        SgFormacionDocente other = (SgFormacionDocente) object;
        if ((this.fdoPk == null && other.fdoPk != null) || (this.fdoPk != null && !this.fdoPk.equals(other.fdoPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgFormacionDocente[ fdoPk=" + fdoPk + " ]";
    }

}
