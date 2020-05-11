BASEDIR=$(dirname "$0")

for KEY in $(find ${BASEDIR}/crypto-wallet -type f -name "*_sk"); do
    KEY_DIR=$(dirname ${KEY})
    mv ${KEY} ${KEY_DIR}/key.pem
    chmod 644 ${KEY_DIR}/key.pem
done