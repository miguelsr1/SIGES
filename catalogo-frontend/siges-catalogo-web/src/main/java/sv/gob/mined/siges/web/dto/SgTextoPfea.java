package sv.gob.mined.siges.web.dto;


/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "texPk", scope = SgTextoPfea.class)
public class SgTextoPfea implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long texPk;

    private String texCodigo;
    
    private String texValor;

    private LocalDateTime texUltModFecha;

    private String texUltModUsuario;

    private Integer texVersion;
    


    public SgTextoPfea() {
    }

    public Long getTexPk() {
        return texPk;
    }

    public void setTexPk(Long texPk) {
        this.texPk = texPk;
    }

    public String getTexCodigo() {
        return texCodigo;
    }

    public void setTexCodigo(String texCodigo) {
        this.texCodigo = texCodigo;
    }

    public String getTexValor() {
        return texValor;
    }

    public void setTexValor(String texValor) {
        this.texValor = texValor;
    }

    public LocalDateTime getTexUltModFecha() {
        return texUltModFecha;
    }

    public void setTexUltModFecha(LocalDateTime texUltModFecha) {
        this.texUltModFecha = texUltModFecha;
    }

    public String getTexUltModUsuario() {
        return texUltModUsuario;
    }

    public void setTexUltModUsuario(String texUltModUsuario) {
        this.texUltModUsuario = texUltModUsuario;
    }

    public Integer getTexVersion() {
        return texVersion;
    }

    public void setTexVersion(Integer texVersion) {
        this.texVersion = texVersion;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.texPk);
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
        final SgTextoPfea other = (SgTextoPfea) obj;
        if (!Objects.equals(this.texPk, other.texPk)) {
            return false;
        }
        return true;
    }


   
    
    

}

