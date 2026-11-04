# Closest Distance

This is your first robot project. Until now, your programs ran once in the console and then
ended. A robot program keeps running, and instead of reading printed text, you can watch
your values on a picture of the field in a tool called **AdvantageScope**.

This lesson uses WPILib's `Translation2d`, which is a point on the field, together with
loops and a bit of geometry.

## What you're making

A program that picks a random spot on the field and finds which of the field points is
closest to it. When you're done, AdvantageScope will look like this:

![The finished 2D field, with a line through all the field points and two glowing spots](images/fullfield.png)

The line connects the six field points, so each corner of the line is one field point. The
two glowing spots are the random point and the closest field point. Notice that one of them
sits right on a corner of the line.

## Where does my code go?

A robot project has a lot of files, but you only need one of them:
`src/main/java/frc/robot/RobotContainer.java`. Everything else is already set up for you.

Open that file and find the comment that says `// Start working here!`. It's inside a method
called `robotPeriodic()`, at the bottom of the file. Almost all of your code goes there.

There's no `main` method to write this time. The robot calls `robotPeriodic()` over and over,
about 50 times every second, for as long as the robot program is running.

A few things are already set up for you near the top of the class:

| Name                  | What it is                                    |
| --------------------- | --------------------------------------------- |
| `fieldPoints`         | a `List` of six `Translation2d` field points  |
| `FIELD_LENGTH_METERS` | how long the field is (`17.55`), along x      |
| `FIELD_WIDTH_METERS`  | how wide the field is (`8.05`), along y       |

## How to run your code

Robot projects don't use the Run button. Use the **Driver Station** at the bottom of the page:

1. Click **Start**.
2. Wait for **Comms** and **Robot Code** to turn green.

That's all you need for this lesson. You don't have to click **Enable**, because
`robotPeriodic()` runs even when the robot is disabled.

Anything you print shows up in the **Console** tab. After you change your code, click
**Restart** to run the new version.

## Steps

### 1. Hardcode a point to check

A `Translation2d` holds an x and a y, both in meters. One corner of the field is `(0, 0)`.
x runs along the long side of the field and y runs along the short side.

Add this line inside `robotPeriodic()`, right under the `// Start working here!` comment:

```java
Translation2d targetPoint = new Translation2d(8.0, 4.0);
```

This makes a point at x = 8.0 meters and y = 4.0 meters. You can change those two numbers to
any spot inside the field.

> **Think about it:** look at the six `fieldPoints` at the top of the class. Before you write
> any more code, guess which one is closest to your point. You'll use that guess to check
> your work in step 2.

### 2. Loop through `fieldPoints` and find the closest one

**The distance formula.** The straight-line distance between two points `(x1, y1)` and
`(x2, y2)` is:

```
distance = √( (x2 − x1)² + (y2 − y1)² )
```

You'll write this in Java yourself, using these pieces:

- `point.getX()` and `point.getY()` give you the x and y of a point. They work on
  `targetPoint` too.
- `Math.sqrt(49)` takes a square root, which here gives `7.0`.
- Java has no symbol for squaring, so multiply a number by itself: `3.5 * 3.5`.

**Finding the closest.** Think about finding the shortest pencil in a pile. You hold the
shortest one you've seen *so far*. You look at each pencil in turn, and if it's shorter than
the one in your hand, you swap. When you reach the end of the pile, the one in your hand is
the answer.

Your code works the same way. Add this right after your `targetPoint` line, then replace the
comment inside the loop with your own code. The program won't run until you do, because the
`if` needs a variable called `distance`.

```java
Translation2d closestPoint = fieldPoints.get(0);
double closestDistance = Double.MAX_VALUE;

for (Translation2d point : fieldPoints) {
  // Use the distance formula to find how far point is from targetPoint.
  // Store the answer in a double called distance.

  if (distance < closestDistance) {
    closestDistance = distance;
    closestPoint = point;
  }
}

System.out.println(closestPoint);
```

Here's what each part does:

- `closestPoint` and `closestDistance` are the pencil in your hand. They're created *before*
  the loop so they keep their values the whole way through. If they were inside the loop,
  they'd start over every time around.
- `fieldPoints.get(0)` is the first point in the list. Lists start counting at 0.
- `Double.MAX_VALUE` is the biggest number a `double` can hold, so the very first point
  always beats it.
- `for (Translation2d point : fieldPoints)` is a *for-each* loop. It runs its code once for
  every item in the list. Each time around, `point` holds the next item.
- The `if` checks whether this point is closer than the best one so far. If it is, the point
  becomes the new best.

> **Check your work:** click **Start** and look at the **Console** tab. A `Translation2d`
> prints like `Translation2d(X: 3.50, Y: 6.40)`. Does it match your guess from step 1? Since
> `robotPeriodic()` runs about 50 times a second, the same line prints over and over. That's
> fine for a quick check. You'll switch to logging in step 4.

### 3. Use a random point instead

`Math.random()` gives you a random decimal from `0.0` up to (but not including) `1.0`.
Multiplying it by the size of the field gives you a random spot that's always inside the
field.

