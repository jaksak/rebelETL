package pl.longhorn.rebETL.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import pl.longhorn.rebETL.model.comment.HtmlComment;
import pl.longhorn.rebETL.model.exception.IllegalUrlException;
import pl.longhorn.rebETL.model.export.ExportParam;
import pl.longhorn.rebETL.util.HtmlCommentsFactory;
import pl.longhorn.rebETL.util.UrlHelper;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ExportService implements EtlService<ExportParam> {

    private final String COMMENT_CLASS_NAME = "user-comment";

    private final FileSystemService fileSystemService;

    @Override
    public long process(ExportParam param) {
        String pathToAllComments = UrlHelper.getPathToAllComments(param.getUrl());
        return exportFromAllCommentsPage(pathToAllComments, param.getUrl());
    }

    private int exportFromAllCommentsPage(String pathToAllComments, String url) {
        val htmlComments = getHtmlComments(pathToAllComments);
        return saveTemporallyHtmlComments(htmlComments, url);
    }

    private int saveTemporallyHtmlComments(Elements htmlComments, String url) {
        htmlComments.forEach(htmlComment -> saveTemporallyHtmlComment(htmlComment, url));
        return htmlComments.size();
    }

    private void saveTemporallyHtmlComment(Element one, String url) {
        HtmlComment comment = HtmlCommentsFactory.from(one, url);
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
