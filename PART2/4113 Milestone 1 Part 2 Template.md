# IY4113 Milestone 1 Part 2

| Assessment Details | Please Complete All Details                                      |
| ------------------ | ---------------------------------------------------------------- |
| Group              | B                                                                |
| Module Title       | Applied Software Engineering using Object-Orientated Programming |
| Assessment Type    | Java Fundamentals 1                                              |
| Module Tutor Name  | Jonathan Shore                                                   |
| Student ID Number  | P510008                                                          |
| Date of Submission | 22nd March 2026                                                  |
| Word Count         | 3823                                                             |
| GitHub Link        | https://github.com/T0510008-Deborah/4113_T0510008_PART_1         |

- [x] *I confirm that this assignment is my own work. Where I have referred to academic sources, I have provided in-text citations and included the sources in
  the final reference list.*

- [x] *Where I have used AI, I have cited and referenced appropriately.

------------------------------------------------------------------------------------------------------------------------------

### Purpose of the Program

------------------------------------------------------------------------------------------------------------------------------

The aim of this program is to develop a console - based Java application named CityRide Lite, designed to record and manage public transport journeys within a single day. The system enables users to enter key journey details such as passenger name, travel zones, passenger category, and time band. Using a predefined fare structure that includes base fares, discounts, and daily caps, the application calculates the total cost of each journey accurately.

CityRide Lite Part 2 expands on the initial version developed in Part 1. While the earlier version operated as a single - session, in memory system, this updated version introduces a more advanced and persistent transport management solution. It incorporates multiple user roles and allows data to be stored between sessions. Users can now create and manage profiles, record journeys over time, and rely on configurable transport rules to calculate fares, making the system more flexible and realistic.

To improve overall functionality and user experience, several enhancements have been added. These include the ability to filter journeys based on different criteria, calculate totals according to passenger type, reset journey records for a new day, and enforce stronger input validation. The system also generates more detailed outputs, such as journey summaries and the number of zones travelled, providing clearer insights for users.

Furthermore, the application supports two main roles: Rider and Admin. Riders can manage personal profiles, add and organise journeys, import and export data using CSV files, and generate end of day reports. All relevant data is stored persistently using JSON and CSV formats. The Admin role, secured with a password, allows authorised users to modify fare settings such as base fares, discounts, daily caps, and peak travel periods, with validation checks to ensure accuracy. The original fare calculation method remains in place, factoring in zones travelled and time bands, while applying discounts and caps automatically. Overall, the program demonstrates advanced concepts such as file handling, data validation, role - based access control, and dynamic recalculation within a structured and user friendly console interface.

------------------------------------------------------------------------------------

##### Core Program Functionality

**Role Selection:** On launch, the user chooses between the Rider and Admin roles. This determines which menu and features are accessible. If <u>Rider</u> is selected; Load the RiderMenu, If <u>Admin</u> is selected; Prompt for a password. For Example, user enters "<u>Admin</u>". System asks: "*Enter Admin Password:*". User enters "*CityRide2026*". If correct, access is granted; otherwise, return to the main role selection.

**Configuration Loading:** The system loads its fare rules, discounts, daily caps, and peak time windows from a JSON configuration file (e.g **config.json**) file at startup. If the file is missing, safe default values are used automatically.

**Rider Profile Management:** A Rider can create a new profile, save it to a JSON file, or load a previously saved profile. The profile stores the Rider's name, passenger type, and default payment option. A user named "*Deborah*" saves her profile. Next time she uses the app, she selects "Load Profile," and the system automatically sets the Passenger Type to "Student" without her having to type it again.

**Journey Management:** Riders can add journeys manually (entering zones, time band, and passenger type), edit existing journeys, or delete journeys with automatic recalculation of totals and caps. Journeys can also be imported in bulk from a CSV file e.g weekly_commute.csv.

**Fare Calculation:** The system calculates fares using the same dataset rules as Part 1: base fare by zone pair and time band, passenger discount applied, and daily cap enforced per passenger type.

**End of Day Summary:** The system produces a detailed summary that includes total journeys, total cost, average cost per journey, the most expensive journey (by ID), whether the daily cap was reached, savings compared to uncapped fares, and category counts (peak vs off - peak, counts per zone pair).

**Export:** The summary and journey list can be exported as both a CSV report (line items) and a human - readable text file. File names include the date and rider name for clear identification.

**Admin Menu:** Password protected access allows an Admin to view the active configuration and add, update, or delete fare rules, discounts, daily caps, and peak time windows. All input is validated before saving.

