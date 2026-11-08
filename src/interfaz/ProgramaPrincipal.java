package interfaz;
 
import control.Controladora;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
 
public class VentanaPrincipal extends JFrame {
 
    private Controladora controladora;
    private JTabbedPane pestanas;
 
    public VentanaPrincipal(Controladora controladora) {
        super("Barbería - Sistema de citas");
        this.controladora = controladora;
 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // centra la ventana
 
        pestanas = new JTabbedPane();
        pestanas.addTab("Clientes", new PanelClientes(controladora));
        pestanas.addTab("Citas", crearPanelPendiente("Sección de citas"));
        pestanas.addTab("Lista de espera", crearPanelPendiente("Sección de lista de espera"));
        pestanas.addTab("Tipos de servicio", crearPanelPendiente("Sección de tipos de servicio"));
        pestanas.addTab("Administración", crearPanelPendiente("Notificaciones y horario de atención"));
 
        add(pestanas);
    }
