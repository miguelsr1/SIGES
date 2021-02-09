/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgIdentificacionOAE;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_oae_identificacion_oae", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rioPk", scope = SgRelOAEIdentificacionOAE.class)
@Audited
public class SgRelOAEIdentificacionOAE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rio_pk", nullable = false)
    private Long rioPk;

    @JoinColumn(name = "rio_oae_fk", referencedColumnName = "oae_pk")
    @ManyToOne
    private SgOrganismoAdministracionEscolar rioOrganismoAdministracionEscolarFk;

    @JoinColumn(name = "rio_identificacion_oae_fk", referencedColumnName = "ioa_pk")
    @ManyToOne
    private SgIdentificacionOAE rioIdentificacionOAEfk;
   
    @Column(name = "rio_valor")
    private String rioValor;

    @Column(name = "rio_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rioUltModFecha;

    @Size(max = 45)
    @Column(name = "rio_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rioUltModUsuario;

    @Column(name = "rio_version")
    @Version
    private Integer rioVersion;

    public SgRelOAEIdentificacionOAE() {
    }


    public Long getRioPk() {
        return rioPk;
    }

    public void setRioPk(Long rioPk) {
        this.rioPk = rioPk;
    }

    public LocalDateTime getRioUltModFecha() {
        return rioUltModFecha;
    }

    public void setRioUltModFecha(LocalDateTime rioUltModFecha) {
        this.rioUltModFecha = rioUltModFecha;
    }

    public String getRioUltModUsuario() {
        return rioUltModUsuario;
    }

    public void setRioUltModUsuario(String rioUltModUsuario) {
        this.rioUltModUsuario = rioUltModUsuario;
    }

    public Integer getRioVersion() {
        return rioVersion;
    }

    public void setRioVersion(Integer rioVersion) {
        this.rioVersion = rioVersion;
    }

    public SgOrganismoAdministracionEscolar getRioOrganismoAdministracionEscolarFk() {
        return rioOrganismoAdministracionEscolarFk;
    }

    public void setRioOrganismoAdministracionEscolarFk(SgOrganismoAdministracionEscolar rioOrganismoAdministracionEscolarFk) {
        this.rioOrganismoAdministracionEscolarFk = rioOrganismoAdministracionEscolarFk;
    }
    

    public SgIdentificacionOAE getRioIdentificacionOAEfk() {
        return rioIdentificacionOAEfk;
    }

    public void setRioIdentificacionOAEfk(SgIdentificacionOAE rioIdentificacionOAEfk) {
        this.rioIdentificacionOAEfk = rioIdentificacionOAEfk;
    }

    public String getRioValor() {
        return rioValor;
    }

    public void setRioValor(String rioValor) {
        this.rioValor = rioValor;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.rioPk);
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
        final SgRelOAEIdentificacionOAE other = (SgRelOAEIdentificacionOAE) obj;
        if (!Objects.equals(this.rioPk, other.rioPk)) {
            return false;
        }
        return true;
    }

}
