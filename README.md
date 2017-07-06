#MICRO SERVICE AUTHORIZATION

This is a Spring Boot based Micro Service Authorization Micro Service.  This uses Java 8 and Gradle as the build system.  
In order to use this we do not need to install Gradle in our workspace.

This Micro Service has exposed 03 endpoints related to managing authorization.

    1. Request Token
    2. Validate Token
    3. Remove Token


**1. Request Token**

    This will create a new token if one does not exist.  If a token exists then it will return the existing token.
    The Micro Service, uses JWT for token creation.

    The parameters required to create a valid token are:
    
        1. Username - The name of the User. Example: eddygrant@example.com
        2. User's Salt - The secret that will be used to sign the token
        3. User's Authorities - A Firm Short Name wise map containing a list of authorities the user can perform for a given firm.
        
    This endpoint will return a HS512 Algorythm based JWT Token.
    
    An example of a valid request is as follows:
    
        curl -H "Content-Type: application/json" 
             -X POST 
             -d '{"username": "sshanmugalingm@bglcorp.com.au", 
                  "salt": "3432-23423023423-23423",
                  "authorities": {
                                   "sf360test" : ["AUTH_CREATE_FUNDS", "AUTH_VIEW_FUNDS"],
                                   "bgl001" : ["AUTH_CREATE_DOCUMENTS"]
                                 }
                  }' 
             http://<url>/token/requestToken

**2. Validate Token**

    This will validate the provided token to see if the token is authenticated and then authorize if a user can access a particular resource.
    
    The parameters required to validate are:
    
        1. The JWT Token String
        2. The Firm Short Name
        3. A List of Authorities to validate against
        
     This will return 404, if the token cannot be found or cannot be validated.  This will return true if the Token and Authorization are success or return false otherwise.
 
     An example of a valid request is as follows:
     
         curl -H "Content-Type: application/json" 
              -X POST 
              -d '{"token" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwic3NoYW5tdWdhbGluZ21AYmdsY29ycC5jb20uYXVcIixcImF1dGhvcml0aWVzXCI6e1wic2YzNjB0ZXN0XCI6W1wiQVVUSF9DUkVBVEVfRlVORFNcIixcIkFVVEhfVklFV19GVU5EU1wiXSxcImJnbDAwMVwiOltcIkFVVEhfQ1JFQVRFX0RPQ1VNRU5UU1wiXX19In0.fR_JofKTa-n4TUEYwIbJQ6dwfe1WoEjmJqZAYLqjZN3ka4bM9dW6UnWPkfyT1wouX3y_Cta6vtU3cyHQ0Q1rRA",
                   "firmShortName" : "sf360test",
                   "authorities" : ["AUTH_CREATE_FUNDS"]
                  }' 
              http://<url>/token/validateToken
 
**3. Remove Token**
 
     This will remove a provided token from it's token store.
     
     The parameter required is the token.
     
     This will return true, if the token is removed successfully.  This will return 404 if the token cannot be found or if the token is invalid.
     
     An example of a valid request is as follows:
          
              curl -H "Content-Type: application/json" 
                   -X POST 
                   -d '{"token" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJ1c2VybmFtZVwiOlwic3NoYW5tdWdhbGluZ21AYmdsY29ycC5jb20uYXVcIixcImF1dGhvcml0aWVzXCI6e1wic2YzNjB0ZXN0XCI6W1wiQVVUSF9DUkVBVEVfRlVORFNcIixcIkFVVEhfVklFV19GVU5EU1wiXSxcImJnbDAwMVwiOltcIkFVVEhfQ1JFQVRFX0RPQ1VNRU5UU1wiXX19In0.fR_JofKTa-n4TUEYwIbJQ6dwfe1WoEjmJqZAYLqjZN3ka4bM9dW6UnWPkfyT1wouX3y_Cta6vtU3cyHQ0Q1rRA"}' 
                   http://<url>/token/removeToken
     
When any of the above endpoints are called a Basic Authorization Header needs to be added in the http call. An example is as follows:

    curl -X POST 
         http://<url>/token/requestToken 
         --header Basic REVWRUxPUE1FTlQ6MDM2ODUzMmYtNGZkYi00ZjkzLWE0NGYtNTdkNThlMmVlNWVm
