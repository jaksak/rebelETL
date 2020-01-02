package pl.longhorn.rebETL.model.comment;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment {
    @Id
    private Integer id;
    private String nick;
    private LocalDate date;
    private Integer productRating;
    private Integer commentRating;
    @Lob
    private String text;
}
