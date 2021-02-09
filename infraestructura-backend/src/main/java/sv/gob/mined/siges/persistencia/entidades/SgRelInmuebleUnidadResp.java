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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumTipoUnidadResponsable;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.persistencia.entidades.centros.SgSedeLite;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_inmueble_unidad_resp",  schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "riuPk", scope = SgRelInmuebleUnidadResp.class)
@Audited
public class SgRelInmuebleUnidadResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "riu_pk", nullable = false)
    private Long riuPk;
    
    @JoinColumn(name = "riu_inmueble_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes riuInmuebleFk;
    
    @Column(name = "riu_tipo_unidad")
    @Enumerated(value = EnumType.STRING)
    private EnumTipoUnidadResponsable riuTipoUnidadResp;
    

    @JoinColumn(name = "riu_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSedeLite riuSedeFk;
    
    @JoinColumn(name = "riu_unidad_administrativa_fk", referencedColumnName = "uad_pk")
    @ManyToOne
    private SgAfUnidadesAdministrativas riuAfUnidadAdministrativa;

    @Column(name = "riu_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime riuUltModFecha;

    @Size(max = 45)
    @Column(name = "riu_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String riuUltModUsuario;

    @Column(name = "riu_version")
    @Version
    private Integer riuVersion;

    public SgRelInmuebleUnidadResp() {
    }



    public Long getRiuPk() {
        return riuPk;
    }

    public void setRiuPk(Long riuPk) {
        this.riuPk = riuPk;
    }

    public EnumTipoUnidadResponsable getRiuTipoUnidadResp() {
        return riuTipoUnidadResp;
    }

    public void setRiuTipoUnidadResp(EnumTipoUnidadResponsable riuTipoUnidadResp) {
        this.riuTipoUnidadResp = riuTipoUnidadResp;
    }

    public SgSedeLite getRiuSedeFk() {
        return riuSedeFk;
    }

    public void setRiuSedeFk(SgSedeLite riuSedeFk) {
        this.riuSedeFk = riuSedeFk;
    }

    public SgAfUnidadesAdministrativas getRiuAfUnidadAdministrativa() {
        return riuAfUnidadAdministrativa;
    }

    public void setRiuAfUnidadAdministrativa(SgAfUnidadesAdministrativas riuAfUnidadAdministrativa) {
        this.riuAfUnidadAdministrativa = riuAfUnidadAdministrativa;
    }
 

    public LocalDateTime getRiuUltModFecha() {
        return riuUltModFecha;
    }

    public void setRiuUltModFecha(LocalDateTime riuUltModFecha) {
        this.riuUltModFecha = riuUltModFecha;
    }

    public String getRiuUltModUsuario() {
        return riuUltModUsuario;
    }

    public void setRiuUltModUsuario(String riuUltModUsuario) {
        this.riuUltModUsuario = riuUltModUsuario;
    }

    public Integer getRiuVersion() {
        return riuVersion;
    }

    public void setRiuVersion(Integer riuVersion) {
        this.riuVersion = riuVersion;
    }

    public SgInmueblesSedes getRiuInmuebleFk() {
        return riuInmuebleFk;
    }

    public void setRiuInmuebleFk(SgInmueblesSedes riuInmuebleFk) {
        this.riuInmuebleFk = riuInmuebleFk;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.riuPk);
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
        final SgRelInmuebleUnidadResp other = (SgRelInmuebleUnidadResp) obj;
        if (!Objects.equals(this.riuPk, other.riuPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelInmuebleUnidadResp{" + "riuPk=" + riuPk + ", riuTipoUnidadResp=" + riuTipoUnidadResp + ", riuSedeFk=" + riuSedeFk + ", riuAfUnidadAdministrativa=" + riuAfUnidadAdministrativa + ", riuUltModFecha=" + riuUltModFecha + ", riuUltModUsuario=" + riuUltModUsuario + ", riuVersion=" + riuVersion + '}';
    }

  
    
    

}
