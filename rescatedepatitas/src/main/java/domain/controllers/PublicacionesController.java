package domain.controllers;

import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
import domain.entities.Organizacion.Organizacion;
import domain.entities.ResizerDeImagenes.ResizerDeImagen;
import domain.entities.formaDeNotificacion.NotificadorPorMail;
import domain.entities.formaDeNotificacion.NotificadorPorSMS;
import domain.entities.formaDeNotificacion.NotificadorPorWhatsApp;
import domain.entities.hogar.Ubicacion;
import domain.entities.mascota.*;
import domain.entities.otros.Foto;
import domain.entities.personas.*;
import domain.entities.otros.TipoDeDocumento;
import domain.entities.publicacion.*;
import domain.repositories.Repositorio;
import domain.repositories.factories.FactoryRepositorio;
import org.apache.commons.io.FileUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static spark.Spark.halt;

public class PublicacionesController {

    private Repositorio<PublicacionDarEnAdopcion> repositorio;
    private Repositorio<PublicacionPedirAdopcion> repoAdoptantes;
    private Repositorio<PublicacionDeMascotaEncontrada> repoPublicacionesMascotaEncontrada;
    private Repositorio<Publicacion> repoPublicaciones;
    private Repositorio<Rescatista> repoRescatista;
    private Repositorio<Voluntario> repoVoluntario;
    private Repositorio<Mascota> repositorioMascota;
    private Repositorio<CaracteristicasMascotas> repositorioCaracteristicasMascotas;
    private Repositorio<Organizacion> repositorioOrganizaciones;
    private Repositorio<Duenio> reposDuenio;
    private Repositorio<Especie> repositorioEspecie;
    private Repositorio<Raza> repoRazas;
    private Repositorio<Caracteristica> repositorioCaracteristicas;

    public PublicacionesController() {
        this.repositorio = FactoryRepositorio.get(PublicacionDarEnAdopcion.class);
        this.repoPublicacionesMascotaEncontrada = FactoryRepositorio.get(PublicacionDeMascotaEncontrada.class);
        this.repoAdoptantes = FactoryRepositorio.get(PublicacionPedirAdopcion.class);
        this.repoPublicaciones = FactoryRepositorio.get(Publicacion.class);
        this.repoRescatista = FactoryRepositorio.get(Rescatista.class);
        this.repositorioMascota = FactoryRepositorio.get(Mascota.class);
        this.repoVoluntario = FactoryRepositorio.get(Voluntario.class);
        this.repositorioCaracteristicasMascotas = FactoryRepositorio.get(CaracteristicasMascotas.class);
        this.repositorioOrganizaciones = FactoryRepositorio.get((Organizacion.class));
        this.reposDuenio = FactoryRepositorio.get((Duenio.class));
        this.repositorioOrganizaciones = FactoryRepositorio.get(Organizacion.class);
        this.repositorioEspecie = FactoryRepositorio.get(Especie.class);
        this.repoRazas = FactoryRepositorio.get(Raza.class);
        this.repositorioCaracteristicas = FactoryRepositorio.get(Caracteristica.class);
    }

    public ModelAndView crearAdoptantePub(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        List<Especie> especies = this.repositorioEspecie.buscarTodos();
        List<Organizacion> organizaciones = this.repositorioOrganizaciones.buscarTodos();

        parametros.put("organizaciones", organizaciones);
        parametros.put("especies", especies);

        return new ModelAndView(parametros, "crearPublicacionQuiereAdoptarMascota.hbs");
    }