If you picked the random point inside `robotPeriodic()`, you'd get a brand-new spot 50 times
a second. To get one spot that stays put, create it outside of any method, the same way
`fieldPoints` is created. That code only runs once, when the program starts.

First, **delete** the `Translation2d targetPoint = ...` line from `robotPeriodic()`. Then add
this near the top of the class, right after the `fieldPoints` list:

```java
private final Translation2d targetPoint =
    new Translation2d(Math.random() * FIELD_LENGTH_METERS, Math.random() * FIELD_WIDTH_METERS);
```

Your loop doesn't need to change, because the variable still has the same name.

> **Check your work:** click **Restart** a few times. The point in the **Console** should
> change between some of the runs, because each run picks a new random spot.

### 4. Log the points so you can see them on the field

`Logger.recordOutput` sends a value to AdvantageScope. It needs an import, so add this line
at the top of the file with the other imports:

```java
import org.littletonrobotics.junction.Logger;
```

Next, delete your `System.out.println(closestPoint);` line and put these three lines in its
place, at the end of `robotPeriodic()`:

```java
Logger.recordOutput("ClosestDistance/FieldPoints", fieldPoints.toArray(new Translation2d[0]));
Logger.recordOutput("ClosestDistance/RandomPoint", targetPoint);
Logger.recordOutput("ClosestDistance/ClosestPoint", closestPoint);
```

The first part of each line is the name (the **key**) that the value shows up under. Slashes
in the key group values into folders. The second part is the value to send.

`recordOutput` can't log a `List`, so the `toArray` part of the first line turns the list
into an array, which it can log.

Now set up the field view. You only need to do this once:

1. Click **Restart** in the Driver Station, and wait for **Comms** and **Robot Code** to turn
   green.
2. Click **AdvantageScope** in the top bar of CodeRunner, if it isn't already open.
3. Inside AdvantageScope, click the **2D Field** tab. You'll see a picture of the field, with
   a section called **Poses** underneath it.
4. In the list on the left, open `AdvantageKit`, then `RealOutputs`, then `ClosestDistance`.
   Click the arrow next to each one to open it. You should see your three keys.
5. Drag `FieldPoints` from the list and drop it on the **Poses** section. It should say
   **Trajectory**, and a line should appear on the field. If it says **Heatmap** instead,
   click the colored icon to the left of it and choose **Trajectory**.

   ![The menu for FieldPoints, with Trajectory checked](images/trajectory.png)

6. Drag `RandomPoint` and `ClosestPoint` onto the **Poses** section too. They should both say
   **Heatmap**. A heatmap draws a glowing spot wherever a point has been.
7. The heatmaps won't show up yet, because they only count time when the robot is enabled.
   Click the red map icon to the left of `RandomPoint` and choose **Visible Range**. Do the
   same for `ClosestPoint`.

   ![The menu for a heatmap, with Visible Range checked](images/heatmap.png)

> **Check your work:** your field should look like the picture at the top of this lesson,
> with the glowing spots in different places, since your random point is different. One spot
> should sit right on a corner of the line, and it should be the corner nearest the other
> spot.

### 5. Replace your formula with `getDistance()`

Measuring the distance between two points is so common that `Translation2d` can do it for
you. In your loop, delete the code you wrote for the distance formula, and put this one line
in its place:

```java
double distance = point.getDistance(targetPoint);
```

> **Check your work:** click **Restart**. The closest point should still sit on the corner
> nearest the random point. If it doesn't, check the code you just changed.

### 6. Replace the whole loop with `nearest()`

`Translation2d` can also search a list for you. `nearest()` returns whichever point in the
list is closest.

Delete the `closestPoint` line, the `closestDistance` line, and the whole `for` loop. Put
this one line in their place:

```java
Translation2d closestPoint = targetPoint.nearest(fieldPoints);
```

Keep your logging. `robotPeriodic()` should now look like this:

```java
public void robotPeriodic() {
  // Start working here!
  Translation2d closestPoint = targetPoint.nearest(fieldPoints);

  Logger.recordOutput("ClosestDistance/FieldPoints", fieldPoints.toArray(new Translation2d[0]));
  Logger.recordOutput("ClosestDistance/RandomPoint", targetPoint);
  Logger.recordOutput("ClosestDistance/ClosestPoint", closestPoint);
}
```

> **Check your work:** click **Restart**. Again, the closest point should still sit on the
> corner nearest the random point.

> **Think about it:** your loop from step 2 is now one line. Is it still worth knowing how to
> write the loop yourself? (`nearest()` is running a loop just like yours behind the scenes.)

## Bonus

- **Log the distance to the closest point.** Add this line with your other logging, then
  drag `DistanceMeters` onto the **Line Graph** tab in AdvantageScope:

  ```java
  Logger.recordOutput("ClosestDistance/DistanceMeters", targetPoint.getDistance(closestPoint));
  ```

- **Make a new random point every loop.** Move the `targetPoint` code to the top of
  `robotPeriodic()`, and delete `private final` from the start of it. What happens to the two
  heatmaps?
- **Add more points to the field.** Add more `new Translation2d(...)` lines inside the
  `List.of(...)` at the top of the class. Put a comma between each one, but not after the
  last one. Keep them inside the field, then check that your closest-point code still works
  with the new points.
