/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAsociacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgBeneficio;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgProyectoInstitucional;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;


@Entity
@Table(name = "sg_proyectos_institucionales_sede", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "proPk", scope = SgProyectoInstitucionalSede.class)
@Audited
public class SgProyectoInstitucionalSede implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pro_pk")
    private Long proPk;
    
    @JoinColumn(name = "pro_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede proSede;
    
    @JoinColumn(name = "pro_proyecto_institucional_fk", referencedColumnName = "pin_pk")
    @ManyToOne
    private SgProyectoInstitucional proProyectoInstitucional;
    
    @Column(name = "pro_fecha_otorgado")
    private LocalDate proFechaOtorgado;
    
    @Size(max = 500)
    @Column(name = "pro_observaciones")
    private String proObservaciones;
    
    @JoinColumn(name = "pro_beneficio_fk", referencedColumnName = "bnf_pk")
    @ManyToOne
    private SgBeneficio proBeneficio;
    
    @JoinColumn(name = "pro_asociacion_fk", referencedColumnName = "aso_pk")
    @ManyToOne
    private SgAsociacion proAsociacion;
    
    @Column(name = "pro_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime proUltModFecha;
    
    @Size(max = 45)
    @Column(name = "pro_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String proUltModUsuario;
    
    @Column(name = "pro_version")
    @Version
    private Integer proVersion;
    
    @OneToMany(mappedBy = "pieProyectoInstitucional")
    @NotAudited
    private List<SgProyectoInstEstudiante> proyectoInstEstudiante;

    public SgProyectoInstitucionalSede() {
    }

    public SgProyectoInstitucionalSede(Long proPk) {
        this.proPk = proPk;
    }

    public Long getProPk() {
        return proPk;
    }

    public void setProPk(Long proPk) {
        this.proPk = proPk;
    }

    public SgSede getProSede() {
        return proSede;
    }

    public void setProSede(SgSede proSede) {
        this.proSede = proSede;
    }

    public SgProyectoInstitucional getProProyectoInstitucional() {
        return proProyectoInstitucional;
    }

    public void setProProyectoInstitucional(SgProyectoInstitucional proProyectoInstitucional) {
        this.proProyectoInstitucional = proProyectoInstitucional;
    }

    public LocalDate getProFechaOtorgado() {
        return proFechaOtorgado;
    }

    public void setProFechaOtorgado(LocalDate proFechaOtorgado) {
        this.proFechaOtorgado = proFechaOtorgado;
    }

    public String getProObservaciones() {
        return proObservaciones;
    }

    public void setProObservaciones(String proObservaciones) {
        this.proObservaciones = proObservaciones;
    }

    public SgBeneficio getProBeneficio() {
        return proBeneficio;
    }

    public void setProBeneficio(SgBeneficio proBeneficio) {
        this.proBeneficio = proBeneficio;
    }

    public SgAsociacion getProAsociacion() {
        return proAsociacion;
    }

    public void setProAsociacion(SgAsociacion proAsociacion) {
        this.proAsociacion = proAsociacion;
    }
    

    public LocalDateTime getProUltModFecha() {
        return proUltModFecha;
    }

    public void setProUltModFecha(LocalDateTime proUltModFecha) {
        this.proUltModFecha = proUltModFecha;
    }

    public String getProUltModUsuario() {
        return proUltModUsuario;
    }

    public void setProUltModUsuario(String proUltModUsuario) {
        this.proUltModUsuario = proUltModUsuario;
    }

    public Integer getProVersion() {
        return proVersion;
    }

    public void setProVersion(Integer proVersion) {
        this.proVersion = proVersion;
    }

    public List<SgProyectoInstEstudiante> getProyectoInstEstudiante() {
        return proyectoInstEstudiante;
    }

    public void setProyectoInstEstudiante(List<SgProyectoInstEstudiante> proyectoInstEstudiante) {
        this.proyectoInstEstudiante = proyectoInstEstudiante;
    }


    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (proPk != null ? proPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgProyectoInstitucionalSede)) {
            return false;
        }
        SgProyectoInstitucionalSede other = (SgProyectoInstitucionalSede) object;
        if ((this.proPk == null && other.proPk != null) || (this.proPk != null && !this.proPk.equals(other.proPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgProyectoInstitucionalSede[ proPk=" + proPk + " ]";
    }
    
}
