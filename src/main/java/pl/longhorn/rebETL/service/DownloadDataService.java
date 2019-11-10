package pl.longhorn.rebETL.service;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;
import pl.longhorn.rebETL.model.comment.Comment;
import pl.longhorn.rebETL.model.exception.CommentNotFoundException;
import pl.longhorn.rebETL.repository.CommentRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@AllArgsConstructor
public class DownloadDataService {

    private final CommentRepository commentRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());

    @SneakyThrows
    public void download(int id, HttpServletResponse response) {
        val comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            val data = getData(comment.get());
            InputStream inputStream = new ByteArrayInputStream(data);
            setHeader(response);
            IOUtils.copy(inputStream, response.getOutputStream());
            closeStream(response, inputStream);
        } else {
            throw new CommentNotFoundException();
        }
    }

    private void closeStream(HttpServletResponse response, InputStream inputStream) throws IOException {
        response.flushBuffer();
        inputStream.close();
    }

    private void setHeader(HttpServletResponse response) {
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment; filename=comment.txt");
        response.setHeader("charset", "UTF-8");
    }

    @SneakyThrows
    private byte[] getData(Comment comment) {
        String json = objectWriter.writeValueAsString(comment);
        return json.getBytes();
    }
}
