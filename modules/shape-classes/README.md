# Perimeter and Area Part 2

Work in `src/main/java/frc/robot/RobotContainer.java` and create new classes in the same
package. Run this lesson with the CodeRunner Driver Station.

In this lesson you'll calculate the perimeter and area of shapes, where each kind of shape
is its own **class**. A class is a blueprint. It describes what a thing *remembers* (its
**fields**) and what it can *do* (its **methods**). Once you have a blueprint, you can build
as many **objects** from it as you like: a `Rectangle` class can make a 3-by-5 rectangle, a
10-by-2 rectangle, and so on.

**Where does my code go?** Each shape class gets its own new file. Then, in
`RobotContainer.java`, the `robotPeriodic()` method creates shapes and logs their values. The
robot calls `robotPeriodic()` over and over, about 50 times every second.

## What you're making

`Rectangle`, `Triangle`, and `Circle` classes that each know how to calculate their own
perimeter and area. You'll create a few shapes and see their measurements, perimeter, and
area logged in AdvantageScope, grouped by shape (for example `Rectangle/Area`).

## Steps

The examples below build a `Battery` class one piece at a time. Your `Rectangle` follows the
same pattern.

### 1. Create a `Rectangle` class

Make a new file called `Rectangle.java` in `src/main/java/frc/robot/`, the same folder as
`RobotContainer.java`. Every file in that folder starts with the same `package` line:

```java
package frc.robot;

public class Battery {
  private double voltage;
  private double capacityAmpHours;
}
```

The two lines inside the braces are **fields**: values every `Battery` object remembers.
`private` means only code inside this class can use them directly.

In `Rectangle.java`, create the `Rectangle` class. What does every rectangle need to
remember? Add those as `double` fields.

> **Common mistake:** the file name and the class name must match exactly, including the
> capital letter. `Rectangle.java` holds `public class Rectangle`. If they don't match, Java
> won't compile.

### 2. Give it a constructor that takes `length` and `width`

A **constructor** runs once, when an object is created. It has the same name as the class
and no return type. Its job is to fill in the fields:

```java
public class Battery {
  private double voltage;
  private double capacityAmpHours;

  public Battery(double voltage, double capacityAmpHours) {
    this.voltage = voltage;
    this.capacityAmpHours = capacityAmpHours;
  }
}
```

The constructor's inputs (its **parameters**) have the same names as the fields. `this.voltage`
means "the field," and plain `voltage` means "the parameter that was passed in."

Add a constructor to `Rectangle` that takes a `length` and a `width` and stores them in your
fields.

> **Common mistake:** writing `voltage = voltage;` without `this.` copies the parameter onto
> itself and never touches the field. The field stays `0.0`.

### 3. Add public `perimeter()` and `area()` methods

A method that gives back an answer says what type it returns, and uses `return` to hand the
answer back. Methods can use the object's own fields:

```java
public double energyWattHours() {
  return voltage * capacityAmpHours;
}
```

`public` means other classes (like `RobotContainer`) are allowed to call it.

Add a `perimeter()` method and an `area()` method to `Rectangle`. Both return a `double`.

- **Perimeter** is the distance all the way around the outside. Picture walking around the
  rectangle: how many sides do you walk, and how long is each one?
- **Area** is the amount of space inside.

> **Check your work:** work it out by hand first. A rectangle with length `3` and width `5`
> should have perimeter `16.0` and area `15.0`. You'll see your real answers in step 4.

### 4. Create a rectangle and log it in `RobotContainer.java`

Use `new` to build an object from a class, then call its methods with a dot:

```java
Battery fresh = new Battery(12.8, 18.0);
double energy = fresh.energyWattHours(); // 230.4
```

To send a value to AdvantageScope, use `Logger.recordOutput`. It needs an import at the top
of `RobotContainer.java`:

```java
import org.littletonrobotics.junction.Logger;
```

The first part is the name (the **key**) the value shows up under, and the slash groups
values into a folder:

```java
Logger.recordOutput("Battery/Energy", fresh.energyWattHours());
```

In `robotPeriodic()`, create a `Rectangle` and log its length, width, perimeter, and area
under `Rectangle/` (for example `Rectangle/Length` and `Rectangle/Area`).

> **Think about it:** `length` and `width` are `private`, so `RobotContainer` can't read them
> from your rectangle. You could log the numbers you passed into the constructor, or you
> could add a small **getter** method to `Rectangle`, such as `getLength()`, that just
> returns the field. If you later change the rectangle's size, which way means changing
> only one number?

> **Check your work:** run the Driver Station, open AdvantageScope, and find your `Rectangle`
> keys (they show up inside `RealOutputs`). A 3-by-5 rectangle should show `16.0` and `15.0`.

### 5. Create `Triangle` and `Circle` classes and log them too

Make a `Triangle.java` and a `Circle.java`, each with its own fields, constructor,
`perimeter()`, and `area()`. Then create one of each in `robotPeriodic()` and log their
measurements, perimeter, and area under `Triangle/` and `Circle/`.

**Triangle (base and height).** Treat it as a *right* triangle, the kind with a square
corner.

- Area is one half of base times height. Be careful how you write "one half" in Java:

  ```java
  System.out.println(7 / 2);   // 3   <- two ints: Java drops the decimal!
  System.out.println(7.0 / 2); // 3.5 <- a double is involved, so you keep it
  ```

  What does `1 / 2` equal in Java? What would that do to your area?

- For the perimeter you need all three sides, but you only have two. The third side (the
  long, slanted one) comes from the Pythagorean theorem: `c² = a² + b²`. `Math.sqrt(49)`
  gives `7.0`.

**Circle (radius).**

- Perimeter (for a circle it's called the *circumference*) is `2 × π × radius`.
- Area is `π × radius²`.
- Java has π built in as `Math.PI`. Don't type out `3.14`.

> **Check your work:**
>
> | Shape     | Input                | Perimeter      | Area           |
> | --------- | -------------------- | -------------- | -------------- |
> | Rectangle | length 3, width 5    | 16.0           | 15.0           |
> | Triangle  | base 3, height 4     | 12.0           | 6.0            |
> | Circle    | radius 2             | about 12.566   | about 12.566   |
>
> (Yes, for a circle with radius 2, the perimeter and area really are the same number.)

## Bonus

- **Create a `Shape` interface with `name`, `perimeter`, and `area`.** An **interface** is a
  list of methods that a class promises to have. It goes in its own file, just like a class:
  ```java
  package frc.robot;

  public interface Mechanism {
    String name();

    double currentDrawAmps();
  }
  ```
  Notice the methods have no body, just a `;`. The classes fill in the bodies.
- **Make every shape class implement `Shape`.** Add `implements` after the class name, then
  make sure the class has every method the interface lists:
  ```java
  public class Intake implements Mechanism {
    public String name() {
      return "Intake";
    }

    public double currentDrawAmps() {
      return 20.0;
    }
  }
  ```
  Your shapes already have `perimeter()` and `area()`. What do they still need? (The methods
  must be `public`, or Java will complain.)
- **Store the shapes in a `Shape[]` and use a loop to log every shape.** Because every shape
  *is a* `Shape`, one array can hold all of them:
  ```java
  Mechanism[] mechanisms = {new Intake(), new Shooter()};
  for (Mechanism m : mechanisms) {
    System.out.println(m.name() + " draws " + m.currentDrawAmps() + " amps");
  }
  ```
  Your loop needs to log under `Rectangle/`, `Triangle/`, and `Circle/` without knowing
  which shape it has. How could you build the key from the shape's `name()`?
