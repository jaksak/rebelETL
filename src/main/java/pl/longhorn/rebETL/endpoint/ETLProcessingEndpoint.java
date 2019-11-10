package pl.longhorn.rebETL.endpoint;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.longhorn.rebETL.model.export.ExportView;
import pl.longhorn.rebETL.model.load.LoadView;
import pl.longhorn.rebETL.model.processing.ExportParam;
import pl.longhorn.rebETL.model.processing.LoadParam;
import pl.longhorn.rebETL.model.processing.TransformParam;
import pl.longhorn.rebETL.model.transform.TransformView;
import pl.longhorn.rebETL.service.SequenceEtlProcessingService;


@RestController
@AllArgsConstructor
public class ETLProcessingEndpoint {

    private final SequenceEtlProcessingService etlProcessingService;

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
}
