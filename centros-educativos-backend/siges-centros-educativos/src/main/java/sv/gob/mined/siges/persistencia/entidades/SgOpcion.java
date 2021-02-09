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
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.persistencia.annotations.AtributoDescripcion;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSectorEconomico;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_opciones", uniqueConstraints = {
    @UniqueConstraint(name = "opc_codigo_uk", columnNames = {"opc_codigo"}),
    @UniqueConstraint(name = "opc_nombre_codigo_uk", columnNames = {"opc_nombre","opc_sector_economico"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "opcPk", scope = SgOpcion.class)
@Audited
public class SgOpcion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "opc_pk")
    private Long opcPk;
    
    @Size(max = 4)
    @Column(name = "opc_codigo",length = 4)
    @AtributoCodigo
    private String opcCodigo;
    
    @Size(max = 500)
    @Column(name = "opc_nombre",length = 500)
    @AtributoNormalizable
    private String opcNombre;

    @Size(max = 255)
    @Column(name = "opc_nombre_busqueda", length = 255)
    @AtributoNombre
    private String opcNombreBusqueda;

    
    @Size(max = 255)
    @Column(name = "opc_descripcion",length = 500)
    @AtributoDescripcion
    private String opcDescripcion;    
    
    
    @Column(name = "opc_habilitado")
    @AtributoHabilitado
    private Boolean opcHabilitado;
    
    @Column(name = "opc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime opcUltModFecha;
    
    @Size(max = 45)
    @Column(name = "opc_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String opcUltModUsuario;
    
    @Column(name = "opc_version")
    @Version
    private Integer opcVersion;
    
    @JoinColumn(name = "opc_sector_economico", referencedColumnName = "sec_pk",nullable = true)
    @ManyToOne
    private SgSectorEconomico opcSectorEconomico;
    
    @JoinColumn(name = "opc_modalidad_fk", referencedColumnName = "mod_pk")
    @ManyToOne(optional = false)
    private SgModalidad opcModalidad;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roeOpcion", orphanRemoval = true)
    @NotAudited
    private List<SgRelOpcionProgEd> opcRelacionOpcionProgramaEdu;

    public SgOpcion() {
    }

    public SgOpcion(Long opcPk, Integer opcVersion) {
        this.opcPk = opcPk;
        this.opcVersion = opcVersion;
    }
    
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.opcNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.opcNombre);
    }

    public Long getOpcPk() {
        return opcPk;
    }

    public void setOpcPk(Long opcPk) {
        this.opcPk = opcPk;
    }

    public String getOpcCodigo() {
        return opcCodigo;
    }

    public void setOpcCodigo(String opcCodigo) {
        this.opcCodigo = opcCodigo;
    }

    public String getOpcNombre() {
        return opcNombre;
    }

    public void setOpcNombre(String opcNombre) {
        this.opcNombre = opcNombre;
    }

    public String getOpcNombreBusqueda() {
        return opcNombreBusqueda;
    }

    public void setOpcNombreBusqueda(String opcNombreBusqueda) {
        this.opcNombreBusqueda = opcNombreBusqueda;
    }   

    public Boolean getOpcHabilitado() {
        return opcHabilitado;
    }

    public void setOpcHabilitado(Boolean opcHabilitado) {
        this.opcHabilitado = opcHabilitado;
    }

    public LocalDateTime getOpcUltModFecha() {
        return opcUltModFecha;
    }

    public void setOpcUltModFecha(LocalDateTime opcUltModFecha) {
        this.opcUltModFecha = opcUltModFecha;
    }

    public String getOpcUltModUsuario() {
        return opcUltModUsuario;
    }

    public void setOpcUltModUsuario(String opcUltModUsuario) {
        this.opcUltModUsuario = opcUltModUsuario;
    }

    public Integer getOpcVersion() {
        return opcVersion;
    }

    public void setOpcVersion(Integer opcVersion) {
        this.opcVersion = opcVersion;
    }

    public SgSectorEconomico getOpcSectorEconomico() {
        return opcSectorEconomico;
    }

    public void setOpcSectorEconomico(SgSectorEconomico opcSectorEconomico) {
        this.opcSectorEconomico = opcSectorEconomico;
    }
    
    public String getOpcDescripcion() {
        return opcDescripcion;
    }

    public void setOpcDescripcion(String opcDescripcion) {
        this.opcDescripcion = opcDescripcion;
    }

    public List<SgRelOpcionProgEd> getOpcRelacionOpcionProgramaEdu() {
        return opcRelacionOpcionProgramaEdu;
    }

    public void setOpcRelacionOpcionProgramaEdu(List<SgRelOpcionProgEd> opcRelacionOpcionProgramaEdu) {
        this.opcRelacionOpcionProgramaEdu = opcRelacionOpcionProgramaEdu;
    }

    public SgModalidad getOpcModalidad() {
        return opcModalidad;
    }

    public void setOpcModalidad(SgModalidad opcModalidad) {
        this.opcModalidad = opcModalidad;
    }
    
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (opcPk != null ? opcPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgOpcion)) {
            return false;
        }
        SgOpcion other = (SgOpcion) object;
        if ((this.opcPk == null && other.opcPk != null) || (this.opcPk != null && !this.opcPk.equals(other.opcPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesOpcion[ opcPk=" + opcPk + " ]";
    }
    
}
