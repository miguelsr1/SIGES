/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import java.time.LocalDate;
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
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "sg_rh_etiqueta", schema = Constantes.SCHEMA_REGISTRO_HISTORICO) 
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "etiPk", scope = SgRhEtiqueta.class)
@Audited
public class SgRhEtiqueta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eti_pk")
    private Long etiPk;

    @Size(max = 255)
    @Column(name = "eti_apellido", length = 255)
    private String etiApellido;
    
    @Size(max = 255)
    @Column(name = "eti_nombre", length = 255)
    private String etiNombre;

    @Column(name = "eti_dui", length = 20)
    @Size(max = 20)
    private String etiDui;    

    @Column(name = "eti_nie", length = 20)
    @Size(max = 20)
    private String etiNie;    
        
    @JoinColumn(name = "eti_pagina", referencedColumnName = "pag_pk")
    @ManyToOne
    private SgRhPagina etiPagina;
    
    @Column(name = "eti_folio", length = 25)
    @Size(max = 20)
    private String etiFolio;
    
    @Column(name = "eti_correlativo", length = 20)
    private Integer etiCorrelativo;
  
    @Column(name = "eti_fecha")
    private LocalDate etiFecha;    
    
    @Column(name = "eti_folio_mined", length = 25)
    @Size(max = 20)
    private String etiFolioMined;
    
    @Size(max = 600)
    @Column(name = "eti_nombre_sede", length = 255)
    private String etiNombreSede;
    
    @Column(name = "eti_habilitado")
    @AtributoHabilitado
    private Boolean etiHabilitado;

    @Column(name = "eti_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime etiUltModFecha;

    @Size(max = 45)
    @Column(name = "eti_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String etiUltModUsuario;

    @Column(name = "eti_version")
    @Version
    private Integer etiVersion;
    
    @Size(max = 255)
    @Column(name = "eti_link_archivo", length = 255)    
    private String etiLinkArchivo;
    
    @Size(max = 100)
    @Column(name = "eti_nombre_libro", length = 100)    
    private String etiNombreLibro;

    @Transient
    private Boolean etiPaginaCreada;

    public SgRhEtiqueta() {
    }
    
    public Long getEtiPk() {
        return etiPk;
    }

    public void setEtiPk(Long etiPk) {
        this.etiPk = etiPk;
    }

    public String getEtiApellido() {
        return etiApellido;
    }

    public void setEtiApellido(String etiApellido) {
        this.etiApellido = etiApellido;
    }

    public String getEtiNombre() {
        return etiNombre;
    }

    public void setEtiNombre(String etiNombre) {
        this.etiNombre = etiNombre;
    }

    public String getEtiDui() {
        return etiDui;
    }

    public void setEtiDui(String etiDui) {
        this.etiDui = etiDui;
    }

    public String getEtiNie() {
        return etiNie;
    }

    public void setEtiNie(String etiNie) {
        this.etiNie = etiNie;
    }

    public SgRhPagina getEtiPagina() {
        return etiPagina;
    }

    public void setEtiPagina(SgRhPagina etiPagina) {
        this.etiPagina = etiPagina;
    }

    public String getEtiNombreSede() {
        return etiNombreSede;
    }

    public void setEtiNombreSede(String etiNombreSede) {
        this.etiNombreSede = etiNombreSede;
    }

    public Boolean getEtiHabilitado() {
        return etiHabilitado;
    }

    public void setEtiHabilitado(Boolean etiHabilitado) {
        this.etiHabilitado = etiHabilitado;
    }

    public LocalDateTime getEtiUltModFecha() {
        return etiUltModFecha;
    }

    public void setEtiUltModFecha(LocalDateTime etiUltModFecha) {
        this.etiUltModFecha = etiUltModFecha;
    }

    public String getEtiUltModUsuario() {
        return etiUltModUsuario;
    }

    public void setEtiUltModUsuario(String etiUltModUsuario) {
        this.etiUltModUsuario = etiUltModUsuario;
    }

    public Integer getEtiVersion() {
        return etiVersion;
    }

    public void setEtiVersion(Integer etiVersion) {
        this.etiVersion = etiVersion;
    }    

    public String getEtiFolio() {
        return etiFolio;
    }

    public void setEtiFolio(String etiFolio) {
        this.etiFolio = etiFolio;
    }
    
    public Integer getEtiCorrelativo() {
        return etiCorrelativo;
    }

    public void setEtiCorrelativo(Integer etiCorrelativo) {
        this.etiCorrelativo = etiCorrelativo;
    }

    public LocalDate getEtiFecha() {
        return etiFecha;
    }

    public void setEtiFecha(LocalDate etiFecha) {
        this.etiFecha = etiFecha;
    }

    public String getEtiFolioMined() {
        return etiFolioMined;
    }

    public void setEtiFolioMined(String etiFolioMined) {
        this.etiFolioMined = etiFolioMined;
    }

    public String getEtiLinkArchivo() {
        return etiLinkArchivo;
    }

    public void setEtiLinkArchivo(String etiLinkArchivo) {
        this.etiLinkArchivo = etiLinkArchivo;
    }

    public String getEtiNombreLibro() {
        return etiNombreLibro;
    }

    public void setEtiNombreLibro(String etiNombreLibro) {
        this.etiNombreLibro = etiNombreLibro;
    }

    public Boolean getEtiPaginaCreada() {
        return etiPaginaCreada;
    }

    public void setEtiPaginaCreada(Boolean etiPaginaCreada) {
        this.etiPaginaCreada = etiPaginaCreada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (etiPk != null ? etiPk.hashCode() : 0);
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
        final SgRhEtiqueta other = (SgRhEtiqueta) obj;
        if (!Objects.equals(this.etiPk, other.etiPk)) {
            return false;
        }
        return true;
    }

}
