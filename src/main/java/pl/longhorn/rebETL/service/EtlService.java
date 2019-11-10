package pl.longhorn.rebETL.service;

import pl.longhorn.rebETL.model.processing.ProcessParam;

public interface EtlService<USE extends ProcessParam> {
    long process(USE param);
}
