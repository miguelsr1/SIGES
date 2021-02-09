/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumEstadoPlaza;
import sv.gob.mined.siges.enumerados.EnumSituacionPlaza;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_plazas", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "plaPk", scope = SgPlaza.class)
@Audited
public class SgPlaza implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pla_pk")
    private Long plaPk;
    
    @Size(max = 100)
    @Column(name = "pla_nombre")
    private String plaNombre;
    
    @Size(max = 255)
    @Column(name = "pla_nombre_busqueda")
    private String plaNombreBusqueda;
    
    @Size(max = 255)
    @Column(name = "pla_codigo_busqueda")
    private String plaCodigoBusqueda;
    
    @Column(name = "pla_id_puesto")
    private Long plaIdPuesto;
    
    @Column(name = "pla_partida")
    private Integer plaPartida;
            
    @Column(name = "pla_subpartida")
    private Integer plaSubpartida;
    
    @Column(name = "pla_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoPlaza plaEstado;
    
    @Column(name = "pla_situacion")
    @Enumerated(value = EnumType.STRING)
    private EnumSituacionPlaza plaSituacion;

    @Column(name = "pla_anio_partida")
    private Integer plaAnioPartida;
    
    @Column(name = "pla_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime plaUltModFecha;
    
    @Size(max = 45)
    @Column(name = "pla_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String plaUltModUsuario;
    
    @Column(name = "pla_version")
    @Version
    private Integer plaVersion;
    
    @JoinColumn(name = "pla_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede plaSedeFk;


    public SgPlaza() {
    }
    
    @PrePersist
    @PreUpdate
    public void preSave() {
        this.plaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.plaNombre);
        this.plaCodigoBusqueda= plaPartida +"-"+plaSubpartida;
    }

    public Long getPlaPk() {
        return plaPk;
    }

    public void setPlaPk(Long plaPk) {
        this.plaPk = plaPk;
    }

    public String getPlaNombre() {
        return plaNombre;
    }

    public void setPlaNombre(String plaNombre) {
        this.plaNombre = plaNombre;
    }

    public Long getPlaIdPuesto() {
        return plaIdPuesto;
    }

    public void setPlaIdPuesto(Long plaIdPuesto) {
        this.plaIdPuesto = plaIdPuesto;
    }

    public Integer getPlaPartida() {
        return plaPartida;
    }

    public void setPlaPartida(Integer plaPartida) {
        this.plaPartida = plaPartida;
    }

    public Integer getPlaSubpartida() {
        return plaSubpartida;
    }

    public void setPlaSubpartida(Integer plaSubpartida) {
        this.plaSubpartida = plaSubpartida;
    }

    public EnumEstadoPlaza getPlaEstado() {
        return plaEstado;
    }

    public void setPlaEstado(EnumEstadoPlaza plaEstado) {
        this.plaEstado = plaEstado;
    }

    public EnumSituacionPlaza getPlaSituacion() {
        return plaSituacion;
    }

    public void setPlaSituacion(EnumSituacionPlaza plaSituacion) {
        this.plaSituacion = plaSituacion;
    }

    public Integer getPlaAnioPartida() {
        return plaAnioPartida;
    }

    public void setPlaAnioPartida(Integer plaAnioPartida) {
        this.plaAnioPartida = plaAnioPartida;
    }

    public LocalDateTime getPlaUltModFecha() {
        return plaUltModFecha;
    }

    public void setPlaUltModFecha(LocalDateTime plaUltModFecha) {
        this.plaUltModFecha = plaUltModFecha;
    }

    public String getPlaUltModUsuario() {
        return plaUltModUsuario;
    }

    public void setPlaUltModUsuario(String plaUltModUsuario) {
        this.plaUltModUsuario = plaUltModUsuario;
    }

    public Integer getPlaVersion() {
        return plaVersion;
    }

    public void setPlaVersion(Integer plaVersion) {
        this.plaVersion = plaVersion;
    }

    public SgSede getPlaSedeFk() {
        return plaSedeFk;
    }

    public void setPlaSedeFk(SgSede plaSedeFk) {
        this.plaSedeFk = plaSedeFk;
    }

    public String getPlaNombreBusqueda() {
        return plaNombreBusqueda;
    }

    public void setPlaNombreBusqueda(String plaNombreBusqueda) {
        this.plaNombreBusqueda = plaNombreBusqueda;
    }

    public String getPlaCodigoBusqueda() {
        return plaCodigoBusqueda;
    }

    public void setPlaCodigoBusqueda(String plaCodigoBusqueda) {
        this.plaCodigoBusqueda = plaCodigoBusqueda;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plaPk != null ? plaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgPlaza)) {
            return false;
        }
        SgPlaza other = (SgPlaza) object;
        if ((this.plaPk == null && other.plaPk != null) || (this.plaPk != null && !this.plaPk.equals(other.plaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgPlaza[ plaPk=" + plaPk + " ]";
    }
    
}
