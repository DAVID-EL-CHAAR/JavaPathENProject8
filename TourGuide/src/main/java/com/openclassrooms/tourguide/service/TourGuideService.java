package com.openclassrooms.tourguide.service;

import com.openclassrooms.tourguide.helper.InternalTestHelper;
import com.openclassrooms.tourguide.model.AttractionDistance;
import com.openclassrooms.tourguide.model.AttractionInformationDto;
import com.openclassrooms.tourguide.tracker.Tracker;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import jakarta.annotation.PreDestroy;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	boolean testMode = true;
	private ExecutorService executorService = Executors.newFixedThreadPool(20);

	@PreDestroy
	public void shutdownExecutorService() {
		executorService.shutdown();
	}
	
    

    public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
        this.gpsUtil = gpsUtil;
        this.rewardsService = rewardsService;

        Locale.setDefault(Locale.US);

        if (testMode) {
            logger.info("TestMode enabled");
            logger.debug("Initializing users");
            initializeInternalUsers(); 
            logger.debug("Finished initializing users");
        }
        this.tracker = new Tracker(this);
        addShutDownHook();
    }

	/*public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;
		
		Locale.setDefault(Locale.US);

		if (testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}*/

    public User getUserByUserId(UUID userId) {
        return usersByUuid.get(userId);
    }
    
	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
				: trackUserLocation(user);
		return visitedLocation;
	}

	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}
	
	public User getUserById(UUID userId) {
		return internalUserMap.get(userId);
	}

	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}
	
	/*public User getUserByUserId(UUID userId) {
	    return getAllUsers().stream()
	            .filter(user -> user.getUserId().equals(userId))
	            .findFirst()
	            .orElse(null); // Retourne null si aucun utilisateur correspondant n'est trouvé
	}*/


	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(), user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}

	public VisitedLocation trackUserLocation(User user) {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewardss(user);
		return visitedLocation;
	}
	
	public CompletableFuture<Void> trackUserLocationAsync(User user) {
	    return CompletableFuture.runAsync(() -> trackUserLocation(user), executorService);
	}

	////
	public CompletableFuture<VisitedLocation> trackUserLocationA(User user) {
	    return CompletableFuture.supplyAsync(() -> {
	        VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());
	        user.addToVisitedLocations(visitedLocation);
	        rewardsService.calculateRewards(user);
	        return visitedLocation;
	    });
	}



	/*
	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		for (Attraction attraction : gpsUtil.getAttractions()) {
			if (rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
				nearbyAttractions.add(attraction);
			}
		}

		return nearbyAttractions;
	} */
	
/*	public List<AttractionInformationDto> getNearByAttractions(VisitedLocation visitedLocation) {
	    User user = getUserByUserId(visitedLocation.userId); // Récupère l'utilisateur par son UUID
	    if (user == null) {
	        // Gérer le cas où aucun utilisateur n'est trouvé
	        return Collections.emptyList();
	    }

	    List<Attraction> allAttractions = gpsUtil.getAttractions();
	    return allAttractions.stream()
	        .map(attraction -> new AttractionInformationDto(
	            attraction.attractionName,
	            attraction.latitude,
	            attraction.longitude,
	            visitedLocation.location.latitude,
	            visitedLocation.location.longitude,
	            rewardsService.getDistance(attraction, visitedLocation.location),
	            rewardsService.getRewardPoints(attraction, user))) // Utiliser l'objet User ici
	        .sorted(Comparator.comparingDouble(AttractionInformationDto::getDistance))
	        .limit(5)
	        .collect(Collectors.toList());
	} */
	
