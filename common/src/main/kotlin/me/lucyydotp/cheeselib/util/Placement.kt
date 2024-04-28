package me.lucyydotp.cheeselib.util

inline fun <T> Iterable<T>.calculateOrdinalRanking(crossinline placementSelector: (T) -> Int) =
    buildList<Pair<T, Int>> {
        this@calculateOrdinalRanking.map { it to placementSelector(it) }
            .sortedByDescending { it.second }
            .forEachIndexed { i, (obj, placement) ->
                if (i == 0 || placement != last().second) {
                    add(obj to i)
                } else {
                    add(obj to last().second)
                }
            }
    }
