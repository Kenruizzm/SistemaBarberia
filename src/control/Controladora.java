package control;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import logica.Cita;
import logica.Cliente;
import logica.HorarioDia;
import logica.ListaEspera;
import logica.TipoServicio;

public class Controladora implements java.io.Serializable {

    private List<Cliente> clientes;
    private List<Cita> citas;
    private List<TipoServicio> tiposServicio;
    private List<HorarioDia> horarios;
    private ListaEspera listaEspera;
    private static final long serialVersionUID = 1L;
    

    // Contadores para generar los ids automáticamente
    private int siguienteIdCita;
    private int siguienteIdTipo;

    public Controladora() {
        this.clientes = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.tiposServicio = new ArrayList<>();
        this.horarios = new ArrayList<>();
        this.listaEspera = new ListaEspera();
        this.siguienteIdCita = 1;
        this.siguienteIdTipo = 1;
    }

    // clientes
    // crear clien
    public Cliente crearCliente(String nombre, String email,
            String telefono) throws Exception {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío.");
        }

        if (!validarEmail(email)) {
            throw new Exception("El email no es válido.");
        }

        if (!validarTelefono(telefono)) {
            throw new Exception("El teléfono debe tener un formato válido.");
        }

        if (consultarCliente(email) != null) {
            throw new Exception("Ya existe un cliente con ese email.");
        }

        Cliente cliente = new Cliente(nombre.trim(), email.trim(), telefono.trim());
        clientes.add(cliente);
        return cliente;
    }


    // modificar clien
    public void modificarCliente(String emailOriginal, String nombre,
            String email, String telefono) throws Exception {

        Cliente cliente = consultarCliente(emailOriginal);

        if (cliente == null) {
            throw new Exception("No existe un cliente con el email "
                    + emailOriginal + ".");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío.");
        }

        if (!validarEmail(email)) {
            throw new Exception("El email no es válido.");
        }

        if (!validarTelefono(telefono)) {
            throw new Exception("El teléfono debe tener un formato válido.");
        }

        Cliente otro = consultarCliente(email);

        if (otro != null && otro != cliente) {
            throw new Exception("Ya existe otro cliente con ese email.");
        }

        cliente.setNombre(nombre.trim());
        cliente.setEmail(email.trim());
        cliente.setTelefono(telefono.trim());
    }


    // borrar clien

    public void borrarCliente(String email) throws Exception {

        Cliente cliente = consultarCliente(email);

        if (cliente == null) {
            throw new Exception("No existe un cliente con el email "
                    + email + ".");
        }

        if (listaEspera.contieneClienteEspera(cliente.getEmail())) {
            throw new Exception("No se puede borrar: el cliente está "
                    + "en la lista de espera.");
        }

        LocalDate hoy = LocalDate.now();

        for (Cita cita : citas) {

            if (cita.getCliente() == cliente) {

                if (cita.getFecha().isAfter(hoy)
                        || cita.getFecha().isEqual(hoy)) {

                    throw new Exception("No se puede borrar: el cliente "
                            + "tiene citas pendientes.");
                }
            }
        }

        clientes.remove(cliente);
    }

        // consultar clien

    public Cliente consultarCliente(String email) {

        if (email == null) {
            return null;
        }

        for (Cliente c : clientes) {
            if (c.getEmail().equalsIgnoreCase(email.trim())) {
                return c;
            }
        }

        return null;
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }

    private boolean validarEmail(String email) {
        return email != null
                && email.trim().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    }

        // vali telefono

    private boolean validarTelefono(String telefono) {
    return telefono != null
            && telefono.trim().matches("(?=.*[0-9])[0-9()\\-]+"); 
        }



// citas
// crear cita
public Cita crearCita(LocalDate fecha, int hora,
        String emailCliente, int idTipoServicio) throws Exception {

    Cliente cliente = consultarCliente(emailCliente);

    if (cliente == null) {
        throw new Exception("No existe el cliente indicado.");
    }

    TipoServicio servicio = buscarTipoServicio(idTipoServicio);

    if (servicio == null) {
        throw new Exception("No existe el tipo de servicio indicado.");
    }

    validarDisponibilidad(fecha, hora, -1);

    Cita cita = new Cita(siguienteIdCita, fecha, hora,
            false, cliente, servicio);

    citas.add(cita);
    siguienteIdCita++;

    return cita;
}

// modificar cita 

public void modificarCita(int id, LocalDate fecha,
        int hora, int idTipoServicio) throws Exception {

    Cita cita = consultarCita(id);

    if (cita == null) {
        throw new Exception("No existe la cita indicada.");
    }

    TipoServicio servicio = buscarTipoServicio(idTipoServicio);

    if (servicio == null) {
        throw new Exception("No existe el tipo de servicio indicado.");
    }

    validarDisponibilidad(fecha, hora, id);

    cita.setFecha(fecha);
    cita.setHora(hora);
    cita.setTipoServicio(servicio);
}

