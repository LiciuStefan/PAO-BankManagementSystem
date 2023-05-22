package repository;

import config.DatabaseConfiguration;
import exception.CustomerAlreadyExistsException;
import exception.EntityAlreadyExistsException;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository<T extends WriteableToCSVFile> {
    private String filename;
    protected List<T> entities = new ArrayList<>();

    protected static DatabaseConfiguration databaseConfiguration = DatabaseConfiguration.getInstance();
    public BaseRepository(String filename) {
        this.filename = filename;
    }

    public BaseRepository(){

    }

    public String getFilename() {
        return filename;
    }

    abstract void loadDatabaseFromFile();

    public List<T> getEntities() {
        return entities;
    }

    abstract void saveEntity(String line);

    public void addEntity(T entity) {
        if (this.entities.contains(entity))
            throw new EntityAlreadyExistsException(entity.getClass().getSimpleName() + " already exists!");
        this.entities.add(entity);
    }
    public void addEntityToFile(T entity)
    {
        try {
            if (this.entities.contains(entity))
                throw new EntityAlreadyExistsException(entity.getClass().getSimpleName() + " already exists!");
            try (var writer = Files.newBufferedWriter(Paths.get(this.getFilename()), java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND)) {
                writer.write(entity.toCSV());
                writer.newLine();
                //entities.add(entity);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }catch(CustomerAlreadyExistsException e){
            throw new RuntimeException(e);
        }
    }

    public void saveChanges(){
        //Need to save every account to file:
        try (var writer = Files.newBufferedWriter(Paths.get(this.getFilename()), java.nio.charset.StandardCharsets.UTF_8)) {
            for(var object : this.entities){
                writer.write(object.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
