package tourGuide;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.service.tourGuideService.TourGuideService;
import tourGuide.user.User;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;

	@RequestMapping("/")
	public String index() {
		return "Greetings from TourGuide!";
	}

	@PostMapping("/getLocation")
	public String getLocation(@RequestParam String userName) {
		VisitedLocation visitedLocation = tourGuideService.getUserLocation(getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
	}

	@PostMapping("/getNearByAttractions")
	public String getNearbyAttractions(@RequestParam String userName) {
		User user = getUser(userName);
		return JsonStream.serialize(tourGuideService.getNearByAttractions(user));
	}

	@PostMapping("/getRewards")
	public String getRewards(@RequestParam String userName) {
		return JsonStream.serialize(getUser(userName).getUserRewards());
	}

	@PostMapping("/getAllCurrentLocations")
	public String getAllCurrentLocations() {
		return JsonStream.serialize(tourGuideService.getAllCurrentLocations());
	}

// TODO : les utilisateurs se sont plaints du fait que leurs offres de voyage ne
	// correspondaient pas exactement ? leurs pr?f?rences, par exemple au niveau
	// du nombre d?enfants ou de la dur?e du s?jour
	@PostMapping("/getTripDeals")
	public String getTripDeals(@RequestParam String userName) {
		List<Provider> providers = tourGuideService.getTripDeals(getUser(userName));
		return JsonStream.serialize(providers);
	}

	@PostMapping("/getUser")
	private User getUser(@RequestParam String userName) {
		return tourGuideService.getUser(userName);
	}

}