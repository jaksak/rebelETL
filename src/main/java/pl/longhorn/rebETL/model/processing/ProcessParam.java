package pl.longhorn.rebETL.model.processing;

import pl.longhorn.rebETL.service.EtlService;

public interface ProcessParam<SERVED_BY extends EtlService> {
    TaskType getType();

    boolean canStartWhen(TaskType lastTask);
}
