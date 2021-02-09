/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_cod_ins_articulo")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class CodificacionInsumo4Articulo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cod_ins_ar_gen")
    @SequenceGenerator(name = "seq_cod_ins_ar_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_cod_ins_ar", allocationSize = 1)
    @Column(name = "cod_id")
    private Integer id;

    @Column(name = "cod_codigo")
    private String codigo;

    @Column(name = "cod_titulo")
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "cod_padre")
    private CodificacionInsumo3Clase padre;

    //Auditoria
    @Column(name = "cod_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "cod_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "cod_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public CodificacionInsumo3Clase getPadre() {
        return padre;
    }

    public void setPadre(CodificacionInsumo3Clase padre) {
        this.padre = padre;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
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

    // </editor-fold>
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
        final CodificacionInsumo4Articulo other = (CodificacionInsumo4Articulo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
