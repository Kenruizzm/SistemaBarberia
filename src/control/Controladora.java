
package control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import logica.Cita;
import logica.Cliente;
import logica.HorarioDia;
import logica.ListaEspera;
import logica.TipoServicio;

public class Controladora {

    private List<Cliente> clientes;
    private List<Cita> citas;
    private List<TipoServicio> tiposServicio;
    private List<HorarioDia> horarios;
    private ListaEspera listaEspera;

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

    //---Clientes---//

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

    private boolean validarTelefono(String telefono) {
        return telefono != null
                && telefono.trim().matches("[0-9()\\-]+");
    }
}