**Save on Exit:** When the Rider exits, the system offers to save the current day's profile and journeys to JSON so they can be continued in a future session.

**In summary;**

**Rider Features:**

- Create, load, and save rider profile (JSON)

- Add, edit, delete journeys

- Import journeys (CSV)

- Export journeys and reports (CSV + text)

- View running totals and summaries

- End of day report generation

**Admin Features:**

- Login with password

- View system configuration

- Add/update/delete (Base fares, Discounts, Daily caps & Peak time windows)

- Validate all inputs before saving

**System Features:**

- Load config file at startup

- Use default values if file missing

- Apply fare rules (Base fare, Discounts & Daily caps)

- Store data using (JSON (profiles/config) & CSV (journeys/reports))

----------------------------------------------------------------

##### System Constraints

**Two roles only:** The system supports exactly two roles; Rider and Admin. Role is selected at startup.

**Console - based interface:** All interaction is through a text menu driven console. No graphical interface is provided.

**File - based persistence:** Profiles and configuration are stored as JSON; journeys and reports are stored as CSV. No database is used.

**Config file dependency:** If the configuration JSON file is absent, the system defaults to safe built in values rather than crashing.

**Fixed passenger types:** Valid types are ADULT, STUDENT, CHILD, and SENIOR CITIZEN (as defined in CityRideDataset).

**Fixed time bands:** Journeys must be either PEAK or OFF_PEAK.

**Zone range 1 - 5:** From and to zones must be integers between 1 and 5 inclusive.

**Daily caps per passenger type:** Each passenger type has its own cap; once reached, further journeys cost £0.

**Admin password required:** The admin menu cannot be accessed without the correct password.

**All admin changes validated:** No update to the config is saved unless it passes validation.

**Dynamic recalculation on delete:** Deleting or undoing a journey triggers a full recalculation of totals and caps.

**Unique journey IDs:** Each journey is assigned a unique ID for the session, used when editing or deleting.

------------------------------------------------------------------------------------------------------------------------------

### Input Process Output Table

------------------------------------------------------------------------------------------------------------------------------

The IPO table identifies, for each main function, what data the user provides (Input), what the system does with it (Process), and what is produced (Output). Each feature is user initiated, ensuring the system remains user driven.

