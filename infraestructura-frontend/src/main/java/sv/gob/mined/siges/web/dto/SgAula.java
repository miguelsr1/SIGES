/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipologiaConstruccion;
import sv.gob.mined.siges.web.enumerados.EnumEstadoAula;
import sv.gob.mined.siges.web.enumerados.EnumTipoAula;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "aulPk", scope = SgAula.class)
public class SgAula implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long aulPk;

    private String aulCodigo;

    private String aulNombre;

    private Integer aulNivel;
  
    private String aulObservaciones;
    
    private LocalDateTime aulUltModFecha;
   
    private String aulUltModUsuario;
  
    private Integer aulVersion;    
  
    private SgEdificio aulEdificioFk;
    
    private List<SgRelAulaEspacio> aulRelAulaEspacio;
    
    private List<SgRelAulaServicio> aulRelAulaServicio;
    
    private EnumEstadoAula aulEstado;

    private EnumTipoAula aulTipo;

    private BigDecimal aulArea;

    private List<SgInfTipologiaConstruccion> aulTipologiaConstruccion;
    
    public SgAula() {
       aulTipologiaConstruccion=new ArrayList();
    }
    
    @JsonIgnore
    public String getTipologiaConstruccionAsString(){
        if (this.aulTipologiaConstruccion != null){
            return this.aulTipologiaConstruccion.stream().map(d -> d.getTicNombre()).collect(Collectors.joining(", "));
        }
        return null;
    }

    public Long getAulPk() {
        return aulPk;
    }

    public void setAulPk(Long aulPk) {
        this.aulPk = aulPk;
    }

    public String getAulCodigo() {
        return aulCodigo;
    }

    public void setAulCodigo(String aulCodigo) {
        this.aulCodigo = aulCodigo;
    }

    public String getAulNombre() {
        return aulNombre;
    }

    public void setAulNombre(String aulNombre) {
        this.aulNombre = aulNombre;
    }

    public LocalDateTime getAulUltModFecha() {
        return aulUltModFecha;
    }

    public void setAulUltModFecha(LocalDateTime aulUltModFecha) {
        this.aulUltModFecha = aulUltModFecha;
    }

    public String getAulUltModUsuario() {
        return aulUltModUsuario;
    }

    public void setAulUltModUsuario(String aulUltModUsuario) {
        this.aulUltModUsuario = aulUltModUsuario;
    }

    public Integer getAulVersion() {
        return aulVersion;
    }

    public void setAulVersion(Integer aulVersion) {
        this.aulVersion = aulVersion;
    }

    public Integer getAulNivel() {
        return aulNivel;
    }

    public void setAulNivel(Integer aulNivel) {
        this.aulNivel = aulNivel;
    }

    public String getAulObservaciones() {
        return aulObservaciones;
    }

    public void setAulObservaciones(String aulObservaciones) {
        this.aulObservaciones = aulObservaciones;
    }

    public SgEdificio getAulEdificioFk() {
        return aulEdificioFk;
    }

    public void setAulEdificioFk(SgEdificio aulEdificioFk) {
        this.aulEdificioFk = aulEdificioFk;
    }

    public List<SgRelAulaEspacio> getAulRelAulaEspacio() {
        return aulRelAulaEspacio;
    }

    public void setAulRelAulaEspacio(List<SgRelAulaEspacio> aulRelAulaEspacio) {
        this.aulRelAulaEspacio = aulRelAulaEspacio;
    }

    public List<SgRelAulaServicio> getAulRelAulaServicio() {
        return aulRelAulaServicio;
    }

    public void setAulRelAulaServicio(List<SgRelAulaServicio> aulRelAulaServicio) {
        this.aulRelAulaServicio = aulRelAulaServicio;
    }

    public EnumEstadoAula getAulEstado() {
        return aulEstado;
    }

    public void setAulEstado(EnumEstadoAula aulEstado) {
        this.aulEstado = aulEstado;
    }

    public EnumTipoAula getAulTipo() {
        return aulTipo;
    }

    public void setAulTipo(EnumTipoAula aulTipo) {
        this.aulTipo = aulTipo;
    }

    public BigDecimal getAulArea() {
        return aulArea;
    }

    public void setAulArea(BigDecimal aulArea) {
        this.aulArea = aulArea;
    }

    public List<SgInfTipologiaConstruccion> getAulTipologiaConstruccion() {
        return aulTipologiaConstruccion;
    }

    public void setAulTipologiaConstruccion(List<SgInfTipologiaConstruccion> aulTipologiaConstruccion) {
        this.aulTipologiaConstruccion = aulTipologiaConstruccion;
    }

    
   


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aulPk != null ? aulPk.hashCode() : 0);
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
        final SgAula other = (SgAula) obj;
        if (!Objects.equals(this.aulPk, other.aulPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgAula[ aulPk=" + aulPk + " ]";
    }
    
}
