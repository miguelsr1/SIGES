/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "recPk", scope = SgReglaContexto.class)
public class SgReglaContexto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long recPk;

    private LocalDateTime recUltModFecha;

    private String recUltModUsuario;

    private Integer recVersion;

    private String recNombre;
    
    private String recRegla;

    public SgReglaContexto() {
    }

    public SgReglaContexto(Long recPk) {
        this.recPk = recPk;
    }

    public Long getRecPk() {
        return recPk;
    }

    public void setRecPk(Long recPk) {
        this.recPk = recPk;
    }

    public LocalDateTime getRecUltModFecha() {
        return recUltModFecha;
    }

    public void setRecUltModFecha(LocalDateTime recUltModFecha) {
        this.recUltModFecha = recUltModFecha;
    }

    public String getRecUltModUsuario() {
        return recUltModUsuario;
    }

    public void setRecUltModUsuario(String recUltModUsuario) {
        this.recUltModUsuario = recUltModUsuario;
    }

    public Integer getRecVersion() {
        return recVersion;
    }

    public void setRecVersion(Integer recVersion) {
        this.recVersion = recVersion;
    }

    public String getRecNombre() {
        return recNombre;
    }

    public void setRecNombre(String recNombre) {
        this.recNombre = recNombre;
    }

    public String getRecRegla() {
        return recRegla;
    }

    public void setRecRegla(String recRegla) {
        this.recRegla = recRegla;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.recPk);
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
        final SgReglaContexto other = (SgReglaContexto) obj;
        if (!Objects.equals(this.recPk, other.recPk)) {
            return false;
        }
        return true;
    }


    

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgReglaContexto[ recPk=" + recPk + " ]";
    }

}
