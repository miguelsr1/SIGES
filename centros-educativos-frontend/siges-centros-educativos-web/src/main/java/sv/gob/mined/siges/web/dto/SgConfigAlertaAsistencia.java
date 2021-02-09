/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cnfPk", scope = SgConfigAlertaAsistencia.class)
public class SgConfigAlertaAsistencia implements Serializable {
    
    private Long cnfPk;
    
    private SgConfigAlerta cnfCabezal;
    
    private SgCiclo cnfCategoria;
    
    private Integer cnfRiesgoMuyAlto;
    
    private Integer cnfRiesgoAlto;
    
    private Integer cnfRiesgoMedio;
    
    private LocalDateTime cnfUltModFecha;

    private String cnfUltModUsuario;
    
    private Integer cnfVersion;
    
    @JsonIgnore
    public String getCnfNombre(){
        return this.cnfCategoria.getCicNivel().getNivNombre() + " - " + this.cnfCategoria.getCicNombre();
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

    public SgCiclo getCnfCategoria() {
        return cnfCategoria;
    }

    public void setCnfCategoria(SgCiclo cnfCategoria) {
        this.cnfCategoria = cnfCategoria;
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

    
    public Integer getCnfRiesgoMuyAlto() {
        return cnfRiesgoMuyAlto;
    }

    public void setCnfRiesgoMuyAlto(Integer cnfRiesgoMuyAlto) {
        this.cnfRiesgoMuyAlto = cnfRiesgoMuyAlto;
    }

    public Integer getCnfRiesgoAlto() {
        return cnfRiesgoAlto;
    }

    public void setCnfRiesgoAlto(Integer cnfRiesgoAlto) {
        this.cnfRiesgoAlto = cnfRiesgoAlto;
    }

    public Integer getCnfRiesgoMedio() {
        return cnfRiesgoMedio;
    }

    public void setCnfRiesgoMedio(Integer cnfRiesgoMedio) {
        this.cnfRiesgoMedio = cnfRiesgoMedio;
    }
    
    
    
}
