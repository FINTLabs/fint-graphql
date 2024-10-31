# FINT GraphQL

Rutine for å hente inn modell og publisere løsningen er omarbeidet i november 2024.

## Oppdatere modell og skjema

* Oppdater `version`-feltet i [gradle.properties](gradle.properties)
* Kjør `generate.sh` for å generere modell og skjema.
* Merge endringer / få løsningen til å bygge. Det er noen kjente avvik mellom det som blir generert av fint-graphql-cli og modellen som krever at man justerer casing på noen metoder (f.eks. getOtUngdom -> getOtungdom).

Om du vil gjøre en opprydding og generere hele modellen på nytt:
* Slett mappen `/src/main/resources/schema`
* Slett mappen `/src/main/java/no/fint/graphql/model`
* Følg instruksene i [## Oppdatere modell og skjema](#oppdatere-modell-og-skjema) for å generere modellen på nytt.

## Hvordan publisere ny versjon

1. Følg instruksene i [## Oppdatere modell og skjema](#oppdatere-modell-og-skjema)
2. Endre `MODEL_VERSION` og `LIB_VERSION` i GitHub Action-filene [.github/workflows/CD.yaml](.github/workflows/CD.yaml) og [.github/workflows/CI.yaml](.github/workflows/CI.yaml).
3. Commit og push endringene
4. Manuelt deploy fra `fint-infra-tools`

## Teste lokalt

Applikasjonen skal kunne testes lokalt uten noen konfigurasjon.  
Start `Application` fra IDE-en din og gå til http://localhost:8080/graphiql

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