    public Response generarPublicacionAdopcion(Request request, Response response) {

        Duenio futuro_duenio = reposDuenio.buscar(new Long(request.session().attribute("idUsuario").toString()));

        List<CaracteristicasMascotas> preferencias = new ArrayList<CaracteristicasMascotas>();
        Boolean patioBool = false;

        Organizacion org = this.repositorioOrganizaciones.buscar(new Integer(request.queryParams("organizacion")));

        Especie especie = this.repositorioEspecie.buscar(new Long(request.queryParams("especie")));

        Raza raza = this.repoRazas.buscar(new Long(request.queryParams("raza")));
        String sexo = request.queryParams("sexoValue");

        String patio = request.queryParams("patioValue");
        String tamanio = request.queryParams("tamanio");
        String otrasMascotas = request.queryParams("mascotasValue");
        String juguetes = request.queryParams("JuguetesValue");
        String paseo = request.queryParams("paseoValue");
        String pelaje = request.queryParams("PelajeValue");
        String energia = request.queryParams("energiaMascota");
        String obediencia = request.queryParams("obedienciaValue");

        if(patio.equals("Si")){
            patioBool = true;
        }
        List<Caracteristica> lista_caracteristicas = this.repositorioCaracteristicas.buscarTodos();

        Caracteristica tamanioCaracteristica = lista_caracteristicas.stream().filter(c -> c.getUnaCaracteristica().equals("Tamanio")).findFirst().get();
        CaracteristicasMascotas tamanioMascota = new CaracteristicasMascotas();
        tamanioMascota.setCaracteristica(tamanioCaracteristica);
        tamanioMascota.setDescripcion(tamanio);

        //CaracteristicasMascotas mascotas = new CaracteristicasMascotas();
        //mascotas.setDescripcion(otrasMascotas);

        //CaracteristicasMascotas tieneJuguetes = new CaracteristicasMascotas();
        //tieneJuguetes.setDescripcion(juguetes);

        //CaracteristicasMascotas paseos = new CaracteristicasMascotas();
        //paseos.setDescripcion(paseo);

        Caracteristica pelajeCaracteristica = lista_caracteristicas.stream().filter(c -> c.getUnaCaracteristica().equals("Pelaje")).findFirst().get();
        CaracteristicasMascotas tipoPelaje = new CaracteristicasMascotas();
        tipoPelaje.setCaracteristica(pelajeCaracteristica);
        tipoPelaje.setDescripcion(pelaje);

        Caracteristica energiaCaracteristica = lista_caracteristicas.stream().filter(c -> c.getUnaCaracteristica().equals("Energia")).findFirst().get();
        CaracteristicasMascotas cantidadEnergia = new CaracteristicasMascotas();
        cantidadEnergia.setCaracteristica(energiaCaracteristica);
        cantidadEnergia.setDescripcion(energia);

        Caracteristica conductaCaracteristica = lista_caracteristicas.stream().filter(c -> c.getUnaCaracteristica().equals("Conducta")).findFirst().get();
        CaracteristicasMascotas nivelObediencia = new CaracteristicasMascotas();
        nivelObediencia.setCaracteristica(conductaCaracteristica);
        nivelObediencia.setDescripcion(obediencia);





        preferencias.add(tamanioMascota);
        //preferencias.add(mascotas);
        //preferencias.add(tieneJuguetes);
        //preferencias.add(paseos);
        preferencias.add(tipoPelaje);
        preferencias.add(cantidadEnergia);
        preferencias.add(nivelObediencia);

        PublicacionPedirAdopcion publicacion = new PublicacionPedirAdopcion(futuro_duenio, preferencias, patioBool,"TOKEN", especie, raza, sexo, org.getUbicacion());
        publicacion.setOrganizacion(org);

        this.repoAdoptantes.agregar(publicacion);
        response.redirect("/publicacionesAdoptantes");
        return response;
    }

    public ModelAndView detallePublicacion(Request request, Response response) {
        //throw new NotInstrumentedException("");
        Long id_duenio = new Long(request.session().attribute("idUsuario").toString());
        Long id = new Long(0);
        try {
            id = Long.parseLong(request.params("idPublicacion"));
        } catch (Exception e) {
            response.status(400);
            halt(400, "El id de la publicacion no es valido");
        }

        Publicacion publicacion = this.repoPublicaciones.buscar(id);

        if (publicacion == null) {
            response.status(404);
            halt(400, "La publicacion no existe");
        } else if (!publicacion.autor.getId().equals(id_duenio)) {
            response.status(404);
            halt(400, "Esa publicacion no le pertenece");
        }

        return new ModelAndView(publicacion, "detallePublicacion.hbs");
    }

    public ModelAndView mostrarMisPublicaciones(Request request, Response response) {

        Long id_duenio = new Long(request.session().attribute("idUsuario").toString());
        Map<String, Object> parametros = new HashMap<>();

        List<PublicacionDeMascotaEncontrada> publicacionesRescate = this.repoPublicacionesMascotaEncontrada.buscarTodos().stream().filter(publi -> id_duenio.equals(publi.autor.getId())).collect(Collectors.toList());
        List<PublicacionDarEnAdopcion> publicacionesDarAdopcion = this.repositorio.buscarTodos().stream().filter(publi -> id_duenio.equals(publi.autor.getId())).collect(Collectors.toList());
        List<PublicacionPedirAdopcion> publicacionesAdoptante = this.repoAdoptantes.buscarTodos().stream().filter(publi -> id_duenio.equals(publi.autor.getId())).collect(Collectors.toList());

        parametros.put("publicacionesRescate", publicacionesRescate);
        parametros.put("publicacionesDarAdopcion", publicacionesDarAdopcion);
        parametros.put("publicacionesAdoptante", publicacionesAdoptante);

        return new ModelAndView(parametros, "misPublicaciones.hbs");
    }

