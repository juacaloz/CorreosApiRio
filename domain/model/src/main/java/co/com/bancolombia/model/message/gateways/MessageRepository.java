package co.com.bancolombia.model.message.gateways;

import java.util.List;

import co.com.bancolombia.model.folder.Folder;
import co.com.bancolombia.model.message.Message;

public interface MessageRepository {

    List<Message> getMessageList();

    // List<Folder> getMailFolders();

    // void moveMessageToEmailFolder(Message var1, int var2);

    // void createFolderIfNotExists(List<Folder> var1);

    // String validarSubjet(Message var1);

}
