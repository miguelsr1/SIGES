package uy.com.sofis.pfea.entities;

import com.sofis.persistence.anotaciones.AtributoUltimaModificacion;
import com.sofis.persistence.anotaciones.AtributoUltimoUsuario;
import com.sofis.persistence.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.hibernate.envers.Audited;
import uy.com.sofis.pfea.constantes.ConstantesConfiguracion;
import uy.com.sofis.pfea.enums.EstadoDocumentoBandeja;

/**
 *
 * @author sofis3
 */
@Entity
@Table(name = "ss_documentos", schema = ConstantesConfiguracion.SCHEMA_PFEA, uniqueConstraints = {
@UniqueConstraint(name = "uk_doc_identificador", columnNames = { "doc_identificador" }) })
@EntityListeners({DecoratorEntityListener.class})
@Audited
public class Documento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_documentos_gen")
    @SequenceGenerator(name = "seq_documentos_gen", schema = ConstantesConfiguracion.SCHEMA_PFEA, sequenceName = "seq_documentos", allocationSize = 1)
    @Column(name = "doc_id")
    private Integer id;
    
    @Column(name="doc_identificador")
    private String identificador;
    
    @Column(name="doc_codigopac")
    private String codigopac;
    
    @Size(max = 255)
    @Column(name = "doc_nombre_archivo")
    private String nombreArchivo;
    
    @Size(max = 255)
    @Column(name = "doc_nombre_original")
    private String nombreOriginal;
    
    @Size(max = 255)
    @Column(name = "doc_descripcion")
    private String descripcion;
    
    @Column(name = "doc_enviado_por_usuario")
    private String enviadoPorUsuario;
    
    @Column(name = "doc_debe_firmar_usuario")
    private String debeFirmarUsuario;
    
    @Column(name = "doc_fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    @Column(name = "doc_fecha_expiracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpiracion;
    
    @Column(name = "doc_fecha_eliminacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEliminacion;
    
    @Size(max = 255)
    @Column(name="doc_motivo")
    private String motivo;
    
    @Column(name = "doc_firmado")
    @Basic(optional = false)
    private boolean firmado = false;
    
    @Size(max = 255)
    @Column(name = "doc_documentotipo")
    private String documentoTipo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "doc_estado_documento")
    private EstadoDocumentoBandeja estadoDocumento;
    
    @Size(max = 255)
    @Column(name="doc_origen")
    private String origen;

    @Size(max = 1000)
    @Column(name="doc_retorno", columnDefinition = "VARCHAR(1000)")
    private String retorno;

    @Size(max = 1000)
    @Column(name="doc_retorno_navegacion", columnDefinition = "VARCHAR(1000)")
    private String retornoNavegacion;

    @Size(max = 1000)
    @Column(name="doc_respuesta", columnDefinition = "VARCHAR(1000)")
    private String respuesta;

    @Column(name = "doc_respuesta_error")
    private boolean respuestaError = false;

    @Size(max = 50)
    @Column(name = "doc_nubeabitab_pid")
    private String nubeAbitabPid;
    
    @Size(max = 4000)
    @Column(name="doc_ultima_accion", columnDefinition = "VARCHAR(4000)")
    private String ultimaAccion;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    @Column(name = "doc_eliminado")
    @Basic(optional = false)
    private Boolean eliminado;
    
    @Column(name = "doc_ultimo_usuario")
    @AtributoUltimoUsuario
    private String ultimoUsuario;
    
    @Column(name = "doc_ultima_modificacion")
    @Temporal(javax.persistence.TemporalType.DATE)
    @AtributoUltimaModificacion
    private Date ultimaModificacion;
    
    @Column(name = "doc_version")
    @Version
    private Integer docVersion;

    @Transient
    private List<byte[]> documentoBytes;

    public Integer getDocVersion() {
        return docVersion;
    }

    public void setDocVersion(Integer docVersion) {
        this.docVersion = docVersion;
    }

    public List<byte[]> getDocumentoBytes() {
        return documentoBytes;
    }

    public void setDocumentoBytes(List<byte[]> documentoBytes) {
        this.documentoBytes = documentoBytes;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Documento other = (Documento) obj;
        return Objects.equals(this.id, other.id);
             
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

   public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Date getFechaEliminacion() {
        return fechaEliminacion;
    }

    public void setFechaEliminacion(Date fechaEliminacion) {
        this.fechaEliminacion = fechaEliminacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public boolean getFirmado() {
        return firmado;
    }

    public void setFirmado(boolean firmado) {
        this.firmado = firmado;
    }

    public boolean getRespuestaError() {
        return respuestaError;
    }

    public void setRespuestaError(boolean respuestaError) {
        this.respuestaError = respuestaError;
    }

    public String getDocumentoTipo() {
        return documentoTipo;
    }

    public void setDocumentoTipo(String documentoTipo) {
        this.documentoTipo = documentoTipo;
    }

    public EstadoDocumentoBandeja getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setEstadoDocumento(EstadoDocumentoBandeja estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public String getRetornoNavegacion() {
        return retornoNavegacion;
    }

    public void setRetornoNavegacion(String retornoNavegacion) {
        this.retornoNavegacion = retornoNavegacion;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getUltimoUsuario() {
        return ultimoUsuario;
    }

    public void setUltimoUsuario(String ultimoUsuario) {
        this.ultimoUsuario = ultimoUsuario;
    }

    public Date getUltimaModificacion() {
        return ultimaModificacion;
    }

    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }

    public String getCodigopac() {
        return codigopac;
    }

    public void setCodigopac(String codigopac) {
        this.codigopac = codigopac;
    }

    public String getNubeAbitabPid() {
        return nubeAbitabPid;
    }

    public void setNubeAbitabPid(String nubeAbitabPid) {
        this.nubeAbitabPid = nubeAbitabPid;
    }

    public String getUltimaAccion() {
        return ultimaAccion;
    }

    public void setUltimaAccion(String ultimaAccion) {
        this.ultimaAccion = ultimaAccion;
    }

    public String getEnviadoPorUsuario() {
        return enviadoPorUsuario;
    }

    public void setEnviadoPorUsuario(String enviadoPorUsuario) {
        this.enviadoPorUsuario = enviadoPorUsuario;
    }

    public String getDebeFirmarUsuario() {
        return debeFirmarUsuario;
    }

    public void setDebeFirmarUsuario(String debeFirmarUsuario) {
        this.debeFirmarUsuario = debeFirmarUsuario;
    }
    
    

}
