/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import sv.gob.mined.siges.persistencia.utilidades.RevListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@RevisionEntity(RevListener.class)
@Table(name = "revinfo")
/**
 * Clase de revisions para envers
 */
public class RevEntity {

    @Id
    @GeneratedValue
    @RevisionNumber
    private Long rev;

    @RevisionTimestamp
    private Long revtstmp;

    private String usuario;

    public Long getRev() {
        return rev;
    }

    public void setRev(Long rev) {
        this.rev = rev;
    }

    public Long getRevtstmp() {
        return revtstmp;
    }

    public void setRevtstmp(Long revtstmp) {
        this.revtstmp = revtstmp;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}
