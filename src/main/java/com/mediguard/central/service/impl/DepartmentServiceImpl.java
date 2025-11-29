package com.mediguard.central.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mediguard.central.dto.AlarmThresholdConfigDTO;
import com.mediguard.central.dto.DepartmentCapacityDTO;
import com.mediguard.central.dto.response.DepartmentConfigResponseDTO;
import com.mediguard.central.entity.*;
import com.mediguard.central.enums.AlarmLevel;
import com.mediguard.central.enums.DeviceType;
import com.mediguard.central.mapper.*;
import com.mediguard.central.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 科室服务实现类（重构版）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, DepartmentEntity>
        implements DepartmentService {

    private final DeviceWaveformConfigMapper waveformConfigMapper;
    private final DeviceParameterConfigMapper parameterConfigMapper;
    private final AlarmThresholdConfigMapper thresholdConfigMapper;
    private final BedLabelConfigMapper bedLabelConfigMapper;

    @Override
    public DepartmentConfigResponseDTO getConfig(String departmentCode) {
        log.info("【科室配置】获取科室配置，科室={}", departmentCode);
        DepartmentEntity dept = this.getOne(new LambdaQueryWrapper<DepartmentEntity>()
                .eq(DepartmentEntity::getCode, departmentCode));

        if (dept == null) {
            throw new RuntimeException("科室不存在");
        }

        DepartmentConfigResponseDTO response = new DepartmentConfigResponseDTO();

        // 1. 床位容量
        response.setDeptCapacity(Map.of(dept.getCode(), dept.getCapacity()));

        // 2. 床位标签
        List<BedLabelConfigEntity> bedLabels = bedLabelConfigMapper.selectList(
                new LambdaQueryWrapper<BedLabelConfigEntity>()
                        .eq(BedLabelConfigEntity::getDepartmentCode, departmentCode));
        Map<String, String> bedLabelMap = bedLabels.stream()
                .collect(Collectors.toMap(BedLabelConfigEntity::getBedNumber, BedLabelConfigEntity::getCustomLabel));
        response.setBedLabels(Map.of(dept.getCode(), bedLabelMap));

        // 3. 设备配置（波形和参数）
        Map<String, DepartmentConfigResponseDTO.DeviceConfigDTO> deviceConfigs = new HashMap<>();
        for (DeviceType deviceType : DeviceType.values()) {
            DepartmentConfigResponseDTO.DeviceConfigDTO config = new DepartmentConfigResponseDTO.DeviceConfigDTO();

            // 波形配置
            List<DeviceWaveformConfigEntity> waveforms = waveformConfigMapper.selectList(
                    new LambdaQueryWrapper<DeviceWaveformConfigEntity>()
                            .eq(DeviceWaveformConfigEntity::getDepartmentCode, departmentCode)
                            .eq(DeviceWaveformConfigEntity::getDeviceType, deviceType)
                            .orderByAsc(DeviceWaveformConfigEntity::getDisplayOrder));
            List<DepartmentConfigResponseDTO.WaveformItemDTO> waveformList = waveforms.stream().map(w -> {
                DepartmentConfigResponseDTO.WaveformItemDTO dto = new DepartmentConfigResponseDTO.WaveformItemDTO();
                dto.setId(w.getWaveformId());
                dto.setLabel(w.getLabel());
                dto.setVisible(w.getVisible());
                dto.setColor(w.getColor());
                dto.setOrder(w.getDisplayOrder());
                return dto;
            }).collect(Collectors.toList());
            config.setWaveforms(waveformList);

            // 参数配置
            List<DeviceParameterConfigEntity> parameters = parameterConfigMapper.selectList(
                    new LambdaQueryWrapper<DeviceParameterConfigEntity>()
                            .eq(DeviceParameterConfigEntity::getDepartmentCode, departmentCode)
                            .eq(DeviceParameterConfigEntity::getDeviceType, deviceType)
                            .orderByAsc(DeviceParameterConfigEntity::getDisplayOrder));
            List<DepartmentConfigResponseDTO.ParameterItemDTO> parameterList = parameters.stream().map(p -> {
                DepartmentConfigResponseDTO.ParameterItemDTO dto = new DepartmentConfigResponseDTO.ParameterItemDTO();
                dto.setId(p.getParameterId());
                dto.setLabel(p.getLabel());
                dto.setUnit(p.getUnit());
                dto.setVisible(p.getVisible());
                dto.setOrder(p.getDisplayOrder());
                return dto;
            }).collect(Collectors.toList());
            config.setParameters(parameterList);

            deviceConfigs.put(deviceType.getCode(), config);
        }
        response.setDeviceConfigs(Map.of(dept.getCode(), deviceConfigs));

        // 4. 报警阈值
        List<AlarmThresholdConfigEntity> thresholds = thresholdConfigMapper.selectList(
                new LambdaQueryWrapper<AlarmThresholdConfigEntity>()
                        .eq(AlarmThresholdConfigEntity::getDepartmentCode, departmentCode));
        List<DepartmentConfigResponseDTO.AlarmThresholdDTO> thresholdList = thresholds.stream().map(t -> {
            DepartmentConfigResponseDTO.AlarmThresholdDTO dto = new DepartmentConfigResponseDTO.AlarmThresholdDTO();
            dto.setParamId(t.getParamId());
            dto.setLabel(t.getLabel());
            dto.setMin(t.getMinValue());
            dto.setMax(t.getMaxValue());
            dto.setDelay(t.getDelaySeconds());
            dto.setPriority(t.getPriority().getCode());
            dto.setEnabled(t.getEnabled());
            return dto;
        }).collect(Collectors.toList());
        response.setAlarmThresholds(Map.of(dept.getCode(), thresholdList));

        // 5. 偏好设置
        com.mediguard.central.dto.DepartmentPreferencesDTO prefs = new com.mediguard.central.dto.DepartmentPreferencesDTO();
        prefs.setIsDemoMode(dept.getIsDemoMode());
        prefs.setSimulationSpeed(dept.getSimulationSpeed());
        prefs.setNightMode(dept.getNightMode());
        prefs.setAudioEnabled(dept.getAudioEnabled());
        response.setPreferences(prefs);

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePreferences(String departmentCode,
            com.mediguard.central.dto.DepartmentPreferencesDTO preferences) {
        log.info("【科室配置】更新偏好设置，科室={}，参数={}", departmentCode, preferences);
        DepartmentEntity dept = this.getOne(new LambdaQueryWrapper<DepartmentEntity>()
                .eq(DepartmentEntity::getCode, departmentCode));
        if (dept == null)
            throw new RuntimeException("科室不存在");

        if (preferences.getIsDemoMode() != null)
            dept.setIsDemoMode(preferences.getIsDemoMode());
        if (preferences.getSimulationSpeed() != null)
            dept.setSimulationSpeed(preferences.getSimulationSpeed());
        if (preferences.getNightMode() != null)
            dept.setNightMode(preferences.getNightMode());
        if (preferences.getAudioEnabled() != null)
            dept.setAudioEnabled(preferences.getAudioEnabled());

        this.updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCapacity(String departmentCode, DepartmentCapacityDTO dto) {
        log.info("【科室配置】更新床位容量，科室={}，参数={}", departmentCode, dto);
        DepartmentEntity dept = this.getOne(new LambdaQueryWrapper<DepartmentEntity>()
                .eq(DepartmentEntity::getCode, departmentCode));
        if (dept == null)
            throw new RuntimeException("科室不存在");

        if (dto.getCapacity() != null) {
            dept.setCapacity(dto.getCapacity());
            this.updateById(dept);
        }

        // 更新床位标签
        if (dto.getBedLabels() != null) {
            for (Map.Entry<String, String> entry : dto.getBedLabels().entrySet()) {
                BedLabelConfigEntity label = bedLabelConfigMapper.selectOne(
                        new LambdaQueryWrapper<BedLabelConfigEntity>()
                                .eq(BedLabelConfigEntity::getDepartmentCode, departmentCode)
                                .eq(BedLabelConfigEntity::getBedNumber, entry.getKey()));
                if (label == null) {
                    label = new BedLabelConfigEntity();
                    label.setDepartmentCode(departmentCode);
                    label.setBedNumber(entry.getKey());
                    label.setCustomLabel(entry.getValue());
                    bedLabelConfigMapper.insert(label);
                } else {
                    label.setCustomLabel(entry.getValue());
                    bedLabelConfigMapper.updateById(label);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWaveformConfigs(String departmentCode, String deviceType,
            List<com.mediguard.central.dto.WaveformConfigDTO> waveforms) {
        log.info("【科室配置】更新设备波形配置，科室={}，设备类型={}", departmentCode, deviceType);

        DeviceType type = DeviceType.valueOf(deviceType);

        // 删除旧配置
        waveformConfigMapper.delete(new LambdaQueryWrapper<DeviceWaveformConfigEntity>()
                .eq(DeviceWaveformConfigEntity::getDepartmentCode, departmentCode)
                .eq(DeviceWaveformConfigEntity::getDeviceType, type));

        // 插入新配置
        for (com.mediguard.central.dto.WaveformConfigDTO waveform : waveforms) {
            DeviceWaveformConfigEntity entity = new DeviceWaveformConfigEntity();
            entity.setDepartmentCode(departmentCode);
            entity.setDeviceType(type);
            entity.setWaveformId(waveform.getId());
            entity.setLabel(waveform.getLabel());
            entity.setVisible(waveform.getVisible());
            entity.setColor(waveform.getColor());
            entity.setDisplayOrder(waveform.getOrder());
            waveformConfigMapper.insert(entity);
        }

        log.info("【科室配置】波形配置更新成功，共 {} 条", waveforms.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateParameterConfigs(String departmentCode, String deviceType,
            List<com.mediguard.central.dto.ParameterConfigDTO> parameters) {
        log.info("【科室配置】更新设备参数配置，科室={}，设备类型={}", departmentCode, deviceType);

        DeviceType type = DeviceType.valueOf(deviceType);

        // 删除旧配置
        parameterConfigMapper.delete(new LambdaQueryWrapper<DeviceParameterConfigEntity>()
                .eq(DeviceParameterConfigEntity::getDepartmentCode, departmentCode)
                .eq(DeviceParameterConfigEntity::getDeviceType, type));

        // 插入新配置
        for (com.mediguard.central.dto.ParameterConfigDTO parameter : parameters) {
            DeviceParameterConfigEntity entity = new DeviceParameterConfigEntity();
            entity.setDepartmentCode(departmentCode);
            entity.setDeviceType(type);
            entity.setParameterId(parameter.getId());
            entity.setLabel(parameter.getLabel());
            entity.setUnit(parameter.getUnit());
            entity.setVisible(parameter.getVisible());
            entity.setDisplayOrder(parameter.getOrder());
            parameterConfigMapper.insert(entity);
        }

        log.info("【科室配置】参数配置更新成功，共 {} 条", parameters.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAlarmThresholds(String departmentCode, List<AlarmThresholdConfigDTO> thresholds) {
        log.info("【科室配置】更新报警阈值，科室={}", departmentCode);
        // 删除旧配置
        thresholdConfigMapper.delete(new LambdaQueryWrapper<AlarmThresholdConfigEntity>()
                .eq(AlarmThresholdConfigEntity::getDepartmentCode, departmentCode));

        // 插入新配置
        for (AlarmThresholdConfigDTO threshold : thresholds) {
            AlarmThresholdConfigEntity entity = new AlarmThresholdConfigEntity();
            entity.setDepartmentCode(departmentCode);
            entity.setParamId(threshold.getParamId());
            entity.setLabel(threshold.getLabel());
            entity.setMinValue(threshold.getMin());
            entity.setMaxValue(threshold.getMax());
            entity.setDelaySeconds(threshold.getDelay());
            entity.setPriority(AlarmLevel.valueOf(threshold.getPriority()));
            entity.setEnabled(threshold.getEnabled());
            thresholdConfigMapper.insert(entity);
        }
    }

    @Override
    public List<com.mediguard.central.dto.response.DepartmentResponseDTO> getAllDepartments() {
        log.info("【科室管理】获取所有科室列表");

        List<DepartmentEntity> departments = this.list();

        return departments.stream()
                .map(dept -> {
                    com.mediguard.central.dto.response.DepartmentResponseDTO dto = new com.mediguard.central.dto.response.DepartmentResponseDTO();
                    dto.setId(dept.getId());
                    dto.setCode(dept.getCode());
                    dto.setName(dept.getName());
                    dto.setCapacity(dept.getCapacity());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public com.mediguard.central.dto.response.DepartmentResponseDTO createDepartment(
            com.mediguard.central.dto.CreateDepartmentDTO dto) {
        log.info("【科室管理】创建新科室，code={}, name={}", dto.getCode(), dto.getName());

        // 检查科室代码是否已存在
        DepartmentEntity existing = this.getOne(new LambdaQueryWrapper<DepartmentEntity>()
                .eq(DepartmentEntity::getCode, dto.getCode()));

        if (existing != null) {
            throw new RuntimeException("科室代码已存在: " + dto.getCode());
        }

        // 创建新科室
        DepartmentEntity department = new DepartmentEntity();
        department.setCode(dto.getCode());
        department.setName(dto.getName());
        department.setCapacity(dto.getCapacity());

        this.save(department);

        // 初始化默认配置（监护仪）
        initializeDefaultConfigs(department.getCode());

        // 返回创建结果
        com.mediguard.central.dto.response.DepartmentResponseDTO response = new com.mediguard.central.dto.response.DepartmentResponseDTO();
        response.setId(department.getId());
        response.setCode(department.getCode());
        response.setName(department.getName());
        response.setCapacity(department.getCapacity());

        log.info("【科室管理】科室创建成功，id={}，已初始化默认配置", department.getId());
        return response;
    }

    /**
     * 为新科室初始化默认配置
     */
    private void initializeDefaultConfigs(String departmentCode) {
        log.info("【科室管理】初始化科室默认配置，code={}", departmentCode);

        // 初始化监护仪配置
        initMonitorConfigs(departmentCode);

        // 初始化呼吸机配置
        initVentilatorConfigs(departmentCode);

        // 初始化麻醉机配置
        initAnesthesiaConfigs(departmentCode);

        log.info("【科室管理】默认配置初始化完成");
    }

    /**
     * 初始化监护仪配置
     */
    private void initMonitorConfigs(String departmentCode) {
        // 监护仪波形配置
        DeviceWaveformConfigEntity[] monitorWaveforms = {
                createWaveformConfig(departmentCode, DeviceType.MONITOR, "ecg", "ECG I", "#00ff41", 1),
                createWaveformConfig(departmentCode, DeviceType.MONITOR, "spo2", "SpO2", "#00d0ff", 2),
                createWaveformConfig(departmentCode, DeviceType.MONITOR, "resp", "Resp", "#ffea00", 3),
                createWaveformConfig(departmentCode, DeviceType.MONITOR, "ibp", "IBP", "#ff0000", 4)
        };
        for (DeviceWaveformConfigEntity config : monitorWaveforms) {
            waveformConfigMapper.insert(config);
        }

        // 监护仪参数配置
        DeviceParameterConfigEntity[] monitorParameters = {
                createParameterConfig(departmentCode, DeviceType.MONITOR, "hr", "HR", "bpm", 1),
                createParameterConfig(departmentCode, DeviceType.MONITOR, "spo2", "SpO2", "%", 2),
                createParameterConfig(departmentCode, DeviceType.MONITOR, "nibp", "NIBP", "mmHg", 3),
                createParameterConfig(departmentCode, DeviceType.MONITOR, "rr", "RR", "rpm", 4),
                createParameterConfig(departmentCode, DeviceType.MONITOR, "temp", "Temp", "°C", 5)
        };
        for (DeviceParameterConfigEntity config : monitorParameters) {
            parameterConfigMapper.insert(config);
        }

        // 监护仪报警阈值配置
        AlarmThresholdConfigEntity[] monitorThresholds = {
                createThresholdConfig(departmentCode, "hr", "心率", 50, 120, 2, AlarmLevel.CRITICAL),
                createThresholdConfig(departmentCode, "spo2", "血氧", 90, 100, 5, AlarmLevel.CRITICAL),
                createThresholdConfig(departmentCode, "rr", "呼吸率", 10, 30, 3, AlarmLevel.WARNING),
                createThresholdConfig(departmentCode, "temp", "体温", 36, 38, 10, AlarmLevel.WARNING)
        };
        for (AlarmThresholdConfigEntity config : monitorThresholds) {
            thresholdConfigMapper.insert(config);
        }
    }

    /**
     * 初始化呼吸机配置
     */
    private void initVentilatorConfigs(String departmentCode) {
        // 呼吸机波形配置
        DeviceWaveformConfigEntity[] ventilatorWaveforms = {
                createWaveformConfig(departmentCode, DeviceType.VENTILATOR, "paw", "Paw", "#ff9500", 1),
                createWaveformConfig(departmentCode, DeviceType.VENTILATOR, "flow", "Flow", "#00d0ff", 2),
                createWaveformConfig(departmentCode, DeviceType.VENTILATOR, "volume", "Volume", "#ffea00", 3)
        };
        for (DeviceWaveformConfigEntity config : ventilatorWaveforms) {
            waveformConfigMapper.insert(config);
        }

        // 呼吸机参数配置
        DeviceParameterConfigEntity[] ventilatorParameters = {
                createParameterConfig(departmentCode, DeviceType.VENTILATOR, "ppeak", "Ppeak", "cmH2O", 1),
                createParameterConfig(departmentCode, DeviceType.VENTILATOR, "peep", "PEEP", "cmH2O", 2),
                createParameterConfig(departmentCode, DeviceType.VENTILATOR, "vte", "VTe", "mL", 3),
                createParameterConfig(departmentCode, DeviceType.VENTILATOR, "fio2", "FiO2", "%", 4)
        };
        for (DeviceParameterConfigEntity config : ventilatorParameters) {
            parameterConfigMapper.insert(config);
        }

        // 呼吸机报警阈值配置
        AlarmThresholdConfigEntity[] ventilatorThresholds = {
                createThresholdConfig(departmentCode, "ppeak", "气道峰压", null, 40, 1, AlarmLevel.CRITICAL),
                createThresholdConfig(departmentCode, "peep", "呼气末压", 3, 20, 1, AlarmLevel.WARNING),
                createThresholdConfig(departmentCode, "vte", "潮气量", 300, 800, 2, AlarmLevel.WARNING)
        };
        for (AlarmThresholdConfigEntity config : ventilatorThresholds) {
            thresholdConfigMapper.insert(config);
        }
    }

    /**
     * 初始化麻醉机配置
     */
    private void initAnesthesiaConfigs(String departmentCode) {
        // 麻醉机波形配置
        DeviceWaveformConfigEntity[] anesthesiaWaveforms = {
                createWaveformConfig(departmentCode, DeviceType.ANESTHESIA, "ecg", "ECG", "#00ff41", 1),
                createWaveformConfig(departmentCode, DeviceType.ANESTHESIA, "ibp", "IBP", "#ff0000", 2),
                createWaveformConfig(departmentCode, DeviceType.ANESTHESIA, "etco2", "EtCO2", "#ffea00", 3)
        };
        for (DeviceWaveformConfigEntity config : anesthesiaWaveforms) {
            waveformConfigMapper.insert(config);
        }

        // 麻醉机参数配置
        DeviceParameterConfigEntity[] anesthesiaParameters = {
                createParameterConfig(departmentCode, DeviceType.ANESTHESIA, "hr", "HR", "bpm", 1),
                createParameterConfig(departmentCode, DeviceType.ANESTHESIA, "nibp", "NIBP", "mmHg", 2),
                createParameterConfig(departmentCode, DeviceType.ANESTHESIA, "spo2", "SpO2", "%", 3),
                createParameterConfig(departmentCode, DeviceType.ANESTHESIA, "etco2", "EtCO2", "mmHg", 4),
                createParameterConfig(departmentCode, DeviceType.ANESTHESIA, "mac", "MAC", "", 5)
        };
        for (DeviceParameterConfigEntity config : anesthesiaParameters) {
            parameterConfigMapper.insert(config);
        }

        // 麻醉机报警阈值配置
        AlarmThresholdConfigEntity[] anesthesiaThresholds = {
                createThresholdConfig(departmentCode, "hr", "心率", 50, 120, 2, AlarmLevel.CRITICAL),
                createThresholdConfig(departmentCode, "spo2", "血氧", 90, 100, 5, AlarmLevel.CRITICAL),
                createThresholdConfig(departmentCode, "etco2", "呼末二氧化碳", 30, 45, 3, AlarmLevel.WARNING)
        };
        for (AlarmThresholdConfigEntity config : anesthesiaThresholds) {
            thresholdConfigMapper.insert(config);
        }
    }

    /**
     * 创建波形配置实体
     */
    private DeviceWaveformConfigEntity createWaveformConfig(String deptCode, DeviceType deviceType,
            String waveformId, String label,
            String color, int order) {
        DeviceWaveformConfigEntity entity = new DeviceWaveformConfigEntity();
        entity.setDepartmentCode(deptCode);
        entity.setDeviceType(deviceType);
        entity.setWaveformId(waveformId);
        entity.setLabel(label);
        entity.setVisible(true);
        entity.setColor(color);
        entity.setDisplayOrder(order);
        return entity;
    }

    /**
     * 创建参数配置实体
     */
    private DeviceParameterConfigEntity createParameterConfig(String deptCode, DeviceType deviceType,
            String parameterId, String label,
            String unit, int order) {
        DeviceParameterConfigEntity entity = new DeviceParameterConfigEntity();
        entity.setDepartmentCode(deptCode);
        entity.setDeviceType(deviceType);
        entity.setParameterId(parameterId);
        entity.setLabel(label);
        entity.setUnit(unit);
        entity.setVisible(true);
        entity.setDisplayOrder(order);
        return entity;
    }

    /**
     * 创建报警阈值配置实体
     */
    private AlarmThresholdConfigEntity createThresholdConfig(String deptCode, String paramId,
            String label, Integer min, Integer max,
            int delay, AlarmLevel priority) {
        AlarmThresholdConfigEntity entity = new AlarmThresholdConfigEntity();
        entity.setDepartmentCode(deptCode);
        entity.setParamId(paramId);
        entity.setLabel(label);
        entity.setMinValue(min);
        entity.setMaxValue(max);
        entity.setDelaySeconds(delay);
        entity.setPriority(priority);
        entity.setEnabled(true);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepartment(String code) {
        log.info("【科室管理】删除科室，code={}", code);

        // 检查科室是否存在
        DepartmentEntity department = this.getOne(new LambdaQueryWrapper<DepartmentEntity>()
                .eq(DepartmentEntity::getCode, code));

        if (department == null) {
            throw new RuntimeException("科室不存在: " + code);
        }

        // 删除科室相关配置
        waveformConfigMapper.delete(new LambdaQueryWrapper<DeviceWaveformConfigEntity>()
                .eq(DeviceWaveformConfigEntity::getDepartmentCode, code));

        parameterConfigMapper.delete(new LambdaQueryWrapper<DeviceParameterConfigEntity>()
                .eq(DeviceParameterConfigEntity::getDepartmentCode, code));

        thresholdConfigMapper.delete(new LambdaQueryWrapper<AlarmThresholdConfigEntity>()
                .eq(AlarmThresholdConfigEntity::getDepartmentCode, code));

        bedLabelConfigMapper.delete(new LambdaQueryWrapper<BedLabelConfigEntity>()
                .eq(BedLabelConfigEntity::getDepartmentCode, code));

        // 删除科室
        this.remove(new LambdaQueryWrapper<DepartmentEntity>()
                .eq(DepartmentEntity::getCode, code));

        log.info("【科室管理】科室删除成功，code={}", code);
    }
}
