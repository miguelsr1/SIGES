/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package sv.gob.mined.siges.web.dto.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.time.LocalDate;

/**
 *
 * @author Sofis Solutions
 */


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = SsFuenteFinanciamiento.class)
public class SsFuenteFinanciamiento implements Serializable {

    private Long id;

    private String nombre;

    private Boolean habilitado;

    private Integer orden;

    private String codigo;

    private List<SsFuenteRecursos> fuentesDeRecursos;

    private String ultUsuario;

    private LocalDate ultMod;

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
