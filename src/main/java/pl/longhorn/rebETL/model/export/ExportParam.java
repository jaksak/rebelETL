package pl.longhorn.rebETL.model.export;

import lombok.Value;
import pl.longhorn.rebETL.model.processing.ProcessParam;
import pl.longhorn.rebETL.model.processing.TaskType;
import pl.longhorn.rebETL.service.ExportService;

@Value
public class ExportParam implements ProcessParam<ExportService> {
    private String url;

    @Override
    public TaskType getType() {
        return TaskType.EXPORT;
    }

    @Override
    public boolean canStartWhen(TaskType lastTask) {
        return !TaskType.EXPORT.equals(lastTask) && !TaskType.TRANSFORM.equals(lastTask);
    }
}
