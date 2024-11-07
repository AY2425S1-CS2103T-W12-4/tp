---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2425S1-CS2103T-W12-4/tp/blob/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `TabPane` (which contains `PersonListPanel` and `EventListPanel`), `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2425S1-CS2103T-W12-4/tp/blob/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2425S1-CS2103T-W12-4/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` and `Event` objects residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2425S1-CS2103T-W12-4/tp/blob/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete event 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete event 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. The `DeleteCommandParser` parses the command and creates either a `DeletePersonCommandParser` or a `DeleteEventCommandParser` object depending on the command type, which then parses the input.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeletePersonCommand` or `DeleteEventCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZEventCommand` object (e.g., `ClearCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddPersonCommandParser`, `DeletePersonCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2425S1-CS2103T-W12-4/tp/blob/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` and `Event` objects (which are contained in `UniquePersonList` and `UniqueEventList` objects).
* stores the currently 'selected' `Person` and `Event` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2425S1-CS2103T-W12-4/tp/blob/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Event and Person Synchronisation

#### Implementation
The current `Event` class has a `Person` field that stores the `Person` object associated with the event. This is because the `Event` object should be aware of the `Person` object it is associated with. This ensures that when details of the `Person` object are updated, the `Event` object is aware of the changes and can update itself accordingly. This applies to the contact list in `Event` as well.

Step 1: The user adds a new `Event` through the `add event` command. During execution of the command, the `Person` object associated with the `Event` is retrieved from the `Model` using the `findPerson` command.

The following sequence diagram shows how the `Event` object is created and associated with a `Person` object:
![AddEvent](images/AddEventSequenceDiagram.png)

The following sequence diagram shows how `findPerson` command is executed and the `Person` object is retrieved:
![FindPerson](images/FindPersonSequenceDiagram.png)

Step 2: The user updates the details of a `Person` object through the `edit person` command. During execution of the command, is done by updating the `Person` object in the `Model`, the updated `Person` replaces all `Event` associate with the original `Person` using the `replacePerosnInEvents` method.

The following sequence diagram shows how the `Person` object is edited:
![EditPerson](images/EditPersonSequenceDiagram.png)

The following sequence diagram shows how `findPerson` command is executed and the `Person` object is retrieved:
![ReplacePerson](images/ReplacePersonInEvents.png)

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**: Celebrity Talent Managers

* has a need to manage a significant number of contacts (talents, contractors, etc)
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: The address book offers celebrity managers a **secure, offline tool** to manage **various contacts and stakeholders, track VIP relationships, and schedule events** efficiently. With a customizable field, it streamlines coordination while ensuring **privacy and data control** in a high-stakes environment.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                                        | I want to …​                                                  | So that I can…​                                          |
|----------|----------------------------------------------------------------|---------------------------------------------------------------|----------------------------------------------------------|
| `* * *`  | Celebrity Talent Manager                                       | add new contacts to my address book                           | keep track of important stakeholders                     |
| `* * *`  | Celebrity Talent Manager                                       | use command-line shortcuts to manage my contacts and schedule | work faster and more efficiently                         |
| `* * *`  | Celebrity Talent Manager                                       | delete a contact                                              | remove entries that I no longer need                     |
| `* * *`  | Celebrity Talent Manager                                       | view details of a specific contact                            | quickly access specific information when needed          |
| `* * *`  | Celebrity Talent Manager                                       | list my contacts                                              | see an overview of my contacts                           |
| `* * *`  | Celebrity Talent Manager                                       | add important events                                          | quickly view my important tasks                          |
| `* * *`  | Celebrity Talent Manager                                       | view the event details a client has                           | see and plan around their schedule                       |
| `* * *`  | Celebrity Talent Manager                                       | view all my events                                            | have an overview of my timetable                         |
| `* * *`  | Celebrity Talent Manager                                       | delete an event                                               | remove outdated entries                                  |
| `* * *`  | Celebrity Talent Manager                                       | save my data                                                  | come back to it next time                                |
| `* *`    | Celebrity Talent Manager dealing with various groups           | group and tag my contacts                                     | group contacts based on specific tags                    |
| `* *`    | Celebrity Talent Manager with many contacts                    | search for clients using tags                                 | quickly find individuals in a specific group             |
| `* *`    | Celebrity Talent Manager with many contacts                    | search for clients using names                                | quickly find specific individuals                        |
| `* *`    | Celebrity Talent Manager                                       | delete a contact only after confirmation                      | prevent accidental deletion of something still important |
| `* *`    | Celebrity Talent Manager                                       | edit contact information                                      | update ay changes in their information                   |
| `* *`    | Celebrity Talent Manager who can be forgetful                  | be alerted when I add duplicate entries                       | avoid duplicate entries                                  |
| `* *`    | Celebrity Talent Manager who is new to the application         | access a help guide in the application                        | find out the command I need when I forget it             |
| `* *`    | Celebrity Talent Manager                                       | perform mass deletion of entries                              | save time when I have to delete a lot of entries         |
| `* *`    | Celebrity Talent Manager                                       | be alerted when events clash                                  | identify clashes and resolve them easily                 |
| `* *`    | Celebrity Talent Manager                                       | mark events as over or ended                                  | keep track my schedule                                   |
| `*`      | Celebrity Talent Manager                                       | add a photo for my contacts                                   | easily remember or recognise the contact                 |
| `*`      | Celebrity Talent Manager                                       | flag priority contacts                                        | identify contacts that require immediate focus           |
| `*`      | Celebrity Talent Manager                                       | export and import client data                                 | safely transfer information on different devices         |
| `*`      | Celebrity Talent Manager who can be careless at times          | undo my last command                                          | quickly revert my changes when they are wrong            |
| `*`      | Celebrity Talent Manager                                       | set reminders for tasks                                       | get reminded of upcoming tasks that are due soon         |
| `*`      | Celebrity Talent Manager                                       | set visual indicators for my contacts based on availability   | quickly see if someone is available or not               |
| `*`      | Celebrity Talent Manager                                       | customise my data storage location                            | change where my data is kept locally                     |
| `*`      | Celebrity Talent Manager                                       | set my own CLI shortcuts                                      | use the commands faster based on my preferences          |

### Use cases

(For all use cases below, the **TalentHub** is the `TalentHub` and the **Actor** is the `celebrity talent manager`, unless specified otherwise)

**Use case: UC01 - Add Contact**

**MSS**

1. Talent Manager requests to add a specific contact
2. TalentHub adds the person to the list

Use case ends.

**Extensions**

* 1a. The command format is incorrect.

  * 1a1. TalentHub outputs a generic error message about incorrect command format.

    Use case ends.
  
* 1b. Any compulsory parameter is missing or invalid.

  * 1b1. TalentHub outputs an error message specifying the issue.

    Use case ends.

* 1c. An identical name is detected.

    * 1c1. TalentHub outputs an error message specifying the issue.

      Use case ends.

* 1d. An identical phone number is detected.

  * 1d1. TalentHub outputs an error message specifying the issue.

    Use case ends.

**Use case: UC02 - Edit Contact**

**MSS**

1. Talent Manager requests to edit the information a specific contact
2. TalentHub change the information of the target person

Use case ends.

**Extensions**

* 1a. The command format is incorrect.

    * 1a1. TalentHub outputs a generic error message about incorrect command format.

      Use case ends.

* 1b. An identical name is detected.

    * 1b1. TalentHub outputs an error message specifying the issue.

      Use case ends.

* 1c. An identical phone number is detected.

    * 1c1. TalentHub outputs an error message specifying the issue.

      Use case ends.



**Use case: UC03 - Delete Contact after List**

**MSS**

1. Talent Manager <a style="text-decoration:underline;">list contacts (UC06)</a>
2. Talent Manager <a style="text-decoration:underline;">delete contact (UC05)</a>

Use case ends.


**Use case: UC04 - Delete Contact after Find**

**MSS**

1. Talent Manager <a style="text-decoration:underline;">find contact (UC07)</a>
2. Talent Manager <a style="text-decoration:underline;">delete contact (UC05)</a>

   Use case ends.

**Use case: UC05 - Delete Contact**

**MSS**

1. Talent Manager requests to delete a specific person in the list
2. TalentHub requests for confirmation of deletion
3. TalentHub deletes the person and all his/her corresponding events

   Use case ends.

**Extensions**

* 1a. The command format is incorrect.

  * 1a1. TalentHub outputs a generic error message about incorrect command format.

    Use case ends.

* 1b. The list is empty.

  * 1b1. TalentHub shows an index error message.

    Use case ends.

* 1c. The given index is invalid.

  * 1c1. TalentHub shows an index error message.

    Use case ends.

* 2a. Talent Manager confirms the deletion. 

  * Use case resumes from step 3.

* 2b. Talent Manager cancels the deletion.

  * 2b1. TalentHub outputs an successful cancellation message.

    Use case ends.

* 2c. The parameter is missing or invalid.

  * 2b1. TalentHub outputs an error message specifying the issue.

    Use case resumes from step 2.

**Use case: UC06 - List All Contacts**

**MSS**

1. Talent Manager requests to list contacts
2. TalentHub shows a list of all contacts

   Use case ends.

**Extensions**

* 1a. The command format is incorrect.

    * 1a1. TalentHub outputs a generic error message about incorrect command format.

      Use case ends.

**Use case: UC07 - Find Contact by Name**

**MSS**

1. User requests to find persons whose name contains `keywords`
2. TalentHub processes and list person(s) whose name contains `keywords`

   Use case ends.

**Extensions**

* 1a. The command format is incorrect.

  * 1a1. TalentHub outputs a generic error message about incorrect command format.

    Use case ends.

* 1b. The keyword is empty.

  * 1b1. TalentHub shows an error message.

    Use case ends.

**Use case: UC08 - Filter Contact by Tag**

**MSS**

1. User requests to filter persons with tag `keywords`
2. TalentHub processes and list person(s) with tag `keywords`

   Use case ends.

**Extensions**

* 1a. The command format is incorrect.

    * 1a1. TalentHub outputs a generic error message about incorrect command format.

      Use case ends.

* 1b. The keyword is empty.

    * 1b1. TalentHub shows an error message.

      Use case ends.

**Use case: UC09 - Add Event**

**MSS**

1. Talent Manager requests to add an event for a specific celebrity
2. TalentHub adds the event

Use case ends.

**Extensions**

* 1a. The command format is incorrect.

  * 1a1. TalentHub outputs a generic error message about incorrect command format.

    Use case ends.

* 1b. Any compulsory parameter is missing or invalid.

    * 1b1. TalentHub outputs an error message specifying the issue.

      Use case ends.

* 1c. A duplicate event is detected:
  
  * 1c1. TalentHub displays a message informing the Talent Manager of the duplicate event and does not add it.

    Use case ends.

* 1d. Time clash is detected:

  * 1d1. TalentHub displays a message informing the Talent Manager of the time clash and does not add it.

    Use case ends.

**Use case: UC10 - Edit Event**

**MSS**

1. Talent Manager requests to edit the information of an event for a specific celebrity
2. TalentHub change the information of the event

Use case ends.

**Extensions**

* 1a. The command format is incorrect.

    * 1a1. TalentHub outputs a generic error message about incorrect command format.

      Use case ends.

* 1b. The input parameter is invalid.

    * 1b1. TalentHub outputs an error message specifying the issue.

      Use case ends.

* 1c. A duplicate event is detected:

    * 1c1. TalentHub displays a message informing the Talent Manager of the duplicate event and does not change it.

      Use case ends.

* 1d. Time clash is detected:

    * 1d1. TalentHub displays a message informing the Talent Manager of the time clash and does not change it.

      Use case ends.

**Use case: UC11 - Delete Event after List**

**MSS**

1. Talent Manager <a style="text-decoration:underline;">List Events (UC14)</a>
2. Talent Manager <a style="text-decoration:underline;">Delete Event (UC12)</a>

   Use case ends.

**Use case: UC12 - Delete Event after Find**

**MSS**

1. Talent Manager <a style="text-decoration:underline;">Find Event (UC15)</a>
2. Talent Manager <a style="text-decoration:underline;">Delete Event (UC12)</a>

   Use case ends.

**Use case: UC13 - Delete Event**

1. Talent Manager requests to delete a specific event in the list
2. TalentHub requests for confirmation of deletion
3. TalentHub deletes the event

   Use case ends.

**Extensions**

* 1a. The command format is incorrect.

    * 1a1. TalentHub outputs a generic error message about incorrect command format.

      Use case ends.

* 1b. The list is empty.

    * 1b1. TalentHub shows an index error message.

      Use case ends.

* 1c. The given index is invalid.

    * 1c1. TalentHub shows an index error message.

      Use case ends.

* 2a. Talent Manager confirms the deletion.

    * Use case resumes from step 3.

* 2b. Talent Manager cancels the deletion.

    * 2b1. TalentHub outputs an successful cancellation message.

      Use case ends.

* 2c. The parameter is missing or invalid.

    * 2b1. TalentHub outputs an error message specifying the issue.

      Use case resumes from step 2.

**Use case: UC14 - List All Events**

**MSS**

1. Talent Manager requests to list all events.
2. TalentHub retrieves and displays all events in chronological order.

   Use case ends.

**Extensions**

* 1a. The command format is invalid.

    * 1a1. TalentHub outputs a generic error message about incorrect command format.

      Use case ends.

**Use case: UC15 - Find Contact by Name**

**MSS**

1. User requests to find event with name contains `keywords`
2. TalentHub processes and list event with name contains `keywords`

   Use case ends.

**Extensions**

* 1a. The command format is invalid.

    * 1a1. TalentHub outputs a generic error message about incorrect command format.

      Use case ends.

* 1b. The keyword is empty.

    * 1b1. TalentHub shows an error message.

      Use case ends.

    
**Use case: UC16 - Filter Event by Celebrity**

**MSS**

1. User requests to filter event by celebrity's name `keywords`
2. TalentHub processes and list celebrity named `keywords`'s events

   Use case ends.

**Extensions**

* 1a. The command format is incorrect.

    * 1a1. TalentHub outputs a generic error message about incorrect command format.

      Use case ends.

* 1b. The keyword is empty.

    * 1b1. TalentHub shows an error message.

      Use case ends.

**Use case: UC17 - Clear Events**

1. Talent Manager requests to clear all event in the list
2. TalentHub requests for confirmation of clear
3. TalentHub clears all events in the list

   Use case ends.

**Extensions**

* 1a. The command format is incorrect.

    * 1a1. TalentHub outputs a generic error message about incorrect command format.

      Use case ends.

* 2a. Talent Manager confirms the clear.

    * Use case resumes from step 3.

* 2b. Talent Manager cancels the clear.

    * 2b1. TalentHub outputs an successful cancellation message.

      Use case ends.

* 2c. The parameter is missing or invalid.

    * 2b1. TalentHub outputs an error message specifying the issue.

      Use case resumes from step 2.

**Use case: UC17 - Clear Persons and Events**

1. Talent Manager requests to clear all persons and events in the list
2. TalentHub requests for confirmation of clear
3. TalentHub clears all persons and events in the list

   Use case ends.

**Extensions**

* 1a. The command format is incorrect.

    * 1a1. TalentHub outputs a generic error message about incorrect command format.

      Use case ends.

* 2a. Talent Manager confirms the clear.

    * Use case resumes from step 3.

* 2b. Talent Manager cancels the clear.

    * 2b1. TalentHub outputs an successful cancellation message.

      Use case ends.

* 2c. The parameter is missing or invalid.

    * 2b1. TalentHub outputs an error message specifying the issue.

      Use case resumes from step 2.

### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. The software should work without requiring an installer.
3. The software should not depend on a remote server. 
4. Since the app is intended to be an offline tool, it should function fully without any internet connection.
5. The application should be packaged into a single JAR file.
6. File sizes should be reasonable and not exceed the limits given below. 
   - Product (i.e., the JAR/ZIP file): 100MB
   - Documents (i.e., PDF files): 15MB/file
7. The developer guide and user guide should be PDF-friendly. Don't use expandable panels, embedded videos, animated GIFs etc.
8. The use of third-party libraries/frameworks/services should not be used so as to improve security and stability of the application.
9. Talent managers with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
10. The application should prioritise one-shot commands over multi-step commands as they are faster. If a multi-step command is provided to help new users, a one-shot equivalent should also be provided for regular/expert users.
11. Regular tasks (e.g., adding a contact, listing events) should be executable within a few milliseconds to maintain efficiency and not interrupt the talent manager’s workflow.
12. The system should efficiently handle a large database of contacts and events (e.g., 1,000+ entries), allowing for fast searches, retrievals, and modifications without performance degradation.
13. The data should be stored locally and should be in a human editable text file.
14. The data should not be stored using a Database Management System.
15. The data should be able to be loaded using a data file if the network is down.
16. The system should provide a user-friendly and intuitive interface with clear instructions and feedback to guide talent managers who are not IT-savvy in using the command-line interface effectively.
17. The GUI should work well (i.e., should not cause any resolution-related inconveniences to the user) for standard screen resolutions 1920x1080 and higher and for screen scales 100% and 125%.
18. The GUI should be usable (i.e., all functions can be used even if the user experience is not optimal) for resolutions 1280x720 and higher and for screen scales 150%.
19. As the app is an offline tool, it should be robust and able to run for extended periods without crashing. Downtime should be limited to under 1% (if any issues require restarting).
20. The system should be able to recover from crashes within 5 seconds, ensuring minimal disruption to the talent manager’s workflow.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Talents**: Well known or up-and-coming individuals who partake in the entertainment industry in one way or another
* **Industry Professional**: Individuals that talents make contact for events
* **Events**: Social and networking events organised by third parties e.g. award shows, brand events
* **Talent Manager**: Industry professionals involved in managing and planning talent schedules
* **Model-View-Controller (MVC)**: A design pattern that separates an application into three main logical components: the Model, the View, and the Controller. 
* **Logic Component**: A part of the architecture responsible for handling user commands and parsing them. 
* **Model Component**: Manages the application’s data and handles the business logic of the application. 
* **Storage Component**: Manages reading from and writing to the persistent storage. 
* **VersionedAddressBook**: A class responsible for implementing the undo/redo feature by maintaining different states of the address book.  
* **Person**: Refers to a contact object within the address book application. 
* **Index**: Refers to the position of an individuals contact in the address book
* **Tag**: The associated description with said contact based on common groups
* **Undo/Redo Feature**: A functionality that allows reverting or reapplying actions taken by the user within the application. 
--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
