# Timed Robot

Work in `src/main/java/frc/robot/RobotContainer.java`. Run this lesson with the CodeRunner
Driver Station.

A robot program doesn't run from top to bottom once and then stop. Instead, the robot calls
certain methods for you at certain times. In this lesson you'll use two of them:

| Method             | When it runs                                                      |
| ------------------ | ----------------------------------------------------------------- |
| `teleopInit()`     | **once**, each time teleop is enabled                             |
| `teleopPeriodic()` | **over and over** while teleop is enabled, about every 20 ms      |

Both methods are already in `RobotContainer.java`, with a comment saying what goes in each.
`Robot.java` calls them at the right times, so you don't need to edit it.

## What you're making

A robot program that shows the difference between code that runs once and code that runs
every loop. You'll log which robot state is running, blink a pretend LED on a timer, and log
live controller input (a button and a joystick), then watch all of it change in
AdvantageScope.

## Steps

### 1. Log `"RobotState"` as `"TeleopInit"` in `teleopInit()`

`Logger.recordOutput` sends a value to AdvantageScope. It needs an import at the top of the
file:

```java
import org.littletonrobotics.junction.Logger;
```

The first part is the name (the **key**) the value shows up under. The second part is the
value. It can be text, a number, a `true`/`false`, and more:

```java
Logger.recordOutput("Intake/Status", "Running"); // text
Logger.recordOutput("Battery/Volts", 12.4);      // a number
```

In `teleopInit()`, log the key `"RobotState"` with the text `"TeleopInit"`.

### 2. Log `"RobotState"` as `"TeleopPeriodic"` in `teleopPeriodic()`

In `teleopPeriodic()`, log the **same** key, `"RobotState"`, with the text
`"TeleopPeriodic"`.

> **Check your work:** run the Driver Station, enable teleop, and open AdvantageScope. Find
> `RobotState` (it shows up inside `RealOutputs`). It should say `TeleopPeriodic`.

> **Think about it:** you probably never see `TeleopInit`. It was logged! So where did it
> go? How long is it until `teleopPeriodic()` runs and logs over it? Try zooming in on the
> AdvantageScope timeline right at the moment you enabled teleop.

### 3. Blink an LED by flipping a boolean every loop

A `boolean` is `true` or `false`. The `!` operator flips it:

```java
intakeDeployed = !intakeDeployed; // false becomes true, true becomes false
```

The pretend LED needs to **remember** whether it's on from one loop to the next, so it
can't be a variable inside `teleopPeriodic()`. Create it as a **field** instead: inside the
class, but outside of any method (next to the constructor is a good spot):

```java
private boolean intakeDeployed = false;
```

Create a `boolean` field for the LED. Then, in `teleopPeriodic()`, flip it and log it with
a key like `"LED"`.

> **Common mistake:** if you create the boolean *inside* `teleopPeriodic()`, it's created
> fresh every loop with the same starting value. Flip it once and it ends up the same every
> single time.

> **Check your work:** in AdvantageScope, put `LED` on a line graph and zoom way in. It
> should switch between `true` and `false` on every loop.

> **Think about it:** how many times a second does your LED flip? Could a person even see a
> real LED blinking that fast?

### 4. Blink more slowly using `Timer.getFPGATimestamp()`

`Timer.getFPGATimestamp()` tells you how many seconds have passed since the robot program
started, as a `double`. It needs an import:

```java
import edu.wpi.first.wpilibj.Timer;
```

Subtracting two times tells you how long something has been happening:

```java
double elapsed = Timer.getFPGATimestamp() - intakeStartTime;
if (elapsed > 2.0) {
  // the intake has been running for more than 2 seconds
}
```

Change your LED so it flips only every half second (or any slower timing you like).

- You'll need to remember *when* the LED last flipped. Should that be a field or a variable
  inside `teleopPeriodic()`?
- Each loop, how can you tell whether enough time has passed since the last flip?
- When you do flip the LED, what else needs to be updated?

> **Testing tip:** also log the elapsed time while you're working on this. Watching it climb
> and reset in AdvantageScope makes it much easier to see what your code is doing.

> **Check your work:** on a line graph, `LED` should now stay `true` for half a second, then
> `false` for half a second, and so on.

### 5. Create an Xbox controller object

To read a controller, create an object for it. Here's what a flight-stick style joystick
looks like:

```java
private final Joystick flightStick = new Joystick(1);
```

The number is the controller's **port**: which slot it's in on the Driver Station. The first
controller is port `0`.

The Xbox version of this class is called `XboxController`, and it needs an import:

```java
import edu.wpi.first.wpilibj.XboxController;
```

Create an `XboxController` field on port `0`.

> **Think about it:** why create the controller as a field instead of inside
> `teleopPeriodic()`? How many controller objects would you make in one minute of driving?

### 6. Log whether the A button is pressed

`XboxController` has a method for every button and stick:

| Method                                         | Gives you                                      |
| ---------------------------------------------- | ---------------------------------------------- |
| `getAButton()`                                 | `true` while A is held down, otherwise `false` |
| `getBButton()`, `getXButton()`, `getYButton()` | the same for B, X, and Y                       |
| `getLeftX()`                                   | the left stick's left/right position, -1 to 1  |
| `getLeftY()`                                   | the left stick's up/down position, -1 to 1     |
| `getRightX()`                                  | the right stick's left/right position          |
| `getRightY()`                                  | the right stick's up/down position             |

In `teleopPeriodic()`, log whether the A button is pressed, with a key like `"Controller/A"`.

> **Check your work:** with teleop enabled, hold A. The value in AdvantageScope should be
> `true` only while you're holding it.

### 7. Log a joystick value

In `teleopPeriodic()`, log one of the joystick values from the table, with a key like
`"Controller/LeftY"`. Then use keyboard controls to change it.

> **Check your work:** the value should sit near `0` when you let go, and reach `-1` or `1`
> when you push all the way.

> **Think about it:** push the stick *up*. Is Y positive or negative? It surprises most
> people. This is why robot code often puts a minus sign in front of `getLeftY()`.

## Bonus

- **Make a boolean turn true only when all four face buttons are pressed.** `&&` means
  "and": the result is `true` only if both sides are `true`.
  ```java
  boolean readyToShoot = hasGamePiece && shooterAtSpeed;
  ```
  You can chain more than two with more `&&`. Log your boolean and test it by pressing
  A, B, X, and Y together.
- **Log a `Pose2d` and increment X/Y when the joysticks move.** A `Pose2d` is a robot's
  position on the field (x and y in meters) plus which way it's facing. You'll need these
  imports:
  ```java
  import edu.wpi.first.math.geometry.Pose2d;
  import edu.wpi.first.math.geometry.Rotation2d;
  ```
  ```java
  Pose2d start = new Pose2d(2.0, 3.0, Rotation2d.kZero); // x, y, facing angle
  double x = start.getX(); // 2.0
  ```
  A `Pose2d` can't be changed after it's made, so to "move" it you make a *new* `Pose2d`
  from the old one's values. Where should the pose be stored so it's remembered between
  loops? Each loop, how much should you add to x and y? (Hint: the stick goes up to `1`, and
  the loop runs 50 times a second. Adding a full `1` meter every loop would be very fast!)
  Log the pose and put it on a 2D field view in AdvantageScope to watch it move.
