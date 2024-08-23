 echo "${{ secrets.SERVICE_ACCOUNT_JSON }}" > /home/runner/work/MyDailyPet/MyDailyPet/app/google-services.json
 echo AD_MOB_ID="${{ secrets.AD_MOB_ID }}" > ./local.properties
 echo BANNER_AD_MOB_ID="${{ secrets.BANNER_AD_MOB_ID }}" >> ./local.properties
 cat ./local.properties
 echo "${{ secrets.KEYSTORE }}" | base64 --decode > ./app/my-daily-pet.jks
 cat ./app/my-daily-pet.jks
 echo keystore-file=my-daily-pet.jks > ./keystore.properties
 echo keystore-pswd="${{ secrets.KEY_STORE_PASSWORD }}" >> ./keystore.properties
 echo keystore-alias-pswd="${{ secrets.KEY_ALIAS }}" >> ./keystore.properties
 cat ./keystore.properties