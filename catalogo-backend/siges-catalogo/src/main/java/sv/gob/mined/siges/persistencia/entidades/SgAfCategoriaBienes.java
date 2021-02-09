/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_categoria_bienes", uniqueConstraints = {
    @UniqueConstraint(name = "cab_codigo_uk", columnNames = {"cab_codigo"})
    ,
    @UniqueConstraint(name = "cab_nombre_uk", columnNames = {"cab_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cabPk", scope = SgAfCategoriaBienes.class)
public class SgAfCategoriaBienes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cab_pk", nullable = false)
    private Long cabPk;

    @Size(max = 4)
    @Column(name = "cab_codigo", length = 4)
    @AtributoCodigo
    private String cabCodigo;

    @Size(max = 255)
    @Column(name = "cab_nombre", length = 255)
    @AtributoNormalizable
    private String cabNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "cab_nombre_busqueda", length = 255)
    private String cabNombreBusqueda;

    @Column(name = "cab_vida_util")
    private Integer cabVidaUtil;
    
    @Column(name = "cab_porcentaje_valor_residual")
    private Integer cabPorcentajeValorResidual;
    
    @Column(name = "cab_valor_maximo")
    private BigDecimal cabValorMaximo;
    
    @Column(name = "cab_habilitado")
    @AtributoHabilitado
    private Boolean cabHabilitado;
    
    @Column(name = "cab_secciones_bienes", nullable = true)
    @Type(type = "sv.gob.mined.siges.persistencia.utilidades.StringArrayType")
    private String[] cabSeccionBienes;
    
    @Size(max = 50)
    @Column(name = "cab_path_cargar", length = 50)
    private String cabPathCargar;
    
    @JoinColumn(name = "cab_clasificacion_bienes_fk", referencedColumnName = "cbi_pk")
    @ManyToOne
    private SgAfClasificacionBienes cabClasificacionBienes;
    
    @Column(name = "cab_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cabUltModFecha;

    @Size(max = 45)
    @Column(name = "cab_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cabUltModUsuario;

    @Column(name = "cab_version")
    @Version
    private Integer cabVersion;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "scaCategoriaFk", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SgAfSeccionCategoria> sgAfSeccionesCategoriaList;
    
    public SgAfCategoriaBienes() {
        this.sgAfSeccionesCategoriaList = new ArrayList();
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cabNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cabNombre);
    }

    public Long getCabPk() {
        return cabPk;
    }

    public void setCabPk(Long cabPk) {
        this.cabPk = cabPk;
    }

    public String getCabCodigo() {
        return cabCodigo;
    }

    public void setCabCodigo(String cabCodigo) {
        this.cabCodigo = cabCodigo;
    }

    public String getCabNombre() {
        return cabNombre;
    }

    public void setCabNombre(String cabNombre) {
        this.cabNombre = cabNombre;
    }

    public String getCabNombreBusqueda() {
        return cabNombreBusqueda;
    }

    public void setCabNombreBusqueda(String cabNombreBusqueda) {
        this.cabNombreBusqueda = cabNombreBusqueda;
    }

    public Boolean getCabHabilitado() {
        return cabHabilitado;
    }

    public void setCabHabilitado(Boolean cabHabilitado) {
        this.cabHabilitado = cabHabilitado;
    }

    public SgAfClasificacionBienes getCabClasificacionBienes() {
        return cabClasificacionBienes;
    }

    public void setCabClasificacionBienes(SgAfClasificacionBienes cabClasificacionBienes) {
        this.cabClasificacionBienes = cabClasificacionBienes;
    }

    public LocalDateTime getCabUltModFecha() {
        return cabUltModFecha;
    }

    public void setCabUltModFecha(LocalDateTime cabUltModFecha) {
        this.cabUltModFecha = cabUltModFecha;
    }

    public String getCabUltModUsuario() {
        return cabUltModUsuario;
    }

    public void setCabUltModUsuario(String cabUltModUsuario) {
        this.cabUltModUsuario = cabUltModUsuario;
    }

    public Integer getCabVersion() {
        return cabVersion;
    }

    public void setCabVersion(Integer cabVersion) {
        this.cabVersion = cabVersion;
    }

    public Integer getCabVidaUtil() {
        return cabVidaUtil;
    }

    public void setCabVidaUtil(Integer cabVidaUtil) {
        this.cabVidaUtil = cabVidaUtil;
    }

    public String[] getCabSeccionBienes() {
        return cabSeccionBienes;
    }

    public void setCabSeccionBienes(String[] cabSeccionBienes) {
        this.cabSeccionBienes = cabSeccionBienes;
    }

    public Integer getCabPorcentajeValorResidual() {
        return cabPorcentajeValorResidual;
    }

    public void setCabPorcentajeValorResidual(Integer cabPorcentajeValorResidual) {
        this.cabPorcentajeValorResidual = cabPorcentajeValorResidual;
    }

    public String getCabPathCargar() {
        return cabPathCargar;
    }

    public void setCabPathCargar(String cabPathCargar) {
        this.cabPathCargar = cabPathCargar;
    }

    public List<SgAfSeccionCategoria> getSgAfSeccionesCategoriaList() {
        return sgAfSeccionesCategoriaList;
    }

    public void setSgAfSeccionesCategoriaList(List<SgAfSeccionCategoria> sgAfSeccionesCategoriaList) {
        this.sgAfSeccionesCategoriaList = sgAfSeccionesCategoriaList;
    }

    public BigDecimal getCabValorMaximo() {
        return cabValorMaximo;
    }

    public void setCabValorMaximo(BigDecimal cabValorMaximo) {
        this.cabValorMaximo = cabValorMaximo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.cabPk);
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
        final SgAfCategoriaBienes other = (SgAfCategoriaBienes) obj;
        if (!Objects.equals(this.cabPk, other.cabPk)) {
            return false;
        }
        return true;
    }
 
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCategoriaBienes[ cabPk =" + cabPk + " ]";
    }

}
