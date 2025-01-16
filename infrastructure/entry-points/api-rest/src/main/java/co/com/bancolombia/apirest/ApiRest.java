package co.com.bancolombia.ApiRest;

import org.springframework.stereotype.Controller;

import co.com.bancolombia.usecase.message.MessageUseCase;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

@Controller
@AllArgsConstructor
public class ApiRest {

    @Autowired
    private final MessageUseCase messageUseCase;

    @Scheduled(cron = "0 */2 7-18 * * MON-FRI")
    public void commandName(){

        messageUseCase.getMessageList();
    }

    
}
