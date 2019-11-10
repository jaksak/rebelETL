package pl.longhorn.rebETL.model.processing;

import lombok.Value;
import pl.longhorn.rebETL.service.ExportService;

@Value
public class ExportParam implements ProcessParam<ExportService> {
    private String url;

    @Override
    public TaskType getType() {
        return TaskType.EXPORT;
    }

    @Override
    public Class<ExportService> getServedBy() {
        return ExportService.class;
    }
}
