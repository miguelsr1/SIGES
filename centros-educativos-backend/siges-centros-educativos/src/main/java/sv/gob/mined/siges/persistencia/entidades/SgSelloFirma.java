/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDate;
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
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoRepresentante;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_sellos_firmas", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sfiPk", scope = SgSelloFirma.class)
@Audited
public class SgSelloFirma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sfi_pk", nullable = false)
    private Long sfiPk;
    

    @Column(name = "sfi_comentario")
    @Size(max = 500)
    private String sfiComentario;
    

    @Column(name = "sfi_fecha_desde")
    private LocalDate sfiFechaDesde;


    @Column(name = "sfi_fecha_hasta")
    private LocalDate sfiFechaHasta;


    @Column(name = "sfi_aclaracion_firma")
    @Size(max = 255)
    private String sfiAclaracionFirma;


    @Column(name = "sfi_numero_documento")
    @Size(max = 255)
    private String sfiNumeroDocumento;


    @Column(name = "sfi_fecha_documento")
    private LocalDate sfiFechaDocumento;


    @Column(name = "sfi_institucion")
    @Size(max = 255)
    private String sfiInstitucion;

    @JoinColumn(name = "sfi_sede_fk")
    @ManyToOne(optional = false)
    private SgSedeLite sfiSede;
    
    @JoinColumn(name = "sfi_tipo_representante_fk")
    @ManyToOne
    private SgTipoRepresentante sfiTipoRepresentante;
    
    
    @JoinColumn(name = "sfi_persona_fk")
    @ManyToOne(optional = false)
    private SgPersonaLite sfiPersona;


    @JoinColumn(name = "sfi_firma_sello_archivo_fk")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private SgArchivo sfiFirmaSello;


    @Column(name = "sfi_habilitado")
    @AtributoHabilitado
    private Boolean sfiHabilitado;
    

    @Column(name = "sfi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime sfiUltModFecha;
    

    @Size(max = 45)
    @Column(name = "sfi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String sfiUltModUsuario;

    @Column(name = "sfi_version")
    @Version
    private Integer sfiVersion;

    public SgSelloFirma() {
    }

 

    public Long getSfiPk() {
        return sfiPk;
    }

    public void setSfiPk(Long sfiPk) {
        this.sfiPk = sfiPk;
    }



    public Boolean getSfiHabilitado() {
        return sfiHabilitado;
    }

    public void setSfiHabilitado(Boolean sfiHabilitado) {
        this.sfiHabilitado = sfiHabilitado;
    }

    public String getSfiComentario() {
        return sfiComentario;
    }

    public void setSfiComentario(String sfiComentario) {
        this.sfiComentario = sfiComentario;
    }

    public LocalDate getSfiFechaDesde() {
        return sfiFechaDesde;
    }

    public void setSfiFechaDesde(LocalDate sfiFechaDesde) {
        this.sfiFechaDesde = sfiFechaDesde;
    }

    public LocalDate getSfiFechaHasta() {
        return sfiFechaHasta;
    }

    public void setSfiFechaHasta(LocalDate sfiFechaHasta) {
        this.sfiFechaHasta = sfiFechaHasta;
    }

    public String getSfiAclaracionFirma() {
        return sfiAclaracionFirma;
    }

    public void setSfiAclaracionFirma(String sfiAclaracionFirma) {
        this.sfiAclaracionFirma = sfiAclaracionFirma;
    }

    public String getSfiNumeroDocumento() {
        return sfiNumeroDocumento;
    }

    public void setSfiNumeroDocumento(String sfiNumeroDocumento) {
        this.sfiNumeroDocumento = sfiNumeroDocumento;
    }

    public LocalDate getSfiFechaDocumento() {
        return sfiFechaDocumento;
    }

    public void setSfiFechaDocumento(LocalDate sfiFechaDocumento) {
        this.sfiFechaDocumento = sfiFechaDocumento;
    }

    public String getSfiInstitucion() {
        return sfiInstitucion;
    }

    public void setSfiInstitucion(String sfiInstitucion) {
        this.sfiInstitucion = sfiInstitucion;
    }

    public SgSedeLite getSfiSede() {
        return sfiSede;
    }

    public void setSfiSede(SgSedeLite sfiSede) {
        this.sfiSede = sfiSede;
    }
 
    public SgTipoRepresentante getSfiTipoRepresentante() {
        return sfiTipoRepresentante;
    }

    public void setSfiTipoRepresentante(SgTipoRepresentante sfiTipoRepresentante) {
        this.sfiTipoRepresentante = sfiTipoRepresentante;
    }

    public SgPersonaLite getSfiPersona() {
        return sfiPersona;
    }

    public void setSfiPersona(SgPersonaLite sfiPersona) {
        this.sfiPersona = sfiPersona;
    }

    public SgArchivo getSfiFirmaSello() {
        return sfiFirmaSello;
    }

    public void setSfiFirmaSello(SgArchivo sfiFirmaSello) {
        this.sfiFirmaSello = sfiFirmaSello;
    }

    public LocalDateTime getSfiUltModFecha() {
        return sfiUltModFecha;
    }

    public void setSfiUltModFecha(LocalDateTime sfiUltModFecha) {
        this.sfiUltModFecha = sfiUltModFecha;
    }

    public String getSfiUltModUsuario() {
        return sfiUltModUsuario;
    }

    public void setSfiUltModUsuario(String sfiUltModUsuario) {
        this.sfiUltModUsuario = sfiUltModUsuario;
    }

    public Integer getSfiVersion() {
        return sfiVersion;
    }

    public void setSfiVersion(Integer sfiVersion) {
        this.sfiVersion = sfiVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.sfiPk);
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
        final SgSelloFirma other = (SgSelloFirma) obj;
        if (!Objects.equals(this.sfiPk, other.sfiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgSelloFirma{" + "sfiPk=" + sfiPk + ", sfiHabilitado=" + sfiHabilitado + ", sfiUltModFecha=" + sfiUltModFecha + ", sfiUltModUsuario=" + sfiUltModUsuario + ", sfiVersion=" + sfiVersion + '}';
    }
    
    

}
