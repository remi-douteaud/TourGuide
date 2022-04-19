package tourGuide.user;

import java.util.Objects;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

public class UserReward {

  private final VisitedLocation visitedLocation;
  private final Attraction attraction;
  private int rewardPoints;

  public UserReward(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
    this.visitedLocation = visitedLocation;
    this.attraction = attraction;
    this.rewardPoints = rewardPoints;
  }

  public UserReward(VisitedLocation visitedLocation, Attraction attraction) {
    this.visitedLocation = visitedLocation;
    this.attraction = attraction;
  }

  public VisitedLocation getVisitedLocation() {
    return visitedLocation;
  }

  public Attraction getAttraction() {
    return attraction;
  }

  public void setRewardPoints(int rewardPoints) {
    this.rewardPoints = rewardPoints;
  }

  public int getRewardPoints() {
    return rewardPoints;
  }

  @Override
  public int hashCode() {
    return Objects.hash(attraction.attractionName, visitedLocation.location);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    UserReward other = (UserReward) obj;
    return Objects.equals(attraction.attractionName, other.attraction.attractionName) && Objects.equals(visitedLocation.location, other.visitedLocation.location);
  }

}
