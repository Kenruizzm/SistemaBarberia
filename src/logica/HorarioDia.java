package logica;
import java.io.Serializable;
import java.time.DayOfWeek;

public class HorarioDia implements Serializable {
    private static final long serialVersionUID = 1L;

    private DayOfWeek dia;
    private boolean atiende;
    private int horaApertura;
    private int horaCierre;

    public HorarioDia(DayOfWeek dia, boolean atiende, int horaApertura, int horaCierre) {
        this.dia = dia;
        this.atiende = atiende;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
    }

    public DayOfWeek getDia() {
        return dia;
    }
    public boolean isAtiende() {
        return atiende;
    }
    public int getHoraApertura() {
        return horaApertura;
    }
    public int getHoraCierre() {
        return horaCierre;
    }

    public void configurar(boolean atiende, int horaApertura, int horaCierre) {
        this.atiende = atiende;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
    }
}