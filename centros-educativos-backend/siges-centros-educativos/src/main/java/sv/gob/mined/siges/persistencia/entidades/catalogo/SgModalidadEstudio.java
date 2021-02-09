/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

@Entity
@Table(name = "sg_modalidades_estudio", uniqueConstraints = {
    @UniqueConstraint(name = "mes_codigo_uk", columnNames = {"mes_codigo"})
    ,
    @UniqueConstraint(name = "mes_nombre_uk", columnNames = {"mes_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mesPk", scope = SgModalidadEstudio.class)
public class SgModalidadEstudio implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mes_pk")
    private Long mesPk;
    
    @Size(max = 45)
    @Column(name = "mes_codigo",length = 45)
    @AtributoCodigo
    private String mesCodigo;
    
    @Column(name = "mes_habilitado")
    @AtributoHabilitado
    private Boolean mesHabilitado;
    
    @Size(max = 255)
    @Column(name = "mes_nombre",length = 255)
    @AtributoNormalizable
    private String mesNombre;
    
    @Size(max = 255)
    @Column(name = "mes_nombre_busqueda",length = 255)
    @AtributoNombre
    private String mesNombreBusqueda;
    
    @Column(name = "mes_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mesUltModFecha;
    
    @Size(max = 45)
    @Column(name = "mes_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String mesUltModUsuario;
    
    @Column(name = "mes_version")
    @Version
    private Integer mesVersion;

    public SgModalidadEstudio() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.mesNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.mesNombre);
    }

    public SgModalidadEstudio(Long mesPk) {
        this.mesPk = mesPk;
    }

    public Long getMesPk() {
        return mesPk;
    }

    public void setMesPk(Long mesPk) {
        this.mesPk = mesPk;
    }

    public String getMesCodigo() {
        return mesCodigo;
    }

    public void setMesCodigo(String mesCodigo) {
        this.mesCodigo = mesCodigo;
    }

    public Boolean getMesHabilitado() {
        return mesHabilitado;
    }

    public void setMesHabilitado(Boolean mesHabilitado) {
        this.mesHabilitado = mesHabilitado;
    }

    public String getMesNombre() {
        return mesNombre;
    }

    public void setMesNombre(String mesNombre) {
        this.mesNombre = mesNombre;
    }

    public String getMesNombreBusqueda() {
        return mesNombreBusqueda;
    }

    public void setMesNombreBusqueda(String mesNombreBusqueda) {
        this.mesNombreBusqueda = mesNombreBusqueda;
    }

    public LocalDateTime getMesUltModFecha() {
        return mesUltModFecha;
    }

    public void setMesUltModFecha(LocalDateTime mesUltModFecha) {
        this.mesUltModFecha = mesUltModFecha;
    }

    public String getMesUltModUsuario() {
        return mesUltModUsuario;
    }

    public void setMesUltModUsuario(String mesUltModUsuario) {
        this.mesUltModUsuario = mesUltModUsuario;
    }

    public Integer getMesVersion() {
        return mesVersion;
    }

    public void setMesVersion(Integer mesVersion) {
        this.mesVersion = mesVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mesPk != null ? mesPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgModalidadEstudio)) {
            return false;
        }
        SgModalidadEstudio other = (SgModalidadEstudio) object;
        if ((this.mesPk == null && other.mesPk != null) || (this.mesPk != null && !this.mesPk.equals(other.mesPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgModalidadEstudio[ mesPk=" + mesPk + " ]";
    }
    
}
