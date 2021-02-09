/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class,property = "cabPk", scope = SgAfCategoriaBienes.class)
public class SgAfCategoriaBienes implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long cabPk;

    private String cabCodigo;

    private String cabNombre;

    private SgAfClasificacionBienes cabClasificacionBienes;
    
    private Integer cabVidaUtil;
    
    private BigDecimal cabValorMaximo;
    
    private Integer cabPorcentajeValorResidual;
    
    private Boolean cabHabilitado;
    
    private String cabPathCargar;
    
    private LocalDateTime cabUltModFecha;

    private String cabUltModUsuario;

    private Integer cabVersion;
    
    private List<SgAfSeccionCategoria> sgAfSeccionesCategoriaList;
    
    public SgAfCategoriaBienes() {   
        this.cabHabilitado = Boolean.TRUE;
        this.cabClasificacionBienes = new SgAfClasificacionBienes();
        this.sgAfSeccionesCategoriaList = new ArrayList();
    }

    @JsonIgnore
    public String getCabNombreCategoriaClasificacion() {
        String nombre = this.getCabNombre();
        if(cabClasificacionBienes != null) {
            nombre = nombre + " - " + cabClasificacionBienes.getCbiNombre();
        }
        return nombre;
    }
    
    public Integer getCabVidaUtil() {
        return cabVidaUtil;
    }

    public void setCabVidaUtil(Integer cabVidaUtil) {
        this.cabVidaUtil = cabVidaUtil;
    }

    public Long getCabPk() {
        return cabPk;
    }

    public void setCabPk(Long cabPk) {
        this.cabPk = cabPk;
    }

    public String getCabCodigo() {
        return cabCodigo;
    }

    public void setCabCodigo(String cabCodigo) {
        this.cabCodigo = cabCodigo;
    }

    public String getCabNombre() {
        return cabNombre;
    }

    public void setCabNombre(String cabNombre) {
        this.cabNombre = cabNombre;
    }

    public SgAfClasificacionBienes getCabClasificacionBienes() {
        return cabClasificacionBienes;
    }

    public void setCabClasificacionBienes(SgAfClasificacionBienes cabClasificacionBienes) {
        this.cabClasificacionBienes = cabClasificacionBienes;
    }

    public Boolean getCabHabilitado() {
        return cabHabilitado;
    }

    public void setCabHabilitado(Boolean cabHabilitado) {
        this.cabHabilitado = cabHabilitado;
    }

    public LocalDateTime getCabUltModFecha() {
        return cabUltModFecha;
    }

    public void setCabUltModFecha(LocalDateTime cabUltModFecha) {
        this.cabUltModFecha = cabUltModFecha;
    }

    public String getCabUltModUsuario() {
        return cabUltModUsuario;
    }

    public void setCabUltModUsuario(String cabUltModUsuario) {
        this.cabUltModUsuario = cabUltModUsuario;
    }

    public Integer getCabVersion() {
        return cabVersion;
    }

    public void setCabVersion(Integer cabVersion) {
        this.cabVersion = cabVersion;
    }

    public Integer getCabPorcentajeValorResidual() {
        return cabPorcentajeValorResidual;
    }

    public void setCabPorcentajeValorResidual(Integer cabPorcentajeValorResidual) {
        this.cabPorcentajeValorResidual = cabPorcentajeValorResidual;
    }

    public String getCabPathCargar() {
        return cabPathCargar;
    }

    public void setCabPathCargar(String cabPathCargar) {
        this.cabPathCargar = cabPathCargar;
    }

    public List<SgAfSeccionCategoria> getSgAfSeccionesCategoriaList() {
        return sgAfSeccionesCategoriaList;
    }

    public void setSgAfSeccionesCategoriaList(List<SgAfSeccionCategoria> sgAfSeccionesCategoriaList) {
        this.sgAfSeccionesCategoriaList = sgAfSeccionesCategoriaList;
    }

    public BigDecimal getCabValorMaximo() {
        return cabValorMaximo;
    }

    public void setCabValorMaximo(BigDecimal cabValorMaximo) {
        this.cabValorMaximo = cabValorMaximo;
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.cabPk);
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
        final SgAfCategoriaBienes other = (SgAfCategoriaBienes) obj;
        if (!Objects.equals(this.cabPk, other.cabPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgCategoriaBienes[ cbiPk=" + cabPk + " ]";
    }
    
}
