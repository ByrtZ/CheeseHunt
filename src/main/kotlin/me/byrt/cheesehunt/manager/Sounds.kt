package me.byrt.cheesehunt.manager

@Suppress("unused")
class Sounds(private val game : Game) {
    object Music {
        const val GAME_MUSIC = "mcc.rocket_spleef"
        const val OVERTIME_INTRO_MUSIC = "mcc.overtime_intro"
        const val OVERTIME_MUSIC = "mcc.overtime"
        const val GAME_OVER_MUSIC = "mcc.game_over"
    }
    object Timer {
        const val STARTING_TICK = "block.note_block.bass"
        const val STARTING_TICK_FINAL = "block.note_block.pling"
        const val CLOCK_TICK = "block.note_block.bass"
        const val CLOCK_TICK_HIGH = "block.note_block.bass"
    }
    object Round {
        const val ROUND_END_PLING = "block.note_block.pling"
        const val WIN_ROUND = "ui.toast.challenge_complete"
        const val LOSE_ROUND = "entity.ender_dragon.growl"
        const val DRAW_ROUND = "entity.wither.spawn"
    }
    object GameOver {
        const val GAME_OVER_PLING = "block.note_block.pling"
        const val GAME_OVER_EFFECT_1 = "block.respawn_anchor.deplete"
        const val GAME_OVER_EFFECT_2 = "ui.toast.challenge_complete"
    }
    object Start {
        const val START_GAME_SUCCESS = "block.end_portal.spawn"
        const val START_GAME_FAIL = "entity.enderman.teleport"
    }
    object Respawn {
        const val RESPAWN_TIMER = "ui.button.click"
        const val RESPAWN = "block.beacon.power_select"
    }
    object Score {
        const val ELIMINATION = "entity.player.levelup"
        const val COLLECT_CHEESE = "entity.wandering_trader.reappeared"
        const val LOSE_CHEESE_PRIMARY = "block.note_block.didgeridoo"
        const val LOSE_CHEESE_SECONDARY = "entity.wandering_trader.disappeared"
        const val CLAIM_CHEESE = "entity.player.levelup"
    }
    object Tutorial {
        const val TUTORIAL_POP = "entity.item.pickup"
    }
    object Command {
        const val SHUFFLE_START = "block.note_block.flute"
        const val SHUFFLE_COMPLETE = "block.note_block.flute"
        const val PING = "entity.experience_orb.pickup"
        const val BUILDMODE_SUCCESS = "entity.mooshroom.convert"
        const val BUILDMODE_FAIL = "entity.enderman.teleport"
    }
}