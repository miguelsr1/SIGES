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
import sv.gob.mined.siges.web.dto.catalogo.SgCarrera;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadEstudio;
import sv.gob.mined.siges.web.dto.catalogo.SgEspecialidad;
import sv.gob.mined.siges.web.dto.catalogo.SgPais;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoEstudio;
import sv.gob.mined.siges.web.utilidades.ViewDto;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "esuPk", scope = SgEstudioSuperior.class)
public class SgEstudioSuperior implements Serializable, ViewDto {

    private static final long serialVersionUID = 1L;

    private Long esuPk;

    private SgEstudioRealizado esuEstudios;

    private SgTipoEstudio esuTipo;

    private SgPais esuPais;

    private SgCarrera esuCarrera;

    private String esuCarreraTxt;

    private SgEspecialidad esuEspecialidad;

    private SgSede esuInstitucionEstudio;

    private String esuInstitucionEstudioTxt;

    private LocalDate esuDesde;

    private LocalDate esuHasta;

    private SgModalidadEstudio esuModalidad;

    private Integer esuAnioTitulacion;
    
    private Boolean esuValidado;

    private LocalDateTime esuUltModFecha;

    private String esuUltModUsuario;

    private Integer esuVersion;

    private Boolean esuPkForView;

    public SgEstudioSuperior() {
        this.esuValidado = Boolean.FALSE;
    }

    public SgEstudioSuperior(Long esuPk) {
        this.esuPk = esuPk;
    }

    public Long getEsuPk() {
        return esuPk;
    }

    public void setEsuPk(Long esuPk) {
        this.esuPk = esuPk;
    }

    public SgEstudioRealizado getEsuEstudios() {
        return esuEstudios;
    }

    public void setEsuEstudios(SgEstudioRealizado esuEstudios) {
        this.esuEstudios = esuEstudios;
    }

    public SgTipoEstudio getEsuTipo() {
        return esuTipo;
    }

    public void setEsuTipo(SgTipoEstudio esuTipo) {
        this.esuTipo = esuTipo;
    }

    public SgPais getEsuPais() {
        return esuPais;
    }

    public void setEsuPais(SgPais esuPais) {
        this.esuPais = esuPais;
    }

    public SgCarrera getEsuCarrera() {
        return esuCarrera;
    }

    public void setEsuCarrera(SgCarrera esuCarrera) {
        this.esuCarrera = esuCarrera;
    }

    public SgEspecialidad getEsuEspecialidad() {
        return esuEspecialidad;
    }

    public void setEsuEspecialidad(SgEspecialidad esuEspecialidad) {
        this.esuEspecialidad = esuEspecialidad;
    }

    public SgSede getEsuInstitucionEstudio() {
        return esuInstitucionEstudio;
    }

    public void setEsuInstitucionEstudio(SgSede esuInstitucionEstudio) {
        this.esuInstitucionEstudio = esuInstitucionEstudio;
    }

    public LocalDate getEsuDesde() {
        return esuDesde;
    }

    public void setEsuDesde(LocalDate esuDesde) {
        this.esuDesde = esuDesde;
    }

    public LocalDate getEsuHasta() {
        return esuHasta;
    }

    public void setEsuHasta(LocalDate esuHasta) {
        this.esuHasta = esuHasta;
    }

    public SgModalidadEstudio getEsuModalidad() {
        return esuModalidad;
    }

    public void setEsuModalidad(SgModalidadEstudio esuModalidad) {
        this.esuModalidad = esuModalidad;
    }

    public Integer getEsuAnioTitulacion() {
        return esuAnioTitulacion;
    }

    public void setEsuAnioTitulacion(Integer esuAnioTitulacion) {
        this.esuAnioTitulacion = esuAnioTitulacion;
    }

    public LocalDateTime getEsuUltModFecha() {
        return esuUltModFecha;
    }

    public void setEsuUltModFecha(LocalDateTime esuUltModFecha) {
        this.esuUltModFecha = esuUltModFecha;
    }

    public String getEsuUltModUsuario() {
        return esuUltModUsuario;
    }

    public void setEsuUltModUsuario(String esuUltModUsuario) {
        this.esuUltModUsuario = esuUltModUsuario;
    }

    public Integer getEsuVersion() {
        return esuVersion;
    }

    public void setEsuVersion(Integer esuVersion) {
        this.esuVersion = esuVersion;
    }

    public String getEsuCarreraTxt() {
        return esuCarreraTxt;
    }

    public void setEsuCarreraTxt(String esuCarreraTxt) {
        this.esuCarreraTxt = esuCarreraTxt;
    }

    public Boolean getEsuPkForView() {
        return esuPkForView;
    }

    public void setEsuPkForView(Boolean esuPkForView) {
        this.esuPkForView = esuPkForView;
    }

    public String getEsuInstitucionEstudioTxt() {
        return esuInstitucionEstudioTxt;
    }

    public void setEsuInstitucionEstudioTxt(String esuInstitucionEstudioTxt) {
        this.esuInstitucionEstudioTxt = esuInstitucionEstudioTxt;
    }

    public Boolean getEsuValidado() {
        return esuValidado;
    }

    public void setEsuValidado(Boolean esuValidado) {
        this.esuValidado = esuValidado;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (esuPk != null ? esuPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgEstudioSuperior)) {
            return false;
        }
        SgEstudioSuperior other = (SgEstudioSuperior) object;
        if ((this.esuPk == null && other.esuPk != null) || (this.esuPk != null && !this.esuPk.equals(other.esuPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstudioSuperior[ esuPk=" + esuPk + " ]";
    }

    @Override
    public Long getId() {
        return this.esuPk;
    }

    @Override
    public void setId(Long id) {
        this.esuPk = id;
    }

    @Override
    public Boolean getIdForView() {
        return this.esuPkForView;
    }

    @Override
    public void setIdForView(Boolean valor) {
        this.esuPkForView = valor;
    }

}
