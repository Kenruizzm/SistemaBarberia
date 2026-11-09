package interfaz;
 
import control.Controladora;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import logica.Cliente;
 
public class PanelClientes extends JPanel {
 
    private Controladora controladora;
 
    private DefaultTableModel modeloTabla;
    private JTable tabla;
 
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JTextField txtTelefono;
 
 // Email del cliente seleccionado en la tabla (null si es que no habria ninguno )
    private String emailSeleccionado;
 
    public PanelClientes(Controladora controladora) {
        this.controladora = controladora;
 
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        add(crearTabla(), BorderLayout.CENTER);
        add(crearFormulario(), BorderLayout.EAST);
 
        actualizarTabla();
    }
 

 //Construcción de la interfazzzzzzzzz
 
    private JScrollPane crearTabla() {
        modeloTabla = new DefaultTableModel(new String[]{"Nombre", "Email", "Teléfono"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false; // la tabla es solo para ver, se edita con el formulario
            }
        };
 
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
// Al seleccionar una fila se cargan los datos en el formulario (Consultar cliente)
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarClienteSeleccionado();
            }
        });
 
        return new JScrollPane(tabla);
    }
 
    private JPanel crearFormulario() {
        txtNombre = new JTextField(20);
        txtEmail = new JTextField(20);
        txtTelefono = new JTextField(20);
 
        JButton btnCrear = new JButton("Crear");
        JButton btnModificar = new JButton("Modificar");
        JButton btnBorrar = new JButton("Borrar");
        JButton btnLimpiar = new JButton("Limpiar");
 
        btnCrear.addActionListener(e -> crearCliente());
        btnModificar.addActionListener(e -> modificarCliente());
        btnBorrar.addActionListener(e -> borrarCliente());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
 
        JPanel campos = new JPanel(new GridLayout(0, 1, 4, 4));
        campos.add(new JLabel("Nombre completo:"));
        campos.add(txtNombre);
        campos.add(new JLabel("Email:"));
        campos.add(txtEmail);
        campos.add(new JLabel("Teléfono:"));
        campos.add(txtTelefono);
        campos.add(new JLabel()); // espacio
        campos.add(btnCrear);
        campos.add(btnModificar);
        campos.add(btnBorrar);
        campos.add(btnLimpiar);
 
// Se mete en NORTH para que los campos no se estiren a todo lo alto
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBorder(BorderFactory.createTitledBorder("Datos del cliente"));
        contenedor.add(campos, BorderLayout.NORTH);
        return contenedor;
    }
 