    public Object eliminarPublicacion(Request request, Response response) {
        Long numero = new Long(request.params("id"));
        Publicacion publi = this.repoPublicaciones.buscar(numero);//TODO hacer lo mismo que con caracteristicas
        this.repoPublicaciones.eliminar(publi);
        return response;
        /*
        Long numero = new Long(request.params("id"));
        PublicacionDeMascotaEncontrada publi = this.repo.buscar(numero);
        this.repo.eliminar(publi);
        return response;
         */
    }

    public ModelAndView adoptarMascotaPub(Request request, Response response, String mensaje) {
        Map<String, Object> parametros = new HashMap<>();
        Map<String, String> caracteristicasMap = new HashMap<>();
        List<PublicacionDarEnAdopcion> publicaciones = this.repositorio.buscarTodos();
        publicaciones.removeIf(publicacion -> publicacion.estado != Estado.APROBADO);

        String[] filtrosCaracteristicas = request.queryParamsValues("caracteristicas[]");
        if (filtrosCaracteristicas != null) {
            System.out.println("foreach");
            for (String c : filtrosCaracteristicas) {
                String[] c_split = c.split(":");
                if (c_split.length < 2)
                    continue;

                Long caracteristica_id;
                try {
                    caracteristica_id = Long.parseLong(c_split[0]);
                } catch (NumberFormatException e) {
                    continue;
                }

                publicaciones = publicaciones.stream().filter(
                        p -> p.getMascota().getCaracteristicasMascotas().stream().anyMatch(cm -> cm.getCaracteristica().getId().equals(caracteristica_id)
                                && cm.getDescripcion().equalsIgnoreCase(c_split[1])))
                        .collect(Collectors.toList());

                caracteristicasMap.put(c_split[0], c_split[1]);

            }
        }

        String ordenar = request.queryParams("ordenar");
        String sexo = request.queryParams("sexo");
        Long especie, raza;
        try {
            especie = Long.parseLong(request.queryParams("especie"));
        } catch (NumberFormatException e) {
            especie = null;
        }
        try {
            raza = Long.parseLong(request.queryParams("raza"));
        } catch (NumberFormatException e) {
            raza = null;
        }
        List<Raza> razasParam = new ArrayList<Raza>();
        if (sexo != null && !sexo.isEmpty())
            publicaciones = publicaciones.stream().filter(publicacion -> publicacion.getMascota().getSexo().equals(sexo)).collect(Collectors.toList());
        if (especie != null) {
            Long finalEspecie = especie;
            publicaciones = publicaciones.stream().filter(publicacion -> publicacion.getMascota().getEspecie().getId().equals(finalEspecie)).collect(Collectors.toList());
            razasParam = repositorioEspecie.buscar(finalEspecie).getRazas();
        }
        if (raza != null) {
            Long finalRaza = raza;
            publicaciones = publicaciones.stream().filter(publicacion -> publicacion.getMascota().getRaza().getId().equals(finalRaza)).collect(Collectors.toList());
        }

        if (ordenar != null) {
            if (ordenar.equals("1") || ordenar.equals("2")) {
                publicaciones.sort((p1, p2) -> p1.getMascota().getNombre().compareTo(p2.getMascota().getNombre()));
                if (ordenar.equals("2")) {
                    Collections.reverse(publicaciones);
                }
            } else if (ordenar.equals("3")) {
                Collections.reverse(publicaciones);
            }
        }

        Map<Caracteristica, List<CaracteristicasMascotas>> caracteristicas = repositorioCaracteristicasMascotas.buscarTodos().stream().collect(Collectors.groupingBy(caracteristicaMasctoa -> caracteristicaMasctoa.getCaracteristica()));

        if (mensaje != null)
            parametros.put("mensaje", mensaje);
        parametros.put("publicaciones", publicaciones);
        parametros.put("especies", new MascotaController().getEspecies());
        parametros.put("caracteristicasParam", caracteristicas);
        parametros.put("caracteristicasFiltros", caracteristicasMap);
        parametros.put("ordenar", ordenar);
        parametros.put("sexo", sexo);
        parametros.put("especie", especie);
        parametros.put("razas", razasParam);
        parametros.put("raza", raza);
        return new ModelAndView(parametros, "publicacionMascotaEnAdopcion.hbs");
    }

