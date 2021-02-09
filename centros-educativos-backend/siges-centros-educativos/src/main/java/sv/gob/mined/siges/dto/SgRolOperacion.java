/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ropPk", scope = SgRolOperacion.class)
public class SgRolOperacion implements Serializable {

    private Long ropPk;

    private Boolean ropCascada;

    private Boolean ropHabilitado;

    private LocalDateTime ropUltModFecha;

    private String ropUltModUsuario;

    private Integer ropVersion;

    private SgOperacion ropOperacion;

    private SgRol ropRol;

    private Boolean ropPkForView;

    public SgRolOperacion() {
    }

    public Long getRopPk() {
        return ropPk;
    }

    public void setRopPk(Long ropPk) {
        this.ropPk = ropPk;
    }

    public Boolean getRopCascada() {
        return ropCascada;
    }

    public void setRopCascada(Boolean ropCascada) {
        this.ropCascada = ropCascada;
    }

    public Boolean getRopHabilitado() {
        return ropHabilitado;
    }

    public void setRopHabilitado(Boolean ropHabilitado) {
        this.ropHabilitado = ropHabilitado;
    }

    public LocalDateTime getRopUltModFecha() {
        return ropUltModFecha;
    }

    public void setRopUltModFecha(LocalDateTime ropUltModFecha) {
        this.ropUltModFecha = ropUltModFecha;
    }

    public String getRopUltModUsuario() {
        return ropUltModUsuario;
    }

    public void setRopUltModUsuario(String ropUltModUsuario) {
        this.ropUltModUsuario = ropUltModUsuario;
    }

    public Integer getRopVersion() {
        return ropVersion;
    }

    public void setRopVersion(Integer ropVersion) {
        this.ropVersion = ropVersion;
    }

    public SgOperacion getRopOperacion() {
        return ropOperacion;
    }

    public void setRopOperacion(SgOperacion ropOperacion) {
        this.ropOperacion = ropOperacion;
    }

    public SgRol getRopRol() {
        return ropRol;
    }

    public void setRopRol(SgRol ropRol) {
        this.ropRol = ropRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ropPk != null ? ropPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof SgRolOperacion)) {
            return false;
        }
        SgRolOperacion other = (SgRolOperacion) object;

        if (this == other) {
            return true;
        }
        //si la clave primaria son iguales
        if ((this.ropPk != null && other.ropPk != null && this.ropPk.equals(other.ropPk))) {
            return true;
        }
        
        return false;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRolOperacion[ ropPk=" + ropPk + " ]";
    }

}
