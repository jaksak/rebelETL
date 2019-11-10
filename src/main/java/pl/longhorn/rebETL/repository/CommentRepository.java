package pl.longhorn.rebETL.repository;

import org.springframework.data.repository.CrudRepository;
import pl.longhorn.rebETL.model.comment.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
}
