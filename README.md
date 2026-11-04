# FINT GraphQL

## Bygg og test

Prosjektet bruker Java **27** (Temurin), Spring Boot **4.1.2** og Gradle
**9.8.2**. Sett `JAVA_HOME` til en JDK 27-installasjon og bruk alltid wrapperen:

```sh
./gradlew clean build
```

Gradle kan også startes med JDK 21 eller nyere og laster da ned manglende
Java 27-toolchain via Foojay-resolveren i `settings.gradle`. Dette brukes av
GitHubs automatiske dependency submission, som starter med Java 21.
CI installerer nyeste tilgjengelige Temurin 27-oppdatering.
`javaVersion=27` i `gradle.properties` styrer toolchain for begge modulene.
CodeQL støtter foreløpig bygg til og med Java 26. CodeQL bruker derfor Ubuntu
24.04, Temurin 26 og `-PjavaVersion=26`; vanlige bygg og containere bruker Java 27.
Docker-bygget bruker digest-låste JDK- og JRE-bilder på Ubuntu 26.04 (Resolute).
Nyeste publiserte Temurin 27-containerbilder er foreløpig `27_35`; de kan ha
en eldre Java-oppdatering enn CI. Dependabot følger nye bildeversjoner og digester.

Testene bruker Spock 2.4, Groovy 5, Byte Buddy og lokale MockWebServer-servere.
De trenger ikke tilgang til FINT eller produksjonens identitetsleverandør.
Spring Boot styrer versjonene til Spring, GraphQL Java, Jackson, Reactor og
JUnit. Modellbibliotekene fra Novari er oppgradert til 4.1.0; modellheaderen
`x-fint-model-version` er fortsatt `V4`.

Spring for GraphQL erstatter GraphQL Kickstart. Jackson 3 brukes til JSON.
Den ubrukte transitive Jackson 2 databind-avhengigheten er ekskludert fra
Novari-modellene. Jackson-annotasjoner og Jackson 2 core beholdes fordi modellene
refererer til disse typene; Spring Boot styrer versjonene.
Spring Boot 4.1.2 styrer Jackson 3 (`3.1.7`), Jackson 2 (`2.21.7`), Tomcat
(`11.0.27`) og Lombok. Tidligere Jackson- og Tomcat-overstyringer er fjernet.
Byggepluginenes classpath har fortsatt en minimumsversjon av Commons Lang
(`3.20.0`), siden Boot-pluginens Commons Compress ellers trekker inn `3.16.0`.
`spring.threads.virtual.enabled=true` aktiverer virtuelle tråder for servlet-
forespørsler og Spring sine blokkerende GraphQL-metoder. WebClient og eksisterende
asynkrone resolvere beholder sin reaktive kjøring og tilkoblingsbegrensning.

API-et som applikasjonen henter data fra, er tregt, og spørringer kan omfatte
store datamengder. Brannmuren setter en øvre grense på 120 sekunder for brukerens
forespørsel. Derfor er `fint.graphql.query-timeout` satt til 110 sekunder:
spørringen får tid til å hente data, samtidig som applikasjonen har en margin
til å returnere et GraphQL-svar før brannmuren bryter forbindelsen.
Ved tidsavbrudd får JSON-klienter HTTP 200 med feilkoden `QUERY_TIMEOUT`.

`fint.graphql.async-request-timeout` setter Tomcat sin async-frist til 115 sekunder,
slik at GraphQL-fristen på 110 sekunder rekker å utløse og svaret kan skrives.
Tomcat-fristen settes eksplisitt for å unngå standardfristen på 30 sekunder.
Spring MVC sin separate frist er deaktivert. Ved egendefinerte verdier begrenses
GraphQL-fristen til før servlet-fristen med en margin på 10 prosent, maksimalt
ett sekund. Hvis begge verdiene er null eller negative, deaktiveres begge
fristene i applikasjonen; brannmurens grense på 120 sekunder gjelder fortsatt.

Autorisasjon, organisasjon, tillatte stier, tellere og oppslagsbuffer tilhører
GraphQL-spørringen og krever ikke servlet-requesten under asynkrone oppslag.
Når spørringen avsluttes eller avbrytes, kanselleres aktive og ventende
WebClient-oppslag, og nye oppslag for spørringen stoppes.

