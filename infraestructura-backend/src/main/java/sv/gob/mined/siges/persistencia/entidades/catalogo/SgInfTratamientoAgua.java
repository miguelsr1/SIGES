/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

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
@Table(name = "sg_inf_tratamiento_agua", uniqueConstraints = {
    @UniqueConstraint(name = "tra_codigo_uk", columnNames = {"tra_codigo"})
    ,
    @UniqueConstraint(name = "tra_nombre_uk", columnNames = {"tra_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "traPk", scope = SgInfTratamientoAgua.class)
@Audited
public class SgInfTratamientoAgua implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tra_pk", nullable = false)
    private Long traPk;

    @Size(max = 45)
    @Column(name = "tra_codigo", length = 45)
    @AtributoCodigo
    private String traCodigo;

    @Size(max = 255)
    @Column(name = "tra_nombre", length = 255)
    @AtributoNormalizable
    private String traNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tra_nombre_busqueda", length = 255)
    private String traNombreBusqueda;

    @Column(name = "tra_habilitado")
    @AtributoHabilitado
    private Boolean traHabilitado;

    @Column(name = "tra_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime traUltModFecha;

    @Size(max = 45)
    @Column(name = "tra_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String traUltModUsuario;

    @Column(name = "tra_version")
    @Version
    private Integer traVersion;
    
    @Column(name= "tra_orden")
    private Integer traOrden;
    
    @Column(name= "tra_aplica_otros")
    private Boolean traAplicaOtros;
    
    @Column(name= "tra_aplica_abastecimiento_agua")
    private Boolean traAplicaAbastecimientoAgua;
    
    @Column(name= "tra_aplica_almacenamiento_agua")
    private Boolean traAplicaAlmacenamientoAgua;
    
    @Column(name= "tra_aplica_drenaje")
    private Boolean traAplicaDrenaje;
    


    public SgInfTratamientoAgua() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.traNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.traNombre);
    }

    public Long getTraPk() {
        return traPk;
    }

    public void setTraPk(Long traPk) {
        this.traPk = traPk;
    }

    public String getTraCodigo() {
        return traCodigo;
    }

    public void setTraCodigo(String traCodigo) {
        this.traCodigo = traCodigo;
    }

    public String getTraNombre() {
        return traNombre;
    }

    public void setTraNombre(String traNombre) {
        this.traNombre = traNombre;
    }

    public String getTraNombreBusqueda() {
        return traNombreBusqueda;
    }

    public void setTraNombreBusqueda(String traNombreBusqueda) {
        this.traNombreBusqueda = traNombreBusqueda;
    }

    public Boolean getTraHabilitado() {
        return traHabilitado;
    }

    public void setTraHabilitado(Boolean traHabilitado) {
        this.traHabilitado = traHabilitado;
    }

    public LocalDateTime getTraUltModFecha() {
        return traUltModFecha;
    }

    public void setTraUltModFecha(LocalDateTime traUltModFecha) {
        this.traUltModFecha = traUltModFecha;
    }

    public String getTraUltModUsuario() {
        return traUltModUsuario;
    }

    public void setTraUltModUsuario(String traUltModUsuario) {
        this.traUltModUsuario = traUltModUsuario;
    }

    public Integer getTraVersion() {
        return traVersion;
    }

    public void setTraVersion(Integer traVersion) {
        this.traVersion = traVersion;
    }

    public Integer getTraOrden() {
        return traOrden;
    }

    public void setTraOrden(Integer traOrden) {
        this.traOrden = traOrden;
    }

    public Boolean getTraAplicaOtros() {
        return traAplicaOtros;
    }

    public void setTraAplicaOtros(Boolean traAplicaOtros) {
        this.traAplicaOtros = traAplicaOtros;
    }

    public Boolean getTraAplicaAbastecimientoAgua() {
        return traAplicaAbastecimientoAgua;
    }

    public void setTraAplicaAbastecimientoAgua(Boolean traAplicaAbastecimientoAgua) {
        this.traAplicaAbastecimientoAgua = traAplicaAbastecimientoAgua;
    }

    public Boolean getTraAplicaAlmacenamientoAgua() {
        return traAplicaAlmacenamientoAgua;
    }

    public void setTraAplicaAlmacenamientoAgua(Boolean traAplicaAlmacenamientoAgua) {
        this.traAplicaAlmacenamientoAgua = traAplicaAlmacenamientoAgua;
    }

    public Boolean getTraAplicaDrenaje() {
        return traAplicaDrenaje;
    }

    public void setTraAplicaDrenaje(Boolean traAplicaDrenaje) {
        this.traAplicaDrenaje = traAplicaDrenaje;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.traPk);
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
        final SgInfTratamientoAgua other = (SgInfTratamientoAgua) obj;
        if (!Objects.equals(this.traPk, other.traPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgInfTratamientoAgua{" + "traPk=" + traPk + ", traCodigo=" + traCodigo + ", traNombre=" + traNombre + ", traNombreBusqueda=" + traNombreBusqueda + ", traHabilitado=" + traHabilitado + ", traUltModFecha=" + traUltModFecha + ", traUltModUsuario=" + traUltModUsuario + ", traVersion=" + traVersion + '}';
    }
    
    

}
