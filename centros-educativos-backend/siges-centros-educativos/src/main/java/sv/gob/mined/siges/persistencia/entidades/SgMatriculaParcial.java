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
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_matriculas_parciales", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "matPk", scope = SgMatriculaParcial.class)
public class SgMatriculaParcial implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mat_pk")
    private Long matPk;
    
    @Size(max = 100)
    @Column(name = "mat_est_primer_nombre")
    @AtributoNormalizable
    private String matEstPrimerNombre;
    
    @Size(max = 100)
    @Column(name = "mat_est_segundo_nombre")
    @AtributoNormalizable
    private String matEstSegundoNombre;
        
    @Size(max = 100)
    @Column(name = "mat_est_primer_apellido")
    @AtributoNormalizable
    private String matEstPrimerApellido;
    
    @Size(max = 100)
    @Column(name = "mat_est_segundo_apellido")
    @AtributoNormalizable
    private String matEstSegundoApellido;
    
    @Column(name = "mat_est_nie")
    private Long matEstNie;

    @Column(name = "mat_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime matUltModFecha;
    
    @Size(max = 45)
    @Column(name = "mat_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String matUltModUsuario;
    
    @Size(max = 45)
    @Column(name = "mat_creacion_usuario", length = 45)
    private String matCreacionUsuario;
    
    @Column(name = "mat_version")
    @Version
    private Integer matVersion;
    
    @Column(name = "mat_json")
    private String matJson;
    
    
    public SgMatriculaParcial() {
    }
    
 
    public SgMatriculaParcial(Long matPk) {
        this.matPk = matPk;
    }

    public Long getMatPk() {
        return matPk;
    }

    public void setMatPk(Long matPk) {
        this.matPk = matPk;
    }

    public String getMatEstPrimerNombre() {
        return matEstPrimerNombre;
    }

    public void setMatEstPrimerNombre(String matEstPrimerNombre) {
        this.matEstPrimerNombre = matEstPrimerNombre;
    }

    public String getMatEstSegundoNombre() {
        return matEstSegundoNombre;
    }

    public void setMatEstSegundoNombre(String matEstSegundoNombre) {
        this.matEstSegundoNombre = matEstSegundoNombre;
    }

    public String getMatEstPrimerApellido() {
        return matEstPrimerApellido;
    }

    public void setMatEstPrimerApellido(String matEstPrimerApellido) {
        this.matEstPrimerApellido = matEstPrimerApellido;
    }

    public String getMatEstSegundoApellido() {
        return matEstSegundoApellido;
    }

    public void setMatEstSegundoApellido(String matEstSegundoApellido) {
        this.matEstSegundoApellido = matEstSegundoApellido;
    }

    public Long getMatEstNie() {
        return matEstNie;
    }

    public void setMatEstNie(Long matEstNie) {
        this.matEstNie = matEstNie;
    }

    public String getMatJson() {
        return matJson;
    }

    public void setMatJson(String matJson) {
        this.matJson = matJson;
    }

    public LocalDateTime getMatUltModFecha() {
        return matUltModFecha;
    }

    public void setMatUltModFecha(LocalDateTime matUltModFecha) {
        this.matUltModFecha = matUltModFecha;
    }

    public String getMatUltModUsuario() {
        return matUltModUsuario;
    }

    public void setMatUltModUsuario(String matUltModUsuario) {
        this.matUltModUsuario = matUltModUsuario;
    }

    public Integer getMatVersion() {
        return matVersion;
    }

    public void setMatVersion(Integer matVersion) {
        this.matVersion = matVersion;
    }


    public String getMatCreacionUsuario() {
        return matCreacionUsuario;
    }

    public void setMatCreacionUsuario(String matCreacionUsuario) {
        this.matCreacionUsuario = matCreacionUsuario;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matPk != null ? matPk.hashCode() : 0);
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
        final SgMatriculaParcial other = (SgMatriculaParcial) obj;
        if (!Objects.equals(this.matPk, other.matPk)) {
            return false;
        }
        return true;
    }

   

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgMatriculaParcial[ matPk=" + matPk + " ]";
    }
    
}
