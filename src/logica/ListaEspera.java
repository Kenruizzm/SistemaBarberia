package logica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListaEspera implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Cliente> clientes;

    public ListaEspera() {
        this.clientes = new ArrayList<>();
    }

    public List<Cliente> mostrarLista() {
        return new ArrayList<>(clientes);
    }

    public void agregarCliente(Cliente cliente) {
        if (cliente != null && cliente.getEmail() != null
                && !cliente.getEmail().trim().isEmpty()
                && !contieneClienteEspera(cliente.getEmail())) {
            clientes.add(cliente);
        }
    }

    public boolean borrarClienteEspera(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        return clientes.removeIf(c -> email.equalsIgnoreCase(c.getEmail()));
    }

    public Cliente buscarClienteEspera(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        for (Cliente c : clientes) {
            if (email.equalsIgnoreCase(c.getEmail())) {
                return c;
            }
        }

        return null;
    }

    public boolean contieneClienteEspera(String email) {
        return buscarClienteEspera(email) != null;
    }
}