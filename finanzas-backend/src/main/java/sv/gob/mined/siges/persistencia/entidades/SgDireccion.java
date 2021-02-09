/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
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
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCanton;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCaserio;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMunicipio;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoCalle;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgZona;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_direcciones", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dirPk", scope = SgDireccion.class)
@Audited
/**
 * Entidad correspondiente a las direcciones
 */
public class SgDireccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dir_pk", nullable = false)
    private Long dirPk;

    @Size(max = 500)
    @Column(name = "dir_direccion", length = 500)
    private String dirDireccion;

    @JoinColumn(name = "dir_departamento")
    @ManyToOne(optional = false)
    private SgDepartamento dirDepartamento;

    @JoinColumn(name = "dir_municipio")
    @ManyToOne
    private SgMunicipio dirMunicipio;

    @JoinColumn(name = "dir_canton")
    @ManyToOne
    private SgCanton dirCanton;

    @JoinColumn(name = "dir_caserio")
    @ManyToOne
    private SgCaserio dirCaserio;

    @Column(name = "dir_caserio_texto")
    private String dirCaserioTexto;

    @JoinColumn(name = "dir_tipo_calle_fk")
    @ManyToOne
    private SgTipoCalle dirTipoCalle;

    @JoinColumn(name = "dir_zona")
    @ManyToOne
    private SgZona dirZona;

    @Column(name = "dir_latitud")
    private Double dirLatitud;

    @Column(name = "dir_longitud")
    private Double dirLongitud;

    @Column(name = "dir_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dirUltModFecha;

    @Size(max = 45)
    @Column(name = "dir_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dirUltModUsuario;

    @Column(name = "dir_version")
    @Version
    private Integer dirVersion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sedDireccion")
    private List<SgSede> dirSede;

    public SgDireccion() {
    }

    public Long getDirPk() {
        return dirPk;
    }

    public void setDirPk(Long dirPk) {
        this.dirPk = dirPk;
    }

    public String getDirDireccion() {
        return dirDireccion;
    }

    public void setDirDireccion(String dirDireccion) {
        this.dirDireccion = dirDireccion;
    }

    public SgDepartamento getDirDepartamento() {
        return dirDepartamento;
    }

    public void setDirDepartamento(SgDepartamento dirDepartamento) {
        this.dirDepartamento = dirDepartamento;
    }

    public SgMunicipio getDirMunicipio() {
        return dirMunicipio;
    }

    public void setDirMunicipio(SgMunicipio dirMunicipio) {
        this.dirMunicipio = dirMunicipio;
    }

    public SgCanton getDirCanton() {
        return dirCanton;
    }

    public void setDirCanton(SgCanton dirCanton) {
        this.dirCanton = dirCanton;
    }

    public SgCaserio getDirCaserio() {
        return dirCaserio;
    }

    public void setDirCaserio(SgCaserio dirCaserio) {
        this.dirCaserio = dirCaserio;
    }

    public SgZona getDirZona() {
        return dirZona;
    }

    public void setDirZona(SgZona dirZona) {
        this.dirZona = dirZona;
    }

    public Double getDirLatitud() {
        return dirLatitud;
    }

    public void setDirLatitud(Double dirLatitud) {
        this.dirLatitud = dirLatitud;
    }

    public Double getDirLongitud() {
        return dirLongitud;
    }

    public void setDirLongitud(Double dirLongitud) {
        this.dirLongitud = dirLongitud;
    }

    public LocalDateTime getDirUltModFecha() {
        return dirUltModFecha;
    }

    public void setDirUltModFecha(LocalDateTime dirUltModFecha) {
        this.dirUltModFecha = dirUltModFecha;
    }

    public String getDirUltModUsuario() {
        return dirUltModUsuario;
    }

    public void setDirUltModUsuario(String dirUltModUsuario) {
        this.dirUltModUsuario = dirUltModUsuario;
    }

    public Integer getDirVersion() {
        return dirVersion;
    }

    public void setDirVersion(Integer dirVersion) {
        this.dirVersion = dirVersion;
    }

    public String getDirCaserioTexto() {
        return dirCaserioTexto;
    }

    public void setDirCaserioTexto(String dirCaserioTexto) {
        this.dirCaserioTexto = dirCaserioTexto;
    }

    public SgTipoCalle getDirTipoCalle() {
        return dirTipoCalle;
    }

    public void setDirTipoCalle(SgTipoCalle dirTipoCalle) {
        this.dirTipoCalle = dirTipoCalle;
    }

    public List<SgSede> getDirSede() {
        return dirSede;
    }

    public void setDirSede(List<SgSede> dirSede) {
        this.dirSede = dirSede;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.dirPk);
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
        final SgDireccion other = (SgDireccion) obj;
        if (!Objects.equals(this.dirPk, other.dirPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgDireccion{" + "dirPk=" + dirPk + ", dirDireccion=" + dirDireccion + ", dirDepartamento=" + dirDepartamento + ", dirMunicipio=" + dirMunicipio + ", dirCanton=" + dirCanton + ", dirCaserio=" + dirCaserio + ", dirZona=" + dirZona + ", dirLatitud=" + dirLatitud + ", dirLongitud=" + dirLongitud + ", dirUltModFecha=" + dirUltModFecha + ", dirUltModUsuario=" + dirUltModUsuario + ", dirVersion=" + dirVersion + '}';
    }

}
