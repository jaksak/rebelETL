package pl.longhorn.rebETL.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.longhorn.rebETL.model.export.ExportView;
import pl.longhorn.rebETL.model.load.LoadView;
import pl.longhorn.rebETL.service.ExportService;
import pl.longhorn.rebETL.service.LoadService;
import pl.longhorn.rebETL.service.TransformService;


@RestController
@AllArgsConstructor
public class ETLProcessingEndpoint {

    private final ExportService exportService;
    private final TransformService transformService;
    private final LoadService loadService;

    @PostMapping("export")
    public ExportView export(@RequestBody String url) {
        return exportService.exportFromIndexPage(url);
    }

    @PostMapping("transform")
    public void transport() {
        transformService.transformData();
    }

    @PostMapping("load")
    public LoadView load() {
        long affectedRow = loadService.load();
        return new LoadView(affectedRow);
    }
}
