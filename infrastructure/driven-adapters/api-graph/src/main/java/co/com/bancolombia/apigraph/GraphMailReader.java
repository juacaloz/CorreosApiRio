package co.com.bancolombia.apigraph;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.microsoft.graph.http.GraphServiceException;
import com.microsoft.graph.requests.MessageCollectionPage;

import co.com.bancolombia.apigraph.config.ApiGraphConfig;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.gateways.MessageRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder(toBuilder = true)
@Service
@Getter
@Setter
public class GraphMailReader implements MessageRepository{

    private final String subjectMailIncompletos;
    public static final int ITEMSTOPROCESS = 999;
    public static final int MAXNUMBEROFCONSECUTIVE = 999;
    public static final int MAXNUMBEROFFOLDER = 99;
    private final ApiGraphConfig apiGraphConfig;

    @Override
    public List<Message> getMessageList() {
        List<Message> messages = new ArrayList<>();
        try{
            MessageCollectionPage messageCollectionPage = apiGraphConfig.initializeGraph()
                    .users()
                    .mailFolders("inbox")
                    .messages()
                    .buildRequest()
                    .top(ITEMSTOPROCESS)
                    .get();


        } catch ( GraphServiceException e){
            System.out.println("Error al obtener los mensajes");

        }

        return messages;
    }

}