## Paginering av store relasjoner

Relasjoner hentes normalt med ett oppslag per lenke. En resolver ser bare lenkene til
sin egen overordnede ressurs, så alle lenker samles først opp på tvers av alle
ressurser på samme nivå i spørringen. Når et nivå ønsker flere ulike ressurser fra
samme samling enn `fint.graphql.relation-pagination.threshold` (standard 50), leses
samlingen i stedet side for side, med `fint.graphql.relation-pagination.page-size`
(standard 500) ressurser per side. Dette gjelder alle relasjoner, også de med én
verdi, og endrer ikke GraphQL-skjemaet.

| På ett nivå i spørringen | Ulike ressurser | Resultat |
|---|---|---|
| 2000 `elevforhold` → `elev` | 2000 | Én gjennomgang av samlingen |
| 2000 `elevforhold` → `skole` | 1 | Ett oppslag, deretter buffer |
| 30 `klasse` → 30 `klassemedlemskap` hver | 900 | Én gjennomgang av samlingen |
| 1 `elevforhold` → `elev` | 1 | Ett oppslag |

Klienten følger `_links.next` til det ikke finnes flere sider, eller til alle ønskede
ressurser er funnet.

- Ressurser kobles til relasjonen via selvlenkene sine. FINT lager én selvlenke per
  utfylt identifikator, så dette virker uavhengig av hvilken identifikator lenken
  bruker. Feltnavnet sammenlignes uten hensyn til store og små bokstaver, verdien
  med.
- Ressurser som ikke ble funnet, hentes enkeltvis som før. Det gjelder også resten
  hvis en side feiler. Feil på et enkeltoppslag gir samme resultat som før.
- Sider bruker samme token, organisasjon, stikontroll og samtidighetsbegrensning som
  andre oppslag, og `next`-lenker sendes alltid til `fint.endpoint.root`.
- Sider lagres ikke i oppslagsbufferen, så en gjennomgang holder høyst én side i
  minnet. Bare ønskede ressurser beholdes.
- Et nivå venter til alle resolvere på nivået har kjørt, så en treg overordnet
  ressurs forsinker hele nivået.
- Enkeltvis henting kan i praksis velges ved å sette terskelen svært høyt. Ugyldige
  verdier stopper oppstarten.

`spring.http.codecs.max-in-memory-size` (16 MB i `application.yml`) begrenser hvert
svar og må økes sammen med sidestørrelsen ved behov. `fint.webclient.response-timeout`
gjelder også for hver side.

## Modulene og kodegenerering

Rotprosjektet samler `app` (Spring Boot-applikasjonen) og `model-generator`
(internt Java-basert byggeverktøy). Generatoren publiseres ikke, har ingen
CLI-distribusjon og inngår ikke i applikasjonens runtime-classpath eller boot-JAR.
Den trenger verken Jackson, Go, Docker eller et utsjekket naborepo.

`./gradlew build` genererer Java-kilder og GraphQL-skjema automatisk før
kompilering og ressurskopiering. Bare generering kan kjøres med
`./gradlew :app:generateGraphql` eller `./generate.sh`.
Resultatet ligger i `app/build/generated/graphql/java` og
`app/build/generated/graphql/resources/schema`; ikke rediger disse filene.
Håndskrevet kode og tester ligger under `app/src`.

Modellen lastes ned fra `FINTLabs/fint-informasjonsmodell`, taggen
`v${apiVersion}`, og kontrolleres mot `modelSha256` i `gradle.properties`.
Den verifiserte råfilen lagres under `.gradle/fint-model/<versjon>/<sha256>/`.
Denne cachen overlever `clean`. Første bygg trenger GitHub-tilgang; senere
bygg kan kjøres med `./gradlew --offline clean build` når også Gradle-avhengighetene
er cachet. Sjekksummen kontrolleres ved hver bruk. Manglende offline-cache,
HTTP-feil eller feil sjekksum stopper bygget uten fallback til en annen modell.
Ved korrupt cache: fjern den berørte modellfilen og kjør på nytt med nettverk.

