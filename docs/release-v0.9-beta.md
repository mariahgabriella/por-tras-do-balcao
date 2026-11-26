# Verificação do APK — v0.9-beta

> Registro da verificação do APK de release, com a saída real das ferramentas.
> Não é teste de instalação (ver `docs/teste-instalacao.md`, com status "aguardando teste real").
> Não contém senha, alias nem caminho de keystore.

## Saída real

```text
$ apksigner verify --verbose app/release/ptdb.apk

Verifies
Verified using v1 scheme (JAR signing): false
Verified using v2 scheme (APK Signature Scheme v2): true
Verified using v3 scheme (APK Signature Scheme v3): false
Verified using v3.1 scheme (APK Signature Scheme v3.1): false
Verified using v3.2 scheme (APK Signature Scheme v3.2): false
Verified using v4 scheme (APK Signature Scheme v4): false
Verified for SourceStamp: false
Number of signers: 1

$ aapt2 dump badging app/release/ptdb.apk (trechos)
package: name='com.kaigang.balcao' versionCode='2' versionName='0.9-beta' platformBuildVersionName='15' platformBuildVersionCode='35' compileSdkVersion='35' compileSdkVersionCodename='15'
targetSdkVersion:'35'
application-label:'Por Trás do Balcão'
launchable-activity: name='com.example.portrasdobalcao.MainActivity'  label='Por Trás do Balcão' icon=''

$ sha256sum app/release/ptdb.apk
55e20ffd5f7214bd107503f4dbca5b7bde804fe9eeaa3ec4149d694299eeb55e  app/release/ptdb.apk
```
