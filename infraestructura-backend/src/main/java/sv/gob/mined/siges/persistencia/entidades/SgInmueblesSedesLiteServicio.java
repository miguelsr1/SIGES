/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.enumerados.EnumEstado;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgInfTipoAbastecimiento;

@Entity
@Table(name = "sg_inmuebles_sedes", schema = Constantes.SCHEMA_INFRAESTRUCTURA,uniqueConstraints = {
    @UniqueConstraint(name = "tis_codigo_uk", columnNames = {"tis_codigo"})})
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property = "tisPk", scope = SgInmueblesSedesLiteServicio.class)
@Audited
public class SgInmueblesSedesLiteServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tis_pk")
    private Long tisPk;

    
    @Column(name = "tis_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tisUltModFecha;
    
    @Column(name = "tis_version")
    @Version
    private Integer tisVersion;
    
    @OneToMany(mappedBy = "ritInmuebleSede", cascade = CascadeType.ALL, orphanRemoval = true)  
    @NotAudited
    private List<SgRelInmuebleServicioSanitario> tisServicioSanitario; 
    
    @OneToMany(mappedBy = "iaaInmuebleSede", cascade = CascadeType.ALL, orphanRemoval = true)  
    @NotAudited
    private List<SgRelInmuebleAbastecimientoAgua> tisAbastecimientoAgua; 
   
    @OneToMany(mappedBy = "iacInmuebleSede", cascade = CascadeType.ALL, orphanRemoval = true)  
    @NotAudited
    private List<SgRelInmuebleAccesibilidad> tisAccesibilidad; 
    
    @OneToMany(mappedBy = "ialInmuebleSede", cascade = CascadeType.ALL, orphanRemoval = true)  
    @NotAudited
    private List<SgRelInmuebleAlmacenamientoAgua> tisAlmacenamientoAgua; 
    
    @OneToMany(mappedBy = "imdInmuebleSede", cascade = CascadeType.ALL, orphanRemoval = true)  
    @NotAudited
    private List<SgRelInmuebleManejoDesechos> tisInmuebleManejoDesechos; 
    
    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sg_inmueble_tipo_abastecimiento",
            schema = Constantes.SCHEMA_INFRAESTRUCTURA,
            joinColumns = @JoinColumn(name = "tis_pk"),
            inverseJoinColumns = @JoinColumn(name = "tab_pk"))
    @NotAudited
    private List<SgInfTipoAbastecimiento> tisTipoAbastecimiento;
    
    @OneToMany(mappedBy = "itdInmuebleSede", cascade = CascadeType.ALL, orphanRemoval = true)  
    @NotAudited
    private List<SgRelInmuebleTipoDrenaje> tisTipoDrenaje; 

    @Column(name = "tis_tiene_transformador")
    private Boolean tisTieneTransformador;
    
    @Column(name = "tis_capacidad_transformador")
    private Integer tisCapacidadTransformador;
    
    @Column(name = "tis_condiciones_instalaciones_elec")
    @Enumerated(value = EnumType.STRING)
    private EnumEstado tisCondicionesInstalacionesElec;
    
    @Column(name = "tis_posee_area_acopio")
    private Boolean tisPoseeAreaAcopio;
    
    @Column(name = "tis_condiciones_acceso")
    private String tisCondicionesAcceso;

    
    public SgInmueblesSedesLiteServicio() {
    }

    public Long getTisPk() {
        return tisPk;
    }

    public void setTisPk(Long tisPk) {
        this.tisPk = tisPk;
    }

    public LocalDateTime getTisUltModFecha() {
        return tisUltModFecha;
    }

    public void setTisUltModFecha(LocalDateTime tisUltModFecha) {
        this.tisUltModFecha = tisUltModFecha;
    }

    public Integer getTisVersion() {
        return tisVersion;
    }

    public void setTisVersion(Integer tisVersion) {
        this.tisVersion = tisVersion;
    }

    public List<SgRelInmuebleServicioSanitario> getTisServicioSanitario() {
        return tisServicioSanitario;
    }

    public void setTisServicioSanitario(List<SgRelInmuebleServicioSanitario> tisServicioSanitario) {
        this.tisServicioSanitario = tisServicioSanitario;
    }

    public Boolean getTisTieneTransformador() {
        return tisTieneTransformador;
    }

    public void setTisTieneTransformador(Boolean tisTieneTransformador) {
        this.tisTieneTransformador = tisTieneTransformador;
    }

    public Integer getTisCapacidadTransformador() {
        return tisCapacidadTransformador;
    }

    public void setTisCapacidadTransformador(Integer tisCapacidadTransformador) {
        this.tisCapacidadTransformador = tisCapacidadTransformador;
    }

    public EnumEstado getTisCondicionesInstalacionesElec() {
        return tisCondicionesInstalacionesElec;
    }

    public void setTisCondicionesInstalacionesElec(EnumEstado tisCondicionesInstalacionesElec) {
        this.tisCondicionesInstalacionesElec = tisCondicionesInstalacionesElec;
    }

    public Boolean getTisPoseeAreaAcopio() {
        return tisPoseeAreaAcopio;
    }

    public void setTisPoseeAreaAcopio(Boolean tisPoseeAreaAcopio) {
        this.tisPoseeAreaAcopio = tisPoseeAreaAcopio;
    }

    public String getTisCondicionesAcceso() {
        return tisCondicionesAcceso;
    }

    public void setTisCondicionesAcceso(String tisCondicionesAcceso) {
        this.tisCondicionesAcceso = tisCondicionesAcceso;
    }

    public List<SgRelInmuebleAbastecimientoAgua> getTisAbastecimientoAgua() {
        return tisAbastecimientoAgua;
    }

    public void setTisAbastecimientoAgua(List<SgRelInmuebleAbastecimientoAgua> tisAbastecimientoAgua) {
        this.tisAbastecimientoAgua = tisAbastecimientoAgua;
    }

    public List<SgRelInmuebleAccesibilidad> getTisAccesibilidad() {
        return tisAccesibilidad;
    }

    public void setTisAccesibilidad(List<SgRelInmuebleAccesibilidad> tisAccesibilidad) {
        this.tisAccesibilidad = tisAccesibilidad;
    }

    public List<SgRelInmuebleAlmacenamientoAgua> getTisAlmacenamientoAgua() {
        return tisAlmacenamientoAgua;
    }

    public void setTisAlmacenamientoAgua(List<SgRelInmuebleAlmacenamientoAgua> tisAlmacenamientoAgua) {
        this.tisAlmacenamientoAgua = tisAlmacenamientoAgua;
    }

    public List<SgRelInmuebleManejoDesechos> getTisInmuebleManejoDesechos() {
        return tisInmuebleManejoDesechos;
    }

    public void setTisInmuebleManejoDesechos(List<SgRelInmuebleManejoDesechos> tisInmuebleManejoDesechos) {
        this.tisInmuebleManejoDesechos = tisInmuebleManejoDesechos;
    }

    public List<SgInfTipoAbastecimiento> getTisTipoAbastecimiento() {
        return tisTipoAbastecimiento;
    }

    public void setTisTipoAbastecimiento(List<SgInfTipoAbastecimiento> tisTipoAbastecimiento) {
        this.tisTipoAbastecimiento = tisTipoAbastecimiento;
    }


    public List<SgRelInmuebleTipoDrenaje> getTisTipoDrenaje() {
        return tisTipoDrenaje;
    }

    public void setTisTipoDrenaje(List<SgRelInmuebleTipoDrenaje> tisTipoDrenaje) {
        this.tisTipoDrenaje = tisTipoDrenaje;
    }

 

    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.tisPk);
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
        final SgInmueblesSedesLiteServicio other = (SgInmueblesSedesLiteServicio) obj;
        if (!Objects.equals(this.tisPk, other.tisPk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgInmueblesSedes[ tisPk=" + tisPk + " ]";
    }

  
    
}
