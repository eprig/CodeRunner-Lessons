# PID Controllers

This project starts from the AdvantageKit kitbot template with `FieldConstants.BLUE_HUB` defined.

Work in these files, all under `src/main/java/frc/robot/`:

| File                                            | What you'll do there                 |
| ----------------------------------------------- | ------------------------------------ |
| `subsystems/superstructure/Superstructure.java` | the shooter flywheel PID (steps 1-3) |
| `commands/DriveCommands.java`                   | the aiming PID command (step 4)      |
| `RobotContainer.java`                           | bind the aiming command to a button  |

Run this lesson with the CodeRunner Driver Station and enable teleop. The robot's position
is logged as `Odometry/Robot`, so put it on a 2D field view in AdvantageScope to watch it.

**What is a PID controller?** It's a bit of math that pushes something toward a goal. You
give it a **setpoint** (where you want to be) and a **measurement** (where you are now). The
difference between them is the **error**. Every loop, the controller turns the error into an
**output**, like a voltage for a motor. It adds up three parts:

| Part  | Looks at                       | What it does                                           |
| ----- | ------------------------------ | ------------------------------------------------------ |
| **P** | the error right now            | pushes harder the farther away you are                 |
| **I** | the error added up over time   | slowly fixes a small error that never goes away        |
| **D** | how fast the error is changing | brakes when you're closing in fast, to stop overshoot  |

How much of each part you use is set by three numbers called the **gains**: `kP`, `kI`, and
`kD`. Picking good gains is called **tuning**.

WPILib's `PIDController` does the math for you:

```java
import edu.wpi.first.math.controller.PIDController;
```

```java
PIDController armPID = new PIDController(0.8, 0.0, 0.1); // kP, kI, kD
double volts = armPID.calculate(armAngle, targetAngle);  // measurement first, then setpoint
```

`calculate` has to be called **every loop** with a fresh measurement. Each call gives you the
output for that moment.

## What you're making

Two PID controllers for the kitbot. One holds the shooter flywheel at a target speed. The
other turns the whole robot to face the hub (`FieldConstants.BLUE_HUB`). You'll log the
setpoints, measurements, and outputs so you can tune both in AdvantageScope.

## Steps

### 1. Add a `PIDController` for the shooter flywheel

The kitbot uses one motor, the **intake/launcher**, to both pull game pieces in and launch
them out. That's the flywheel. A second motor, the **feeder**, pushes game pieces into it.

Open `Superstructure.java` and read `launch()`. That's the command the right bumper runs.
Right now it sends a fixed voltage, `launchingLauncherVoltage`, to the flywheel. That's
called **open loop**: the code never checks how fast the flywheel is really spinning.

**Pick a target speed first.** The flywheel speed is measured in radians per second. Before
you change anything, enable teleop, hold the right bumper, and look at
`Superstructure/IntakeLauncherVelocityRadPerSec` in AdvantageScope. That's how fast the
fixed voltage spins it. Choose a target a little below that, so the motor has room to reach
it. A good home for the target is a new constant in `SuperstructureConstants.java`, next to
`launchingLauncherVoltage`.

Then add a `PIDController` field to `Superstructure`. Start with a small `kP`, and `0` for
`kI` and `kD`.

> **Think about it:** the error here is measured in hundreds of radians per second, but the
> output is in volts, and a battery only gives about 12. If the error is `400` and `kP` is
> `1`, how many volts does the PID ask for? About what size should `kP` be?

### 2. Use the flywheel speed from the superstructure inputs as the measurement

`Superstructure` has a field called `inputs`. Its `periodic()` method refreshes it every
loop with the latest sensor readings, and logs them under `Superstructure/`. The flywheel's
current speed is:

```java
inputs.intakeLauncherVelocityRadPerSec
```

This is the measurement your PID will use. In AdvantageScope, the same value is
`Superstructure/IntakeLauncherVelocityRadPerSec`.

### 3. Use the PID output to control the shooter voltage

In `launch()`, both `run(...)` blocks call:

```java
io.setIntakeLauncherVoltage(launchingLauncherVoltage);
```

Replace that fixed voltage with your PID's output, using the measurement from step 2 and
your target speed from step 1. The code inside `run(() -> { ... })` runs every loop while
the command is active, so that's where `calculate` belongs.

Leave `intake()` and `eject()` alone. They use the same motor, but they don't need a target
speed.

> **Common mistake:** if you call `calculate` once, outside the `run(...)` lambda, you get
> one number that never changes. The PID only works if it gets a fresh measurement every
> loop.

> **Check your work:** hold the right bumper. The flywheel speed should climb toward your
> target. Raise `kP` a bit at a time and it should get closer, faster.

> **Think about it:** with only `kP`, the flywheel usually settles a little *below* the
> target. When the speed equals the target, the error is `0`, so P asks for `0` volts. What
> does a flywheel do with `0` volts? How could `kI` help? (Another fix teams use is a
> **feedforward**: a base voltage you always send, with the PID output added on top.)

### 4. Add a chassis orientation PID that turns the robot toward the hub

The hub's position is `FieldConstants.BLUE_HUB`, a `Translation2d` defined in
`FieldConstants.java`. The robot's position and heading come from the drive:

