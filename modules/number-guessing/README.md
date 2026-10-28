# Number Guessing Game

Work in `src/Main.java`. Run this lesson with the editor Run button.

The computer picks a secret number, and the user tries to guess it. You already know how to
read a number with `Scanner` from **Hello, Name**. The new parts are random numbers and
making decisions with `if`.

**Plan before you code.** Think about how you'd play this game with a friend, step by step:
pick a number, ask for a guess, compare the guess to the number, give a hint. Each of those
becomes a few lines of code, in that order.

## Steps

### 1. Pick a secret number from 1 to 10

`Random` works a lot like `Scanner`: import it, create it once, then call methods on it.

```java
import java.util.Random;
```

```java
Random rng = new Random();
int roll = rng.nextInt(6); // a random whole number: 0, 1, 2, 3, 4, or 5
```

`nextInt(6)` gives you **6 possible numbers**, but it starts counting at **0**, not 1.
So it can never return 6.

Store a random number from **1 to 10** in a variable called `secret`.

- How many possible numbers are there from 1 to 10?
- `nextInt` always starts at 0. How could you shift every result up by one?

> **Testing tip:** for now, print the secret number right after you pick it. Run the program
> several times and check that you see 1 and 10, but never 0 or 11. You'll need this printout
> in step 3 to test your hints. Delete it when the game is finished, or it's not much of a game!

### 2. Read the user's guess

Ask the user for a guess and store it in an `int` called `guess`. You've done this before:
look at how you read the age in Hello, Name.

### 3. Print `higher`, `lower`, or `correct`

An `if` statement runs some code only when a condition is true. Conditions usually compare
two values:

| Operator | Means                    |
| -------- | ------------------------ |
| `==`     | equal to                 |
| `!=`     | not equal to             |
| `<`      | less than                |
| `>`      | greater than             |
| `<=`     | less than or equal to    |
| `>=`     | greater than or equal to |

> **Common mistake:** `=` and `==` are different. One `=` *stores* a value (`int x = 5;`).
> Two `==` *compares* values (`if (x == 5)`).

To choose between several options, chain `if`, `else if`, and `else`:

```java
if (batteryVoltage < 11) {
  System.out.println("Swap the battery!");
} else if (batteryVoltage > 13) {
  System.out.println("Fresh off the charger.");
} else {
  System.out.println("Battery is fine.");
}
```

Java checks the conditions from top to bottom and runs **only the first one that is true**.
If none are true, it runs the `else`.

Compare `guess` to `secret` and print the right hint.

> **Think about it:** if the guess is *smaller* than the secret, should the program say
> `higher` or `lower`? The hint tells the player which way to go next, so it's easy to mix
> these up.

> **Check your work:** with the secret number printed, run the program three times. Guess
> one number below the secret, one above it, and the secret itself. All three hints should
> make sense.

## Bonus

- **Keep guessing until correct.** A `while (true)` loop repeats its code forever, and
  `break` jumps out of it:
  ```java
  while (true) {
    // everything in here repeats...
    if (/* the thing you're waiting for happened */) {
      break; // ...until break exits the loop
    }
  }
  // the program continues here after break
  ```
  Before you write it, decide which lines belong **inside** the loop and which stay
  **outside**. What happens if picking the secret number goes inside the loop?
- **Increase the range.** Instead of typing `10` in several places, store the top of the
  range in a variable such as `max`. Then you only have to change it in one place. Remember
  to update the question too, for example `"Guess a number from 1 to " + max`.
- **Keep score.** Count the guesses with a variable that starts at `0` and goes up by one
  each guess (`count = count + 1;` or the shortcut `count++;`). Where should you create it:
  inside the loop or before it? What happens to the count if you get this wrong?
- **Limit the number of guesses.** Swap `while (true)` for a condition, for example
  `while (count < maxGuesses)`. When the loop ends, how will you know whether the player won
  or ran out of guesses? A `boolean` variable (`true`/`false`) that starts as `false` can
  remember that for you.
