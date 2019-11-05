package at.fhv.itb17.s5.teamb.core.domain.general;

import at.fhv.itb17.s5.teamb.core.domain.search.SearchServiceCore;
import at.fhv.itb17.s5.teamb.core.domain.search.SearchServiceCoreImpl;
import at.fhv.itb17.s5.teamb.persistence.entities.Address;
import at.fhv.itb17.s5.teamb.persistence.entities.Artist;
import at.fhv.itb17.s5.teamb.persistence.entities.Event;
import at.fhv.itb17.s5.teamb.persistence.entities.EventCategory;
import at.fhv.itb17.s5.teamb.persistence.entities.EventOccurrence;
import at.fhv.itb17.s5.teamb.persistence.entities.Organizer;
import at.fhv.itb17.s5.teamb.persistence.repository.EntityRepository;
import at.fhv.itb17.s5.teamb.persistence.repository.EventRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedList;

public class CoreServiceInjectorImpl implements CoreServiceInjector {
    private final EntityRepository entityRepository = new EntityRepository();
    private final EventRepository eventRepository = new EventRepository(entityRepository);
    private final SearchServiceCore searchServiceCore = new SearchServiceCoreImpl(eventRepository);

    public CoreServiceInjectorImpl() {
        addDBDATA();
    }

    private void addDBDATA() {
        EventCategory evCat0 = new EventCategory("cat_name_ev0", 99 * 100, 5000, 4711);
        EventCategory evCat1 = new EventCategory("cat_name_ev1", 20 * 100, 800, 11);
        LinkedList<EventCategory> cats = new LinkedList<>(Arrays.asList(evCat0, evCat1));
        Address addressEvOc0 = new Address("evt_country", "evt_zip", "evt_city", "evt_street", "evt_house");
        Address addressEvOc1 = new Address("evt_country", "evt_zip", "evt_city", "evt_street", "evt_house");
        EventOccurrence evOccurrenceDTO0 = new EventOccurrence(LocalDate.now(), LocalTime.now(), cats, addressEvOc0);
        EventOccurrence evOccurrenceDTO1 = new EventOccurrence(LocalDate.now().plusDays(3), LocalTime.now(), cats, addressEvOc1);
        EventOccurrence evOccurrenceDTO2 = new EventOccurrence(LocalDate.now(), LocalTime.now(), cats, addressEvOc0);
        EventOccurrence evOccurrenceDTO3 = new EventOccurrence(LocalDate.now().plusDays(3), LocalTime.now(), cats, addressEvOc1);
        LinkedList<EventOccurrence> occurrences = new LinkedList<>(Arrays.asList(evOccurrenceDTO0, evOccurrenceDTO1));
        LinkedList<EventOccurrence> occurrences1 = new LinkedList<>(Arrays.asList(evOccurrenceDTO2, evOccurrenceDTO3));
        LinkedList<Artist> artistNames = new LinkedList<>(Arrays.asList(new Artist("Hugo Hugo"), new Artist("Franz Peter Werner")));
        Address addressOrg0 = new Address("org0_country", "org0_zip", "org0_city", "org0_street", "org0_house");
        Address addressOrg1 = new Address("org1_country", "org1_zip", "org1_city", "org1_street", "org1_house");
        Organizer org0 = new Organizer("org0_name", "org0_email", addressOrg0);
        Organizer org1 = new Organizer("org1_name", "org1_email", addressOrg1);
        Event eventDTO0 = new Event("Demo Concert0", "A very descriptive description0", "08/15 0", occurrences, org0, artistNames);
        Event eventDTO1 = new Event("Demo Concert1", "A very descriptive description1", "08/15 1", occurrences1, org1, artistNames);
        entityRepository.saveOrUpdate(eventDTO0);
        entityRepository.saveOrUpdate(eventDTO1);
    }

    public SearchServiceCore getSearchServiceCore() {
        return searchServiceCore;
    }
}