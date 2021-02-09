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
@Table(name = "sg_inf_accesibilidad", uniqueConstraints = {
    @UniqueConstraint(name = "acc_codigo_uk", columnNames = {"acc_codigo"})
    ,
    @UniqueConstraint(name = "acc_nombre_uk", columnNames = {"acc_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "accPk", scope = SgInfAccesibilidad.class)
@Audited
public class SgInfAccesibilidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "acc_pk", nullable = false)
    private Long accPk;

    @Size(max = 45)
    @Column(name = "acc_codigo", length = 45)
    @AtributoCodigo
    private String accCodigo;

    @Size(max = 255)
    @Column(name = "acc_nombre", length = 255)
    @AtributoNormalizable
    private String accNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "acc_nombre_busqueda", length = 255)
    private String accNombreBusqueda;

    @Column(name = "acc_habilitado")
    @AtributoHabilitado
    private Boolean accHabilitado;

    @Column(name = "acc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime accUltModFecha;

    @Size(max = 45)
    @Column(name = "acc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String accUltModUsuario;

    @Column(name = "acc_version")
    @Version
    private Integer accVersion;
    
    @Column(name= "acc_orden")
    private Integer accOrden;
    
    @Column(name= "acc_aplica_otros")
    private Boolean accAplicaOtros;

    public SgInfAccesibilidad() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.accNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.accNombre);
    }

    public Long getAccPk() {
        return accPk;
    }

    public void setAccPk(Long accPk) {
        this.accPk = accPk;
    }

    public String getAccCodigo() {
        return accCodigo;
    }

    public void setAccCodigo(String accCodigo) {
        this.accCodigo = accCodigo;
    }

    public String getAccNombre() {
        return accNombre;
    }

    public void setAccNombre(String accNombre) {
        this.accNombre = accNombre;
    }

    public String getAccNombreBusqueda() {
        return accNombreBusqueda;
    }

    public void setAccNombreBusqueda(String accNombreBusqueda) {
        this.accNombreBusqueda = accNombreBusqueda;
    }

    public Boolean getAccHabilitado() {
        return accHabilitado;
    }

    public void setAccHabilitado(Boolean accHabilitado) {
        this.accHabilitado = accHabilitado;
    }

    public LocalDateTime getAccUltModFecha() {
        return accUltModFecha;
    }

    public void setAccUltModFecha(LocalDateTime accUltModFecha) {
        this.accUltModFecha = accUltModFecha;
    }

    public String getAccUltModUsuario() {
        return accUltModUsuario;
    }

    public void setAccUltModUsuario(String accUltModUsuario) {
        this.accUltModUsuario = accUltModUsuario;
    }

    public Integer getAccVersion() {
        return accVersion;
    }

    public void setAccVersion(Integer accVersion) {
        this.accVersion = accVersion;
    }

    public Integer getAccOrden() {
        return accOrden;
    }

    public void setAccOrden(Integer accOrden) {
        this.accOrden = accOrden;
    }

    public Boolean getAccAplicaOtros() {
        return accAplicaOtros;
    }

    public void setAccAplicaOtros(Boolean accAplicaOtros) {
        this.accAplicaOtros = accAplicaOtros;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.accPk);
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
        final SgInfAccesibilidad other = (SgInfAccesibilidad) obj;
        if (!Objects.equals(this.accPk, other.accPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgInfAccesibilidad{" + "accPk=" + accPk + ", accCodigo=" + accCodigo + ", accNombre=" + accNombre + ", accNombreBusqueda=" + accNombreBusqueda + ", accHabilitado=" + accHabilitado + ", accUltModFecha=" + accUltModFecha + ", accUltModUsuario=" + accUltModUsuario + ", accVersion=" + accVersion + '}';
    }
    
    

}
