package me.byrt.cheesehunt.manager

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

import kotlin.collections.ArrayList
import kotlin.random.Random

@Suppress("unused")
class TabListManager(private var game : Game) {
    private var cheesePuns: ArrayList<String> = arrayListOf()
    private var header: Component = Component.text("")
    private var footer: Component = Component.text("")

    private fun assignRandomPun() {
        val randomIndex = Random.nextInt(cheesePuns.size)
        val randomPun = cheesePuns[randomIndex]
        header = Component.text("Cheese Hunt").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, true)
            .append(Component.text("\n               An MCC Tester classic meme, brought to you by ").color(NamedTextColor.WHITE).decoration(TextDecoration.BOLD, false).append(Component.text("Byrt").color(NamedTextColor.RED))
            .append(Component.text(".               ").color(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false)))
        footer = Component.text(randomPun).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, true)
    }

    fun populateCheesePuns() {
        cheesePuns.add("'When life gives you cheese, you make a minigame with it'")
        cheesePuns.add("'I don't have any gouda puns to put here'")
        cheesePuns.add("'Do you brie-lieve in life after love?'")
        cheesePuns.add("'I'd make a cheesy joke, but I gouda say, it'd brie a bit too forced'")
        cheesePuns.add("'You cheddar not say any cheese jokes!'")
        cheesePuns.add("'Made you look: Cheese Edition'")
        cheesePuns.add("'Nothing gets cheddar than this'")
        cheesePuns.add("'Anything you can do, I can do feta'")
        cheesePuns.add("'You think you’re feta than me?'")
        cheesePuns.add("'Hello, is it brie you're looking for?'")
        cheesePuns.add("'This might sound cheesy, but I think you're really grate'")
        cheesePuns.add("'Damn! You're looking mozzar-hella good today!'")
        cheesePuns.add("'Hey, I camembert the last time we saw each other'")
        cheesePuns.add("'After the cheese factory exploded, all that was left was de-brie'")
        cheesePuns.add("'Sweet dreams are made of cheese'")
        cheesePuns.add("'I brought my Cheese rifle, it's time to go hunting'")
        cheesePuns.add("'I swiss you the best!'")
        cheesePuns.add("'Cheddar days are coming...'")
        cheesePuns.add("'Sweet dreams are made of cheese,\nWho am I to disa-brie,\nI cheddar the world by the feta cheese,\nEverybody's looking for stilton.'")
        assignRandomPun()
    }

    fun getTabHeader(): Component {
        return header
    }

    fun getTabFooter(): Component {
        return footer
    }
}