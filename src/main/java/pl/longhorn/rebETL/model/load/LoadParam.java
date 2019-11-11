package pl.longhorn.rebETL.model.load;

import pl.longhorn.rebETL.model.processing.ProcessParam;
import pl.longhorn.rebETL.model.processing.TaskType;

public class LoadParam implements ProcessParam {
    @Override
    public TaskType getType() {
        return TaskType.LOAD;
    }

    @Override
    public boolean canStartWhen(TaskType lastTask) {
        return TaskType.TRANSFORM.equals(lastTask);
    }
}
