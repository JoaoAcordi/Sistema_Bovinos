// CAMADA: Repository
// RESPONSABILIDADE: operações de banco de dados para a entidade Movimentacao.
// SITUAÇÃO: implementada.
package api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import api.model.Movimentacao;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    List<Movimentacao> findByBoiIdOrderByDataMovimentacaoDesc(Long boiId);
}
