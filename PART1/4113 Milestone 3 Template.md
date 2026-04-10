# IY4113 Milestone 3

| Assessment Details | Please Complete All Details                                       |
| ------------------ | ----------------------------------------------------------------- |
| Group              | B                                                                 |
| Module Title       | Applied Software Engineering using Object Orientiated Programming |
| Assessment Type    | Java Fundamentals 1                                               |
| Module Tutor Name  | Jonathan Shore                                                    |
| Student ID Number  | P510008                                                           |
| Date of Submission | 8 February 2026                                                   |
| Word Count         | 1388                                                              |

- [x] *I confirm that this assignment is my own work. Where I have referred to academic sources, I have provided in-text citations and included the sources in
  the final reference list.*

- [x] *Where I have used AI, I have cited and referenced appropriately.

------------------------------------------------------------------------------------------------------------------------------

### Research (minimum of 2, at least 3)

---

The following researches focus on Java programming concepts and resources that support the development of the CityRide Lite application. It explores official documentation and tutorials related to user input handling, object - oriented programming, and data structures. These resources helped guide coding decisions, improve program structure, and ensure that the implementation follows good Java programming practices within a console - based environment.

These include the following;

------------------------------------------------------------------------------------------------------------------------------

##### Research 1

**Title of research:** Java Scanner Class Tutorial (Oracle Documentation)

**Reference (link):** Oracle Corporation (n.d.). *Class Scanner*. Retrieved from: https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html [Accessed 8 February 2026].

**How does the research help with coding practise?:**

This research helped me understand how to correctly take user input in a Java console application using the Scanner class. Since my program is menu based and requires the user to enter values such as menu choices, zones, passenger type and time band, this documentation was important for learning how to use methods like nextInt() and next() properly. It also helped me understand common issues such as input mismatch errors and how to avoid problems when switching between reading numbers and strings. Overall, it made my input handling more reliable and reduced bugs in the program.

**Key coding ideas you could reuse in your program:**

- Using Scanner to read user input from the keyboard.

- Using nextInt() for numeric input such as menu options and zones.

- Using next() or nextLine() for text input such as passenger type.

- Reusing a single Scanner object instead of creating many.

**Screenshot of research:**

![](C:\Users\user\OneDrive\Pictures\Screenshots\Screenshot%202026-02-08%20173901.png)

------------------------------------------------------------------------------------------------------------------------------

##### Research 2

**Title of research:** Classes and Objects in Java (GeeksforGeeks)

**Reference (link):** GeeksforGeeks (2026, January). *Classes and Objects in Java*. Retrieved from: https://www.geeksforgeeks.org/java/classes-objects-java [Accessed 8 February 2026].

**How does the research help with coding practise?:**

In this research, I understood the basic principles of object - oriented programming in Java, especially creating classes, defining attributes, and creating objects. It supported the design of my program by helping me separate my code into different classes such as Journey, JourneyManager, and CityRideLite instead of placing everything inside one main class. This made the program easier to understand, easier to maintain, and easier to extend in later milestones when more features are added.

**Key coding ideas you could reuse in your program:**

- Creating classes to represent real world objects (e.g. a Journey).

- Using constructors to initialise object data.

- Using getters and setters to access private fields.

- Keeping each class responsible for one main task.

- Following the idea of encapsulation by hiding internal data.

**Screenshot of research:**

![](C:\Users\user\OneDrive\Pictures\Screenshots\Screenshot%202026-02-08%20165759.png)

------------------------------------------------------------------------------------------------------------------------------

##### Research 3

**Title of research:** Java ArrayList (GeeksforGeeks)

**Reference (link):** GeeksforGeeks (2026, February). *ArrayList in Java*. Retrieved from: https://www.geeksforgeeks.org/java/arraylist-in-java [Accessed 8 February 2026].

**How does the research help with coding practise?:**

This research explains how the ArrayList data structure works in Java and how it can be used to store and manage collections of objects. Although my current version of the program uses simple variables, this research showed me how I can later store multiple Journey objects inside an ArrayList. This will be useful for features such as listing journeys, filtering journeys and generating summaries, because ArrayLists allow the program to grow dynamically as more journeys are added.

**Key coding ideas you could reuse in your program:**

- Using an ArrayList to store Journey objects.

- Adding new journeys using the .add() method.

- Looping through the list using a for loop or enhanced for loop.

- Using .size() to calculate totals and summaries.

- Removing items from the list when a journey is deleted.

**Screenshot of research:**

