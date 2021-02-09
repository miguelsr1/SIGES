/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "modPk", scope = SgModalidad.class)
public class SgModalidad implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long modPk;

    private String modCodigo;

    private String modNombre;

    private Boolean modHabilitado;

    private Boolean modAplicaDiplomado;
    
    private Integer modOrden;

    private Boolean modAdmiteOpcion;

    private SgCiclo modCiclo;

    private LocalDateTime modUltModFecha;

    private String modUltModUsuario;

    private Integer modVersion;

    private List<SgRelModEdModAten> modRelModalidadAtencion;

    private List<SgOpcion> modOpciones;

    public SgModalidad() {
        this.modHabilitado = Boolean.TRUE;
        this.modAdmiteOpcion = Boolean.FALSE;
    }

    public Long getModPk() {
        return modPk;
    }

    public void setModPk(Long modPk) {
        this.modPk = modPk;
    }

    public List<SgRelModEdModAten> getModRelModalidadAtencion() {
        return modRelModalidadAtencion;
    }

    public void setModRelModalidadAtencion(List<SgRelModEdModAten> modRelModalidadAtencion) {
        this.modRelModalidadAtencion = modRelModalidadAtencion;
    }

    public String getModCodigo() {
        return modCodigo;
    }

    public void setModCodigo(String modCodigo) {
        this.modCodigo = modCodigo;
    }

    public Boolean getModAdmiteOpcion() {
        return modAdmiteOpcion;
    }

    public void setModAdmiteOpcion(Boolean modAdmiteOpcion) {
        this.modAdmiteOpcion = modAdmiteOpcion;
    }

    public String getModNombre() {
        return modNombre;
    }

    public void setModNombre(String modNombre) {
        this.modNombre = modNombre;
    }

    public LocalDateTime getModUltModFecha() {
        return modUltModFecha;
    }

    public void setModUltModFecha(LocalDateTime modUltModFecha) {
        this.modUltModFecha = modUltModFecha;
    }

    public String getModUltModUsuario() {
        return modUltModUsuario;
    }

    public void setModUltModUsuario(String modUltModUsuario) {
        this.modUltModUsuario = modUltModUsuario;
    }

    public Integer getModVersion() {
        return modVersion;
    }

    public void setModVersion(Integer modVersion) {
        this.modVersion = modVersion;
    }

    public Boolean getModHabilitado() {
        return modHabilitado;
    }

    public void setModHabilitado(Boolean modHabilitado) {
        this.modHabilitado = modHabilitado;
    }

    public SgCiclo getModCiclo() {
        return modCiclo;
    }

    public void setModCiclo(SgCiclo modCiclo) {
        this.modCiclo = modCiclo;
    }

    public Integer getModOrden() {
        return modOrden;
    }

    public void setModOrden(Integer modOrden) {
        this.modOrden = modOrden;
    }

    public List<SgOpcion> getModOpciones() {
        return modOpciones;
    }

    public void setModOpciones(List<SgOpcion> modOpciones) {
        this.modOpciones = modOpciones;
    }

    public Boolean getModAplicaDiplomado() {
        return modAplicaDiplomado;
    }

    public void setModAplicaDiplomado(Boolean modAplicaDiplomado) {
        this.modAplicaDiplomado = modAplicaDiplomado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modPk != null ? modPk.hashCode() : 0);
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
        final SgModalidad other = (SgModalidad) obj;
        if (!Objects.equals(this.modPk, other.modPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgModalidad[ modPk=" + modPk + " ]";
    }

}
