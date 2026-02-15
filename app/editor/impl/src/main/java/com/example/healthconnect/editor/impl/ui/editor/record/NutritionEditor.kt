package com.example.healthconnect.editor.impl.ui.editor.record

import androidx.health.connect.client.records.MealType
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.units.Energy
import androidx.health.connect.client.units.Mass
import androidx.health.connect.client.units.grams
import androidx.health.connect.client.units.kilocalories
import com.example.healthconnect.components.api.domain.entity.field.atomic.SelectorField
import com.example.healthconnect.components.api.domain.entity.field.atomic.StringField
import com.example.healthconnect.components.api.domain.entity.field.atomic.TimeField
import com.example.healthconnect.components.api.domain.entity.field.atomic.ValueField
import com.example.healthconnect.editor.api.domain.record.Nutrition
import com.example.healthconnect.editor.api.ui.mapper.MetadataMapper
import java.time.Instant
import java.time.ZoneOffset

class NutritionEditor() : Editor<NutritionRecord, Nutrition>() {

    override fun toModel(
        record: NutritionRecord,
        mapper: MetadataMapper,
    ): Nutrition = Nutrition(
        time = TimeField.Interval(
            startTime = record.startTime,
            startZoneOffset = record.startZoneOffset,
            endTime = record.endTime,
            endZoneOffset = record.endZoneOffset
        ),
        metadata = mapper.toEntity(record.metadata),
        name = StringField(
            value = record.name ?: "",
            type = StringField.Type.NutritionName()
        ),
        mealType = SelectorField(
            value = record.mealType,
            type = SelectorField.Type.MealType()
        ),
        energy = record.energy.toValueField("Energy"),
        energyFromFat = record.energyFromFat.toValueField("Energy from fat"),
        totalFat = record.totalFat.toValueField("Total Fat"),
        saturatedFat = record.saturatedFat.toValueField("Saturated Fat"),
        transFat = record.transFat.toValueField("Trans Fat"),
        polyunsaturatedFat = record.polyunsaturatedFat.toValueField("Polyunsaturated Fat"),
        monounsaturatedFat = record.monounsaturatedFat.toValueField("Monounsaturated Fat"),
        unsaturatedFat = record.unsaturatedFat.toValueField("Unsaturated Fat"),
        cholesterol = record.cholesterol.toValueField("Cholesterol"),
        sodium = record.sodium.toValueField("Sodium"),
        totalCarbohydrate = record.totalCarbohydrate.toValueField("Total Carbohydrate"),
        dietaryFiber = record.dietaryFiber.toValueField("Dietary Fiber"),
        sugar = record.sugar.toValueField("Sugar"),
        protein = record.protein.toValueField("Protein"),
        vitaminA = record.vitaminA.toValueField("Vitamin A"),
        vitaminB6 = record.vitaminB6.toValueField("Vitamin B6"),
        vitaminB12 = record.vitaminB12.toValueField("Vitamin B12"),
        vitaminC = record.vitaminC.toValueField("Vitamin C"),
        vitaminD = record.vitaminD.toValueField("Vitamin D"),
        vitaminE = record.vitaminE.toValueField("Vitamin E"),
        vitaminK = record.vitaminK.toValueField("Vitamin K"),
        calcium = record.calcium.toValueField("Calcium"),
        iron = record.iron.toValueField("Iron"),
        potassium = record.potassium.toValueField("Potassium"),
        pantothenicAcid = record.pantothenicAcid.toValueField("Pantothenic Acid"),
        niacin = record.niacin.toValueField("Niacin"),
        biotin = record.biotin.toValueField("Biotin"),
        riboflavin = record.riboflavin.toValueField("Riboflavin"),
        thiamin = record.thiamin.toValueField("Thiamin"),
        folate = record.folate.toValueField("Folate"),
        folicAcid = record.folicAcid.toValueField("Folic Acid"),
        iodine = record.iodine.toValueField("Iodine"),
        magnesium = record.magnesium.toValueField("Magnesium"),
        manganese = record.manganese.toValueField("Manganese"),
        phosphorus = record.phosphorus.toValueField("Phosphorus"),
        selenium = record.selenium.toValueField("Selenium"),
        zinc = record.zinc.toValueField("Zinc"),
        chloride = record.chloride.toValueField("Chloride"),
        chromium = record.chromium.toValueField("Chromium"),
        copper = record.copper.toValueField("Copper"),
        molybdenum = record.molybdenum.toValueField("Molybdenum"),
        caffeine = record.caffeine.toValueField("Caffeine"),
    )

