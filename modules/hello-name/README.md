# Hello, Name

Work in `src/Main.java`. Run this lesson with the editor Run button.

In this lesson you'll write a program that talks to the user: it asks questions, remembers
the answers in **variables**, and prints them back.

**Where does my code go?** Almost everything goes *inside* the curly braces of `main`.
The one exception is `import` lines, which go at the very top of the file:

```java
import java.util.Scanner; // imports go up here, above the class

public class Main {
  public static void main(String[] args) {
    // your code goes here, one statement per line
  }
}
```

Java reads `main` from top to bottom, one line at a time. Keep that in mind: you can't use
a variable on a line *above* the one where you created it.

## Steps

### 1. Store your name in a variable

A variable is a labeled box that holds a value. Creating one takes three parts: a **type**,
a **name**, and a **value**.

```java
int wheels = 4;
// type: int  (a whole number)
// name: wheels
// value: 4
```

Text (a word or a sentence) uses the `String` type, and text values always go inside double
quotes: `"like this"`.

Create a `String` variable called `name` that holds your name.

> **Common mistakes:** `String` needs a capital **S**, and every statement ends with a
> semicolon `;`. If the editor underlines something in red, check for those two first.

### 2. Print a greeting

`System.out.println(...)` prints whatever is inside the parentheses, then moves to a new line.

```java
System.out.println("Go team!"); // prints the text exactly as written
System.out.println(wheels);     // prints the value stored in the variable: 4
```

Use `+` to glue text and variables together into one line:

```java
System.out.println("The robot has " + wheels + " wheels");
// The robot has 4 wheels
```

Print `Hello, <your name>` using your `name` variable, then press Run.

> **Check your work:** you should see something like `Hello, Ada`. If you see `Hello,Ada`,
> the space is missing. Does the space belong inside the quotes or outside them?

> **Think about it:** what's the difference between `println("name")` and `println(name)`?
> Predict what each prints, then try both.

### 3. Ask the user for their name

Right now your program only ever greets *you*. A `Scanner` lets the program read what the
user types into the terminal. Using one takes three pieces:

1. **Import it** at the top of the file: `import java.util.Scanner;`
2. **Create it once** at the start of `main`: `Scanner input = new Scanner(System.in);`
3. **Ask a question, then read the answer:**

```java
System.out.print("What's your team number? ");
String team = input.nextLine(); // waits for the user to type and press Enter
```

Notice this uses `print`, not `println`. `print` stays on the same line, so the user types
their answer right after the question.

Change your program so it *asks* for the name instead of having it typed into the code.
You don't need a new variable. What should `name` be set to now?

> **Check your work:**
> ```
> What is your name? Sam
> Hello, Sam
> ```
> When the program reaches `nextLine()`, it stops and waits. If it seems frozen, click into
> the terminal and type something.

### 4. Print today's date

Java already knows how to find today's date. It lives in a class called `LocalDate`, which
needs its own import:

```java
import java.time.LocalDate;
```

`LocalDate.now()` gives you today's date. Store it in a variable (what type do you think it
is?) and print it with a label in front, like `Today is ...`.

> **Check your work:** you should see something like `Today is 2026-10-28`.

> **Think about it:** the date prints as year-month-day. Why might programmers prefer that
> order to month/day/year? (Hint: think about sorting a list of dates.)

### 5. Ask for the user's age

An age is a whole number, so store it as an `int`, not a `String`. `Scanner` has a method
for reading whole numbers:

```java
System.out.print("How many robots are on an alliance? ");
int robots = input.nextInt();
```

Ask for the user's age, store it in an `int`, and print a sentence that uses it.

> **Think about it:** since age is a number, you can do math with it. Try printing
> `"Next year you will be " + age + 1`. What prints? Why isn't it what you expected?
> (Hint: Java does `+` from left to right. What happens when you `+` a String and a
> number?) How could parentheses fix it?

> **Check your work:** a full run should look something like this:
> ```
> What is your name? Sam
> Hello, Sam
> Today is 2026-10-28
> How old are you? 15
> You are 15 years old
> ```

## Bonus

- **Handle bad input without crashing.** Run your program and type `fifteen` for your age.
  The red `InputMismatchException` is Java saying "that's not a whole number." Scanner can
  check *before* reading: `input.hasNextInt()` is `true` if the user typed a whole number.
  Combine it with `if`/`else`:
  ```java
  if (/* some true-or-false check */) {
    // runs when the check is true
  } else {
    // runs when it's false
  }
  ```
- **Ask for first and last name separately.** Read them into two variables, then greet the
  full name. Watch the spacing between the two names.
- **Add one or two more questions and print a short summary.** Watch out: if you call
  `nextLine()` *after* `nextInt()`, the question gets skipped. `nextInt()` reads the number
  but leaves the Enter key behind, and `nextLine()` grabs that leftover empty line. To fix it,
  call `input.nextLine();` once right after `nextInt()` to throw the leftover away.
