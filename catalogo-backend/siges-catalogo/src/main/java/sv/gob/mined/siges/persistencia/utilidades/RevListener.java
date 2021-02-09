/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import org.hibernate.envers.RevisionListener;


/**
 *
 * @author Sofis Solutions
 */
public class RevListener implements RevisionListener{

    @Override
    public void newRevision(Object revisionEntity) {
        //RevEntity exampleRevEntity = (RevEntity) revisionEntity;
        //exampleRevEntity.setUsuario(username);
    }
    
}
