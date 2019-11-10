package pl.longhorn.rebETL.service;

import lombok.val;
import org.springframework.stereotype.Component;
import pl.longhorn.rebETL.model.exception.IllegalTaskException;
import pl.longhorn.rebETL.model.processing.ProcessParam;
import pl.longhorn.rebETL.model.processing.TaskType;
import pl.longhorn.rebETL.util.TaskValidator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;


@Component
public class SequenceEtlProcessingService {

    private final ReentrantLock lock = new ReentrantLock();
    private final Map<Class, EtlService> etlServices = new ConcurrentHashMap<>();
    private TaskType lastFinishedTask = null;

    public SequenceEtlProcessingService(List<EtlService> etlServices) {
        etlServices.forEach(this::addElServiceToMap);
    }

    private void addElServiceToMap(EtlService etlService) {
        for (Type genericInterface : etlService.getClass().getGenericInterfaces()) {
            Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
            for (Type genericType : genericTypes) {
                etlServices.put((Class) genericType, etlService);
            }
        }
    }

    public long process(ProcessParam param) {
        blockOtherRequest();
        try {
            checkPossibilityToStart(param.getType());
            val choosedService = etlServices.get(param.getClass());
            long result = choosedService.process(param);
            lastFinishedTask = param.getType();
            return result;
        } finally {
            lock.unlock();
        }
    }

    private void blockOtherRequest() {
        lock.lock();
    }

    private void checkPossibilityToStart(TaskType task) {
        if (!TaskValidator.isPossibleToDoWhenStatus(task, lastFinishedTask)) {
            throw new IllegalTaskException();
        }
    }
}
