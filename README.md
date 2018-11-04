# CardGame (Texas Hold'Em - Java Edition)
**Version: 0.1 (Pre-Alpha)**

This project strives to create a version of Texas Hold'Em purely in the language of Java. The game is currently in a state where it can be played off a command line, but nothing more. It is still very buggy and features still need to be added. *In theory* though, a game is totally possible. A GUI is expected to be added later, but until it can be only played on a command line, and I have only been testing through my IDE (IntellIJ Idea), so nothing is guaranteed.

## How to Play
The command line gives instructions on how to play. 

## Roadmap
- Stable Game
- Refactoring
- New/Improved Features
- GUI

## Versions

### 0.2 [???]
- [Feature] GUI interface

### 0.1 [4/11/2018]
- [Feature] During a turn, a user can enter a command to see what their hand value is
- [Feature] Simple end game added, when works in two ways. Firstly, a player hits 0, and they have lost. Secondly, a computer player hits 0, and the cash totals of all remaining players are calculated, with the highest winning.
- [Enhancement] Improved printing message when a round has multiple winners
- [Enhancement] AI has drastic improvements after the flop, it will be statistically more likely to call and raise
- [Enhancement] General modifications
- [Fix] Head2Head class returning the wrong player when determining a winner
- [Fix] Ensure that split winnings are equally given to all winners
- [Fix] Stop crashing when determining secondary ranks (different hands in same rank) and tertiary rank (card value)
- [Fix] Modify round betting loop to ensure betting ends once all players have placed equal bets in (when possible)
- [Fix] Improvement of calling after a raise that wasn't from the first player
- [Fix] When winning on a high card, message will return the actual value of the card rather than a null
- [Fix] "Raise" move was modified and rewritten to better make sense and not break anything
- [Fix] When a player hits a cash value of 0, it will not allow them to make any moves whatsoever
- [Removal] When a player wins a round despite betting less, they will win everything in the pot rather than winning relative to what they bet
- BIG FUN SURPRISE!!

### 0.02 [23/10/2018]
- [Fix] Ensure that all AI cards (hand and table) are calculated
- [Enhancement] Various removal of commented out code & print statements

### 0.01 [13/10/2018]
- Initial Release