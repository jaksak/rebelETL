package pl.longhorn.rebETL.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import pl.longhorn.rebETL.repository.CommentRepository;
import pl.longhorn.rebETL.service.FileSystemService;

@Component
@RequiredArgsConstructor
public class NonFinishedDataDestroyer implements DisposableBean {

    private final FileSystemService fileSystemService;
    private final CommentRepository commentRepository;

    @Override
    public void destroy() {
        fileSystemService.clear();
        commentRepository.deleteAll();
    }
}
