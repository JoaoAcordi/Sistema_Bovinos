// Declara o pacote das classes de configuração da aplicação.
package api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiDoProjeto() {
        return new OpenAPI()
                .info(new Info()
                        .title("BoiChip - Sistema de Cadastro e Controle de Bovinos")
                        .version("Etapa 1")
                        .description("API REST para controlar o rebanho de uma fazenda de gado de corte por meio do microchip "
                                + "de cada boi: cadastro dos animais, pesagens, troca de piquete e baixa (venda, abate ou morte). "
                                + "Todo corpo de requisição e de resposta é um **DTO** e todo erro 400/404 sai no formato `ErroDTO`.")
                        .contact(new Contact().name("BoiChip Team - SATC")));
    }
}
