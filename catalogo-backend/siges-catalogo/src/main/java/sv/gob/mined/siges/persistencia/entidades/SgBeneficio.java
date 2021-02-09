/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumTipoBeneficio;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_beneficios",  schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bnfPk", scope = SgBeneficio.class)
@Audited
public class SgBeneficio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bnf_pk", nullable = false)
    private Long bnfPk;

    @JoinColumn(name = "bnf_proyecto_institucional_fk", referencedColumnName = "pin_pk")
    @ManyToOne(optional = false)
    private SgProyectoInstitucional bnfProyectoInstitucional;

    @Size(max = 100)
    @Column(name = "bnf_nombre", length = 100)
    private String bnfNombre;

    @Size(max = 255)
    @Column(name = "bnf_descripcion", length = 255)
    private String bnfDescripcion;

    @Size(max = 500)
    @Column(name = "bnf_objetivos", length = 500)
    private String bnfObjetivos;

    @Column(name = "bnf_tipo_beneficio")
    @Enumerated(EnumType.STRING)
    private EnumTipoBeneficio bnfTipoBeneficio;

    @Column(name = "bnf_cantidad_anual")
    private Integer bnfCantidadAnual;

    @Column(name = "bnf_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime bnfUltModFecha;

    @Size(max = 45)
    @Column(name = "bnf_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String bnfUltModUsuario;

    @Column(name = "bnf_version")
    @Version
    private Integer bnfVersion;

    public SgBeneficio() {
    }

    public Long getBnfPk() {
        return bnfPk;
    }

    public void setBnfPk(Long bnfPk) {
        this.bnfPk = bnfPk;
    }

    public SgProyectoInstitucional getBnfProyectoInstitucional() {
        return bnfProyectoInstitucional;
    }

    public void setBnfProyectoInstitucional(SgProyectoInstitucional bnfProyectoInstitucional) {
        this.bnfProyectoInstitucional = bnfProyectoInstitucional;
    }

    public String getBnfNombre() {
        return bnfNombre;
    }

    public void setBnfNombre(String bnfNombre) {
        this.bnfNombre = bnfNombre;
    }

    public String getBnfDescripcion() {
        return bnfDescripcion;
    }

    public void setBnfDescripcion(String bnfDescripcion) {
        this.bnfDescripcion = bnfDescripcion;
    }

    public String getBnfObjetivos() {
        return bnfObjetivos;
    }

    public void setBnfObjetivos(String bnfObjetivos) {
        this.bnfObjetivos = bnfObjetivos;
    }

    public EnumTipoBeneficio getBnfTipoBeneficio() {
        return bnfTipoBeneficio;
    }

    public void setBnfTipoBeneficio(EnumTipoBeneficio bnfTipoBeneficio) {
        this.bnfTipoBeneficio = bnfTipoBeneficio;
    }

    public Integer getBnfCantidadAnual() {
        return bnfCantidadAnual;
    }

    public void setBnfCantidadAnual(Integer bnfCantidadAnual) {
        this.bnfCantidadAnual = bnfCantidadAnual;
    }

    public LocalDateTime getBnfUltModFecha() {
        return bnfUltModFecha;
    }

    public void setBnfUltModFecha(LocalDateTime bnfUltModFecha) {
        this.bnfUltModFecha = bnfUltModFecha;
    }

    public String getBnfUltModUsuario() {
        return bnfUltModUsuario;
    }

    public void setBnfUltModUsuario(String bnfUltModUsuario) {
        this.bnfUltModUsuario = bnfUltModUsuario;
    }

    public Integer getBnfVersion() {
        return bnfVersion;
    }

    public void setBnfVersion(Integer bnfVersion) {
        this.bnfVersion = bnfVersion;
    }

    
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.bnfPk);
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
        final SgBeneficio other = (SgBeneficio) obj;
        if (!Objects.equals(this.bnfPk, other.bnfPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgBeneficio{" + "bnfPk=" + bnfPk + ", bnfNombre=" + bnfNombre + ", bnfUltModFecha=" + bnfUltModFecha + ", bnfUltModUsuario=" + bnfUltModUsuario + ", bnfVersion=" + bnfVersion + '}';
    }
    
    

}
