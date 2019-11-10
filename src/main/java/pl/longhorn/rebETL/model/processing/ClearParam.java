package pl.longhorn.rebETL.model.processing;

import lombok.AllArgsConstructor;
import pl.longhorn.rebETL.service.ClearingService;

@AllArgsConstructor
public class ClearParam implements ProcessParam {
    @Override
    public TaskType getType() {
        return TaskType.CLEAR;
    }

    @Override
    public Class getServedBy() {
        return ClearingService.class;
    }
}
