## getUser
#### request
```
http://{interface}:{port}/api/user/{userId}
```
example
```
http://localhost:8081/api/user/2
```
#### response
```
{
    "id": 2,
    "firstname": "TestFN_1",
    "lastname": "TestLN_1",
    "login": "TestLogin_1",
    "email": "TestEMAIL_1",
    "gender": true,
    "description": null,
    "isactive": true,
    "createdat": "2017-12-28 13:29:34.802",
    "updatedat": null,
    "deletedat": null,
    "isdeleted": false
}
```

## getMessage
#### request
```
http://{interface}:{port}/api/message/{messageId}
```
example
```
http://localhost:8081/api/message/3
```
#### response
```
{
    "id": 3,
    "text": "Test_text_1",
    "userid": 2,
    "createdat": "2017-12-28 13:29:34.837",
    "updatedat": null,
    "deletedat": null,
    "isdeleted": false
}
```

## getMessages

#### request
```
http://{interface}:{port}/api/messages/{userId}
```
example
```
http://localhost:8081/api/message/3
```
#### response
```
[
    {
        "id": 3,
        "text": "Test_text_1",
        "userid": 2,
        "createdat": "2017-12-28 13:29:34.837",
        "updatedat": null,
        "deletedat": null,
        "isdeleted": false
    },
    {
        "id": 4,
        "text": "Test_text_2",
        "userid": 2,
        "createdat": "2017-12-28 13:29:34.837",
        "updatedat": null,
        "deletedat": null,
        "isdeleted": false
    }
]
```