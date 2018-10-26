# CardGame (Texas Hold'Em - Java Edition)
**Version: 0.02 (Pre-Alpha)**

This project strives to create a version of Texas Hold'Em purely in the language of Java. The game is currently in a state where it can be played off a command line, but nothing more. It is still very buggy and features still need to be added. *In theory* though, a game is totally possible. A GUI is expected to be added later, but until it can be only played on a command line, and I have only been testing through my IDE (IntellIJ Idea), so nothing is guaranteed.

## How to Play
The command line gives instructions on how to play. 

## Roadmap
- Stable Game
- Refactoring
- New/Improved Features
- GUI

## Versions

### 0.1 [???]
- [Fix] Head2Head class returning the wrong player when determining a winner
- [Fix] Stop crashing when determining secondary ranks (different hands in same rank) and tertiary rank (card value)
- [Fix] Modify round betting loop to ensure betting ends once all players have placed equal bets in (when possible)
- [Fix] Improvement of calling after a raise that wasn't from the first player
- [Enhancement] Improved printing message when a round has multiple winners
- [Enhancement] AI has drastic improvements after the flop

### 0.02 [23/10/2018]
- [Fix] Ensure that all AI cards (hand and table) are calculated
- [Enhancement] Various removal of commented out code & print statements

### 0.01 [13/10/2018]
- Initial Release