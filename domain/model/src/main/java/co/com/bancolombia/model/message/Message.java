package co.com.bancolombia.model.message;
import java.util.List;

import co.com.bancolombia.model.attachment.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Message {

    private String id;
    private String subject;
    private boolean hasAttachment;
    private int attachmentCount;
    private String sender;
    private List<Attachment> attachments;
    private String information;
}
