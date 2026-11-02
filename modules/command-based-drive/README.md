# Command-Based Robot

Work mainly in `src/main/java/frc/robot/RobotContainer.java`, `commands/`, and
`subsystems/drive/Drive.java`. Run this lesson with the CodeRunner Driver Station.

Most competition robots are written in the **command-based** style. The robot is split into
**subsystems** (parts of the robot, like a drivetrain or a shooter) and **commands** (actions
those parts can do, like "drive forward 2 meters"). Every loop, WPILib's command scheduler
runs whichever commands are active. `Robot.java` already sets that up, so you don't need to
edit it.

This project has one subsystem, `Drive`, and six command classes in `commands/`:

| Command                                   | What it does                                  |
| ----------------------------------------- | --------------------------------------------- |
| `new DriveForwardCommand(drive, meters)`  | moves forward                                 |
| `new DriveBackwardCommand(drive, meters)` | moves backward                                |
| `new DriveLeftCommand(drive, meters)`     | slides sideways to the left, without turning  |
| `new DriveRightCommand(drive, meters)`    | slides sideways to the right, without turning |
| `new TurnLeftCommand(drive, degrees)`     | turns left (counterclockwise) in place        |
| `new TurnRightCommand(drive, degrees)`    | turns right (clockwise) in place              |

This `Drive` is a simple pretend drivetrain with no motors. Each command moves the robot's
position all at once and then finishes right away. The robot starts at `(2, 2)` on the field,
facing along the field's x direction. Its position is logged as `Drive/Pose`.

## What you're making

A simulated robot that drives itself in a square during autonomous, plus controller buttons
that run driving commands, including a figure-eight path around the field. You'll watch the
robot's path in AdvantageScope.

## Steps

### 1. Turn the autonomous command into a square

Open `RobotContainer.java`. In the constructor, this line sets up the autonomous command:

```java
autoChooser.addDefaultOption("Do Nothing", Commands.none());
```

The text is the name of the option, and `Commands.none()` is a command that does nothing.

`Commands.sequence(...)` makes one command out of several. It runs them one after another,
starting each one when the one before it finishes:

```java
Commands.sequence(
    new DriveBackwardCommand(drive, 1.0),
    new TurnRightCommand(drive, 45.0),
    new DriveBackwardCommand(drive, 0.5))
```

The command classes live in a different package, so each one you use needs an import at the
top of the file, for example:

```java
import frc.robot.commands.DriveBackwardCommand;
```

Replace `Commands.none()` with a sequence that drives in a square. Rename `"Do Nothing"` to
something that describes your new auto, too.

- How many sides does a square have? How many turns?
- How many degrees is each turn?
- If every turn goes the same direction, where does the robot end up when it's done?

> **Think about it:** the robot starts at `(2, 2)`, facing along x. If it turns right after
> the first side, it heads toward y = 0, the edge of the field. With 3-meter sides, would it
> stay on the field? Which way should it turn?

> **Common mistake:** each command object can only go into a sequence once. If you make
> `Command side = new DriveForwardCommand(drive, 2.0);` and put `side` in the sequence four
> times, the robot program stops with an error that starts
> `Cannot compose a command twice in the same composition!`. Write `new ...` for each step
> instead.

### 2. Run autonomous and watch `Drive/Pose`

Run the Driver Station and enable **autonomous**. Open AdvantageScope and find `Drive/Pose`
(it shows up inside `RealOutputs`). Put it on a 2D field view.

> **Testing tip:** each command here finishes right away, and the sequence moves on to the
> next command each loop. That means the whole square takes only a fraction of a second,
> and the robot ends right where it started! Drag the AdvantageScope timeline back to watch
> it step through each corner. To slow it down, put a pause between steps:
> `Commands.waitSeconds(0.5)` is a command that just waits half a second.

> **Check your work:** the robot should visit four corners and end back at `(2, 2)`, facing
> the same way it started.

### 3. Bind the drive commands to controller buttons

Button bindings go in `configureButtonBindings()`. There's already one there:

```java
controller.b().onTrue(Commands.runOnce(drive::resetPose, drive));
```

`controller.b()` is the B button. `onTrue(...)` starts a command once, at the moment the
button is pressed. Here's another example:

```java
controller.start().onTrue(new TurnRightCommand(drive, 180.0)); // spin around
```

Bind each of the six drive commands to its own button with `onTrue`. Choose your own
distances and angles. Leave **A** free for step 4 and keep **B** as it is. Some other
buttons on `controller`:

| Method                                            | Button                   |
| ------------------------------------------------- | ------------------------ |
| `x()`, `y()`                                      | X and Y face buttons     |
| `leftBumper()`, `rightBumper()`                   | the bumpers              |
| `povUp()`, `povDown()`, `povLeft()`, `povRight()` | the D-pad                |
| `start()`, `back()`                               | the small middle buttons |

> **Check your work:** enable **teleop** (buttons don't run commands while the robot is
> disabled). Each press should move or turn the robot exactly once. Holding the button down
> shouldn't keep it moving.

### 4. Create a figure-eight path and bind it to A

A figure eight is two loops that touch in the middle: one loop turning left, and one loop
turning right. With the commands you have, each loop can be a square (or a shape with more,
smaller turns if you want it rounder).

Sketch the path on paper first. Start at the crossing point, facing along x.

- Which way do you turn for the first loop? Which way for the second?
- Where does the robot need to be, and which way does it need to face, after the first loop?
- Can you make it big enough to go "around the field" but still stay on it? The field is
  about 16.5 m long and 8 m wide.

Build the path with `Commands.sequence(...)` and bind it to A with `onTrue`.

> **Think about it:** your two loops are almost the same list of commands, with left and
> right swapped. Remember, you can't reuse the same command objects. A small method that
> *builds and returns* a new sequence each time you call it is one way to avoid typing the
> list twice.

> **Check your work:** press A. The robot should trace two loops and end back where it
> started, facing the same way.

### 5. Keep B as the reset-pose button

The B binding from step 3 calls `drive.resetPose()`, which jumps the robot back to its
starting spot at `(2, 2)`. `Commands.runOnce(...)` turns one quick action into a command,
and `drive::resetPose` means "call `resetPose` on `drive`."

Keep it as is, and test it: drive around with your buttons, press B, then press A to run the
figure eight again from the start.

> **Check your work:** after pressing B, `Drive/Pose` should be back at `(2, 2)`, facing
> along x.

## Bonus

- **Try `whileTrue` by moving forward in very small increments.** `whileTrue(...)` starts a
  command when the button is pressed and cancels it when the button is released. But it
  doesn't restart a command that finishes on its own, and these commands finish right away.
  `.repeatedly()` makes a command start over every time it finishes:
  ```java
  new TurnLeftCommand(drive, 2.0).repeatedly()
  ```
  What distance per step gives smooth, slow driving while you hold the button? (Remember,
  each step happens once per loop, 50 times a second.)
- **Add more command classes for other driving patterns.** Copy `DriveForwardCommand.java`
  as a starting point, and rename the class *and* the file. A command can also do a little
  bit every loop: put code in an `execute()` method (it runs every loop while the command
  is active) and make `isFinished()` return `true` only when the pattern is done.
