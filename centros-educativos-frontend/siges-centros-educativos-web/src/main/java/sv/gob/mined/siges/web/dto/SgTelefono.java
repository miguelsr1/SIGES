/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.web.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoTelefono;
import sv.gob.mined.siges.web.utilidades.ViewDto;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "telPk", scope = SgTelefono.class)
public class SgTelefono implements Serializable, ViewDto {

    private static final long serialVersionUID = 1L;

    private Long telPk;

    private String telTelefono;

    private SgTipoTelefono telTipoTelefono;

    private LocalDateTime telUltModFecha;

    private String telUltModUsuario;

    private Integer telVersion;

    private SgPersona telPersona;

    private Boolean telPkForView;

    public SgTelefono() {
    }

    public SgTelefono(Long telPk) {
        this.telPk = telPk;
    }

    public Long getTelPk() {
        return telPk;
    }

    public void setTelPk(Long telPk) {
        this.telPk = telPk;
    }

    public String getTelTelefono() {
        return telTelefono;
    }

    public void setTelTelefono(String telTelefono) {
        this.telTelefono = telTelefono;
    }

    public SgTipoTelefono getTelTipoTelefono() {
        return telTipoTelefono;
    }

    public void setTelTipoTelefono(SgTipoTelefono telTipoTelefono) {
        this.telTipoTelefono = telTipoTelefono;
    }

    public LocalDateTime getTelUltModFecha() {
        return telUltModFecha;
    }

    public void setTelUltModFecha(LocalDateTime telUltModFecha) {
        this.telUltModFecha = telUltModFecha;
    }

    public String getTelUltModUsuario() {
        return telUltModUsuario;
    }

    public void setTelUltModUsuario(String telUltModUsuario) {
        this.telUltModUsuario = telUltModUsuario;
    }

    public Integer getTelVersion() {
        return telVersion;
    }

    public void setTelVersion(Integer telVersion) {
        this.telVersion = telVersion;
    }

    public SgPersona getTelPersona() {
        return telPersona;
    }

    public void setTelPersona(SgPersona telPersona) {
        this.telPersona = telPersona;
    }

    @Override
    public Long getId() {
        return this.telPk;
    }

    @Override
    public void setId(Long id) {
        this.telPk = id;
    }

    @Override
    public Boolean getIdForView() {
        return this.telPkForView;
    }

    @Override
    public void setIdForView(Boolean valor) {
        this.telPkForView = valor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (telPk != null ? telPk.hashCode() : 0);
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
        final SgTelefono other = (SgTelefono) obj;
        return this.telPk != null && this.telPk.equals(other.telPk);
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesTelefono[ telPk=" + telPk + " ]";
    }

}
