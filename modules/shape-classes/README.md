# Perimeter and Area Part 2

Work in `src/main/java/frc/robot/RobotContainer.java` and create new classes in the same package.

## What you're making

`Rectangle`, `Triangle`, and `Circle` classes that each know how to calculate their own
perimeter and area. You'll create a few shapes and see their measurements, perimeter, and
area logged in AdvantageScope, grouped by shape (for example `Rectangle/Area`).

## Steps

1. Create a `Rectangle` class.
2. Give it a constructor that takes `length` and `width`.
3. Add public `perimeter()` and `area()` methods.
4. In `RobotContainer.java`, create a rectangle and log its length, width, perimeter, and area under `Rectangle/`.
5. Create `Triangle` and `Circle` classes and log their values too.

## Bonus

- Create a `Shape` interface with `name`, `perimeter`, and `area`.
- Make every shape class implement `Shape`.
- Store the shapes in a `Shape[]` and use a loop to log every shape.
