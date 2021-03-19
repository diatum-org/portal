RECORD="$1"
HOSTNAME=`printf "db%04d" "$RECORD"`

IP=`mysql -u root -proot portal -sN -e "select address from device where id='$RECORD'"`
REGION=`mysql -u root -proot portal -sN -e "select str_value from config where config_id='config_region'"`
ZONE=`mysql -u root -proot portal -sN -e "select str_value from config where config_id='config_hosted_zone'"`
DOMAIN=`mysql -u root -proot portal -sN -e "select str_value from config where config_id='config_domain'"`
ACCESS_KEY=`mysql -u root -proot portal -sN -e "select str_value from config where config_id='config_access_key'"`
KEY_ID=`mysql -u root -proot portal -sN -e "select str_value from config where config_id='config_key_id'"`
export AWS_SECRET_ACCESS_KEY=$ACCESS_KEY
export AWS_ACCESS_KEY_ID=$KEY_ID

debuglog () {
  mysql -u root -proot portal -sN -e "insert into log (level, message) values ('debug', '$1')";
}

mysql -u root -proot portal -sN -e "update device set dns='$HOSTNAME.$DOMAIN', dns_status='created' where id='$RECORD'"

ROUTE=`/usr/bin/aws route53 change-resource-record-sets --hosted-zone-id $ZONE --region $REGION --output json --change-batch "{\"Changes\": [{\"Action\": \"UPSERT\",\"ResourceRecordSet\": {\"Name\": \"$HOSTNAME.$DOMAIN\",\"Type\": \"A\",\"TTL\": 300,\"ResourceRecords\": [{ \"Value\": \"$IP\"}]}}]}"` || {
  mysql -u root -proot portal -sN -e "update device set dns_status='failed' where id='$RECORD'"
  logger "$RECORD: change-resource-record-sets failed"
  debuglog "$RECORD: change-resource-record-sets failed"
  exit 1;
}
CHANGE=`echo $ROUTE | jq -r .ChangeInfo.Id`
STATE=`echo $ROUTE | jq -r .ChangeInfo.Status`

# wait for DNS to sync
mysql -u root -proot portal -sN -e "update device set dns_status='syncing' where id='$RECORD'"
logger "$RECORD: waiting for dns sync [$HOSTNAME] [$DOMAIN] [$IP]"
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
  mysql -u root -proot portal -sN -e "update device set dns_status='failed' where id='$RECORD'"
  logger "$RECORD: failed to sync dns"
  debuglog "$RECORD: failed to sync dns"
  exit 1;
fi
mysql -u root -proot portal -sN -e "update device set dns_status='synced' where id='$RECORD'"


