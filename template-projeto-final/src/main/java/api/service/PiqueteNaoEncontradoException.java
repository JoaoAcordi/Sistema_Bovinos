// CAMADA: Service
// RESPONSABILIDADE: exceção lançada quando um piquete não for localizado (HTTP 404).
// SITUAÇÃO: implementada.
package api.service;

public class PiqueteNaoEncontradoException extends RecursoNaoEncontradoException {

    public PiqueteNaoEncontradoException(Long id) {
        super("Piquete não encontrado: " + id);
    }
}
