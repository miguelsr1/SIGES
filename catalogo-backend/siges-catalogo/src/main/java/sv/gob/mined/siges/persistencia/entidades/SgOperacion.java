/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;


@Entity
@Table(name = "sg_operaciones", uniqueConstraints = {
    @UniqueConstraint(name = "ope_codigo_uk", columnNames = {"ope_codigo"})}, 
        schema = Constantes.SCHEMA_SEGURIDAD)
@XmlRootElement
@Audited
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "opePk", scope = SgOperacion.class)

public class SgOperacion implements Serializable {

    private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ope_pk")
    private Long opePk;


    @Size(max = 10)
    @Column(name = "ope_codigo", length = 10)
    @AtributoCodigo
    private String opeCodigo;

    @Size(max = 255)
    @Column(name = "ope_nombre", length = 255)
    @AtributoNormalizable    
    private String opeNombre;

    @Column(name = "ope_habilitado")
    @AtributoHabilitado    
    private Boolean opeHabilitado;

    public SgOperacion() {
    }

    public Long getOpePk() {
        return opePk;
    }

    public void setOpePk(Long opePk) {
        this.opePk = opePk;
    }

    public String getOpeCodigo() {
        return opeCodigo;
    }

    public void setOpeCodigo(String opeCodigo) {
        this.opeCodigo = opeCodigo;
    }

    public String getOpeNombre() {
        return opeNombre;
    }

    public void setOpeNombre(String opeNombre) {
        this.opeNombre = opeNombre;
    }

    public Boolean getOpeHabilitado() {
        return opeHabilitado;
    }

    public void setOpeHabilitado(Boolean opeHabilitado) {
        this.opeHabilitado = opeHabilitado;
    }

    
    

    @Override
    public int hashCode() {
        return Objects.hashCode(this.opePk);
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
        final SgOperacion other = (SgOperacion) obj;
        if (!Objects.equals(this.opePk, other.opePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgOperacion{" + "opePk=" + opePk + ", opeNombre=" + opeNombre + ", opeCodigo=" + opeCodigo + ", opeHabilitado=" + opeHabilitado + '}';
    }
    
    
}
