DEVICE="$1"
HOSTED="$2"
HOSTNAME=`printf "db%04d" "$DEVICE"`

IP=`mysql -u root -proot portal -sN -e "select address from device where id='$DEVICE'"`
REGION=`mysql -u root -proot portal -sN -e "select region from domain where id='$HOSTED'"`
ZONE=`mysql -u root -proot portal -sN -e "select zone from domain where id='$HOSTED'"`
DOMAIN=`mysql -u root -proot portal -sN -e "select name from domain where id='$HOSTED'"`
ACCESS_KEY=`mysql -u root -proot portal -sN -e "select key_value from domain where id='$HOSTED'"`
KEY_ID=`mysql -u root -proot portal -sN -e "select key_id from domain where id='$HOSTED'"`
export AWS_SECRET_ACCESS_KEY=$ACCESS_KEY
export AWS_ACCESS_KEY_ID=$KEY_ID

debuglog () {
  mysql -u root -proot portal -sN -e "insert into log (level, message) values ('debug', '$1')";
}

TS=`date +%s`
mysql -u root -proot portal -sN -e "update device set dns='$HOSTNAME.$DOMAIN', dns_status='created' where id='$DEVICE'"
mysql -u root -proot portal -sN -e "insert into hostname (domain_id, name, timestamp) values ($HOSTED, '$HOSTNAME.$DOMAIN', $TS)"

echo "/usr/bin/aws route53 change-resource-record-sets --hosted-zone-id $ZONE --region $REGION --output json --change-batch \"{\"Changes\": [{\"Action\": \"CREATE\",\"ResourceRecordSet\": {\"Name\": \"$HOSTNAME.$DOMAIN\",\"Type\": \"A\",\"TTL\": 300,\"ResourceRecords\": [{ \"Value\": \"$IP\"}]}}]}\""

ROUTE=`/usr/bin/aws route53 change-resource-record-sets --hosted-zone-id $ZONE --region $REGION --output json --change-batch "{\"Changes\": [{\"Action\": \"CREATE\",\"ResourceRecordSet\": {\"Name\": \"$HOSTNAME.$DOMAIN\",\"Type\": \"A\",\"TTL\": 300,\"ResourceRecords\": [{ \"Value\": \"$IP\"}]}}]}"` || {
  mysql -u root -proot portal -sN -e "update device set dns_status='failed' where id='$DEVICE'"
  logger "$DEVICE: change-resource-record-sets failed"
  debuglog "$DEVICE: change-resource-record-sets failed"
  exit 1;
}
CHANGE=`echo $ROUTE | jq -r .ChangeInfo.Id`
STATE=`echo $ROUTE | jq -r .ChangeInfo.Status`

# wait for DNS to sync
mysql -u root -proot portal -sN -e "update device set dns_status='syncing' where id='$DEVICE'"
logger "$DEVICE: waiting for dns sync"
for (( d=0; d < 60; ++d))
do
  ROUTE=`/usr/bin/aws route53 get-change --region $REGION --output json --id $CHANGE`
  STATE=`echo $ROUTE | jq -r .ChangeInfo.Status`
  if [ "$STATE" = "INSYNC" ]; then
    break;
  fi
  sleep 5
done
if [ "$STATE" != "INSYNC" ]; then
  mysql -u root -proot portal -sN -e "update device set dns_status='failed' where id='$DEVICE'"
  logger "$DEVICE: failed to sync dns"
  debuglog "$DEVICE: failed to sync dns"
  exit 1;
fi
mysql -u root -proot portal -sN -e "update device set dns_status='synced' where id='$DEVICE'"


