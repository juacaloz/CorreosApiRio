package co.com.bancolombia.apigraph;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.requests.GraphServiceClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Setter
@Getter

public class ApiGraphConfig {
    private GraphServiceClient appClient;

    @Value("${apigraph.credentials.tenantId}")
    private String tenantId;
    @Value("${apigraph.credentials.clientSecret}")
    private String secret;
    @Value("${apigraph.credentials.clientId}")
    private String clientId;

    public GraphServiceClient initializeGraph(){
        if (appClient == null) {
            ClientSecretCredential clientSecretCredential;
            clientSecretCredential = new ClientSecretCredentialBuilder()
                    .clientId(clientId)
                    .tenantId(tenantId)
                    .clientSecret(secret)
                    .build();
            final TokenCredentialAuthProvider authProvider =
                    new TokenCredentialAuthProvider(
                            List.of("https://graph.microsoft.com/.default"), clientSecretCredential);

            appClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
        }
        return appClient;
    }
}
