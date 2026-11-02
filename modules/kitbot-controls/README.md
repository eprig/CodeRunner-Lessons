# Kitbot Controls

This project starts from the AdvantageKit kitbot template with drive speed slowed down.

Work mainly in `src/main/java/frc/robot/RobotContainer.java` (in `configureButtonBindings()`)
and `src/main/java/frc/robot/commands/DriveCommands.java`. Run this lesson with the CodeRunner
Driver Station and enable teleop. The robot's position on the field is logged as
`Odometry/Robot`, so put that on a 2D field view in AdvantageScope to watch it drive.

**How the controls work right now.** The kitbot drivetrain is a *differential* drive (also
called a tank drive): a left side and a right side of wheels. It can drive forward and
backward and turn, but it can't slide sideways. Here's what's already in
`configureButtonBindings()`:

| Control           | What it does                                                         |
| ----------------- | -------------------------------------------------------------------- |
| left stick Y      | drive forward/backward (`DriveCommands.arcadeDrive`, the default)    |
| right stick X     | turn left/right (same command)                                       |
| left bumper       | intake, while held                                                   |
| right bumper      | launch, while held                                                   |
| A                 | eject, while held                                                    |

The top drive speed is `maxSpeedMetersPerSec` in `subsystems/drive/DriveConstants.java`.

**Commands and requirements.** A command that controls the drive **requires** the `drive`
subsystem. Only one command can use a subsystem at a time, so when a new command that
requires `drive` starts, it interrupts whatever was using `drive` before. Arcade drive is
the drive's **default command**: it runs whenever no other command is using the drive, and
it comes back automatically when the other command ends.

## What you're making

New driver controls for the kitbot: a **fast mode** button that doubles drive speed while
held, and a **beyblade mode** button that spins the robot in place while held. By the end,
the driver should still be able to drive around the field while the robot spins.

## Steps

### 1. Add a fast-mode button that doubles drive speed while held

`whileTrue(...)` starts a command when a button is pressed and cancels it when the button
is released. The superstructure bindings already use it:

```java
controller.leftBumper().whileTrue(superstructure.intake());
```

So fast mode can be *another* driving command, bound with `whileTrue`. While you hold the
button, it interrupts the normal arcade drive. When you let go, arcade drive comes back.

Open `DriveCommands.java` and read `arcadeDrive`. Find the line where it turns the joystick
values into a speed in meters per second. That's where the top speed comes in.

- How could you make a version of `arcadeDrive` that uses a different top speed, without
  copying the whole method? (Hint: a method can take an extra parameter, and the caller
  decides what to pass in.)
- If you change `arcadeDrive`'s parameters, what else that calls it has to change too?

Pick a free button (for example `b()`, `x()`, `y()`, `leftTrigger()`, or `rightTrigger()`)
and bind fast mode to it with `whileTrue`.

> **Common mistake:** multiplying the joystick value by 2 doesn't work. `arcadeDriveIK`
> clamps its inputs to the range -1 to 1, so a stick value of `2.0` gets cut back to `1.0`.
> The doubling has to happen *after* that.

> **Check your work:** `Drive/LeftSetpointRadPerSec` is the wheel speed the drive is asking
> for. With the left stick pushed all the way, it should be about twice as big while you
> hold the fast-mode button. The robot on the field should move faster too.

### 2. Make the robot spin in place as fast as possible

To drive straight, both sides of a tank drive go the same direction. To spin in place, the
two sides go *opposite* directions. Here are the `Drive` methods you can use:

| Method                                                     | What it does                          |
| ---------------------------------------------------------- | ------------------------------------- |
| `drive.runClosedLoop(leftMetersPerSec, rightMetersPerSec)` | asks each side for a speed            |
| `drive.runOpenLoop(leftVolts, rightVolts)`                 | sends a voltage straight to each side |
| `drive.stop()`                                             | sends 0 volts to both sides           |

- "As fast as possible" means as much power as the motors can get. A full robot battery
  gives about 12 volts. Which method lets you ask for that directly?
- If the left side goes forward and the right side goes backward, which way does the robot
  spin?

### 3. Start beyblade mode as a command that requires the drive

`Commands.run(...)` makes a command that runs some code every loop until it's cancelled.
Anything you list after the code is a subsystem the command requires:

```java
Commands.run(() -> drive.stop(), drive) // a "hold still" command that requires drive
```

`() -> drive.stop()` is a **lambda**: a small piece of code you hand to the command to run
later. You can put a call like the ones from step 2 in its place.

Make a beyblade command that spins the robot in place and requires `drive`, and bind it to a
free button with `whileTrue`. You can write it right in `configureButtonBindings()`, or add
a new `static` method to `DriveCommands` that returns it, the same way `arcadeDrive` does.

> **Think about it:** what would happen if you left `drive` off the end? Arcade drive would
> keep running too, and both commands would send the motors orders every loop. Which order
> wins?

> **Check your work:** hold the button and the robot should spin in place. Let go and it
> should stop spinning and drive normally again.

### 4. Challenge: keep translation control while the robot spins

Now the hard part. **Translation** means moving across the field, not just turning. While
beyblade mode is held, the driver should still be able to push the left stick to move the
robot across the field.

There's a catch: the robot is spinning, so "forward" for the robot keeps changing. Instead,
the stick should be **field-relative**: pushing the stick *away from you* means "move toward
the far end of the field" no matter which way the robot is facing.

A tank drive can't slide sideways, but a spinning tank drive has a trick. Think about it in
pieces:

- When the robot happens to face the direction you want to go, should it drive forward or
  backward?
- When it faces the *opposite* direction?
- When it faces sideways to that direction?

The cosine of an angle does exactly this. It's `1` at 0°, `0` at 90°, and `-1` at 180°:

```java
double c = Math.cos(Math.PI); // -1.0   (Math.cos uses radians)
Rotation2d heading = drive.getRotation();
double c2 = heading.getCos();  // the cosine of the robot's heading
```

A `Rotation2d` can be subtracted from another with `minus(...)`, which gives you the angle
between them. And a joystick's x and y can become a direction with a `Translation2d`:

```java
Translation2d stick = new Translation2d(fieldX, fieldY);
Rotation2d direction = stick.getAngle(); // which way it points
double howFar = stick.getNorm();         // how far it's pushed, 0 to about 1
```

If you use these classes in `DriveCommands.java`, add their imports:
`edu.wpi.first.math.geometry.Rotation2d` and `edu.wpi.first.math.geometry.Translation2d`.

Some things to work out:

- Field x points away from the blue driver station. When the driver pushes the stick up,
  `getLeftY()` gives a **negative** number, and pushing left gives a negative `getLeftX()`.
  How do the stick values turn into field x and field y?
- How much should you add to (or subtract from) each side's spin to move in the right
  direction?
- If the spin already uses the full 12 volts, there's no room left to add more. What could
  you change about the spin speed in this mode?

> **Testing tip:** in AdvantageScope, watch `Odometry/Robot`. Spinning in place keeps the
> robot in one spot. When translation works, holding the stick in one direction should make
> the robot drift across the field in that direction while it spins.

## Bonus

- **Add a button that toggles between blue-alliance and red-alliance driving modes.** A
  `boolean` field can remember the mode, and `Commands.runOnce(...)` runs a quick action one
  time:
  ```java
  controller.back().onTrue(Commands.runOnce(() -> slowMode = !slowMode));
  ```
  This only works on a **field** of the class. A lambda can't change a local variable.

  Log the mode under the key `"RedAllianceMode"` (`true` when red mode is on) so you can
  see it in AdvantageScope. A lambda can run more than one line if you wrap it in `{ }`, so
  you can log right after you flip the mode:
  ```java
  controller.back().onTrue(Commands.runOnce(() -> {
    slowMode = !slowMode;
    Logger.recordOutput("SlowMode", slowMode);
  }));
  ```
  `Logger` needs `import org.littletonrobotics.junction.Logger;` at the top of the file.
- **Mirror the controls when red-alliance mode is active.** Red drivers stand at the other
  end of the field, so "away from me" is the field's **negative** x direction. Which part of
  your field-relative beyblade driving needs to flip? (`Rotation2d.k180deg` is a half turn.)
  Your driving command also needs to read the mode *every loop*, not just once when it's
  created. `arcadeDrive` solves the same problem for the stick values by taking a
  `DoubleSupplier` like `() -> -controller.getLeftY()`. A `BooleanSupplier` works the same
  way for a `boolean`.