Genereringen omfatter administrasjon, utdanning og felles, med `Fravar` og
`Fravarstype` ekskludert. `PersonService` er håndskrevet og utelates fra generering;
persontjenesten har én implementasjon under `app/src/main/java`.
Den beholder ikke-nullverdier fra administrasjon, fyller manglende verdier fra
utdanning og kombinerer relasjonslenker uten duplikater uten å endre cachede
ressurser. En vellykket kilde kan brukes selv om den andre feiler.

Relasjonslister beholder rekkefølge, maksimalt åtte samtidige oppslag og
`null`-plasser ved manglende svar. Valgfrie tomme Feide-identifikatorer normaliseres
til `null`. Tomme GraphQL-objekttyper utelates, og `Date` og `Long` deklareres.

## Oppdatere modell og skjema

1. Oppdater `apiVersion` for både XMI-modellen og Novari-bibliotekene.
2. Hent `FINT-informasjonsmodell.xml` fra den tilsvarende versjonstaggen, kontroller
   opprinnelsen og sett `modelSha256` til SHA-256 av råfilen, før tegnsettkonvertering.
3. Kjør `./gradlew :app:generateGraphql`, gjennomgå forskjellene mot sist godkjente
   skjema og Java-kode, og oppdater referansedataene beskrevet i
   [generatorens testgrunnlag](model-generator/src/test/resources/baseline/README.md).
   Referanser skal ikke oppdateres automatisk for å gjøre en feilende test grønn.
4. Kjør `./gradlew clean build` og containerbygget. Hele testpakken må bestå før
   modellen tas i bruk. Endringer i offentlig skjema må gjennomgås eksplisitt.

Generatorens tester sammenligner alle filer med både den tidligere Go-generatoren
og applikasjonen ved migreringen, med en separat gjennomgått referanse for
URI-maler og rutekatalogen. Java sammenlignes som syntakstrær, og skjema som
GraphQL-syntakstrær; kommentarer og formatering påvirker ikke sammenligningen.
Gradle TestKit tester inkrementelle bygg, endret generator/modell, offline-cache,
sjekksumfeil og opprydding. Applikasjonstestene kjører mot de genererte kildene.

Jackson 3.1.5 brukes allerede og er verifisert med Novari 4.1.0. Jackson 2 databind
er fortsatt ekskludert. Jackson 2 core trengs av `TypeReference`-referanser i
Novari-bibliotekene; å fjerne denne krever oppdatering av bibliotekene, ikke av
GraphQL-generatoren. Delte Jackson-annotasjoner brukes også av Jackson 3.
Spring Boot styrer Jackson-versjonene.

## Metrikker for kall til FINT

`http.client.requests` bruker stabile URI-maler, for eksempel
`/administrasjon/fullmakt/rolle/navn/{value}`. Identifikatorer og query-parametre
inngår ikke i `uri`-etiketten. Den genererte katalogen inneholder 228 ruter,
inkludert samlinger og begge personkildene. Ukjente lenker samles under `/unknown`.
Endringer i `fint.endpoint.*` påvirker rutematching, men ikke de kanoniske etikettene.

`management.metrics.web.client.max-uri-tags` er satt til 256. En regresjonstest
kontrollerer at hele katalogen og reserverte etiketter får plass. Ved modellendring
må katalog og grense vurderes samlet; antall identifikatorverdier skal aldri
påvirke antall URI-etiketter. Cache og tilgangskontroll bruker fortsatt konkrete
ressursadresser.

Ved utrulling: tilpass dashboards som tidligere grupperte på `uri="none"`, og
kontroller i eksisterende overvåkning at antallet URI-etiketter er stabilt og at
loggen ikke viser advarsler om maksimalgrensen. Actuator-eksponeringen er uendret;
endringen åpner ikke nye metrikkeendepunkter.

## Oversikt over spørringer

Hver spørring gir én JSON-logglinje, `GraphQL query completed`, med feltene `queryUid`,
`durationMs`, `outcome` (`ok`, `partial`, `error`, `timeout`, `cancelled` eller `unknown`),
`requestCount`, `client`, `organisation`, `operation`, `queryHash` og `query`.

- `query` er spørringens form. Alle verdier, aliaser og kommentarer er fjernet med graphql-javas
  `AstSignature`, og teksten kuttes ved 10 000 tegn. Variabelverdier logges aldri.