// borrar cita 

public void borrarCita(int id) throws Exception {

    Cita cita = consultarCita(id);

    if (cita == null) {
        throw new Exception("No existe la cita indicada.");
    }

    citas.remove(cita);
}

// consultar cita 

public Cita consultarCita(int id) {

    for (Cita cita : citas) {
        if (cita.getId() == id) {
            return cita;
        }
    }

    return null;
}

// listar cita 

public List<Cita> listarCitas() {
    return new ArrayList<>(citas);
}

// confirmar cita 

public void confirmarCita(int id) throws Exception {

    Cita cita = consultarCita(id);

    if (cita == null) {
        throw new Exception("No existe la cita indicada.");
    }

    cita.confirmar();
}

// obtener cita

public List<Cita> obtenerCitasSinConfirmar() {

    List<Cita> sinConfirmar = new ArrayList<>();

    LocalDate manana = LocalDate.now().plusDays(1);

    for (Cita cita : citas) {
        if (!cita.isConfirmada()
                && cita.getFecha().equals(manana)) {

            sinConfirmar.add(cita);
        }
    }

    return sinConfirmar;
}



// validar dispo citas

private void validarDisponibilidad(LocalDate fecha,
        int hora, int idExcluir) throws Exception {

    if (fecha == null) {
        throw new Exception("Debe indicar una fecha.");
    }

    if (!estaDentroDelHorario(fecha, hora)) {
        throw new Exception(
                "La fecha u hora está fuera del horario de atención.");
    }

    for (Cita cita : citas) {

        if (cita.getId() != idExcluir
                && cita.getFecha().equals(fecha)
                && cita.getHora() == hora) {

            throw new Exception(
                    "La hora seleccionada ya está ocupada.");
        }
    }
}

// enviar noti  REVISAR 

public void enviarNotificacionesConfirmacion() {

    List<Cita> pendientes = obtenerCitasSinConfirmar();

    if (pendientes.isEmpty()) {
        System.out.println(
                "No hay citas pendientes de confirmación para mañana.");
        return;
    }

    for (Cita cita : pendientes) {

        boolean enviado = enviarCorreoConfirmacion(cita);

        if (enviado) {
            System.out.println("Correo enviado a: "
                    + cita.getCliente().getEmail());
        } else {
            System.out.println("No se pudo enviar el correo a: "
                    + cita.getCliente().getEmail());
        }
    }
}





// enviar correo  REVISAR 

public boolean enviarCorreoConfirmacion(Cita cita) {

    if (cita == null || cita.isConfirmada()) {
        return false;
    }

    /*
     * hay que configurar
     * un servicio SMTP 
     * biblioteca de correo electrónico (Jakarta Mail.)
     */

    return false;
}




// ver calendario 

public List<Cita> verCalendarioCitas(
        LocalDate fechaInicio, String periodo) {

    if (fechaInicio == null) {
        throw new IllegalArgumentException("La fecha no puede ser nula.");
    }

    if (periodo == null) {
        throw new IllegalArgumentException("El periodo no puede estar vacío.");
    }

    LocalDate inicio;
    LocalDate fin;

    if (periodo.equalsIgnoreCase("semana")) {

        inicio = fechaInicio;
        fin = inicio.plusDays(7);

    } else if (periodo.equalsIgnoreCase("mes")) {

        inicio = fechaInicio;
        fin = inicio.plusMonths(1);

    } else {
        throw new IllegalArgumentException(
                "El periodo debe ser 'semana' o 'mes'.");
    }

    List<Cita> resultado = new ArrayList<>();

    for (Cita cita : citas) {

        if (!cita.getFecha().isBefore(inicio)
                && cita.getFecha().isBefore(fin)) {

            resultado.add(cita);
        }
    }

    // Ordenar las citas por fecha y hora
    resultado.sort((c1, c2) -> {

        int comparacion = c1.getFecha().compareTo(c2.getFecha());

        if (comparacion == 0) {
            return Integer.compare(c1.getHora(), c2.getHora());
        }

        return comparacion;
    });

    return resultado;
}



// tipo serv


// crear serv

public TipoServicio crearTipoServicio(String descripcion) throws Exception {

    if (descripcion == null || descripcion.trim().isEmpty()) {
        throw new Exception("La descripción no puede estar vacía.");
    }

    for (TipoServicio servicio : tiposServicio) {
        if (servicio.getDescripcion().equalsIgnoreCase(descripcion.trim())) {
            throw new Exception("Ya existe un servicio con esa descripción.");
        }
    }

    TipoServicio servicio = new TipoServicio(
            siguienteIdTipo, descripcion.trim());

    tiposServicio.add(servicio);
    siguienteIdTipo++;

    return servicio;
}

