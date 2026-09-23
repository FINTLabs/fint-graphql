# FINT GraphQL

## Bygg og test

Prosjektet bruker Java **26** (Temurin 26.0.2+10), Spring Boot **4.1.1** og Gradle
**9.7.1**. Sett `JAVA_HOME` til en JDK 26-installasjon og bruk alltid wrapperen:

```sh
./gradlew clean build
```

Testene bruker Spock 2.4, Groovy 5, Byte Buddy og lokale MockWebServer-servere.
De trenger ikke tilgang til FINT eller produksjonens identitetsleverandør.
Spring Boot styrer versjonene til Spring, GraphQL Java, Jackson, Reactor og
JUnit. Modellbibliotekene fra Novari er oppgradert til 4.1.0; modellheaderen
`x-fint-model-version` er fortsatt `V4`.

Spring for GraphQL erstatter GraphQL Kickstart. Jackson 3 brukes til JSON.
Den ubrukte transitive Jackson 2 databind-avhengigheten er ekskludert fra
Novari-modellene. Jackson-annotasjoner og Jackson 2 core beholdes fordi modellene
refererer til disse typene; Spring Boot styrer versjonene.
`spring.threads.virtual.enabled=true` aktiverer virtuelle tråder for servlet-
forespørsler og Spring sine blokkerende GraphQL-metoder. WebClient og eksisterende
asynkrone resolvere beholder sin reaktive kjøring og tilkoblingsbegrensning.

`fint.graphql.query-timeout` og `fint.graphql.async-request-timeout` beholdes.
Den minste positive verdien brukes som frist; hvis begge er null eller negative,
settes ingen GraphQL-frist. Tidsavbrudd gir fortsatt HTTP 200 med feilkoden
`QUERY_TIMEOUT` for JSON-klienter. Spring MVC sin separate frist er deaktivert
slik at den ikke konkurrerer med GraphQL-svaret.

## Oppdatere modell og skjema

1. Oppdater `apiVersion` i `gradle.properties` for Java-modellbibliotekene.
2. Kjør `./generate.sh` med Docker/Podman og Python 3.9 eller nyere tilgjengelig.
3. Generator 1.3.1 skriver kandidater til en midlertidig mappe. Skriptet
   `scripts/migrate-graphql-mappings.py` konverterer til eksplisitte Spring
   GraphQL-annotasjoner. Konverteringen kan kjøres flere ganger uten nye endringer.
   Deretter kopieres `PersonService.txt` over generert `model/person/PersonService.java`,
   slik at sammenslåing fra administrasjon og utdanning beholdes også i kandidatene.
4. Sammenlign og flett kandidatene inn i `src/main/resources/schema` og
   `src/main/java/no/fint/graphql/model`. Generatoren bruker taggen `v${apiVersion}`
   fra `gradle.properties`; generatorbildet er også låst til digest.
5. Behold tilpasningene i `PersonService`, relasjonsresolverne og det offentlige
   skjemaet. Generering overskriver aldri disse filene automatisk. Kjør hele
   testpakken etter fletting.

`PersonService.txt` og `src/main/java/no/fint/graphql/model/model/person/PersonService.java`
skal oppdateres sammen; en test kontrollerer at innholdet er identisk. Personoppslag
beholder ikke-nullverdier fra administrasjon, fyller manglende verdier fra utdanning
og kombinerer relasjonslenker uten duplikater mellom kildene. En vellykket kilde
kan brukes selv om den andre ikke er tilgjengelig eller tillatt. Sammenslåingen
endrer ikke de opprinnelige ressursene i forespørselscachen.

De to tomme, ubrukte generatortypene `Grepreferanse` og `Vigoreferanse` fjernes
under konverteringen fordi GraphQL Java krever felt på objekttyper.

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

Start `Application` fra IDE-en eller kjør `./gradlew bootRun`.
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
