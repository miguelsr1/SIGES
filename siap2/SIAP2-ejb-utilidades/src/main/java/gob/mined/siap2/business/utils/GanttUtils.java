/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.utils;

import gob.mined.siap2.business.datatype.gantt.DataGantt;
import gob.mined.siap2.business.datatype.gantt.DataGanttTask;
import gob.mined.siap2.entities.data.impl.Gantt;
import gob.mined.siap2.entities.data.impl.GanttTask;
import gob.mined.siap2.entities.enums.TipoTarea;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Sofis Solutions
 */
public class GanttUtils {

    public static Gantt getGantt(DataGantt dataGantt) {
        Gantt g = new Gantt();
        g.setTasks(new LinkedList());
        g.setId(dataGantt.getDatabaseId());
        g.setVersion(dataGantt.getDatabaseVersion());

        for (DataGanttTask dataTask : dataGantt.getTasks()) {
            GanttTask gt = new GanttTask();
            gt.setId(dataTask.getDatabaseId());
            gt.setVersion(dataTask.getDatabaseVersion());

            gt.setJsId(dataTask.getId());
            gt.setName(dataTask.getName());
            gt.setProgress(dataTask.getProgress());
            gt.setDescription(dataTask.getDescription());
            gt.setLevel(dataTask.getLevel());
            gt.setStatus(dataTask.getStatus());
            gt.setDepends(dataTask.getDepends());
            gt.setStart(dataTask.getStart());
            gt.setEnd(dataTask.getEnd());
            gt.setDuration(dataTask.getDuration());
            gt.setStartIsMilestone(dataTask.getStartIsMilestone());
            gt.setEndIsMilestone(dataTask.getEndIsMilestone());
            gt.setHasChild(dataTask.getHasChild());
            gt.setHoraFin(dataTask.getHoraFin());
            try {
                gt.setTipoTarea(TipoTarea.valueOf(dataTask.getCode()));
            } catch (Exception e) {
                gt.setTipoTarea(null);
            }

            g.getTasks().add(gt);
        }

        return g;
    }

    public static DataGantt getDataGantt(Gantt g) {
        DataGantt dataGantt = new DataGantt();
        dataGantt.setDatabaseId(g.getId());
        dataGantt.setDatabaseVersion(g.getVersion());

        dataGantt.setTasks(new LinkedList());
        dataGantt.setSelectedRow(0);
        dataGantt.setCanWrite(true);
        dataGantt.setCanWriteOnParent(true);
        dataGantt.setTasks(new LinkedList());

        for (GanttTask gt : g.getTasks()) {
            DataGanttTask dataTask = new DataGanttTask();
            dataTask.setDatabaseId(gt.getId());
            dataTask.setDatabaseVersion(gt.getVersion());

            dataTask.setId(gt.getJsId());
            dataTask.setName(gt.getName());
            dataTask.setProgress(gt.getProgress());
            dataTask.setDescription(gt.getDescription());

            if (gt.getTipoTarea() == null) {
                dataTask.setCode("");
            } else {
                dataTask.setCode(String.valueOf(gt.getTipoTarea()));
            }

            dataTask.setLevel(gt.getLevel());
            dataTask.setStatus(gt.getStatus());
            dataTask.setDepends(gt.getDepends());
            dataTask.setCanWrite(true);
            dataTask.setStart(gt.getStart());
            dataTask.setEnd(gt.getEnd());
            dataTask.setDuration(gt.getDuration());
            dataTask.setStartIsMilestone(gt.getStartIsMilestone());
            dataTask.setEndIsMilestone(gt.getEndIsMilestone());
            dataTask.setHasChild(gt.getHasChild());
            dataTask.setHoraFin(gt.getHoraFin());

            dataGantt.getTasks().add(dataTask);
        }

        return dataGantt;
    }

