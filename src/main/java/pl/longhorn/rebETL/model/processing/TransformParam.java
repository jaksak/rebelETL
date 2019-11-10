package pl.longhorn.rebETL.model.processing;

import pl.longhorn.rebETL.service.TransformService;

public class TransformParam implements ProcessParam<TransformService> {
    @Override
    public TaskType getType() {
        return TaskType.TRANSFORM;
    }

    @Override
    public Class<TransformService> getServedBy() {
        return TransformService.class;
    }
}
