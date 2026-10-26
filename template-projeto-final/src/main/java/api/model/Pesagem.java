// CAMADA: Model
// RESPONSABILIDADE: entidade que armazena uma pesagem de um animal em determinada data.
// SITUAÇÃO: implementada.
package api.model;

import java.math.BigDecimal;
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
@Table(name = "pesagens")
public class Pesagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boi_id", nullable = false)
    private Boi boi;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal peso;

    @Column(name = "data_pesagem", nullable = false)
    private LocalDate dataPesagem;

    protected Pesagem() {
    }

    public Pesagem(Boi boi, BigDecimal peso, LocalDate dataPesagem) {
        this.boi = boi;
        this.peso = peso;
        this.dataPesagem = dataPesagem;
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

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public LocalDate getDataPesagem() {
        return dataPesagem;
    }

    public void setDataPesagem(LocalDate dataPesagem) {
        this.dataPesagem = dataPesagem;
    }
}
