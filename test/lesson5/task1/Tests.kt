package lesson5.task1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class Tests {
    @Test
    @Tag("Example")
    fun shoppingListCostTest() {
        val itemCosts = mapOf(
            "Хлеб" to 50.0,
            "Молоко" to 100.0
        )
        assertEquals(
            150.0,
            shoppingListCost(
                listOf("Хлеб", "Молоко"),
                itemCosts
            )
        )
        assertEquals(
            150.0,
            shoppingListCost(
                listOf("Хлеб", "Молоко", "Кефир"),
                itemCosts
            )
        )
        assertEquals(
            0.0,
            shoppingListCost(
                listOf("Хлеб", "Молоко", "Кефир"),
                mapOf()
            )
        )
    }

    @Test
    @Tag("Example")
    fun filterByCountryCode() {
        val phoneBook = mutableMapOf(
            "Quagmire" to "+1-800-555-0143",
            "Adam's Ribs" to "+82-000-555-2960",
            "Pharmakon Industries" to "+1-800-555-6321"
        )

        filterByCountryCode(phoneBook, "+1")
        assertEquals(2, phoneBook.size)

        filterByCountryCode(phoneBook, "+1")
        assertEquals(2, phoneBook.size)

        filterByCountryCode(phoneBook, "+999")
        assertEquals(0, phoneBook.size)
    }

    @Test
    @Tag("Example")
    fun removeFillerWords() {
        assertEquals(
            "Я люблю Котлин".split(" "),
            removeFillerWords(
                "Я как-то люблю Котлин".split(" "),
                "как-то"
            )
        )
        assertEquals(
            "Я люблю Котлин".split(" "),
            removeFillerWords(
                "Я как-то люблю таки Котлин".split(" "),
                "как-то",
                "таки"
            )
        )
        assertEquals(
            "Я люблю Котлин".split(" "),
            removeFillerWords(
                "Я люблю Котлин".split(" "),
                "как-то",
                "таки"
            )
        )
    }

    @Test
    @Tag("Example")
    fun buildWordSet() {
        assertEquals(
            mutableSetOf("Я", "люблю", "Котлин"),
            buildWordSet("Я люблю Котлин".split(" "))
        )
        assertEquals(
            mutableSetOf("Котлин", "люблю", "Я"),
            buildWordSet("Я люблю люблю Котлин".split(" "))
        )
        assertEquals(
            mutableSetOf<String>(),
            buildWordSet(listOf())
        )
    }

    @Test
    @Tag("2")
    fun buildGrades() {
        assertEquals(
            mapOf<Int, List<String>>(),
            buildGrades(mapOf())
        )
        assertEquals(
            mapOf(5 to listOf("Михаил", "Семён"), 3 to listOf("Марат")),
            buildGrades(mapOf("Марат" to 3, "Семён" to 5, "Михаил" to 5))
                .mapValues { (_, v) -> v.sorted() }
        )
        assertEquals(
            mapOf(3 to listOf("Марат", "Михаил", "Семён")),
            buildGrades(mapOf("Марат" to 3, "Семён" to 3, "Михаил" to 3))
                .mapValues { (_, v) -> v.sorted() }
        )
    }

    @Test
    @Tag("2")
    fun containsIn() {
        assertTrue(containsIn(mapOf("a" to "z"), mapOf("a" to "z", "b" to "sweet")))
        assertFalse(containsIn(mapOf("a" to "z"), mapOf("a" to "zee", "b" to "sweet")))
    }

    @Test
    @Tag("2")
    fun subtractOf() {
        val from = mutableMapOf("a" to "z", "b" to "c")

        subtractOf(from, mapOf())
        assertEquals(mapOf("a" to "z", "b" to "c"), from)

        subtractOf(from, mapOf("b" to "z"))
        assertEquals(mapOf("a" to "z", "b" to "c"), from)

        subtractOf(from, mapOf("a" to "z"))
        assertEquals(mapOf("b" to "c"), from)
    }

    @Test
    @Tag("2")
    fun whoAreInBoth() {
        assertEquals(
            emptyList<String>(),
            whoAreInBoth(emptyList(), emptyList())
        )

        assertEquals(
            listOf(""),
            whoAreInBoth(listOf("", ""), listOf(""))
        )

        assertEquals(
            listOf("Marat"),
            whoAreInBoth(listOf("Marat", "Mikhail"), listOf("Marat", "Kirill"))
        )
        assertEquals(
            emptyList<String>(),
            whoAreInBoth(listOf("Marat", "Mikhail"), listOf("Sveta", "Kirill"))
        )
    }

    @Test
    @Tag("3")
    fun mergePhoneBooks() {
        assertEquals(
            mapOf("Emergency" to "112"),
            mergePhoneBooks(
                mapOf("Emergency" to "112"),
                mapOf("Emergency" to "112")
            )
        )

        assertEquals(
            mapOf("" to "O\\\"oId~s&4\\\\!Gqgs;U{AA=3~\\\"%8;hfnPJp?N\\\"`z=C3}vSrkg>k<\\\",G%\\tx'dP7GkA@t^/!|u>i\$U{dTCV{=c@^|\\tJO6pP!=H0yi+pC.7(]V@A]AZJp+n3, "),
            mergePhoneBooks(
                mapOf("" to "O\\\"oId~s&4\\\\!Gqgs;U{AA=3~\\\"%8;hfnPJp?N\\\"`z=C3}vSrkg>k<\\\",G%\\tx'dP7GkA@t^/!|u>i\$U{dTCV{=c@^|\\tJO6pP!=H0yi+pC.7(]V@A]AZJp+n3"),
                mapOf("" to "")
            )
        )

        assertEquals(
            mapOf("Emergency" to "112", "Police" to "02"),
            mergePhoneBooks(
                mapOf("Emergency" to "112"),
                mapOf("Emergency" to "112", "Police" to "02")
            )
        )
        assertEquals(
            mapOf("Emergency" to "112, 911", "Police" to "02"),
            mergePhoneBooks(
                mapOf("Emergency" to "112"),
                mapOf("Emergency" to "911", "Police" to "02")
            )
        )
        assertEquals(
            mapOf("Emergency" to "112, 911", "Fire department" to "01", "Police" to "02"),
            mergePhoneBooks(
                mapOf("Emergency" to "112", "Fire department" to "01"),
                mapOf("Emergency" to "911", "Police" to "02")
            )
        )
    }

    @Test
    @Tag("4")
    fun averageStockPrice() {
        assertEquals(
            mapOf<String, Double>(),
            averageStockPrice(listOf())
        )
        assertEquals(
            mapOf("MSFT" to 100.0, "NFLX" to 40.0),
            averageStockPrice(listOf("MSFT" to 100.0, "NFLX" to 40.0))
        )
        assertEquals(
            mapOf("MSFT" to 150.0, "NFLX" to 40.0),
            averageStockPrice(listOf("MSFT" to 100.0, "MSFT" to 200.0, "NFLX" to 40.0))
        )
        assertEquals(
            mapOf("MSFT" to 150.0, "NFLX" to 45.0),
            averageStockPrice(listOf("MSFT" to 100.0, "MSFT" to 200.0, "NFLX" to 40.0, "NFLX" to 50.0))
        )
    }

    @Test
    @Tag("4")
    fun findCheapestStuff() {
        assertNull(
            findCheapestStuff(
                mapOf("Мария" to ("печенье" to 20.0), "Орео" to ("печенье" to 100.0)),
                "торт"
            )
        )
        assertEquals(
            "Мария",
            findCheapestStuff(
                mapOf("Мария" to ("печенье" to 20.0), "Орео" to ("печенье" to 100.0)),
                "печенье"
            )
        )
    }

    @Test
    @Tag("3")
    fun canBuildFrom() {
        assertFalse(canBuildFrom(listOf('a', 'a'), " "))
        assertFalse(canBuildFrom(emptyList(), "foo"))
        assertTrue(canBuildFrom(listOf('a', 'b', 'o'), "baobab"))
        assertFalse(canBuildFrom(listOf('a', 'm', 'r'), "Marat"))
    }

    @Test
    @Tag("4")
    fun extractRepeats() {
        assertEquals(
            emptyMap<String, Int>(),
            extractRepeats(emptyList())
        )
        assertEquals(
            mapOf("a" to 2),
            extractRepeats(listOf("a", "b", "a"))
        )
        assertEquals(
            emptyMap<String, Int>(),
            extractRepeats(listOf("a", "b", "c"))
        )
    }

    @Test
    @Tag("3")
    fun hasAnagrams() {
        assertFalse(hasAnagrams(emptyList()))
        assertTrue(hasAnagrams(listOf("рот", "свет", "тор")))
        assertFalse(hasAnagrams(listOf("рот", "свет", "код", "дверь")))
        assertFalse(hasAnagrams(listOf("поле", "полено")))
        assertTrue(hasAnagrams(listOf("лунь", "нуль")))
    }

    @Test
    @Tag("5")
    fun propagateHandshakes() {
        assertEquals(
            mapOf(
                "0" to setOf("3b1", "6", "1"),
                "1" to setOf("3b1", "6", "0"),
                "3b1" to setOf("0", "1", "6"),
                "6" to setOf(),
            ),
            propagateHandshakes(
                mapOf(
                    "0" to setOf("1", "3b1"),
                    "1" to setOf("3b1"),
                    "6" to setOf(),
                    "3b1" to setOf("0", "6"),
                )
            )
        )

        assertEquals(
            mapOf(
                "1" to setOf(),
                "2" to setOf("0", "3", "1"),
                "0" to setOf("3", "2", "1"),
                "3" to setOf("1", "2", "0"),
            ),
            propagateHandshakes(
                mapOf(
                    "1" to setOf(),
                    "2" to setOf("0"),
                    "0" to setOf("3", "2"),
                    "3" to setOf("1", "2"),
                )
            )
        )

        assertEquals(
            mapOf(
                "0" to setOf(),
                "2" to setOf("0"),
                "3" to setOf("1", "2", "0"),
                "1" to setOf("2", "0"),
            ),
            propagateHandshakes(
                mapOf(
                    "0" to setOf(),
                    "2" to setOf("0"),
                    "3" to setOf("1"),
                    "1" to setOf("2"),
                )
            )
        )

        assertEquals(
            mapOf(
                "0" to setOf("1", "3b1", "6"),
                "3b1" to setOf("0", "6", "1"),
                "1" to setOf(),
                "6" to setOf(),
            ),
            propagateHandshakes(
                mapOf(
                    "0" to setOf("1", "3b1"),
                    "3b1" to setOf("0", "6"),
                )
            )
        )

        assertEquals(
            mapOf(
                "0" to setOf("2", "23b"),
                "2" to setOf("23b"),
                "3" to setOf("0", "2", "23b"),
                "23b" to setOf(),
            ),
            propagateHandshakes(
                mapOf(
                    "0" to setOf("2"),
                    "2" to setOf("23b"),
                    "3" to setOf("0"),
                )
            )
        )
        assertEquals(
            mapOf(
                "3a9" to setOf("337"),
                "0" to setOf("1e3", "3a9", "337"),
                "337" to setOf(),
                "1e3" to setOf(),
            ),
            propagateHandshakes(
                mapOf(
                    "3a9" to setOf("337"),
                    "0" to setOf("1e3", "3a9"),
                )
            )
        )
        assertEquals(
            mapOf(
                "Marat" to setOf("Misha"),
                "Sveta" to setOf(),
                "Misha" to setOf(),
                "Mikhail" to setOf(),
            ),
            propagateHandshakes(
                mapOf(
                    "Marat" to setOf("Misha"),
                    "Sveta" to setOf(),
                    "Mikhail" to setOf()
                )
            )
        )
        assertEquals(
            mapOf(
                "0" to setOf("1", "3", "2"),
                "1" to setOf("2"),
                "3" to setOf(),
                "2" to setOf()
            ),
            propagateHandshakes(
                mapOf(
                    "0" to setOf("1", "3"),
                    "1" to setOf("2"),
                )
            )
        )

        assertEquals(
            mapOf(
                "Marat" to setOf("Sveta", "Mikhail"),
                "Sveta" to setOf("Mikhail", "Marat"),
                "Mikhail" to setOf("Marat", "Sveta")
            ),
            propagateHandshakes(
                mapOf(
                    "Marat" to setOf("Sveta", "Mikhail"),
                    "Sveta" to setOf("Mikhail"),
                    "Mikhail" to setOf("Marat", "Sveta")
                )
            )
        )
        assertEquals(
            mapOf(
                "Marat" to setOf("Mikhail", "Sveta"),
                "Sveta" to setOf("Mikhail"),
                "Mikhail" to setOf()
            ),
            propagateHandshakes(
                mapOf(
                    "Marat" to setOf("Sveta"),
                    "Sveta" to setOf("Mikhail")
                )
            )
        )
        assertEquals(
            mapOf(
                "Marat" to setOf("Mikhail", "Sveta"),
                "Sveta" to setOf("Marat", "Mikhail"),
                "Mikhail" to setOf("Sveta", "Marat"),
                "Friend" to setOf("GoodGnome"),
                "EvilGnome" to setOf(),
                "GoodGnome" to setOf()
            ),
            propagateHandshakes(
                mapOf(
                    "Marat" to setOf("Mikhail", "Sveta"),
                    "Sveta" to setOf("Marat"),
                    "Mikhail" to setOf("Sveta"),
                    "Friend" to setOf("GoodGnome"),
                    "EvilGnome" to setOf()
                )
            )
        )
    }

    @Test
    @Tag("6")
    fun findSumOfTwo() {
        assertEquals(
            Pair(-1, -1),
            findSumOfTwo(emptyList(), 1)
        )
        assertEquals(
            Pair(0, 2),
            findSumOfTwo(listOf(1, 2, 3), 4)
        )
        assertEquals(
            Pair(-1, -1),
            findSumOfTwo(listOf(1, 2, 3), 6)
        )

        assertEquals(
            Pair(0, 2),
            findSumOfTwo(listOf(2,2,3,3), 5)
        )
    }

    @Test
    @Tag("8")
    fun bagPacking() {
        assertEquals(
            emptySet<String>(),
            bagPacking(
                mapOf(),
                1
            )
        )

        assertEquals(
            setOf("0"),
            bagPacking(
                mapOf(
                    "0" to (1 to 1),
                    "1" to (1 to 1),
                    "2" to (1 to 1),
                    "3" to (1 to 1),
                    "4" to (1 to 1),
                    "5" to (1 to 1),
                    "6" to (1 to 1),
                    "7" to (1 to 1),
                    "8" to (1 to 1),
                    "9" to (1 to 1),
                    "10" to (1 to 1),
                    "11" to (1 to 1),
                    "12" to (1 to 1),
                    "13" to (1 to 1),
                    "14" to (1 to 1),
                    "15" to (1 to 1),
                    "16" to (1 to 1),
                    "17" to (1 to 1),
                    "18" to (1 to 1),
                    "19" to (1 to 1),
                    "20" to (1 to 1),
                    "21" to (1 to 1),
                    "22" to (1 to 1),
                    "23" to (1 to 1),
                    "24" to (1 to 1),
                    "25" to (1 to 1),
                    "26" to (1 to 1),
                    "27" to (1 to 1),
                    "28" to (1 to 1),
                    "29" to (1 to 1),
                    "30" to (1 to 1),
                    "31" to (1 to 1),
                ),
                1
            )
        )

        assertEquals(
            setOf("Камень", "Слиток", "Кубок"),
            bagPacking(
                mapOf(
                    "Кубок" to (500 to 2000),
                    "Слиток" to (500 to 5000),
                    "Камень" to (500 to 2500),
                    "Книга" to (600 to 2000),
                    "Ножницы" to (100 to 200),
                    "Гиря" to (5000 to 1000),
                    "Слон" to (5000000 to 250000)
                ),
                1500
            )
        )

        assertEquals(
            setOf("Кубок", "Слиток"),
            bagPacking(
                mapOf("Кубок" to (500 to 2000), "Слиток" to (750 to 5000), "Книга" to (600 to 2000)),
                1500
            )
        )
        assertEquals(
            setOf("Кубок", "Слиток"),
            bagPacking(
                mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
                1500
            )
        )
        assertEquals(
            setOf("Кубок"),
            bagPacking(
                mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
                850
            )
        )
        assertEquals(
            emptySet<String>(),
            bagPacking(
                mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
                450
            )
        )

        assertEquals(
            setOf("Гитара", "Ноутбук"),
            bagPacking(
                mapOf("Магнитофон" to (4 to 3000), "Ноутбук" to (3 to 2000), "Гитара" to (1 to 1500)),
                4
            )
        )

        assertEquals(
            setOf("Ноутбук", "iPhone"),
            bagPacking(
                mapOf(
                    "Магнитофон" to (4 to 3000),
                    "Ноутбук" to (3 to 2000),
                    "Гитара" to (1 to 1500),
                    "iPhone" to (1 to 2000)
                ),
                4
            )
        )

    }
}
