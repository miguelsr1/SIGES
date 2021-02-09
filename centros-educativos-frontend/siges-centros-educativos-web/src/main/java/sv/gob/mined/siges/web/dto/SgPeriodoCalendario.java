/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.dto.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.web.dto.catalogo.SgSubModalidadAtencion;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cesPk", scope = SgPeriodoCalendario.class)
public class SgPeriodoCalendario implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cesPk;

    private String cesNombre;

    private LocalDate cesFechaDesde;

    private LocalDate cesFechaHasta;
    
    private Boolean cesHabilitado; 

    private EnumCalendarioEscolar cesTipo;

    private LocalDateTime cesUltModFecha;

    private String cesUltModUsuario;

    private Integer cesVersion;

    private SgNivel cesNivel;

    private SgModalidadAtencion cesModalidadAtencion;
    
    private SgSubModalidadAtencion cesSubModalidadAtencion;

    private SgCalendario cesCalendario;

    public SgPeriodoCalendario() {
        cesHabilitado=Boolean.TRUE;

    }

    public Long getCesPk() {
        return cesPk;
    }

    public void setCesPk(Long cesPk) {
        this.cesPk = cesPk;
    }

    public LocalDate getCesFechaDesde() {
        return cesFechaDesde;
    }

    public void setCesFechaDesde(LocalDate cesFechaDesde) {
        this.cesFechaDesde = cesFechaDesde;
    }

    public LocalDate getCesFechaHasta() {
        return cesFechaHasta;
    }

    public void setCesFechaHasta(LocalDate cesFechaHasta) {
        this.cesFechaHasta = cesFechaHasta;
    }

    public EnumCalendarioEscolar getCesTipo() {
        return cesTipo;
    }

    public void setCesTipo(EnumCalendarioEscolar cesTipo) {
        this.cesTipo = cesTipo;
    }

    public SgNivel getCesNivel() {
        return cesNivel;
    }

    public void setCesNivel(SgNivel cesNivel) {
        this.cesNivel = cesNivel;
    }

    public SgModalidadAtencion getCesModalidadAtencion() {
        return cesModalidadAtencion;
    }

    public void setCesModalidadAtencion(SgModalidadAtencion cesModalidadAtencion) {
        this.cesModalidadAtencion = cesModalidadAtencion;
    }

    public LocalDateTime getCesUltModFecha() {
        return cesUltModFecha;
    }

    public void setCesUltModFecha(LocalDateTime cesUltModFecha) {
        this.cesUltModFecha = cesUltModFecha;
    }

    public String getCesUltModUsuario() {
        return cesUltModUsuario;
    }

    public void setCesUltModUsuario(String cesUltModUsuario) {
        this.cesUltModUsuario = cesUltModUsuario;
    }

    public Integer getCesVersion() {
        return cesVersion;
    }

    public void setCesVersion(Integer cesVersion) {
        this.cesVersion = cesVersion;
    }

    public SgCalendario getCesCalendario() {
        return cesCalendario;
    }

    public void setCesCalendario(SgCalendario cesCalendario) {
        this.cesCalendario = cesCalendario;
    }

    public String getCesNombre() {
        return cesNombre;
    }

    public void setCesNombre(String cesNombre) {
        this.cesNombre = cesNombre;
    }

    public Boolean getCesHabilitado() {
        return cesHabilitado;
    }

    public void setCesHabilitado(Boolean cesHabilitado) {
        this.cesHabilitado = cesHabilitado;
    }

    public SgSubModalidadAtencion getCesSubModalidadAtencion() {
        return cesSubModalidadAtencion;
    }

    public void setCesSubModalidadAtencion(SgSubModalidadAtencion cesSubModalidadAtencion) {
        this.cesSubModalidadAtencion = cesSubModalidadAtencion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cesPk != null ? cesPk.hashCode() : 0);
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
        final SgPeriodoCalendario other = (SgPeriodoCalendario) obj;
        if (!Objects.equals(this.cesPk, other.cesPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgCalendarioEscolar[ cesPk=" + cesPk + " ]";
    }

}
