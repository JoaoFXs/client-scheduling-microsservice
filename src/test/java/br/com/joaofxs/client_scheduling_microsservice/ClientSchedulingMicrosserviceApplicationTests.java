package br.com.joaofxs.client_scheduling_microsservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ClientSchedulingMicrosserviceApplicationTests {

	@Test
	@DisplayName("Deve carregar o contexto da aplicação sem erros")
	void contextLoads() {
		// Este teste verifica se o contexto do Spring pode ser carregado.
		// O corpo vazio é intencional. O teste passa se nenhuma exceção for lançada durante a inicialização.
	}

	@Test
	@DisplayName("Deve executar o método main para cobertura de código")
	void main() {
		// Este teste cobre a chamada ao método main().
		// Usamos um mock para evitar que a aplicação Spring inicie de verdade.
		try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
			mocked.when(() -> SpringApplication.run(ClientSchedulingMicrosserviceApplication.class, new String[]{}))
					.thenReturn(Mockito.mock(ConfigurableApplicationContext.class));

			ClientSchedulingMicrosserviceApplication.main(new String[]{});

			mocked.verify(() -> SpringApplication.run(ClientSchedulingMicrosserviceApplication.class, new String[]{}));
		}
	}
}