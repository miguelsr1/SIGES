/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.persistencia.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import sv.gob.mined.siges.constantes.Constantes;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDateTime;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.centroseducativos.SgNivel;


@Entity
@Table(name = "sg_rh_pagina", schema = Constantes.SCHEMA_REGISTRO_HISTORICO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pagPk", scope = SgRhPagina.class)
@Audited
public class SgRhPagina implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pag_pk")
    private Long pagPk;
    
    @Column(name = "pag_libro")
    private Integer pagLibro;
    
    @Column(name = "pag_pagina")
    private Integer pagPagina;
    
    @Column(name = "pag_anio")
    private Integer pagAnio;
    
    @JoinColumn(name = "pag_nivel")
    @ManyToOne
    private SgNivel pagNivel;
    
    @Column(name = "pag_nombre_libro")
    private String pagNombreLibro;
    
    @Column(name = "pag_ruta")
    private String pagRuta;        
 
    @Column(name = "pag_habilitado")
    @AtributoHabilitado
    private Boolean pagHabilitado;    
    
    @Column(name = "pag_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime pagUltModFecha;
    
    @Column(name = "pag_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String pagUltModUsuario;
    
    @Column(name = "pag_version")
    @Version
    private Integer pagVersion;
    

    public SgRhPagina() {
    }

    public SgRhPagina(Long pagPk) {
        this.pagPk = pagPk;
    }

    public Long getPagPk() {
        return pagPk;
    }

    public void setPagPk(Long pagPk) {
        this.pagPk = pagPk;
    }

    public Boolean getPagHabilitado() {
        return pagHabilitado;
    }

    public void setPagHabilitado(Boolean pagHabilitado) {
        this.pagHabilitado = pagHabilitado;
    }

    public Integer getPagLibro() {
        return pagLibro;
    }

    public void setPagLibro(Integer pagLibro) {
        this.pagLibro = pagLibro;
    }

    public Integer getPagPagina() {
        return pagPagina;
    }

    public void setPagPagina(Integer pagPagina) {
        this.pagPagina = pagPagina;
    }

    public Integer getPagAnio() {
        return pagAnio;
    }

    public void setPagAnio(Integer pagAnio) {
        this.pagAnio = pagAnio;
    }

    public SgNivel getPagNivel() {
        return pagNivel;
    }

    public void setPagNivel(SgNivel pagNivel) {
        this.pagNivel = pagNivel;
    }

    public LocalDateTime getPagUltModFecha() {
        return pagUltModFecha;
    }

    public void setPagUltModFecha(LocalDateTime pagUltModFecha) {
        this.pagUltModFecha = pagUltModFecha;
    }
    
    public String getPagUltModUsuario() {
        return pagUltModUsuario;
    }

    public void setPagUltModUsuario(String pagUltModUsuario) {
        this.pagUltModUsuario = pagUltModUsuario;
    }

    public Integer getPagVersion() {
        return pagVersion;
    }

    public void setPagVersion(Integer pagVersion) {
        this.pagVersion = pagVersion;
    }

    public String getPagNombreLibro() {
        return pagNombreLibro;
    }

    public void setPagNombreLibro(String pagNombreLibro) {
        this.pagNombreLibro = pagNombreLibro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pagPk != null ? pagPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRhPagina)) {
            return false;
        }
        SgRhPagina other = (SgRhPagina) object;
        if ((this.pagPk == null && other.pagPk != null) || (this.pagPk != null && !this.pagPk.equals(other.pagPk))) {
            return false;
        }
        return true;
    }

    public String getPagRuta() {
        return pagRuta;
    }

    public void setPagRuta(String pagRuta) {
        this.pagRuta = pagRuta;
    }

    @Override
    public String toString() {
        return "entity.SgRhPagina[ pagPk=" + pagPk + " ]";
    }
    
}