    public ModelAndView inicio(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        List<PublicacionDarEnAdopcion> publicaciones = this.repositorio.buscarTodos();
        parametros.put("publicaciones", publicaciones);
        return new ModelAndView(parametros, "inicio.hbs");
    }

    public ModelAndView animalesPerdidosPub(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        Map<String, String> caracteristicasMap = new HashMap<>();

        List<PublicacionDeMascotaEncontrada> publicaciones = this.repoPublicacionesMascotaEncontrada.buscarTodos();
        publicaciones.removeIf(publicacion -> publicacion.estado != Estado.APROBADO);
        String[] filtrosCaracteristicas = request.queryParamsValues("caracteristicas[]");
        if (filtrosCaracteristicas != null) {
            for (String c : filtrosCaracteristicas) {
                String[] c_split = c.split(":");
                if (c_split.length < 2)
                    continue;

                Long caracteristica_id;
                try {
                    caracteristica_id = Long.parseLong(c_split[0]);
                } catch (NumberFormatException e) {
                    continue;
                }

                publicaciones = publicaciones.stream().filter(
                        p -> p.getCaracteristicas().stream().anyMatch(cm -> cm.getCaracteristica().getId().equals(caracteristica_id)
                                && cm.getDescripcion().equalsIgnoreCase(c_split[1])))
                        .collect(Collectors.toList());

                caracteristicasMap.put(c_split[0], c_split[1]);

            }
        }

        String ordenar = request.queryParams("ordenar");
        String sexo = request.queryParams("sexo");
        Long especie;
        try {
            especie = Long.parseLong(request.queryParams("especie"));
        } catch (NumberFormatException e) {
            especie = null;
        }

        //TODO: CONSULTAR QUE HACEMOS CON EL SEXO Y ESPECIE
        //if (sexo != null && !sexo.isEmpty())
        //    publicaciones = publicaciones.stream().filter(publicacion -> publicacion.getMascota().getSexo().equals(sexo)).collect(Collectors.toList());
//        if (especie != null) {
//            Long finalEspecie = especie;
//            publicaciones = publicaciones.stream().filter(publicacion -> publicacion.getEspecie().getId().equals(finalEspecie)).collect(Collectors.toList());
//        }

        if (ordenar != null) {
            if (ordenar.equals("3")) {
                Collections.reverse(publicaciones);
            }
        }

        Map<Caracteristica, List<CaracteristicasMascotas>> caracteristicas = repositorioCaracteristicasMascotas.buscarTodos().stream().collect(Collectors.groupingBy(caracteristicaMasctoa -> caracteristicaMasctoa.getCaracteristica()));

        parametros.put("mensaje", request.queryParams("mensaje"));
        parametros.put("publicaciones", publicaciones);
        parametros.put("especies", new MascotaController().getEspecies());
        parametros.put("caracteristicasParam", caracteristicas);
        parametros.put("caracteristicasFiltros", caracteristicasMap);
        parametros.put("ordenar", ordenar);
//        parametros.put("sexo", sexo);
//        parametros.put("especie", especie);
        return new ModelAndView(parametros, "publicacionMascotaPerdida.hbs");
    }


    public Response postMascotaEncontrada(Request request, Response response) throws ParseException, UnsupportedEncodingException {
        Rescatista rescatista = generarRescatista(request);
        this.repoRescatista.agregar(rescatista);
        Long id_mascota = new Long(request.params("id"));//recordar que id_mascota no del DUENIO
        Mascota mascota = this.repositorioMascota.buscar(id_mascota);//TODO si al hacer el post, el id de la mascota no existe se parte.
        mascota.getDuenio().contactarMascotaEncontrada(rescatista);//TODO me parece que no esta funcionando con varios mails esto

        response.redirect("/animalesPerdidos");
        return response;
    }

    public ModelAndView getMascotaEncontrada(Request request, Response response) {
        Long id_mascota = new Long(0);
        try {
            id_mascota = new Long(request.params("id"));//recordar que id_mascota no del DUENIO

        } catch (Exception e) {
            response.status(400);
            halt(400, "El id de la mascota no es valido");
        }

        Mascota mascota = this.repositorioMascota.buscar(id_mascota);//TODO si al hacer el post, el id de la mascota no existe se parte.

        //TODO codigo


        if (mascota == null) {
            response.status(404);
            halt(404, "La mascota no existe");
        }

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("id", request.params("id"));
        return new ModelAndView(parametros, "formMascotaEncontrada.hbs");
    }

