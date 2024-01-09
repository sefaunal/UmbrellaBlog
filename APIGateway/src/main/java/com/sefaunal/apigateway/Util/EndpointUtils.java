package com.sefaunal.apigateway.Util;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author github.com/sefaunal
 * @since 2024-01-11
 */
public class EndpointUtils {
    public static boolean isSecureEndpoint(String requestURI) {
        // Load the YAML file from the resources folder
        InputStream inputStream = EndpointUtils.class.getResourceAsStream("/endpoints/secure.yaml");
        Yaml yaml = new Yaml();
        Map<String, List<Map<String, Object>>> endpointConfig = yaml.load(inputStream);

        List<Map<String, Object>> securedEndpoints = endpointConfig.get("SECURE");
        for (Map<String, Object> endpoint : securedEndpoints) {
            String uri = (String) endpoint.get("URI");
            if (requestURI.equals(uri)) {
                return true;
            }
        }
        return false;
    }
}
