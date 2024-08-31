package io.github.josephsimutis

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.float
import com.github.ajalt.clikt.parameters.types.int

private fun <E> List<E>.containsAny(second: ArrayList<E>): Boolean {
    forEach { element ->
        if (second.contains(element)) return true
    }
    return false
}

class CardDrawer() : CliktCommand() {
    val deckFile by option(help="The location of the deck file").file(mustExist = true).required()
    val count by option(help="The number of cards to draw").int().prompt()
    val delay by option(help="The delay between each card being drawn in seconds").float().default(0f)
    // Make sure to re-add removeFromDeck at some point
    //val removeFromDeck by option(help="Enables the removal of the drawn cards from the deck file").counted()
    val tag by option(help="The tags used to filter the deck").multiple()

    override fun run() {
        // In deck, the first array in the pair is the tags, and the second is the card.
        val deck = ArrayList<Pair<Array<String>, String>>()
        val currentTags = ArrayList<String>()
        deckFile.forEachLine { line ->
            if (line.startsWith('+')) {
                currentTags.addAll(line.removePrefix("+").replace(" ", "").split(',').toSet())
            } else if (line.startsWith('-')) {
                currentTags.removeAll(line.removePrefix("-").replace(" ", "").split(',').toSet())
            } else {
                deck += Pair(currentTags.toTypedArray(), line)
            }
        }
        // In filteredTags, the first arraylist is that tags that are needed, and the second arraylist is the ones that are not allowed.
        val filteredTags = Pair<ArrayList<String>, ArrayList<String>>(ArrayList(), ArrayList())
        tag.forEach {
            if (it.startsWith('-')) {
                filteredTags.second += it.removePrefix("-")
            } else {
                filteredTags.first += it.removePrefix("+")
            }
        }
        val section = ArrayList<String>()
        deck.forEach { (tags, card) ->
            if (tags.toList().containsAll(filteredTags.first) && !tags.toList().containsAny(filteredTags.second)) {
                section.add(card)
            }
        }
        for (i in 1..count) {
            Thread.sleep((delay * 1000).toLong())
            val drawnCard = section.random()
            section.remove(drawnCard)
            echo(drawnCard)
        }
        //if (removeFromDeck > 0) deckFile.writeText(section.joinToString("\n"))
    }
}

fun main(args: Array<String>) = CardDrawer().versionOption("1.1.0-pre.1").main(args)