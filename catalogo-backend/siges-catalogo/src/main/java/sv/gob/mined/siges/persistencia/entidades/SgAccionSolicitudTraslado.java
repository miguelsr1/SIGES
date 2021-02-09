/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudTraslado;

@Entity
@Table(name = "sg_acciones", uniqueConstraints = {
    @UniqueConstraint(name = "acc_campos_uk", columnNames = {"acc_num", "acc_estado_origen", "acc_estado_destino", "acc_nombre_accion"})}, 
        schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "accPk", scope = SgAccionSolicitudTraslado.class) 
public class SgAccionSolicitudTraslado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "acc_pk")
    private Long accPk;
    
    @Column(name = "acc_num")
    private Integer accNum;
    
    
    @Column(name = "acc_estado_origen")
    @Enumerated(EnumType.STRING)
    private EnumEstadoSolicitudTraslado accEstadoOrigen;
    
    @Column(name = "acc_estado_destino")
    @Enumerated(EnumType.STRING)
    private EnumEstadoSolicitudTraslado accEstadoDestino;
    
    @Size(max = 45)
    @Column(name = "acc_nombre_accion", length = 45)
    private String accNombreAccion;
    
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

    public SgAccionSolicitudTraslado() {
    }

    public SgAccionSolicitudTraslado(Long accPk) {
        this.accPk = accPk;
    }

    public Long getAccPk() {
        return accPk;
    }

    public void setAccPk(Long accPk) {
        this.accPk = accPk;
    }

    public Integer getAccNum() {
        return accNum;
    }

    public void setAccNum(Integer accNum) {
        this.accNum = accNum;
    }

    public EnumEstadoSolicitudTraslado getAccEstadoOrigen() {
        return accEstadoOrigen;
    }

    public void setAccEstadoOrigen(EnumEstadoSolicitudTraslado accEstadoOrigen) {
        this.accEstadoOrigen = accEstadoOrigen;
    }

    public EnumEstadoSolicitudTraslado getAccEstadoDestino() {
        return accEstadoDestino;
    }

    public void setAccEstadoDestino(EnumEstadoSolicitudTraslado accEstadoDestino) {
        this.accEstadoDestino = accEstadoDestino;
    }

    public String getAccNombreAccion() {
        return accNombreAccion;
    }

    public void setAccNombreAccion(String accNombreAccion) {
        this.accNombreAccion = accNombreAccion;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accPk != null ? accPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAccionSolicitudTraslado)) {
            return false;
        }
        SgAccionSolicitudTraslado other = (SgAccionSolicitudTraslado) object;
        if ((this.accPk == null && other.accPk != null) || (this.accPk != null && !this.accPk.equals(other.accPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAccion[ accPk=" + accPk + " ]";
    }
    
}
