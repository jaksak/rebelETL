package pl.longhorn.rebETL.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import pl.longhorn.rebETL.repository.CommentRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class CsvService {

    private final CommentRepository commentRepository;

    private final String DEFAULT_FILENAME = "comments.csv";

    @SneakyThrows
    public void downloadData(HttpServletResponse response) {
        setHeader(response);
        CsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.EXCEL_PREFERENCE);
        commentRepository.findAll().forEach(comment -> {
            try {
                csvBeanWriter.write(comment, "id", "nick", "date", "productRating", "commentRating", "text");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setHeader(HttpServletResponse response) {
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + DEFAULT_FILENAME + "\"");
        response.setHeader("charset", "UTF-8");
        response.setCharacterEncoding("UTF-8");
    }
}
