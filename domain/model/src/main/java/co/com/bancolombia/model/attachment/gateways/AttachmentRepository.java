package co.com.bancolombia.model.attachment.gateways;

import java.io.File;
import java.util.List;
import java.io.IOException;

import co.com.bancolombia.model.attachment.Attachment;
import co.com.bancolombia.model.message.Message;

public interface AttachmentRepository {

    List<Attachment> getAttachmentsList(String var1, String var2);

    boolean validateAttachments(List<Attachment> var1, String var2);

    boolean validateFiles(List<File> var1);

    Attachment renameFile(Attachment var1, String var2);

    String getExtension(String var1);

    List<File> unzipFiles(Message var1, Attachment var2) throws IOException;

    List<File> renameFiles(List<File> var1, int var2) throws IOException;

    List<File> processFile(List<File> var1) throws IOException;

    boolean countFilesZipped(Attachment var1) throws IOException;





}
