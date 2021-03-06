package tourGuide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;
import tourGuide.DTO.AttractionDTO;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.TourGuideService;
import tourGuide.service.rewardService.RewardsService;
import tourGuide.user.User;
import tripPricer.Provider;

@SpringBootTest
public class TestTourGuideService {

  @Autowired
  GpsUtil gpsUtil;

  @Autowired
  RewardsService rewardsService;

  @Autowired
  TourGuideService tourGuideService;

  @BeforeEach
  public void beforeEach() {
    Locale.setDefault(Locale.US);
  }

  @Disabled
  @Test
  public void getUserLocation() {

    InternalTestHelper.setInternalUserNumber(0);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

    VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

    assertTrue(visitedLocation.userId.equals(user.getUserId()));
  }

  @Disabled
  @Test
  public void addUser() {
    GpsUtil gpsUtil = new GpsUtil();
    InternalTestHelper.setInternalUserNumber(0);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

    tourGuideService.addUser(user);
    tourGuideService.addUser(user2);

    User retrivedUser = tourGuideService.getUser(user.getUserName());
    User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

    assertEquals(user, retrivedUser);
    assertEquals(user2, retrivedUser2);
  }

  @Disabled
  @Test
  public void getAllUsers() {
    GpsUtil gpsUtil = new GpsUtil();
    InternalTestHelper.setInternalUserNumber(0);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

    tourGuideService.addUser(user);
    tourGuideService.addUser(user2);

    List<User> allUsers = tourGuideService.getAllUsers();

    assertTrue(allUsers.contains(user));
    assertTrue(allUsers.contains(user2));
  }

  @Disabled
  @Test
  public void trackUser() {
    GpsUtil gpsUtil = new GpsUtil();
    InternalTestHelper.setInternalUserNumber(0);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

    assertEquals(user.getUserId(), visitedLocation.userId);
  }

  @Disabled
  @Test
  public void getNearbyAttractions() {
    GpsUtil gpsUtil = new GpsUtil();
    InternalTestHelper.setInternalUserNumber(0);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

    List<AttractionDTO> attractions = tourGuideService.getNearByAttractions(user);

    assertEquals(5, attractions.size());
  }

  @Disabled
  @Test
  public void getTripDeals() {

    InternalTestHelper.setInternalUserNumber(0);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

    List<Provider> providers = tourGuideService.getTripDeals(user);

    assertEquals(5, providers.size());
  }

}
