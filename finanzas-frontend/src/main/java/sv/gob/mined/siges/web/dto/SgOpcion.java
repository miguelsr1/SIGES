/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 *
 * @author Sofis Solutions
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "opcPk", scope = SgOpcion.class)
public class SgOpcion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long opcPk;

    private String opcCodigo;

    private String opcNombre;

    private String opcNombreBusqueda;

    private String opcDescripcion;

    private Boolean opcHabilitado;

    private LocalDateTime opcUltModFecha;

    private String opcUltModUsuario;

    private Integer opcVersion;

    private SgModalidad opcModalidad;

    private List<SgRelOpcionProgEd> opcRelacionOpcionProgramaEdu;

    public SgOpcion() {
        this.opcHabilitado = Boolean.TRUE;
        this.opcRelacionOpcionProgramaEdu = new ArrayList<>();
    }

    public Long getOpcPk() {
        return opcPk;
    }

    public void setOpcPk(Long opcPk) {
        this.opcPk = opcPk;
    }

    public String getOpcCodigo() {
        return opcCodigo;
    }

    public void setOpcCodigo(String opcCodigo) {
        this.opcCodigo = opcCodigo;
    }

    public String getOpcNombre() {
        return opcNombre;
    }

    public void setOpcNombre(String opcNombre) {
        this.opcNombre = opcNombre;
    }

    public String getOpcNombreBusqueda() {
        return opcNombreBusqueda;
    }

    public void setOpcNombreBusqueda(String opcNombreBusqueda) {
        this.opcNombreBusqueda = opcNombreBusqueda;
    }

    public String getOpcDescripcion() {
        return opcDescripcion;
    }

    public void setOpcDescripcion(String opcDescripcion) {
        this.opcDescripcion = opcDescripcion;
    }

    public LocalDateTime getOpcUltModFecha() {
        return opcUltModFecha;
    }

    public void setOpcUltModFecha(LocalDateTime opcUltModFecha) {
        this.opcUltModFecha = opcUltModFecha;
    }

    public String getOpcUltModUsuario() {
        return opcUltModUsuario;
    }

    public void setOpcUltModUsuario(String opcUltModUsuario) {
        this.opcUltModUsuario = opcUltModUsuario;
    }

    public Integer getOpcVersion() {
        return opcVersion;
    }

    public void setOpcVersion(Integer opcVersion) {
        this.opcVersion = opcVersion;
    }

    public Boolean getOpcHabilitado() {
        return opcHabilitado;
    }

    public void setOpcHabilitado(Boolean opcHabilitado) {
        this.opcHabilitado = opcHabilitado;
    }

    public SgModalidad getOpcModalidad() {
        return opcModalidad;
    }

    public void setOpcModalidad(SgModalidad opcModalidad) {
        this.opcModalidad = opcModalidad;
    }


    public List<SgRelOpcionProgEd> getOpcRelacionOpcionProgramaEdu() {
        return opcRelacionOpcionProgramaEdu;
    }

    public void setOpcRelacionOpcionProgramaEdu(List<SgRelOpcionProgEd> opcRelacionOpcionProgramaEdu) {
        this.opcRelacionOpcionProgramaEdu = opcRelacionOpcionProgramaEdu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (opcPk != null ? opcPk.hashCode() : 0);
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
        final SgOpcion other = (SgOpcion) obj;
        if (!Objects.equals(this.opcPk, other.opcPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.sofis.entidades.SgOpcion[ opcPk=" + opcPk + " ]";
    }

}
