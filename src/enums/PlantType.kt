package enums

import harvesting.PlantSpec

enum class PlantType(private val spec: PlantSpec) {
    OAT(
        PlantSpec(
            1200000, // 1.2 mio
            300,
            90,
            listOf(6), // 2nd march
            listOf(13,14,15,16), // july, august
            listOf(7,8,9), // first 3 ticks after sowing
            emptyList(),
            emptyList(),
            emptyList(),
            false,
            mapOf(
                Action.HARVESTING to Pair(1, 20.0),
                Action.HARVESTING to Pair(2, 20.0)
            ) // same as WHEAT
        )
    ),
    PUMPKIN(
        PlantSpec(
            500000, // 500k
            600,
            120,
            listOf(9,10,11), // 2nd May until June
            listOf(17,18), // september
            emptyList(), // every 2nd tick after sowing
            emptyList(), // 2 ticks after sowing, lasts for 2
            emptyList(),
            emptyList(),
            true,
            emptyMap() // ded
        )
    ),
    POTATO(
        PlantSpec(
            1000000, // one mio g
            500,
            130,
            listOf(7,8,9,10), // april or may
            listOf(17,18,19,20), // sept or oct
            emptyList(), // every 2nd tick after SOWING
            emptyList(), // 3 ticks after sowing for one tick but that's irrelevant
            emptyList(),
            emptyList(),
            false, // can be but doesn't influence harvest like what?
            emptyMap() // ded
        )
    ),
    WHEAT(
        PlantSpec(
            1500000, // 1.5 mio g
            450,
            90,
            listOf(19,20), // october
            listOf(11,12,13), // june or first of july
            emptyList(), // 3 and 9 ticks after sowing
            emptyList(), // 1st tick of may but self-pollinating
            emptyList(),
            emptyList(),
            false,
            mapOf(
                Action.HARVESTING to Pair(1, 20.0),
                Action.HARVESTING to Pair(2, 20.0)
            )
        )
    ),
    APPLE(
        PlantSpec(
            1700000, //1.7 mio
            100,
            50,
            emptyList(),
            listOf(17,18,19), // sept or early oct
            emptyList(),
            listOf(8,9), // 2 april 1 may
            listOf(21,22,3,4), // nov and feb after
            listOf(11,17), // 1 june, 1 sept
            true,
            mapOf(
                Action.HARVESTING to Pair(1, 50.0)
            )
        )
    ),
    ALMOND(
        PlantSpec(
            800000,
            400,
            130,
            emptyList(),
            listOf(16,17,18,19), // 2nd august until 1st oct
            emptyList(),
            listOf(4,5), // 2nd feb until 1st march
            listOf(21,22,3,4), // nov or subsequent feb
            listOf(11,17), // early june, early sept
            true,
            mapOf(
                Action.HARVESTING to Pair(1, 10.0)
            )
        )
    ),
    CHERRY(
        PlantSpec(
            1200000, // 1.2 mio
            150,
            120,
            emptyList(),
            listOf(13,14), // july
            emptyList(),
            listOf(8,9), // 2nd april and 1st may
            listOf(21,22,3,4), // nov or subseq feb
            listOf(11), // early june
            true,
            mapOf(
                Action.HARVESTING to Pair(1, 30.0)
            )
        )
    ),
    GRAPE(
        PlantSpec(
            1200000, // 1.2 mio
            250,
            150,
            emptyList(),
            listOf(17), // 1st sept
            emptyList(),
            listOf(12, 13), // self polli 2nd june, 1st july
            listOf(14,15,16), // 2nd july or august
            listOf(7,13), // early april, early july
            false,
            mapOf(
                Action.HARVESTING to Pair(1, 5.0),
                Action.HARVESTING to Pair(2, 5.0),
                Action.HARVESTING to Pair(3, 5.0)
            )
        )
    );

    fun getSpec() = spec
}