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
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_espacios_comunes", uniqueConstraints = {
    @UniqueConstraint(name = "eco_codigo_uk", columnNames = {"eco_codigo"})
    ,
    @UniqueConstraint(name = "eco_nombre_uk", columnNames = {"eco_nombre"})},schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ecoPk", scope = SgEspacioComun.class)
@Audited
public class SgEspacioComun implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eco_pk", nullable = false)
    private Long ecoPk;

    @Size(max = 45)
    @Column(name = "eco_codigo", length = 45)
    @AtributoCodigo
    private String ecoCodigo;

    @Size(max = 255)
    @Column(name = "eco_nombre", length = 255)
    @AtributoNormalizable
    private String ecoNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "eco_nombre_busqueda", length = 255)
    private String ecoNombreBusqueda;

    @Column(name = "eco_habilitado")
    @AtributoHabilitado
    private Boolean ecoHabilitado;    
    
    @Column(name = "eco_permite_ingresar_descripcion")
    private Boolean ecoPermiteIngresarDescripcion;
    
    @Column(name = "eco_aplica_terreno")
    private Boolean ecoAplicaTerreno;
    
    @Column(name = "eco_aplica_edificio")
    private Boolean ecoAplicaEdificio;
    
    @Column(name = "eco_aplica_aula")
    private Boolean ecoAplicaAula;
    
    @Size(max = 500)
    @Column(name = "eco_descripcion", length = 500)
    private String ecoDescripcion;

    @Column(name = "eco_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ecoUltModFecha;

    @Size(max = 45)
    @Column(name = "eco_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ecoUltModUsuario;

    @Column(name = "eco_version")
    @Version
    private Integer ecoVersion;    
    
    @Column(name = "eco_orden")
    private Integer ecoOrden;

    public SgEspacioComun() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ecoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ecoNombre);
    }

    public Long getEcoPk() {
        return ecoPk;
    }

    public void setEcoPk(Long ecoPk) {
        this.ecoPk = ecoPk;
    }

    public String getEcoCodigo() {
        return ecoCodigo;
    }

    public void setEcoCodigo(String ecoCodigo) {
        this.ecoCodigo = ecoCodigo;
    }

    public String getEcoNombre() {
        return ecoNombre;
    }

    public void setEcoNombre(String ecoNombre) {
        this.ecoNombre = ecoNombre;
    }

    public String getEcoNombreBusqueda() {
        return ecoNombreBusqueda;
    }

    public void setEcoNombreBusqueda(String ecoNombreBusqueda) {
        this.ecoNombreBusqueda = ecoNombreBusqueda;
    }

    public Boolean getEcoHabilitado() {
        return ecoHabilitado;
    }

    public void setEcoHabilitado(Boolean ecoHabilitado) {
        this.ecoHabilitado = ecoHabilitado;
    }

    public Boolean getEcoAplicaTerreno() {
        return ecoAplicaTerreno;
    }

    public void setEcoAplicaTerreno(Boolean ecoAplicaTerreno) {
        this.ecoAplicaTerreno = ecoAplicaTerreno;
    }

    public Boolean getEcoAplicaEdificio() {
        return ecoAplicaEdificio;
    }

    public void setEcoAplicaEdificio(Boolean ecoAplicaEdificio) {
        this.ecoAplicaEdificio = ecoAplicaEdificio;
    }

    public Boolean getEcoAplicaAula() {
        return ecoAplicaAula;
    }

    public void setEcoAplicaAula(Boolean ecoAplicaAula) {
        this.ecoAplicaAula = ecoAplicaAula;
    }

    public String getEcoDescripcion() {
        return ecoDescripcion;
    }

    public void setEcoDescripcion(String ecoDescripcion) {
        this.ecoDescripcion = ecoDescripcion;
    }

    public LocalDateTime getEcoUltModFecha() {
        return ecoUltModFecha;
    }

    public void setEcoUltModFecha(LocalDateTime ecoUltModFecha) {
        this.ecoUltModFecha = ecoUltModFecha;
    }

    public String getEcoUltModUsuario() {
        return ecoUltModUsuario;
    }

    public void setEcoUltModUsuario(String ecoUltModUsuario) {
        this.ecoUltModUsuario = ecoUltModUsuario;
    }

    public Integer getEcoVersion() {
        return ecoVersion;
    }

    public void setEcoVersion(Integer ecoVersion) {
        this.ecoVersion = ecoVersion;
    }

    public Boolean getEcoPermiteIngresarDescripcion() {
        return ecoPermiteIngresarDescripcion;
    }

    public void setEcoPermiteIngresarDescripcion(Boolean ecoPermiteIngresarDescripcion) {
        this.ecoPermiteIngresarDescripcion = ecoPermiteIngresarDescripcion;
    }

    public Integer getEcoOrden() {
        return ecoOrden;
    }

    public void setEcoOrden(Integer ecoOrden) {
        this.ecoOrden = ecoOrden;
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ecoPk);
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
        final SgEspacioComun other = (SgEspacioComun) obj;
        if (!Objects.equals(this.ecoPk, other.ecoPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEspacioComun{" + "ecoPk=" + ecoPk + ", ecoCodigo=" + ecoCodigo + ", ecoNombre=" + ecoNombre + ", ecoNombreBusqueda=" + ecoNombreBusqueda + ", ecoHabilitado=" + ecoHabilitado + ", ecoUltModFecha=" + ecoUltModFecha + ", ecoUltModUsuario=" + ecoUltModUsuario + ", ecoVersion=" + ecoVersion + '}';
    }
    
    

}
