# Application Management

This API is used for managing list of applications with content, name and state.

## State flow for each application:
- create application - application must have not empty name and content
- delete application - application which is created can be deleted but with not empty reason only - end of flow
- verify application - verify created application if we want to go further
- reject application - application which is verified can be rejected but with not empty reason only - end of flow
- accept application - accept verified application if we want to go further 
- reject application - application which is accepted can be rejected but with not empty reason only - end of flow
- publish application - publish accepted application if we want to go further - end of flow
    
Each action from the flow described above has separate endpoint.
## List of state flow action endpoints

- create - POST http://localhost:8080/create

        INPUT BODY
        { 
            "content": "application content - cannot be empty",
            "name": "application name - cannot be empty"
        }
        
        OUTPUT BODY
        {
            "id": "Application database identifier",
            "state": "CREATED",
            "name": "application name",
            "content": "application content",
            "publishedId": null
        }

- delete - PUT http://localhost:8080/delete

        INPUT BODY
        {
            "applicationId": "Application database identifier",
            "reason": "deletion reason - cannot be empty"
        }
        
        OUTPUT BODY
        {
            "id": "Application database identifier",
            "state": "DELETED",
            "name": "application name",
            "content": "application content",
            "publishedId": null
        }

- verify - PUT http://localhost:8080/verify

        REQUEST PARAM
        id=appliationDatabaseIdentifier
        
        OUTPUT BODY
        {
            "id": "Application database identifier",
            "state": "VERIFIED",
            "name": "application name",
            "content": "application content",
            "publishedId": null
        }

- reject - PUT http://localhost:8080/reject

        INPUT BODY
        {
            "applicationId": "Application database identifier",
            "reason": "rejection reason - cannot be empty"
        }
        
        OUTPUT BODY
        {
            "id": "Application database identifier",
            "state": "REJECTED",
            "name": "application name",
            "content": "application content",
            "publishedId": null
        }

- accept - PUT http://localhost:8080/accept

        REQUEST PARAM
        id=appliationDatabaseIdentifier
        
        OUTPUT BODY
        {
            "id": "Application database identifier",
            "state": "ACCEPTED",
            "name": "application name",
            "content": "application content",
            "publishedId": null
        }

- publish - PUT http://localhost:8080/publish

        REQUEST PARAM
        id=appliationDatabaseIdentifier
        
        OUTPUT BODY
        {
            "id": "Application database identifier",
            "state": "PUBLISHED",
            "name": "application name",
            "content": "application content",
            "publishedId": "Unique number identifier of published application"
        }

## Other endpoints to get information about applications
There are another endpoints not connected with state flow but prepared for fetching data about application in database.

- get all applications - GET http://localhost:8080
        
        INPUT PARAMS
        - offset=pageNumber - pagination param 
            - not required, 
            - default 0
        - limit=pageSize - pagination param 
            - not required, 
            - default 10
        - sortBy=sortProperty - sorting param 
            - not required, 
            - default id,
            - possible values: id, name, content, publishedId
        - direction=sortDirection - sorting param 
            - not required, 
            - default asc, 
            = possible values: asc, desc
        - name=applicationName - filter param 
            - not required, 
            - if set results contains applicaitons only with requested name
        - state=applicationState - filter param 
            - not required, 
            - if set results contains applicaitons only with requested state
            - possible values: CREATED, VERIFIED, DELETED, REJECTED, ACCEPTED, PUBLISHED

        OUTPUT BODY
        [
            {
            "id": "Application database id",
            "state": "ACCEPTED",
            "name": "application name",
            "content": "application content",
            "publishedId": null
            }, ...
        ]

- get application with state changes history - GET http://localhost:8080/{id}
       
        INPUT PATH VARIABLE
        /{id} - application database identifier

        OUTPUT BODY
        {
            "id": "Application database id",
            "state": "ACCEPTED",
            "name": "application name",
            "content": "application content",
            "publishedId": null,
            "changesHistory": [
                {
                    "applicationId": "Application database id",
                    "modificationDate": "Date of modification",
                    "state": "Application state after modification",
                    "reason": "Modification reason - not empty in case of rejection or deletion"
                }, ...
            ]
        }

## Error handling
In case of any error request will return
bad request code (400) and body like

        {
            "errorInfo": "description of error"
        }