    public ModelAndView mostrarPendientes(Request request, Response response) {
        Long idVoluntario = request.session().attribute("idUsuario");
        Voluntario voluntario = this.repoVoluntario.buscar(idVoluntario);
        int idOrganizacion = voluntario.getOrganizacion().getId();
        Map<String, Object> parametros = new HashMap<>();
        List<Publicacion> publicacionesPendientes = this.repoPublicaciones.buscarTodos();
        publicacionesPendientes.removeIf(publicacion -> (publicacion.estado != Estado.PENDIENTE) || (publicacion.getOrganizacion().getId() != idOrganizacion));
        parametros.put("publicacionesPendientes", publicacionesPendientes);
        return new ModelAndView(parametros, "publicacionesPendientes.hbs");
    }

    public ModelAndView mostrarHistorial(Request request, Response response) {
        Long idVoluntario = request.session().attribute("idUsuario");
        Voluntario voluntario = this.repoVoluntario.buscar(idVoluntario);
        int idOrganizacion = voluntario.getOrganizacion().getId();
        Map<String, Object> parametros = new HashMap<>();
        List<Publicacion> publicaciones = this.repoPublicaciones.buscarTodos();
        publicaciones.removeIf(publicacion -> (publicacion.estado == Estado.PENDIENTE) || (publicacion.getOrganizacion().getId() != idOrganizacion));
        parametros.put("publicaciones", publicaciones);
        return new ModelAndView(parametros, "publicacionesHistorial.hbs");
    }


    public Void aceptarPublicacion(Request request, Response response) {
        Publicacion publicacion = repoPublicaciones.buscar(Long.valueOf(request.params(":id")));
        publicacion.setEstado(Estado.APROBADO);
        repoPublicaciones.modificar(publicacion);

        response.redirect("/publicaciones/pendientes");

        return null;
    }

    public Void rechazarPublicacion(Request request, Response response) {
        Publicacion publicacion = repoPublicaciones.buscar(Long.valueOf(request.params(":id")));
        publicacion.setEstado(Estado.RECHAZADO);
        repoPublicaciones.modificar(publicacion);

        response.redirect("/publicaciones/pendientes");

        return null;
    }

    public Rescatista generarRescatista(Request request) throws ParseException, UnsupportedEncodingException {

        Rescatista autor = new Rescatista();
//        System.out.println("FUNCIONOOOO: EL OBJETO ES: " + datosDeContacto.getFormasDeNotificacion().stream().f);
        String[] datitos = request.queryParamsValues("datitos[]");
        for (String datito : datitos) {
            String myStringDecoded = URLDecoder.decode(datito, "UTF-8");

            String[] parts = myStringDecoded.split("&");
            JsonObject json = new JsonObject();

            for (String part : parts) {
                String[] keyVal = part.split("="); // The equal separates key and values
                System.out.println(keyVal[0] + " - " + keyVal[1]);
                json.addProperty(keyVal[0], keyVal[1]);
            }

            DatosDeContacto contacto = new DatosDeContacto();
            contacto.setNombreYApellido(json.get("contName").getAsString());
            contacto.setEmail(json.get("contMail").getAsString());
            contacto.setTelefono(json.get("contTel").getAsString());

            if (json.get("whatsapp").getAsBoolean()) {
                contacto.setFormasDeNotificacion(new NotificadorPorWhatsApp());
            }
            if (json.get("mail").getAsBoolean()) {
                contacto.setFormasDeNotificacion(new NotificadorPorMail());
            }
            if (json.get("sms").getAsBoolean()) {
                contacto.setFormasDeNotificacion(new NotificadorPorSMS());
            }

            autor.agregarDatosDeContacto(contacto);
        }


        autor.setNombreYApellido(request.queryParams("nombre") + " " + request.queryParams("apellido"));
        autor.setTipoDocumento(TipoDeDocumento.valueOf(request.queryParams("tipoDocumento")));
        autor.setNumeroDocumento(new Integer(request.queryParams("numeroDeDocumento")));

        String fecha = request.queryParams("fechaDeNacimiento");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
        autor.setFechaDeNacimiento(date);

        autor.setDireccion(request.queryParams("direccion"));
        return autor;
    }

