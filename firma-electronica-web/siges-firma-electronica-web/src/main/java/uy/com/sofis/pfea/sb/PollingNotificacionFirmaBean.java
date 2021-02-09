package uy.com.sofis.pfea.sb;

import com.sofis.persistence.dao.GeneralDAO;
import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import uy.com.sofis.pfea.anotaciones.JPADAO;
import uy.com.sofis.pfea.entities.Documento;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@Startup
@Singleton
public class PollingNotificacionFirmaBean {

    private static Logger logger = Logger.getLogger(PollingNotificacionFirmaBean.class.getName());
    private static long LOCK_ID = 19202122232425L;

    @Inject
    @JPADAO
    private GeneralDAO generalDAO;

    @EJB
    DocumentosSB docsSB;

    @Resource
    private SessionContext ctx;

    @PostConstruct
    public void init() {

    }

    @SuppressWarnings("unchecked")
    @Schedule(second="0", minute="*/5", hour="*", persistent=false)
//    @Schedule(hour = "*", minute = "*", second = "*/10", persistent = false)
    public void verificarNotificacionExitosaFirma() {

        try {
            generalDAO.lockGlobalById(LOCK_ID);

            // obtiene los documentos que no han podido notificarse
            // correctamente
            List<Documento> result = (List<Documento>) generalDAO.findListByQuery("SELECT d FROM Documento d WHERE d.respuestaError = true and "
                    + "(d.retorno like 'http://%' or d.retorno like 'https://%')", 20);

            result.forEach(doc -> {
                docsSB.enviarNotificacionDocumento(doc);
            });

        } catch (DAOGeneralException e) {
            e.printStackTrace();
        } finally {
            generalDAO.unlockGlobalById(LOCK_ID);
        }
    }
}
