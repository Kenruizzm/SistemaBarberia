package logica;

import java.io.Serializable;
import java.time.LocalDate;

public class Cita implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private LocalDate fecha;
    private int hora;
    private boolean confirmada;
    private Cliente cliente;
    private TipoServicio tipoServicio;

    public Cita(int id, LocalDate fecha, int hora, boolean confirmada, Cliente cliente, TipoServicio tipoServicio) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.confirmada = confirmada;
        this.cliente = cliente;
        this.tipoServicio = tipoServicio;
    }

    public int getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public void confirmar() {
        this.confirmada = true;
    }
}