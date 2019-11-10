package pl.longhorn.rebETL.service;

import lombok.val;
import org.springframework.stereotype.Component;
import pl.longhorn.rebETL.model.exception.IllegalTaskException;
import pl.longhorn.rebETL.model.processing.ProcessParam;
import pl.longhorn.rebETL.model.processing.TaskType;
import pl.longhorn.rebETL.util.TaskValidator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


@Component
public class SequenceEtlProcessingService {

    private final ReentrantLock lock = new ReentrantLock();
    private final Map<Class, EtlService> etlServices;
    private TaskType lastFinishedTask = null;

    public SequenceEtlProcessingService(List<EtlService> etlServices) {
        this.etlServices = etlServices.stream().collect(Collectors.toMap(EtlService::getClass, service -> service));
    }

    public long process(ProcessParam param) {
        blockOtherRequest();
        try {
            checkPossibilityToStart(param.getType());
            val choosedService = etlServices.get(param.getServedBy());
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
