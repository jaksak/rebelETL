package pl.longhorn.rebETL.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import pl.longhorn.rebETL.model.comment.HtmlComment;
import pl.longhorn.rebETL.model.exception.IllegalUrlException;
import pl.longhorn.rebETL.model.export.ExportView;
import pl.longhorn.rebETL.util.HtmlCommentsFactory;
import pl.longhorn.rebETL.util.UrlHelper;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final String COMMENT_CLASS_NAME = "user-comment";

    private final FileSystemService fileSystemService;

    public ExportView exportFromIndexPage(@NotEmpty String url) {
        String pathToAllComments = UrlHelper.getPathToAllComments(url);
        int affectedAmount = exportFromAllCommentsPage(pathToAllComments);
        return new ExportView(affectedAmount);
    }

    private int exportFromAllCommentsPage(String pathToAllComments) {
        val htmlComments = getHtmlComments(pathToAllComments);
        return saveTemporallyHtmlComments(htmlComments);
    }

    private int saveTemporallyHtmlComments(Elements htmlComments) {
        htmlComments.forEach(this::saveTemporallyHtmlComment);
        return htmlComments.size();
    }

    private void saveTemporallyHtmlComment(Element one) {
        HtmlComment comment = HtmlCommentsFactory.from(one);
        fileSystemService.save(comment);
    }

    private Elements getHtmlComments(String pathToAllComments) {
        try {
            return Jsoup.connect(pathToAllComments).get().body().getElementsByClass(COMMENT_CLASS_NAME);
        } catch (IOException e) {
            throw new IllegalUrlException();
        }
    }
}
