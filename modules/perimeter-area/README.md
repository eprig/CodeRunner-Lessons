# Perimeter and Area

Work in `src/Main.java`. Run this lesson with the editor Run button.

This lesson combines reading input with `Scanner` and choosing between options with
`if`/`else if`. The new parts are decimal numbers and doing math in Java.

## What you're making

A shape calculator. The program shows a menu of shapes, asks for the measurements of the one
the user picks, and prints that shape's perimeter and area. For example:

```
Pick a shape:
1. Rectangle
2. Triangle
3. Circle
1
Length: 3
Width: 5
Perimeter: 16.0
Area: 15.0
```

## Steps

### 1. Ask for a rectangle's length and width

Measurements aren't always whole numbers. A side could be `2.5` long. An `int` only holds
whole numbers, so use **`double`**, which can hold decimals:

```java
double speed = 3.75;
```

To read a decimal from the user, use `Scanner`'s `nextDouble()` method. As a reminder, a
`Scanner` needs an import at the very top of the file:

```java
import java.util.Scanner;
```

Then create it once near the start of `main`, and use it to read each answer:

```java
Scanner input = new Scanner(System.in);

System.out.print("How fast is the robot? ");
double speed = input.nextDouble(); // waits for the user to type a number and press Enter
```

For whole numbers, use `nextInt()` instead (you'll need that in step 3).

Ask the user for a length and a width, and store each one in its own `double` variable.

> **Think about it:** what would happen if you used `nextInt()` and the user typed `2.5`?
> Try it if you're not sure.

### 2. Calculate and print the perimeter and area

Java's math operators:

| Operator | Means                                  | Example   | Result |
| -------- | -------------------------------------- | --------- | ------ |
| `+`      | add                                    | `7 + 2`   | `9`    |
| `-`      | subtract                               | `7 - 2`   | `5`    |
| `*`      | multiply                               | `7 * 2`   | `14`   |
| `/`      | divide                                 | `7.0 / 2` | `3.5`  |
| `%`      | remainder (what's left after dividing) | `7 % 2`   | `1`    |

Java follows the same order of operations as math class, so `*` and `/` happen before `+`
and `-`. Use parentheses to change the order:

```java
double a = 2 + 3 * 4;   // 14.0
double b = (2 + 3) * 4; // 20.0
```

- **Perimeter** is the distance all the way around the outside. Picture walking around the
  rectangle: how many sides do you walk, and how long is each one?
- **Area** is the amount of space inside.

Store each result in its own variable, then print both with labels.

> **Check your work:** length `3` and width `5` should give perimeter `16.0` and area
> `15.0`. Do the math by hand first so you know what to expect.

### 3. Add a shape menu

Print a menu and read the user's choice as an `int`:

```
Pick a shape:
1. Rectangle
2. Triangle
3. Circle
```

Then use an `if` / `else if` / `else` chain on the choice:

```java
if (mode == 1) {
  System.out.println("Driving");
} else if (mode == 2) {
  System.out.println("Shooting");
} else {
  System.out.println("Unknown mode");
}
```

Java checks the conditions from top to bottom and runs **only the first one that is true**.
Two `==` *compares* values; one `=` would *store* a value instead.

Move your rectangle code from step 2 inside the `1` branch. The `else` branch should handle
a choice that isn't 1, 2, or 3.

**Build in small pieces.** Before you write any triangle or circle math, put a placeholder
`println` in each branch (like `"You picked triangle"`). Run it and try every option,
including a wrong one like `7`. Once the menu works, replace the placeholders one at a time.

> **Common mistake:** a variable created *inside* `{ }` only exists inside those braces.
> If you create `area` inside the rectangle branch and try to print it after the whole
> `if` chain, Java says `cannot find symbol`. Print each shape's results inside its own branch.

### 4. Calculate the triangle and circle

Each shape needs different measurements, so each branch asks its own questions.

**Triangle (base and height).** Treat it as a *right* triangle, the kind with a square
corner.

- Area is one half of base times height. Be careful how you write "one half" in Java:

  ```java
  System.out.println(7 / 2);   // 3   <- two ints: Java drops the decimal!
  System.out.println(7.0 / 2); // 3.5 <- a double is involved, so you keep it
  ```

  What does `1 / 2` equal in Java? What does that do to your area? How can you fix it?

- For the perimeter you need all three sides, but you only have two. The third side (the
  long, slanted one) comes from the Pythagorean theorem: `c² = a² + b²`. Java has a
  square root built in:

  ```java
  double root = Math.sqrt(49); // 7.0
  ```

  You can square a number by multiplying it by itself.

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

**Too many decimals?** `printf` can round for you when it prints. `%.2f` means "a decimal
number with 2 digits after the point," and `%n` is a new line:

```java
System.out.printf("Speed: %.2f m/s%n", speed); // Speed: 3.75 m/s
```

## Bonus

- **Handle invalid input without crashing.** There are several kinds of bad input. Your
  `else` already catches a menu choice that's out of range. What about someone typing `abc`?
  `input.hasNextInt()` is `true` if the user typed a whole number, and `hasNextDouble()`
  does the same for decimals, so you can check *before* reading. And what
  about a negative length? Does a shape with a side of `-3` make sense?
- **Add a cube and calculate its volume.** Add a `4` to the menu. Volume is the space inside
  a 3D shape, and a cube's volume is side × side × side.
- **Add another shape.** Pick one (a square, a trapezoid, a hexagon...), look up its
  formulas, decide what measurements to ask for, and add it to the menu. Test it with numbers
  you've worked out by hand.
