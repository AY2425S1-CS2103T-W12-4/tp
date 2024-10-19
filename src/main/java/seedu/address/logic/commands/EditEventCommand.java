package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_CELEBRITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_VENUE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_EVENTS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventName;
import seedu.address.model.event.Time;
import seedu.address.model.event.Venue;
import seedu.address.model.person.Person;
import seedu.address.ui.CommandTabChange;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditEventCommand extends EditCommand {

    public static final String COMMAND_FIELD = "event";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + COMMAND_FIELD + ": Edits the details of the event identified "
            + "by the index number used in the displayed event list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_EVENT_NAME + "EVENT NAME] "
            + "[" + PREFIX_EVENT_TIME + "TIME] "
            + "[" + PREFIX_EVENT_VENUE + "VENUE] "
            + "[" + PREFIX_EVENT_CELEBRITY + "CELEBRITY]...\n"
            + "Example: " + COMMAND_WORD
            + " " + COMMAND_FIELD + " 1 "
            + PREFIX_EVENT_TIME + "Oct 4th 2022 "
            + PREFIX_EVENT_VENUE + "Broadway";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Event: %1$s";
    public static final String MESSAGE_DUPLICATE_EVENT = "This event already exists in the address book.";

    private final Index index;
    private final EditEventDescriptor editEventDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editEventDescriptor details to edit the person with
     */
    public EditEventCommand(Index index, EditEventDescriptor editEventDescriptor) {
        requireNonNull(index);
        requireNonNull(editEventDescriptor);

        this.index = index;
        this.editEventDescriptor = new EditEventDescriptor(editEventDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Event> lastShownList = model.getFilteredEventList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        if (editEventDescriptor.celebrityName != null ) {
            editEventDescriptor.setCelebrity(model.findPerson(editEventDescriptor.celebrityName));
        }

        Event eventToEdit = lastShownList.get(index.getZeroBased());
        Event editedEvent = createEditedEvent(eventToEdit, editEventDescriptor);

        if (!eventToEdit.isSameEvent(editedEvent) && model.hasEvent(editedEvent)) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }

        model.setEvent(eventToEdit, editedEvent);
        model.updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.eventFormat(editedEvent)),
                                                            false, false, CommandTabChange.EVENT);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Event createEditedEvent(Event eventToEdit, EditEventDescriptor editEventDescriptor) {
        assert eventToEdit != null;

        EventName updatedEventName = editEventDescriptor.getName().orElse(eventToEdit.getName());
        Time updatedTime = editEventDescriptor.getTime().orElse(eventToEdit.getTime());
        Venue updatedVenue = editEventDescriptor.getVenue().orElse(eventToEdit.getVenue());
        Person updatedCelebrity = editEventDescriptor.getCelebrity().orElse(eventToEdit.getCelebrity());

        return new Event(updatedEventName, updatedTime, updatedVenue, updatedCelebrity);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditPersonCommand)) {
            return false;
        }

        EditEventCommand otherEditEventCommand = (EditEventCommand) other;
        return index.equals(otherEditEventCommand.index)
                && editEventDescriptor.equals(otherEditEventCommand.editEventDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editEventDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditEventDescriptor {
        private EventName name;
        private Time time;
        private Venue venue;
        private String celebrityName;
        private Person celebrity;

        public EditEventDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditEventDescriptor(EditEventDescriptor toCopy) {
            setEventName(toCopy.name);
            setTime(toCopy.time);
            setVenue(toCopy.venue);
            setCelebrityName(toCopy.celebrityName);
            setCelebrity(toCopy.celebrity);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, time, venue, celebrity, celebrityName);
        }

        public void setEventName(EventName name) {
            this.name = name;
        }

        public Optional<EventName> getName() {
            return Optional.ofNullable(name);
        }

        public void setTime(Time time) {
            this.time = time;
        }

        public Optional<Time> getTime() {
            return Optional.ofNullable(time);
        }

        public void setVenue(Venue venue) {
            this.venue = venue;
        }

        public Optional<Venue> getVenue() {
            return Optional.ofNullable(venue);
        }

        public void setCelebrity(Person celebrity) {
            this.celebrity = celebrity;
        }

        public Optional<Person> getCelebrity() {
            return Optional.ofNullable(celebrity);
        }

//        public String getCelebrityName() {
//            return celebrityName;
//        }

        public void setCelebrityName(String celebrityName) {
            this.celebrityName = celebrityName;
        }


        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditEventDescriptor)) {
                return false;
            }

            EditEventDescriptor otherEditEventDescriptor = (EditEventDescriptor) other;
            return Objects.equals(name, otherEditEventDescriptor.name)
                    && Objects.equals(time, otherEditEventDescriptor.time)
                    && Objects.equals(venue, otherEditEventDescriptor.venue)
                    && Objects.equals(celebrity, otherEditEventDescriptor.celebrity);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("time", time)
                    .add("venue", venue)
                    .add("celebrity", celebrity)
                    .toString();
        }
    }
}
