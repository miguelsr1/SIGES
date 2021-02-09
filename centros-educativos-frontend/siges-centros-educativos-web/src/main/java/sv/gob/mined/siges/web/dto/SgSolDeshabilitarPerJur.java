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
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudOAE;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dpjPk", scope = SgSolDeshabilitarPerJur.class)
public class SgSolDeshabilitarPerJur implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long dpjPk;
    
    private String dpjMotivo;
    
    private EnumEstadoSolicitudOAE dpjEstado;
    
    private SgArchivo dpjActa;
    
    private String dpjNumeroAcuerdo;
    
    private LocalDate dpjFechaAcuerdo;
    
    private SgArchivo dpjAcuerdo;
    
    private SgOrganismoAdministracionEscolar dpjOaeFk;
    
    private LocalDateTime dpjUltModFecha;

    private String dpjUltModUsuario;

    private Integer dpjVersion;
    
    private Boolean dpjAprobar;

    public Long getDpjPk() {
        return dpjPk;
    }

    public void setDpjPk(Long dpjPk) {
        this.dpjPk = dpjPk;
    }

    public String getDpjMotivo() {
        return dpjMotivo;
    }

    public void setDpjMotivo(String dpjMotivo) {
        this.dpjMotivo = dpjMotivo;
    }

    public EnumEstadoSolicitudOAE getDpjEstado() {
        return dpjEstado;
    }

    public void setDpjEstado(EnumEstadoSolicitudOAE dpjEstado) {
        this.dpjEstado = dpjEstado;
    }

    public SgArchivo getDpjActa() {
        return dpjActa;
    }

    public void setDpjActa(SgArchivo dpjActa) {
        this.dpjActa = dpjActa;
    }

    public String getDpjNumeroAcuerdo() {
        return dpjNumeroAcuerdo;
    }

    public void setDpjNumeroAcuerdo(String dpjNumeroAcuerdo) {
        this.dpjNumeroAcuerdo = dpjNumeroAcuerdo;
    }

    public LocalDate getDpjFechaAcuerdo() {
        return dpjFechaAcuerdo;
    }

    public void setDpjFechaAcuerdo(LocalDate dpjFechaAcuerdo) {
        this.dpjFechaAcuerdo = dpjFechaAcuerdo;
    }

    public SgArchivo getDpjAcuerdo() {
        return dpjAcuerdo;
    }

    public void setDpjAcuerdo(SgArchivo dpjAcuerdo) {
        this.dpjAcuerdo = dpjAcuerdo;
    }

    public SgOrganismoAdministracionEscolar getDpjOaeFk() {
        return dpjOaeFk;
    }

    public void setDpjOaeFk(SgOrganismoAdministracionEscolar dpjOaeFk) {
        this.dpjOaeFk = dpjOaeFk;
    }

    public LocalDateTime getDpjUltModFecha() {
        return dpjUltModFecha;
    }

    public void setDpjUltModFecha(LocalDateTime dpjUltModFecha) {
        this.dpjUltModFecha = dpjUltModFecha;
    }

    public String getDpjUltModUsuario() {
        return dpjUltModUsuario;
    }

    public void setDpjUltModUsuario(String dpjUltModUsuario) {
        this.dpjUltModUsuario = dpjUltModUsuario;
    }

    public Integer getDpjVersion() {
        return dpjVersion;
    }

    public void setDpjVersion(Integer dpjVersion) {
        this.dpjVersion = dpjVersion;
    }

    public Boolean getDpjAprobar() {
        return dpjAprobar;
    }

    public void setDpjAprobar(Boolean dpjAprobar) {
        this.dpjAprobar = dpjAprobar;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.dpjPk);
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
        final SgSolDeshabilitarPerJur other = (SgSolDeshabilitarPerJur) obj;
        if (!Objects.equals(this.dpjPk, other.dpjPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgSolDeshabilitarPerJur{" + "dpjPk=" + dpjPk + ", dpjMotivo=" + dpjMotivo + ", dpjArchivo=" + dpjActa + ", dpjUltModFecha=" + dpjUltModFecha + ", dpjUltModUsuario=" + dpjUltModUsuario + ", dpjVersion=" + dpjVersion + '}';
    }
    
}
