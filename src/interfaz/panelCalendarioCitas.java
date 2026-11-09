package interfaz;

import control.Controladora;
import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import logica.Cita;

public class panelCalendarioCitas extends JPanel {

    private final Controladora controladora;

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private JTextField txtFecha;
    private JComboBox<String> cmbPeriodo;

    public panelCalendarioCitas(Controladora controladora) {

        this.controladora = controladora;

        setLayout(new BorderLayout(12, 12));
        setBackground(EstiloUI.FONDO);

        setBorder(BorderFactory.createEmptyBorder(
                18, 20, 18, 20));

        add(crearFormulario(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
    }

    private JPanel crearFormulario() {

        txtFecha = new JTextField(10);

        EstiloUI.estilizarCampo(txtFecha);

        cmbPeriodo = new JComboBox<>(
                new String[]{"Semana", "Mes"});

        EstiloUI.estilizarCombo(cmbPeriodo);

        JButton consultar = new JButton("Consultar");

        EstiloUI.botonPrincipal(consultar);

        consultar.addActionListener(
                e -> consultarCalendario());

        JPanel p = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 8));

        p.setBackground(EstiloUI.BLANCO);

        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        EstiloUI.BORDE, 1, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        p.add(EstiloUI.etiqueta(
                "Fecha inicial (AAAA-MM-DD):"));

        p.add(txtFecha);

        p.add(EstiloUI.etiqueta("Periodo:"));

        p.add(cmbPeriodo);

        p.add(consultar);

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

        EstiloUI.estilizarTabla(tabla);

        return EstiloUI.scroll(tabla);
    }

    private void consultarCalendario() {

        try {

            LocalDate fecha =
                    LocalDate.parse(txtFecha.getText().trim());

            String periodo =
                    cmbPeriodo.getSelectedItem().toString().toLowerCase();

            modeloTabla.setRowCount(0);

            for (Cita c : controladora.verCalendarioCitas(
                    fecha, periodo)) {

                modeloTabla.addRow(new Object[]{
                    c.getId(),
                    c.getFecha(),
                    String.format("%02d:00", c.getHora()),
                    c.getCliente().getNombre(),
                    c.getTipoServicio().getDescripcion(),
                    c.isConfirmada() ? "Confirmada" : "Pendiente"
                });
            }

            if (modeloTabla.getRowCount() == 0) {

                EstiloUI.mensaje(
                        this,
                        "No hay citas registradas en ese periodo.");
            }

        } catch (Exception ex) {

            EstiloUI.error(
                    this,
                    "Verifique que la fecha tenga el formato AAAA-MM-DD.");
        }
    }
}