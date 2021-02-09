/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

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
@Table(name = "sg_motivos_seleccion_plaza", uniqueConstraints = {
    @UniqueConstraint(name = "msp_codigo_uk", columnNames = {"msp_codigo"})
    ,
    @UniqueConstraint(name = "msp_nombre_uk", columnNames = {"msp_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mspPk", scope = SgMotivosSeleccionPLaza.class)
@Audited
public class SgMotivosSeleccionPLaza implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "msp_pk", nullable = false)
    private Long mspPk;

    @Size(max = 45)
    @Column(name = "msp_codigo", length = 45)
    @AtributoCodigo
    private String mspCodigo;

    @Size(max = 255)
    @Column(name = "msp_nombre", length = 255)
    @AtributoNormalizable
    private String mspNombre;
    
    @Column(name = "msp_orden")
    private Integer mspOrden;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "msp_nombre_busqueda", length = 255)
    private String mspNombreBusqueda;

    @Column(name = "msp_habilitado")
    @AtributoHabilitado
    private Boolean mspHabilitado;

    @Column(name = "msp_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mspUltModFecha;

    @Size(max = 45)
    @Column(name = "msp_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mspUltModUsuario;

    @Column(name = "msp_version")
    @Version
    private Integer mspVersion;

    public SgMotivosSeleccionPLaza() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mspNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mspNombre);
    }

    public Long getMspPk() {
        return mspPk;
    }

    public void setMspPk(Long mspPk) {
        this.mspPk = mspPk;
    }

    public String getMspCodigo() {
        return mspCodigo;
    }

    public void setMspCodigo(String mspCodigo) {
        this.mspCodigo = mspCodigo;
    }

    public String getMspNombre() {
        return mspNombre;
    }

    public void setMspNombre(String mspNombre) {
        this.mspNombre = mspNombre;
    }

    public Integer getMspOrden() {
        return mspOrden;
    }

    public void setMspOrden(Integer mspOrden) {
        this.mspOrden = mspOrden;
    }

    public String getMspNombreBusqueda() {
        return mspNombreBusqueda;
    }

    public void setMspNombreBusqueda(String mspNombreBusqueda) {
        this.mspNombreBusqueda = mspNombreBusqueda;
    }

    public Boolean getMspHabilitado() {
        return mspHabilitado;
    }

    public void setMspHabilitado(Boolean mspHabilitado) {
        this.mspHabilitado = mspHabilitado;
    }

    public LocalDateTime getMspUltModFecha() {
        return mspUltModFecha;
    }

    public void setMspUltModFecha(LocalDateTime mspUltModFecha) {
        this.mspUltModFecha = mspUltModFecha;
    }

    public String getMspUltModUsuario() {
        return mspUltModUsuario;
    }

    public void setMspUltModUsuario(String mspUltModUsuario) {
        this.mspUltModUsuario = mspUltModUsuario;
    }

    public Integer getMspVersion() {
        return mspVersion;
    }

    public void setMspVersion(Integer mspVersion) {
        this.mspVersion = mspVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.mspPk);
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
        final SgMotivosSeleccionPLaza other = (SgMotivosSeleccionPLaza) obj;
        if (!Objects.equals(this.mspPk, other.mspPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMotivosSeleccionPLaza{" + "mspPk=" + mspPk + ", mspCodigo=" + mspCodigo + ", mspNombre=" + mspNombre + ", mspNombreBusqueda=" + mspNombreBusqueda + ", mspHabilitado=" + mspHabilitado + ", mspUltModFecha=" + mspUltModFecha + ", mspUltModUsuario=" + mspUltModUsuario + ", mspVersion=" + mspVersion + '}';
    }
    
    

}
