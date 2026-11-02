# Closest Distance

Work in `src/main/java/frc/robot/RobotContainer.java`. Run this lesson with the CodeRunner
Driver Station.

This lesson uses WPILib's `Translation2d`, which is a point on the field, together with loops
and a bit of geometry. The new parts are looping through a list of points and logging values
so you can *see* them on a field in AdvantageScope.

**Where does my code go?** Almost everything goes inside the `robotPeriodic()` method at the
bottom of `RobotContainer.java`. The robot calls `robotPeriodic()` over and over, about 50
times every second, for as long as the robot program is running. A few things are already
set up for you near the top of the class:

| Name                  | What it is                                    |
| --------------------- | --------------------------------------------- |
| `fieldPoints`         | a `List` of six `Translation2d` field points  |
| `FIELD_LENGTH_METERS` | how long the field is (`17.55`), along x      |
| `FIELD_WIDTH_METERS`  | how wide the field is (`8.05`), along y       |

## What you're making

A program that picks a random spot on the field and finds which of the field points is
closest to it. In AdvantageScope you'll see all the field points, the random spot, and the
closest point drawn on the field.

## Steps

### 1. Hardcode a point to check

A `Translation2d` holds an x and a y, both in meters. One corner of the field is `(0, 0)`.
x runs along the long side of the field and y runs along the short side.

```java
Translation2d hubSpot = new Translation2d(8.0, 4.0); // x = 8.0 m, y = 4.0 m
double x = hubSpot.getX(); // 8.0
double y = hubSpot.getY(); // 4.0
```

`Translation2d` is already imported at the top of the file, so you can use it right away.

In `robotPeriodic()`, create a `Translation2d` variable for the point you want to check.
Pick any x and y that are inside the field.

> **Think about it:** look at the six `fieldPoints` at the top of the class. Before you write
> any more code, guess which one is closest to your point. You'll use that guess to check
> your work in step 2.

### 2. Loop through `fieldPoints` and find the closest one

**The distance formula.** The straight-line distance between two points `(x1, y1)` and
`(x2, y2)` is:

```
distance = √( (x2 − x1)² + (y2 − y1)² )
```

Java has square root built in, and you can square a number by multiplying it by itself:

```java
double root = Math.sqrt(49); // 7.0
double squared = 3.5 * 3.5;  // 12.25
```

**Looping through a list.** A *for-each* loop runs its code once for every item in a list.
Each time around, the loop variable holds the next item:

```java
int leftHalf = 0;
for (Translation2d point : fieldPoints) {
  // this runs once per point, and `point` is a different one each time
  if (point.getX() < FIELD_LENGTH_METERS / 2) {
    leftHalf++; // count the points on the left half of the field
  }
}
```

**Finding the closest.** Think about finding the shortest pencil in a pile. You hold the
shortest one you've seen *so far*. You look at each pencil in turn, and if it's shorter than
the one in your hand, you swap. When you reach the end of the pile, the one in your hand is
the answer.

Your code works the same way. You need two variables, created *before* the loop: the closest
point so far, and its distance.

- What should the "closest distance so far" start as, so that the very first point always
  beats it? (Hint: `Double.MAX_VALUE` is the biggest number a `double` can hold.)
- Inside the loop, what condition tells you the current point is a new best?
- When it is, which two variables need to change?

Write the loop in `robotPeriodic()`, after your point from step 1.

> **Common mistake:** if you create the "closest so far" variables *inside* the loop, they
> start over every time around, and you'll just end up with the last point in the list.

> **Check your work:** print the closest point with `System.out.println(...)`. A
> `Translation2d` prints like `Translation2d(X: 3.50, Y: 6.40)`. Does it match your guess
> from step 1? Since `robotPeriodic()` runs about 50 times a second, the same line prints
> over and over. That's fine for a quick check. You'll switch to logging in step 4.

### 3. Use a random point instead

`Math.random()` gives you a random decimal from `0.0` up to (but not including) `1.0`.
Multiply it to stretch that range:

```java
double voltage = Math.random() * 12.0; // anywhere from 0.0 up to 12.0
```

Replace the hardcoded x and y from step 1 with random numbers that are always inside the
field. Which field size constant goes with x, and which goes with y?

> **Think about it:** `robotPeriodic()` runs about 50 times a second. If you pick the random
> point inside `robotPeriodic()`, you'll get a brand-new spot every loop. If you'd rather
> have one spot that stays put, where else could you create it? (Hint: `fieldPoints` is
> created once, as a field of the class, outside of any method.) Either choice is OK. Try
> one, and see which one you like watching.

### 4. Log the points so you can see them on the field

AdvantageKit's `Logger.recordOutput` sends a value to AdvantageScope. It needs an import at
the top of the file:

```java
import org.littletonrobotics.junction.Logger;
```

The first part is the name (the **key**) the value shows up under. Slashes in the key group
values into folders:

```java
Logger.recordOutput("Robot/Speed", 3.5);                          // a number
Logger.recordOutput("Robot/Target", new Translation2d(8.0, 4.0)); // a single point
```

`recordOutput` can log one point or an **array** of points, but it can't log a `List`
directly. A list can turn itself into an array:

```java
Translation2d[] spots = someList.toArray(new Translation2d[0]);
// the `new Translation2d[0]` just tells toArray what kind of array to make
```

At the end of `robotPeriodic()`, after you've found the closest point, log these three
values using these exact keys:

| Key                            | Value                                              |
| ------------------------------ | -------------------------------------------------- |
| `ClosestDistance/FieldPoints`  | all the field points (turn the list into an array) |
| `ClosestDistance/RandomPoint`  | your random point                                  |
| `ClosestDistance/ClosestPoint` | the closest field point you found                  |

Each one is a single line. Only the key and the variable change:

```java
Logger.recordOutput("ClosestDistance/RandomPoint", yourRandomPoint);
```

Run the Driver Station, open AdvantageScope, and find the `ClosestDistance` folder (it shows
up inside `RealOutputs`). Put all three keys on a 2D field view.

> **Check your work:** the closest point should sit right on top of one of the field points,
> and it should be the one nearest the random point. If you made a new random point every
> loop, the closest point should jump around to follow it.

### 5. Replace your formula with `getDistance()`

Measuring the distance between two points is so common that `Translation2d` can do it for
you:

```java
Translation2d robot = new Translation2d(2.0, 3.0);
Translation2d hub = new Translation2d(5.0, 7.0);
double meters = robot.getDistance(hub); // 5.0
```

Replace your `Math.sqrt` distance formula with `getDistance()`.

> **Check your work:** the closest point in AdvantageScope should behave exactly the same as
> before. If it changed, one of the two versions had a mistake.

### 6. Replace the whole loop with `nearest()`

`Translation2d` can also search a list for you. `nearest()` returns whichever point in the
list is closest:

```java
Translation2d closestBall = robotSpot.nearest(gamePieces);
```

Replace your whole loop with one call to `nearest()`. Keep your logging.

> **Check your work:** again, nothing should change in AdvantageScope.

> **Think about it:** your loop from step 2 is now one line. Is it still worth knowing how to
> write the loop yourself? (`nearest()` is running a loop just like yours behind the scenes.)

## Bonus

- **Log the distance to the closest point.** `getDistance()` gives you a `double`. Log it
  under the key `ClosestDistance/DistanceMeters`, the same way you logged the points. A
  number is easiest to read on a line graph in AdvantageScope.
- **Add more points to the field.** Add more `new Translation2d(...)` lines inside the
  `List.of(...)` at the top of the class. Put a comma between each one, but not after the
  last one. Keep them inside the field, then check that your closest-point code still works
  with the new points.
