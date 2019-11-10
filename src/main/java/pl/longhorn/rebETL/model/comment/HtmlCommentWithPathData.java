package pl.longhorn.rebETL.model.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;

@Getter
@AllArgsConstructor(staticName = "of")
public class HtmlCommentWithPathData {
    private final HtmlComment htmlComment;
    private final Path path;
}
