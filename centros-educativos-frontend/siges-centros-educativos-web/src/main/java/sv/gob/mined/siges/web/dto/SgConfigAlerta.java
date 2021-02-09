/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import sv.gob.mined.siges.web.enumerados.EnumRiesgoAlerta;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cnfPk", scope = SgConfigAlerta.class)
public class SgConfigAlerta implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cnfPk;

    private List<SgConfigAlertaManifestacionViolencia> cnfAlertaManifestacionViolencia;

    private List<SgConfigAlertaTrabajo> cnfAlertaTrabajo;
    
    private List<SgConfigAlertaAsistencia> cnfAlertaAsistencia;
    
    private List<SgConfigAlertaDesempenio> cnfAlertaDesempenio;
    
    private List<SgConfigAlertaSobreedad> cnfAlertaSobreedad;

    private EnumRiesgoAlerta cnfRiesgoMadre;

    private EnumRiesgoAlerta cnfRiesgoEmbarazo;

    private EnumRiesgoAlerta cnfRiesgoAcompaniado;

    private EnumRiesgoAlerta cnfRiesgoMatrimonio;

    private LocalDateTime cnfUltModFecha;

    private String cnfUltModUsuario;

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

    public List<SgConfigAlertaTrabajo> getCnfAlertaTrabajo() {
        return cnfAlertaTrabajo;
    }

    public void setCnfAlertaTrabajo(List<SgConfigAlertaTrabajo> cnfAlertaTrabajo) {
        this.cnfAlertaTrabajo = cnfAlertaTrabajo;
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