/*	public List<AttractionInformationDto> getNearByAttractions(VisitedLocation visitedLocation, User user) {
	    List<Attraction> allAttractions = gpsUtil.getAttractions();
	    return allAttractions.stream()
	        // Trier les attractions du plus proche au plus éloigné
	        .sorted(Comparator.comparingDouble(attraction -> rewardsService.getDistance(attraction, visitedLocation.location)))
	        .map(attraction -> new AttractionInformationDto(
	            attraction.attractionName,
	            attraction.latitude,
	            attraction.longitude,
	            visitedLocation.location.latitude,
	            visitedLocation.location.longitude,
	            rewardsService.getDistance(attraction, visitedLocation.location),
	            rewardsService.getRewardPoints(attraction, user))) // Utilisez votre méthode getRewardPoints
	        // Prendre les 5 premières attractions
	        .limit(5)
	        // Convertir le flux en liste
	        .collect(Collectors.toList());
	} */
	
	/* 1 public List<AttractionInformationDto> getNearByAttractions(VisitedLocation visitedLocation, User user) {
	    List<Attraction> allAttractions = gpsUtil.getAttractions();

	    // Utiliser une TreeMap pour trier automatiquement les attractions par distance
	    TreeMap<Double, Attraction> sortedAttractions = new TreeMap<>();
	    for (Attraction attraction : allAttractions) {
	        double distance = rewardsService.getDistance(attraction, visitedLocation.location);
	        sortedAttractions.put(distance, attraction);
	    }

	    List<AttractionInformationDto> nearestAttractions = new ArrayList<>();
	    int count = 0;
	    for (Map.Entry<Double, Attraction> entry : sortedAttractions.entrySet()) {
	        if (count >= 5) break; // Limite aux 5 attractions les plus proches

	        Attraction attraction = entry.getValue();
	        AttractionInformationDto dto = new AttractionInformationDto(
	            attraction.attractionName,
	            attraction.latitude,
	            attraction.longitude,
	            visitedLocation.location.latitude,
	            visitedLocation.location.longitude,
	            entry.getKey(), // La distance est déjà calculée
	            rewardsService.getRewardPoints(attraction, user)
	        );
	        nearestAttractions.add(dto);
	        count++;
	    }

	    return nearestAttractions;
	}


*/
	/*
	public List<AttractionInformationDto> getNearByAttractions(VisitedLocation visitedLocation, User user) {
	    return gpsUtil.getAttractions().stream()
	        .map(attraction -> createAttractionInformationDto(attraction, visitedLocation, user))
	        .sorted(Comparator.comparing(AttractionInformationDto::getDistance))
	        .limit(5)
	        .collect(Collectors.toList());
	} */
	
	public List<AttractionInformationDto> getNearByAttractions(VisitedLocation visitedLocation, User user) {
	    // Obtenez toutes les attractions et calculez leur distance à l'utilisateur
	    List<AttractionDistance> attractionDistances = gpsUtil.getAttractions().stream()
	        .map(attraction -> new AttractionDistance(attraction, rewardsService.getDistance(new Location(attraction.latitude, attraction.longitude), visitedLocation.location)))
	        .collect(Collectors.toList());

	    // Triez les attractions par distance et limitez-les aux 5 plus proches
	    List<Attraction> closestAttractions = attractionDistances.stream()
	        .sorted(Comparator.comparing(AttractionDistance::getDistance))
	        .limit(5)
	        .map(AttractionDistance::getAttraction)
	        .collect(Collectors.toList());

	    // Transformez les attractions en DTO
	    return closestAttractions.stream()
	        .map(attraction -> createAttractionInformationDto(attraction, visitedLocation, user))
	        .collect(Collectors.toList());
	}


	private AttractionInformationDto createAttractionInformationDto(Attraction attraction, VisitedLocation visitedLocation, User user) {
	    AttractionInformationDto dto = new AttractionInformationDto();
	    dto.setAttractionName(attraction.attractionName);
	    dto.setAttractionLat(attraction.latitude);
	    dto.setAttractionLong(attraction.longitude);
	    dto.setUserLat(visitedLocation.location.latitude);
	    dto.setUserLong(visitedLocation.location.longitude);
	    dto.setDistance(rewardsService.getDistance(new Location(attraction.latitude, attraction.longitude), visitedLocation.location));
	    dto.setRewardPoints(rewardsService.getRewardPoints(attraction, user));
	    return dto;
	}
	
	/*public List<AttractionInformationDto> getNearByAttractions(String userName) {
	    User user = getUser(userName);
	    if (user == null) {
	        // Gérer le cas où l'utilisateur n'existe pas
	        return Collections.emptyList();
	    }
	    VisitedLocation visitedLocation = getUserLocation(user);

	    List<AttractionDistance> attractionDistances = gpsUtil.getAttractions().stream()
	        .map(attraction -> new AttractionDistance(attraction,rewardsService.getDistance(new Location(attraction.latitude, attraction.longitude), visitedLocation.location)))
	        .collect(Collectors.toList());

	    List<Attraction> closestAttractions = attractionDistances.stream()
	        .sorted(Comparator.comparing(AttractionDistance::getDistance))
	        .limit(5)
	        .map(AttractionDistance::getAttraction)
	        .collect(Collectors.toList());

	    return closestAttractions.stream()
	        .map(attraction -> createAttractionInformationDto(attraction, visitedLocation, user))
	        .collect(Collectors.toList());
	}
	
	


	private AttractionInformationDto createAttractionInformationDto(Attraction attraction, VisitedLocation visitedLocation, User user) {
	    AttractionInformationDto dto = new AttractionInformationDto();
	    dto.setAttractionName(attraction.attractionName);
	    dto.setAttractionLat(attraction.latitude);
	    dto.setAttractionLong(attraction.longitude);
	    dto.setUserLat(visitedLocation.location.latitude);
	    dto.setUserLong(visitedLocation.location.longitude);
	    dto.setDistance(rewardsService.getDistance(new Location(attraction.latitude, attraction.longitude), visitedLocation.location));
	    dto.setRewardPoints(rewardsService.getRewardPoints(attraction, user));
	    return dto;
	}
*/
	







	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				tracker.stopTracking();
			}
		});
	}

	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes
	// internal users are provided and stored in memory
	/*private final Map<String, User> internalUserMap = new HashMap<>();

	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}*/
	
	private Map<String, User> internalUserMap = new HashMap<>();
	private Map<UUID, User> usersByUuid = new HashMap<>(); // Ajout de la map UUID -> User

	private void initializeInternalUsers() {
	    IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
	        String userName = "internalUser" + i;
	        String phone = "000";
	        String email = userName + "@tourGuide.com";
	        UUID userId = UUID.randomUUID(); // Générer un UUID pour l'utilisateur
	        User user = new User(userId, userName, phone, email);
	        generateUserLocationHistory(user);

	        internalUserMap.put(userName, user); // Ajouter à la map utilisant le nom d'utilisateur
	        usersByUuid.put(userId, user); // Ajouter également à la map utilisant l'UUID
	    });
	    logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

    //y avait un point d'arret 
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
					new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

}
