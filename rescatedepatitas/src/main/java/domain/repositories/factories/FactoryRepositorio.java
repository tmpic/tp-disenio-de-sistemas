package domain.repositories.factories;

import configuracion.Config;
import domain.repositories.Repositorio;
import domain.repositories.daos.*;


import java.util.HashMap;

public class FactoryRepositorio {
    private static HashMap<String, Repositorio> repos;

    static {
        repos = new HashMap<>();
    }

    public static <T> Repositorio<T> get(Class<T> type){
        Repositorio<T> repo;
        if(repos.containsKey(type.getName())){
            repo = repos.get(type.getName());
        }
        else{
                DAO<T> dao = new DAOHibernate<>(type);
                repo = new Repositorio<>(dao);
            repos.put(type.toString(), repo);
        }
        return repo;

    }
}
