package domain.entities.recomendaciones;

import domain.controllers.PublicacionesController;
import domain.entities.publicacion.PublicacionPedirAdopcion;
import domain.entities.publicacion.Publicaciones;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class RecomendacionesSemanalesJob implements Job {
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println("--JOB--");
        PublicacionesController publicacionesRepository = new PublicacionesController();
        publicacionesRepository.sugerirAdopcion();
    }
}
