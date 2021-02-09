/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "notPk", scope = SgNoticia.class)
public class SgNoticia implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long notPk;

    private String notCodigo;

    private String notContenido;

    private LocalDate notFechaDesde;

    private LocalDate notFechaHasta;

    private Boolean notHabilitado;

    private LocalDateTime notUltModFecha;

    private String notUltModUsuario;

    private Integer notVersion;

    private String notTitulo;

    private Integer notOrden;

    private Boolean notResaltada;

    public SgNoticia() {
        this.notHabilitado = Boolean.TRUE;
    }

    public SgNoticia(Long notPk) {
        this.notPk = notPk;
    }

    public Long getNotPk() {
        return notPk;
    }

    public void setNotPk(Long notPk) {
        this.notPk = notPk;
    }

    public String getNotCodigo() {
        return notCodigo;
    }

    public void setNotCodigo(String notCodigo) {
        this.notCodigo = notCodigo;
    }

    public String getNotContenido() {
        return notContenido;
    }

    public void setNotContenido(String notContenido) {
        this.notContenido = notContenido;
    }

    public LocalDate getNotFechaDesde() {
        return notFechaDesde;
    }

    public void setNotFechaDesde(LocalDate notFechaDesde) {
        this.notFechaDesde = notFechaDesde;
    }

    public LocalDate getNotFechaHasta() {
        return notFechaHasta;
    }

    public void setNotFechaHasta(LocalDate notFechaHasta) {
        this.notFechaHasta = notFechaHasta;
    }

    public Boolean getNotHabilitado() {
        return notHabilitado;
    }

    public void setNotHabilitado(Boolean notHabilitado) {
        this.notHabilitado = notHabilitado;
    }

    public LocalDateTime getNotUltModFecha() {
        return notUltModFecha;
    }

    public void setNotUltModFecha(LocalDateTime notUltModFecha) {
        this.notUltModFecha = notUltModFecha;
    }

    public String getNotUltModUsuario() {
        return notUltModUsuario;
    }

    public void setNotUltModUsuario(String notUltModUsuario) {
        this.notUltModUsuario = notUltModUsuario;
    }

    public Integer getNotVersion() {
        return notVersion;
    }

    public void setNotVersion(Integer notVersion) {
        this.notVersion = notVersion;
    }

    public String getNotTitulo() {
        return notTitulo;
    }

    public void setNotTitulo(String notTitulo) {
        this.notTitulo = notTitulo;
    }

    public Integer getNotOrden() {
        return notOrden;
    }

    public void setNotOrden(Integer notOrden) {
        this.notOrden = notOrden;
    }

    public Boolean getNotResaltada() {
        return notResaltada;
    }

    public void setNotResaltada(Boolean notResaltada) {
        this.notResaltada = notResaltada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notPk != null ? notPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgNoticia)) {
            return false;
        }
        SgNoticia other = (SgNoticia) object;
        if ((this.notPk == null && other.notPk != null) || (this.notPk != null && !this.notPk.equals(other.notPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgNoticias[ notPk=" + notPk + " ]";
    }

}
