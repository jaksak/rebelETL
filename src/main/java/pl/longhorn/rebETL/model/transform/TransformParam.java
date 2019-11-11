package pl.longhorn.rebETL.model.transform;

import pl.longhorn.rebETL.model.processing.ProcessParam;
import pl.longhorn.rebETL.model.processing.TaskType;
import pl.longhorn.rebETL.service.TransformService;

public class TransformParam implements ProcessParam<TransformService> {
    @Override
    public TaskType getType() {
        return TaskType.TRANSFORM;
    }

    @Override
    public boolean canStartWhen(TaskType lastTask) {
        return TaskType.EXPORT.equals(lastTask);
    }
}
