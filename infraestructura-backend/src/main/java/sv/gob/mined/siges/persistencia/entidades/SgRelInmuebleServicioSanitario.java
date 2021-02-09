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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoServicioSanitario;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_inmueble_servicio_sanitario", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ritPk", scope = SgRelInmuebleServicioSanitario.class)
@Audited
public class SgRelInmuebleServicioSanitario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rit_pk", nullable = false)
    private Long ritPk;
    
    @Column(name = "rit_bueno")
    private Integer ritBueno;
    
    @Column(name = "rit_malo")
    private Integer ritMalo;
    
    @Column(name = "rit_regular")
    private Integer ritRegular;
    
    @Column(name = "rit_ninos")
    private Integer ritNinos;
    
    @Column(name = "rit_ninas")
    private Integer ritNinas;
    
    @Column(name = "rit_maestros")
    private Integer ritMaestros;
    
    @Column(name = "rit_administrativos")
    private Integer ritAdministrativos;
    
    @Column(name = "rit_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ritUltModFecha;

    @Size(max = 45)
    @Column(name = "rit_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ritUltModUsuario;

    @Column(name = "rit_version")
    @Version
    private Integer ritVersion;
    
    @JoinColumn(name = "rit_inmueble_sede_fk", referencedColumnName = "tis_pk")
    @ManyToOne
    private SgInmueblesSedes ritInmuebleSede;
    
    @JoinColumn(name = "rit_servicio_sanitario_fk", referencedColumnName = "tss_pk")
    @ManyToOne
    private SgTipoServicioSanitario ritServicioSanitario;

    public SgRelInmuebleServicioSanitario() {
    }

 
    public Long getRitPk() {
        return ritPk;
    }

    public void setRitPk(Long ritPk) {
        this.ritPk = ritPk;
    }


    public LocalDateTime getRitUltModFecha() {
        return ritUltModFecha;
    }

    public void setRitUltModFecha(LocalDateTime ritUltModFecha) {
        this.ritUltModFecha = ritUltModFecha;
    }

    public String getRitUltModUsuario() {
        return ritUltModUsuario;
    }

    public void setRitUltModUsuario(String ritUltModUsuario) {
        this.ritUltModUsuario = ritUltModUsuario;
    }

    public Integer getRitVersion() {
        return ritVersion;
    }

    public void setRitVersion(Integer ritVersion) {
        this.ritVersion = ritVersion;
    }

    public Integer getRitBueno() {
        return ritBueno;
    }

    public void setRitBueno(Integer ritBueno) {
        this.ritBueno = ritBueno;
    }

    public Integer getRitMalo() {
        return ritMalo;
    }

    public void setRitMalo(Integer ritMalo) {
        this.ritMalo = ritMalo;
    }

    public Integer getRitRegular() {
        return ritRegular;
    }

    public void setRitRegular(Integer ritRegular) {
        this.ritRegular = ritRegular;
    }

    public Integer getRitNinos() {
        return ritNinos;
    }

    public void setRitNinos(Integer ritNinos) {
        this.ritNinos = ritNinos;
    }

    public Integer getRitNinas() {
        return ritNinas;
    }

    public void setRitNinas(Integer ritNinas) {
        this.ritNinas = ritNinas;
    }

    public Integer getRitMaestros() {
        return ritMaestros;
    }

    public void setRitMaestros(Integer ritMaestros) {
        this.ritMaestros = ritMaestros;
    }

    public Integer getRitAdministrativos() {
        return ritAdministrativos;
    }

    public void setRitAdministrativos(Integer ritAdministrativos) {
        this.ritAdministrativos = ritAdministrativos;
    }

    public SgInmueblesSedes getRitInmuebleSede() {
        return ritInmuebleSede;
    }

    public void setRitInmuebleSede(SgInmueblesSedes ritInmuebleSede) {
        this.ritInmuebleSede = ritInmuebleSede;
    }

    public SgTipoServicioSanitario getRitServicioSanitario() {
        return ritServicioSanitario;
    }

    public void setRitServicioSanitario(SgTipoServicioSanitario ritServicioSanitario) {
        this.ritServicioSanitario = ritServicioSanitario;
    }

  
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.ritPk);
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
        final SgRelInmuebleServicioSanitario other = (SgRelInmuebleServicioSanitario) obj;
        if (!Objects.equals(this.ritPk, other.ritPk)) {
            return false;
        }
        if (!Objects.equals(this.ritInmuebleSede, other.ritInmuebleSede)) {
            return false;
        }
        if (!Objects.equals(this.ritServicioSanitario, other.ritServicioSanitario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelInmuebleServicioSanitario{" + "ritPk=" + ritPk + ", ritBueno=" + ritBueno + ", ritMalo=" + ritMalo + ", ritRegular=" + ritRegular + ", ritNinos=" + ritNinos + ", ritNinas=" + ritNinas + ", ritMaestros=" + ritMaestros + ", ritAdministrativos=" + ritAdministrativos + ", ritUltModFecha=" + ritUltModFecha + ", ritUltModUsuario=" + ritUltModUsuario + ", ritVersion=" + ritVersion + ", ritInmuebleSede=" + ritInmuebleSede + ", ritServicioSanitario=" + ritServicioSanitario + '}';
    }

   

    
    
    

}
