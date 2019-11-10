package pl.longhorn.rebETL.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import pl.longhorn.rebETL.model.comment.Comment;
import pl.longhorn.rebETL.repository.CommentRepository;

import javax.servlet.http.HttpServletResponse;

@Component
@AllArgsConstructor
public class CsvService {

    private final CommentRepository commentRepository;

    @SneakyThrows
    public void downloadData(HttpServletResponse response) {
        String filename = "comments.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<Comment> writer = new StatefulBeanToCsvBuilder<Comment>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        //write all users to csv file
        commentRepository.findAll()
                .forEach(comment -> {
                    try {
                        writer.write(comment);
                    } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
                        e.printStackTrace();
                    }
                });
    }
}
