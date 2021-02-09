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
import sv.gob.mined.siges.web.dto.catalogo.SgAlcanceCapacitacion;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadEstudio;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoCapacitacion;
import sv.gob.mined.siges.web.utilidades.ViewDto;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "capPk", scope = SgCapacitacion.class)
public class SgCapacitacion implements Serializable, ViewDto {

    private static final long serialVersionUID = 1L;

    private Long capPk;

    private SgEstudioRealizado capEstudios;

    private String capNombreCapacitacion;

    private SgTipoCapacitacion capTipoCapacitacion;

    private SgAlcanceCapacitacion capAlcanceCapacitacion;

    private String capInstitucionEstudio;

    private LocalDate capDesde;

    private LocalDate capHasta;

    private Integer capDuracionHoras;

    private SgModalidadEstudio capModalidad;

    private Boolean capCursoAcreditado;
    
    private Boolean capValidado;

    private LocalDateTime capUltModFecha;

    private String capUltModUsuario;

    private Integer capVersion;

    private Boolean capPkForView;

    public SgCapacitacion() {
        this.capValidado = Boolean.FALSE;
    }

    public SgCapacitacion(Long capPk) {
        this.capPk = capPk;
    }

    public Long getCapPk() {
        return capPk;
    }

    public void setCapPk(Long capPk) {
        this.capPk = capPk;
    }

    public String getCapNombreCapacitacion() {
        return capNombreCapacitacion;
    }

    public void setCapNombreCapacitacion(String capNombreCapacitacion) {
        this.capNombreCapacitacion = capNombreCapacitacion;
    }

    public SgEstudioRealizado getCapEstudios() {
        return capEstudios;
    }

    public void setCapEstudios(SgEstudioRealizado capEstudios) {
        this.capEstudios = capEstudios;
    }

    public SgTipoCapacitacion getCapTipoCapacitacion() {
        return capTipoCapacitacion;
    }

    public void setCapTipoCapacitacion(SgTipoCapacitacion capTipoCapacitacion) {
        this.capTipoCapacitacion = capTipoCapacitacion;
    }

    public SgAlcanceCapacitacion getCapAlcanceCapacitacion() {
        return capAlcanceCapacitacion;
    }

    public void setCapAlcanceCapacitacion(SgAlcanceCapacitacion capAlcanceCapacitacion) {
        this.capAlcanceCapacitacion = capAlcanceCapacitacion;
    }

    public String getCapInstitucionEstudio() {
        return capInstitucionEstudio;
    }

    public void setCapInstitucionEstudio(String capInstitucionEstudio) {
        this.capInstitucionEstudio = capInstitucionEstudio;
    }

    public LocalDate getCapDesde() {
        return capDesde;
    }

    public void setCapDesde(LocalDate capDesde) {
        this.capDesde = capDesde;
    }

    public LocalDate getCapHasta() {
        return capHasta;
    }

    public void setCapHasta(LocalDate capHasta) {
        this.capHasta = capHasta;
    }

    public Integer getCapDuracionHoras() {
        return capDuracionHoras;
    }

    public void setCapDuracionHoras(Integer capDuracionHoras) {
        this.capDuracionHoras = capDuracionHoras;
    }

    public SgModalidadEstudio getCapModalidad() {
        return capModalidad;
    }

    public void setCapModalidad(SgModalidadEstudio capModalidad) {
        this.capModalidad = capModalidad;
    }

    public Boolean getCapCursoAcreditado() {
        return capCursoAcreditado;
    }

    public void setCapCursoAcreditado(Boolean capCursoAcreditado) {
        this.capCursoAcreditado = capCursoAcreditado;
    }

    public LocalDateTime getCapUltModFecha() {
        return capUltModFecha;
    }

    public void setCapUltModFecha(LocalDateTime capUltModFecha) {
        this.capUltModFecha = capUltModFecha;
    }

    public String getCapUltModUsuario() {
        return capUltModUsuario;
    }

    public void setCapUltModUsuario(String capUltModUsuario) {
        this.capUltModUsuario = capUltModUsuario;
    }

    public Integer getCapVersion() {
        return capVersion;
    }

    public void setCapVersion(Integer capVersion) {
        this.capVersion = capVersion;
    }

    public Boolean getCapPkForView() {
        return capPkForView;
    }

    public void setCapPkForView(Boolean capPkForView) {
        this.capPkForView = capPkForView;
    }

    public Boolean getCapValidado() {
        return capValidado;
    }

    public void setCapValidado(Boolean capValidado) {
        this.capValidado = capValidado;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (capPk != null ? capPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCapacitacion)) {
            return false;
        }
        SgCapacitacion other = (SgCapacitacion) object;
        if ((this.capPk == null && other.capPk != null) || (this.capPk != null && !this.capPk.equals(other.capPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCapacitacion[ capPk=" + capPk + " ]";
    }

    @Override
    public Long getId() {
        return this.capPk;
    }

    @Override
    public void setId(Long id) {
        this.capPk = id;
    }

    @Override
    public Boolean getIdForView() {
        return this.capPkForView;
    }

    @Override
    public void setIdForView(Boolean valor) {
        this.capPkForView = valor;
    }

}
