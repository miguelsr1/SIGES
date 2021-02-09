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
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumServiciosRest;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_proceso_nocturno", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "prnPk", scope = SgProcesoNocturno.class)
@Audited
public class SgProcesoNocturno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "prn_pk", nullable = false)
    private Long prnPk;

   
    @Column(name = "prn_servicio")   
    @Enumerated(EnumType.STRING)
    private EnumServiciosRest prnServicio;
    
    @Size(max = 255)
    @Column(name = "prn_url")   
    private String prnUrl;

    @Size(max = 255)
    @Column(name = "prn_nombre", length = 255)
    @AtributoNormalizable
    private String prnNombre;
    
    @Size(max = 255)
    @Column(name = "prn_nombre_busqueda", length = 255)
    @AtributoNormalizable
    @AtributoNombre
    private String prnNombreBusqueda;


    @Column(name = "prn_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime prnUltModFecha;

    @Size(max = 45)
    @Column(name = "prn_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String prnUltModUsuario;

    @Column(name = "prn_version")
    @Version
    private Integer prnVersion;
    
    @OneToMany(mappedBy = "eprProcesoNocturnoFk", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SgEjecucionProcesoNocturno> ejecucionesProcesoNocturno;

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.prnNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(prnNombre);
    }
    
    public SgProcesoNocturno() {
        
    }
    
    

    public Long getPrnPk() {
        return prnPk;
    }

    public void setPrnPk(Long prnPk) {
        this.prnPk = prnPk;
    }

    public String getPrnNombre() {
        return prnNombre;
    }

    public void setPrnNombre(String prnNombre) {
        this.prnNombre = prnNombre;
    }

    public LocalDateTime getPrnUltModFecha() {
        return prnUltModFecha;
    }

    public void setPrnUltModFecha(LocalDateTime prnUltModFecha) {
        this.prnUltModFecha = prnUltModFecha;
    }

    public String getPrnUltModUsuario() {
        return prnUltModUsuario;
    }

    public void setPrnUltModUsuario(String prnUltModUsuario) {
        this.prnUltModUsuario = prnUltModUsuario;
    }

    public Integer getPrnVersion() {
        return prnVersion;
    }

    public void setPrnVersion(Integer prnVersion) {
        this.prnVersion = prnVersion;
    }

    public EnumServiciosRest getPrnServicio() {
        return prnServicio;
    }

    public void setPrnServicio(EnumServiciosRest prnServicio) {
        this.prnServicio = prnServicio;
    }

    public String getPrnUrl() {
        return prnUrl;
    }

    public void setPrnUrl(String prnUrl) {
        this.prnUrl = prnUrl;
    }

    public List<SgEjecucionProcesoNocturno> getEjecucionesProcesoNocturno() {
        return ejecucionesProcesoNocturno;
    }

    public void setEjecucionesProcesoNocturno(List<SgEjecucionProcesoNocturno> ejecucionesProcesoNocturno) {
        this.ejecucionesProcesoNocturno = ejecucionesProcesoNocturno;
    }

    public String getPrnNombreBusqueda() {
        return prnNombreBusqueda;
    }

    public void setPrnNombreBusqueda(String prnNombreBusqueda) {
        this.prnNombreBusqueda = prnNombreBusqueda;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.prnPk);
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
        final SgProcesoNocturno other = (SgProcesoNocturno) obj;
        if (!Objects.equals(this.prnPk, other.prnPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgProcesoNocturno{" + "prnPk=" + prnPk + ", prnUltModFecha=" + prnUltModFecha + ", prnUltModUsuario=" + prnUltModUsuario + ", prnVersion=" + prnVersion + '}';
    }
    
    

}
