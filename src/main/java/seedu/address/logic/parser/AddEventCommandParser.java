package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_CELEBRITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_VENUE;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventName;
import seedu.address.model.event.Time;
import seedu.address.model.event.Venue;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Parses the user input to create a new instance of an AddEventCommand
 */
public class AddEventCommandParser implements Parser<AddEventCommand> {

    /**
     * Parses the given arguments in the context of the AddEventCommand
     * and returns an AddEventCommand object
     * @param userInput
     * @return AddEventCommand object
     * @throws ParseException
     */
    @Override
    public AddEventCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput,
                PREFIX_EVENT_NAME, PREFIX_EVENT_TIME, PREFIX_EVENT_VENUE, PREFIX_EVENT_CELEBRITY);

        if (!arePrefixesPresent(argMultimap, PREFIX_EVENT_NAME, PREFIX_EVENT_TIME, PREFIX_EVENT_VENUE,
                PREFIX_EVENT_CELEBRITY) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_EVENT_NAME, PREFIX_EVENT_TIME,
                PREFIX_EVENT_VENUE, PREFIX_EVENT_CELEBRITY);
        EventName name = ParserUtil.parseEventName(argMultimap.getValue(PREFIX_EVENT_NAME).get());
        Time time = ParserUtil.parseTime(argMultimap.getValue(PREFIX_EVENT_TIME).get());
        Venue venue = ParserUtil.parseVenue((argMultimap.getValue(PREFIX_EVENT_VENUE)).get());
        String celebrity = argMultimap.getValue(PREFIX_EVENT_CELEBRITY).get();

        return new AddEventCommand(name, time, venue, celebrity);

    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
