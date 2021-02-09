/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import sv.gob.mined.siges.web.dto.catalogo.SgCategoriaViolencia;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoManifestacion;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cnfPk", scope = SgConfigAlertaManifestacionViolencia.class)
public class SgConfigAlertaManifestacionViolencia implements Serializable {
    
    private Long cnfPk;
    
    private SgConfigAlerta cnfCabezal;
    
    private SgCategoriaViolencia cnfCategoria;
    
    private List<SgTipoManifestacion> cnfRiesgoMuyAlto;
    
    private List<SgTipoManifestacion> cnfRiesgoAlto;
    
    private List<SgTipoManifestacion> cnfRiesgoMedio;
    
    private LocalDateTime cnfUltModFecha;

    private String cnfUltModUsuario;
    
    private Integer cnfVersion;

    public SgConfigAlertaManifestacionViolencia() {
        this.cnfRiesgoMedio = new ArrayList<>();
        this.cnfRiesgoAlto = new ArrayList<>();
        this.cnfRiesgoMuyAlto = new ArrayList<>(); 
    }

    public Long getCnfPk() {
        return cnfPk;
    }

    public void setCnfPk(Long cnfPk) {
        this.cnfPk = cnfPk;
    }

    public SgConfigAlerta getCnfCabezal() {
        return cnfCabezal;
    }

    public void setCnfCabezal(SgConfigAlerta cnfCabezal) {
        this.cnfCabezal = cnfCabezal;
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
    
    

    public SgCategoriaViolencia getCnfCategoria() {
        return cnfCategoria;
    }

    public void setCnfCategoria(SgCategoriaViolencia cnfCategoria) {
        this.cnfCategoria = cnfCategoria;
    }

    public List<SgTipoManifestacion> getCnfRiesgoMuyAlto() {
        return cnfRiesgoMuyAlto;
    }

    public void setCnfRiesgoMuyAlto(List<SgTipoManifestacion> cnfRiesgoMuyAlto) {
        this.cnfRiesgoMuyAlto = cnfRiesgoMuyAlto;
    }

    public List<SgTipoManifestacion> getCnfRiesgoAlto() {
        return cnfRiesgoAlto;
    }

    public void setCnfRiesgoAlto(List<SgTipoManifestacion> cnfRiesgoAlto) {
        this.cnfRiesgoAlto = cnfRiesgoAlto;
    }

    public List<SgTipoManifestacion> getCnfRiesgoMedio() {
        return cnfRiesgoMedio;
    }

    public void setCnfRiesgoMedio(List<SgTipoManifestacion> cnfRiesgoMedio) {
        this.cnfRiesgoMedio = cnfRiesgoMedio;
    }
    
}
