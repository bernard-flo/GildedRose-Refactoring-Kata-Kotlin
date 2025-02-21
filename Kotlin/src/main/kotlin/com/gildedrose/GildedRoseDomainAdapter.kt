package com.gildedrose

import com.gildedrose.domain.*
import com.gildedrose.domain.Item as DomainItem


object GildedRoseDomainAdapter {

    fun updateQuality(items: List<Item>) {

        items
            .map { itemToDomainItem(it) }
            .map { it.next() }
            .forEachIndexed { index, item ->
                items[index].sellIn = item.sellIn.value
                items[index].quality = item.quality.value
            }
    }

}


private fun itemToDomainItem(item: Item): DomainItem {

    return when (item.name) {

        "Aged Brie" -> AgedBrie(
            SellIn(item.sellIn),
            Quality.Normal(item.quality),
        )

        "Sulfuras, Hand of Ragnaros" -> Sulfuras(
            SellIn(item.sellIn),
        )

        "Backstage passes to a TAFKAL80ETC concert" -> BackstagePasses(
            SellIn(item.sellIn),
            Quality.Normal(item.quality),
        )

        else -> NormalItem(
            SellIn(item.sellIn),
            Quality.Normal(item.quality),
        )
    }
}
