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
@Table(name = "sg_rel_edificio_servicio_sanitario", schema = Constantes.SCHEMA_INFRAESTRUCTURA)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "retPk", scope = SgRelEdificioServicioSanitario.class)
@Audited
public class SgRelEdificioServicioSanitario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ret_pk", nullable = false)
    private Long retPk;

    @Column(name = "ret_bueno")
    private Integer retBueno;
    
    @Column(name = "ret_malo")
    private Integer retMalo;
    
    @Column(name = "ret_regular")
    private Integer retRegular;
    
    @Column(name = "ret_ninos")
    private Integer retNinos;
    
    @Column(name = "ret_ninas")
    private Integer retNinas;
    
    @Column(name = "ret_maestros")
    private Integer retMaestros;
    
    @Column(name = "ret_administrativos")
    private Integer retAdministrativos;

    @Column(name = "ret_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime retUltModFecha;

    @Size(max = 45)
    @Column(name = "ret_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String retUltModUsuario;

    @Column(name = "ret_version")
    @Version
    private Integer retVersion;
    
    @JoinColumn(name = "ret_edificio_fk", referencedColumnName = "edi_pk")
    @ManyToOne
    private SgEdificio retEdificio;
    
    @JoinColumn(name = "ret_servicio_sanitario_fk", referencedColumnName = "tss_pk")
    @ManyToOne
    private SgTipoServicioSanitario retServicioSanitario;

    public SgRelEdificioServicioSanitario() {
    }

 

    public Long getRetPk() {
        return retPk;
    }

    public void setRetPk(Long retPk) {
        this.retPk = retPk;
    }

    public Integer getRetBueno() {
        return retBueno;
    }

    public void setRetBueno(Integer retBueno) {
        this.retBueno = retBueno;
    }

    public Integer getRetMalo() {
        return retMalo;
    }

    public void setRetMalo(Integer retMalo) {
        this.retMalo = retMalo;
    }

    public Integer getRetRegular() {
        return retRegular;
    }

    public void setRetRegular(Integer retRegular) {
        this.retRegular = retRegular;
    }

    public Integer getRetNinos() {
        return retNinos;
    }

    public void setRetNinos(Integer retNinos) {
        this.retNinos = retNinos;
    }

    public Integer getRetNinas() {
        return retNinas;
    }

    public void setRetNinas(Integer retNinas) {
        this.retNinas = retNinas;
    }

    public Integer getRetMaestros() {
        return retMaestros;
    }

    public void setRetMaestros(Integer retMaestros) {
        this.retMaestros = retMaestros;
    }

    public Integer getRetAdministrativos() {
        return retAdministrativos;
    }

    public void setRetAdministrativos(Integer retAdministrativos) {
        this.retAdministrativos = retAdministrativos;
    }

    public SgEdificio getRetEdificio() {
        return retEdificio;
    }

    public void setRetEdificio(SgEdificio retEdificio) {
        this.retEdificio = retEdificio;
    }

    public SgTipoServicioSanitario getRetServicioSanitario() {
        return retServicioSanitario;
    }

    public void setRetServicioSanitario(SgTipoServicioSanitario retServicioSanitario) {
        this.retServicioSanitario = retServicioSanitario;
    }

   

    public LocalDateTime getRetUltModFecha() {
        return retUltModFecha;
    }

    public void setRetUltModFecha(LocalDateTime retUltModFecha) {
        this.retUltModFecha = retUltModFecha;
    }

    public String getRetUltModUsuario() {
        return retUltModUsuario;
    }

    public void setRetUltModUsuario(String retUltModUsuario) {
        this.retUltModUsuario = retUltModUsuario;
    }

    public Integer getRetVersion() {
        return retVersion;
    }

    public void setRetVersion(Integer retVersion) {
        this.retVersion = retVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.retPk);
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
        final SgRelEdificioServicioSanitario other = (SgRelEdificioServicioSanitario) obj;
        if (!Objects.equals(this.retPk, other.retPk)) {
            return false;
        }
        if (!Objects.equals(this.retEdificio, other.retEdificio)) {
            return false;
        }
        if (!Objects.equals(this.retServicioSanitario, other.retServicioSanitario)) {
            return false;
        }
        return true;
    }

    
    
    

}
