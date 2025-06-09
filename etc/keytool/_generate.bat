for %%x in (*.p12) do del %%x

:: Generate a client and server EC key pair
keytool -genkeypair -alias server -keyalg EC -groupname secp256r1 -sigalg SHA256withECDSA -validity 3650 -dname "CN=Server" -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keypass 123456 -keystore server-keystore.p12 -storeType PKCS12 -storepass 123456
keytool -genkeypair -alias client -keyalg EC -groupname secp256r1 -sigalg SHA256withECDSA -validity 3650 -dname "CN=Client" -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keypass 123456 -keystore client-keystore.p12 -storeType PKCS12 -storepass 123456

:: Export public certificates for both the client and server
keytool -exportcert -alias client -file client-public.cer -keystore client-keystore.p12 -storepass 123456
keytool -exportcert -alias server -file server-public.cer -keystore server-keystore.p12 -storepass 123456

:: Import the client and server public certificates into each others keystore
keytool -importcert -keystore server-truststore.p12 -alias client-public -file client-public.cer -storepass 123456 -noprompt
keytool -importcert -keystore client-truststore.p12 -alias server-public -file server-public.cer -storepass 123456 -noprompt

for %%x in (*.cer) do del %%x
