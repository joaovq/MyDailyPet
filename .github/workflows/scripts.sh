#!/bin/bash
echo "$GOOGLE_SERVICES_DATA" > /home/runner/work/MyDailyPet/MyDailyPet/app/google-services.json
echo AD_MOB_ID="$AD_MOB_ID" > ./local.properties
echo BANNER_AD_MOB_ID="$BANNER_AD_MOB_ID" >> ./local.properties
echo INTERSTICIAL_AD_MOB_ID="$INTERSTICIAL_AD_MOB_ID" >> ./local.properties
cat ./local.properties
echo "$KEYSTORE" | base64 --decode > ./app/my-daily-pet.jks
cat ./app/my-daily-pet.jks
echo keystore-file=my-daily-pet.jks > ./keystore.properties
echo keystore-pswd="$KEY_STORE_PASSWORD" >> ./keystore.properties
echo keystore-alias-pswd="$KEY_PASSWORD" >> ./keystore.properties
cat ./keystore.properties