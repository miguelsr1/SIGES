/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

@Entity
@Table(name = "sg_cargos_roles", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "carPk", scope = SgCargoRoles.class)
@Audited
public class SgCargoRoles implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "car_pk", nullable = false)
    private Long carPk;
    
    @JoinColumn(name = "car_cargo_fk")
    @ManyToOne
    private SgCargo carCargo;
    
    @JoinColumn(name = "car_rol_fk")
    @ManyToOne
    private SgRol carRol;
    
    @Column(name = "car_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime carUltModFecha;  
    
    @Size(max = 45)
    @Column(name = "car_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String carUltModUsuario;
        
    @Column(name = "car_version")
    @Version
    private Integer carVersion;

    public Long getCarPk() {
        return carPk;
    }

    public void setCarPk(Long carPk) {
        this.carPk = carPk;
    }

    public SgCargo getCarCargo() {
        return carCargo;
    }

    public void setCarCargo(SgCargo carCargo) {
        this.carCargo = carCargo;
    }

    public SgRol getCarRol() {
        return carRol;
    }

    public void setCarRol(SgRol carRol) {
        this.carRol = carRol;
    }

    public LocalDateTime getCarUltModFecha() {
        return carUltModFecha;
    }

    public void setCarUltModFecha(LocalDateTime carUltModFecha) {
        this.carUltModFecha = carUltModFecha;
    }

    public String getCarUltModUsuario() {
        return carUltModUsuario;
    }

    public void setCarUltModUsuario(String carUltModUsuario) {
        this.carUltModUsuario = carUltModUsuario;
    }

    public Integer getCarVersion() {
        return carVersion;
    }

    public void setCarVersion(Integer carVersion) {
        this.carVersion = carVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.carPk);
        hash = 97 * hash + Objects.hashCode(this.carCargo);
        hash = 97 * hash + Objects.hashCode(this.carRol);
        hash = 97 * hash + Objects.hashCode(this.carUltModFecha);
        hash = 97 * hash + Objects.hashCode(this.carUltModUsuario);
        hash = 97 * hash + Objects.hashCode(this.carVersion);
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
        final SgCargoRoles other = (SgCargoRoles) obj;
        if (!Objects.equals(this.carPk, other.carPk)) {
            return false;
        }
        return true;
    }
    
    
}
