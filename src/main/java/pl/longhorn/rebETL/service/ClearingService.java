package pl.longhorn.rebETL.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.longhorn.rebETL.model.processing.ClearParam;
import pl.longhorn.rebETL.repository.CommentRepository;

@Component
@RequiredArgsConstructor
public class ClearingService implements EtlService<ClearParam> {

    private final CommentRepository commentRepository;
    private final FileSystemService fileSystemService;

    @Override
    public long process(ClearParam param) {
        long affectedRow = commentRepository.count();
        commentRepository.deleteAll();
        affectedRow += fileSystemService.clear();
        return affectedRow;
    }
}