| Feature / Task                 | Inputs                                                                                          | Processing (what the system does)                                                                                                               | Outputs                                                      |
| ------------------------------ | ----------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------ |
| **Start Program**              | None (program launch)                                                                           | Load config from JSON; if missing, apply safe defaults; initialise data structures                                                              | Config loaded; role selection menu shown                     |
| **Select Role**                | Role choice: RIDER or ADMIN (String)                                                            | Validate input; route to Rider menu or Admin login                                                                                              | Appropriate menu displayed                                   |
| **Admin Login**                | Password (String)                                                                               | Compare input against stored password; grant or deny access                                                                                     | Admin menu shown or access denied message                    |
| **Load Config**                | Config file (JSON)                                                                              | Parse JSON; populate fare rules, discounts, caps, peak windows                                                                                  | System configured; confirmation message                      |
| **Create Rider Profile**       | Name (String), passenger type (String), default payment (String)                                | Validate inputs; create RiderProfile object                                                                                                     | Profile created in memory                                    |
| **Save Rider Profile**         | Confirmation to save (Boolean)                                                                  | Saving profile to JSON; write to file                                                                                                           | JSON profile file saved                                      |
| **Load Rider Profile**         | Profile filename or name (String)                                                               | Read JSON file; parsing into RiderProfile object                                                                                                | Profile loaded into session                                  |
| **Add Journey**                | From zone (int), to zone (int), time band (String), passenger type (String), date/time (String) | Validate all inputs; calculate zones crossed; look up base fare; apply discount; check and apply daily cap; assign unique ID; store journey     | Journey stored; fare and cap status displayed                |
| **Edit Journey**               | Journey ID (int), field to edit (String), new value (String/int)                                | Validate ID exists; validate new value; update journey; recalculate all totals and caps                                                         | Journey updated; updated totals displayed                    |
| **Delete Journey**             | Journey ID (int)                                                                                | Validate ID exists; remove journey; recalculate all totals and caps                                                                             | Confirmation message; updated totals displayed               |
| **Import Journeys from CSV**   | CSV filename (String)                                                                           | Read CSV file; parse each row; validate each journey; calculate fares; add to journey list                                                      | Journeys imported; count of imported/rejected journeys shown |
| **Export Journeys to CSV**     | Confirmation to export (Boolean)                                                                | Format all journeys as CSV rows; write to file with date and rider name                                                                         | CSV file saved; file path shown                              |
| **View Journey List**          | View request (Boolean)                                                                          | Loop through all stored journeys; format each for display                                                                                       | All journeys displayed with IDs, zones, fare, cap status     |
| **Calculate Fare**             | From zone (int), to zone (int), time band (String), passenger type (String)                     | Calculate zones crossed; look up base fare from dataset; apply passenger discount; check daily cap; return final charged fare                   | Final fare displayed; cap applied notification if relevant   |
| **View Running Totals**        | View request (Boolean)                                                                          | Sum all journey fares for current session; compare to cap                                                                                       | Total cost, cap reached status, savings displayed            |
| **End-of-Day Summary**         | Summary request (Boolean)                                                                       | Count journeys; sum total/average cost; find most expensive journey ID; calculate savings vs uncapped; count peak/off_peak; count per zone pair | Detailed summary displayed on screen                         |
| **Export Summary to CSV**      | Confirmation (Boolean)                                                                          | Format summary data as CSV with line items; include rider name and date in filename                                                             | CSV report file saved                                        |
| **Export Summary to Text**     | Confirmation (Boolean)                                                                          | Format summary as human readable text; include rider name and date in filename                                                                  | Text summary file saved                                      |
| **Admin View Config**          | View request (Boolean)                                                                          | Read active configuration from memory                                                                                                           | All fare rules, discounts, caps, and peak windows displayed  |
| **Admin Add/Update Fare Rule** | From zone (int), to zone (int), time band (String), new fare (BigDecimal)                       | Validate all fields; check fare is positive; update config in memory                                                                            | Confirmation or detailed validation error message            |
| **Admin Update Discount**      | Passenger type (String), new discount rate (BigDecimal)                                         | Validate passenger type; validate discount is between 0.00 and 1.00; update config                                                              | Confirmation or validation error                             |
| **Admin Update Daily Cap**     | Passenger type (String), new cap (BigDecimal)                                                   | Validate passenger type; validate cap is positive; update config                                                                                | Confirmation or validation error                             |
| **Admin Update Peak Window**   | Start time (String), end time (String)                                                          | Validate time format; validate start is before end; update config                                                                               | Confirmation or validation error                             |
| **Save Config**                | Confirmation to save (Boolean)                                                                  | Save config to JSON; write to file                                                                                                              | JSON config file saved; confirmation shown                   |
| **Save and Exit**              | Exit confirmation (Boolean)                                                                     | Offer to save profile and journeys; save to JSON if confirmed; close scanner                                                                    | Farewell message; files saved if chosen                      |

------------------------------------------------------------------------------------------------------------------------------

### Algorithm Design

---

**Main Menu Flowchart**

The main menu flowchart represents the top level control structure of the program. On launch, the system calls the Load Config subroutine. The user then selects a role. If Rider is selected, the Rider Menu subroutine is called. If Admin is selected, the Admin Login subroutine is called. The loop continues until the user exits.

<img src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20232719.png" title="" alt="" data-align="center">

**Select Role / Load Config Flowchart**

On launch, the system attempts to load the JSON configuration file. If found and valid, config is loaded. If the file is missing or corrupted, safe default values are applied automatically. The user then selects Rider or Admin. Invalid input loops back to the prompt.

<img src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20232736.png" title="" alt="" data-align="center">

**Add Journey Flowchart (subroutine)**

The Add Journey subroutine prompts the user for from zone, to zone, time band, and passenger type. Each input is validated in a loop. Zones must be 1 - 5. Time band must be PEAK or OFF_PEAK. Passenger type must match a valid enum value. Once all inputs are valid, the Calculate Fare subroutine is called. A Journey object is created and stored. A confirmation message is displayed.

<img src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20232759.png" title="" alt="" data-align="center">

**Calculate Fare Flowchart (subroutine)**

The Calculate Fare subroutine receives validated journey details. It looks up the base fare from the dataset using the from zone, to zone, and time band key. It then applies the passenger discount. It retrieves the running total so far for this passenger type and checks whether adding this fare would exceed the daily cap. If so, the charged fare is reduced to the remaining cap allowance. The final charged fare is returned.

<img src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20232845.png" title="" alt="" data-align="center">

**Delete Journey Flowchart (subroutine)**

