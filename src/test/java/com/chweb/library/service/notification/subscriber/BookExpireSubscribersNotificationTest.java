package com.chweb.library.service.notification.subscriber;

import com.chweb.library.entity.*;
import com.chweb.library.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;

/**
 * @author chervinko <br>
 * 02.09.2021
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles(profiles = "test")
public class BookExpireSubscribersNotificationTest {
    private final JournalEntity journalEntity = new JournalEntity();
    private final BookEntity bookEntity = new BookEntity();
    private final LibrarianEntity librarianEntity = new LibrarianEntity();
    private final SubscriberEntity subscriberEntity = new SubscriberEntity();

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private JournalItemRepository journalItemRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    PublishingHouseRepository publishingHouseRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookExpireSubscribersNotification bookExpireSubscribersNotification;

    @Value("${library.book-use-number-days}")
    private Integer bookUseNumberDays;

    @Before
    public void initData() {
        initLibrarianEntity();
        initSubscriberEntity();
        initBookEntity();
        initJournalEntity();
    }

    @Test
    public void send() {
        bookExpireSubscribersNotification.send();
    }

    private void initLibrarianEntity() {
        librarianEntity.setFirstName("firstName");
        librarianEntity.setMiddleName("middleName");
        librarianEntity.setLastName("lastName");
        librarianEntity.setPhoneNumber("+7 (999) 999-999-99");
        librarianEntity.setAddress("address");
        librarianEntity.setEmploymentDate(LocalDate.now());
        librarianRepository.save(librarianEntity);
    }

    private void initSubscriberEntity() {
        subscriberEntity.setFirstName("firstName");
        subscriberEntity.setMiddleName("middleName");
        subscriberEntity.setLastName("lastName");
        subscriberEntity.setBirthDate(LocalDate.now().minusYears(25));
        subscriberEntity.setPassportData("passportData");
        subscriberEntity.setPhoneNumber("+7 (999) 999-99-99");
        subscriberEntity.setEmail("info@library.ru");
        subscriberEntity.setAddress("address");
        subscriberRepository.save(subscriberEntity);
    }

    private void initBookEntity() {
        PublishingHouseEntity publishingHouseEntity = new PublishingHouseEntity();
        publishingHouseEntity.setName("name");
        publishingHouseEntity.setDescription("description");
        publishingHouseRepository.save(publishingHouseEntity);

        ThemeEntity themeEntity = new ThemeEntity();
        themeEntity.setName("name");
        themeEntity.setDescription("description");
        themeRepository.save(themeEntity);

        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setFirstName("firstName");
        authorEntity.setMiddleName("middleName");
        authorEntity.setLastName("lastName");
        authorEntity.setBirthDate(LocalDate.now().minusYears(50));
        authorEntity.setDeathDate(LocalDate.now());
        authorEntity.setDescription("description");
        authorRepository.save(authorEntity);

        bookEntity.setName("name");
        bookEntity.setPublishYear(1950);
        bookEntity.setAmount(50);
        bookEntity.setDescription("description");
        bookEntity.setPublishingHouse(publishingHouseEntity);
        bookEntity.setThemes(Collections.singleton(themeEntity));
        bookEntity.setAuthors(Collections.singleton(authorEntity));
        bookRepository.save(bookEntity);
    }

    private void initJournalEntity() {
        journalEntity.setIssueDate(LocalDate.now().minusDays(bookUseNumberDays));
        journalEntity.setLibrarian(librarianEntity);
        journalEntity.setSubscriber(subscriberEntity);
        journalRepository.save(journalEntity);

        JournalItemEntity journalItemEntity = journalItemRepository.save(new JournalItemEntity(
                journalEntity,
                bookEntity
        ));
        journalEntity.setJournalItems(Collections.singleton(journalItemEntity));
    }
}