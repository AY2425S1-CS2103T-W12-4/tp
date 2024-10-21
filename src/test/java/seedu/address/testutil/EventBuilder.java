package seedu.address.testutil;

import seedu.address.model.event.Event;
import seedu.address.model.event.EventName;
import seedu.address.model.event.Time;
import seedu.address.model.event.Venue;
import seedu.address.model.person.Person;

/**
 * A utility class to help with building Event objects.
 */
public class EventBuilder {

    public static final String DEFAULT_NAME = "Interview";
    public static final String DEFAULT_TIME = "2021-10-01 14:00";
    public static final String DEFAULT_VENUE = "Esplanade";

    private EventName name;
    private Time time;
    private Venue venue;
    private Person celebrity;

    /**
     * Creates a {@code EventBuilder} with the default details.
     */
    public EventBuilder() {
        name = new EventName(DEFAULT_NAME);
        time = new Time(DEFAULT_TIME);
        venue = new Venue(DEFAULT_VENUE);
        celebrity = new PersonBuilder().build();
    }

    /**
     * Initializes the EventBuilder with the data of {@code eventToCopy}.
     */
    public EventBuilder(Event eventToCopy) {
        name = eventToCopy.getName();
        time = eventToCopy.getTime();
        venue = eventToCopy.getVenue();
        celebrity = eventToCopy.getCelebrity();
    }

    /**
     * Sets the {@code Name} of the {@code Event} that we are building.
     */
    public EventBuilder withName(String name) {
        this.name = new EventName(name);
        return this;
    }

    /**
     * Sets the {@code Celebrity} of the {@code Event} that we are building.
     */
    public EventBuilder withCelebrity(Person celebrity) {
        this.celebrity = new Person(celebrity.getName(), celebrity.getPhone(),
                celebrity.getEmail(), celebrity.getAddress(), celebrity.getTags());
        return this;
    }

    /**
     * Sets the {@code Time} of the {@code Event} that we are building.
     */
    public EventBuilder withTime(String time) {
        this.time = new Time(time);
        return this;
    }

    /**
     * Sets the {@code Venue} of the {@code Event} that we are building.
     */
    public EventBuilder withVenue(String venue) {
        this.venue = new Venue(venue);
        return this;
    }

    public Event build() {
        return new Event(name, time, venue, celebrity);
    }

}
