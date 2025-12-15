package com.mediguard.central.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mediguard.central.entity.PatientEntity;
import com.mediguard.central.mapper.PatientMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * STOMP WebSocket 生命体征数据推送服务 (优化版)
 * 1. 使用内存缓存避免每秒查询数据库
 * 2. 仅在患者变更时刷新缓存
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StompVitalSignsPushService {

    private final SimpMessagingTemplate messagingTemplate;
    private final PatientMapper patientMapper;
    private final Random random = new Random();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    // 患者缓存：key=patientId(String), value=PatientEntity
    private final Map<String, PatientEntity> patientCache = new ConcurrentHashMap<>();

    /**
     * 初始化加载患者缓存
     */
    @PostConstruct
    public void init() {
        refreshPatientCache();
        log.info("【STOMP推送】初始化患者缓存,患者数={}", patientCache.size());
    }

    /**
     * 每30秒刷新一次患者缓存 (检测患者入科/出科)
     */
    @Scheduled(fixedRate = 30000)
    public void refreshPatientCache() {
        try {
            List<PatientEntity> patients = patientMapper.selectList(null);

            // 清空旧缓存
            patientCache.clear();

            // 重新加载
            for (PatientEntity patient : patients) {
                patientCache.put(patient.getId(), patient);
            }

            log.debug("【STOMP推送】刷新患者缓存,患者数={}", patientCache.size());
        } catch (Exception e) {
            log.error("【STOMP推送】刷新缓存失败", e);
        }
    }

    /**
     * 手动刷新缓存 (患者入科/出科时调用)
     */
    public void forceRefreshCache() {
        refreshPatientCache();
    }

    /**
     * 每秒推送一次生命体征数据到 /topic/vitals
     * 使用缓存的患者列表,不查询数据库
     */
    @Scheduled(fixedRate = 1000)
    public void pushVitalSigns() {
        if (patientCache.isEmpty()) {
            return; // 没有患者时不推送
        }

        try {
            // 构建推送数据数组 (从缓存读取)
            List<Map<String, Object>> dataList = new ArrayList<>();

            for (PatientEntity patient : patientCache.values()) {
                Map<String, Object> deviceData = new HashMap<>();
                deviceData.put("deviceId", patient.getDeviceId());
                deviceData.put("bedNumber", patient.getBedNumber());
                deviceData.put("patientId", patient.getId());
                deviceData.put("timestamp", LocalDateTime.now().format(formatter));

                // 生成参数数据
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("hr", generateHR(patient));
                parameters.put("spo2", generateSpO2(patient));
                parameters.put("nibp", generateNIBP(patient));
                parameters.put("rr", generateRR(patient));
                parameters.put("temp", generateTemp(patient));
                deviceData.put("parameters", parameters);

                // 生成波形数据
                Map<String, List<Number>> waveforms = new HashMap<>();
                waveforms.put("ecg", generateECGWaveform());
                waveforms.put("spo2", generateSpO2Waveform());
                waveforms.put("resp", generateRespWaveform());
                deviceData.put("waveforms", waveforms);

                dataList.add(deviceData);
            }

            // 推送到 /topic/vitals
            messagingTemplate.convertAndSend("/topic/vitals", dataList);

            log.debug("【STOMP推送】推送生命体征数据,患者数={}", patientCache.size());

        } catch (Exception e) {
            log.error("【STOMP推送】推送失败", e);
        }
    }

    private int generateHR(PatientEntity patient) {
        int baseHR = 75;
        int variance = 10;

        if (patient.getStatus() != null) {
            switch (patient.getStatus()) {
                case CRITICAL:
                    baseHR = 130;
                    variance = 15;
                    break;
                case WARNING:
                    baseHR = 95;
                    variance = 12;
                    break;
                default:
                    break;
            }
        }

        return baseHR + random.nextInt(variance * 2) - variance;
    }

    private int generateSpO2(PatientEntity patient) {
        int baseSpO2 = 98;
        int variance = 2;

        if (patient.getStatus() != null) {
            switch (patient.getStatus()) {
                case CRITICAL:
                    baseSpO2 = 88;
                    variance = 3;
                    break;
                case WARNING:
                    baseSpO2 = 93;
                    variance = 2;
                    break;
                default:
                    break;
            }
        }

        return Math.min(100, baseSpO2 + random.nextInt(variance * 2) - variance);
    }

    private String generateNIBP(PatientEntity patient) {
        int systolic = 120 + random.nextInt(20) - 10;
        int diastolic = 80 + random.nextInt(10) - 5;
        return systolic + "/" + diastolic;
    }

    private int generateRR(PatientEntity patient) {
        return 16 + random.nextInt(8) - 4;
    }

    private double generateTemp(PatientEntity patient) {
        double baseTemp = 36.5;
        double variance = 0.5;
        return Math.round((baseTemp + (random.nextDouble() * variance * 2 - variance)) * 10.0) / 10.0;
    }

    private List<Number> generateECGWaveform() {
        List<Number> waveform = new ArrayList<>();
        // 生成0.5秒的波形数据 (60个点约等于0.5秒 @ 120Hz采样率)
        for (int i = 0; i < 60; i++) {
            double angle = (i / 60.0) * 2 * Math.PI; // 一个完整心跳周期
            int baseValue = 50;

            // 模拟P-QRS-T波形
            if (i < 10) {
                // P波
                baseValue += (int) (10 * Math.sin(angle));
            } else if (i >= 15 && i < 35) {
                // QRS波群
                double qrsAngle = ((i - 15) / 20.0) * 2 * Math.PI;
                if (i == 25) {
                    baseValue += 30; // R波峰值
                } else if (i < 25) {
                    baseValue -= 20; // Q波
                } else {
                    baseValue -= 10; // S波
                }
            } else if (i >= 40 && i < 55) {
                // T波
                double tAngle = ((i - 40) / 15.0) * Math.PI;
                baseValue += (int) (15 * Math.sin(tAngle));
            }

            waveform.add(baseValue + random.nextInt(5) - 2);
        }
        return waveform;
    }

    private List<Number> generateSpO2Waveform() {
        List<Number> waveform = new ArrayList<>();
        // 脉搜波，60个点约等于1个心跳周期
        for (int i = 0; i < 60; i++) {
            double angle = (i / 60.0) * 2 * Math.PI;
            int value = 50 + (int) (40 * Math.sin(angle));
            if (i > 45) {
                // 下降支
                value = 50 + (int) (20 * Math.sin(angle));
            }
            waveform.add(value + random.nextInt(3));
        }
        return waveform;
    }

    private List<Number> generateRespWaveform() {
        List<Number> waveform = new ArrayList<>();
        // 呼吸波，60个点约等于1次呼吸周期
        for (int i = 0; i < 60; i++) {
            double angle = (i / 60.0) * 2 * Math.PI;
            waveform.add((int) (Math.sin(angle) * 30 + 50));
        }
        return waveform;
    }
}