| Method                | Gives you                                  |
| --------------------- | ------------------------------------------ |
| `drive.getPose()`     | a `Pose2d`: the robot's x, y, and heading  |
| `drive.getRotation()` | a `Rotation2d`: just the heading           |

**Which way is the hub?** Subtracting two points gives the arrow from one to the other, and
`getAngle()` gives the direction that arrow points:

```java
Translation2d toBall = ballSpot.minus(robotSpot); // arrow from the robot to the ball
Rotation2d ballDirection = toBall.getAngle();     // which way that arrow points
```

`getRadians()` turns a `Rotation2d` into a plain number. For this PID, the setpoint is the
direction to the hub and the measurement is the robot's heading.

**Wraparound.** Angles go around in a circle: 179° and -179° are only 2° apart, but
subtracting them gives 358°. Tell the controller the angle wraps around, and it'll always
take the short way:

```java
headingPID.enableContinuousInput(-Math.PI, Math.PI);
```

**Sending the output to the drive.** The PID's output is a turning speed in radians per
second, with positive meaning counterclockwise (turning left). `ChassisSpeeds` describes how
fast the whole robot should move:

```java
import edu.wpi.first.math.kinematics.ChassisSpeeds;
```

```java
// forward 1 m/s, sideways 0 m/s, turning left at 0.5 rad/s
drive.runClosedLoop(new ChassisSpeeds(1.0, 0.0, 0.5));
```

The middle number is sideways speed, which a tank drive can't do, so keep it `0.0`.

Add a new `static` method to `DriveCommands` that returns an aiming command, the same way
`arcadeDrive` does. Use `Commands.run(..., drive)` so it runs every loop and requires the
drive. Create the `PIDController` inside your method, *before* the `return`. Then the
command's lambda can use it. (`feedforwardCharacterization` in the same file does this with
its `Timer` and lists.)

`DriveCommands` is in a different package, so it needs imports for anything new it uses:
`frc.robot.FieldConstants`, `edu.wpi.first.math.controller.PIDController`,
`edu.wpi.first.math.geometry.Rotation2d`, and `edu.wpi.first.math.geometry.Translation2d`.

In `RobotContainer.configureButtonBindings()`, bind your aiming command to a free button
with `whileTrue`. A and both bumpers are already used.

> **Think about it:** why work out the direction to the hub *inside* the lambda, every loop,
> instead of once when the command is made?

> **Check your work:** drive somewhere, then hold your aim button. The robot should turn to
> face the hub and stay pointed at it. To see the hub on the field, log
> `FieldConstants.BLUE_HUB_POSE` under the key `"Aim/HubPose"` inside your aiming command
> (see step 5 for the `Logger` import) and put it on the 2D field view
> too.

### 5. Log setpoints, measurements, and PID outputs, then tune

`Logger.recordOutput` sends a value to AdvantageScope. It needs
`import org.littletonrobotics.junction.Logger;`, which `Superstructure.java` already has but
`DriveCommands.java` doesn't:

```java
Logger.recordOutput("Arm/SetpointRad", targetAngle);
```

For each PID, log three numbers using these exact keys. Put the lines right after you call
`calculate`, inside the same `run(...)` lambda, so they're logged every loop:

| Key                         | Value                           |
| --------------------------- | ------------------------------- |
| `Shooter/SetpointRadPerSec` | your target flywheel speed      |
| `Shooter/MeasuredRadPerSec` | the flywheel speed you measured |
| `Shooter/OutputVolts`       | what `calculate` returned       |
| `Aim/SetpointRad`           | the direction to the hub        |
| `Aim/MeasuredRad`           | the robot's heading             |
| `Aim/OutputRadPerSec`       | what `calculate` returned       |

`PIDController` can also tell you `getSetpoint()` and `getError()` if you'd like to log those.

**Tuning.** Put each setpoint and measurement on the same line graph so you can see how
close they are. Then change one gain at a time:

1. Raise `kP` until the measurement reaches the setpoint quickly. If it starts to bounce
   back and forth (oscillate), back off a little.
2. If it overshoots, add a small `kD` to brake it.
3. If it settles close but never quite gets there, add a small `kI`.

> **Testing tip:** write down each set of gains you try and what the graph looked like. It's
> easy to forget which numbers worked best.

> **Check your work:** for a well-tuned PID, the measurement line climbs to the setpoint
> line quickly and stays there, without bouncing.

## Bonus

- **Add other field targets, such as passing zones.** Add more `Translation2d` constants to
  `FieldConstants.java`, next to `BLUE_HUB`. Make up spots that make sense on the field.
- **Let the driver choose which target to aim at.** Store the current target in a field of
  `RobotContainer`, and use buttons with `Commands.runOnce(...)` to change it. Your aiming
  command needs to read the target *every loop*, not once when it's created. `arcadeDrive`
  solves the same problem for the stick values by taking a `DoubleSupplier`. A
  `Supplier<Translation2d>` (from `java.util.function.Supplier`) works the same way: you pass
  in `() -> currentTarget`, and the command calls `.get()` on it each loop.
