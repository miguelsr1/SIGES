/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumTipoComponentePentaho;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;

@Entity
@Table(name = "sg_menu_pentaho", uniqueConstraints = {
    @UniqueConstraint(name = "mpe_ruta_uk", columnNames = {"mpe_ruta"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mpePk", scope = SgMenuPentaho.class)
public class SgMenuPentaho implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mpe_pk")
    private Long mpePk;
    
    @Column(name = "mpe_tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumTipoComponentePentaho mpeTipo;
    
    @Size(max = 255)
    @Column(name = "mpe_nombre",length = 255)
    @AtributoNormalizable
    @AtributoNombre
    private String mpeNombre;
    
    
    @JoinColumn(name = "mpe_operacion_fk", referencedColumnName = "ope_pk")
    @ManyToOne(optional = false)
    private SgOperacion mpeOperacionFk;
    
    @Size(max = 255)
    @Column(name = "mpe_ruta",length = 255)
    @AtributoNormalizable
    private String mpeRuta;
        
    @Column(name = "mpe_habilitado")
    @AtributoHabilitado
    private Boolean mpeHabilitado;
    
    @Column(name = "mpe_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mpeUltModFecha;
    
    @Size(max = 45)
    @Column(name = "mpe_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String mpeUltModUsuario;
    
    @Column(name = "mpe_version")
    @Version
    private Integer mpeVersion;

    public SgMenuPentaho() {
    }

    public Long getMpePk() {
        return mpePk;
    }

    public void setMpePk(Long mpePk) {
        this.mpePk = mpePk;
    }

    public EnumTipoComponentePentaho getMpeTipo() {
        return mpeTipo;
    }

    public void setMpeTipo(EnumTipoComponentePentaho mpeTipo) {
        this.mpeTipo = mpeTipo;
    }

    public Boolean getMpeHabilitado() {
        return mpeHabilitado;
    }

    public void setMpeHabilitado(Boolean mpeHabilitado) {
        this.mpeHabilitado = mpeHabilitado;
    }

    public String getMpeNombre() {
        return mpeNombre;
    }

    public void setMpeNombre(String mpeNombre) {
        this.mpeNombre = mpeNombre;
    }
    
    
    public String getMpeRuta() {
        return mpeRuta;
    }

    public void setMpeRuta(String mpeRuta) {
        this.mpeRuta = mpeRuta;
    }

    public LocalDateTime getMpeUltModFecha() {
        return mpeUltModFecha;
    }

    public void setMpeUltModFecha(LocalDateTime mpeUltModFecha) {
        this.mpeUltModFecha = mpeUltModFecha;
    }

    public String getMpeUltModUsuario() {
        return mpeUltModUsuario;
    }

    public void setMpeUltModUsuario(String mpeUltModUsuario) {
        this.mpeUltModUsuario = mpeUltModUsuario;
    }

    public Integer getMpeVersion() {
        return mpeVersion;
    }

    public void setMpeVersion(Integer mpeVersion) {
        this.mpeVersion = mpeVersion;
    }
    
    public SgOperacion getMpeOperacionFk() {
        return mpeOperacionFk;
    }

    public void setMpeOperacionFk(SgOperacion mpeOperacionFk) {
        this.mpeOperacionFk = mpeOperacionFk;
    }

    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mpePk);
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
        final SgMenuPentaho other = (SgMenuPentaho) obj;
        if (!Objects.equals(this.mpePk, other.mpePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMenuPentaho{" + "mpePk=" + mpePk + ", mpeRuta=" + mpeRuta + ", mpeNombre=" + mpeNombre + ", mpeHabilitado=" + mpeHabilitado + ", mpeUltModFecha=" + mpeUltModFecha + ", mpeUltModUsuario=" + mpeUltModUsuario + ", mpeVersion=" + mpeVersion + '}';
    }
    
    
}
