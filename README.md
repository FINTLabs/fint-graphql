# FINT graphql

## When you're releasing a new version remember these two things

1. Update `version` in [gradle.properties](gradle.properties)
2. Update `MODEL_VERSION` and `LIB_VERSION` in the GitHub Action file [.github/workflows/cicd.yaml](.github/workflows/cicd.yaml).
3. Push play and 🤞


## Getting started

The application works out of the box.  Launch `Application` from within your IDE and go to http://localhost:8080/graphiql

## Queries

Some queries to get you going:

### First name of all Persons

```graphql
{
  person {
    navn {
      fornavn
    }
  }
}
```

### All employees with first name, employment number, and position title

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

### Birthday directory, with first names, birthdays, and mobile phone numbers

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

### Personnel, with name of department managers

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

### Students with names, schools, and group memberships

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