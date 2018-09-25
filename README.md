# FINT graphql

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