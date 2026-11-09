
package interfaz;

import control.Controladora;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import logica.Cita;
import logica.HorarioDia;

public class paneldeAdministracion extends JPanel {

    private final Controladora controladora;

    private JTable tablaPendientes;
    private JTable tablaEnviadas;

    private DefaultTableModel modeloPendientes;
    private DefaultTableModel modeloEnviadas;

    private JLabel lblPendientes;
    private JLabel lblEnviadas;

    private JComboBox<String> cmbDia;
    private JCheckBox chkAtiende;
    private JComboBox<Integer> cmbApertura;
    private JComboBox<Integer> cmbCierre;

    private final List<Cita> citasEnviadas = new ArrayList<>();

    public paneldeAdministracion(Controladora controladora) {

        this.controladora = controladora;

        setLayout(new BorderLayout(15, 15));
        setBackground(EstiloUI.FONDO);

        setBorder(BorderFactory.createEmptyBorder(
                25, 25, 25, 25
        ));

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
        add(crearPanelHorario(), BorderLayout.SOUTH);

        cargarHorario();
        actualizarTablas();
    }


    private JPanel crearEncabezado() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 5));
        textos.setOpaque(false);

        JLabel titulo = new JLabel("Administración");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 27));
        titulo.setForeground(EstiloUI.TEXTO);

        JLabel subtitulo = new JLabel(
                "Gestiona las notificaciones y el horario de atención."
        );

        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(EstiloUI.SECUNDARIO);

        textos.add(titulo);
        textos.add(subtitulo);

        JButton btnActualizar = crearBoton("Actualizar información");

        btnActualizar.addActionListener(e -> actualizarTablas());

        panel.add(textos, BorderLayout.WEST);
        panel.add(btnActualizar, BorderLayout.EAST);

        return panel;
    }



    private JPanel crearContenido() {

        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setOpaque(false);

        panel.add(crearPanelPendientes());
        panel.add(crearPanelEnviadas());

        return panel;
    }



    private JPanel crearPanelPendientes() {

        JPanel panel = crearTarjeta();

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);

        JLabel titulo = crearTitulo("Citas pendientes");

        lblPendientes = crearEtiquetaCantidad("0 citas");

        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(lblPendientes, BorderLayout.EAST);

        modeloPendientes = crearModelo();

        tablaPendientes = new JTable(modeloPendientes);
        estilizarTabla(tablaPendientes);

        JScrollPane scroll = new JScrollPane(tablaPendientes);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JLabel descripcion = crearDescripcion(
                "Citas programadas para mañana que requieren confirmación."
        );

        panel.add(encabezado, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(descripcion, BorderLayout.SOUTH);

        return panel;
    }



    private JPanel crearPanelEnviadas() {

        JPanel panel = crearTarjeta();

        JPanel encabezado = new JPanel(new BorderLayout());

        encabezado.setOpaque(false);

        JLabel titulo = crearTitulo("Notificaciones enviadas");

        lblEnviadas = crearEtiquetaCantidad("0 enviadas");

        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(lblEnviadas, BorderLayout.EAST);

        modeloEnviadas = crearModelo();

        tablaEnviadas = new JTable(modeloEnviadas);
        estilizarTabla(tablaEnviadas);

        JScrollPane scroll = new JScrollPane(tablaEnviadas);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JLabel descripcion = crearDescripcion(
                "Correos enviados correctamente durante esta sesión."
        );

        JButton btnEnviar = crearBoton("Enviar notificaciones");

        btnEnviar.addActionListener(e -> enviarNotificaciones());

        JPanel inferior = new JPanel(new BorderLayout(5, 10));
        inferior.setOpaque(false);

        inferior.add(descripcion, BorderLayout.NORTH);
        inferior.add(btnEnviar, BorderLayout.SOUTH);

        panel.add(encabezado, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(inferior, BorderLayout.SOUTH);

        return panel;
    }



    private DefaultTableModel crearModelo() {

        return new DefaultTableModel(
                new String[]{
                    "ID", "Cliente", "Servicio", "Fecha", "Hora"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }



    public void actualizarTablas() {

        modeloPendientes.setRowCount(0);
        modeloEnviadas.setRowCount(0);

        List<Cita> pendientes =
                controladora.obtenerCitasSinConfirmar();

        for (Cita cita : pendientes) {
            agregarCita(modeloPendientes, cita);
        }

        for (Cita cita : citasEnviadas) {
            agregarCita(modeloEnviadas, cita);
        }

        lblPendientes.setText(pendientes.size() + " citas");
        lblEnviadas.setText(citasEnviadas.size() + " enviadas");
    }

    private void agregarCita(
            DefaultTableModel modelo,
            Cita cita) {

        modelo.addRow(new Object[]{
            cita.getId(),
            cita.getCliente().getNombre(),
            cita.getTipoServicio().getDescripcion(),
            cita.getFecha(),
            String.format("%02d:00", cita.getHora())
        });
    }



    private void enviarNotificaciones() {

        List<Cita> citas =
                controladora.obtenerCitasSinConfirmar();

        if (citas.isEmpty()) {

            mostrarMensaje(
                    "No hay citas pendientes de confirmación para mañana."
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "Se encontraron " + citas.size()
                + " citas pendientes para mañana.\n"
                + "¿Desea iniciar el envío de notificaciones?",
                "Confirmar envío",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        int enviadas = 0;
        int omitidas = 0;

        for (Cita cita : citas) {

            boolean enviado = false;

            while (!enviado) {

                try {

                    enviado =
                            controladora.enviarCorreoConfirmacion(cita);

                } catch (Exception ex) {

                    enviado = false;
                }

                if (enviado) {

                    if (!citasEnviadas.contains(cita)) {
                        citasEnviadas.add(cita);
                    }

                    enviadas++;
                    break;
                }

                Object[] opciones = {
                    "Reintentar",
                    "Continuar",
                    "Abortar"
                };

                int opcion = JOptionPane.showOptionDialog(
                        this,
                        "No se pudo enviar el correo al cliente:\n"
                        + cita.getCliente().getNombre()
                        + "\n\n¿Qué desea hacer?",
                        "Error al enviar notificación",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        opciones,
                        opciones[0]
                );

                if (opcion == 0) {

                    continue;

                } else if (opcion == 1) {

                    omitidas++;
                    break;

                } else {

                    actualizarTablas();

                    mostrarMensaje(
                            "Proceso interrumpido.\n"
                            + "Enviadas: " + enviadas
                            + "\nOmitidas: " + omitidas
                    );

                    return;
                }
            }
        }

        actualizarTablas();

        mostrarMensaje(
                "Proceso finalizado.\n"
                + "Notificaciones enviadas: " + enviadas
                + "\nCitas omitidas: " + omitidas
        );
    }



    private JPanel crearPanelHorario() {

        JPanel panel = crearTarjeta();

        JLabel titulo = crearTitulo("Horario de atención");

        cmbDia = new JComboBox<>(new String[]{
            "Lunes", "Martes", "Miércoles",
            "Jueves", "Viernes", "Sábado", "Domingo"
        });

        chkAtiende = new JCheckBox("Se atiende este día");
        chkAtiende.setOpaque(false);
        chkAtiende.setForeground(EstiloUI.TEXTO);

        cmbApertura = new JComboBox<>();
        cmbCierre = new JComboBox<>();

        for (int i = 0; i <= 24; i++) {
            cmbApertura.addItem(i);
            cmbCierre.addItem(i);
        }

        JButton btnGuardar = crearBoton("Guardar horario");

        btnGuardar.addActionListener(e -> guardarHorario());

        cmbDia.addActionListener(e -> cargarHorarioDia());

        chkAtiende.addActionListener(e -> actualizarEstadoHoras());

        JPanel campos = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 12, 8)
        );

        campos.setOpaque(false);

        campos.add(new JLabel("Día:"));
        campos.add(cmbDia);
        campos.add(chkAtiende);

        campos.add(new JLabel("Apertura:"));
        campos.add(cmbApertura);

        campos.add(new JLabel("Cierre:"));
        campos.add(cmbCierre);

        campos.add(btnGuardar);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(campos, BorderLayout.CENTER);

        return panel;
    }



    private void cargarHorario() {

        cmbDia.setSelectedIndex(0);
        cargarHorarioDia();
    }

    private void cargarHorarioDia() {

        DayOfWeek dia = obtenerDiaSeleccionado();

        HorarioDia horario = controladora.obtenerHorario(dia);

        if (horario != null) {

            chkAtiende.setSelected(horario.isAtiende());

            cmbApertura.setSelectedItem(
                    horario.getHoraApertura()
            );

            cmbCierre.setSelectedItem(
                    horario.getHoraCierre()
            );

        } else {

            chkAtiende.setSelected(false);
            cmbApertura.setSelectedItem(8);
            cmbCierre.setSelectedItem(17);
        }

        actualizarEstadoHoras();
    }



    private void guardarHorario() {

        try {

            DayOfWeek dia = obtenerDiaSeleccionado();

            boolean atiende = chkAtiende.isSelected();

            int apertura = (Integer) cmbApertura.getSelectedItem();
            int cierre = (Integer) cmbCierre.getSelectedItem();

            if (atiende && apertura >= cierre) {

                mostrarError(
                        "La hora de cierre debe ser posterior "
                        + "a la hora de apertura."
                );

                return;
            }

            controladora.configurarHorario(
                    dia,
                    atiende,
                    apertura,
                    cierre
            );

            mostrarMensaje("Horario guardado correctamente.");

        } catch (Exception ex) {

            mostrarError(ex.getMessage());
        }
    }

    private DayOfWeek obtenerDiaSeleccionado() {

        return DayOfWeek.of(cmbDia.getSelectedIndex() + 1);
    }

    private void actualizarEstadoHoras() {

        boolean atiende = chkAtiende.isSelected();

        cmbApertura.setEnabled(atiende);
        cmbCierre.setEnabled(atiende);
    }



    private JPanel crearTarjeta() {

        JPanel panel = new JPanel(new BorderLayout(12, 12));

        panel.setBackground(Color.WHITE);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 220, 220)
                ),
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        ));

        return panel;
    }

    private JLabel crearTitulo(String texto) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 16));
        etiqueta.setForeground(EstiloUI.VINO);

        return etiqueta;
    }

    private JLabel crearEtiquetaCantidad(String texto) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 12));
        etiqueta.setForeground(EstiloUI.VINO);

        return etiqueta;
    }

    private JLabel crearDescripcion(String texto) {

        JLabel etiqueta = new JLabel(texto);

        etiqueta.setFont(new Font("SansSerif", Font.PLAIN, 11));
        etiqueta.setForeground(EstiloUI.SECUNDARIO);

        return etiqueta;
    }

    private JButton crearBoton(String texto) {

        JButton boton = new JButton(texto);

        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBackground(EstiloUI.VINO);

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);

        boton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        boton.setBorder(
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        );

        return boton;
    }

    private void estilizarTabla(JTable tabla) {

        tabla.setRowHeight(30);

        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.setForeground(EstiloUI.TEXTO);

        tabla.setBackground(Color.WHITE);

        tabla.setSelectionBackground(
                new Color(239, 222, 226)
        );

        tabla.setSelectionForeground(EstiloUI.TEXTO);

        tabla.setGridColor(new Color(235, 230, 230));

        tabla.setShowVerticalLines(false);
        tabla.setFillsViewportHeight(true);

        tabla.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        tabla.getTableHeader().setBackground(
                new Color(242, 237, 237)
        );

        tabla.getTableHeader().setForeground(EstiloUI.TEXTO);

        tabla.getTableHeader().setReorderingAllowed(false);
    }



    private void mostrarMensaje(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Administración",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
