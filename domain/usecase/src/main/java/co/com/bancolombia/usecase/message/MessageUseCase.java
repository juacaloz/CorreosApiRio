package co.com.bancolombia.usecase.message;

import java.util.ArrayList;
import java.util.List;

import co.com.bancolombia.model.attachment.Attachment;
import co.com.bancolombia.model.attachment.gateways.AttachmentRepository;
import co.com.bancolombia.model.folder.gateways.FolderRepository;
import co.com.bancolombia.model.message.Message;
import co.com.bancolombia.model.message.gateways.MessageRepository;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class MessageUseCase {
    private final MessageRepository messageRepository;
    private final FolderRepository folderRepository;
    private final AttachmentRepository attachmentRepository;

    public List<Message> getMessageList() {
        List<Message> messageList = new ArrayList<>();

        // folderRepository.createDirectory();
        // messageRepository.createFolderIfNotExists(messageRepository.getMailFolders());
        for (Message message : messageRepository.getMessageList()) {

            List<Attachment> attachments = attachmentRepository.getAttachmentsList(message.getId(), message.getSubject());
            message.setAttachments(attachments);
            messageList.add(message);

        }
        return  messageList;



    }
    // public void moveMessageToEmailFolder(Message message, int folderId) {
    //     messageRepository.moveMessageToEmailFolder(message, folderId);
    // }

    // public void createFolderIfNotExists(List<Folder> folders) {
    //     folderRepository.createFolderIfNotExists(folders);
    // }

    // public String validarSubjet(Message message) {
    //     return messageRepository.validarSubjet(message);
    // }
}
