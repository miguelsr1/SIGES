/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "taoPk", scope = SgTipoAcuerdo.class)
public class SgTipoAcuerdo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long taoPk;

    private String taoCodigo;

    private String taoNombre;
    
    private Boolean taoTramiteRevocatoriaSede;

    private Boolean taoTramiteCreacionSede;

    private Boolean taoTramiteNominacionSede;
    
    private Boolean taoTramiteCambioDomicilioSede;
    
    private Boolean taoTramiteAmpliacionServicios;
    
    private Boolean taoTramiteDisminucionServicios;

    private Boolean taoHabilitado;

    private LocalDateTime taoUltModFecha;

    private String taoUltModUsuario;

    private Integer taoVersion;
    
    
    public SgTipoAcuerdo() {
        this.taoHabilitado = Boolean.TRUE;
    }

    public Long getTaoPk() {
        return taoPk;
    }

    public void setTaoPk(Long taoPk) {
        this.taoPk = taoPk;
    }

    public String getTaoCodigo() {
        return taoCodigo;
    }

    public void setTaoCodigo(String taoCodigo) {
        this.taoCodigo = taoCodigo;
    }

    public String getTaoNombre() {
        return taoNombre;
    }

    public void setTaoNombre(String taoNombre) {
        this.taoNombre = taoNombre;
    }

    public LocalDateTime getTaoUltModFecha() {
        return taoUltModFecha;
    }

    public void setTaoUltModFecha(LocalDateTime taoUltModFecha) {
        this.taoUltModFecha = taoUltModFecha;
    }

    public String getTaoUltModUsuario() {
        return taoUltModUsuario;
    }

    public void setTaoUltModUsuario(String taoUltModUsuario) {
        this.taoUltModUsuario = taoUltModUsuario;
    }

    public Integer getTaoVersion() {
        return taoVersion;
    }

    public void setTaoVersion(Integer taoVersion) {
        this.taoVersion = taoVersion;
    }

    
     public Boolean getTaoHabilitado() {
        return taoHabilitado;
    }

    public void setTaoHabilitado(Boolean taoHabilitado) {
        this.taoHabilitado = taoHabilitado;
    }

    public Boolean getTaoTramiteRevocatoriaSede() {
        return taoTramiteRevocatoriaSede;
    }

    public void setTaoTramiteRevocatoriaSede(Boolean taoTramiteRevocatoriaSede) {
        this.taoTramiteRevocatoriaSede = taoTramiteRevocatoriaSede;
    }

    public Boolean getTaoTramiteCreacionSede() {
        return taoTramiteCreacionSede;
    }

    public void setTaoTramiteCreacionSede(Boolean taoTramiteCreacionSede) {
        this.taoTramiteCreacionSede = taoTramiteCreacionSede;
    }

    public Boolean getTaoTramiteNominacionSede() {
        return taoTramiteNominacionSede;
    }

    public void setTaoTramiteNominacionSede(Boolean taoTramiteNominacionSede) {
        this.taoTramiteNominacionSede = taoTramiteNominacionSede;
    }

    public Boolean getTaoTramiteCambioDomicilioSede() {
        return taoTramiteCambioDomicilioSede;
    }

    public void setTaoTramiteCambioDomicilioSede(Boolean taoTramiteCambioDomicilioSede) {
        this.taoTramiteCambioDomicilioSede = taoTramiteCambioDomicilioSede;
    }

    public Boolean getTaoTramiteAmpliacionServicios() {
        return taoTramiteAmpliacionServicios;
    }

    public void setTaoTramiteAmpliacionServicios(Boolean taoTramiteAmpliacionServicios) {
        this.taoTramiteAmpliacionServicios = taoTramiteAmpliacionServicios;
    }

    public Boolean getTaoTramiteDisminucionServicios() {
        return taoTramiteDisminucionServicios;
    }

    public void setTaoTramiteDisminucionServicios(Boolean taoTramiteDisminucionServicios) {
        this.taoTramiteDisminucionServicios = taoTramiteDisminucionServicios;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taoPk != null ? taoPk.hashCode() : 0);
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
        final SgTipoAcuerdo other = (SgTipoAcuerdo) obj;
        if (!Objects.equals(this.taoPk, other.taoPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgTipoAcuerdo[ taoPk=" + taoPk + " ]";
    }
    
}
