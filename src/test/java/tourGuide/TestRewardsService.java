package tourGuide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.TourGuideService;
import tourGuide.service.rewardService.RewardsService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

public class TestRewardsService {

  @Autowired
  RewardsService rewardsService;

  @Autowired
  TourGuideService tourGuideService;

  @BeforeAll
  private static void beforeAll() {
    Locale.setDefault(Locale.US);
  }

  @Test
  public void userGetRewards() {
    GpsUtil gpsUtil = new GpsUtil();

    InternalTestHelper.setInternalUserNumber(0);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    Attraction attraction = gpsUtil.getAttractions().get(0);
    user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
    tourGuideService.trackUserLocation(user);
    List<UserReward> userRewards = user.getUserRewards();
    assertTrue(userRewards.size() == 1);
  }

  @Disabled
  @Test
  public void isWithinAttractionProximity() {
    GpsUtil gpsUtil = new GpsUtil();
    Attraction attraction = gpsUtil.getAttractions().get(0);
    assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
  }

  @Disabled
  @Test
  public void nearAllAttractions() {
    GpsUtil gpsUtil = new GpsUtil();
    rewardsService.setProximityBuffer(Integer.MAX_VALUE);

    InternalTestHelper.setInternalUserNumber(1);

    rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
    List<UserReward> userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));

    assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
  }

}
