# Technologies

> Java 17

> Spring Boot 3.1.1

> JUnit 5  

# Comment rendre disponibles les dépendances gpsUtil, rewardCentral et tripPricer ?

> Exécutez les commandes suivantes :

```bash
  mvn install:install-file -Dfile=TourGuide/libs/gpsUtil.jar -DgroupId=gpsUtil -DartifactId=gpsUtil -Dversion=1.0.0 -Dpackaging=jar
  mvn install:install-file -Dfile=TourGuide/libs/RewardCentral.jar -DgroupId=rewardCentral -DartifactId=rewardCentral -Dversion=1.0.0 -Dpackaging=jar
  mvn install:install-file -Dfile=TourGuide/libs/TripPricer.jar -DgroupId=tripPricer -DartifactId=tripPricer -Dversion=1.0.0 -Dpackaging=jar
