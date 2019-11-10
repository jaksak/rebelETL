package pl.longhorn.rebETL.service;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;
import pl.longhorn.rebETL.model.exception.CommentNotFoundException;
import pl.longhorn.rebETL.repository.CommentRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@AllArgsConstructor
public class DownloadDataService {

    private final CommentRepository commentRepository;


    @SneakyThrows
    public void download(int id, HttpServletResponse response) {
        val comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
            String json = objectWriter.writeValueAsString(comment.get());
            val data = json.getBytes();
            InputStream inputStream = new ByteArrayInputStream(data);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename=comment.txt");
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } else {
            throw new CommentNotFoundException();
        }
    }
}
