package com.example.healthconnect.permissions.impl.data

import com.example.healthconnect.permissions.api.domain.HealthPermission
import com.example.healthconnect.permissions.api.domain.PermissionType

/**
 * Canonical list of every Health Connect permission declared in AndroidManifest.xml.
 *
 * Ordered: Read permissions alphabetically, then Write permissions alphabetically.
 * This order drives the grouping shown on PermissionsScreen.
 *
 * If the manifest changes, update [dataTypeNames] to match.
 */
internal object AllHealthPermissions {

    private const val PREFIX = "android.permission.health."

    private val dataTypeNames: List<String> = listOf(
        "ACTIVE_CALORIES_BURNED",
        "ACTIVITY_INTENSITY",
        "BASAL_BODY_TEMPERATURE",
        "BASAL_METABOLIC_RATE",
        "BLOOD_GLUCOSE",
        "BLOOD_PRESSURE",
        "BODY_FAT",
        "BODY_TEMPERATURE",
        "BODY_WATER_MASS",
        "BONE_MASS",
        "CERVICAL_MUCUS",
        "DISTANCE",
        "ELEVATION_GAINED",
        "EXERCISE",
        "EXERCISE_ROUTE",
        "FLOORS_CLIMBED",
        "HEART_RATE",
        "HEART_RATE_VARIABILITY",
        "HEIGHT",
        "HYDRATION",
        "INTERMENSTRUAL_BLEEDING",
        "LEAN_BODY_MASS",
        "MENSTRUATION",
        "MINDFULNESS",
        "NUTRITION",
        "OVULATION_TEST",
        "OXYGEN_SATURATION",
        "PLANNED_EXERCISE",
        "POWER",
        "RESPIRATORY_RATE",
        "RESTING_HEART_RATE",
        "SEXUAL_ACTIVITY",
        "SKIN_TEMPERATURE",
        "SLEEP",
        "SPEED",
        "STEPS",
        "TOTAL_CALORIES_BURNED",
        "VO2_MAX",
        "WEIGHT",
        "WHEELCHAIR_PUSHES",
    ).sorted()

    val read: List<HealthPermission> = dataTypeNames.map { name ->
        HealthPermission("${PREFIX}READ_$name", PermissionType.Read)
    }

    val write: List<HealthPermission> = dataTypeNames.map { name ->
        HealthPermission("${PREFIX}WRITE_$name", PermissionType.Write)
    }

    /** READ_METADATA has no WRITE counterpart. */
    val metadata: HealthPermission =
        HealthPermission("${PREFIX}READ_METADATA", PermissionType.Read)

    val all: List<HealthPermission> = read + listOf(metadata) + write

    /** Fast O(1) lookup: permissionString → HealthPermission */
    val byString: Map<String, HealthPermission> = all.associateBy { it.permissionString }
}
