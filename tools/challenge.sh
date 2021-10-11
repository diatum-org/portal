ACTION="$1"
RECORD="$2"
NAME="$3"
VALUE=\\\"$4\\\"

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

ROUTE=`/usr/bin/aws route53 change-resource-record-sets --hosted-zone-id $ZONE --region $REGION --output json --change-batch "{\"Changes\": [{\"Action\": \"$ACTION\",\"ResourceRecordSet\": {\"Name\": \"$NAME.$HOSTNAME\",\"Type\": \"TXT\",\"TTL\": 300,\"ResourceRecords\": [{ \"Value\": \"$VALUE\"}]}}]}"` || {
  logger "$DEVICE: change-resource-record-sets failed"
  debuglog "$DEVICE: change-resource-record-sets failed"
  exit 1;
}
CHANGE=`echo $ROUTE | jq -r .ChangeInfo.Id`
STATE=`echo $ROUTE | jq -r .ChangeInfo.Status`

# wait for DNS to sync
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
  logger "$DEVICE: failed to sync dns"
  debuglog "$DEVICE: failed to sync dns"
  exit 1;
fi

