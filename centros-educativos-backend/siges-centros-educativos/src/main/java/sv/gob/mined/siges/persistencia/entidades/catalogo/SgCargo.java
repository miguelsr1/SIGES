/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_cargo", uniqueConstraints = {
    @UniqueConstraint(name = "car_codigo_uk", columnNames = {"car_codigo"})
    ,
    @UniqueConstraint(name = "car_nombre_uk", columnNames = {"car_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "carPk", scope = SgCargo.class)
public class SgCargo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "car_pk")
    private Long carPk;
    
    @Size(max = 45)
    @Column(name = "car_codigo",length = 45)
    @AtributoCodigo
    private String carCodigo;
    
    @Column(name = "car_habilitado")
    @AtributoHabilitado
    private Boolean carHabilitado;
    
    @Size(max = 255)
    @Column(name = "car_nombre",length = 255)
    @AtributoNormalizable
    private String carNombre;
    
    @Size(max = 255)
    @Column(name = "car_nombre_busqueda",length = 255)
    @AtributoNombre
    private String carNombreBusqueda;
     
    @Column(name = "car_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime carUltModFecha;
    
    @Size(max = 45)
    @Column(name = "car_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String carUltModUsuario;
    
    @Column(name = "car_version")
    @Version
    private Integer carVersion;
    
    @Column(name = "car_aplica_acuerdo")
    private Boolean carAplicaAcuerdo;
    
    @Column(name = "car_aplica_contrato")
    private Boolean carAplicaContrato;
    
    @Column(name = "car_aplica_otros")
    private Boolean carAplicaOtros;
      
    public SgCargo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.carNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.carNombre);
    }

    public SgCargo(Long carPk) {
        this.carPk = carPk;
    }

    public Long getCarPk() {
        return carPk;
    }

    public void setCarPk(Long carPk) {
        this.carPk = carPk;
    }

    public String getCarCodigo() {
        return carCodigo;
    }

    public void setCarCodigo(String carCodigo) {
        this.carCodigo = carCodigo;
    }

    public Boolean getCarHabilitado() {
        return carHabilitado;
    }

    public void setCarHabilitado(Boolean carHabilitado) {
        this.carHabilitado = carHabilitado;
    }

    public String getCarNombre() {
        return carNombre;
    }

    public void setCarNombre(String carNombre) {
        this.carNombre = carNombre;
    }

    public String getCarNombreBusqueda() {
        return carNombreBusqueda;
    }

    public void setCarNombreBusqueda(String carNombreBusqueda) {
        this.carNombreBusqueda = carNombreBusqueda;
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

    public Boolean getCarAplicaAcuerdo() {
        return carAplicaAcuerdo;
    }

    public void setCarAplicaAcuerdo(Boolean carAplicaAcuerdo) {
        this.carAplicaAcuerdo = carAplicaAcuerdo;
    }

    public Boolean getCarAplicaContrato() {
        return carAplicaContrato;
    }

    public void setCarAplicaContrato(Boolean carAplicaContrato) {
        this.carAplicaContrato = carAplicaContrato;
    }

    public Boolean getCarAplicaOtros() {
        return carAplicaOtros;
    }

    public void setCarAplicaOtros(Boolean carAplicaOtros) {
        this.carAplicaOtros = carAplicaOtros;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carPk != null ? carPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCargo)) {
            return false;
        }
        SgCargo other = (SgCargo) object;
        if ((this.carPk == null && other.carPk != null) || (this.carPk != null && !this.carPk.equals(other.carPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCargo[ carPk=" + carPk + " ]";
    }
    
}
