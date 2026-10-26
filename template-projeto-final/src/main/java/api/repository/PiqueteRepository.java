// CAMADA: Repository
// RESPONSABILIDADE: operações de banco de dados para a entidade Piquete.
// SITUAÇÃO: implementada.
package api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.model.Piquete;

public interface PiqueteRepository extends JpaRepository<Piquete, Long> {
}
