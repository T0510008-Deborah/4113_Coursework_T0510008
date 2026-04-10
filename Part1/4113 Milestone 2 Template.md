# IY4113 Milestone 2

| Assessment Details | Please Complete All Details                                       |
| ------------------ | ----------------------------------------------------------------- |
| Group              | B                                                                 |
| Module Title       | Applied Software Engineering using Object Orientiated Programming |
| Assessment Type    | Java Fundamentals 1                                               |
| Module Tutor Name  | Jonathan Shore                                                    |
| Student ID Number  | P510008                                                           |
| Date of Submission | 1 February 2026                                                   |
| Word Count         | 736                                                               |

- [x] *I confirm that this assignment is my own work. Where I have referred to academic sources, I have provided in-text citations and included the sources in
  the final reference list.*

- [x] *Where I have used AI, I have cited and referenced appropriately.

------------------------------------------------------------------------------------------------------------------------------

### Algorithm Design

------------------------------------------------------------------------------------------------------------------------------

<img title="" src="file:///C:/Users/user/Desktop/flows/FLOWCHART1.png" alt="" width="250">

<img src="file:///C:/Users/user/Desktop/flows/FLOWCHART2.png" title="" alt="" width="562">

<img src="file:///C:/Users/user/Desktop/flows/FLOWCHART3.png" title="" alt="" width="441">

    <img src="file:///C:/Users/user/Desktop/flows/FLOWCHART4.png" title="" alt="" width="454">

<img src="file:///C:/Users/user/Desktop/flows/FLOWCHART5.png" title="" alt="" width="363">

**CLASS DIAGRAM**

- ![](C:\Users\user\AppData\Roaming\marktext\images\2026-02-01-23-00-49-image.png)

------------------------------------------------------------------------------------------------------------------------------

### Research (minimum of 1 required, preferably 2)

------------------------------------------------------------------------------------------------------------------------------

The following research analyzes the **Namma Metro Trip Planner**, a Java based console application. This program serves as a great technical reference for transport systems, demonstrating how to effectively manages station data through arrays and figures out complicated routes within a command line environment.

------------------------------------------------------------------------------------------------------------------------------**Name of program:** Namma Metro Trip Planner

Reference (link): Namma  (n.d.). Namma-Metro-Trip-Planner. Retrieved from: https://github.com/mueezmamoon/Namma-Metro-Trip-Planner [Accessed 28 January 2026]

**What it does well (2-3 features that work effectively):**

The program effectively uses String arrays **(purp[] and green[])** to store station names. This allows for easy lookup and print of the journey path, similar to how the **CityRide Lite** dataset stores base fares.
It doesn't just calculate a fare; it determines the direction of travel (e.g., "Board trains going towards Nagasandra") and the platform number. This feature adds a high level of utility for the user.
It handles "Line Selection" and "Interchanges" **(Majestic Station)** effectively using boolean flags like **changeLines = true**, showing how to manage state across different parts of a journey.

**What it does poorly (at least 1 feature):**

The logic is heavily nested with if/else statements within a single main class. This makes the code difficult to maintain and therefore, lacks the Functional Decomposition.

**Key design ideas you could reuse (e.g., layout, navigation, input/output, program structure):**

The use of a numbered menu for line selection (e.g., "1. **Purple Line**", "2. **Green Line**") is a clean way to handle user input.
The program uses text-based lines **(e.g., -------------------- )** to separate different parts of the console output, which makes the interface much easier to read.
The while loops used to print the sequence of stations passed **(System.out.print(" "+green[transitDepart]+" -->"))** could be adapted to my View Journeys feature to show the zones crossed.

**Screenshot (showing the interface/output):**

<img title="" src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-02-01%20190745.png" alt="" width="358">

<img src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-02-01%20190808.png" title="" alt="" width="361">

![](C:\Users\user\OneDrive\Pictures\Screenshots\Screenshot%202026-02-01%20191348.png)

------------------------------------------------------------------------------------------------------------------------------

### Updated Gantt Chart

------------------------------------------------------------------------------------------------------------------------------

![](C:\Users\user\Desktop\flows\GANTT%20CHART.png)

<img src="file:///C:/Users/user/Desktop/flows/GANTT%20CHART2.png" title="" alt="" width="638">

------------------------------------------------------------------------------------------------------------------------------

### Diary Entries

------------------------------------------------------------------------------------------------------------------------------

##### **27/01/2026 - Diary Entry 1 – Algorithm Design**

In this stage of the project, I focused on designing the algorithms for the CityRide Lite system using flowcharts. I started by breaking the program down into smaller parts, such as the main menu, adding a journey, calculating fares, filtering journeys, and removing journeys. This helped me to clearly understand how the system should flow and how the user will interact with it at each step. Using flowcharts also made it easier to think about validation, such as checking if zones are within the correct range and ensuring passenger types and time bands are valid before continuing. I also further created Class diagram for the project.

##### **01/02/2026 - Diary Entry 2 - Research and Gantt Chart Update**

Today I carried out research on an existing console-based transport system called the Namma Metro Trip Planner to understand how similar problems have been approached in practice. This helped me see how station data can be stored using arrays and how user input can be handled through menus and simple logic. I found some useful design ideas, such as using numbered menu options and clear text separators to improve readability in the console. I also noticed that the program relied heavily on nested if/else statements, which made the structure harder to follow, and this reinforced the importance of using better functional decomposition in my own design. After completing the research, I updated the Gantt chart to reflect the planned design and implementation tasks for the remaining milestones.

------------------------------------------------------------------------------------------------------------------------------
