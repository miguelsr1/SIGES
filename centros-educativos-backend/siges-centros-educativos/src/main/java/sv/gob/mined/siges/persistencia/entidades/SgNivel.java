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
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
import java.util.List;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "sg_niveles", uniqueConstraints = {
    @UniqueConstraint(name = "niv_codigo_uk", columnNames = {"niv_codigo"}),
    @UniqueConstraint(name = "niv_nombre_orgcurricular_uk", columnNames = {"niv_nombre","niv_organizacion_curricular"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "nivPk", scope = SgNivel.class)
@Audited
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
    
    @Column(name = "niv_nombre_seccion_libre")
    private Boolean nivNombreSeccionLibre;
    
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
    
    @JoinColumn(name = "niv_organizacion_curricular", referencedColumnName = "ocu_pk")
    @ManyToOne(optional = false)
    private SgOrganizacionCurricular nivOrganizacionCurricular;

    @OneToMany(mappedBy = "cicNivel")
    private List<SgCiclo> nivCiclos;

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

    public SgOrganizacionCurricular getNivOrganizacionCurricular() {
        return nivOrganizacionCurricular;
    }

    public void setNivOrganizacionCurricular(SgOrganizacionCurricular nivOrganizacionCurricular) {
        this.nivOrganizacionCurricular = nivOrganizacionCurricular;
    }
    
    public List<SgCiclo> getNivCiclos() {
        return nivCiclos;
    }

    public void setNivCiclos(List<SgCiclo> nivCiclos) {
        this.nivCiclos = nivCiclos;
    }

    public Boolean getNivNombreSeccionLibre() {
        return nivNombreSeccionLibre;
    }

    public void setNivNombreSeccionLibre(Boolean nivNombreSeccionLibre) {
        this.nivNombreSeccionLibre = nivNombreSeccionLibre;
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
