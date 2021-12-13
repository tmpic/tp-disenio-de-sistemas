package server;

import domain.entities.recomendaciones.RecomendacionesSemanalesScheduler;
import org.quartz.SchedulerException;
import spark.Spark;
import spark.debug.DebugScreen;

public class Server {
    public static void main(String[] args) throws SchedulerException {
        RecomendacionesSemanalesScheduler.CrearScheduler();
        Spark.port(getHerokuAssignedPort());//Este es el host
        Router.init();
        DebugScreen.enableDebugScreen();//TODO Fletar en la exposicion final
    }
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
