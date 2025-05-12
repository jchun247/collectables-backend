# Collectables API

A Spring Boot application for managing trading card collections with a focus on Pokémon cards.

## Features

- Card database with detailed information including prices, sets, and Pokémon-specific details
- Collection management system with support for different collection types:
    - Collection lists (tracking owned cards)
    - Portfolios (tracking value history over time)
- User authentication and authorization via Auth0
- RESTful API with comprehensive endpoints

## API Endpoints

### Cards

```
GET /api/cards - Access card information with filtering and pagination (publicly available)
  Query parameters:
  - rarity: Filter by card rarity
  - condition: Filter by card condition
  - sortOption: Option to sort results by price or name
  - maxPrice: Maximum price filter
  - minPrice: Minimum price filter
  - games: Filter by related games
  - searchQuery: Text search for cards
  - page: Page number for pagination
  - setId: Filter by specific card set
```

### Card Sets

```
GET /api/sets/series - Access card set series information

GET /api/sets/by-series - Access card sets filtered by series (publicly available)
  Query parameters:
    - series: Filter sets by series name
```

### Collections

```
POST /api/collections/lists - Create a new collection list
POST /api/collections/portfolios - Create a new portfolio
DELETE /api/collections/{collectionId} - Delete a collection
POST /api/collections/{collectionId}/cards - Add a card to a collection
DELETE /api/collections/{collectionId}/cards/{collectionCardId} - Remove cards from a collection
GET /api/collections/{collectionId} - Get collection details
GET /api/collections/{collectionId}/cards - Get paginated cards in a collection
GET /api/collections/{collectionId}/value-history - Get portfolio value history
GET /api/collections/users/{auth0UserId} - Get collections for a specific user
```

### Users

```
POST /api/users/provision - Create a new user
```

## Technologies

- Java 17+
- Spring Boot
- Spring Security with JWT authentication
- MapStruct
- Maven
- Lombok
- PostgreSQL

## Security

- OAuth 2.0 resource server with JWT validation
- CORS configuration for frontend access
- Public endpoints for card data retrieval
- Protected endpoints for user-specific operations

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven
- A configured Auth0 account for authentication

### Configuration

Set the following properties in your `application.properties` or environment variables:

```
cors.allowed-origins=<your-frontend-url>
```

Additional application configuration may be required for database connection and Auth0 settings.

### License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE.md) file for details.
