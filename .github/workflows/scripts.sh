#!/bin/bash
echo "$GOOGLE_SERVICES_DATA" > /home/runner/work/MyDailyPet/MyDailyPet/app/google-services.json
echo AD_MOB_ID="$AD_MOB_ID" > ./local.properties
echo BANNER_AD_MOB_ID="$BANNER_AD_MOB_ID" >> ./local.properties
echo "$KEYSTORE" | base64 --decode > ./app/my-daily-pet.jks
echo keystore-file=my-daily-pet.jks > ./keystore.properties
echo keystore-pswd="$KEY_STORE_PASSWORD" >> ./keystore.properties
echo keystore-alias-pswd="$KEY_PASSWORD" >> ./keystore.properties

# Never `cat` these files: GitHub masks the base64 secret string, not the
# decoded keystore bytes, so printing the .jks leaks the signing key into the
# Actions log. Use a checksum if you need to confirm the file arrived intact.
ls -l ./app/my-daily-pet.jks
sha256sum ./app/my-daily-pet.jks
