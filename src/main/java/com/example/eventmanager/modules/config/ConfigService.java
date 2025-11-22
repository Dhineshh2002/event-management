package com.example.eventmanager.modules.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private final ConfigRepository configRepository;
    private Map<String, String> configMap = new HashMap<>();

    @PostConstruct
    void init() {
        configMap = configRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Config::getConfigKey, Config::getConfigValue));
    }

    public String get(String key) {
        return configMap.get(key);
    }

    public Integer getInteger(String key) {
        return Integer.parseInt(configMap.get(key));
    }

    public Double getDouble(String key) {
        return Double.parseDouble(configMap.get(key));
    }

    public BigDecimal getBigDecimal(String key) {
        return new BigDecimal(configMap.get(key));
    }
}
