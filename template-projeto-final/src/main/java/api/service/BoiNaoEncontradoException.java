// CAMADA: Service
// RESPONSABILIDADE: exceção lançada quando um boi não for localizado por ID ou microchip (HTTP 404).
// SITUAÇÃO: implementada.
package api.service;

public class BoiNaoEncontradoException extends RecursoNaoEncontradoException {

    public BoiNaoEncontradoException(Long id) {
        super("Boi não encontrado: " + id);
    }

    public BoiNaoEncontradoException(String microchip) {
        super("Boi não encontrado com microchip: " + microchip);
    }
}
