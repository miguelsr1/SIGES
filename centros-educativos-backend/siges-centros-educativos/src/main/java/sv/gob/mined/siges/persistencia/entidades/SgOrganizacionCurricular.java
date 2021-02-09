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
import java.util.Objects;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoDescripcion;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgPlantilla;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_organizaciones_curricular", uniqueConstraints = {
    @UniqueConstraint(name = "ocu_codigo_uk", columnNames = {"ocu_codigo"})
    ,
    @UniqueConstraint(name = "ocu_nombre_uk", columnNames = {"ocu_nombre"})}, schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ocuPk", scope = SgOrganizacionCurricular.class)
@Audited
public class SgOrganizacionCurricular implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ocu_pk")
    private Long ocuPk;

    @Size(max = 4)
    @Column(name = "ocu_codigo", length = 4)
    @AtributoCodigo
    private String ocuCodigo;

    @Size(max = 255)
    @Column(name = "ocu_nombre", length = 255)
    @AtributoNormalizable
    private String ocuNombre;
    
    @Size(max = 255)
    @Column(name = "ocu_nombre_busqueda", length = 255)
    @AtributoNombre
    private String ocuNombreBusqueda;

    @Size(max = 255)
    @Column(name = "ocu_descripcion", length = 255)
    @AtributoDescripcion
    private String ocuDescripcion;

    @Column(name = "ocu_habilitado")
    @AtributoHabilitado
    private Boolean ocuHabilitado;
    
    @Column(name = "ocu_aplica_alertas_tempranas")
    private Boolean ocuAplicaAlertasTempranas;

    @Column(name = "ocu_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ocuUltModFecha;

    @Size(max = 45)
    @Column(name = "ocu_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ocuUltModUsuario;

    @Column(name = "ocu_version")
    @Version
    private Integer ocuVersion;

    @OneToMany(mappedBy = "nivOrganizacionCurricular")
    private List<SgNivel> ocuNiveles;
    
    @OneToMany(mappedBy = "plaOrganizacionCurricular", fetch = FetchType.LAZY)
    private List<SgPlantilla> ocuPlantillas;

    public SgOrganizacionCurricular() {
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ocuNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ocuNombre);
    }

    public Long getOcuPk() {
        return ocuPk;
    }

    public void setOcuPk(Long ocuPk) {
        this.ocuPk = ocuPk;
    }

    public String getOcuCodigo() {
        return ocuCodigo;
    }

    public void setOcuCodigo(String ocuCodigo) {
        this.ocuCodigo = ocuCodigo;
    }

    public String getOcuNombre() {
        return ocuNombre;
    }

    public void setOcuNombre(String ocuNombre) {
        this.ocuNombre = ocuNombre;
    }

    public String getOcuNombreBusqueda() {
        return ocuNombreBusqueda;
    }

    public void setOcuNombreBusqueda(String ocuNombreBusqueda) {
        this.ocuNombreBusqueda = ocuNombreBusqueda;
    }    

    public String getOcuDescripcion() {
        return ocuDescripcion;
    }

    public void setOcuDescripcion(String ocuDescripcion) {
        this.ocuDescripcion = ocuDescripcion;
    }

    public Boolean getOcuHabilitado() {
        return ocuHabilitado;
    }

    public void setOcuHabilitado(Boolean ocuHabilitado) {
        this.ocuHabilitado = ocuHabilitado;
    }

    public LocalDateTime getOcuUltModFecha() {
        return ocuUltModFecha;
    }

    public void setOcuUltModFecha(LocalDateTime ocuUltModFecha) {
        this.ocuUltModFecha = ocuUltModFecha;
    }

    public String getOcuUltModUsuario() {
        return ocuUltModUsuario;
    }

    public void setOcuUltModUsuario(String ocuUltModUsuario) {
        this.ocuUltModUsuario = ocuUltModUsuario;
    }

    public List<SgNivel> getOcuNiveles() {
        return ocuNiveles;
    }

    public void setOcuNiveles(List<SgNivel> ocuNiveles) {
        this.ocuNiveles = ocuNiveles;
    }

    public Integer getOcuVersion() {
        return ocuVersion;
    }

    public void setOcuVersion(Integer ocuVersion) {
        this.ocuVersion = ocuVersion;
    }

    public Boolean getOcuAplicaAlertasTempranas() {
        return ocuAplicaAlertasTempranas;
    }

    public void setOcuAplicaAlertasTempranas(Boolean ocuAplicaAlertasTempranas) {
        this.ocuAplicaAlertasTempranas = ocuAplicaAlertasTempranas;
    }

    public List<SgPlantilla> getOcuPlantillas() {
        return ocuPlantillas;
    }

    public void setOcuPlantillas(List<SgPlantilla> ocuPlantillas) {
        this.ocuPlantillas = ocuPlantillas;
    }
 
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ocuPk != null ? ocuPk.hashCode() : 0);
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
        final SgOrganizacionCurricular other = (SgOrganizacionCurricular) obj;
        if (!Objects.equals(this.ocuPk, other.ocuPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgOrganizacionCurricular{" + "ocuPk=" + ocuPk + ", ocuCodigo=" + ocuCodigo + ", ocuNombre=" + ocuNombre + ", ocuDescripcion=" + ocuDescripcion + ", ocuHabilitado=" + ocuHabilitado + ", ocuUltModFecha=" + ocuUltModFecha + ", ocuUltModUsuario=" + ocuUltModUsuario + ", ocuVersion=" + ocuVersion + ", ocuNiveles=" + ocuNiveles + '}';
    }

}
