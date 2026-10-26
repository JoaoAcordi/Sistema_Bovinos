// CAMADA: Model
// RESPONSABILIDADE: entidade que representa um animal do rebanho identificado por microchip.
// SITUAÇÃO: implementada.
package api.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bois")
public class Boi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String microchip;

    @Column(nullable = false, length = 100)
    private String raca;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusBoi status = StatusBoi.ATIVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "piquete_id", nullable = false)
    private Piquete piquete;

    protected Boi() {
    }

    public Boi(String microchip, String raca, LocalDate dataNascimento, StatusBoi status, Piquete piquete) {
        this.microchip = microchip;
        this.raca = raca;
        this.dataNascimento = dataNascimento;
        this.status = status;
        this.piquete = piquete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMicrochip() {
        return microchip;
    }

    public void setMicrochip(String microchip) {
        this.microchip = microchip;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public StatusBoi getStatus() {
        return status;
    }

    public void setStatus(StatusBoi status) {
        this.status = status;
    }

    public Piquete getPiquete() {
        return piquete;
    }

    public void setPiquete(Piquete piquete) {
        this.piquete = piquete;
    }
}
