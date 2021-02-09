/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import java.io.Serializable;
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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "SS_PLANTILLAS_CORREO", catalog = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SsPlantillasCorreo.findAll", query = "SELECT s FROM SsPlantillasCorreo s"),
    @NamedQuery(name = "SsPlantillasCorreo.findByPlcId", query = "SELECT s FROM SsPlantillasCorreo s WHERE s.plcId = :plcId"),
    @NamedQuery(name = "SsPlantillasCorreo.findByPlcCodigo", query = "SELECT s FROM SsPlantillasCorreo s WHERE s.plcCodigo = :plcCodigo"),
    @NamedQuery(name = "SsPlantillasCorreo.findByPlcDescripcion", query = "SELECT s FROM SsPlantillasCorreo s WHERE s.plcDescripcion = :plcDescripcion"),
    @NamedQuery(name = "SsPlantillasCorreo.findByPlcContenido", query = "SELECT s FROM SsPlantillasCorreo s WHERE s.plcContenido = :plcContenido"),
    @NamedQuery(name = "SsPlantillasCorreo.findByPlcUltusuario", query = "SELECT s FROM SsPlantillasCorreo s WHERE s.plcUltusuario = :plcUltusuario"),
    @NamedQuery(name = "SsPlantillasCorreo.findByPlcUltmod", query = "SELECT s FROM SsPlantillasCorreo s WHERE s.plcUltmod = :plcUltmod"),
    @NamedQuery(name = "SsPlantillasCorreo.findByPlcVersion", query = "SELECT s FROM SsPlantillasCorreo s WHERE s.plcVersion = :plcVersion")})
public class SsPlantillasCorreo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PLC_ID", nullable = false, precision = 38, scale = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_plantillas_correo_generator")
    @SequenceGenerator(name = "seq_plantillas_correo_generator", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_plantillas_correo", allocationSize = 1)
    private Integer plcId;
    @Size(max = 50)
    @Column(name = "PLC_CODIGO", length = 50)
    private String plcCodigo;
    @Size(max = 1000)
    @Column(name = "PLC_DESCRIPCION", length = 1000)
    private String plcDescripcion;
    @Size(max = 4000)
    @Column(name = "PLC_CONTENIDO", length = 4000)
    private String plcContenido;
    @Size(max = 45)
    @Column(name = "PLC_ULTUSUARIO", length = 45)
    private String plcUltusuario;
    @Column(name = "PLC_ULTMOD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date plcUltmod;
    @Version
    @Column(name = "PLC_VERSION")
    private Long plcVersion;

    public SsPlantillasCorreo() {
    }

    public SsPlantillasCorreo(Integer plcId) {
        this.plcId = plcId;
    }

    public Integer getPlcId() {
        return plcId;
    }

    public void setPlcId(Integer plcId) {
        this.plcId = plcId;
    }

    public String getPlcCodigo() {
        return plcCodigo;
    }

    public void setPlcCodigo(String plcCodigo) {
        this.plcCodigo = plcCodigo;
    }

    public String getPlcDescripcion() {
        return plcDescripcion;
    }

    public void setPlcDescripcion(String plcDescripcion) {
        this.plcDescripcion = plcDescripcion;
    }

    public String getPlcContenido() {
        return plcContenido;
    }

    public void setPlcContenido(String plcContenido) {
        this.plcContenido = plcContenido;
    }

    public String getPlcUltusuario() {
        return plcUltusuario;
    }

    public void setPlcUltusuario(String plcUltusuario) {
        this.plcUltusuario = plcUltusuario;
    }

    public Date getPlcUltmod() {
        return plcUltmod;
    }

    public void setPlcUltmod(Date plcUltmod) {
        this.plcUltmod = plcUltmod;
    }

    public Long getPlcVersion() {
        return plcVersion;
    }

    public void setPlcVersion(Long plcVersion) {
        this.plcVersion = plcVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plcId != null ? plcId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SsPlantillasCorreo)) {
            return false;
        }
        SsPlantillasCorreo other = (SsPlantillasCorreo) object;
        if ((this.plcId == null && other.plcId != null) || (this.plcId != null && !this.plcId.equals(other.plcId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gob.mined.siap2.entities.data.impl.SsPlantillasCorreo[ plcId=" + plcId + " ]";
    }
    
}
