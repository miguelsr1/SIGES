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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCargoTitular;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_sello_firma_titular", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sftPk", scope = SgSelloFirmaTitular.class)
@Audited
public class SgSelloFirmaTitular implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sft_pk", nullable = false)
    private Long sftPk;


    @Size(max = 255)
    @Column(name = "sft_primer_nombre", length = 255)
    @AtributoNormalizable
    private String sftPrimerNombre;
    
    @Size(max = 255)
    @Column(name = "sft_segundo_nombre", length = 255)
    @AtributoNormalizable
    private String sftSegundoNombre;
    
    @Size(max = 255)
    @Column(name = "sft_primer_apellido", length = 255)
    @AtributoNormalizable
    private String sftPrimerApellido;
    
    @Size(max = 255)
    @Column(name = "sft_segundo_apellido", length = 255)
    @AtributoNormalizable
    private String sftSegundoApellido;
    
 
       
    @Column(name = "sft_fecha_desde")
    private LocalDate sftFechaDesde;


    @Column(name = "sft_fecha_hasta")
    private LocalDate sftFechaHasta;
    
    @Column(name = "sft_observaciones")
    @Size(max = 500)
    private String sftObservaciones;

    @Size(max = 500)
    @AtributoNombre
    @Column(name = "sft_nombre_busqueda", length = 500)
    private String sftNombreBusqueda;

    @Column(name = "sft_habilitado")
    @AtributoHabilitado
    private Boolean sftHabilitado;

    @Column(name = "sft_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime sftUltModFecha;

    @Size(max = 45)
    @Column(name = "sft_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String sftUltModUsuario;

    @Column(name = "sft_version")
    @Version
    private Integer sftVersion;
    
    
    @JoinColumn(name = "sft_firma_sello_archivo_fk")
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private SgArchivo sftFirmaSello;
        
    @JoinColumn(name = "sft_cargo_titular_fk")
    @ManyToOne
    private SgCargoTitular sftCargoTitular;

    public SgSelloFirmaTitular() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.sftNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.getPerNombreCompleto());   
    
    }
    
    public String getPerNombreCompleto(){
        StringBuilder s = new StringBuilder();
        if (this.sftPrimerNombre != null){
            s.append(this.sftPrimerNombre).append(" ");
        }
        if (this.sftSegundoNombre != null){
            s.append(this.sftSegundoNombre).append(" ");
        }
        if (this.sftPrimerApellido != null){
            s.append(this.sftPrimerApellido).append(" ");
        }
        if (this.sftSegundoApellido != null){
            s.append(this.sftSegundoApellido).append(" ");
        }
        
 
        return SofisStringUtils.normalizarString(s.toString());
    }
 

    public Long getSftPk() {
        return sftPk;
    }

    public void setSftPk(Long sftPk) {
        this.sftPk = sftPk;
    }

    public String getSftPrimerNombre() {
        return sftPrimerNombre;
    }

    public void setSftPrimerNombre(String sftPrimerNombre) {
        this.sftPrimerNombre = sftPrimerNombre;
    }

    public String getSftSegundoNombre() {
        return sftSegundoNombre;
    }

    public void setSftSegundoNombre(String sftSegundoNombre) {
        this.sftSegundoNombre = sftSegundoNombre;
    }

    public String getSftPrimerApellido() {
        return sftPrimerApellido;
    }

    public void setSftPrimerApellido(String sftPrimerApellido) {
        this.sftPrimerApellido = sftPrimerApellido;
    }

    public String getSftSegundoApellido() {
        return sftSegundoApellido;
    }

    public void setSftSegundoApellido(String sftSegundoApellido) {
        this.sftSegundoApellido = sftSegundoApellido;
    }

    public LocalDate getSftFechaDesde() {
        return sftFechaDesde;
    }

    public void setSftFechaDesde(LocalDate sftFechaDesde) {
        this.sftFechaDesde = sftFechaDesde;
    }

    public LocalDate getSftFechaHasta() {
        return sftFechaHasta;
    }

    public void setSftFechaHasta(LocalDate sftFechaHasta) {
        this.sftFechaHasta = sftFechaHasta;
    }

    public String getSftObservaciones() {
        return sftObservaciones;
    }

    public void setSftObservaciones(String sftObservaciones) {
        this.sftObservaciones = sftObservaciones;
    }

    public SgArchivo getSftFirmaSello() {
        return sftFirmaSello;
    }

    public void setSftFirmaSello(SgArchivo sftFirmaSello) {
        this.sftFirmaSello = sftFirmaSello;
    }

    public SgCargoTitular getSftCargoTitular() {
        return sftCargoTitular;
    }

    public void setSftCargoTitular(SgCargoTitular sftCargoTitular) {
        this.sftCargoTitular = sftCargoTitular;
    }


    public String getSftNombreBusqueda() {
        return sftNombreBusqueda;
    }

    public void setSftNombreBusqueda(String sftNombreBusqueda) {
        this.sftNombreBusqueda = sftNombreBusqueda;
    }

    public Boolean getSftHabilitado() {
        return sftHabilitado;
    }

    public void setSftHabilitado(Boolean sftHabilitado) {
        this.sftHabilitado = sftHabilitado;
    }

    public LocalDateTime getSftUltModFecha() {
        return sftUltModFecha;
    }

    public void setSftUltModFecha(LocalDateTime sftUltModFecha) {
        this.sftUltModFecha = sftUltModFecha;
    }

    public String getSftUltModUsuario() {
        return sftUltModUsuario;
    }

    public void setSftUltModUsuario(String sftUltModUsuario) {
        this.sftUltModUsuario = sftUltModUsuario;
    }

    public Integer getSftVersion() {
        return sftVersion;
    }

    public void setSftVersion(Integer sftVersion) {
        this.sftVersion = sftVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.sftPk);
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
        final SgSelloFirmaTitular other = (SgSelloFirmaTitular) obj;
        if (!Objects.equals(this.sftPk, other.sftPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgSelloFirmaTitular{" + "sftPk=" + sftPk +", sftNombreBusqueda=" + sftNombreBusqueda + ", sftHabilitado=" + sftHabilitado + ", sftUltModFecha=" + sftUltModFecha + ", sftUltModUsuario=" + sftUltModUsuario + ", sftVersion=" + sftVersion + '}';
    }
    
    

}
