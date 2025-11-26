# Neighbourly

## About our app
Neighbourly is a wep app that collectively brings in communities and neighbours together. Through Neighbourly, 
neighbours are able to request and offer services and resources, allowing for a much more cost-effective and reliable way to obtain aid. 

## Setup 
1. Make a .env file to store keys 
```bash
touch .env
echo MONDODB_URI=YOUR_MONGODB_URI >> .env ## replace with actual uri 
echo GEMINI_API=YOUR_GEMINI_API
```

2. Run main.py
```bash
cd /Users/isabellazhong/neighbourly
mvn clean compile
mvn exec:java -Dexec.mainClass="app.Main"
```


