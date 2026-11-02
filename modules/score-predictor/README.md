# Score Predictor

Work in `src/main/java/frc/robot/RobotContainer.java`, `FrcTeam.java`, `FrcAlliance.java`, and `FrcMatch.java`. Run this lesson with the CodeRunner Driver Station and enable teleop.

## What you're making

A match predictor. You'll make up six FRC teams with their last three match scores, split
them into a red and a blue alliance, and predict which alliance wins based on each team's
average score. When you enable teleop, the console should print something like:

```
Red predicted score: 225.0
Blue predicted score: 210.0
The red alliance is predicted to win!
```

## Steps

1. In `RobotContainer.teleopInit()`, create six `FrcTeam` objects. Give each team a team number and three made-up previous match scores.
2. Create one red alliance and one blue alliance, with three teams on each alliance.
3. Create a `FrcMatch` with the two alliances and call `predictWinner()`.
4. In `FrcTeam`, calculate the team's average score from `lastThreeMatchScores`.
5. Make `FrcTeam.predictScore()` return the team's predicted score.
6. In `FrcAlliance.predictAllianceScore()`, use a loop to add the predicted scores from all three teams.
7. In `FrcMatch.predictWinner()`, calculate both alliance scores, print the scores, and print the predicted winner.

Examples:

```java
FrcTeam team1 = new FrcTeam(5885, new int[] {70, 75, 80});
FrcAlliance blueAlliance = new FrcAlliance("blue", new FrcTeam[] {team1, team2, team3});
```

## Bonus

- Log each team's predicted score with `Logger.recordOutput`.
- Add a margin of victory.
- Make the match print each team number on the winning alliance.
