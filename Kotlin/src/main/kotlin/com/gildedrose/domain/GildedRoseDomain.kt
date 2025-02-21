package com.gildedrose.domain


sealed interface Item {

    val sellIn: SellIn
    val quality: Quality

    fun next(): Item

}


data class NormalItem(
    override val sellIn: SellIn,
    override val quality: Quality.Normal,
) : Item {

    companion object {

        private val qualityChangeBeforeSellByDate = QualityChange.WithValue.Decrease.Normal
        private val qualityChangeAfterSellByDate = qualityChangeBeforeSellByDate.twice()
    }

    override fun next(): NormalItem {

        val nextSellIn = sellIn.decrease()

        val nextQuality = when (sellIn.sellByDateHasPassed()) {
            false -> quality.withChange(qualityChangeBeforeSellByDate)
            true -> quality.withChange(qualityChangeAfterSellByDate)
        }

        return NormalItem(nextSellIn, nextQuality)
    }

}


data class AgedBrie(
    override val sellIn: SellIn,
    override val quality: Quality.Normal,
) : Item {

    companion object {

        private val qualityChangeBeforeSellByDate = QualityChange.WithValue.Increase(1)
        private val qualityChangeAfterSellByDate = qualityChangeBeforeSellByDate.twice()
    }

    override fun next(): AgedBrie {

        val nextSellIn = sellIn.decrease()

        val nextQuality = when (sellIn.sellByDateHasPassed()) {
            false -> quality.withChange(qualityChangeBeforeSellByDate)
            true -> quality.withChange(qualityChangeAfterSellByDate)
        }

        return AgedBrie(nextSellIn, nextQuality)
    }

}


data class Sulfuras(
    override val sellIn: SellIn,
) : Item {

    override val quality = Quality.Legendary

    override fun next(): Sulfuras {

        return this
    }

}


data class BackstagePasses(
    override val sellIn: SellIn,
    override val quality: Quality.Normal,
) : Item {

    companion object {

        private val qualityChangeIncrease1 = QualityChange.WithValue.Increase(1)
        private val qualityChangeIncrease2 = QualityChange.WithValue.Increase(2)
        private val qualityChangeIncrease3 = QualityChange.WithValue.Increase(3)
    }

    override fun next(): BackstagePasses {

        val nextSellIn = sellIn.decrease()

        val nextQuality = when {
            sellIn.value > 10 -> quality.withChange(qualityChangeIncrease1)
            sellIn.value > 5 -> quality.withChange(qualityChangeIncrease2)
            sellIn.value > 0 -> quality.withChange(qualityChangeIncrease3)
            else -> quality.withChange(QualityChange.ToZero)
        }

        return BackstagePasses(nextSellIn, nextQuality)
    }

}


sealed interface Quality {

    val value: Int

    data class Normal(
        override val value: Int,
    ) : Quality {

        private val min = 0
        private val max = 50

        init {

            require(value in min..max)
        }

        fun withChange(qualityChange: QualityChange): Normal {

            return when (qualityChange) {
                is QualityChange.WithValue -> Normal((value - qualityChange.value).coerceIn(min, max))
                is QualityChange.ToZero -> Normal(0)
            }
        }

    }

    data object Legendary : Quality {

        override val value = 80
    }

}


sealed interface QualityChange {

    open class WithValue
    private constructor(
        val value: Int,
    ) : QualityChange {

        fun twice(): WithValue {

            return WithValue(value * 2)
        }

        open class Decrease(
            decreaseValue: Int,
        ) : WithValue(decreaseValue) {

            object Normal : Decrease(1)

        }

        class Increase(
            increaseValue: Int,
        ) : WithValue(increaseValue * -1)

    }

    data object ToZero : QualityChange

}


data class SellIn(
    val value: Int,
) {

    fun decrease(): SellIn {

        return SellIn(value - 1)
    }

    fun sellByDateHasPassed(): Boolean {

        return value <= 0
    }

}
