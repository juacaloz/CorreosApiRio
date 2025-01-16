package co.com.bancolombia.model.attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Attachment {

    private String id;
    private String name;
    private String emailXmlName;
    private String emailPdfName;
    private String excel;
    private int size;
    private String extension;

}
