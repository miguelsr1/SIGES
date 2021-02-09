/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package sv.gob.mined.siges.persistencia.entidades.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_fuente_financiamiento")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "id", scope = SsFuenteFinanciamiento.class)
@Audited
/**
 * Entidad correspondiente a las fuentes de financiamiento.
 */
public class SsFuenteFinanciamiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fue_id")
    private Long id;

    @Column(name = "fue_nombre")
    private String nombre;

    @Column(name = "fue_habilitado")
    @AtributoHabilitado
    private Boolean habilitado = Boolean.TRUE;

    @Column(name = "fue_orden")
    private Integer orden;

    @Column(name = "fue_codigo", nullable = false, unique = true)
    private String codigo;

    @OneToMany(mappedBy = "fuenteFinanciamiento")
    private List<SsFuenteRecursos> fuentesDeRecursos;

    @Column(name = "fue_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "fue_ult_mod")
    @AtributoUltimaModificacion
    private LocalDate ultMod;

    @Column(name = "fue_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public List<SsFuenteRecursos> getFuentesDeRecursos() {
        return fuentesDeRecursos;
    }

    public void setFuentesDeRecursos(List<SsFuenteRecursos> fuentesDeRecursos) {
        this.fuentesDeRecursos = fuentesDeRecursos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public LocalDate getUltMod() {
        return ultMod;
    }

    public void setUltMod(LocalDate ultMod) {
        this.ultMod = ultMod;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    // </editor-fold>

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final SsFuenteFinanciamiento other = (SsFuenteFinanciamiento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
