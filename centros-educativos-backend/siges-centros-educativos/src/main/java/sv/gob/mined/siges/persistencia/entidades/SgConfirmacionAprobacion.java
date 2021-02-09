/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_confirmaciones_aprobaciones", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cprPk", scope = SgConfirmacionAprobacion.class)
@Audited
public class SgConfirmacionAprobacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cpr_pk", nullable = false)
    private Long cprPk;

    @Column(name = "cpr_fecha_confirmacion")
    private LocalDateTime cprFechaConfirmacion;
    
    @Size(max = 45)
    @Column(name = "cpr_usuario_confirmacion",length = 45)
    private String cprUsuarioConfirmacion;
    
    @JoinColumn(name = "cpr_seccion_fk")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgSeccion cprSeccion;
    
    @JoinColumn(name = "cpr_componente_plan_estudio_fk")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgComponentePlanEstudio cprComponentePlanEstudio;
        
    @Column(name = "cpr_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cprUltModFecha;

    @Size(max = 45)
    @Column(name = "cpr_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cprUltModUsuario;

    @Column(name = "cpr_version")
    @Version
    private Integer cprVersion;
    
    @JoinColumn(name = "cpr_archivo_firmado_fk")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private SgArchivo cprArchivoFirmado;
    
    @Column(name = "cpr_firmada")
    private Boolean cprFirmada;
    
    @Column(name = "cpr_firma_transaction_id")
    private String cprFirmaTransactionId; // Transaction id
    
    @Transient
    private String cprTransactionSignatureUrl; // Url a donde redirigir para firmar
    
    @Transient
    private String cprTransactionReturnUrl; // Url a donde redirigir luego de firma
    

    public SgConfirmacionAprobacion() {
    }

    public Long getCprPk() {
        return cprPk;
    }

    public void setCprPk(Long cprPk) {
        this.cprPk = cprPk;
    }

    public LocalDateTime getCprFechaConfirmacion() {
        return cprFechaConfirmacion;
    }

    public void setCprFechaConfirmacion(LocalDateTime cprFechaConfirmacion) {
        this.cprFechaConfirmacion = cprFechaConfirmacion;
    }

    public String getCprUsuarioConfirmacion() {
        return cprUsuarioConfirmacion;
    }

    public void setCprUsuarioConfirmacion(String cprUsuarioConfirmacion) {
        this.cprUsuarioConfirmacion = cprUsuarioConfirmacion;
    }

    public SgSeccion getCprSeccion() {
        return cprSeccion;
    }

    public void setCprSeccion(SgSeccion cprSeccion) {
        this.cprSeccion = cprSeccion;
    }

    public LocalDateTime getCprUltModFecha() {
        return cprUltModFecha;
    }

    public void setCprUltModFecha(LocalDateTime cprUltModFecha) {
        this.cprUltModFecha = cprUltModFecha;
    }

    public String getCprUltModUsuario() {
        return cprUltModUsuario;
    }

    public void setCprUltModUsuario(String cprUltModUsuario) {
        this.cprUltModUsuario = cprUltModUsuario;
    }

    public Integer getCprVersion() {
        return cprVersion;
    }

    public void setCprVersion(Integer cprVersion) {
        this.cprVersion = cprVersion;
    }

    public SgArchivo getCprArchivoFirmado() {
        return cprArchivoFirmado;
    }

    public void setCprArchivoFirmado(SgArchivo cprArchivoFirmado) {
        this.cprArchivoFirmado = cprArchivoFirmado;
    }


    public Boolean getCprFirmada() {
        return cprFirmada;
    }

    public void setCprFirmada(Boolean cprFirmada) {
        this.cprFirmada = cprFirmada;
    }

    public String getCprFirmaTransactionId() {
        return cprFirmaTransactionId;
    }

    public void setCprFirmaTransactionId(String cprFirmaTransactionId) {
        this.cprFirmaTransactionId = cprFirmaTransactionId;
    }

    public String getCprTransactionSignatureUrl() {
        return cprTransactionSignatureUrl;
    }

    public void setCprTransactionSignatureUrl(String cprTransactionSignatureUrl) {
        this.cprTransactionSignatureUrl = cprTransactionSignatureUrl;
    }

    public String getCprTransactionReturnUrl() {
        return cprTransactionReturnUrl;
    }

    public void setCprTransactionReturnUrl(String cprTransactionReturnUrl) {
        this.cprTransactionReturnUrl = cprTransactionReturnUrl;
    }

    public SgComponentePlanEstudio getCprComponentePlanEstudio() {
        return cprComponentePlanEstudio;
    }

    public void setCprComponentePlanEstudio(SgComponentePlanEstudio cprComponentePlanEstudio) {
        this.cprComponentePlanEstudio = cprComponentePlanEstudio;
    }
    
 
    @Override
    public int hashCode() {
        return Objects.hashCode(this.cprPk);
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
        final SgConfirmacionAprobacion other = (SgConfirmacionAprobacion) obj;
        if (!Objects.equals(this.cprPk, other.cprPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgConfirmacionAprobacion{" + "cprPk=" + cprPk + ", cprSeccion=" + cprSeccion + ", cprComponentePlanEstudio=" + cprComponentePlanEstudio + '}';
    }

    

    
    
    

}
