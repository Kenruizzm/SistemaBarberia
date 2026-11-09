package interfaz;

import control.Controladora;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import logica.Cliente;

public class paneldeClientes extends JPanel {

    private final Controladora controladora;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private JTextField txtNombre;
    private JTextField txtEmail;
    private JTextField txtTelefono;

    private String emailSeleccionado;

    public paneldeClientes(Controladora controladora) {

        this.controladora = controladora;

        setLayout(new BorderLayout(18, 18));
        setBackground(EstiloUI.FONDO);

        setBorder(BorderFactory.createEmptyBorder(
                25, 28, 25, 28));

        add(encabezado(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
        add(crearFormulario(), BorderLayout.EAST);

        actualizarTabla();
    }

    private JPanel encabezado() {

        JPanel p = new JPanel(new GridLayout(2, 1, 0, 5));
        p.setOpaque(false);

        JLabel t = new JLabel("Clientes");
        t.setFont(new Font("SansSerif", Font.BOLD, 24));
        t.setForeground(EstiloUI.TEXTO);

        JLabel s = new JLabel(
                "Registra y administra la información de tus clientes.");

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

        txtNombre = new JTextField(18);
        txtEmail = new JTextField(18);
        txtTelefono = new JTextField(18);

        EstiloUI.estilizarCampo(txtNombre);
        EstiloUI.estilizarCampo(txtEmail);
        EstiloUI.estilizarCampo(txtTelefono);

        JButton crear = new JButton("Crear cliente");
        JButton modificar = new JButton("Guardar cambios");
        JButton borrar = new JButton("Borrar");
        JButton limpiar = new JButton("Limpiar");

        EstiloUI.botonPrincipal(crear);

        for (JButton b : new JButton[]{
            modificar, borrar, limpiar
        }) {
            EstiloUI.estilizarBoton(b);
        }

        crear.addActionListener(e -> crearCliente());
        modificar.addActionListener(e -> modificarCliente());
        borrar.addActionListener(e -> borrarCliente());
        limpiar.addActionListener(e -> limpiarFormulario());

        JPanel campos = new JPanel(
                new GridLayout(0, 1, 7, 7));

        campos.setOpaque(false);

        campos.add(EstiloUI.etiqueta("Nombre completo"));
        campos.add(txtNombre);

        campos.add(EstiloUI.etiqueta("Correo electrónico"));
        campos.add(txtEmail);

        campos.add(EstiloUI.etiqueta("Teléfono"));
        campos.add(txtTelefono);

        campos.add(crear);
        campos.add(modificar);
        campos.add(borrar);
        campos.add(limpiar);

        JPanel contenedor = EstiloUI.tarjeta("Datos del cliente");

        contenedor.setPreferredSize(new Dimension(270, 0));

        contenedor.add(campos, BorderLayout.CENTER);

        return contenedor;
    }

    private void crearCliente() {

        try {

            controladora.crearCliente(
                    txtNombre.getText(),
                    txtEmail.getText(),
                    txtTelefono.getText()
            );

            actualizarTabla();
            limpiarFormulario();

            EstiloUI.mensaje(
                    this, "Cliente creado correctamente.");

        } catch (Exception ex) {

            EstiloUI.error(this, ex.getMessage());
        }
    }

    private void modificarCliente() {

        if (emailSeleccionado == null) {
            EstiloUI.error(
                    this, "Seleccione un cliente de la tabla.");
            return;
        }

        try {

            controladora.modificarCliente(
                    emailSeleccionado,
                    txtNombre.getText(),
                    txtEmail.getText(),
                    txtTelefono.getText()
            );

            actualizarTabla();
            limpiarFormulario();

            EstiloUI.mensaje(
                    this, "Los datos fueron guardados correctamente.");

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
                "¿Desea borrar al cliente " + emailSeleccionado + "?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (r != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            controladora.borrarCliente(emailSeleccionado);

            actualizarTabla();
            limpiarFormulario();

            EstiloUI.mensaje(this, "Cliente borrado.");

        } catch (Exception ex) {

            EstiloUI.error(this, ex.getMessage());
        }
    }

    private void cargarClienteSeleccionado() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            emailSeleccionado = null;
            return;
        }

        String email =
                (String) modeloTabla.getValueAt(fila, 1);

        Cliente c = controladora.consultarCliente(email);

        if (c != null) {

            emailSeleccionado = c.getEmail();

            txtNombre.setText(c.getNombre());
            txtEmail.setText(c.getEmail());
            txtTelefono.setText(c.getTelefono());
        }
    }

    public void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (Cliente c : controladora.listarClientes()) {

            modeloTabla.addRow(new Object[]{
                c.getNombre(),
                c.getEmail(),
                c.getTelefono()
            });
        }
    }

    private void limpiarFormulario() {

        tabla.clearSelection();
        emailSeleccionado = null;

        txtNombre.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
    }
}