    public Response guardar(Request request, Response response) throws ParseException, IOException, ServletException {
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
        request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);

        Rescatista autor = generarRescatista(request);

        PublicacionDeMascotaEncontrada publicacion = new PublicacionDeMascotaEncontrada();
        publicacion.setEstado(Estado.PENDIENTE);
        publicacion.setAutor(autor);
        publicacion.setDescripcion(request.queryParams("descripcion"));
        publicacion.setFecha(LocalDate.now());

        List<Organizacion> organizaciones = this.repositorioOrganizaciones.buscarTodos();
        Double lat = new Double(request.queryParams("latitud"));
        Double longi = new Double(request.queryParams("longitud"));
        String direc = request.queryParams("direc");
        Ubicacion ubicacion_seleccionada_mapa = new Ubicacion(lat, longi);
        ubicacion_seleccionada_mapa.setDireccion(direc);
        publicacion.setUbicacion(ubicacion_seleccionada_mapa);

        Optional<Organizacion> org_mas_cercana = organizaciones.stream().min(Comparator.comparing(una_org -> una_org.getUbicacion().distanciaCon(ubicacion_seleccionada_mapa)));
        publicacion.setOrganizacion(org_mas_cercana.get());//Ojo con esto, para mi no es null nunca TODO DESCOMENTAR


        Collection<Part> files = request.raw().getParts().stream().filter(file -> file.getName().equals("fotos")).collect(Collectors.toList());

        for (Part file : files) {
            String GUIDwithext = Paths.get(file.getSubmittedFileName()).getFileName().toString();
            String ruta = "target/classes/public/img/" + GUIDwithext;
            String rutaRelativa = "img\\" + GUIDwithext;
            File targetFile = new File(ruta);

            FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);

            ResizerDeImagen resizer = new ResizerDeImagen();
            resizer.normalizarImagen(ruta);


