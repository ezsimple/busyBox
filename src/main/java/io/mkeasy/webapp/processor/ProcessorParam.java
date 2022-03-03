package io.mkeasy.webapp.processor;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections.map.CaseInsensitiveMap;

@Data
@Builder
public class ProcessorParam {
    private String nameSpace;
    private String nameSpaceId;
    private CaseInsensitiveMap params;
}
