package pl.longhorn.rebETL.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.longhorn.rebETL.model.CompleteView;
import pl.longhorn.rebETL.model.clear.ClearParam;
import pl.longhorn.rebETL.model.comment.Comment;
import pl.longhorn.rebETL.model.export.ExportParam;
import pl.longhorn.rebETL.model.export.ExportView;
import pl.longhorn.rebETL.model.load.LoadParam;
import pl.longhorn.rebETL.model.load.LoadView;
import pl.longhorn.rebETL.model.transform.TransformParam;
import pl.longhorn.rebETL.model.transform.TransformView;
import pl.longhorn.rebETL.repository.CommentRepository;
import pl.longhorn.rebETL.service.CsvService;
import pl.longhorn.rebETL.service.DownloadDataService;
import pl.longhorn.rebETL.service.SequenceEtlProcessingService;

import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
public class RebEtlEndpoint {

    private final SequenceEtlProcessingService sequenceEtlProcessingService;
    private final CsvService csvService;
    private final DownloadDataService downloadDataService;
    private final CommentRepository commentRepository;

    @PostMapping("export")
    public ExportView export(@RequestBody String url) {
        ExportParam param = new ExportParam(url);
        long affectedRow = sequenceEtlProcessingService.process(param);
        return new ExportView(affectedRow);
    }

    @PostMapping("transform")
    public TransformView transform() {
        long affectedRow = sequenceEtlProcessingService.process(new TransformParam());
        return new TransformView(affectedRow);
    }

    @PostMapping("load")
    public LoadView load() {
        long affectedRow = sequenceEtlProcessingService.process(new LoadParam());
        return new LoadView(affectedRow);
    }

    @PostMapping
    public CompleteView exportTransformAndLoad(@RequestBody String url) {
        return CompleteView.builder()
                .exportView(export(url))
                .transformView(transform())
                .loadView(load())
                .build();
    }

    @PostMapping("clear")
    public long clear() {
        return sequenceEtlProcessingService.process(new ClearParam());
    }

    @GetMapping("csv")
    public void downloadCsv(HttpServletResponse response) {
        csvService.downloadData(response);
    }

    @GetMapping("{id}/")
    public void download(@PathVariable("id") int id, HttpServletResponse response) {
        downloadDataService.download(id, response);
    }

    @GetMapping
    public Iterable<Comment> getAll() {
        return commentRepository.findAll();
    }
}
