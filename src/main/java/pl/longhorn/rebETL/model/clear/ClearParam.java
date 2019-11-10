package pl.longhorn.rebETL.model.clear;

import lombok.AllArgsConstructor;
import pl.longhorn.rebETL.model.processing.ProcessParam;
import pl.longhorn.rebETL.model.processing.TaskType;

@AllArgsConstructor
public class ClearParam implements ProcessParam {
    @Override
    public TaskType getType() {
        return TaskType.CLEAR;
    }
}
