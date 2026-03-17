package com.example.healthconnect.components.api.domain.entity.field.atomic

import androidx.health.connect.client.records.ActivityIntensityRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.records.CervicalMucusRecord
import androidx.health.connect.client.records.ExerciseSegment
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.records.MindfulnessSessionRecord
import androidx.health.connect.client.records.OvulationTestRecord
import androidx.health.connect.client.records.PlannedExerciseStep
import androidx.health.connect.client.records.SexualActivityRecord
import androidx.health.connect.client.records.SkinTemperatureRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.example.healthconnect.components.api.domain.entity.ComponentModel.Companion.PRIORITY_DEFAULT
import java.util.UUID

data class SelectorField(
    val value: Int,
    val type: Type,
    override val instanceId: UUID = UUID.randomUUID(),
    override val priority: Int = PRIORITY_DEFAULT,
) : Atomic(instanceId) {

    override fun isValid(): Boolean  = type.items.any { it.first == value }

    fun map(item: Int): String {
        val foundItem = type.items.find { it.first == item }
        return requireNotNull(foundItem?.second) {
            "${type.title} = $item not found among available items: ${type.items}"
        }
    }

    sealed class Type {
        abstract val title: String
        abstract val supportText: String
        abstract val items: List<Pair<Int, String>>

        data class MeasurementLocationBodyTemperature(
            override val title: String = "Measurement Location",
            override val supportText: String = "Where on the user's body the temperature measurement was taken from. Optional field.",
            override val items: List<Pair<Int, String>> = listOf(
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_UNKNOWN to "UNKNOWN",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_ARMPIT to "ARMPIT",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_FINGER to "FINGER",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_FOREHEAD to "FOREHEAD",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_MOUTH to "MOUTH",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_RECTUM to "RECTUM",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_TEMPORAL_ARTERY to "TEMPORAL_ARTERY",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_TOE to "TOE",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_EAR to "EAR",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_WRIST to "WRIST",
                BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_VAGINA to "VAGINA",
            ),
        ) : Type()

        data class SpecimenSource(
            override val title: String = "Specimen Source",
            override val supportText: String = "Type of body fluid used to measure the blood glucose. Optional, enum field.",
            override val items: List<Pair<Int, String>> = listOf(
                BloodGlucoseRecord.SPECIMEN_SOURCE_UNKNOWN to "UNKNOWN",
                BloodGlucoseRecord.SPECIMEN_SOURCE_INTERSTITIAL_FLUID to "INTERSTITIAL_FLUID",
                BloodGlucoseRecord.SPECIMEN_SOURCE_CAPILLARY_BLOOD to "CAPILLARY_BLOOD",
                BloodGlucoseRecord.SPECIMEN_SOURCE_PLASMA to "PLASMA",
                BloodGlucoseRecord.SPECIMEN_SOURCE_SERUM to "SERUM",
                BloodGlucoseRecord.SPECIMEN_SOURCE_TEARS to "TEARS",
                BloodGlucoseRecord.SPECIMEN_SOURCE_WHOLE_BLOOD to "WHOLE_BLOOD",
            ),
        ) : Type()

        data class NutritionMealType(
            override val title: String = "Meal Type",
            override val supportText: String = "Type of meal related to the nutrients consumed. Optional, enum field.",
            override val items: List<Pair<Int, String>> = MEAL_TYPES,
        ) : Type()

        data class BloodGlucoseMealType(
            override val title: String = "Meal Type",
            override val supportText: String = "Type of meal related to the blood glucose measurement. Optional, enum field.",
            override val items: List<Pair<Int, String>> = MEAL_TYPES,
        ) : Type()

        data class RelationToMeal(
            override val title: String = "Relation To Meal",
            override val supportText: String = "Relationship of the meal to the blood glucose measurement. Optional, enum field.",
            override val items: List<Pair<Int, String>> = listOf(
                BloodGlucoseRecord.RELATION_TO_MEAL_UNKNOWN to "UNKNOWN",
                BloodGlucoseRecord.RELATION_TO_MEAL_GENERAL to "GENERAL",
                BloodGlucoseRecord.RELATION_TO_MEAL_FASTING to "FASTING",
                BloodGlucoseRecord.RELATION_TO_MEAL_BEFORE_MEAL to "BEFORE_MEAL",
                BloodGlucoseRecord.RELATION_TO_MEAL_AFTER_MEAL to "AFTER_MEAL",
            ),
        ) : Type()

        data class BodyPosition(
            override val title: String = "Body Position",
            override val supportText: String = "The user's body position when the measurement was taken. Optional field.",
            override val items: List<Pair<Int, String>> = listOf(
                BloodPressureRecord.BODY_POSITION_UNKNOWN to "UNKNOWN",
                BloodPressureRecord.BODY_POSITION_STANDING_UP to "STANDING_UP",
                BloodPressureRecord.BODY_POSITION_SITTING_DOWN to "SITTING_DOWN",
                BloodPressureRecord.BODY_POSITION_LYING_DOWN to "LYING_DOWN",
                BloodPressureRecord.BODY_POSITION_RECLINING to "RECLINING",
            ),
        ) : Type()

        data class MeasurementLocationBloodPressure(
            override val title: String = "Measurement Location",
            override val supportText: String = "The arm and part of the arm where the measurement was taken. Optional field.",
            override val items: List<Pair<Int, String>> = listOf(
                BloodPressureRecord.MEASUREMENT_LOCATION_UNKNOWN to "UNKNOWN",
                BloodPressureRecord.MEASUREMENT_LOCATION_LEFT_WRIST to "LEFT_WRIST",
                BloodPressureRecord.MEASUREMENT_LOCATION_RIGHT_WRIST to "RIGHT_WRIST",
                BloodPressureRecord.MEASUREMENT_LOCATION_LEFT_UPPER_ARM to "LEFT_UPPER_ARM",
                BloodPressureRecord.MEASUREMENT_LOCATION_RIGHT_UPPER_ARM to "RIGHT_UPPER_ARM",
            ),
        ) : Type()

        data class Appearance(
            override val title: String = "Appearance",
            override val supportText: String = "The consistency of the user's cervical mucus.",
            override val items: List<Pair<Int, String>> = listOf(
                CervicalMucusRecord.APPEARANCE_UNKNOWN to "UNKNOWN",
                CervicalMucusRecord.APPEARANCE_DRY to "DRY",
                CervicalMucusRecord.APPEARANCE_STICKY to "STICKY",
                CervicalMucusRecord.APPEARANCE_CREAMY to "CREAMY",
                CervicalMucusRecord.APPEARANCE_WATERY to "WATERY",
                CervicalMucusRecord.APPEARANCE_EGG_WHITE to "EGG_WHITE",
                CervicalMucusRecord.APPEARANCE_UNUSUAL to "UNUSUAL",
            ),
        ) : Type()

        data class Sensation(
            override val title: String = "Sensation",
            override val supportText: String = "The feel of the user's cervical mucus.",
            override val items: List<Pair<Int, String>> = listOf(
                CervicalMucusRecord.SENSATION_UNKNOWN to "UNKNOWN",
                CervicalMucusRecord.SENSATION_LIGHT to "LIGHT",
                CervicalMucusRecord.SENSATION_MEDIUM to "MEDIUM",
                CervicalMucusRecord.SENSATION_HEAVY to "HEAVY",
            ),
        ) : Type()

        data class Flow(
            override val title: String = "Flow",
            override val supportText: String = "How heavy the user's menstrual flow was. Optional field.",
            override val items: List<Pair<Int, String>> = listOf(
                MenstruationFlowRecord.FLOW_UNKNOWN to "UNKNOWN",
                MenstruationFlowRecord.FLOW_LIGHT to "LIGHT",
                MenstruationFlowRecord.FLOW_MEDIUM to "MEDIUM",
                MenstruationFlowRecord.FLOW_HEAVY to "HEAVY",
            ),
        ) : Type()

        data class Result(
            override val title: String = "Result of an ovulation test",
            override val supportText: String = "The result of a user's ovulation test, which shows if they're ovulating or not. Required field.",
            //TODO add extended description (from javadoc)
            override val items: List<Pair<Int, String>> = listOf(
                OvulationTestRecord.RESULT_POSITIVE to "POSITIVE",
                OvulationTestRecord.RESULT_HIGH to "HIGH",
                OvulationTestRecord.RESULT_NEGATIVE to "NEGATIVE",
                OvulationTestRecord.RESULT_INCONCLUSIVE to "INCONCLUSIVE"
            ),
        ) : Type()

        data class ProtectionUsed(
            override val title: String = "Protection",
            override val supportText: String = "Whether protection was used during sexual activity. Optional field.",
            override val items: List<Pair<Int, String>> = listOf(
                SexualActivityRecord.PROTECTION_USED_UNKNOWN to "UNKNOWN",
                SexualActivityRecord.PROTECTION_USED_PROTECTED to "PROTECTED",
                SexualActivityRecord.PROTECTION_USED_UNPROTECTED to "UNPROTECTED",
            ),
        ) : Type()

        data class Vo2MaxMeasurementMethod(
            override val title: String = "Measurement method",
            override val supportText: String = "VO2 max measurement method. Optional field.",
            override val items: List<Pair<Int, String>> = listOf(
                Vo2MaxRecord.MEASUREMENT_METHOD_OTHER to "OTHER",
                Vo2MaxRecord.MEASUREMENT_METHOD_METABOLIC_CART to "METABOLIC CART",
                Vo2MaxRecord.MEASUREMENT_METHOD_HEART_RATE_RATIO to "HEART RATE RATIO",
                Vo2MaxRecord.MEASUREMENT_METHOD_COOPER_TEST to "COOPER TEST",
                Vo2MaxRecord.MEASUREMENT_METHOD_MULTISTAGE_FITNESS_TEST to "MULTISTAGE FITNESS TEST",
                Vo2MaxRecord.MEASUREMENT_METHOD_ROCKPORT_FITNESS_TEST to "ROCKPORT FITNESS TEST",
            ),
        ) : Type()

        data class ActivityIntensityType(
            override val title: String = "Activity Intensity",
            override val supportText: String = "The intensity of the activity. Required field.",
            override val items: List<Pair<Int, String>> = listOf(
                ActivityIntensityRecord.ACTIVITY_INTENSITY_TYPE_MODERATE to "MODERATE",
                ActivityIntensityRecord.ACTIVITY_INTENSITY_TYPE_VIGOROUS to "VIGOROUS",
            ),
        ) : Type()

        data class MindfulnessSessionType(
            override val title: String = "Mindfulness Session Type",
            override val supportText: String = "The type of the mindfulness session. Required field.",
            override val items: List<Pair<Int, String>> = listOf(
                MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_UNKNOWN to "UNKNOWN",
                MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_BREATHING to "BREATHING",
                MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_MEDITATION to "MEDITATION",
                MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_MOVEMENT to "MOVEMENT",
                MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_MUSIC to "MUSIC",
                MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_UNGUIDED to "UNGUIDED",
            ),
        ) : Type()

        data class ExerciseType(
            override val title: String = "Exercise Type",
            override val supportText: String = "The type of the exercise session. Required field.",
            override val items: List<Pair<Int, String>> = listOf(
                ExerciseSessionRecord.EXERCISE_TYPE_BADMINTON to "BADMINTON",
                ExerciseSessionRecord.EXERCISE_TYPE_BASEBALL to "BASEBALL",
                ExerciseSessionRecord.EXERCISE_TYPE_BASKETBALL to "BASKETBALL",
                ExerciseSessionRecord.EXERCISE_TYPE_BIKING to "BIKING",
                ExerciseSessionRecord.EXERCISE_TYPE_BIKING_STATIONARY to "BIKING_STATIONARY",
                ExerciseSessionRecord.EXERCISE_TYPE_BOOT_CAMP to "BOOT_CAMP",
                ExerciseSessionRecord.EXERCISE_TYPE_BOXING to "BOXING",
                ExerciseSessionRecord.EXERCISE_TYPE_CALISTHENICS to "CALISTHENICS",
                ExerciseSessionRecord.EXERCISE_TYPE_CRICKET to "CRICKET",
                ExerciseSessionRecord.EXERCISE_TYPE_DANCING to "DANCING",
                ExerciseSessionRecord.EXERCISE_TYPE_ELLIPTICAL to "ELLIPTICAL",
                ExerciseSessionRecord.EXERCISE_TYPE_EXERCISE_CLASS to "EXERCISE_CLASS",
                ExerciseSessionRecord.EXERCISE_TYPE_FENCING to "FENCING",
                ExerciseSessionRecord.EXERCISE_TYPE_FOOTBALL_AMERICAN to "FOOTBALL_AMERICAN",
                ExerciseSessionRecord.EXERCISE_TYPE_FOOTBALL_AUSTRALIAN to "FOOTBALL_AUSTRALIAN",
                ExerciseSessionRecord.EXERCISE_TYPE_FRISBEE_DISC to "FRISBEE_DISC",
                ExerciseSessionRecord.EXERCISE_TYPE_GOLF to "GOLF",
                ExerciseSessionRecord.EXERCISE_TYPE_GUIDED_BREATHING to "GUIDED_BREATHING",
                ExerciseSessionRecord.EXERCISE_TYPE_GYMNASTICS to "GYMNASTICS",
                ExerciseSessionRecord.EXERCISE_TYPE_HANDBALL to "HANDBALL",
                ExerciseSessionRecord.EXERCISE_TYPE_HIGH_INTENSITY_INTERVAL_TRAINING to "HIIT",
                ExerciseSessionRecord.EXERCISE_TYPE_HIKING to "HIKING",
                ExerciseSessionRecord.EXERCISE_TYPE_ICE_HOCKEY to "ICE_HOCKEY",
                ExerciseSessionRecord.EXERCISE_TYPE_ICE_SKATING to "ICE_SKATING",
                ExerciseSessionRecord.EXERCISE_TYPE_MARTIAL_ARTS to "MARTIAL_ARTS",
                ExerciseSessionRecord.EXERCISE_TYPE_PADDLING to "PADDLING",
                ExerciseSessionRecord.EXERCISE_TYPE_PARAGLIDING to "PARAGLIDING",
                ExerciseSessionRecord.EXERCISE_TYPE_PILATES to "PILATES",
                ExerciseSessionRecord.EXERCISE_TYPE_RACQUETBALL to "RACQUETBALL",
                ExerciseSessionRecord.EXERCISE_TYPE_ROCK_CLIMBING to "ROCK_CLIMBING",
                ExerciseSessionRecord.EXERCISE_TYPE_ROLLER_HOCKEY to "ROLLER_HOCKEY",
                ExerciseSessionRecord.EXERCISE_TYPE_ROWING to "ROWING",
                ExerciseSessionRecord.EXERCISE_TYPE_ROWING_MACHINE to "ROWING_MACHINE",
                ExerciseSessionRecord.EXERCISE_TYPE_RUGBY to "RUGBY",
                ExerciseSessionRecord.EXERCISE_TYPE_RUNNING to "RUNNING",
                ExerciseSessionRecord.EXERCISE_TYPE_RUNNING_TREADMILL to "RUNNING_TREADMILL",
                ExerciseSessionRecord.EXERCISE_TYPE_SAILING to "SAILING",
                ExerciseSessionRecord.EXERCISE_TYPE_SCUBA_DIVING to "SCUBA_DIVING",
                ExerciseSessionRecord.EXERCISE_TYPE_SKATING to "SKATING",
                ExerciseSessionRecord.EXERCISE_TYPE_SKIING to "SKIING",
                ExerciseSessionRecord.EXERCISE_TYPE_SNOWBOARDING to "SNOWBOARDING",
                ExerciseSessionRecord.EXERCISE_TYPE_SNOWSHOEING to "SNOWSHOEING",
                ExerciseSessionRecord.EXERCISE_TYPE_SOCCER to "SOCCER",
                ExerciseSessionRecord.EXERCISE_TYPE_SOFTBALL to "SOFTBALL",
                ExerciseSessionRecord.EXERCISE_TYPE_SQUASH to "SQUASH",
                ExerciseSessionRecord.EXERCISE_TYPE_STAIR_CLIMBING to "STAIR_CLIMBING",
                ExerciseSessionRecord.EXERCISE_TYPE_STAIR_CLIMBING_MACHINE to "STAIR_CLIMBING_MACHINE",
                ExerciseSessionRecord.EXERCISE_TYPE_STRENGTH_TRAINING to "STRENGTH_TRAINING",
                ExerciseSessionRecord.EXERCISE_TYPE_STRETCHING to "STRETCHING",
                ExerciseSessionRecord.EXERCISE_TYPE_SURFING to "SURFING",
                ExerciseSessionRecord.EXERCISE_TYPE_SWIMMING_OPEN_WATER to "SWIMMING_OPEN_WATER",
                ExerciseSessionRecord.EXERCISE_TYPE_SWIMMING_POOL to "SWIMMING_POOL",
                ExerciseSessionRecord.EXERCISE_TYPE_TABLE_TENNIS to "TABLE_TENNIS",
                ExerciseSessionRecord.EXERCISE_TYPE_TENNIS to "TENNIS",
                ExerciseSessionRecord.EXERCISE_TYPE_VOLLEYBALL to "VOLLEYBALL",
                ExerciseSessionRecord.EXERCISE_TYPE_WALKING to "WALKING",
                ExerciseSessionRecord.EXERCISE_TYPE_WATER_POLO to "WATER_POLO",
                ExerciseSessionRecord.EXERCISE_TYPE_WEIGHTLIFTING to "WEIGHTLIFTING",
                ExerciseSessionRecord.EXERCISE_TYPE_WHEELCHAIR to "WHEELCHAIR",
                ExerciseSessionRecord.EXERCISE_TYPE_OTHER_WORKOUT to "OTHER_WORKOUT",
                ExerciseSessionRecord.EXERCISE_TYPE_YOGA to "YOGA",
            ),
        ) : Type()

        data class ExerciseSegmentType(
            override val title: String = "Segment Type",
            override val supportText: String = "The type of the exercise segment. Required field.",
            override val items: List<Pair<Int, String>> = listOf(
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_UNKNOWN to "UNKNOWN",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_ARM_CURL to "ARM_CURL",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BACK_EXTENSION to "BACK_EXTENSION",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BALL_SLAM to "BALL_SLAM",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BARBELL_SHOULDER_PRESS to "BARBELL_SHOULDER_PRESS",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BENCH_PRESS to "BENCH_PRESS",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BENCH_SIT_UP to "BENCH_SIT_UP",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BIKING to "BIKING",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BIKING_STATIONARY to "BIKING_STATIONARY",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_BURPEE to "BURPEE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_CRUNCH to "CRUNCH",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DEADLIFT to "DEADLIFT",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DOUBLE_ARM_TRICEPS_EXTENSION to "DOUBLE_ARM_TRICEPS_EXTENSION",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_CURL_LEFT_ARM to "DUMBBELL_CURL_LEFT_ARM",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_CURL_RIGHT_ARM to "DUMBBELL_CURL_RIGHT_ARM",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_FRONT_RAISE to "DUMBBELL_FRONT_RAISE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_LATERAL_RAISE to "DUMBBELL_LATERAL_RAISE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_ROW to "DUMBBELL_ROW",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_TRICEPS_EXTENSION_LEFT_ARM to "DUMBBELL_TRICEPS_EXTENSION_LEFT_ARM",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_TRICEPS_EXTENSION_RIGHT_ARM to "DUMBBELL_TRICEPS_EXTENSION_RIGHT_ARM",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_TRICEPS_EXTENSION_TWO_ARM to "DUMBBELL_TRICEPS_EXTENSION_TWO_ARM",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_ELLIPTICAL to "ELLIPTICAL",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_FORWARD_TWIST to "FORWARD_TWIST",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_FRONT_RAISE to "FRONT_RAISE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_HIGH_INTENSITY_INTERVAL_TRAINING to "HIIT",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_HIP_THRUST to "HIP_THRUST",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_HULA_HOOP to "HULA_HOOP",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_JUMPING_JACK to "JUMPING_JACK",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_JUMP_ROPE to "JUMP_ROPE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_KETTLEBELL_SWING to "KETTLEBELL_SWING",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LATERAL_RAISE to "LATERAL_RAISE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LAT_PULL_DOWN to "LAT_PULL_DOWN",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LEG_CURL to "LEG_CURL",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LEG_EXTENSION to "LEG_EXTENSION",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LEG_PRESS to "LEG_PRESS",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LEG_RAISE to "LEG_RAISE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_LUNGE to "LUNGE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_MOUNTAIN_CLIMBER to "MOUNTAIN_CLIMBER",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_OTHER_WORKOUT to "OTHER_WORKOUT",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_PAUSE to "PAUSE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_PILATES to "PILATES",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_PLANK to "PLANK",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_PULL_UP to "PULL_UP",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_PUNCH to "PUNCH",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_REST to "REST",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_ROWING_MACHINE to "ROWING_MACHINE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_RUNNING to "RUNNING",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_RUNNING_TREADMILL to "RUNNING_TREADMILL",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SHOULDER_PRESS to "SHOULDER_PRESS",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SINGLE_ARM_TRICEPS_EXTENSION to "SINGLE_ARM_TRICEPS_EXTENSION",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SIT_UP to "SIT_UP",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SQUAT to "SQUAT",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_STAIR_CLIMBING to "STAIR_CLIMBING",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_STAIR_CLIMBING_MACHINE to "STAIR_CLIMBING_MACHINE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_STRETCHING to "STRETCHING",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_BACKSTROKE to "SWIMMING_BACKSTROKE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_BREASTSTROKE to "SWIMMING_BREASTSTROKE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_BUTTERFLY to "SWIMMING_BUTTERFLY",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_FREESTYLE to "SWIMMING_FREESTYLE",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_MIXED to "SWIMMING_MIXED",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_OPEN_WATER to "SWIMMING_OPEN_WATER",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_OTHER to "SWIMMING_OTHER",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_POOL to "SWIMMING_POOL",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_UPPER_TWIST to "UPPER_TWIST",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_WALKING to "WALKING",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_WEIGHTLIFTING to "WEIGHTLIFTING",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_WHEELCHAIR to "WHEELCHAIR",
                ExerciseSegment.EXERCISE_SEGMENT_TYPE_YOGA to "YOGA",
            ),
        ) : Type()

        data class ExercisePhase(
            override val title: String = "Exercise Phase",
            override val supportText: String = "The phase of the exercise step. Required field.",
            override val items: List<Pair<Int, String>> = listOf(
                PlannedExerciseStep.EXERCISE_PHASE_UNKNOWN to "UNKNOWN",
                PlannedExerciseStep.EXERCISE_PHASE_WARMUP to "WARMUP",
                PlannedExerciseStep.EXERCISE_PHASE_ACTIVE to "ACTIVE",
                PlannedExerciseStep.EXERCISE_PHASE_REST to "REST",
                PlannedExerciseStep.EXERCISE_PHASE_COOLDOWN to "COOLDOWN",
            ),
        ) : Type()

        data class RecordingMethod(
            override val title: String = "Recording Method",
            override val supportText: String = "How the data was recorded. Optional field.",
            override val items: List<Pair<Int, String>> = listOf(
                Metadata.Companion.RECORDING_METHOD_UNKNOWN to "UNKNOWN",
                Metadata.Companion.RECORDING_METHOD_ACTIVELY_RECORDED to "ACTIVELY RECORDED",
                Metadata.Companion.RECORDING_METHOD_AUTOMATICALLY_RECORDED to "AUTOMATICALLY RECORDED",
                Metadata.Companion.RECORDING_METHOD_MANUAL_ENTRY to "MANUAL ENTRY",
            ),
        ) : Type()

        data class SkinTemperatureMeasurementLocation(
            override val title: String = "Measurement Location",
            override val supportText: String = "Location on the body where the skin temperature was measured.",
            override val items: List<Pair<Int, String>> = listOf(
                SkinTemperatureRecord.MEASUREMENT_LOCATION_UNKNOWN to "UNKNOWN",
                SkinTemperatureRecord.MEASUREMENT_LOCATION_FINGER to "FINGER",
                SkinTemperatureRecord.MEASUREMENT_LOCATION_TOE to "TOE",
                SkinTemperatureRecord.MEASUREMENT_LOCATION_WRIST to "WRIST",
            ),
        ) : Type()

        data class SleepStageType(
            override val title: String = "Sleep Stage",
            override val supportText: String = "Type of sleep stage.",
            override val items: List<Pair<Int, String>> = listOf(
                SleepSessionRecord.STAGE_TYPE_UNKNOWN to "UNKNOWN",
                SleepSessionRecord.STAGE_TYPE_AWAKE to "AWAKE",
                SleepSessionRecord.STAGE_TYPE_SLEEPING to "SLEEPING",
                SleepSessionRecord.STAGE_TYPE_OUT_OF_BED to "OUT OF BED",
                SleepSessionRecord.STAGE_TYPE_LIGHT to "LIGHT",
                SleepSessionRecord.STAGE_TYPE_DEEP to "DEEP",
                SleepSessionRecord.STAGE_TYPE_REM to "REM",
                SleepSessionRecord.STAGE_TYPE_AWAKE_IN_BED to "AWAKE IN BED",
            ),
        ) : Type()

        companion object {
            private val MEAL_TYPES = listOf(
                androidx.health.connect.client.records.MealType.MEAL_TYPE_UNKNOWN to "UNKNOWN",
                androidx.health.connect.client.records.MealType.MEAL_TYPE_BREAKFAST to "BREAKFAST",
                androidx.health.connect.client.records.MealType.MEAL_TYPE_LUNCH to "LUNCH",
                androidx.health.connect.client.records.MealType.MEAL_TYPE_DINNER to "DINNER",
                androidx.health.connect.client.records.MealType.MEAL_TYPE_SNACK to "SNACK",
            )
        }
    }
}
