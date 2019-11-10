package pl.longhorn.rebETL.model.processing;

import pl.longhorn.rebETL.service.LoadService;

public class LoadParam implements ProcessParam<LoadService> {
    @Override
    public TaskType getType() {
        return TaskType.LOAD;
    }

    @Override
    public Class<LoadService> getServedBy() {
        return LoadService.class;
    }
}
