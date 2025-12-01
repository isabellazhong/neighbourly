# Neighbourly

## About our app
Neighbourly is a wep app that collectively brings in communities and neighbours together. Through Neighbourly, 
neighbours are able to request and offer services and resources, allowing for a much more cost-effective and reliable way to obtain aid. 

## Setup 
1. Make a .env file to store keys 
```bash
touch .env
## replace with actual uri 
echo MONDODB_URI=YOUR_MONGODB_URI >> .env 
## replace with actual gemini api 
echo GEMINI_API=YOUR_GEMINI_API >> .env 
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


