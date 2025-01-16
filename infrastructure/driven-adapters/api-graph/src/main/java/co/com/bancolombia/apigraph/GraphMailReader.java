package co.com.bancolombia.apigraph;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.GraphServiceException;
import com.microsoft.graph.models.FileAttachment;
import com.microsoft.graph.models.MailFolder;
import com.microsoft.graph.models.MessageMoveParameterSet;
import com.microsoft.graph.requests.AttachmentCollectionPage;
import com.microsoft.graph.requests.MailFolderCollectionPage;
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








    
    // @Override
    // public List<Folder> getMailFolders(){
    //     List<Folder> mailFolders = new ArrayList<>();
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Se solicita la recuperación de carpetas del buzón de correos.");
    //     try{
    //         MailFolderCollectionPage mailFolderCollectionPage = apiGraphConfig.initializeGraph()
    //                 .users(userId)
    //                 .mailFolders()
    //                 .buildRequest()
    //                 .top(MAXNUMBEROFFOLDER)
    //                 .get();
    //         mailFolders = mailFolderCollectionPage.getCurrentPage()
    //                 .stream()
    //                 .map(folder -> {
    //                     return Folder.builder().id(folder.id).name(folder.displayName).build();
    //                 }).collect(Collectors.toList());
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //                 "Se finaliza la recuperación de correos electrónicos " +
    //                         "en bandeja de entrada correctamente.");
    //         return mailFolders;
    //     } catch (GraphServiceException e){
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELERROR,
    //                 "Falló la recuperación de correos electrónicos. Revise " +
    //                         "sus credenciales o que los parámetros de ApiGraph correspondan " +
    //                         "con lo esperado en el buzón. ERROR: " + e.getMessage());
    //         return mailFolders;
    //     }

    // }

    // @Override
    // public List<Attachment> getAttachmentList(String messageId, String subject){
    //     List<Attachment> attachmentList = new ArrayList<>();
    //     try{
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //                 "Se solicita la recuperación de adjuntos del correo: " + subject);
    //         AttachmentCollectionPage attachments = apiGraphConfig.initializeGraph()
    //                 .users(userId)
    //                 .messages(messageId)
    //                 .attachments()
    //                 .buildRequest()
    //                 .top(ITEMSTOPROCESS)
    //                 .get();
    //         attachmentList = attachments.getCurrentPage()
    //                 .stream()
    //                 .map(attachment -> {
    //                     return Attachment.builder().id(attachment.id).name(attachment.name)
    //                             .size(attachment.size)
    //                             .extension(this.getExtension(attachment.name))
    //                             .contentType(attachment.contentType)
    //                             .decodedFile(((FileAttachment) attachment).contentBytes)
    //                             .mailDate(attachment.lastModifiedDateTime)
    //                             .build();
    //                 }).collect(Collectors.toList());
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //                 "Se solicita la recuperación de adjuntos del correo: " + subject);
    //         return attachmentList;
    //     }catch (GraphServiceException e){
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELERROR,
    //                 "Falló la recuperación de adjuntos del " +
    //                         "correo: + " + subject + ". ERROR: " + e.getMessage());
    //         return attachmentList;
    //     }

    // }

    // @Override
    // public boolean validateAttachments(List<Attachment> attachmentList, String subject) {
    //     List<String> extensions = new ArrayList<>();
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO, "Se inicia la " +
    //             "validación de los adjuntos. ASUNTO: " + subject);
    //     for (Attachment attachment : attachmentList){
    //         extensions.add(attachment.getExtension());
    //     }
    //     List<String> uniqueExtensions = extensions.stream().distinct().collect(Collectors.toList());
    //     if ((uniqueExtensions.contains("pdf")
    //             && uniqueExtensions.contains("xml")) ||
    //             (uniqueExtensions.contains("PDF")
    //             && uniqueExtensions.contains("XML"))){
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //                 "Se finaliza la validación de los adjuntos. " +
    //                         "ASUNTO: " + subject + " satisfactoriamente");
    //         return true;
    //     }
    //     logConfRepository.registrarInfo(this.getClass(), LOGWARNLEVEL,
    //             "Se finaliza la validación de los adjuntos los cuales " +
    //                     "no fueron válidos. ASUNTO: " + subject);
    //     return false;
    // }

    // @Override
    // public boolean validateFiles(List<File> fileList) {
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Se inicia la validación de archivos adjuntos.");
    //     List<String> extensions = new ArrayList<>();
    //     for (File file : fileList){
    //         extensions.add(getExtension(file.getName()));
    //     }
    //     List<String> uniqueExtensions = extensions.stream().distinct().collect(Collectors.toList());
    //     if ((uniqueExtensions.contains("pdf")
    //             && uniqueExtensions.contains("xml")
    //             && uniqueExtensions.size() == LIMITFILENUMBER)
    //         || (uniqueExtensions.contains("PDF")
    //             && uniqueExtensions.contains("XML")
    //             && uniqueExtensions.size() == LIMITFILENUMBER)){
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //                 "Se finaliza la validación de archivos adjuntos satisfactoriamente.");
    //         return true;
    //     }
    //     logConfRepository.registrarInfo(GraphServiceException.class,LOGWARNLEVEL,
    //             "Se finaliza la validación de archivos adjuntos " +
    //                     "satisfactoriamente los cuales no fueron válidos.");
    //     return false;
    // }

    // @Override
    // public void moveMessageToEmailFolder(Message message, int i) {
    //     String destinationFolder = "";
    //     logConfRepository.registrarInfo(this.getClass(),LOGLEVELINFO,
    //             "Se inicia proceso de mover correo :" +
    //                     "" + message.getSubject() + "a carpeta de destino.");
    //     if (i == 0){
    //         for(Folder folder : getMailFolders()){
    //             if(NOTPROCESS.equals(folder.name)){
    //                 destinationFolder = folder.id;
    //             }
    //         }
    //     }else if (i == 1){
    //         for(Folder folder : getMailFolders()){
    //             if(folder.name.equals(PROCESSED)){
    //                 destinationFolder = folder.id;
    //             }
    //         }
    //     }else{
    //         for(Folder folder : getMailFolders()){
    //             if(folder.name.equals(UNCOMPLETED)){
    //                 destinationFolder = folder.id;
    //             }
    //         }
    //     }
    //     try{
    //         apiGraphConfig.
    //                 initializeGraph()
    //                 .users(userId)
    //                 .messages(message.getId())
    //                 .move(MessageMoveParameterSet
    //                         .newBuilder()
    //                         .withDestinationId(destinationFolder)
    //                         .build())
    //                 .buildRequest()
    //                 .post()
    //         ;
    //         logConfRepository.registrarInfo(this.getClass(),LOGLEVELINFO,
    //                 "Se finaliza proceso de mover correo :" + message.getSubject() + "a " +
    //                         "carpeta de destino satisfactoriamente.");
    //     }catch (ClientException e){
    //         logConfRepository.registrarInfo(this.getClass(),LOGLEVELERROR,
    //                 "Falló el movimiento de correos :" + message.getSubject());
    //     }
    // }

    // @Override
    // public void createFolderIfNotExists(List<Folder> folders){
    //     boolean procesado = false;
    //     boolean noProcesado = false;
    //     boolean incompleto = false;
    //     MailFolder mailFolder2 = new MailFolder();
    //     MailFolder mailFolder1 = new MailFolder();
    //     MailFolder mailFolder = new MailFolder();
    //     for(Folder folder : folders){
    //         if (PROCESSED.equals(folder.name)){
    //             procesado = true;
    //         }
    //         if (NOTPROCESS.equals(folder.name)){
    //             noProcesado = true;
    //         }
    //         if (UNCOMPLETED.equals(folder.name)){
    //             incompleto = true;
    //         }
    //     }
    //     try{
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //                 "Se solicita la creación de carpetas que no existan " +
    //                         "y sean requeridas en el buzón de correos.");
    //         if (!procesado){
    //             mailFolder1.displayName = PROCESSED;
    //             apiGraphConfig
    //                     .initializeGraph()
    //                     .users(userId)
    //                     .mailFolders()
    //                     .buildRequest()
    //                     .post(mailFolder1)
    //             ;
    //         }
    //         if (!noProcesado){
    //             mailFolder.displayName = NOTPROCESS;
    //             apiGraphConfig
    //                     .initializeGraph()
    //                     .users(userId)
    //                     .mailFolders()
    //                     .buildRequest()
    //                     .post(mailFolder)
    //             ;
    //         }
    //         if (!incompleto){
    //             mailFolder2.displayName = UNCOMPLETED;
    //             apiGraphConfig
    //                     .initializeGraph()
    //                     .users(userId)
    //                     .mailFolders()
    //                     .buildRequest()
    //                     .post(mailFolder2)
    //             ;
    //         }
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //                 "Se finaliza la creación de carpetas en buzón de correos correctamente.");

    //     } catch (GraphServiceException e){
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELERROR,
    //                 "Falló la creación de carpetas en buzón de correos. ERROR: " + e.getMessage());
    //     }

    // }

    // @Override
    // public Attachment renameFile(Attachment attachment, String identifier){
    //     attachment.setName(attachment.getName().concat(identifier));
    //     return attachment;
    // }

    // @Override
    // public String getNewFileName(){
    //     DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    //     LocalDateTime now = LocalDateTime.now();
    //     return "LEA"
    //             .concat(dtf.format(now)
    //             .replaceAll("[^a-zA-Z0-9]", ""));
    // }

    // @Override
    // public String getExtension(String name){
    //     Optional<String> extension = Optional.ofNullable(name)
    //             .filter(f -> f.contains("."))
    //             .map(f -> f.substring(name.lastIndexOf(".") + 1));
    //     if (extension.isPresent()){
    //         return extension.get();
    //     }
    //     return null;
    // }

    // @Override
    // public void registerLog(Message message, List<Attachment> attachments){
    //     try {
    //         Log log =  new Log(Long.parseLong("0"), "Consumo GRAPH"
    //                 , "Descarga de facturas desde correo"
    //                 , LocalDateTime.now()
    //                 , "ADJUNTOS INCORRECTOS: No se pudo realizar descarga de facturas " +
    //                 "desde el correo, con asunto: '"+message.getSubject()+"' ");
    //         logRepository.saveLog(log);
    //     } catch (Exception e) {
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELERROR,
    //                 "Error al registrar log de descarga de facturas desde correo " +
    //                         "cuando los adjuntos no cumplen con estructura. ERROR: " + e.getMessage());
    //     }
    // }

    // public boolean countFilesZipped(Attachment attachment) throws IOException {
    //     boolean valid = true;
    //     File file = new File(RUTA + attachment.getName());
    //     file.createNewFile();
    //     OutputStream os = new FileOutputStream(file);
    //     os.write(attachment.getDecodedFile());
    //     os.flush();
    //     InputStream inputStream = new FileInputStream(file);
    //     try (ZipInputStream zis = new ZipInputStream(inputStream)) {
    //         ZipEntry entrada;
    //         while ((entrada = zis.getNextEntry()) != null) {
    //             if (!entrada.isDirectory()) {
    //                 if(!("xml".equals(getExtension(entrada.getName()))
    //                         || "pdf".equals(getExtension(entrada.getName()))
    //                         || "zip".equals(getExtension(entrada.getName()))
    //                         || "XML".equals(getExtension(entrada.getName()))
    //                         || "PDF".equals(getExtension(entrada.getName())))){
    //                     valid = false;
    //                 }
    //             }
    //         }
    //     } finally {
    //         inputStream.close();
    //         os.close();
    //         FileUtils.fileDelete(file.getName());
    //     }
    //     return valid;
    // }

    // public List<File> lookupSomethingInZip(InputStream fileInputStream) throws IOException {
    //     ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
    //     List<File> files = new ArrayList<>();
    //     ZipEntry zipEntry = new ZipEntry(".");
    //     try{
    //         zipEntry = zipInputStream.getNextEntry();
    //         if(!zipEntry.getName().endsWith(".zip")){
    //             while(zipEntry != null){
    //                 File outputFile = new File(RUTA + zipEntry.getName());
    //                 outputFile.createNewFile();
    //                 Files.copy(zipInputStream, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    //                 files.add(outputFile);
    //                 zipEntry = zipInputStream.getNextEntry();
    //                 FileUtils.fileDelete(outputFile.getName());
    //             }
    //         }else{
    //             ZipInputStream zis = new ZipInputStream(fileInputStream);
    //             ZipEntry subentry;
    //             while ((subentry = zis.getNextEntry()) != null)
    //             {
    //                 File outputFile = new File(RUTA + subentry.getName());
    //                 outputFile.createNewFile();
    //                 Files.copy(zis, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    //                 files.add(outputFile);
    //                 FileUtils.fileDelete(outputFile.getName());
    //             }
    //             zis.closeEntry();
    //         }
    //         zipInputStream.close();
    //     }catch (IOException e){
    //         logConfRepository.registrarInfo(Exception.class,LOGLEVELERROR,
    //                 "Ocurrió un problema al descomprirmir las facturas. " +
    //                         "ERROR: " + e.getMessage());
    //     }
    //     return files;
    // }

    // @Override
    // public List<File> unzipFiles(Message message,Attachment attachment) throws IOException {
    //     logConfRepository.registrarInfo(Exception.class,LOGLEVELINFO,
    //             "Se inicia proceso de descompresion de archivos.");
    //     List<File> files;
    //     File file = new File(RUTA + attachment.getName());
    //     file.createNewFile();
    //     OutputStream os = new FileOutputStream(file);
    //     os.write(attachment.getDecodedFile());
    //     os.flush();
    //     InputStream inputStream = new FileInputStream(file);
    //     files = lookupSomethingInZip(inputStream);

    //     inputStream.close();
    //     os.close();
    //     FileUtils.fileDelete(file.getName());
    //     return files;
    // }

    // @Override
    // public List<File> renameFiles(List<File> files,int auto) throws IOException {
    //     String newName = getNewFileName().concat(String.format("%03d", auto)).concat(".");
    //     List<File> listFileDest = new ArrayList<>();
    //     for (File file : files){
    //         Path path = Paths.get(file.getPath());
    //         Path path2 = Paths.get(file.getParent() + FileSystems.getDefault().getSeparator() + newName.concat(getExtension(file.getName())));
    //         try {
    //             Files.move(path, path2);
    //             File fileDest = new File(file.getParent(),newName.concat(getExtension(file.getName())));
    //             listFileDest.add(fileDest);
    //         } catch (IOException e) {
    //             logConfRepository.registrarInfo(this.getClass(), LOGLEVELERROR,
    //                     "Falló el renombramiento de archivos. ERROR: " + e.getMessage());
    //         }
    //     }
    //     return listFileDest;
    // }

    // @Override
    // public boolean deleteFolder(String path){
    //     File archivo = new File(path);
    //     return deleteFile(archivo);
    // }

    // public boolean deleteFile(File file){
    //     if (file.exists()) {
    //         if (file.isFile())
    //             file.delete();
    //         else {
    //             File f[]=file.listFiles();
    //             for (int i = 0; i < f.length; i++) {
    //                 deleteFile(f[i]);
    //             }
    //             file.delete();
    //         }
    //         return true;
    //     }else
    //         return false;
    // }

    // @Override
    // public Invoice getInvoiceInfo(List<File> files, List<File> renamedFiles, Attachment attachment){
    //     List<Invoice> invoices = new ArrayList<>();
    //     int i = 0;
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Se inicia la recuperación de datos para ser insertados en la base de datos.");
    //     Invoice invoice = new Invoice();
    //     for (File file: files){
    //         if(getExtension(renamedFiles.get(i).getName()).compareTo("xml") == 0
    //         || getExtension(renamedFiles.get(i).getName()).compareTo("XML") == 0){
    //             invoice.setXmlName(renamedFiles.get(i).getName());
    //         }else{
    //             invoice.setPdfName(renamedFiles.get(i).getName());
    //         }
    //         i++;
    //         if(getExtension(file.getName()).compareTo("xml") == 0
    //         || getExtension(file.getName()).compareTo("XML") == 0){
    //             invoice.setEmailXmlName(file.getName());
    //         }else{
    //             invoice.setEmailPdfName(file.getName());
    //         }
    //         invoice.setCommerce("LEASING");
    //         invoice.setDownloadDate(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    //         invoice.setMailDate(attachment.getMailDate().truncatedTo(ChronoUnit.SECONDS));
    //         invoice.setDeclineTypeId(1);
    //     }
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Se finaliza la recuperación de datos para ser insertados " +
    //                     "en la base de datos satisfactoriamente.");
    //     return invoice;
    // }

    // @Override
    // public List<File> processFile(List<File> files) throws IOException {
    //     boolean processed = false;
    //     List<File> filesDest = new ArrayList<>();
    //     List<File> filesPrev = new ArrayList<>();
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Se inicia el proceso de renombre y " +
    //                     "cargue de archivos adjuntos a bucket de S3.");
    //     int auto = 0;
    //     while (!processed) {
    //         auto++;
    //         if (auto > MAXNUMBEROFCONSECUTIVE) {
    //             processed = false;
    //             break;
    //         }
    //         filesPrev = renameFiles(files,auto);
    //         for (File file : filesPrev) {
    //             if(fileRepository.findFileS3(file.getName())){
    //                 processed = false;
    //                 files = filesPrev;
    //                 logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //                         "El archivo tuvo que ser renombrado dado " +
    //                                 "que ya se encontraba en bucket de S3.");
    //                 break;
    //             }else{
    //                 processed = true;
    //                 filesDest.add(file);
    //             }
    //         }
    //     }
    //     if (processed){
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //                 "Se inicia el cargue de archivos adjuntos a bucket de S3.");
    //         fileRepository.uploadFile(filesDest);
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //                 "Se realiza el cargue de archivos adjuntos " +
    //                         "a bucket de S3 satisfactoriamente.");
    //     }
    //     return filesPrev;
    // }

    // @Override
    // public void saveInvoiceDBR(Invoice invoice, List<Attachment> attachments, Message message) throws Exception {
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Se inicia proceso de registro de información en base de datos.");
    //     invoiceRepository.save(invoice);
    //     Invoice newInvoice = new Invoice();
    //     newInvoice = invoiceRepository.findByXmlName(invoice.getXmlName());
    //     ArrayList<String> names = new ArrayList<>();
    //     if (newInvoice.getXmlName() != null) {
    //         names.add(invoice.getEmailXmlName());
    //         names.add(invoice.getEmailPdfName());
    //         try {
    //             Log log =  new Log(newInvoice.getInvoiceId(), "Consumo GRAPH"
    //                     , "Descarga de facturas desde correo"
    //                     , LocalDateTime.now()
    //                     , "Se realiza descarga de facturas desde el correo, con asunto: '"+message.getSubject()+"' " +
    //                     "y los nombres originales de los archivos son: '"+names.get(0)+"' y '"+names.get(1)+"'");
    //             logRepository.saveLog(log);
    //         } catch (Exception e) {
    //             invoiceRepository.deleteInvoice(newInvoice);
    //             throw new Exception("Falló el registro de información en la base de datos. ERROR: " + e.getMessage());
    //         }
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //                 "Se ha obtenido el ID de la factura en la base de datos satisfactoriamente.");
    //     } else {
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELERROR,
    //                 "Falló la busqueda del ID de la factura en la base de datos.");
    //     }
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Se finaliza proceso de registro de información " +
    //                     "en base de datos satisfactoriamente.");
    // }

    // public void processItems(Message message, boolean processed, List<Attachment> attachments){
    //     boolean charged;
    //     Invoice invoice;
    //     if (processed){
    //         charged = fileRepository.uploadByte(attachments);
    //         try {
    //             invoice = getInvoiceInfoByte(attachments);
    //             saveInvoiceDBR(invoice, attachments, message);
    //             moveMessageToEmailFolder(message, PROCESEEDFOLDERID);
    //         } catch (Exception e){
    //             if (charged){
    //                 logConfRepository.registrarInfo(this.getClass(), LOGLEVELERROR,
    //                         "El proceso de almacenamiento en base de datos falló, " +
    //                                 "se eliminan objetos de bucket de S3.");
    //                 deleteFromBucket(attachments);
    //             }
    //         }
    //     }

    // }

    // @Override
    // public void processByte(Message message,List<Attachment> attachments){
    //     boolean processed = false;
    //     int auto = 0;
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Se inicia procesamiento de adjuntos.");
    //     for (Attachment attachment : attachments) {
    //         if(attachment.getExtension().compareTo("xml") == 0
    //                 || attachment.getExtension().compareTo("XML") == 0){
    //             attachment.setEmailXmlName(attachment.getName());
    //         }else{
    //             attachment.setEmailPdfName(attachment.getName());
    //         }
    //     }
    //     while (!processed) {
    //         auto++;
    //         if (auto > MAXNUMBEROFCONSECUTIVE) {
    //             processed = false;
    //             break;
    //         }
    //         String newName = getNewFileName().concat(String.format("%03d", auto));
    //         for (Attachment attachment : attachments) {
    //             attachment.setName(newName.concat(".").concat(attachment.getExtension()));
    //             if(fileRepository.findFileS3(attachment.getName())){
    //                 processed = false;
    //                 break;
    //             }else{
    //                 processed = true;
    //             }
    //         }
    //     }
    //     processItems(message,processed,attachments);
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Se finaliza procesamiento de adjuntos satisfactoriamente.");}

    // @Override
    // public Invoice getInvoiceInfoByte(List<Attachment> attachments){
    //     Invoice invoice = new Invoice();
    //     for (Attachment attachment: attachments){
    //         if(getExtension(attachment.getName()).compareTo("xml") == 0
    //         || getExtension(attachment.getName()).compareTo("XML") == 0){
    //             invoice.setXmlName(attachment.getName());
    //             invoice.setEmailXmlName(attachment.getEmailXmlName());
    //         }else{
    //             invoice.setPdfName(attachment.getName());
    //             invoice.setEmailPdfName(attachment.getEmailPdfName());
    //         }
    //         invoice.setCommerce("LEASING");
    //         invoice.setDownloadDate(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    //         invoice.setMailDate(attachment.getMailDate().truncatedTo(ChronoUnit.SECONDS));
    //         invoice.setDeclineTypeId(1);
    //     }
    //     return invoice;
    // }

    // @Override
    // public boolean validateWrongExtension(List<Attachment> attachments){
    //     boolean toProcess = false;
    //     List<String> extensions = new ArrayList<>();
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Se solicita la validación de las extensiones de los adjuntos.");
    //     for (Attachment attachment : attachments){
    //         extensions.add(getExtension(attachment.getName()));
    //     }
    //     List<String> uniqueExtensions = extensions.stream().distinct().collect(Collectors.toList());
    //     if (uniqueExtensions.contains("pdf")
    //             || uniqueExtensions.contains("xml")
    //             || uniqueExtensions.contains("zip")){
    //         toProcess = true;
    //     }
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Se realiza la validación de las extensiones " +
    //                     "de los adjuntos satisfactoriamente.");
    //     return toProcess;
    // }

    // @Override
    // public void deleteFilesFolder(List<File> filesToDelete) throws IOException {
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Se solicita la eliminación de los archivos de la carpeta.");
    //     for (File file : filesToDelete){
    //         Files.delete(file.toPath());
    //     }
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Se realiza la eliminación de los archivos de la carpeta satisfactoriamente.");

    // }

    // @Override
    // public void deleteObjectBucket(List<File> filesToDelete){
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Dado que la inserción de base de datos falló, " +
    //                     "se eliminan archivos del bucket de S3.");
    //     fileRepository.deleteFiles(filesToDelete);
    // }

    // @Override
    // public void deleteFromBucket(List<Attachment> attachments){
    //     logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //             "Dado que la inserción de base de datos falló, " +
    //                     "se eliminan archivos del bucket de S3.");
    //     fileRepository.deleteBytes(attachments);
    // }

    // @Override
    // public void createDirectory(){
    //     try {
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //                 "Se inicia la creación de carpeta tmp.");

    //         File theDir = new File(FileSystems.getDefault().getSeparator() + "tmp");
    //         if (!theDir.exists()){
    //             theDir.mkdir();
    //         }
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELINFO,
    //                 "Creación de carpetas logs y tmp satisfactoria.");
    //     } catch (Exception e) {
    //         logConfRepository.registrarInfo(this.getClass(), LOGLEVELERROR,
    //                 "Falló la creación de carpetas tmp/logs. " +
    //                         "Favor valide si cuenta con los permisos suficientes para esta acción. " +
    //                         "ERROR: " + e.getMessage());
    //     }

    // }

    // @Override
    // public boolean hasFileBucket(String fileName){
    //     return fileRepository.findFileS3(fileName);
    // }

}
