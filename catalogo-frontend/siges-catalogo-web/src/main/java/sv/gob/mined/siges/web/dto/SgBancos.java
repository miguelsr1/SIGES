/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bncPk", scope = SgBancos.class)
public class SgBancos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long bncPk;
    private String bncCodigo;
    private String bncNombre;
    private String bncTelefono;
    private String bncCorreoElectronico;
    private String bncNombreBusqueda;
    private Boolean bncHabilitado;
    private String[] bncAplicaPara;
    private String bncEstilo;
    private LocalDateTime bncUltModFecha;
    private String bncUltModUsuario;
    private Integer bncVersion;

    public SgBancos() {
        bncHabilitado = Boolean.TRUE;
    }

    public Long getBncPk() {
        return bncPk;
    }

    public void setBncPk(Long bncPk) {
        this.bncPk = bncPk;
    }

    public String getBncCodigo() {
        return bncCodigo;
    }

    public void setBncCodigo(String bncCodigo) {
        this.bncCodigo = bncCodigo;
    }

    public String getBncNombre() {
        return bncNombre;
    }

    public void setBncNombre(String bncNombre) {
        this.bncNombre = bncNombre;
    }

    public String getBncTelefono() {
        return bncTelefono;
    }

    public void setBncTelefono(String bncTelefono) {
        this.bncTelefono = bncTelefono;
    }

    public String getBncCorreoElectronico() {
        return bncCorreoElectronico;
    }

    public void setBncCorreoElectronico(String bncCorreoElectronico) {
        this.bncCorreoElectronico = bncCorreoElectronico;
    }

    public String getBncNombreBusqueda() {
        return bncNombreBusqueda;
    }

    public void setBncNombreBusqueda(String bncNombreBusqueda) {
        this.bncNombreBusqueda = bncNombreBusqueda;
    }

    public Boolean getBncHabilitado() {
        return bncHabilitado;
    }

    public void setBncHabilitado(Boolean bncHabilitado) {
        this.bncHabilitado = bncHabilitado;
    }

    public LocalDateTime getBncUltModFecha() {
        return bncUltModFecha;
    }

    public void setBncUltModFecha(LocalDateTime bncUltModFecha) {
        this.bncUltModFecha = bncUltModFecha;
    }

    public String getBncUltModUsuario() {
        return bncUltModUsuario;
    }

    public void setBncUltModUsuario(String bncUltModUsuario) {
        this.bncUltModUsuario = bncUltModUsuario;
    }

    public Integer getBncVersion() {
        return bncVersion;
    }

    public void setBncVersion(Integer bncVersion) {
        this.bncVersion = bncVersion;
    }

    public String[] getBncAplicaPara() {
        return bncAplicaPara;
    }

    public void setBncAplicaPara(String[] bncAplicaPara) {
        this.bncAplicaPara = bncAplicaPara;
    }

    public String getBncEstilo() {
        return bncEstilo;
    }

    public void setBncEstilo(String bncEstilo) {
        this.bncEstilo = bncEstilo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.bncPk);
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
        final SgBancos other = (SgBancos) obj;
        if (!Objects.equals(this.bncPk, other.bncPk)) {
            return false;
        }
        return true;
    }
    
    
}

