package pl.longhorn.rebETL.model.processing;

public interface ProcessParam {
    TaskType getType();

    boolean canStartWhen(TaskType lastTask);
}
