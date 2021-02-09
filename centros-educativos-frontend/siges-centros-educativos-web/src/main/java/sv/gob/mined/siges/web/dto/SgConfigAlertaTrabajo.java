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
import sv.gob.mined.siges.web.dto.catalogo.SgTipoTrabajo;
import sv.gob.mined.siges.web.enumerados.EnumCategoriaAlertaTrabajo;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cnfPk", scope = SgConfigAlertaTrabajo.class)
public class SgConfigAlertaTrabajo implements Serializable {
    
    private Long cnfPk;
    
    private SgConfigAlerta cnfCabezal;
    
    private EnumCategoriaAlertaTrabajo cnfCategoria;
    
    private List<SgTipoTrabajo> cnfRiesgoMuyAlto;

    private List<SgTipoTrabajo> cnfRiesgoAlto;
    
    private List<SgTipoTrabajo> cnfRiesgoMedio;
    
    private LocalDateTime cnfUltModFecha;

    private String cnfUltModUsuario;
    
    private Integer cnfVersion;

    public SgConfigAlertaTrabajo() {
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
    
    

    public EnumCategoriaAlertaTrabajo getCnfCategoria() {
        return cnfCategoria;
    }

    public void setCnfCategoria(EnumCategoriaAlertaTrabajo cnfCategoria) {
        this.cnfCategoria = cnfCategoria;
    }

    public List<SgTipoTrabajo> getCnfRiesgoMuyAlto() {
        return cnfRiesgoMuyAlto;
    }

    public void setCnfRiesgoMuyAlto(List<SgTipoTrabajo> cnfRiesgoMuyAlto) {
        this.cnfRiesgoMuyAlto = cnfRiesgoMuyAlto;
    }

    public List<SgTipoTrabajo> getCnfRiesgoAlto() {
        return cnfRiesgoAlto;
    }

    public void setCnfRiesgoAlto(List<SgTipoTrabajo> cnfRiesgoAlto) {
        this.cnfRiesgoAlto = cnfRiesgoAlto;
    }

    public List<SgTipoTrabajo> getCnfRiesgoMedio() {
        return cnfRiesgoMedio;
    }

    public void setCnfRiesgoMedio(List<SgTipoTrabajo> cnfRiesgoMedio) {
        this.cnfRiesgoMedio = cnfRiesgoMedio;
    }

}
