/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoCuentaBancaria;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "gesId", scope = SsGesPresEs.class)
public class SsGesPresEs implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long gesId;

    private String gesCod;

    private String gesNombre;

    private String gesDescripcion;

    private String gesProyecto;

    private String gesSubactividad;

    private String gesCategoria;

    private String gesConvenio;
    
    private SgTipoCuentaBancaria tipoCuentaBancaria;
    
    private SsTipoTransferencia tipoTransferencia;

    private SsCategoriaPresupuestoEscolar gesCategoriaComponente;

    private Boolean gesHabilitado;

    private Integer gesVersion;

    public SsGesPresEs() {
    }

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public Long getGesId() {
        return gesId;
    }

    public String getGesCod() {
        return gesCod;
    }

    public String getGesNombre() {
        return gesNombre;
    }

    public String getGesDescripcion() {
        return gesDescripcion;
    }

    public String getGesProyecto() {
        return gesProyecto;
    }

    public String getGesSubactividad() {
        return gesSubactividad;
    }

    public String getGesCategoria() {
        return gesCategoria;
    }

    public String getGesConvenio() {
        return gesConvenio;
    }

    public Boolean getGesHabilitado() {
        return gesHabilitado;
    }

    public Integer getGesVersion() {
        return gesVersion;
    }

    public void setGesId(Long gesId) {
        this.gesId = gesId;
    }

    public void setGesCod(String gesCod) {
        this.gesCod = gesCod;
    }

    public void setGesNombre(String gesNombre) {
        this.gesNombre = gesNombre;
    }

    public void setGesDescripcion(String gesDescripcion) {
        this.gesDescripcion = gesDescripcion;
    }

    public void setGesProyecto(String gesProyecto) {
        this.gesProyecto = gesProyecto;
    }

    public void setGesSubactividad(String gesSubactividad) {
        this.gesSubactividad = gesSubactividad;
    }

    public void setGesCategoria(String gesCategoria) {
        this.gesCategoria = gesCategoria;
    }

    public void setGesConvenio(String gesConvenio) {
        this.gesConvenio = gesConvenio;
    }

    public void setGesHabilitado(Boolean gesHabilitado) {
        this.gesHabilitado = gesHabilitado;
    }

    public SsCategoriaPresupuestoEscolar getGesCategoriaComponente() {
        return gesCategoriaComponente;
    }

    public void setGesCategoriaComponente(SsCategoriaPresupuestoEscolar gesCategoriaComponente) {
        this.gesCategoriaComponente = gesCategoriaComponente;
    }

    public void setGesVersion(Integer gesVersion) {
        this.gesVersion = gesVersion;
    }

    public SsTipoTransferencia getTipoTransferencia() {
        return tipoTransferencia;
    }

    public void setTipoTransferencia(SsTipoTransferencia tipoTransferencia) {
        this.tipoTransferencia = tipoTransferencia;
    }

    public SgTipoCuentaBancaria getTipoCuentaBancaria() {
        return tipoCuentaBancaria;
    }

    public void setTipoCuentaBancaria(SgTipoCuentaBancaria tipoCuentaBancaria) {
        this.tipoCuentaBancaria = tipoCuentaBancaria;
    }
    
    

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gesId != null ? gesId.hashCode() : 0);
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
        final SsGesPresEs other = (SsGesPresEs) obj;
        if (!Objects.equals(this.gesId, other.gesId)) {
            return false;
        }
        return true;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "com.sofis.entidades.SsGesPresEs[ gesId=" + gesId + " ]";
    }
    // </editor-fold>
}
