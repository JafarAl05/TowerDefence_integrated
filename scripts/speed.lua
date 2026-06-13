
speedScalePerWave = 0.15
healthScalePerWave = 0.20
rewardScalePerWave = 0.08

function getEnemySpeedMultiplier(wave)
    return 1.0 + speedScalePerWave * math.max(0, wave - 1)
end

function getEnemyHealthMultiplier(wave)
    return 1.0 + healthScalePerWave * math.max(0, wave - 1)
end

function getEnemyRewardMultiplier(wave)
    return 1.0 + rewardScalePerWave * math.max(0, wave - 1)
end
