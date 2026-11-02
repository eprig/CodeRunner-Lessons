# PID Controllers

This project starts from the AdvantageKit kitbot template with `FieldConstants.BLUE_HUB` defined.

## What you're making

Two PID controllers for the kitbot. One holds the shooter flywheel at a target speed. The
other turns the whole robot to face the hub (`FieldConstants.BLUE_HUB`). You'll log the
setpoints, measurements, and outputs so you can tune both in AdvantageScope.

## Steps

1. Add a `PIDController` for the shooter flywheel.
2. Use the current flywheel speed from the superstructure inputs as the PID measurement.
3. Use the PID output to control the shooter voltage.
4. Add a chassis orientation PID that turns the robot toward `FieldConstants.BLUE_HUB`.
5. Log setpoints, measurements, and PID outputs so you can tune in AdvantageScope.

## Bonus

- Add other field targets, such as passing zones.
- Let the driver choose which target to aim at.
