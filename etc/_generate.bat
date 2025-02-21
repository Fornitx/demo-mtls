cd etc

del *.cer
del *.p12

keytool -genkeypair -alias server -keyalg EC -keysize 256 -sigalg SHA256withECDSA -validity 365 -dname "CN=localhost,OU=Server,O=Examples,L=,S=CA,C=U" -keypass 123456 -keystore server.p12 -storeType PKCS12 -storepass 123456
keytool -genkeypair -alias client -keyalg EC -keysize 256 -sigalg SHA256withECDSA -validity 365 -dname "CN=localhost,OU=Server,O=Examples,L=,S=CA,C=U" -keypass 123456 -keystore client.p12 -storeType PKCS12 -storepass 123456

keytool -exportcert -alias client -file client.cer -keystore client.p12 -storepass 123456
keytool -exportcert -alias server -file server.cer -keystore server.p12 -storepass 123456

keytool -importcert -keystore client-truststore.p12 -alias server-public -file server.cer -storepass 123456 -noprompt
keytool -importcert -keystore server-truststore.p12 -alias client-public -file client.cer -storepass 123456 -noprompt

cd ..
