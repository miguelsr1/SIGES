/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.entities.constantes.Constantes;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Cache;


@Entity
@Table(name = "sg_clasificaciones", schema = Constantes.SCHEMA_CATALOGO)
@Cache(expiry = 150000)
public class SgClasificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cla_pk", nullable = false)
    private Long claPk;

    @Size(max = 4)
    @Column(name = "cla_codigo", length = 4)
    private String claCodigo;

    @Size(max = 255)
    @Column(name = "cla_nombre", length = 255)
    private String claNombre;

    @Size(max = 255)
    @Column(name = "cla_nombre_busqueda", length = 255)
    private String claNombreBusqueda;

    @Column(name = "cla_habilitado")
    private Boolean claHabilitado;

    @Column(name = "cla_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date claUltModFecha;

    @Size(max = 45)
    @Column(name = "cla_ult_mod_usuario", length = 45)
    private String claUltModUsuario;

    @Column(name = "cla_version")
    @Version
    private Integer claVersion;

    public SgClasificacion() {
    }


    public Long getClaPk() {
        return claPk;
    }

    public void setClaPk(Long claPk) {
        this.claPk = claPk;
    }

    public String getClaCodigo() {
        return claCodigo;
    }

    public void setClaCodigo(String claCodigo) {
        this.claCodigo = claCodigo;
    }

    public String getClaNombre() {
        return claNombre;
    }

    public void setClaNombre(String claNombre) {
        this.claNombre = claNombre;
    }

    public String getClaNombreBusqueda() {
        return claNombreBusqueda;
    }

    public void setClaNombreBusqueda(String claNombreBusqueda) {
        this.claNombreBusqueda = claNombreBusqueda;
    }

    public Boolean getClaHabilitado() {
        return claHabilitado;
    }

    public void setClaHabilitado(Boolean claHabilitado) {
        this.claHabilitado = claHabilitado;
    }

    public Date getClaUltModFecha() {
        return claUltModFecha;
    }

    public void setClaUltModFecha(Date claUltModFecha) {
        this.claUltModFecha = claUltModFecha;
    }

    public String getClaUltModUsuario() {
        return claUltModUsuario;
    }

    public void setClaUltModUsuario(String claUltModUsuario) {
        this.claUltModUsuario = claUltModUsuario;
    }

    public Integer getClaVersion() {
        return claVersion;
    }

    public void setClaVersion(Integer claVersion) {
        this.claVersion = claVersion;
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.claPk);
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
        final SgClasificacion other = (SgClasificacion) obj;
        if (!Objects.equals(this.claPk, other.claPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgClasificacion{" + "claPk=" + claPk + '}';
    }

    public SgClasificacion(Long claPk) {
        this.claPk = claPk;
    }
}
