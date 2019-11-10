package pl.longhorn.rebETL.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.longhorn.rebETL.repository.CommentRepository;

@Component
@RequiredArgsConstructor
public class LoadService {

    private final FileSystemService fileSystemService;
    private final CommentRepository commentRepository;

    public long load() {
        long rowBeforeOperation = commentRepository.count();
        var commentWithPathDataOptional = fileSystemService.getAnyComment();
        for (; commentWithPathDataOptional.isPresent(); commentWithPathDataOptional = fileSystemService.getAnyComment()) {
            var commentWithPathData = commentWithPathDataOptional.get();
            commentRepository.save(commentWithPathData.getComment());
            fileSystemService.remove(commentWithPathData.getPath());
        }
        long rowAfterOperation = commentRepository.count();
        return rowAfterOperation - rowBeforeOperation;
    }
}
