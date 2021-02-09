/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_confirmaciones_solicitudes_traslado", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sotPk", scope = SgConfirmacionSolicitudTraslado.class)
public class SgConfirmacionSolicitudTraslado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sot_pk")
    private Long sotPk;

    @Column(name = "sot_solicitud_traslado_fk")
    private Long sotSolicitudTrasladoPk;
    
    @Column(name = "sot_fecha_traslado")
    private LocalDate sotFechaTraslado;

    @JoinColumn(name = "sot_seccion_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgSeccion sotSeccion;
    
    @Column(name = "sot_firmada")
    private Boolean sotFirmada;
    
    @Size(max = 500)
    @Column(name = "sot_resolucion", length = 500)
    private String sotResolucion;


    @Column(name = "sot_firma_transaction_id")
    private String sotFirmaTransactionId; // Transaction id
    
    @Column(name = "sot_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime sotUltModFecha;

    @Size(max = 45)
    @Column(name = "sot_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String sotUltModUsuario;
 
    @Column(name = "sot_version")
    @Version
    private Integer sotVersion;
    
    @Transient
    private byte[] sotArchivoFirmado;
    
    @Transient
    private String sotTransactionSignatureUrl; // Url a donde redirigir para firmar
    
    @Transient
    private String sotTransactionReturnUrl; // Url a donde redirigir luego de firma


    public SgConfirmacionSolicitudTraslado() {
    }

    public SgConfirmacionSolicitudTraslado(Long sotPk) {
        this.sotPk = sotPk;
    }

    public Boolean getSotFirmada() {
        return sotFirmada;
    }

    public void setSotFirmada(Boolean sotFirmada) {
        this.sotFirmada = sotFirmada;
    }
    
    public Long getSotPk() {
        return sotPk;
    }

    public void setSotPk(Long sotPk) {
        this.sotPk = sotPk;
    }

    public Long getSotSolicitudTrasladoPk() {
        return sotSolicitudTrasladoPk;
    }

    public void setSotSolicitudTrasladoPk(Long sotSolicitudTrasladoPk) {
        this.sotSolicitudTrasladoPk = sotSolicitudTrasladoPk;
    }

    public SgSeccion getSotSeccion() {
        return sotSeccion;
    }

    public void setSotSeccion(SgSeccion sotSeccion) {
        this.sotSeccion = sotSeccion;
    }

    public LocalDate getSotFechaTraslado() {
        return sotFechaTraslado;
    }

    public void setSotFechaTraslado(LocalDate sotFechaTraslado) {
        this.sotFechaTraslado = sotFechaTraslado;
    }

    public String getSotResolucion() {
        return sotResolucion;
    }

    public void setSotResolucion(String sotResolucion) {
        this.sotResolucion = sotResolucion;
    }
  
    public String getSotFirmaTransactionId() {
        return sotFirmaTransactionId;
    }

    public void setSotFirmaTransactionId(String sotFirmaTransactionId) {
        this.sotFirmaTransactionId = sotFirmaTransactionId;
    }

    public String getSotTransactionSignatureUrl() {
        return sotTransactionSignatureUrl;
    }

    public void setSotTransactionSignatureUrl(String sotTransactionSignatureUrl) {
        this.sotTransactionSignatureUrl = sotTransactionSignatureUrl;
    }

    public String getSotTransactionReturnUrl() {
        return sotTransactionReturnUrl;
    }

    public void setSotTransactionReturnUrl(String sotTransactionReturnUrl) {
        this.sotTransactionReturnUrl = sotTransactionReturnUrl;
    }

    public Integer getSotVersion() {
        return sotVersion;
    }

    public void setSotVersion(Integer sotVersion) {
        this.sotVersion = sotVersion;
    }

    public byte[] getSotArchivoFirmado() {
        return sotArchivoFirmado;
    }

    public void setSotArchivoFirmado(byte[] sotArchivoFirmado) {
        this.sotArchivoFirmado = sotArchivoFirmado;
    }

    public LocalDateTime getSotUltModFecha() {
        return sotUltModFecha;
    }

    public void setSotUltModFecha(LocalDateTime sotUltModFecha) {
        this.sotUltModFecha = sotUltModFecha;
    }

    public String getSotUltModUsuario() {
        return sotUltModUsuario;
    }

    public void setSotUltModUsuario(String sotUltModUsuario) {
        this.sotUltModUsuario = sotUltModUsuario;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sotPk != null ? sotPk.hashCode() : 0);
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
        final SgConfirmacionSolicitudTraslado other = (SgConfirmacionSolicitudTraslado) obj;
        if (!Objects.equals(this.sotPk, other.sotPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSolicitudTraslado[ sotPk=" + sotPk + " ]";
    }

}
