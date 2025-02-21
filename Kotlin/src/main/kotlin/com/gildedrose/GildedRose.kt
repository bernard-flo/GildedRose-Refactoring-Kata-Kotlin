package com.gildedrose

class GildedRose(var items: List<Item>) {

    fun updateQuality() {

        GildedRoseDomainAdapter.updateQuality(items)
    }

}
