package pl.longhorn.rebETL.model.comment;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HtmlComment implements Serializable {
    private String id;
    private String nick;
    private String date;
    private String productRating;
    private String commentRating;
    private String text;

}
