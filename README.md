MICRO SERVICE AUTHORIZATION

This is a Spring Boot based Micro Service Authorization Micro Service.  This uses Java 8 and Gradle as the build system.  
In order to use this we do not need to install Gradle in our workspace.

This Micro Service has exposed 03 endpoints related to managing authorization.

1. Request Token
2. Validate Token
3. Remove Token

1. Request Token

This will create a new token if one does not exist.  If a token exists then it will return the existing token.
The Micro Service, uses JWT for token creation.

The parameters required to create a valid token are:
    1. Username - The name of the User. Example: eddygrant@example.com
    2. User's Salt - The secret that will be used to sign the token
    3. User's Authorities - A Firm Short Name wise map containing a list of authorities the user can perform for a given firm.
    
This endpoint will return a HS512 Algorythm based JWT Token.

2. Validate Token

This will validate the provided token to see if the token is authenticated and then authorize if a user can access a particular resource.

The parameters required to validate are:
    1. The JWT Token String
    2. The Firm Short Name
    3. A List of Authorities to validate against
    
 This will return 404, if the token cannot be found or cannot be validated.  This will return true if the Token and Authorization are success or return false otherwise.
 
 3. Remove Token
 
 This will remove a provided token from it's token store.
 
 The parameter required is the token.
 
 This will return true, if the token is removed successfully.  This will return 404 if the token cannot be found or if the token is invalid.