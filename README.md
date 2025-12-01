# Neighbourly

## About our app
Neighbourly is a wep app that collectively brings in communities and neighbours together. Through Neighbourly, 
neighbours are able to request and offer services and resources, allowing for a much more cost-effective and reliable way to obtain aid. 

## Setup 
1. Make a .env file to store keys 
```bash
touch .env
## replace with actual uri 
echo MONGODB_URI=YOUR_MONGODB_URI >> .env 
## replace with actual gemini api 
echo GEMINI_API=YOUR_GEMINI_API >> .env 
echo SENDBIRD_APP_ID=YOUR_SENDBIRD_APP_ID >> .env 
echo SENDBIRD_MASTER_TOKEN=YOUR_SENDBIRD_MASTER_TOKEN >> .env
echo MAPBOX_TOKEN=YOUR_MAPBOXAPI_PUBLICKEY>> .env 
```

2. Run main.py
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="app.Main"
```

# Contributors 

## Isabella 
### 1. Use case 

#### User story: 
As a user, I want to be able to sign up into my app and login in whenever I want. 

#### Features:
- Add ID verfication to prevent suspicious memebers from joining 
- Send user entity to the database once signed up 
- Display signing up/logging errors if there are invalid inputs 
- Allow for retries if verfication fails 

### 2. Implementation and Contributions:
- Integrated MongoDB database to store users/requests/offers
- Used Gemini API to verify user id during signup process 
- Implemented the start interface flow from login -> hompage and login -> signup -> homepage 


## Sam
### 2. Use case 

#### User story: 
 As a user, I want to be able to access my homepage to view and search current requests. I also want to create a request and add additional details to my request.

#### Features:
- Create a button to add either a request or an offer
- Allow users to create a new request with a title and additional details
- choose the type of request(service, or resource)

### 2. Implementation and Contributions:
- Implemented homepage ui, search, create button
- Implemented request window


## Anna
### 3. Use case

#### User story:
As a user, I want to be able to easily post offers that I can provide and view a history of all the offers I have submitted. 

#### Features:
- Allow user to create and submit new offers with a title and description
- Automatically assign creation timestamp for every submitted offer
- Display clear success/error messages upon submission
- Provide scrollable page to view all submitted offers made by the current user

### 2. Implementation and Contributions:
- Implemented Clean Architecture for Create Offer and My Offers use cases



## Tahmid
### 5. Use case

#### User story:
As a helper, I want to accept a request, see the requester on a map with route/ETA, and open a live map so I know how to get there. 

#### Features:
- Show requester and helper locations with a route on a static map (in-app)  
- Display walk, bike, and car ETAs and reverse-geocoded addresses for both parties  
- Provide a “View location” dialog with an “Open in browser” option for the live Mapbox GL map  
- Include a “Message requester” button in the location dialog  

### 2. Implementation and Contributions:
- Added Mapbox integration behind clean architecture ports (`MapService`, `MapImageProvider`) with `MapboxMapService` / `RealMapImageProvider` implementations  
- Centralized config/env loading via `AppConfig`, injecting Mapbox services into the UI (no direct env reads in views)  
- Built `RequestMapView` to render static maps, multi-mode ETAs, addresses, and a browser launch for the interactive map  
- Wrote headless-safe tests with full coverage of the Mapbox flow (routes, fallback, browser launch, address handling) and enabled JaCoCo coverage reporting


