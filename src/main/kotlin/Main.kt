package io.github.josephsimutis

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.float
import com.github.ajalt.clikt.parameters.types.int

class CardDrawer() : CliktCommand() {
    val deckFile by option(help="The location of the deck file").file(mustExist = true).required()
    val count by option(help="The number of cards to draw").int().prompt()
    val delay by option(help="The delay between each card being drawn in seconds").float().default(0f)
    val removeFromDeck by option(help="Enables the removal of the drawn cards from the deck file").counted()

    override fun run() {
        val deck = ArrayList(deckFile.readLines())
        for (i in 1..count) {
            Thread.sleep((delay * 1000).toLong())
            val drawnCard = deck.random()
            deck.remove(drawnCard)
            echo(drawnCard)
        }
        if (removeFromDeck > 0) deckFile.writeText(deck.joinToString("\n"))
    }
}

fun main(args: Array<String>) = CardDrawer().versionOption("1.0.0").main(args)