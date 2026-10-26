// CAMADA: Model
// RESPONSABILIDADE: entidade que registra a transferência de um animal entre piquetes.
// SITUAÇÃO: implementada.
package api.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimentacoes")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boi_id", nullable = false)
    private Boi boi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piquete_origem_id", nullable = false)
    private Piquete piqueteOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piquete_destino_id", nullable = false)
    private Piquete piqueteDestino;

    @Column(name = "data_movimentacao", nullable = false)
    private LocalDate dataMovimentacao;

    @Column(nullable = false)
    private String motivo;

    protected Movimentacao() {
    }

    public Movimentacao(Boi boi, Piquete piqueteOrigem, Piquete piqueteDestino, LocalDate dataMovimentacao, String motivo) {
        this.boi = boi;
        this.piqueteOrigem = piqueteOrigem;
        this.piqueteDestino = piqueteDestino;
        this.dataMovimentacao = dataMovimentacao;
        this.motivo = motivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boi getBoi() {
        return boi;
    }

    public void setBoi(Boi boi) {
        this.boi = boi;
    }

    public Piquete getPiqueteOrigem() {
        return piqueteOrigem;
    }

    public void setPiqueteOrigem(Piquete piqueteOrigem) {
        this.piqueteOrigem = piqueteOrigem;
    }

    public Piquete getPiqueteDestino() {
        return piqueteDestino;
    }

    public void setPiqueteDestino(Piquete piqueteDestino) {
        this.piqueteDestino = piqueteDestino;
    }

    public LocalDate getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDate dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
