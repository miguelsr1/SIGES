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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCargoOAE;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSexo;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_persona_organismo_administracion", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "poaPk", scope = SgPersonaOrganismoAdministracion.class)
@Audited
public class SgPersonaOrganismoAdministracion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "poa_pk", nullable = false)
    private Long poaPk;


    @Size(max = 255)
    @Column(name = "poa_nombre", length = 255)
    @AtributoNormalizable
    private String poaNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "poa_nombre_busqueda", length = 255)
    private String poaNombreBusqueda;
    
    @Column(name = "poa_dui", length = 20)
    @Size(max = 20)
    private String poaDui;
    
    @Column(name = "poa_ocupacion")
    private String poaOcupacion;
    
    @Column(name = "poa_domicilio")
    private String poaDomicilio;

    @Column(name = "poa_habilitado")
    @AtributoHabilitado
    private Boolean poaHabilitado;

    @Column(name = "poa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime poaUltModFecha;

    @Size(max = 45)
    @Column(name = "poa_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String poaUltModUsuario;

    @Column(name = "poa_version")
    @Version
    private Integer poaVersion;
    
    @JoinColumn(name = "poa_organismo_administrativo_escolar", referencedColumnName = "oae_pk")
    @ManyToOne
    private SgOrganismoAdministracionEscolar poaOrganismoAdministrativoEscolar;
    
    @JoinColumn(name = "poa_cargo", referencedColumnName = "coa_pk")
    @ManyToOne
    private SgCargoOAE poaCargo;

    @Column(name = "poa_prerregistro")
    private Boolean poaPrerregistro;
    
    @JoinColumn(name = "poa_persona_fk")
    @ManyToOne
    private SgPersona poaPersona;

    @Size(max=20)
    @Column(name = "poa_nit", length = 20)
    private String poaNit;

    @Size(max=100)
    @Column(name = "poa_correo", length = 100)
    private String poaCorreo;

    @Size(max=10)
    @Column(name = "poa_telefono", length = 10)
    private String poaTelefono;
    
    @JoinColumn(name = "poa_sexo")
    @ManyToOne
    private SgSexo poaSexo;

    @Column(name = "poa_desde")
    private LocalDate poaDesde;

    @Column(name = "poa_hasta")
    private LocalDate poaHasta;

    @Transient
    private SgPersonaOrganismoAdministracion miembroReemplazar;
    
    @Column(name = "poa_per_reemplazo")
    private Long miembroReemplazo;
    
    @Column(name = "poa_dui_expedido_en")
    private String poaDuiExpedidoEn;
    
    @Column(name = "poa_fecha_expedicion_dui")
    private LocalDate poaFechaExpedicionDUI;
    
    @Transient
    private Boolean noHabilitadoParaRemplazar;
    
    
    
    public SgPersonaOrganismoAdministracion() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.poaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.poaNombre);
    }

    public Long getPoaPk() {
        return poaPk;
    }

    public void setPoaPk(Long poaPk) {
        this.poaPk = poaPk;
    }

    public String getPoaNombre() {
        return poaNombre;
    }

    public void setPoaNombre(String poaNombre) {
        this.poaNombre = poaNombre;
    }

    public String getPoaNombreBusqueda() {
        return poaNombreBusqueda;
    }

    public void setPoaNombreBusqueda(String poaNombreBusqueda) {
        this.poaNombreBusqueda = poaNombreBusqueda;
    }

    public Boolean getPoaHabilitado() {
        return poaHabilitado;
    }

    public void setPoaHabilitado(Boolean poaHabilitado) {
        this.poaHabilitado = poaHabilitado;
    }

    public LocalDateTime getPoaUltModFecha() {
        return poaUltModFecha;
    }

    public void setPoaUltModFecha(LocalDateTime poaUltModFecha) {
        this.poaUltModFecha = poaUltModFecha;
    }

    public String getPoaUltModUsuario() {
        return poaUltModUsuario;
    }

    public void setPoaUltModUsuario(String poaUltModUsuario) {
        this.poaUltModUsuario = poaUltModUsuario;
    }

    public Integer getPoaVersion() {
        return poaVersion;
    }

    public void setPoaVersion(Integer poaVersion) {
        this.poaVersion = poaVersion;
    }

    public String getPoaDui() {
        return poaDui;
    }

    public void setPoaDui(String poaDui) {
        this.poaDui = poaDui;
    }

    public String getPoaOcupacion() {
        return poaOcupacion;
    }

    public void setPoaOcupacion(String poaOcupacion) {
        this.poaOcupacion = poaOcupacion;
    }

    public String getPoaDomicilio() {
        return poaDomicilio;
    }

    public void setPoaDomicilio(String poaDomicilio) {
        this.poaDomicilio = poaDomicilio;
    }

    public SgCargoOAE getPoaCargo() {
        return poaCargo;
    }

    public void setPoaCargo(SgCargoOAE poaCargo) {
        this.poaCargo = poaCargo;
    }

    public SgOrganismoAdministracionEscolar getPoaOrganismoAdministrativoEscolar() {
        return poaOrganismoAdministrativoEscolar;
    }

    public void setPoaOrganismoAdministrativoEscolar(SgOrganismoAdministracionEscolar poaOrganismoAdministrativoEscolar) {
        this.poaOrganismoAdministrativoEscolar = poaOrganismoAdministrativoEscolar;
    }

    public Boolean getPoaPrerregistro() {
        return poaPrerregistro;
    }

    public void setPoaPrerregistro(Boolean poaPrerregistro) {
        this.poaPrerregistro = poaPrerregistro;
    }

    public SgPersona getPoaPersona() {
        return poaPersona;
    }

    public void setPoaPersona(SgPersona poaPersona) {
        this.poaPersona = poaPersona;
    }

    public String getPoaNit() {
        return poaNit;
    }

    public void setPoaNit(String poaNit) {
        this.poaNit = poaNit;
    }

    public String getPoaCorreo() {
        return poaCorreo;
    }

    public void setPoaCorreo(String poaCorreo) {
        this.poaCorreo = poaCorreo;
    }

    public String getPoaTelefono() {
        return poaTelefono;
    }

    public void setPoaTelefono(String poaTelefono) {
        this.poaTelefono = poaTelefono;
    }

    public LocalDate getPoaDesde() {
        return poaDesde;
    }

    public void setPoaDesde(LocalDate poaDesde) {
        this.poaDesde = poaDesde;
    }

    public LocalDate getPoaHasta() {
        return poaHasta;
    }

    public void setPoaHasta(LocalDate poaHasta) {
        this.poaHasta = poaHasta;
    }

    public SgSexo getPoaSexo() {
        return poaSexo;
    }

    public void setPoaSexo(SgSexo poaSexo) {
        this.poaSexo = poaSexo;
    }

    public SgPersonaOrganismoAdministracion getMiembroReemplazar() {
        return miembroReemplazar;
    }

    public void setMiembroReemplazar(SgPersonaOrganismoAdministracion miembroReemplazar) {
        this.miembroReemplazar = miembroReemplazar;
    }

    public Long getMiembroReemplazo() {
        return miembroReemplazo;
    }

    public void setMiembroReemplazo(Long miembroReemplazo) {
        this.miembroReemplazo = miembroReemplazo;
    }

    public String getPoaDuiExpedidoEn() {
        return poaDuiExpedidoEn;
    }

    public void setPoaDuiExpedidoEn(String poaDuiExpedidoEn) {
        this.poaDuiExpedidoEn = poaDuiExpedidoEn;
    }

    public LocalDate getPoaFechaExpedicionDUI() {
        return poaFechaExpedicionDUI;
    }

    public void setPoaFechaExpedicionDUI(LocalDate poaFechaExpedicionDUI) {
        this.poaFechaExpedicionDUI = poaFechaExpedicionDUI;
    }

    public Boolean getNoHabilitadoParaRemplazar() {
        return noHabilitadoParaRemplazar;
    }

    public void setNoHabilitadoParaRemplazar(Boolean noHabilitadoParaRemplazar) {
        this.noHabilitadoParaRemplazar = noHabilitadoParaRemplazar;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.poaPk);
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
        final SgPersonaOrganismoAdministracion other = (SgPersonaOrganismoAdministracion) obj;
        if (!Objects.equals(this.poaPk, other.poaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgPersonaOrganismoAdministracion{" + "poaPk=" + poaPk + ", poaHabilitado=" + poaHabilitado + ", poaUltModFecha=" + poaUltModFecha + ", poaUltModUsuario=" + poaUltModUsuario + ", poaVersion=" + poaVersion + '}';
    }
    
    

}
