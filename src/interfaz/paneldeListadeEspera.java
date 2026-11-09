package interfaz;

import control.Controladora;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import logica.Cliente;

public class paneldeListadeEspera extends JPanel {

    private final Controladora controladora;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private JComboBox<String> cmbCliente;

    private String emailSeleccionado;

    public paneldeListadeEspera(Controladora controladora) {

        this.controladora = controladora;

        setLayout(new BorderLayout(18, 18));
        setBackground(EstiloUI.FONDO);

        setBorder(BorderFactory.createEmptyBorder(
                25, 28, 25, 28));

        add(encabezado(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
        add(crearFormulario(), BorderLayout.EAST);

        actualizarCombos();
        actualizarTabla();
    }

    private JPanel encabezado() {

        JPanel p = new JPanel(new GridLayout(2, 1, 0, 5));
        p.setOpaque(false);

        JLabel t = new JLabel("Lista de espera");
        t.setFont(new Font("SansSerif", Font.BOLD, 24));
        t.setForeground(EstiloUI.TEXTO);

        JLabel s = new JLabel(
                "Gestiona los clientes que esperan un espacio disponible.");

        s.setForeground(EstiloUI.SECUNDARIO);

        p.add(t);
        p.add(s);

        return p;
    }

    private JScrollPane crearTabla() {

        modeloTabla = new DefaultTableModel(
                new String[]{"Nombre", "Email", "Teléfono"}, 0) {

            @Override
            public boolean isCellEditable(int f, int c) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);

        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        EstiloUI.estilizarTabla(tabla);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarClienteSeleccionado();
            }
        });

        return EstiloUI.scroll(tabla);
    }

    private JPanel crearFormulario() {

        cmbCliente = new JComboBox<>();

        EstiloUI.estilizarCombo(cmbCliente);

        JButton agregar = new JButton("Agregar a lista");
        JButton borrar = new JButton("Eliminar de lista");
        JButton actualizar = new JButton("Actualizar");
        JButton limpiar = new JButton("Limpiar");

        EstiloUI.botonPrincipal(agregar);

        for (JButton b : new JButton[]{
            borrar, actualizar, limpiar
        }) {
            EstiloUI.estilizarBoton(b);
        }

        agregar.addActionListener(e -> agregarCliente());
        borrar.addActionListener(e -> borrarCliente());
        actualizar.addActionListener(e -> actualizarTodo());
        limpiar.addActionListener(e -> limpiarFormulario());

        JPanel campos = new JPanel(
                new GridLayout(0, 1, 8, 8));

        campos.setOpaque(false);

        campos.add(EstiloUI.etiqueta("Seleccione un cliente"));
        campos.add(cmbCliente);

        campos.add(agregar);
        campos.add(borrar);
        campos.add(actualizar);
        campos.add(limpiar);

        JPanel tarjeta = EstiloUI.tarjeta("Lista de espera");

        tarjeta.setPreferredSize(new Dimension(270, 0));

        tarjeta.add(campos, BorderLayout.CENTER);

        return tarjeta;
    }

    private void agregarCliente() {

        if (cmbCliente.getSelectedItem() == null) {
            EstiloUI.error(
                    this, "No hay clientes disponibles para agregar.");
            return;
        }

        try {

            controladora.agregarClienteListaEspera(
                    (String) cmbCliente.getSelectedItem());

            actualizarTabla();
            limpiarFormulario();

            EstiloUI.mensaje(
                    this, "Cliente agregado a la lista de espera.");

        } catch (Exception ex) {

            EstiloUI.error(this, ex.getMessage());
        }
    }

    private void borrarCliente() {

        if (emailSeleccionado == null) {
            EstiloUI.error(
                    this, "Seleccione un cliente de la tabla.");
            return;
        }

        int r = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar al cliente " + emailSeleccionado
                + " de la lista de espera?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (r != JOptionPane.YES_OPTION) {
            return;
        }

        if (controladora.borrarClienteListaEspera(emailSeleccionado)) {

            actualizarTabla();
            limpiarFormulario();

            EstiloUI.mensaje(
                    this, "Cliente eliminado de la lista de espera.");

        } else {

            EstiloUI.error(this, "No se pudo eliminar el cliente.");
        }
    }

    private void cargarClienteSeleccionado() {

        int f = tabla.getSelectedRow();

        if (f == -1) {
            emailSeleccionado = null;
            return;
        }

        emailSeleccionado =
                (String) modeloTabla.getValueAt(f, 1);

        cmbCliente.setSelectedItem(emailSeleccionado);
    }

    public void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (Cliente c : controladora.mostrarListaEspera()) {

            modeloTabla.addRow(new Object[]{
                c.getNombre(),
                c.getEmail(),
                c.getTelefono()
            });
        }
    }

    public void actualizarCombos() {

        if (cmbCliente == null) {
            return;
        }

        cmbCliente.removeAllItems();

        for (Cliente c : controladora.listarClientes()) {
            cmbCliente.addItem(c.getEmail());
        }
    }

    private void actualizarTodo() {

        actualizarCombos();
        actualizarTabla();
        limpiarFormulario();

        EstiloUI.mensaje(
                this, "Lista de espera actualizada.");
    }

    private void limpiarFormulario() {

        tabla.clearSelection();
        emailSeleccionado = null;

        if (cmbCliente.getItemCount() > 0) {
            cmbCliente.setSelectedIndex(0);
        }
    }
}