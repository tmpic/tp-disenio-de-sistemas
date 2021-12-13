//package domain.repositories;
//
//import domain.entities.mascota.CaracteristicasMascotas;
//import domain.repositories.daos.DAO;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//
//public class RepositorioCaracteristicasMascotas extends Repositorio<CaracteristicasMascotas> {
//
//    public RepositorioDeUsuarios(DAO<CaracteristicasMascotas> dao) {
//        super(dao);
//    }
//
//    public Boolean existe(String nombreDeUsuario, String contrasenia) {
//        return buscarUsuario(nombreDeUsuario, contrasenia) != null;
//    }
//
//    public Usuario buscarUsuario(String nombreDeUsuario, String contrasenia) {
//        return this.dao.buscar(condicionUsuarioYContrasenia(nombreDeUsuario, contrasenia));
//    }
//
//    private BusquedaCondicional condicionUsuarioYContrasenia(String nombreDeUsuario, String contrasenia){
//        CriteriaBuilder criteriaBuilder = criteriaBuilder();
//        CriteriaQuery<CaracteristicasMascotas> usuarioQuery = criteriaBuilder.createQuery(CaracteristicasMascotas.class);
//
//        Root<CaracteristicasMascotas> condicionRaiz = usuarioQuery.from(CaracteristicasMascotas.class);
//
//        return new BusquedaCondicional(null, usuarioQuery);
//    }
//}
