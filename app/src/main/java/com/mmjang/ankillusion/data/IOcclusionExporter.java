package com.mmjang.ankillusion.data;

import java.util.List;

public interface IOcclusionExporter {
    OperationResult export(List<OcclusionObject> occlusionObject);
}
