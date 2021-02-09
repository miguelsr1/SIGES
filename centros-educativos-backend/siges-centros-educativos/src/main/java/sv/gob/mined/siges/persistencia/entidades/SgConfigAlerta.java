/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumRiesgoAlerta;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_config_alerta", schema = Constantes.SCHEMA_ALERTAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cnfPk", scope = SgConfigAlerta.class)
public class SgConfigAlerta implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cnf_pk")
    private Long cnfPk;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cnfCabezal", orphanRemoval = true, fetch = FetchType.EAGER)
    @NotAudited
    @Fetch(FetchMode.SUBSELECT)
    private List<SgConfigAlertaManifestacionViolencia> cnfAlertaManifestacionViolencia;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cnfCabezal", orphanRemoval = true, fetch = FetchType.EAGER)
    @NotAudited
    @Fetch(FetchMode.SUBSELECT)
    private List<SgConfigAlertaTrabajo> cnfAlertaTrabajo;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cnfCabezal", orphanRemoval = true, fetch = FetchType.EAGER)
    @NotAudited
    @Fetch(FetchMode.SUBSELECT)
    private List<SgConfigAlertaAsistencia> cnfAlertaAsistencia;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cnfCabezal", orphanRemoval = true, fetch = FetchType.EAGER)
    @NotAudited
    @Fetch(FetchMode.SUBSELECT)
    private List<SgConfigAlertaDesempenio> cnfAlertaDesempenio;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cnfCabezal", orphanRemoval = true, fetch = FetchType.EAGER)
    @NotAudited
    @Fetch(FetchMode.SUBSELECT)
    private List<SgConfigAlertaSobreedad> cnfAlertaSobreedad;
    
    @Column(name = "cnf_riesgo_madre")
    @Enumerated(EnumType.STRING)
    private EnumRiesgoAlerta cnfRiesgoMadre;
    
    @Column(name = "cnf_riesgo_embarazo")
    @Enumerated(EnumType.STRING)
    private EnumRiesgoAlerta cnfRiesgoEmbarazo;
    
    @Column(name = "cnf_riesgo_acompaniado")
    @Enumerated(EnumType.STRING)
    private EnumRiesgoAlerta cnfRiesgoAcompaniado;
    
    @Column(name = "cnf_riesgo_matrimonio")
    @Enumerated(EnumType.STRING)
    private EnumRiesgoAlerta cnfRiesgoMatrimonio;

    @Column(name = "cnf_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cnfUltModFecha;
    
    @Size(max = 45)
    @Column(name = "cnf_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cnfUltModUsuario;

    @Column(name = "cnf_version")
    @Version
    private Integer cnfVersion;

    public SgConfigAlerta() {
    }
    

    public Long getCnfPk() {
        return cnfPk;
    }

    public void setCnfPk(Long cnfPk) {
        this.cnfPk = cnfPk;
    }

    public List<SgConfigAlertaManifestacionViolencia> getCnfAlertaManifestacionViolencia() {
        return cnfAlertaManifestacionViolencia;
    }

    public void setCnfAlertaManifestacionViolencia(List<SgConfigAlertaManifestacionViolencia> cnfAlertaManifestacionViolencia) {
        this.cnfAlertaManifestacionViolencia = cnfAlertaManifestacionViolencia;
    }

    public LocalDateTime getCnfUltModFecha() {
        return cnfUltModFecha;
    }

    public void setCnfUltModFecha(LocalDateTime cnfUltModFecha) {
        this.cnfUltModFecha = cnfUltModFecha;
    }

    public String getCnfUltModUsuario() {
        return cnfUltModUsuario;
    }

    public void setCnfUltModUsuario(String cnfUltModUsuario) {
        this.cnfUltModUsuario = cnfUltModUsuario;
    }

    public Integer getCnfVersion() {
        return cnfVersion;
    }

    public void setCnfVersion(Integer cnfVersion) {
        this.cnfVersion = cnfVersion;
    }

    public List<SgConfigAlertaTrabajo> getCnfAlertaTrabajo() {
        return cnfAlertaTrabajo;
    }

    public void setCnfAlertaTrabajo(List<SgConfigAlertaTrabajo> cnfAlertaTrabajo) {
        this.cnfAlertaTrabajo = cnfAlertaTrabajo;
    }

    public EnumRiesgoAlerta getCnfRiesgoMadre() {
        return cnfRiesgoMadre;
    }

    public void setCnfRiesgoMadre(EnumRiesgoAlerta cnfRiesgoMadre) {
        this.cnfRiesgoMadre = cnfRiesgoMadre;
    }

    public EnumRiesgoAlerta getCnfRiesgoEmbarazo() {
        return cnfRiesgoEmbarazo;
    }

    public void setCnfRiesgoEmbarazo(EnumRiesgoAlerta cnfRiesgoEmbarazo) {
        this.cnfRiesgoEmbarazo = cnfRiesgoEmbarazo;
    }

    public EnumRiesgoAlerta getCnfRiesgoAcompaniado() {
        return cnfRiesgoAcompaniado;
    }

    public void setCnfRiesgoAcompaniado(EnumRiesgoAlerta cnfRiesgoAcompaniado) {
        this.cnfRiesgoAcompaniado = cnfRiesgoAcompaniado;
    }

    public EnumRiesgoAlerta getCnfRiesgoMatrimonio() {
        return cnfRiesgoMatrimonio;
    }

    public void setCnfRiesgoMatrimonio(EnumRiesgoAlerta cnfRiesgoMatrimonio) {
        this.cnfRiesgoMatrimonio = cnfRiesgoMatrimonio;
    }

    public List<SgConfigAlertaAsistencia> getCnfAlertaAsistencia() {
        return cnfAlertaAsistencia;
    }

    public void setCnfAlertaAsistencia(List<SgConfigAlertaAsistencia> cnfAlertaAsistencia) {
        this.cnfAlertaAsistencia = cnfAlertaAsistencia;
    }

    public List<SgConfigAlertaSobreedad> getCnfAlertaSobreedad() {
        return cnfAlertaSobreedad;
    }

    public void setCnfAlertaSobreedad(List<SgConfigAlertaSobreedad> cnfAlertaSobreedad) {
        this.cnfAlertaSobreedad = cnfAlertaSobreedad;
    }

    public List<SgConfigAlertaDesempenio> getCnfAlertaDesempenio() {
        return cnfAlertaDesempenio;
    }

    public void setCnfAlertaDesempenio(List<SgConfigAlertaDesempenio> cnfAlertaDesempenio) {
        this.cnfAlertaDesempenio = cnfAlertaDesempenio;
    }
     
}
