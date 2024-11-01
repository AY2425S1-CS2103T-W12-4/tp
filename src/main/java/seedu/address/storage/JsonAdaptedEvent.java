package seedu.address.storage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventName;
import seedu.address.model.event.Time;
import seedu.address.model.event.Venue;
import seedu.address.model.person.Person;

/**
 * Jackson-friendly version of {@link Event}.
 */
class JsonAdaptedEvent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Event's %s field is missing!";

    private final String name;
    private final JsonAdaptedTime time;
    private final String venue;
    private final JsonAdaptedPerson celebrity;
    private final Set<JsonAdaptedPerson> contacts = new HashSet<>();

    /**
     * Constructs a {@code JsonAdaptedEvent} with the given event details.
     */
    @JsonCreator
    public JsonAdaptedEvent(@JsonProperty("name") String name,
                            @JsonProperty("time") JsonAdaptedTime time,
                            @JsonProperty("venue") String venue,
                            @JsonProperty("celebrity") JsonAdaptedPerson celebrity,
                            @JsonProperty("contacts") List<JsonAdaptedPerson> contacts) {
        this.name = name;
        this.time = time;
        this.venue = venue;
        this.celebrity = celebrity;
        this.contacts.addAll(contacts);
    }

    /**
     * Converts a given {@code Event} into this class for Jackson use.
     */
    public JsonAdaptedEvent(Event source) {
        name = source.getName().getEventName();
        time = new JsonAdaptedTime(source.getTime());
        venue = source.getVenue().map(Venue::toString).orElse(null);
        celebrity = new JsonAdaptedPerson(source.getCelebrity());
        contacts.addAll(source.getContacts().stream()
                .map(JsonAdaptedPerson::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted event object into the model's {@code Event} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted event.
     */
    public Event toModelType() throws IllegalValueException {
        final Set<Person> eventContacts = new HashSet<>();
        for (JsonAdaptedPerson contact : contacts) {
            eventContacts.add(contact.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, EventName.class.getSimpleName())
            );
        }
        final EventName eventName = new EventName(name);

        if (time == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Time.class.getSimpleName()));
        }
        final Time eventTime = time.toModelType();

        Venue eventVenue = null;
        if (venue != null) {
            eventVenue = new Venue(venue);
        }

        if (celebrity == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Person.class.getSimpleName()));
        }
        final Person eventCelebrity = celebrity.toModelType();

        return Event.createEvent(eventName, eventTime, eventVenue, eventCelebrity, eventContacts);
    }

}
