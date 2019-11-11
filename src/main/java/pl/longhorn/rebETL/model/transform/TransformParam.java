package pl.longhorn.rebETL.model.transform;

import pl.longhorn.rebETL.model.processing.ProcessParam;
import pl.longhorn.rebETL.model.processing.TaskType;

public class TransformParam implements ProcessParam {
    @Override
    public TaskType getType() {
        return TaskType.TRANSFORM;
    }

    @Override
    public boolean canStartWhen(TaskType lastTask) {
        return TaskType.EXPORT.equals(lastTask);
    }
}