- `queryHash` er lik for spørringer som bare skiller seg i verdier.
- `client` kommer fra tokenets `cn`-claim, eller `sub` hvis den mangler, ikke fra `x-client`-headeren.
- `durationMs` måles i GraphQL-laget og tar ikke med skriving av svaret til klienten.
- Spørringer som ikke kan parses, logges uten `operation`, `queryHash` og `query`.

`grafana/graphql-queries.json` er et Grafana-dashboard som leser disse linjene fra Loki. Grafen viser
én linje per spørring opp til 120 sekunder, med markering ved 110 sekunder (`fint.graphql.query-timeout`)
og 120 sekunder (gatewayen). Klikk på en linje for å se klient, varighet og spørring, eller på en
`queryHash` for å vise bare den spørringsformen. Sett `Log stream` til Lokis etiketter for tjenesten
ved import. Grafen viser de siste 5000 spørringene i tidsrommet, og historikken følger Lokis retention.

## Container

```sh
docker build -t fint-graphql:local .
docker run --rm -p 8080:8080 fint-graphql:local
```

Begge Temurin-bildene er låst til versjon og digest. Byggesteget bruker Gradle-
wrapperen og kjører testene; kjørebildet kjører som UID 10001. Oppdater både tag
og digest ved fremtidige bildeoppgraderinger.

## Hvordan publisere ny versjon

1. Følg instruksene i [## Oppdatere modell og skjema](#oppdatere-modell-og-skjema)
2. Commit og push endringer
3. Kjør CD action på github. Fyll inn nødvendige paramtere.
(NB: Dersom man sletter og gjennoppretter deployment for dette prosjektet (kubctl delete og kubectl apply), så blir servicen tildelt en ny IP-adresse, og kunden mister tilgang. Dersom det skjer må FLAIS kontaktes for å oppdatere til den nye IPen.)

## Teste lokalt

Start `Application` fra IDE-en eller kjør `./gradlew :app:bootRun`.
GraphQL er tilgjengelig på `POST /graphql`, og GraphiQL på `/graphiql`.
GraphiQL og GraphQL krever autentisering; send et gyldig bearer-token for den
konfigurerte utstederen. `fint.security.oauth2.issuer-uri` kan peke på en lokal
utsteder. Metadata hentes først når et token skal verifiseres.

`/schema.json` og `/actuator/health`, inkludert liveness/readiness, er offentlige.
Ved `server.servlet.context-path=/graphql` blir GraphQL-adressen
`/graphql/graphql`; GraphiQL beregner denne adressen automatisk. Ved egen
`management.server.port` ligger helseendepunktene på denne porten.

Eksemplene under må tilpasses feltene og identifikatorargumentene i gjeldende
skjema; rotfeltene returnerer enkeltressurser.

## Spørringer

Noen spørringer for å komme i gang:

### Fornavn på alle personer

```graphql
{
  person {
    navn {
      fornavn
    }
  }
}
```

### Alle ansatte med fornavn, ansattnummer og stillingstittel

```graphql
{
  personalressurs {
    person {
      navn {
        fornavn
      }
    }
    ansattnummer {
      identifikatorverdi
    }
    arbeidsforhold {
      stillingstittel
    }
  }
}
```

### Bursdagsoversikt med fornavn, fødselsdato og mobiltelefonnummer

```graphql
{
  person {
    navn {
      fornavn
    }
    fodselsdato
    kontaktinformasjon {
      mobiltelefonnummer
    }
  }
}
```

### Personell med navn på avdelingsledere

```graphql
{
  person {
    navn {
      etternavn
      fornavn
    }
    personalressurs {
      ansattnummer {
        identifikatorverdi
      }
      arbeidsforhold {
        stillingstittel
        arbeidssted {
          leder {
            person {
              navn {
                etternavn
                fornavn
              }
            }
          }
        }
      }
    }
  }
}
```

### Elever med navn, skoler og gruppemedlemskap

```graphql
{
  elev {
    person {
      navn {
        fornavn
      }
    }
    elevforhold {
      beskrivelse
      skole {
        navn
      }
      basisgruppe {
        navn
      }
      kontaktlarergruppe {
        navn
      }
      undervisningsgruppe {
        navn
      }
    }
  }
}
```
