package pl.longhorn.rebETL.service;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Component;
import pl.longhorn.rebETL.model.comment.Comment;
import pl.longhorn.rebETL.model.comment.CommentWithPathData;
import pl.longhorn.rebETL.model.comment.HtmlComment;
import pl.longhorn.rebETL.model.comment.HtmlCommentWithPathData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class FileSystemService {

    private final static String PATH_TO_RAW_DATA_FOLDER = "src/main/resources/rawData/";
    private final static String PATH_TO_TRANSFORMED_DATA_FOLDER = "src/main/resources/transformedData/";

    private final ObjectMapper objectMapper;

    void save(Comment comment) {
        save(comment, getUniqueFile(PATH_TO_TRANSFORMED_DATA_FOLDER));
    }

    void save(HtmlComment comment) {
        save(comment, getUniqueFile(PATH_TO_RAW_DATA_FOLDER));
    }

    @SneakyThrows
    private File getUniqueFile(String directory) {
        Path newFilePath = Paths.get(directory + getRandomName());
        val path = Files.createFile(newFilePath);
        return path.toFile();
    }

    private String getRandomName() {
        return UUID.randomUUID().toString();
    }

    @SneakyThrows
    private void save(Object toSave, File where) {
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
        objectWriter.writeValue(where, toSave);
    }

    Optional<HtmlCommentWithPathData> getAnyHtmlComment() {
        return getAnyPath(PATH_TO_RAW_DATA_FOLDER)
                .map(this::htmlCommentWithPathData);
    }

    @SneakyThrows
    private Optional<Path> getAnyPath(String directory) {
        return Files.walk(Paths.get(directory))
                .filter(this::isStandardDataFile)
                .findAny();
    }

    private boolean isStandardDataFile(Path path) {
        return Files.isRegularFile(path) && isNotKeepFile(path);
    }

    private HtmlCommentWithPathData htmlCommentWithPathData(Path path) {
        val htmlComment = mapToProperType(path, HtmlComment.class);
        return HtmlCommentWithPathData.of(htmlComment, path);
    }


    Optional<CommentWithPathData> getAnyComment() {
        return getAnyPath(PATH_TO_TRANSFORMED_DATA_FOLDER)
                .map(this::commentWithPathData);
    }

    private CommentWithPathData commentWithPathData(Path path) {
        Comment comment = mapToProperType(path, Comment.class);
        return CommentWithPathData.of(comment, path);
    }

    @SneakyThrows
    private <T> T mapToProperType(Path path, Class<T> typeKey) {
        return objectMapper.readValue(path.toFile(), typeKey);
    }

    private boolean isNotKeepFile(Path path) {
        return !path.getFileName().toString().equals(".keep");
    }

    void remove(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long clear() {
        long affectedRow = clear(PATH_TO_RAW_DATA_FOLDER);
        affectedRow += clear(PATH_TO_TRANSFORMED_DATA_FOLDER);
        return affectedRow;
    }

    @SneakyThrows
    private long clear(String path) {
        AtomicInteger affectedRow = new AtomicInteger();
        Files.walk(Paths.get(path))
                .filter(this::isStandardDataFile)
                .forEach(file -> {
                    remove(file);
                    affectedRow.getAndIncrement();
                });
        return affectedRow.get();
    }
}
