set -e

rm -rf backup
mkdir -p backup
mysqldump -u root -proot portal > backup/portal.sql
rm -f backup.tgz
tar -czvf backup.tgz backup

