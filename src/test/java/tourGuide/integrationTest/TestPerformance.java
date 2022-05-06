package tourGuide.integrationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.service.rewardService.RewardsService;
import tourGuide.service.tourGuideService.TourGuideService;
import tourGuide.user.User;

@SpringBootTest
public class TestPerformance {

  @Autowired
  GpsUtil gpsUtil;

  /*
   * A note on performance improvements: The number of users generated for the high volume tests can be easily adjusted via this method: InternalTestHelper.setInternalUserNumber(100000); These tests
   * can be modified to suit new solutions, just as long as the performance metrics at the end of the tests remains consistent. These are performance metrics that we are trying to hit:
   * highVolumeTrackLocation: 100,000 users within 15 minutes: assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())); highVolumeGetRewards: 100,000 users
   * within 20 minutes: assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
   */

  @Autowired
  RewardsService rewardsService;

  @Autowired
  TourGuideService tourGuideService;

  @BeforeEach
  public void beforeEach() {
    Locale.setDefault(Locale.US);
  }

  @Test
  public void highVolumeTrackLocation() {

    // Users should be incremented up to 100,000, and test finishes within 15
    // minutes
    tourGuideService.setInternalUsersNumberCount(100000);

    List<User> allUsers = new ArrayList<>();
    allUsers = tourGuideService.getAllUsers();

    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    //allUsers.parallelStream().forEach(u -> tourGuideService.trackUserLocation(u));

    for (User u : allUsers) {
      tourGuideService.trackUserLocationAsync(u);
    }

    stopWatch.stop();

    System.out.println("highVolumeTrackLocation: Time Elapsed: "
        + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
    assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
  }

  @Test
  public void highVolumeGetRewards() throws InterruptedException, ExecutionException {

    ExecutorService executor = Executors.newCachedThreadPool();
    List<Future<Void>> results = new ArrayList<>();
    List<Callable<Void>> callables = new ArrayList<>();

    // Users should be incremented up to 100,000, and test finishes within 20
    // minutes
    tourGuideService.setInternalUsersNumberCount(100000);
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    Attraction attraction = gpsUtil.getAttractions().get(0);
    List<User> allUsers = new ArrayList<>();
    allUsers = tourGuideService.getAllUsers();
    allUsers.parallelStream().forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

    allUsers.forEach(u -> callables.add(new Callable<Void>() {

      @Override
      public Void call() throws Exception {
        rewardsService.calculateRewards(u);
        return null;
      }
    }));

    results = executor.invokeAll(callables);

    for (Future<Void> future : results)
      future.get();
    //allUsers.forEach(u -> rewardsService.calculateRewardsAsync(u));

    for (User user : allUsers) {
      assertTrue(user.getUserRewards().size() > 0);
    }
    stopWatch.stop();

    System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime())
        + " seconds.");
    assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
  }

}
