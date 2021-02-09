/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ieoPk", scope = SgItemEvaluarOrganismo.class)
public class SgItemEvaluarOrganismo implements Serializable {
    
    private static final long serialVersionUID = 1L;
	
    private Long ieoPk;

    private String ieoCodigo;

    private String ieoNombre;
    
    private Integer ieoOrden;

    private LocalDateTime ieoUltModFecha;

    private String ieoUltModUsuario;

    private Integer ieoVersion;
    
    private SgTipoOrganismoAdministrativo ieoTipoOrganismo;
    
    
    public SgItemEvaluarOrganismo() {
        
    }

    public Long getIeoPk() {
        return ieoPk;
    }

    public void setIeoPk(Long ieoPk) {
        this.ieoPk = ieoPk;
    }

    public String getIeoCodigo() {
        return ieoCodigo;
    }

    public void setIeoCodigo(String ieoCodigo) {
        this.ieoCodigo = ieoCodigo;
    }

    public String getIeoNombre() {
        return ieoNombre;
    }

    public void setIeoNombre(String ieoNombre) {
        this.ieoNombre = ieoNombre;
    }

    public LocalDateTime getIeoUltModFecha() {
        return ieoUltModFecha;
    }

    public void setIeoUltModFecha(LocalDateTime ieoUltModFecha) {
        this.ieoUltModFecha = ieoUltModFecha;
    }

    public String getIeoUltModUsuario() {
        return ieoUltModUsuario;
    }

    public void setIeoUltModUsuario(String ieoUltModUsuario) {
        this.ieoUltModUsuario = ieoUltModUsuario;
    }

    public Integer getIeoVersion() {
        return ieoVersion;
    }

    public void setIeoVersion(Integer ieoVersion) {
        this.ieoVersion = ieoVersion;
    }

    public Integer getIeoOrden() {
        return ieoOrden;
    }

    public void setIeoOrden(Integer ieoOrden) {
        this.ieoOrden = ieoOrden;
    }

    public SgTipoOrganismoAdministrativo getIeoTipoOrganismo() {
        return ieoTipoOrganismo;
    }

    public void setIeoTipoOrganismo(SgTipoOrganismoAdministrativo ieoTipoOrganismo) {
        this.ieoTipoOrganismo = ieoTipoOrganismo;
    }
    


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ieoPk != null ? ieoPk.hashCode() : 0);
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
        final SgItemEvaluarOrganismo other = (SgItemEvaluarOrganismo) obj;
        if (!Objects.equals(this.ieoPk, other.ieoPk)) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "com.sofis.entidades.SgItemEvaluarOrganismo[ ieoPk=" + ieoPk + " ]";
    }
    
}
