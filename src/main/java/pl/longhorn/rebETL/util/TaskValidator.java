package pl.longhorn.rebETL.util;

import pl.longhorn.rebETL.model.processing.TaskType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class TaskValidator {
    private final static ConcurrentHashMap<TaskType, Predicate<TaskType>> requirementsToStartProcess = new ConcurrentHashMap<>();

    static {
        requirementsToStartProcess.put(TaskType.EXPORT, status -> !TaskType.EXPORT.equals(status) && !TaskType.TRANSFORM.equals(status));
        requirementsToStartProcess.put(TaskType.TRANSFORM, TaskType.EXPORT::equals);
        requirementsToStartProcess.put(TaskType.LOAD, TaskType.TRANSFORM::equals);
    }

    public static boolean isPossibleToDoWhenStatus(TaskType newTask, TaskType lastTask) {
        return requirementsToStartProcess.getOrDefault(newTask, status -> true)
                .test(lastTask);
    }
}
