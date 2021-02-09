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
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_tipos_acuerdos", uniqueConstraints = {
    @UniqueConstraint(name = "tao_codigo_uk", columnNames = {"tao_codigo"})
    ,
    @UniqueConstraint(name = "tao_nombre_uk", columnNames = {"tao_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "taoPk", scope = SgTipoAcuerdo.class)
@Audited
public class SgTipoAcuerdo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tao_pk", nullable = false)
    private Long taoPk;

    @Size(max = 45)
    @Column(name = "tao_codigo", length = 45)
    @AtributoCodigo
    private String taoCodigo;

    @Size(max = 255)
    @Column(name = "tao_nombre", length = 255)
    @AtributoNormalizable
    private String taoNombre;
    
    @Column(name = "tao_tramite_revocatoria_sede")
    private Boolean taoTramiteRevocatoriaSede;

    @Column(name = "tao_tramite_creacion_sede")
    private Boolean taoTramiteCreacionSede;

    @Column(name = "tao_tramite_nominacion_sede")
    private Boolean taoTramiteNominacionSede;
    
    @Column(name = "tao_tramite_cambio_domicilio_sede")
    private Boolean taoTramiteCambioDomicilioSede;
    
    @Column(name = "tao_tramite_ampliacion_servicios")
    private Boolean taoTramiteAmpliacionServicios;
    
    @Column(name = "tao_tramite_disminucion_servicios")
    private Boolean taoTramiteDisminucionServicios;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tao_nombre_busqueda", length = 255)
    private String taoNombreBusqueda;

    @Column(name = "tao_habilitado")
    @AtributoHabilitado
    private Boolean taoHabilitado;

    @Column(name = "tao_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime taoUltModFecha;

    @Size(max = 45)
    @Column(name = "tao_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String taoUltModUsuario;

    @Column(name = "tao_version")
    @Version
    private Integer taoVersion;

    public SgTipoAcuerdo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.taoNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.taoNombre);
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

    public String getTaoNombreBusqueda() {
        return taoNombreBusqueda;
    }

    public void setTaoNombreBusqueda(String taoNombreBusqueda) {
        this.taoNombreBusqueda = taoNombreBusqueda;
    }

    public Boolean getTaoHabilitado() {
        return taoHabilitado;
    }

    public void setTaoHabilitado(Boolean taoHabilitado) {
        this.taoHabilitado = taoHabilitado;
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
        return Objects.hashCode(this.taoPk);
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
        return "SgTipoAcuerdo{" + "taoPk=" + taoPk + ", taoCodigo=" + taoCodigo + ", taoNombre=" + taoNombre + ", taoNombreBusqueda=" + taoNombreBusqueda + ", taoHabilitado=" + taoHabilitado + ", taoUltModFecha=" + taoUltModFecha + ", taoUltModUsuario=" + taoUltModUsuario + ", taoVersion=" + taoVersion + '}';
    }
    
    

}