The Delete Journey subroutine prompts for a journey ID. If invalid, an error is shown. If found, the journey is removed from the list. All daily totals and caps are recalculated from the remaining journeys. A confirmation message is displayed.

<img src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20232907.png" title="" alt="" data-align="center">

**Admin Login and Admin Menu Flowchart**

The Admin Login subroutine prompts for a password. If incorrect, an error message is shown. After a maximum number of attempts, access is denied and the program returns to the role selection screen. If correct, the Admin Menu is displayed. From the Admin Menu the admin can view config, update fare rules, update discounts, update daily caps, update peak windows, or save and exit.

<img src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20232921.png" title="" alt="" data-align="center">

**Admin Update Config Flowchart (subroutine)**

When the admin selects an update option (fare, discount, cap, or peak window), the subroutine prompts for the relevant fields. All inputs are validated. If any input fails validation, a clear error message is shown and nothing is saved. If all inputs pass, the in memory config is updated. The admin is offered the option to save the config to the JSON file.

<img src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20233043.png" title="" alt="" data-align="center">

**Import Journeys from CSV (subroutine)**

The Import CSV subroutine prompts for a filename. It attempts to open the file. If the file is not found, an error is shown. For each row, the subroutine validates the number of columns, then validates each field. If a row is invalid, it is skipped and counted. If valid, a Journey object is created via the Calculate Fare subroutine and added to the list. A summary of imported and rejected rows is displayed.

<img src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20233100.png" title="" alt="" data-align="center">

##### Class Diagram

The class diagram below shows the object - oriented structure of CityRide Lite Part 2. The application has been extended from Part 1 with the following new or updated classes;

**CityRideLite** - Main entry point. Handles role selection and routes to Rider or Admin menu. *Uses* RiderSession and AdminSession.

**RiderSession** - Manages the Rider menu loop. *Uses* JourneyManager, RiderProfile, FileManager, and SummaryReporter.

**AdminSession** - Manages the Admin menu loop. *Uses* ConfigManager.

**RiderProfile** - Stores rider name, passenger type, and default payment option.

**JourneyManager** - Manages the list of journeys. Handles add, edit, delete, filter, and summary calculations. *Aggregates* Journey objects (1 to many).

**Journey** - Stores full journey data: ID, date/time, fromZone, toZone, timeBand, passengerType, zonesCrossed, baseFare, discountApplied, chargedFare.

**FareCalculator** - Calculates the charged fare given journey inputs, applying discount and daily cap.

**ConfigManager** - Loads, holds, and saves the system configuration (fares, discounts, caps, peak windows) as JSON.

**FileManager** - Handles all file I/O: JSON read/write for profiles and config, CSV import/export for journeys, text and CSV export for summaries.

**CityRideDataset** - Unchanged from Part 1. Static constants class providing base fare data.

**Relationships used:**

- Solid line with open arrowhead = **Association** (uses / calls)
- Diamond with open arrowhead = **Aggregation** (has - a, can exist independently)
- Triangle arrowhead = **Inheritance** (not used in this design as no class extends another)

<img src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20234628.png" title="" alt="" data-align="center">

<img src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20234711.png" title="" alt="" data-align="center">

<img src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20234740.png" title="" alt="" data-align="center">

------------------------------------------------------------------------------------------------------------------------------

### Research (minimum of 1 required, preferrebly 2)

---

A terminal Python program that is structurally very close to CityRide Lite. Chosen specifically because it has three things directly comparable to Part 2: a PassengerDataCsv class that saves bookings to CSV (maps to FileManager), a separate Admin class with password authentication (maps to AdminSession), and fare calculation from combined parameters.

------------------------------------------------------------------------------------------------------------------------------

**Name of program:** Online Bus Ticket Reservation System - Console based Python Application

**Reference (link):** help4code (n.d.). bus-booking-system. Retrieved from: https://github.com/help4code/bus-booking-system [Accessed 20 March 2026].

**What it does well (2-3 features that work effectively):**

Persistent CSV storage for booking records - the program uses a dedicated PassengerDataCsv class whose sole responsibility is saving and loading passenger booking records to a CSV file. Each booking is written as a row with all key fields (name, departure, destination, fare, seat number, bus type). This means data survives between sessions, exactly the kind of file based persistence required in CityRide Lite Part 2 for exporting journeys and summary reports. The separation into its own class also demonstrates single responsibility well.

Separate Admin class with its own menu - the program implements an Admin class that provides a distinct menu for managing bookings, separate from the passenger. The admin must authenticate before accessing this menu. This two role structure (passenger user + admin) maps directly onto CityRide Lite Part 2's Rider and Admin role design.

