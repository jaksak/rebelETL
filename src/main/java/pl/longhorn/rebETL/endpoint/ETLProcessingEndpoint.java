package pl.longhorn.rebETL.endpoint;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import pl.longhorn.rebETL.model.export.ExportView;
import pl.longhorn.rebETL.model.load.LoadView;
import pl.longhorn.rebETL.model.processing.ClearParam;
import pl.longhorn.rebETL.model.processing.ExportParam;
import pl.longhorn.rebETL.model.processing.LoadParam;
import pl.longhorn.rebETL.model.processing.TransformParam;
import pl.longhorn.rebETL.model.transform.TransformView;
import pl.longhorn.rebETL.service.CsvService;
import pl.longhorn.rebETL.service.DownloadDataService;
import pl.longhorn.rebETL.service.SequenceEtlProcessingService;

import javax.servlet.http.HttpServletResponse;

@RestController
@AllArgsConstructor
public class ETLProcessingEndpoint {

    private final SequenceEtlProcessingService etlProcessingService;
    private final CsvService csvService;
    private final DownloadDataService downloadDataService;

    @PostMapping("export")
    public ExportView export(@RequestBody String url) {
        ExportParam param = new ExportParam(url);
        long affectedRow = etlProcessingService.process(param);
        return new ExportView(affectedRow);
    }

    @PostMapping("transform")
    public TransformView transform() {
        long affectedRow = etlProcessingService.process(new TransformParam());
        return new TransformView(affectedRow);
    }

    @PostMapping("load")
    public LoadView load() {
        long affectedRow = etlProcessingService.process(new LoadParam());
        return new LoadView(affectedRow);
    }

    @PostMapping("clear")
    public long clear() {
        return etlProcessingService.process(new ClearParam());
    }

    @GetMapping("csv")
    public void downloadCsv(HttpServletResponse response) {
        csvService.downloadData(response);
    }

    @GetMapping("/{id}")
    @SneakyThrows
    public void download(@PathVariable("id") int id, HttpServletResponse response) {
        downloadDataService.download(id, response);
    }
}
