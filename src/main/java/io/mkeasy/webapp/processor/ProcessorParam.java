package io.mkeasy.webapp.processor;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.map.CaseInsensitiveMap;

@Data
@NoArgsConstructor
public class ProcessorParam {
    private String nameSpace;
    private String nameSpaceId;
    private CaseInsensitiveMap params;
}
