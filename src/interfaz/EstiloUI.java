package interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public final class EstiloUI {

    public static final Color FONDO = new Color(248, 247, 245);
    public static final Color BLANCO = Color.WHITE;
    public static final Color VINO = new Color(128, 36, 55);
    public static final Color VINO_OSCURO = new Color(93, 24, 40);
    public static final Color TEXTO = new Color(48, 45, 45);
    public static final Color SECUNDARIO = new Color(125, 119, 117);
    public static final Color BORDE = new Color(230, 225, 222);
    public static final Color SUAVE = new Color(248, 239, 241);

    private EstiloUI() {}

    public static void aplicar() {
        UIManager.put("Panel.background", FONDO);
        UIManager.put("OptionPane.background", BLANCO);
        UIManager.put("OptionPane.messageForeground", TEXTO);
    }

    public static void estilizarBoton(JButton boton) {
        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setForeground(TEXTO);
        boton.setBackground(BLANCO);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBorder(new CompoundBorder(
                new LineBorder(BORDE, 1, true),
                new EmptyBorder(9, 14, 9, 14)));
        boton.setOpaque(true);
    }

    public static void botonPrincipal(JButton boton) {
        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBackground(VINO);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBorder(new EmptyBorder(10, 16, 10, 16));
        boton.setOpaque(true);
    }

    public static void estilizarCampo(JTextField campo) {
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campo.setForeground(TEXTO);
        campo.setBackground(BLANCO);
        campo.setBorder(new CompoundBorder(
                new LineBorder(BORDE, 1, true),
                new EmptyBorder(8, 9, 8, 9)));
    }

    public static void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setBackground(BLANCO);
        combo.setForeground(TEXTO);
    }

    public static JPanel tarjeta(String titulo) {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBackground(BLANCO);
        p.setBorder(new CompoundBorder(
                new LineBorder(BORDE, 1, true),
                new EmptyBorder(14, 14, 14, 14)));

        if (titulo != null && !titulo.isBlank()) {
            JLabel l = new JLabel(titulo);
            l.setFont(new Font("SansSerif", Font.BOLD, 14));
            l.setForeground(TEXTO);
            p.add(l, BorderLayout.NORTH);
        }

        return p;
    }

    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.setForeground(TEXTO);
        tabla.setBackground(BLANCO);
        tabla.setRowHeight(29);
        tabla.setGridColor(BORDE);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(SUAVE);
        tabla.setSelectionForeground(VINO_OSCURO);
        tabla.setFillsViewportHeight(true);

        JTableHeader h = tabla.getTableHeader();
        h.setFont(new Font("SansSerif", Font.BOLD, 12));
        h.setForeground(TEXTO);
        h.setBackground(new Color(244, 241, 239));
        h.setPreferredSize(
                new Dimension(h.getPreferredSize().width, 34));
    }

    public static JScrollPane scroll(JTable tabla) {
        JScrollPane s = new JScrollPane(tabla);
        s.setBorder(new LineBorder(BORDE, 1, true));
        s.getViewport().setBackground(BLANCO);
        return s;
    }

    public static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setForeground(SECUNDARIO);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        return l;
    }

    public static void mensaje(Component padre, String texto) {
        JOptionPane.showMessageDialog(
                padre, texto, "Barbería",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(Component padre, String texto) {
        JOptionPane.showMessageDialog(
                padre, texto, "Revisar información",
                JOptionPane.ERROR_MESSAGE);
    }
}