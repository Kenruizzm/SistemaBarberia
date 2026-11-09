package interfaz;

import control.Controladora;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import logica.TipoServicio;

public class panelTiposServicio extends JPanel {

    private final Controladora controladora;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private JTextField txtDescripcion;

    private Integer idSeleccionado;

    public panelTiposServicio(Controladora controladora) {

        this.controladora = controladora;

        setLayout(new BorderLayout(16, 16));
        setBackground(EstiloUI.FONDO);

        setBorder(BorderFactory.createEmptyBorder(
                20, 22, 20, 22));

        add(crearTabla(), BorderLayout.CENTER);
        add(crearFormulario(), BorderLayout.EAST);

        actualizarTabla();
    }

    private JScrollPane crearTabla() {

        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Descripción"}, 0) {

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
                cargarServicioSeleccionado();
            }
        });

        return EstiloUI.scroll(tabla);
    }

    private JPanel crearFormulario() {

        txtDescripcion = new JTextField(18);

        EstiloUI.estilizarCampo(txtDescripcion);

        JButton crear = new JButton("Crear servicio");
        JButton modificar = new JButton("Guardar cambios");
        JButton borrar = new JButton("Borrar");
        JButton limpiar = new JButton("Limpiar");

        EstiloUI.botonPrincipal(crear);

        for (JButton b : new JButton[]{
            modificar, borrar, limpiar
        }) {
            EstiloUI.estilizarBoton(b);
        }

        crear.addActionListener(e -> crearServicio());
        modificar.addActionListener(e -> modificarServicio());
        borrar.addActionListener(e -> borrarServicio());
        limpiar.addActionListener(e -> limpiarFormulario());

        JPanel campos = new JPanel(
                new GridLayout(0, 1, 7, 7));

        campos.setOpaque(false);

        campos.add(EstiloUI.etiqueta("Descripción del servicio"));
        campos.add(txtDescripcion);

        campos.add(crear);
        campos.add(modificar);
        campos.add(borrar);
        campos.add(limpiar);

        JPanel tarjeta = EstiloUI.tarjeta("Datos del servicio");

        tarjeta.setPreferredSize(new Dimension(270, 0));

        tarjeta.add(campos, BorderLayout.CENTER);

        return tarjeta;
    }

    private void crearServicio() {

        try {

            controladora.crearTipoServicio(
                    txtDescripcion.getText());

            actualizarTabla();
            limpiarFormulario();

            EstiloUI.mensaje(
                    this, "Tipo de servicio creado correctamente.");

        } catch (Exception ex) {

            EstiloUI.error(this, ex.getMessage());
        }
    }

    private void modificarServicio() {

        if (idSeleccionado == null) {
            EstiloUI.error(
                    this, "Seleccione un servicio de la tabla.");
            return;
        }

        try {

            controladora.modificarTipoServicio(
                    idSeleccionado,
                    txtDescripcion.getText()
            );

            actualizarTabla();
            limpiarFormulario();

            EstiloUI.mensaje(
                    this, "Tipo de servicio modificado correctamente.");

        } catch (Exception ex) {

            EstiloUI.error(this, ex.getMessage());
        }
    }

    private void borrarServicio() {

        if (idSeleccionado == null) {
            EstiloUI.error(
                    this, "Seleccione un servicio de la tabla.");
            return;
        }

        int r = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar este tipo de servicio?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (r != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            controladora.borrarTipoServicio(idSeleccionado);

            actualizarTabla();
            limpiarFormulario();

            EstiloUI.mensaje(
                    this, "Tipo de servicio eliminado correctamente.");

        } catch (Exception ex) {

            EstiloUI.error(this, ex.getMessage());
        }
    }

    private void cargarServicioSeleccionado() {

        int f = tabla.getSelectedRow();

        if (f == -1) {
            idSeleccionado = null;
            return;
        }

        idSeleccionado =
                (Integer) modeloTabla.getValueAt(f, 0);

        TipoServicio servicio =
                controladora.consultarTipoServicio(idSeleccionado);

        if (servicio != null) {
            txtDescripcion.setText(servicio.getDescripcion());
        }
    }

    public void actualizarTabla() {

        modeloTabla.setRowCount(0);

        for (TipoServicio servicio :
                controladora.listarTiposServicio()) {

            modeloTabla.addRow(new Object[]{
                servicio.getId(),
                servicio.getDescripcion()
            });
        }
    }

    private void limpiarFormulario() {

        tabla.clearSelection();

        idSeleccionado = null;

        txtDescripcion.setText("");
    }
}