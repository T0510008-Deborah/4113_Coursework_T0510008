# IY4113 Milestone 1

| Assessment Details | Please Complete All Details                                       |
| ------------------ | ----------------------------------------------------------------- |
| Group              | B                                                                 |
| Module Title       | Applied Software Engineering using Object Orientiated Programming |
| Assessment Type    | Java Fundamentals 1                                               |
| Module Tutor Name  | Jonathan Shore                                                    |
| Student ID Number  | P510008                                                           |
| Date of Submission | 25th January 2026                                                 |
| Word Count         | 1034                                                              |

- [x] *I confirm that this assignment is my own work. Where I have referred to academic sources, I have provided in-text citations and included the sources in
  the final reference list.*

- [x] *Where I have used AI, I have cited and referenced appropriately.

------------------------------------------------------------------------------------------------------------------------------

### Purpose of the Program

------------------------------------------------------------------------------------------------------------------------------

The program, which is a console-based Java application called CityRide Lite that allows the user to record and manage all of their public transport journeys for a single day. The user can enter journey details such as starting zone, destination zone, time band (peak/off-peak), passenger type. With the use of the supplied City Ride dataset (fixed), the system calculates fares, discounts and daily caps. 

#### **Core program functionality**

**Display Menu**: The program shows a main menu with different options such as adding journeys, viewing journeys, filtering, viewing summaries, and exiting. The menu keeps looping so the user can continue using the system until they choose to exit.

**Add journeys**: The user can add a new journey by entering the required details, including zones, time band, and passenger type. The system checks that the input is valid before saving the journey and confirming it has been added. Therefore;

- Assigns each journey a unique session IDs

- Stores all journeys in memory for the current session

**Calculate Fares**: The system Calculates how many zones were crossed using the formula abs (tozone – fromzone) + 1. From the provided CityRide dataset, the system will work out the base fare using: (from zone, to zone, time band) - > base fare.

- **Apply discounts**:
  
  Adult: 0%
  
  Student: 25%
  
  Child: 50%
  
  Senior Citizen: 30%

- **Apply Daily Caps**:
  
  Adult: £8.00
  
  Student: £6.00
  
  Child: £4.00
  
  Senior Citizen: £7.00
  
  *NOTE*: Ensure fares never exceed the passenger’s daily cap.

**Store journeys**: The program keeps a list of all journeys entered during the current session. Each journey stores information such as the date, starting and ending zones, passenger type, time band, and the final fare charged.

**View journeys**: The user can see all journeys that have been entered so far. Each journey is shown with its ID and key details so it is easy to understand and refer back to.

**Filter journeys**: The system allows the user to filter journeys by passenger type, time band, zone, or date. This makes it easier to look at specific trips instead of scrolling through everything. 

**Remove journeys**: Similarly, If the user makes a mistake, they can remove a journey by ID, with confirmation. The program then updates the stored journeys and recalculates the totals.

**Summary**: The program produces summaries such as;

- Total number of journeys

- Total cost charged

- Average cost per journey

- Most expensive journey

- Totals per passenger type

- Peak vs Off-Peak counts

- Zone usage counts



#### **System constraints (limitations / rules)**

**Single day session only**: The system only stores journeys for one session and everything is cleared when the program closes.

**Console UI only** : All interaction with the user happens through text menus and keyboard input.

**Zones limited to 1 - 5**: The user can only enter zones between 1 and 5, as these are the valid transport zones.

**Fixed passenger types**: Only Adult, Student, Child, and Senior Citizen are allowed so that discounts can be applied correctly.

**Fixed time bands**: Journeys must be either Peak or Off - Peak so the correct fare can be selected from the dataset.

**Dataset rules cannot be changed**: All fares, discounts, and caps must come from the provided CityRide dataset and cannot be edited or modified.

**Daily caps apply per passenger type**: Each passenger type has its own maximum amount that can be charged in one day.

**No data is saved after exit**: When the program is closed, all journeys and totals are lost and must be re-entered next time.

------------------------------------------------------------------------------------------------------------------------------

### Input Process Output Table

------------------------------------------------------------------------------------------------------------------------------

![](C:\Users\user\Downloads\Input-Process-Output-Table.jpg)

------------------------------------------------------------------------------------------------------------------------------

### Gantt Chart

------------------------------------------------------------------------------------------------------------------------------

![](C:\Users\user\Downloads\Gantt_Chart-Milestone1.jpg)

------------------------------------------------------------------------------------------------------------------------------

### Diary Entries

------------------------------------------------------------------------------------------------------------------------------

#### 23/01/2026 - Diary Entry 1 - Understanding the Problem and planning and Analysing of the System

Today I focused on understanding the CityRide Lite specification and what the system was expected to do. I carefully read through the specification and highlighted the main features, such as adding journeys, calculating fares, applying discounts, and enforcing daily caps. After this, I wrote the purpose of the program in my own words so I had a clear idea of what I was trying to build. I created the Input Process Output Table to help map out how the user’s input would be handled and turned into useful output by the system.
This part of the work helped me break the problem into smaller pieces, which made it easier to understand. One challenge I had was making sure I included all the required inputs and outputs, especially for things like fare calculation and summaries. To deal with this, I went back to the brief and checked each requirement carefully against my table for thorough analysis.

#### 25/01/2026 - Diary Entry 2 - Planning and Creating the Gantt Chart

In this part of the milestone, I focused on organising the work for the whole CityRide Lite project. After completing the purpose of the program and the Input Process Output table, I used this information to create a Gantt chart. The chart shows all the numbered milestones and the final submission, so I could see how the work will be spread out over time. Even though only Milestone 1 has been completed so far, creating the Gantt chart still required me to think carefully about how the work would be spreaded over time and the later stages of the project. With this, I have a clear plan that I can follow as I move on to the next milestones.

------------------------------------------------------------------------------------------------------------------------------
