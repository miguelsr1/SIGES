/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

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
import sv.gob.mined.siges.auditoria.ResultadoOperacion;

@Entity
@Table(name = "sg_registros_auditoria_consumo_rnpn", schema = "auditoria")
public class SgRegistroAuditoriaConsumoRNPN implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aud_pk", nullable = false)
    private Long audPk;
    
    @Column(name = "aud_persona_documento")
    private String audPersonaDocumento;
        
    @Size(max = 255)
    @Column(name = "aud_operacion", length = 255)
    private String audOperacion; //CUN, DUI
    
    @Column(name = "aud_endpoint")
    private String audEndpoint;
    
    @Column(name = "aud_cuerpo_enviado")
    private String audCuerpoEnviado;

    @Column(name = "aud_cuerpo_recibido")
    private String audCuerpoRecibido;
    
    @Column(name = "aud_resultado_operacion")
    @Enumerated(EnumType.STRING)
    private ResultadoOperacion audResultadoOperacion;

    @Column(name = "aud_fecha")
    private LocalDateTime audFecha;

    @Size(max = 1000)
    @Column(name = "aud_mensaje", length = 1000)
    private String audMensaje;

    @Size(max = 45)
    @Column(name = "aud_codigo_usuario", length = 45)
    private String audCodigoUsuario;

    @Size(max = 300)
    @Column(name = "aud_hash_md5", length = 300)
    private String audHashMD5;
    
 
    public SgRegistroAuditoriaConsumoRNPN() {
    }
    
    public String calcularMD5() throws Exception {
        StringBuffer cadena = new StringBuffer();
        cadena.append(this.audPersonaDocumento);
        cadena.append(this.audOperacion);
        cadena.append(this.audCuerpoEnviado);
        cadena.append(this.audCuerpoRecibido);
        cadena.append(this.audResultadoOperacion);
        cadena.append(this.audFecha);
        cadena.append(this.audMensaje);
        cadena.append(this.audCodigoUsuario);
        cadena.append(this.audEndpoint);
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

    public String getAudOperacion() {
        return audOperacion;
    }

    public void setAudOperacion(String audOperacion) {
        this.audOperacion = audOperacion;
    }
    
    public ResultadoOperacion getAudResultadoOperacion() {
        return audResultadoOperacion;
    }

    public void setAudResultadoOperacion(ResultadoOperacion audResultadoOperacion) {
        this.audResultadoOperacion = audResultadoOperacion;
    }

    public LocalDateTime getAudFecha() {
        return audFecha;
    }

    public void setAudFecha(LocalDateTime audFecha) {
        this.audFecha = audFecha;
    }

    public String getAudMensaje() {
        return audMensaje;
    }

    public void setAudMensaje(String audMensaje) {
        this.audMensaje = audMensaje;
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

    public String getAudPersonaDocumento() {
        return audPersonaDocumento;
    }

    public void setAudPersonaDocumento(String audPersonaDocumento) {
        this.audPersonaDocumento = audPersonaDocumento;
    } 

    public String getAudCuerpoEnviado() {
        return audCuerpoEnviado;
    }

    public void setAudCuerpoEnviado(String audCuerpoEnviado) {
        this.audCuerpoEnviado = audCuerpoEnviado;
    }

    public String getAudCuerpoRecibido() {
        return audCuerpoRecibido;
    }

    public void setAudCuerpoRecibido(String audCuerpoRecibido) {
        this.audCuerpoRecibido = audCuerpoRecibido;
    }

    public String getAudEndpoint() {
        return audEndpoint;
    }

    public void setAudEndpoint(String audEndpoint) {
        this.audEndpoint = audEndpoint;
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
        final SgRegistroAuditoriaConsumoRNPN other = (SgRegistroAuditoriaConsumoRNPN) obj;
        if (!Objects.equals(this.audPk, other.audPk)) {
            return false;
        }
        return true;
    }

    

}
