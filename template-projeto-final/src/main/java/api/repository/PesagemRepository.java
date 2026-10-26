// CAMADA: Repository
// RESPONSABILIDADE: operações de banco de dados para a entidade Pesagem.
// SITUAÇÃO: implementada.
package api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import api.model.Pesagem;

public interface PesagemRepository extends JpaRepository<Pesagem, Long> {

    List<Pesagem> findByBoiIdOrderByDataPesagemDesc(Long boiId);

    long countByBoiId(Long boiId);
}
