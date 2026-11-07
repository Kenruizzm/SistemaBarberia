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
          if (cliente != null && !contieneClienteEspera(cliente.getEmail())) {
        clientes.add(cliente);
        }
        
    }

    public boolean borrarClienteEspera(String email) {
        return clientes.removeIf(c -> c.getEmail().equalsIgnoreCase(email)); 
    }

    public Cliente buscarClienteEspera (String email) {
        for (Cliente c : clientes) {
            if (c.getEmail().equalsIgnoreCase(email)) {
                return c;
            }
        }
        return null; 
    }
    public boolean contieneClienteEspera (String email) {
        return buscarClienteEspera(email) != null;
    }

}