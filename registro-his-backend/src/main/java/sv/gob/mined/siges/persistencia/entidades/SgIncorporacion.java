/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEstadoCivil;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgNacionalidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgPais;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSexo;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_incorporaciones", schema = Constantes.SCHEMA_REGISTRO_HISTORICO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "incPk", scope = SgIncorporacion.class)
@Audited
public class SgIncorporacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "inc_pk", nullable = false)
    private Long incPk;
    
    @Column(name = "inc_dui")
    private String incDui;
    
    @Column(name = "inc_carne_residente")
    private String incCarneResidente;
    
    @Column(name = "inc_pasaporte")
    private String incPasaporte;
    
    @JoinColumn(name = "inc_pasaporte_pais_emisor")
    @ManyToOne
    private SgPais incPasaportePaisEmisor;

    @Size(max = 100)
    @Column(name = "inc_primer_nombre")
    @AtributoNormalizable
    private String incPrimerNombre;

    @Size(max = 100)
    @Column(name = "inc_segundo_nombre")
    @AtributoNormalizable
    private String incSegundoNombre;

    @Size(max = 100)
    @Column(name = "inc_tercer_nombre")
    @AtributoNormalizable
    private String incTercerNombre;

    @Size(max = 100)
    @Column(name = "inc_primer_apellido")
    @AtributoNormalizable
    private String incPrimerApellido;

    @Size(max = 100)
    @Column(name = "inc_segundo_apellido")
    @AtributoNormalizable
    private String incSegundoApellido;

    @Size(max = 100)
    @Column(name = "inc_tercer_apellido")
    @AtributoNormalizable
    private String incTercerApellido;

    @Size(max = 650)
    @Column(name = "inc_nombre_busqueda")
    private String incNombreBusqueda;
    
    @Column(name = "inc_fecha_nacimiento")
    private LocalDate incFechaNacimiento;

    @JoinColumn(name = "inc_sexo_fk")
    @ManyToOne
    private SgSexo incSexo;

    @JoinColumn(name = "inc_estado_civil_fk")
    @ManyToOne
    private SgEstadoCivil incEstadoCivil;
    
    @JoinColumn(name = "inc_nacionalidad_fk")
    @ManyToOne
    private SgNacionalidad incNacionalidad;
    
    @Column(name = "inc_nombre_sede")
    private String incNombreSede;

    @Column(name = "inc_ult_grado_estudio")
    private String incUltimoGradoEstudio;
    
    @Column(name = "inc_anio_estudio")
    private Integer incAnioEstudio;
    
    @Column(name = "inc_ciudad")
    private String incCiudad;
    
    @Column(name = "inc_numero_tramite")
    private String incNumeroTramite;
    
    @Column(name = "inc_numero_resolucion")
    private String incNumeroResolucion;
    
    @Column(name = "inc_fecha_aprobacion")
    private LocalDate incFechaAprobacion;

    @Column(name = "inc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime incUltModFecha;

    @Size(max = 45)
    @Column(name = "inc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String incUltModUsuario;

    @Column(name = "inc_version")
    @Version
    private Integer incVersion;

    public SgIncorporacion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.incNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.getIncNombreCompleto());

    }

    @JsonIgnore
    public String getIncNombreCompleto() {
        StringBuilder s = new StringBuilder();
        if (this.incPrimerNombre != null) {
            s.append(this.incPrimerNombre).append(" ");
        }
        if (this.incSegundoNombre != null) {
            s.append(this.incSegundoNombre).append(" ");
        }
        if (this.incTercerNombre != null) {
            s.append(this.incTercerNombre).append(" ");
        }
        if (this.incPrimerApellido != null) {
            s.append(this.incPrimerApellido).append(" ");
        }
        if (this.incSegundoApellido != null) {
            s.append(this.incSegundoApellido).append(" ");
        }
        if (this.incTercerApellido != null) {
            s.append(this.incTercerApellido).append(" ");
        }
        return SofisStringUtils.normalizarString(s.toString());
    }

    public Long getIncPk() {
        return incPk;
    }

    public void setIncPk(Long incPk) {
        this.incPk = incPk;
    }


    public String getIncNombreBusqueda() {
        return incNombreBusqueda;
    }

    public void setIncNombreBusqueda(String incNombreBusqueda) {
        this.incNombreBusqueda = incNombreBusqueda;
    }


    public LocalDateTime getIncUltModFecha() {
        return incUltModFecha;
    }

    public void setIncUltModFecha(LocalDateTime incUltModFecha) {
        this.incUltModFecha = incUltModFecha;
    }

    public String getIncUltModUsuario() {
        return incUltModUsuario;
    }

    public void setIncUltModUsuario(String incUltModUsuario) {
        this.incUltModUsuario = incUltModUsuario;
    }

    public Integer getIncVersion() {
        return incVersion;
    }

    public void setIncVersion(Integer incVersion) {
        this.incVersion = incVersion;
    }

    public String getIncDui() {
        return incDui;
    }

    public void setIncDui(String incDui) {
        this.incDui = incDui;
    }

    public String getIncCarneResidente() {
        return incCarneResidente;
    }

    public void setIncCarneResidente(String incCarneResidente) {
        this.incCarneResidente = incCarneResidente;
    }

    public String getIncPasaporte() {
        return incPasaporte;
    }

    public void setIncPasaporte(String incPasaporte) {
        this.incPasaporte = incPasaporte;
    }

    public SgPais getIncPasaportePaisEmisor() {
        return incPasaportePaisEmisor;
    }

    public void setIncPasaportePaisEmisor(SgPais incPasaportePaisEmisor) {
        this.incPasaportePaisEmisor = incPasaportePaisEmisor;
    }

    public String getIncPrimerNombre() {
        return incPrimerNombre;
    }

    public void setIncPrimerNombre(String incPrimerNombre) {
        this.incPrimerNombre = incPrimerNombre;
    }

    public String getIncSegundoNombre() {
        return incSegundoNombre;
    }

    public void setIncSegundoNombre(String incSegundoNombre) {
        this.incSegundoNombre = incSegundoNombre;
    }

    public String getIncTercerNombre() {
        return incTercerNombre;
    }

    public void setIncTercerNombre(String incTercerNombre) {
        this.incTercerNombre = incTercerNombre;
    }

    public String getIncPrimerApellido() {
        return incPrimerApellido;
    }

    public void setIncPrimerApellido(String incPrimerApellido) {
        this.incPrimerApellido = incPrimerApellido;
    }

    public String getIncSegundoApellido() {
        return incSegundoApellido;
    }

    public void setIncSegundoApellido(String incSegundoApellido) {
        this.incSegundoApellido = incSegundoApellido;
    }

    public String getIncTercerApellido() {
        return incTercerApellido;
    }

    public void setIncTercerApellido(String incTercerApellido) {
        this.incTercerApellido = incTercerApellido;
    }

    public SgSexo getIncSexo() {
        return incSexo;
    }

    public void setIncSexo(SgSexo incSexo) {
        this.incSexo = incSexo;
    }

    public SgEstadoCivil getIncEstadoCivil() {
        return incEstadoCivil;
    }

    public void setIncEstadoCivil(SgEstadoCivil incEstadoCivil) {
        this.incEstadoCivil = incEstadoCivil;
    }

    public SgNacionalidad getIncNacionalidad() {
        return incNacionalidad;
    }

    public void setIncNacionalidad(SgNacionalidad incNacionalidad) {
        this.incNacionalidad = incNacionalidad;
    }

    public String getIncNombreSede() {
        return incNombreSede;
    }

    public void setIncNombreSede(String incNombreSede) {
        this.incNombreSede = incNombreSede;
    }

    public Integer getIncAnioEstudio() {
        return incAnioEstudio;
    }

    public void setIncAnioEstudio(Integer incAnioEstudio) {
        this.incAnioEstudio = incAnioEstudio;
    }

    public String getIncUltimoGradoEstudio() {
        return incUltimoGradoEstudio;
    }

    public void setIncUltimoGradoEstudio(String incUltimoGradoEstudio) {
        this.incUltimoGradoEstudio = incUltimoGradoEstudio;
    }

    public String getIncCiudad() {
        return incCiudad;
    }

    public void setIncCiudad(String incCiudad) {
        this.incCiudad = incCiudad;
    }

    public String getIncNumeroTramite() {
        return incNumeroTramite;
    }

    public void setIncNumeroTramite(String incNumeroTramite) {
        this.incNumeroTramite = incNumeroTramite;
    }

    public String getIncNumeroResolucion() {
        return incNumeroResolucion;
    }

    public void setIncNumeroResolucion(String incNumeroResolucion) {
        this.incNumeroResolucion = incNumeroResolucion;
    }

    public LocalDate getIncFechaAprobacion() {
        return incFechaAprobacion;
    }

    public void setIncFechaAprobacion(LocalDate incFechaAprobacion) {
        this.incFechaAprobacion = incFechaAprobacion;
    }

    public LocalDate getIncFechaNacimiento() {
        return incFechaNacimiento;
    }

    public void setIncFechaNacimiento(LocalDate incFechaNacimiento) {
        this.incFechaNacimiento = incFechaNacimiento;
    }
    

    @Override
    public int hashCode() {
        return Objects.hashCode(this.incPk);
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
        final SgIncorporacion other = (SgIncorporacion) obj;
        if (!Objects.equals(this.incPk, other.incPk)) {
            return false;
        }
        return true;
    }

}
