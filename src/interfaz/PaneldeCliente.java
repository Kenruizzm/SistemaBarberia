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
 
