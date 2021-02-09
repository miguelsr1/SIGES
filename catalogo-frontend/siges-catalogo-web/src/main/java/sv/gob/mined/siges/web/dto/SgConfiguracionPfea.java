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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "conPk", scope = SgConfiguracionPfea.class)
public class SgConfiguracionPfea implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long conPk;

    private String conCodigo;

    private String conNombre;

    private String conNombreBusqueda;

    private String conValor;

    private LocalDateTime conUltModFecha;

    private String conUltModUsuario;

    private Integer conVersion;

    private Boolean conEsEditor;

    public SgConfiguracionPfea() {
    }

    public String getConValorResumen() {
        if (conValor != null && conValor.length() > 200) {
            return conValor.substring(0, 200).concat("...");
        }
        return conValor;
    }

    public Long getConPk() {
        return conPk;
    }

    public void setConPk(Long conPk) {
        this.conPk = conPk;
    }

    public String getConCodigo() {
        return conCodigo;
    }

    public void setConCodigo(String conCodigo) {
        this.conCodigo = conCodigo;
    }

    public String getConNombre() {
        return conNombre;
    }

    public void setConNombre(String conNombre) {
        this.conNombre = conNombre;
    }

    public String getConNombreBusqueda() {
        return conNombreBusqueda;
    }

    public void setConNombreBusqueda(String conNombreBusqueda) {
        this.conNombreBusqueda = conNombreBusqueda;
    }

    public LocalDateTime getConUltModFecha() {
        return conUltModFecha;
    }

    public void setConUltModFecha(LocalDateTime conUltModFecha) {
        this.conUltModFecha = conUltModFecha;
    }

    public String getConUltModUsuario() {
        return conUltModUsuario;
    }

    public void setConUltModUsuario(String conUltModUsuario) {
        this.conUltModUsuario = conUltModUsuario;
    }

    public Integer getConVersion() {
        return conVersion;
    }

    public void setConVersion(Integer conVersion) {
        this.conVersion = conVersion;
    }

    public String getConValor() {
        return conValor;
    }

    public void setConValor(String conValor) {
        this.conValor = conValor;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.conPk);
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
        final SgConfiguracionPfea other = (SgConfiguracionPfea) obj;
        if (!Objects.equals(this.conPk, other.conPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgConfiguracionPfea{" + "conPk=" + conPk + ", conCodigo=" + conCodigo + ", conNombre=" + conNombre + ", conNombreBusqueda=" + conNombreBusqueda + ", conUltModFecha=" + conUltModFecha + ", conUltModUsuario=" + conUltModUsuario + ", conVersion=" + conVersion + '}';
    }

    public Boolean getConEsEditor() {
        return conEsEditor;
    }

    public void setConEsEditor(Boolean conEsEditor) {
        this.conEsEditor = conEsEditor;
    }

}
