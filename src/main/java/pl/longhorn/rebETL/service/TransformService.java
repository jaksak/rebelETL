package pl.longhorn.rebETL.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Component;
import pl.longhorn.rebETL.model.comment.Comment;
import pl.longhorn.rebETL.model.comment.HtmlComment;
import pl.longhorn.rebETL.model.transform.TransformParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class TransformService implements EtlService<TransformParam> {

    private final Pattern DATE_RUBBISH_REMOVER_PATTERN = Pattern.compile("\\d{2}.\\d{2}.\\d{4}");
    private final Pattern PRODUCT_RATING_PATTERN = Pattern.compile("\\d");
    private final DateTimeFormatter DATE_STYLE_PATTERN = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final FileSystemService fileSystemService;

    @Override
    public long process(TransformParam param) {
        var htmlCommentWithPathDataOptional = fileSystemService.getAnyHtmlComment();
        int affectedRow = 0;
        for (; htmlCommentWithPathDataOptional.isPresent(); htmlCommentWithPathDataOptional = fileSystemService.getAnyHtmlComment()) {
            var htmlCommentWithPathData = htmlCommentWithPathDataOptional.get();
            var comment = fromHtmlComment(htmlCommentWithPathData.getHtmlComment());
            fileSystemService.save(comment);
            fileSystemService.remove(htmlCommentWithPathData.getPath());
            affectedRow++;
        }
        return affectedRow;
    }

    private Comment fromHtmlComment(HtmlComment htmlComment) {
        return Comment.builder()
                .id(getId(htmlComment))
                .nick(htmlComment.getNick())
                .date(getDate(htmlComment.getDate()))
                .productRating(getProductRating(htmlComment.getProductRating()))
                .commentRating(getCommentRating(htmlComment.getCommentRating()))
                .text(htmlComment.getText())
                .build();
    }

    private Integer getCommentRating(String commentRating) {
        return Integer.parseInt(commentRating);
    }

    private Integer getProductRating(String productRating) {
        if (productRating != null) {
            val matcher = PRODUCT_RATING_PATTERN.matcher(productRating);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            }
        }
        return null;
    }

    private LocalDate getDate(String dateWithRubbish) {
        val textDate = findTextDate(dateWithRubbish);
        return textDate.map(s -> LocalDate.parse(s, DATE_STYLE_PATTERN)).orElse(null);
    }

    private Optional<String> findTextDate(String dateWithRubbish) {
        val matcher = DATE_RUBBISH_REMOVER_PATTERN.matcher(dateWithRubbish);
        if (matcher.find()) {
            return Optional.of(matcher.group());
        }
        return Optional.empty();
    }

    private int getId(HtmlComment htmlComment) {
        return Integer.parseInt(htmlComment.getId());
    }
}
