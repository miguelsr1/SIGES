/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_personas", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@Audited
public class SgPersona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "per_pk")
    private Long perPk;

    @Size(max = 100)
    @Column(name = "per_primer_nombre")
    @AtributoNormalizable
    private String perPrimerNombre;

    @Size(max = 100)
    @Column(name = "per_segundo_nombre")
    @AtributoNormalizable
    private String perSegundoNombre;

    @Size(max = 100)
    @Column(name = "per_tercer_nombre")
    @AtributoNormalizable
    private String perTercerNombre;

    @Size(max = 100)
    @Column(name = "per_primer_apellido")
    @AtributoNormalizable
    private String perPrimerApellido;

    @Size(max = 100)
    @Column(name = "per_segundo_apellido")
    @AtributoNormalizable
    private String perSegundoApellido;

    @Size(max = 100)
    @Column(name = "per_tercer_apellido")
    @AtributoNormalizable
    private String perTercerApellido;

    @Size(max = 100)
    @Column(name = "per_primer_nombre_busqueda")
    private String perPrimerNombreBusqueda;

    @Size(max = 100)
    @Column(name = "per_segundo_nombre_busqueda")
    private String perSegundoNombreBusqueda;

    @Size(max = 100)
    @Column(name = "per_tercer_nombre_busqueda")
    private String perTercerNombreBusqueda;

    @Size(max = 100)
    @Column(name = "per_primer_apellido_busqueda")
    private String perPrimerApellidoBusqueda;

    @Size(max = 100)
    @Column(name = "per_segundo_apellido_busqueda")
    private String perSegundoApellidoBusqueda;

    @Size(max = 100)
    @Column(name = "per_tercer_apellido_busqueda")
    private String perTercerApellidoBusqueda;

    @Size(max = 650)
    @Column(name = "per_nombre_busqueda", length = 650)
    private String perNombreBusqueda;

    @Column(name = "per_habilitado")
    @AtributoHabilitado
    private Boolean perHabilitado;

    @Column(name = "per_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime perUltModFecha;

    @Size(max = 45)
    @Column(name = "per_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String perUltModUsuario;

    @Column(name = "per_dui", length = 20)
    @Size(max = 20)
    private String perDui;

    @JoinColumn(name = "per_ocupacion_fk")
    @ManyToOne
    private SgOcupacion perOcupacion;
    
    @Column(name = "per_version")
    @Version
    private Integer perVersion;

    

    public SgPersona() {
    }

    public SgPersona(Long perPk) {
        this.perPk = perPk;
    }

    public SgPersona(Long perPk, Integer perVersion) {
        this.perPk = perPk;
        this.perVersion = perVersion;
    }

    @PrePersist
    @PreUpdate
    public void preSave() throws Exception {
        throw new Exception("Esta clase no debe ser guardada por si sola. Debe utilizarse para asociaciones EntidadPadre - SgPersona.");
    }

    @JsonIgnore
    public String getPerNombreCompleto() {
        StringBuilder s = new StringBuilder();
        if (this.perPrimerNombre != null) {
            s.append(this.perPrimerNombre).append(" ");
        }
        if (this.perSegundoNombre != null) {
            s.append(this.perSegundoNombre).append(" ");
        }
        if (this.perTercerNombre != null) {
            s.append(this.perTercerNombre).append(" ");
        }
        if (this.perPrimerApellido != null) {
            s.append(this.perPrimerApellido).append(" ");
        }
        if (this.perSegundoApellido != null) {
            s.append(this.perSegundoApellido).append(" ");
        }
        if (this.perTercerApellido != null) {
            s.append(this.perTercerApellido).append(" ");
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    public Long getPerPk() {
        return perPk;
    }

    public void setPerPk(Long perPk) {
        this.perPk = perPk;
    }

    public String getPerPrimerNombre() {
        return perPrimerNombre;
    }

    public void setPerPrimerNombre(String perPrimerNombre) {
        this.perPrimerNombre = perPrimerNombre;
    }

    public String getPerSegundoNombre() {
        return perSegundoNombre;
    }

    public void setPerSegundoNombre(String perSegundoNombre) {
        this.perSegundoNombre = perSegundoNombre;
    }

    public String getPerTercerNombre() {
        return perTercerNombre;
    }

    public void setPerTercerNombre(String perTercerNombre) {
        this.perTercerNombre = perTercerNombre;
    }

    public String getPerPrimerApellido() {
        return perPrimerApellido;
    }

    public void setPerPrimerApellido(String perPrimerApellido) {
        this.perPrimerApellido = perPrimerApellido;
    }

    public String getPerSegundoApellido() {
        return perSegundoApellido;
    }

    public void setPerSegundoApellido(String perSegundoApellido) {
        this.perSegundoApellido = perSegundoApellido;
    }

    public String getPerTercerApellido() {
        return perTercerApellido;
    }

    public void setPerTercerApellido(String perTercerApellido) {
        this.perTercerApellido = perTercerApellido;
    }

    public Boolean getPerHabilitado() {
        return perHabilitado;
    }

    public void setPerHabilitado(Boolean perHabilitado) {
        this.perHabilitado = perHabilitado;
    }

    public LocalDateTime getPerUltModFecha() {
        return perUltModFecha;
    }

    public void setPerUltModFecha(LocalDateTime perUltModFecha) {
        this.perUltModFecha = perUltModFecha;
    }

    public String getPerUltModUsuario() {
        return perUltModUsuario;
    }

    public void setPerUltModUsuario(String perUltModUsuario) {
        this.perUltModUsuario = perUltModUsuario;
    }

    public Integer getPerVersion() {
        return perVersion;
    }

    public void setPerVersion(Integer perVersion) {
        this.perVersion = perVersion;
    }
    
    public String getPerPrimerNombreBusqueda() {
        return perPrimerNombreBusqueda;
    }

    public void setPerPrimerNombreBusqueda(String perPrimerNombreBusqueda) {
        this.perPrimerNombreBusqueda = perPrimerNombreBusqueda;
    }

    public String getPerSegundoNombreBusqueda() {
        return perSegundoNombreBusqueda;
    }

    public void setPerSegundoNombreBusqueda(String perSegundoNombreBusqueda) {
        this.perSegundoNombreBusqueda = perSegundoNombreBusqueda;
    }

    public String getPerTercerNombreBusqueda() {
        return perTercerNombreBusqueda;
    }

    public void setPerTercerNombreBusqueda(String perTercerNombreBusqueda) {
        this.perTercerNombreBusqueda = perTercerNombreBusqueda;
    }

    public String getPerPrimerApellidoBusqueda() {
        return perPrimerApellidoBusqueda;
    }

    public void setPerPrimerApellidoBusqueda(String perPrimerApellidoBusqueda) {
        this.perPrimerApellidoBusqueda = perPrimerApellidoBusqueda;
    }

    public String getPerSegundoApellidoBusqueda() {
        return perSegundoApellidoBusqueda;
    }

    public void setPerSegundoApellidoBusqueda(String perSegundoApellidoBusqueda) {
        this.perSegundoApellidoBusqueda = perSegundoApellidoBusqueda;
    }

    public String getPerTercerApellidoBusqueda() {
        return perTercerApellidoBusqueda;
    }

    public void setPerTercerApellidoBusqueda(String perTercerApellidoBusqueda) {
        this.perTercerApellidoBusqueda = perTercerApellidoBusqueda;
    }

    public String getPerNombreBusqueda() {
        return perNombreBusqueda;
    }

    public void setPerNombreBusqueda(String perNombreBusqueda) {
        this.perNombreBusqueda = perNombreBusqueda;
    }

    public String getPerDui() {
        return perDui;
    }

    public void setPerDui(String perDui) {
        this.perDui = perDui;
    }

    public SgOcupacion getPerOcupacion() {
        return perOcupacion;
    }

    public void setPerOcupacion(SgOcupacion perOcupacion) {
        this.perOcupacion = perOcupacion;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (perPk != null ? perPk.hashCode() : 0);
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
        final SgPersona other = (SgPersona) obj;
        if (!Objects.equals(this.perPk, other.perPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgPersonaLite{" + "perPk=" + perPk + '}';
    }
    
    
    

}
