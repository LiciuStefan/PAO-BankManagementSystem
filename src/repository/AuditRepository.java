package repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static constants.Constants.FILENAME_AUDIT;

public class AuditRepository {

    private String filename;

    private AuditRepository(String filename){
        this.filename = filename;
    }

    public static AuditRepository getInstance(){
        return new AuditRepository(FILENAME_AUDIT);
    }
    public String getFilename(){
        return filename;
    }

    public void writeAudit(String action){
        try (var writer = Files.newBufferedWriter(Paths.get(this.getFilename()), java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND)) {
            writer.write(action);
            writer.newLine();
            //entities.add(entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
