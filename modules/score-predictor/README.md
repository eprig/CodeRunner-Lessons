# Score Predictor

Work in `src/main/java/frc/robot/RobotContainer.java`, `FrcTeam.java`, `FrcAlliance.java`,
and `FrcMatch.java`. Run this lesson with the CodeRunner Driver Station and enable teleop.

This lesson uses four classes that work together. The new parts are **arrays** (a list of
values stored in one variable) and objects that hold other objects:

| Class            | What it represents          | What it holds                      |
| ---------------- | --------------------------- | ---------------------------------- |
| `FrcTeam`        | one team                    | a team number and 3 match scores   |
| `FrcAlliance`    | one alliance (red or blue)  | a colour and 3 `FrcTeam` objects   |
| `FrcMatch`       | one match                   | a red and a blue `FrcAlliance`     |
| `RobotContainer` | where you build and run it  | your teams, alliances, and match   |

All four files are in `src/main/java/frc/robot/`. The other three classes already have their
fields and constructors. You fill in the `TODO` parts.

**When does my code run?** `RobotContainer.teleopInit()` runs once, each time you enable
teleop in the Driver Station. The predictions print in the console.

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

Steps 1-3 build the teams, alliances, and match in `RobotContainer.java`. Steps 4-7 fill in
the math inside the other classes.

### 1. Create six teams

An **array** stores several values of the same type in one variable. You can write one
directly inside `{ }`:

```java
int[] batteryCycles = new int[] {12, 40, 7, 25};
```

An `FrcTeam` is built from a team number and an array of its last three match scores. Here
is one team:

```java
FrcTeam team1 = new FrcTeam(5885, new int[] {70, 75, 80});
```

In `RobotContainer.teleopInit()`, create six `FrcTeam` objects. Make up a team number and
three previous match scores for each one.

### 2. Create a red alliance and a blue alliance

An `FrcAlliance` is built from a colour and an array of three teams. An array can hold
objects, too:

```java
FrcAlliance blueAlliance = new FrcAlliance("blue", new FrcTeam[] {team1, team2, team3});
```

Create a blue alliance and a red alliance in `teleopInit()`, with three teams on each. Each
team should be on only one alliance.

> **Common mistake:** the class spells it `colour` (with a **u**). If you add code that uses
> that field later, spell it the same way.

### 3. Create the match and predict the winner

`FrcMatch`'s constructor takes the red alliance first, then the blue alliance:

```java
public FrcMatch(FrcAlliance redAlliance, FrcAlliance blueAlliance)
```

Create an `FrcMatch` in `teleopInit()` with your two alliances, then call its
`predictWinner()` method. To call a method on an object, use a dot:

```java
myMatch.predictWinner();
```

> **Check your work:** run the Driver Station and enable teleop. You should see:
> ```
> Red predicted score: 0.0
> Blue predicted score: 0.0
> It's predicted to be a tie!
> ```
> That's expected! The other classes still return `0.0` placeholders. The next steps fix
> that.

### 4. Calculate each team's average score in `FrcTeam`

Open `FrcTeam.java`. The constructor has a `TODO` where `averagePointsScored` is set to
`0.0`. The team's scores are in the array `lastThreeMatchScores`.

Reading values out of an array:

```java
int[] batteryCycles = new int[] {12, 40, 7, 25};
int first = batteryCycles[0];       // 12  <- counting starts at 0, not 1
int howMany = batteryCycles.length; // 4
```

A **for-each loop** runs once for every value in an array:

```java
int oldBatteries = 0;
for (int cycles : batteryCycles) {
  // `cycles` holds the next value each time around: 12, then 40, then 7, then 25
  if (cycles > 20) {
    oldBatteries++;
  }
}
```

An **average** is the total of all the values, divided by how many values there are.

- How could you keep a running total as the loop goes through each score? Where does that
  total variable need to be created: before the loop, or inside it?
- Should you divide by `3`, or by `lastThreeMatchScores.length`? Which still works if
  someone passes in four scores?

