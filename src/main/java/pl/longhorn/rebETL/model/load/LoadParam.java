package pl.longhorn.rebETL.model.load;

import pl.longhorn.rebETL.model.processing.ProcessParam;
import pl.longhorn.rebETL.model.processing.TaskType;
import pl.longhorn.rebETL.service.LoadService;

public class LoadParam implements ProcessParam<LoadService> {
    @Override
    public TaskType getType() {
        return TaskType.LOAD;
    }
}