    public static Date getFechaMaxima(Gantt g) {
        Date maxGant = null;
        if (g != null && g.getTasks() != null) {
            for (GanttTask t : g.getTasks()) {
                Date tg = new Date(t.getEnd());
                if (maxGant == null || maxGant.compareTo(tg) < 0) {
                    maxGant = tg;
                }
            }
        }
        return maxGant;
    }

    public static Date getFechaMinima(Gantt g) {
        Date minGant = null;
        if (g != null && g.getTasks() != null) {
            for (GanttTask t : g.getTasks()) {
                Date tg = new Date(t.getStart());
                if (minGant == null || minGant.compareTo(tg) > 0) {
                    minGant = tg;
                }
            }
        }
        return minGant;
    }

    public static Date getFechaMinimaRecepcionTDR(Gantt g) {
        Date minGant = null;
        if (g != null && g.getTasks() != null) {
            Iterator<GanttTask> tareaIt = g.getTasks().iterator();
            boolean encontroTarea = false;
            while (tareaIt.hasNext() && !encontroTarea) {
                GanttTask t = tareaIt.next();
                if (t.getTipoTarea() == TipoTarea.RECEPCION_DE_TDR_O_ESPECIFICACIONES_TECNICAS) {
                    encontroTarea = true;
                    Date tg = new Date(t.getStart());
                    if (minGant == null || minGant.compareTo(tg) > 0) {
                        minGant = tg;
                    }
                }
            }
        }
        return minGant;
    }

    public static Gantt copiarGantt(Gantt ganttOrigen) {
        Gantt ganttDestino = new Gantt();
        ganttDestino.setTasks(new LinkedList<GanttTask>());
        for (GanttTask tareaOrigen : ganttOrigen.getTasks()) {
            GanttTask tareaDestino = new GanttTask();
            tareaDestino.setDepends(tareaOrigen.getDepends());
            tareaDestino.setDescription(tareaOrigen.getDescription());
            tareaDestino.setDuration(tareaOrigen.getDuration());
            tareaDestino.setEnd(tareaOrigen.getEnd());
            tareaDestino.setEndIsMilestone(tareaOrigen.getEndIsMilestone());
            tareaDestino.setHasChild(tareaOrigen.getHasChild());
            tareaDestino.setJsId(tareaOrigen.getJsId());
            tareaDestino.setLevel(tareaOrigen.getLevel());
            tareaDestino.setName(tareaOrigen.getName());
            tareaDestino.setPosicion(tareaOrigen.getPosicion());
            tareaDestino.setProgress(tareaOrigen.getProgress());
            tareaDestino.setStart(tareaOrigen.getStart());
            tareaDestino.setStartIsMilestone(tareaOrigen.getStartIsMilestone());
            tareaDestino.setStatus(tareaOrigen.getStatus());
            tareaDestino.setTipoTarea(tareaOrigen.getTipoTarea());
            ganttDestino.getTasks().add(tareaDestino);
        }
        return ganttDestino;
    }
    
    /**
     * Recorre todas las tareas de un Gantt (pasado por parámetro) y se devuelve la que sea del mismo tipo que el
     * pasado por parámetro y tenga la mayo fecha de fin.
     * @param gantt
     * @param tipoTarea
     * @return 
     */
    public static GanttTask obtenerTareaConMayorFechaFinPorTipoTarea(Gantt gantt, TipoTarea tipoTarea){
        GanttTask tareaConMayorFechaFin = null;
        Long fechaFinMayor = Long.MIN_VALUE;
        
        for(GanttTask tarea : gantt.getTasks()){
            if(tarea.getTipoTarea()!=null && tarea.getTipoTarea().equals(tipoTarea)){
                if(tarea.getEnd().compareTo(fechaFinMayor)>0){
                    fechaFinMayor = tarea.getEnd();
                    tareaConMayorFechaFin = tarea;
                }
            }
        }
        
        return tareaConMayorFechaFin;
        
    }

}
