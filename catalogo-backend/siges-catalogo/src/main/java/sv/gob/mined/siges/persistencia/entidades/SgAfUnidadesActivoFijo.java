/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_unidades_activo_fijo", uniqueConstraints = {
    @UniqueConstraint(name = "uaf_codigo_uk", columnNames = {"uaf_codigo"})
    ,
    @UniqueConstraint(name = "auf_nombre_uk", columnNames = {"uaf_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uafPk", scope = SgAfUnidadesActivoFijo.class)
public class SgAfUnidadesActivoFijo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "uaf_pk", nullable = false)
    private Long uafPk;

    @Size(max = 5)
    @Column(name = "uaf_codigo", length = 5)
    @AtributoCodigo
    private String uafCodigo;

    @Size(max = 255)
    @AtributoNormalizable
    @Column(name = "uaf_nombre", length = 255)
    private String uafNombre;
    
    @Size(max = 255)
    @AtributoNombre
    @Column(name = "uaf_nombre_busqueda", length = 255)
    private String uafNombreBusqueda;
    
    @Column(name = "uaf_habilitado")
    @AtributoHabilitado
    private Boolean uafHabilitado;

    @JoinColumn(name = "uaf_departamento_fk", referencedColumnName = "dep_pk")
    @ManyToOne(optional = false)
    private SgDepartamento uafDepartamento;

    @Size(max = 100)
    @Column(name = "uaf_responsable_af",  length = 100)
    private String uafResponsableAF;
    
    @Size(max = 100)
    @Column(name = "uaf_cargo_responsable_af",  length = 100)
    private String uafCargoResponsableAF;
    
    @NotAudited
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "cdeActivoFijoFk", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SgAfComisionDescargo> sgAfComisionDescargoList;
    
    @Column(name = "uaf_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime uafUltModFecha;

    @Size(max = 45)
    @Column(name = "uaf_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String uafUltModUsuario;

    @Column(name = "uaf_version")
    @Version
    private Integer uafVersion;

    public SgAfUnidadesActivoFijo() {
        this.sgAfComisionDescargoList = new ArrayList<SgAfComisionDescargo>();
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.uafNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.uafNombre);
    }
    
    public Long getUafPk() {
        return uafPk;
    }

    public void setUafPk(Long uafPk) {
        this.uafPk = uafPk;
    }

    public String getUafCodigo() {
        return uafCodigo;
    }

    public void setUafCodigo(String uafCodigo) {
        this.uafCodigo = uafCodigo;
    }

    public String getUafNombre() {
        return uafNombre;
    }

    public void setUafNombre(String uafNombre) {
        this.uafNombre = uafNombre;
    }

    public String getUafNombreBusqueda() {
        return uafNombreBusqueda;
    }

    public void setUafNombreBusqueda(String uafNombreBusqueda) {
        this.uafNombreBusqueda = uafNombreBusqueda;
    }

    public Boolean getUafHabilitado() {
        return uafHabilitado;
    }

    public void setUafHabilitado(Boolean uafHabilitado) {
        this.uafHabilitado = uafHabilitado;
    }

    public SgDepartamento getUafDepartamento() {
        return uafDepartamento;
    }

    public void setUafDepartamento(SgDepartamento uafDepartamento) {
        this.uafDepartamento = uafDepartamento;
    }

    public LocalDateTime getUafUltModFecha() {
        return uafUltModFecha;
    }

    public void setUafUltModFecha(LocalDateTime uafUltModFecha) {
        this.uafUltModFecha = uafUltModFecha;
    }

    public String getUafUltModUsuario() {
        return uafUltModUsuario;
    }

    public void setUafUltModUsuario(String uafUltModUsuario) {
        this.uafUltModUsuario = uafUltModUsuario;
    }

    public Integer getUafVersion() {
        return uafVersion;
    }

    public void setUafVersion(Integer uafVersion) {
        this.uafVersion = uafVersion;
    }

    public List<SgAfComisionDescargo> getSgAfComisionDescargoList() {
        return sgAfComisionDescargoList;
    }

    public void setSgAfComisionDescargoList(List<SgAfComisionDescargo> sgAfComisionDescargoList) {
         if(this.sgAfComisionDescargoList != null) {
            this.sgAfComisionDescargoList.clear();
            this.sgAfComisionDescargoList.addAll(sgAfComisionDescargoList); 
        }
    }

    public String getUafResponsableAF() {
        return uafResponsableAF;
    }

    public void setUafResponsableAF(String uafResponsableAF) {
        this.uafResponsableAF = uafResponsableAF;
    }

    public String getUafCargoResponsableAF() {
        return uafCargoResponsableAF;
    }

    public void setUafCargoResponsableAF(String uafCargoResponsableAF) {
        this.uafCargoResponsableAF = uafCargoResponsableAF;
    }

    
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfUnidadesActivoFijo{" + "uafPk=" + uafPk + '}';
    }
}