Replace the `0.0` so `averagePointsScored` is set to the team's real average.

> **Common mistake:** dividing two `int`s drops the decimal. `226 / 3` is `75`, not
> `75.333...`. If your total is an `int`, how could you make sure a `double` is involved in
> the division?

> **Common mistake:** `averagePointsScored` is `final`, which means it can be set only once.
> Add up the total in a separate variable first, then set `averagePointsScored` one time, in
> place of the `0.0`.

### 5. Return the predicted score from `FrcTeam.predictScore()`

Our prediction is simple: a team will score its average. `predictScore()` currently returns
`0.0`. Which field already holds the answer?

Change `predictScore()` so it returns the team's predicted score.

> **Think about it:** `RobotContainer` could just read the average itself, if the field were
> public. Why have a `predictScore()` method instead? (Hint: what if you later want a smarter
> prediction, like counting the most recent match more? Which code would have to change?)

> **Check your work:** a team with scores `{70, 75, 80}` should predict `75.0`. A team with
> `{70, 75, 81}` should predict about `75.33`.

### 6. Add up the alliance's score in `FrcAlliance.predictAllianceScore()`

Open `FrcAlliance.java`. The alliance keeps its teams in an array called `teams`. A for-each
loop works on an array of objects the same way it works on an array of numbers, and you can
call a method on each object as you go:

```java
for (Battery battery : batteryCart) {
  System.out.println(battery.getVoltage());
}
```

The alliance's predicted score is the predicted scores of all its teams added together. Use
a loop in `predictAllianceScore()` to add them up, and return the total.

> **Check your work:** an alliance whose teams predict `75.0`, `70.0`, and `80.0` should
> predict `225.0`.

### 7. Predict the winner in `FrcMatch.predictWinner()`

Open `FrcMatch.java`. `predictWinner()` already prints and logs both scores, and already has
an `if` / `else if` / `else` that prints the winner. You need to fill in two `TODO`s:

1. Set `redScore` and `blueScore` using each alliance's `predictAllianceScore()` method. The
   alliances are stored in the fields `redAlliance` and `blueAlliance`.
2. Replace the `false` placeholders for `redWins` and `blueWins` with score comparisons.

A `boolean` can store the result of a comparison directly:

```java
boolean overheating = motorTempCelsius > 80;
```

| Operator | Means                    |
| -------- | ------------------------ |
| `==`     | equal to                 |
| `<`      | less than                |
| `>`      | greater than             |
| `<=`     | less than or equal to    |
| `>=`     | greater than or equal to |

> **Think about it:** if the two scores are exactly equal, should `redWins` be true? Should
> `blueWins`? Which branch of the `if` runs then?

> **Check your work:** enable teleop again. The console should show both real scores and the
> right winner. Try changing one team's scores so the other alliance wins. Then try giving
> both alliances the same total to get a tie. The scores are also logged as
> `Prediction/RedScore` and `Prediction/BlueScore`, so you can see them in AdvantageScope.

## Bonus

- **Log each team's predicted score with `Logger.recordOutput`.** It takes a name (the
  **key**) and a value, and needs `import org.littletonrobotics.junction.Logger;` at the top
  of the file:
  ```java
  Logger.recordOutput("Battery/Voltage", 12.6);
  ```
  Log each team under the key `Teams/<team number>/PredictedScore`, for example
  `Teams/5885/PredictedScore`. You can build that key with `+`:
  ```java
  "Teams/" + teamNumber + "/PredictedScore"
  ```
  Which class already visits every team in a loop? That's a good place to log.
- **Add a margin of victory.** The margin is how many points the winner wins by. Subtracting
  in the wrong order gives a negative number, so `Math.abs` can help: `Math.abs(-15.0)` is
  `15.0`. Print it with the winner.
- **Make the match print each team number on the winning alliance.** `FrcMatch` can't see an
  alliance's `teams` array, because it's `private`. You could add a method to `FrcAlliance`
  that prints its own team numbers, or one that returns the array. `FrcTeam` already has a
  `getTeamNumber()` method you can use.
