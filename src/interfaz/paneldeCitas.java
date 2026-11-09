package interfaz;

import control.Controladora;
import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import logica.Cita;
import logica.TipoServicio;

public class paneldeCitas extends JPanel {

    private final Controladora controladora;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private JTextField txtFecha;
    private JComboBox<Integer> cmbHora;
    private JComboBox<String> cmbCliente;
    private JComboBox<TipoServicio> cmbServicio;

    private Integer idSeleccionado;

    public paneldeCitas(Controladora controladora) {

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

        JLabel t = new JLabel("Citas");
        t.setFont(new Font("SansSerif", Font.BOLD, 24));
        t.setForeground(EstiloUI.TEXTO);

        JLabel s = new JLabel(
                "Programa, consulta y administra las citas de la barbería.");

        s.setForeground(EstiloUI.SECUNDARIO);

        p.add(t);
        p.add(s);

        return p;
    }

    private JScrollPane crearTabla() {

        modeloTabla = new DefaultTableModel(
                new String[]{
                    "ID", "Fecha", "Hora",
                    "Cliente", "Servicio", "Estado"
                }, 0) {

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
                cargarCitaSeleccionada();
            }
        });

        return EstiloUI.scroll(tabla);
    }

    private JPanel crearFormulario() {

        txtFecha = new JTextField(16);
        EstiloUI.estilizarCampo(txtFecha);

        cmbHora = new JComboBox<>();

        for (int i = 0; i < 24; i++) {
            cmbHora.addItem(i);
        }

        cmbCliente = new JComboBox<>();
        cmbServicio = new JComboBox<>();

        EstiloUI.estilizarCombo(cmbHora);
        EstiloUI.estilizarCombo(cmbCliente);
        EstiloUI.estilizarCombo(cmbServicio);

        JButton crear = new JButton("Crear cita");
        JButton modificar = new JButton("Guardar cambios");
        JButton borrar = new JButton("Cancelar cita");
        JButton confirmar = new JButton("Confirmar cita");
        JButton limpiar = new JButton("Limpiar");

        JButton tipos = new JButton("Tipos de servicio");
        JButton calendario = new JButton("Ver calendario");

        EstiloUI.botonPrincipal(crear);

        for (JButton b : new JButton[]{
            modificar, borrar, confirmar,
            limpiar, tipos, calendario
        }) {
            EstiloUI.estilizarBoton(b);
        }

        crear.addActionListener(e -> crearCita());
        modificar.addActionListener(e -> modificarCita());
        borrar.addActionListener(e -> borrarCita());
        confirmar.addActionListener(e -> confirmarCita());
        limpiar.addActionListener(e -> limpiarFormulario());

        tipos.addActionListener(e -> abrirTiposServicio());
        calendario.addActionListener(e -> abrirCalendario());

        JPanel campos = new JPanel(
                new GridLayout(0, 1, 7, 7));

        campos.setOpaque(false);

        campos.add(EstiloUI.etiqueta("Fecha (AAAA-MM-DD)"));
        campos.add(txtFecha);

        campos.add(EstiloUI.etiqueta("Hora"));
        campos.add(cmbHora);

        campos.add(EstiloUI.etiqueta("Cliente (correo)"));
        campos.add(cmbCliente);

        campos.add(EstiloUI.etiqueta("Tipo de servicio"));
        campos.add(cmbServicio);

        campos.add(crear);
        campos.add(modificar);
        campos.add(borrar);
        campos.add(confirmar);
        campos.add(limpiar);
        campos.add(tipos);
        campos.add(calendario);

        JPanel tarjeta = EstiloUI.tarjeta("Datos de la cita");

        tarjeta.setPreferredSize(new Dimension(285, 0));

        tarjeta.add(campos, BorderLayout.CENTER);

        return tarjeta;
    }

    private void crearCita() {

        try {

            LocalDate fecha =
                    LocalDate.parse(txtFecha.getText().trim());

            if (cmbCliente.getSelectedItem() == null) {
                EstiloUI.error(this,
                        "Debe seleccionar un cliente.");
                return;
            }

            if (cmbServicio.getSelectedItem() == null) {
                EstiloUI.error(this,
                        "Debe seleccionar un tipo de servicio.");
                return;
            }

            int hora = (Integer) cmbHora.getSelectedItem();

            String email =
                    (String) cmbCliente.getSelectedItem();

            TipoServicio servicio =
                    (TipoServicio) cmbServicio.getSelectedItem();

            controladora.crearCita(
                    fecha,
                    hora,
                    email,
                    servicio.getId()
            );

            actualizarTabla();
            limpiarFormulario();

            EstiloUI.mensaje(
                    this, "Cita creada correctamente.");

        } catch (Exception ex) {

            EstiloUI.error(this, ex.getMessage());
        }
    }

    private void modificarCita() {

        if (idSeleccionado == null) {
            EstiloUI.error(
                    this, "Seleccione una cita de la tabla.");
            return;
        }

        try {

            LocalDate fecha =
                    LocalDate.parse(txtFecha.getText().trim());

            if (cmbServicio.getSelectedItem() == null) {
                EstiloUI.error(this,
                        "Debe seleccionar un tipo de servicio.");
                return;
            }

            TipoServicio servicio =
                    (TipoServicio) cmbServicio.getSelectedItem();

            controladora.modificarCita(
                    idSeleccionado,
                    fecha,
                    (Integer) cmbHora.getSelectedItem(),
                    servicio.getId()
            );

            actualizarTabla();
            limpiarFormulario();

            EstiloUI.mensaje(
                    this, "Cita modificada correctamente.");

        } catch (Exception ex) {

            EstiloUI.error(this, ex.getMessage());
        }
    }

    private void borrarCita() {

        if (idSeleccionado == null) {
            EstiloUI.error(
                    this, "Seleccione una cita de la tabla.");
            return;
        }

        int r = JOptionPane.showConfirmDialog(
                this,
                "¿Desea cancelar esta cita?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION
        );

        if (r != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            controladora.borrarCita(idSeleccionado);

            actualizarTabla();
            limpiarFormulario();

            EstiloUI.mensaje(
                    this, "Cita cancelada correctamente.");

        } catch (Exception ex) {

            EstiloUI.error(this, ex.getMessage());
        }
    }

    private void confirmarCita() {

        if (idSeleccionado == null) {
            EstiloUI.error(
                    this, "Seleccione una cita de la tabla.");
            return;
        }

        try {

            controladora.confirmarCita(idSeleccionado);

            actualizarTabla();
            limpiarFormulario();

            EstiloUI.mensaje(
                    this, "Cita confirmada correctamente.");

        } catch (Exception ex) {

            EstiloUI.error(this, ex.getMessage());
        }
    }

    private void cargarCitaSeleccionada() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            idSeleccionado = null;
            return;
        }

        Cita cita = controladora.consultarCita(
                (Integer) modeloTabla.getValueAt(fila, 0));

        if (cita != null) {

            idSeleccionado = cita.getId();

            txtFecha.setText(cita.getFecha().toString());

            cmbHora.setSelectedItem(cita.getHora());

            cmbCliente.setSelectedItem(
                    cita.getCliente().getEmail());

            for (int i = 0; i < cmbServicio.getItemCount(); i++) {

                TipoServicio servicio = cmbServicio.getItemAt(i);

                if (servicio.getId()
                        == cita.getTipoServicio().getId()) {

                    cmbServicio.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    public void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (Cita c : controladora.listarCitas()) {

            modeloTabla.addRow(new Object[]{
                c.getId(),
                c.getFecha(),
                String.format("%02d:00", c.getHora()),
                c.getCliente().getNombre(),
                c.getTipoServicio().getDescripcion(),
                c.isConfirmada() ? "Confirmada" : "Pendiente"
            });
        }
    }

    public void actualizarCombos() {

        if (cmbCliente == null || cmbServicio == null) {
            return;
        }

        cmbCliente.removeAllItems();

        for (logica.Cliente c : controladora.listarClientes()) {
            cmbCliente.addItem(c.getEmail());
        }

        cmbServicio.removeAllItems();

        for (TipoServicio s : controladora.listarTiposServicio()) {
            cmbServicio.addItem(s);
        }
    }

    private void abrirTiposServicio() {

        Window w = SwingUtilities.getWindowAncestor(this);

        JDialog d = new JDialog(
                w,
                "Tipos de servicio",
                Dialog.ModalityType.APPLICATION_MODAL
        );

        panelTiposServicio panel =
                new panelTiposServicio(controladora);

        d.add(panel);
        d.setSize(760, 520);
        d.setLocationRelativeTo(this);
        d.setVisible(true);

        actualizarCombos();
    }

    private void abrirCalendario() {

        Window w = SwingUtilities.getWindowAncestor(this);

        JDialog d = new JDialog(
                w,
                "Calendario de citas",
                Dialog.ModalityType.APPLICATION_MODAL
        );

        panelCalendarioCitas panel =
                new panelCalendarioCitas(controladora);

        d.add(panel);
        d.setSize(850, 520);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    private void limpiarFormulario() {

        tabla.clearSelection();

        idSeleccionado = null;

        txtFecha.setText("");

        if (cmbHora.getItemCount() > 0) {
            cmbHora.setSelectedIndex(0);
        }

        if (cmbCliente.getItemCount() > 0) {
            cmbCliente.setSelectedIndex(0);
        }

        if (cmbServicio.getItemCount() > 0) {
            cmbServicio.setSelectedIndex(0);
        }
    }
}