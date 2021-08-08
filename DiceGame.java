package com.example.demo;

import java.util.*;
import java.util.stream.Collectors;

class Player {

    private String name;
    private Integer score;
    private Integer lastRoll;
    private boolean canRoll;

    public boolean getCanRoll() {
        return canRoll;
    }

    public void setCanRoll(boolean canRoll) {
        this.canRoll = canRoll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getLastRoll() {
        return lastRoll;
    }

    public void setLastRoll(Integer lastRoll) {
        this.lastRoll = lastRoll;
    }

    public Player(String name, Integer score) {
        this.name = name;
        this.score = score;
        this.lastRoll = 0;
        this.canRoll = true;
    }
}


public class DiceGame {

    // Method to get a random numebr for each roll
    public static int rollDice() {
        return new Random().nextInt(6) + 1;
    }

    // To print the sorted points table with ranking
    public static void printTable(int numberOfPlayers, List<Player> players) {
        System.out.println("Current score table:-\n" + "Player Name \t " + "Score \t" + "Rank");
        Comparator<Player> scoreCommparator = Comparator.comparing(Player::getScore);
        List<Player> listSorted = players.stream().sorted(scoreCommparator.reversed()).collect(Collectors.toList());
        for (int n = 0; n < numberOfPlayers; n++) {
            System.out.println(listSorted.get(n).getName() + "\t\t\t" + listSorted.get(n).getScore() + "\t\t" + (n + 1));
        }
        System.out.println("\n");
    }

    public static boolean isFinished(Player player, int randomNumber, int pointsToAccumulate) {
        int totalScore = player.getScore() + randomNumber;
        player.setScore(totalScore);
        return totalScore >= pointsToAccumulate;
    }

    public static void skipTurn(Player player) {
        System.out.println("You have rolled 2 consecutive 1's, Your next turn will be skipped");
        player.setCanRoll(false);
    }

    public static void main(String[] args) {

        boolean gameFlag = true;
        int winnersCount = 0;

        System.out.print("Enter the number of players: ");

        // Initializing the Scanner object to read input
        Scanner input = new Scanner(System.in);
        int numberOfPlayers = input.nextInt();

        System.out.print("Enter the points to accumulate: ");
        int pointsToAccumulate = input.nextInt();

        // Initializing the list with name and score
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player("Player " + (i + 1), 0));
        }
        Collections.shuffle(players);
        System.out.println("Playing order will be: ");
        for (int i = 0; i < players.size(); i++) {
            System.out.println(players.get(i).getName());
        }


        while (gameFlag) {
            for (int i = 0; i < numberOfPlayers; i++) {
                Player currentPlayer = players.get(i);
                if (currentPlayer.getScore() < pointsToAccumulate && currentPlayer.getCanRoll()) {
                    System.out.println(currentPlayer.getName() + " its your turn (press r to roll the dice)");
                    String rollInput = input.next("r");
                    if (rollInput.equals("r")) {
                        int randomNumber = rollDice();
                        System.out.println("You have got " + randomNumber);
                        if (isFinished(currentPlayer, randomNumber, pointsToAccumulate)) {
                            winnersCount++;
                            System.out.println("UPDATE:- " + currentPlayer.getName() + " has ranked " + winnersCount);
                            // When player rolls a 6 he gets another turn
                        } else if (randomNumber == 6) {
                            System.out.println("You have rolled 6, got another chance to roll");
                            System.out.println("(press r to roll the dice)");
                            rollInput = input.next("r");
                            if (rollInput.equals("r")) {
                                randomNumber = rollDice();
                                System.out.println("You have got " + randomNumber);
                                if (isFinished(players.get(i), randomNumber, pointsToAccumulate)) {
                                    winnersCount++;
                                    System.out.println("UPDATE:- " + currentPlayer.getName() + " has ranked " + winnersCount);
                                }
                            }
                            // When player gets 2 consecutive 1's
                        } else if (randomNumber == 1 && currentPlayer.getLastRoll() == 1) {
                            skipTurn(currentPlayer);
                        }
                        currentPlayer.setLastRoll(randomNumber);
                    } else {
                        System.out.println("Wrong input");
                    }
                    printTable(numberOfPlayers, players);
                } else {
                    // After skipping a turn, restoring to default
                    currentPlayer.setLastRoll(0);
                    currentPlayer.setCanRoll(true);
                }
            }
            if (winnersCount == numberOfPlayers) {
                gameFlag = false;
            }
        }
        input.close();
        System.out.println("Game finished.");
    }
}