// modifi serv

public void modificarTipoServicio(int id, String descripcion) throws Exception {

    TipoServicio servicio = buscarTipoServicio(id);

    if (servicio == null) {
        throw new Exception("No existe el tipo de servicio indicado.");
    }

    if (descripcion == null || descripcion.trim().isEmpty()) {
        throw new Exception("La descripción no puede estar vacía.");
    }

    for (TipoServicio otro : tiposServicio) {

        if (otro != servicio
                && otro.getDescripcion().equalsIgnoreCase(descripcion.trim())) {

            throw new Exception("Ya existe otro servicio con esa descripción.");
        }
    }

    servicio.setDescripcion(descripcion.trim());
}


// borrar serv


public void borrarTipoServicio(int id) throws Exception {

    TipoServicio servicio = buscarTipoServicio(id);

    if (servicio == null) {
        throw new Exception("No existe el tipo de servicio indicado.");
    }

    for (Cita cita : citas) {

        if (cita.getTipoServicio() == servicio) {
            throw new Exception(
                    "No se puede eliminar: el servicio está asociado a una cita.");
        }
    }

    tiposServicio.remove(servicio);
}

// consultar serv


public TipoServicio consultarTipoServicio(int id) {

    return buscarTipoServicio(id);
}

// listar serv

public List<TipoServicio> listarTiposServicio() {

    return new ArrayList<>(tiposServicio);
}


// buscar serv REVISAR

private TipoServicio buscarTipoServicio(int id) {

    for (TipoServicio servicio : tiposServicio) {

        if (servicio.getId() == id) {
            return servicio;
        }
    }

    return null;
}


// lista espera

// mostrar list
public List<Cliente> mostrarListaEspera() {
    return listaEspera.mostrarLista();
}

// agregar list
public boolean agregarClienteListaEspera(String emailCliente)
        throws Exception {

    Cliente cliente = consultarCliente(emailCliente);

    if (cliente == null) {
        throw new Exception("No existe el cliente indicado.");
    }

    if (listaEspera.contieneClienteEspera(emailCliente)) {
        throw new Exception("El cliente ya está en la lista de espera.");
    }

    listaEspera.agregarCliente(cliente);

    return true;
}

// borrar list 
public boolean borrarClienteListaEspera(String email) {

    return listaEspera.borrarClienteEspera(email);
}

// horario ate

// confi horario
public void configurarHorario(DayOfWeek dia, boolean atiende,
        int horaApertura, int horaCierre) throws Exception {

    if (dia == null) {
        throw new Exception("Debe indicar un día.");
    }

    if (atiende && (horaApertura < 0 || horaApertura > 23
            || horaCierre < 1 || horaCierre > 24
            || horaApertura >= horaCierre)) {

        throw new Exception("El horario indicado no es válido.");
    }

    HorarioDia horario = obtenerHorario(dia);

    if (horario == null) {

        horario = new HorarioDia(
                dia, atiende, horaApertura, horaCierre);

        horarios.add(horario);

    } else {

        horario.configurar(atiende, horaApertura, horaCierre);
    }
}

// obtener horario 
public HorarioDia obtenerHorario(DayOfWeek dia) {

    for (HorarioDia horario : horarios) {

        if (horario.getDia() == dia) {
            return horario;
        }
    }

    return null;
}

// dentro horario
public boolean estaDentroDelHorario(LocalDate fecha, int hora) {

    if (fecha == null || hora < 0 || hora > 23) {
        return false;
    }

    HorarioDia horario = obtenerHorario(fecha.getDayOfWeek());

    if (horario == null || !horario.isAtiende()) {
        return false;
    }

    return hora >= horario.getHoraApertura()
            && hora < horario.getHoraCierre();
}

// guardar y cargar info

    // Nombre del archivo donde se guarda todo
    public static final String ARCHIVO = "barberia.dat";

    // guardar en disco
    public void guardarDatos(String archivo) throws IOException {
        try (ObjectOutputStream salida = new ObjectOutputStream(
                new FileOutputStream(archivo))) {
            salida.writeObject(this);
        }
    }

    // cargar de disco
    public static Controladora cargarDatos(String archivo)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream entrada = new ObjectInputStream(
                new FileInputStream(archivo))) {
            return (Controladora) entrada.readObject();
        }
    }

    // cargar al iniciar: si no hay archivo, empieza vacío
    public static Controladora iniciar(String archivo) {
        java.io.File f = new java.io.File(archivo);
        if (!f.exists()) {
            return new Controladora();
        }
        try {
            return cargarDatos(archivo);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se pudieron cargar los datos: " + e.getMessage());
            return new Controladora();
        }
    }

}