package com.example.healthconnect.utilty.impl.domain.mapper

import com.example.healthconnect.utilty.impl.domain.SupportedModels
import org.junit.Assert.assertNotNull
import org.junit.Test

class RecordTypePermissionMapperTest {

    private val mapper = RecordTypePermissionMapper()

    @Test
    fun `all supported record types have a read permission mapping`() {
        SupportedModels.all.forEach { type ->
            // throws if mapping is missing — catches omissions when new types are added
            val permission = mapper.readPermission(type)
            assertNotNull(
                "readPermission returned null for ${type.simpleName}",
                permission,
            )
        }
    }

    @Test
    fun `returned permission strings are declared in the manifest`() {
        val declaredPermissions = setOf(
            "android.permission.health.READ_ACTIVE_CALORIES_BURNED",
            "android.permission.health.READ_ACTIVITY_INTENSITY",
            "android.permission.health.READ_BASAL_BODY_TEMPERATURE",
            "android.permission.health.READ_BASAL_METABOLIC_RATE",
            "android.permission.health.READ_BLOOD_GLUCOSE",
            "android.permission.health.READ_BLOOD_PRESSURE",
            "android.permission.health.READ_BODY_FAT",
            "android.permission.health.READ_BODY_TEMPERATURE",
            "android.permission.health.READ_BODY_WATER_MASS",
            "android.permission.health.READ_BONE_MASS",
            "android.permission.health.READ_CERVICAL_MUCUS",
            "android.permission.health.READ_DISTANCE",
            "android.permission.health.READ_ELEVATION_GAINED",
            "android.permission.health.READ_EXERCISE",
            "android.permission.health.READ_FLOORS_CLIMBED",
            "android.permission.health.READ_HEART_RATE",
            "android.permission.health.READ_HEART_RATE_VARIABILITY",
            "android.permission.health.READ_HEIGHT",
            "android.permission.health.READ_HYDRATION",
            "android.permission.health.READ_INTERMENSTRUAL_BLEEDING",
            "android.permission.health.READ_LEAN_BODY_MASS",
            "android.permission.health.READ_MENSTRUATION",
            "android.permission.health.READ_MINDFULNESS",
            "android.permission.health.READ_NUTRITION",
            "android.permission.health.READ_OVULATION_TEST",
            "android.permission.health.READ_OXYGEN_SATURATION",
            "android.permission.health.READ_PLANNED_EXERCISE",
            "android.permission.health.READ_POWER",
            "android.permission.health.READ_RESPIRATORY_RATE",
            "android.permission.health.READ_RESTING_HEART_RATE",
            "android.permission.health.READ_SEXUAL_ACTIVITY",
            "android.permission.health.READ_SKIN_TEMPERATURE",
            "android.permission.health.READ_SLEEP",
            "android.permission.health.READ_SPEED",
            "android.permission.health.READ_STEPS",
            "android.permission.health.READ_TOTAL_CALORIES_BURNED",
            "android.permission.health.READ_VO2_MAX",
            "android.permission.health.READ_WEIGHT",
            "android.permission.health.READ_WHEELCHAIR_PUSHES",
        )

        SupportedModels.all.forEach { type ->
            val permission = mapper.readPermission(type)
            assert(permission.permissionString in declaredPermissions) {
                "${type.simpleName} maps to '${permission.permissionString}' " +
                    "which is not declared in AndroidManifest.xml"
            }
        }
    }
}
