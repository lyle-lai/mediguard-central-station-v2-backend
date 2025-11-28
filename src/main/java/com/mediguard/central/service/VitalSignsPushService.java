package com.mediguard.central.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediguard.central.dto.VitalSignsPushDTO;
import com.mediguard.central.entity.PatientEntity;
import com.mediguard.central.mapper.PatientMapper;
import com.mediguard.central.websocket.VitalSignsWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 生命体征数据推送服务
 * 模拟设备数据推送
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VitalSignsPushService {

    private final VitalSignsWebSocketHandler webSocketHandler;
    private final PatientMapper patientMapper;
    private final ObjectMapper objectMapper;

    private final Random random = new Random();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /**
     * 每秒推送一次生命体征数据
     */
    @Scheduled(fixedRate = 1000)
    public void pushVitalSigns() {
        if (webSocketHandler.getConnectionCount() == 0) {
            return; // 没有连接时不推送
        }

        try {
            // 获取所有患者
            List<PatientEntity> patients = patientMapper.selectList(null);

            for (PatientEntity patient : patients) {
                VitalSignsPushDTO data = generateVitalSignsData(patient);
                String json = objectMapper.writeValueAsString(data);
                webSocketHandler.broadcast(json);
            }

            log.debug("【生命体征推送】推送数据，患者数={}", patients.size());
        } catch (Exception e) {
            log.error("【生命体征推送】推送失败", e);
        }
    }

    /**
     * 生成模拟的生命体征数据
     */
    private VitalSignsPushDTO generateVitalSignsData(PatientEntity patient) {
        VitalSignsPushDTO dto = new VitalSignsPushDTO();
        dto.setPatientId(patient.getId());
        dto.setBedNumber(patient.getBedNumber());
        dto.setTimestamp(LocalDateTime.now().format(formatter));

        // 生成参数数据
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("hr", generateHR(patient));
        parameters.put("spo2", generateSpO2(patient));
        parameters.put("nibp", generateNIBP(patient));
        parameters.put("rr", generateRR(patient));
        parameters.put("temp", generateTemp(patient));
        dto.setParameters(parameters);

        // 生成波形数据
        Map<String, List<Number>> waveforms = new HashMap<>();
        waveforms.put("ecg", generateECGWaveform());
        waveforms.put("spo2", generateSpO2Waveform());
        waveforms.put("resp", generateRespWaveform());
        dto.setWaveforms(waveforms);

        return dto;
    }

    /**
     * 生成心率数据（根据患者状态调整）
     */
    private int generateHR(PatientEntity patient) {
        int baseHR = 75;
        int variance = 10;

        switch (patient.getStatus()) {
            case CRITICAL:
                baseHR = 130; // 危急状态心率偏高
                variance = 15;
                break;
            case WARNING:
                baseHR = 95;
                variance = 12;
                break;
            case NORMAL:
            default:
                baseHR = 75;
                variance = 10;
                break;
        }

        return baseHR + random.nextInt(variance * 2) - variance;
    }

    /**
     * 生成血氧数据
     */
    private int generateSpO2(PatientEntity patient) {
        int baseSpO2 = 98;
        int variance = 2;

        switch (patient.getStatus()) {
            case CRITICAL:
                baseSpO2 = 88;
                variance = 3;
                break;
            case WARNING:
                baseSpO2 = 93;
                variance = 2;
                break;
            case NORMAL:
            default:
                baseSpO2 = 98;
                variance = 1;
                break;
        }

        return Math.min(100, baseSpO2 + random.nextInt(variance * 2) - variance);
    }

    /**
     * 生成血压数据
     */
    private String generateNIBP(PatientEntity patient) {
        int systolic = 120 + random.nextInt(20) - 10;
        int diastolic = 80 + random.nextInt(10) - 5;
        return systolic + "/" + diastolic;
    }

    /**
     * 生成呼吸率数据
     */
    private int generateRR(PatientEntity patient) {
        return 16 + random.nextInt(8) - 4;
    }

    /**
     * 生成体温数据
     */
    private double generateTemp(PatientEntity patient) {
        double baseTemp = 36.5;
        double variance = 0.5;
        return Math.round((baseTemp + (random.nextDouble() * variance * 2 - variance)) * 10.0) / 10.0;
    }

    /**
     * 生成 ECG 波形数据（模拟）
     */
    private List<Number> generateECGWaveform() {
        List<Number> waveform = new ArrayList<>();
        // 简化的 ECG 波形：P波-QRS波群-T波
        int[] pattern = { 50, 60, 55, 50, 45, 50, 80, -20, 50, 50, 55, 60, 50 };
        for (int value : pattern) {
            waveform.add(value + random.nextInt(10) - 5);
        }
        return waveform;
    }

    /**
     * 生成 SpO2 波形数据（模拟）
     */
    private List<Number> generateSpO2Waveform() {
        List<Number> waveform = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            waveform.add(90 + random.nextInt(10));
        }
        return waveform;
    }

    /**
     * 生成呼吸波形数据（模拟）
     */
    private List<Number> generateRespWaveform() {
        List<Number> waveform = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            double angle = (i / 20.0) * 2 * Math.PI;
            waveform.add((int) (Math.sin(angle) * 30 + 50));
        }
        return waveform;
    }
}