    override fun toRecord(
        validModel: Nutrition,
        mapper: MetadataMapper,
    ): NutritionRecord = NutritionRecord(
        startTime = validModel.getStartValidTime().instant,
        startZoneOffset = validModel.getStartValidTime().zoneOffset,
        endTime = validModel.getEndValidTime().instant,
        endZoneOffset = validModel.getEndValidTime().zoneOffset,
        metadata = mapper.toLibMetadata(validModel.metadata),
        name = validModel.name.value.ifBlank { null },
        mealType = validModel.mealType.value,
        energy = validModel.energy.toEnergy(),
        energyFromFat = validModel.energyFromFat.toEnergy(),
        totalFat = validModel.totalFat.toMass(),
        saturatedFat = validModel.saturatedFat.toMass(),
        transFat = validModel.transFat.toMass(),
        polyunsaturatedFat = validModel.polyunsaturatedFat.toMass(),
        monounsaturatedFat = validModel.monounsaturatedFat.toMass(),
        unsaturatedFat = validModel.unsaturatedFat.toMass(),
        cholesterol = validModel.cholesterol.toMass(),
        sodium = validModel.sodium.toMass(),
        totalCarbohydrate = validModel.totalCarbohydrate.toMass(),
        dietaryFiber = validModel.dietaryFiber.toMass(),
        sugar = validModel.sugar.toMass(),
        protein = validModel.protein.toMass(),
        vitaminA = validModel.vitaminA.toMass(),
        vitaminB6 = validModel.vitaminB6.toMass(),
        vitaminB12 = validModel.vitaminB12.toMass(),
        vitaminC = validModel.vitaminC.toMass(),
        vitaminD = validModel.vitaminD.toMass(),
        vitaminE = validModel.vitaminE.toMass(),
        vitaminK = validModel.vitaminK.toMass(),
        calcium = validModel.calcium.toMass(),
        iron = validModel.iron.toMass(),
        potassium = validModel.potassium.toMass(),
        pantothenicAcid = validModel.pantothenicAcid.toMass(),
        niacin = validModel.niacin.toMass(),
        biotin = validModel.biotin.toMass(),
        riboflavin = validModel.riboflavin.toMass(),
        thiamin = validModel.thiamin.toMass(),
        folate = validModel.folate.toMass(),
        folicAcid = validModel.folicAcid.toMass(),
        iodine = validModel.iodine.toMass(),
        magnesium = validModel.magnesium.toMass(),
        manganese = validModel.manganese.toMass(),
        phosphorus = validModel.phosphorus.toMass(),
        selenium = validModel.selenium.toMass(),
        zinc = validModel.zinc.toMass(),
        chloride = validModel.chloride.toMass(),
        chromium = validModel.chromium.toMass(),
        copper = validModel.copper.toMass(),
        molybdenum = validModel.molybdenum.toMass(),
        caffeine = validModel.caffeine.toMass(),
    )

    private fun Mass?.toValueField(label: String): ValueField = ValueField.Dbl(
        parsedValue = this?.inGrams ?: 0.0,
        type = ValueField.Type.NutritionMass(label),
    )

    private fun Energy?.toValueField(label: String): ValueField = ValueField.Dbl(
        parsedValue = this?.inKilocalories ?: 0.0,
        type = if (label == "Energy") ValueField.Type.NutritionEnergy() else ValueField.Type.NutritionEnergy(label, "$label in Energy unit. Optional field."),
    )

    private fun ValueField.toMass(): Mass? = (this as ValueField.Dbl).parsedValue?.takeIf { it > 0 }?.grams

    private fun ValueField.toEnergy(): Energy? = (this as ValueField.Dbl).parsedValue?.takeIf { it > 0 }?.kilocalories

    override fun createDefault(): NutritionRecord {
        val instant = Instant.now()
        return NutritionRecord(
            startTime = instant.minusSeconds(600),
            startZoneOffset = ZoneOffset.UTC,
            endTime = instant,
            endZoneOffset = ZoneOffset.UTC,
            metadata = Metadata.unknownRecordingMethod(),
            mealType = MealType.MEAL_TYPE_UNKNOWN,
        )
    }
}
