package me.byrt.cheesehunt.manager

class BlockManager(private val game : Game) {
    fun removeBarriers() {
        when(game.getRoundState()) {
            RoundState.ROUND_ONE -> {
                /*for (x in 14..19) { // Removing red round one barrier
                    for (y in -43..-40) {
                        getWorld("Cheese")?.getBlockAt(x, y, 41)?.type = Material.AIR
                    }
                }

                for (x in -66..-61) { // Removing blue round one barrier
                    for (y in -43..-40) {
                        getWorld("Cheese")?.getBlockAt(x, y, 41)?.type = Material.AIR
                    }
                }*/
            }
        }
    }

    fun resetBarriers() {
        /*for (x in 14..19) { // Removing red round one barrier
            for (y in -43..-40) {
                getWorld("Cheese")?.getBlockAt(x, y, 41)?.type = Material.RED_STAINED_GLASS
            }
        }

        for (x in -66..-61) { // Removing blue round one barrier
            for (y in -43..-40) {
                getWorld("Cheese")?.getBlockAt(x, y, 41)?.type = Material.BLUE_STAINED_GLASS
            }
        }

        for (x in -66..-61) { // Removing blue round two barrier
            for (y in -52..-49) {
                getWorld("Cheese")?.getBlockAt(x, y, 74)?.type = Material.RED_STAINED_GLASS
            }
        }

        for (x in 14..19) { // Removing blue round two barrier
            for (y in -52..-49) {
                getWorld("Cheese")?.getBlockAt(x, y, 74)?.type = Material.BLUE_STAINED_GLASS
            }
        }*/
    }
}