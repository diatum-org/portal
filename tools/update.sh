set -e

RECORD="$1"

IP=`mysql -u root -proot portal -sN -e "select address from device where id='$RECORD'"`
HOSTNAME=`mysql -u root -proot portal -sN -e "select dns from device where id='$RECORD'"`
DOMAIN=`echo "$HOSTNAME" | cut -c 8-`
DOMAIN_ID=`mysql -u root -proot portal -sN -e "select id from domain where name='$DOMAIN'"`

REGION=`mysql -u root -proot portal -sN -e "select region from domain where id='$DOMAIN_ID'"`
ZONE=`mysql -u root -proot portal -sN -e "select zone from domain where id='$DOMAIN_ID'"`
ACCESS_KEY=`mysql -u root -proot portal -sN -e "select key_value from domain where id='$DOMAIN_ID'"`
KEY_ID=`mysql -u root -proot portal -sN -e "select key_id from domain where id='$DOMAIN_ID'"`

export AWS_SECRET_ACCESS_KEY=$ACCESS_KEY
export AWS_ACCESS_KEY_ID=$KEY_ID

debuglog () {
  mysql -u root -proot portal -sN -e "insert into log (level, message) values ('debug', '$1')";
}

mysql -u root -proot portal -sN -e "update device set dns_status='pending' where id='$RECORD'"

ROUTE=`/usr/bin/aws route53 change-resource-record-sets --hosted-zone-id $ZONE --region $REGION --output json --change-batch "{\"Changes\": [{\"Action\": \"UPSERT\",\"ResourceRecordSet\": {\"Name\": \"$HOSTNAME\",\"Type\": \"A\",\"TTL\": 300,\"ResourceRecords\": [{ \"Value\": \"$IP\"}]}}]}"` || {
  mysql -u root -proot portal -sN -e "update device set dns_status='failed' where id='$RECORD'"
  logger "$RECORD: change-resource-record-sets failed"
  debuglog "$RECORD: change-resource-record-sets failed"
  exit 1;
}
CHANGE=`echo $ROUTE | jq -r .ChangeInfo.Id`
STATE=`echo $ROUTE | jq -r .ChangeInfo.Status`

mysql -u root -proot portal -sN -e "update device set dns_status='syncing' where id='$RECORD'"
logger "$RECORD: waiting for dns sync"
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

logger "$RECORD: dns sync complete"

