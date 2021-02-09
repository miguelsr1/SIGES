/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.centroseducativos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

@Entity
@Table(name = "sg_niveles", uniqueConstraints = {
    @UniqueConstraint(name = "niv_codigo_uk", columnNames = {"niv_codigo"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited(targetAuditMode = NOT_AUDITED)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "nivPk", scope = SgNivel.class) //Es el único que debe quedar
public class SgNivel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "niv_pk")
    private Long nivPk;
    
    @Size(max = 4)
    @Column(name = "niv_codigo",length = 4)
    @AtributoCodigo
    private String nivCodigo;
    
    @Size(max = 255)
    @Column(name = "niv_nombre",length = 255)
    @AtributoNombre
    private String nivNombre;
    
    @Column(name = "niv_orden")
    private Integer nivOrden;
    
    @Column(name = "niv_obligatorio")
    private Boolean nivObligatorio;
    
    @Column(name = "niv_habilitado")
    @AtributoHabilitado
    private Boolean nivHabilitado;
    
    @Column(name = "niv_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime nivUltModFecha;
    
    @Size(max = 45)
    @Column(name = "niv_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String nivUltModUsuario;
    
    @Column(name = "niv_version")
    @Version
    private Integer nivVersion;
    

    public SgNivel() {
    }

    public Long getNivPk() {
        return nivPk;
    }

    public void setNivPk(Long nivPk) {
        this.nivPk = nivPk;
    }

    public String getNivCodigo() {
        return nivCodigo;
    }

    public void setNivCodigo(String nivCodigo) {
        this.nivCodigo = nivCodigo;
    }

    public String getNivNombre() {
        return nivNombre;
    }

    public void setNivNombre(String nivNombre) {
        this.nivNombre = nivNombre;
    }

    public Integer getNivOrden() {
        return nivOrden;
    }

    public void setNivOrden(Integer nivOrden) {
        this.nivOrden = nivOrden;
    }


    public Boolean getNivObligatorio() {
        return nivObligatorio;
    }

    public void setNivObligatorio(Boolean nivObligatorio) {
        this.nivObligatorio = nivObligatorio;
    }
    
    public Boolean getNivHabilitado() {
        return nivHabilitado;
    }

    public void setNivHabilitado(Boolean nivHabilitado) {
        this.nivHabilitado = nivHabilitado;
    }

    public LocalDateTime getNivUltModFecha() {
        return nivUltModFecha;
    }

    public void setNivUltModFecha(LocalDateTime nivUltModFecha) {
        this.nivUltModFecha = nivUltModFecha;
    }

    public String getNivUltModUsuario() {
        return nivUltModUsuario;
    }

    public void setNivUltModUsuario(String nivUltModUsuario) {
        this.nivUltModUsuario = nivUltModUsuario;
    }

    public Integer getNivVersion() {
        return nivVersion;
    }

    public void setNivVersion(Integer nivVersion) {
        this.nivVersion = nivVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nivPk != null ? nivPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgNivel)) {
            return false;
        }
        SgNivel other = (SgNivel) object;
        if ((this.nivPk == null && other.nivPk != null) || (this.nivPk != null && !this.nivPk.equals(other.nivPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgNivel{" + "nivPk=" + nivPk + '}';
    }

    

    


  
}