**What it does poorly (at least 1 feature):**

The program has no input validation - if the user enters an invalid bus type, seat number, or departure location, the program either crashes or silently accepts the bad input. There is also no daily cap concept, no discount system by passenger category, and no end of day summary or reporting feature. All booking data is stored in a single CSV with no ID lookup, meaning there is no way to remove or edit a specific booking by reference which is a limitation that CityRide Lite addresses through unique journey IDs and ID based deletion.

**Key design ideas you could reuse (e.g., layout, navigation, input/output, program structure):**

The most reusable idea is the dedicated CSV handler class pattern - a class whose only job is reading from and writing to a specific file. In CityRide Lite Part 2 this becomes the FileManager class, with separate methods for profile JSON, journey CSV, and summary export, keeping file I/O completely separate from the business logic in JourneyManager.

**Screenshot (showing the interface/output):**

<img title="" src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20200518.png" alt="" data-align="center" width="426">

<img title="" src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20200634.png" alt="" data-align="center" width="419">

<img title="" src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20200659.png" alt="" data-align="center" width="405">

<img title="" src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20200829.png" alt="" width="492" data-align="center">

<img title="" src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20200840.png" alt="" width="504" data-align="center">

------------------------------------------------------------------------------------------------------------------------------

### Gantt Chart

------------------------------------------------------------------------------------------------------------------------------

<img title="" src="file:///C:/Users/user/OneDrive/Pictures/Screenshots/Screenshot%202026-03-22%20220241.png" alt="" data-align="center" width="686">

------------------------------------------------------------------------------------------------------------------------------

### Diary Entries

------------------------------------------------------------------------------------------------------------------------------

##### 20/03/2026 Diary Entry 1: Analysis and Planning.

Today I began work on Part 2 of the CityRide Lite project. My first step was to carefully re read the Part 2 assignment brief in full, alongside the feedback from my Part 1 submission, so I could understand exactly what was expected and what needed to be improved. I then wrote the Purpose of the Program section, identifying what the extended system must do that is new compared to Part 1; specifically, the two role structure (Rider and Admin), persistent file storage using JSON and CSV, rider profile management, and the enhanced end of day summary with export functionality.

After that, I worked through all functional requirements listed in the brief and proceeded each one to in the IPO table. I was careful this time to only include genuine user inputs in the Input column, in my Part 1 submission, the tutor noted that some "inputs" were actually internal data structures rather than user provided values. I corrected this approach throughout the new IPO table, making sure each input column entry represents something the user actually types or provides.

I also noted the non functional constraints carefully, which includes the console only interface, file based persistence, admin password protection, and the need for all admin changes to be validated before saving. I listed these under the System Constraints section as they are important for understanding the boundaries of the program.

##### 22/03/2026 – Diary Entry 2: Algorithm, Research and Ghant Chart.

Today I focused developing the design and planning components of my Part 2 project. I began by creating flowcharts to represent the main algorithms of the system, including user interactions such as creating a rider profile, adding journeys, and handling admin configuration updates. While doing this, I paid close attention to using the correct flowchart symbols and improving the layout, as I received feedback in Part 1 about incorrect notation and poor readability. This time, i use corrected flowchart notation throughout using subroutine symbols for called functions, proper decision diamonds, and the subroutine name (not "Start") at the top of each sub flowchart. I ensured that all processes, decisions, and inputs/outputs were clearly represented and logically structured.

I also worked on designing the class diagram for the system. I identified key classes such as Rider, Journey, Admin, and Configuration, and defined their attributes and methods. I made sure to apply object - oriented principles such as encapsulation and separation of responsibilities, so that each class has a clear and specific role. One challenge I faced was deciding how different classes should interact, particularly how journeys are linked to rider profiles and how configuration data is accessed across the system. I resolved this by structuring relationships carefully and ensuring consistency in data flow.

In addition, I conducted research based on other programs, specifically working with JSON and CSV formats, as these are required for storing profiles, journeys, and reports. This research helped me understand how data persistence can be implemented and influenced my design decisions, particularly in separating file handling logic from core program logic. Finally, I drew up the Gantt chart to plan the work across the available weeks, separating Milestone 1, Milestone 2 and the final submission into distinct weekly tasks to organise my project timeline, breaking the work such as analysis, design, implementation, testing, and final submission. This has helped me plan my time more effectively and ensure that I stay on track with deadlines.

------------------------------------------------------------------------------------------------------------------------------
