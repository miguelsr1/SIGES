/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.auditoria;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

@Entity
@Table(name = "sg_registros_auditoria", schema = "auditoria")
public class SgRegistroAuditoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aud_pk", nullable = false)
    private Long audPk;

    @Size(max = 45)
    @Column(name = "aud_ip", length = 45)
    private String audIp;

    @Size(max = 255)
    @Column(name = "aud_operacion", length = 255)
    private String audOperacion;

    @Size(max = 255)
    @Column(name = "aud_clase", length = 255)
    private String audClase;
    
    @Column(name = "aud_entidad_id")
    private Long audEntidadId;

    @Size(max = 1000)
    @Column(name = "aud_comentario", length = 1000)
    private String audComentario;

    @Column(name = "aud_resultado_operacion")
    @Enumerated(EnumType.STRING)
    private ResultadoOperacion audResultadoOperacion;

    @Column(name = "aud_fecha")
    private LocalDateTime audFecha;

    @Size(max = 1000)
    @Column(name = "aud_excepcion", length = 1000)
    private String audExcepcion;

    @Size(max = 45)
    @Column(name = "aud_codigo_usuario", length = 45)
    private String audCodigoUsuario;

    @Size(max = 300)
    @Column(name = "aud_hash_md5", length = 300)
    private String audHashMD5;

    public SgRegistroAuditoria() {
    }
    
    public String calcularMD5() throws Exception {
        StringBuffer cadena = new StringBuffer();
        cadena.append(this.audIp);
        cadena.append(this.audOperacion);
        cadena.append(this.audClase);
        cadena.append(this.audEntidadId);
        cadena.append(this.audComentario);
        cadena.append(this.audResultadoOperacion);
        cadena.append(this.audFecha);
        cadena.append(this.audExcepcion);
        cadena.append(this.audCodigoUsuario);
        String datos = cadena.toString();
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(datos.getBytes(), 0, datos.length());
        return new BigInteger(1, m.digest()).toString(16);
    }

    public Long getAudPk() {
        return audPk;
    }

    public void setAudPk(Long audPk) {
        this.audPk = audPk;
    }

    public String getAudIp() {
        return audIp;
    }

    public void setAudIp(String audIp) {
        this.audIp = audIp;
    }

    public String getAudOperacion() {
        return audOperacion;
    }

    public void setAudOperacion(String audOperacion) {
        this.audOperacion = audOperacion;
    }

    public String getAudClase() {
        return audClase;
    }

    public void setAudClase(String audClase) {
        this.audClase = audClase;
    }

    public ResultadoOperacion getAudResultadoOperacion() {
        return audResultadoOperacion;
    }

    public void setAudResultadoOperacion(ResultadoOperacion audResultadoOperacion) {
        this.audResultadoOperacion = audResultadoOperacion;
    }

    public Long getAudEntidadId() {
        return audEntidadId;
    }

    public void setAudEntidadId(Long audEntidadId) {
        this.audEntidadId = audEntidadId;
    }

    public String getAudComentario() {
        return audComentario;
    }

    public void setAudComentario(String audComentario) {
        this.audComentario = audComentario;
    }

    public LocalDateTime getAudFecha() {
        return audFecha;
    }

    public void setAudFecha(LocalDateTime audFecha) {
        this.audFecha = audFecha;
    }

    public String getAudExcepcion() {
        return audExcepcion;
    }

    public void setAudExcepcion(String audExcepcion) {
        this.audExcepcion = audExcepcion;
    }

    public String getAudCodigoUsuario() {
        return audCodigoUsuario;
    }

    public void setAudCodigoUsuario(String audCodigoUsuario) {
        this.audCodigoUsuario = audCodigoUsuario;
    }

    public String getAudHashMD5() {
        return audHashMD5;
    }

    public void setAudHashMD5(String audHashMD5) {
        this.audHashMD5 = audHashMD5;
    }

    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.audPk);
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
        final SgRegistroAuditoria other = (SgRegistroAuditoria) obj;
        if (!Objects.equals(this.audPk, other.audPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SigesRegistroAuditoria{" + "audPk=" + audPk + ", audIp=" + audIp + ", audOperacion=" + audOperacion + ", audClase=" + audClase + ", audEntidadId=" + audEntidadId + ", audComentario=" + audComentario + ", audResultadoOperacion=" + audResultadoOperacion + ", audFecha=" + audFecha + ", audExcepcion=" + audExcepcion + ", audCodigoUsuario=" + audCodigoUsuario + ", audHashMD5=" + audHashMD5 + '}';
    }

    

}