```java
#geeksforgeeks
import java.util.ArrayList;

class Main {
    public static void main (String[] args) {

          // Creating an ArrayList
          ArrayList<Integer> a = new ArrayList<Integer>();

          // Adding Element in ArrayList
          a.add(1);
          a.add(2);
          a.add(3);

          // Printing ArrayList
          System.out.println(a);
    }
}
```

![](C:\Users\user\OneDrive\Pictures\Screenshots\Screenshot%202026-02-08%20171513.png)

------------------------------------------------------------------------------------------------------------------------------

### Program Code

---

**CityRideLite.java (Main Class)**

This is the main entry point of the program and is responsible for controlling the overall flow of the application. It displays the main menu, handles user input, and calls the appropriate methods based on the user’s selection. The class uses a while loop to keep the program running until the user chooses to exit. This class acts as the user interface controller, managing how the user interacts with the system through the console.

------------------------------------------------------------------------------------------------------------------------------

```java
import java.util.Scanner;

public class CityRideLite {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        JourneyManager journeyManager = new JourneyManager();
        int choice;

        do {
            System.out.println("\n--- CityRide Lite ---");
            System.out.println("1. Add Journey");
            System.out.println("2. View Journeys");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Enter from zone (1-5): ");
                int fromZone = scanner.nextInt();

                System.out.print("Enter to zone (1-5): ");
                int toZone = scanner.nextInt();

                System.out.print("Enter passenger type: ");
                String passengerType = scanner.next();

                System.out.print("Enter time band: ");
                String timeBand = scanner.next();

                Journey journey = new Journey(fromZone, toZone, passengerType, timeBand);
                journeyManager.addJourney(journey);

            } else if (choice == 2) {
                journeyManager.listJourneys();

            } else if (choice == 3) {
                System.out.println("Exiting program...");

            } else {
                System.out.println("Invalid option.");
            }

        } while (choice != 3);
    }
}
```

------------------------------------------------------------------------------------------------------------------------------

**Journey.java (Data Class)**

The Journey class represents a single public transport journey. It stores all the relevant information about a trip, such as the journey ID, starting zone, destination zone, passenger type, and time band. Each time a new journey is created.

------------------------------------------------------------------------------------------------------------------------------

```java
public class Journey {

    private int fromZone;
    private int toZone;
    private String passengerType;
    private String timeBand;

    public Journey(int fromZone, int toZone, String passengerType, String timeBand) {
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.passengerType = passengerType;
        this.timeBand = timeBand;
    }

    public int getFromZone() {
        return fromZone;
    }

    public int getToZone() {
        return toZone;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public String getTimeBand() {
        return timeBand;
    }

    public void displayJourney() {
        System.out.println("From Zone: " + fromZone);
        System.out.println("To Zone: " + toZone);
        System.out.println("Passenger Type: " + passengerType);
        System.out.println("Time Band: " + timeBand);
    }
}
```

------------------------------------------------------------------------------------------------------------------------------

### Updated Gantt Chart

------------------------------------------------------------------------------------------------------------------------------

![](C:\Users\user\OneDrive\Pictures\Screenshots\Screenshot%202026-02-08%20234021.png)

------------------------------------------------------------------------------------------------------------------------------

### Diary Entries

------------------------------------------------------------------------------------------------------------------------------

##### 05/02/2026 - Diary Entry 1 – Research and Initial Program Structure

In this milestone, I focused on conducting research to support my coding process and understanding how to properly structure a Java console application. I used resources such as the Oracle documentation for the Scanner class and GeeksforGeeks tutorials on object - oriented programming and ArrayLists. This research helped me understand how to handle user input, create classes, and organise data in a more structured way, which influenced how I designed the main menu and input logic for the CityRide Lite program. In addition, I started with implementing the main program file (CityRideLite.java). This class currently handles displaying the menu, taking basic user input, and controlling the overall flow of the program. Although the functionality is still limited, this milestone helped me establish the core structure of the system and provided a foundation that I can build on in later milestones.

##### 08/02/2026 - Diary Entry 2 – Additional Program Class and Gantt Chart Update

Today, I created the core data class (Journey.java) to represent a single transport journey. This class stores information such as the starting zone, destination zone, passenger type, and time band. Creating this class helped me apply object - oriented principles by separating data representation from program logic, which makes the system easier to manage and extend. I also updated the Gantt chart to reflect the planned work for Milestones 3. The JourneyManager class and the provided dataset (CityRideDataset.java) will be implemented in the next milestone as the program functionality is which includes implementing journey management, fare calculations, and summary features.

------------------------------------------------------------------------------------------------------------------------------
