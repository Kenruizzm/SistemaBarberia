package interfaz;

import control.Controladora;
import java.awt.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProgramaPrincipal extends JFrame {

    private final Controladora controladora;

    private CardLayout tarjetas;
    private JPanel contenido;

    private paneldeClientes panelClientes;
    private paneldeCitas panelCitas;
    private paneldeListadeEspera panelLista;
    private paneldeAdministracion panelAdmin;

    private JButton botonActivo;

    public ProgramaPrincipal(Controladora controladora) {

        super("Barbería | Administración");

        this.controladora = controladora;

        EstiloUI.aplicar();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 680));
        setSize(1180, 740);
        setLocationRelativeTo(null);

        getContentPane().setBackground(EstiloUI.FONDO);
        setLayout(new BorderLayout());

        tarjetas = new CardLayout();
        contenido = new JPanel(tarjetas);

        crearContenido();

        add(crearMenuLateral(), BorderLayout.WEST);
        add(contenido, BorderLayout.CENTER);

        // Guardar los datos al cerrar el programa
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                try {

                    controladora.guardarDatos(Controladora.ARCHIVO);

                    System.out.println(
                            "Datos guardados correctamente.");

                } catch (java.io.IOException ex) {

                    JOptionPane.showMessageDialog(
                            ProgramaPrincipal.this,
                            "No se pudieron guardar los datos: "
                            + ex.getMessage(),
                            "Error al guardar",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    private void crearContenido() {

        panelClientes = new paneldeClientes(controladora);
        panelCitas = new paneldeCitas(controladora);
        panelLista = new paneldeListadeEspera(controladora);
        panelAdmin = new paneldeAdministracion(controladora);

        contenido.setBackground(EstiloUI.FONDO);

        contenido.add(crearInicio(), "inicio");
        contenido.add(panelClientes, "clientes");
        contenido.add(panelCitas, "citas");
        contenido.add(panelLista, "lista");
        contenido.add(panelAdmin, "admin");
    }

    private JPanel crearMenuLateral() {

        JPanel menu = new JPanel();

        menu.setPreferredSize(new Dimension(225, 0));
        menu.setBackground(new Color(50, 47, 48));

        menu.setBorder(
                BorderFactory.createEmptyBorder(22, 14, 18, 14));

        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        JLabel marca = new JLabel("  BARBERÍA");
        marca.setForeground(Color.WHITE);
        marca.setFont(new Font("SansSerif", Font.BOLD, 21));

        JLabel subtitulo = new JLabel("Sistema Barberia");
        subtitulo.setForeground(new Color(205, 190, 190));
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 10));

        menu.add(marca);
        menu.add(Box.createVerticalStrut(5));
        menu.add(subtitulo);
        menu.add(Box.createVerticalStrut(38));

        agregarOpcion(menu, "Inicio", "inicio");
        agregarOpcion(menu, "Clientes", "clientes");
        agregarOpcion(menu, "Citas", "citas");
        agregarOpcion(menu, "Lista de espera", "lista");
        agregarOpcion(menu, "Administración", "admin");

        menu.add(Box.createVerticalGlue());

        JLabel pie = new JLabel("Sistema de gestión");
        pie.setForeground(new Color(190, 180, 180));
        pie.setFont(new Font("SansSerif", Font.PLAIN, 11));

        menu.add(pie);

        return menu;
    }

    private void agregarOpcion(JPanel menu,
            String texto, String tarjeta) {

        JButton b = new JButton(texto);

        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);

        b.setForeground(new Color(235, 230, 230));
        b.setBackground(new Color(50, 47, 48));

        b.setBorder(
                BorderFactory.createEmptyBorder(0, 15, 0, 8));

        b.setFocusPainted(false);
        b.setOpaque(true);

        b.setFont(new Font("SansSerif", Font.BOLD, 13));

        b.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        b.addActionListener(e -> {

            tarjetas.show(contenido, tarjeta);

            if (botonActivo != null) {
                botonActivo.setBackground(
                        new Color(50, 47, 48));

                botonActivo.setForeground(
                        new Color(235, 230, 230));
            }

            botonActivo = b;

            b.setBackground(EstiloUI.VINO);
            b.setForeground(Color.WHITE);

            refrescarDatos();
        });

        menu.add(b);
        menu.add(Box.createVerticalStrut(7));

        if (tarjeta.equals("inicio")) {
            botonActivo = b;
            b.setBackground(EstiloUI.VINO);
            b.setForeground(Color.WHITE);
        }
    }

    private JPanel crearInicio() {

        JPanel p = new JPanel(new BorderLayout());

        p.setBackground(EstiloUI.FONDO);

        p.setBorder(
                BorderFactory.createEmptyBorder(50, 40, 40, 40));

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("SistemaBarberia");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        titulo.setForeground(EstiloUI.VINO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel bienvenida = new JLabel(
                "¡Bienvenido a nuestro Sistema de Barberia!");

        bienvenida.setFont(new Font("SansSerif", Font.PLAIN, 16));
        bienvenida.setForeground(EstiloUI.SECUNDARIO);
        bienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descripcion = new JLabel(
                "<html><div style='text-align:center;'>"
                + "Qué tal viajero. Aquí podéis administrar las citas, "
                + "clientes y servicios de tu barbería desde un solo lugar.<br>"
                + "Mantén toda la información organizada de manera sencilla."
                + "</div></html>",
                SwingConstants.CENTER
        );

        descripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descripcion.setForeground(EstiloUI.SECUNDARIO);
        descripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        descripcion.setHorizontalAlignment(SwingConstants.CENTER);
        descripcion.setMaximumSize(new Dimension(700, 60));

        JLabel opciones = new JLabel("¿Qué deseas hacer hoy?");
        opciones.setFont(new Font("SansSerif", Font.BOLD, 20));
        opciones.setForeground(EstiloUI.TEXTO);
        opciones.setAlignmentX(Component.CENTER_ALIGNMENT);

        centro.add(Box.createVerticalGlue());
        centro.add(titulo);
        centro.add(Box.createVerticalStrut(12));
        centro.add(bienvenida);
        centro.add(Box.createVerticalStrut(15));
        centro.add(descripcion);
        centro.add(Box.createVerticalStrut(65));
        centro.add(opciones);
        centro.add(Box.createVerticalStrut(25));

        centro.add(botonInicio("Clientes", "clientes"));
        centro.add(Box.createVerticalStrut(15));

        centro.add(botonInicio("Citas", "citas"));
        centro.add(Box.createVerticalStrut(15));

        centro.add(botonInicio("Lista de espera", "lista"));
        centro.add(Box.createVerticalStrut(15));

        centro.add(botonInicio("Administración", "admin"));

        centro.add(Box.createVerticalGlue());

        p.add(centro, BorderLayout.CENTER);

        return p;
    }

    private JButton botonInicio(String texto, String destino) {

        JButton boton = new JButton(texto) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());

                g2.fillRoundRect(
                        0, 0, getWidth(), getHeight(), 25, 25);

                g2.dispose();

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {

            }
        };

        boton.setText(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(EstiloUI.VINO);

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);

        boton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        boton.setPreferredSize(new Dimension(260, 48));
        boton.setMaximumSize(new Dimension(260, 48));
        boton.setMinimumSize(new Dimension(260, 48));

        boton.setAlignmentX(Component.CENTER_ALIGNMENT);

        boton.addActionListener(e -> {
            tarjetas.show(contenido, destino);
            refrescarDatos();
        });

        return boton;
    }

    private void refrescarDatos() {

        panelClientes.actualizarTabla();

        panelCitas.actualizarCombos();
        panelCitas.actualizarTabla();

        panelLista.actualizarCombos();
        panelLista.actualizarTabla();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Recuperar los datos guardados anteriormente
            Controladora controladora =
                    Controladora.iniciar(Controladora.ARCHIVO);

            // Abrir el sistema con la información recuperada
            new ProgramaPrincipal(controladora).setVisible(true);
        });
    }
}
