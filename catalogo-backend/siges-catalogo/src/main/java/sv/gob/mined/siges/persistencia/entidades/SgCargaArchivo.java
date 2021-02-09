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
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumTipoArchivoCarga;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_cargas_archivos", uniqueConstraints = {
    @UniqueConstraint(name = "cga_codigo_uk", columnNames = {"cga_codigo"})
    ,
    @UniqueConstraint(name = "cga_nombre_uk", columnNames = {"cga_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cgaPk", scope = SgCargaArchivo.class)
@Audited
public class SgCargaArchivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cga_pk", nullable = false)
    private Long cgaPk;

    @Size(max = 45)
    @Column(name = "cga_codigo", length = 45)
    @AtributoCodigo
    private String cgaCodigo;

    @Size(max = 255)
    @Column(name = "cga_nombre", length = 255)
    @AtributoNormalizable
    private String cgaNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "cga_nombre_busqueda", length = 255)
    private String cgaNombreBusqueda;
    
    @Column(name = "cga_tipo_archivo")
    @Enumerated(EnumType.STRING)
    private EnumTipoArchivoCarga cgaTipoArchivo;

    @Size(max = 100)
    @AtributoNombre
    @Column(name = "cga_formatos_permitidos", length = 100)
    private String cgaFormatosPermitidos;
    
    @Column(name = "cga_ancho")
    private Integer cgaAncho;
    
    @Column(name = "cga_alto")
    private Integer cgaAlto;

    @Column(name = "cga_habilitado")
    @AtributoHabilitado
    private Boolean cgaHabilitado;

    @Column(name = "cga_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cgaUltModFecha;

    @Size(max = 45)
    @Column(name = "cga_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cgaUltModUsuario;

    @Column(name = "cga_version")
    @Version
    private Integer cgaVersion;

    public SgCargaArchivo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cgaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cgaNombre);
    }
    

    //<editor-fold defaultstate="collapsed" desc="GET & SET">

    public Long getCgaPk() {
        return cgaPk;
    }

    public void setCgaPk(Long cgaPk) {
        this.cgaPk = cgaPk;
    }

    public String getCgaCodigo() {
        return cgaCodigo;
    }

    public void setCgaCodigo(String cgaCodigo) {
        this.cgaCodigo = cgaCodigo;
    }

    public String getCgaNombre() {
        return cgaNombre;
    }

    public void setCgaNombre(String cgaNombre) {
        this.cgaNombre = cgaNombre;
    }

    public String getCgaNombreBusqueda() {
        return cgaNombreBusqueda;
    }

    public void setCgaNombreBusqueda(String cgaNombreBusqueda) {
        this.cgaNombreBusqueda = cgaNombreBusqueda;
    }

    public Boolean getCgaHabilitado() {
        return cgaHabilitado;
    }

    public void setCgaHabilitado(Boolean cgaHabilitado) {
        this.cgaHabilitado = cgaHabilitado;
    }

    public LocalDateTime getCgaUltModFecha() {
        return cgaUltModFecha;
    }

    public void setCgaUltModFecha(LocalDateTime cgaUltModFecha) {
        this.cgaUltModFecha = cgaUltModFecha;
    }

    public String getCgaUltModUsuario() {
        return cgaUltModUsuario;
    }

    public void setCgaUltModUsuario(String cgaUltModUsuario) {
        this.cgaUltModUsuario = cgaUltModUsuario;
    }

    public Integer getCgaVersion() {
        return cgaVersion;
    }

    public void setCgaVersion(Integer cgaVersion) {
        this.cgaVersion = cgaVersion;
    }

    public EnumTipoArchivoCarga getCgaTipoArchivo() {
        return cgaTipoArchivo;
    }

    public void setCgaTipoArchivo(EnumTipoArchivoCarga cgaTipoArchivo) {
        this.cgaTipoArchivo = cgaTipoArchivo;
    }

    public String getCgaFormatosPermitidos() {
        return cgaFormatosPermitidos;
    }

    public void setCgaFormatosPermitidos(String cgaFormatosPermitidos) {
        this.cgaFormatosPermitidos = cgaFormatosPermitidos;
    }

    public Integer getCgaAncho() {
        return cgaAncho;
    }

    public void setCgaAncho(Integer cgaAncho) {
        this.cgaAncho = cgaAncho;
    }

    public Integer getCgaAlto() {
        return cgaAlto;
    }

    public void setCgaAlto(Integer cgaAlto) {
        this.cgaAlto = cgaAlto;
    }
    
    //</editor-fold>
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.cgaPk);
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
        final SgCargaArchivo other = (SgCargaArchivo) obj;
        if (!Objects.equals(this.cgaPk, other.cgaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCargaArchivo{" + "cgaPk=" + cgaPk + ", cgaCodigo=" + cgaCodigo + ", cgaNombre=" + cgaNombre + ", cgaNombreBusqueda=" + cgaNombreBusqueda + ", cgaHabilitado=" + cgaHabilitado + ", cgaUltModFecha=" + cgaUltModFecha + ", cgaUltModUsuario=" + cgaUltModUsuario + ", cgaVersion=" + cgaVersion + '}';
    }
    
    

}
