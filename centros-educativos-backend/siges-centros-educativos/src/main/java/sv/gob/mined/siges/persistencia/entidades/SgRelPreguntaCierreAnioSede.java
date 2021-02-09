/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgPreguntaCierreAnio;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_rel_pregunta_cierre_anio_sede", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rpcPk", scope = SgRelPreguntaCierreAnioSede.class)
@Audited
public class SgRelPreguntaCierreAnioSede implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rpc_pk")
    private Long rpcPk;
    
    @JoinColumn(name = "rpc_cierre_anio_lectivo_sede_fk")
    @ManyToOne
    private SgCierreAnioLectivoSede rpcCierreAnioLectivoSede;
    
    @JoinColumn(name = "rpc_pregunta_cierre_anio_fk")
    @ManyToOne
    private SgPreguntaCierreAnio rpcPreguntaCierreAnio;
    
    @Column(name = "rpc_pregunta_validada")
    private Boolean rpcPreguntaValidada;
    
    @Column(name = "rpc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rpcUltModFecha;
    
    @Size(max = 45)
    @Column(name = "rpc_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String rpcUltModUsuario;
    
    @Column(name = "rpc_version")
    @Version
    private Integer rpcVersion;
 

    public SgRelPreguntaCierreAnioSede() {
    }

    public SgRelPreguntaCierreAnioSede(Long rpcPk) {
        this.rpcPk = rpcPk;
    }

    public Long getRpcPk() {
        return rpcPk;
    }

    public void setRpcPk(Long rpcPk) {
        this.rpcPk = rpcPk;
    }
   
    public LocalDateTime getRpcUltModFecha() {
        return rpcUltModFecha;
    }

    public void setRpcUltModFecha(LocalDateTime rpcUltModFecha) {
        this.rpcUltModFecha = rpcUltModFecha;
    }

    public String getRpcUltModUsuario() {
        return rpcUltModUsuario;
    }

    public void setRpcUltModUsuario(String rpcUltModUsuario) {
        this.rpcUltModUsuario = rpcUltModUsuario;
    }

    public Integer getRpcVersion() {
        return rpcVersion;
    }

    public void setRpcVersion(Integer rpcVersion) {
        this.rpcVersion = rpcVersion;
    }

    public SgCierreAnioLectivoSede getRpcCierreAnioLectivoSede() {
        return rpcCierreAnioLectivoSede;
    }

    public void setRpcCierreAnioLectivoSede(SgCierreAnioLectivoSede rpcCierreAnioLectivoSede) {
        this.rpcCierreAnioLectivoSede = rpcCierreAnioLectivoSede;
    }

    public SgPreguntaCierreAnio getRpcPreguntaCierreAnio() {
        return rpcPreguntaCierreAnio;
    }

    public void setRpcPreguntaCierreAnio(SgPreguntaCierreAnio rpcPreguntaCierreAnio) {
        this.rpcPreguntaCierreAnio = rpcPreguntaCierreAnio;
    }

    public Boolean getRpcPreguntaValidada() {
        return rpcPreguntaValidada;
    }

    public void setRpcPreguntaValidada(Boolean rpcPreguntaValidada) {
        this.rpcPreguntaValidada = rpcPreguntaValidada;
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
        final SgRelPreguntaCierreAnioSede other = (SgRelPreguntaCierreAnioSede) obj;
        if (!Objects.equals(this.rpcPk, other.rpcPk)) {
            return false;
        }
        return true;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rpcPk != null ? rpcPk.hashCode() : 0);
        return hash;
    }

   

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRelSedeServicioInfra[ rpcPk=" + rpcPk + " ]";
    }
    
}
