package tourGuide.integrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.rewardService.RewardsService;
import tourGuide.service.tourGuideService.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;

@SpringBootTest
public class TestRewardsService {

  @Autowired
  RewardsService rewardsService;

  @Autowired
  GpsUtil gpsUtil;

  @Autowired
  TourGuideService tourGuideService;

  @BeforeEach
  public void beforeEach() {
    Locale.setDefault(Locale.US);
  }

  @Test
  public void userGetRewards() {

    InternalTestHelper.setInternalUserNumber(0);
    rewardsService.setProximityBuffer(10);

    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    Attraction attraction = gpsUtil.getAttractions().get(0);
    user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
    tourGuideService.trackUserLocation(user);
    List<UserReward> userRewards = user.getUserRewards();
    assertTrue(userRewards.size() == 1);
  }

  @Test
  public void isWithinAttractionProximity() {
    Attraction attraction = gpsUtil.getAttractions().get(0);
    assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
  }

  @Test
  public void nearAllAttractions() {
    rewardsService.setProximityBuffer(Integer.MAX_VALUE);

    InternalTestHelper.setInternalUserNumber(1);

    User user = tourGuideService.getAllUsers().get(0);
    rewardsService.calculateRewards(user);
    List<UserReward> userRewards = user.getUserRewards();

    assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
  }

}
