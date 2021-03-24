set -e

cd ./fe
npm install
ng build --prod --base-href /app/
cd ../
cd be
mvn install -DskipTests

