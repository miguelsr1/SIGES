/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.hibernate.envers.RevisionListener;
import sv.gob.mined.siges.dto.RevEntity;

/**
 *
 * @author Sofis Solutions
 */
public class RevListener implements RevisionListener {

    private static final Logger LOGGER = Logger.getLogger(RevListener.class.getName());

    @Override
    public void newRevision(Object revisionEntity) {
        try {
            JsonWebToken token = Lookup.obtenerJWT();
            RevEntity revEntity = (RevEntity) revisionEntity;
            revEntity.setUsuario(token.getSubject());
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, ex.getMessage(), ex);
        }
    }

}
