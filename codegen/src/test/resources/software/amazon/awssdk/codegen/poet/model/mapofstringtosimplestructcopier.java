package software.amazon.awssdk.services.jsonprotocoltests.model;

import static java.util.stream.Collectors.toMap;

import java.util.Collections;
import java.util.Map;
import software.amazon.awssdk.annotations.Generated;
import software.amazon.awssdk.core.util.DefaultSdkAutoConstructMap;
import software.amazon.awssdk.core.util.SdkAutoConstructMap;

@Generated("software.amazon.awssdk:codegen")
final class MapOfStringToSimpleStructCopier {
    static Map<String, SimpleStruct> copy(Map<String, SimpleStruct> mapOfStringToSimpleStructParam) {
        if (mapOfStringToSimpleStructParam == null || mapOfStringToSimpleStructParam instanceof SdkAutoConstructMap) {
            return DefaultSdkAutoConstructMap.getInstance();
        }
        Map<String, SimpleStruct> mapOfStringToSimpleStructParamCopy = mapOfStringToSimpleStructParam.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Collections.unmodifiableMap(mapOfStringToSimpleStructParamCopy);
    }

    static Map<String, SimpleStruct> copyFromBuilder(Map<String, ? extends SimpleStruct.Builder> mapOfStringToSimpleStructParam) {
        if (mapOfStringToSimpleStructParam == null) {
            return null;
        }
        return copy(mapOfStringToSimpleStructParam.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> e.getValue().build())));
    }
}
