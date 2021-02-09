/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siges.entities.data.impl.SgAfTipoBienes;
import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import gob.mined.siap2.ws.to.DataFile;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.PostLoad;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */


@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_insumo",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"ins_cod"})
        }
)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class Insumo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_insumo_gen")
    @SequenceGenerator(name = "seq_insumo_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_insumo", allocationSize = 1)
    @Column(name = "ins_id")
    private Integer id;

    @Column(name = "ins_cod")
    private String codigo;

    @Column(name = "ins_nombre")
    private String nombre;
   
    @Column(name = "ins_descr")
    private String descripcion;
    
    @Column(name = "ins_conocido_por")
    private String conocidoPor;
    
    @Column(name = "ins_aplica_a_centros_educativos")
    private Boolean aplicaACentrosEducativos;
    
    @Column(name = "ins_corresponde_activo_fijo")
    private Boolean correspondeActivoFijo;

    @ManyToOne
    @JoinColumn(name = "ins_tipo_bien")
    private SgAfTipoBienes tipoBien;
    
    @ManyToOne
    @JoinColumn(name = "ins_articulo")
    private CodificacionInsumo4Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "ins_oeg_cod")
    private ObjetoEspecificoGasto objetoEspecificoGasto;

    @OneToOne
    @JoinColumn(name = "pro_archivo")
    private Archivo archivo;

    @Transient
    private DataFile tempUploadedFile;

    @ManyToOne
    @JoinColumn(name = "ins_plantilla")
    private NodoPlantillaDeInsumos plnatillaDeInsumos;

    //Auditoria
    @Column(name = "ins_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "ins_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "ins_version")
    @Version
    private Integer version;

    public SgAfTipoBienes getTipoBien() {
        return tipoBien;
    }

    public void setTipoBien(SgAfTipoBienes tipoBien) {
        this.tipoBien = tipoBien;
    }

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Archivo getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    public DataFile getTempUploadedFile() {
        return tempUploadedFile;
    }

    public void setTempUploadedFile(DataFile tempUploadedFile) {
        this.tempUploadedFile = tempUploadedFile;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CodificacionInsumo4Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(CodificacionInsumo4Articulo articulo) {
        this.articulo = articulo;
    }

    public ObjetoEspecificoGasto getObjetoEspecificoGasto() {
        return objetoEspecificoGasto;
    }

    public void setObjetoEspecificoGasto(ObjetoEspecificoGasto objetoEspecificoGasto) {
        this.objetoEspecificoGasto = objetoEspecificoGasto;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public NodoPlantillaDeInsumos getPlnatillaDeInsumos() {
        return plnatillaDeInsumos;
    }

    public void setPlnatillaDeInsumos(NodoPlantillaDeInsumos plnatillaDeInsumos) {
        this.plnatillaDeInsumos = plnatillaDeInsumos;
    }

    public Boolean getAplicaACentrosEducativos() {
        return aplicaACentrosEducativos;
    }

    public void setAplicaACentrosEducativos(Boolean aplicaACentrosEducativos) {
        this.aplicaACentrosEducativos = aplicaACentrosEducativos;
    }

    public Boolean getCorrespondeActivoFijo() {
        return correspondeActivoFijo;
    }

    public void setCorrespondeActivoFijo(Boolean correspondeActivoFijo) {
        this.correspondeActivoFijo = correspondeActivoFijo;
    }
    public String getConocidoPor() {
        return conocidoPor;
    }

    public void setConocidoPor(String conocidoPor) {
        this.conocidoPor = conocidoPor;
    }
    // </editor-fold>
    
    @PostLoad
    public void cargarDatos() {
        if(this.tipoBien != null) {
            String codigoNombreCategoria = this.tipoBien.getTbiCodigo() != null ? this.tipoBien.getTbiCodigo().trim() : "";
            if(StringUtils.isNotBlank(this.tipoBien.getTbiNombre())) {
                codigoNombreCategoria += "-" + this.tipoBien.getTbiNombre();
            }
            if(this.tipoBien.getTbiCategoriaBienes() != null && this.tipoBien.getTbiCategoriaBienes().getCabNombre() != null 
                    && StringUtils.isNotBlank(this.tipoBien.getTbiCategoriaBienes().getCabNombre())) {
                codigoNombreCategoria += "-" + this.tipoBien.getTbiCategoriaBienes().getCabNombre().trim();
            }
            this.tipoBien.setTbiCodigoNombreCategoria(codigoNombreCategoria);
        }
    }
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Insumo other = (Insumo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
