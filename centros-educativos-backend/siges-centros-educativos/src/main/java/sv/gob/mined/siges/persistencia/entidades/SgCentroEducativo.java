/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoOrganismoAdministrativo;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_sedes_ced", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@Audited
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "sedPk", scope = SgSede.class)
public class SgCentroEducativo extends SgSede implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = true)
    @Column(name = "ced_legalizado")
    private Boolean cedLegalizado;
    
    @Basic(optional = true)
    @Column(name = "ced_fines_de_lucro")
    private Boolean cedFinesDeLucro;
    
    @JoinColumn(name = "ced_tipo_organismo_administrativo_fk", referencedColumnName = "toa_pk")
    @ManyToOne
    private SgTipoOrganismoAdministrativo cedTipoOrganismoAdministrativo;
    

    public SgCentroEducativo() {
        super();
    }
    
    public SgCentroEducativo(Long sedPk) {
        super(sedPk);
    }

    public Boolean getCedLegalizado() {
        return cedLegalizado;
    }

    public void setCedLegalizado(Boolean cedLegalizado) {
        this.cedLegalizado = cedLegalizado;
    }

    public Boolean getCedFinesDeLucro() {
        return cedFinesDeLucro;
    }

    public void setCedFinesDeLucro(Boolean cedFinesDeLucro) {
        this.cedFinesDeLucro = cedFinesDeLucro;
    }

    public SgTipoOrganismoAdministrativo getCedTipoOrganismoAdministrativo() {
        return cedTipoOrganismoAdministrativo;
    }

    public void setCedTipoOrganismoAdministrativo(SgTipoOrganismoAdministrativo cedTipoOrganismoAdministrativo) {
        this.cedTipoOrganismoAdministrativo = cedTipoOrganismoAdministrativo;
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCentroEducativo{" + "sedPk=" + this.getSedPk() + '}';
    }

}
