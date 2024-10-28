package seedu.address.model.util;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventName;
import seedu.address.model.event.Time;
import seedu.address.model.event.Venue;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), Optional.of(new Email("alexyeoh@example.com")),
                Optional.of(new Address("Blk 30 Geylang Street 29, #06-40")),
                getTagSet("Celebrity")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), Optional.of(new Email("berniceyu@example.com")),
                Optional.of(new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18")),
                getTagSet("Celebrity")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), Optional.of(new Email("charlotte@example.com")),
                Optional.of(new Address("Blk 11 Ang Mo Kio Street 74, #11-04")),
                getTagSet("Hairdresser", "MakeupArtist")),
            new Person(new Name("David Li"), new Phone("91031282"), Optional.of(new Email("lidavid@example.com")),
                Optional.of(new Address("Blk 436 Serangoon Gardens Street 26, #16-43")),
                getTagSet("CameraMan")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), Optional.of(new Email("irfan@example.com")),
                Optional.of(new Address("Blk 47 Tampines Street 20, #17-35")),
                getTagSet("CameraMan")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), Optional.of(new Email("royb@example.com")),
                Optional.of(new Address("Blk 45 Aljunied Street 85, #11-31")),
                getTagSet("Chauffeur")),
        };
    }

    public static Event[] getSampleEvents() {
        return new Event[] {
            new Event(new EventName("Oscars"), new Time(LocalDateTime.parse("2015-02-20T06:30:00"),
                    LocalDateTime.parse("2015-02-20T07:30:00")),
                new Venue("Hollywood"), getSamplePersons()[0],
                Set.of(getSamplePersons()[2], getSamplePersons()[3], getSamplePersons()[4])),
            new Event(new EventName("Photo Shoot"), new Time(LocalDateTime.parse("2015-03-20T06:30:00"),
                    LocalDateTime.parse("2015-03-20T07:30:00")),
                new Venue("Park"), getSamplePersons()[1],
                Set.of(getSamplePersons()[2], getSamplePersons()[3], getSamplePersons()[4])),
            new Event(new EventName("Brand Event"), new Time(LocalDateTime.parse("2015-04-20T06:30:00"),
                    LocalDateTime.parse("2015-04-20T07:30:00")),
                new Venue("Mall"), getSamplePersons()[1],
                Set.of(getSamplePersons()[2], getSamplePersons()[3], getSamplePersons()[4])),
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        for (Event sampleEvent : getSampleEvents()) {
            sampleAb.addEvent(sampleEvent);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
