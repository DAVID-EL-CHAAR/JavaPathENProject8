package com.openclassrooms.tourguide.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral;
	
	public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
	}
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}
	
	
	/*2.public void calculateRewards(User user) {
	List<VisitedLocation> userLocations = user.getVisitedLocations();
	List<Attraction> attractions = gpsUtil.getAttractions();
	List<UserReward> newRewards = new ArrayList<>();

	for (VisitedLocation visitedLocation : userLocations) {
	    for (Attraction attraction : attractions) {
	        if (user.getUserRewards().stream().noneMatch(r -> r.attraction.attractionName.equals(attraction.attractionName))) {
	            if (nearAttraction(visitedLocation, attraction)) {
	                UserReward newReward = new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user));
	                newRewards.add(newReward);
	            }
	        }
	    }
	}

	// Ajouter toutes les nouvelles récompenses à la liste des récompenses de l'utilisateur
	// après avoir terminé les boucles pour éviter ConcurrentModificationException
	newRewards.forEach(user::addUserReward);
	} */
	/*public void calculateRewards(User user) {
	List<VisitedLocation> userLocations = new ArrayList<>(user.getVisitedLocations());
	List<Attraction> attractions = gpsUtil.getAttractions();

	List<UserReward> newRewards = new ArrayList<>(user.getUserRewards()); // Create a copy of newRewards

	for (VisitedLocation visitedLocation : userLocations) {
	    for (Attraction attraction : attractions) {
	        if (user.getUserRewards().stream().noneMatch(r -> r.attraction.attractionName.equals(attraction.attractionName))) {
	            if (nearAttraction(visitedLocation, attraction)) {
	                UserReward newReward = new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user));
	                newRewards.add(newReward); // Add newly created reward to the copy
	            }
	        }
	    }
	}

	// Add all the new rewards to the list of rewards for the user
	newRewards.forEach(user::addUserReward);
	}*/




	/*public void calculateRewards(User user) {
	List<VisitedLocation> userLocations = new ArrayList<>(user.getVisitedLocations());
	List<Attraction> attractions = gpsUtil.getAttractions();

	for(VisitedLocation visitedLocation : userLocations) {
		for(Attraction attraction : attractions) {
			if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
				if(nearAttraction(visitedLocation, attraction)) {
					user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
				}
			}
		}
	}
	}*/

	/*
	public void calculateRewards(User user) {
	List<VisitedLocation> userLocations = new ArrayList<>(user.getVisitedLocations());
	List<Attraction> attractions = gpsUtil.getAttractions();

	for(VisitedLocation visitedLocation : userLocations) {
		for(Attraction attraction : attractions) {
			if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
				if(nearAttraction(visitedLocation, attraction)) {
					user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
				}
			}
		}
	}
	}*/


	/*	public void calculateRewards(User user) {
	// Créer une copie de la liste des lieux visités
	List<VisitedLocation> userLocations = user.getVisitedLocations();
	List<Attraction> allAttractions = gpsUtil.getAttractions();
	List<UserReward> newRewards = new ArrayList<>();

	for (VisitedLocation visitedLocation : userLocations) {
	    for (Attraction attraction : allAttractions) {
	        // Utiliser une copie de la liste des récompenses pour la vérification
	        boolean alreadyRewarded = new ArrayList<>(user.getUserRewards()).stream()
	                .anyMatch(reward -> reward.attraction.attractionName.equals(attraction.attractionName));

	        if (!alreadyRewarded && nearAttraction(visitedLocation, attraction)) {
	            UserReward newReward = new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user));
	            newRewards.add(newReward);
	        }
	    }
	}
	}
	*/

	/*
	1.public void calculateRewards(User user) {
	// Créer une copie de la liste des lieux visités
	List<VisitedLocation> userLocations = new ArrayList<>(user.getVisitedLocations());
	List<Attraction> allAttractions = gpsUtil.getAttractions();
	List<UserReward> newRewards = new ArrayList<>();

	for (VisitedLocation visitedLocation : userLocations) {
	    for (Attraction attraction : allAttractions) {
	        // Utiliser une copie de la liste des récompenses pour la vérification
	        boolean alreadyRewarded = new ArrayList<>(user.getUserRewards()).stream()
	                .anyMatch(reward -> reward.attraction.attractionName.equals(attraction.attractionName));

	        if (!alreadyRewarded && nearAttraction(visitedLocation, attraction)) {
	            UserReward newReward = new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user));
	            newRewards.add(newReward);
	        }
	    }
	}

	// Ajouter toutes les nouvelles récompenses après avoir terminé la boucle
	for (UserReward newReward : newRewards) {
	    user.addUserReward(newReward);
	}
	} 


	*/








	/*public void calculateRewards(User user) {
	List<VisitedLocation> userLocations = new ArrayList<>(user.getVisitedLocations());
	Set<String> userRewardAttractions = user.getUserRewards().stream()
					.map(UserReward::getAttractionName)
					.collect(Collectors.toSet());

	userLocations.parallelStream().forEach( userLocation ->
			//loop through all attractions
			gpsUtil.getAttractions().stream().forEach(attraction -> {
				//loop through all the user's rewards and check which are the ones he never got a reward for
				if (!userRewardAttractions.contains(attraction.attractionName)) {
					if (nearAttraction(userLocation, attraction)) {
						user.addUserReward(new UserReward(userLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			})
	);
	}
	*/
	 public void calculateRewardss(User user) {
    // Créer une copie de la liste des lieux visités
    //List<VisitedLocation> userLocations = new ArrayList<>(user.getVisitedLocations());
	CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>(user.getVisitedLocations());
    List<Attraction> allAttractions = gpsUtil.getAttractions();
    List<UserReward> newRewards = new ArrayList<>();
    

    for (VisitedLocation visitedLocation : userLocations) {
        for (Attraction attraction : allAttractions) {
            // Utiliser une copie de la liste des récompenses pour la vérification
            boolean alreadyRewarded = new ArrayList<>(user.getUserRewards()).stream()
                    .anyMatch(reward -> reward.attraction.attractionName.equals(attraction.attractionName));

            if (!alreadyRewarded && nearAttraction(visitedLocation, attraction)) {
                UserReward newReward = new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user));
                newRewards.add(newReward);
            }
        }
    }

    // Ajouter toutes les nouvelles récompenses après avoir terminé la boucle
    for (UserReward newReward : newRewards) {
        user.addUserReward(newReward);
    }
} 

	/*
	public void calculateRewards(User user) {
	    List<Attraction> allAttractions = gpsUtil.getAttractions();

	    // Utiliser des flux parallèles pour traiter chaque lieu visité
	    List<CompletableFuture<Void>> futures = user.getVisitedLocations().stream()
	        .map(visitedLocation ->
	            CompletableFuture.runAsync(() -> {
	                allAttractions.stream()
	                    .filter(attraction -> nearAttraction(visitedLocation, attraction))
	                    .filter(attraction -> user.getUserRewards().stream()
	                        .noneMatch(reward -> reward.attraction.attractionName.equals(attraction.attractionName)))
	                    .forEach(attraction -> {
	                        UserReward newReward = new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user));
	                        user.addUserReward(newReward);
	                    });
	            })
	        ).collect(Collectors.toList());

	    // Attendre que toutes les futures soient terminées
	    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
	}

	*/
	/*
	public void calculateRewards(User user) {
	    List<Attraction> allAttractions = gpsUtil.getAttractions();
	    CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>(user.getVisitedLocations());

	    // Préparation d'une liste pour stocker les futures
	    List<CompletableFuture<Void>> futures = new ArrayList<>();

	    for (VisitedLocation visitedLocation : userLocations) {
	        // Pour chaque lieu visité, créer une tâche asynchrone
	        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
	            for (Attraction attraction : allAttractions) {
	                // Créer une copie de la liste des récompenses pour la vérification
	                boolean alreadyRewarded = new ArrayList<>(user.getUserRewards()).stream()
	                    .anyMatch(reward -> reward.attraction.attractionName.equals(attraction.attractionName));

	                if (!alreadyRewarded && nearAttraction(visitedLocation, attraction)) {
	                    UserReward newReward = new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user));
	                    // Ajouter la récompense de manière synchronisée
	                    synchronized (user) {
	                        user.addUserReward(newReward);
	                    }
	                }
	            }
	        });
	        futures.add(future);
	    }

	    // Attendre que toutes les futures soient terminées
	    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
	}*/
	
	public CompletableFuture<Void> calculateRewards(User user) {
        return CompletableFuture.runAsync(() -> {
            List<Attraction> allAttractions = gpsUtil.getAttractions();
            
            // Utiliser une copie de la liste des lieux visités pour éviter ConcurrentModificationException
            //List<VisitedLocation> userLocations = new ArrayList<>(user.getVisitedLocations());
            CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>(user.getVisitedLocations());

            for (VisitedLocation visitedLocation : userLocations) {
                for (Attraction attraction : allAttractions) {
                    // Synchroniser sur l'utilisateur pour la lecture de getUserRewards
                    boolean alreadyRewarded;
                    synchronized (user) {
                        alreadyRewarded = user.getUserRewards().stream()
                            .anyMatch(reward -> reward.attraction.attractionName.equals(attraction.attractionName));
                    }

                    if (!alreadyRewarded && nearAttraction(visitedLocation, attraction)) {
                        int rewardPoints = getRewardPoints(attraction, user);
                        UserReward newReward = new UserReward(visitedLocation, attraction, rewardPoints);
                        
                        // Synchroniser sur l'utilisateur pour l'ajout des récompenses
                        synchronized (user) {
                            user.addUserReward(newReward);
                        }
                    }
                }
            }
        });
    }
		


	
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}
	
	private synchronized boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}
	
	

	
	public int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
	
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

}