            publicacion.agregarFotos(new Foto(rutaRelativa));
        }

        this.repoPublicacionesMascotaEncontrada.agregar(publicacion);
        response.redirect("/animalesPerdidos");
        return response;
    }

    public ModelAndView formularioMascotaPerdida(Request request, Response response) {
        return new ModelAndView(null, "formulario.hbs");
    }

    public ModelAndView adoptantePub(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        Map<String, String> caracteristicasMap = new HashMap<>();

        List<PublicacionPedirAdopcion> publicaciones = repoAdoptantes.buscarTodos();
        publicaciones.removeIf(publicacion -> publicacion.estado != Estado.APROBADO);

        String[] filtrosCaracteristicas = request.queryParamsValues("caracteristicas[]");
        if (filtrosCaracteristicas != null) {
            System.out.println("foreach");
            for (String c : filtrosCaracteristicas) {
                String[] c_split = c.split(":");
                if (c_split.length < 2)
                    continue;

                Long caracteristica_id;
                try {
                    caracteristica_id = Long.parseLong(c_split[0]);
                } catch (NumberFormatException e) {
                    continue;
                }

                publicaciones = publicaciones.stream().filter(
                        p -> p.getCaracteristicas().stream().anyMatch(cm -> cm.getCaracteristica().getId().equals(caracteristica_id)
                                && cm.getDescripcion().equalsIgnoreCase(c_split[1])))
                        .collect(Collectors.toList());

                caracteristicasMap.put(c_split[0], c_split[1]);

            }
        }

        String ordenar = request.queryParams("ordenar");
        String sexo = request.queryParams("sexo");
        Long especie, raza;
        try {
            especie = Long.parseLong(request.queryParams("especie"));
        } catch (NumberFormatException e) {
            especie = null;
        }
        try {
            raza = Long.parseLong(request.queryParams("raza"));
        } catch (NumberFormatException e) {
            raza = null;
        }
        List<Raza> razasParam = new ArrayList<Raza>();
        //TODO: Descomentar cuando se agrege sexo, especie y raza al formulario
//        if (sexo != null && !sexo.isEmpty())
//            publicaciones = publicaciones.stream().filter(publicacion -> publicacion.getSe().getSexo().equals(sexo)).collect(Collectors.toList());
//        if (especie != null) {
//            Long finalEspecie = especie;
//            publicaciones = publicaciones.stream().filter(publicacion -> publicacion.gete().getEspecie().getId().equals(finalEspecie)).collect(Collectors.toList());
//            razasParam = repositorioEspecie.buscar(finalEspecie).getRazas();
//        }
//        if (raza != null) {
//            Long finalRaza = raza;
//            publicaciones = publicaciones.stream().filter(publicacion -> publicacion.getMascota().getRaza().getId().equals(finalRaza)).collect(Collectors.toList());
//        }

        if (ordenar != null) {
            if (ordenar.equals("3")) {
                Collections.reverse(publicaciones);
            }
        }

        Map<Caracteristica, List<CaracteristicasMascotas>> caracteristicas = repositorioCaracteristicasMascotas.buscarTodos().stream().collect(Collectors.groupingBy(caracteristicaMasctoa -> caracteristicaMasctoa.getCaracteristica()));

        parametros.put("publicaciones", publicaciones);
        parametros.put("especies", new MascotaController().getEspecies());
        parametros.put("caracteristicasParam", caracteristicas);
        parametros.put("caracteristicasFiltros", caracteristicasMap);
        parametros.put("ordenar", ordenar);
        parametros.put("sexo", sexo);
        parametros.put("especie", especie);
        parametros.put("razas", razasParam);
        parametros.put("raza", raza);
        parametros.put("publicaciones", publicaciones);
        return new ModelAndView(parametros, "publicacionQuiereAdoptarMascota.hbs");
    }



    //region Scheduler

    public void sugerir(Publicacion publicacion) {
        List<PublicacionDarEnAdopcion> recomendaciones = this.sugerirPublicacionesPara(publicacion.getCaracteristicas());
        //List<PublicacionDarEnAdopcion> recomendaciones = repositorio.buscarTodos();
        String mensaje = "Hola esta es una lista de recomendaciones:" + this.recomendaciones(recomendaciones).collect(Collectors.toList()) ;

        //TODO aca van los datos de las publicaciones recomendadas y el token para dar de baja la misma

        if (recomendaciones.size() > 0) {
            publicacion.autor.contactar(mensaje);
            //Todo: darle formato al mail
        }
    }

    private Stream<String> recomendaciones(List<PublicacionDarEnAdopcion> publicaciones){
        return publicaciones.stream().map(publicacionDarEnAdopcion -> publicacionDarEnAdopcion.Mensaje());
    }
    public List<PublicacionDarEnAdopcion> sugerirPublicacionesPara(List<CaracteristicasMascotas> preferencias) {
        List<PublicacionDarEnAdopcion> recomendaciones = new ArrayList<PublicacionDarEnAdopcion>();
        try {
            recomendaciones = repositorio.buscarTodos()
                    .stream().filter(p -> p.getEstado().equals(Estado.APROBADO) && (p.getMascota().getCaracteristicasMascotas().stream().anyMatch(c -> preferencias.stream().anyMatch(pr -> pr.getCaracteristica().equals(c.getCaracteristica()) && pr.getDescripcion().equals(c.getDescripcion()))) ||
                            p.getCaracteristicas().stream().anyMatch(c -> preferencias.stream().anyMatch(pr -> pr.getCaracteristica().equals(c.getCaracteristica()) && pr.getDescripcion().equals(c.getDescripcion())))))
                    .collect(Collectors.toList());
            return recomendaciones;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void sugerirAdopcion() {

        repoAdoptantes.buscarTodos().stream().filter(p -> p.getEstado().equals(Estado.APROBADO)).forEach(publicacionPedirAdopcion -> {
            sugerir(publicacionPedirAdopcion);
        });
    }


    //endregion

    //region token
    public Object esMiMascota(Request request, Response response) {
        response.type("application/json");

        try {
            Long id_publicacion = Long.parseLong(request.queryParams("id"));
            PublicacionDeMascotaEncontrada publicacion = repoPublicacionesMascotaEncontrada.buscar(id_publicacion);
            String token = Hashing.md5().newHasher()
                    .putString(publicacion.getId().toString(), Charset.defaultCharset())
                    .hash()
                    .toString();
            Duenio duenio = reposDuenio.buscar(request.session().attribute("idUsuario"));

            publicacion.getAutor().contactarEsMiMascota(duenio, token);

            return "{\"exito\":true}";
        } catch (Exception ex) {
            return "{\"exito\":false, \"mensaje\":\"" + ex.getMessage() + "\"}";

        }
    }

    public Object quieroAdoptar(Request request, Response response) {
        response.type("application/json");
        try {
            Long id_publicacion = Long.parseLong(request.queryParams("id"));
            PublicacionDarEnAdopcion publicacion = repositorio.buscar(id_publicacion);
            String token = Hashing.md5().newHasher()
                    .putString(publicacion.getId().toString(), Charset.defaultCharset())
                    .hash()
                    .toString();
            Duenio duenio = reposDuenio.buscar(request.session().attribute("idUsuario"));
            Mascota mascota = repositorio.buscar(id_publicacion).getMascota();
            publicacion.getAutor().contactarAlguienQuiereAdoptar(duenio, mascota, token);

            return "{\"exito\":true}";
        } catch (Exception ex) {
            return "{\"exito\":false}";
        }

        //return adoptarMascotaPub(request, response, "El dueño de la mascota ha sido contactado.");
    }

    public Object intencionDeAdopcion(Request request, Response response) {
        response.type("application/json");

        try {
            Long id_publicacion = Long.parseLong(request.queryParams("id"));
            PublicacionPedirAdopcion publicacion = repoAdoptantes.buscar(id_publicacion);
            String token = Hashing.md5().newHasher()
                    .putString(publicacion.getId().toString(), Charset.defaultCharset())
                    .hash()
                    .toString();
            //Duenio duenio = reposDuenio.buscar(request.session().attribute("idUsuario"));

            publicacion.getAutor().contactarIntencionDeAdopcion(token);

            return "{\"exito\":true}";
        } catch (Exception ex) {
            return "{\"exito\":false}";
        }
    }

    public String finalizarPublicacion(Request request, Response response) {
        String token = request.params("token");

        Optional<Publicacion> publicacion = repoPublicaciones.buscarTodos().stream().filter(p -> Hashing.md5().newHasher()
                .putString(p.getId().toString(), Charset.defaultCharset())
                .hash()
                .toString().equals(token)).findFirst();

        if (!publicacion.isPresent())
            return "El token no es valido";

        String id_nuevoDuenio = request.queryParams("dn");
        if (id_nuevoDuenio != null) {
            Duenio duenio = reposDuenio.buscar(Long.parseLong(id_nuevoDuenio));
            PublicacionDarEnAdopcion publicacionDarEnAdopcion = repositorio.buscar(publicacion.get().getId());
            Mascota mascota = publicacionDarEnAdopcion.getMascota();
            mascota.setDuenio(duenio);
            mascota.setEstado(EstadoMascota.ENCONTRADA);
            repositorioMascota.modificar(mascota);
        }
        publicacion.get().setEstado(Estado.FINALIZADO);
        repoPublicaciones.modificar(publicacion.get());

        return "La publicación ha sido finalizada con éxito";

    }

    public Void generarFormularioParaDarEnAdopcion(Request request, Response response) {
        PublicacionDarEnAdopcion publicacion = new PublicacionDarEnAdopcion();
        Mascota mascota = repositorioMascota.buscar(Long.parseLong(request.queryParams("mascota")));
        publicacion.setMascota(mascota);
        publicacion.setOrganizacion(mascota.getOrganizacion());
        publicacion.setAutor(mascota.getDuenio());
        publicacion.setEstado(Estado.PENDIENTE);
        publicacion.setFecha(LocalDate.now());

        List<Foto> fotos = mascota.getFotos();
        publicacion.agregarFotos(fotos);

        List<CaracteristicasMascotas> caracterisiticas = mascota.getCaracteristicasMascotas();
        publicacion.agregarCaracteristicas(caracterisiticas);
        try {
            publicacion.setDescripcion(request.queryParams("observaciones"));
        } catch (NullPointerException e) {
            publicacion.setDescripcion("Publicacion para dar en adopcio a " + mascota.getNombre() + ".");
        }
        publicacion.setUbicacion(mascota.getOrganizacion().getUbicacion());
        repositorio.agregar(publicacion);
        response.redirect("/");
        return null;
    }

    public ModelAndView detallePubDarEnAdopcion(Request request, Response response) {
        Long id = new Long(0);
        try {
            id = Long.parseLong(request.params("idPublicacion"));
        } catch (Exception e) {
            response.status(400);
            halt(400, "El id de la publicacion no es valido");
        }

        PublicacionDarEnAdopcion publicacionDarEnAdopcion = this.repositorio.buscar(id);

        if (publicacionDarEnAdopcion == null) {
            response.status(404);
            halt(400, "la Publicacion no existe");
        }
        return new ModelAndView(publicacionDarEnAdopcion, "detallePubDarEnAdopcion.hbs");

    }

    //endregion
}
