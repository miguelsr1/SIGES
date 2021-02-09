/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "SS_LOG_AUDITORIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogAuditoria.findAll", query = "SELECT s FROM LogAuditoria s"),
    @NamedQuery(name = "LogAuditoria.findByLogId", query = "SELECT s FROM LogAuditoria s WHERE s.logId = :logId"),
    @NamedQuery(name = "LogAuditoria.findByLogFecha", query = "SELECT s FROM LogAuditoria s WHERE s.logFecha = :logFecha"),
    @NamedQuery(name = "LogAuditoria.findByLogUsuario", query = "SELECT s FROM LogAuditoria s WHERE s.logUsuario = :logUsuario"),
    @NamedQuery(name = "LogAuditoria.findByLogTrn", query = "SELECT s FROM LogAuditoria s WHERE s.logTrn = :logTrn"),
    @NamedQuery(name = "LogAuditoria.findByLogDescripcion", query = "SELECT s FROM LogAuditoria s WHERE s.logDescripcion = :logDescripcion"),
    @NamedQuery(name = "LogAuditoria.findByLogOperacion", query = "SELECT s FROM LogAuditoria s WHERE s.logOperacion = :logOperacion"),
    @NamedQuery(name = "LogAuditoria.findByLogEntidadId", query = "SELECT s FROM LogAuditoria s WHERE s.logEntidadId = :logEntidadId"),
    @NamedQuery(name = "LogAuditoria.findByLogEntidadTipo", query = "SELECT s FROM LogAuditoria s WHERE s.logEntidadTipo = :logEntidadTipo")})
public class LogAuditoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOG_ID", nullable = false, precision = 38, scale = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_log_auditoria_gen")
    @SequenceGenerator(name = "seq_log_auditoria_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_log_auditoria", allocationSize = 1)
    private BigDecimal logId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOG_FECHA", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date logFecha;
    @Size(max = 50)
    @Column(name = "LOG_USUARIO", length = 50)
    private String logUsuario;
    @Column(name = "LOG_TRN")
    private BigInteger logTrn;
    @Size(max = 1000)
    @Column(name = "LOG_DESCRIPCION", length = 1000)
    private String logDescripcion;
    @Size(max = 50)
    @Column(name = "LOG_OPERACION", length = 50)
    private String logOperacion;
    @Column(name = "LOG_ENTIDAD_ID")
    private BigInteger logEntidadId;
    @Column(name = "LOG_ENTIDAD_TIPO")
    private BigInteger logEntidadTipo;

    public LogAuditoria() {
    }

    public LogAuditoria(BigDecimal logId) {
        this.logId = logId;
    }

    public LogAuditoria(BigDecimal logId, Date logFecha) {
        this.logId = logId;
        this.logFecha = logFecha;
    }

    public BigDecimal getLogId() {
        return logId;
    }

    public void setLogId(BigDecimal logId) {
        this.logId = logId;
    }

    public Date getLogFecha() {
        return logFecha;
    }

    public void setLogFecha(Date logFecha) {
        this.logFecha = logFecha;
    }

    public String getLogUsuario() {
        return logUsuario;
    }

    public void setLogUsuario(String logUsuario) {
        this.logUsuario = logUsuario;
    }

    public BigInteger getLogTrn() {
        return logTrn;
    }

    public void setLogTrn(BigInteger logTrn) {
        this.logTrn = logTrn;
    }

    public String getLogDescripcion() {
        return logDescripcion;
    }

    public void setLogDescripcion(String logDescripcion) {
        this.logDescripcion = logDescripcion;
    }

    public String getLogOperacion() {
        return logOperacion;
    }

    public void setLogOperacion(String logOperacion) {
        this.logOperacion = logOperacion;
    }

    public BigInteger getLogEntidadId() {
        return logEntidadId;
    }

    public void setLogEntidadId(BigInteger logEntidadId) {
        this.logEntidadId = logEntidadId;
    }

    public BigInteger getLogEntidadTipo() {
        return logEntidadTipo;
    }

    public void setLogEntidadTipo(BigInteger logEntidadTipo) {
        this.logEntidadTipo = logEntidadTipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logId != null ? logId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LogAuditoria)) {
            return false;
        }
        LogAuditoria other = (LogAuditoria) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gob.mined.siap2.entities.data.impl.LogAuditoria[ logId=" + logId + " ]";
    }
    
}
