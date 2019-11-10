package pl.longhorn.rebETL.model;

import lombok.*;
import pl.longhorn.rebETL.model.export.ExportView;
import pl.longhorn.rebETL.model.load.LoadView;
import pl.longhorn.rebETL.model.transform.TransformView;

import java.io.Serializable;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompleteView implements Serializable {
    private ExportView exportView;
    private TransformView transformView;
    private LoadView loadView;
}
