package domain.entities.recomendaciones;

import configuracion.Configuracion;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;

import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class RecomendacionesSemanalesScheduler {
    public  static void CrearScheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail job = newJob(RecomendacionesSemanalesJob.class)
                .withIdentity("myJob", "group1")
                .build();


        int intervalo = Integer.parseInt(Configuracion.INSTANCE.getPropiedad("scheduler.intervalo"));
        String unidad = Configuracion.INSTANCE.getPropiedad("scheduler.unidad");


        Trigger trigger= newTrigger()
                .withIdentity("myJob", "group1")
                .withSchedule(calendarIntervalSchedule().withInterval(intervalo, DateBuilder.IntervalUnit.valueOf(unidad)))
                .build();

        scheduler.scheduleJob(job, trigger);

        scheduler.start();
    }
}
