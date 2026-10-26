// CAMADA: Repository
// RESPONSABILIDADE: operações de banco de dados para a entidade Boi.
// SITUAÇÃO: implementada.
package api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import api.model.Boi;
import api.model.StatusBoi;

public interface BoiRepository extends JpaRepository<Boi, Long> {

    boolean existsByMicrochip(String microchip);

    Optional<Boi> findByMicrochip(String microchip);

    long countByPiqueteIdAndStatus(Long piqueteId, StatusBoi status);

    List<Boi> findByStatus(StatusBoi status);

    List<Boi> findByPiqueteId(Long piqueteId);

    List<Boi> findByPiqueteIdAndStatus(Long piqueteId, StatusBoi status);
}
