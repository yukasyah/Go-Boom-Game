# OOPDS-ASSIGNMENT-1
ASSIGNMENT OOPDS 1 : GO BOOM GAME

# Go Boom Game
This is an object-oriented Java program for the Go Boom card game.

# How to Play
Go Boom is a card game played with a standard deck of 52 cards. The objective of the game is to get rid of all your cards before the other players. Here's how to play:

1. Setup: Shuffle the deck and deal 7 cards to each player. Place the remaining deck face-down in the center of the table.

2. Gameplay: The player with the first lead card starts the game.

3. Taking Turns: On your turn, you must play a card that matches either the rank or the suit of the card on top of the center pile. If you cannot play a matching card, you must draw cards from the deck until you obtain a playable card. If the deck is empty, move to the next player.

4. Playing a Card: To play a card, simply type in "card" and press enter. After that, card's code or name. For example, "s3" represents the 3 of Spades. If the card is playable, it is added to the discard pile.

5. Winning a Trick: A trick is won by playing the highest-ranked card with the same suit as the lead card. The winner of a trick leads the next card.

6. Winning the Game: The first player to get rid of all their cards is the winner. The game continues until only one player has cards remaining.

7. Scorekeeping: You can keep track of the score by assigning points to each player based on the number of cards they have at the end of each round. The player with the fewest cards receives the fewest points.

8. New Game: You can start a new game by typing "s" at any time.
That's the basic gameplay of Go Boom! Remember to follow the rules and have fun playing with your friends or family.

# Features
1. All cards are faced up to facilitate checking.
2. The game starts with a new deck of 52 randomized cards.
3. The first card in the deck becomes the first lead card and is placed at the center.
4. The first lead card determines the first player:

      a.  A, 5, 9, K for Player 1
      
      b.  2, 6, 10 for Player 2
      
      c.  3, 7, J for Player 3
      
      d.  4, 8, Q for Player 4
      
5. Each player is dealt 7 cards.
6. All players must follow the suit or rank of the lead card.
7. The highest-ranked card with the same suit as the lead card wins the trick.
8. The winner of a trick leads the next card.

# Commands
The game supports the following commands:

s - Start a new game.

x - Exit the game.

d - Draw cards from the deck until a playable card is obtained. If the deck is empty, skip to the next player.

card - A card played by the current player. Then, the card you want to play, for example s3

sk - The player will skip their turn to play a card

save - saving current game 

load - load previous saved game

# Getting Started
To run the Go Boom game, follow these steps:

a. Clone the repository or download the source code.

b. Compile the Java files: javac *.java

c. Run the game: java GoBoomGame.

