package me.byrt.cheesehunt.queue

enum class QueueState {
    IDLE,
    AWAITING_PLAYERS,
    NO_GAME_AVAILABLE,
    SENDING_PLAYERS_TO_GAME
}